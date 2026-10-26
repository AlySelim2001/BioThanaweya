package com.example

import com.example.data.repository.BiologyCurriculumRepository
import org.junit.Assert.*
import org.junit.Test

class CurriculumCoverageTest {

    @Test
    fun `verify curriculum completeness for every subtopic in Units 1 and 2`() {
        val topics = BiologyCurriculumRepository.subTopics
        assertFalse("Curriculum must have registered subtopics", topics.isEmpty())

        for (topic in topics) {
            val pack = BiologyCurriculumRepository.getMasterStudyPack(topic.id)

            // 1. ELI5 explanation must not be blank
            assertNotNull("ELI5 must not be null for ${topic.id}", pack.eli5Analogy)
            assertTrue("ELI5 must have substantive content for ${topic.id}", pack.eli5Analogy.length > 30)

            // 2. Arabic ELI5 must be present
            assertNotNull("Arabic ELI5 must not be null for ${topic.id}", pack.arabicEli5)
            assertTrue("Arabic ELI5 must have substantive Arabic content for ${topic.id}", pack.arabicEli5.length > 20)

            // 3. Textbook page citation must be present
            assertNotNull("Textbook page citation must not be null for ${topic.id}", pack.textbookPageReference)
            assertTrue("Textbook page citation must cite Ministry textbook for ${topic.id}", pack.textbookPageReference.contains("Ministry Textbook"))

            // 4. Deep Technical Breakdown must not be blank
            assertNotNull("Deep breakdown must not be null for ${topic.id}", pack.deepTechnicalBreakdown)
            assertTrue("Deep breakdown must have scientific depth for ${topic.id}", pack.deepTechnicalBreakdown.length > 50)

            // 5. At least 1 ASCII diagram
            assertNotNull("ASCII schematic must not be null for ${topic.id}", pack.asciiSchematic)
            assertTrue("ASCII schematic must be present for ${topic.id}", pack.asciiSchematic.isNotBlank())

            // 6. Must contain questions
            assertNotNull("Questions list must not be null for ${topic.id}", pack.questions)
            assertTrue("Must contain question items for ${topic.id}", pack.questions.isNotEmpty())
        }
    }

    @Test
    fun `verify simulated exam pool and topic masteries`() {
        val examPool = BiologyCurriculumRepository.getSimulatedMinistryExamPool()
        assertTrue("Exam pool must have at least 10 high-yield questions", examPool.size >= 10)

        val masteries = BiologyCurriculumRepository.getTopicMasteries()
        assertEquals("Mastery entries must match number of subtopics", BiologyCurriculumRepository.subTopics.size, masteries.size)
        assertTrue("All masteries must have positive stability", masteries.all { it.stability > 0.0 })
    }
}
