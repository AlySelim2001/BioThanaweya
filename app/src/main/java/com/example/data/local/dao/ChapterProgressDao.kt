package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.ChapterProgress
import kotlinx.coroutines.flow.Flow

/**
 * ChapterProgressDao:
 * Room Data Access Object providing reactive Flow streams and asynchronous
 * CRUD operations for offline chapter completion tracking.
 */
@Dao
interface ChapterProgressDao {

    @Query("SELECT * FROM chapter_progress ORDER BY chapterId ASC")
    fun getAllChapterProgress(): Flow<List<ChapterProgress>>

    @Query("SELECT * FROM chapter_progress WHERE chapterId = :chapterId")
    fun getChapterProgressById(chapterId: Int): Flow<ChapterProgress?>

    @Query("SELECT * FROM chapter_progress WHERE chapterId = :chapterId")
    suspend fun getChapterProgressByIdSync(chapterId: Int): ChapterProgress?

    @Query("SELECT * FROM chapter_progress WHERE chapterKey = :key LIMIT 1")
    fun getChapterProgressByKey(key: String): Flow<ChapterProgress?>

    @Query("SELECT * FROM chapter_progress WHERE isCompleted = 1 ORDER BY chapterId ASC")
    fun getCompletedChapters(): Flow<List<ChapterProgress>>

    @Query("SELECT COUNT(*) FROM chapter_progress WHERE isCompleted = 1")
    fun getCompletedChaptersCount(): Flow<Int>

    @Query("SELECT AVG(completionPercentage) FROM chapter_progress")
    fun getAverageCompletionPercentage(): Flow<Float?>

    @Query("SELECT * FROM chapter_progress WHERE unitNumber = :unitNumber ORDER BY chapterId ASC")
    fun getChapterProgressByUnit(unitNumber: Int): Flow<List<ChapterProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: ChapterProgress)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chapters: List<ChapterProgress>)

    @Update
    suspend fun update(progress: ChapterProgress)

    @Query("""
        UPDATE chapter_progress 
        SET isCompleted = :isCompleted,
            completionPercentage = CASE WHEN :isCompleted = 1 THEN 100.0 ELSE completionPercentage END,
            lastStudiedTimestamp = :timestamp 
        WHERE chapterId = :chapterId
    """)
    suspend fun updateCompletionStatus(
        chapterId: Int,
        isCompleted: Boolean,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE chapter_progress 
        SET completionPercentage = :percentage,
            isCompleted = CASE WHEN :percentage >= 100.0 THEN 1 ELSE isCompleted END,
            lastStudiedTimestamp = :timestamp 
        WHERE chapterId = :chapterId
    """)
    suspend fun updateProgressPercentage(
        chapterId: Int,
        percentage: Float,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE chapter_progress 
        SET questionsSolved = questionsSolved + :count,
            lastStudiedTimestamp = :timestamp 
        WHERE chapterId = :chapterId
    """)
    suspend fun incrementQuestionsSolved(
        chapterId: Int,
        count: Int = 1,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE chapter_progress 
        SET flashcardsMastered = flashcardsMastered + :count,
            lastStudiedTimestamp = :timestamp 
        WHERE chapterId = :chapterId
    """)
    suspend fun incrementFlashcardsMastered(
        chapterId: Int,
        count: Int = 1,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE chapter_progress 
        SET studyTimeMinutes = studyTimeMinutes + :minutes,
            lastStudiedTimestamp = :timestamp 
        WHERE chapterId = :chapterId
    """)
    suspend fun addStudyMinutes(
        chapterId: Int,
        minutes: Int,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("SELECT COUNT(*) FROM chapter_progress")
    suspend fun getTotalChapterCount(): Int

    @Query("DELETE FROM chapter_progress")
    suspend fun deleteAll()

    @Query("""
        UPDATE chapter_progress 
        SET isCompleted = 0, 
            completionPercentage = 0.0, 
            questionsSolved = 0, 
            flashcardsMastered = 0, 
            studyTimeMinutes = 0, 
            lastStudiedTimestamp = :timestamp
    """)
    suspend fun resetAllProgress(timestamp: Long = System.currentTimeMillis())
}
