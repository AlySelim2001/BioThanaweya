package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * SearchHistoryEntity:
 * Room entity recording recent keyword search queries offline.
 */
@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey
    val query: String,
    val timestamp: Long = System.currentTimeMillis(),
    val resultCount: Int = 0
)
