package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * StudentProgressEntity:
 * Represents overall student learning streak, XP, and question grading telemetry.
 */
@Entity(tableName = "student_progress")
data class StudentProgressEntity(
    @PrimaryKey val id: Int = 1,
    val streakDays: Int = 3,
    val totalXp: Int = 500,
    val questionsGraded: Int = 0,
    val lastActiveEpoch: Long = System.currentTimeMillis()
)
