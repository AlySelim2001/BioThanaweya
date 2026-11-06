import React, { useState } from 'react';
import {
  Activity,
  ArrowRight,
  BookOpen,
  Calculator,
  CheckCircle,
  Clock,
  Layers,
  Play,
  Sparkles,
  Zap,
} from 'lucide-react';
import { SUB_TOPICS, CHAPTERS } from '../services/curriculumRepository';
import { GamificationBanner } from '../components/GamificationBanner';
import { SubTopic, TopicMastery, UnitCategory, WellbeingProfile } from '../types';

interface DashboardScreenProps {
  onNavigateToTopic: (topicId: string) => void;
  onNavigateToPractice: (topicId: string) => void;
  onNavigateToFsrs: (topicId?: string) => void;
  onNavigateToFormulaLab: () => void;
  onNavigateToSimulatedExam: () => void;
  onOpenAgentsModal: () => void;
  topicMasteries: TopicMastery[];
  profile: WellbeingProfile;
}

export const DashboardScreen: React.FC<DashboardScreenProps> = ({
  onNavigateToTopic,
  onNavigateToPractice,
  onNavigateToFsrs,
  onNavigateToFormulaLab,
  onNavigateToSimulatedExam,
  onOpenAgentsModal,
  topicMasteries,
  profile,
}) => {
  const [selectedUnit, setSelectedUnit] = useState<UnitCategory | 'ALL'>('ALL');

  const filteredTopics = SUB_TOPICS.filter((t) => {
    if (selectedUnit === 'ALL') return true;
    return t.unit === selectedUnit;
  });

  // Calculate overall retention
  const overallRetained = Math.round(
    topicMasteries.reduce((acc, m) => acc + (m.stability >= 7 ? 1 : 0), 0) /
      (topicMasteries.length || 1) *
      100
  );

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8 animate-in fade-in duration-300">
      {/* Hero Header */}
      <div className="relative rounded-3xl bg-gradient-to-br from-[#071329] via-[#0E2045] to-[#123060] border border-cyan-500/20 p-6 md:p-8 shadow-2xl overflow-hidden">
        <div className="absolute top-0 right-0 -mr-16 -mt-16 w-72 h-72 rounded-full bg-teal-500/10 blur-3xl pointer-events-none" />
        <div className="relative z-10 max-w-3xl space-y-4">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-teal-500/15 border border-teal-500/30 text-teal-300 text-xs font-bold tracking-wide">
            <Sparkles className="w-3.5 h-3.5 text-teal-400" />
            7-AGENT AI LEARNING ECOSYSTEM
          </div>

          <h1 className="text-2xl md:text-4xl font-extrabold text-white tracking-tight">
            Thanaweya Biology (Units 1 & 2)
          </h1>

          <p className="text-sm md:text-base text-slate-300 leading-relaxed">
            Grounded in the official Egyptian Ministry textbook. Deep English technical breakdowns, Arabic ELI5 analogies, Mark Scheme keyword grading & FSRS spaced memory retention.
          </p>

          <div className="flex flex-wrap items-center gap-3 pt-2">
            <button
              onClick={() => onNavigateToPractice('sarcomere_contraction')}
              className="flex items-center gap-2 px-5 py-2.5 rounded-xl bg-teal-500 hover:bg-teal-400 text-slate-950 font-bold text-sm shadow-lg shadow-teal-500/25 transition"
            >
              <Zap className="w-4 h-4" />
              Practice Exam Items
            </button>

            <button
              onClick={() => onNavigateToFsrs('sarcomere_contraction')}
              className="flex items-center gap-2 px-5 py-2.5 rounded-xl bg-slate-900/80 hover:bg-slate-800 text-cyan-300 border border-cyan-500/30 font-bold text-sm transition"
            >
              <Layers className="w-4 h-4 text-cyan-400" />
              FSRS Flashcards
            </button>

            <button
              onClick={onNavigateToFormulaLab}
              className="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-amber-500/15 hover:bg-amber-500/25 text-amber-300 border border-amber-500/30 font-bold text-sm transition"
            >
              <Calculator className="w-4 h-4 text-amber-400" />
              Bio-Law Lab
            </button>
          </div>
        </div>
      </div>

      {/* Simulated Ministry Exam Hero Card */}
      <div className="rounded-2xl border border-rose-500/40 bg-gradient-to-r from-rose-950/20 via-slate-900 to-red-950/20 p-6 shadow-xl relative overflow-hidden">
        <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
          <div className="space-y-2 max-w-2xl">
            <div className="flex items-center gap-2.5">
              <div className="p-2 rounded-xl bg-rose-500/20 text-rose-400">
                <Clock className="w-5 h-5 animate-pulse" />
              </div>
              <div>
                <h3 className="text-lg font-bold text-rose-300">
                  Simulated Ministry Exam Mode
                </h3>
                <p className="text-xs text-slate-400">
                  45-Minute Timed • 15 Authentic Review Items • Mark Scheme Grading • Voice Answering
                </p>
              </div>
            </div>
            <p className="text-xs md:text-sm text-slate-300 leading-relaxed">
              Experience the actual Egyptian examination environment with strict timer countdown, Ministry keyword evaluation, penalty warnings, and official textbook citations.
            </p>
          </div>

          <button
            onClick={onNavigateToSimulatedExam}
            className="flex items-center gap-2 px-6 py-3 rounded-xl bg-rose-600 hover:bg-rose-500 text-white font-extrabold text-sm shadow-lg shadow-rose-600/30 transition shrink-0"
          >
            <Play className="w-4 h-4 fill-white" />
            Launch Ministry Simulation
          </button>
        </div>
      </div>

      {/* Curriculum Mastery Heatmap (FSRS) */}
      <div className="rounded-2xl border border-slate-800 bg-[#0E1A35]/60 p-6 space-y-4">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2">
          <div>
            <div className="flex items-center gap-2">
              <Activity className="w-5 h-5 text-teal-400" />
              <h2 className="text-base font-bold text-white">
                FSRS Curriculum Mastery Heatmap
              </h2>
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Real-time retention stability across all curriculum units calculated by FSRS v4.5/v5:
            </p>
          </div>
          <div className="px-3 py-1 rounded-lg bg-emerald-500/15 border border-emerald-500/30 text-emerald-300 text-xs font-extrabold">
            {overallRetained}% High Retention
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 pt-2">
          {topicMasteries.map((m) => {
            const levelColor =
              m.masteryLevel === 'MASTERED'
                ? 'bg-emerald-500/15 text-emerald-300 border-emerald-500/30'
                : m.masteryLevel === 'GOOD'
                ? 'bg-teal-500/15 text-teal-300 border-teal-500/30'
                : m.masteryLevel === 'LEARNING'
                ? 'bg-amber-500/15 text-amber-300 border-amber-500/30'
                : 'bg-rose-500/15 text-rose-300 border-rose-500/30';

            return (
              <div
                key={m.topicId}
                onClick={() => onNavigateToTopic(m.topicId)}
                className="p-3.5 rounded-xl bg-slate-900/80 border border-slate-800 hover:border-slate-700 transition cursor-pointer flex items-center justify-between group"
              >
                <div className="space-y-1 pr-2">
                  <div className="text-xs font-bold text-slate-200 group-hover:text-teal-300 transition line-clamp-1">
                    {m.title}
                  </div>
                  <div className="text-[10px] text-slate-400">
                    {m.unit === 'UNIT_1' ? 'Unit 1' : 'Unit 2'} • {Math.round(m.stability)}d Stability
                  </div>
                </div>
                <span
                  className={`text-[10px] font-extrabold px-2 py-0.5 rounded-md border shrink-0 ${levelColor}`}
                >
                  {m.masteryLevel.replace('_', ' ')}
                </span>
              </div>
            );
          })}
        </div>
      </div>

      {/* Gamification & Daily Wellbeing Banner */}
      <GamificationBanner profile={profile} />

      {/* Unit Filter Tabs & Curriculum Chapters */}
      <div className="space-y-4">
        <div className="flex items-center justify-between flex-wrap gap-3">
          <h2 className="text-lg font-bold text-white flex items-center gap-2">
            <BookOpen className="w-5 h-5 text-teal-400" />
            Ministry Curriculum Topics
          </h2>

          <div className="flex items-center gap-2">
            <button
              onClick={() => setSelectedUnit('ALL')}
              className={`px-3 py-1.5 rounded-lg text-xs font-bold transition ${
                selectedUnit === 'ALL'
                  ? 'bg-teal-500 text-slate-950'
                  : 'bg-slate-800 text-slate-400 hover:text-white'
              }`}
            >
              All Topics ({SUB_TOPICS.length})
            </button>
            <button
              onClick={() => setSelectedUnit('UNIT_1')}
              className={`px-3 py-1.5 rounded-lg text-xs font-bold transition ${
                selectedUnit === 'UNIT_1'
                  ? 'bg-teal-500 text-slate-950'
                  : 'bg-slate-800 text-slate-400 hover:text-white'
              }`}
            >
              Unit 1 (Structure & Function)
            </button>
            <button
              onClick={() => setSelectedUnit('UNIT_2')}
              className={`px-3 py-1.5 rounded-lg text-xs font-bold transition ${
                selectedUnit === 'UNIT_2'
                  ? 'bg-teal-500 text-slate-950'
                  : 'bg-slate-800 text-slate-400 hover:text-white'
              }`}
            >
              Unit 2 (Molecular Biology)
            </button>
          </div>
        </div>

        {/* Topic Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {filteredTopics.map((topic: SubTopic) => {
            const chap = CHAPTERS.find((c) => c.id === topic.chapterId);
            return (
              <div
                key={topic.id}
                className="rounded-2xl border border-slate-800 bg-[#0E1A35]/80 hover:border-slate-700 p-5 flex flex-col justify-between transition group shadow-md"
              >
                <div className="space-y-3">
                  <div className="flex items-center justify-between">
                    <span
                      className={`text-[10px] font-extrabold px-2 py-0.5 rounded-full border ${
                        topic.unit === 'UNIT_1'
                          ? 'bg-teal-500/10 text-teal-300 border-teal-500/20'
                          : 'bg-purple-500/10 text-purple-300 border-purple-500/20'
                      }`}
                    >
                      {topic.unit === 'UNIT_1' ? 'Unit 1' : 'Unit 2'}
                    </span>
                    <span className="flex items-center gap-1 text-[11px] font-bold text-amber-400">
                      <Zap className="w-3.5 h-3.5" />
                      High-Yield
                    </span>
                  </div>

                  <div>
                    <h3 className="text-base font-bold text-white group-hover:text-teal-300 transition line-clamp-1">
                      {topic.title}
                    </h3>
                    <p className="text-xs text-teal-400 font-arabic font-semibold mt-0.5 line-clamp-1">
                      {topic.arabicTitle}
                    </p>
                  </div>

                  <p className="text-xs text-slate-400 leading-relaxed line-clamp-2">
                    {topic.summary}
                  </p>
                </div>

                <div className="pt-5 mt-4 border-t border-slate-800 flex items-center justify-between">
                  <span className="text-[11px] text-slate-400 font-medium">
                    {chap ? chap.title : ''}
                  </span>
                  <div className="flex items-center gap-2">
                    <button
                      onClick={() => onNavigateToPractice(topic.id)}
                      className="px-2.5 py-1 text-xs font-semibold rounded-lg text-slate-300 hover:text-white bg-slate-800 hover:bg-slate-700 transition"
                    >
                      Practice
                    </button>
                    <button
                      onClick={() => onNavigateToTopic(topic.id)}
                      className="flex items-center gap-1 px-3 py-1 text-xs font-bold rounded-lg bg-teal-500/20 hover:bg-teal-500/30 text-teal-300 transition"
                    >
                      Study Pack
                      <ArrowRight className="w-3.5 h-3.5" />
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
