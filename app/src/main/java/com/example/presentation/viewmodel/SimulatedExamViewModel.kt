package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.ExamAttemptEntity
import com.example.data.local.entity.ExamMistakeEntity
import com.example.data.model.*
import com.example.data.provider.MinistryOfficialExamsProvider
import com.example.domain.engine.MarkSchemeEngine
import com.example.domain.engine.MinistryExamScoringEngine
import com.example.domain.model.ExamDurationMode
import com.example.domain.model.ExamResultReport
import com.example.domain.model.MinistryExamFullReport
import com.example.domain.model.MinistryExamPaper
import com.example.domain.model.MinistryExamQuestion
import com.example.domain.repository.BiologyRepository
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ExamUiState {
    data class PaperSelection(
        val availablePapers: List<MinistryExamPaper>,
        val selectedPaperId: String,
        val durationMode: ExamDurationMode = ExamDurationMode.OFFICIAL_STANDARD,
        val activeTab: SelectionTab = SelectionTab.OFFICIAL_PAPERS
    ) : ExamUiState

    data class InProgress(
        val paper: MinistryExamPaper,
        val currentIndex: Int,
        val remainingSeconds: Long,
        val selectedOptions: Map<Int, Int>, // question index -> option index 0..3
        val essayAnswers: Map<Int, String>,  // question index -> student text
        val flaggedQuestions: Set<Int>,      // question indices
        val isBubbleSheetViewActive: Boolean = false,
        val isArabicPrimary: Boolean = true,
        val isTimeWarning: Boolean = false
    ) : ExamUiState

    data class Completed(
        val report: MinistryExamFullReport,
        val legacyReport: ExamResultReport
    ) : ExamUiState
}

enum class SelectionTab {
    OFFICIAL_PAPERS,
    CHAPTER_QUIZZES,
    MISTAKE_NOTEBOOK,
    PAST_ATTEMPTS
}

class SimulatedExamViewModel @Inject constructor(
    private val biologyRepository: BiologyRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExamUiState>(
        ExamUiState.PaperSelection(
            availablePapers = MinistryOfficialExamsProvider.availablePapers,
            selectedPaperId = MinistryOfficialExamsProvider.availablePapers.first().id,
            durationMode = ExamDurationMode.OFFICIAL_STANDARD,
            activeTab = SelectionTab.OFFICIAL_PAPERS
        )
    )
    val uiState: StateFlow<ExamUiState> = _uiState.asStateFlow()

    val pastAttempts: StateFlow<List<ExamAttemptEntity>> = biologyRepository?.getExamAttempts()
        ?.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        ?: MutableStateFlow(emptyList())

    val unresolvedMistakes: StateFlow<List<ExamMistakeEntity>> = biologyRepository?.getUnresolvedMistakes()
        ?.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        ?: MutableStateFlow(emptyList())

    val unresolvedMistakesCount: StateFlow<Int> = biologyRepository?.getUnresolvedMistakesCount()
        ?.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
        ?: MutableStateFlow(0)

    private var timerJob: Job? = null
    private var examDurationSeconds: Long = 180 * 60 // Default 3 hours

    fun setSelectionTab(tab: SelectionTab) {
        val current = _uiState.value as? ExamUiState.PaperSelection ?: return
        _uiState.value = current.copy(activeTab = tab)
    }

    fun selectPaper(paperId: String) {
        val current = _uiState.value as? ExamUiState.PaperSelection ?: return
        _uiState.value = current.copy(selectedPaperId = paperId)
    }

    fun setDurationMode(mode: ExamDurationMode) {
        val current = _uiState.value as? ExamUiState.PaperSelection ?: return
        _uiState.value = current.copy(durationMode = mode)
    }

    fun startMinistryExam(paperId: String? = null, durationMode: ExamDurationMode? = null) {
        timerJob?.cancel()
        val currentSelect = _uiState.value as? ExamUiState.PaperSelection
        val targetPaperId = paperId ?: currentSelect?.selectedPaperId ?: MinistryOfficialExamsProvider.availablePapers.first().id
        val targetMode = durationMode ?: currentSelect?.durationMode ?: ExamDurationMode.OFFICIAL_STANDARD

        val paper = MinistryOfficialExamsProvider.getPaperById(targetPaperId)
        startExamWithPaper(paper, targetMode.minutes * 60L)
    }

    fun startChapterQuiz(chapterKey: String, chapterTitleArabic: String) {
        timerJob?.cancel()
        val quizPaper = MinistryOfficialExamsProvider.createChapterQuizPaper(chapterKey, chapterTitleArabic)
        startExamWithPaper(quizPaper, quizPaper.durationMinutes * 60L)
    }

    fun startMistakeRemediationQuiz(mistakes: List<ExamMistakeEntity>) {
        if (mistakes.isEmpty()) return
        timerJob?.cancel()
        val questionNumbers = mistakes.map { it.questionNumber }.toSet()
        val remediationPaper = MinistryOfficialExamsProvider.createMistakeReviewPaper(questionNumbers)
        startExamWithPaper(remediationPaper, remediationPaper.durationMinutes * 60L)
    }

    private fun startExamWithPaper(paper: MinistryExamPaper, durationSeconds: Long) {
        examDurationSeconds = durationSeconds

        _uiState.value = ExamUiState.InProgress(
            paper = paper,
            currentIndex = 0,
            remainingSeconds = examDurationSeconds,
            selectedOptions = emptyMap(),
            essayAnswers = emptyMap(),
            flaggedQuestions = emptySet(),
            isBubbleSheetViewActive = false,
            isArabicPrimary = true,
            isTimeWarning = false
        )

        timerJob = viewModelScope.launch {
            var timeLeft = examDurationSeconds
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1
                val curr = _uiState.value
                if (curr is ExamUiState.InProgress) {
                    _uiState.value = curr.copy(
                        remainingSeconds = timeLeft,
                        isTimeWarning = timeLeft <= 300 // 5 minutes remaining
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

    fun toggleBubbleSheetView() {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        _uiState.value = current.copy(isBubbleSheetViewActive = !current.isBubbleSheetViewActive)
    }

    fun toggleLanguageDirection() {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        _uiState.value = current.copy(isArabicPrimary = !current.isArabicPrimary)
    }

    fun toggleFlagQuestion(questionIndex: Int) {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        val updatedFlags = current.flaggedQuestions.toMutableSet()
        if (updatedFlags.contains(questionIndex)) {
            updatedFlags.remove(questionIndex)
        } else {
            updatedFlags.add(questionIndex)
        }
        _uiState.value = current.copy(flaggedQuestions = updatedFlags)
    }

    fun onOptionSelected(optionIndex: Int) {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        val updatedOptions = current.selectedOptions.toMutableMap()
        updatedOptions[current.currentIndex] = optionIndex
        _uiState.value = current.copy(selectedOptions = updatedOptions)
    }

    fun onOptionSelectedForIndex(questionIndex: Int, optionIndex: Int) {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        val updatedOptions = current.selectedOptions.toMutableMap()
        updatedOptions[questionIndex] = optionIndex
        _uiState.value = current.copy(selectedOptions = updatedOptions)
    }

    fun onAnswerTextChange(text: String) {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        val updatedAnswers = current.essayAnswers.toMutableMap()
        updatedAnswers[current.currentIndex] = text
        _uiState.value = current.copy(essayAnswers = updatedAnswers)
    }

    fun nextQuestion() {
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        if (current.currentIndex < current.paper.questions.size - 1) {
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
        if (index in current.paper.questions.indices) {
            _uiState.value = current.copy(currentIndex = index, isBubbleSheetViewActive = false)
        }
    }

    fun submitExam() {
        timerJob?.cancel()
        val current = _uiState.value as? ExamUiState.InProgress ?: return
        val paper = current.paper
        val timeSpent = examDurationSeconds - current.remainingSeconds

        val fullReport = MinistryExamScoringEngine.gradeExamSession(
            paper = paper,
            selectedOptions = current.selectedOptions,
            essayAnswers = current.essayAnswers,
            timeSpentSeconds = timeSpent
        )

        // Generate backward compatible legacy report for any secondary views
        val legacyReport = ExamResultReport(
            totalScore = fullReport.totalScoreAwarded,
            maxScore = fullReport.maxScore,
            percentage = fullReport.percentage.toInt(),
            letterGrade = fullReport.ministryGradeLabelEnglish,
            passed = fullReport.percentage >= 50.0,
            timeSpentSeconds = timeSpent,
            chapterBreakdown = mapOf(
                "Unit 1: Structure & Function" to (fullReport.unitBreakdowns.getOrNull(0)?.scoredMarks ?: 0.0),
                "Unit 2: Molecular Biology" to (fullReport.unitBreakdowns.getOrNull(1)?.scoredMarks ?: 0.0)
            ),
            questionsReviewed = emptyList<Any>()
        )

        _uiState.value = ExamUiState.Completed(
            report = fullReport,
            legacyReport = legacyReport
        )

        // Asynchronously save attempt and mistakes to Room Database
        viewModelScope.launch {
            try {
                val attemptId = biologyRepository?.recordExamAttempt(
                    ExamAttemptEntity(
                        paperId = paper.id,
                        paperTitle = paper.arabicTitle,
                        totalQuestions = paper.questions.size,
                        correctAnswers = fullReport.questionSummaries.count { it.isCorrect },
                        totalMarks = fullReport.totalScoreAwarded,
                        maxPossibleMarks = fullReport.maxScore,
                        percentage = fullReport.percentage,
                        passed = fullReport.percentage >= 50.0,
                        durationSeconds = timeSpent,
                        fallenTrapsCount = fullReport.fallenTraps.size
                    )
                ) ?: 0L

                // Extract mistakes and fallen ministry traps
                val mistakeEntities = mutableListOf<ExamMistakeEntity>()
                for ((trapQ, trapReason) in fullReport.fallenTraps) {
                    val qIndex = paper.questions.indexOfFirst { it.number == trapQ.number }
                    val selected = if (qIndex >= 0) current.selectedOptions[qIndex] ?: -1 else -1
                    mistakeEntities.add(
                        ExamMistakeEntity(
                            attemptId = attemptId,
                            paperId = paper.id,
                            questionNumber = trapQ.number,
                            questionTextArabic = trapQ.questionArabic,
                            questionTextEnglish = trapQ.questionEnglish,
                            selectedOptionIndex = selected,
                            correctOptionIndex = trapQ.correctOptionIndex,
                            trapDescriptionArabic = trapReason,
                            trapDescriptionEnglish = trapQ.ministryTrapWarningEnglish,
                            explanationArabic = trapQ.scientificExplanationArabic,
                            isResolved = false
                        )
                    )
                }

                // Add incorrect questions not categorized as official traps
                for (summary in fullReport.questionSummaries) {
                    if (!summary.isCorrect && mistakeEntities.none { it.questionNumber == summary.question.number }) {
                        val q = paper.questions.find { it.number == summary.question.number }
                        if (q != null) {
                            mistakeEntities.add(
                                ExamMistakeEntity(
                                    attemptId = attemptId,
                                    paperId = paper.id,
                                    questionNumber = q.number,
                                    questionTextArabic = q.questionArabic,
                                    questionTextEnglish = q.questionEnglish,
                                    selectedOptionIndex = summary.studentSelectedOption,
                                    correctOptionIndex = q.correctOptionIndex,
                                    trapDescriptionArabic = q.ministryTrapWarningArabic,
                                    trapDescriptionEnglish = q.ministryTrapWarningEnglish,
                                    explanationArabic = q.scientificExplanationArabic,
                                    isResolved = false
                                )
                            )
                        }
                    }
                }

                if (mistakeEntities.isNotEmpty()) {
                    biologyRepository?.recordMistakes(mistakeEntities)
                }
            } catch (ignored: Exception) {}
        }
    }

    fun resolveMistake(mistakeId: Long) {
        viewModelScope.launch {
            try {
                biologyRepository?.resolveMistake(mistakeId)
            } catch (ignored: Exception) {}
        }
    }

    fun clearResolvedMistakes() {
        viewModelScope.launch {
            try {
                biologyRepository?.clearResolvedMistakes()
            } catch (ignored: Exception) {}
        }
    }

    fun resetExam() {
        timerJob?.cancel()
        _uiState.value = ExamUiState.PaperSelection(
            availablePapers = MinistryOfficialExamsProvider.availablePapers,
            selectedPaperId = MinistryOfficialExamsProvider.availablePapers.first().id,
            durationMode = ExamDurationMode.OFFICIAL_STANDARD,
            activeTab = SelectionTab.OFFICIAL_PAPERS
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
