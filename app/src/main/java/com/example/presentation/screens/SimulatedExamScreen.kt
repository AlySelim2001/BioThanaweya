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
import com.example.data.local.entity.ExamAttemptEntity
import com.example.data.local.entity.ExamMistakeEntity
import com.example.data.model.QuestionType
import com.example.data.provider.MinistryOfficialExamsProvider
import com.example.domain.model.ExamDurationMode
import com.example.domain.model.MinistryExamPaper
import com.example.domain.model.MinistryExamQuestion
import com.example.presentation.components.BubbleSheetView
import com.example.presentation.viewmodel.ExamUiState
import com.example.presentation.viewmodel.SelectionTab
import com.example.presentation.viewmodel.SimulatedExamViewModel
import com.example.ui.theme.*
import com.example.util.SpeechToTextHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatedExamScreen(
    viewModel: SimulatedExamViewModel,
    onBack: () -> Unit,
    onNavigateToFormulaLab: () -> Unit = {},
    onNavigateToFsrs: () -> Unit = {},
    onNavigateToGlossary: () -> Unit = {},
    onNavigateToMistakeAnalytics: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val state by viewModel.uiState.collectAsState()
    val pastAttempts by viewModel.pastAttempts.collectAsState()
    val unresolvedMistakes by viewModel.unresolvedMistakes.collectAsState()
    val unresolvedMistakesCount by viewModel.unresolvedMistakesCount.collectAsState()
    val context = LocalContext.current
    var showSubmitConfirmDialog by remember { mutableStateOf(false) }

    // Speech-to-Text integration for essay questions
    var isListening by remember { mutableStateOf(false) }
    var speechStatusText by remember { mutableStateOf("") }

    val speechHelper = remember {
        SpeechToTextHelper(
            context = context,
            onResult = { recognized ->
                viewModel.onAnswerTextChange(recognized)
                isListening = false
                speechStatusText = "تم التعرف على الصوت بنجاح"
            },
            onPartialResult = { partial ->
                speechStatusText = "جارٍ الاستماع: $partial"
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
            speechStatusText = "جارٍ الاستماع... تحدث بوضوح"
            speechHelper.startListening("ar-EG")
        } else {
            speechStatusText = "إذن الميكروفون مطلوب للإجابة الصوتية"
        }
    }

    fun toggleVoiceInput() {
        if (isListening) {
            speechHelper.stopListening()
            isListening = false
            speechStatusText = "تم إيقاف التسجيل الصوتي"
        } else {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                isListening = true
                speechStatusText = "جارٍ الاستماع... تحدث بوضوح"
                speechHelper.startListening("ar-EG")
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
                            text = "محاكي امتحانات الثانوية العامة الرسمي",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        val subtitle = when (val s = state) {
                            is ExamUiState.InProgress -> s.paper.arabicTitle
                            is ExamUiState.Completed -> "تقرير النتيجة والشهادة الوزارية (60 درجة)"
                            is ExamUiState.PaperSelection -> "نماذج الامتحانات، اختبارات الفصول، ودفتر الأخطاء"
                        }
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1
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
                        // Timer Badge
                        val minutes = s.remainingSeconds / 60
                        val seconds = s.remainingSeconds % 60
                        val timerText = String.format("%02d:%02d", minutes, seconds)

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (s.isTimeWarning) AlertCoral else BioTealPrimary,
                            modifier = Modifier.padding(end = 6.dp).testTag("exam_timer_badge")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = timerText,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Toggle View: Question vs Bubble Sheet
                        IconButton(
                            onClick = { viewModel.toggleBubbleSheetView() },
                            modifier = Modifier.testTag("toggle_bubble_sheet_btn")
                        ) {
                            Icon(
                                imageVector = if (s.isBubbleSheetViewActive) Icons.Default.Description else Icons.Default.GridOn,
                                contentDescription = "Toggle Bubble Sheet Mode",
                                tint = BioTealPrimary
                            )
                        }

                        // Language Toggle: Arabic / English
                        IconButton(
                            onClick = { viewModel.toggleLanguageDirection() },
                            modifier = Modifier.testTag("toggle_exam_language_btn")
                        ) {
                            Icon(
                                Icons.Default.Translate,
                                contentDescription = "Toggle Language",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Submit Button
                        IconButton(
                            onClick = { showSubmitConfirmDialog = true },
                            modifier = Modifier.testTag("exam_submit_icon_btn")
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Submit Exam",
                                tint = BioSuccessGreen
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        when (val s = state) {
            is ExamUiState.PaperSelection -> {
                Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    TabRow(
                        selectedTabIndex = s.activeTab.ordinal,
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.testTag("exam_selection_tabs")
                    ) {
                        Tab(
                            selected = s.activeTab == SelectionTab.OFFICIAL_PAPERS,
                            onClick = { viewModel.setSelectionTab(SelectionTab.OFFICIAL_PAPERS) },
                            text = { Text("نماذج الوزارة", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(18.dp)) },
                            modifier = Modifier.testTag("tab_official_papers")
                        )
                        Tab(
                            selected = s.activeTab == SelectionTab.CHAPTER_QUIZZES,
                            onClick = { viewModel.setSelectionTab(SelectionTab.CHAPTER_QUIZZES) },
                            text = { Text("اختبارات الفصول", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp)) },
                            modifier = Modifier.testTag("tab_chapter_quizzes")
                        )
                        Tab(
                            selected = s.activeTab == SelectionTab.MISTAKE_NOTEBOOK,
                            onClick = { viewModel.setSelectionTab(SelectionTab.MISTAKE_NOTEBOOK) },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("دفتر الأخطاء", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    if (unresolvedMistakesCount > 0) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Surface(
                                            shape = CircleShape,
                                            color = AlertCoral
                                        ) {
                                            Text(
                                                text = "$unresolvedMistakesCount",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                            },
                            icon = { Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(18.dp)) },
                            modifier = Modifier.testTag("tab_mistake_notebook")
                        )
                        Tab(
                            selected = s.activeTab == SelectionTab.PAST_ATTEMPTS,
                            onClick = { viewModel.setSelectionTab(SelectionTab.PAST_ATTEMPTS) },
                            text = { Text("السجل (${pastAttempts.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(18.dp)) },
                            modifier = Modifier.testTag("tab_past_attempts")
                        )
                    }

                    when (s.activeTab) {
                        SelectionTab.OFFICIAL_PAPERS -> {
                            ExamPaperSelectionContent(
                                availablePapers = s.availablePapers,
                                selectedPaperId = s.selectedPaperId,
                                selectedDurationMode = s.durationMode,
                                onSelectPaper = { viewModel.selectPaper(it) },
                                onSelectDurationMode = { viewModel.setDurationMode(it) },
                                onStartExam = { viewModel.startMinistryExam() }
                            )
                        }

                        SelectionTab.CHAPTER_QUIZZES -> {
                            ChapterQuizzesTabContent(
                                onStartChapterQuiz = { key, title ->
                                    viewModel.startChapterQuiz(key, title)
                                }
                            )
                        }

                        SelectionTab.MISTAKE_NOTEBOOK -> {
                            MistakeNotebookTabContent(
                                mistakes = unresolvedMistakes,
                                onStartRemediation = {
                                    viewModel.startMistakeRemediationQuiz(unresolvedMistakes)
                                },
                                onResolveMistake = { id -> viewModel.resolveMistake(id) },
                                onClearResolved = { viewModel.clearResolvedMistakes() },
                                onOpenRadarAnalytics = onNavigateToMistakeAnalytics
                            )
                        }

                        SelectionTab.PAST_ATTEMPTS -> {
                            PastAttemptsTabContent(attempts = pastAttempts)
                        }
                    }
                }
            }

            is ExamUiState.InProgress -> {
                if (s.isBubbleSheetViewActive) {
                    BubbleSheetView(
                        questions = s.paper.questions,
                        selectedOptions = s.selectedOptions,
                        flaggedQuestions = s.flaggedQuestions,
                        onOptionSelected = { qIdx, optIdx ->
                            viewModel.onOptionSelectedForIndex(qIdx, optIdx)
                        },
                        onToggleFlag = { qIdx ->
                            viewModel.toggleFlagQuestion(qIdx)
                        },
                        onJumpToQuestion = { qIdx ->
                            viewModel.jumpToQuestion(qIdx)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    QuestionCardView(
                        paper = s.paper,
                        currentIndex = s.currentIndex,
                        selectedOptions = s.selectedOptions,
                        essayAnswers = s.essayAnswers,
                        flaggedQuestions = s.flaggedQuestions,
                        isArabicPrimary = s.isArabicPrimary,
                        isListening = isListening,
                        speechStatusText = speechStatusText,
                        onOptionSelected = { optIdx -> viewModel.onOptionSelected(optIdx) },
                        onAnswerTextChange = { txt -> viewModel.onAnswerTextChange(txt) },
                        onToggleVoiceInput = { toggleVoiceInput() },
                        onToggleFlag = { viewModel.toggleFlagQuestion(s.currentIndex) },
                        onNext = { viewModel.nextQuestion() },
                        onPrev = { viewModel.prevQuestion() },
                        onJumpToQuestion = { idx -> viewModel.jumpToQuestion(idx) },
                        onSubmitClick = { showSubmitConfirmDialog = true },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            is ExamUiState.Completed -> {
                MinistryOfficialCertificateView(
                    report = s.report,
                    onRetakeExam = { viewModel.resetExam() },
                    onNavigateToFormulaLab = onNavigateToFormulaLab,
                    onNavigateToFsrs = onNavigateToFsrs,
                    onNavigateToGlossary = onNavigateToGlossary,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitConfirmDialog) {
        val s = state
        val answeredCount = if (s is ExamUiState.InProgress) {
            s.selectedOptions.size + s.essayAnswers.filterValues { it.isNotBlank() }.size
        } else 0
        val totalQuestions = if (s is ExamUiState.InProgress) s.paper.questions.size else 46

        AlertDialog(
            onDismissRequest = { showSubmitConfirmDialog = false },
            title = {
                Text(
                    text = "تسليم ورقة الإجابة وإنهاء الامتحان",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "لقد قمت بالإجابة على $answeredCount من أصل $totalQuestions سؤالاً."
                    )
                    if (answeredCount < totalQuestions) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AlertCoral.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "تنبيه: يوجد ${totalQuestions - answeredCount} سؤالاً لم تقم بالإجابة عليها بعد. سيتم احتساب درجتها بصفر.",
                                color = AlertCoral,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    Text(
                        text = "هل أنت متأكد من رغبتك في تسليم ورقة الامتحان واستخراج الشهادة والتحليل الوزاري الرسمي وتوثيق الأخطاء والتريكات؟",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmDialog = false
                        viewModel.submitExam()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BioSuccessGreen),
                    modifier = Modifier.testTag("confirm_submit_exam_dialog_btn")
                ) {
                    Text("نعم، تسليم الامتحان")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirmDialog = false }) {
                    Text("العودة للمراجعة")
                }
            }
        )
    }
}

@Composable
private fun ExamPaperSelectionContent(
    availablePapers: List<MinistryExamPaper>,
    selectedPaperId: String,
    selectedDurationMode: ExamDurationMode,
    onSelectPaper: (String) -> Unit,
    onSelectDurationMode: (ExamDurationMode) -> Unit,
    onStartExam: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Specifications Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BioTealPrimary.copy(alpha = 0.08f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BioTealPrimary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = null,
                            tint = BioTealPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "المواصفات الفنية للورقة الامتحانية (60 درجة)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = BioTealPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "وفقاً لأحدث معايير المركز القومي للامتحانات والتقويم التربوي بجمهورية مصر العربية لمادة الأحياء للثانوية العامة ومدارس اللغات.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SpecPill("46 سؤالاً", "إجمالي الأسئلة")
                        SpecPill("60 درجة", "الدرجة الكلية")
                        SpecPill("بابل شيت", "تظليل رصاص 2B")
                    }
                }
            }
        }

        // Section: Choose Official Exam Paper
        item {
            Text(
                text = "اختر النموذج الامتحاني الوزاري:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        items(availablePapers) { paper ->
            val isSelected = paper.id == selectedPaperId

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) BioTealPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    if (isSelected) 2.dp else 1.dp,
                    if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectPaper(paper.id) }
                    .testTag("paper_card_${paper.id}")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectPaper(paper.id) }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = paper.arabicTitle,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = BioTealPrimary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = paper.session,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = BioTealPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = paper.descriptionArabic,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "⏱ 3 ساعات",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "🎯 60 درجة",
                                style = MaterialTheme.typography.labelSmall,
                                color = BioSuccessGreen
                            )
                            Text(
                                text = "📝 46 سؤالاً",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Section: Duration Mode
        item {
            Text(
                text = "نمط التوقيت والتحكم:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ExamDurationMode.entries.forEach { mode ->
                    val isSelected = mode == selectedDurationMode

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) BioTealPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            if (isSelected) 1.5.dp else 1.dp,
                            if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectDurationMode(mode) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onSelectDurationMode(mode) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = mode.arabicTitle,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = mode.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Start Button
        item {
            Button(
                onClick = onStartExam,
                colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_exam_btn")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "بدء جلسة الامتحان الرسمية",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ChapterQuizzesTabContent(
    onStartChapterQuiz: (String, String) -> Unit
) {
    val chapters = listOf(
        Triple("support_movement", "الدعامة والحركة في الكائنات الحية", "الدعامة الفسيولوجية والتركيبية، الهيكل العظمي، والانقباض العضلي والساركومير"),
        Triple("hormonal_coordination", "التنسيق الهرموني في الكائنات الحية", "الغدد الصماء، الهيبوثلاموس، الغدة النخامية، الأنسولين والجلوكاجون وتأثيراتها"),
        Triple("reproduction", "التكاثر في الكائنات الحية", "التكاثر اللاجنسي والجنسي، تعاقب الأجيال، والتكاثر في النباتات الزهرية والإنسان"),
        Triple("immunity", "المناعة في الكائنات الحية", "خطوط الدفاع، الأجسام المضادة، الخلايا الليمفاوية B وT، وآليات المناعة الخلطية والخلوية"),
        Triple("molecular_genetics", "البيولوجيا الجزيئية (DNA & RNA)", "الحمض النووي DNA، تضاعف المادة الوراثية، تخليق البروتين، وتطبيقات الهندسة الوراثية")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "اختبارات ذكية وتفاعلية مخصصة لكل فصل، توفر تدريباً مركزاً وسريعاً (20 دقيقة) على تريكات الامتحان الوزاري.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                    )
                }
            }
        }

        items(chapters) { (key, title, desc) ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().testTag("chapter_quiz_card_$key")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BioTealPrimary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "⏱ 20 دقيقة",
                                style = MaterialTheme.typography.labelSmall,
                                color = BioTealPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onStartChapterQuiz(key, title) },
                        colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("start_chapter_quiz_$key")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("بدء الاختبار الفصلي الذكي", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun MistakeNotebookTabContent(
    mistakes: List<ExamMistakeEntity>,
    onStartRemediation: () -> Unit,
    onResolveMistake: (Long) -> Unit,
    onClearResolved: () -> Unit,
    onOpenRadarAnalytics: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AlertCoral.copy(alpha = 0.08f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, AlertCoral.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = AlertCoral)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "دفتر الأخطاء والتريكات الوزارية المرصودة",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = AlertCoral
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "يتم حفظ كافة الأسئلة والتريكات التي أخطأت بها تلقائياً في قاعدة البيانات المحلية (Room). أعد تدريب نفسك عليها حتى تثبت المعلومة بنسبة 100%.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (mistakes.isNotEmpty()) {
                            Button(
                                onClick = onStartRemediation,
                                colors = ButtonDefaults.buttonColors(containerColor = AlertCoral),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().testTag("start_mistake_remediation_btn")
                            ) {
                                Icon(Icons.Default.Replay, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "بدء اختبار معالجة الأخطاء والتريكات (${mistakes.size} سؤالاً)",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = onOpenRadarAnalytics,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("open_radar_analytics_btn")
                        ) {
                            Icon(Icons.Default.Radar, contentDescription = null, tint = BioTealPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تحليل التريكات ومخطط الرادار (Radar Mastery)",
                                fontWeight = FontWeight.Bold,
                                color = BioTealPrimary
                            )
                        }
                    }
                }
            }
        }

        if (mistakes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.CheckCircleOutline,
                            contentDescription = null,
                            tint = BioSuccessGreen,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "رائع! لا توجد أخطاء مسجلة حالياً",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "أي سؤال تخطئ به في الامتحانات أو الكويزات سيظهر هنا فوراً لدراسة التريكة وإعادة اختباره.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(mistakes) { mistake ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().testTag("mistake_card_${mistake.id}")
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
                                    text = "سؤال رقم ${mistake.questionNumber}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AlertCoral,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            TextButton(
                                onClick = { onResolveMistake(mistake.id) },
                                modifier = Modifier.testTag("resolve_mistake_${mistake.id}")
                            ) {
                                Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("تم الاستيعاب", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = mistake.questionTextArabic,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, lineHeight = 22.sp)
                        )

                        if (mistake.trapDescriptionArabic.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AlertCoral.copy(alpha = 0.1f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Bolt, contentDescription = null, tint = AlertCoral, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "التريكة الوزارية: ${mistake.trapDescriptionArabic}",
                                        style = MaterialTheme.typography.labelSmall.copy(color = AlertCoral, fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }

                        if (mistake.explanationArabic.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "التفسير العلمي: ${mistake.explanationArabic}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PastAttemptsTabContent(
    attempts: List<ExamAttemptEntity>
) {
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (attempts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.AssignmentLate,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "لا توجد محاولات امتحانية سابقة مسجلة",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ابدأ جلستك الأولى من تبويب 'نماذج الوزارة' لتسجيل درجاتك ومتابعة تقدمك.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(attempts) { attempt ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().testTag("attempt_card_${attempt.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = attempt.paperTitle,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (attempt.passed) BioSuccessGreen.copy(alpha = 0.12f) else AlertCoral.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = if (attempt.passed) "ناجح" else "راسب",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (attempt.passed) BioSuccessGreen else AlertCoral,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${attempt.totalMarks} / ${attempt.maxPossibleMarks.toInt()} درجة (${attempt.percentage.toInt()}%)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (attempt.passed) BioSuccessGreen else AlertCoral
                                )
                                Text(
                                    text = "${attempt.correctAnswers} إجابة صحيحة من أصل ${attempt.totalQuestions}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "⏱ ${attempt.durationSeconds / 60} دقيقة",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                                )
                                Text(
                                    text = dateFormat.format(Date(attempt.timestampEpoch)),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.SpecPill(title: String, label: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.weight(1f)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = BioTealPrimary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuestionCardView(
    paper: MinistryExamPaper,
    currentIndex: Int,
    selectedOptions: Map<Int, Int>,
    essayAnswers: Map<Int, String>,
    flaggedQuestions: Set<Int>,
    isArabicPrimary: Boolean,
    isListening: Boolean,
    speechStatusText: String,
    onOptionSelected: (Int) -> Unit,
    onAnswerTextChange: (String) -> Unit,
    onToggleVoiceInput: () -> Unit,
    onToggleFlag: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onJumpToQuestion: (Int) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val q = paper.questions[currentIndex]
    val isFlagged = flaggedQuestions.contains(currentIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Quick Jump Carousel Bar
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(paper.questions.size) { idx ->
                val isAnswered = selectedOptions.containsKey(idx) || !essayAnswers[idx].isNullOrBlank()
                val isCurrent = idx == currentIndex
                val isItemFlagged = flaggedQuestions.contains(idx)

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when {
                        isCurrent -> BioTealPrimary
                        isItemFlagged -> AlertCoral
                        isAnswered -> BioSuccessGreen.copy(alpha = 0.8f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    modifier = Modifier
                        .size(34.dp)
                        .clickable { onJumpToQuestion(idx) }
                        .testTag("jump_q_$idx")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${idx + 1}",
                            color = if (isCurrent || isItemFlagged || isAnswered) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Active Question Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header: Question number, type, marks, and flag button
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = BioTealPrimary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "سؤال ${currentIndex + 1} من ${paper.questions.size}",
                                    color = BioTealPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "${q.marks.toInt()} ${if (q.marks > 1.0) "درجات" else "درجة"}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = onToggleFlag,
                            modifier = Modifier.testTag("flag_question_btn")
                        ) {
                            Icon(
                                imageVector = if (isFlagged) Icons.Default.Flag else Icons.Default.OutlinedFlag,
                                contentDescription = "Flag question",
                                tint = if (isFlagged) AlertCoral else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Question Statement
                item {
                    Text(
                        text = if (isArabicPrimary) q.questionArabic else q.questionEnglish,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 26.sp
                        ),
                        modifier = Modifier.testTag("question_text")
                    )
                }

                // Options (for MCQ) or Text Input (for Essay)
                if (q.type == QuestionType.MCQ) {
                    val options = if (isArabicPrimary) q.optionsArabic else q.optionsEnglish
                    val chosen = selectedOptions[currentIndex]

                    items(options.size) { optIndex ->
                        val isSelected = chosen == optIndex
                        val optionLabel = when (optIndex) {
                            0 -> if (isArabicPrimary) "(أ)" else "(A)"
                            1 -> if (isArabicPrimary) "(ب)" else "(B)"
                            2 -> if (isArabicPrimary) "(ج)" else "(C)"
                            else -> if (isArabicPrimary) "(د)" else "(D)"
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) BioTealPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOptionSelected(optIndex) }
                                .testTag("option_${optIndex}_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = optionLabel,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = options[optIndex],
                                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                } else {
                    // Essay / Give Reason Section
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.EditNote,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "سؤال مقالي (درجتان): اكتب إجابتك أو استخدم الإملاء الصوتي.",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = essayAnswers[currentIndex].orEmpty(),
                                onValueChange = onAnswerTextChange,
                                placeholder = { Text("اكتب الإجابة العلمية الدقيقة مع ذكر المصطلحات الفسيولوجية المطلوبة...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 120.dp)
                                    .testTag("essay_answer_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Voice Input Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = onToggleVoiceInput,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(if (isListening) AlertCoral else BioTealPrimary)
                                        .testTag("essay_voice_btn")
                                ) {
                                    Icon(
                                        imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                                        contentDescription = "Voice Input",
                                        tint = Color.White
                                    )
                                }

                                if (speechStatusText.isNotBlank()) {
                                    Text(
                                        text = speechStatusText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isListening) AlertCoral else BioTealPrimary,
                                        modifier = Modifier.weight(1f).padding(start = 12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bottom Navigation Bar (Previous, Next, Submit)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onPrev,
                enabled = currentIndex > 0,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("exam_prev_btn")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("السابق")
            }

            if (currentIndex == paper.questions.size - 1) {
                Button(
                    onClick = onSubmitClick,
                    colors = ButtonDefaults.buttonColors(containerColor = BioSuccessGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("exam_final_submit_btn")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تسليم الامتحان (60 درجة)", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("exam_next_btn")
                ) {
                    Text("التالي")
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
