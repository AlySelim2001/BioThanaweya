/**
 * @module src/services/storage.ts
 * @description Storage Service for BioThanaweya.
 * Combines ultra-fast synchronous in-memory caching with asynchronous IndexedDB persistence,
 * automatic legacy localStorage migration, and real-time sync notification dispatch.
 */

import { FSRSCard, StudentBadge, TopicMastery, WellbeingProfile, ExamResultReport } from '../types';
import { getDefaultTopicMasteries, STUDY_PACKS } from './curriculumRepository';
import { dbEngine } from './db/indexedDbEngine';
import { BackupService } from './db/backupService';

// Legacy keys for automatic migration
const LEGACY_FSRS_CARDS_KEY = 'biothanaweya_fsrs_cards_v1';
const LEGACY_PROFILE_KEY = 'biothanaweya_wellbeing_profile_v1';
const LEGACY_MASTERIES_KEY = 'biothanaweya_topic_masteries_v1';
const LEGACY_EXAM_HISTORY_KEY = 'biothanaweya_exam_history_v1';

export type SyncState = 'synced' | 'saving' | 'error';
export type SyncListener = (state: SyncState) => void;

export const INITIAL_BADGES: StudentBadge[] = [
  {
    id: 'b_sarcomere',
    title: 'Sarcomere Architect',
    titleArabic: 'مهندس القطعة العضلية',
    iconName: 'Activity',
    description: 'Mastered Huxley sliding filament calculations and landmark relationships.',
    unlockedAt: new Date().toISOString(),
  },
  {
    id: 'b_chargaff',
    title: 'Chargaff Whiz',
    titleArabic: 'خبير شارجاف والوراثة',
    iconName: 'Dna',
    description: 'Solved 10 DNA double helix base pairing puzzles without error.',
    unlockedAt: new Date().toISOString(),
  },
  {
    id: 'b_ministry',
    title: 'Ministry Gold Scholar',
    titleArabic: 'طبيب المستقبل - الدرجة النهائية',
    iconName: 'Award',
    description: 'Achieved ≥ 90% on a simulated 45-minute Ministry biology examination.',
  },
  {
    id: 'b_streak7',
    title: '7-Day Focus Titan',
    titleArabic: 'بطل الالتزام الأسبوعي',
    iconName: 'Flame',
    description: 'Maintained a 7-day active study streak in Thanaweya Biology.',
  },
];

export const INITIAL_PROFILE: WellbeingProfile = {
  streakDays: 4,
  totalXp: 850,
  earnedBadges: INITIAL_BADGES.slice(0, 2),
  examAnxietyScore: 22,
  studyChunkMinutes: 25,
  motivationalQuote:
    'التميز عادة، وليس فعلًا عابرًا. استمر في التركيز على الآليات والروابط الجزيئية!',
};

// In-Memory Cache
let memoryProfile: WellbeingProfile = INITIAL_PROFILE;
let memoryMasteries: TopicMastery[] = getDefaultTopicMasteries();
let memoryCards: FSRSCard[] = [];
let memoryExams: ExamResultReport[] = [];

// Seed default cards
const seedInitialCards = (): FSRSCard[] => {
  const allFlashcards = Object.values(STUDY_PACKS).flatMap((p) => p.flashcards);
  return allFlashcards.map((fc, idx) => ({
    cardId: fc.id,
    due: Date.now() + idx * 60000,
    stability: 3.0,
    difficulty: 4.5,
    elapsedDays: 0,
    scheduledDays: 1,
    reps: 0,
    lapses: 0,
    state: 0 /* NEW */,
  }));
};

memoryCards = seedInitialCards();

// Sync listener registry
const syncListeners: Set<SyncListener> = new Set();
let currentSyncState: SyncState = 'synced';

const notifySyncState = (state: SyncState) => {
  currentSyncState = state;
  syncListeners.forEach((listener) => {
    try {
      listener(state);
    } catch {
      // Ignore listener errors
    }
  });
};

// Asynchronous persistence helper
const persistAsync = async (task: () => Promise<void>) => {
  notifySyncState('saving');
  try {
    await task();
    notifySyncState('synced');
  } catch (err) {
    console.error('[StorageService] Asynchronous persistence failure:', err);
    notifySyncState('error');
  }
};

export const StorageService = {
  /**
   * Initializes the storage engine, performs legacy localStorage migration if needed,
   * and loads data into memory cache.
   */
  async init(): Promise<void> {
    try {
      if (typeof window === 'undefined') return;

      // 1. Check if IndexedDB has profile
      const storedProfile = await dbEngine.get<{ id: string; profile: WellbeingProfile }>(
        'profile',
        'current'
      );

      if (storedProfile && storedProfile.profile) {
        // Load from IndexedDB
        memoryProfile = storedProfile.profile;
        const [cards, masteries, exams] = await Promise.all([
          dbEngine.getAll<FSRSCard>('cards'),
          dbEngine.getAll<TopicMastery>('masteries'),
          dbEngine.getAll<ExamResultReport>('exams'),
        ]);

        if (cards.length > 0) memoryCards = cards;
        if (masteries.length > 0) memoryMasteries = masteries;
        if (exams.length > 0) memoryExams = exams;
        notifySyncState('synced');
        return;
      }

      // 2. Check for legacy localStorage data
      let legacyProfile: WellbeingProfile | null = null;
      let legacyCards: FSRSCard[] | null = null;
      let legacyMasteries: TopicMastery[] | null = null;
      let legacyExams: ExamResultReport[] | null = null;

      try {
        const rawProf = localStorage.getItem(LEGACY_PROFILE_KEY);
        if (rawProf) legacyProfile = JSON.parse(rawProf);

        const rawCards = localStorage.getItem(LEGACY_FSRS_CARDS_KEY);
        if (rawCards) legacyCards = JSON.parse(rawCards);

        const rawMast = localStorage.getItem(LEGACY_MASTERIES_KEY);
        if (rawMast) legacyMasteries = JSON.parse(rawMast);

        const rawExams = localStorage.getItem(LEGACY_EXAM_HISTORY_KEY);
        if (rawExams) legacyExams = JSON.parse(rawExams);
      } catch {
        // Fallback to defaults
      }

      // Assign migrated or default data
      memoryProfile = legacyProfile || INITIAL_PROFILE;
      memoryCards = legacyCards && legacyCards.length > 0 ? legacyCards : seedInitialCards();
      memoryMasteries =
        legacyMasteries && legacyMasteries.length > 0
          ? legacyMasteries
          : getDefaultTopicMasteries();
      memoryExams = legacyExams || [];

      // 3. Persist to IndexedDB
      await Promise.all([
        dbEngine.put('profile', { id: 'current', profile: memoryProfile }),
        dbEngine.putMany('cards', memoryCards),
        dbEngine.putMany('masteries', memoryMasteries),
        memoryExams.length > 0 ? dbEngine.putMany('exams', memoryExams) : Promise.resolve(),
      ]);

      // Create initial snapshot
      await BackupService.createAutoSnapshot('Initial database initialization & migration');

      notifySyncState('synced');
    } catch (err) {
      console.error('[StorageService] Error during storage initialization:', err);
      notifySyncState('error');
    }
  },

  /**
   * Subscribes to real-time sync state changes ('synced' | 'saving' | 'error').
   */
  onSyncStatusChange(listener: SyncListener): () => void {
    syncListeners.add(listener);
    listener(currentSyncState);
    return () => syncListeners.delete(listener);
  },

  getSyncState(): SyncState {
    return currentSyncState;
  },

  /**
   * Refreshes the in-memory cache directly from IndexedDB (e.g., after an import or restore).
   */
  async reloadFromIndexedDb(): Promise<void> {
    const [profileRec, cards, masteries, exams] = await Promise.all([
      dbEngine.get<{ id: string; profile: WellbeingProfile }>('profile', 'current'),
      dbEngine.getAll<FSRSCard>('cards'),
      dbEngine.getAll<TopicMastery>('masteries'),
      dbEngine.getAll<ExamResultReport>('exams'),
    ]);

    if (profileRec?.profile) memoryProfile = profileRec.profile;
    if (cards.length > 0) memoryCards = cards;
    if (masteries.length > 0) memoryMasteries = masteries;
    memoryExams = exams;

    notifySyncState('synced');
  },

  getProfile(): WellbeingProfile {
    return memoryProfile;
  },

  saveProfile(profile: WellbeingProfile): void {
    memoryProfile = profile;
    try {
      localStorage.setItem(LEGACY_PROFILE_KEY, JSON.stringify(profile));
    } catch {}

    persistAsync(async () => {
      await dbEngine.put('profile', { id: 'current', profile });
    });
  },

  addXp(xp: number): WellbeingProfile {
    const prof = { ...this.getProfile(), totalXp: this.getProfile().totalXp + xp };
    this.saveProfile(prof);
    return prof;
  },

  incrementStreak(): WellbeingProfile {
    const prof = { ...this.getProfile(), streakDays: this.getProfile().streakDays + 1 };
    this.saveProfile(prof);
    return prof;
  },

  getTopicMasteries(): TopicMastery[] {
    return memoryMasteries;
  },

  saveTopicMasteries(masteries: TopicMastery[]): void {
    memoryMasteries = masteries;
    try {
      localStorage.setItem(LEGACY_MASTERIES_KEY, JSON.stringify(masteries));
    } catch {}

    persistAsync(async () => {
      await dbEngine.putMany('masteries', masteries);
    });
  },

  updateTopicStability(topicId: string, stabilityDelta: number): void {
    const masteries = [...this.getTopicMasteries()];
    const target = masteries.find((m) => m.topicId === topicId);
    if (target) {
      target.stability = Math.max(0.5, Math.round((target.stability + stabilityDelta) * 10) / 10);
      target.reps += 1;
      if (target.stability >= 15) target.masteryLevel = 'MASTERED';
      else if (target.stability >= 7) target.masteryLevel = 'GOOD';
      else if (target.stability >= 2.5) target.masteryLevel = 'LEARNING';
      else target.masteryLevel = 'NEEDS_REVIEW';
      this.saveTopicMasteries(masteries);
    }
  },

  getFsrsCards(topicId?: string): FSRSCard[] {
    if (topicId) {
      const topicCardIds = new Set((STUDY_PACKS[topicId]?.flashcards || []).map((f) => f.id));
      return memoryCards.filter((c) => topicCardIds.has(c.cardId));
    }
    return memoryCards;
  },

  saveFsrsCards(cards: FSRSCard[]): void {
    memoryCards = cards;
    try {
      localStorage.setItem(LEGACY_FSRS_CARDS_KEY, JSON.stringify(cards));
    } catch {}

    persistAsync(async () => {
      await dbEngine.putMany('cards', cards);
    });
  },

  updateCard(updatedCard: FSRSCard): void {
    const all = [...this.getFsrsCards()];
    const idx = all.findIndex((c) => c.cardId === updatedCard.cardId);
    if (idx >= 0) {
      all[idx] = updatedCard;
    } else {
      all.push(updatedCard);
    }
    this.saveFsrsCards(all);
  },

  saveExamReport(report: ExamResultReport): void {
    memoryExams.unshift(report);
    if (memoryExams.length > 25) {
      memoryExams = memoryExams.slice(0, 25);
    }
    try {
      localStorage.setItem(LEGACY_EXAM_HISTORY_KEY, JSON.stringify(memoryExams));
    } catch {}

    persistAsync(async () => {
      await dbEngine.put('exams', report);
      await BackupService.createAutoSnapshot(
        `Post-Exam Report: Grade ${report.letterGrade} (${report.percentage}%)`
      );
    });
  },

  getExamReports(): ExamResultReport[] {
    return memoryExams;
  },

  /**
   * Performs a clean factory reset of local IndexedDB and localStorage, reseeding default state.
   */
  async resetAll(): Promise<void> {
    try {
      localStorage.removeItem(LEGACY_PROFILE_KEY);
      localStorage.removeItem(LEGACY_FSRS_CARDS_KEY);
      localStorage.removeItem(LEGACY_MASTERIES_KEY);
      localStorage.removeItem(LEGACY_EXAM_HISTORY_KEY);

      await Promise.all([
        dbEngine.clear('cards'),
        dbEngine.clear('masteries'),
        dbEngine.clear('exams'),
        dbEngine.clear('profile'),
        dbEngine.clear('backups'),
      ]);

      memoryProfile = INITIAL_PROFILE;
      memoryMasteries = getDefaultTopicMasteries();
      memoryCards = seedInitialCards();
      memoryExams = [];

      await Promise.all([
        dbEngine.put('profile', { id: 'current', profile: memoryProfile }),
        dbEngine.putMany('cards', memoryCards),
        dbEngine.putMany('masteries', memoryMasteries),
      ]);

      notifySyncState('synced');
    } catch (err) {
      console.error('[StorageService] Error during database reset:', err);
      notifySyncState('error');
    }
  },
};

// Immediate background initialization on client load
if (typeof window !== 'undefined') {
  StorageService.init().catch((err) => {
    console.warn('[StorageService] Initial bootstrap notice:', err);
  });
}
