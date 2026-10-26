package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.StudyNoteEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for biology study notes CRUD operations and caching.
 */
@Dao
interface StudyNoteDao {

    @Query("SELECT * FROM study_notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<StudyNoteEntity>>

    @Query("SELECT * FROM study_notes WHERE topicId = :topicId ORDER BY updatedAt DESC")
    fun getNotesByTopic(topicId: String): Flow<List<StudyNoteEntity>>

    @Query("SELECT * FROM study_notes WHERE isBookmarked = 1 ORDER BY updatedAt DESC")
    fun getBookmarkedNotes(): Flow<List<StudyNoteEntity>>

    @Query("SELECT * FROM study_notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): StudyNoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: StudyNoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<StudyNoteEntity>)

    @Update
    suspend fun updateNote(note: StudyNoteEntity)

    @Delete
    suspend fun deleteNote(note: StudyNoteEntity)

    @Query("DELETE FROM study_notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Long)

    @Query("DELETE FROM study_notes WHERE topicId = :topicId")
    suspend fun deleteNotesByTopic(topicId: String)

    @Query("DELETE FROM study_notes")
    suspend fun clearAllNotes()
}
