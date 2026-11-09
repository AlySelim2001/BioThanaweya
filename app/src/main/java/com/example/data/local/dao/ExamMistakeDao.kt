package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.ExamMistakeEntity
import kotlinx.coroutines.flow.Flow

/**
 * ExamMistakeDao:
 * Access methods for saving, retrieving, and resolving mistaken questions
 * and fallen traps for targeted student re-testing.
 */
@Dao
interface ExamMistakeDao {
    @Query("SELECT * FROM exam_mistakes ORDER BY timestampEpoch DESC")
    fun getAllMistakes(): Flow<List<ExamMistakeEntity>>

    @Query("SELECT * FROM exam_mistakes WHERE isResolved = 0 ORDER BY timestampEpoch DESC")
    fun getUnresolvedMistakes(): Flow<List<ExamMistakeEntity>>

    @Query("SELECT COUNT(*) FROM exam_mistakes WHERE isResolved = 0")
    fun getUnresolvedMistakesCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: ExamMistakeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistakes(mistakes: List<ExamMistakeEntity>): List<Long>

    @Query("UPDATE exam_mistakes SET isResolved = 1 WHERE id = :id")
    suspend fun markResolved(id: Long)

    @Query("DELETE FROM exam_mistakes WHERE isResolved = 1")
    suspend fun clearResolvedMistakes()

    @Query("DELETE FROM exam_mistakes WHERE id = :id")
    suspend fun deleteMistake(id: Long)
}
