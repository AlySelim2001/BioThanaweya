package com.example.domain.engine

data class CodonCalculationResult(
    val rnaLengthNucleotides: Int,
    val totalCodons: Int,
    val stopCodonsCount: Int,
    val aminoAcidsCount: Int,
    val peptideBondsCount: Int,
    val waterMoleculesReleased: Int,
    val trnaMoleculesRequired: Int,
    val dnaDoubleHelixNucleotides: Int,
    val startCodonIncluded: Boolean,
    val explanations: List<String>
)

object CodonCalculator {

    val STOP_CODONS: Set<String> = setOf("UAA", "UAG", "UGA")
    const val START_CODON: String = "AUG"

    fun calculateFromNucleotides(
        rnaNucleotides: Int,
        stopCodonsCount: Int = 1,
        includeStartMethionine: Boolean = true
    ): Result<CodonCalculationResult> {
        if (rnaNucleotides <= 0) {
            return Result.failure(
                IllegalArgumentException("RNA nucleotide count must be greater than 0. Received: $rnaNucleotides")
            )
        }
        if (rnaNucleotides % 3 != 0) {
            return Result.failure(
                IllegalArgumentException("RNA length must be a strict multiple of 3 (triplet genetic code). Received: $rnaNucleotides (remainder ${rnaNucleotides % 3})")
            )
        }
        if (stopCodonsCount < 0) {
            return Result.failure(
                IllegalArgumentException("Stop codon count cannot be negative.")
            )
        }

        val totalCodons = rnaNucleotides / 3
        if (totalCodons < stopCodonsCount) {
            return Result.failure(
                IllegalArgumentException("Total codons ($totalCodons) cannot be less than stop codons ($stopCodonsCount).")
            )
        }

        // Stop codons (UAA, UAG, UGA) bind Release Factor and DO NOT code for any amino acid!
        val codingCodons = totalCodons - stopCodonsCount
        val aminoAcids = if (includeStartMethionine) {
            codingCodons
        } else {
            maxOf(0, codingCodons - 1) // If initial Methionine is post-translationally cleaved
        }

        val peptideBonds = maxOf(0, aminoAcids - 1)
        val waterMolecules = peptideBonds
        // tRNA carries amino acids. Stop codons have no tRNA!
        val trnaCount = codingCodons
        val dnaNucleotides = rnaNucleotides * 2

        val steps = listOf(
            "Total Codons = RNA Nucleotides / 3 = $rnaNucleotides / 3 = $totalCodons codons",
            "Stop Codons ($stopCodonsCount: UAA/UAG/UGA) DO NOT code for amino acids; they bind Release Factor.",
            "Amino Acids in Polypeptide = Total Codons - Stop Codons = $totalCodons - $stopCodonsCount = $aminoAcids aa (Start Met included: $includeStartMethionine)",
            "Peptide Bonds = Amino Acids - 1 = $aminoAcids - 1 = $peptideBonds bonds",
            "Water Molecules Released (Condensation) = Peptide Bonds = $waterMolecules H₂O",
            "tRNA molecules involved = $trnaCount (Exam Trap: Stop codon has no corresponding tRNA)",
            "DNA gene double-strand nucleotides = mRNA nucleotides × 2 = $rnaNucleotides × 2 = $dnaNucleotides nucleotides"
        )

        return Result.success(
            CodonCalculationResult(
                rnaLengthNucleotides = rnaNucleotides,
                totalCodons = totalCodons,
                stopCodonsCount = stopCodonsCount,
                aminoAcidsCount = aminoAcids,
                peptideBondsCount = peptideBonds,
                waterMoleculesReleased = waterMolecules,
                trnaMoleculesRequired = trnaCount,
                dnaDoubleHelixNucleotides = dnaNucleotides,
                startCodonIncluded = includeStartMethionine,
                explanations = steps
            )
        )
    }

    fun calculateFromAminoAcids(
        aminoAcids: Int,
        includeStartMethionine: Boolean = true
    ): Result<CodonCalculationResult> {
        if (aminoAcids <= 0) {
            return Result.failure(
                IllegalArgumentException("Amino acid count must be positive. Received: $aminoAcids")
            )
        }
        // amino acids + 1 stop codon
        val codons = aminoAcids + 1
        val nucleotides = codons * 3
        return calculateFromNucleotides(nucleotides, stopCodonsCount = 1, includeStartMethionine = includeStartMethionine)
    }
}
