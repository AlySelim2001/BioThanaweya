package com.example.domain.engine

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

data class DnaChargaffResult(
    val adeninePercent: Double,
    val thyminePercent: Double,
    val guaninePercent: Double,
    val cytosinePercent: Double,
    val purinesPercent: Double,
    val pyrimidinesPercent: Double,
    val totalNucleotides: Int,
    val basePairsCount: Int,
    val completeTurnsCount: Double,
    val hydrogenBondsTotal: Int,
    val explanations: List<String>
)

object DnaChargaffCalculator {

    // Egyptian Curriculum standard constant: 10 base pairs per turn in double helix
    const val BASE_PAIRS_PER_TURN: Int = 10
    private const val EPSILON: Double = 1e-4

    fun calculate(
        inputBase: String,
        inputPercent: Double,
        totalNucleotides: Int = 1000
    ): Result<DnaChargaffResult> {
        if (inputPercent <= 0.0 || inputPercent >= 50.0) {
            return Result.failure(
                IllegalArgumentException("Base percentage in double-stranded DNA must be strictly between 0% and 50%. Received: $inputPercent%")
            )
        }
        if (totalNucleotides <= 0 || totalNucleotides % 2 != 0) {
            return Result.failure(
                IllegalArgumentException("Total nucleotides in double-stranded DNA must be an even positive integer. Received: $totalNucleotides")
            )
        }

        val baseClean = inputBase.trim().uppercase()
        val percentBd = BigDecimal(inputPercent.toString()).setScale(4, RoundingMode.HALF_UP)
        val fiftyBd = BigDecimal("50.0")
        val hundredBd = BigDecimal("100.0")

        val (aBd, tBd, gBd, cBd) = when (baseClean) {
            "A", "ADENINE", "T", "THYMINE" -> {
                val rem = fiftyBd.subtract(percentBd)
                listOf(percentBd, percentBd, rem, rem)
            }
            "G", "GUANINE", "C", "CYTOSINE" -> {
                val rem = fiftyBd.subtract(percentBd)
                listOf(rem, rem, percentBd, percentBd)
            }
            else -> {
                return Result.failure(
                    IllegalArgumentException("Unknown nitrogenous base: $inputBase. Must be A, T, G, or C.")
                )
            }
        }

        // Verify sum = 100% within epsilon
        val sum = aBd.add(tBd).add(gBd).add(cBd).toDouble()
        if (abs(sum - 100.0) > EPSILON) {
            return Result.failure(
                IllegalStateException("Calculated base percentages sum ($sum%) deviates from 100%.")
            )
        }

        val a = aBd.toDouble()
        val t = tBd.toDouble()
        val g = gBd.toDouble()
        val c = cBd.toDouble()

        val basePairs = totalNucleotides / 2
        val completeTurns = basePairs.toDouble() / BASE_PAIRS_PER_TURN.toDouble()

        val aCount = (totalNucleotides * (a / 100.0)).toInt()
        val gCount = (totalNucleotides * (g / 100.0)).toInt()
        val hBonds = (aCount * 2) + (gCount * 3)

        val steps = listOf(
            "Chargaff Rule: In double-stranded DNA, %A = %T ($a%) and %G = %C ($g%)",
            "Purines (A + G) = 50.0% and Pyrimidines (T + C) = 50.0% (Equivalence ratio = 1)",
            "Total Base Pairs = Total Nucleotides / 2 = $totalNucleotides / 2 = $basePairs base pairs",
            "Double Helix Complete Turns = Base Pairs / $BASE_PAIRS_PER_TURN = $basePairs / 10 = ${String.format("%.1f", completeTurns)} turns",
            "Hydrogen Bonds = (2 × A) + (3 × G) = (2 × $aCount) + (3 × $gCount) = $hBonds bonds"
        )

        return Result.success(
            DnaChargaffResult(
                adeninePercent = a,
                thyminePercent = t,
                guaninePercent = g,
                cytosinePercent = c,
                purinesPercent = 50.0,
                pyrimidinesPercent = 50.0,
                totalNucleotides = totalNucleotides,
                basePairsCount = basePairs,
                completeTurnsCount = completeTurns,
                hydrogenBondsTotal = hBonds,
                explanations = steps
            )
        )
    }
}
