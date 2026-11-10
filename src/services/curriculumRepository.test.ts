import { describe, it, expect } from 'vitest';
import {
  STUDY_PACKS,
  SUB_TOPICS,
  getMasterStudyPack,
  getAllExamQuestions,
  getSimulatedMinistryExamPool,
  getDefaultTopicMasteries,
} from './curriculumRepository';

describe('Curriculum Repository & MasterStudyPack Integrity', () => {
  const packs = Object.values(STUDY_PACKS);
  const subTopicIds = new Set(SUB_TOPICS.map((s) => s.id));

  it('ensures every study pack topicId exists in SUB_TOPICS and matches its object key', () => {
    expect(packs.length).toBeGreaterThan(0);

    for (const [key, pack] of Object.entries(STUDY_PACKS)) {
      expect(key).toBe(pack.topicId);
      expect(subTopicIds.has(pack.topicId)).toBe(true);
      expect(pack.topicTitle.trim().length).toBeGreaterThan(0);
      expect(pack.chapterTitle.trim().length).toBeGreaterThan(0);
      expect(pack.textbookPageReference.trim().length).toBeGreaterThan(0);
      expect(pack.deepTechnicalBreakdown.trim().length).toBeGreaterThan(50);
    }
  });

  it('guarantees unique question IDs across all study packs', () => {
    const seenQuestionIds = new Set<string>();
    const duplicateQuestionIds: string[] = [];

    for (const pack of packs) {
      for (const question of pack.questions) {
        expect(question.id.trim().length).toBeGreaterThan(0);
        if (seenQuestionIds.has(question.id)) {
          duplicateQuestionIds.push(question.id);
        }
        seenQuestionIds.add(question.id);
      }
    }

    expect(duplicateQuestionIds).toEqual([]);
  });

  it('guarantees unique flashcard IDs across all study packs', () => {
    const seenCardIds = new Set<string>();
    const duplicateCardIds: string[] = [];

    for (const pack of packs) {
      for (const card of pack.flashcards) {
        expect(card.id.trim().length).toBeGreaterThan(0);
        if (seenCardIds.has(card.id)) {
          duplicateCardIds.push(card.id);
        }
        seenCardIds.add(card.id);
      }
    }

    expect(duplicateCardIds).toEqual([]);
  });

  it('validates that MCQ questions have correctOptionIndex strictly within bounds', () => {
    for (const pack of packs) {
      for (const question of pack.questions) {
        if (question.type === 'MCQ') {
          expect(question.options).toBeDefined();
          expect(Array.isArray(question.options)).toBe(true);
          expect(question.options!.length).toBeGreaterThanOrEqual(2);

          expect(typeof question.correctOptionIndex).toBe('number');
          expect(question.correctOptionIndex).toBeGreaterThanOrEqual(0);
          expect(question.correctOptionIndex).toBeLessThan(question.options!.length);

          // Verify officialAnswer aligns with selected option (either exact match or contains option text)
          const selectedOption = question.options![question.correctOptionIndex!];
          const matches =
            question.officialAnswer === selectedOption ||
            question.officialAnswer.toLowerCase().includes(selectedOption.toLowerCase()) ||
            selectedOption.toLowerCase().includes(question.officialAnswer.toLowerCase());
          expect(matches).toBe(true);
        }
      }
    }
  });

  it('validates that all flashcards have non-empty frontPrompt and backAnswer', () => {
    for (const pack of packs) {
      expect(pack.flashcards.length).toBeGreaterThan(0);
      for (const card of pack.flashcards) {
        expect(card.frontPrompt).toBeDefined();
        expect(card.frontPrompt.trim().length).toBeGreaterThan(0);

        expect(card.backAnswer).toBeDefined();
        expect(card.backAnswer.trim().length).toBeGreaterThan(0);

        expect(card.topicId).toBe(pack.topicId);
        expect(card.category.trim().length).toBeGreaterThan(0);
      }
    }
  });

  it('validates key terms, formulas, and comparison tables for each study pack', () => {
    for (const pack of packs) {
      expect(pack.keyTerms.length).toBeGreaterThanOrEqual(3);
      for (const term of pack.keyTerms) {
        expect(term.term.trim().length).toBeGreaterThan(0);
        expect(term.definition.trim().length).toBeGreaterThan(0);
        if (term.arabicTerm) {
          expect(term.arabicTerm.trim().length).toBeGreaterThan(0);
        }
      }

      expect(Array.isArray(pack.formulas)).toBe(true);
      for (const formula of pack.formulas) {
        expect(formula.name.trim().length).toBeGreaterThan(0);
        expect(formula.formulaLatex.trim().length).toBeGreaterThan(0);
        expect(formula.description.trim().length).toBeGreaterThan(0);
      }

      expect(pack.comparisonTables.length).toBeGreaterThanOrEqual(1);
      for (const table of pack.comparisonTables) {
        expect(table.title.trim().length).toBeGreaterThan(0);
        expect(table.columnA.trim().length).toBeGreaterThan(0);
        expect(table.columnB.trim().length).toBeGreaterThan(0);
        expect(table.rows.length).toBeGreaterThanOrEqual(2);
        for (const row of table.rows) {
          expect(row.criteria.trim().length).toBeGreaterThan(0);
          expect(row.valueA.trim().length).toBeGreaterThan(0);
          expect(row.valueB.trim().length).toBeGreaterThan(0);
        }
      }
    }
  });

  it('verifies complete Bloom taxonomy progression in every study pack', () => {
    const requiredLevels = [
      'Remembering',
      'Understanding',
      'Applying',
      'Analyzing',
      'Evaluating',
      'Creating',
    ];

    for (const pack of packs) {
      expect(pack.bloomProgression.length).toBe(6);
      const levelNames = pack.bloomProgression.map((b) => b.levelName);
      expect(levelNames).toEqual(requiredLevels);

      for (const level of pack.bloomProgression) {
        expect(level.objective.trim().length).toBeGreaterThan(0);
        expect(level.masteryPrompt.trim().length).toBeGreaterThan(0);
      }
    }
  });

  it('validates helper functions: getMasterStudyPack, getAllExamQuestions, getSimulatedMinistryExamPool, getDefaultTopicMasteries', () => {
    for (const pack of packs) {
      const fetched = getMasterStudyPack(pack.topicId);
      expect(fetched.topicId).toBe(pack.topicId);
    }

    const allQuestions = getAllExamQuestions();
    expect(allQuestions.length).toBeGreaterThanOrEqual(packs.length * 2);

    const examPool = getSimulatedMinistryExamPool();
    expect(examPool.length).toBeGreaterThan(0);

    const masteries = getDefaultTopicMasteries();
    expect(masteries.length).toBe(SUB_TOPICS.length);
    for (const mastery of masteries) {
      expect(subTopicIds.has(mastery.topicId)).toBe(true);
      expect(mastery.stability).toBeGreaterThan(0);
      expect(mastery.difficulty).toBeGreaterThan(0);
    }
  });
});
