package com.example.domain.fsrs

import java.util.Date

enum class FSRSRating {
    AGAIN,
    HARD,
    GOOD,
    EASY
}

enum class FSRSState(val value: Int) {
    NEW(0),
    LEARNING(1),
    REVIEW(2),
    RELEARNING(3)
}

data class FSRSCard(
    val cardId: String,
    val due: Date,
    val stability: Double,
    val difficulty: Double,
    val reps: Int,
    val lapses: Int,
    val state: FSRSState,
    val lastReview: Date? = null
)

data class ReviewLog(
    val rating: FSRSRating,
    val elapsedDays: Int = 0,
    val scheduledDays: Int = 1,
    val reviewTime: Date = Date(),
    val state: FSRSState = FSRSState.REVIEW
)

data class SchedulingInfo(
    val card: FSRSCard,
    val reviewLog: ReviewLog? = null
)

class FSRS {
    fun repeat(card: FSRSCard, now: Date): Map<FSRSRating, SchedulingInfo> {
        val nowMillis = now.time
        val dayInMillis = 86400000L

        val results = mutableMapOf<FSRSRating, SchedulingInfo>()

        for (rating in FSRSRating.values()) {
            val (newStability, newDifficulty, intervalDays, newState, lapses) = when (rating) {
                FSRSRating.AGAIN -> {
                    val s = (card.stability * 0.5).coerceAtLeast(0.5)
                    val d = (card.difficulty + 1.0).coerceAtMost(10.0)
                    val days = 1L
                    Tuple5(s, d, days, FSRSState.RELEARNING, card.lapses + 1)
                }
                FSRSRating.HARD -> {
                    val s = (card.stability * 1.2).coerceAtLeast(1.0)
                    val d = (card.difficulty + 0.5).coerceAtMost(10.0)
                    val days = Math.max(1L, Math.round(s))
                    Tuple5(s, d, days, FSRSState.REVIEW, card.lapses)
                }
                FSRSRating.GOOD -> {
                    val s = (card.stability * 2.0).coerceAtLeast(2.0)
                    val d = card.difficulty
                    val days = Math.max(2L, Math.round(s * 1.5))
                    Tuple5(s, d, days, FSRSState.REVIEW, card.lapses)
                }
                FSRSRating.EASY -> {
                    val s = (card.stability * 3.5).coerceAtLeast(3.0)
                    val d = (card.difficulty - 0.5).coerceAtLeast(1.0)
                    val days = Math.max(4L, Math.round(s * 2.5))
                    Tuple5(s, d, days, FSRSState.REVIEW, card.lapses)
                }
            }

            val newCard = card.copy(
                due = Date(nowMillis + intervalDays * dayInMillis),
                stability = newStability,
                difficulty = newDifficulty,
                reps = card.reps + 1,
                lapses = lapses,
                state = newState,
                lastReview = now
            )

            val log = ReviewLog(
                rating = rating,
                scheduledDays = intervalDays.toInt(),
                reviewTime = now,
                state = newState
            )

            results[rating] = SchedulingInfo(card = newCard, reviewLog = log)
        }

        return results
    }

    private data class Tuple5<A, B, C, D, E>(
        val a: A,
        val b: B,
        val c: C,
        val d: D,
        val e: E
    )
}
