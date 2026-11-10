package com.example.domain.engine

data class WeightedKeyword(
    val word: String,
    val weight: Double = 1.0,
    val synonyms: List<String> = emptyList()
)

data class PenaltyFlag(
    val warningEnglish: String = "",
    val warningArabic: String = ""
)

data class DetailedGradingResult(
    val finalScore: Double,
    val maxScore: Double,
    val isFullMark: Boolean,
    val detectedKeywords: List<String>,
    val missingKeywords: List<String>,
    val feedback: String = "",
    val triggeredPenalties: List<PenaltyFlag> = emptyList(),
    val examinerFeedback: String = feedback,
    val modelAnswer: String = ""
)

object MarkSchemeEngine {

    fun grade(
        studentInput: String,
        weightedKeywords: List<WeightedKeyword>,
        maxScore: Double = 2.0,
        modelAnswer: String = ""
    ): DetailedGradingResult {
        if (studentInput.isBlank()) {
            return DetailedGradingResult(
                finalScore = 0.0,
                maxScore = maxScore,
                isFullMark = false,
                detectedKeywords = emptyList(),
                missingKeywords = weightedKeywords.map { it.word },
                feedback = "الإجابة فارغة. يرجى كتابة الشرح العلمي بالكلمات المفتاحية.",
                examinerFeedback = "الإجابة فارغة. يرجى كتابة الشرح العلمي بالكلمات المفتاحية.",
                modelAnswer = modelAnswer
            )
        }

        val detected = mutableListOf<String>()
        val missing = mutableListOf<String>()
        var scoredWeight = 0.0
        val totalWeight = weightedKeywords.sumOf { it.weight }.coerceAtLeast(1.0)

        val cleanStudentText = studentInput.lowercase().trim()

        for (kw in weightedKeywords) {
            val primaryMatch = cleanStudentText.contains(kw.word.lowercase())
            val synonymMatch = kw.synonyms.any { cleanStudentText.contains(it.lowercase()) }

            if (primaryMatch || synonymMatch) {
                detected.add(kw.word)
                scoredWeight += kw.weight
            } else {
                missing.add(kw.word)
            }
        }

        val calculatedScore = ((scoredWeight / totalWeight) * maxScore)
        val finalScore = (Math.round(calculatedScore * 10.0) / 10.0).coerceIn(0.0, maxScore)
        val isFullMark = finalScore >= (maxScore * 0.95)

        val feedback = when {
            isFullMark -> "إجابة ممتازة ومكتملة بجميع الكلمات المفتاحية الوزارية!"
            finalScore >= maxScore * 0.5 -> "إجابة جيدة ولكن تنقصها بعض المصطلحات الدقيقة: ${missing.joinToString(", ")}"
            else -> "إجابة غير مكتملة، يرجى مراجعة نموذج الإجابة وإعادة الصياغة."
        }

        return DetailedGradingResult(
            finalScore = finalScore,
            maxScore = maxScore,
            isFullMark = isFullMark,
            detectedKeywords = detected,
            missingKeywords = missing,
            feedback = feedback,
            examinerFeedback = feedback,
            modelAnswer = modelAnswer
        )
    }
}
