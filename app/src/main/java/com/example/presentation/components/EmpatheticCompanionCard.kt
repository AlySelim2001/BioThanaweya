package com.example.presentation.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CompanionDiagnosticSession
import com.example.domain.model.StudentComprehensionLevel
import com.example.domain.model.StudentSentiment
import com.example.ui.theme.*

@Composable
fun EmpatheticCompanionCard(
    session: CompanionDiagnosticSession,
    onSentimentChanged: (StudentSentiment) -> Unit,
    onExportAnkiJson: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isMicroInterventionExpanded by remember { mutableStateOf(false) }
    var studentReflectionText by remember { mutableStateOf("") }
    var hasSubmittedReflection by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("companion_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, BioTealPrimary.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Agent 5 Identity & Diagnostic Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BioTealPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.SupportAgent,
                        contentDescription = "Empathetic Companion",
                        tint = BioTealPrimary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "الرفيق التعليمي المتكيف (Agent 5)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Empathetic Student Companion & Formative Tracker",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                // Comprehension Badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = when (session.detectedComprehensionLevel) {
                        StudentComprehensionLevel.NOVICE -> AlertCoral.copy(alpha = 0.15f)
                        StudentComprehensionLevel.PROFICIENT -> ExamGold.copy(alpha = 0.2f)
                        StudentComprehensionLevel.MASTERED -> BioTealPrimary.copy(alpha = 0.2f)
                    }
                ) {
                    Text(
                        text = when (session.detectedComprehensionLevel) {
                            StudentComprehensionLevel.NOVICE -> "🌱 مبتدئ"
                            StudentComprehensionLevel.PROFICIENT -> "⭐ متمكن"
                            StudentComprehensionLevel.MASTERED -> "🏆 خبير"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (session.detectedComprehensionLevel) {
                            StudentComprehensionLevel.NOVICE -> AlertCoral
                            StudentComprehensionLevel.PROFICIENT -> DeepNavy
                            StudentComprehensionLevel.MASTERED -> BioTealPrimary
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sentiment Mood Bar: How do you feel right now?
            Text(
                text = "كيف تشعر تجاه هذا الدرس الآن؟",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StudentSentiment.entries.forEach { sentiment ->
                    val isSelected = session.initialSentiment == sentiment
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) BioTealPrimary.copy(alpha = 0.2f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                            .border(
                                width = if (isSelected) 2.dp else 0.5.dp,
                                color = if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSentimentChanged(sentiment) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("companion_sentiment_${sentiment.name}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = sentiment.emoji, fontSize = 16.sp)
                            Text(
                                text = sentiment.labelArabic.take(5),
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Empathetic Greeting & Pedagogical Baseline
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = null,
                            tint = AlertCoral,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "رسالة رفيقك التعليمي:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = session.empatheticGreeting,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Empathetic Check-in Question (Step 5 of Pipeline)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.QuestionAnswer,
                            contentDescription = null,
                            tint = MolecularPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "سؤال تفقد الفهم (Check-In):",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MolecularPurple
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = session.checkInQuestionArabic,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!hasSubmittedReflection) {
                        OutlinedTextField(
                            value = studentReflectionText,
                            onValueChange = { studentReflectionText = it },
                            placeholder = { Text("اكتب إجابتك أو انطباعك هنا...", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 2,
                            textStyle = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = {
                                if (studentReflectionText.isNotBlank()) {
                                    hasSubmittedReflection = true
                                    Toast.makeText(context, "أحسنت! تم تسجيل انطباعك التعليمي بنجاح.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                            modifier = Modifier.align(Alignment.End),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("تأكيد الفهم", fontSize = 11.sp)
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BioTealPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "شكراً لمشاركتك! استمرارك في التفكير الذاتي يعزز تثبيت المعلومة.",
                                fontSize = 11.sp,
                                color = BioTealPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Micro-Intervention Accordion
            OutlinedButton(
                onClick = { isMicroInterventionExpanded = !isMicroInterventionExpanded },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    if (isMicroInterventionExpanded) Icons.Default.ExpandLess else Icons.Default.Psychology,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isMicroInterventionExpanded) "إخفاء التدخل التدعيمي المصغر" else "عرض التدخل التدعيمي والتشبيه البديل",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            AnimatedVisibility(visible = isMicroInterventionExpanded) {
                val micro = session.targetedMicroIntervention
                Column(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = micro.titleArabic,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = BioTealPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "💡 التشبيه البديل: ${micro.auxiliaryAnalogy}",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "🎯 تدريب سريع: ${micro.correctiveDrillPrompt}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = MolecularPurple)
                    )
                    Text(
                        text = "نموذج الإجابة: ${micro.correctiveModelAnswer}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action: Export to Anki / Quizlet
            FilledTonalButton(
                onClick = onExportAnkiJson,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("تصدير بطاقات التكرار المتباعد (Anki JSON Export)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
