package com.example.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.viewmodel.ChapterVulnerability
import com.example.presentation.viewmodel.MistakeAnalyticsViewModel
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MistakeAnalyticsDashboardScreen(
    viewModel: MistakeAnalyticsViewModel,
    onBack: () -> Unit,
    onLaunchRemediationQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val state by viewModel.analyticsState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("screen_mistake_analytics_dashboard"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "المساعد الذكي لتحليل الأخطاء والتريكات",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Offline Cognitive Diagnostic • Ministry Traps Radar",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("analytics_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Top Telemetry Metrics
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "إجمالي الأخطاء",
                        value = "${state.totalMistakes}",
                        subtitle = "مرصودة محلياً",
                        icon = Icons.Default.Warning,
                        color = AlertCoral,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "نسبة الاستيعاب",
                        value = "${state.resolutionRatePercentage}%",
                        subtitle = "${state.totalMistakes - state.unresolvedCount} محلولة",
                        icon = Icons.Default.CheckCircle,
                        color = BioSuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "تأثير الدرجات",
                        value = "-${state.estimatedExamImpactMarks.toInt()}",
                        subtitle = "من 60 درجة",
                        icon = Icons.Default.TrendingDown,
                        color = Color(0xFFFFB703),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // The Interactive Radar Chart Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth().testTag("radar_chart_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Radar, contentDescription = null, tint = BioTealPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "مخطط الرادار لقوة الفصول (Radar Mastery)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = BioTealPrimary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "5 وحدات منهجية",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BioTealPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Custom Radar Chart Canvas
                        RadarChartCanvas(
                            scores = state.chapterScores,
                            modifier = Modifier.size(260.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Radar Legend
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).background(BioTealPrimary, CircleShape))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("المستوى الحالي للطالب", style = MaterialTheme.typography.labelSmall)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).border(1.dp, MaterialTheme.colorScheme.outline, CircleShape))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("الحد الأقصى (100%)", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            // Quick Remediation Quiz CTA
            if (state.unresolvedCount > 0) {
                item {
                    Button(
                        onClick = onLaunchRemediationQuiz,
                        colors = ButtonDefaults.buttonColors(containerColor = AlertCoral),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("launch_remediation_quiz_btn")
                    ) {
                        Icon(Icons.Default.Replay, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "بدء كويز معالجة التريكات (${state.unresolvedCount} أسئلة غير مستوعبة)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Diagnostic Trap Feed Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "سجل التريكات الوزارية التفصيلي (${state.mistakesList.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    if (state.mistakesList.any { it.isResolved }) {
                        TextButton(
                            onClick = { viewModel.clearResolvedMistakes() },
                            modifier = Modifier.testTag("clear_resolved_mistakes_btn")
                        ) {
                            Text("حذف المحلول", fontSize = 12.sp)
                        }
                    }
                }
            }

            // List of mistakes
            if (state.mistakesList.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = BioSuccessGreen, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "ممتاز! لم يتم رصد أي فخاخ أو أخطاء بعد",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "عند حل نماذج الامتحانات أو كويزات الفصول، سيتولى المحرك رصد أي تريكة تقع فيها تلقائياً.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(state.mistakesList) { mistake ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (mistake.isResolved) MaterialTheme.colorScheme.surface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (mistake.isResolved) BioSuccessGreen.copy(alpha = 0.4f) else AlertCoral.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("diagnostic_mistake_card_${mistake.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (mistake.isResolved) BioSuccessGreen.copy(alpha = 0.12f) else AlertCoral.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = "سؤال رقم ${mistake.questionNumber}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (mistake.isResolved) BioSuccessGreen else AlertCoral,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    if (mistake.isResolved) {
                                        Text("تم الاستيعاب ✓", fontSize = 11.sp, color = BioSuccessGreen, fontWeight = FontWeight.Bold)
                                    }
                                }

                                if (!mistake.isResolved) {
                                    OutlinedButton(
                                        onClick = { viewModel.resolveMistake(mistake.id) },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(30.dp).testTag("resolve_btn_${mistake.id}")
                                    ) {
                                        Text("فهمت التريكة", fontSize = 11.sp)
                                    }
                                }
                            }

                            Text(
                                text = mistake.questionTextArabic,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, lineHeight = 20.sp)
                            )

                            if (mistake.trapDescriptionArabic.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = AlertCoral.copy(alpha = 0.08f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Bolt, contentDescription = null, tint = AlertCoral, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "التريكة: ${mistake.trapDescriptionArabic}",
                                            style = MaterialTheme.typography.labelSmall.copy(color = AlertCoral, fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }

                            if (mistake.explanationArabic.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "التفسير العلمي المعتمد: ${mistake.explanationArabic}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// 5-Axis Radar Chart Canvas Composable
@Composable
private fun RadarChartCanvas(
    scores: List<ChapterVulnerability>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val radius = (w / 2f) * 0.72f
        val numAxes = 5

        // Draw 5 concentric web polygons (20%, 40%, 60%, 80%, 100%)
        for (step in 1..5) {
            val r = radius * (step / 5f)
            val webPath = Path()
            for (i in 0 until numAxes) {
                val angle = Math.toRadians((i * (360f / numAxes) - 90).toDouble())
                val x = cx + (r * cos(angle)).toFloat()
                val y = cy + (r * sin(angle)).toFloat()
                if (i == 0) webPath.moveTo(x, y) else webPath.lineTo(x, y)
            }
            webPath.close()
            drawPath(
                webPath,
                color = Color.Gray.copy(alpha = 0.25f),
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Draw 5 axis lines radiating from center
        for (i in 0 until numAxes) {
            val angle = Math.toRadians((i * (360f / numAxes) - 90).toDouble())
            val x = cx + (radius * cos(angle)).toFloat()
            val y = cy + (radius * sin(angle)).toFloat()
            drawLine(
                color = Color.Gray.copy(alpha = 0.35f),
                start = Offset(cx, cy),
                end = Offset(x, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Draw Student Data Polygon
        if (scores.size >= numAxes) {
            val dataPath = Path()
            val points = mutableListOf<Offset>()

            for (i in 0 until numAxes) {
                val score = scores.getOrNull(i)?.masteryScore ?: 0.8f
                val r = radius * score
                val angle = Math.toRadians((i * (360f / numAxes) - 90).toDouble())
                val x = cx + (r * cos(angle)).toFloat()
                val y = cy + (r * sin(angle)).toFloat()
                val pt = Offset(x, y)
                points.add(pt)
                if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
            }
            dataPath.close()

            // Fill polygon with semi-transparent Teal
            drawPath(
                dataPath,
                color = BioTealPrimary.copy(alpha = 0.35f)
            )

            // Draw glowing outline
            drawPath(
                dataPath,
                color = BioTealPrimary,
                style = Stroke(width = 2.5.dp.toPx())
            )

            // Draw data point dots on vertices
            points.forEach { pt ->
                drawCircle(color = Color.White, radius = 4.dp.toPx(), center = pt)
                drawCircle(color = BioTealPrimary, radius = 2.5.dp.toPx(), center = pt)
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
