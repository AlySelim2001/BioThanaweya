package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.ChapterSearchRepository
import com.example.domain.repository.SearchResultItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChapterSearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val results: List<SearchResultItem> = emptyList(),
    val totalMatchesCount: Int = 0,
    val selectedUnit: Int? = null,              // null = All, 1 = Unit 1, 2 = Unit 2
    val selectedChapterId: Int? = null,         // null = All, 1..11
    val recentSearches: List<String> = emptyList(),
    val selectedDetailItem: SearchResultItem? = null,
    val isSeeding: Boolean = false
)

@OptIn(FlowPreview::class)
@HiltViewModel
class ChapterSearchViewModel @Inject constructor(
    private val searchRepository: ChapterSearchRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _selectedUnit = MutableStateFlow<Int?>(null)
    private val _selectedChapterId = MutableStateFlow<Int?>(null)
    private val _selectedDetailItem = MutableStateFlow<SearchResultItem?>(null)
    private val _isSearching = MutableStateFlow(false)

    val popularKeywords: List<String> = listOf(
        "أوكسينات",
        "ساركومير",
        "ATP",
        "إنزيم بلمرة",
        "جسم مضاد",
        "دورة كريبس",
        "توالد بكري",
        "خلايا تائية",
        "بلازميد",
        "إخصاب مزدوج",
        "نيفرون",
        "ميوسين",
        "كودون البدء",
        "إنزيمات القصر"
    )

    // Debounced query stream for fluid, responsive typing without UI stutter
    private val debouncedQuery = _query
        .debounce(150)
        .distinctUntilChanged()

    val uiState: StateFlow<ChapterSearchUiState> = combine(
        debouncedQuery,
        _selectedUnit,
        _selectedChapterId,
        _selectedDetailItem,
        searchRepository.getRecentSearches()
    ) { query, unit, chapterId, detailItem, recent ->
        SearchParams(query, unit, chapterId, detailItem, recent)
    }.flatMapLatest { params ->
        _isSearching.value = true
        searchRepository.search(
            query = params.query,
            unitNumber = params.unit,
            chapterId = params.chapterId
        ).map { results ->
            _isSearching.value = false
            ChapterSearchUiState(
                query = _query.value,
                isSearching = false,
                results = results,
                totalMatchesCount = results.size,
                selectedUnit = params.unit,
                selectedChapterId = params.chapterId,
                recentSearches = params.recent,
                selectedDetailItem = params.detailItem
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChapterSearchUiState(isSearching = true)
    )

    init {
        viewModelScope.launch {
            // Seed curriculum content on-device if first run
            searchRepository.seedCurriculumContentIfEmpty()
        }
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    fun clearQuery() {
        _query.value = ""
    }

    fun setUnitFilter(unit: Int?) {
        _selectedUnit.value = unit
        // If switching unit, reset chapter filter if it doesn't belong
        if (unit != null) {
            val currentChap = _selectedChapterId.value
            if (currentChap != null) {
                val chapBelongsToUnit = if (unit == 1) currentChap in 1..9 else currentChap in 10..11
                if (!chapBelongsToUnit) {
                    _selectedChapterId.value = null
                }
            }
        }
    }

    fun setChapterFilter(chapterId: Int?) {
        _selectedChapterId.value = chapterId
    }

    fun selectKeyword(keyword: String) {
        _query.value = keyword
        recordSearchHistory(keyword)
    }

    fun commitSearch(query: String) {
        recordSearchHistory(query)
    }

    fun selectDetailItem(item: SearchResultItem?) {
        _selectedDetailItem.value = item
    }

    fun deleteRecentSearch(query: String) {
        viewModelScope.launch {
            searchRepository.deleteRecentSearch(query)
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            searchRepository.clearRecentSearches()
        }
    }

    private fun recordSearchHistory(query: String) {
        val trimmed = query.trim()
        if (trimmed.length >= 2) {
            viewModelScope.launch {
                val currentCount = uiState.value.results.size
                searchRepository.recordSearch(trimmed, currentCount)
            }
        }
    }

    private data class SearchParams(
        val query: String,
        val unit: Int?,
        val chapterId: Int?,
        val detailItem: SearchResultItem?,
        val recent: List<String>
    )
}
