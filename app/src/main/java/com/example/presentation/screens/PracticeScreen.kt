package com.example.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.core.content.ContextCompat
import com.example.presentation.components.MetacognitiveCard
import com.example.presentation.components.SevenAgentBadge
import com.example.presentation.viewmodel.PracticeUiState
import com.example.presentation.viewmodel.PracticeViewModel
import com.example.ui.theme.*
import com.example.util.SpeechToTextHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    viewModel: PracticeViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Speech-to-Text Helper
    var isListening by remember { mutableStateOf(false) }
    var speechStatusText by remember { mutableStateOf("") }

    val speechHelper = remember {
        SpeechToTextHelper(
            context = context,
            onResult = { recognized ->
                viewModel.onAnswerChanged(recognized)
                isListening = false
                speechStatusText = "Speech recognized"
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
            speechStatusText = "Microphone permission required for voice input"
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
                            text = "Ministry Examiner & Mark Scheme",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Agents 3 + 4 + 5 • Grounded in Ministry Rubric",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("practice_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        when (val s = state) {
            is PracticeUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BioTealPrimary)
                }
            }
            is PracticeUiState.Content -> {
                if (s.questions.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No practice questions found for this topic.")
                    }
                } else {
                    val currentQ = s.questions[s.currentQuestionIndex]

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            SevenAgentBadge(
                                agentNumber = 4,
                                agentTitleArabic = "Ministry Examiner Agent",
                                agentRoleEnglish = "Mark Scheme Engine + Text Normalization + Penalty Flags"
                            )
                        }

                        // Question Header Card
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
                                                text = currentQ.type.label.uppercase(),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AlertCoral,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = "Question ${s.currentQuestionIndex + 1} of ${s.questions.size}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = currentQ.question,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Agent 5: Metacognitive Confidence Slider
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MolecularPurpleLight.copy(alpha = 0.5f)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MolecularPurple.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Psychology, contentDescription = null, tint = MolecularPurple)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Agent 5 — Metacognitive Confidence Calibration:",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = MolecularPurple
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "How confident are you before examiner grading? (${s.confidencePercent}%)",
                                        fontSize = 12.sp,
                                        color = DeepNavy
                                    )
                                    Slider(
                                        value = s.confidencePercent.toFloat(),
                                        onValueChange = { viewModel.onConfidenceChanged(it.toInt()) },
                                        valueRange = 0f..100f,
                                        steps = 9,
                                        colors = SliderDefaults.colors(thumbColor = MolecularPurple, activeTrackColor = MolecularPurple)
                                    )
                                }
                            }
                        }

                        // Student Input Field & Voice Button
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = s.studentAnswer,
                                    onValueChange = { viewModel.onAnswerChanged(it) },
                                    label = { Text("Write scientific answer (English or Arabic)...") },
                                    placeholder = { Text("Type answer or tap microphone to speak aloud...") },
                                    minLines = 4,
                                    maxLines = 8,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth().testTag("practice_answer_input")
                                )

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
                                        modifier = Modifier.testTag("practice_mic_btn")
                                    ) {
                                        Icon(
                                            imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                                            contentDescription = "Voice Input",
                                            tint = if (isListening) AlertCoral else BioTealPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(if (isListening) "Stop Listening" else "Speak Answer")
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

                        // Submit and Next Controls
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.submitAnswer() },
                                    colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                                    shape = RoundedCornerShape(12.dp),
                                    enabled = s.studentAnswer.isNotBlank(),
                                    modifier = Modifier.weight(1.4f).height(46.dp).testTag("practice_submit_btn")
                                ) {
                                    Icon(Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Grade via Mark Scheme", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { viewModel.nextQuestion() },
                                    colors = ButtonDefaults.buttonColors(containerColor = MolecularPurple),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).height(46.dp).testTag("practice_next_btn")
                                ) {
                                    Text("Next", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        // Evaluation Report
                        if (s.gradingResult != null) {
                            val result = s.gradingResult!!
                            item {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, if (result.isFullMark) BioSuccessGreen else AlertCoral),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = if (result.isFullMark) Icons.Default.CheckCircle else Icons.Default.Warning,
                                                    contentDescription = null,
                                                    tint = if (result.isFullMark) BioSuccessGreen else AlertCoral
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = if (result.isFullMark) "FULL MARK" else "PARTIAL CREDIT",
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (result.isFullMark) BioSuccessGreen else AlertCoral
                                                )
                                            }
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = (if (result.isFullMark) BioSuccessGreen else AlertCoral).copy(alpha = 0.15f)
                                            ) {
                                                Text(
                                                    text = "${result.finalScore} / ${result.maxScore}",
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = if (result.isFullMark) BioSuccessGreen else AlertCoral,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Detected vs Missing Keywords
                                        if (result.detectedKeywords.isNotEmpty()) {
                                            Text(
                                                text = "✓ Detected Key Terms: ${result.detectedKeywords.joinToString(", ")}",
                                                color = BioSuccessGreen,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        if (result.missingKeywords.isNotEmpty()) {
                                            Text(
                                                text = "✗ Missing Required Terms: ${result.missingKeywords.joinToString(", ")}",
                                                color = AlertCoral,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        // Penalty Flags
                                        if (result.triggeredPenalties.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = AlertCoral.copy(alpha = 0.1f),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(10.dp)) {
                                                    Text(
                                                        text = "⚠️ Ministry Trap Penalty Triggered:",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 12.sp,
                                                        color = AlertCoral
                                                    )
                                                    for (flag in result.triggeredPenalties) {
                                                        Text("• ${flag.warningEnglish}", fontSize = 11.sp, color = AlertCoral)
                                                    }
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text("Examiner Feedback:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(result.examinerFeedback, style = MaterialTheme.typography.bodySmall)

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Official Model Answer:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BioTealPrimary)
                                        Text(result.modelAnswer, style = MaterialTheme.typography.bodySmall, color = BioTealPrimary)
                                    }
                                }
                            }

                            // Metacognitive Evaluation Card
                            if (s.metacognitiveAnalysis != null) {
                                item {
                                    MetacognitiveCard(
                                        analysis = s.metacognitiveAnalysis
                                    )
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(30.dp)) }
                    }
                }
            }
        }
    }
}
