import { FSRSCard, FSRSRating, FSRSState, SchedulingInfo, FSRSItemReviewLog } from '../types';

/**
 * Pure TypeScript Implementation of FSRS (Free Spaced Repetition Scheduler - v4.5 / v5 standard)
 * Direct faithful port of FSRS.kt
 */
export class FSRS {
  private readonly requestRetention: number;
  private readonly maximumInterval: number;
  // Standard FSRS default weights
  private readonly w: number[] = [
    0.40255,
    1.18385,
    3.173,
    15.69105, // initial stabilities for Again, Hard, Good, Easy
    7.1949,
    0.5345,
    1.4604,
    0.0046, // difficulty updates
    1.54575,
    0.1192,
    1.01925, // stability updates
    1.9395,
    0.11,
    0.29605,
    2.2698, // retrieval factors
  ];

  constructor(requestRetention = 0.9, maximumInterval = 36500) {
    this.requestRetention = requestRetention;
    this.maximumInterval = maximumInterval;
  }

  public repeat(card: FSRSCard, now: Date = new Date()): Record<FSRSRating, SchedulingInfo> {
    const elapsedDays =
      card.state === 0 /* NEW */
        ? 0
        : Math.max(
            0,
            Math.floor((now.getTime() - (card.lastReview ?? now.getTime())) / (1000 * 60 * 60 * 24))
          );

    const updatedCard: FSRSCard = { ...card, elapsedDays };

    return {
      1: this.schedule(updatedCard, 1, now),
      2: this.schedule(updatedCard, 2, now),
      3: this.schedule(updatedCard, 3, now),
      4: this.schedule(updatedCard, 4, now),
    };
  }

  public schedule(card: FSRSCard, rating: FSRSRating, now: Date = new Date()): SchedulingInfo {
    let nextState: FSRSState = 0;
    if (card.state === 0 /* NEW */) {
      nextState = rating === 1 ? 1 /* LEARNING */ : 2; /* REVIEW */
    } else if (card.state === 1 || card.state === 3 /* LEARNING or RELEARNING */) {
      nextState = rating === 1 ? card.state : 2; /* REVIEW */
    } else if (card.state === 2 /* REVIEW */) {
      nextState = rating === 1 ? 3 /* RELEARNING */ : 2; /* REVIEW */
    }

    const nextDiff = this.nextDifficulty(card, rating);
    const nextStab = this.nextStability(card, rating);
    const nextIntervalDays = this.nextInterval(nextStab);

    const nextDue = now.getTime() + nextIntervalDays * 24 * 60 * 60 * 1000;
    const nextLapses = card.state === 2 && rating === 1 ? card.lapses + 1 : card.lapses;

    const nextCard: FSRSCard = {
      cardId: card.cardId,
      due: nextDue,
      stability: nextStab,
      difficulty: nextDiff,
      elapsedDays: card.elapsedDays,
      scheduledDays: nextIntervalDays,
      reps: card.reps + 1,
      lapses: nextLapses,
      state: nextState,
      lastReview: now.getTime(),
    };

    const reviewLog: FSRSItemReviewLog = {
      rating,
      scheduledDays: nextIntervalDays,
      elapsedDays: card.elapsedDays,
      review: now.getTime(),
      state: card.state,
    };

    return { card: nextCard, reviewLog };
  }

  private nextDifficulty(card: FSRSCard, rating: FSRSRating): number {
    if (card.state === 0 /* NEW */) {
      const d0 = this.w[4] - Math.exp(this.w[5] * (rating - 1)) + 1;
      return Math.min(10.0, Math.max(1.0, d0));
    } else {
      const nextD = card.difficulty - this.w[6] * (rating - 3);
      const meanReversion =
        this.w[7] * (this.w[4] - Math.exp(this.w[5] * 2) + 1) + (1 - this.w[7]) * nextD;
      return Math.min(10.0, Math.max(1.0, meanReversion));
    }
  }

  private nextStability(card: FSRSCard, rating: FSRSRating): number {
    if (card.state === 0 /* NEW */) {
      const s0 = this.w[rating - 1];
      return Math.max(0.1, s0);
    } else {
      const retrievability = this.currentRetrievability(card);
      if (rating === 1 /* AGAIN */) {
        const sMin = Math.min(
          card.stability,
          this.w[11] *
            Math.pow(card.difficulty, -this.w[12]) *
            (Math.pow(card.stability + 1, this.w[13]) - 1) *
            Math.exp(this.w[14] * (1 - retrievability))
        );
        return Math.max(0.1, sMin);
      } else {
        const hardPenalty = rating === 2 ? this.w[15] : 1.0;
        const easyBonus = rating === 4 ? this.w[16] : 1.0;
        const factor =
          Math.exp(this.w[8]) *
          (11 - card.difficulty) *
          Math.pow(card.stability, -this.w[9]) *
          (Math.exp((1 - retrievability) * this.w[10]) - 1) *
          hardPenalty *
          easyBonus;
        return card.stability * (1 + factor);
      }
    }
  }

  private nextInterval(stability: number): number {
    const interval = Math.round(
      (stability / 19.0) * (Math.pow(this.requestRetention, -1.0 / 0.5) - 1.0)
    );
    return Math.min(this.maximumInterval, Math.max(1, interval));
  }

  public currentRetrievability(card: FSRSCard, now: Date = new Date()): number {
    if (card.state === 0 /* NEW */) return 0.0;
    const diffMs = now.getTime() - (card.lastReview ?? now.getTime());
    const days = Math.max(0.0, diffMs / (1000.0 * 60.0 * 60.0 * 24.0));
    if (card.stability <= 0.0) return 0.0;
    return Math.pow(1.0 + (19.0 * days) / (9.0 * card.stability), -0.5);
  }
}

export const defaultFsrs = new FSRS();
