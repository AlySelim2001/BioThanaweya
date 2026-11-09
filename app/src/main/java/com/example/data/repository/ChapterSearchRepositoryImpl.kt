package com.example.data.repository

import com.example.data.local.dao.ChapterContentDao
import com.example.data.local.dao.SearchHistoryDao
import com.example.data.local.entity.ChapterContentEntity
import com.example.data.local.entity.SearchHistoryEntity
import com.example.data.provider.ThanawyaAmmaCurriculumContent
import com.example.domain.repository.ChapterSearchRepository
import com.example.domain.repository.SearchResultItem
import com.example.util.ArabicSearchUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ChapterSearchRepositoryImpl:
 * Implementation of ChapterSearchRepository backed by Room Database,
 * enabling instantaneous offline keyword lookups, Arabic-resilient matching,
 * contextual snippet generation, and search history caching.
 */
@Singleton
class ChapterSearchRepositoryImpl @Inject constructor(
    private val chapterContentDao: ChapterContentDao,
    private val searchHistoryDao: SearchHistoryDao
) : ChapterSearchRepository {

    override fun search(
        query: String,
        unitNumber: Int?,
        chapterId: Int?
    ): Flow<List<SearchResultItem>> {
        val trimmed = query.trim()
        val normalized = ArabicSearchUtils.normalize(trimmed)

        val flow = if (normalized.isBlank()) {
            if (chapterId != null) {
                chapterContentDao.getContentsByChapter(chapterId)
            } else {
                chapterContentDao.getAllContents()
            }
        } else {
            chapterContentDao.searchWithFilters(
                normalizedQuery = normalized,
                rawQuery = trimmed,
                unitNumber = unitNumber,
                chapterId = chapterId
            )
        }

        return flow.map { list ->
            list.map { entity ->
                val snippet = ArabicSearchUtils.extractSnippet(entity.contentAr, trimmed)
                val score = ArabicSearchUtils.calculateScore(
                    query = trimmed,
                    sectionTitle = entity.sectionTitleAr,
                    keywords = entity.keywordsAr,
                    chapterTitle = entity.chapterTitleAr,
                    content = entity.contentAr,
                    baseImportance = entity.importanceScore
                )
                // Identify matched keyword if any
                val matchedKw = entity.keywordsAr.split(" ")
                    .firstOrNull { ArabicSearchUtils.matches(it, trimmed) } ?: ""

                SearchResultItem(
                    content = entity,
                    highlightedSectionTitle = entity.sectionTitleAr,
                    matchedSnippet = snippet,
                    matchedKeyword = matchedKw,
                    relevanceScore = score
                )
            }.sortedByDescending { it.relevanceScore }
        }
    }

    override fun getAllContents(): Flow<List<ChapterContentEntity>> {
        return chapterContentDao.getAllContents()
    }

    override fun getContentsByChapter(chapterId: Int): Flow<List<ChapterContentEntity>> {
        return chapterContentDao.getContentsByChapter(chapterId)
    }

    override fun getContentById(id: String): Flow<ChapterContentEntity?> {
        return chapterContentDao.getContentById(id)
    }

    override fun getRecentSearches(): Flow<List<String>> {
        return searchHistoryDao.getRecentQueries(limit = 8)
    }

    override suspend fun recordSearch(query: String, resultCount: Int) {
        val trimmed = query.trim()
        if (trimmed.length >= 2) {
            searchHistoryDao.insertSearch(
                SearchHistoryEntity(
                    query = trimmed,
                    timestamp = System.currentTimeMillis(),
                    resultCount = resultCount
                )
            )
        }
    }

    override suspend fun deleteRecentSearch(query: String) {
        searchHistoryDao.deleteSearch(query)
    }

    override suspend fun clearRecentSearches() {
        searchHistoryDao.clearHistory()
    }

    override suspend fun seedCurriculumContentIfEmpty() {
        val count = chapterContentDao.getCount()
        if (count == 0) {
            chapterContentDao.insertAll(ThanawyaAmmaCurriculumContent.ALL_CHAPTER_CONTENTS)
        }
    }
}
