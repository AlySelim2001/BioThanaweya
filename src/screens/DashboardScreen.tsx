import React, { useState } from 'react';
import {
  Activity,
  BookOpen,
  Calculator,
  Clock,
  Download,
  Dna,
  Droplet,
  Flame,
  FlaskConical,
  Layers,
  Play,
  Repeat,
  Shield,
  Sparkles,
  Zap,
} from 'lucide-react';
import { SUB_TOPICS, CHAPTERS } from '../services/curriculumRepository';
import { GamificationBanner } from '../components/GamificationBanner';
import { TopicMastery, UnitCategory, WellbeingProfile } from '../types';
import { useLanguage } from '../context/LanguageContext';

interface DashboardScreenProps {
  onNavigateToTopic: (topicId: string) => void;
  onNavigateToPractice: (topicId: string) => void;
  onNavigateToFsrs: (topicId?: string) => void;
  onNavigateToFormulaLab: () => void;
  onNavigateToSimulatedExam: () => void;
  onOpenTextbookModal: () => void;
  topicMasteries: TopicMastery[];
  profile: WellbeingProfile;
}

export const DashboardScreen: React.FC<DashboardScreenProps> = ({
  onNavigateToTopic,
  onNavigateToPractice,
  onNavigateToFsrs,
  onNavigateToFormulaLab,
  onNavigateToSimulatedExam,
  onOpenTextbookModal,
  topicMasteries,
  profile,
}) => {
  const [selectedUnit, setSelectedUnit] = useState<UnitCategory | 'ALL'>('ALL');
  const { language, isRtl, t } = useLanguage();

  const filteredTopics = SUB_TOPICS.filter((tItem) => {
    if (selectedUnit === 'ALL') return true;
    return tItem.unit === selectedUnit;
  });

  const getChapterIcon = (iconName: string) => {
    switch (iconName) {
      case 'Activity':
        return <Activity className="w-5 h-5 text-teal-400" />;
      case 'Droplet':
        return <Droplet className="w-5 h-5 text-cyan-400" />;
      case 'Repeat':
        return <Repeat className="w-5 h-5 text-emerald-400" />;
      case 'Shield':
        return <Shield className="w-5 h-5 text-blue-400" />;
      case 'Dna':
        return <Dna className="w-5 h-5 text-purple-400" />;
      case 'FlaskConical':
        return <FlaskConical className="w-5 h-5 text-amber-400" />;
      default:
        return <Sparkles className="w-5 h-5 text-teal-400" />;
    }
  };

  return (
    <div
      className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8 animate-in fade-in duration-300"
      dir={isRtl ? 'rtl' : 'ltr'}
    >
      {/* 1. Hero Bento Card */}
      <div className="relative rounded-3xl bg-gradient-to-br from-[#071329] via-[#0E2045] to-[#123060] border border-cyan-500/20 p-6 md:p-8 shadow-2xl shadow-cyan-950/30 overflow-hidden">
        <div className="absolute top-0 right-0 -mr-16 -mt-16 w-80 h-80 rounded-full bg-teal-500/10 blur-3xl pointer-events-none" />
        <div className="relative z-10 max-w-3xl space-y-4">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-teal-500/15 border border-teal-500/30 text-teal-300 text-xs font-bold tracking-wide">
            <Sparkles
              className="w-3.5 h-3.5 text-teal-400 animate-spin"
              style={{ animationDuration: '6s' }}
            />
            <span>{t('heroBadge')}</span>
          </div>

          <h1 className="text-2xl md:text-4xl font-black text-white tracking-tight">
            {t('heroTitle')}
          </h1>

          <p className="text-sm md:text-base text-slate-300 leading-relaxed font-normal">
            {t('heroSubtitle')}
          </p>

          <div className="flex flex-wrap items-center gap-3 pt-2">
            <button
              onClick={() => onNavigateToPractice('sarcomere_contraction')}
              className="flex items-center gap-2 px-5 py-2.5 rounded-2xl bg-teal-400 hover:bg-teal-300 text-slate-950 font-black text-sm shadow-lg shadow-teal-500/25 transition active:scale-95"
            >
              <Zap className="w-4 h-4 fill-slate-950" />
              <span>{t('practiceItemsBtn')}</span>
            </button>

            <button
              onClick={() => onNavigateToFsrs('sarcomere_contraction')}
              className="flex items-center gap-2 px-5 py-2.5 rounded-2xl bg-slate-900/90 hover:bg-slate-800 text-cyan-300 border border-cyan-500/30 font-bold text-sm shadow-md transition active:scale-95"
            >
              <Layers className="w-4 h-4 text-cyan-400" />
              <span>{t('fsrsCardsBtn')}</span>
            </button>

            <button
              onClick={onNavigateToFormulaLab}
              className="flex items-center gap-2 px-4 py-2.5 rounded-2xl bg-amber-500/15 hover:bg-amber-500/25 text-amber-300 border border-amber-500/30 font-bold text-sm transition active:scale-95"
            >
              <Calculator className="w-4 h-4 text-amber-400" />
              <span>{t('bioLabBtn')}</span>
            </button>

            <button
              onClick={onOpenTextbookModal}
              className="flex items-center gap-2 px-4 py-2.5 rounded-2xl bg-emerald-500/15 hover:bg-emerald-500/25 text-emerald-300 border border-emerald-500/30 font-bold text-sm transition active:scale-95"
            >
              <BookOpen className="w-4 h-4 text-emerald-400" />
              <span>{t('textbookBtn')}</span>
            </button>
          </div>
        </div>
      </div>

      {/* 2. Simulated Ministry Exam Hero Card */}
      <div className="rounded-3xl border border-rose-500/40 bg-gradient-to-r from-rose-950/20 via-slate-900 to-red-950/20 p-6 md:p-7 shadow-xl shadow-rose-950/10 relative overflow-hidden">
        <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
          <div className="space-y-2.5 max-w-2xl">
            <div className="flex items-center gap-3">
              <div className="p-2.5 rounded-2xl bg-rose-500/20 text-rose-400 border border-rose-500/30 shadow-inner">
                <Clock className="w-5 h-5 animate-pulse" />
              </div>
              <div>
                <h3 className="text-lg md:text-xl font-bold text-rose-200">{t('examHeroTitle')}</h3>
                <p className="text-xs text-rose-300/80 font-medium">{t('examHeroSpecs')}</p>
              </div>
            </div>
            <p className="text-xs md:text-sm text-slate-300 leading-relaxed">{t('examHeroDesc')}</p>
          </div>

          <button
            onClick={onNavigateToSimulatedExam}
            className="flex items-center justify-center gap-2.5 px-6 py-3.5 rounded-2xl bg-gradient-to-r from-rose-600 to-red-500 hover:from-rose-500 hover:to-red-400 text-white font-black text-sm shadow-xl shadow-rose-600/30 transition active:scale-95 shrink-0 self-stretch sm:self-auto"
          >
            <Play className="w-4 h-4 fill-white" />
            <span>{t('launchExamBtn')}</span>
          </button>
        </div>
      </div>

      {/* 3. Bento Grid: Chapter & Curriculum Units Overview */}
      <div className="space-y-5">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <Layers className="w-5 h-5 text-teal-400" />
              <h2 className="text-lg md:text-xl font-bold text-white tracking-tight">
                {t('curriculumBentoTitle')}
              </h2>
            </div>
            <p className="text-xs text-slate-400 mt-1">{t('curriculumBentoSub')}</p>
          </div>

          {/* Unit Filter Tabs */}
          <div className="flex items-center gap-1.5 p-1 rounded-2xl bg-slate-900/90 border border-slate-800 text-xs font-bold self-start sm:self-auto">
            <button
              onClick={() => setSelectedUnit('ALL')}
              className={`px-3 py-1.5 rounded-xl transition ${
                selectedUnit === 'ALL'
                  ? 'bg-teal-500 text-slate-950 font-black shadow-sm'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              {t('allFilter')}
            </button>
            <button
              onClick={() => setSelectedUnit('UNIT_1')}
              className={`px-3 py-1.5 rounded-xl transition ${
                selectedUnit === 'UNIT_1'
                  ? 'bg-teal-500 text-slate-950 font-black shadow-sm'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              {language === 'ar' ? 'الوحدة 1' : 'Unit 1'}
            </button>
            <button
              onClick={() => setSelectedUnit('UNIT_2')}
              className={`px-3 py-1.5 rounded-xl transition ${
                selectedUnit === 'UNIT_2'
                  ? 'bg-teal-500 text-slate-950 font-black shadow-sm'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              {language === 'ar' ? 'الوحدة 2' : 'Unit 2'}
            </button>
          </div>
        </div>

        {/* Bento Grid Layout of Topics */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {filteredTopics.map((topic) => {
            const chapter = CHAPTERS.find((c) => c.id === topic.chapterId);
            const mastery = topicMasteries.find((m) => m.topicId === topic.id);
            const isMastered = (mastery?.stability || 0) >= 7;

            return (
              <div
                key={topic.id}
                className="group relative rounded-3xl bg-slate-900/80 hover:bg-slate-900 border border-slate-800/80 hover:border-teal-500/40 p-5 md:p-6 transition-all duration-200 shadow-xl hover:shadow-teal-500/5 flex flex-col justify-between gap-5"
              >
                <div className="space-y-3.5">
                  {/* Top Bar: Chapter + High-Yield badge */}
                  <div className="flex items-center justify-between gap-2">
                    <div className="flex items-center gap-2">
                      <div className="w-8 h-8 rounded-xl bg-slate-800 border border-slate-700/60 flex items-center justify-center">
                        {chapter ? (
                          getChapterIcon(chapter.iconName)
                        ) : (
                          <Sparkles className="w-4 h-4 text-teal-400" />
                        )}
                      </div>
                      <span className="text-[11px] font-bold text-slate-400 uppercase tracking-wider">
                        {topic.unit === 'UNIT_1'
                          ? language === 'ar'
                            ? 'الوحدة الأولى'
                            : 'Unit 1'
                          : language === 'ar'
                            ? 'الوحدة الثانية'
                            : 'Unit 2'}
                      </span>
                    </div>

                    {topic.highYieldFlag && (
                      <span className="text-[10px] font-extrabold px-2 py-0.5 rounded-full bg-amber-500/15 text-amber-300 border border-amber-500/30">
                        {t('highYieldBadge')}
                      </span>
                    )}
                  </div>

                  {/* Dual Language Titles */}
                  <div className="space-y-1">
                    <h3 className="font-extrabold text-base text-slate-100 group-hover:text-teal-300 transition line-clamp-2">
                      {language === 'ar' ? topic.arabicTitle : topic.title}
                    </h3>
                    <p className="text-xs text-slate-400 font-medium line-clamp-1">
                      {language === 'ar' ? topic.title : topic.arabicTitle}
                    </p>
                  </div>

                  {/* Topic Summary */}
                  <p className="text-xs text-slate-300 leading-relaxed line-clamp-2">
                    {topic.summary}
                  </p>
                </div>

                {/* Direct Action Grid (4 Actions) */}
                <div className="space-y-3 pt-2 border-t border-slate-800/60">
                  <div className="grid grid-cols-2 gap-2 text-xs font-bold">
                    {/* Practice Questions */}
                    <button
                      onClick={() => onNavigateToPractice(topic.id)}
                      className="flex items-center justify-center gap-1.5 py-2 px-2.5 rounded-xl bg-teal-500/15 hover:bg-teal-500/25 text-teal-300 border border-teal-500/30 transition active:scale-95"
                    >
                      <Zap className="w-3.5 h-3.5 text-teal-400" />
                      <span>{t('btnSolve')}</span>
                    </button>

                    {/* FSRS Flashcards */}
                    <button
                      onClick={() => onNavigateToFsrs(topic.id)}
                      className="flex items-center justify-center gap-1.5 py-2 px-2.5 rounded-xl bg-cyan-500/15 hover:bg-cyan-500/25 text-cyan-300 border border-cyan-500/30 transition active:scale-95"
                    >
                      <Layers className="w-3.5 h-3.5 text-cyan-400" />
                      <span>{t('btnCards')}</span>
                    </button>

                    {/* Detailed Breakdown / Lab */}
                    <button
                      onClick={() => onNavigateToTopic(topic.id)}
                      className="flex items-center justify-center gap-1.5 py-2 px-2.5 rounded-xl bg-slate-800 hover:bg-slate-700/80 text-slate-200 border border-slate-700 transition active:scale-95"
                    >
                      <Activity className="w-3.5 h-3.5 text-amber-400" />
                      <span>{t('btnExplore')}</span>
                    </button>

                    {/* Download Ministry PDF */}
                    <button
                      onClick={onOpenTextbookModal}
                      className="flex items-center justify-center gap-1.5 py-2 px-2.5 rounded-xl bg-emerald-500/15 hover:bg-emerald-500/25 text-emerald-300 border border-emerald-500/30 transition active:scale-95"
                    >
                      <Download className="w-3.5 h-3.5 text-emerald-400" />
                      <span>{t('btnDownloadPdf')}</span>
                    </button>
                  </div>

                  {/* FSRS Retention Indicator Footer */}
                  <div className="flex items-center justify-between text-[11px] text-slate-400 pt-1">
                    <span className="flex items-center gap-1">
                      <Flame className="w-3 h-3 text-amber-400" />
                      {language === 'ar' ? 'الاستقرار التكراري:' : 'FSRS Stability:'}
                    </span>
                    <span
                      className={`font-bold ${isMastered ? 'text-emerald-400' : 'text-slate-400'}`}
                    >
                      {mastery ? `${mastery.stability.toFixed(1)}d` : '3.0d'}
                    </span>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* 4. Gamification & Wellbeing Banner */}
      <GamificationBanner profile={profile} />
    </div>
  );
};
