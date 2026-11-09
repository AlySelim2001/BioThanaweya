package com.example.domain.repository

import com.example.data.local.entity.ExamAttemptEntity
import com.example.data.local.entity.FlashcardEntity
import com.example.data.local.entity.KeyConceptEntity
import com.example.data.local.entity.SavedBiologyDefinitionEntity
import com.example.data.local.entity.StudentProgressEntity
import com.example.data.local.entity.TopicMasteryEntity
import com.example.domain.fsrs.FSRSRating
import com.example.domain.fsrs.SchedulingInfo
import com.example.data.model.MasterStudyPack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

/**
 * BiologyRepository:
 * Core contract bridging Room Database persistence, FSRS scheduling,
 * offline key concepts, saved definitions, and the Ministry Curriculum.
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
    suspend fun recordGradedQuestion(xpBonus: Int = 10) {}
    suspend fun recordExamCompletionProgress(durationMinutes: Int, newMastery: Float, newPercentile: Float) {}

    // Simulated Ministry Exam History
    fun getExamAttempts(): Flow<List<ExamAttemptEntity>>
    fun getLatestExamAttempt(): Flow<ExamAttemptEntity?>
    suspend fun recordExamAttempt(attempt: ExamAttemptEntity): Long

    // Mistake Notebook & Targeted Re-test (دفتر الأخطاء والتريكات)
    fun getAllMistakes(): Flow<List<com.example.data.local.entity.ExamMistakeEntity>> = emptyFlow()
    fun getUnresolvedMistakes(): Flow<List<com.example.data.local.entity.ExamMistakeEntity>> = emptyFlow()
    fun getUnresolvedMistakesCount(): Flow<Int> = flowOf(0)
    suspend fun recordMistakes(mistakes: List<com.example.data.local.entity.ExamMistakeEntity>) {}
    suspend fun resolveMistake(mistakeId: Long) {}
    suspend fun clearResolvedMistakes() {}
    suspend fun deleteMistake(mistakeId: Long) {}

    // Analytics Heatmap (Mastery per topic)
    fun getTopicMasteries(): Flow<List<TopicMasteryEntity>>
    suspend fun updateTopicMastery(topicId: String, scoreEarned: Float)
    suspend fun initializeTopicMasteriesIfEmpty()

    // Saved Biology Definitions (Local Bookmarks & Annotations)
    fun getSavedDefinitions(): Flow<List<SavedBiologyDefinitionEntity>> = emptyFlow()
    fun getSavedDefinitionsByUnit(unitCategory: String): Flow<List<SavedBiologyDefinitionEntity>> = emptyFlow()
    fun isDefinitionSaved(term: String): Flow<Boolean> = flowOf(false)
    suspend fun saveDefinition(definition: SavedBiologyDefinitionEntity): Long = 0L
    suspend fun toggleSaveDefinition(
        term: String,
        arabicTerm: String,
        definition: String,
        arabicDefinition: String = "",
        chapterTitle: String,
        unitCategory: String = "UNIT_1"
    ): Boolean = false
    suspend fun removeSavedDefinition(id: Long) {}
    suspend fun updateDefinitionNotes(id: Long, notes: String) {}
    suspend fun updateDefinitionMastery(id: Long, masteryLevel: String) {}

    // Offline Key Concepts
    fun getAllKeyConcepts(): Flow<List<KeyConceptEntity>> = emptyFlow()
    fun getKeyConceptsByTopic(topicId: String): Flow<List<KeyConceptEntity>> = emptyFlow()
    fun getKeyConceptsByUnit(unitCategory: String): Flow<List<KeyConceptEntity>> = emptyFlow()
    fun getKeyConceptById(conceptId: String): Flow<KeyConceptEntity?> = flowOf(null)
    suspend fun upsertKeyConcept(concept: KeyConceptEntity) {}
    suspend fun seedDefaultKeyConceptsIfEmpty() {}
}
