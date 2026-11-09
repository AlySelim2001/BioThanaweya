package com.example.domain.engine

import com.example.domain.model.DivisionProcess
import com.example.domain.model.DivisionStage
import org.junit.Assert.*
import org.junit.Test

class CellDivisionEngineTest {

    @Test
    fun testMitosisStagesFormulasAndTraps() {
        val interphase = CellDivisionEngine.getStageInfo(DivisionProcess.MITOSIS, DivisionStage.INTERPHASE)
        assertTrue(interphase.dnaContentFormula.contains("4C"))
        assertTrue(interphase.ministryExamTrapWarning.contains("مضاعفة جزيئات DNA"))

        val metaphase = CellDivisionEngine.getStageInfo(DivisionProcess.MITOSIS, DivisionStage.METAPHASE)
        assertTrue(metaphase.chromosomeCountFormula.contains("46"))
        assertTrue(metaphase.chromatidsCountFormula.contains("92"))

        val anaphase = CellDivisionEngine.getStageInfo(DivisionProcess.MITOSIS, DivisionStage.ANAPHASE)
        assertTrue(anaphase.chromosomeCountFormula.contains("92"))
        assertTrue(anaphase.keyMicroscopicEventArabic.contains("انشطار السنترومير"))
    }

    @Test
    fun testMeiosis1ReductionalDivisionFormulas() {
        val prophase1 = CellDivisionEngine.getStageInfo(DivisionProcess.MEIOSIS_1, DivisionStage.PROPHASE)
        assertTrue(prophase1.keyMicroscopicEventArabic.contains("العبور الوراثي"))

        val anaphase1 = CellDivisionEngine.getStageInfo(DivisionProcess.MEIOSIS_1, DivisionStage.ANAPHASE)
        assertTrue(anaphase1.ministryExamTrapWarning.contains("لا ينقسم السنترومير"))

        val telophase1 = CellDivisionEngine.getStageInfo(DivisionProcess.MEIOSIS_1, DivisionStage.TELOPHASE)
        assertTrue(telophase1.chromosomeCountFormula.contains("23"))
        assertTrue(telophase1.dnaContentFormula.contains("2C"))
    }

    @Test
    fun testMeiosis2EquationalDivisionFormulas() {
        val anaphase2 = CellDivisionEngine.getStageInfo(DivisionProcess.MEIOSIS_2, DivisionStage.ANAPHASE)
        assertTrue(anaphase2.ministryExamTrapWarning.contains("ينقسم السنترومير"))

        val telophase2 = CellDivisionEngine.getStageInfo(DivisionProcess.MEIOSIS_2, DivisionStage.TELOPHASE)
        assertTrue(telophase2.chromosomeCountFormula.contains("23"))
        assertTrue(telophase2.dnaContentFormula.contains("1C"))
    }
}
