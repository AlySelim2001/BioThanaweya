import { beforeEach, describe, expect, it } from 'vitest';
import type { FSRSCard, FSRSRating } from '../types';
import { FSRS } from '../services/fsrs';

const fixedNow = new Date('2025-01-15T12:00:00.000Z');
const dayMs = 24 * 60 * 60 * 1000;

const createNewCard = (): FSRSCard => ({
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

  it('returns all four rating outcomes without mutating its input card', () => {
    const card = createNewCard();
    const original = structuredClone(card);

    const outcomes = scheduler.repeat(card, fixedNow);

    expect(Object.keys(outcomes).sort()).toEqual(['1', '2', '3', '4']);
    expect(outcomes[1].card.reps).toBe(1);
    expect(outcomes[4].card.reps).toBe(1);
    expect(card).toEqual(original);
  });

  it.each([
    [1, 1],
    [2, 2],
    [3, 2],
    [4, 2],
  ] as const)('transitions a new card for rating %i to state %i', (rating, expectedState) => {
    const result = scheduler.schedule(createNewCard(), rating, fixedNow);

    expect(result.card.state).toBe(expectedState);
    expect(result.card.lastReview).toBe(fixedNow.getTime());
    expect(result.reviewLog.state).toBe(0);
  });

  it.each([
    [1, 1],
    [2, 2],
    [3, 2],
    [4, 2],
  ] as const)('transitions a learning card for rating %i to state %i', (rating, expectedState) => {
    const learningCard: FSRSCard = {
      ...createNewCard(),
      state: 1,
      reps: 2,
      lastReview: fixedNow.getTime() - dayMs,
    };
    const result = scheduler.schedule(learningCard, rating, fixedNow);

    expect(result.card.state).toBe(expectedState);
    expect(result.card.reps).toBe(3);
    expect(result.reviewLog.state).toBe(1);
  });

  it.each([
    [1, 3],
    [2, 2],
    [3, 2],
    [4, 2],
  ] as const)('transitions a review card for rating %i to state %i', (rating, expectedState) => {
    const reviewCard: FSRSCard = {
      ...createNewCard(),
      state: 2,
      reps: 8,
      lapses: 2,
      stability: 12,
      difficulty: 6,
      lastReview: fixedNow.getTime() - 3 * dayMs,
    };
    const result = scheduler.schedule(reviewCard, rating, fixedNow);

    expect(result.card.state).toBe(expectedState);
    expect(result.card.reps).toBe(9);
    expect(result.reviewLog.state).toBe(2);
    expect(result.card.lapses).toBe(rating === 1 ? 3 : 2);
  });

  it.each([
    [1, 3],
    [2, 2],
    [3, 2],
    [4, 2],
  ] as const)(
    'transitions a relearning card for rating %i to state %i',
    (rating, expectedState) => {
      const relearningCard: FSRSCard = {
        ...createNewCard(),
        state: 3,
        reps: 4,
        lapses: 1,
        stability: 1.5,
        difficulty: 8,
        lastReview: fixedNow.getTime() - dayMs,
      };
      const result = scheduler.schedule(relearningCard, rating, fixedNow);

      expect(result.card.state).toBe(expectedState);
      expect(result.card.reps).toBe(5);
      expect(result.card.lapses).toBe(1);
    }
  );

  it('produces deterministic, finite stability for every rating', () => {
    const card: FSRSCard = {
      ...createNewCard(),
      state: 2,
      stability: 10,
      difficulty: 5,
      reps: 3,
      lastReview: fixedNow.getTime() - 5 * dayMs,
    };

    const first = scheduler.repeat(card, fixedNow);
    const second = scheduler.repeat(card, fixedNow);

    for (const rating of [1, 2, 3, 4] as const satisfies readonly FSRSRating[]) {
      expect(first[rating]).toEqual(second[rating]);
      expect(Number.isFinite(first[rating].card.stability)).toBe(true);
      expect(first[rating].card.stability).toBeGreaterThan(0);
      expect(first[rating].card.due).toBe(
        fixedNow.getTime() + first[rating].card.scheduledDays * dayMs
      );
    }

    expect(first[1].card.stability).toBeLessThanOrEqual(card.stability);
    expect(first[4].card.stability).toBeGreaterThan(first[3].card.stability);
  });

  it('uses configured initial stability values for the four ratings', () => {
    const stabilityByRating = ([1, 2, 3, 4] as const).map(
      (rating) => scheduler.schedule(createNewCard(), rating, fixedNow).card.stability
    );

    expect(stabilityByRating).toEqual([0.40255, 1.18385, 3.173, 15.69105]);
  });

  it('clamps difficulty to the inclusive [1, 10] range under extreme inputs', () => {
    const difficultCard: FSRSCard = {
      ...createNewCard(),
      state: 2,
      stability: 100,
      difficulty: 1000000,
      lastReview: fixedNow.getTime() - 30 * dayMs,
    };
    const easyCard: FSRSCard = {
      ...difficultCard,
      difficulty: -1000000,
    };

    for (const rating of [1, 2, 3, 4] as const) {
      const difficultResult = scheduler.schedule(difficultCard, rating, fixedNow);
      const easyResult = scheduler.schedule(easyCard, rating, fixedNow);
      expect(difficultResult.card.difficulty).toBeGreaterThanOrEqual(1);
      expect(difficultResult.card.difficulty).toBeLessThanOrEqual(10);
      expect(easyResult.card.difficulty).toBeGreaterThanOrEqual(1);
      expect(easyResult.card.difficulty).toBeLessThanOrEqual(10);
    }
  });

  it('calculates elapsed days and retrievability deterministically', () => {
    const reviewCard: FSRSCard = {
      ...createNewCard(),
      state: 2,
      stability: 10,
      lastReview: fixedNow.getTime() - 4 * dayMs,
    };
    const repeated = scheduler.repeat(reviewCard, fixedNow);

    expect(repeated[3].card.elapsedDays).toBe(4);
    expect(scheduler.currentRetrievability(reviewCard, fixedNow)).toBeCloseTo(
      Math.pow(1 + (19 * 4) / (9 * 10), -0.5),
      14
    );
  });
});
