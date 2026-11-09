package com.example.data.local

import com.example.data.local.entity.KeyConceptEntity
import com.example.data.local.entity.SavedBiologyDefinitionEntity
import com.example.data.local.entity.StudentProgressEntity
import org.junit.Assert.*
import org.junit.Test

/**
 * RoomDatabaseSchemaTest:
 * Unit tests validating schema models and integrity for local storage of
 * student progress, saved biology definitions, and offline key concepts.
 */
class RoomDatabaseSchemaTest {

    @Test
    fun testStudentProgressEntityDefaultsAndValues() {
        val progress = StudentProgressEntity(
            id = 1,
            streakDays = 5,
            totalXp = 1250,
            questionsGraded = 25,
            examsCompleted = 2,
            totalStudyMinutes = 120,
            overallMasteryPercentage = 0.85f,
            projectedPercentile = 98.2f,
            savedDefinitionsCount = 14
        )

        assertEquals(1, progress.id)
        assertEquals(5, progress.streakDays)
        assertEquals(1250, progress.totalXp)
        assertEquals(25, progress.questionsGraded)
        assertEquals(2, progress.examsCompleted)
        assertEquals(120, progress.totalStudyMinutes)
        assertEquals(0.85f, progress.overallMasteryPercentage, 0.001f)
        assertEquals(98.2f, progress.projectedPercentile, 0.001f)
        assertEquals(14, progress.savedDefinitionsCount)
    }

    @Test
    fun testSavedBiologyDefinitionEntityCreation() {
        val definition = SavedBiologyDefinitionEntity(
            id = 101L,
            term = "Sarcomere",
            arabicTerm = "القطعة العضلية",
            definition = "The distance between two successive Z-lines in a myofibril.",
            arabicDefinition = "المسافة بين خطي Z متتاليين في اللييفة العضلية وهي أصغر وحدة انقباض.",
            chapterTitle = "Support & Movement",
            unitCategory = "UNIT_1",
            isBookmarked = true,
            studentNotes = "Focus on H-zone changes during contraction",
            masteryLevel = "MASTERED"
        )

        assertEquals(101L, definition.id)
        assertEquals("Sarcomere", definition.term)
        assertEquals("القطعة العضلية", definition.arabicTerm)
        assertEquals("UNIT_1", definition.unitCategory)
        assertTrue(definition.isBookmarked)
        assertEquals("MASTERED", definition.masteryLevel)
        assertEquals("Focus on H-zone changes during contraction", definition.studentNotes)
    }

    @Test
    fun testKeyConceptEntityOfflineAccessProperties() {
        val concept = KeyConceptEntity(
            conceptId = "sarcomere_huxley_sliding",
            topicId = "sarcomere_contraction",
            chapterId = "unit1_chap1",
            unitCategory = "UNIT_1",
            title = "Sliding Filament Theory",
            arabicTitle = "نظرية الخيوط المنزلقة",
            summary = "Myosin cross-bridges pull actin filaments via Ca2+ and ATP.",
            arabicEli5 = "أذرع تجديف تسحب الحبال نحو المركز.",
            keyTakeawaysJson = "A-band constant|I and H shorten|Ca2+ binds troponin",
            formulaOrRule = "Sarcomeres = Z-lines - 1",
            examTrapWarning = "Filament lengths do not shorten; they merely slide!",
            isAvailableOffline = true
        )

        assertEquals("sarcomere_huxley_sliding", concept.conceptId)
        assertEquals("sarcomere_contraction", concept.topicId)
        assertEquals("UNIT_1", concept.unitCategory)
        assertTrue(concept.isAvailableOffline)
        assertTrue(concept.formulaOrRule.contains("Sarcomeres = Z-lines - 1"))
        assertTrue(concept.examTrapWarning.contains("Filament lengths do not shorten"))
    }
}
