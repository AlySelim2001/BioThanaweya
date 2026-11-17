import { useState, type FC } from 'react';
import { AlertCircle, Clock, Flag, Pause, Play, Send } from 'lucide-react';
import { PacingMetrics } from '../../services/PacingEfficiencyEngine';
import { MinistryExamScoringEngine } from '../../services/MinistryExamScoringEngine';

interface ExamHeaderProps {
  paperTitle: string;
  currentIndex: number;
  totalQuestions: number;
  currentMarks: 1 | 2;
  elapsedSeconds: number;
  remainingSeconds: number;
  isPaused: boolean;
  pacing: PacingMetrics;
  answeredCount: number;
  flaggedCount: number;
  onTogglePause: () => void;
  onSubmitExam: () => void;
}

export const ExamHeader: FC<ExamHeaderProps> = ({
  paperTitle,
  currentIndex,
  totalQuestions,
  currentMarks,
  remainingSeconds,
  isPaused,
  pacing,
  answeredCount,
  flaggedCount,
  onTogglePause,
  onSubmitExam,
}) => {
  const [showConfirmModal, setShowConfirmModal] = useState<boolean>(false);

  const formattedRemainingTime = MinistryExamScoringEngine.formatTime(remainingSeconds);
  const isUrgent = remainingSeconds < 900; // less than 15 minutes
  const isCritical = remainingSeconds < 300; // less than 5 minutes

  const completionPercent =
    totalQuestions > 0 ? Math.round((answeredCount / totalQuestions) * 100) : 0;

  return (
    <>
      <div className="sticky top-[57px] z-30 w-full bg-obsidian-800/95 backdrop-blur-md border-b border-slate-700/80 px-4 py-2.5 shadow-md">
        <div className="max-w-7xl mx-auto flex flex-wrap items-center justify-between gap-3">
          {/* Question Counter & Marks badge */}
          <div className="flex items-center gap-2" title={paperTitle}>
            <div className="px-3 py-1 rounded-xl bg-obsidian-900 border border-slate-700 flex items-center gap-1.5">
              <span className="text-xs text-slate-400">سؤال:</span>
              <span className="text-sm font-extrabold text-bioteal">{currentIndex + 1}</span>
              <span className="text-xs text-slate-500">/</span>
              <span className="text-xs font-semibold text-slate-300">{totalQuestions}</span>
            </div>

            <span
              className={`text-[11px] font-extrabold px-2.5 py-1 rounded-lg border ${
                currentMarks === 2
                  ? 'bg-biotrap-500/20 text-biotrap-500 border-biotrap-500/40'
                  : 'bg-slate-700/50 text-slate-300 border-slate-600'
              }`}
            >
              {currentMarks === 2 ? 'درجتان (وزن مضاعف)' : 'درجة واحدة'}
            </span>
          </div>

          {/* Real-time Timer & Pacing Widget */}
          <div className="flex items-center gap-2 sm:gap-3">
            {/* Pacing pill */}
            <div
              title={pacing.adviceArabic}
              className={`hidden sm:flex items-center gap-1 px-2.5 py-1 rounded-lg text-xs font-bold border ${pacing.colorClass}`}
            >
              <span>{pacing.statusArabic}</span>
            </div>

            {/* Timer countdown */}
            <div
              className={`flex items-center gap-2 px-3.5 py-1 rounded-xl font-mono text-sm sm:text-base font-extrabold border transition-all ${
                isCritical
                  ? 'bg-red-500/20 border-red-500 text-red-400 animate-pulse'
                  : isUrgent
                    ? 'bg-biotrap-500/20 border-biotrap-500 text-biotrap-500'
                    : 'bg-obsidian-900 border-bioteal/40 text-bioteal'
              }`}
            >
              <Clock className="w-4 h-4" />
              <span>{formattedRemainingTime}</span>
            </div>

            {/* Pause/Resume Toggle */}
            <button
              onClick={onTogglePause}
              title={isPaused ? 'استئناف الامتحان' : 'إيقاف مؤقت'}
              className="p-1.5 rounded-lg bg-obsidian-900 hover:bg-obsidian-700 border border-slate-700 text-slate-300 hover:text-white transition-colors"
            >
              {isPaused ? (
                <Play className="w-4 h-4 text-bioemerald" />
              ) : (
                <Pause className="w-4 h-4" />
              )}
            </button>

            {/* Submit Button */}
            <button
              onClick={() => setShowConfirmModal(true)}
              className="flex items-center gap-1.5 px-3.5 py-1 rounded-xl text-xs sm:text-sm font-extrabold bg-gradient-to-r from-bioteal to-bioemerald text-obsidian-950 hover:brightness-110 shadow-lg shadow-bioteal/20 transition-all"
            >
              <Send className="w-3.5 h-3.5" />
              <span>تسليم الامتحان</span>
            </button>
          </div>
        </div>

        {/* Global Progress Bar */}
        <div className="max-w-7xl mx-auto mt-2 flex items-center gap-3">
          <div className="flex-1 h-1.5 bg-obsidian-900 rounded-full overflow-hidden border border-slate-800">
            <div
              className="h-full bg-gradient-to-r from-bioteal via-bioemerald to-cyan-400 transition-all duration-300 rounded-full"
              style={{ width: `${completionPercent}%` }}
            />
          </div>
          <div className="text-[11px] font-bold text-slate-400 flex items-center gap-2 shrink-0">
            <span>
              تمت الإجابة: <strong className="text-slate-200">{answeredCount}</strong>/
              {totalQuestions}
            </span>
            {flaggedCount > 0 && (
              <span className="flex items-center gap-1 text-biotrap-500">
                <Flag className="w-3 h-3 fill-biotrap-500" />
                {flaggedCount} للمراجعة
              </span>
            )}
          </div>
        </div>
      </div>

      {/* Confirmation Modal before Submit */}
      {showConfirmModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-in fade-in">
          <div className="max-w-md w-full rounded-2xl bg-obsidian-800 border border-slate-700 p-6 shadow-2xl text-right">
            <div className="w-12 h-12 rounded-xl bg-biotrap-500/20 border border-biotrap-500/40 text-biotrap-500 flex items-center justify-center mb-4">
              <AlertCircle className="w-6 h-6" />
            </div>

            <h3 className="text-lg font-extrabold text-white mb-2">تأكيد تسليم ورقة الإجابة</h3>

            <p className="text-sm text-slate-300 leading-relaxed mb-4">
              هل أنت متأكد من رغبتك في إنهاء الامتحان ورصد الدرجة الوزارية الرسمية؟
            </p>

            <div className="bg-obsidian-900 p-3.5 rounded-xl border border-slate-800 mb-5 space-y-2 text-xs">
              <div className="flex justify-between items-center text-slate-300">
                <span>الأسئلة المجاب عنها:</span>
                <span className="font-bold text-bioteal">
                  {answeredCount} من {totalQuestions}
                </span>
              </div>
              <div className="flex justify-between items-center text-slate-300">
                <span>الأسئلة المتبقية دون إجابة:</span>
                <span className="font-bold text-biotrap-500">{totalQuestions - answeredCount}</span>
              </div>
              <div className="flex justify-between items-center text-slate-300">
                <span>أسئلة مؤشر عليها للمراجعة:</span>
                <span className="font-bold text-amber-400">{flaggedCount}</span>
              </div>
            </div>

            <div className="flex items-center justify-end gap-3">
              <button
                onClick={() => setShowConfirmModal(false)}
                className="px-4 py-2 rounded-xl text-xs font-bold text-slate-300 bg-obsidian-900 border border-slate-700 hover:bg-obsidian-700 transition-colors"
              >
                العودة للحل
              </button>
              <button
                onClick={() => {
                  setShowConfirmModal(false);
                  onSubmitExam();
                }}
                className="px-5 py-2 rounded-xl text-xs font-extrabold bg-gradient-to-r from-bioteal to-bioemerald text-obsidian-950 hover:brightness-110 shadow-lg shadow-bioteal/20 transition-all"
              >
                تأكيد التسليم الآن
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};
