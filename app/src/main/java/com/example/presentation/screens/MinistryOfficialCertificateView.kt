package com.example.presentation.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.MinistryExamFullReport
import com.example.domain.model.QuestionGradingSummary
import com.example.ui.theme.AlertCoral
import com.example.ui.theme.BioSuccessGreen
import com.example.ui.theme.BioTealPrimary

@Composable
fun MinistryOfficialCertificateView(
    report: MinistryExamFullReport,
    onRetakeExam: () -> Unit,
    onNavigateToFormulaLab: () -> Unit,
    onNavigateToFsrs: () -> Unit,
    onNavigateToGlossary: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedQuestionIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Official Ministry Certificate Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(2.dp, BioTealPrimary.copy(alpha = 0.6f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth().testTag("official_ministry_certificate_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Emblem & Ministry Heading
                    Surface(
                        shape = CircleShape,
                        color = BioTealPrimary.copy(alpha = 0.12f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = BioTealPrimary,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "جمهورية مصر العربية",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "وزارة التربية والتعليم والتعليم الفني",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "شهادة إتمام تقييم مادة الأحياء (60 درجة)",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        ),
                        color = BioTealPrimary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Score Display Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = BioTealPrimary.copy(alpha = 0.08f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BioTealPrimary.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Total Mark
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${report.totalScoreAwarded} / ${report.maxScore.toInt()}",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        fontSize = 28.sp
                                    ),
                                    color = if (report.percentage >= 85.0) BioSuccessGreen else BioTealPrimary
                                )
                                Text(
                                    text = "الدرجة الكلية الرسمية",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Divider(
                                modifier = Modifier
                                    .height(44.dp)
                                    .width(1.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            // Percentage & Percentile
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${report.percentage}%",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        fontSize = 28.sp
                                    ),
                                    color = if (report.percentage >= 85.0) BioSuccessGreen else BioTealPrimary
                                )
                                Text(
                                    text = "النسبة المئوية",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // National Percentile & Ministry Classification
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = report.ministryGradeLabelArabic,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "التقدير العام",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "أعلى ${report.nationalPercentile}%",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = BioSuccessGreen
                                )
                                Text(
                                    text = "الترتيب القومي المتوقع",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }

        // Unit Breakdown Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Analytics, contentDescription = null, tint = BioTealPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تحليل الأداء حسب وحدات المنهج الوزاري",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    report.unitBreakdowns.forEach { unitData ->
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = unitData.unitNameArabic,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${unitData.scoredMarks} / ${unitData.totalMarks.toInt()} (${unitData.percentage.toInt()}%)",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = if (unitData.percentage >= 80) BioSuccessGreen else AlertCoral
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { (unitData.percentage / 100.0).toFloat().coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = if (unitData.percentage >= 80) BioSuccessGreen else BioTealPrimary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Ministry Traps Encountered
        if (report.fallenTraps.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AlertCoral.copy(alpha = 0.08f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AlertCoral.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = AlertCoral)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "المصائد الوزارية المرصودة (${report.fallenTraps.size} مصائد)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = AlertCoral
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        report.fallenTraps.forEach { (q, trapWarning) ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, AlertCoral.copy(alpha = 0.2f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "سؤال رقم ${q.number} (${q.topicTitle})",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = trapWarning,
                                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Action Buttons Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onRetakeExam,
                    colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("cert_retake_exam_btn")
                ) {
                    Icon(Icons.Default.Replay, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("إعادة خوض الامتحان الرسمي", fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateToFormulaLab,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("cert_formula_lab_btn")
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("معمل المسائل", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onNavigateToFsrs,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("cert_fsrs_btn")
                    ) {
                        Icon(Icons.Default.Style, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("تثبيت الذاكرة FSRS", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onNavigateToGlossary,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("cert_glossary_btn")
                    ) {
                        Icon(Icons.Default.Translate, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("القاموس", fontSize = 12.sp)
                    }
                }
            }
        }

        // Itemized Questions Review
        item {
            Text(
                text = "مراجعة تفصيلية لجميع الأسئلة (46 سؤالاً)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(report.questionSummaries) { summary ->
            val isExpanded = expandedQuestionIndex == summary.question.number

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (summary.isCorrect) BioSuccessGreen.copy(alpha = 0.05f) else AlertCoral.copy(alpha = 0.05f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (summary.isCorrect) BioSuccessGreen.copy(alpha = 0.3f) else AlertCoral.copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (summary.isCorrect) BioSuccessGreen else AlertCoral,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (summary.isCorrect) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "سؤال ${summary.question.number} (${summary.scoreAwarded} / ${summary.maxScore.toInt()} درجة)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        IconButton(onClick = {
                            expandedQuestionIndex = if (isExpanded) null else summary.question.number
                        }) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Expand"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = summary.question.questionArabic,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        maxLines = if (isExpanded) Int.MAX_VALUE else 2
                    )

                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (summary.question.type == com.example.data.model.QuestionType.MCQ) {
                                Text(
                                    text = "إجابتك: ${if (summary.studentSelectedOption >= 0) summary.question.optionsArabic.getOrElse(summary.studentSelectedOption) { "لم يتم الاختيار" } else "فارغ"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (summary.isCorrect) BioSuccessGreen else AlertCoral
                                )
                                Text(
                                    text = "الإجابة الصحيحة: ${summary.question.modelAnswerArabic}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = BioSuccessGreen
                                )
                            } else {
                                Text(
                                    text = "إجابتك المسجلة: ${summary.studentEssayAnswer.ifBlank { "لم تُكتب إجابة" }}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (summary.isCorrect) BioSuccessGreen else AlertCoral
                                )
                                Text(
                                    text = "النموذج الوزاري: ${summary.question.modelAnswerArabic}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = BioSuccessGreen
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "التفسير العلمي: ${summary.question.scientificExplanationArabic}",
                                    style = MaterialTheme.typography.labelSmall.copy(lineHeight = 18.sp),
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
