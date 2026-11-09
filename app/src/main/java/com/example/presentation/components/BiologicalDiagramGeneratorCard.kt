package com.example.presentation.components

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.domain.model.DiagramPreset
import com.example.domain.model.DiagramStyle
import com.example.presentation.viewmodel.BiologicalDiagramViewModel
import com.example.presentation.viewmodel.DiagramGenUiState
import com.example.ui.theme.*

@Composable
fun BiologicalDiagramGeneratorCard(
    viewModel: BiologicalDiagramViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var isFullscreenOpen by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .border(1.dp, BioTealPrimary.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            .testTag("ai_diagram_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with AI Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(BioTealPrimary, BioCyanAccent))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "مخططات بيولوجية بالذكاء الاصطناعي",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Gemini AI Schematic • مجهزة للمراجعة السريعة",
                            style = MaterialTheme.typography.labelSmall,
                            color = BioTealPrimary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MolecularPurpleLight
                ) {
                    Text(
                        text = "Agent 2: Visual",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MolecularPurple,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (val s = state) {
                is DiagramGenUiState.Idle -> {
                    DiagramIdleContent(
                        state = s,
                        onSelectPreset = { viewModel.selectPreset(it) },
                        onSelectStyle = { viewModel.setStyle(it) },
                        onCustomPromptChange = { viewModel.setCustomPrompt(it) },
                        onGenerate = { viewModel.generateDiagram() }
                    )
                }
                is DiagramGenUiState.Generating -> {
                    DiagramGeneratingContent(state = s)
                }
                is DiagramGenUiState.Success -> {
                    DiagramSuccessContent(
                        state = s,
                        onOpenFullscreen = { isFullscreenOpen = true },
                        onToggleQuickReview = { viewModel.toggleQuickReview() },
                        onSelectLandmark = { viewModel.selectActiveLandmark(it) },
                        onGenerateAnother = { viewModel.resetToIdle() }
                    )

                    if (isFullscreenOpen && s.diagram.bitmap != null) {
                        FullscreenDiagramDialog(
                            bitmap = s.diagram.bitmap,
                            title = s.diagram.title,
                            onDismiss = { isFullscreenOpen = false }
                        )
                    }
                }
                is DiagramGenUiState.Error -> {
                    DiagramErrorContent(
                        state = s,
                        onRetry = { viewModel.generateDiagram() },
                        onReset = { viewModel.resetToIdle() }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiagramIdleContent(
    state: DiagramGenUiState.Idle,
    onSelectPreset: (DiagramPreset) -> Unit,
    onSelectStyle: (DiagramStyle) -> Unit,
    onCustomPromptChange: (String) -> Unit,
    onGenerate: () -> Unit
) {
    var showCustomInput by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "اختر أحد المخططات الوزارية المجهزة أو اكتب طلباً مخصصاً:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Presets horizontal carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.presets.forEach { preset ->
                val isSelected = preset.id == state.selectedPreset?.id
                Card(
                    modifier = Modifier
                        .width(220.dp)
                        .clickable { onSelectPreset(preset) }
                        .testTag("preset_${preset.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) BioCyanLight.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 1.5.dp else 0.5.dp,
                        color = if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) BioTealPrimary else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = preset.titleAr,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${preset.keyLandmarks.size} معالم امتحانية محددة",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Style Selector Chips
        Text(
            text = "أسلوب الرسم المطلوب:",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            DiagramStyle.values().forEach { style ->
                val isSelected = state.selectedStyle == style
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectStyle(style) },
                    label = { Text(style.labelAr, fontSize = 11.sp) },
                    modifier = Modifier.testTag("style_${style.name.lowercase()}")
                )
            }
        }

        // Custom Prompt Expander Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showCustomInput = !showCustomInput },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (showCustomInput) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = BioTealPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (showCustomInput) "إخفاء الطلب المخصص" else "كتابة طلب مخصص للرسم (اختياري)",
                style = MaterialTheme.typography.labelSmall,
                color = BioTealPrimary
            )
        }

        if (showCustomInput) {
            OutlinedTextField(
                value = state.customPrompt,
                onValueChange = onCustomPromptChange,
                placeholder = { Text("مثال: ارسم قطاع طولي في زهرة نموذجية موضحاً المبيض والتخت...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("custom_prompt_input"),
                maxLines = 2,
                textStyle = MaterialTheme.typography.bodySmall
            )
        }

        // Generate Action Button
        Button(
            onClick = onGenerate,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("generate_diagram_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "توليد المخطط العلمي الآن (AI Generate)",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun DiagramGeneratingContent(state: DiagramGenUiState.Generating) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(BioTealPrimary.copy(alpha = alphaAnim * 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Biotech,
                contentDescription = null,
                tint = BioTealPrimary,
                modifier = Modifier.size(36.dp)
            )
        }

        CircularProgressIndicator(
            color = BioTealPrimary,
            modifier = Modifier.size(32.dp),
            strokeWidth = 3.dp
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = state.progressMessage,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "جاري البناء بواسطة Gemini Nano Banana 2.1 • الوقت المنقضي: ${state.elapsedSeconds} ثانية",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DiagramSuccessContent(
    state: DiagramGenUiState.Success,
    onOpenFullscreen: () -> Unit,
    onToggleQuickReview: () -> Unit,
    onSelectLandmark: (Int) -> Unit,
    onGenerateAnother: () -> Unit
) {
    val diagram = state.diagram

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Diagram Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = diagram.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepNavy
            )
            IconButton(
                onClick = onOpenFullscreen,
                modifier = Modifier.testTag("open_fullscreen_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = "Fullscreen",
                    tint = BioTealPrimary
                )
            }
        }

        // Rendered Image Container
        if (diagram.bitmap != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                    .clickable { onOpenFullscreen() }
                    .testTag("generated_diagram_image"),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = diagram.bitmap.asImageBitmap(),
                    contentDescription = diagram.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                // Magnify Hint Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ZoomIn, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("انقر للتكبير", color = Color.White, fontSize = 10.sp)
                    }
                }
            }
        }

        // Quick Review Formatted Study Panel
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FactCheck, contentDescription = null, tint = BioTealPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "بطاقة المراجعة السريعة (Quick Review):",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    IconButton(onClick = onToggleQuickReview, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = if (state.isQuickReviewExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null
                        )
                    }
                }

                if (state.isQuickReviewExpanded) {
                    // Landmark items
                    Text(
                        text = "المعالم والبيانات الواجب فحصها في الامتحان:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    diagram.keyLandmarks.forEachIndexed { index, landmark ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onSelectLandmark(index) }
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${index + 1}. ",
                                fontWeight = FontWeight.Bold,
                                color = BioTealPrimary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = landmark,
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // Exam Trap Warning
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = ExamGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = diagram.examTrapWarning,
                            fontSize = 11.sp,
                            color = DeepNavy,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Golden Rule
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = diagram.goldenRule,
                            fontSize = 11.sp,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onGenerateAnother,
                modifier = Modifier
                    .weight(1f)
                    .testTag("generate_another_diagram_btn")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("توليد مخطط آخر")
            }
            Button(
                onClick = onOpenFullscreen,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary)
            ) {
                Icon(Icons.Default.ZoomIn, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("عرض كامل الشاشة")
            }
        }
    }
}

@Composable
private fun DiagramErrorContent(
    state: DiagramGenUiState.Error,
    onRetry: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(36.dp))
        Text(
            text = "تعذر توليد المخطط البيولوجي",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = state.message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onReset) {
                Text("إلغاء والعودة")
            }
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("إعادة المحاولة")
            }
        }
    }
}

@Composable
fun FullscreenDiagramDialog(
    bitmap: Bitmap,
    title: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.85f))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
            },
            containerColor = Color.Black
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
