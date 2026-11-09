package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.DiagramPreset
import com.example.domain.model.DiagramStyle
import com.example.domain.model.GeneratedBiologicalDiagram
import com.example.domain.usecase.Agent2VisualArchitectUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DiagramGenUiState {
    data class Idle(
        val presets: List<DiagramPreset>,
        val selectedPreset: DiagramPreset?,
        val selectedStyle: DiagramStyle = DiagramStyle.LABELED_TEXTBOOK,
        val customPrompt: String = ""
    ) : DiagramGenUiState

    data class Generating(
        val prompt: String,
        val style: DiagramStyle,
        val progressMessage: String,
        val elapsedSeconds: Int = 0
    ) : DiagramGenUiState

    data class Success(
        val diagram: GeneratedBiologicalDiagram,
        val presets: List<DiagramPreset>,
        val isQuickReviewExpanded: Boolean = true,
        val activeLandmarkIndex: Int = 0
    ) : DiagramGenUiState

    data class Error(
        val message: String,
        val canRetry: Boolean = true,
        val presets: List<DiagramPreset>
    ) : DiagramGenUiState
}

class BiologicalDiagramViewModel(
    val topicId: String,
    private val visualArchitectUseCase: Agent2VisualArchitectUseCase
) : ViewModel() {

    private val presets: List<DiagramPreset> by lazy {
        visualArchitectUseCase.getDiagramPresets(topicId)
    }

    private val _uiState = MutableStateFlow<DiagramGenUiState>(
        DiagramGenUiState.Idle(
            presets = presets,
            selectedPreset = presets.firstOrNull()
        )
    )
    val uiState: StateFlow<DiagramGenUiState> = _uiState.asStateFlow()

    fun selectPreset(preset: DiagramPreset) {
        val current = _uiState.value
        if (current is DiagramGenUiState.Idle) {
            _uiState.value = current.copy(selectedPreset = preset)
        }
    }

    fun setStyle(style: DiagramStyle) {
        val current = _uiState.value
        if (current is DiagramGenUiState.Idle) {
            _uiState.value = current.copy(selectedStyle = style)
        }
    }

    fun setCustomPrompt(prompt: String) {
        val current = _uiState.value
        if (current is DiagramGenUiState.Idle) {
            _uiState.value = current.copy(customPrompt = prompt)
        }
    }

    fun generateDiagram() {
        val current = _uiState.value
        val (preset, style, custom) = when (current) {
            is DiagramGenUiState.Idle -> Triple(current.selectedPreset, current.selectedStyle, current.customPrompt)
            is DiagramGenUiState.Error -> Triple(presets.firstOrNull(), DiagramStyle.LABELED_TEXTBOOK, "")
            is DiagramGenUiState.Success -> Triple(presets.firstOrNull(), DiagramStyle.LABELED_TEXTBOOK, "")
            is DiagramGenUiState.Generating -> return
        }

        viewModelScope.launch {
            _uiState.value = DiagramGenUiState.Generating(
                prompt = custom.ifBlank { preset?.titleAr ?: "توليد مخطط بيولوجي" },
                style = style,
                progressMessage = "بدء الاتصال بمحرك الذكاء الاصطناعي (Gemini Nano Banana)..."
            )

            // Dynamic progress status updates
            val timerJob = launch {
                val stages = listOf(
                    1 to "تحليل البنية التشريحية لمنهج الثانوية العامة...",
                    4 to "توليد الأسهم والمعالم البيولوجية بدقة عالية...",
                    10 to "صياغة المخطط التوضيحي وتنسيقه للمراجعة السريعة...",
                    20 to "تحسين التباين ودقة المسميات العلمية..."
                )
                var elapsed = 0
                while (true) {
                    delay(1000)
                    elapsed++
                    val nextMsg = stages.lastOrNull { elapsed >= it.first }?.second
                        ?: "جاري استكمال إنتاج المخطط العلمي..."
                    val activeGen = _uiState.value as? DiagramGenUiState.Generating
                    if (activeGen != null) {
                        _uiState.value = activeGen.copy(
                            progressMessage = nextMsg,
                            elapsedSeconds = elapsed
                        )
                    }
                }
            }

            try {
                val result = visualArchitectUseCase.generateDiagram(
                    topicId = topicId,
                    preset = preset,
                    customPrompt = custom,
                    style = style
                )
                timerJob.cancel()

                result.fold(
                    onSuccess = { diagram ->
                        _uiState.value = DiagramGenUiState.Success(
                            diagram = diagram,
                            presets = presets
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = DiagramGenUiState.Error(
                            message = error.localizedMessage ?: "فشل في توليد المخطط البيولوجي عبر الذكاء الاصطناعي.",
                            presets = presets
                        )
                    }
                )
            } catch (e: CancellationException) {
                timerJob.cancel()
                throw e
            } catch (e: Exception) {
                timerJob.cancel()
                _uiState.value = DiagramGenUiState.Error(
                    message = e.localizedMessage ?: "حدث خطأ غير متوقع أثناء توليد الصورة.",
                    presets = presets
                )
            }
        }
    }

    fun toggleQuickReview() {
        val current = _uiState.value
        if (current is DiagramGenUiState.Success) {
            _uiState.value = current.copy(isQuickReviewExpanded = !current.isQuickReviewExpanded)
        }
    }

    fun selectActiveLandmark(index: Int) {
        val current = _uiState.value
        if (current is DiagramGenUiState.Success) {
            _uiState.value = current.copy(activeLandmarkIndex = index)
        }
    }

    fun resetToIdle() {
        _uiState.value = DiagramGenUiState.Idle(
            presets = presets,
            selectedPreset = presets.firstOrNull()
        )
    }
}
