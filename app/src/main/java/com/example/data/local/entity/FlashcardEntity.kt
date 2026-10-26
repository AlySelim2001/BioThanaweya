package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * FlashcardEntity:
 * Represents a study flashcard with FSRS Spaced Repetition parameters.
 */
@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val topicId: String,
    val frontPrompt: String,
    val backAnswer: String,
    val category: String,
    val stability: Double = 0.0,
    val difficulty: Double = 0.0,
    val reps: Int = 0,
    val lapses: Int = 0,
    val state: Int = 0, // FSRSState ordinal: 0=NEW, 1=LEARNING, 2=REVIEW, 3=RELEARNING
    val dueEpoch: Long = System.currentTimeMillis(),
    val lastReviewEpoch: Long? = null
)
