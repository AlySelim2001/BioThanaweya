package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic_mastery")
data class TopicMasteryEntity(
    @PrimaryKey val topicId: String,
    val topicTitle: String,
    val unitNumber: Int = 1,
    val masteryLevel: Float = 0.5f,
    val cardsReviewed: Int = 0,
    val averageStability: Double = 3.0,
    val lastUpdatedEpoch: Long = System.currentTimeMillis()
)
