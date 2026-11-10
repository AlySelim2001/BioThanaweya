package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Flashcard
import com.example.domain.fsrs.FSRSCard
import com.example.domain.fsrs.FSRSRating
import com.example.domain.fsrs.FSRSState
import com.example.domain.usecase.OrchestratorUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

data class FsrsCardItem(
    val flashcard: Flashcard,
    val fsrsCard: FSRSCard = FSRSCard(),
    val isFlipped: Boolean = false
)

data class FsrsReviewUiState(
    val topicId: String = "",
    val cards: List<FsrsCardItem> = emptyList(),
    val currentIndex: Int = 0,
    val completedCardsCount: Int = 0,
    val isSessionComplete: Boolean = false,
    val lastRatingGiven: FSRSRating? = null
)

class FsrsReviewViewModel(
    private val orchestrator: OrchestratorUseCase,
    private val topicId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(FsrsReviewUiState(topicId = topicId))
    val uiState: StateFlow<FsrsReviewUiState> = _uiState.asStateFlow()

    init {
        loadFlashcardsForTopic(topicId)
    }

    fun loadFlashcardsForTopic(id: String) {
        viewModelScope.launch {
            val pack = orchestrator.educatorAgent.getStudyPack(id)
            val cardItems = if (pack.flashcards.isNotEmpty()) {
                pack.flashcards.map { FsrsCardItem(flashcard = it) }
            } else {
                listOf(
                    FsrsCardItem(
                        flashcard = Flashcard(
                            id = "default_fc_1",
                            topicId = id,
                            frontPrompt = "ما المقصود بالدعامة الفسيولوجية؟",
                            backAnswer = "دعامة مؤقتة تتناول الخلية ككل وتعتمد على امتلاء الفجوة العصارية بالماء بالأسموزية.",
                            category = "Biology"
                        )
                    )
                )
            }
            _uiState.value = FsrsReviewUiState(
                topicId = id,
                cards = cardItems,
                currentIndex = 0,
                completedCardsCount = 0,
                isSessionComplete = cardItems.isEmpty()
            )
        }
    }

    fun flipCurrentCard() {
        val state = _uiState.value
        if (state.currentIndex >= state.cards.size) return

        val updated = state.cards.toMutableList()
        val current = updated[state.currentIndex]
        updated[state.currentIndex] = current.copy(isFlipped = !current.isFlipped)
        _uiState.value = state.copy(cards = updated)
    }

    fun rateCurrentCard(rating: FSRSRating) {
        val state = _uiState.value
        if (state.currentIndex >= state.cards.size) return

        val current = state.cards[state.currentIndex]
        val scheduling = orchestrator.examinerAgent.scheduleFsrsReview(current.fsrsCard, rating, Date())
        val updatedCard = current.copy(fsrsCard = scheduling.card)

        val updatedList = state.cards.toMutableList()
        updatedList[state.currentIndex] = updatedCard

        val nextIndex = state.currentIndex + 1
        val isComplete = nextIndex >= state.cards.size

        _uiState.value = state.copy(
            cards = updatedList,
            currentIndex = nextIndex,
            completedCardsCount = state.completedCardsCount + 1,
            isSessionComplete = isComplete,
            lastRatingGiven = rating
        )
    }

    fun resetSession() {
        val state = _uiState.value
        val resetList = state.cards.map { it.copy(isFlipped = false) }
        _uiState.value = state.copy(
            cards = resetList,
            currentIndex = 0,
            completedCardsCount = 0,
            isSessionComplete = false
        )
    }
}
