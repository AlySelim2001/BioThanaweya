package com.example.domain.repository

import com.example.data.local.entity.ChapterContentEntity
import kotlinx.coroutines.flow.Flow

/**
 * SearchResultItem:
 * Represents a matched search result with highlighted title, contextual snippet,
 * relevance score, and the underlying chapter content entity.
 */
data class SearchResultItem(
    val content: ChapterContentEntity,
    val highlightedSectionTitle: String,
    val matchedSnippet: String,
    val matchedKeyword: String,
    val relevanceScore: Int
)

/**
 * ChapterSearchRepository:
 * Domain contract for offline-first full-text search across all 11 Thanawya Amma biology chapters.
 */
interface ChapterSearchRepository {

    /**
     * Executes instant reactive search across chapters with optional Unit and Chapter filters.
     */
    fun search(
        query: String,
        unitNumber: Int? = null,
        chapterId: Int? = null
    ): Flow<List<SearchResultItem>>

    /**
     * Observes all searchable contents.
     */
    fun getAllContents(): Flow<List<ChapterContentEntity>>

    /**
     * Observes all contents for a specific chapter ID (1 to 11).
     */
    fun getContentsByChapter(chapterId: Int): Flow<List<ChapterContentEntity>>

    /**
     * Retrieves a single content section by ID.
     */
    fun getContentById(id: String): Flow<ChapterContentEntity?>

    /**
     * Observes recent search queries stored offline.
     */
    fun getRecentSearches(): Flow<List<String>>

    /**
     * Records a search query in offline history.
     */
    suspend fun recordSearch(query: String, resultCount: Int)

    /**
     * Deletes a search query from offline history.
     */
    suspend fun deleteRecentSearch(query: String)

    /**
     * Clears all search history offline.
     */
    suspend fun clearRecentSearches()

    /**
     * Pre-seeds the Room database with comprehensive curriculum data if empty.
     */
    suspend fun seedCurriculumContentIfEmpty()
}
