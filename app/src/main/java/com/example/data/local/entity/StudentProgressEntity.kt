package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * StudentProgressEntity:
 * Represents overall student learning streak, XP, questions graded,
 * completed exams, study time, and projected national percentile ranking.
 */
@Entity(tableName = "student_progress")
data class StudentProgressEntity(
    @PrimaryKey val id: Int = 1,
    val streakDays: Int = 3,
    val totalXp: Int = 500,
    val questionsGraded: Int = 0,
    val examsCompleted: Int = 0,
    val totalStudyMinutes: Int = 45,
    val overallMasteryPercentage: Float = 0.65f,
    val projectedPercentile: Float = 94.5f,
    val savedDefinitionsCount: Int = 0,
    val lastActiveEpoch: Long = System.currentTimeMillis()
)
