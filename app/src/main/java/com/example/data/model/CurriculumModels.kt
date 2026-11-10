package com.example.data.model

enum class UnitCategory(val arabicTitle: String, val englishTitle: String, val code: String) {
    UNIT_1("الوحدة الأولى: التركيب والوظيفة في الكائنات الحية", "Unit 1: Structure & Function in Living Organisms", "Unit 1"),
    UNIT_2("الوحدة الثانية: البيولوجيا الجزيئية", "Unit 2: Molecular Biology", "Unit 2"),
    UNIT_3("الوحدة الثالثة: الوراثة والمناعة", "Unit 3: Genetics & Immunity", "Unit 3"),
    COMPREHENSIVE("المراجعات الشاملة ونماذج الوزارة", "Comprehensive Reviews & Ministry Papers", "Comp");

    val title: String get() = arabicTitle
}

enum class QuestionType(val label: String) {
    MCQ("MCQ"),
    GIVE_REASON("Give Reason"),
    WHAT_HAPPENS_IF("What Happens If"),
    DIAGRAM_ANALYSIS("Diagram Analysis"),
    SCIENTIFIC_TERM("Scientific Term")
}

enum class MasteryLevel(val label: String, val arabicLabel: String, val colorHex: Long) {
    MASTERED("Mastered", "متقن تماماً", 0xFF10B981L),
    GOOD("Good", "جيد", 0xFF43A047L),
    LEARNING("Learning", "قيد التعلم", 0xFFF59E0BL),
    NEEDS_REVIEW("Needs Review", "يحتاج مراجعة", 0xFFEF4444L);
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
    val highYieldFlag: Boolean = false
) {
    val topicDescription: String get() = summary
    val titleAr: String get() = arabicTitle
    val titleEn: String get() = title
}

data class KeyTerm(
    val term: String,
    val definition: String,
    val arabicTerm: String
)

data class FormulaItem(
    val name: String,
    val formulaLatex: String,
    val description: String,
    val variables: String,
    val sampleCalculation: String
)

data class ComparisonRow(
    val aspect: String,
    val valueA: String,
    val valueB: String
)

data class ComparisonTable(
    val title: String,
    val columnA: String,
    val columnB: String,
    val rows: List<ComparisonRow>,
    val columnAspect: String = "وجه المقارنة"
)

data class BloomLevelItem(
    val levelName: String,
    val levelNumber: Int,
    val description: String,
    val sampleQuestion: String
)

data class FlowchartStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val tag: String
)

data class VideoQuery(
    val query: String,
    val arabicTitle: String,
    val duration: String
) {
    val topicDescription: String get() = arabicTitle
}

data class PracticeQuestion(
    val id: String,
    val type: QuestionType,
    val question: String,
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = 0,
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
    val boxLevel: Int = 1
)

data class MinistryExamItem(
    val id: String,
    val questionType: QuestionType,
    val prompt: String,
    val maxScore: Double,
    val requiredKeywords: List<String>,
    val partialCreditKeywords: List<String>,
    val modelAnswer: String,
    val remediationNotes: String
)

data class MasterStudyPack(
    val topicId: String,
    val topicTitle: String,
    val unit: UnitCategory,
    val chapterTitle: String,
    val eli5Analogy: String,
    val deepTechnicalBreakdown: String,
    val keyTerms: List<KeyTerm> = emptyList(),
    val formulas: List<FormulaItem> = emptyList(),
    val comparisonTables: List<ComparisonTable> = emptyList(),
    val bloomProgression: List<BloomLevelItem> = emptyList(),
    val asciiSchematic: String = "",
    val schematicCaption: String = "",
    val flowcharts: List<FlowchartStep> = emptyList(),
    val visualInspectionMarkers: List<String> = emptyList(),
    val youtube3DQueries: List<VideoQuery> = emptyList(),
    val questions: List<PracticeQuestion> = emptyList(),
    val examTraps: List<ExamTrap> = emptyList(),
    val flashcards: List<Flashcard> = emptyList(),
    val examSimulationItem: MinistryExamItem? = null,
    val arabicEli5: String = "",
    val textbookPageReference: String = ""
)

data class TopicMastery(
    val topicId: String,
    val title: String,
    val chapterTitle: String,
    val unit: UnitCategory,
    val stability: Double,
    val difficulty: Double,
    val reps: Int,
    val masteryLevel: MasteryLevel,
    val titleAr: String = title,
    val titleEn: String = title,
    val chapterNumber: Int = 1,
    val masteryPercentage: Int = when (masteryLevel) {
        MasteryLevel.MASTERED -> 92
        MasteryLevel.GOOD -> 75
        MasteryLevel.LEARNING -> 50
        MasteryLevel.NEEDS_REVIEW -> 25
    },
    val cardsReviewed: Int = reps
)
