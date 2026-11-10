import React, { useState, useEffect } from 'react';
import {
  Activity,
  Award,
  BookOpen,
  Calculator,
  Database,
  Flame,
  Globe2,
  HardDriveDownload,
  Layers,
  Sparkles,
  Timer,
  Wifi,
  WifiOff,
} from 'lucide-react';
import { WellbeingProfile } from '../types';
import { StorageService, SyncState } from '../services/storage';
import { PWAInstallButton } from './PWAInstallButton';
import { useLanguage } from '../context/LanguageContext';

interface NavbarProps {
  currentScreen: string;
  onNavigate: (screen: string, topicId?: string) => void;
  profile: WellbeingProfile;
  onOpenAgentsModal: () => void;
  onOpenStorageModal: () => void;
  onOpenTextbookModal: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({
  currentScreen,
  onNavigate,
  profile,
  onOpenAgentsModal,
  onOpenStorageModal,
  onOpenTextbookModal,
}) => {
  const [syncState, setSyncState] = useState<SyncState>('synced');
  const [isOnline, setIsOnline] = useState<boolean>(true);
  const { language, toggleLanguage, isRtl, t } = useLanguage();

  useEffect(() => {
    const unsubscribe = StorageService.onSyncStatusChange((state) => {
      setSyncState(state);
    });

    const handleOnline = () => setIsOnline(true);
    const handleOffline = () => setIsOnline(false);

    if (typeof window !== 'undefined') {
      setIsOnline(navigator.onLine);
      window.addEventListener('online', handleOnline);
      window.addEventListener('offline', handleOffline);
    }

    return () => {
      unsubscribe();
      if (typeof window !== 'undefined') {
        window.removeEventListener('online', handleOnline);
        window.removeEventListener('offline', handleOffline);
      }
    };
  }, []);

  return (
    <header className="sticky top-0 z-40 bg-[#0B132B]/95 backdrop-blur-xl border-b border-slate-800/80 shadow-lg shadow-black/20" dir={isRtl ? 'rtl' : 'ltr'}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between gap-2">
        {/* Brand / Logo */}
        <div
          onClick={() => onNavigate('dashboard')}
          className="flex items-center gap-3 cursor-pointer group shrink-0"
        >
          <div className="w-10 h-10 rounded-2xl bg-gradient-to-tr from-teal-500 via-emerald-400 to-cyan-300 p-0.5 shadow-md shadow-teal-500/20 group-hover:shadow-teal-500/40 transition">
            <div className="w-full h-full bg-[#0B132B] rounded-[14px] flex items-center justify-center">
              <Activity className="w-5 h-5 text-teal-400 group-hover:scale-110 transition" />
            </div>
          </div>
          <div>
            <div className="flex items-center gap-2">
              <span className="font-extrabold text-base sm:text-lg tracking-tight bg-gradient-to-r from-teal-200 via-cyan-100 to-white bg-clip-text text-transparent">
                {language === 'ar' ? 'تعلم مع علي' : 'BioThanaweya'}
              </span>
              <span className="text-[10px] font-extrabold px-2 py-0.5 rounded-full bg-teal-500/15 text-teal-300 border border-teal-500/30">
                {language === 'ar' ? 'أحياء ثانوية عامة' : 'Languages Track'}
              </span>
            </div>
            <p className="text-[11px] text-slate-400 hidden sm:flex items-center gap-1.5">
              <span>{t('appSub')}</span>
            </p>
          </div>
        </div>

        {/* Center Desktop Navigation Links */}
        <nav className="hidden lg:flex items-center gap-1 text-xs font-bold">
          <button
            onClick={() => onNavigate('dashboard')}
            className={`px-3 py-1.5 rounded-xl transition ${
              currentScreen === 'dashboard'
                ? 'bg-slate-800 text-teal-300 font-extrabold shadow-sm'
                : 'text-slate-300 hover:text-white hover:bg-slate-800/60'
            }`}
          >
            {t('dashboard')}
          </button>
          <button
            onClick={() => onNavigate('simulations')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-xl transition ${
              currentScreen === 'simulations'
                ? 'bg-slate-800 text-teal-300 font-extrabold shadow-sm'
                : 'text-slate-300 hover:text-white hover:bg-slate-800/60'
            }`}
          >
            <Activity className="w-3.5 h-3.5 text-teal-400" />
            {t('simLab')}
          </button>
          <button
            onClick={() => onNavigate('formula_lab')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-xl transition ${
              currentScreen === 'formula_lab'
                ? 'bg-slate-800 text-amber-300 font-extrabold shadow-sm'
                : 'text-slate-300 hover:text-white hover:bg-slate-800/60'
            }`}
          >
            <Calculator className="w-3.5 h-3.5 text-amber-400" />
            {t('formulaLab')}
          </button>
          <button
            onClick={() => onNavigate('fsrs')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-xl transition ${
              currentScreen === 'fsrs'
                ? 'bg-slate-800 text-cyan-300 font-extrabold shadow-sm'
                : 'text-slate-300 hover:text-white hover:bg-slate-800/60'
            }`}
          >
            <Layers className="w-3.5 h-3.5 text-cyan-400" />
            {t('fsrsCards')}
          </button>
          <button
            onClick={() => onNavigate('simulated_exam')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-xl transition ${
              currentScreen === 'simulated_exam'
                ? 'bg-rose-500/20 text-rose-300 border border-rose-500/40 font-extrabold'
                : 'text-rose-400 hover:bg-rose-500/10'
            }`}
          >
            <Timer className="w-3.5 h-3.5 text-rose-400 animate-pulse" />
            {t('ministryExam')}
          </button>
        </nav>

        {/* Right Actions: Textbook Download + Language Switch + Gamification + Agents */}
        <div className="flex items-center gap-2 sm:gap-2.5">
          {/* Prominent Ministry Textbook Download Button */}
          <button
            onClick={onOpenTextbookModal}
            className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-gradient-to-r from-teal-500/20 via-emerald-500/20 to-teal-500/10 hover:from-teal-500/30 hover:to-emerald-500/20 text-teal-300 border border-teal-500/40 text-xs font-bold shadow-md shadow-teal-500/10 transition active:scale-95"
            title={t('textbookBtn')}
          >
            <BookOpen className="w-4 h-4 text-teal-400" />
            <span className="hidden sm:inline font-extrabold">{t('textbookBtn')}</span>
            <span className="text-[10px] px-1.5 py-0.2 rounded bg-teal-400 text-slate-950 font-black">
              PDF
            </span>
          </button>

          {/* Bilingual Language Switcher (Ar / En) */}
          <button
            onClick={toggleLanguage}
            className="flex items-center gap-1.5 px-2.5 py-1.5 rounded-xl bg-slate-800/80 hover:bg-slate-700/80 text-slate-200 border border-slate-700 text-xs font-bold transition active:scale-95"
            title="Switch Language / تغيير اللغة"
          >
            <Globe2 className="w-3.5 h-3.5 text-cyan-400" />
            <span>{language === 'ar' ? 'English' : 'العربية'}</span>
          </button>

          {/* Streak Indicator */}
          <div className="flex items-center gap-1 bg-slate-800/80 border border-slate-700/80 px-2 sm:px-2.5 py-1 rounded-xl text-xs font-bold text-amber-300 shadow-sm">
            <Flame className="w-3.5 h-3.5 text-amber-400 fill-amber-400" />
            <span>{profile.streakDays}d</span>
          </div>

          {/* Offline/Online Indicator */}
          <div 
            className={`hidden md:flex items-center gap-1 px-2 py-1 rounded-xl text-[11px] font-bold border ${
              isOnline 
                ? 'bg-emerald-500/10 text-emerald-300 border-emerald-500/20' 
                : 'bg-amber-500/15 text-amber-300 border-amber-500/30'
            }`}
            title={isOnline ? 'Network Connected' : 'Offline Mode (Local-First IndexedDB Active)'}
          >
            {isOnline ? <Wifi className="w-3 h-3 text-emerald-400" /> : <WifiOff className="w-3 h-3 text-amber-400" />}
            <span>{isOnline ? 'Online' : 'Offline'}</span>
          </div>

          {/* 7 Agents Ecosystem Button */}
          <button
            onClick={onOpenAgentsModal}
            className="flex items-center gap-1.5 px-2.5 py-1.5 rounded-xl bg-cyan-500/10 hover:bg-cyan-500/20 text-cyan-300 border border-cyan-500/30 text-xs font-bold transition active:scale-95"
            title="7-Agent AI Ecosystem Status"
          >
            <Sparkles className="w-3.5 h-3.5 text-cyan-400" />
            <span className="hidden xl:inline">{t('agents')}</span>
            <span className="w-2 h-2 rounded-full bg-cyan-400 animate-pulse" />
          </button>

          {/* Storage Vault Modal Button */}
          <button
            onClick={onOpenStorageModal}
            className={`p-2 rounded-xl border transition ${
              syncState === 'error'
                ? 'bg-rose-500/20 border-rose-500/40 text-rose-300'
                : syncState === 'saving'
                ? 'bg-amber-500/20 border-amber-500/40 text-amber-300 animate-pulse'
                : 'bg-slate-800/80 hover:bg-slate-700 border-slate-700 text-slate-300 hover:text-white'
            }`}
            title="Offline Storage & Backup Vault"
          >
            <Database className="w-3.5 h-3.5" />
          </button>

          {/* PWA Install Button */}
          <PWAInstallButton />
        </div>
      </div>
    </header>
  );
};
