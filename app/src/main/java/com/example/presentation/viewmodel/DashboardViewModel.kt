package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Chapter
import com.example.data.model.MasteryLevel
import com.example.data.model.SubTopic
import com.example.data.model.TopicMastery
import com.example.data.repository.BiologyCurriculumRepository
import com.example.domain.model.WellbeingProfile
import com.example.domain.repository.BiologyRepository
import com.example.domain.usecase.OrchestratorUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(
        val chapters: List<Chapter>,
        val subTopics: List<SubTopic>,
        val topicMasteries: List<TopicMastery>,
        val overallMasteryPercentage: Int,
        val wellbeingProfile: WellbeingProfile,
        val dailyTip: String,
        val suggestedTopicId: String
    ) : DashboardUiState
}

class DashboardViewModel @Inject constructor(
    private val orchestrator: OrchestratorUseCase,
    private val biologyRepository: BiologyRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            val chapters = BiologyCurriculumRepository.chapters
            val subTopics = BiologyCurriculumRepository.subTopics
            val masteries = BiologyCurriculumRepository.getTopicMasteries()
            val masteredCount = masteries.count { it.masteryLevel == MasteryLevel.MASTERED || it.masteryLevel == MasteryLevel.GOOD }
            val overallMastery = ((masteredCount.toDouble() / masteries.size.coerceAtLeast(1)) * 100).toInt()

            val profile = orchestrator.wellbeingAgent.getStudentProfile()
            val tip = orchestrator.wellbeingAgent.getAnxietyReliefTip(profile.examAnxietyScore)

            _uiState.value = DashboardUiState.Success(
                chapters = chapters,
                subTopics = subTopics,
                topicMasteries = masteries,
                overallMasteryPercentage = overallMastery,
                wellbeingProfile = profile,
                dailyTip = tip,
                suggestedTopicId = "sarcomere_contraction"
            )
        }
    }
}
