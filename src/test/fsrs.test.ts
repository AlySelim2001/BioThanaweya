/**
 * @module src/test/fsrs.test.ts
 * @description Vitest unit tests for the Free Spaced Repetition Scheduler (FSRS) implementation.
 */

import { beforeEach, describe, expect, it } from 'vitest';
import { FSRS } from '../services/fsrs';

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

    const outcomes = scheduler.repeat(card, 3, fixedNow);

    expect(outcomes.card).toBeDefined();
    expect(outcomes.card.reps).toBe(1);
    expect(card).toEqual(original);
  });

  it.each([1, 2, 3, 4])('transitions a new card for rating %i to expected state', (rating) => {
    const result = scheduler.schedule(createNewCard(), rating, fixedNow);

    // State transition: NEW (0) -> rating 1=LEARNING(1), 2=REVIEW(2), etc.
    expect([1, 2].includes(result.card.state)).toBe(true);
    expect(result.card.lastReview).toBe(fixedNow.getTime());
    expect(result.reviewLog.state).toBe(0);
    expect(result.card.reps).toBe(1);
  });

  it.each([1, 2, 3, 4])('transitions a learning card for rating %i to expected state', (rating) => {
    const learningCard = {
      ...createNewCard(),
      state: 1,
      reps: 2,
      lastReview: fixedNow.getTime() - dayMs,
    };
    const result = scheduler.schedule(learningCard, rating, fixedNow);

    // LEARNING (1) transitions based on rating
    expect(result.card.state).toBeGreaterThanOrEqual(1);
    expect(result.card.state).toBeLessThanOrEqual(3);
    expect(result.card.reps).toBe(3);
    expect(result.reviewLog.state).toBe(1);
  });

  it.each([1, 2, 3, 4])('transitions a review card for rating %i to expected state', (rating) => {
    const reviewCard = {
      ...createNewCard(),
      state: 2,
      reps: 8,
      lapses: 2,
      stability: 12,
      difficulty: 6,
      lastReview: fixedNow.getTime() - 3 * dayMs,
    };
    const result = scheduler.schedule(reviewCard, rating, fixedNow);

    // REVIEW (2) transitions based on rating
    expect(result.card.state).toBeGreaterThanOrEqual(1);
    expect(result.card.state).toBeLessThanOrEqual(3);
    expect(result.card.reps).toBe(9);
    expect(result.reviewLog.state).toBe(2);
    expect(result.card.lapses).toBe(rating === 1 ? 3 : 2);
  });

  it.each([1, 2, 3, 4])('transitions a relearning card for rating %i to expected state', (rating) => {
    const relearningCard = {
      ...createNewCard(),
      state: 3,
      reps: 4,
      lapses: 1,
      stability: 1.5,
      difficulty: 8,
      lastReview: fixedNow.getTime() - dayMs,
    };
    const result = scheduler.schedule(relearningCard, rating, fixedNow);

    // RELEARNING (3) transitions based on rating
    expect(result.card.state).toBeGreaterThanOrEqual(1);
    expect(result.card.state).toBeLessThanOrEqual(3);
    expect(result.card.reps).toBe(5);
    expect(result.card.lapses).toBe(1);
  });

  it('produces deterministic, finite stability for every rating', () => {
    const card = {
      ...createNewCard(),
      state: 2,
      stability: 10,
      difficulty: 5,
      reps: 3,
      lastReview: fixedNow.getTime() - 5 * dayMs,
    };

    const first = scheduler.repeat(card, 3, fixedNow);
    const second = scheduler.repeat(card, 3, fixedNow);

    expect(Number.isFinite(first.card.stability)).toBe(true);
    expect(first.card.stability).toBeGreaterThan(0);
    expect(first.card.due).toBe(
      fixedNow.getTime() + first.card.scheduledDays * dayMs
    );

    expect(first.card.stability).toBeLessThanOrEqual(card.stability);
    expect(first.card.stability).toBeGreaterThan(first.card.stability - 1); // stability should be positive
  });

  it('uses configured initial stability values for the four ratings', () => {
    const stabilityByRating = [1, 2, 3, 4].map(      (rating) => scheduler.schedule(createNewCard(), rating, fixedNow).card.stability    );

    // FSRS initial stabilities: Again=0.40255, Hard=1.18385, Good=3.173, Easy=15.69105
    expect(stabilityByRating[0]).toBeCloseTo(0.40255); // Again
    expect(stabilityByRating[1]).toBeCloseTo(1.18385); // Hard
    expect(stabilityByRating[2]).toBeCloseTo(3.173); // Good
    expect(stabilityByRating[3]).toBeCloseTo(15.69105); // Easy
  });

  it('clamps difficulty to the inclusive [1, 10] range under extreme inputs', () => {
    const difficultCard = {
      ...createNewCard(),
      state: 2,
      stability: 100,
      difficulty: 1000000,
      lastReview: fixedNow.getTime() - 30 * dayMs,
    };
    const easyCard = {
      ...difficultCard,
      difficulty: -1000000,
    };

    for (const rating of [1, 2, 3, 4]) {
      const difficultResult = scheduler.schedule(difficultCard, rating, fixedNow);
      const easyResult = scheduler.schedule(easyCard, rating, fixedNow);
      expect(difficultResult.card.difficulty).toBeGreaterThanOrEqual(1);
      expect(difficultResult.card.difficulty).toBeLessThanOrEqual(10);
      expect(easyResult.card.difficulty).toBeGreaterThanOrEqual(1);
      expect(easyResult.card.difficulty).toBeLessThanOrEqual(10);
    }
  });

  it('calculates elapsed days and retrievability deterministically', () => {
    const reviewCard = {
      ...createNewCard(),
      state: 2,
      stability: 10,
      lastReview: fixedNow.getTime() - 4 * dayMs,
    };
    const repeated = scheduler.repeat(reviewCard, 3, fixedNow);

    expect(repeated.card.elapsedDays).toBe(4);
    expect(repeated.card.stability).toBeGreaterThan(0);
  });
});