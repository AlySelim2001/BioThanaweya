package com.example

import com.example.data.local.entity.ExamAttemptEntity
import com.example.data.local.entity.FlashcardEntity
import com.example.data.local.entity.StudentProgressEntity
import com.example.data.local.entity.TopicMasteryEntity
import com.example.data.model.MasterStudyPack
import com.example.data.model.UnitCategory
import com.example.domain.fsrs.FSRSRating
import com.example.domain.fsrs.SchedulingInfo
import com.example.domain.repository.BiologyRepository
import com.example.presentation.viewmodel.ChartType
import com.example.presentation.viewmodel.ProgressUiState
import com.example.presentation.viewmodel.ProgressViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProgressViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeMasteriesFlow = MutableStateFlow<List<TopicMasteryEntity>>(emptyList())

    private val fakeRepository = object : BiologyRepository {
        override fun getAllTopics(): List<MasterStudyPack> = emptyList()
        override fun getTopicById(topicId: String): MasterStudyPack? = null
        override fun searchCurriculum(query: String): List<MasterStudyPack> = emptyList()
        override fun getDueCards(nowEpoch: Long): Flow<List<FlashcardEntity>> = MutableStateFlow(emptyList())
        override fun getCardsByTopic(topicId: String): Flow<List<FlashcardEntity>> = MutableStateFlow(emptyList())
        override suspend fun recordCardReview(cardId: String, rating: FSRSRating): SchedulingInfo = error("unused")
        override suspend fun seedDefaultCardsIfEmpty() {}
        override fun getStudentProgress(): Flow<StudentProgressEntity?> = MutableStateFlow(null)
        override suspend fun addXpAndStreak(xpToAdd: Int) {}
        override fun getExamAttempts(): Flow<List<ExamAttemptEntity>> = MutableStateFlow(emptyList())
        override fun getLatestExamAttempt(): Flow<ExamAttemptEntity?> = MutableStateFlow(null)
        override suspend fun recordExamAttempt(attempt: ExamAttemptEntity): Long = 1L
        override fun getTopicMasteries(): Flow<List<TopicMasteryEntity>> = fakeMasteriesFlow
        override suspend fun updateTopicMastery(topicId: String, scoreEarned: Float) {
            val current = fakeMasteriesFlow.value.toMutableList()
            val idx = current.indexOfFirst { it.topicId == topicId }
            if (idx >= 0) {
                current[idx] = current[idx].copy(masteryLevel = scoreEarned)
            } else {
                current.add(
                    TopicMasteryEntity(
                        topicId = topicId,
                        topicTitle = topicId,
                        unitNumber = 1,
                        masteryLevel = scoreEarned,
                        cardsReviewed = 5,
                        averageStability = 4.0
                    )
                )
            }
            fakeMasteriesFlow.value = current
        }
        override suspend fun initializeTopicMasteriesIfEmpty() {}
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeMasteriesFlow.value = listOf(
            TopicMasteryEntity("sarcomere_contraction", "Support & Movement", 1, 0.88f, 10, 5.0),
            TopicMasteryEntity("nutrition_digestion", "Nutrition", 1, 0.75f, 8, 4.0),
            TopicMasteryEntity("dna_structure_replication", "DNA", 2, 0.90f, 12, 6.0)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `progressViewModel loads all 11 topics and calculates score`() = runTest(testDispatcher) {
        val viewModel = ProgressViewModel(fakeRepository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("UiState must be Success", state is ProgressUiState.Success)

        val success = state as ProgressUiState.Success
        assertEquals("Must contain exactly 11 topics", 11, success.allTopics.size)
        assertTrue("Overall mastery must be positive", success.overallMasteryPercentage > 0)
        assertTrue("Predicted score must be <= 60.0", success.predictedMinistryScore in 0.0..60.0)
    }

    @Test
    fun `filtering by Unit 1 returns 8 topics and Unit 2 returns 3 topics`() = runTest(testDispatcher) {
        val viewModel = ProgressViewModel(fakeRepository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        testScheduler.advanceUntilIdle()

        // Filter Unit 1
        viewModel.setUnitFilter(UnitCategory.UNIT_1)
        testScheduler.advanceUntilIdle()

        var success = viewModel.uiState.value as ProgressUiState.Success
        assertEquals("Unit 1 should contain 8 topics", 8, success.filteredTopics.size)

        // Filter Unit 2
        viewModel.setUnitFilter(UnitCategory.UNIT_2)
        testScheduler.advanceUntilIdle()

        success = viewModel.uiState.value as ProgressUiState.Success
        assertEquals("Unit 2 should contain 3 topics", 3, success.filteredTopics.size)

        // Filter All
        viewModel.setUnitFilter(null)
        testScheduler.advanceUntilIdle()

        success = viewModel.uiState.value as ProgressUiState.Success
        assertEquals("All units combined must be 11 topics", 11, success.filteredTopics.size)
    }

    @Test
    fun `chart type toggle and topic selection update state`() = runTest(testDispatcher) {
        val viewModel = ProgressViewModel(fakeRepository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        testScheduler.advanceUntilIdle()

        viewModel.setChartType(ChartType.RADAR_D3)
        viewModel.selectTopic("nutrition_digestion")
        testScheduler.advanceUntilIdle()

        val success = viewModel.uiState.value as ProgressUiState.Success
        assertEquals("Chart type should be RADAR_D3", ChartType.RADAR_D3, success.selectedChartType)
        assertEquals("Selected topic should be nutrition_digestion", "nutrition_digestion", success.selectedTopic?.topicId)
    }

    @Test
    fun `simulateTopicReview updates topic mastery in real time`() = runTest(testDispatcher) {
        val viewModel = ProgressViewModel(fakeRepository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        testScheduler.advanceUntilIdle()

        viewModel.simulateTopicReview("nutrition_digestion", 0.98f)
        testScheduler.advanceUntilIdle()

        val success = viewModel.uiState.value as ProgressUiState.Success
        val topic = success.allTopics.find { it.topicId == "nutrition_digestion" }
        assertNotNull(topic)
        assertEquals("Mastery percentage should be 98%", 98, topic!!.masteryPercentage)
    }
}
