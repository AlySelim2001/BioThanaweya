package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SarcomereCalculationResult(
    val sarcomeresCount: Int = 5,
    val zLines: Int = 6,
    val darkBandsA: Int = 5,
    val completeLightBandsI: Int = 4,
    val incompleteLightBandsI: Int = 2,
    val hBandsRelaxed: Int = 5,
    val hBandsFullyContracted: Int = 0
)

data class ChargaffCalculationResult(
    val adeninePercent: Double = 28.0,
    val thyminePercent: Double = 28.0,
    val guaninePercent: Double = 22.0,
    val cytosinePercent: Double = 22.0,
    val purinesPercent: Double = 50.0,
    val pyrimidinesPercent: Double = 50.0
)

data class DnaTurnsCalculationResult(
    val totalBasePairs: Int = 200,
    val totalNucleotides: Int = 400,
    val completeTurns: Double = 20.0,
    val codonsCount: Int = 133
)

data class FormulaLabUiState(
    val selectedTab: Int = 0, // 0: Sarcomere, 1: Chargaff DNA, 2: DNA Turns
    val sarcomereInput: String = "5",
    val sarcomereResult: SarcomereCalculationResult = SarcomereCalculationResult(),
    val adenineInput: String = "28",
    val chargaffResult: ChargaffCalculationResult = ChargaffCalculationResult(),
    val basePairsInput: String = "200",
    val dnaTurnsResult: DnaTurnsCalculationResult = DnaTurnsCalculationResult()
)

class FormulaLabViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FormulaLabUiState())
    val uiState: StateFlow<FormulaLabUiState> = _uiState.asStateFlow()

    fun setSelectedTab(tab: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun calculateSarcomere(input: String) {
        val n = input.toIntOrNull() ?: 1
        val safeN = n.coerceAtLeast(1)
        val result = SarcomereCalculationResult(
            sarcomeresCount = safeN,
            zLines = safeN + 1,
            darkBandsA = safeN,
            completeLightBandsI = (safeN - 1).coerceAtLeast(0),
            incompleteLightBandsI = 2,
            hBandsRelaxed = safeN,
            hBandsFullyContracted = 0
        )
        _uiState.value = _uiState.value.copy(
            sarcomereInput = input,
            sarcomereResult = result
        )
    }

    fun calculateChargaff(input: String) {
        val a = input.toDoubleOrNull() ?: 25.0
        val safeA = a.coerceIn(0.0, 49.9)
        val cg = (100.0 - (safeA * 2.0)) / 2.0
        val result = ChargaffCalculationResult(
            adeninePercent = safeA,
            thyminePercent = safeA,
            guaninePercent = cg,
            cytosinePercent = cg,
            purinesPercent = 50.0,
            pyrimidinesPercent = 50.0
        )
        _uiState.value = _uiState.value.copy(
            adenineInput = input,
            chargaffResult = result
        )
    }

    fun calculateDnaTurns(input: String) {
        val bp = input.toIntOrNull() ?: 100
        val safeBp = bp.coerceAtLeast(10)
        val turns = safeBp / 10.0
        val totalNuc = safeBp * 2
        val codons = totalNuc / 3
        val result = DnaTurnsCalculationResult(
            totalBasePairs = safeBp,
            totalNucleotides = totalNuc,
            completeTurns = turns,
            codonsCount = codons
        )
        _uiState.value = _uiState.value.copy(
            basePairsInput = input,
            dnaTurnsResult = result
        )
    }
}
