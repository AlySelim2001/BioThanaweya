package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.SavedBiologyDefinitionEntity
import kotlinx.coroutines.flow.Flow

/**
 * SavedDefinitionDao:
 * Room Data Access Object for student bookmarked biology definitions.
 */
@Dao
interface SavedDefinitionDao {
    @Query("SELECT * FROM saved_definitions ORDER BY savedAtEpoch DESC")
    fun getAllSavedDefinitions(): Flow<List<SavedBiologyDefinitionEntity>>

    @Query("SELECT * FROM saved_definitions WHERE unitCategory = :unitCategory ORDER BY term ASC")
    fun getSavedDefinitionsByUnit(unitCategory: String): Flow<List<SavedBiologyDefinitionEntity>>

    @Query("SELECT * FROM saved_definitions WHERE term = :term LIMIT 1")
    suspend fun getDefinitionByTerm(term: String): SavedBiologyDefinitionEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM saved_definitions WHERE term = :term)")
    fun isTermSaved(term: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(definition: SavedBiologyDefinitionEntity): Long

    @Query("UPDATE saved_definitions SET studentNotes = :notes WHERE id = :id")
    suspend fun updateStudentNotes(id: Long, notes: String)

    @Query("UPDATE saved_definitions SET masteryLevel = :masteryLevel WHERE id = :id")
    suspend fun updateMasteryLevel(id: Long, masteryLevel: String)

    @Delete
    suspend fun delete(definition: SavedBiologyDefinitionEntity)

    @Query("DELETE FROM saved_definitions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM saved_definitions WHERE term = :term")
    suspend fun deleteByTerm(term: String)

    @Query("SELECT COUNT(*) FROM saved_definitions")
    fun getSavedCount(): Flow<Int>
}
