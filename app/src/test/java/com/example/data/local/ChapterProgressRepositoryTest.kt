package com.example.data.local

import com.example.data.local.dao.ChapterProgressDao
import com.example.data.local.entity.ChapterProgress
import com.example.data.repository.ChapterProgressRepositoryImpl
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * ChapterProgressRepositoryTest:
 * Unit tests validating ChapterProgress data model, 11-chapter Thanawya Amma curriculum
 * integrity, progress fractions, and repository operations.
 */
class ChapterProgressRepositoryTest {

    @Test
    fun testChapterProgressModelDefaultsAndCalculations() {
        val progress = ChapterProgress(
            chapterId = 1,
            chapterKey = "support_movement",
            unitNumber = 1,
            titleAr = "الدعامة والحركة في الكائنات الحية",
            titleEn = "Support and Movement",
            summaryAr = "الدعامة الفسيولوجية والتركيبية، ونظرية الخيوط المنزلقة.",
            iconName = "fitness_center",
            isCompleted = false,
            completionPercentage = 50.0f,
            questionsSolved = 6,
            totalQuestions = 12,
            flashcardsMastered = 9,
            totalFlashcards = 18,
            studyTimeMinutes = 45
        )

        assertEquals(1, progress.chapterId)
        assertEquals("support_movement", progress.chapterKey)
        assertEquals(1, progress.unitNumber)
        assertFalse(progress.isCompleted)
        assertEquals(0.5f, progress.progressFraction, 0.001f)
        assertEquals(0.5f, progress.questionsProgressFraction, 0.001f)
        assertEquals(0.5f, progress.flashcardsProgressFraction, 0.001f)
        assertEquals(45, progress.studyTimeMinutes)
    }

    @Test
    fun testDefault11ChaptersExactCountAndUniqueness() {
        val chapters = ChapterProgressRepositoryImpl.DEFAULT_11_CHAPTERS

        assertEquals("Must contain exactly 11 Thanawya Amma biology chapters", 11, chapters.size)

        // Verify chapter IDs 1 to 11
        val ids = chapters.map { it.chapterId }.sorted()
        assertEquals((1..11).toList(), ids)

        // Verify unique keys
        val keys = chapters.map { it.chapterKey }.toSet()
        assertEquals(11, keys.size)

        // Verify all have non-blank Arabic & English titles
        chapters.forEach { chapter ->
            assertTrue("Chapter ${chapter.chapterId} must have Arabic title", chapter.titleAr.isNotBlank())
            assertTrue("Chapter ${chapter.chapterId} must have English title", chapter.titleEn.isNotBlank())
            assertTrue("Unit must be 1 or 2", chapter.unitNumber in 1..2)
            assertFalse("Initial completion must be false", chapter.isCompleted)
            assertEquals("Initial percentage must be 0", 0f, chapter.completionPercentage, 0.001f)
            assertTrue("Total questions must be > 0", chapter.totalQuestions > 0)
            assertTrue("Total flashcards must be > 0", chapter.totalFlashcards > 0)
        }

        // Verify unit distribution (Unit 1: 9 chapters, Unit 2: 2 chapters)
        val unit1Chapters = chapters.filter { it.unitNumber == 1 }
        val unit2Chapters = chapters.filter { it.unitNumber == 2 }
        assertEquals(9, unit1Chapters.size)
        assertEquals(2, unit2Chapters.size)
    }

    @Test
    fun testRepositorySeedingWhenEmpty() = runBlocking {
        var insertedList: List<ChapterProgress>? = null

        val fakeDao = object : ChapterProgressDao {
            var count = 0

            override fun getAllChapterProgress() = flowOf(emptyList<ChapterProgress>())
            override fun getChapterProgressById(chapterId: Int) = flowOf<ChapterProgress?>(null)
            override suspend fun getChapterProgressByIdSync(chapterId: Int): ChapterProgress? = null
            override fun getChapterProgressByKey(key: String) = flowOf<ChapterProgress?>(null)
            override fun getCompletedChapters() = flowOf(emptyList<ChapterProgress>())
            override fun getCompletedChaptersCount() = flowOf(0)
            override fun getAverageCompletionPercentage() = flowOf(0f)
            override fun getChapterProgressByUnit(unitNumber: Int) = flowOf(emptyList<ChapterProgress>())
            override suspend fun insertOrUpdate(progress: ChapterProgress) {}
            override suspend fun insertAll(chapters: List<ChapterProgress>) {
                insertedList = chapters
                count = chapters.size
            }
            override suspend fun update(progress: ChapterProgress) {}
            override suspend fun updateCompletionStatus(chapterId: Int, isCompleted: Boolean, timestamp: Long) {}
            override suspend fun updateProgressPercentage(chapterId: Int, percentage: Float, timestamp: Long) {}
            override suspend fun incrementQuestionsSolved(chapterId: Int, count: Int, timestamp: Long) {}
            override suspend fun incrementFlashcardsMastered(chapterId: Int, count: Int, timestamp: Long) {}
            override suspend fun addStudyMinutes(chapterId: Int, minutes: Int, timestamp: Long) {}
            override suspend fun getTotalChapterCount(): Int = count
            override suspend fun deleteAll() {}
            override suspend fun resetAllProgress(timestamp: Long) {}
        }

        val repository = ChapterProgressRepositoryImpl(fakeDao)
        repository.seedDefault11ChaptersIfEmpty()

        assertNotNull("Chapters must be inserted", insertedList)
        assertEquals(11, insertedList?.size)
    }
}
