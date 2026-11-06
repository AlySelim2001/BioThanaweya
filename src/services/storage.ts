import { FSRSCard, StudentBadge, TopicMastery, WellbeingProfile, ExamResultReport } from '../types';
import { getDefaultTopicMasteries, STUDY_PACKS } from './curriculumRepository';

const FSRS_CARDS_KEY = 'biothanaweya_fsrs_cards_v1';
const PROFILE_KEY = 'biothanaweya_wellbeing_profile_v1';
const MASTERIES_KEY = 'biothanaweya_topic_masteries_v1';
const EXAM_HISTORY_KEY = 'biothanaweya_exam_history_v1';

export const INITIAL_BADGES: StudentBadge[] = [
  { id: 'b_sarcomere', title: 'Sarcomere Architect', titleArabic: 'مهندس القطعة العضلية', iconName: 'Activity', description: 'Mastered Huxley sliding filament calculations and landmark relationships.', unlockedAt: new Date().toISOString() },
  { id: 'b_chargaff', title: 'Chargaff Whiz', titleArabic: 'خبير شارجاف والوراثة', iconName: 'Dna', description: 'Solved 10 DNA double helix base pairing puzzles without error.', unlockedAt: new Date().toISOString() },
  { id: 'b_ministry', title: 'Ministry Gold Scholar', titleArabic: 'طبيب المستقبل - الدرجة النهائية', iconName: 'Award', description: 'Achieved ≥ 90% on a simulated 45-minute Ministry biology examination.' },
  { id: 'b_streak7', title: '7-Day Focus Titan', titleArabic: 'بطل الالتزام الأسبوعي', iconName: 'Flame', description: 'Maintained a 7-day active study streak in Thanaweya Biology.' },
];

export const INITIAL_PROFILE: WellbeingProfile = {
  streakDays: 4,
  totalXp: 850,
  earnedBadges: INITIAL_BADGES.slice(0, 2),
  examAnxietyScore: 22,
  studyChunkMinutes: 25,
  motivationalQuote: 'التميز عادة، وليس فعلًا عابرًا. استمر في التركيز على الآليات والروابط الجزيئية!',
};

export const StorageService = {
  getProfile(): WellbeingProfile {
    try {
      const raw = localStorage.getItem(PROFILE_KEY);
      if (raw) return JSON.parse(raw);
    } catch {}
    return INITIAL_PROFILE;
  },

  saveProfile(profile: WellbeingProfile): void {
    try {
      localStorage.setItem(PROFILE_KEY, JSON.stringify(profile));
    } catch {}
  },

  addXp(xp: number): WellbeingProfile {
    const prof = this.getProfile();
    prof.totalXp += xp;
    this.saveProfile(prof);
    return prof;
  },

  incrementStreak(): WellbeingProfile {
    const prof = this.getProfile();
    prof.streakDays += 1;
    this.saveProfile(prof);
    return prof;
  },

  getTopicMasteries(): TopicMastery[] {
    try {
      const raw = localStorage.getItem(MASTERIES_KEY);
      if (raw) return JSON.parse(raw);
    } catch {}
    const defaults = getDefaultTopicMasteries();
    this.saveTopicMasteries(defaults);
    return defaults;
  },

  saveTopicMasteries(masteries: TopicMastery[]): void {
    try {
      localStorage.setItem(MASTERIES_KEY, JSON.stringify(masteries));
    } catch {}
  },

  updateTopicStability(topicId: string, stabilityDelta: number): void {
    const masteries = this.getTopicMasteries();
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
    let cards: FSRSCard[] = [];
    try {
      const raw = localStorage.getItem(FSRS_CARDS_KEY);
      if (raw) {
        cards = JSON.parse(raw);
      }
    } catch {}

    if (cards.length === 0) {
      // Seed with initial cards from curriculum
      const allFlashcards = Object.values(STUDY_PACKS).flatMap((p) => p.flashcards);
      cards = allFlashcards.map((fc, idx) => ({
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
      this.saveFsrsCards(cards);
    }

    if (topicId) {
      // Find cards matching the topic
      const topicCardIds = new Set((STUDY_PACKS[topicId]?.flashcards || []).map((f) => f.id));
      return cards.filter((c) => topicCardIds.has(c.cardId));
    }

    return cards;
  },

  saveFsrsCards(cards: FSRSCard[]): void {
    try {
      localStorage.setItem(FSRS_CARDS_KEY, JSON.stringify(cards));
    } catch {}
  },

  updateCard(updatedCard: FSRSCard): void {
    const all = this.getFsrsCards();
    const idx = all.findIndex((c) => c.cardId === updatedCard.cardId);
    if (idx >= 0) {
      all[idx] = updatedCard;
    } else {
      all.push(updatedCard);
    }
    this.saveFsrsCards(all);
  },

  saveExamReport(report: ExamResultReport): void {
    try {
      const raw = localStorage.getItem(EXAM_HISTORY_KEY);
      const history: ExamResultReport[] = raw ? JSON.parse(raw) : [];
      history.unshift(report);
      localStorage.setItem(EXAM_HISTORY_KEY, JSON.stringify(history.slice(0, 10)));
    } catch {}
  },
};
