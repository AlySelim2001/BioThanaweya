package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val topicId: String,
    val frontPrompt: String,
    val backAnswer: String,
    val category: String,
    val stability: Double = 2.0,
    val difficulty: Double = 5.0,
    val reps: Int = 0,
    val lapses: Int = 0,
    val state: Int = 0,
    val dueEpoch: Long = System.currentTimeMillis(),
    val lastReviewEpoch: Long? = null
)
