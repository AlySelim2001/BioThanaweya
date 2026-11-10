package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.TopicMasteryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicMasteryDao {
    @Query("SELECT * FROM topic_mastery")
    fun getAllMasteries(): Flow<List<TopicMasteryEntity>>

    @Query("SELECT * FROM topic_mastery WHERE topicId = :topicId LIMIT 1")
    fun getMasteryForTopic(topicId: String): Flow<TopicMasteryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMastery(mastery: TopicMasteryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMasteries(masteries: List<TopicMasteryEntity>)
}
