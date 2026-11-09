package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.PracticeQuestion
import com.example.domain.engine.DetailedGradingResult
import com.example.domain.engine.PacingEfficiencyEngine
import com.example.domain.engine.WeightedKeyword
import com.example.domain.model.MetacognitiveAnalysis
import com.example.domain.model.PacingCategory
import com.example.domain.model.PracticePacingSummary
import com.example.domain.model.QuestionPacingRecord
import com.example.domain.usecase.OrchestratorUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

import javax.inject.Inject

sealed interface PracticeUiState {
    data object Loading : PracticeUiState
    data class Content(
        val questions: List<PracticeQuestion>,
        val currentQuestionIndex: Int = 0,
        val studentAnswer: String = "",
        val confidencePercent: Int = 70,
        val gradingResult: DetailedGradingResult? = null,
        val metacognitiveAnalysis: MetacognitiveAnalysis? = null,
        val showModelAnswer: Boolean = false,
        // Timer & Pacing Analytics State
        val currentQuestionSeconds: Int = 0,
        val totalSessionSeconds: Int = 0,
        val isTimerRunning: Boolean = true,
        val currentPacingCategory: PacingCategory = PacingCategory.FAST,
        val pacingRecords: List<QuestionPacingRecord> = emptyList(),
        val pacingSummary: PracticePacingSummary? = null,
        val showPacingSummaryDialog: Boolean = false
    ) : PracticeUiState
}

class PracticeViewModel @Inject constructor(
    private val orchestrator: OrchestratorUseCase,
    private val topicId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<PracticeUiState>(PracticeUiState.Loading)
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            val questions = orchestrator.assessmentAgent.getAssessmentItems(topicId)
            _uiState.value = PracticeUiState.Content(
                questions = questions,
                currentQuestionIndex = 0,
                currentQuestionSeconds = 0,
                totalSessionSeconds = 0,
                isTimerRunning = true,
                currentPacingCategory = PacingCategory.FAST
            )
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                val current = _uiState.value
                if (current is PracticeUiState.Content && current.isTimerRunning) {
                    val nextQSeconds = current.currentQuestionSeconds + 1
                    val nextTotalSeconds = current.totalSessionSeconds + 1
                    _uiState.value = current.copy(
                        currentQuestionSeconds = nextQSeconds,
                        totalSessionSeconds = nextTotalSeconds,
                        currentPacingCategory = PacingCategory.fromSeconds(nextQSeconds)
                    )
                }
            }
        }
    }

    fun toggleTimer() {
        val current = _uiState.value
        if (current is PracticeUiState.Content) {
            val newRunning = !current.isTimerRunning
            _uiState.value = current.copy(isTimerRunning = newRunning)
        }
    }

    fun pauseTimer() {
        val current = _uiState.value
        if (current is PracticeUiState.Content && current.isTimerRunning) {
            _uiState.value = current.copy(isTimerRunning = false)
        }
    }

    fun resumeTimer() {
        val current = _uiState.value
        if (current is PracticeUiState.Content && !current.isTimerRunning) {
            _uiState.value = current.copy(isTimerRunning = true)
        }
    }

    fun onAnswerChanged(text: String) {
        val current = _uiState.value
        if (current is PracticeUiState.Content) {
            _uiState.value = current.copy(studentAnswer = text)
        }
    }

    fun onConfidenceChanged(confidence: Int) {
        val current = _uiState.value
        if (current is PracticeUiState.Content) {
            _uiState.value = current.copy(confidencePercent = confidence)
        }
    }

    fun submitAnswer() {
        val current = _uiState.value
        if (current is PracticeUiState.Content && current.questions.isNotEmpty()) {
            val q = current.questions[current.currentQuestionIndex]

            // Convert question keywords into WeightedKeywords
            val keywords = listOf(
                WeightedKeyword("atp", 1.0, listOf("طاقه", "adenosine triphosphate")),
                WeightedKeyword("actin", 1.0, listOf("اكتين", "خيوط رفيعه")),
                WeightedKeyword("cross bridges", 1.0, listOf("روابط مستعرضه", "خطاطيف", "transverse hooks")),
                WeightedKeyword("detach", 1.0, listOf("انفصال", "فك الارتباط", "release")),
                WeightedKeyword("spasm", 1.0, listOf("شد عضلي", "تشنج"))
            )

            val grading = orchestrator.examinerAgent.gradeStudentSubmission(
                studentAnswer = current.studentAnswer,
                weightedKeywords = keywords,
                maxScore = 2.0,
                modelAnswer = q.officialAnswer
            )

            val meta = orchestrator.metacognitiveAgent.analyzeConfidenceAccuracy(
                confidencePercent = current.confidencePercent,
                isCorrect = grading.isFullMark
            )

            // Record question pacing record
            val pacingRecord = QuestionPacingRecord(
                questionIndex = current.currentQuestionIndex,
                questionPrompt = q.question,
                timeSpentSeconds = current.currentQuestionSeconds,
                targetSeconds = 90,
                pacingCategory = current.currentPacingCategory,
                scoreAchieved = grading.finalScore,
                maxScore = grading.maxScore,
                isFullMark = grading.isFullMark,
                missingKeywordsCount = grading.missingKeywords.size,
                detectedKeywordsCount = grading.detectedKeywords.size
            )

            val updatedRecords = current.pacingRecords + pacingRecord
            val summary = PacingEfficiencyEngine.analyzePacing(
                records = updatedRecords,
                totalSessionSeconds = current.totalSessionSeconds
            )

            _uiState.value = current.copy(
                gradingResult = grading,
                metacognitiveAnalysis = meta,
                showModelAnswer = true,
                pacingRecords = updatedRecords,
                pacingSummary = summary
            )
        }
    }

    fun nextQuestion() {
        val current = _uiState.value
        if (current is PracticeUiState.Content) {
            val nextIdx = (current.currentQuestionIndex + 1) % current.questions.size
            _uiState.value = current.copy(
                currentQuestionIndex = nextIdx,
                studentAnswer = "",
                gradingResult = null,
                metacognitiveAnalysis = null,
                showModelAnswer = false,
                currentQuestionSeconds = 0,
                currentPacingCategory = PacingCategory.FAST,
                isTimerRunning = true
            )
        }
    }

    fun openPacingSummary() {
        val current = _uiState.value
        if (current is PracticeUiState.Content) {
            val summary = PacingEfficiencyEngine.analyzePacing(
                records = current.pacingRecords,
                totalSessionSeconds = current.totalSessionSeconds
            )
            _uiState.value = current.copy(
                pacingSummary = summary,
                showPacingSummaryDialog = true
            )
        }
    }

    fun dismissPacingSummary() {
        val current = _uiState.value
        if (current is PracticeUiState.Content) {
            _uiState.value = current.copy(showPacingSummaryDialog = false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
