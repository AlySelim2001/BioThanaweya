/**
 * @module src/services/fsrs.test.ts
 * @description Vitest unit tests for the Free Spaced Repetition Scheduler (FSRS) implementation.
 */

import { describe, it, expect } from 'vitest';
import { FSRS, type FSRSCard } from './fsrs';

describe('FSRS Engine', () => {
  const fsrs = new FSRS();

  const newCard: FSRSCard = {
    cardId: 'card_test_sarcomere_1',
    due: Date.now(),
    stability: 3.0,
    difficulty: 4.5,
    elapsedDays: 0,
    scheduledDays: 1,
    reps: 0,
    lapses: 0,
    state: 0,
  };

  it('generates valid scheduling outcome for Good rating', () => {
    const outcomes = fsrs.repeat(newCard, 3);

    expect(outcomes.card).toBeDefined();
    expect(outcomes.card.reps).toBe(1);
    expect(outcomes.reviewLog).toBeDefined();
  });

  it('moves NEW card to LEARNING state when rated Again (1)', () => {
    const scheduled = fsrs.schedule(newCard, 1);
    expect(scheduled.card.state).toBe(1);
  });

  it('moves NEW card to REVIEW state when rated Good (3)', () => {
    const scheduled = fsrs.schedule(newCard, 3);
    expect(scheduled.card.state).toBe(2);
  });

  it('increments lapses count when a card in REVIEW state is rated Again (1)', () => {
    const reviewCard: FSRSCard = {
      ...newCard,
      state: 2,
      reps: 5,
      lapses: 1,
    };

    const scheduled = fsrs.schedule(reviewCard, 1);
    expect(scheduled.card.lapses).toBe(2);
    expect(scheduled.card.state).toBe(3);
  });
});
