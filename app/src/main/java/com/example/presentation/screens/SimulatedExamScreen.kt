package com.example.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.QuestionType
import com.example.presentation.viewmodel.ExamUiState
import com.example.presentation.viewmodel.SimulatedExamViewModel
import com.example.ui.theme.*
import com.example.util.SpeechToTextHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatedExamScreen(
    viewModel: SimulatedExamViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showSubmitConfirmDialog by remember { mutableStateOf(false) }

    // Speech-to-Text integration for open-ended questions
    var isListening by remember { mutableStateOf(false) }
    var speechStatusText by remember { mutableStateOf("") }

    val speechHelper = remember {
        SpeechToTextHelper(
            context = context,
            onResult = { recognized ->
                viewModel.onAnswerTextChange(recognized)
                isListening = false
                speechStatusText = "Speech recognized successfully"
            },
            onPartialResult = { partial ->
                speechStatusText = "Listening: $partial"
            },
            onError = { err ->
                isListening = false
                speechStatusText = err
            },
            onListeningStateChanged = { listening ->
                isListening = listening
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            speechHelper.destroy()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isListening = true
            speechStatusText = "Listening... Speak your answer"
            speechHelper.startListening("en-US")
        } else {
            speechStatusText = "Microphone permission is required for voice input"
        }
    }

    fun toggleVoiceInput() {
        if (isListening) {
            speechHelper.stopListening()
            isListening = false
            speechStatusText = "Voice input stopped"
        } else {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                isListening = true
                speechStatusText = "Listening... Speak clearly"
                speechHelper.startListening("en-US")
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Ministry Exam Simulator",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Official Review Questions • Units 1 & 2",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("simulated_exam_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    val s = state
                    if (s is ExamUiState.InProgress) {
                        val minutes = s.remainingSeconds / 60
                        val seconds = s.remainingSeconds % 60
                        val timerText = String.format("%02d:%02d", minutes, seconds)

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (s.isTimeWarning) AlertCoral else BioTealPrimary,
                            modifier = Modifier.padding(end = 12.dp).testTag("exam_timer_badge")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = timerText,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        when (val s = state) {
            is ExamUiState.Idle -> {
                // Instructions & Start Screen
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = BioTealPrimary.copy(alpha = 0.08f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BioTealPrimary.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.FactCheck,
                                        contentDescription = null,
                                        tint = BioTealPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Official Ministry Exam Format",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = BioTealPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "This simulation strictly adheres to the Egyptian Ministry of Education specifications for Thanaweya Amma (Language Schools). It covers core mechanisms across Units 1 & 2.",
                                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    text = "Exam Specifications:",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                ExamSpecRow(Icons.Default.FormatListNumbered, "15 Ministry Questions (MCQs, Scientific Terms, Give Reasons, What Happens If)")
                                ExamSpecRow(Icons.Default.Timer, "45 Minutes timed countdown with automatic submission")
                                ExamSpecRow(Icons.Default.Mic, "Voice-to-Text answering enabled for oral practice")
                                ExamSpecRow(Icons.Default.Rule, "Strict Ministry Mark Scheme engine with penalty flags")
                                ExamSpecRow(Icons.Default.AutoStories, "Grounded in official textbook review questions (pp. 21-22, 36-37, 77-81, 107-110, 148-151)")
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { viewModel.startExam() },
                            colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("start_exam_btn")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start Ministry Simulation", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            is ExamUiState.InProgress -> {
                val q = s.questions[s.currentIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    // Question Navigation Row
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        items(s.questions.size) { index ->
                            val isSelected = index == s.currentIndex
                            val isAnswered = if (s.questions[index].type == QuestionType.MCQ) {
                                s.selectedOptions.containsKey(index)
                            } else {
                                !s.studentAnswers[index].isNullOrBlank()
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> BioTealPrimary
                                            isAnswered -> BioSuccessGreen
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) DeepNavy else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { viewModel.jumpToQuestion(index) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = if (isSelected || isAnswered) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Question Header
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = AlertCoral.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                text = q.type.label.uppercase(),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AlertCoral,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = "Question ${s.currentIndex + 1} of ${s.questions.size}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = q.question,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Input or Options
                        if (q.type == QuestionType.MCQ) {
                            itemsIndexed(q.options) { optionIndex, optionText ->
                                val isSelected = s.selectedOptions[s.currentIndex] == optionIndex
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) BioTealPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.onOptionSelected(optionIndex) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { viewModel.onOptionSelected(optionIndex) },
                                            colors = RadioButtonDefaults.colors(selectedColor = BioTealPrimary)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = optionText,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = s.studentAnswers[s.currentIndex] ?: "",
                                        onValueChange = { viewModel.onAnswerTextChange(it) },
                                        label = { Text("Enter scientific answer...") },
                                        placeholder = { Text("Type here or tap microphone to speak your answer...") },
                                        minLines = 4,
                                        maxLines = 8,
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.fillMaxWidth().testTag("exam_answer_input")
                                    )

                                    // Voice Answering Control Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        OutlinedButton(
                                            onClick = { toggleVoiceInput() },
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = if (isListening) AlertCoral else BioTealPrimary
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.testTag("exam_mic_btn")
                                        ) {
                                            Icon(
                                                imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                                                contentDescription = "Voice Input",
                                                tint = if (isListening) AlertCoral else BioTealPrimary
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(if (isListening) "Stop Listening" else "Answer by Voice")
                                        }

                                        if (speechStatusText.isNotBlank()) {
                                            Text(
                                                text = speechStatusText,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (isListening) AlertCoral else MaterialTheme.colorScheme.outline,
                                                modifier = Modifier.weight(1f).padding(start = 8.dp),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Bottom Navigation Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.prevQuestion() },
                            enabled = s.currentIndex > 0,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(46.dp)
                        ) {
                            Text("Previous")
                        }

                        if (s.currentIndex < s.questions.size - 1) {
                            Button(
                                onClick = { viewModel.nextQuestion() },
                                colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).height(46.dp).testTag("exam_next_btn")
                            ) {
                                Text("Next")
                            }
                        } else {
                            Button(
                                onClick = { showSubmitConfirmDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = AlertCoral),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).height(46.dp).testTag("exam_submit_btn")
                            ) {
                                Text("Submit Exam", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Confirmation Dialog
                if (showSubmitConfirmDialog) {
                    AlertDialog(
                        onDismissRequest = { showSubmitConfirmDialog = false },
                        title = { Text("Submit Ministry Exam?") },
                        text = {
                            Text("Are you sure you want to finish and submit your answers? The official Mark Scheme engine will grade your responses and calculate your percentage.")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showSubmitConfirmDialog = false
                                    viewModel.submitExam()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AlertCoral)
                            ) {
                                Text("Submit & View Results")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showSubmitConfirmDialog = false }) {
                                Text("Continue Exam")
                            }
                        }
                    )
                }
            }

            is ExamUiState.Completed -> {
                val report = s.report

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Result Summary Card
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (report.passed) BioSuccessGreen.copy(alpha = 0.1f) else AlertCoral.copy(alpha = 0.1f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (report.passed) BioSuccessGreen else AlertCoral
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (report.passed) "CONGRATULATIONS!" else "REVISION RECOMMENDED",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (report.passed) BioSuccessGreen else AlertCoral
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${report.percentage}%",
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (report.passed) BioSuccessGreen else AlertCoral
                                )
                                Text(
                                    text = "Score: ${report.totalScore.toInt()} / ${report.maxScore.toInt()} marks",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface
                                ) {
                                    Text(
                                        text = report.letterGrade,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        color = if (report.passed) BioSuccessGreen else AlertCoral
                                    )
                                }
                            }
                        }
                    }

                    // Chapter Breakdown
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Curriculum Domain Breakdown:",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                report.chapterBreakdown.forEach { (chapter, score) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(chapter, fontSize = 13.sp)
                                        Text("${score.toInt()} pts", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BioTealPrimary)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Question-by-Question Mark Scheme Review:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Itemized Review List
                    itemsIndexed(report.questionsReviewed) { index, item ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (item.scoreAwarded > 0.0) BioSuccessGreen.copy(alpha = 0.4f) else AlertCoral.copy(alpha = 0.4f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Q${index + 1}: ${item.question.type.label}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "${item.scoreAwarded} / ${item.maxScore} marks",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.scoreAwarded > 0.0) BioSuccessGreen else AlertCoral
                                    )
                                }
                                Text(
                                    text = item.question.question,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                                )
                                Divider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                Text(
                                    text = "Your Answer: ${item.studentAnswer.ifBlank { "(No Answer Provided)" }}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Ministry Model Answer: ${item.modelAnswer}",
                                    fontSize = 12.sp,
                                    color = BioTealPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Feedback: ${item.feedback}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { viewModel.resetExam() },
                            colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("retake_exam_btn")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retake Simulation", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExamSpecRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = BioTealPrimary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
