package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TopicMasteryEntity:
 * Records mastery level (0.0 to 1.0) per topic used to populate the Analytics Heatmap.
 */
@Entity(tableName = "topic_mastery")
data class TopicMasteryEntity(
    @PrimaryKey val topicId: String,
    val topicTitle: String,
    val unitNumber: Int,
    val masteryLevel: Float, // 0.0f (Novice) to 1.0f (Mastered)
    val cardsReviewed: Int,
    val averageStability: Double,
    val lastUpdatedEpoch: Long = System.currentTimeMillis()
)
