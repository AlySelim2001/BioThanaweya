package com.example.domain.engine

import com.example.domain.model.PacingCategory
import com.example.domain.model.PracticePacingSummary
import com.example.domain.model.QuestionPacingRecord

/**
 * PacingEfficiencyEngine:
 * Analyzes time allocation per question and generates actionable pacing analytics
 * grounded in the official Egyptian Ministry of Education 3-hour exam constraints.
 */
object PacingEfficiencyEngine {

    fun analyzePacing(
        records: List<QuestionPacingRecord>,
        totalSessionSeconds: Int
    ): PracticePacingSummary {
        if (records.isEmpty()) {
            return PracticePacingSummary(
                totalSessionSeconds = totalSessionSeconds,
                questionsCount = 0,
                averageSecondsPerQuestion = 0,
                optimalCount = 0,
                fastCount = 0,
                overtimeCount = 0,
                pacingEfficiencyScore = 100,
                pacingRatingArabic = "لم تبدأ بعد",
                pacingRatingEnglish = "Not Started",
                executiveSummaryArabic = "لم يتم تسجيل أي أسئلة مكتملة بعد.",
                thanweyaAmmaTip = "المعدل المثالي لأسئلة المقال والتفسير في الأحياء هو 90 ثانية لكل سؤال.",
                questionRecords = emptyList()
            )
        }

        val optimalCount = records.count { it.pacingCategory == PacingCategory.OPTIMAL }
        val fastCount = records.count { it.pacingCategory == PacingCategory.FAST }
        val overtimeCount = records.count { it.pacingCategory == PacingCategory.OVERTIME }

        val avgSeconds = totalSessionSeconds / records.size

        // Calculate Pacing Efficiency Score (0 - 100)
        var score = 100
        for (rec in records) {
            when (rec.pacingCategory) {
                PacingCategory.OPTIMAL -> {
                    // Perfect pacing: no deduction
                }
                PacingCategory.FAST -> {
                    // If rushed and missed keywords, heavy penalty; if full marks, minor deduction
                    if (rec.missingKeywordsCount > 0) {
                        score -= 15
                    } else {
                        score -= 5
                    }
                }
                PacingCategory.OVERTIME -> {
                    // Penalty scales with how much overtime
                    val overtimeExcess = rec.timeSpentSeconds - rec.targetSeconds
                    val penalty = if (overtimeExcess > 60) 15 else 8
                    score -= penalty
                }
            }
        }
        val efficiencyScore = score.coerceIn(25, 100)

        val (ratingAr, ratingEn) = when {
            efficiencyScore >= 90 -> "كفاءة زمنية استثنائية" to "Exceptional Mastery Pacing"
            efficiencyScore >= 75 -> "إدارة وقت متزنة وجيدة" to "Solid Steady Pacing"
            efficiencyScore >= 60 -> "تذبذب زمني بحاجة للضبط" to "Inconsistent Pacing"
            else -> "بحاجة عاجلة للتدريب على إدارة الوقت" to "Critical Time Management Needed"
        }

        val executiveSummary = buildString {
            if (fastCount > 0 && records.any { it.pacingCategory == PacingCategory.FAST && it.missingKeywordsCount > 0 }) {
                append("⚠️ التسرع الزمني: تم إنهاء $fastCount سؤال بسرعة فائقة مما ارتبط بنقص في الكلمات المفتاحية المطلوبة في نموذج الإجابة. ")
            }
            if (overtimeCount > 0) {
                append("⏱️ استهلاك زائد: $overtimeCount أسئلة تجاوزت حاجز الـ 90 ثانية، مما قد يسرق الوقت المخصص للأسئلة البيولوجية التركيبية. ")
            }
            if (optimalCount > 0) {
                append("🎯 دقة ممتازة: $optimalCount أسئلة تم حلها في النطاق الذهبي المثالي (45-90 ثانية). ")
            }
        }

        val thanweyaTip = when {
            overtimeCount > optimalCount ->
                "💡 نصيحة ثانوية عامة: يتكون امتحان الأحياء الوزاري من 46 سؤالاً في 180 دقيقة. تدرب على استحضار المخططات الذهنية سريعا لتوفير 20 دقيقة لمراجعة البابل شيت."
            fastCount > optimalCount ->
                "💡 نصيحة ثانوية عامة: المصحح في الثانوية العامة يمنح الدرجة وفق مفاتيح نموذج الإجابة (Mark Scheme). لا تتسرع في تسليم السؤال حتى تتأكد من كتابة المصطلحات العلمية كاملة."
            else ->
                "💡 نصيحة ثانوية عامة: حافظ على هذا الرتم الزمني الممتاز! يتيح لك هذا المعدل (أقل من دقيقتين للسؤال) مراجعة كافة الفصول والمسائل الوراثية بارتياح."
        }

        return PracticePacingSummary(
            totalSessionSeconds = totalSessionSeconds,
            questionsCount = records.size,
            averageSecondsPerQuestion = avgSeconds,
            optimalCount = optimalCount,
            fastCount = fastCount,
            overtimeCount = overtimeCount,
            pacingEfficiencyScore = efficiencyScore,
            pacingRatingArabic = ratingAr,
            pacingRatingEnglish = ratingEn,
            executiveSummaryArabic = executiveSummary.ifEmpty { "وتيرة نموذجية وموزونة في كافة الأسئلة." },
            thanweyaAmmaTip = thanweyaTip,
            questionRecords = records
        )
    }
}
