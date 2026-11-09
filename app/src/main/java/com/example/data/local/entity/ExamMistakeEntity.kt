package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ExamMistakeEntity:
 * Represents an error or fallen trap in a simulated exam or quiz session.
 * Stored in Room to power the Mistake Notebook (دفتر الأخطاء والتريكات)
 * and enable targeted remediation quizzes.
 */
@Entity(tableName = "exam_mistakes")
data class ExamMistakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val attemptId: Long = 0,
    val paperId: String = "",
    val questionNumber: Int,
    val questionTextArabic: String,
    val questionTextEnglish: String = "",
    val selectedOptionIndex: Int = -1,
    val correctOptionIndex: Int = 0,
    val trapDescriptionArabic: String = "",
    val trapDescriptionEnglish: String = "",
    val explanationArabic: String = "",
    val isResolved: Boolean = false,
    val timestampEpoch: Long = System.currentTimeMillis()
)
