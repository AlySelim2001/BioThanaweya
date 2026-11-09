package com.example.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.CellBiologyDataProvider
import com.example.domain.model.*
import kotlin.math.*

private data class ProjectedOrganelle(
    val instance: Organelle3DInstance,
    val screenX: Float,
    val screenY: Float,
    val transformedZ: Float,
    val projectedRadius: Float,
    val alpha: Float,
    val isSelected: Boolean
)

@Composable
fun Cell3DViewport(
    cellType: CellType,
    selectedOrganelle: OrganelleType?,
    simulationMode: SimulationViewMode,
    isCutawayEnabled: Boolean,
    onOrganelleSelected: (OrganelleType) -> Unit,
    modifier: Modifier = Modifier
) {
    var rotX by remember { mutableFloatStateOf(0.35f) } // Pitch
    var rotY by remember { mutableFloatStateOf(0.45f) } // Yaw
    var zoomLevel by remember { mutableFloatStateOf(1.05f) }
    var isAutoRotate by remember { mutableStateOf(true) }

    // Auto-rotation when enabled
    val infiniteTransition = rememberInfiniteTransition(label = "cell_infinite")
    val autoRotDelta by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auto_rot"
    )

    // Pulse animation for selected organelle highlight
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "selection_pulse"
    )

    // Secretory vesicle movement flow animation (0f..1f)
    val secretoryProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "secretory_flow"
    )

    // Effective yaw angle combining manual and auto-rotation
    val effectiveRotY = if (isAutoRotate) (rotY + autoRotDelta) % (2f * Math.PI.toFloat()) else rotY

    val instances = remember(cellType) {
        CellBiologyDataProvider.get3DInstances(cellType)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(380.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF132238),
                        Color(0xFF09121E),
                        Color(0xFF050B12)
                    )
                )
            )
            .border(1.dp, Color(0xFF00ACC1).copy(alpha = 0.35f), RoundedCornerShape(24.dp))
            .testTag("cell_3d_viewport_container")
    ) {
        // Direct Canvas rendering with 3D projection & Painter's algorithm
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(cellType, isAutoRotate) {
                    detectDragGestures(
                        onDragStart = { isAutoRotate = false },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            rotY += dragAmount.x * 0.008f
                            rotX = (rotX + dragAmount.y * 0.008f).coerceIn(-1.1f, 1.1f)
                        }
                    )
                }
                .pointerInput(instances, effectiveRotY, rotX, zoomLevel) {
                    detectTapGestures { tapOffset ->
                        val centerX = size.width / 2f
                        val centerY = size.height / 2f
                        val fov = 350f * zoomLevel

                        var closest: OrganelleType? = null
                        var minDistance = Float.MAX_VALUE

                        instances.forEach { instance ->
                            val p = instance.localPosition
                            val r1 = p.rotateY(effectiveRotY)
                            val r2 = r1.rotateX(rotX)
                            val persp = fov / (fov + r2.z + 240f)
                            val sx = centerX + r2.x * persp * zoomLevel
                            val sy = centerY + r2.y * persp * zoomLevel
                            val hitRadius = (instance.baseRadius * persp * zoomLevel).coerceAtLeast(36f)

                            val dist = hypot(tapOffset.x - sx, tapOffset.y - sy)
                            if (dist <= hitRadius && dist < minDistance) {
                                minDistance = dist
                                closest = instance.type
                            }
                        }

                        if (closest != null) {
                            onOrganelleSelected(closest!!)
                        }
                    }
                }
                .testTag("cell_3d_canvas")
        ) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val fov = 350f * zoomLevel
            val cellRadius = 135f * zoomLevel

            // 1. Draw subtle background coordinate & cytoplasmic matrix grid
            drawCytoplasmicGrid(centerX, centerY, cellRadius, effectiveRotY)

            // 2. Project all organelles to camera space and sort by depth (Z)
            val projectedList = instances.map { inst ->
                val p = inst.localPosition
                val r1 = p.rotateY(effectiveRotY)
                val r2 = r1.rotateX(rotX)
                val persp = fov / (fov + r2.z + 240f)
                val sx = centerX + r2.x * persp * zoomLevel
                val sy = centerY + r2.y * persp * zoomLevel
                val pr = inst.baseRadius * persp * zoomLevel
                val alpha = ((r2.z + 180f) / 360f).coerceIn(0.45f, 1.0f)

                ProjectedOrganelle(
                    instance = inst,
                    screenX = sx,
                    screenY = sy,
                    transformedZ = r2.z,
                    projectedRadius = pr,
                    alpha = alpha,
                    isSelected = (inst.type == selectedOrganelle)
                )
            }.sortedBy { it.transformedZ } // Painter's Algorithm: render farthest first

            // 3. Draw outer cell membrane back-hemisphere
            drawCellMembraneBack(
                centerX = centerX,
                centerY = centerY,
                radius = cellRadius,
                rotY = effectiveRotY,
                rotX = rotX,
                cellType = cellType,
                isCutaway = isCutawayEnabled
            )

            // 4. Render organelles sorted by Z
            projectedList.forEach { proj ->
                drawOrganelle(
                    proj = proj,
                    simulationMode = simulationMode,
                    secretoryProgress = secretoryProgress,
                    pulseScale = pulseScale,
                    cellType = cellType
                )
            }

            // 5. If Secretory Pathway mode is active, draw animated vesicle trajectory
            if (simulationMode == SimulationViewMode.SECRETORY_PATHWAY) {
                drawSecretoryPathwayVesicles(
                    centerX = centerX,
                    centerY = centerY,
                    rotY = effectiveRotY,
                    rotX = rotX,
                    zoomLevel = zoomLevel,
                    progress = secretoryProgress
                )
            }

            // 6. Draw front cutaway cell membrane rim & bilayer cross-section
            drawCellMembraneFront(
                centerX = centerX,
                centerY = centerY,
                radius = cellRadius,
                rotY = effectiveRotY,
                rotX = rotX,
                cellType = cellType,
                isCutaway = isCutawayEnabled
            )

            // 7. Draw highlight halo and label pin for selected organelle
            val selectedProj = projectedList.find { it.isSelected }
            if (selectedProj != null) {
                drawSelectionPin(selectedProj, pulseScale)
            }
        }

        // Overlay Viewport Controls (Top Right & Bottom Row)
        ViewportOverlayControls(
            cellType = cellType,
            isAutoRotate = isAutoRotate,
            isCutawayEnabled = isCutawayEnabled,
            zoomLevel = zoomLevel,
            onToggleAutoRotate = { isAutoRotate = !isAutoRotate },
            onResetCamera = {
                rotX = 0.35f
                rotY = 0.45f
                zoomLevel = 1.05f
            },
            onZoomIn = { zoomLevel = (zoomLevel + 0.15f).coerceAtMost(2.0f) },
            onZoomOut = { zoomLevel = (zoomLevel - 0.15f).coerceAtLeast(0.65f) }
        )

        // Selected Organelle Quick Callout Header
        if (selectedOrganelle != null) {
            val detail = CellBiologyDataProvider.organelleDetails[selectedOrganelle]
            if (detail != null) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .testTag("selected_organelle_badge"),
                    color = Color(0xFF09121E).copy(alpha = 0.88f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(detail.type.primaryColorHex))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(detail.type.primaryColorHex))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${detail.arabicName} • ${detail.englishName}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Touch Hint / 3D Orbit Indicator
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.TouchApp,
                contentDescription = null,
                tint = Color(0xFF00ACC1).copy(alpha = 0.75f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "اسحب للتدوير 3D • اضغط للفحص",
                fontSize = 11.sp,
                color = Color(0xFFB0BEC5)
            )
        }
    }
}

@Composable
private fun BoxScope.ViewportOverlayControls(
    cellType: CellType,
    isAutoRotate: Boolean,
    isCutawayEnabled: Boolean,
    zoomLevel: Float,
    onToggleAutoRotate: () -> Unit,
    onResetCamera: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit
) {
    // Top right control icons
    Column(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        IconButton(
            onClick = onToggleAutoRotate,
            modifier = Modifier
                .size(34.dp)
                .background(
                    if (isAutoRotate) Color(0xFF00897B) else Color(0xFF1E293B).copy(alpha = 0.85f),
                    CircleShape
                )
                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                .testTag("viewport_auto_rotate_btn")
        ) {
            Icon(
                imageVector = if (isAutoRotate) Icons.Default.Sync else Icons.Default.SyncDisabled,
                contentDescription = "Auto Rotate",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        IconButton(
            onClick = onZoomIn,
            modifier = Modifier
                .size(34.dp)
                .background(Color(0xFF1E293B).copy(alpha = 0.85f), CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                .testTag("viewport_zoom_in_btn")
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Zoom In",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        IconButton(
            onClick = onZoomOut,
            modifier = Modifier
                .size(34.dp)
                .background(Color(0xFF1E293B).copy(alpha = 0.85f), CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                .testTag("viewport_zoom_out_btn")
        ) {
            Icon(
                Icons.Default.Remove,
                contentDescription = "Zoom Out",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        IconButton(
            onClick = onResetCamera,
            modifier = Modifier
                .size(34.dp)
                .background(Color(0xFF1E293B).copy(alpha = 0.85f), CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                .testTag("viewport_reset_cam_btn")
        ) {
            Icon(
                Icons.Default.CenterFocusStrong,
                contentDescription = "Reset Camera",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ----------------- DRAW SCOPE HELPERS (CANVAS RENDERING) -----------------

private fun DrawScope.drawCytoplasmicGrid(
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotY: Float
) {
    // Subtle ambient cytoplasmic glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF00ACC1).copy(alpha = 0.08f),
                Color(0xFF00897B).copy(alpha = 0.03f),
                Color.Transparent
            ),
            center = Offset(centerX, centerY),
            radius = radius * 1.25f
        ),
        radius = radius * 1.25f,
        center = Offset(centerX, centerY)
    )

    // Orbital latitude rings representing 3D sphere volume
    val step = radius * 0.32f
    for (i in -2..2) {
        val yOffset = i * step * 0.7f
        val r = sqrt((radius * radius - yOffset * yOffset).coerceAtLeast(0f))
        drawOval(
            color = Color(0xFF37474F).copy(alpha = 0.18f),
            topLeft = Offset(centerX - r, centerY + yOffset - (r * 0.22f)),
            size = Size(r * 2f, r * 0.44f),
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

private fun DrawScope.drawCellMembraneBack(
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotY: Float,
    rotX: Float,
    cellType: CellType,
    isCutaway: Boolean
) {
    val membraneColor = if (cellType == CellType.PLANT_CELL) {
        Color(0xFF2E7D32) // Cellulose wall + membrane green
    } else {
        Color(0xFF00897B) // Animal lipid bilayer cyan/teal
    }

    // Outer membrane volume back hemisphere
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                membraneColor.copy(alpha = 0.04f),
                membraneColor.copy(alpha = 0.12f),
                membraneColor.copy(alpha = 0.28f)
            ),
            center = Offset(centerX - radius * 0.2f, centerY - radius * 0.2f),
            radius = radius
        ),
        radius = radius,
        center = Offset(centerX, centerY)
    )

    if (cellType == CellType.PLANT_CELL) {
        // Hexagonal rigid cell wall outline
        drawRoundRect(
            color = Color(0xFF4CAF50).copy(alpha = 0.35f),
            topLeft = Offset(centerX - radius * 1.05f, centerY - radius * 1.05f),
            size = Size(radius * 2.1f, radius * 2.1f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(30.dp.toPx()),
            style = Stroke(width = 4.dp.toPx())
        )
    }
}

private fun DrawScope.drawCellMembraneFront(
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotY: Float,
    rotX: Float,
    cellType: CellType,
    isCutaway: Boolean
) {
    val rimColor = if (cellType == CellType.PLANT_CELL) Color(0xFF81C784) else Color(0xFF80DEEA)

    if (isCutaway) {
        // 3D Cutaway Slice: Draw translucent cut section showing thickness of lipid bilayer
        val path = Path().apply {
            moveTo(centerX, centerY)
            lineTo(centerX + radius * 0.95f, centerY - radius * 0.2f)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    centerX - radius,
                    centerY - radius,
                    centerX + radius,
                    centerY + radius
                ),
                startAngleDegrees = -12f,
                sweepAngleDegrees = 110f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(
            path = path,
            brush = Brush.linearGradient(
                colors = listOf(
                    rimColor.copy(alpha = 0.35f),
                    rimColor.copy(alpha = 0.08f)
                ),
                start = Offset(centerX, centerY),
                end = Offset(centerX + radius, centerY + radius)
            )
        )
        drawPath(
            path = path,
            color = rimColor.copy(alpha = 0.65f),
            style = Stroke(width = 2.dp.toPx())
        )
    }

    // Outer Fresnel Rim Glow (Glowing edge of 3D sphere)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.Transparent,
                rimColor.copy(alpha = 0.15f),
                rimColor.copy(alpha = 0.55f)
            ),
            center = Offset(centerX, centerY),
            radius = radius
        ),
        radius = radius,
        center = Offset(centerX, centerY),
        style = Stroke(width = 3.dp.toPx())
    )
}

private fun DrawScope.drawOrganelle(
    proj: ProjectedOrganelle,
    simulationMode: SimulationViewMode,
    secretoryProgress: Float,
    pulseScale: Float,
    cellType: CellType
) {
    val center = Offset(proj.screenX, proj.screenY)
    val r = proj.projectedRadius
    val baseColor = Color(proj.instance.type.primaryColorHex)

    when (proj.instance.type) {
        OrganelleType.NUCLEUS -> {
            // Spherical double membrane with nuclear pores and chromatin
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF7E57C2).copy(alpha = proj.alpha),
                        Color(0xFF4527A0).copy(alpha = proj.alpha * 0.95f),
                        Color(0xFF311B92).copy(alpha = proj.alpha)
                    ),
                    center = Offset(center.x - r * 0.3f, center.y - r * 0.3f),
                    radius = r
                ),
                radius = r,
                center = center
            )
            // Nuclear pores dots
            val poreCount = 8
            for (i in 0 until poreCount) {
                val angle = (i * (2 * Math.PI / poreCount)).toFloat()
                val px = center.x + (r * 0.88f) * cos(angle)
                val py = center.y + (r * 0.88f) * sin(angle)
                drawCircle(
                    color = Color(0xFFD1C4E9).copy(alpha = proj.alpha * 0.8f),
                    radius = 2.5.dp.toPx(),
                    center = Offset(px, py)
                )
            }
            // Double membrane boundary
            drawCircle(
                color = Color(0xFFB39DDB).copy(alpha = proj.alpha),
                radius = r,
                center = center,
                style = Stroke(width = 2.5.dp.toPx())
            )
        }

        OrganelleType.NUCLEOLUS -> {
            // Dense inner RNA core
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE040FB).copy(alpha = proj.alpha),
                        Color(0xFF8E24AA).copy(alpha = proj.alpha)
                    ),
                    center = Offset(center.x - r * 0.2f, center.y - r * 0.2f),
                    radius = r
                ),
                radius = r,
                center = center
            )
        }

        OrganelleType.MITOCHONDRIA -> {
            // Elongated sausage shape with cristae folds
            val ovalWidth = r * 2.2f
            val ovalHeight = r * 1.2f
            val rect = androidx.compose.ui.geometry.Rect(
                center.x - ovalWidth / 2f,
                center.y - ovalHeight / 2f,
                center.x + ovalWidth / 2f,
                center.y + ovalHeight / 2f
            )

            // Outer capsule
            drawOval(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFF7043).copy(alpha = proj.alpha),
                        Color(0xFFD84315).copy(alpha = proj.alpha)
                    ),
                    start = Offset(rect.left, rect.top),
                    end = Offset(rect.right, rect.bottom)
                ),
                topLeft = Offset(rect.left, rect.top),
                size = Size(ovalWidth, ovalHeight)
            )

            // Inner cristae membrane folds (zigzag lines)
            val foldCount = 4
            val step = ovalWidth / (foldCount + 1)
            for (i in 1..foldCount) {
                val fx = rect.left + i * step
                val isUp = (i % 2 == 0)
                drawLine(
                    color = Color(0xFFFFCCBC).copy(alpha = proj.alpha * 0.9f),
                    start = Offset(fx, if (isUp) rect.top + 4.dp.toPx() else rect.bottom - 4.dp.toPx()),
                    end = Offset(fx, if (isUp) rect.bottom - 8.dp.toPx() else rect.top + 8.dp.toPx()),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // ATP Energy sparks if in ATP simulation mode
            if (simulationMode == SimulationViewMode.ATP_PRODUCTION) {
                drawCircle(
                    color = Color(0xFFFFD54F).copy(alpha = 0.9f),
                    radius = 3.dp.toPx() * pulseScale,
                    center = Offset(center.x + r * 0.6f, center.y - r * 0.4f)
                )
                drawCircle(
                    color = Color(0xFFFFD54F).copy(alpha = 0.8f),
                    radius = 2.5.dp.toPx() * pulseScale,
                    center = Offset(center.x - r * 0.5f, center.y + r * 0.3f)
                )
            }
        }

        OrganelleType.ROUGH_ER -> {
            // Folded cisternal ribbons dotted with ribosomes
            for (layer in 0..2) {
                val offsetLayer = layer * 8.dp.toPx()
                drawArc(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF42A5F5).copy(alpha = proj.alpha),
                            Color(0xFF1E88E5).copy(alpha = proj.alpha * 0.8f)
                        )
                    ),
                    startAngle = 135f + layer * 15f,
                    sweepAngle = 140f,
                    useCenter = false,
                    topLeft = Offset(center.x - r - offsetLayer, center.y - r - offsetLayer),
                    size = Size((r + offsetLayer) * 2f, (r + offsetLayer) * 2f),
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )

                // Ribosome specks on the ER
                for (dot in 0..4) {
                    val angle = (145f + layer * 15f + dot * 24f) * (Math.PI / 180f)
                    val rad = r + offsetLayer
                    val dx = center.x + (rad * cos(angle)).toFloat()
                    val dy = center.y + (rad * sin(angle)).toFloat()
                    drawCircle(
                        color = Color(0xFFBBDEFB).copy(alpha = proj.alpha),
                        radius = 2.dp.toPx(),
                        center = Offset(dx, dy)
                    )
                }
            }
        }

        OrganelleType.SMOOTH_ER -> {
            // Smooth interconnected tubular loops
            for (tube in 0..3) {
                val angle = tube * 45f * (Math.PI / 180f)
                val tx = center.x + (r * 0.5f * cos(angle)).toFloat()
                val ty = center.y + (r * 0.5f * sin(angle)).toFloat()
                drawOval(
                    color = Color(0xFF26C6DA).copy(alpha = proj.alpha * 0.85f),
                    topLeft = Offset(tx - 12.dp.toPx(), ty - 6.dp.toPx()),
                    size = Size(24.dp.toPx(), 12.dp.toPx()),
                    style = Stroke(width = 2.5.dp.toPx())
                )
            }
        }

        OrganelleType.GOLGI_APPARATUS -> {
            // Stack of curved crescent cisternae with budding vesicles
            val cisternaeCount = 4
            for (i in 0 until cisternaeCount) {
                val cOffset = i * 6.5.dp.toPx()
                drawArc(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFA726).copy(alpha = proj.alpha),
                            Color(0xFFF57C00).copy(alpha = proj.alpha)
                        )
                    ),
                    startAngle = -45f,
                    sweepAngle = 110f,
                    useCenter = false,
                    topLeft = Offset(center.x - r + cOffset, center.y - r),
                    size = Size(r * 2f, r * 2f),
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            // Budding secretory vesicles
            drawCircle(
                color = Color(0xFFFFE082).copy(alpha = proj.alpha),
                radius = 4.dp.toPx(),
                center = Offset(center.x + r * 0.9f, center.y - r * 0.3f)
            )
            drawCircle(
                color = Color(0xFFFFE082).copy(alpha = proj.alpha),
                radius = 3.5.dp.toPx(),
                center = Offset(center.x + r * 1.1f, center.y + r * 0.2f)
            )
        }

        OrganelleType.LYSOSOMES -> {
            // Hydrolytic enzyme sphere
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF4081).copy(alpha = proj.alpha),
                        Color(0xFFC2185B).copy(alpha = proj.alpha)
                    ),
                    center = Offset(center.x - r * 0.3f, center.y - r * 0.3f),
                    radius = r
                ),
                radius = r,
                center = center
            )
            drawCircle(
                color = Color(0xFFF8BBD0).copy(alpha = proj.alpha * 0.9f),
                radius = r * 0.35f,
                center = Offset(center.x - r * 0.25f, center.y - r * 0.25f)
            )
        }

        OrganelleType.CENTROSOME -> {
            // Two perpendicular centriole cylinders
            val cW = 18.dp.toPx()
            val cH = 7.dp.toPx()
            // Horizontal cylinder
            drawRoundRect(
                color = Color(0xFF8D6E63).copy(alpha = proj.alpha),
                topLeft = Offset(center.x - cW / 2f, center.y - cH / 2f),
                size = Size(cW, cH),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
            )
            // Vertical cylinder (perpendicular 90°)
            drawRoundRect(
                color = Color(0xFFA1887F).copy(alpha = proj.alpha),
                topLeft = Offset(center.x + 2.dp.toPx(), center.y - cW / 2f),
                size = Size(cH, cW),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
            )
        }

        OrganelleType.CHLOROPLAST -> {
            // Green lens shape with stacked thylakoid grana
            val cWidth = r * 2.3f
            val cHeight = r * 1.4f
            drawOval(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF66BB6A).copy(alpha = proj.alpha),
                        Color(0xFF2E7D32).copy(alpha = proj.alpha)
                    )
                ),
                topLeft = Offset(center.x - cWidth / 2f, center.y - cHeight / 2f),
                size = Size(cWidth, cHeight)
            )
            // Grana stacks
            for (g in -1..1) {
                val gx = center.x + g * 12.dp.toPx()
                for (th in 0..2) {
                    val gy = center.y + (th - 1) * 4.dp.toPx()
                    drawOval(
                        color = Color(0xFF1B5E20).copy(alpha = proj.alpha),
                        topLeft = Offset(gx - 5.dp.toPx(), gy - 1.5.dp.toPx()),
                        size = Size(10.dp.toPx(), 3.dp.toPx())
                    )
                }
            }
        }

        OrganelleType.CENTRAL_VACUOLE -> {
            // Giant water-filled reservoir with turgor shimmer
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF81D4FA).copy(alpha = proj.alpha * 0.45f),
                        Color(0xFF29B6F6).copy(alpha = proj.alpha * 0.75f),
                        Color(0xFF0288D1).copy(alpha = proj.alpha * 0.85f)
                    ),
                    center = Offset(center.x - r * 0.25f, center.y - r * 0.25f),
                    radius = r
                ),
                radius = r,
                center = center
            )
            drawCircle(
                color = Color(0xFFE1F5FE).copy(alpha = proj.alpha * 0.8f),
                radius = r,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        else -> {
            // Generic organelle sphere
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(baseColor.copy(alpha = proj.alpha), baseColor.copy(alpha = proj.alpha * 0.6f)),
                    center = Offset(center.x - r * 0.3f, center.y - r * 0.3f),
                    radius = r
                ),
                radius = r,
                center = center
            )
        }
    }
}

private fun DrawScope.drawSelectionPin(
    proj: ProjectedOrganelle,
    pulseScale: Float
) {
    val center = Offset(proj.screenX, proj.screenY)
    val baseRadius = proj.projectedRadius

    // Pulsing outer aura ring
    drawCircle(
        color = Color(proj.instance.type.primaryColorHex).copy(alpha = 0.4f),
        radius = (baseRadius + 14.dp.toPx()) * pulseScale,
        center = center,
        style = Stroke(width = 2.dp.toPx())
    )

    // Inner bright targeting ring
    drawCircle(
        color = Color.White,
        radius = baseRadius + 6.dp.toPx(),
        center = center,
        style = Stroke(width = 2.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
    )

    // Pin indicator icon
    drawCircle(
        color = Color(proj.instance.type.primaryColorHex),
        radius = 5.dp.toPx(),
        center = Offset(center.x, center.y - baseRadius - 12.dp.toPx())
    )
}

private fun DrawScope.drawSecretoryPathwayVesicles(
    centerX: Float,
    centerY: Float,
    rotY: Float,
    rotX: Float,
    zoomLevel: Float,
    progress: Float
) {
    // 4 Key Waypoints:
    // 1. Nucleus/RER -> 2. Transport Vesicle -> 3. Golgi Apparatus -> 4. Plasma Membrane Exocytosis
    val p0 = Offset(centerX - 30f * zoomLevel, centerY - 30f * zoomLevel)
    val p1 = Offset(centerX + 10f * zoomLevel, centerY + 10f * zoomLevel)
    val p2 = Offset(centerX + 60f * zoomLevel, centerY + 40f * zoomLevel)
    val p3 = Offset(centerX + 120f * zoomLevel, centerY + 60f * zoomLevel)

    // Interpolate current position along bezier/segments
    val currentPos = when {
        progress < 0.33f -> {
            val t = progress / 0.33f
            Offset(p0.x + (p1.x - p0.x) * t, p0.y + (p1.y - p0.y) * t)
        }
        progress < 0.66f -> {
            val t = (progress - 0.33f) / 0.33f
            Offset(p1.x + (p2.x - p1.x) * t, p1.y + (p2.y - p1.y) * t)
        }
        else -> {
            val t = (progress - 0.66f) / 0.34f
            Offset(p2.x + (p3.x - p2.x) * t, p2.y + (p3.y - p2.y) * t)
        }
    }

    // Guide trajectory path
    val path = Path().apply {
        moveTo(p0.x, p0.y)
        lineTo(p1.x, p1.y)
        lineTo(p2.x, p2.y)
        lineTo(p3.x, p3.y)
    }
    drawPath(
        path = path,
        color = Color(0xFFFFD54F).copy(alpha = 0.45f),
        style = Stroke(
            width = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), progress * 50f)
        )
    )

    // Glowing travelling vesicle particle
    drawCircle(
        color = Color(0xFFFFEB3B),
        radius = 7.dp.toPx(),
        center = currentPos
    )
    drawCircle(
        color = Color.White,
        radius = 3.5.dp.toPx(),
        center = currentPos
    )
}
