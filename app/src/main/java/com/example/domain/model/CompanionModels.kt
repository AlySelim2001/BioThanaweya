package com.example.domain.model

enum class StudentSentiment(val labelArabic: String, val labelEnglish: String, val emoji: String) {
    CONFIDENT("واثق ومستعد", "Confident", "💪"),
    CURIOUS("شغوف بالاستكشاف", "Curious", "🔍"),
    CONFUSED("مشوش أو مرتبك", "Confused", "🤔"),
    ANXIOUS("قلق من الامتحان", "Anxious", "😰"),
    FATIGUED("مرهق أو مجهد", "Fatigued", "🥱")
}

enum class StudentComprehensionLevel(val labelArabic: String, val labelEnglish: String, val levelScore: Int) {
    NOVICE("مبتدئ (يحتاج تأصيل المفهوم)", "Novice", 1),
    PROFICIENT("متمكن (يحتاج التدريب على المصائد)", "Proficient", 2),
    MASTERED("خبير متميز (مستعد لأسئلة المستويات العليا)", "Mastered", 3)
}

data class MicroIntervention(
    val id: String,
    val titleArabic: String,
    val auxiliaryAnalogy: String,
    val correctiveDrillPrompt: String,
    val correctiveModelAnswer: String,
    val scaffoldingAdjustment: String,
    val empatheticEncouragement: String
)

data class CompanionDiagnosticSession(
    val topicId: String,
    val initialSentiment: StudentSentiment,
    val detectedComprehensionLevel: StudentComprehensionLevel,
    val pedagogicalBaselineTone: String,
    val empatheticGreeting: String,
    val checkInQuestionArabic: String,
    val targetedMicroIntervention: MicroIntervention
)

data class AnatomicalDissectionLayer(
    val layerNumber: Int,
    val layerNameArabic: String,
    val layerNameEnglish: String,
    val histologicalDescription: String,
    val magnificationLevel: String,
    val clinicalExamRelevance: String
)

data class AnatomicalDissectionProtocol(
    val topicId: String,
    val organOrSystemArabic: String,
    val organOrSystemEnglish: String,
    val macroscopicOverview: String,
    val dissectionSteps: List<String>,
    val microscopicLayers: List<AnatomicalDissectionLayer>,
    val openSource3DParameters: String
)
