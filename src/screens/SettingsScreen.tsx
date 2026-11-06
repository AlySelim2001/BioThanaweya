import React, { useState } from 'react';
import {
  ArrowLeft,
  Settings,
  Clock,
  Shield,
  RotateCcw,
  BookOpen,
  Check,
} from 'lucide-react';
import { StorageService } from '../services/storage';
import { WellbeingProfile } from '../types';

interface SettingsScreenProps {
  onBack: () => void;
  profile: WellbeingProfile;
  onProfileUpdated: (p: WellbeingProfile) => void;
}

export const SettingsScreen: React.FC<SettingsScreenProps> = ({
  onBack,
  profile,
  onProfileUpdated,
}) => {
  const [chunkMinutes, setChunkMinutes] = useState(profile.studyChunkMinutes);
  const [savedToast, setSavedToast] = useState(false);

  const handleSaveChunk = (mins: number) => {
    setChunkMinutes(mins);
    const updated = { ...profile, studyChunkMinutes: mins };
    StorageService.saveProfile(updated);
    onProfileUpdated(updated);
    setSavedToast(true);
    setTimeout(() => setSavedToast(false), 2000);
  };

  const handleReset = () => {
    if (confirm('Are you sure you want to reset your local study progress, FSRS cards, and exam history?')) {
      localStorage.clear();
      window.location.reload();
    }
  };

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6 animate-in fade-in duration-300">
      {/* Header */}
      <div className="flex items-center gap-3 pb-4 border-b border-slate-800">
        <button
          onClick={onBack}
          className="p-2 rounded-xl bg-slate-900 border border-slate-800 hover:bg-slate-800 text-slate-300 hover:text-white transition"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>
        <div>
          <h1 className="text-xl font-bold text-white flex items-center gap-2">
            <Settings className="w-5 h-5 text-teal-400" />
            Application Settings & Curriculum Guide
          </h1>
          <p className="text-xs text-slate-400">
            Customize your learning parameters, Pomodoro intervals, and study preferences.
          </p>
        </div>
      </div>

      {savedToast && (
        <div className="flex items-center gap-2 p-3 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 text-xs font-bold animate-in fade-in">
          <Check className="w-4 h-4" />
          <span>Settings saved successfully.</span>
        </div>
      )}

      {/* Study Chunk / Pomodoro */}
      <div className="p-5 rounded-2xl bg-[#0E1A35]/80 border border-slate-800 space-y-3">
        <div className="flex items-center gap-2 text-sm font-bold text-white">
          <Clock className="w-4 h-4 text-teal-400" />
          <span>Agent 7: Study Chunk (Pomodoro Duration)</span>
        </div>
        <p className="text-xs text-slate-400 leading-relaxed">
          Intervals of focused biological deep dive before taking a 5-minute memory consolidation break.
        </p>

        <div className="flex items-center gap-3 pt-2">
          {[25, 45, 60].map((mins) => (
            <button
              key={mins}
              onClick={() => handleSaveChunk(mins)}
              className={`px-4 py-2 rounded-xl text-xs font-bold border transition ${
                chunkMinutes === mins
                  ? 'bg-teal-500 text-slate-950 border-teal-400 font-black'
                  : 'bg-slate-900 text-slate-300 border-slate-700 hover:bg-slate-800'
              }`}
            >
              {mins} Minutes
            </button>
          ))}
        </div>
      </div>

      {/* Curriculum Standards Notice */}
      <div className="p-5 rounded-2xl bg-[#0E1A35]/80 border border-slate-800 space-y-3">
        <div className="flex items-center gap-2 text-sm font-bold text-white">
          <BookOpen className="w-4 h-4 text-cyan-400" />
          <span>Official Egyptian Ministry Standards</span>
        </div>
        <p className="text-xs text-slate-300 leading-relaxed">
          BioThanaweya is designed specifically for students in Egyptian Thanaweya Amma (Languages Schools / التجريبية والمتميزة واللغات الخاصة), strictly following the Ministry of Education biology curriculum:
        </p>
        <ul className="text-xs text-slate-400 space-y-1 list-disc list-inside">
          <li>Unit 1: Structure and Function in Living Organisms (Support & Movement, Hormones, Reproduction, Immunity).</li>
          <li>Unit 2: Molecular Biology (DNA structure & replication, RNA, Genetic Code, and Genetic Engineering).</li>
          <li>Mark schemes enforce mandatory deductions for omitted ions (Ca²⁺), neurotransmitters (ACh), enzymes (cholinesterase, ligase), and structural landmarks.</li>
        </ul>
      </div>

      {/* Data Management */}
      <div className="p-5 rounded-2xl bg-rose-950/20 border border-rose-500/30 space-y-3">
        <div className="flex items-center gap-2 text-sm font-bold text-rose-300">
          <Shield className="w-4 h-4 text-rose-400" />
          <span>Data Storage & Local State</span>
        </div>
        <p className="text-xs text-slate-300 leading-relaxed">
          All your FSRS card repetitions, topic mastery levels, and exam history are saved locally in your browser storage.
        </p>
        <button
          onClick={handleReset}
          className="flex items-center gap-2 px-4 py-2 rounded-xl bg-rose-600/30 hover:bg-rose-600/50 text-rose-200 border border-rose-500/40 text-xs font-bold transition"
        >
          <RotateCcw className="w-4 h-4" />
          Reset All Progress & Re-seed
        </button>
      </div>
    </div>
  );
};
