package com.example.domain

data class SarcomereResult(
    val numberOfSarcomeres: Int,
    val numberOfZLines: Int,
    val numberOfABands: Int,
    val numberOfCompleteIBands: Int,
    val numberOfIncompleteIBands: Int,
    val numberOfHZonesRelaxed: Int,
    val numberOfHZonesContracted: Int,
    val explanations: List<String>
)

object SarcomereCalculator {
    fun calculateFromSarcomeres(sarcomeres: Int): SarcomereResult {
        val s = maxOf(1, sarcomeres)
        val z = s + 1
        val aBands = s
        val completeI = maxOf(0, s - 1)
        val incompleteI = 2
        val hRelaxed = s
        val hContracted = 0 // In full contraction, actin filaments slide completely over each other

        val explanations = listOf(
            "Z-lines = Sarcomeres + 1 = $s + 1 = $z",
            "A-bands (Dark bands, thick myosin filaments) = Sarcomeres = $s (constant length during contraction!)",
            "Complete I-bands (Light bands between two Z-lines) = Sarcomeres - 1 = $completeI",
            "Incomplete I-bands = 2 (one at each end of the myofibril)",
            "H-zones (Semi-light zones, myosin only) = $s in relaxed state, but drops to 0 during complete/maximal contraction!"
        )

        return SarcomereResult(
            numberOfSarcomeres = s,
            numberOfZLines = z,
            numberOfABands = aBands,
            numberOfCompleteIBands = completeI,
            numberOfIncompleteIBands = incompleteI,
            numberOfHZonesRelaxed = hRelaxed,
            numberOfHZonesContracted = hContracted,
            explanations = explanations
        )
    }

    fun calculateFromZLines(zLines: Int): SarcomereResult {
        val z = maxOf(2, zLines)
        return calculateFromSarcomeres(z - 1)
    }
}

data class DnaCalculationResult(
    val adeninePercent: Double,
    val thyminePercent: Double,
    val guaninePercent: Double,
    val cytosinePercent: Double,
    val purinesPercent: Double,
    val pyrimidinesPercent: Double,
    val sampleTotalNucleotides: Int,
    val numberOfBasePairs: Int,
    val numberOfCompleteTurns: Double,
    val hydrogenBondsCount: Int,
    val explanations: List<String>
)

object DnaCalculator {
    fun calculateFromBasePercent(baseName: String, percent: Double, totalNucleotides: Int = 1000): DnaCalculationResult {
        val p = percent.coerceIn(0.1, 49.9)
        var a = 0.0
        var t = 0.0
        var g = 0.0
        var c = 0.0

        when (baseName.uppercase()) {
            "A", "ADENINE", "T", "THYMINE" -> {
                a = p
                t = p
                val remaining = 100.0 - (a + t)
                g = remaining / 2.0
                c = remaining / 2.0
            }
            "G", "GUANINE", "C", "CYTOSINE" -> {
                g = p
                c = p
                val remaining = 100.0 - (g + c)
                a = remaining / 2.0
                t = remaining / 2.0
            }
            else -> {
                a = 25.0
                t = 25.0
                g = 25.0
                c = 25.0
            }
        }

        val basePairs = totalNucleotides / 2
        // In double helix, each turn consists of 10 base pairs (or 20 nucleotides)
        val turns = basePairs / 10.0

        val countA = (totalNucleotides * (a / 100.0)).toInt()
        val countG = (totalNucleotides * (g / 100.0)).toInt()
        // A=T has 2 hydrogen bonds; G≡C has 3 hydrogen bonds
        val hBonds = (countA * 2) + (countG * 3)

        val explanations = listOf(
            "Chargaff's Rule: In double-stranded DNA, %A = %T (${String.format("%.1f", a)}%) and %G = %C (${String.format("%.1f", g)}%)",
            "Purines (A + G) = 50.0% and Pyrimidines (T + C) = 50.0%",
            "Total Nucleotides = $totalNucleotides → Base Pairs = $basePairs",
            "Complete turns in DNA double helix = Base Pairs / 10 = $basePairs / 10 = ${String.format("%.1f", turns)} turns",
            "Hydrogen Bonds = (2 × Adenine count) + (3 × Guanine count) = (2 × $countA) + (3 × $countG) = $hBonds bonds"
        )

        return DnaCalculationResult(
            adeninePercent = a,
            thyminePercent = t,
            guaninePercent = g,
            cytosinePercent = c,
            purinesPercent = 50.0,
            pyrimidinesPercent = 50.0,
            sampleTotalNucleotides = totalNucleotides,
            numberOfBasePairs = basePairs,
            numberOfCompleteTurns = turns,
            hydrogenBondsCount = hBonds,
            explanations = explanations
        )
    }
}

data class ProteinCalculationResult(
    val numberOfAminoAcids: Int,
    val minimumCodonsOnMrna: Int,
    val minimumNucleotidesOnMrna: Int,
    val minimumNucleotidesOnDnaMolecule: Int,
    val numberOfPeptideBonds: Int,
    val numberOfWaterMoleculesReleased: Int,
    val numberOfTrnaRequired: Int,
    val explanations: List<String>
)

object ProteinCalculator {
    fun calculateFromAminoAcids(aminoAcids: Int): ProteinCalculationResult {
        val aa = maxOf(1, aminoAcids)
        // Codons on mRNA = Amino Acids + 1 (Stop Codon: UAA, UAG, or UGA)
        val codons = aa + 1
        // Nucleotides on mRNA = Codons * 3
        val mrnaNucleotides = codons * 3
        // DNA is double stranded, so DNA nucleotides = mRNA nucleotides * 2
        val dnaNucleotides = mrnaNucleotides * 2
        val peptideBonds = aa - 1
        val waterMolecules = aa - 1
        // tRNA carries amino acids. Stop codon does NOT bind tRNA (it binds Release Factor!)
        val trnaCount = aa

        val explanations = listOf(
            "Amino acids in peptide chain = $aa",
            "Minimum mRNA codons = Amino acids + 1 (Stop Codon) = $aa + 1 = $codons codons",
            "Minimum mRNA nucleotides = Codons × 3 = $codons × 3 = $mrnaNucleotides nucleotides",
            "DNA double helix nucleotides = mRNA nucleotides × 2 = $mrnaNucleotides × 2 = $dnaNucleotides nucleotides",
            "Peptide bonds formed = Amino acids - 1 = $aa - 1 = $peptideBonds bonds",
            "Water molecules eliminated during condensation = Peptide bonds = $waterMolecules H₂O",
            "tRNA molecules involved = $trnaCount (Exam Trap: Stop codon has NO corresponding tRNA; Release Factor binds instead!)"
        )

        return ProteinCalculationResult(
            numberOfAminoAcids = aa,
            minimumCodonsOnMrna = codons,
            minimumNucleotidesOnMrna = mrnaNucleotides,
            minimumNucleotidesOnDnaMolecule = dnaNucleotides,
            numberOfPeptideBonds = peptideBonds,
            numberOfWaterMoleculesReleased = waterMolecules,
            numberOfTrnaRequired = trnaCount,
            explanations = explanations
        )
    }
}
