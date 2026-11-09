package com.example

import com.example.data.local.entity.TopicMasteryEntity
import com.example.data.repository.BiologyCurriculumRepository
import com.example.presentation.components.calculateUnitMastery
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UnitMasteryDashboardComponentTest {

    @Test
    fun testUnit1CalculationWithCustomRoomEntities() {
        val unit1Topics = BiologyCurriculumRepository.subTopics.filter { it.unit.name.contains("1") }
        assertTrue("Unit 1 must contain curriculum topics", unit1Topics.isNotEmpty())

        val mockEntities = unit1Topics.mapIndexed { index, subTopic ->
            TopicMasteryEntity(
                topicId = subTopic.id,
                unitNumber = 1,
                topicTitle = subTopic.title,
                masteryLevel = if (index % 2 == 0) 0.85f else 0.55f, // Alternate high and medium
                cardsReviewed = 10,
                averageStability = 12.0,
                lastUpdatedEpoch = System.currentTimeMillis()
            )
        }

        val result = calculateUnitMastery(
            unitNumber = 1,
            titleArabic = "الوحدة الأولى: التركيب والوظيفة في الكائنات الحية",
            titleEnglish = "Unit 1: Structure & Function in Living Organisms",
            allCurriculumTopics = unit1Topics,
            roomEntities = mockEntities
        )

        assertEquals(1, result.unitNumber)
        assertEquals(unit1Topics.size, result.totalTopicsCount)
        assertEquals(unit1Topics.size * 10, result.totalCardsReviewed)
        assertTrue("Mastery percentage should be between 0 and 100", result.masteryPercentage in 0..100)
        assertTrue("Progress fraction should match percentage / 100", result.progressFraction in 0f..1f)
        assertEquals(result.masteryPercentage / 100f, result.progressFraction, 0.01f)
        assertTrue("Mastered count must be non-negative", result.masteredTopicsCount >= 0)
    }

    @Test
    fun testUnit2CalculationWithEmptyEntitiesDefaultsGracefully() {
        val unit2Topics = BiologyCurriculumRepository.subTopics.filter { it.unit.name.contains("2") }
        assertTrue("Unit 2 must contain curriculum topics", unit2Topics.isNotEmpty())

        // Passing empty entities should use default mastery levels without throwing exceptions
        val result = calculateUnitMastery(
            unitNumber = 2,
            titleArabic = "الوحدة الثانية: البيولوجيا الجزيئية",
            titleEnglish = "Unit 2: Molecular Biology",
            allCurriculumTopics = unit2Topics,
            roomEntities = emptyList()
        )

        assertEquals(2, result.unitNumber)
        assertEquals(unit2Topics.size, result.totalTopicsCount)
        assertTrue("Default mastery percentage should be valid", result.masteryPercentage > 0)
        assertTrue("Total cards reviewed default should be positive", result.totalCardsReviewed > 0)
    }

    @Test
    fun testPerfectMasteryCalculates100Percent() {
        val unit1Topics = BiologyCurriculumRepository.subTopics.filter { it.unit.name.contains("1") }
        val perfectEntities = unit1Topics.map { subTopic ->
            TopicMasteryEntity(
                topicId = subTopic.id,
                unitNumber = 1,
                topicTitle = subTopic.title,
                masteryLevel = 1.0f,
                cardsReviewed = 25,
                averageStability = 30.0,
                lastUpdatedEpoch = System.currentTimeMillis()
            )
        }

        val result = calculateUnitMastery(
            unitNumber = 1,
            titleArabic = "الوحدة الأولى",
            titleEnglish = "Unit 1",
            allCurriculumTopics = unit1Topics,
            roomEntities = perfectEntities
        )

        assertEquals(100, result.masteryPercentage)
        assertEquals(1.0f, result.progressFraction, 0.001f)
        assertEquals(unit1Topics.size, result.masteredTopicsCount)
    }
}
