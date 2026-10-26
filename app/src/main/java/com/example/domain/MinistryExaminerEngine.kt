package com.example.domain

import com.example.data.model.ExamGradingResult
import com.example.data.model.GradeStatus
import com.example.data.model.MinistryExamItem

object MinistryExaminerEngine {

    fun gradeStudentAnswer(
        examItem: MinistryExamItem,
        studentAnswer: String
    ): ExamGradingResult {
        val cleanAnswer = studentAnswer.trim().lowercase()

        if (cleanAnswer.length < 5) {
            return ExamGradingResult(
                scoreAwarded = 0.0,
                maxScore = examItem.maxScore,
                detectedKeywords = emptyList(),
                missingKeywords = examItem.requiredKeywords,
                examinerFeedback = "The submitted answer is too brief or empty. Thanaweya Amma mark schemes require specific physiological terms and causative reasoning.",
                modelAnswer = examItem.modelAnswer,
                gradeStatus = GradeStatus.NEEDS_REVISION
            )
        }

        val detectedKeywords = mutableListOf<String>()
        val missingKeywords = mutableListOf<String>()

        for (kw in examItem.requiredKeywords) {
            if (isKeywordPresent(cleanAnswer, kw)) {
                detectedKeywords.add(kw)
            } else {
                missingKeywords.add(kw)
            }
        }

        val totalRequired = examItem.requiredKeywords.size.toDouble().coerceAtLeast(1.0)
        val matchRatio = detectedKeywords.size / totalRequired

        // Calculate score
        val score = when {
            matchRatio >= 0.85 -> examItem.maxScore
            matchRatio >= 0.5 -> examItem.maxScore * 0.75
            matchRatio >= 0.3 -> examItem.maxScore * 0.5
            detectedKeywords.isNotEmpty() -> examItem.maxScore * 0.25
            else -> 0.0
        }

        val gradeStatus = when {
            score >= examItem.maxScore * 0.9 -> GradeStatus.FULL_MARK
            score >= examItem.maxScore * 0.4 -> GradeStatus.PARTIAL_CREDIT
            else -> GradeStatus.NEEDS_REVISION
        }

        val feedback = buildString {
            when (gradeStatus) {
                GradeStatus.FULL_MARK -> {
                    append("Excellent! High-scoring response adhering to the Egyptian Ministry mark scheme. You correctly integrated the core physiological mechanisms and terminology.")
                }
                GradeStatus.PARTIAL_CREDIT -> {
                    append("Good attempt, but missing critical Ministry keywords. In Thanaweya Amma, examiners strictly deduct 0.5 to 1.0 mark if specific enzymes, ions, or structural names are omitted.")
                }
                GradeStatus.NEEDS_REVISION -> {
                    append("Sub-standard response. You missed essential scientific terms. Read the official model answer and focus on exact biological causality.")
                }
            }
            if (missingKeywords.isNotEmpty()) {
                append("\n\nPenalty Warning: Egyptian Ministry examiners would deduct marks for missing: ${missingKeywords.joinToString(", ")}.")
            }
            if (examItem.remediationNotes.isNotBlank()) {
                append("\n\nRemediation Focus: ${examItem.remediationNotes}")
            }
        }

        return ExamGradingResult(
            scoreAwarded = score,
            maxScore = examItem.maxScore,
            detectedKeywords = detectedKeywords,
            missingKeywords = missingKeywords,
            examinerFeedback = feedback,
            modelAnswer = examItem.modelAnswer,
            gradeStatus = gradeStatus
        )
    }

    private fun isKeywordPresent(studentText: String, keyword: String): Boolean {
        val kw = keyword.lowercase().trim()
        if (studentText.contains(kw)) return true

        // Handle variations (plurals, hyphens, chemical formulas)
        val cleanKw = kw.replace("-", " ").replace("  ", " ")
        val cleanText = studentText.replace("-", " ").replace("  ", " ")
        if (cleanText.contains(cleanKw)) return true

        // Specific biological synonym checks
        if (kw.contains("calcium") && (studentText.contains("ca2+") || studentText.contains("ca++") || studentText.contains("ca2") || studentText.contains("ca ions"))) return true
        if (kw.contains("acetylcholine") && studentText.contains("ach")) return true
        if (kw.contains("cholinesterase") && (studentText.contains("acetylcholinesterase") || studentText.contains("ache"))) return true
        if (kw.contains("atp") && studentText.contains("adenosine triphosphate")) return true
        if (kw.contains("transverse") && studentText.contains("cross bridge")) return true
        if (kw.contains("cross bridge") && (studentText.contains("cross-bridge") || studentText.contains("transverse hook"))) return true
        if (kw.contains("sarcoplasmic reticulum") && studentText.contains("sr")) return true
        if (kw.contains("t helper") && (studentText.contains("th") || studentText.contains("cd4"))) return true
        if (kw.contains("cytotoxic") && (studentText.contains("tc") || studentText.contains("cd8"))) return true
        if (kw.contains("perforin") && studentText.contains("perforating")) return true
        if (kw.contains("repolarization") && studentText.contains("resting potential")) return true
        if (kw.contains("helicase") && studentText.contains("unwind")) return true
        if (kw.contains("okazaki") && (studentText.contains("lagging") || studentText.contains("fragments"))) return true

        // Check if individual major words of a multi-word keyword are present
        val words = kw.split(" ").filter { it.length > 3 }
        if (words.size >= 2) {
            val allWordsPresent = words.all { studentText.contains(it) }
            if (allWordsPresent) return true
        }

        return false
    }
}
