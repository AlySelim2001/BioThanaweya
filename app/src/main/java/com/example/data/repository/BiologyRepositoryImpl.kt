package com.example.data.repository

import com.example.data.local.dao.ExamAttemptDao
import com.example.data.local.dao.FlashcardDao
import com.example.data.local.dao.ProgressDao
import com.example.data.local.dao.TopicMasteryDao
import com.example.data.local.entity.ExamAttemptEntity
import com.example.data.local.entity.FlashcardEntity
import com.example.data.local.entity.StudentProgressEntity
import com.example.data.local.entity.TopicMasteryEntity
import com.example.data.model.MasterStudyPack
import com.example.domain.fsrs.*
import com.example.domain.repository.BiologyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiologyRepositoryImpl @Inject constructor(
    private val flashcardDao: FlashcardDao,
    private val progressDao: ProgressDao,
    private val examAttemptDao: ExamAttemptDao,
    private val topicMasteryDao: TopicMasteryDao,
    private val fsrs: FSRS = FSRS()
) : BiologyRepository {

    override fun getAllTopics(): List<MasterStudyPack> {
        return BiologyCurriculumRepository.subTopics.map {
            BiologyCurriculumRepository.getMasterStudyPack(it.id)
        }
    }

    override fun getTopicById(topicId: String): MasterStudyPack? {
        return BiologyCurriculumRepository.getMasterStudyPack(topicId)
    }

    override fun searchCurriculum(query: String): List<MasterStudyPack> {
        val topics = getAllTopics()
        if (query.isBlank()) return topics
        val q = query.trim().lowercase()
        return topics.filter { pack ->
            pack.topicTitle.lowercase().contains(q) ||
            pack.chapterTitle.lowercase().contains(q) ||
            pack.deepTechnicalBreakdown.lowercase().contains(q) ||
            pack.eli5Analogy.lowercase().contains(q) ||
            pack.arabicEli5.contains(query.trim()) ||
            pack.keyTerms.any { it.term.lowercase().contains(q) || it.arabicTerm.contains(query.trim()) }
        }
    }

    override fun getDueCards(nowEpoch: Long): Flow<List<FlashcardEntity>> {
        return flashcardDao.getDueCards(nowEpoch)
    }

    override fun getCardsByTopic(topicId: String): Flow<List<FlashcardEntity>> {
        return flashcardDao.getCardsByTopic(topicId)
    }

    override suspend fun recordCardReview(cardId: String, rating: FSRSRating): SchedulingInfo {
        val allCards = flashcardDao.getAllCardsSync()
        val entity = allCards.firstOrNull { it.id == cardId }
            ?: FlashcardEntity(
                id = cardId,
                topicId = "general",
                frontPrompt = "Review Prompt",
                backAnswer = "Answer",
                category = "General"
            )

        val fsrsCard = FSRSCard(
            cardId = entity.id,
            due = Date(entity.dueEpoch),
            stability = entity.stability,
            difficulty = entity.difficulty,
            reps = entity.reps,
            lapses = entity.lapses,
            state = when (entity.state) {
                1 -> FSRSState.LEARNING
                2 -> FSRSState.REVIEW
                3 -> FSRSState.RELEARNING
                else -> FSRSState.NEW
            },
            lastReview = entity.lastReviewEpoch?.let { Date(it) }
        )

        val schedulingInfo = fsrs.schedule(fsrsCard, rating, Date())
        val updatedCard = schedulingInfo.card

        val updatedEntity = entity.copy(
            stability = updatedCard.stability,
            difficulty = updatedCard.difficulty,
            reps = updatedCard.reps,
            lapses = updatedCard.lapses,
            state = updatedCard.state.value,
            dueEpoch = updatedCard.due.time,
            lastReviewEpoch = updatedCard.lastReview?.time ?: System.currentTimeMillis()
        )

        flashcardDao.updateCard(updatedEntity)
        addXpAndStreak(15)
        return schedulingInfo
    }

    override suspend fun seedDefaultCardsIfEmpty() {
        val existing = flashcardDao.getAllCardsSync()
        if (existing.isEmpty()) {
            val defaultCards = getAllTopics().flatMap { pack ->
                pack.flashcards.map { fc ->
                    FlashcardEntity(
                        id = fc.id,
                        topicId = pack.topicId,
                        frontPrompt = fc.frontPrompt,
                        backAnswer = fc.backAnswer,
                        category = fc.category,
                        stability = 2.0,
                        difficulty = 4.5,
                        reps = 0,
                        lapses = 0,
                        state = 0,
                        dueEpoch = System.currentTimeMillis()
                    )
                }
            }
            if (defaultCards.isNotEmpty()) {
                flashcardDao.insertCards(defaultCards)
            }
        }
    }

    override fun getStudentProgress(): Flow<StudentProgressEntity?> {
        return progressDao.getProgress()
    }

    override suspend fun addXpAndStreak(xpToAdd: Int) {
        val current = progressDao.getProgress().firstOrNull() ?: StudentProgressEntity()
        val now = System.currentTimeMillis()
        val oneDayMs = 24L * 60 * 60 * 1000
        val daysSinceLastActive = (now - current.lastActiveEpoch) / oneDayMs
        val newStreak = when {
            daysSinceLastActive <= 1 -> current.streakDays
            daysSinceLastActive == 2L -> current.streakDays + 1
            else -> 1
        }
        val updated = current.copy(
            streakDays = newStreak,
            totalXp = current.totalXp + xpToAdd,
            questionsGraded = current.questionsGraded + 1,
            lastActiveEpoch = now
        )
        progressDao.saveProgress(updated)
    }

    override fun getExamAttempts(): Flow<List<ExamAttemptEntity>> {
        return examAttemptDao.getAllAttempts()
    }

    override fun getLatestExamAttempt(): Flow<ExamAttemptEntity?> {
        return examAttemptDao.getLatestAttempt()
    }

    override suspend fun recordExamAttempt(attempt: ExamAttemptEntity): Long {
        val id = examAttemptDao.insertAttempt(attempt)
        val xpEarned = (attempt.totalMarks * 25).toInt()
        addXpAndStreak(xpEarned)
        return id
    }

    override fun getTopicMasteries(): Flow<List<TopicMasteryEntity>> {
        return topicMasteryDao.getAllMasteries()
    }

    override suspend fun updateTopicMastery(topicId: String, scoreEarned: Float) {
        val existing = topicMasteryDao.getMasteryForTopic(topicId).firstOrNull()
        val topic = getTopicById(topicId)
        val unitNum = if (topic?.unit?.name?.contains("2") == true) 2 else 1
        val newMastery = if (existing != null) {
            val updatedLevel = (existing.masteryLevel * 0.7f + scoreEarned * 0.3f).coerceIn(0.0f, 1.0f)
            existing.copy(
                masteryLevel = updatedLevel,
                cardsReviewed = existing.cardsReviewed + 1,
                lastUpdatedEpoch = System.currentTimeMillis()
            )
        } else {
            TopicMasteryEntity(
                topicId = topicId,
                topicTitle = topic?.topicTitle ?: topicId,
                unitNumber = unitNum,
                masteryLevel = scoreEarned.coerceIn(0.0f, 1.0f),
                cardsReviewed = 1,
                averageStability = 3.0,
                lastUpdatedEpoch = System.currentTimeMillis()
            )
        }
        topicMasteryDao.upsertMastery(newMastery)
    }

    override suspend fun initializeTopicMasteriesIfEmpty() {
        val existing = topicMasteryDao.getAllMasteries().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            val defaults = getAllTopics().mapIndexed { index, pack ->
                val unitNum = if (pack.unit.name.contains("2")) 2 else 1
                val baseline = when (index % 4) {
                    0 -> 0.88f // Mastered (Green)
                    1 -> 0.72f // Good (Emerald)
                    2 -> 0.55f // Moderate (Orange)
                    else -> 0.35f // Needs review (Coral)
                }
                TopicMasteryEntity(
                    topicId = pack.topicId,
                    topicTitle = pack.topicTitle,
                    unitNumber = unitNum,
                    masteryLevel = baseline,
                    cardsReviewed = 4 + index * 2,
                    averageStability = 4.2,
                    lastUpdatedEpoch = System.currentTimeMillis()
                )
            }
            topicMasteryDao.upsertMasteries(defaults)
        }
    }
}
