package com.example.domain.model

/**
 * Pacing categories for Egyptian Thanweya Amma biology practice questions.
 * Standard exam benchmark for 2-mark explanation questions: 45 to 90 seconds.
 */
enum class PacingCategory(
    val labelArabic: String,
    val labelEnglish: String,
    val adviceArabic: String
) {
    FAST(
        labelArabic = "متسرع (أقل من 45 ثانية)",
        labelEnglish = "Fast / Rushed (< 45s)",
        adviceArabic = "سرعة عالية قد تسبب نسيان الكلمات المفتاحية في نموذج الإجابة الوزاري."
    ),
    OPTIMAL(
        labelArabic = "وتيرة مثالية (45 - 90 ثانية)",
        labelEnglish = "Optimal Pacing (45s - 90s)",
        adviceArabic = "توازن ممتاز بين استرجاع المفاهيم وكتابة المصطلحات الدقيقة."
    ),
    OVERTIME(
        labelArabic = "تجاوز الوقت الموصى به (> 90 ثانية)",
        labelEnglish = "Overtime (> 90s)",
        adviceArabic = "استغراق وقت أطول من المخصص قد يضغط على وقت باقي أسئلة الامتحان."
    );

    val label: String get() = labelArabic

    companion object {
        fun fromSeconds(seconds: Int): PacingCategory {
            return when {
                seconds < 45 -> FAST
                seconds <= 90 -> OPTIMAL
                else -> OVERTIME
            }
        }
    }
}

/**
 * Pacing record for an individual practice question.
 */
data class QuestionPacingRecord(
    val questionIndex: Int,
    val questionPrompt: String,
    val timeSpentSeconds: Int,
    val targetSeconds: Int = 90,
    val pacingCategory: PacingCategory = PacingCategory.fromSeconds(timeSpentSeconds),
    val scoreAchieved: Double? = null,
    val maxScore: Double = 2.0,
    val isFullMark: Boolean = false,
    val missingKeywordsCount: Int = 0,
    val detectedKeywordsCount: Int = 0
)

/**
 * Comprehensive analysis of practice pacing efficiency.
 */
data class PracticePacingSummary(
    val totalSessionSeconds: Int,
    val questionsCount: Int,
    val averageSecondsPerQuestion: Int,
    val optimalCount: Int,
    val fastCount: Int,
    val overtimeCount: Int,
    val pacingEfficiencyScore: Int, // 0 to 100%
    val pacingRatingArabic: String,
    val pacingRatingEnglish: String,
    val executiveSummaryArabic: String,
    val thanweyaAmmaTip: String,
    val questionRecords: List<QuestionPacingRecord>
)
