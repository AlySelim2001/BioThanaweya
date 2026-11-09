package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.StudentProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * ProgressDao:
 * Access methods for student gamification stats, streak, XP,
 * and comprehensive academic learning telemetry.
 */
@Dao
interface ProgressDao {
    @Query("SELECT * FROM student_progress WHERE id = 1")
    fun getProgress(): Flow<StudentProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: StudentProgressEntity)

    @Query("""
        UPDATE student_progress
        SET totalXp = totalXp + :xpToAdd,
            lastActiveEpoch = :nowEpoch
        WHERE id = 1
    """)
    suspend fun addXp(xpToAdd: Int, nowEpoch: Long = System.currentTimeMillis())

    @Query("""
        UPDATE student_progress
        SET questionsGraded = questionsGraded + 1,
            totalXp = totalXp + :xpBonus,
            lastActiveEpoch = :nowEpoch
        WHERE id = 1
    """)
    suspend fun recordGradedQuestion(xpBonus: Int = 10, nowEpoch: Long = System.currentTimeMillis())

    @Query("""
        UPDATE student_progress
        SET examsCompleted = examsCompleted + 1,
            totalStudyMinutes = totalStudyMinutes + :durationMinutes,
            overallMasteryPercentage = :newMastery,
            projectedPercentile = :newPercentile,
            lastActiveEpoch = :nowEpoch
        WHERE id = 1
    """)
    suspend fun recordExamCompletion(
        durationMinutes: Int,
        newMastery: Float,
        newPercentile: Float,
        nowEpoch: Long = System.currentTimeMillis()
    )

    @Query("UPDATE student_progress SET savedDefinitionsCount = :count WHERE id = 1")
    suspend fun updateSavedDefinitionsCount(count: Int)
}
