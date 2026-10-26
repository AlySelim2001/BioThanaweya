package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ExamAttemptEntity:
 * Records completed Ministry Exam simulations with official scoring and marks.
 */
@Entity(tableName = "exam_attempts")
data class ExamAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampEpoch: Long = System.currentTimeMillis(),
    val totalQuestions: Int,
    val correctAnswers: Int,
    val totalMarks: Double,
    val maxPossibleMarks: Double,
    val percentage: Double,
    val passed: Boolean,
    val durationSeconds: Long
)
