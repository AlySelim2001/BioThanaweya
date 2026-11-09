package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.ChapterContentEntity
import kotlinx.coroutines.flow.Flow

/**
 * ChapterContentDao:
 * Room Data Access Object providing offline-first full-text search across
 * biology chapters, subtopics, scientific concepts, and exam traps.
 */
@Dao
interface ChapterContentDao {

    @Query("SELECT * FROM chapter_contents ORDER BY chapterId ASC, id ASC")
    fun getAllContents(): Flow<List<ChapterContentEntity>>

    @Query("""
        SELECT * FROM chapter_contents 
        WHERE normalizedSearchIndex LIKE '%' || :normalizedQuery || '%'
        ORDER BY 
            CASE 
                WHEN sectionTitleAr LIKE '%' || :rawQuery || '%' THEN 1
                WHEN keywordsAr LIKE '%' || :rawQuery || '%' THEN 2
                WHEN chapterTitleAr LIKE '%' || :rawQuery || '%' THEN 3
                ELSE 4
            END ASC,
            importanceScore DESC
    """)
    fun searchContent(normalizedQuery: String, rawQuery: String): Flow<List<ChapterContentEntity>>

    @Query("""
        SELECT * FROM chapter_contents 
        WHERE chapterId = :chapterId 
          AND normalizedSearchIndex LIKE '%' || :normalizedQuery || '%'
        ORDER BY importanceScore DESC
    """)
    fun searchContentByChapter(normalizedQuery: String, chapterId: Int): Flow<List<ChapterContentEntity>>

    @Query("""
        SELECT * FROM chapter_contents 
        WHERE unitNumber = :unitNumber 
          AND normalizedSearchIndex LIKE '%' || :normalizedQuery || '%'
        ORDER BY chapterId ASC, importanceScore DESC
    """)
    fun searchContentByUnit(normalizedQuery: String, unitNumber: Int): Flow<List<ChapterContentEntity>>

    @Query("""
        SELECT * FROM chapter_contents 
        WHERE (:unitNumber IS NULL OR unitNumber = :unitNumber)
          AND (:chapterId IS NULL OR chapterId = :chapterId)
          AND normalizedSearchIndex LIKE '%' || :normalizedQuery || '%'
        ORDER BY 
            CASE 
                WHEN sectionTitleAr LIKE '%' || :rawQuery || '%' THEN 1
                WHEN keywordsAr LIKE '%' || :rawQuery || '%' THEN 2
                ELSE 3
            END ASC,
            importanceScore DESC
    """)
    fun searchWithFilters(
        normalizedQuery: String,
        rawQuery: String,
        unitNumber: Int?,
        chapterId: Int?
    ): Flow<List<ChapterContentEntity>>

    @Query("SELECT * FROM chapter_contents WHERE chapterId = :chapterId ORDER BY id ASC")
    fun getContentsByChapter(chapterId: Int): Flow<List<ChapterContentEntity>>

    @Query("SELECT * FROM chapter_contents WHERE id = :id LIMIT 1")
    fun getContentById(id: String): Flow<ChapterContentEntity?>

    @Query("SELECT * FROM chapter_contents WHERE id = :id LIMIT 1")
    suspend fun getContentByIdSync(id: String): ChapterContentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contents: List<ChapterContentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(content: ChapterContentEntity)

    @Query("SELECT COUNT(*) FROM chapter_contents")
    suspend fun getCount(): Int

    @Query("DELETE FROM chapter_contents")
    suspend fun deleteAll()
}
