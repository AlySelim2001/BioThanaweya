package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.StudentProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * ProgressDao:
 * Access methods for student gamification stats, streak, and XP.
 */
@Dao
interface ProgressDao {
    @Query("SELECT * FROM student_progress WHERE id = 1")
    fun getProgress(): Flow<StudentProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: StudentProgressEntity)
}
