package com.example

import android.content.Context
import com.example.data.local.entity.ExamAttemptEntity
import com.example.data.local.entity.FlashcardEntity
import com.example.data.local.entity.StudentProgressEntity
import com.example.data.local.entity.TopicMasteryEntity
import com.example.data.model.MasterStudyPack
import com.example.di.AppContainer
import com.example.domain.fsrs.FSRSRating
import com.example.domain.fsrs.SchedulingInfo
import com.example.domain.repository.BiologyRepository
import com.example.presentation.viewmodel.DashboardUiState
import com.example.presentation.viewmodel.DashboardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class DashboardUnitMasteryTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var context: Context

    private val masteriesFlow = MutableStateFlow<List<TopicMasteryEntity>>(emptyList())
    private val progressFlow = MutableStateFlow<StudentProgressEntity?>(null)

    private val testRepository = object : BiologyRepository {
        override fun getAllTopics(): List<MasterStudyPack> = emptyList()
        override fun getTopicById(topicId: String): MasterStudyPack? = null
        override fun searchCurriculum(query: String): List<MasterStudyPack> = emptyList()
        override fun getDueCards(nowEpoch: Long): Flow<List<FlashcardEntity>> = MutableStateFlow(emptyList())
        override fun getCardsByTopic(topicId: String): Flow<List<FlashcardEntity>> = MutableStateFlow(emptyList())
        override suspend fun recordCardReview(cardId: String, rating: FSRSRating): SchedulingInfo = error("unused")
        override suspend fun seedDefaultCardsIfEmpty() {}
        override fun getStudentProgress(): Flow<StudentProgressEntity?> = progressFlow
        override suspend fun addXpAndStreak(xpToAdd: Int) {}
        override fun getExamAttempts(): Flow<List<ExamAttemptEntity>> = MutableStateFlow(emptyList())
        override fun getLatestExamAttempt(): Flow<ExamAttemptEntity?> = MutableStateFlow(null)
        override suspend fun recordExamAttempt(attempt: ExamAttemptEntity): Long = 1L
        override fun getTopicMasteries(): Flow<List<TopicMasteryEntity>> = masteriesFlow
        override suspend fun updateTopicMastery(topicId: String, scoreEarned: Float) {}
        override suspend fun initializeTopicMasteriesIfEmpty() {}
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        context = RuntimeEnvironment.getApplication()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testUnitMasteryCalculationFromRoomEntities() = runTest(testDispatcher) {
        masteriesFlow.value = listOf(
            TopicMasteryEntity("sarcomere_contraction", "Sarcomere", 1, 0.85f, 15, 9.5),
            TopicMasteryEntity("plant_human_support", "Support", 1, 0.75f, 12, 8.0),
            TopicMasteryEntity("dna_structure_replication", "DNA", 2, 0.90f, 20, 14.0)
        )
        progressFlow.value = StudentProgressEntity(id = 1, streakDays = 7, totalXp = 1250, questionsGraded = 35)

        val appContainer = AppContainer(context)
        val viewModel = DashboardViewModel(appContainer.orchestrator, testRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State must be Success", state is DashboardUiState.Success)
        val success = state as DashboardUiState.Success

        // Verify Unit 1 Mastery Ring values
        assertEquals(1, success.unit1Mastery.unitNumber)
        assertTrue("Unit 1 percentage should be around 80%", success.unit1Mastery.percentage in 75..85)
        assertTrue(success.unit1Mastery.progressFraction in 0.75f..0.85f)
        assertEquals(27, success.unit1Mastery.cardsReviewed) // 15 + 12

        // Verify Unit 2 Mastery Ring values
        assertEquals(2, success.unit2Mastery.unitNumber)
        assertTrue("Unit 2 percentage should be around 90%", success.unit2Mastery.percentage in 85..95)
        assertTrue(success.unit2Mastery.progressFraction in 0.85f..0.95f)
        assertEquals(20, success.unit2Mastery.cardsReviewed)

        // Overall progress
        assertTrue(success.overallMasteryPercentage in 80..90)
        assertEquals(47, success.totalCardsReviewed) // 27 + 20
        assertEquals(35, success.questionsGraded)
        assertEquals(7, success.streakDays)
    }

    @Test
    fun testFallbackWhenRoomIsEmpty() = runTest(testDispatcher) {
        masteriesFlow.value = emptyList()
        progressFlow.value = null

        val appContainer = AppContainer(context)
        val viewModel = DashboardViewModel(appContainer.orchestrator, testRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State must be Success", state is DashboardUiState.Success)
        val success = state as DashboardUiState.Success

        assertNotNull(success.unit1Mastery)
        assertNotNull(success.unit2Mastery)
        assertTrue(success.unit1Mastery.percentage in 0..100)
        assertTrue(success.unit2Mastery.percentage in 0..100)
        assertTrue(success.overallMasteryPercentage in 0..100)
    }
}
