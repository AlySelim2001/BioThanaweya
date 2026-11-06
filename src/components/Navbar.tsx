import React from 'react';
import {
  Activity,
  Award,
  Calculator,
  Flame,
  Layers,
  Settings,
  Sparkles,
  Timer,
} from 'lucide-react';
import { WellbeingProfile } from '../types';

interface NavbarProps {
  currentScreen: string;
  onNavigate: (screen: string, topicId?: string) => void;
  profile: WellbeingProfile;
  onOpenAgentsModal: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({
  currentScreen,
  onNavigate,
  profile,
  onOpenAgentsModal,
}) => {
  return (
    <header className="sticky top-0 z-40 bg-[#0B132B]/90 backdrop-blur-md border-b border-slate-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        {/* Brand / Logo */}
        <div
          onClick={() => onNavigate('dashboard')}
          className="flex items-center gap-3 cursor-pointer group"
        >
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-teal-600 to-cyan-400 p-0.5 shadow-lg shadow-teal-500/20 group-hover:shadow-teal-500/40 transition">
            <div className="w-full h-full bg-[#0B132B] rounded-[10px] flex items-center justify-center">
              <Activity className="w-5 h-5 text-teal-400" />
            </div>
          </div>
          <div>
            <div className="flex items-center gap-2">
              <span className="font-extrabold text-lg tracking-tight bg-gradient-to-r from-teal-300 via-cyan-200 to-white bg-clip-text text-transparent">
                BioThanaweya
              </span>
              <span className="text-[10px] font-bold px-1.5 py-0.5 rounded-full bg-teal-500/10 text-teal-400 border border-teal-500/20">
                Languages
              </span>
            </div>
            <p className="text-[11px] text-slate-400 flex items-center gap-1">
              Thanaweya Amma Biology • 7 AI Agents
            </p>
          </div>
        </div>

        {/* Navigation Links (Desktop) */}
        <nav className="hidden md:flex items-center gap-1 text-sm font-medium">
          <button
            onClick={() => onNavigate('dashboard')}
            className={`px-3 py-1.5 rounded-lg transition ${
              currentScreen === 'dashboard'
                ? 'bg-slate-800 text-teal-400 font-semibold'
                : 'text-slate-300 hover:text-white hover:bg-slate-800/50'
            }`}
          >
            Dashboard
          </button>
          <button
            onClick={() => onNavigate('formula_lab')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg transition ${
              currentScreen === 'formula_lab'
                ? 'bg-slate-800 text-amber-400 font-semibold'
                : 'text-slate-300 hover:text-white hover:bg-slate-800/50'
            }`}
          >
            <Calculator className="w-4 h-4 text-amber-400" />
            Formula Lab
          </button>
          <button
            onClick={() => onNavigate('fsrs')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg transition ${
              currentScreen === 'fsrs'
                ? 'bg-slate-800 text-cyan-400 font-semibold'
                : 'text-slate-300 hover:text-white hover:bg-slate-800/50'
            }`}
          >
            <Layers className="w-4 h-4 text-cyan-400" />
            FSRS Cards
          </button>
          <button
            onClick={() => onNavigate('simulated_exam')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg transition ${
              currentScreen === 'simulated_exam'
                ? 'bg-red-500/20 text-red-400 border border-red-500/30 font-semibold'
                : 'text-rose-400 hover:bg-rose-500/10'
            }`}
          >
            <Timer className="w-4 h-4 text-rose-400 animate-pulse" />
            Ministry Exam
          </button>
        </nav>

        {/* Right Actions: Gamification Stats & Agent Modal */}
        <div className="flex items-center gap-3">
          {/* Streak Indicator */}
          <div className="flex items-center gap-1.5 bg-slate-800/80 border border-slate-700 px-2.5 py-1 rounded-full text-xs font-bold text-amber-300">
            <Flame className="w-4 h-4 text-amber-400 fill-amber-400" />
            <span>{profile.streakDays}d</span>
          </div>

          {/* XP Badge */}
          <div className="hidden sm:flex items-center gap-1 bg-teal-500/10 border border-teal-500/30 px-2.5 py-1 rounded-full text-xs font-bold text-teal-300">
            <Award className="w-3.5 h-3.5 text-teal-400" />
            <span>{profile.totalXp} XP</span>
          </div>

          {/* 7 Agents Ecosystem Button */}
          <button
            onClick={onOpenAgentsModal}
            title="7 AI Agents Architecture"
            className="flex items-center gap-1.5 px-2.5 py-1.5 rounded-lg bg-indigo-500/15 border border-indigo-500/30 text-indigo-300 hover:bg-indigo-500/25 transition text-xs font-bold"
          >
            <Sparkles className="w-3.5 h-3.5 text-indigo-400" />
            <span className="hidden sm:inline">7 Agents</span>
          </button>

          {/* Settings */}
          <button
            onClick={() => onNavigate('settings')}
            className={`p-2 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition ${
              currentScreen === 'settings' ? 'text-teal-400 bg-slate-800' : ''
            }`}
          >
            <Settings className="w-4 h-4" />
          </button>
        </div>
      </div>
    </header>
  );
};
