package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Flashcard
import com.example.data.repository.BiologyCurriculumRepository
import com.example.domain.fsrs.*
import com.example.domain.usecase.OrchestratorUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class FsrsCardUiModel(
    val card: Flashcard,
    val fsrsState: FSRSCard
)

sealed interface FsrsUiState {
    data object Loading : FsrsUiState
    data class Reviewing(
        val queue: List<FsrsCardUiModel>,
        val currentIndex: Int = 0,
        val isFlipped: Boolean = false,
        val cardsReviewedToday: Int = 0,
        val averageStability: Double = 2.5
    ) : FsrsUiState
    data object AllDone : FsrsUiState
}

class FsrsReviewViewModel @Inject constructor(
    private val orchestrator: OrchestratorUseCase,
    private val topicId: String = "sarcomere_contraction"
) : ViewModel() {

    private val _uiState = MutableStateFlow<FsrsUiState>(FsrsUiState.Loading)
    val uiState: StateFlow<FsrsUiState> = _uiState.asStateFlow()

    init {
        loadDeck()
    }

    fun loadDeck() {
        viewModelScope.launch {
            val pack = BiologyCurriculumRepository.getMasterStudyPack(topicId)
            val queue = pack.flashcards.mapIndexed { idx, fc ->
                FsrsCardUiModel(
                    card = fc,
                    fsrsState = FSRSCard(cardId = fc.id, state = FSRSState.NEW)
                )
            }

            if (queue.isEmpty()) {
                _uiState.value = FsrsUiState.AllDone
            } else {
                _uiState.value = FsrsUiState.Reviewing(queue = queue, currentIndex = 0)
            }
        }
    }

    fun flipCard() {
        val current = _uiState.value
        if (current is FsrsUiState.Reviewing) {
            _uiState.value = current.copy(isFlipped = !current.isFlipped)
        }
    }

    fun rateCard(rating: FSRSRating) {
        val current = _uiState.value
        if (current is FsrsUiState.Reviewing && current.queue.isNotEmpty()) {
            val item = current.queue[current.currentIndex]
            val record = orchestrator.examinerAgent.scheduleFsrsReview(item.fsrsState, rating, Date())

            val nextIndex = current.currentIndex + 1
            if (nextIndex >= current.queue.size) {
                _uiState.value = FsrsUiState.AllDone
            } else {
                _uiState.value = current.copy(
                    currentIndex = nextIndex,
                    isFlipped = false,
                    cardsReviewedToday = current.cardsReviewedToday + 1,
                    averageStability = record.card.stability
                )
            }
        }
    }
}
