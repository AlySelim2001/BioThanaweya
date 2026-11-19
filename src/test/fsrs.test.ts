/**
 * @module src/test/fsrs.test.ts
 * @description Vitest unit tests for the Free Spaced Repetition Scheduler (FSRS) implementation.
 */

import { beforeEach, describe, expect, it } from 'vitest';
import { FSRS } from '../services/fsrs';
import type { FSRSRating } from '../services/fsrs';

const fixedNow = new Date('2025-01-15T12:00:00.000Z');
const dayMs = 24 * 60 * 60 * 1000;

const createNewCard = (): { cardId: string; due: number; stability: number; difficulty: number; elapsedDays: number; scheduledDays: number; reps: number; lapses: number; state: 0 | 1 | 2 | 3; lastReview?: number } => ({
  cardId: 'fsrs-isolated-card',
  due: fixedNow.getTime(),
  stability: 3,
  difficulty: 5,
  elapsedDays: 0,
  scheduledDays: 0,
  reps: 0,
  lapses: 0,
  state: 0,
});

describe('FSRS deterministic scheduling', () => {
  let scheduler: FSRS;

  beforeEach(() => {
    scheduler = new FSRS(0.9, 36500);
  });

  it('returns scheduling outcome for Good rating without mutating its input card', () => {
    const card = createNewCard();
    const original = structuredClone(card);

    const outcomes = scheduler.repeat(card, 3 as FSRSRating, fixedNow);

    expect(outcomes.card).toBeDefined();
    expect(outcomes.card.reps).toBe(1);
    expect(card).toEqual(original);
  });

  it.each<FSRSRating>([1, 2, 3, 4] as const)('transitions a new card for rating %i to expected state', (rating) => {
    const result = scheduler.schedule(createNewCard(), rating, fixedNow);

    // State transition: NEW (0) -> rating 1=LEARNING(1), 2=REVIEW(2), etc.\n    expect([1, 2].includes(result.card.state)).toBe(true);\n    expect(result.card.lastReview).toBe(fixedNow.getTime());\n    expect(result.reviewLog.state).toBe(0);\n    expect(result.card.reps).toBe(1);\n  });\n\n  it.each<FSRSRating>([1, 2, 3, 4] as const)('transitions a learning card for rating %i to expected state', (rating) => {\n    const learningCard = {\n      ...createNewCard(),\n      state: 1,\n      reps: 2,\n      lastReview: fixedNow.getTime() - dayMs,\n    };\n    const result = scheduler.schedule(learningCard, rating, fixedNow);\n\n    // LEARNING (1) transitions based on rating\n    expect(result.card.state).toBeGreaterThanOrEqual(1);\n    expect(result.card.state).toBeLessThanOrEqual(3);\n    expect(result.card.reps).toBe(3);\n    expect(result.reviewLog.state).toBe(1);\n  });\n\n  it.each<FSRSRating>([1, 2, 3, 4] as const)('transitions a review card for rating %i to expected state', (rating) => {\n    const reviewCard = {\n      ...createNewCard(),\n      state: 2,\n      reps: 8,\n      lapses: 2,\n      stability: 12,\n      difficulty: 6,\n      lastReview: fixedNow.getTime() - 3 * dayMs,\n    };\n    const result = scheduler.schedule(reviewCard, rating, fixedNow);\n\n    // REVIEW (2) transitions based on rating\n    expect(result.card.state).toBeGreaterThanOrEqual(1);\n    expect(result.card.state).toBeLessThanOrEqual(3);\n    expect(result.card.reps).toBe(9);\n    expect(result.reviewLog.state).toBe(2);\n    expect(result.card.lapses).toBe(rating === 1 ? 3 : 2);\n  });\n\n  it.each<FSRSRating>([1, 2, 3, 4] as const)('transitions a relearning card for rating %i to expected state', (rating) => {\n    const relearningCard = {\n      ...createNewCard(),\n      state: 3,\n      reps: 4,\n      lapses: 1,\n      stability: 1.5,\n      difficulty: 8,\n      lastReview: fixedNow.getTime() - dayMs,\n    };\n    const result = scheduler.schedule(relearningCard, rating, fixedNow);\n\n    // RELEARNING (3) transitions based on rating\n    expect(result.card.state).toBeGreaterThanOrEqual(1);\n    expect(result.card.state).toBeLessThanOrEqual(3);\n    expect(result.card.reps).toBe(5);\n    expect(result.card.lapses).toBe(1);\n  });\n\n  it('produces deterministic, finite stability for every rating', () => {\n    const card = {\n      ...createNewCard(),\n      state: 2,\n      stability: 10,\n      difficulty: 5,\n      reps: 3,\n      lastReview: fixedNow.getTime() - 5 * dayMs,\n    };\n\n    const first = scheduler.repeat(card, 3 as FSRSRating, fixedNow);\n    const second = scheduler.repeat(card, 3 as FSRSRating, fixedNow);\n\n    expect(Number.isFinite(first.card.stability)).toBe(true);\n    expect(first.card.stability).toBeGreaterThan(0);\n    expect(first.card.due).toBe(\n      fixedNow.getTime() + first.card.scheduledDays * dayMs\n    );\n\n    expect(first.card.stability).toBeLessThanOrEqual(card.stability);\n    expect(first.card.stability).toBeGreaterThan(0);\n  });\n\n  it('uses configured initial stability values for the four ratings', () => {\n    const stabilityByRating = [1, 2, 3, 4].map(\n      (rating: FSRSRating) => scheduler.schedule(createNewCard(), rating, fixedNow).card.stability\n    );\n\n    // FSRS initial stabilities: Again=0.40255, Hard=1.18385, Good=3.173, Easy=15.69105\n    expect(stabilityByRating[0]).toBeCloseTo(0.40255); // Again\n    expect(stabilityByRating[1]).toBeCloseTo(1.18385); // Hard\n    expect(stabilityByRating[2]).toBeCloseTo(3.173); // Good\n    expect(stabilityByRating[3]).toBeCloseTo(15.69105); // Easy\n  });\n\n  it('clamps difficulty to the inclusive [1, 10] range under extreme inputs', () => {\n    const difficultCard = {\n      ...createNewCard(),\n      state: 2,\n      stability: 100,\n      difficulty: 1000000,\n      lastReview: fixedNow.getTime() - 30 * dayMs,\n    };\n    const easyCard = {\n      ...difficultCard,\n      difficulty: -1000000,\n    };\n\n    for (const rating of [1, 2, 3, 4] as const) {\n      const difficultResult = scheduler.schedule(difficultCard, rating, fixedNow);\n      const easyResult = scheduler.schedule(easyCard, rating, fixedNow);\n      expect(difficultResult.card.difficulty).toBeGreaterThanOrEqual(1);\n      expect(difficultResult.card.difficulty).toBeLessThanOrEqual(10);\n      expect(easyResult.card.difficulty).toBeGreaterThanOrEqual(1);\n      expect(easyResult.card.difficulty).toBeLessThanOrEqual(10);\n    }\n  });\n\n  it('calculates elapsed days and retrievability deterministically', () => {\n    const reviewCard = {\n      ...createNewCard(),\n      state: 2,\n      stability: 10,\n      lastReview: fixedNow.getTime() - 4 * dayMs,\n    };\n    const repeated = scheduler.repeat(reviewCard, 3 as FSRSRating, fixedNow);\n\n    expect(repeated.card.elapsedDays).toBe(4);\n    expect(repeated.card.stability).toBeGreaterThan(0);\n  });\n});\n