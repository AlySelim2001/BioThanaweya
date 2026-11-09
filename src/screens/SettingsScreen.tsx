import React, { useState } from 'react';
import {
  ArrowLeft,
  Settings,
  Clock,
  Shield,
  RotateCcw,
  BookOpen,
  Check,
  Database,
  Vibrate,
} from 'lucide-react';
import { StorageService } from '../services/storage';
import { haptics } from '../services/haptics';
import { WellbeingProfile } from '../types';

interface SettingsScreenProps {
  onBack: () => void;
  profile: WellbeingProfile;
  onProfileUpdated: (p: WellbeingProfile) => void;
  onOpenStorageModal?: () => void;
}

export const SettingsScreen: React.FC<SettingsScreenProps> = ({
  onBack,
  profile,
  onProfileUpdated,
  onOpenStorageModal,
}) => {
  const [chunkMinutes, setChunkMinutes] = useState(profile.studyChunkMinutes);
  const [hapticsEnabled, setHapticsEnabled] = useState(() => haptics.isEnabled());
  const [savedToast, setSavedToast] = useState(false);

  const handleSaveChunk = (mins: number) => {
    setChunkMinutes(mins);
    const updated = { ...profile, studyChunkMinutes: mins };
    StorageService.saveProfile(updated);
    onProfileUpdated(updated);
    setSavedToast(true);
    setTimeout(() => setSavedToast(false), 2000);
  };

  const handleToggleHaptics = (enable: boolean) => {
    haptics.setEnabled(enable);
    setHapticsEnabled(enable);
    setSavedToast(true);
    setTimeout(() => setSavedToast(false), 2000);
  };

  const handleReset = async () => {
    if (confirm('Are you sure you want to reset your local IndexedDB storage, FSRS cards, and exam history?')) {
      await StorageService.resetAll();
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

      {/* Tactile Haptic Feedback Card */}
      <div className="p-5 rounded-2xl bg-[#0E1A35]/80 border border-slate-800 space-y-3">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2 text-sm font-bold text-white">
            <Vibrate className="w-4 h-4 text-teal-400" />
            <span>Tactile Haptic Feedback (Mobile Vibration API)</span>
          </div>
          <span className="text-[10px] font-mono px-2 py-0.5 rounded-full bg-slate-800 text-slate-400 border border-slate-700">
            {haptics.isSupported() ? 'Supported' : 'Not Supported in Browser'}
          </span>
        </div>
        <p className="text-xs text-slate-400 leading-relaxed">
          Provides subtle mechanical tick pulses (10ms) during screen transitions, modal dialog activations, and achievement awards on touch devices.
        </p>

        <div className="flex items-center gap-3 pt-2">
          <button
            onClick={() => handleToggleHaptics(true)}
            className={`px-4 py-2 rounded-xl text-xs font-bold border transition ${
              hapticsEnabled
                ? 'bg-teal-500 text-slate-950 border-teal-400 font-black'
                : 'bg-slate-900 text-slate-300 border-slate-700 hover:bg-slate-800'
            }`}
          >
            Haptics Enabled
          </button>
          <button
            onClick={() => handleToggleHaptics(false)}
            className={`px-4 py-2 rounded-xl text-xs font-bold border transition ${
              !hapticsEnabled
                ? 'bg-slate-700 text-white border-slate-600 font-bold'
                : 'bg-slate-900 text-slate-400 border-slate-800 hover:bg-slate-800'
            }`}
          >
            Muted (Off)
          </button>
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

      {/* Data Management & IndexedDB Vault */}
      <div className="p-5 rounded-2xl bg-[#0E1A35]/80 border border-teal-500/30 space-y-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2 text-sm font-bold text-white">
            <Database className="w-4 h-4 text-teal-400" />
            <span>Persistent IndexedDB Vault & Portability</span>
          </div>
          <span className="text-[10px] font-mono px-2 py-0.5 rounded-full bg-teal-500/10 text-teal-300 border border-teal-500/20">
            FOSS Engine
          </span>
        </div>
        <p className="text-xs text-slate-300 leading-relaxed">
          Your learning history, FSRS memory weights, topic masteries, and exam results are safely stored in your browser's private IndexedDB database. You can export complete JSON backups or restore previous snapshots at any time.
        </p>
        <div className="flex flex-wrap items-center gap-3 pt-1">
          {onOpenStorageModal && (
            <button
              onClick={onOpenStorageModal}
              className="flex items-center gap-2 px-4 py-2 rounded-xl bg-teal-500 hover:bg-teal-400 text-slate-950 text-xs font-bold transition shadow-lg shadow-teal-500/20"
            >
              <Database className="w-4 h-4" />
              Manage Storage & Backups
            </button>
          )}
          <button
            onClick={handleReset}
            className="flex items-center gap-2 px-4 py-2 rounded-xl bg-rose-600/20 hover:bg-rose-600/30 text-rose-300 border border-rose-500/40 text-xs font-semibold transition"
          >
            <RotateCcw className="w-4 h-4" />
            Factory Reset
          </button>
        </div>
      </div>
    </div>
  );
};
