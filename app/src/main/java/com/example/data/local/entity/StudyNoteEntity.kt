package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_notes")
data class StudyNoteEntity(
    @PrimaryKey val id: String,
    val topicId: String,
    val noteText: String,
    val createdAtEpoch: Long = System.currentTimeMillis()
)
