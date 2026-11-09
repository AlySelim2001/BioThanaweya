package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.KeyConceptEntity
import kotlinx.coroutines.flow.Flow

/**
 * KeyConceptDao:
 * Room Data Access Object for cached offline key concepts.
 */
@Dao
interface KeyConceptDao {
    @Query("SELECT * FROM key_concepts ORDER BY unitCategory ASC, topicId ASC")
    fun getAllKeyConcepts(): Flow<List<KeyConceptEntity>>

    @Query("SELECT * FROM key_concepts WHERE topicId = :topicId")
    fun getKeyConceptsByTopic(topicId: String): Flow<List<KeyConceptEntity>>

    @Query("SELECT * FROM key_concepts WHERE unitCategory = :unitCategory ORDER BY chapterId ASC")
    fun getKeyConceptsByUnit(unitCategory: String): Flow<List<KeyConceptEntity>>

    @Query("SELECT * FROM key_concepts WHERE conceptId = :conceptId LIMIT 1")
    fun getKeyConceptById(conceptId: String): Flow<KeyConceptEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(concept: KeyConceptEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(concepts: List<KeyConceptEntity>)

    @Query("DELETE FROM key_concepts WHERE conceptId = :conceptId")
    suspend fun deleteById(conceptId: String)

    @Query("SELECT COUNT(*) FROM key_concepts")
    fun getConceptsCount(): Flow<Int>
}
