package com.example

import com.example.domain.engine.*
import com.example.domain.fsrs.*
import org.junit.Assert.*
import org.junit.Test
import java.util.Date

class ExampleUnitTest {

    // -------------------------------------------------------------
    // 1. Sarcomere Calculator Tests (Zero/Negative bounds + Band status)
    // -------------------------------------------------------------
    @Test
    fun `sarcomere calculator rejects zero and negative inputs`() {
        val zeroResult = SarcomereCalculator.calculate(0)
        assertTrue("Zero sarcomeres must return failure", zeroResult.isFailure)
        assertTrue(zeroResult.exceptionOrNull() is IllegalArgumentException)

        val negativeResult = SarcomereCalculator.calculate(-4)
        assertTrue("Negative sarcomeres must return failure", negativeResult.isFailure)
        assertTrue(negativeResult.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `sarcomere calculator verifies constant A-band and zero H-zone in full contraction`() {
        val result = SarcomereCalculator.calculate(6).getOrNull()
        assertNotNull(result)
        assertEquals(6, result!!.sarcomeresCount)
        assertEquals(7, result.zLinesCount)
        assertEquals(6, result.aBandsCount)
        assertEquals(5, result.completeIBandsCount) // n - 1
        assertEquals(2, result.incompleteIBandsCount)
        assertEquals(6, result.hZonesRelaxedCount)
        assertEquals(0, result.hZonesContractedCount) // Must be 0 in maximal contraction!
        assertEquals("Constant (Never shortens)", result.aBandStatus)
    }

    // -------------------------------------------------------------
    // 2. DNA Chargaff Calculator Tests (Epsilon + 10 bp turn constant)
    // -------------------------------------------------------------
    @Test
    fun `chargaff calculator enforces base equivalence and epsilon tolerance`() {
        val result = DnaChargaffCalculator.calculate("A", 22.5, 1000).getOrNull()
        assertNotNull(result)
        assertEquals(22.5, result!!.adeninePercent, 1e-4)
        assertEquals(22.5, result.thyminePercent, 1e-4)
        assertEquals(27.5, result.guaninePercent, 1e-4)
        assertEquals(27.5, result.cytosinePercent, 1e-4)

        // Sum must be 100% within 1e-4
        val total = result.adeninePercent + result.thyminePercent + result.guaninePercent + result.cytosinePercent
        assertEquals(100.0, total, 1e-4)

        // Constant: 10 bp/turn in Egyptian Curriculum
        assertEquals(10, DnaChargaffCalculator.BASE_PAIRS_PER_TURN)
        assertEquals(500, result.basePairsCount)
        assertEquals(50.0, result.completeTurnsCount, 1e-4) // 500 / 10 = 50 turns
    }

    @Test
    fun `chargaff calculator rejects percentages outside valid bounds`() {
        val highResult = DnaChargaffCalculator.calculate("A", 55.0, 1000)
        assertTrue("Base percent >= 50% must fail", highResult.isFailure)

        val negResult = DnaChargaffCalculator.calculate("A", -2.0, 1000)
        assertTrue("Negative percent must fail", negResult.isFailure)
    }

    // -------------------------------------------------------------
    // 3. Codon Calculator Tests (Multiple of 3 + Stop Codons exclusion)
    // -------------------------------------------------------------
    @Test
    fun `codon calculator rejects RNA length not multiple of 3`() {
        val nonTripletResult = CodonCalculator.calculateFromNucleotides(100)
        assertTrue("RNA length not multiple of 3 must fail", nonTripletResult.isFailure)
        assertTrue(nonTripletResult.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `codon calculator excludes stop codons and verifies tRNA count`() {
        // 153 nucleotides = 51 codons (50 coding + 1 stop)
        val result = CodonCalculator.calculateFromNucleotides(153, stopCodonsCount = 1, includeStartMethionine = true).getOrNull()
        assertNotNull(result)
        assertEquals(51, result!!.totalCodons)
        assertEquals(1, result.stopCodonsCount)
        assertEquals(50, result.aminoAcidsCount) // 51 - 1 = 50 amino acids
        assertEquals(49, result.peptideBondsCount) // 50 - 1
        assertEquals(49, result.waterMoleculesReleased)
        assertEquals(50, result.trnaMoleculesRequired) // Stop codon does not bind tRNA!
        assertEquals(306, result.dnaDoubleHelixNucleotides) // 153 * 2
    }

    // -------------------------------------------------------------
    // 4. Mark Scheme Engine & Arabic Normalization Tests
    // -------------------------------------------------------------
    @Test
    fun `arabic text normalizer removes tashkeel and unifies alef and taa marbuta`() {
        val input = "الْأَكْتِينُ وَالرَّوَابِطُ الْمُسْتَعْرِضَةُ فِي قِطْعَةٍ عَضَلِيَّةٍ"
        val normalized = ArabicTextNormalizer.normalize(input)
        assertFalse("Tashkeel must be removed", normalized.contains("ْ"))
        assertFalse("Tashkeel must be removed", normalized.contains("َ"))
        assertFalse("Alef with hamza must be unified", normalized.contains("أ"))
        assertFalse("Taa marbuta must be unified to haa", normalized.contains("ة"))
        assertTrue("Must contain normalized word", normalized.contains("الاكتين"))
    }

    @Test
    fun `mark scheme engine detects penalty flags and deducts marks`() {
        val keywords = listOf(
            WeightedKeyword("actin", 1.0, listOf("اكتين")),
            WeightedKeyword("myosin", 1.0, listOf("ميوسين"))
        )

        // Answer containing fatal misconception: A-band narrows!
        val badStudentAnswer = "تنزلق خيوط الاكتين والميوسين ويقل طول المنطقه a"
        val result = MarkSchemeEngine.grade(badStudentAnswer, keywords, maxScore = 2.0)

        assertTrue("A_BAND_NARROWS penalty flag must be triggered", result.triggeredPenalties.contains(PenaltyFlag.A_BAND_NARROWS))
        assertTrue("Deduction must be applied", result.totalDeduction >= 0.5)
        assertTrue("Score must reflect deduction", result.finalScore < result.rawScore)
    }

    // -------------------------------------------------------------
    // 5. FSRS Algorithm Tests
    // -------------------------------------------------------------
    @Test
    fun `fsrs schedules correct initial states and intervals`() {
        val fsrs = FSRS()
        val newCard = FSRSCard(cardId = "test_c1", state = FSRSState.NEW)
        val now = Date()

        val scheduleMap = fsrs.repeat(newCard, now)

        val againRecord = scheduleMap[FSRSRating.AGAIN]!!
        val easyRecord = scheduleMap[FSRSRating.EASY]!!

        assertEquals(FSRSState.LEARNING, againRecord.card.state)
        assertEquals(FSRSState.REVIEW, easyRecord.card.state)
        assertTrue("Easy card stability must exceed Again card stability", easyRecord.card.stability > againRecord.card.stability)
    }

    // -------------------------------------------------------------
    // 6. Study Note Entity Cache Tests
    // -------------------------------------------------------------
    @Test
    fun `study note entity instantiates with default timestamp and bookmark flag`() {
        val note = com.example.data.local.entity.StudyNoteEntity(
            id = 1L,
            topicId = "molecular_genetics",
            title = "انزيم بلمرة الـ DNA",
            content = "يقوم بإضافة نيوكليوتيدات جديدة في اتجاه 5' إلى 3'",
            keyConcepts = "DNA polymerase, replication, 5' to 3'"
        )

        assertEquals("molecular_genetics", note.topicId)
        assertFalse(note.isBookmarked)
        assertTrue(note.createdAt > 0L)
        assertTrue(note.updatedAt >= note.createdAt)
    }
}
