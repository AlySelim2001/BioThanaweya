package com.example.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.engine.CellDivisionEngine
import com.example.domain.model.DivisionProcess
import com.example.domain.model.DivisionStage
import com.example.ui.theme.*
import kotlin.math.sin

@Composable
fun MitosisMeiosisSimulationCanvas(
    modifier: Modifier = Modifier
) {
    var selectedProcess by remember { mutableStateOf(DivisionProcess.MEIOSIS_1) }
    var currentStage by remember { mutableStateOf(DivisionStage.PROPHASE) }
    var isCrossoverTriggered by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(false) }

    // Chromosome interactive drag offsets
    var maternalDragOffset by remember { mutableStateOf(Offset.Zero) }
    var paternalDragOffset by remember { mutableStateOf(Offset.Zero) }

    val stageInfo = remember(selectedProcess, currentStage) {
        CellDivisionEngine.getStageInfo(selectedProcess, currentStage)
    }

    // Reset drag offsets when stage or process changes
    LaunchedEffect(selectedProcess, currentStage) {
        maternalDragOffset = Offset.Zero
        paternalDragOffset = Offset.Zero
    }

    // Auto-play timer
    LaunchedEffect(isPlaying, currentStage) {
        if (isPlaying) {
            kotlinx.coroutines.delay(2800L)
            val allStages = DivisionStage.entries
            val nextIndex = (currentStage.stepOrder + 1) % allStages.size
            currentStage = allStages[nextIndex]
        }
    }

    // Spindle fiber pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "spindle_pulse")
    val spindlePulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "spindle_alpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("mitosis_meiosis_canvas_container"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Process Switcher (Mitosis vs Meiosis I vs Meiosis II)
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth().testTag("process_selector_row")
        ) {
            DivisionProcess.entries.forEachIndexed { index, proc ->
                SegmentedButton(
                    selected = selectedProcess == proc,
                    onClick = {
                        selectedProcess = proc
                        isPlaying = false
                    },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = DivisionProcess.entries.size)
                ) {
                    Text(
                        text = when (proc) {
                            DivisionProcess.MITOSIS -> "ميتوزي"
                            DivisionProcess.MEIOSIS_1 -> "ميوزي أول"
                            DivisionProcess.MEIOSIS_2 -> "ميوزي ثانٍ"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Stage Scrubber Slider
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = BioTealPrimary.copy(alpha = 0.15f),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${currentStage.stepOrder + 1}",
                                    color = BioTealPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stageInfo.stage.stageNameArabic,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = BioTealPrimary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (selectedProcess == DivisionProcess.MEIOSIS_1 && currentStage == DivisionStage.PROPHASE) {
                            FilledTonalButton(
                                onClick = { isCrossoverTriggered = !isCrossoverTriggered },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(32.dp).testTag("btn_toggle_crossover")
                            ) {
                                Icon(Icons.Default.Shuffle, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("تبديل العبور", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier.size(32.dp).testTag("btn_play_pause_stages")
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play Stages",
                                tint = BioTealPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Slider(
                    value = currentStage.stepOrder.toFloat(),
                    onValueChange = { value ->
                        isPlaying = false
                        val idx = value.toInt().coerceIn(0, DivisionStage.entries.size - 1)
                        currentStage = DivisionStage.entries[idx]
                    },
                    valueRange = 0f..4f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth().testTag("stages_slider")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DivisionStage.entries.forEach { st ->
                        Text(
                            text = st.stageNameArabic.replace("الطور ", ""),
                            fontSize = 10.sp,
                            fontWeight = if (st == currentStage) FontWeight.Bold else FontWeight.Normal,
                            color = if (st == currentStage) BioTealPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // The Main Interactive Touch Canvas
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BioTealPrimary.copy(alpha = 0.4f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .testTag("mitosis_interactive_canvas")
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(currentStage) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val touchX = change.position.x
                                val midX = size.width / 2f
                                if (touchX < midX) {
                                    maternalDragOffset += dragAmount
                                } else {
                                    paternalDragOffset += dragAmount
                                }
                            }
                        }
                ) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f
                    val cy = h / 2f

                    // 1. Draw Cell Membrane & Cleavage Furrow (during Telophase)
                    if (currentStage == DivisionStage.TELOPHASE) {
                        val pathLeft = Path().apply {
                            addOval(androidx.compose.ui.geometry.Rect(w * 0.05f, h * 0.15f, w * 0.52f, h * 0.85f))
                        }
                        val pathRight = Path().apply {
                            addOval(androidx.compose.ui.geometry.Rect(w * 0.48f, h * 0.15f, w * 0.95f, h * 0.85f))
                        }
                        drawPath(pathLeft, color = Color(0x3300D2FF))
                        drawPath(pathLeft, color = BioTealPrimary, style = Stroke(width = 3.dp.toPx()))
                        drawPath(pathRight, color = Color(0x33FF4B72))
                        drawPath(pathRight, color = BioTealPrimary, style = Stroke(width = 3.dp.toPx()))
                    } else {
                        // Single Diploid/Haploid Cell
                        drawCircle(
                            brush = Brush.radialGradient(
                                listOf(Color(0x221E3E62), Color(0x050B192C)),
                                center = Offset(cx, cy),
                                radius = w * 0.45f
                            ),
                            radius = w * 0.42f,
                            center = Offset(cx, cy)
                        )
                        drawCircle(
                            color = BioTealPrimary.copy(alpha = 0.6f),
                            radius = w * 0.42f,
                            center = Offset(cx, cy),
                            style = Stroke(width = 2.5.dp.toPx())
                        )
                    }

                    // 2. Draw Centrioles (الأجسام المركزية) & Spindle Fibers (خيوط المغزل)
                    val poleLeft = Offset(w * 0.12f, cy)
                    val poleRight = Offset(w * 0.88f, cy)

                    if (currentStage != DivisionStage.INTERPHASE) {
                        drawCircle(color = Color(0xFFFFD166), radius = 6.dp.toPx(), center = poleLeft)
                        drawCircle(color = Color(0xFFFFD166), radius = 6.dp.toPx(), center = poleRight)

                        // Spindle Fiber lines to equatorial center
                        val spindleAlpha = spindlePulse * 0.4f
                        val fiberColor = Color(0xFFFFD166).copy(alpha = spindleAlpha)

                        for (i in -3..3) {
                            val targetY = cy + (i * 22.dp.toPx())
                            drawLine(
                                color = fiberColor,
                                start = poleLeft,
                                end = Offset(cx, targetY),
                                strokeWidth = 1.dp.toPx()
                            )
                            drawLine(
                                color = fiberColor,
                                start = poleRight,
                                end = Offset(cx, targetY),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                    }

                    // 3. Draw Chromosomes based on Current Stage & Process
                    val maternalColor = Color(0xFFFF4B72) // Pink/Red Maternal
                    val paternalColor = Color(0xFF00D2FF) // Cyan Paternal

                    when (currentStage) {
                        DivisionStage.INTERPHASE -> {
                            // Loose chromatin thread tangle in the nucleus
                            drawCircle(
                                color = Color.White.copy(alpha = 0.15f),
                                radius = 65.dp.toPx(),
                                center = Offset(cx, cy),
                                style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                            )
                            // Chromatin scribble curves
                            for (angle in 0..360 step 30) {
                                val rad = Math.toRadians(angle.toDouble())
                                val r = 35.dp.toPx() + sin(angle.toDouble()) * 15f
                                drawCircle(
                                    color = if (angle % 60 == 0) maternalColor.copy(alpha = 0.5f) else paternalColor.copy(alpha = 0.5f),
                                    radius = 3.dp.toPx(),
                                    center = Offset(cx + (r * kotlin.math.cos(rad)).toFloat(), cy + (r * kotlin.math.sin(rad)).toFloat())
                                )
                            }
                        }

                        DivisionStage.PROPHASE -> {
                            // Homologous Chromosomes condensing & Synapsis in Meiosis I
                            val matPos = Offset(cx - 35.dp.toPx() + maternalDragOffset.x.coerceIn(-50f, 50f), cy + maternalDragOffset.y.coerceIn(-50f, 50f))
                            val patPos = Offset(cx + 35.dp.toPx() + paternalDragOffset.x.coerceIn(-50f, 50f), cy + paternalDragOffset.y.coerceIn(-50f, 50f))

                            // Draw Maternal Chromosome X shape
                            drawChromosomeX(
                                center = matPos,
                                primaryColor = maternalColor,
                                crossoverColor = if (isCrossoverTriggered && selectedProcess == DivisionProcess.MEIOSIS_1) paternalColor else maternalColor
                            )

                            // Draw Paternal Chromosome X shape
                            drawChromosomeX(
                                center = patPos,
                                primaryColor = paternalColor,
                                crossoverColor = if (isCrossoverTriggered && selectedProcess == DivisionProcess.MEIOSIS_1) maternalColor else paternalColor
                            )
                        }

                        DivisionStage.METAPHASE -> {
                            // Equatorial alignment (Single file in Mitosis/Meiosis II, Double in Meiosis I)
                            if (selectedProcess == DivisionProcess.MEIOSIS_1) {
                                // Double row (Tetrad alignment)
                                drawChromosomeX(center = Offset(cx - 24.dp.toPx(), cy - 35.dp.toPx()), primaryColor = maternalColor, crossoverColor = if (isCrossoverTriggered) paternalColor else maternalColor)
                                drawChromosomeX(center = Offset(cx + 24.dp.toPx(), cy - 35.dp.toPx()), primaryColor = paternalColor, crossoverColor = if (isCrossoverTriggered) maternalColor else paternalColor)

                                drawChromosomeX(center = Offset(cx - 24.dp.toPx(), cy + 35.dp.toPx()), primaryColor = paternalColor, crossoverColor = if (isCrossoverTriggered) maternalColor else paternalColor)
                                drawChromosomeX(center = Offset(cx + 24.dp.toPx(), cy + 35.dp.toPx()), primaryColor = maternalColor, crossoverColor = if (isCrossoverTriggered) paternalColor else maternalColor)
                            } else {
                                // Single file vertical line along center axis
                                drawChromosomeX(center = Offset(cx, cy - 45.dp.toPx()), primaryColor = maternalColor, crossoverColor = maternalColor)
                                drawChromosomeX(center = Offset(cx, cy + 45.dp.toPx()), primaryColor = paternalColor, crossoverColor = paternalColor)
                            }
                        }

                        DivisionStage.ANAPHASE -> {
                            // Chromosomes / chromatids pulling towards opposite poles
                            if (selectedProcess == DivisionProcess.MEIOSIS_1) {
                                // Full chromosomes pulling apart without centromere splitting
                                drawChromosomeX(center = Offset(w * 0.30f, cy - 30.dp.toPx()), primaryColor = maternalColor, crossoverColor = if (isCrossoverTriggered) paternalColor else maternalColor)
                                drawChromosomeX(center = Offset(w * 0.70f, cy - 30.dp.toPx()), primaryColor = paternalColor, crossoverColor = if (isCrossoverTriggered) maternalColor else paternalColor)

                                drawChromosomeX(center = Offset(w * 0.30f, cy + 30.dp.toPx()), primaryColor = paternalColor, crossoverColor = if (isCrossoverTriggered) maternalColor else paternalColor)
                                drawChromosomeX(center = Offset(w * 0.70f, cy + 30.dp.toPx()), primaryColor = maternalColor, crossoverColor = if (isCrossoverTriggered) paternalColor else maternalColor)
                            } else {
                                // V-shaped single chromatids pulling away (Centromere split)
                                drawSingleChromatidV(center = Offset(w * 0.30f, cy - 30.dp.toPx()), color = maternalColor, pointingRight = false)
                                drawSingleChromatidV(center = Offset(w * 0.70f, cy - 30.dp.toPx()), color = maternalColor, pointingRight = true)

                                drawSingleChromatidV(center = Offset(w * 0.30f, cy + 30.dp.toPx()), color = paternalColor, pointingRight = false)
                                drawSingleChromatidV(center = Offset(w * 0.70f, cy + 30.dp.toPx()), color = paternalColor, pointingRight = true)
                            }
                        }

                        DivisionStage.TELOPHASE -> {
                            // Formed daughter cell nuclei
                            drawChromosomeX(center = Offset(w * 0.28f, cy), primaryColor = maternalColor, crossoverColor = if (isCrossoverTriggered && selectedProcess == DivisionProcess.MEIOSIS_1) paternalColor else maternalColor)
                            drawChromosomeX(center = Offset(w * 0.72f, cy), primaryColor = paternalColor, crossoverColor = if (isCrossoverTriggered && selectedProcess == DivisionProcess.MEIOSIS_1) maternalColor else paternalColor)
                        }
                    }
                }

                // Interactive touch instruction overlay
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    modifier = Modifier.padding(10.dp).align(Alignment.BottomStart)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.TouchApp, contentDescription = null, tint = BioTealPrimary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentStage == DivisionStage.PROPHASE) "اسحب الكروموسومات باللمس لتجربة الاقتران" else "راقب حركة الكروموسومات وخيوط المغزل",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // Biological Telemetry Card (Formulas & Traps)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(title = "عدد الكروموسومات", value = stageInfo.chromosomeCountFormula, modifier = Modifier.weight(1f))
                    MetricBox(title = "عدد الكروماتيدات", value = stageInfo.chromatidsCountFormula, modifier = Modifier.weight(1f))
                    MetricBox(title = "كمية الحمض DNA", value = stageInfo.dnaContentFormula, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stageInfo.descriptionArabic,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AlertCoral.copy(alpha = 0.09f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AlertCoral.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = AlertCoral, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stageInfo.ministryExamTrapWarning,
                            style = MaterialTheme.typography.labelSmall.copy(color = AlertCoral, fontWeight = FontWeight.Bold, lineHeight = 16.sp)
                        )
                    }
                }
            }
        }
    }
}

// Helper Canvas Draw function for 2-chromatid chromosome X shape
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawChromosomeX(
    center: Offset,
    primaryColor: Color,
    crossoverColor: Color
) {
    val armLength = 26.dp.toPx()
    val armThickness = 5.dp.toPx()

    // Left Chromatid
    drawLine(color = primaryColor, start = Offset(center.x - armLength, center.y - armLength), end = center, strokeWidth = armThickness, cap = StrokeCap.Round)
    drawLine(color = crossoverColor, start = center, end = Offset(center.x - armLength, center.y + armLength), strokeWidth = armThickness, cap = StrokeCap.Round)

    // Right Chromatid
    drawLine(color = primaryColor, start = Offset(center.x + armLength, center.y - armLength), end = center, strokeWidth = armThickness, cap = StrokeCap.Round)
    drawLine(color = crossoverColor, start = center, end = Offset(center.x + armLength, center.y + armLength), strokeWidth = armThickness, cap = StrokeCap.Round)

    // Centromere (نقطة السنترومير المركزية)
    drawCircle(color = Color(0xFFFFD166), radius = 5.dp.toPx(), center = center)
}

// Helper Canvas Draw function for single chromatid V shape (Anaphase)
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSingleChromatidV(
    center: Offset,
    color: Color,
    pointingRight: Boolean
) {
    val dir = if (pointingRight) 1 else -1
    val armLength = 20.dp.toPx()
    val armThickness = 4.5.dp.toPx()

    drawLine(color = color, start = center, end = Offset(center.x - (dir * armLength), center.y - armLength * 0.7f), strokeWidth = armThickness, cap = StrokeCap.Round)
    drawLine(color = color, start = center, end = Offset(center.x - (dir * armLength), center.y + armLength * 0.7f), strokeWidth = armThickness, cap = StrokeCap.Round)

    drawCircle(color = Color(0xFFFFD166), radius = 4.dp.toPx(), center = center)
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = BioTealPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}
