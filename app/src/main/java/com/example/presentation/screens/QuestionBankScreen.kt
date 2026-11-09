package com.example.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.PracticeQuestion
import com.example.data.model.QuestionType
import com.example.data.model.UnitCategory
import com.example.data.repository.BiologyCurriculumRepository
import com.example.ui.theme.*
import com.example.util.SpeechToTextHelper
import com.example.util.TextToSpeechHelper

/**
 * شاشة بنك الأسئلة والتدريب الذكي (Question Bank Prototype)
 * تصميم متكامل وفق معايير Material Design 3 مع دعم كامل للغة العربية (RTL)
 * يتيح للطالب التدريب على أسئلة امتحانات الثانوية العامة مع التحقق الفوري،
 * كشف الفخاخ الوزارية، والدعم الصوتي (TTS / STT).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionBankScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allQuestions = remember { BiologyCurriculumRepository.getAllExamQuestions() }

    var selectedUnitFilter by remember { mutableStateOf<UnitCategory?>(null) }
    var selectedTypeFilter by remember { mutableStateOf<QuestionType?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredQuestions = remember(searchQuery, selectedUnitFilter, selectedTypeFilter, allQuestions) {
        allQuestions.filter { q ->
            val matchesType = selectedTypeFilter == null || q.type == selectedTypeFilter
            val matchesQuery = searchQuery.isBlank() ||
                    q.question.contains(searchQuery, ignoreCase = true) ||
                    q.officialAnswer.contains(searchQuery, ignoreCase = true) ||
                    q.rationale.contains(searchQuery, ignoreCase = true)
            matchesType && matchesQuery
        }
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    val currentQuestion = filteredQuestions.getOrNull(currentIndex)

    var selectedOptionIndex by remember(currentIndex) { mutableIntStateOf(-1) }
    var isSubmitted by remember(currentIndex) { mutableStateOf(false) }
    var essayAnswerText by remember(currentIndex) { mutableStateOf("") }
    var showModelAnswer by remember(currentIndex) { mutableStateOf(false) }

    // TTS & STT
    var isTtsSpeaking by remember { mutableStateOf(false) }
    val ttsHelper = remember {
        TextToSpeechHelper(
            context = context,
            onInitComplete = {},
            onSpeechStatusChange = { isTtsSpeaking = it }
        )
    }

    var isListening by remember { mutableStateOf(false) }
    val speechHelper = remember {
        SpeechToTextHelper(
            context = context,
            onResult = { recognized ->
                essayAnswerText = if (essayAnswerText.isBlank()) recognized else "$essayAnswerText $recognized"
                isListening = false
            },
            onPartialResult = {},
            onError = { isListening = false },
            onListeningStateChanged = { isListening = it }
        )
    }

    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            speechHelper.startListening("ar-EG")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            ttsHelper.shutdown()
            speechHelper.destroy()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "بنك الأسئلة والتدريب الذكي",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "تدريب تفاعلي وفق أحدث مواصفات الورقة الامتحانية الوزارية",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("btn_question_bank_back")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // شريط البحث وفلاتر الأسئلة
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            currentIndex = 0
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_qbank_search"),
                        placeholder = { Text("ابحث في بنك الأسئلة...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "بحث", tint = MaterialTheme.colorScheme.primary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = ""; currentIndex = 0 }) {
                                    Icon(Icons.Default.Close, contentDescription = "مسح")
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        )
                    )
                }

                // فلتر نوع السؤال
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedTypeFilter == null,
                                onClick = { selectedTypeFilter = null; currentIndex = 0 },
                                label = { Text("كل الأنواع (${allQuestions.size})") },
                                leadingIcon = if (selectedTypeFilter == null) {
                                    { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedTypeFilter == QuestionType.MCQ,
                                onClick = { selectedTypeFilter = QuestionType.MCQ; currentIndex = 0 },
                                label = { Text("اختيار من متعدد (MCQ)") },
                                leadingIcon = if (selectedTypeFilter == QuestionType.MCQ) {
                                    { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedTypeFilter == QuestionType.GIVE_REASON,
                                onClick = { selectedTypeFilter = QuestionType.GIVE_REASON; currentIndex = 0 },
                                label = { Text("علل / اذكر السبب") },
                                leadingIcon = if (selectedTypeFilter == QuestionType.GIVE_REASON) {
                                    { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedTypeFilter == QuestionType.WHAT_HAPPENS_IF,
                                onClick = { selectedTypeFilter = QuestionType.WHAT_HAPPENS_IF; currentIndex = 0 },
                                label = { Text("ماذا يحدث إذا") },
                                leadingIcon = if (selectedTypeFilter == QuestionType.WHAT_HAPPENS_IF) {
                                    { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedTypeFilter == QuestionType.DIAGRAM_ANALYSIS,
                                onClick = { selectedTypeFilter = QuestionType.DIAGRAM_ANALYSIS; currentIndex = 0 },
                                label = { Text("تحليل رسومات") },
                                leadingIcon = if (selectedTypeFilter == QuestionType.DIAGRAM_ANALYSIS) {
                                    { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                    }
                }

                // مؤشر التقدم والتنقل بين الأسئلة
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (filteredQuestions.isNotEmpty()) {
                                    "السؤال ${currentIndex + 1} من ${filteredQuestions.size}"
                                } else "لا توجد أسئلة مطابقة للبحث",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(
                                    onClick = {
                                        if (currentIndex > 0) currentIndex--
                                    },
                                    enabled = currentIndex > 0,
                                    modifier = Modifier.testTag("btn_qbank_prev")
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "السابق")
                                }

                                IconButton(
                                    onClick = {
                                        if (currentIndex < filteredQuestions.size - 1) currentIndex++
                                    },
                                    enabled = currentIndex < filteredQuestions.size - 1,
                                    modifier = Modifier.testTag("btn_qbank_next")
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "التالي")
                                }
                            }
                        }
                    }
                }

                // بطاقة السؤال التفاعلية
                if (currentQuestion != null) {
                    item {
                        InteractiveQuestionCard(
                            question = currentQuestion,
                            selectedOptionIndex = selectedOptionIndex,
                            onOptionSelected = { selectedOptionIndex = it },
                            isSubmitted = isSubmitted,
                            onSubmit = { isSubmitted = true },
                            essayAnswerText = essayAnswerText,
                            onEssayAnswerChange = { essayAnswerText = it },
                            showModelAnswer = showModelAnswer,
                            onToggleModelAnswer = { showModelAnswer = !showModelAnswer },
                            isListening = isListening,
                            onToggleListening = {
                                if (isListening) {
                                    speechHelper.stopListening()
                                    isListening = false
                                } else {
                                    val hasPermission = ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.RECORD_AUDIO
                                    ) == PackageManager.PERMISSION_GRANTED
                                    if (hasPermission) {
                                        isListening = true
                                        speechHelper.startListening("ar-EG")
                                    } else {
                                        recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                }
                            },
                            isTtsSpeaking = isTtsSpeaking,
                            onSpeakQuestion = {
                                if (isTtsSpeaking) {
                                    ttsHelper.stop()
                                } else {
                                    val textToSpeak = "${currentQuestion.question}. " +
                                            if (isSubmitted) "التفسير العلمي: ${currentQuestion.rationale}" else ""
                                    ttsHelper.speak(textToSpeak, isArabic = true)
                                }
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun InteractiveQuestionCard(
    question: PracticeQuestion,
    selectedOptionIndex: Int,
    onOptionSelected: (Int) -> Unit,
    isSubmitted: Boolean,
    onSubmit: () -> Unit,
    essayAnswerText: String,
    onEssayAnswerChange: (String) -> Unit,
    showModelAnswer: Boolean,
    onToggleModelAnswer: () -> Unit,
    isListening: Boolean,
    onToggleListening: () -> Unit,
    isTtsSpeaking: Boolean,
    onSpeakQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_question_card_${question.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    MaterialTheme.colorScheme.outlineVariant
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // شريط نوع السؤال والأدوات
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = when (question.type) {
                            QuestionType.MCQ -> "اختيار من متعدد (درجة)"
                            QuestionType.GIVE_REASON -> "سؤال مقالي - علل (درجتان)"
                            QuestionType.WHAT_HAPPENS_IF -> "سؤال مقالي - ماذا يحدث (درجتان)"
                            QuestionType.DIAGRAM_ANALYSIS -> "تحليل رسم ومخطط (درجتان)"
                            QuestionType.SCIENTIFIC_TERM -> "اكتب المصطلح العلمي (درجة)"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                IconButton(
                    onClick = onSpeakQuestion,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isTtsSpeaking) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                        contentDescription = "استماع للسؤال",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // نص السؤال الرئيسي
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // إذا كان السؤال اختيار من متعدد (MCQ)
            if (question.type == QuestionType.MCQ && question.options.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    question.options.forEachIndexed { index, optionText ->
                        val isSelected = selectedOptionIndex == index
                        val isCorrect = index == question.correctOptionIndex

                        val containerColor = when {
                            !isSubmitted -> if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                            isCorrect -> MaterialTheme.colorScheme.primaryContainer
                            isSelected -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        }

                        val borderColor = when {
                            !isSubmitted -> if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            isCorrect -> MaterialTheme.colorScheme.primary
                            isSelected -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                                .clickable(enabled = !isSubmitted) { onOptionSelected(index) }
                                .testTag("option_${index}"),
                            color = containerColor
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = if (!isSubmitted) { { onOptionSelected(index) } } else null,
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )

                                if (isSubmitted) {
                                    if (isCorrect) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "صحيحة", tint = MaterialTheme.colorScheme.primary)
                                    } else if (isSelected) {
                                        Icon(Icons.Default.Cancel, contentDescription = "خاطئة", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }

                    if (!isSubmitted) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onSubmit,
                            enabled = selectedOptionIndex != -1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_qbank_submit"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("تحقق من الإجابة", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            } else {
                // الأسئلة المقالية (Essay / Open-ended)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = essayAnswerText,
                        onValueChange = onEssayAnswerChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp)
                            .testTag("input_essay_answer"),
                        placeholder = { Text("اكتب إجابتك هنا أو استخدم التسجيل الصوتي للإملاء...") },
                        shape = RoundedCornerShape(14.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onToggleListening,
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    if (isListening) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(10.dp)
                                )
                        ) {
                            Icon(
                                imageVector = if (isListening) Icons.Default.Mic else Icons.Default.MicNone,
                                contentDescription = "إملاء صوتي",
                                tint = if (isListening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = onToggleModelAnswer,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_toggle_model_answer"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (showModelAnswer) "إخفاء الإجابة النموذجية" else "عرض الإجابة النموذجية وتوزيع الدرجات")
                        }
                    }
                }
            }

            // التفسير العلمي والفخاخ الوزارية (تظهر عند الإرسال أو عرض الإجابة النموذجية)
            AnimatedVisibility(
                visible = isSubmitted || showModelAnswer,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // بطاقة الإجابة والتفسير العلمي
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "التفسير العلمي المعتمد:",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = question.rationale.ifBlank { question.officialAnswer },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 19.sp
                            )
                        }
                    }

                    // بطاقة الفخ الامتحاني الوزاري (Exam Trap Warning)
                    if (question.ministryExamTip.isNotBlank()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f)
                            ),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.linearGradient(
                                    listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.outlineVariant)
                                )
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.WarningAmber, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "فخ امتحاني وزاري متكرر:",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = question.ministryExamTip,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
