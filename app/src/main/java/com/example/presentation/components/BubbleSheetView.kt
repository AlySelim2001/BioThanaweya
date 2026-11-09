package com.example.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
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
import com.example.domain.model.MinistryExamQuestion
import com.example.ui.theme.BioTealPrimary

@Composable
fun BubbleSheetView(
    questions: List<MinistryExamQuestion>,
    selectedOptions: Map<Int, Int>, // questionIndex -> 0..3
    flaggedQuestions: Set<Int>, // questionIndex set
    onOptionSelected: (questionIndex: Int, optionIndex: Int) -> Unit,
    onToggleFlag: (questionIndex: Int) -> Unit,
    onJumpToQuestion: (questionIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val mcqQuestions = remember(questions) {
        questions.filter { it.type == com.example.data.model.QuestionType.MCQ }
    }

    val answeredCount = remember(selectedOptions) {
        mcqQuestions.indices.count { selectedOptions.containsKey(it) }
    }
    val flaggedCount = flaggedQuestions.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Official Bubble Sheet Header Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, BioTealPrimary.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth().testTag("bubble_sheet_header_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = BioTealPrimary,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "ورقة إجابة البابل شيت الرسمية (Bubble Sheet)",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "تعليمات وزارة التربية والتعليم: التظليل التام بالقلم الرصاص",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Summary Stats Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BioTealPrimary.copy(alpha = 0.15f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$answeredCount / ${mcqQuestions.size}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BioTealPrimary
                                )
                                Text(
                                    text = "تم التظليل",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF57C00).copy(alpha = 0.15f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$flaggedCount",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFFF57C00)
                                )
                                Text(
                                    text = "معلم للمراجعة",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${mcqQuestions.size - answeredCount}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = "متبقي فارغ",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section label for Questions 1-32 (1 Mark)
        item {
            SectionHeaderBadge(
                title = "القسم الأول: الأسئلة الموضوعية (1 إلى 32) — درجة واحدة لكل سؤال",
                subtitle = "Section 1: Questions 1 to 32 (1 Mark Each)"
            )
        }

        // Questions 1 to 32
        items(mcqQuestions.take(32)) { q ->
            val index = q.number - 1
            BubbleSheetQuestionRow(
                question = q,
                questionIndex = index,
                selectedOption = selectedOptions[index],
                isFlagged = flaggedQuestions.contains(index),
                onOptionSelected = { opt -> onOptionSelected(index, opt) },
                onToggleFlag = { onToggleFlag(index) },
                onJumpToQuestion = { onJumpToQuestion(index) }
            )
        }

        // Section label for Questions 33-44 (2 Marks)
        item {
            SectionHeaderBadge(
                title = "القسم الثاني: الأسئلة ذات المستويات العليا (33 إلى 44) — درجتان لكل سؤال",
                subtitle = "Section 2: High-Order Questions 33 to 44 (2 Marks Each)"
            )
        }

        // Questions 33 to 44
        items(mcqQuestions.drop(32)) { q ->
            val index = q.number - 1
            BubbleSheetQuestionRow(
                question = q,
                questionIndex = index,
                selectedOption = selectedOptions[index],
                isFlagged = flaggedQuestions.contains(index),
                onOptionSelected = { opt -> onOptionSelected(index, opt) },
                onToggleFlag = { onToggleFlag(index) },
                onJumpToQuestion = { onJumpToQuestion(index) }
            )
        }

        // Essay Notice
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "الأسئلة المقالية (45 و 46) — 4 درجات",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "تُجاب في الجزء المخصص للأسئلة المقالية في ورقة الإجابة وليس على البابل شيت.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeaderBadge(title: String, subtitle: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = BioTealPrimary.copy(alpha = 0.12f),
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = BioTealPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun BubbleSheetQuestionRow(
    question: MinistryExamQuestion,
    questionIndex: Int,
    selectedOption: Int?,
    isFlagged: Boolean,
    onOptionSelected: (Int) -> Unit,
    onToggleFlag: () -> Unit,
    onJumpToQuestion: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFlagged) {
                Color(0xFFFFF8E1)
            } else if (selectedOption != null) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isFlagged) Color(0xFFFFA000) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bubble_row_${question.number}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Question number & mark pill
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (question.marks >= 2.0) Color(0xFF6A1B9A) else BioTealPrimary,
                    modifier = Modifier.size(width = 38.dp, height = 30.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${question.number}",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = if (question.marks >= 2.0) "2د" else "1د",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Flag Bookmark Button
                IconButton(
                    onClick = onToggleFlag,
                    modifier = Modifier.size(28.dp).testTag("bubble_flag_btn_${question.number}")
                ) {
                    Icon(
                        imageVector = if (isFlagged) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Flag for review",
                        tint = if (isFlagged) Color(0xFFF57C00) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // 4 Bubbles (A, B, C, D / أ، ب، ج، د)
            val bubbleLabels = listOf(
                "أ" to "A",
                "ب" to "B",
                "ج" to "C",
                "د" to "D"
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                bubbleLabels.forEachIndexed { optIndex, (arLabel, enLabel) ->
                    val isSelected = selectedOption == optIndex

                    val bubbleBackground by animateColorAsState(
                        targetValue = if (isSelected) Color(0xFF263238) else Color.White,
                        animationSpec = tween(150),
                        label = "bubble_bg"
                    )

                    val bubbleBorderColor by animateColorAsState(
                        targetValue = if (isSelected) Color(0xFF212121) else Color(0xFF78909C),
                        animationSpec = tween(150),
                        label = "bubble_border"
                    )

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(bubbleBackground)
                            .border(width = if (isSelected) 2.5.dp else 1.5.dp, color = bubbleBorderColor, shape = CircleShape)
                            .clickable {
                                onOptionSelected(optIndex)
                            }
                            .testTag("bubble_${question.number}_opt_$optIndex"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            // Shaded graphite fill indicator
                            Text(
                                text = arLabel,
                                color = Color(0xFFEEEEEE),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        } else {
                            Text(
                                text = arLabel,
                                color = Color(0xFF37474F),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Jump to question in full view
            IconButton(
                onClick = onJumpToQuestion,
                modifier = Modifier.size(32.dp).testTag("bubble_view_question_${question.number}")
            ) {
                Icon(
                    Icons.Default.Visibility,
                    contentDescription = "عرض السؤال كاملاً",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
