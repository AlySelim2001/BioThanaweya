package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.TopicMasteryEntity
import kotlinx.coroutines.flow.Flow

/**
 * TopicMasteryDao:
 * Access methods for student topic mastery heatmap visualization.
 */
@Dao
interface TopicMasteryDao {
    @Query("SELECT * FROM topic_mastery ORDER BY unitNumber ASC, topicId ASC")
    fun getAllMasteries(): Flow<List<TopicMasteryEntity>>

    @Query("SELECT * FROM topic_mastery WHERE topicId = :topicId")
    fun getMasteryForTopic(topicId: String): Flow<TopicMasteryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMastery(mastery: TopicMasteryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMasteries(masteries: List<TopicMasteryEntity>)
}
