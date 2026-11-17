import { useState, type FC } from 'react';
import { Flag, Circle } from 'lucide-react';
import { ExamAnswerRecord, ExamQuestion } from '../../types/exam';

interface DigitalBubbleSheetProps {
  questions: ExamQuestion[];
  answers: Record<string, ExamAnswerRecord>;
  currentIndex: number;
  onSelectIndex: (index: number) => void;
  className?: string;
}

export const DigitalBubbleSheet: FC<DigitalBubbleSheetProps> = ({
  questions,
  answers,
  currentIndex,
  onSelectIndex,
  className = '',
}) => {
  const [filterMode, setFilterMode] = useState<'all' | 'unanswered' | 'flagged'>('all');

  const filteredQuestions = questions.filter((q) => {
    const record = answers[q.id];
    if (filterMode === 'unanswered') {
      return !record || !record.isAnswered;
    }
    if (filterMode === 'flagged') {
      return record && record.isFlaggedForReview;
    }
    return true;
  });

  return (
    <div
      className={`rounded-2xl bg-obsidian-800/90 border border-slate-700/80 p-4 shadow-xl flex flex-col h-full ${className}`}
    >
      {/* Header and Filter Tabs */}
      <div className="flex items-center justify-between gap-2 mb-3 pb-3 border-b border-slate-700/60">
        <div>
          <h4 className="text-sm font-extrabold text-white flex items-center gap-1.5">
            <span>البابل شيت الرقمي</span>
            <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-bioteal/10 text-bioteal border border-bioteal/30">
              وزاري
            </span>
          </h4>
          <p className="text-[11px] text-slate-400">ورقة تسجيل الاختيارات السريعة</p>
        </div>

        {/* Filter buttons */}
        <div className="flex items-center gap-1 bg-obsidian-900 p-1 rounded-xl border border-slate-800 text-[11px] font-bold">
          <button
            onClick={() => setFilterMode('all')}
            className={`px-2 py-1 rounded-lg transition-colors ${
              filterMode === 'all'
                ? 'bg-bioteal text-obsidian-950 font-extrabold shadow-sm'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            الكل ({questions.length})
          </button>
          <button
            onClick={() => setFilterMode('unanswered')}
            className={`px-2 py-1 rounded-lg transition-colors ${
              filterMode === 'unanswered'
                ? 'bg-biotrap-500 text-obsidian-950 font-extrabold shadow-sm'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            المتبقي
          </button>
          <button
            onClick={() => setFilterMode('flagged')}
            className={`px-2 py-1 rounded-lg transition-colors ${
              filterMode === 'flagged'
                ? 'bg-amber-400 text-obsidian-950 font-extrabold shadow-sm'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            للمراجعة
          </button>
        </div>
      </div>

      {/* Bubble Sheet Matrix */}
      <div className="flex-1 overflow-y-auto max-h-[460px] pr-1 space-y-2">
        <div className="grid grid-cols-4 sm:grid-cols-5 md:grid-cols-4 lg:grid-cols-5 gap-2">
          {filteredQuestions.map((q) => {
            const actualIndex = questions.findIndex((item) => item.id === q.id);
            const isCurrent = actualIndex === currentIndex;
            const record = answers[q.id];
            const isAnswered = record?.isAnswered;
            const isFlagged = record?.isFlaggedForReview;
            const chosenOption = record?.selectedOption;

            // Map option ID to Arabic letter
            const optionArabicLabel: Record<string, string> = {
              A: 'أ',
              B: 'ب',
              C: 'ج',
              D: 'د',
            };

            return (
              <button
                key={q.id}
                onClick={() => onSelectIndex(actualIndex)}
                className={`relative group flex flex-col items-center justify-center p-2 rounded-xl border text-center transition-all ${
                  isCurrent
                    ? 'bg-bioteal/20 border-bioteal text-bioteal ring-2 ring-bioteal/40 shadow-lg shadow-bioteal/10'
                    : isAnswered
                      ? 'bg-bioemerald/15 border-bioemerald/40 text-bioemerald hover:bg-bioemerald/25'
                      : 'bg-obsidian-900 border-slate-700/80 text-slate-400 hover:border-slate-500 hover:text-slate-200'
                }`}
              >
                {/* Flag indicator corner */}
                {isFlagged && (
                  <span className="absolute -top-1 -right-1 w-3.5 h-3.5 rounded-full bg-amber-400 flex items-center justify-center text-obsidian-950 shadow-sm">
                    <Flag className="w-2.5 h-2.5 fill-current" />
                  </span>
                )}

                {/* Question Number */}
                <span className="text-xs font-bold leading-none">{q.questionNumber}</span>

                {/* Chosen Option Bubble */}
                <div className="mt-1 flex items-center justify-center">
                  {isAnswered && chosenOption ? (
                    <span className="w-5 h-5 rounded-full bg-bioemerald text-obsidian-950 text-[11px] font-black flex items-center justify-center shadow-sm">
                      {optionArabicLabel[chosenOption] || chosenOption}
                    </span>
                  ) : (
                    <Circle className="w-4 h-4 text-slate-600 stroke-[1.5]" />
                  )}
                </div>

                {/* 2-marks badge dot */}
                {q.marks === 2 && (
                  <span
                    title="سؤال ذو درجتين"
                    className="absolute bottom-1 right-1 w-1.5 h-1.5 rounded-full bg-biotrap-500"
                  />
                )}
              </button>
            );
          })}
        </div>

        {filteredQuestions.length === 0 && (
          <div className="py-8 text-center text-xs text-slate-500">
            لا توجد أسئلة تطابق الفلتر المحدد حالياً.
          </div>
        )}
      </div>

      {/* Legend Footer */}
      <div className="mt-4 pt-3 border-t border-slate-700/60 grid grid-cols-3 gap-2 text-[10px] text-slate-400 font-semibold">
        <div className="flex items-center gap-1">
          <span className="w-2.5 h-2.5 rounded-full bg-bioemerald" />
          <span>محلول</span>
        </div>
        <div className="flex items-center gap-1">
          <span className="w-2.5 h-2.5 rounded-full bg-amber-400" />
          <span>للمراجعة</span>
        </div>
        <div className="flex items-center gap-1">
          <span className="w-2.5 h-2.5 rounded-full bg-biotrap-500" />
          <span>سؤال درجتين</span>
        </div>
      </div>
    </div>
  );
};
