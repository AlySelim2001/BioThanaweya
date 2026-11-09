package com.example.data.local

import com.example.data.local.dao.ChapterContentDao
import com.example.data.local.dao.SearchHistoryDao
import com.example.data.local.entity.ChapterContentEntity
import com.example.data.local.entity.SearchHistoryEntity
import com.example.data.provider.ThanawyaAmmaCurriculumContent
import com.example.data.repository.ChapterSearchRepositoryImpl
import com.example.util.ArabicSearchUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * ChapterSearchRepositoryTest:
 * Unit tests validating Arabic search normalization, full-text Room search queries,
 * curriculum content coverage across all 11 Thanawya Amma chapters, and search history caching.
 */
class ChapterSearchRepositoryTest {

    @Test
    fun testArabicSearchUtilsNormalization() {
        // Tashkeel removal
        assertEquals("احياء", ArabicSearchUtils.normalize("أَحْيَاءٌ"))

        // Hamza unification
        assertEquals("اوكسينات", ArabicSearchUtils.normalize("أوكسينات"))
        assertEquals("انقباض", ArabicSearchUtils.normalize("إنقباض"))
        assertEquals("الاميبا", ArabicSearchUtils.normalize("الأميبا"))

        // Alef maksura and ta marbuta
        assertEquals("عضله هيكليه", ArabicSearchUtils.normalize("عضلة هيكلية"))
        assertEquals("عدوي", ArabicSearchUtils.normalize("عدوى"))

        // Tatweel removal
        assertEquals("ساركومير", ArabicSearchUtils.normalize("ســــاركومير"))
    }

    @Test
    fun testArabicSearchUtilsMatching() {
        // Matching despite spelling / hamza variations
        assertTrue(ArabicSearchUtils.matches("الأوكسينات في النبات", "اوكسين"))
        assertTrue(ArabicSearchUtils.matches("انقباض العضلة الهيكلية", "عضله"))
        assertTrue(ArabicSearchUtils.matches("تضاعف حمض DNA وإنزيم بلمرة", "DNA"))
        assertTrue(ArabicSearchUtils.matches("دورة كريبس وسلسلة نقل الإلكترون", "كربس"))

        // Multi-word query matching
        assertTrue(ArabicSearchUtils.matches("انقباض العضلة الهيكلية ونظرية الخيوط المنزلقة", "عضلة منزلقة"))
    }

    @Test
    fun testSnippetExtraction() {
        val sampleText = "تفرز الغدة النخامية هرمون النمو GH المتحكم في أيض البروتينات، ونقصه يسبب القزامة وزيادته تسبب العملقة أو الأكروميجالي في البالغين."
        val snippet = ArabicSearchUtils.extractSnippet(sampleText, "القزامة", maxLength = 60)

        assertNotNull(snippet)
        assertTrue("Snippet must contain target or match", snippet.contains("القزامة"))
    }

    @Test
    fun testRelevanceScoringOrder() {
        val titleScore = ArabicSearchUtils.calculateScore(
            query = "ساركومير",
            sectionTitle = "الساركومير والقطعة العضلية",
            keywords = "أكتين ميوسين",
            chapterTitle = "الدعامة والحركة",
            content = "شرح العضلة",
            baseImportance = 10
        )

        val contentOnlyScore = ArabicSearchUtils.calculateScore(
            query = "ساركومير",
            sectionTitle = "المفاصل والأربطة",
            keywords = "وتر أخيل",
            chapterTitle = "الدعامة والحركة",
            content = "تحتوي اللييفة على قطع عضلية تدعى ساركومير",
            baseImportance = 5
        )

        assertTrue("Title match must score substantially higher than body-only match", titleScore > contentOnlyScore)
    }

    @Test
    fun testThanawyaAmmaCurriculumCoverageAll11Chapters() {
        val allContents = ThanawyaAmmaCurriculumContent.ALL_CHAPTER_CONTENTS

        assertTrue("Must have multiple content sections", allContents.size >= 11)

        // Verify chapter IDs 1 to 11 are all represented
        val coveredChapters = allContents.map { it.chapterId }.toSet()
        val expectedChapters = (1..11).toSet()
        assertEquals("Curriculum must cover every single chapter from 1 to 11", expectedChapters, coveredChapters)

        // Verify Unit distribution
        val unit1Contents = allContents.filter { it.unitNumber == 1 }
        val unit2Contents = allContents.filter { it.unitNumber == 2 }
        assertTrue("Unit 1 must contain contents", unit1Contents.isNotEmpty())
        assertTrue("Unit 2 must contain molecular biology contents", unit2Contents.isNotEmpty())

        // Verify high-yield key terms exist
        val allText = allContents.joinToString(" ") { "${it.sectionTitleAr} ${it.keywordsAr} ${it.contentAr}" }
        val requiredTerms = listOf(
            "ساركومير",
            "أوكسينات",
            "ATP",
            "كريبس",
            "نيفرون",
            "استيل كولين",
            "توالد بكري",
            "جسم مضاد",
            "DNA",
            "RNA",
            "بلازميد"
        )

        for (term in requiredTerms) {
            assertTrue("Curriculum dataset must contain key biology concept '$term'", allText.contains(term))
        }

        // Verify normalized index is populated for fast lookup
        allContents.forEach { entity ->
            assertTrue("Normalized index must not be empty for ${entity.id}", entity.normalizedSearchIndex.isNotBlank())
            assertTrue("Content must have exam notes or takeaways", entity.examNotesAr.isNotBlank())
        }
    }

    @Test
    fun testSearchRepositorySearchAndFilters() = runBlocking {
        val fakeContents = ThanawyaAmmaCurriculumContent.ALL_CHAPTER_CONTENTS

        val fakeContentDao = object : ChapterContentDao {
            override fun getAllContents() = flowOf(fakeContents)

            override fun searchContent(normalizedQuery: String, rawQuery: String) = flowOf(
                fakeContents.filter { it.normalizedSearchIndex.contains(normalizedQuery) }
            )

            override fun searchContentByChapter(normalizedQuery: String, chapterId: Int) = flowOf(
                fakeContents.filter { it.chapterId == chapterId && it.normalizedSearchIndex.contains(normalizedQuery) }
            )

            override fun searchContentByUnit(normalizedQuery: String, unitNumber: Int) = flowOf(
                fakeContents.filter { it.unitNumber == unitNumber && it.normalizedSearchIndex.contains(normalizedQuery) }
            )

            override fun searchWithFilters(
                normalizedQuery: String,
                rawQuery: String,
                unitNumber: Int?,
                chapterId: Int?
            ) = flowOf(
                fakeContents.filter {
                    val matchesQuery = it.normalizedSearchIndex.contains(normalizedQuery)
                    val matchesUnit = unitNumber == null || it.unitNumber == unitNumber
                    val matchesChapter = chapterId == null || it.chapterId == chapterId
                    matchesQuery && matchesUnit && matchesChapter
                }
            )

            override fun getContentsByChapter(chapterId: Int) = flowOf(
                fakeContents.filter { it.chapterId == chapterId }
            )

            override fun getContentById(id: String) = flowOf(fakeContents.find { it.id == id })
            override suspend fun getContentByIdSync(id: String) = fakeContents.find { it.id == id }
            override suspend fun insertAll(contents: List<ChapterContentEntity>) {}
            override suspend fun insertOrUpdate(content: ChapterContentEntity) {}
            override suspend fun getCount() = fakeContents.size
            override suspend fun deleteAll() {}
        }

        val recordedHistory = mutableListOf<String>()
        val fakeHistoryDao = object : SearchHistoryDao {
            override fun getRecentQueries(limit: Int) = flowOf(recordedHistory)
            override suspend fun insertSearch(search: SearchHistoryEntity) {
                recordedHistory.add(search.query)
            }
            override suspend fun deleteSearch(query: String) {
                recordedHistory.remove(query)
            }
            override suspend fun clearHistory() {
                recordedHistory.clear()
            }
        }

        val repository = ChapterSearchRepositoryImpl(fakeContentDao, fakeHistoryDao)

        // Test search by keyword "ساركومير"
        val sarcomereResults = repository.search(query = "ساركومير").first()
        assertTrue("Search for 'ساركومير' must return results", sarcomereResults.isNotEmpty())
        assertEquals(1, sarcomereResults.first().content.chapterId)

        // Test search by keyword "DNA"
        val dnaResults = repository.search(query = "DNA").first()
        assertTrue("Search for 'DNA' must return results", dnaResults.isNotEmpty())
        assertTrue("DNA results must contain chapter 10 or 11", dnaResults.any { it.content.chapterId in 10..11 })

        // Test unit filter: Unit 2 only
        val unit2Results = repository.search(query = "تضاعف", unitNumber = 2).first()
        assertTrue("Unit 2 results must only contain unit 2", unit2Results.all { it.content.unitNumber == 2 })

        // Test history recording
        repository.recordSearch("أوكسينات", 3)
        assertTrue(recordedHistory.contains("أوكسينات"))

        repository.deleteRecentSearch("أوكسينات")
        assertFalse(recordedHistory.contains("أوكسينات"))
    }
}
