package com.example.domain.repository

import com.example.data.local.entity.ChapterProgress
import kotlinx.coroutines.flow.Flow

/**
 * ChapterProgressRepository:
 * Domain contract for managing and observing offline progress and completion
 * for the 11 Egyptian Thanawya Amma Biology curriculum chapters.
 */
interface ChapterProgressRepository {

    /**
     * Observes real-time progress for all 11 chapters ordered by chapter ID.
     */
    fun getAllChaptersProgress(): Flow<List<ChapterProgress>>

    /**
     * Observes real-time progress of a specific chapter by ID (1 to 11).
     */
    fun getChapterProgress(chapterId: Int): Flow<ChapterProgress?>

    /**
     * Observes progress of a specific chapter by key (e.g. "support_movement").
     */
    fun getChapterProgressByKey(key: String): Flow<ChapterProgress?>

    /**
     * Observes the list of completed chapters.
     */
    fun getCompletedChapters(): Flow<List<ChapterProgress>>

    /**
     * Observes the count of completed chapters (0 to 11).
     */
    fun getCompletedChaptersCount(): Flow<Int>

    /**
     * Observes the average curriculum completion percentage across all chapters.
     */
    fun getAverageCompletionPercentage(): Flow<Float>

    /**
     * Observes chapters belonging to a specific unit (1 or 2).
     */
    fun getChaptersByUnit(unitNumber: Int): Flow<List<ChapterProgress>>

    /**
     * Toggles or marks the completion status of a chapter.
     */
    suspend fun markChapterCompleted(chapterId: Int, isCompleted: Boolean)

    /**
     * Updates completion percentage for a chapter (0.0 to 100.0).
     */
    suspend fun updateChapterProgressPercentage(chapterId: Int, percentage: Float)

    /**
     * Records that a question was solved for the specified chapter.
     */
    suspend fun recordQuestionSolved(chapterId: Int, count: Int = 1)

    /**
     * Records that a flashcard was mastered for the specified chapter.
     */
    suspend fun recordFlashcardMastered(chapterId: Int, count: Int = 1)

    /**
     * Adds offline study time (in minutes) to the specified chapter.
     */
    suspend fun addStudyDuration(chapterId: Int, minutes: Int)

    /**
     * Inserts or updates a full chapter progress model.
     */
    suspend fun saveChapterProgress(progress: ChapterProgress)

    /**
     * Pre-populates the 11 Egyptian Thanawya Amma Biology chapters if the database table is empty.
     */
    suspend fun seedDefault11ChaptersIfEmpty()

    /**
     * Resets all progress back to 0% for study cycle re-taking.
     */
    suspend fun resetAllProgress()
}
