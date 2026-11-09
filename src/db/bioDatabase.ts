import Dexie, { type Table } from 'dexie';
import { ExamAttempt, ExamQuestion, OfficialExamPaper } from '../types/exam';
import { INITIAL_EXAM_PAPERS, OFFICIAL_EXAM_QUESTIONS } from './seeds/curriculumExamsData';

export interface UserSavedNote {
  id: string;
  questionId: string;
  noteText: string;
  createdAt: number;
}

export class BioDexieDatabase extends Dexie {
  examPapers!: Table<OfficialExamPaper, string>;
  questions!: Table<ExamQuestion, string>;
  attempts!: Table<ExamAttempt, string>;
  savedNotes!: Table<UserSavedNote, string>;

  constructor() {
    super('BioThanaweyaOfflineDB');

    this.version(1).stores({
      examPapers: 'id, year, session, difficultyTier',
      questions: 'id, paperId, chapterId, questionNumber, marks',
      attempts: 'id, paperId, startedAtTimestamp, isSubmitted, scoreObtained',
      savedNotes: 'id, questionId, createdAt'
    });
  }

  /**
   * Automatically initializes the local IndexedDB database with ministerial curriculum datasets
   * if tables are empty. Guaranteed zero network calls.
   */
  async ensurePrePopulated(): Promise<void> {
    try {
      const papersCount = await this.examPapers.count();
      if (papersCount === 0) {
        await this.examPapers.bulkAdd(INITIAL_EXAM_PAPERS);
      }

      const questionsCount = await this.questions.count();
      if (questionsCount === 0) {
        await this.questions.bulkAdd(OFFICIAL_EXAM_QUESTIONS);
      }
    } catch (error) {
      console.error('Dexie pre-population error:', error);
    }
  }

  /**
   * Resets and re-seeds the question banks if needed
   */
  async reseedDatabase(): Promise<void> {
    await this.examPapers.clear();
    await this.questions.clear();
    await this.examPapers.bulkAdd(INITIAL_EXAM_PAPERS);
    await this.questions.bulkAdd(OFFICIAL_EXAM_QUESTIONS);
  }
}

export const bioDb = new BioDexieDatabase();

// Execute initial seeding in the background
bioDb.ensurePrePopulated().catch((err) => {
  console.warn('Initial offline seed attempt encountered:', err);
});
