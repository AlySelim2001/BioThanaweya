package com.example.domain.model

import com.example.data.model.QuestionType
import com.example.data.model.UnitCategory

enum class ExamDurationMode(val title: String, val arabicTitle: String, val minutes: Int) {
    OFFICIAL_STANDARD("Official 3-Hour Exam", "امتحان رسمي كامل (3 ساعات)", 180),
    ACCELERATED_MOCK("Accelerated Mock (90 Min)", "محاكاة مكثفة (90 دقيقة)", 90),
    EXPRESS_DRILL("Express Speed Drill (45 Min)", "تدريب سرعة مركز (45 دقيقة)", 45)
}

enum class ExamCognitiveLevel(val label: String, val arabicLabel: String) {
    KNOWLEDGE_RECALL("Knowledge & Recall", "تذكر ومعرفة"),
    APPLICATION_CALCULATION("Application & Calculation", "تطبيق وحسابات"),
    HIGHER_ORDER_ANALYSIS("Higher-Order Analysis", "مستويات عليا وتحليل")
}

data class MinistryExamQuestion(
    val number: Int, // 1 to 46
    val type: QuestionType, // MCQ or ESSAY / GIVE_REASON / SCIENTIFIC_TERM
    val marks: Double, // 1.0 or 2.0
    val unit: UnitCategory,
    val topicTitle: String,
    val cognitiveLevel: ExamCognitiveLevel,
    val questionArabic: String,
    val questionEnglish: String,
    val optionsArabic: List<String> = emptyList(),
    val optionsEnglish: List<String> = emptyList(),
    val correctOptionIndex: Int = -1, // 0..3 for MCQ
    val requiredKeywords: List<String> = emptyList(), // For Essay
    val partialCreditKeywords: List<String> = emptyList(),
    val modelAnswerArabic: String,
    val modelAnswerEnglish: String,
    val scientificExplanationArabic: String,
    val scientificExplanationEnglish: String,
    val ministryTrapWarningArabic: String,
    val ministryTrapWarningEnglish: String,
    val diagramAscii: String? = null
)

data class MinistryExamPaper(
    val id: String,
    val code: String, // e.g. "BIO-2024-S1"
    val title: String,
    val arabicTitle: String,
    val year: Int,
    val session: String, // "الدور الأول"
    val durationMinutes: Int = 180,
    val totalMarks: Double = 60.0,
    val passMark: Double = 30.0,
    val descriptionArabic: String,
    val descriptionEnglish: String,
    val questions: List<MinistryExamQuestion>
)

data class BubbleSheetEntry(
    val questionNumber: Int,
    val selectedOption: Int = -1, // 0=A, 1=B, 2=C, 3=D, -1=empty
    val isFlaggedForReview: Boolean = false,
    val isDoubleMarkedWarning: Boolean = false
)

data class QuestionGradingSummary(
    val question: MinistryExamQuestion,
    val studentSelectedOption: Int = -1,
    val studentEssayAnswer: String = "",
    val scoreAwarded: Double = 0.0,
    val maxScore: Double = 1.0,
    val isCorrect: Boolean = false,
    val detectedKeywords: List<String> = emptyList(),
    val missingKeywords: List<String> = emptyList(),
    val examinerNotes: String = ""
)

data class UnitPerformanceBreakdown(
    val unit: UnitCategory,
    val unitNameArabic: String,
    val unitNameEnglish: String,
    val scoredMarks: Double,
    val totalMarks: Double,
    val percentage: Double,
    val status: String
)

data class MinistryExamFullReport(
    val paperId: String,
    val paperTitleArabic: String,
    val paperTitleEnglish: String,
    val totalScoreAwarded: Double,
    val maxScore: Double = 60.0,
    val percentage: Double,
    val nationalPercentile: Double, // e.g. 96.5%
    val ministryGradeLabelArabic: String, // ممتاز / جيد جداً / جيد / مقبول / دور ثان
    val ministryGradeLabelEnglish: String,
    val timeSpentSeconds: Long,
    val unitBreakdowns: List<UnitPerformanceBreakdown>,
    val questionSummaries: List<QuestionGradingSummary>,
    val fallenTraps: List<Pair<MinistryExamQuestion, String>>, // Question and Trap warning
    val recommendedActions: List<String>
)
