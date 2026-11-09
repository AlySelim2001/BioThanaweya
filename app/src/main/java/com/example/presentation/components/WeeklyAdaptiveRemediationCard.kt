package com.example.presentation.components

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.engine.RemedialDayTask
import com.example.domain.engine.RemedialTaskType
import com.example.domain.engine.WeeklyRemediationPlan
import com.example.ui.theme.*

@Composable
fun WeeklyAdaptiveRemediationCard(
    plan: WeeklyRemediationPlan,
    onToggleTask: (Int) -> Unit,
    onNavigateToTopic: (String) -> Unit,
    onNavigateToPractice: (String) -> Unit,
    onNavigateToFsrs: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .testTag("weekly_remediation_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(BioTealPrimary, MolecularPurple))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AutoGraph,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "خطة المعالجة الأسبوعية التكيفية",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Diagnostic Weekly Remediation • Thanawya Amma",
                            style = MaterialTheme.typography.labelSmall,
                            color = BioTealPrimary
                        )
                    }
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Toggle Plan"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Score boost alert & diagnosis highlight
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = plan.overallStatusAr,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "أولوية التدخل: ${plan.weakestTopicTitleAr} (${plan.weakestTopicMasteryPct}% إتقان)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ExamGold.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ExamGold)
                    ) {
                        Text(
                            text = "+${plan.predictedScoreBoostMarks} درجة متوقعة",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar and completed count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الإنجاز الأسبوعي: ${plan.completedTasksCount} من ${plan.dailyTasks.size} أيام",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${(plan.completedTasksCount * 100 / plan.dailyTasks.size)}%",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = BioTealPrimary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { plan.completedTasksCount / plan.dailyTasks.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = BioTealPrimary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Animated Task Days List
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    plan.dailyTasks.forEach { task ->
                        DayTaskRow(
                            task = task,
                            onToggle = { onToggleTask(task.dayNumber) },
                            onNavigateToTopic = { onNavigateToTopic(task.topicId) },
                            onNavigateToPractice = { onNavigateToPractice(task.topicId) },
                            onNavigateToFsrs = { onNavigateToFsrs(task.topicId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayTaskRow(
    task: RemedialDayTask,
    onToggle: () -> Unit,
    onNavigateToTopic: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToFsrs: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (task.isCompleted) SuccessGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (task.isCompleted) SuccessGreen.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("remedial_task_day_${task.dayNumber}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = SuccessGreen,
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (task.isCompleted) SuccessGreen else BioTealPrimary
                ) {
                    Text(
                        text = "اليوم ${task.dayNumber} • ${task.dayNameAr}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = task.taskType.labelAr,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MolecularPurple.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "+${task.xpReward} XP",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MolecularPurple,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = task.titleAr,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = task.descriptionAr,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Action CTAs for the Day's mission
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⏱️ ${task.estimatedMinutes} دقيقة",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.weight(1f))

                when (task.taskType) {
                    RemedialTaskType.CONCEPT_CLARIFICATION,
                    RemedialTaskType.COMPARISON_SYNTHESIS -> {
                        OutlinedButton(
                            onClick = onNavigateToTopic,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("فتح الشرح", fontSize = 11.sp)
                        }
                    }
                    RemedialTaskType.FSRS_SPACED_REPETITION -> {
                        Button(
                            onClick = onNavigateToFsrs,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MolecularPurple),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("بطاقات FSRS", fontSize = 11.sp)
                        }
                    }
                    RemedialTaskType.FORMULA_CALCULATION,
                    RemedialTaskType.MINISTRY_TRAPS,
                    RemedialTaskType.TARGETED_QUIZ,
                    RemedialTaskType.MINI_SIMULATED_EXAM -> {
                        Button(
                            onClick = onNavigateToPractice,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("بدء التدريب", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
