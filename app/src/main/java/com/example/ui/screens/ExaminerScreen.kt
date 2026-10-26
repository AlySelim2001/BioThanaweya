package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamGradingResult
import com.example.data.model.GradeStatus
import com.example.data.model.MinistryExamItem
import com.example.data.repository.BiologyCurriculumRepository
import com.example.domain.MinistryExaminerEngine
import com.example.ui.components.AgentHeaderBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExaminerScreen(
    initialTopicId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    // Collect all available exam questions across study packs
    val examQuestions = remember {
        listOf(
            BiologyCurriculumRepository.packSarcomereContraction.examSimulationItem,
            BiologyCurriculumRepository.packDnaStructureReplication.examSimulationItem,
            BiologyCurriculumRepository.packImmunityHumoralCellular.examSimulationItem,
            BiologyCurriculumRepository.packHumanReproductionMenstrual.examSimulationItem,
            BiologyCurriculumRepository.packRnaTranscriptionTranslation.examSimulationItem,
            BiologyCurriculumRepository.packPlantHumanSupport.examSimulationItem,
            BiologyCurriculumRepository.packHormonalCoordination.examSimulationItem,
            BiologyCurriculumRepository.packReproductionDoubleFertilization.examSimulationItem,
            BiologyCurriculumRepository.packGeneticEngineering.examSimulationItem
        )
    }

    // Find initial question matching the requested topic
    val initialItem = remember(initialTopicId) {
        val pack = BiologyCurriculumRepository.getMasterStudyPack(initialTopicId)
        pack.examSimulationItem
    }

    var selectedItem by remember { mutableStateOf(initialItem) }
    var studentAnswer by remember { mutableStateOf("") }
    var gradingResult by remember { mutableStateOf<ExamGradingResult?>(null) }
    var showModelAnswer by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Ministry Exam Grader",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Egyptian Thanaweya Amma Mark Scheme",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("examiner_back_button")) {
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
                AgentHeaderBadge(
                    agentNumber = 4,
                    agentTitle = "Thanaweya Amma Examiner Agent",
                    agentRole = "Official Mark Scheme Simulation • Keyword Detection • Grading Rubrics"
                )
            }

            // Question Selector Carousel
            item {
                Text(
                    text = "Select Official Exam Question:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(examQuestions) { item ->
                        val isSelected = item.id == selectedItem.id
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedItem = item
                                studentAnswer = ""
                                gradingResult = null
                                showModelAnswer = false
                            },
                            label = {
                                Text(
                                    text = item.prompt.take(30) + "...",
                                    fontSize = 11.sp
                                )
                            },
                            leadingIcon = {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            }
                        )
                    }
                }
            }

            // Question Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = AlertCoral.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = selectedItem.questionType.label.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AlertCoral,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "Max Score: ${selectedItem.maxScore} Marks",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = selectedItem.prompt,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "💡 Advice from Ministry Examiner: Write full physiological steps including ions, enzymes, or cell types. Partial credit is awarded based on exact keywords.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Student Answer Input Field
            item {
                Text(
                    text = "Your Answer (Type in English):",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = studentAnswer,
                    onValueChange = { studentAnswer = it },
                    placeholder = {
                        Text(
                            "Type your biological reasoning here as if sitting for the Egyptian Thanaweya Amma exam...",
                            fontSize = 13.sp
                        )
                    },
                    minLines = 4,
                    maxLines = 8,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("student_answer_input")
                )
            }

            // Grade Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            gradingResult = MinistryExaminerEngine.gradeStudentAnswer(
                                examItem = selectedItem,
                                studentAnswer = studentAnswer
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                        shape = RoundedCornerShape(12.dp),
                        enabled = studentAnswer.isNotBlank(),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("submit_grading_button")
                    ) {
                        Icon(Icons.Default.Grading, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Grade My Answer", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showModelAnswer = !showModelAnswer },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("toggle_model_answer_button")
                    ) {
                        Text(if (showModelAnswer) "Hide Answer" else "Model Answer", fontSize = 12.sp)
                    }
                }
            }

            // Live Grading Evaluation Report
            if (gradingResult != null) {
                val result = gradingResult!!
                item {
                    val statusColor = when (result.gradeStatus) {
                        GradeStatus.FULL_MARK -> SuccessGreen
                        GradeStatus.PARTIAL_CREDIT -> ExamGold
                        GradeStatus.NEEDS_REVISION -> AlertCoral
                    }

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, statusColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = when (result.gradeStatus) {
                                            GradeStatus.FULL_MARK -> Icons.Default.CheckCircle
                                            GradeStatus.PARTIAL_CREDIT -> Icons.Default.Warning
                                            GradeStatus.NEEDS_REVISION -> Icons.Default.Cancel
                                        },
                                        contentDescription = null,
                                        tint = statusColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = when (result.gradeStatus) {
                                            GradeStatus.FULL_MARK -> "FULL MARK"
                                            GradeStatus.PARTIAL_CREDIT -> "PARTIAL CREDIT"
                                            GradeStatus.NEEDS_REVISION -> "NEEDS REVISION"
                                        },
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = statusColor
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(statusColor.copy(alpha = 0.15f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${result.scoreAwarded} / ${result.maxScore} Marks",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = statusColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Keyword Analysis Checklist
                            Text(
                                text = "Ministry Mark Scheme Keyword Analysis:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            // Detected
                            if (result.detectedKeywords.isNotEmpty()) {
                                Text(
                                    text = "✅ Detected Scientific Keywords:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    result.detectedKeywords.forEach { kw ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = SuccessGreenLight
                                        ) {
                                            Text(
                                                text = "✓ $kw",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = SuccessGreen,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // Missing
                            if (result.missingKeywords.isNotEmpty()) {
                                Text(
                                    text = "⚠️ Missing Ministry Keywords (Strict Deduction!):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AlertCoral
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    result.missingKeywords.forEach { kw ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = AlertCoralLight
                                        ) {
                                            Text(
                                                text = "✗ $kw",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = AlertCoral,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            // Feedback Text
                            Text(
                                text = "Examiner Feedback:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = result.examinerFeedback,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Model Answer Toggle Display
            if (showModelAnswer) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = BioTealPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Official Ministry Model Answer (نموذج الإجابة الرسمي):",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = selectedItem.modelAnswer,
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}
