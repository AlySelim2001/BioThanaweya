package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.ExamAttemptEntity
import com.example.data.local.entity.ExamMistakeEntity
import com.example.domain.repository.BiologyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChapterVulnerability(
    val chapterKey: String,
    val titleArabic: String,
    val masteryScore: Float, // 0.0 to 1.0
    val mistakeCount: Int
)

data class MistakeAnalyticsState(
    val totalMistakes: Int = 0,
    val unresolvedCount: Int = 0,
    val resolutionRatePercentage: Int = 0,
    val chapterScores: List<ChapterVulnerability> = emptyList(),
    val mistakesList: List<ExamMistakeEntity> = emptyList(),
    val estimatedExamImpactMarks: Double = 0.0
)

class MistakeAnalyticsViewModel @Inject constructor(
    private val biologyRepository: BiologyRepository
) : ViewModel() {

    private val allMistakes = biologyRepository.getAllMistakes()
    private val allAttempts = biologyRepository.getExamAttempts()

    val analyticsState: StateFlow<MistakeAnalyticsState> = combine(allMistakes, allAttempts) { mistakes, attempts ->
        val total = mistakes.size
        val unresolved = mistakes.count { !it.isResolved }
        val resolved = total - unresolved
        val resolutionRate = if (total > 0) ((resolved.toDouble() / total) * 100).toInt() else 100

        // Calculate chapter vulnerability
        val chapters = listOf(
            "support_movement" to "الدعامة والحركة",
            "hormonal_coordination" to "التنسيق الهرموني",
            "reproduction" to "التكاثر",
            "immunity" to "المناعة",
            "molecular_genetics" to "البيولوجيا الجزيئية"
        )

        val chapterVulnerabilities = chapters.map { (key, title) ->
            val count = mistakes.count { m ->
                val txt = m.questionTextArabic
                when (key) {
                    "support_movement" -> txt.contains("دعائم") || txt.contains("عضل") || txt.contains("حركة") || txt.contains("سارك") || m.questionNumber <= 8
                    "hormonal_coordination" -> txt.contains("هرمون") || txt.contains("غدة") || txt.contains("أنسولين") || (m.questionNumber in 9..14)
                    "reproduction" -> txt.contains("تكاثر") || txt.contains("حيوان منوي") || txt.contains("بويضة") || (m.questionNumber in 15..24)
                    "immunity" -> txt.contains("مناع") || txt.contains("جسم مضاد") || txt.contains("تائية") || (m.questionNumber in 25..32)
                    else -> m.questionNumber >= 33 || txt.contains("DNA") || txt.contains("RNA")
                }
            }

            // Mastery is 1.0 minus mistake ratio penalty (clamped 0.2 to 1.0)
            val mastery = (1.0f - (count * 0.15f)).coerceIn(0.2f, 1.0f)
            ChapterVulnerability(key, title, mastery, count)
        }

        // Estimated mark impact out of 60
        val impact = (unresolved * 1.5).coerceAtMost(18.0)

        MistakeAnalyticsState(
            totalMistakes = total,
            unresolvedCount = unresolved,
            resolutionRatePercentage = resolutionRate,
            chapterScores = chapterVulnerabilities,
            mistakesList = mistakes,
            estimatedExamImpactMarks = impact
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MistakeAnalyticsState()
    )

    fun resolveMistake(id: Long) {
        viewModelScope.launch {
            biologyRepository.resolveMistake(id)
        }
    }

    fun clearResolvedMistakes() {
        viewModelScope.launch {
            biologyRepository.clearResolvedMistakes()
        }
    }
}
