package com.example.domain.model

enum class MetacognitiveGapType {
    CALIBRATED_MASTERY,
    OVERCONFIDENCE_BIAS,
    UNDERCONFIDENCE_DOUBT,
    CALIBRATED_HUMILITY
}

data class MetacognitiveAnalysis(
    val confidencePercent: Int,
    val isCorrect: Boolean,
    val gapType: MetacognitiveGapType,
    val feedbackTitle: String,
    val actionableAdvice: String
)

data class PblRubricCriterion(
    val title: String,
    val weightPercent: Int,
    val exemplaryCriteria: String,
    val noviceCriteria: String
) {
    val criterion: String get() = title
    val weightPoints: Int get() = weightPercent
    val excellentDescription: String get() = exemplaryCriteria
    val poorDescription: String get() = noviceCriteria
}

data class PblProject(
    val id: String,
    val topicId: String,
    val title: String,
    val titleArabic: String,
    val scenario: String,
    val mission: String,
    val rubrics: List<PblRubricCriterion>,
    val tips: List<String> = emptyList()
)

data class StudentBadge(
    val id: String,
    val title: String,
    val titleArabic: String,
    val iconName: String,
    val description: String
)

data class WellbeingProfile(
    val streakDays: Int,
    val totalXp: Int,
    val earnedBadges: List<StudentBadge>,
    val examAnxietyScore: Int,
    val studyChunkMinutes: Int,
    val motivationalQuote: String
)
