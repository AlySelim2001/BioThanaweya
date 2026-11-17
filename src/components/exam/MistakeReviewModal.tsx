import { useState, useEffect, type FC } from 'react';
import { AlertTriangle, BookOpen, CheckCircle2, Save, X, XCircle } from 'lucide-react';
import { ExamAnswerRecord, ExamQuestion } from '../../types/exam';
import { SvgBioDiagram } from '../common/SvgBioDiagram';
import { bioDb } from '../../db/bioDatabase';

interface MistakeReviewModalProps {
  question: ExamQuestion | null;
  answerRecord?: ExamAnswerRecord;
  onClose: () => void;
}

export const MistakeReviewModal: FC<MistakeReviewModalProps> = ({
  question,
  answerRecord,
  onClose,
}) => {
  const [noteText, setNoteText] = useState<string>('');
  const [isSavedNote, setIsSavedNote] = useState<boolean>(false);

  useEffect(() => {
    if (!question) return;
    const targetQuestionId = question.id;
    async function loadExistingNote() {
      const existing = await bioDb.savedNotes.where('questionId').equals(targetQuestionId).first();
      if (existing) {
        setNoteText(existing.noteText);
        setIsSavedNote(true);
      } else {
        setNoteText('');
        setIsSavedNote(false);
      }
    }
    loadExistingNote();
  }, [question]);

  if (!question) return null;

  const selectedOption = answerRecord?.selectedOption;
  const isCorrect = selectedOption === question.correctOptionId;

  const handleSaveNote = async () => {
    if (!noteText.trim()) return;
    await bioDb.savedNotes.put({
      id: `note_${question.id}`,
      questionId: question.id,
      noteText: noteText.trim(),
      createdAt: Date.now(),
    });
    setIsSavedNote(true);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md animate-in fade-in">
      <div className="max-w-2xl w-full max-h-[90vh] overflow-y-auto rounded-2xl bg-obsidian-800 border border-slate-700 p-6 shadow-2xl text-right">
        {/* Header */}
        <div className="flex items-center justify-between gap-3 pb-4 border-b border-slate-700/80 mb-5">
          <div className="flex items-center gap-2">
            <span
              className={`w-8 h-8 rounded-xl flex items-center justify-center text-obsidian-950 font-black text-sm ${
                isCorrect ? 'bg-bioemerald' : 'bg-red-500'
              }`}
            >
              {question.questionNumber}
            </span>
            <div>
              <h3 className="text-base font-extrabold text-white">
                تحليل السؤال وفخاخ الإجابة الوزارية
              </h3>
              <p className="text-xs text-slate-400">
                {question.chapterTitleArabic} • {question.topicTitleArabic}
              </p>
            </div>
          </div>

          <button
            onClick={onClose}
            className="p-1.5 rounded-lg bg-obsidian-900 hover:bg-obsidian-700 border border-slate-700 text-slate-400 hover:text-white transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Question body */}
        <div className="bg-obsidian-900/90 rounded-xl p-4 border border-slate-800 mb-5">
          <p className="text-sm sm:text-base font-bold text-slate-100 leading-relaxed">
            {question.textArabic}
          </p>

          {question.diagramSvgKey && (
            <div className="mt-3">
              <SvgBioDiagram
                diagramKey={question.diagramSvgKey}
                captionArabic={question.diagramCaptionArabic}
              />
            </div>
          )}
        </div>

        {/* Options Comparison */}
        <div className="space-y-2 mb-5">
          {question.options.map((opt) => {
            const isStudentPick = selectedOption === opt.id;
            const isMinisterialKey = question.correctOptionId === opt.id;

            let cardStyle = 'bg-obsidian-900/50 border-slate-800 text-slate-300';
            if (isMinisterialKey) {
              cardStyle = 'bg-bioemerald/15 border-bioemerald text-white ring-1 ring-bioemerald/50';
            } else if (isStudentPick && !isCorrect) {
              cardStyle = 'bg-red-500/15 border-red-500 text-white ring-1 ring-red-500/50';
            }

            return (
              <div
                key={opt.id}
                className={`p-3 rounded-xl border flex items-center justify-between gap-3 text-xs sm:text-sm font-semibold transition-all ${cardStyle}`}
              >
                <div className="flex items-center gap-2.5">
                  <span className="w-6 h-6 rounded-lg bg-obsidian-950 font-bold flex items-center justify-center border border-slate-700 text-xs">
                    {opt.label}
                  </span>
                  <span>{opt.textArabic}</span>
                </div>

                <div className="shrink-0 flex items-center gap-1.5 text-xs font-bold">
                  {isMinisterialKey && (
                    <span className="flex items-center gap-1 text-bioemerald bg-bioemerald/20 px-2 py-0.5 rounded-md border border-bioemerald/40">
                      <CheckCircle2 className="w-3.5 h-3.5" />
                      الإجابة النموذجية
                    </span>
                  )}
                  {isStudentPick && !isCorrect && (
                    <span className="flex items-center gap-1 text-red-400 bg-red-500/20 px-2 py-0.5 rounded-md border border-red-500/40">
                      <XCircle className="w-3.5 h-3.5" />
                      إجابتك المسجلة
                    </span>
                  )}
                </div>
              </div>
            );
          })}
        </div>

        {/* Trap Analysis Box */}
        <div className="rounded-xl bg-biotrap-500/10 border border-biotrap-500/30 p-4 mb-5">
          <div className="flex items-center gap-2 mb-2 text-biotrap-500 font-extrabold text-sm">
            <AlertTriangle className="w-4 h-4" />
            <span>لماذا هذا فخ امتحاني وزاري؟</span>
          </div>
          <p className="text-xs sm:text-sm text-slate-200 leading-relaxed font-medium">
            {question.trapExplanationArabic}
          </p>
          <div className="mt-3 pt-2.5 border-t border-biotrap-500/20 flex items-center gap-1.5 text-[11px] text-slate-400">
            <BookOpen className="w-3.5 h-3.5 text-bioteal shrink-0" />
            <span>
              <strong>المرجع المعتمد:</strong> {question.scientificReferenceArabic}
            </span>
          </div>
        </div>

        {/* Local Personal Note for Spaced Repetition */}
        <div className="rounded-xl bg-obsidian-900 border border-slate-800 p-4 mb-4">
          <label className="block text-xs font-bold text-slate-300 mb-2">
            ملاحظتك الدراسية الشخصية (محفوظة محلياً للمراجعة السريعة):
          </label>
          <textarea
            value={noteText}
            onChange={(e) => {
              setNoteText(e.target.value);
              setIsSavedNote(false);
            }}
            placeholder="اكتب خلاصة فهمك لتتجنب الوقوع في هذا الفخ مستقبلاً..."
            rows={2}
            className="w-full rounded-lg bg-obsidian-950 border border-slate-700 p-2.5 text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:border-bioteal resize-none font-cairo"
          />
          <div className="flex items-center justify-between mt-2">
            <span className="text-[11px] text-bioemerald">
              {isSavedNote ? '✓ تم حفظ الملاحظة بنجاح' : ''}
            </span>
            <button
              onClick={handleSaveNote}
              className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-bold bg-bioteal text-obsidian-950 hover:brightness-110 transition-colors"
            >
              <Save className="w-3.5 h-3.5" />
              <span>حفظ الملاحظة</span>
            </button>
          </div>
        </div>

        {/* Close Button */}
        <div className="flex justify-end">
          <button
            onClick={onClose}
            className="px-5 py-2 rounded-xl text-xs font-extrabold bg-obsidian-900 text-slate-200 border border-slate-700 hover:bg-obsidian-700 transition-colors"
          >
            إغلاق
          </button>
        </div>
      </div>
    </div>
  );
};
