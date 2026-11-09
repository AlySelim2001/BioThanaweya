package com.example.presentation.viewmodel

import com.example.data.local.entity.ChapterProgress
import com.example.data.repository.ChapterProgressRepositoryImpl
import com.example.domain.repository.ChapterProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChaptersDashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val chaptersFlow = MutableStateFlow<List<ChapterProgress>>(
        ChapterProgressRepositoryImpl.DEFAULT_11_CHAPTERS
    )

    private val fakeRepository = object : ChapterProgressRepository {
        override fun getAllChaptersProgress(): Flow<List<ChapterProgress>> = chaptersFlow

        override fun getChapterProgress(chapterId: Int): Flow<ChapterProgress?> =
            chaptersFlow.map { list -> list.find { it.chapterId == chapterId } }

        override fun getChapterProgressByKey(key: String): Flow<ChapterProgress?> =
            chaptersFlow.map { list -> list.find { it.chapterKey == key } }

        override fun getCompletedChapters(): Flow<List<ChapterProgress>> =
            chaptersFlow.map { list -> list.filter { it.isCompleted || it.completionPercentage >= 100f } }

        override fun getCompletedChaptersCount(): Flow<Int> =
            chaptersFlow.map { list -> list.count { it.isCompleted || it.completionPercentage >= 100f } }

        override fun getAverageCompletionPercentage(): Flow<Float> =
            chaptersFlow.map { list ->
                if (list.isEmpty()) 0f else list.map { it.completionPercentage }.average().toFloat()
            }

        override fun getChaptersByUnit(unitNumber: Int): Flow<List<ChapterProgress>> =
            chaptersFlow.map { list -> list.filter { it.unitNumber == unitNumber } }

        override suspend fun markChapterCompleted(chapterId: Int, isCompleted: Boolean) {
            val updated = chaptersFlow.value.map {
                if (it.chapterId == chapterId) {
                    it.copy(
                        isCompleted = isCompleted,
                        completionPercentage = if (isCompleted) 100f else 0f
                    )
                } else it
            }
            chaptersFlow.value = updated
        }

        override suspend fun updateChapterProgressPercentage(chapterId: Int, percentage: Float) {
            val updated = chaptersFlow.value.map {
                if (it.chapterId == chapterId) {
                    it.copy(
                        completionPercentage = percentage,
                        isCompleted = percentage >= 100f
                    )
                } else it
            }
            chaptersFlow.value = updated
        }

        override suspend fun recordQuestionSolved(chapterId: Int, count: Int) {
            val updated = chaptersFlow.value.map {
                if (it.chapterId == chapterId) {
                    it.copy(questionsSolved = it.questionsSolved + count)
                } else it
            }
            chaptersFlow.value = updated
        }

        override suspend fun recordFlashcardMastered(chapterId: Int, count: Int) {
            val updated = chaptersFlow.value.map {
                if (it.chapterId == chapterId) {
                    it.copy(flashcardsMastered = it.flashcardsMastered + count)
                } else it
            }
            chaptersFlow.value = updated
        }

        override suspend fun addStudyDuration(chapterId: Int, minutes: Int) {
            val updated = chaptersFlow.value.map {
                if (it.chapterId == chapterId) {
                    it.copy(studyTimeMinutes = it.studyTimeMinutes + minutes)
                } else it
            }
            chaptersFlow.value = updated
        }

        override suspend fun saveChapterProgress(progress: ChapterProgress) {}
        override suspend fun seedDefault11ChaptersIfEmpty() {}
        override suspend fun resetAllProgress() {
            val reset = chaptersFlow.value.map {
                it.copy(
                    isCompleted = false,
                    completionPercentage = 0f,
                    questionsSolved = 0,
                    flashcardsMastered = 0,
                    studyTimeMinutes = 0
                )
            }
            chaptersFlow.value = reset
        }
    }

    private lateinit var viewModel: ChaptersDashboardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        chaptersFlow.value = ChapterProgressRepositoryImpl.DEFAULT_11_CHAPTERS
        viewModel = ChaptersDashboardViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialUiStateContainsAll11Chapters() = runTest(testDispatcher) {
        val collectJob = launch { viewModel.uiState.collect {} }
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(11, state.allChapters.size)
        assertEquals(11, state.filteredChapters.size)
        assertEquals(11, state.totalChaptersCount)
        assertEquals(0, state.completedChaptersCount)

        collectJob.cancel()
    }

    @Test
    fun testUnit1AndUnit2Filtering() = runTest(testDispatcher) {
        val collectJob = launch { viewModel.uiState.collect {} }
        testScheduler.advanceUntilIdle()

        // Filter Unit 1 (Structure and Function - 9 chapters)
        viewModel.setFilterMode(ChapterFilterMode.UNIT_1)
        testScheduler.advanceUntilIdle()
        val unit1State = viewModel.uiState.value
        assertEquals(9, unit1State.filteredChapters.size)
        assertTrue(unit1State.filteredChapters.all { it.unitNumber == 1 })

        // Filter Unit 2 (Molecular Biology - 2 chapters)
        viewModel.setFilterMode(ChapterFilterMode.UNIT_2)
        testScheduler.advanceUntilIdle()
        val unit2State = viewModel.uiState.value
        assertEquals(2, unit2State.filteredChapters.size)
        assertTrue(unit2State.filteredChapters.all { it.unitNumber == 2 })

        collectJob.cancel()
    }

    @Test
    fun testArabicAndEnglishSearchFiltering() = runTest(testDispatcher) {
        val collectJob = launch { viewModel.uiState.collect {} }
        testScheduler.advanceUntilIdle()

        // Search by Arabic substring "الدعامة"
        viewModel.onSearchQueryChanged("الدعامة")
        testScheduler.advanceUntilIdle()
        val arabicResult = viewModel.uiState.value
        assertEquals(1, arabicResult.filteredChapters.size)
        assertEquals(1, arabicResult.filteredChapters.first().chapterId)
        assertTrue(arabicResult.filteredChapters.first().titleAr.contains("الدعامة"))

        // Search by English substring "DNA"
        viewModel.onSearchQueryChanged("DNA")
        testScheduler.advanceUntilIdle()
        val englishResult = viewModel.uiState.value
        assertEquals(1, englishResult.filteredChapters.size)
        assertEquals(10, englishResult.filteredChapters.first().chapterId)

        collectJob.cancel()
    }

    @Test
    fun testToggleCompletionUpdatesCircularProgressAndCounters() = runTest(testDispatcher) {
        val collectJob = launch { viewModel.uiState.collect {} }
        testScheduler.advanceUntilIdle()

        // Toggle chapter 1 as completed
        viewModel.toggleChapterCompletion(1)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.completedChaptersCount)
        val chapter1 = state.allChapters.find { it.chapterId == 1 }
        assertNotNull(chapter1)
        assertTrue(chapter1!!.isCompleted)
        assertEquals(100f, chapter1.completionPercentage, 0.01f)
        assertEquals(1.0f, chapter1.progressFraction, 0.01f)

        // Filter by completed only
        viewModel.setFilterMode(ChapterFilterMode.COMPLETED_ONLY)
        testScheduler.advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.filteredChapters.size)

        collectJob.cancel()
    }

    @Test
    fun testResetAllCurriculumProgress() = runTest(testDispatcher) {
        val collectJob = launch { viewModel.uiState.collect {} }
        testScheduler.advanceUntilIdle()

        viewModel.toggleChapterCompletion(1)
        viewModel.toggleChapterCompletion(2)
        testScheduler.advanceUntilIdle()
        assertEquals(2, viewModel.uiState.value.completedChaptersCount)

        viewModel.resetAllCurriculumProgress()
        testScheduler.advanceUntilIdle()

        val resetState = viewModel.uiState.value
        assertEquals(0, resetState.completedChaptersCount)
        assertEquals(0f, resetState.overallCompletionPercentage, 0.01f)
        assertTrue(resetState.allChapters.all { !it.isCompleted && it.completionPercentage == 0f })

        collectJob.cancel()
    }
}
