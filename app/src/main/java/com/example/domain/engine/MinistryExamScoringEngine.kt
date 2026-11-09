package com.example.domain.engine

import com.example.data.model.QuestionType
import com.example.data.model.UnitCategory
import com.example.domain.model.*

object MinistryExamScoringEngine {

    fun gradeExamSession(
        paper: MinistryExamPaper,
        selectedOptions: Map<Int, Int>, // questionIndex (0..45) -> selected option (0..3)
        essayAnswers: Map<Int, String>, // questionIndex (0..45) -> text answer
        timeSpentSeconds: Long
    ): MinistryExamFullReport {
        var totalScore = 0.0
        val questionSummaries = mutableListOf<QuestionGradingSummary>()
        val fallenTraps = mutableListOf<Pair<MinistryExamQuestion, String>>()

        // Unit tracking: UnitCategory -> Pair<ScoredMarks, TotalMarks>
        val unitMarksMap = mutableMapOf<UnitCategory, Pair<Double, Double>>()

        for ((index, q) in paper.questions.withIndex()) {
            val maxMark = q.marks

            val (scoredMark, isCorrect, detectedKw, missingKw, notes) = if (q.type == QuestionType.MCQ) {
                val selectedOpt = selectedOptions[index] ?: -1
                val correct = selectedOpt == q.correctOptionIndex
                val awarded = if (correct) maxMark else 0.0

                if (!correct) {
                    val trapNote = if (q.ministryTrapWarningArabic.isNotBlank()) {
                        q.ministryTrapWarningArabic
                    } else {
                        q.scientificExplanationArabic
                    }
                    fallenTraps.add(q to trapNote)
                }

                Tuples5(
                    awarded,
                    correct,
                    emptyList(),
                    emptyList(),
                    if (correct) "إجابة صحيحة وفق نموذج الإجابة الوزاري." else "إجابة خاطئة. الإجابة الصحيحة: ${q.modelAnswerArabic}"
                )
            } else {
                // Essay / Give Reason question grading
                val studentText = essayAnswers[index].orEmpty().trim().lowercase()
                gradeEssayItem(q, studentText)
            }

            totalScore += scoredMark

            // Accumulate unit marks
            val currentUnitPair = unitMarksMap[q.unit] ?: (0.0 to 0.0)
            unitMarksMap[q.unit] = (currentUnitPair.first + scoredMark) to (currentUnitPair.second + maxMark)

            questionSummaries.add(
                QuestionGradingSummary(
                    question = q,
                    studentSelectedOption = selectedOptions[index] ?: -1,
                    studentEssayAnswer = essayAnswers[index].orEmpty(),
                    scoreAwarded = scoredMark,
                    maxScore = maxMark,
                    isCorrect = isCorrect,
                    detectedKeywords = detectedKw,
                    missingKeywords = missingKw,
                    examinerNotes = notes
                )
            )
        }

        val percentage = (totalScore / paper.totalMarks) * 100.0

        // Ministry Grade Category
        val (gradeArabic, gradeEnglish) = when {
            percentage >= 95.0 -> "ممتاز مع مرتبة الشرف" to "Distinction with Honors"
            percentage >= 85.0 -> "جيد جداً مرتفع" to "Very Good (High)"
            percentage >= 75.0 -> "جيد جداً" to "Good"
            percentage >= 65.0 -> "مقبول" to "Acceptable"
            percentage >= 50.0 -> "يحتاج لدعم مكثف" to "Needs Improvement"
            else -> "دون مستوى النجاح (دور ثان)" to "Below Passing Grade"
        }

        // National Percentile Estimate based on Thanawya Amma official bell-curve
        val nationalPercentile = calculateNationalPercentile(totalScore)

        // Generate unit breakdowns
        val unitBreakdowns = unitMarksMap.map { (unit, pair) ->
            val uScored = pair.first
            val uTotal = pair.second
            val uPct = if (uTotal > 0) (uScored / uTotal) * 100.0 else 0.0

            val (uArabic, uEnglish) = when (unit) {
                UnitCategory.UNIT_1 -> "الوحدة الأولى: التركيب والوظيفة (الدعامة، الحركة، الهرمونات، التكاثر، المناعة)" to "Unit 1: Structure & Function"
                UnitCategory.UNIT_2 -> "الوحدة الثانية: البيولوجيا الجزيئية (DNA & RNA والهندسة الوراثية)" to "Unit 2: Molecular Biology"
            }

            UnitPerformanceBreakdown(
                unit = unit,
                unitNameArabic = uArabic,
                unitNameEnglish = uEnglish,
                scoredMarks = uScored,
                totalMarks = uTotal,
                percentage = uPct,
                status = if (uPct >= 85.0) "متقن" else if (uPct >= 65.0) "متوسط" else "يحتاج مراجعة"
            )
        }

        // Recommendations
        val recommendations = mutableListOf<String>()
        if (percentage >= 95.0) {
            recommendations.add("مستوى متميز يؤهلك للالتحاق بكليات القطاع الطبي (طب بشري، طب أسنان). حافظ على استقرار الذاكرة عبر نظام FSRS.")
        } else if (percentage >= 80.0) {
            recommendations.add("أداء قوي جداً. راجع الأسئلة ذات الدرجتين (الأسئلة 33-44) لتفادي خسارة درجات المستويات العليا.")
        } else {
            recommendations.add("ركز على معالجة المصائد الوزارية التي وقعت فيها في التكاثر والمناعة عبر معمل القوانين والمسائل.")
        }

        if (fallenTraps.isNotEmpty()) {
            recommendations.add("تم رصد ${fallenTraps.size} مصيدة وزارية؛ راجع جدول التحليل لتصحيح المفاهيم الخاطئة.")
        }

        return MinistryExamFullReport(
            paperId = paper.id,
            paperTitleArabic = paper.arabicTitle,
            paperTitleEnglish = paper.title,
            totalScoreAwarded = (Math.round(totalScore * 10.0) / 10.0),
            maxScore = paper.totalMarks,
            percentage = (Math.round(percentage * 10.0) / 10.0),
            nationalPercentile = (Math.round(nationalPercentile * 10.0) / 10.0),
            ministryGradeLabelArabic = gradeArabic,
            ministryGradeLabelEnglish = gradeEnglish,
            timeSpentSeconds = timeSpentSeconds,
            unitBreakdowns = unitBreakdowns,
            questionSummaries = questionSummaries,
            fallenTraps = fallenTraps,
            recommendedActions = recommendations
        )
    }

    private fun gradeEssayItem(q: MinistryExamQuestion, studentText: String): Tuples5<Double, Boolean, List<String>, List<String>, String> {
        if (studentText.length < 4) {
            return Tuples5(
                0.0,
                false,
                emptyList(),
                q.requiredKeywords,
                "إجابة فارغة أو قصيرة جداً. يشترط في الأسئلة المقالية ذكر المصطلحات والتعليلات الفسيولوجية المحددة."
            )
        }

        val detected = mutableListOf<String>()
        val missing = mutableListOf<String>()

        for (kw in q.requiredKeywords) {
            val kwClean = kw.lowercase().trim()
            if (studentText.contains(kwClean) || ArabicTextNormalizer.normalize(studentText).contains(ArabicTextNormalizer.normalize(kwClean))) {
                detected.add(kw)
            } else {
                missing.add(kw)
            }
        }

        val reqCount = q.requiredKeywords.size.toDouble().coerceAtLeast(1.0)

        // Accommodate single-language responses (Arabic or English) without requiring both simultaneously
        val score = when {
            detected.size >= 3 || (detected.size.toDouble() / reqCount) >= 0.40 -> q.marks
            detected.size >= 2 -> q.marks * 0.75
            detected.isNotEmpty() -> q.marks * 0.50
            else -> 0.0
        }

        val isCorrect = score >= (q.marks * 0.75)
        val notes = buildString {
            if (isCorrect) {
                append("إجابة مقالية نموذجية مستوفية للكلمات المفتاحية المطلوبة في كنترول الثانوية العامة.")
            } else {
                append("إجابة غير مكتملة. الكلمات المفتاحية الناقصة: ${missing.joinToString("، ")}.")
            }
        }

        return Tuples5(score, isCorrect, detected, missing, notes)
    }

    private fun calculateNationalPercentile(score: Double): Double {
        // Modeled on Ministry of Education 60-mark Thanaweya Amma distribution
        // 60/60 is top 99.9%
        // 55/60 is ~top 95%
        // 50/60 is ~top 85%
        // 40/60 is ~top 65%
        // 30/60 is ~top 45%
        return when {
            score >= 59.0 -> 99.8
            score >= 57.0 -> 98.5
            score >= 55.0 -> 95.0
            score >= 52.0 -> 89.0
            score >= 48.0 -> 81.0
            score >= 44.0 -> 72.0
            score >= 40.0 -> 63.0
            score >= 35.0 -> 52.0
            score >= 30.0 -> 42.0
            else -> (score / 60.0) * 80.0
        }.coerceIn(5.0, 99.9)
    }
}

private data class Tuples5<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
