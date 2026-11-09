package com.example

import com.example.data.model.Flashcard
import com.example.domain.engine.AnatomicalDissectionProvider
import com.example.domain.engine.AnkiExportHelper
import com.example.domain.engine.EmpatheticCompanionEngine
import com.example.domain.model.StudentComprehensionLevel
import com.example.domain.model.StudentSentiment
import org.junit.Assert.*
import org.junit.Test

class EmpatheticCompanionTest {

    @Test
    fun testSentimentDetectionKeywords() {
        assertEquals(StudentSentiment.FATIGUED, EmpatheticCompanionEngine.detectSentiment("أنا تعبت ومش قادر أكمل دلوقتي"))
        assertEquals(StudentSentiment.ANXIOUS, EmpatheticCompanionEngine.detectSentiment("خايف جداً ومتوتر من امتحان الوزارة"))
        assertEquals(StudentSentiment.CONFUSED, EmpatheticCompanionEngine.detectSentiment("مش فاهم الحتة دي وملخبطة خالص"))
        assertEquals(StudentSentiment.CONFIDENT, EmpatheticCompanionEngine.detectSentiment("أنا واثق ومستعد وممتاز"))
        assertEquals(StudentSentiment.CURIOUS, EmpatheticCompanionEngine.detectSentiment("ما هو التركيب النانوي للحمض النووي؟"))
    }

    @Test
    fun testComprehensionGrading() {
        assertEquals(
            StudentComprehensionLevel.MASTERED,
            EmpatheticCompanionEngine.evaluateComprehension(0.95, 3, StudentSentiment.CONFIDENT)
        )
        assertEquals(
            StudentComprehensionLevel.PROFICIENT,
            EmpatheticCompanionEngine.evaluateComprehension(0.65, 1, StudentSentiment.CURIOUS)
        )
        assertEquals(
            StudentComprehensionLevel.NOVICE,
            EmpatheticCompanionEngine.evaluateComprehension(0.30, 1, StudentSentiment.CONFUSED)
        )
    }

    @Test
    fun testDiagnosticSessionCreation() {
        val session = EmpatheticCompanionEngine.createDiagnosticSession(
            topicId = "sarcomere_contraction",
            studentInput = "أنا متوتر وقلقان من مسائل القطعة العضلية",
            historicalAccuracy = 0.50
        )

        assertEquals("sarcomere_contraction", session.topicId)
        assertEquals(StudentSentiment.ANXIOUS, session.initialSentiment)
        assertTrue(session.empatheticGreeting.contains("امتحان") || session.empatheticGreeting.contains("قلق"))
        assertTrue(session.checkInQuestionArabic.isNotBlank())
        assertNotNull(session.targetedMicroIntervention)
        assertTrue(session.targetedMicroIntervention.auxiliaryAnalogy.isNotBlank())
        assertTrue(session.targetedMicroIntervention.correctiveDrillPrompt.isNotBlank())
    }

    @Test
    fun testAnatomicalDissectionProvider() {
        val proto = AnatomicalDissectionProvider.getProtocolForTopic("sarcomere_contraction")
        assertNotNull(proto)
        assertTrue(proto.dissectionSteps.isNotEmpty())
        assertTrue(proto.microscopicLayers.isNotEmpty())
        assertTrue(proto.openSource3DParameters.contains("Three.js") || proto.openSource3DParameters.contains("3D"))
        proto.microscopicLayers.forEach { layer ->
            assertTrue(layer.layerNameArabic.isNotBlank())
            assertTrue(layer.histologicalDescription.isNotBlank())
            assertTrue(layer.clinicalExamRelevance.isNotBlank())
        }
    }

    @Test
    fun testAnkiJsonExportFormatting() {
        val cards = listOf(
            Flashcard("c1", "sarcomere", "ما هي الوحدة التركيبية للعضلة الهيكلية؟", "الليفة العضلية", "Anatomy", 1),
            Flashcard("c2", "sarcomere", "ما هي الوحدة الوظيفية للعضلة الهيكلية؟", "الوحدة الحركية", "Physiology", 2)
        )

        val json = AnkiExportHelper.exportToAnkiJson("انقباض العضلات", cards)
        assertNotNull(json)
        assertTrue(json.contains("\"__version__\": 2"))
        assertTrue(json.contains("\"cardsCount\": 2"))
        assertTrue(json.contains("\"Front\": \"ما هي الوحدة التركيبية للعضلة الهيكلية؟\""))
        assertTrue(json.contains("\"Back\": \"الليفة العضلية\""))
    }
}
