import { describe, it, expect } from 'vitest';
import { MinistryExamScoringEngine } from '../MinistryExamScoringEngine';
import { ExamAnswerRecord, ExamQuestion } from '../../types/exam';

describe('MinistryExamScoringEngine', () => {
  const sampleQuestions: ExamQuestion[] = [
    {
      id: 'q1',
      paperId: 'p1',
      questionNumber: 1,
      chapterId: 'support_and_movement',
      chapterTitleArabic: 'الدعامة والحركة',
      topicTitleArabic: 'الانقباض العضلي',
      marks: 1,
      textArabic: 'سؤال الدعامة 1',
      options: [
        { id: 'A', label: 'أ', textArabic: 'خيار أ' },
        { id: 'B', label: 'ب', textArabic: 'خيار ب' },
        { id: 'C', label: 'ج', textArabic: 'خيار ج' },
        { id: 'D', label: 'د', textArabic: 'خيار د' }
      ],
      correctOptionId: 'A',
      trapExplanationArabic: 'فخ السؤال',
      scientificReferenceArabic: 'مرجع الوزارة'
    },
    {
      id: 'q2',
      paperId: 'p1',
      questionNumber: 2,
      chapterId: 'molecular_biology_dna_rna',
      chapterTitleArabic: 'البيولوجيا الجزيئية',
      topicTitleArabic: 'تضاعف DNA',
      marks: 2,
      textArabic: 'سؤال الوراثة 2',
      options: [
        { id: 'A', label: 'أ', textArabic: 'خيار أ' },
        { id: 'B', label: 'ب', textArabic: 'خيار ب' },
        { id: 'C', label: 'ج', textArabic: 'خيار ج' },
        { id: 'D', label: 'د', textArabic: 'خيار د' }
      ],
      correctOptionId: 'B',
      trapExplanationArabic: 'فخ السؤال 2',
      scientificReferenceArabic: 'مرجع الوزارة'
    }
  ];

  it('calculates perfect score correctly with weighted marks (1 + 2 = 3)', () => {
    const answers: Record<string, ExamAnswerRecord> = {
      q1: {
        questionId: 'q1',
        questionNumber: 1,
        selectedOption: 'A',
        isFlaggedForReview: false,
        timeSpentSeconds: 60,
        isAnswered: true
      },
      q2: {
        questionId: 'q2',
        questionNumber: 2,
        selectedOption: 'B',
        isFlaggedForReview: false,
        timeSpentSeconds: 80,
        isAnswered: true
      }
    };

    const result = MinistryExamScoringEngine.evaluateExam(sampleQuestions, answers, 140);
    expect(result.scoreObtained).toBe(3);
    expect(result.maxScore).toBe(3);
    expect(result.percentage).toBe(100);
    expect(result.isPassed).toBe(true);
    expect(result.correctCount).toBe(2);
    expect(result.incorrectCount).toBe(0);
    expect(result.trapsTriggeredCount).toBe(0);
    expect(result.gradeTierArabic).toContain('ممتاز');
  });

  it('handles incorrect answers and detects traps triggered', () => {
    const answers: Record<string, ExamAnswerRecord> = {
      q1: {
        questionId: 'q1',
        questionNumber: 1,
        selectedOption: 'C', // incorrect (trap option)
        isFlaggedForReview: false,
        timeSpentSeconds: 40,
        isAnswered: true
      },
      q2: {
        questionId: 'q2',
        questionNumber: 2,
        selectedOption: 'B', // correct (2 marks)
        isFlaggedForReview: false,
        timeSpentSeconds: 70,
        isAnswered: true
      }
    };

    const result = MinistryExamScoringEngine.evaluateExam(sampleQuestions, answers, 110);
    expect(result.scoreObtained).toBe(2);
    expect(result.maxScore).toBe(3);
    expect(result.correctCount).toBe(1);
    expect(result.incorrectCount).toBe(1);
    expect(result.trapsTriggeredCount).toBe(1);
  });

  it('formats time in hh:mm:ss and mm:ss accurately', () => {
    expect(MinistryExamScoringEngine.formatTime(125)).toBe('02:05');
    expect(MinistryExamScoringEngine.formatTime(3665)).toBe('01:01:05');
  });
});
