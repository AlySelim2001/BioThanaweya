package com.example.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.domain.model.PacingCategory
import com.example.domain.model.PracticePacingSummary
import com.example.domain.model.QuestionPacingRecord
import com.example.ui.theme.*

/**
 * Format seconds into mm:ss display
 */
fun formatSeconds(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}

/**
 * PracticeTimerPacingBar:
 * Prominent timer component showing live question duration, pacing classification,
 * total session duration, and quick access to pacing analytics.
 */
@Composable
fun PracticeTimerPacingBar(
    currentQuestionSeconds: Int,
    totalSessionSeconds: Int,
    isTimerRunning: Boolean,
    pacingCategory: PacingCategory,
    answeredCount: Int,
    onToggleTimer: () -> Unit,
    onOpenPacingAnalysis: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (pacingColor, pacingBg, pacingText) = when (pacingCategory) {
        PacingCategory.FAST -> Triple(Color(0xFFE65100), ExamGoldLight, "⚡ رتم سريع (< 45ث)")
        PacingCategory.OPTIMAL -> Triple(BioSuccessGreen, SuccessGreenLight, "✓ وتيرة مثالية (45-90ث)")
        PacingCategory.OVERTIME -> Triple(AlertCoral, AlertCoralLight, "⚠️ تجاوز الوقت (> 90ث)")
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("practice_timer_bar"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (pacingCategory == PacingCategory.OVERTIME) AlertCoral.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Timer Display & Play/Pause Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onToggleTimer,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BioTealPrimary.copy(alpha = 0.12f))
                        .testTag("practice_timer_toggle_btn")
                ) {
                    Icon(
                        imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isTimerRunning) "إيقاف مؤقت" else "استئناف المؤقت",
                        tint = BioTealPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = if (isTimerRunning) pacingColor else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatSeconds(currentQuestionSeconds),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isTimerRunning) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.testTag("practice_current_timer_text")
                        )
                    }
                    Text(
                        text = "الإجمالي: ${formatSeconds(totalSessionSeconds)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            // Pacing Status Chip & Analytics Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = pacingBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, pacingColor.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = pacingText,
                        color = pacingColor,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("practice_pacing_badge")
                    )
                }

                FilledTonalButton(
                    onClick = onOpenPacingAnalysis,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("practice_pacing_analysis_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Pacing Analysis",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "التحليل ($answeredCount)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * PracticePacingSummaryDialog:
 * Comprehensive modal dialog analyzing student question pacing efficiency,
 * time distribution, keyword retention impact, and official exam strategies.
 */
@Composable
fun PracticePacingSummaryDialog(
    summary: PracticePacingSummary,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("practice_pacing_summary_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MolecularPurple.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = MolecularPurple, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text(
                                text = "تحليل كفاءة إدارة الوقت",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Pacing Efficiency & Time Analytics",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }

                HorizontalDivider()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Efficiency Score Card
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${summary.pacingEfficiencyScore}%",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (summary.pacingEfficiencyScore >= 75) BioSuccessGreen else ExamGold
                                )
                                Text(
                                    text = summary.pacingRatingArabic,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = summary.pacingRatingEnglish,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }

                    // Key Metrics Row
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MetricChip(
                                title = "إجمالي الوقت",
                                value = formatSeconds(summary.totalSessionSeconds),
                                modifier = Modifier.weight(1f)
                            )
                            MetricChip(
                                title = "متوسط السؤال",
                                value = "${summary.averageSecondsPerQuestion}ث",
                                modifier = Modifier.weight(1f)
                            )
                            MetricChip(
                                title = "المستهدف",
                                value = "90ث",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Pacing Categories Distribution Chips
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CategoryDistributionChip(
                                label = "مثالي",
                                count = summary.optimalCount,
                                tint = BioSuccessGreen,
                                bg = SuccessGreenLight,
                                modifier = Modifier.weight(1f)
                            )
                            CategoryDistributionChip(
                                label = "متسرع",
                                count = summary.fastCount,
                                tint = Color(0xFFE65100),
                                bg = ExamGoldLight,
                                modifier = Modifier.weight(1f)
                            )
                            CategoryDistributionChip(
                                label = "تجاوز الوقت",
                                count = summary.overtimeCount,
                                tint = AlertCoral,
                                bg = AlertCoralLight,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Executive Summary
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BioCyanLight.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BioCyanAccent.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "📊 التحليل التنفيذي للأداء:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = BioTealDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = summary.executiveSummaryArabic,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Thanweya Amma Exam Pacing Advice
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ExamGoldLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ExamGold.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = summary.thanweyaAmmaTip,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = Color(0xFF7A4100)
                                )
                            }
                        }
                    }

                    // Question-by-Question Breakdown
                    if (summary.questionRecords.isNotEmpty()) {
                        item {
                            Text(
                                text = "سجل توقيت الأسئلة المجابة:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        items(summary.questionRecords) { record ->
                            QuestionPacingItemRow(record)
                        }
                    }
                }

                // Close Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary)
                ) {
                    Text("متابعة التدريب (Resume Practice)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MetricChip(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            Text(text = value, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
private fun CategoryDistributionChip(
    label: String,
    count: Int,
    tint: Color,
    bg: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = bg,
        border = androidx.compose.foundation.BorderStroke(1.dp, tint.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$count", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = tint)
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp), color = tint)
        }
    }
}

@Composable
private fun QuestionPacingItemRow(record: QuestionPacingRecord) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "سؤال ${record.questionIndex + 1}: ${record.questionPrompt.take(45)}...",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
                if (record.scoreAchieved != null) {
                    Text(
                        text = "الدرجة: ${record.scoreAchieved}/${record.maxScore} • كلمات مفتاحية مفقودة: ${record.missingKeywordsCount}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (record.pacingCategory) {
                    PacingCategory.FAST -> ExamGoldLight
                    PacingCategory.OPTIMAL -> SuccessGreenLight
                    PacingCategory.OVERTIME -> AlertCoralLight
                }
            ) {
                Text(
                    text = "${record.timeSpentSeconds}ث",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = when (record.pacingCategory) {
                        PacingCategory.FAST -> Color(0xFFE65100)
                        PacingCategory.OPTIMAL -> BioSuccessGreen
                        PacingCategory.OVERTIME -> AlertCoral
                    },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
