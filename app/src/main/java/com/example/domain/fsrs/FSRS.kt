package com.example.domain.fsrs

import java.util.Date
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Pure Kotlin Implementation of FSRS (Free Spaced Repetition Scheduler - v4.5 / v5 standard)
 * Based on open-spaced-repetition/FSRS specifications (MIT License).
 */

enum class FSRSRating(val value: Int) {
    AGAIN(1),
    HARD(2),
    GOOD(3),
    EASY(4)
}

enum class FSRSState(val value: Int) {
    NEW(0),
    LEARNING(1),
    REVIEW(2),
    RELEARNING(3)
}

data class FSRSCard(
    val cardId: String,
    val due: Date = Date(),
    val stability: Double = 0.0,
    val difficulty: Double = 0.0,
    val elapsedDays: Long = 0,
    val scheduledDays: Long = 0,
    val reps: Int = 0,
    val lapses: Int = 0,
    val state: FSRSState = FSRSState.NEW,
    val lastReview: Date? = null
)

data class FSRSItemReviewLog(
    val rating: FSRSRating,
    val scheduledDays: Long,
    val elapsedDays: Long,
    val review: Date,
    val state: FSRSState
)

data class SchedulingInfo(
    val card: FSRSCard,
    val reviewLog: FSRSItemReviewLog
)

typealias SchedulingRecord = SchedulingInfo

class FSRS(
    private val requestRetention: Double = 0.9,
    private val maximumInterval: Long = 36500L
) {
    // Standard FSRS default weights
    private val w: DoubleArray = doubleArrayOf(
        0.40255, 1.18385, 3.173, 15.69105, // initial stabilities for Again, Hard, Good, Easy
        7.1949, 0.5345, 1.4604, 0.0046,     // difficulty updates
        1.54575, 0.1192, 1.01925,           // stability updates
        1.9395, 0.11, 0.29605, 2.2698       // retrieval factors
    )

    fun repeat(card: FSRSCard, now: Date): Map<FSRSRating, SchedulingInfo> {
        val elapsedDays = if (card.state == FSRSState.NEW) {
            0L
        } else {
            val diffMs = now.time - (card.lastReview?.time ?: now.time)
            max(0L, diffMs / (1000 * 60 * 60 * 24))
        }

        val updatedCard = card.copy(elapsedDays = elapsedDays)
        return mapOf(
            FSRSRating.AGAIN to schedule(updatedCard, FSRSRating.AGAIN, now),
            FSRSRating.HARD to schedule(updatedCard, FSRSRating.HARD, now),
            FSRSRating.GOOD to schedule(updatedCard, FSRSRating.GOOD, now),
            FSRSRating.EASY to schedule(updatedCard, FSRSRating.EASY, now)
        )
    }

    fun schedule(card: FSRSCard, rating: FSRSRating, now: Date): SchedulingInfo {
        val nextState = when (card.state) {
            FSRSState.NEW -> if (rating == FSRSRating.AGAIN) FSRSState.LEARNING else FSRSState.REVIEW
            FSRSState.LEARNING, FSRSState.RELEARNING -> if (rating == FSRSRating.AGAIN) card.state else FSRSState.REVIEW
            FSRSState.REVIEW -> if (rating == FSRSRating.AGAIN) FSRSState.RELEARNING else FSRSState.REVIEW
        }

        val nextDifficulty = nextDifficulty(card, rating)
        val nextStability = nextStability(card, rating)
        val nextInterval = nextInterval(nextStability)

        val nextDue = Date(now.time + nextInterval * 24L * 60L * 60L * 1000L)
        val nextLapses = if (card.state == FSRSState.REVIEW && rating == FSRSRating.AGAIN) card.lapses + 1 else card.lapses

        val nextCard = card.copy(
            due = nextDue,
            stability = nextStability,
            difficulty = nextDifficulty,
            elapsedDays = card.elapsedDays,
            scheduledDays = nextInterval,
            reps = card.reps + 1,
            lapses = nextLapses,
            state = nextState,
            lastReview = now
        )

        val log = FSRSItemReviewLog(
            rating = rating,
            scheduledDays = nextInterval,
            elapsedDays = card.elapsedDays,
            review = now,
            state = card.state
        )

        return SchedulingRecord(nextCard, log)
    }

    private fun nextDifficulty(card: FSRSCard, rating: FSRSRating): Double {
        return if (card.state == FSRSState.NEW) {
            val d0 = w[4] - exp(w[5] * (rating.value - 1)) + 1
            min(10.0, max(1.0, d0))
        } else {
            val nextD = card.difficulty - w[6] * (rating.value - 3)
            val meanReversion = w[7] * (w[4] - exp(w[5] * 2) + 1) + (1 - w[7]) * nextD
            min(10.0, max(1.0, meanReversion))
        }
    }

    private fun nextStability(card: FSRSCard, rating: FSRSRating): Double {
        return if (card.state == FSRSState.NEW) {
            val s0 = w[rating.value - 1]
            max(0.1, s0)
        } else {
            val retrievability = currentRetrievability(card)
            if (rating == FSRSRating.AGAIN) {
                // Short-term lapse drop
                val sMin = min(card.stability, w[11] * card.difficulty.pow(-w[12]) * ((card.stability + 1).pow(w[13]) - 1) * exp(w[14] * (1 - retrievability)))
                max(0.1, sMin)
            } else {
                val hardPenalty = if (rating == FSRSRating.HARD) w[15] else 1.0
                val easyBonus = if (rating == FSRSRating.EASY) w[16] else 1.0
                val factor = exp(w[8]) * (11 - card.difficulty) * card.stability.pow(-w[9]) * (exp((1 - retrievability) * w[10]) - 1) * hardPenalty * easyBonus
                card.stability * (1 + factor)
            }
        }
    }

    private fun nextInterval(stability: Double): Long {
        val interval = (stability / 19.0 * (requestRetention.pow(-1.0 / 0.5) - 1.0)).roundToLong()
        return min(maximumInterval, max(1L, interval))
    }

    fun currentRetrievability(card: FSRSCard, now: Date = Date()): Double {
        if (card.state == FSRSState.NEW) return 0.0
        val diffMs = now.time - (card.lastReview?.time ?: now.time)
        val days = max(0.0, diffMs / (1000.0 * 60.0 * 60.0 * 24.0))
        if (card.stability <= 0.0) return 0.0
        return (1.0 + 19.0 * days / (9.0 * card.stability)).pow(-0.5)
    }
}
