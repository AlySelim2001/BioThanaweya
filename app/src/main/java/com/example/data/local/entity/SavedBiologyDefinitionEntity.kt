package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room Entity for saved and bookmarked biology definitions.
 * Enables offline access, quick revision, and personal student annotations.
 */
@Entity(
    tableName = "saved_definitions",
    indices = [
        Index(value = ["term"], unique = true),
        Index(value = ["unitCategory"]),
        Index(value = ["chapterTitle"])
    ]
)
data class SavedBiologyDefinitionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val term: String,                      // English scientific term, e.g. "Sarcomere"
    val arabicTerm: String,                // Arabic term, e.g. "القطعة العضلية"
    val definition: String,                // English official textbook definition
    val arabicDefinition: String = "",     // Arabic official textbook definition
    val chapterTitle: String,              // Associated chapter
    val unitCategory: String = "UNIT_1",   // "UNIT_1" or "UNIT_2"
    val isBookmarked: Boolean = true,
    val studentNotes: String = "",         // Optional custom student note
    val masteryLevel: String = "LEARNING", // LEARNING, REVIEW, MASTERED
    val savedAtEpoch: Long = System.currentTimeMillis()
)
