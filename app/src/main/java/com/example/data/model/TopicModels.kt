package com.example.data.model

enum class UnitCategory(val code: String, val title: String, val arabicTitle: String) {
    UNIT_1("Unit 1", "Structure & Function in Living Organisms", "التركيب والوظيفة في الكائنات الحية"),
    UNIT_2("Unit 2", "Molecular Biology", "البيولوجيا الجزيئية")
}

data class Chapter(
    val id: String,
    val unit: UnitCategory,
    val number: Int,
    val title: String,
    val arabicTitle: String,
    val summary: String,
    val iconName: String
)

data class SubTopic(
    val id: String,
    val chapterId: String,
    val unit: UnitCategory,
    val title: String,
    val arabicTitle: String,
    val summary: String,
    val highYieldFlag: Boolean = true
)

data class KeyTerm(
    val term: String,
    val definition: String,
    val arabicTerm: String = ""
)

data class FormulaItem(
    val name: String,
    val formulaLatex: String,
    val description: String,
    val variables: String,
    val sampleCalculation: String
)

data class ComparisonTable(
    val title: String,
    val columnA: String,
    val columnB: String,
    val rows: List<ComparisonRow>
)

data class ComparisonRow(
    val criteria: String,
    val valueA: String,
    val valueB: String
)

data class BloomLevelItem(
    val levelName: String, // Remembering, Understanding, Applying, Analyzing, Evaluating, Creating
    val levelNumber: Int,
    val objective: String,
    val masteryPrompt: String
)

data class FlowchartStep(
    val stepNumber: Int,
    val action: String,
    val detail: String,
    val triggerOrEnzyme: String = ""
)

data class VideoQuery(
    val query: String,
    val topicDescription: String,
    val durationRecommendation: String
)

enum class QuestionType(val label: String) {
    SCIENTIFIC_TERM("Scientific Term"),
    GIVE_REASON("Give Reason"),
    WHAT_HAPPENS_IF("What Happens If"),
    DIAGRAM_ANALYSIS("Diagram Analysis"),
    MCQ("Multiple Choice")
}

data class PracticeQuestion(
    val id: String,
    val type: QuestionType,
    val question: String,
    val options: List<String> = emptyList(), // For MCQ
    val correctOptionIndex: Int = -1,        // For MCQ
    val officialAnswer: String,
    val rationale: String,
    val ministryExamTip: String
)

data class ExamTrap(
    val misconception: String,
    val scientificTruth: String,
    val examTrapWarning: String,
    val goldenRule: String
)

data class Flashcard(
    val id: String,
    val topicId: String,
    val frontPrompt: String,
    val backAnswer: String,
    val category: String,
    var boxLevel: Int = 1, // Spaced repetition bucket 1 to 5
    var lastReviewedEpoch: Long = 0L
)

data class MinistryExamItem(
    val id: String,
    val questionType: QuestionType,
    val prompt: String,
    val maxScore: Double = 2.0,
    val requiredKeywords: List<String>,
    val partialCreditKeywords: List<String>,
    val modelAnswer: String,
    val remediationNotes: String
)

data class ExamGradingResult(
    val scoreAwarded: Double,
    val maxScore: Double,
    val detectedKeywords: List<String>,
    val missingKeywords: List<String>,
    val examinerFeedback: String,
    val modelAnswer: String,
    val gradeStatus: GradeStatus
)

enum class GradeStatus {
    FULL_MARK,
    PARTIAL_CREDIT,
    NEEDS_REVISION
}

data class MasterStudyPack(
    val topicId: String,
    val topicTitle: String,
    val unit: UnitCategory,
    val chapterTitle: String,
    // Agent 1: Master Biology Educator
    val eli5Analogy: String,
    val deepTechnicalBreakdown: String,
    val keyTerms: List<KeyTerm>,
    val formulas: List<FormulaItem>,
    val comparisonTables: List<ComparisonTable>,
    val bloomProgression: List<BloomLevelItem>,
    // Agent 2: Visual & Spatial Architect
    val asciiSchematic: String,
    val schematicCaption: String,
    val flowcharts: List<FlowchartStep>,
    val visualInspectionMarkers: List<String>,
    val youtube3DQueries: List<VideoQuery>,
    // Agent 3: Assessment Specialist
    val questions: List<PracticeQuestion>,
    val examTraps: List<ExamTrap>,
    // Agent 4: Adaptive Learning & Evaluator
    val flashcards: List<Flashcard>,
    val examSimulationItem: MinistryExamItem,
    // Ground Truth & Bilingual Support
    val arabicEli5: String = "",
    val textbookPageReference: String = ""
)

enum class MasteryLevel(val label: String, val colorHex: Long) {
    MASTERED("Mastered", 0xFF00897B),
    GOOD("Good", 0xFF43A047),
    LEARNING("Learning", 0xFFFB8C00),
    NEEDS_REVIEW("Needs Review", 0xFFE53935)
}

data class TopicMastery(
    val topicId: String,
    val title: String,
    val chapterTitle: String,
    val unit: UnitCategory,
    val stability: Double,
    val difficulty: Double,
    val reps: Int,
    val masteryLevel: MasteryLevel
)

data class ExamQuestionSubmission(
    val questionId: String,
    val question: PracticeQuestion,
    val studentAnswer: String = "",
    val selectedOptionIndex: Int = -1,
    val scoreAwarded: Double = 0.0,
    val maxScore: Double = 2.0,
    val feedback: String = "",
    val modelAnswer: String = ""
)

data class ExamResultReport(
    val totalScore: Double,
    val maxScore: Double,
    val percentage: Int,
    val letterGrade: String,
    val passed: Boolean,
    val timeSpentSeconds: Long,
    val chapterBreakdown: Map<String, Double> = emptyMap(),
    val questionsReviewed: List<ExamQuestionSubmission> = emptyList()
)

