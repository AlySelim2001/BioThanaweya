import { useState, type FC } from 'react';
import { ChevronLeft, ChevronRight, Flag, Type } from 'lucide-react';
import { ExamAnswerRecord, ExamQuestion, OptionKey } from '../../types/exam';
import { SvgBioDiagram } from '../common/SvgBioDiagram';

interface QuestionCardProps {
  question: ExamQuestion;
  answerRecord?: ExamAnswerRecord;
  totalQuestions: number;
  currentIndex: number;
  onSelectOption: (optionId: OptionKey) => void;
  onToggleFlag: () => void;
  onPrevious: () => void;
  onNext: () => void;
}

export const QuestionCard: FC<QuestionCardProps> = ({
  question,
  answerRecord,
  totalQuestions,
  currentIndex,
  onSelectOption,
  onToggleFlag,
  onPrevious,
  onNext
}) => {
  const [fontSizeTier, setFontSizeTier] = useState<'normal' | 'large' | 'xlarge'>('normal');

  const selectedOption = answerRecord?.selectedOption;
  const isFlagged = answerRecord?.isFlaggedForReview ?? false;

  const fontClass = {
    normal: 'text-base sm:text-lg',
    large: 'text-lg sm:text-xl',
    xlarge: 'text-xl sm:text-2xl'
  }[fontSizeTier];

  return (
    <div className="rounded-2xl bg-obsidian-800/90 border border-slate-700/80 p-5 sm:p-7 shadow-2xl flex flex-col justify-between">
      <div>
        {/* Top bar: Topic metadata, Question weight, Flag button, Font Size adjuster */}
        <div className="flex flex-wrap items-center justify-between gap-3 mb-5 pb-4 border-b border-slate-700/60">
          <div className="flex flex-wrap items-center gap-2">
            <span className="text-xs font-bold px-3 py-1 rounded-full bg-slate-700/60 text-slate-200 border border-slate-600">
              {question.chapterTitleArabic}
            </span>
            <span className="text-xs text-bioteal font-semibold bg-bioteal/10 px-2.5 py-1 rounded-full border border-bioteal/30">
              {question.topicTitleArabic}
            </span>
          </div>

          <div className="flex items-center gap-2">
            {/* Font Size Adjuster */}
            <div className="flex items-center gap-1 bg-obsidian-900 px-2 py-1 rounded-xl border border-slate-800 text-xs">
              <Type className="w-3.5 h-3.5 text-slate-400" />
              <button
                onClick={() => setFontSizeTier('normal')}
                className={`px-1.5 py-0.5 rounded ${fontSizeTier === 'normal' ? 'bg-bioteal text-obsidian-950 font-bold' : 'text-slate-400'}`}
              >
                عادي
              </button>
              <button
                onClick={() => setFontSizeTier('large')}
                className={`px-1.5 py-0.5 rounded ${fontSizeTier === 'large' ? 'bg-bioteal text-obsidian-950 font-bold' : 'text-slate-400'}`}
              >
                كبير
              </button>
              <button
                onClick={() => setFontSizeTier('xlarge')}
                className={`px-1.5 py-0.5 rounded ${fontSizeTier === 'xlarge' ? 'bg-bioteal text-obsidian-950 font-bold' : 'text-slate-400'}`}
              >
                أكبر
              </button>
            </div>

            {/* Flag for review button */}
            <button
              onClick={onToggleFlag}
              className={`flex items-center gap-1.5 px-3 py-1.5 rounded-xl text-xs font-extrabold border transition-all ${
                isFlagged
                  ? 'bg-amber-400 text-obsidian-950 border-amber-400 shadow-md shadow-amber-400/20'
                  : 'bg-obsidian-900 text-slate-300 border-slate-700 hover:border-amber-400 hover:text-amber-400'
              }`}
            >
              <Flag className={`w-3.5 h-3.5 ${isFlagged ? 'fill-obsidian-950' : ''}`} />
              <span>{isFlagged ? 'مُعلَم للمراجعة' : 'تعليم للمراجعة'}</span>
            </button>
          </div>
        </div>

        {/* Question Header & Body */}
        <div className="mb-6">
          <div className="flex items-start gap-3">
            <span className="w-8 h-8 rounded-xl bg-bioteal/20 text-bioteal border border-bioteal/40 font-mono font-extrabold text-sm flex items-center justify-center shrink-0 mt-0.5">
              {question.questionNumber}
            </span>
            <h2 className={`${fontClass} font-bold text-slate-100 leading-relaxed`}>
              {question.textArabic}
            </h2>
          </div>

          {/* Biological SVG Diagram if question contains visual schematic */}
          {question.diagramSvgKey && (
            <div className="mt-4">
              <SvgBioDiagram
                diagramKey={question.diagramSvgKey}
                captionArabic={question.diagramCaptionArabic}
              />
            </div>
          )}
        </div>

        {/* 4 Interactive Answer Options */}
        <div className="space-y-3 mb-8">
          {question.options.map((option) => {
            const isSelected = selectedOption === option.id;

            return (
              <button
                key={option.id}
                onClick={() => onSelectOption(option.id)}
                className={`w-full text-right p-4 rounded-xl border flex items-center justify-between gap-4 transition-all duration-150 ${
                  isSelected
                    ? 'bg-bioteal/15 border-bioteal text-white ring-2 ring-bioteal/40 shadow-lg shadow-bioteal/10'
                    : 'bg-obsidian-900/90 border-slate-700/80 text-slate-200 hover:bg-obsidian-700 hover:border-slate-500'
                }`}
              >
                <div className="flex items-center gap-3.5">
                  <span
                    className={`w-7 h-7 rounded-lg font-bold text-xs flex items-center justify-center border shrink-0 transition-colors ${
                      isSelected
                        ? 'bg-bioteal text-obsidian-950 border-bioteal font-extrabold'
                        : 'bg-obsidian-800 text-slate-300 border-slate-600'
                    }`}
                  >
                    {option.label}
                  </span>
                  <span className="text-sm sm:text-base font-semibold leading-relaxed">
                    {option.textArabic}
                  </span>
                </div>

                <div className="shrink-0">
                  <div
                    className={`w-5 h-5 rounded-full border-2 flex items-center justify-center transition-all ${
                      isSelected
                        ? 'border-bioteal bg-bioteal'
                        : 'border-slate-600'
                    }`}
                  >
                    {isSelected && (
                      <div className="w-2 h-2 rounded-full bg-obsidian-950" />
                    )}
                  </div>
                </div>
              </button>
            );
          })}
        </div>
      </div>

      {/* Bottom Navigation Controls: Prev / Next */}
      <div className="pt-4 border-t border-slate-700/60 flex items-center justify-between gap-3">
        <button
          onClick={onPrevious}
          disabled={currentIndex === 0}
          className={`flex items-center gap-2 px-5 py-2.5 rounded-xl text-xs sm:text-sm font-bold border transition-all ${
            currentIndex === 0
              ? 'opacity-40 cursor-not-allowed bg-obsidian-900 border-slate-800 text-slate-500'
              : 'bg-obsidian-900 border-slate-700 text-slate-200 hover:bg-obsidian-700 hover:border-slate-500'
          }`}
        >
          <ChevronRight className="w-4 h-4" />
          <span>السابق</span>
        </button>

        <div className="text-xs text-slate-400 font-semibold hidden sm:block">
          استخدم أزرار لوحة المفاتيح للتنقل السريع بين الأسئلة
        </div>

        <button
          onClick={onNext}
          disabled={currentIndex === totalQuestions - 1}
          className={`flex items-center gap-2 px-6 py-2.5 rounded-xl text-xs sm:text-sm font-extrabold border transition-all ${
            currentIndex === totalQuestions - 1
              ? 'opacity-40 cursor-not-allowed bg-obsidian-900 border-slate-800 text-slate-500'
              : 'bg-bioteal text-obsidian-950 border-bioteal hover:brightness-110 shadow-lg shadow-bioteal/20'
          }`}
        >
          <span>التالي</span>
          <ChevronLeft className="w-4 h-4" />
        </button>
      </div>
    </div>
  );
};
