package com.example.domain.repository

import com.example.data.local.entity.ExamAttemptEntity
import com.example.data.local.entity.FlashcardEntity
import com.example.data.local.entity.StudentProgressEntity
import com.example.data.local.entity.TopicMasteryEntity
import com.example.domain.fsrs.FSRSRating
import com.example.domain.fsrs.SchedulingInfo
import com.example.data.model.MasterStudyPack
import kotlinx.coroutines.flow.Flow

/**
 * BiologyRepository:
 * Core contract bridging Room Database persistence, FSRS scheduling,
 * and the Ministry Curriculum ground truth for Units 1 & 2.
 */
interface BiologyRepository {
    // Curriculum & Study Packs (Ministry Ground Truth)
    fun getAllTopics(): List<MasterStudyPack>
    fun getTopicById(topicId: String): MasterStudyPack?
    fun searchCurriculum(query: String): List<MasterStudyPack>

    // FSRS Spaced Repetition Flashcards
    fun getDueCards(nowEpoch: Long = System.currentTimeMillis()): Flow<List<FlashcardEntity>>
    fun getCardsByTopic(topicId: String): Flow<List<FlashcardEntity>>
    suspend fun recordCardReview(cardId: String, rating: FSRSRating): SchedulingInfo
    suspend fun seedDefaultCardsIfEmpty()

    // Student Progress & Gamification Telemetry
    fun getStudentProgress(): Flow<StudentProgressEntity?>
    suspend fun addXpAndStreak(xpToAdd: Int)

    // Simulated Ministry Exam History
    fun getExamAttempts(): Flow<List<ExamAttemptEntity>>
    fun getLatestExamAttempt(): Flow<ExamAttemptEntity?>
    suspend fun recordExamAttempt(attempt: ExamAttemptEntity): Long

    // Analytics Heatmap (Mastery per topic)
    fun getTopicMasteries(): Flow<List<TopicMasteryEntity>>
    suspend fun updateTopicMastery(topicId: String, scoreEarned: Float)
    suspend fun initializeTopicMasteriesIfEmpty()
}
