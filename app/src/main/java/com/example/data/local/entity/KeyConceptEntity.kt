package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * KeyConceptEntity:
 * Room Entity enabling offline access to high-yield biology concepts,
 * technical breakdowns, analogies, formulas, and Ministry trap alerts.
 */
@Entity(
    tableName = "key_concepts",
    indices = [
        Index(value = ["topicId"]),
        Index(value = ["unitCategory"]),
        Index(value = ["chapterId"])
    ]
)
data class KeyConceptEntity(
    @PrimaryKey
    val conceptId: String,                 // Unique ID, e.g. "sarcomere_huxley_sliding"
    val topicId: String,                   // Associated subtopic ID
    val chapterId: String = "unit1_chap1", // Associated chapter ID
    val unitCategory: String = "UNIT_1",   // "UNIT_1" or "UNIT_2"
    val title: String,                     // English title
    val arabicTitle: String,               // Arabic title
    val summary: String,                   // High-yield textbook summary
    val arabicEli5: String,                // Conceptual Arabic analogy
    val keyTakeawaysJson: String = "",     // Bullet points (delimiter or JSON)
    val formulaOrRule: String = "",        // Mathematical / anatomical formula or rule
    val examTrapWarning: String = "",      // Ministry exam trap alert
    val isAvailableOffline: Boolean = true,
    val lastCachedEpoch: Long = System.currentTimeMillis()
)
