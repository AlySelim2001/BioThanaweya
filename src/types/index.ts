export type UnitCategory = 'UNIT_1' | 'UNIT_2';

export interface UnitInfo {
  code: string;
  title: string;
  arabicTitle: string;
}

export const UNIT_DETAILS: Record<UnitCategory, UnitInfo> = {
  UNIT_1: {
    code: 'Unit 1',
    title: 'Structure & Function in Living Organisms',
    arabicTitle: 'التركيب والوظيفة في الكائنات الحية',
  },
  UNIT_2: {
    code: 'Unit 2',
    title: 'Molecular Biology',
    arabicTitle: 'البيولوجيا الجزيئية',
  },
};

export interface Chapter {
  id: string;
  unit: UnitCategory;
  number: number;
  title: string;
  arabicTitle: string;
  summary: string;
  iconName: string;
}

export interface SubTopic {
  id: string;
  chapterId: string;
  unit: UnitCategory;
  title: string;
  arabicTitle: string;
  summary: string;
  highYieldFlag?: boolean;
}

export interface KeyTerm {
  term: string;
  definition: string;
  arabicTerm?: string;
}

export interface FormulaItem {
  name: string;
  formulaLatex: string;
  description: string;
  variables: string;
  sampleCalculation: string;
}

export interface ComparisonRow {
  criteria: string;
  valueA: string;
  valueB: string;
}

export interface ComparisonTable {
  title: string;
  columnA: string;
  columnB: string;
  rows: ComparisonRow[];
}

export interface BloomLevelItem {
  levelName: 'Remembering' | 'Understanding' | 'Applying' | 'Analyzing' | 'Evaluating' | 'Creating';
  levelNumber: number;
  objective: string;
  masteryPrompt: string;
}

export interface FlowchartStep {
  stepNumber: number;
  action: string;
  detail: string;
  triggerOrEnzyme?: string;
}

export interface VideoQuery {
  query: string;
  topicDescription: string;
  durationRecommendation: string;
}

export type QuestionType =
  'SCIENTIFIC_TERM' | 'GIVE_REASON' | 'WHAT_HAPPENS_IF' | 'DIAGRAM_ANALYSIS' | 'MCQ';

export interface PracticeQuestion {
  id: string;
  type: QuestionType;
  question: string;
  options?: string[]; // For MCQ
  correctOptionIndex?: number; // For MCQ
  officialAnswer: string;
  rationale: string;
  ministryExamTip: string;
}

export interface ExamTrap {
  misconception: string;
  scientificTruth: string;
  examTrapWarning: string;
  goldenRule: string;
}

export interface Flashcard {
  id: string;
  topicId: string;
  frontPrompt: string;
  backAnswer: string;
  category: string;
  boxLevel?: number;
  lastReviewedEpoch?: number;
}

export interface MinistryExamItem {
  id: string;
  questionType: QuestionType;
  prompt: string;
  maxScore: number;
  requiredKeywords: string[];
  partialCreditKeywords: string[];
  modelAnswer: string;
  remediationNotes: string;
}

export type GradeStatus = 'FULL_MARK' | 'PARTIAL_CREDIT' | 'NEEDS_REVISION';

export interface ExamGradingResult {
  scoreAwarded: number;
  maxScore: number;
  detectedKeywords: string[];
  missingKeywords: string[];
  examinerFeedback: string;
  modelAnswer: string;
  gradeStatus: GradeStatus;
  remediationAdvice?: string;
}

export interface PblRubricCriterion {
  criterion: string;
  weightPoints: number;
  excellentDescription: string;
  poorDescription: string;
}

export interface PblProject {
  id: string;
  topicId: string;
  title: string;
  titleArabic: string;
  scenario: string;
  mission: string;
  rubrics: PblRubricCriterion[];
  tips: string[];
}

export interface MasterStudyPack {
  topicId: string;
  topicTitle: string;
  unit: UnitCategory;
  chapterTitle: string;
  // Agent 1: Master Biology Educator
  eli5Analogy: string;
  deepTechnicalBreakdown: string;
  keyTerms: KeyTerm[];
  formulas: FormulaItem[];
  comparisonTables: ComparisonTable[];
  bloomProgression: BloomLevelItem[];
  // Agent 2: Visual & Spatial Architect
  asciiSchematic: string;
  schematicCaption: string;
  flowcharts: FlowchartStep[];
  visualInspectionMarkers: string[];
  youtube3DQueries: VideoQuery[];
  // Agent 3: Assessment Specialist
  questions: PracticeQuestion[];
  examTraps: ExamTrap[];
  // Agent 4: Adaptive Learning & Evaluator
  flashcards: Flashcard[];
  examSimulationItem: MinistryExamItem;
  // Ground Truth & Bilingual Support
  arabicEli5: string;
  textbookPageReference: string;
  pblProject?: PblProject;
}

export type MasteryLevel = 'MASTERED' | 'GOOD' | 'LEARNING' | 'NEEDS_REVIEW';

export interface TopicMastery {
  topicId: string;
  title: string;
  chapterTitle: string;
  unit: UnitCategory;
  stability: number;
  difficulty: number;
  reps: number;
  masteryLevel: MasteryLevel;
}

export interface ExamQuestionSubmission {
  questionId: string;
  question: PracticeQuestion;
  studentAnswer: string;
  selectedOptionIndex?: number;
  scoreAwarded: number;
  maxScore: number;
  feedback: string;
  modelAnswer: string;
  detectedKeywords?: string[];
  missingKeywords?: string[];
}

export interface ExamResultReport {
  totalScore: number;
  maxScore: number;
  percentage: number;
  letterGrade: string;
  passed: Boolean;
  timeSpentSeconds: number;
  chapterBreakdown: Record<string, number>;
  questionsReviewed: ExamQuestionSubmission[];
}

export interface StudentBadge {
  id: string;
  title: string;
  titleArabic: string;
  iconName: string;
  description: string;
  unlockedAt?: string;
}

export interface WellbeingProfile {
  streakDays: number;
  totalXp: number;
  earnedBadges: StudentBadge[];
  examAnxietyScore: number; // 0-100
  studyChunkMinutes: number;
  motivationalQuote: string;
}

export type MetacognitiveGapType =
  'CALIBRATED_MASTERY' | 'OVERCONFIDENCE_BIAS' | 'UNDERCONFIDENCE_DOUBT' | 'CALIBRATED_HUMILITY';

export interface MetacognitiveAnalysis {
  confidencePercent: number;
  isCorrect: boolean;
  gapType: MetacognitiveGapType;
  feedbackTitle: string;
  actionableAdvice: string;
}

// FSRS types
export type FSRSRating = 1 | 2 | 3 | 4; // 1: Again, 2: Hard, 3: Good, 4: Easy
export type FSRSState = 0 | 1 | 2 | 3; // 0: New, 1: Learning, 2: Review, 3: Relearning

export interface FSRSCard {
  cardId: string;
  due: number; // epoch ms
  stability: number;
  difficulty: number;
  elapsedDays: number;
  scheduledDays: number;
  reps: number;
  lapses: number;
  state: FSRSState;
  lastReview?: number;
}

export interface FSRSItemReviewLog {
  rating: FSRSRating;
  scheduledDays: number;
  elapsedDays: number;
  review: number;
  state: FSRSState;
}

export interface SchedulingInfo {
  card: FSRSCard;
  reviewLog: FSRSItemReviewLog;
}
