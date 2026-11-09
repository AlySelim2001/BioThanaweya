package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ChapterProgress:
 * Persistent Room data model tracking the completion status, mastery percentage,
 * questions solved, flashcards mastered, and study time for the 11 Egyptian
 * Thanawya Amma Biology curriculum chapters.
 */
@Entity(tableName = "chapter_progress")
data class ChapterProgress(
    @PrimaryKey
    val chapterId: Int,                         // 1 to 11
    val chapterKey: String,                     // Unique key (e.g., "support_movement", "nutrition")
    val unitNumber: Int,                        // 1 = التركيب والوظيفة, 2 = البيولوجيا الجزيئية
    val titleAr: String,                        // Arabic chapter title
    val titleEn: String,                        // English chapter title
    val summaryAr: String = "",                 // Arabic syllabus summary
    val iconName: String = "book",              // Icon token
    val isCompleted: Boolean = false,           // Has the student completed this chapter?
    val completionPercentage: Float = 0.0f,     // 0.0f to 100.0f
    val questionsSolved: Int = 0,               // Number of ministry-style questions solved
    val totalQuestions: Int = 10,               // Total questions available
    val flashcardsMastered: Int = 0,            // Number of FSRS flashcards mastered
    val totalFlashcards: Int = 15,              // Total flashcards in chapter
    val studyTimeMinutes: Int = 0,              // Cumulative offline study time in minutes
    val lastStudiedTimestamp: Long = System.currentTimeMillis()
) {
    val progressFraction: Float
        get() = (completionPercentage / 100f).coerceIn(0f, 1f)

    val questionsProgressFraction: Float
        get() = if (totalQuestions > 0) (questionsSolved.toFloat() / totalQuestions).coerceIn(0f, 1f) else 0f

    val flashcardsProgressFraction: Float
        get() = if (totalFlashcards > 0) (flashcardsMastered.toFloat() / totalFlashcards).coerceIn(0f, 1f) else 0f
}
