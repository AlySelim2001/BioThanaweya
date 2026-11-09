package com.example.domain.engine

import com.example.domain.model.PacingCategory
import com.example.domain.model.QuestionPacingRecord
import com.example.presentation.components.formatSeconds
import org.junit.Assert.*
import org.junit.Test

/**
 * PracticePacingEngineTest:
 * Unit tests validating pacing category classification, exam timer formatting,
 * and pacing efficiency calculations grounded in Thanweya Amma exam constraints.
 */
class PracticePacingEngineTest {

    @Test
    fun testPacingCategoryClassification() {
        assertEquals(PacingCategory.FAST, PacingCategory.fromSeconds(25))
        assertEquals(PacingCategory.FAST, PacingCategory.fromSeconds(44))
        assertEquals(PacingCategory.OPTIMAL, PacingCategory.fromSeconds(45))
        assertEquals(PacingCategory.OPTIMAL, PacingCategory.fromSeconds(75))
        assertEquals(PacingCategory.OPTIMAL, PacingCategory.fromSeconds(90))
        assertEquals(PacingCategory.OVERTIME, PacingCategory.fromSeconds(91))
        assertEquals(PacingCategory.OVERTIME, PacingCategory.fromSeconds(150))
    }

    @Test
    fun testFormatSecondsHelper() {
        assertEquals("00:00", formatSeconds(0))
        assertEquals("00:45", formatSeconds(45))
        assertEquals("01:30", formatSeconds(90))
        assertEquals("05:25", formatSeconds(325))
        assertEquals("15:00", formatSeconds(900))
    }

    @Test
    fun testOptimalPacingAnalysisGeneratesHighEfficiency() {
        val records = listOf(
            QuestionPacingRecord(
                questionIndex = 0,
                questionPrompt = "Explain the role of ATP in muscle contraction.",
                timeSpentSeconds = 60,
                pacingCategory = PacingCategory.OPTIMAL,
                scoreAchieved = 2.0,
                isFullMark = true,
                missingKeywordsCount = 0
            ),
            QuestionPacingRecord(
                questionIndex = 1,
                questionPrompt = "Describe the sliding filament mechanism.",
                timeSpentSeconds = 75,
                pacingCategory = PacingCategory.OPTIMAL,
                scoreAchieved = 2.0,
                isFullMark = true,
                missingKeywordsCount = 0
            )
        )

        val summary = PacingEfficiencyEngine.analyzePacing(records, totalSessionSeconds = 135)

        assertEquals(2, summary.questionsCount)
        assertEquals(2, summary.optimalCount)
        assertEquals(0, summary.fastCount)
        assertEquals(0, summary.overtimeCount)
        assertEquals(67, summary.averageSecondsPerQuestion)
        assertEquals(100, summary.pacingEfficiencyScore)
        assertEquals("كفاءة زمنية استثنائية", summary.pacingRatingArabic)
    }

    @Test
    fun testRushedQuestionsWithMissingKeywordsPenalizesScore() {
        val records = listOf(
            QuestionPacingRecord(
                questionIndex = 0,
                questionPrompt = "Role of cross bridges in rigor mortis.",
                timeSpentSeconds = 20,
                pacingCategory = PacingCategory.FAST,
                scoreAchieved = 0.5,
                isFullMark = false,
                missingKeywordsCount = 3
            ),
            QuestionPacingRecord(
                questionIndex = 1,
                questionPrompt = "Action potential transmission at neuromuscular junction.",
                timeSpentSeconds = 30,
                pacingCategory = PacingCategory.FAST,
                scoreAchieved = 1.0,
                isFullMark = false,
                missingKeywordsCount = 2
            )
        )

        val summary = PacingEfficiencyEngine.analyzePacing(records, totalSessionSeconds = 50)

        assertEquals(2, summary.fastCount)
        assertEquals(0, summary.optimalCount)
        assertTrue(summary.pacingEfficiencyScore < 80)
        assertTrue(summary.executiveSummaryArabic.contains("التسرع الزمني"))
    }

    @Test
    fun testOvertimePacingGeneratesThanweyaExamWarning() {
        val records = listOf(
            QuestionPacingRecord(
                questionIndex = 0,
                questionPrompt = "Immune response stages.",
                timeSpentSeconds = 180,
                pacingCategory = PacingCategory.OVERTIME,
                scoreAchieved = 2.0,
                isFullMark = true,
                missingKeywordsCount = 0
            )
        )

        val summary = PacingEfficiencyEngine.analyzePacing(records, totalSessionSeconds = 180)

        assertEquals(1, summary.overtimeCount)
        assertEquals(180, summary.averageSecondsPerQuestion)
        assertTrue(summary.thanweyaAmmaTip.contains("46 سؤالاً في 180 دقيقة"))
    }
}
