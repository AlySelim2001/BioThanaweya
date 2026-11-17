import { useState, type FC } from 'react';
import {
  Award,
  BookOpen,
  CheckCircle,
  Clock,
  Flame,
  PieChart,
  RefreshCw,
  ShieldCheck,
  Target,
  XCircle,
} from 'lucide-react';
import {
  DetailedEvaluationResult,
  MinistryExamScoringEngine,
} from '../../services/MinistryExamScoringEngine';
import { ExamAnswerRecord, ExamQuestion, OfficialExamPaper } from '../../types/exam';
import { MistakeReviewModal } from './MistakeReviewModal';

interface ExamResultAnalyticsProps {
  paper: OfficialExamPaper;
  questions: ExamQuestion[];
  answers: Record<string, ExamAnswerRecord>;
  evaluation: DetailedEvaluationResult;
  elapsedSeconds: number;
  onRetakeExam: () => void;
  onBackToSelector: () => void;
}

export const ExamResultAnalytics: FC<ExamResultAnalyticsProps> = ({
  paper,
  questions,
  answers,
  evaluation,
  elapsedSeconds,
  onRetakeExam,
  onBackToSelector,
}) => {
  const [selectedQuestionForReview, setSelectedQuestionForReview] = useState<ExamQuestion | null>(
    null
  );
  const [filterMode, setFilterMode] = useState<'all' | 'mistakes' | 'correct'>('all');

  const formattedTime = MinistryExamScoringEngine.formatTime(elapsedSeconds);

  const filteredQuestions = questions.filter((q) => {
    const isCorrect = answers[q.id]?.selectedOption === q.correctOptionId;
    if (filterMode === 'mistakes') return !isCorrect;
    if (filterMode === 'correct') return isCorrect;
    return true;
  });

  return (
    <div className="max-w-5xl mx-auto space-y-6 animate-in fade-in py-2">
      {/* Official Score Certificate Hero Card */}
      <div className="rounded-3xl bg-gradient-to-br from-obsidian-800 via-obsidian-850 to-obsidian-900 border border-slate-700/80 p-6 sm:p-8 shadow-2xl relative overflow-hidden">
        {/* Decorative background glow */}
        <div className="absolute -top-24 -left-24 w-72 h-72 rounded-full bg-bioteal/10 blur-3xl pointer-events-none" />
        <div className="absolute -bottom-24 -right-24 w-72 h-72 rounded-full bg-bioemerald/10 blur-3xl pointer-events-none" />

        <div className="relative z-10 flex flex-col md:flex-row items-center justify-between gap-6 text-center md:text-right">
          <div>
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-bioteal/15 text-bioteal text-xs font-bold border border-bioteal/30 mb-3">
              <ShieldCheck className="w-4 h-4" />
              <span>بطاقة تقييم الامتحان الوزاري المعتمدة</span>
            </div>
            <h1 className="text-xl sm:text-2xl font-black text-white mb-2">{paper.titleArabic}</h1>
            <p className="text-xs sm:text-sm text-slate-300 max-w-lg leading-relaxed">
              {evaluation.gradeDescriptionArabic}
            </p>

            <div className="mt-4 flex flex-wrap items-center justify-center md:justify-start gap-4 text-xs font-semibold text-slate-300">
              <span className="flex items-center gap-1.5 bg-obsidian-900/80 px-3 py-1.5 rounded-xl border border-slate-800">
                <Clock className="w-4 h-4 text-bioteal" />
                الوقت المستغرق: <strong className="text-white">{formattedTime}</strong>
              </span>
              <span className="flex items-center gap-1.5 bg-obsidian-900/80 px-3 py-1.5 rounded-xl border border-slate-800">
                <Target className="w-4 h-4 text-bioemerald" />
                نسبة الصواب: <strong className="text-white">{evaluation.percentage}%</strong>
              </span>
              <span className="flex items-center gap-1.5 bg-obsidian-900/80 px-3 py-1.5 rounded-xl border border-slate-800">
                <Flame className="w-4 h-4 text-biotrap-500" />
                فخاخ وقعت بها:{' '}
                <strong className="text-white">{evaluation.trapsTriggeredCount}</strong>
              </span>
            </div>
          </div>

          {/* Large Score Dial */}
          <div className="flex flex-col items-center justify-center p-6 rounded-2xl bg-obsidian-900/90 border border-slate-700/80 shadow-inner shrink-0 w-52">
            <span className="text-xs font-extrabold text-slate-400 mb-1">المجموع الوزاري</span>
            <div className="flex items-baseline gap-1 font-black">
              <span className="text-4xl sm:text-5xl font-mono text-transparent bg-clip-text bg-gradient-to-r from-bioteal to-bioemerald">
                {evaluation.scoreObtained}
              </span>
              <span className="text-lg text-slate-500 font-mono">/ {evaluation.maxScore}</span>
            </div>
            <span className="mt-2 text-xs font-bold px-3 py-1 rounded-full bg-bioemerald/20 text-bioemerald border border-bioemerald/30">
              {evaluation.gradeTierArabic}
            </span>
          </div>
        </div>
      </div>

      {/* Chapter Breakdown Matrix */}
      <div className="rounded-2xl bg-obsidian-800/90 border border-slate-700/80 p-5 sm:p-6 shadow-xl">
        <div className="flex items-center justify-between gap-2 mb-4 pb-3 border-b border-slate-700/60">
          <h3 className="text-base font-extrabold text-white flex items-center gap-2">
            <PieChart className="w-5 h-5 text-bioteal" />
            <span>التحليل التفصيلي لإتقان فصول المنهج</span>
          </h3>
          <span className="text-xs text-slate-400">معايير مخرجات التعلم</span>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3.5">
          {evaluation.chapterBreakdowns.map((ch) => {
            const isMastered = ch.percentage >= 80;
            const isMid = ch.percentage >= 60 && ch.percentage < 80;

            return (
              <div
                key={ch.chapterId}
                className="p-4 rounded-xl bg-obsidian-900/90 border border-slate-800/80 flex flex-col justify-between"
              >
                <div>
                  <div className="flex items-center justify-between gap-2 mb-2">
                    <span className="text-xs font-bold text-slate-200 truncate">
                      {ch.chapterTitleArabic}
                    </span>
                    <span
                      className={`text-[11px] font-black px-2 py-0.5 rounded-md border ${
                        isMastered
                          ? 'bg-bioemerald/20 text-bioemerald border-bioemerald/30'
                          : isMid
                            ? 'bg-bioteal/20 text-bioteal border-bioteal/30'
                            : 'bg-biotrap-500/20 text-biotrap-500 border-biotrap-500/30'
                      }`}
                    >
                      {ch.percentage}%
                    </span>
                  </div>

                  <div className="text-[11px] text-slate-400 flex items-center justify-between mb-2">
                    <span>
                      الدرجات المحققة: {ch.obtainedMarks} من {ch.totalMarks}
                    </span>
                    <span>
                      الأسئلة الصحيحة: {ch.correctCount}/{ch.questionCount}
                    </span>
                  </div>
                </div>

                <div className="w-full h-2 bg-obsidian-950 rounded-full overflow-hidden border border-slate-800">
                  <div
                    className={`h-full rounded-full transition-all duration-500 ${
                      isMastered ? 'bg-bioemerald' : isMid ? 'bg-bioteal' : 'bg-biotrap-500'
                    }`}
                    style={{ width: `${ch.percentage}%` }}
                  />
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Actionable Remediation Tips */}
      {evaluation.remediationAdviceArabic.length > 0 && (
        <div className="rounded-2xl bg-biotrap-500/10 border border-biotrap-500/30 p-5 shadow-lg">
          <h4 className="text-sm font-extrabold text-biotrap-500 mb-2 flex items-center gap-2">
            <Award className="w-4 h-4" />
            <span>توصيات الذكاء الاصطناعي والموجه الوزاري لرفع درجتك:</span>
          </h4>
          <ul className="space-y-1.5 text-xs sm:text-sm text-slate-200">
            {evaluation.remediationAdviceArabic.map((tip, idx) => (
              <li key={idx} className="flex items-start gap-2">
                <span className="text-biotrap-500 font-bold">•</span>
                <span>{tip}</span>
              </li>
            ))}
          </ul>
        </div>
      )}

      {/* Detailed Question Review Matrix */}
      <div className="rounded-2xl bg-obsidian-800/90 border border-slate-700/80 p-5 sm:p-6 shadow-xl">
        <div className="flex flex-wrap items-center justify-between gap-3 mb-4 pb-3 border-b border-slate-700/60">
          <div>
            <h3 className="text-base font-extrabold text-white flex items-center gap-2">
              <BookOpen className="w-5 h-5 text-bioteal" />
              <span>مراجعة الأسئلة وتفكيك الفخاخ</span>
            </h3>
            <p className="text-xs text-slate-400">
              انقر على أي سؤال للاطلاع على الشرح الوزاري الكامل
            </p>
          </div>

          {/* Filter options */}
          <div className="flex items-center gap-1 bg-obsidian-900 p-1 rounded-xl border border-slate-800 text-xs font-bold">
            <button
              onClick={() => setFilterMode('all')}
              className={`px-3 py-1 rounded-lg transition-colors ${
                filterMode === 'all'
                  ? 'bg-bioteal text-obsidian-950 font-extrabold'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              الكل ({questions.length})
            </button>
            <button
              onClick={() => setFilterMode('mistakes')}
              className={`px-3 py-1 rounded-lg transition-colors ${
                filterMode === 'mistakes'
                  ? 'bg-red-500 text-white font-extrabold'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              الأخطاء ({evaluation.incorrectCount})
            </button>
            <button
              onClick={() => setFilterMode('correct')}
              className={`px-3 py-1 rounded-lg transition-colors ${
                filterMode === 'correct'
                  ? 'bg-bioemerald text-obsidian-950 font-extrabold'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              الصحيحة ({evaluation.correctCount})
            </button>
          </div>
        </div>

        {/* Questions Grid */}
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-3">
          {filteredQuestions.map((q) => {
            const isCorrect = answers[q.id]?.selectedOption === q.correctOptionId;

            return (
              <button
                key={q.id}
                onClick={() => setSelectedQuestionForReview(q)}
                className={`p-3 rounded-xl border text-right transition-all flex flex-col justify-between hover:scale-[1.02] ${
                  isCorrect
                    ? 'bg-bioemerald/10 border-bioemerald/40 hover:bg-bioemerald/20'
                    : 'bg-red-500/10 border-red-500/40 hover:bg-red-500/20'
                }`}
              >
                <div className="flex items-center justify-between gap-1 mb-1.5">
                  <span className="text-xs font-bold text-slate-300">سؤال {q.questionNumber}</span>
                  {isCorrect ? (
                    <CheckCircle className="w-4 h-4 text-bioemerald" />
                  ) : (
                    <XCircle className="w-4 h-4 text-red-400" />
                  )}
                </div>

                <div className="text-[11px] font-semibold text-slate-300 truncate">
                  {q.topicTitleArabic}
                </div>

                <div className="mt-2 text-[10px] text-bioteal flex items-center justify-between font-bold">
                  <span>{q.marks === 2 ? 'درجتان' : 'درجة'}</span>
                  <span className="underline">عرض الشرح</span>
                </div>
              </button>
            );
          })}
        </div>
      </div>

      {/* Bottom Actions */}
      <div className="flex flex-wrap items-center justify-between gap-4 pt-4">
        <button
          onClick={onBackToSelector}
          className="px-6 py-2.5 rounded-xl text-xs sm:text-sm font-bold bg-obsidian-800 text-slate-200 border border-slate-700 hover:bg-obsidian-700 transition-colors"
        >
          العودة لقائمة الامتحانات
        </button>

        <button
          onClick={onRetakeExam}
          className="flex items-center gap-2 px-6 py-2.5 rounded-xl text-xs sm:text-sm font-extrabold bg-gradient-to-r from-bioteal to-bioemerald text-obsidian-950 hover:brightness-110 shadow-lg shadow-bioteal/20 transition-all"
        >
          <RefreshCw className="w-4 h-4" />
          <span>إعادة تدريب على هذا النموذج</span>
        </button>
      </div>

      {/* Review Modal */}
      {selectedQuestionForReview && (
        <MistakeReviewModal
          question={selectedQuestionForReview}
          answerRecord={answers[selectedQuestionForReview.id]}
          onClose={() => setSelectedQuestionForReview(null)}
        />
      )}
    </div>
  );
};
