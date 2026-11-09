package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * SearchHistoryDao:
 * Room Data Access Object for persisting and querying recent search keywords offline.
 */
@Dao
interface SearchHistoryDao {

    @Query("SELECT query FROM search_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentQueries(limit: Int = 10): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchHistoryEntity)

    @Query("DELETE FROM search_history WHERE query = :query")
    suspend fun deleteSearch(query: String)

    @Query("DELETE FROM search_history")
    suspend fun clearHistory()
}
