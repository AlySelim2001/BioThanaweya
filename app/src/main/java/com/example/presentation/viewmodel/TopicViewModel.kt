package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.MasterStudyPack
import com.example.domain.model.PblProject
import com.example.domain.usecase.OrchestratorUseCase
import com.example.domain.usecase.VisualAssets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

sealed interface TopicUiState {
    data object Loading : TopicUiState
    data class Success(
        val studyPack: MasterStudyPack,
        val visualAssets: VisualAssets,
        val pblProject: PblProject,
        val activeExplanationMode: String = "eli5" // "eli5" or "technical"
    ) : TopicUiState
    data class Error(val message: String) : TopicUiState
}

class TopicViewModel @Inject constructor(
    private val orchestrator: OrchestratorUseCase,
    private val topicId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<TopicUiState>(TopicUiState.Loading)
    val uiState: StateFlow<TopicUiState> = _uiState.asStateFlow()

    init {
        loadTopic()
    }

    fun loadTopic() {
        viewModelScope.launch {
            try {
                val pack = orchestrator.educatorAgent.getStudyPack(topicId)
                val visuals = orchestrator.visualAgent.getVisualAssets(topicId)
                val pbl = orchestrator.pblAgent.getProjectForTopic(topicId)

                _uiState.value = TopicUiState.Success(
                    studyPack = pack,
                    visualAssets = visuals,
                    pblProject = pbl
                )
            } catch (e: Exception) {
                _uiState.value = TopicUiState.Error(e.localizedMessage ?: "Failed to load topic")
            }
        }
    }

    fun setExplanationMode(mode: String) {
        val current = _uiState.value
        if (current is TopicUiState.Success) {
            _uiState.value = current.copy(activeExplanationMode = mode)
        }
    }

    val visualAgent get() = orchestrator.visualAgent
}
