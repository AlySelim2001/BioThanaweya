package com.example

import com.example.data.model.QuestionType
import com.example.data.repository.BiologyCurriculumRepository
import org.junit.Assert.*
import org.junit.Test

/**
 * Senior QA & Curriculum Audit Test:
 * Enforces 100% complete curriculum coverage across all 11 required Egyptian Secondary Biology chapters.
 * Fails if any chapter or topic is missing, incomplete, or lacks required pedagogical artifacts.
 */
class CurriculumCoverageTest {

    private val required11ChapterKeywords = listOf(
        "الدعامة والحركة",
        "التغذية",
        "النقل",
        "التنفس",
        "الإخراج",
        "التنسيق العصبي",
        "التنسيق الهرموني",
        "التكاثر",
        "DNA",
        "RNA",
        "التقنية الحيوية"
    )

    @Test
    fun `curriculum covers at least 11 comprehensive chapters`() {
        val chapters = BiologyCurriculumRepository.chapters
        assertTrue("Curriculum must contain at least 11 chapters, found: ${chapters.size}", chapters.size >= 11)

        val allArabicTitles = chapters.map { it.arabicTitle }.joinToString(" | ")

        for (keyword in required11ChapterKeywords) {
            val covered = chapters.any { it.arabicTitle.contains(keyword, ignoreCase = true) || it.title.contains(keyword, ignoreCase = true) }
            assertTrue(
                "Curriculum is missing mandatory chapter keyword: '$keyword'. Found chapters: $allArabicTitles",
                covered
            )
        }
    }

    @Test
    fun `every topic contains an in-depth explanation, MCQ, and essay question`() {
        val subTopics = BiologyCurriculumRepository.subTopics
        assertTrue("Must contain at least 11 subtopics, found: ${subTopics.size}", subTopics.size >= 11)

        for (subTopic in subTopics) {
            val pack = BiologyCurriculumRepository.getMasterStudyPack(subTopic.id)
            assertNotNull("MasterStudyPack for '${subTopic.id}' must not be null", pack)

            // 1. Check Explanations (ELI5 and Deep Technical Breakdown)
            val explanation = pack.arabicEli5.ifBlank { pack.eli5Analogy }
            assertTrue("Topic '${subTopic.id}' must have a non-blank ELI5 explanation", explanation.isNotBlank())
            assertTrue(
                "Topic '${subTopic.id}' must have a thorough technical breakdown (min 100 chars)",
                pack.deepTechnicalBreakdown.trim().length >= 100
            )

            // 2. Check Multiple Choice Question (MCQ)
            val mcqQuestions = pack.questions.filter { it.type == QuestionType.MCQ }
            assertTrue(
                "Topic '${subTopic.id}' must have at least one MCQ question. Found: ${mcqQuestions.size}",
                mcqQuestions.isNotEmpty()
            )
            val firstMcq = mcqQuestions.first()
            assertTrue("MCQ in '${subTopic.id}' must have at least 2 options", firstMcq.options.size >= 2)
            assertTrue("MCQ in '${subTopic.id}' must specify a valid correct option index", firstMcq.correctOptionIndex >= 0)
            assertTrue("MCQ in '${subTopic.id}' must have an official answer", firstMcq.officialAnswer.isNotBlank())
            assertTrue("MCQ in '${subTopic.id}' must have a scientific rationale", firstMcq.rationale.isNotBlank())

            // 3. Check Essay / Free Response Question (FRQ)
            val hasEssayQuestion = pack.examSimulationItem.prompt.isNotBlank() ||
                    pack.questions.any { it.type == QuestionType.GIVE_REASON || it.type == QuestionType.WHAT_HAPPENS_IF || it.type == QuestionType.SCIENTIFIC_TERM }
            assertTrue("Topic '${subTopic.id}' must contain an essay/free-response question with mark scheme", hasEssayQuestion)
            assertTrue("Exam simulation item prompt in '${subTopic.id}' must not be blank", pack.examSimulationItem.prompt.isNotBlank())
            assertTrue("Exam simulation model answer in '${subTopic.id}' must not be blank", pack.examSimulationItem.modelAnswer.isNotBlank())
            assertTrue("Exam simulation in '${subTopic.id}' must have required keywords", pack.examSimulationItem.requiredKeywords.isNotEmpty())

            // 4. Check Visual Diagram (ASCII Schematic)
            assertTrue("Topic '${subTopic.id}' must have an ASCII schematic diagram", pack.asciiSchematic.isNotBlank())

            // 5. Check Flashcards for Room Seeding and FSRS
            assertTrue("Topic '${subTopic.id}' must contain flashcards for spaced repetition", pack.flashcards.isNotEmpty())
        }
    }

    @Test
    fun `no placeholder or TODO tags exist in curriculum data`() {
        val subTopics = BiologyCurriculumRepository.subTopics
        for (subTopic in subTopics) {
            val pack = BiologyCurriculumRepository.getMasterStudyPack(subTopic.id)
            assertFalse("Topic '${subTopic.id}' contains 'TODO' tag", pack.deepTechnicalBreakdown.contains("TODO"))
            assertFalse("Topic '${subTopic.id}' contains 'placeholder' tag", pack.deepTechnicalBreakdown.contains("placeholder", ignoreCase = true))
            assertFalse("Topic '${subTopic.id}' contains 'dummy' tag", pack.deepTechnicalBreakdown.contains("dummy", ignoreCase = true))
        }
    }
}
