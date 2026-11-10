/**
 * @module src/services/fsrs.test.ts
 * @description Vitest unit tests for the Free Spaced Repetition Scheduler (FSRS) implementation.
 */

import { describe, it, expect } from 'vitest';
import { FSRS } from './fsrs';
import { FSRSCard } from '../types';

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
    state: 0 /* NEW */,
  };

  it('generates 4 valid scheduling outcomes for repeat evaluation', () => {
    const outcomes = fsrs.repeat(newCard);

    expect(outcomes[1]).toBeDefined(); // Again
    expect(outcomes[2]).toBeDefined(); // Hard
    expect(outcomes[3]).toBeDefined(); // Good
    expect(outcomes[4]).toBeDefined(); // Easy

    expect(outcomes[1].card.reps).toBe(1);
    expect(outcomes[3].card.reps).toBe(1);
  });

  it('moves NEW card to LEARNING state when rated Again (1)', () => {
    const scheduled = fsrs.schedule(newCard, 1);
    expect(scheduled.card.state).toBe(1); // LEARNING
  });

  it('moves NEW card to REVIEW state when rated Good (3)', () => {
    const scheduled = fsrs.schedule(newCard, 3);
    expect(scheduled.card.state).toBe(2); // REVIEW
  });

  it('increments lapses count when a card in REVIEW state is rated Again (1)', () => {
    const reviewCard: FSRSCard = {
      ...newCard,
      state: 2 /* REVIEW */,
      reps: 5,
      lapses: 1,
    };

    const scheduled = fsrs.schedule(reviewCard, 1);
    expect(scheduled.card.lapses).toBe(2);
    expect(scheduled.card.state).toBe(3); // RELEARNING
  });
});
