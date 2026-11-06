import React from 'react';
import { Flame, Award, HeartPulse, Sparkles } from 'lucide-react';
import { WellbeingProfile } from '../types';

interface GamificationBannerProps {
  profile: WellbeingProfile;
}

export const GamificationBanner: React.FC<GamificationBannerProps> = ({ profile }) => {
  const nextLevelXp = 1000;
  const xpProgress = Math.min(100, Math.round((profile.totalXp % nextLevelXp) / 10));

  return (
    <div className="rounded-2xl border border-amber-500/20 bg-gradient-to-r from-amber-950/30 via-slate-900 to-indigo-950/30 p-5 shadow-lg">
      <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
        {/* Streak and Level Stats */}
        <div className="flex items-center gap-4">
          <div className="flex items-center gap-2.5 px-3 py-2 rounded-xl bg-amber-500/10 border border-amber-500/30 text-amber-300">
            <Flame className="w-6 h-6 text-amber-400 fill-amber-400 animate-bounce" />
            <div>
              <div className="text-sm font-extrabold">{profile.streakDays} Day Streak!</div>
              <div className="text-[10px] text-amber-200/80">Daily Biology Review</div>
            </div>
          </div>

          <div className="flex items-center gap-2.5 px-3 py-2 rounded-xl bg-teal-500/10 border border-teal-500/30 text-teal-300">
            <Award className="w-6 h-6 text-teal-400" />
            <div>
              <div className="text-sm font-extrabold">{profile.totalXp} XP</div>
              <div className="text-[10px] text-teal-200/80">Level 2 Scholar</div>
            </div>
          </div>

          <div className="hidden sm:flex items-center gap-2 px-3 py-2 rounded-xl bg-indigo-500/10 border border-indigo-500/30 text-indigo-300">
            <HeartPulse className="w-5 h-5 text-indigo-400" />
            <div>
              <div className="text-xs font-bold">Anxiety: {profile.examAnxietyScore}%</div>
              <div className="text-[10px] text-indigo-200/70">Calm & In Control</div>
            </div>
          </div>
        </div>

        {/* Motivational Quote */}
        <div className="flex-1 max-w-md text-right font-arabic">
          <div className="flex items-center justify-end gap-1.5 text-xs text-amber-400 font-bold mb-1">
            <Sparkles className="w-3.5 h-3.5" />
            <span>نصيحة الوكيل السابع للتحفيز:</span>
          </div>
          <p className="text-xs text-slate-300 leading-relaxed italic">
            "{profile.motivationalQuote}"
          </p>
        </div>
      </div>

      {/* XP progress bar */}
      <div className="mt-4 pt-3 border-t border-slate-800/80 flex items-center gap-3">
        <span className="text-[11px] font-bold text-slate-400 shrink-0">Progress to Level 3:</span>
        <div className="flex-1 h-2 rounded-full bg-slate-800 overflow-hidden">
          <div
            className="h-full bg-gradient-to-r from-teal-400 to-cyan-400 rounded-full transition-all duration-500"
            style={{ width: `${xpProgress}%` }}
          />
        </div>
        <span className="text-[11px] font-bold text-teal-400">{xpProgress}%</span>
      </div>
    </div>
  );
};
