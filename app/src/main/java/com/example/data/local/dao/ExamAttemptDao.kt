package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.ExamAttemptEntity
import kotlinx.coroutines.flow.Flow

/**
 * ExamAttemptDao:
 * Access methods for saving and reviewing simulated exam scores.
 */
@Dao
interface ExamAttemptDao {
    @Query("SELECT * FROM exam_attempts ORDER BY timestampEpoch DESC")
    fun getAllAttempts(): Flow<List<ExamAttemptEntity>>

    @Query("SELECT * FROM exam_attempts ORDER BY timestampEpoch DESC LIMIT 1")
    fun getLatestAttempt(): Flow<ExamAttemptEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: ExamAttemptEntity): Long

    @Query("SELECT COUNT(*) FROM exam_attempts")
    fun getAttemptsCount(): Flow<Int>
}
