package com.example.domain.engine

data class SarcomereCalculation(
    val sarcomeresCount: Int,
    val zLinesCount: Int,
    val aBandsCount: Int,
    val completeIBandsCount: Int,
    val incompleteIBandsCount: Int,
    val hZonesRelaxedCount: Int,
    val hZonesContractedCount: Int,
    val aBandStatus: String = "Constant (Never shortens)",
    val explanations: List<String>
)

object SarcomereCalculator {

    fun calculate(sarcomeres: Int): Result<SarcomereCalculation> {
        if (sarcomeres <= 0) {
            return Result.failure(
                IllegalArgumentException("Sarcomere count must be strictly greater than 0. Received: $sarcomeres")
            )
        }

        val zLines = sarcomeres + 1
        val aBands = sarcomeres
        val completeI = maxOf(0, sarcomeres - 1)
        val incompleteI = 2
        val hRelaxed = sarcomeres
        val hContracted = 0 // In full contraction, actin filaments completely overlap, obliterating H-zone

        val steps = listOf(
            "Z-lines = Sarcomeres + 1 = $sarcomeres + 1 = $zLines",
            "A-bands (Dark bands, thick myosin) = Sarcomeres = $aBands (Length remains strictly constant during contraction!)",
            "Complete I-bands = Sarcomeres - 1 = $completeI (excluding the 2 terminal incomplete bands)",
            "H-zones (Semi-light, myosin only) = $hRelaxed in relaxed muscle; drops to 0 during maximal contraction due to complete actin overlap."
        )

        return Result.success(
            SarcomereCalculation(
                sarcomeresCount = sarcomeres,
                zLinesCount = zLines,
                aBandsCount = aBands,
                completeIBandsCount = completeI,
                incompleteIBandsCount = incompleteI,
                hZonesRelaxedCount = hRelaxed,
                hZonesContractedCount = hContracted,
                explanations = steps
            )
        )
    }

    fun calculateFromZLines(zLines: Int): Result<SarcomereCalculation> {
        if (zLines <= 1) {
            return Result.failure(
                IllegalArgumentException("Z-lines count must be at least 2 to define a sarcomere. Received: $zLines")
            )
        }
        return calculate(zLines - 1)
    }
}
