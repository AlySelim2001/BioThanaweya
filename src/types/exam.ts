export type OptionKey = 'A' | 'B' | 'C' | 'D';

export interface QuestionOption {
  id: OptionKey;
  label: string; // e.g. "أ", "ب", "ج", "د"
  textArabic: string;
  textEnglish?: string;
}

export type UnitChapterId =
  | 'support_and_movement'
  | 'hormonal_coordination'
  | 'reproduction'
  | 'immunity'
  | 'molecular_biology_dna_rna';

export interface ExamQuestion {
  id: string;
  paperId: string;
  questionNumber: number;
  chapterId: UnitChapterId;
  chapterTitleArabic: string;
  topicTitleArabic: string;
  marks: 1 | 2;
  textArabic: string;
  diagramSvgKey?: 'sarcomere' | 'antibody' | 'dna_replication' | 'pollen_tube' | 'feedback_loop';
  diagramCaptionArabic?: string;
  options: QuestionOption[];
  correctOptionId: OptionKey;
  trapExplanationArabic: string;
  scientificReferenceArabic: string;
  distractorTraps?: Partial<Record<OptionKey, string>>;
}

export interface OfficialExamPaper {
  id: string;
  titleArabic: string;
  subtitleArabic: string;
  year: number;
  session: 'first_session' | 'second_session' | 'experimental';
  sessionNameArabic: string;
  durationMinutes: number; // usually 180 min (3 hours)
  totalMarks: number; // 60 marks
  questionCount: number; // 46 to 50 questions
  difficultyTier: 'standard_ministry' | 'challenging' | 'experimental';
  descriptionArabic: string;
  questions?: ExamQuestion[];
}

export interface ExamAnswerRecord {
  questionId: string;
  questionNumber: number;
  selectedOption: OptionKey | null;
  isFlaggedForReview: boolean;
  timeSpentSeconds: number;
  isAnswered: boolean;
}

export interface ExamAttempt {
  id: string;
  paperId: string;
  paperTitleArabic: string;
  startedAtTimestamp: number;
  submittedAtTimestamp?: number;
  elapsedSeconds: number;
  totalTimeAllocatedSeconds: number;
  isSubmitted: boolean;
  scoreObtained: number;
  maxScore: number; // 60
  percentage: number;
  passed: boolean;
  answers: Record<string, ExamAnswerRecord>;
  chapterBreakdown: Record<
    UnitChapterId,
    {
      totalMarks: number;
      obtainedMarks: number;
      percentage: number;
      questionCount: number;
      correctCount: number;
    }
  >;
  trapsTriggeredCount: number;
  pacingSummary: {
    averageSecondsPerQuestion: number;
    idealSecondsPerQuestion: number;
    pacingEfficiencyStatus: 'ahead' | 'on_track' | 'lagging';
  };
}

export interface ExamFilterCriteria {
  chapterId?: UnitChapterId | 'all';
  status?: 'all' | 'unanswered' | 'flagged' | 'answered';
  reviewResult?: 'all' | 'correct' | 'incorrect';
}
