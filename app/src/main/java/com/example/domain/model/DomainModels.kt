package com.example.domain.model

data class PblProject(
    val id: String,
    val topicId: String,
    val title: String,
    val titleArabic: String,
    val scenario: String,
    val mission: String,
    val rubrics: List<PblRubricCriterion>,
    val tips: List<String>
)

data class PblRubricCriterion(
    val criterion: String,
    val weightPoints: Int,
    val excellentDescription: String,
    val poorDescription: String
)

data class WellbeingProfile(
    val streakDays: Int = 3,
    val totalXp: Int = 450,
    val earnedBadges: List<StudentBadge> = emptyList(),
    val examAnxietyScore: Int = 20, // 0-100
    val studyChunkMinutes: Int = 25,
    val motivationalQuote: String = "التميز عادة، وليس فعلًا عابرًا. استمر في التركيز على الآليات والروابط الجزيئية!"
)

data class StudentBadge(
    val id: String,
    val title: String,
    val titleArabic: String,
    val iconName: String,
    val description: String
)

data class MetacognitiveAnalysis(
    val confidencePercent: Int,
    val isCorrect: Boolean,
    val gapType: MetacognitiveGapType,
    val feedbackTitle: String,
    val actionableAdvice: String
)

enum class MetacognitiveGapType {
    CALIBRATED_MASTERY, // High confidence + correct
    OVERCONFIDENCE_BIAS, // High confidence + wrong (Dunning-Kruger effect)
    UNDERCONFIDENCE_DOUBT, // Low confidence + correct (Imposter effect)
    CALIBRATED_HUMILITY // Low confidence + wrong (Aware of knowledge gap)
}
