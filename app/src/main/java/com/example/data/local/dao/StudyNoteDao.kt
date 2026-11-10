package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.StudyNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyNoteDao {
    @Query("SELECT * FROM study_notes WHERE topicId = :topicId")
    fun getNotesByTopic(topicId: String): Flow<List<StudyNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: StudyNoteEntity)

    @Delete
    suspend fun deleteNote(note: StudyNoteEntity)
}
