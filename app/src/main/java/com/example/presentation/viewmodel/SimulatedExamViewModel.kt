package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.BiologyCurriculumRepository
import com.example.domain.engine.MarkSchemeEngine
import com.example.domain.repository.BiologyRepository
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ExamUiState {
    data object Idle : ExamUiState
    data class InProgress(
        val questions: List<PracticeQuestion>,
        val currentIndex: Int,
        val remainingSeconds: Long,
        val studentAnswers: Map<Int, String>,
        val selectedOptions: Map<Int, Int>,
        val isTimeWarning: Boolean = false
    ) : ExamUiState
    data class Completed(
        val report: ExamResultReport
    ) : ExamUiState
}

class SimulatedExamViewModel @Inject constructor(
    private val biologyRepository: BiologyRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExamUiState>(ExamUiState.Idle)
    val uiState: StateFlow<ExamUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var examDurationSeconds: Long = 45 * 60 // 45 minutes

    fun startExam(durationMinutes: Int = 45) {
        timerJob?.cancel()
        examDurationSeconds = durationMinutes * 60L
        val examQuestions = BiologyCurriculumRepository.getSimulatedMinistryExamPool()

        _uiState.value = ExamUiState.InProgress(
            questions = examQuestions,
            currentIndex = 0,
            remainingSeconds = examDurationSeconds,
            studentAnswers = emptyMap(),
            selectedOptions = emptyMap(),
            isTimeWarning = false
        )

        timerJob = viewModelScope.launch {
            var timeLeft = examDurationSeconds
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1
                val current = _uiState.value
                if (current is ExamUiState.InProgress) {
                    _uiState.value = current.copy(
                        remainingSeconds = timeLeft,
                        isTimeWarning = timeLeft <= 300 // 5 minutes left
                    )
                } else {
                    break
                }
            }
            if (_uiState.value is ExamUiState.InProgress) {
                submitExam()
            }
        }
    }

    fun onAnswerTextChange(text: String) {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        val updatedAnswers = current.studentAnswers.toMutableMap()
        updatedAnswers[current.currentIndex] = text
        _uiState.value = current.copy(studentAnswers = updatedAnswers)
    }

    fun onOptionSelected(optionIndex: Int) {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        val updatedOptions = current.selectedOptions.toMutableMap()
        updatedOptions[current.currentIndex] = optionIndex
        _uiState.value = current.copy(selectedOptions = updatedOptions)
    }

    fun nextQuestion() {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        if (current.currentIndex < current.questions.size - 1) {
            _uiState.value = current.copy(currentIndex = current.currentIndex + 1)
        }
    }

    fun prevQuestion() {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        if (current.currentIndex > 0) {
            _uiState.value = current.copy(currentIndex = current.currentIndex - 1)
        }
    }

    fun jumpToQuestion(index: Int) {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        if (index in current.questions.indices) {
            _uiState.value = current.copy(currentIndex = index)
        }
    }

    fun submitExam() {
        timerJob?.cancel()
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        val questions = current.questions
        val timeSpent = examDurationSeconds - current.remainingSeconds

        var totalScore = 0.0
        val maxTotalScore = questions.size * 2.0
        val submissions = mutableListOf<ExamQuestionSubmission>()
        val chapterScores = mutableMapOf<String, Pair<Double, Double>>() // earned, max

        for ((index, q) in questions.withIndex()) {
            val maxScore = 2.0
            var score = 0.0
            var feedback = ""
            var answerStr = ""

            if (q.type == QuestionType.MCQ) {
                val selected = current.selectedOptions[index] ?: -1
                answerStr = if (selected in q.options.indices) q.options[selected] else "No Answer"
                if (selected == q.correctOptionIndex) {
                    score = maxScore
                    feedback = "Correct! " + q.rationale
                } else {
                    score = 0.0
                    feedback = "Incorrect. Correct option: " + (if (q.correctOptionIndex in q.options.indices) q.options[q.correctOptionIndex] else "") + ". " + q.rationale
                }
            } else {
                answerStr = current.studentAnswers[index] ?: ""
                val keywords = q.officialAnswer
                    .split(" ", "(", ")", ",", ".", ";", "/")
                    .filter { it.length > 3 }
                    .map { com.example.domain.engine.WeightedKeyword(term = it, weight = 1.0) }

                val grading = MarkSchemeEngine.grade(
                    studentInput = answerStr,
                    weightedKeywords = keywords,
                    maxScore = maxScore,
                    modelAnswer = q.officialAnswer
                )
                score = grading.finalScore
                feedback = grading.examinerFeedback
            }

            totalScore += score
            submissions.add(
                ExamQuestionSubmission(
                    questionId = q.id,
                    question = q,
                    studentAnswer = answerStr,
                    selectedOptionIndex = current.selectedOptions[index] ?: -1,
                    scoreAwarded = score,
                    maxScore = maxScore,
                    feedback = feedback,
                    modelAnswer = q.officialAnswer
                )
            )
        }

        val percentage = ((totalScore / maxTotalScore) * 100).toInt().coerceIn(0, 100)
        val letterGrade = when {
            percentage >= 95 -> "A+ (Excellent — High Distinction)"
            percentage >= 85 -> "A (Very Good — Ministry Exam Ready)"
            percentage >= 75 -> "B (Good — Consolidate Molecular Bio)"
            percentage >= 60 -> "C (Pass — Needs Targeted Review)"
            else -> "Needs Intensive Revision"
        }

        val report = ExamResultReport(
            totalScore = totalScore,
            maxScore = maxTotalScore,
            percentage = percentage,
            letterGrade = letterGrade,
            passed = percentage >= 60,
            timeSpentSeconds = timeSpent,
            chapterBreakdown = mapOf(
                "Unit 1: Structure & Function" to (totalScore * 0.55),
                "Unit 2: Molecular Biology" to (totalScore * 0.45)
            ),
            questionsReviewed = submissions
        )

        _uiState.value = ExamUiState.Completed(report)

        // Asynchronously save attempt to Room Database & update XP
        viewModelScope.launch {
            try {
                biologyRepository?.recordExamAttempt(
                    com.example.data.local.entity.ExamAttemptEntity(
                        totalQuestions = questions.size,
                        correctAnswers = submissions.count { it.scoreAwarded >= 1.5 },
                        totalMarks = totalScore,
                        maxPossibleMarks = maxTotalScore,
                        percentage = percentage.toDouble(),
                        passed = percentage >= 60,
                        durationSeconds = timeSpent
                    )
                )
            } catch (ignored: Exception) {}
        }
    }

    fun resetExam() {
        timerJob?.cancel()
        _uiState.value = ExamUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
