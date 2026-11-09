package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.ChapterProgress
import com.example.domain.repository.ChapterProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Filter mode for the 11 Thanawya Amma biology chapters.
 */
enum class ChapterFilterMode {
    ALL,
    UNIT_1, // التركيب والوظيفة في الكائنات الحية (Chapters 1-9)
    UNIT_2, // البيولوجيا الجزيئية (Chapters 10-11)
    COMPLETED_ONLY,
    IN_PROGRESS_ONLY
}

data class ChaptersDashboardUiState(
    val isLoading: Boolean = true,
    val allChapters: List<ChapterProgress> = emptyList(),
    val filteredChapters: List<ChapterProgress> = emptyList(),
    val filterMode: ChapterFilterMode = ChapterFilterMode.ALL,
    val searchQuery: String = "",
    val overallCompletionPercentage: Float = 0f,
    val completedChaptersCount: Int = 0,
    val totalChaptersCount: Int = 11,
    val selectedChapterForDetails: ChapterProgress? = null
)

@HiltViewModel
class ChaptersDashboardViewModel @Inject constructor(
    private val chapterProgressRepository: ChapterProgressRepository
) : ViewModel() {

    private val _filterMode = MutableStateFlow(ChapterFilterMode.ALL)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedChapterId = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<ChaptersDashboardUiState> = combine(
        chapterProgressRepository.getAllChaptersProgress(),
        _filterMode,
        _searchQuery,
        _selectedChapterId
    ) { allChapters: List<ChapterProgress>, filterMode: ChapterFilterMode, searchQuery: String, selectedId: Int? ->
        val filtered = allChapters.filter { chapter ->
            val matchesFilter = when (filterMode) {
                ChapterFilterMode.ALL -> true
                ChapterFilterMode.UNIT_1 -> chapter.unitNumber == 1
                ChapterFilterMode.UNIT_2 -> chapter.unitNumber == 2
                ChapterFilterMode.COMPLETED_ONLY -> chapter.isCompleted || chapter.completionPercentage >= 100f
                ChapterFilterMode.IN_PROGRESS_ONLY -> chapter.completionPercentage > 0f && !chapter.isCompleted
            }

            val queryClean = searchQuery.trim().lowercase()
            val matchesSearch = if (queryClean.isEmpty()) {
                true
            } else {
                chapter.titleAr.lowercase().contains(queryClean) ||
                        chapter.titleEn.lowercase().contains(queryClean) ||
                        chapter.summaryAr.lowercase().contains(queryClean) ||
                        chapter.chapterId.toString() == queryClean
            }

            matchesFilter && matchesSearch
        }

        val selectedChapter = allChapters.find { it.chapterId == selectedId }
        val completedCount = allChapters.count { it.isCompleted || it.completionPercentage >= 100f }
        val avgPercentage = if (allChapters.isEmpty()) 0f else allChapters.map { it.completionPercentage }.average().toFloat()

        ChaptersDashboardUiState(
            isLoading = false,
            allChapters = allChapters,
            filteredChapters = filtered,
            filterMode = filterMode,
            searchQuery = searchQuery,
            overallCompletionPercentage = avgPercentage,
            completedChaptersCount = completedCount,
            totalChaptersCount = allChapters.size.coerceAtLeast(11),
            selectedChapterForDetails = selectedChapter
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChaptersDashboardUiState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            // Guarantee 11 chapters are seeded in offline database
            chapterProgressRepository.seedDefault11ChaptersIfEmpty()
        }
    }

    fun setFilterMode(mode: ChapterFilterMode) {
        _filterMode.value = mode
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun selectChapterForDetails(chapter: ChapterProgress?) {
        _selectedChapterId.value = chapter?.chapterId
    }

    fun toggleChapterCompletion(chapterId: Int) {
        viewModelScope.launch {
            val chapter = uiState.value.allChapters.find { it.chapterId == chapterId }
            if (chapter != null) {
                chapterProgressRepository.markChapterCompleted(chapterId, !chapter.isCompleted)
            }
        }
    }

    fun updateProgressPercentage(chapterId: Int, percentage: Float) {
        viewModelScope.launch {
            chapterProgressRepository.updateChapterProgressPercentage(chapterId, percentage)
        }
    }

    fun recordQuestionSolved(chapterId: Int) {
        viewModelScope.launch {
            chapterProgressRepository.recordQuestionSolved(chapterId, 1)
        }
    }

    fun recordFlashcardMastered(chapterId: Int) {
        viewModelScope.launch {
            chapterProgressRepository.recordFlashcardMastered(chapterId, 1)
        }
    }

    fun addStudyMinutes(chapterId: Int, minutes: Int) {
        viewModelScope.launch {
            chapterProgressRepository.addStudyDuration(chapterId, minutes)
        }
    }

    fun resetAllCurriculumProgress() {
        viewModelScope.launch {
            chapterProgressRepository.resetAllProgress()
        }
    }
}
