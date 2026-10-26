package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

/**
 * FlashcardDao:
 * Access methods for FSRS-scheduled biology flashcards.
 */
@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards WHERE topicId = :topicId")
    fun getCardsByTopic(topicId: String): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE dueEpoch <= :nowEpoch ORDER BY dueEpoch ASC")
    fun getDueCards(nowEpoch: Long): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards")
    suspend fun getAllCardsSync(): List<FlashcardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<FlashcardEntity>)

    @Update
    suspend fun updateCard(card: FlashcardEntity)

    @Query("SELECT COUNT(*) FROM flashcards")
    fun getTotalCardsCount(): Flow<Int>
}
