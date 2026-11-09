package com.example

import android.graphics.Bitmap
import com.example.domain.model.DiagramStyle
import com.example.domain.repository.AiRepository
import com.example.domain.usecase.Agent2VisualArchitectUseCase
import com.example.presentation.viewmodel.BiologicalDiagramViewModel
import com.example.presentation.viewmodel.DiagramGenUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class BiologicalDiagramViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val fakeAiRepository = object : AiRepository {
        var shouldSucceed = true
        var failureMessage = "Network error"

        override suspend fun generateExplanation(topic: String, style: String): Result<String> = Result.success("test")
        override suspend fun evaluateAnswer(question: String, studentAnswer: String, modelAnswer: String): Result<String> = Result.success("test")
        override suspend fun generatePblProject(topic: String): Result<String> = Result.success("test")

        override suspend fun generateBiologicalDiagram(prompt: String): Result<Bitmap> {
            return if (shouldSucceed) {
                val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                Result.success(bitmap)
            } else {
                Result.failure(IllegalStateException(failureMessage))
            }
        }
    }

    private lateinit var visualArchitectUseCase: Agent2VisualArchitectUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        visualArchitectUseCase = Agent2VisualArchitectUseCase(fakeAiRepository)
        fakeAiRepository.shouldSucceed = true
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel initializes with topic presets in Idle state`() = runTest(testDispatcher) {
        val viewModel = BiologicalDiagramViewModel("sarcomere_contraction", visualArchitectUseCase)
        val state = viewModel.uiState.value
        assertTrue("State must be Idle", state is DiagramGenUiState.Idle)

        val idle = state as DiagramGenUiState.Idle
        assertTrue("Must contain presets for sarcomere_contraction", idle.presets.isNotEmpty())
        assertNotNull("Must select first preset by default", idle.selectedPreset)
        assertEquals("Default style must be LABELED_TEXTBOOK", DiagramStyle.LABELED_TEXTBOOK, idle.selectedStyle)
    }

    @Test
    fun `preset and style selection update Idle state`() = runTest(testDispatcher) {
        val viewModel = BiologicalDiagramViewModel("sarcomere_contraction", visualArchitectUseCase)
        val initialIdle = viewModel.uiState.value as DiagramGenUiState.Idle

        if (initialIdle.presets.size > 1) {
            val secondPreset = initialIdle.presets[1]
            viewModel.selectPreset(secondPreset)
            val updated = viewModel.uiState.value as DiagramGenUiState.Idle
            assertEquals("Selected preset should update", secondPreset.id, updated.selectedPreset?.id)
        }

        viewModel.setStyle(DiagramStyle.MINISTRY_EXAM_LINE_ART)
        val updatedStyle = viewModel.uiState.value as DiagramGenUiState.Idle
        assertEquals("Style should be MINISTRY_EXAM_LINE_ART", DiagramStyle.MINISTRY_EXAM_LINE_ART, updatedStyle.selectedStyle)

        viewModel.setCustomPrompt("Show cross section")
        val updatedPrompt = viewModel.uiState.value as DiagramGenUiState.Idle
        assertEquals("Custom prompt should update", "Show cross section", updatedPrompt.customPrompt)
    }

    @Test
    fun `generateDiagram transitions through Generating to Success when AI returns bitmap`() = runTest(testDispatcher) {
        val viewModel = BiologicalDiagramViewModel("sarcomere_contraction", visualArchitectUseCase)
        viewModel.generateDiagram()

        testScheduler.advanceUntilIdle()

        // Should be Success
        val successState = viewModel.uiState.value
        assertTrue("State should be Success, was: $successState", successState is DiagramGenUiState.Success)

        val success = successState as DiagramGenUiState.Success
        assertNotNull("Diagram should be non-null", success.diagram)
        assertNotNull("Bitmap should be non-null", success.diagram.bitmap)
        assertTrue("Landmarks should not be empty", success.diagram.keyLandmarks.isNotEmpty())
        assertTrue("Exam trap warning should not be blank", success.diagram.examTrapWarning.isNotBlank())
        assertTrue("Golden rule should not be blank", success.diagram.goldenRule.isNotBlank())
    }

    @Test
    fun `generateDiagram transitions to Error when AI generation fails`() = runTest(testDispatcher) {
        fakeAiRepository.shouldSucceed = false
        fakeAiRepository.failureMessage = "App Check verification failed"

        val viewModel = BiologicalDiagramViewModel("sarcomere_contraction", visualArchitectUseCase)
        viewModel.generateDiagram()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Error when AI fails", state is DiagramGenUiState.Error)

        val error = state as DiagramGenUiState.Error
        assertTrue("Error message should contain failure reason", error.message.contains("App Check verification failed"))
    }

    @Test
    fun `quick review toggle and landmark selection update Success state`() = runTest(testDispatcher) {
        val viewModel = BiologicalDiagramViewModel("sarcomere_contraction", visualArchitectUseCase)
        viewModel.generateDiagram()
        testScheduler.advanceUntilIdle()

        var success = viewModel.uiState.value as DiagramGenUiState.Success
        val initialExpanded = success.isQuickReviewExpanded

        viewModel.toggleQuickReview()
        success = viewModel.uiState.value as DiagramGenUiState.Success
        assertEquals("Expanded state should toggle", !initialExpanded, success.isQuickReviewExpanded)

        viewModel.selectActiveLandmark(2)
        success = viewModel.uiState.value as DiagramGenUiState.Success
        assertEquals("Active landmark should be 2", 2, success.activeLandmarkIndex)

        viewModel.resetToIdle()
        assertTrue("Should reset to Idle", viewModel.uiState.value is DiagramGenUiState.Idle)
    }
}
