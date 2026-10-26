package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.PracticeQuestion
import com.example.domain.engine.DetailedGradingResult
import com.example.domain.engine.WeightedKeyword
import com.example.domain.model.MetacognitiveAnalysis
import com.example.domain.usecase.OrchestratorUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        val showModelAnswer: Boolean = false
    ) : PracticeUiState
}

class PracticeViewModel @Inject constructor(
    private val orchestrator: OrchestratorUseCase,
    private val topicId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<PracticeUiState>(PracticeUiState.Loading)
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            val questions = orchestrator.assessmentAgent.getAssessmentItems(topicId)
            _uiState.value = PracticeUiState.Content(
                questions = questions,
                currentQuestionIndex = 0
            )
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

            _uiState.value = current.copy(
                gradingResult = grading,
                metacognitiveAnalysis = meta,
                showModelAnswer = true
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
                showModelAnswer = false
            )
        }
    }
}
