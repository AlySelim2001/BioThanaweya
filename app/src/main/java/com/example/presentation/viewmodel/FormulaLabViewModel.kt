package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.engine.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FormulaLabState(
    // Sarcomere inputs & results
    val sarcomereInput: String = "5",
    val sarcomereResult: Result<SarcomereCalculation>? = SarcomereCalculator.calculate(5),

    // Chargaff inputs & results
    val dnaBaseInput: String = "A",
    val dnaPercentInput: String = "20",
    val dnaTotalInput: String = "1000",
    val chargaffResult: Result<DnaChargaffResult>? = DnaChargaffCalculator.calculate("A", 20.0, 1000),

    // Codon inputs & results
    val rnaNucleotidesInput: String = "153",
    val stopCodonCountInput: String = "1",
    val includeStartMethionine: Boolean = true,
    val codonResult: Result<CodonCalculationResult>? = CodonCalculator.calculateFromNucleotides(153, 1, true)
)

class FormulaLabViewModel @javax.inject.Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(FormulaLabState())
    val state: StateFlow<FormulaLabState> = _state.asStateFlow()

    fun onSarcomereInputChanged(value: String) {
        val sCount = value.toIntOrNull()
        val result = if (sCount != null) {
            SarcomereCalculator.calculate(sCount)
        } else {
            Result.failure(IllegalArgumentException("Please enter a valid positive integer"))
        }
        _state.value = _state.value.copy(
            sarcomereInput = value,
            sarcomereResult = result
        )
    }

    fun onDnaChanged(base: String, percentStr: String, totalStr: String) {
        val percent = percentStr.toDoubleOrNull() ?: -1.0
        val total = totalStr.toIntOrNull() ?: -1
        val result = DnaChargaffCalculator.calculate(base, percent, total)
        _state.value = _state.value.copy(
            dnaBaseInput = base,
            dnaPercentInput = percentStr,
            dnaTotalInput = totalStr,
            chargaffResult = result
        )
    }

    fun onCodonChanged(nucleotidesStr: String, stopCountStr: String, includeStartMet: Boolean) {
        val nucleotides = nucleotidesStr.toIntOrNull() ?: -1
        val stopCount = stopCountStr.toIntOrNull() ?: 1
        val result = CodonCalculator.calculateFromNucleotides(nucleotides, stopCount, includeStartMet)
        _state.value = _state.value.copy(
            rnaNucleotidesInput = nucleotidesStr,
            stopCodonCountInput = stopCountStr,
            includeStartMethionine = includeStartMet,
            codonResult = result
        )
    }
}
