package com.example.domain.engine

import com.example.data.provider.MinistryOfficialExamsProvider
import org.junit.Assert.*
import org.junit.Test

class MinistryExamScoringEngineTest {

    @Test
    fun testOfficial2024PaperSpecifications() {
        val paper = MinistryOfficialExamsProvider.getPaperById("exam_2024_s1")
        assertEquals(46, paper.questions.size)
        assertEquals(60.0, paper.totalMarks, 0.001)

        val mcqs = paper.questions.filter { it.type == com.example.data.model.QuestionType.MCQ }
        assertEquals(44, mcqs.size)

        val oneMarkMCQs = mcqs.filter { it.marks == 1.0 }
        val twoMarkMCQs = mcqs.filter { it.marks == 2.0 }
        assertEquals(32, oneMarkMCQs.size)
        assertEquals(12, twoMarkMCQs.size)

        val essays = paper.questions.filter { it.type != com.example.data.model.QuestionType.MCQ }
        assertEquals(2, essays.size)
        assertEquals(2.0, essays[0].marks, 0.001)
        assertEquals(2.0, essays[1].marks, 0.001)
    }

    @Test
    fun testFullMarkEvaluationCalculations() {
        val paper = MinistryOfficialExamsProvider.getPaperById("exam_2024_s1")
        val selectedOptions = mutableMapOf<Int, Int>()

        for ((index, q) in paper.questions.withIndex()) {
            if (q.type == com.example.data.model.QuestionType.MCQ) {
                selectedOptions[index] = q.correctOptionIndex
            }
        }

        val essayAnswers = mapOf(
            44 to "نقص أو غياب أيونات الكالسيوم فلا يتحرر الأستيل كولين ونقص جزيئات ATP اللازمة لتكوين وسحب الروابط المستعرضة",
            45 to "لا يتعرف عامل الإطلاق release factor على كودون الوقف وتستمر عملية الترجمة وإضافة أحماض أمينية جديدة"
        )

        val report = MinistryExamScoringEngine.gradeExamSession(
            paper = paper,
            selectedOptions = selectedOptions,
            essayAnswers = essayAnswers,
            timeSpentSeconds = 3600
        )

        assertEquals(60.0, report.totalScoreAwarded, 0.5)
        assertTrue(report.percentage >= 98.0)
        assertTrue(report.nationalPercentile >= 99.0)
        assertEquals("ممتاز مع مرتبة الشرف", report.ministryGradeLabelArabic)
        assertEquals(0, report.fallenTraps.size)
    }

    @Test
    fun testTrapDetectionWhenAnsweringIncorrectly() {
        val paper = MinistryOfficialExamsProvider.getPaperById("exam_2024_s1")
        val selectedOptions = mutableMapOf<Int, Int>()

        // Answer incorrectly for question 0 (q 1)
        selectedOptions[0] = 3

        val report = MinistryExamScoringEngine.gradeExamSession(
            paper = paper,
            selectedOptions = selectedOptions,
            essayAnswers = emptyMap(),
            timeSpentSeconds = 1200
        )

        assertTrue(report.fallenTraps.isNotEmpty())
        assertEquals(1, report.fallenTraps.first().first.number)
    }

    @Test
    fun testChapterQuizPaperGeneration() {
        val quiz = MinistryOfficialExamsProvider.createChapterQuizPaper(
            chapterKey = "immunity",
            chapterTitleArabic = "المناعة في الكائنات الحية"
        )
        assertTrue(quiz.questions.isNotEmpty())
        assertTrue(quiz.durationMinutes > 0)
        assertTrue(quiz.totalMarks > 0)
        assertEquals(1, quiz.questions.first().number)
    }

    @Test
    fun testMistakeRemediationPaperGeneration() {
        val mistakeNumbers = setOf(1, 3, 5)
        val remediation = MinistryOfficialExamsProvider.createMistakeReviewPaper(mistakeNumbers)
        assertEquals(3, remediation.questions.size)
        assertEquals(1, remediation.questions[0].number)
        assertEquals(2, remediation.questions[1].number)
        assertEquals(3, remediation.questions[2].number)
    }
}
