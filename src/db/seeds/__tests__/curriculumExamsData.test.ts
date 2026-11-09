import { describe, it, expect } from 'vitest';
import { INITIAL_EXAM_PAPERS, OFFICIAL_EXAM_QUESTIONS } from '../curriculumExamsData';

describe('Curriculum Exams Seed Data Integrity', () => {
  it('provides valid ministerial exam papers with 60 total marks', () => {
    expect(INITIAL_EXAM_PAPERS.length).toBeGreaterThanOrEqual(3);
    INITIAL_EXAM_PAPERS.forEach((paper) => {
      expect(paper.id).toBeTruthy();
      expect(paper.titleArabic).toBeTruthy();
      expect(paper.totalMarks).toBe(60);
      expect(paper.durationMinutes).toBe(180);
    });
  });

  it('guarantees each question has valid choices, a matching correct option, and a trap explanation', () => {
    expect(OFFICIAL_EXAM_QUESTIONS.length).toBeGreaterThan(0);
    OFFICIAL_EXAM_QUESTIONS.forEach((q) => {
      expect(q.id).toBeTruthy();
      expect(q.textArabic.length).toBeGreaterThan(10);
      expect(q.options.length).toBe(4);
      
      const optionIds = q.options.map((o) => o.id);
      expect(optionIds).toContain(q.correctOptionId);
      expect(q.trapExplanationArabic).toBeTruthy();
      expect(q.scientificReferenceArabic).toBeTruthy();
      expect([1, 2]).toContain(q.marks);
    });
  });
});
