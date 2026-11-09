import { useState, useEffect, type FC } from 'react';
import { Award, CheckCircle, FileText, Play, Shield, Sparkles, TrendingUp } from 'lucide-react';
import { bioDb } from '../../db/bioDatabase';
import { OfficialExamPaper } from '../../types/exam';
import { useLiveExamHistory } from '../../hooks/useLiveExamHistory';

interface ExamPaperSelectorProps {
  onSelectPaper: (paperId: string) => void;
}

export const ExamPaperSelector: FC<ExamPaperSelectorProps> = ({ onSelectPaper }) => {
  const [papers, setPapers] = useState<OfficialExamPaper[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const historySummary = useLiveExamHistory();

  useEffect(() => {
    async function loadPapers() {
      try {
        await bioDb.ensurePrePopulated();
        const loaded = await bioDb.examPapers.toArray();
        setPapers(loaded);
      } catch (err) {
        console.error('Error loading papers:', err);
      } finally {
        setIsLoading(false);
      }
    }
    loadPapers();
  }, []);

  return (
    <div className="max-w-6xl mx-auto space-y-8 animate-in fade-in py-4">
      {/* Hero Welcome Banner */}
      <div className="relative rounded-3xl bg-gradient-to-r from-obsidian-950 via-obsidian-850 to-obsidian-900 border border-slate-700/80 p-6 sm:p-10 shadow-2xl overflow-hidden">
        <div className="absolute top-0 right-0 w-96 h-96 bg-bioteal/10 rounded-full blur-3xl pointer-events-none" />
        <div className="absolute bottom-0 left-0 w-96 h-96 bg-bioemerald/10 rounded-full blur-3xl pointer-events-none" />

        <div className="relative z-10 flex flex-col md:flex-row items-center justify-between gap-6">
          <div className="text-center md:text-right max-w-xl">
            <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-bioteal/10 text-bioteal text-xs font-extrabold border border-bioteal/30 mb-4 shadow-sm">
              <Sparkles className="w-4 h-4 text-bioteal" />
              <span>محاكي الثانوية العامة المعتمد للأحياء • 100% دون إنترنت</span>
            </div>
            <h1 className="text-2xl sm:text-4xl font-black text-white leading-tight mb-3">
              استعد للامتحان الوزاري بأعلى معايير الإتقان والتصحيح
            </h1>
            <p className="text-sm sm:text-base text-slate-300 leading-relaxed">
              تدرب على النماذج الرسمية المعتمدة (60 درجة) مع بابل شيت رقمي، ومؤقت زمني دقيق، وتحليل فوري لفخاخ الأسئلة والمفاهيم العلمية.
            </p>
          </div>

          {/* Quick Metrics Bento Card */}
          <div className="grid grid-cols-2 gap-3 shrink-0 w-full md:w-auto">
            <div className="bg-obsidian-900/90 border border-slate-700/80 p-4 rounded-2xl flex flex-col items-center justify-center text-center shadow-md min-w-[130px]">
              <Award className="w-6 h-6 text-bioteal mb-1" />
              <span className="text-2xl font-black text-white font-mono">
                {historySummary.highestScore}
              </span>
              <span className="text-[11px] text-slate-400 font-semibold">أعلى درجة محققة / 60</span>
            </div>

            <div className="bg-obsidian-900/90 border border-slate-700/80 p-4 rounded-2xl flex flex-col items-center justify-center text-center shadow-md min-w-[130px]">
              <TrendingUp className="w-6 h-6 text-bioemerald mb-1" />
              <span className="text-2xl font-black text-white font-mono">
                {historySummary.averageScorePercentage}%
              </span>
              <span className="text-[11px] text-slate-400 font-semibold">متوسط الدقة</span>
            </div>

            <div className="bg-obsidian-900/90 border border-slate-700/80 p-4 rounded-2xl flex flex-col items-center justify-center text-center shadow-md min-w-[130px]">
              <CheckCircle className="w-6 h-6 text-amber-400 mb-1" />
              <span className="text-2xl font-black text-white font-mono">
                {historySummary.totalAttempts}
              </span>
              <span className="text-[11px] text-slate-400 font-semibold">امتحانات مكتملة</span>
            </div>

            <div className="bg-obsidian-900/90 border border-slate-700/80 p-4 rounded-2xl flex flex-col items-center justify-center text-center shadow-md min-w-[130px]">
              <Shield className="w-6 h-6 text-cyan-400 mb-1" />
              <span className="text-2xl font-black text-white font-mono">
                {papers.length}
              </span>
              <span className="text-[11px] text-slate-400 font-semibold">نماذج وزارية</span>
            </div>
          </div>
        </div>
      </div>

      {/* Available Exam Papers Section */}
      <div>
        <div className="flex items-center justify-between gap-3 mb-5">
          <div className="flex items-center gap-2">
            <FileText className="w-5 h-5 text-bioteal" />
            <h2 className="text-lg sm:text-xl font-extrabold text-white">
              النماذج الامتحانية الوزارية المعتمدة
            </h2>
          </div>
          <span className="text-xs font-semibold text-slate-400">
            تحديث المنهج الرسمي لشهادة إتمام الثانوية العامة
          </span>
        </div>

        {isLoading ? (
          <div className="p-12 text-center text-slate-400 font-bold animate-pulse">
            جاري تحميل النماذج الامتحانية من قاعدة البيانات المحلية...
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
            {papers.map((paper) => {
              // Find matching attempt for this paper if any
              const pastAttempts = historySummary.attempts.filter((a) => a.paperId === paper.id);
              const bestAttempt = pastAttempts.reduce<number>((max, curr) => Math.max(max, curr.scoreObtained), 0);
              const hasAttempted = pastAttempts.length > 0;

              return (
                <div
                  key={paper.id}
                  className="rounded-2xl bg-obsidian-800/90 border border-slate-700/80 p-6 flex flex-col justify-between hover:border-bioteal/50 transition-all duration-200 shadow-xl group hover:shadow-2xl hover:shadow-bioteal/5"
                >
                  <div>
                    {/* Top Badges */}
                    <div className="flex items-center justify-between gap-2 mb-3">
                      <span className="text-xs font-extrabold px-3 py-1 rounded-full bg-bioteal/15 text-bioteal border border-bioteal/30">
                        {paper.sessionNameArabic}
                      </span>
                      <span className="text-xs font-bold text-slate-400 font-mono">
                        {paper.year}
                      </span>
                    </div>

                    {/* Paper Title & Subtitle */}
                    <h3 className="text-base font-extrabold text-white group-hover:text-bioteal transition-colors mb-2 leading-snug">
                      {paper.titleArabic}
                    </h3>
                    <p className="text-xs text-slate-400 line-clamp-3 leading-relaxed mb-4">
                      {paper.descriptionArabic}
                    </p>

                    {/* Specifications List */}
                    <div className="grid grid-cols-3 gap-2 bg-obsidian-900/80 p-3 rounded-xl border border-slate-800 text-center mb-4">
                      <div>
                        <span className="block text-[10px] text-slate-400 font-medium">الدرجة</span>
                        <strong className="text-xs font-bold text-slate-200 font-mono">{paper.totalMarks} درجة</strong>
                      </div>
                      <div>
                        <span className="block text-[10px] text-slate-400 font-medium">الزمن</span>
                        <strong className="text-xs font-bold text-slate-200 font-mono">{paper.durationMinutes} دقيقة</strong>
                      </div>
                      <div>
                        <span className="block text-[10px] text-slate-400 font-medium">الأسئلة</span>
                        <strong className="text-xs font-bold text-slate-200 font-mono">{paper.questionCount} سؤال</strong>
                      </div>
                    </div>
                  </div>

                  <div>
                    {/* Past Attempt Status */}
                    {hasAttempted && (
                      <div className="mb-3 px-3 py-1.5 rounded-lg bg-bioemerald/10 border border-bioemerald/30 text-bioemerald text-xs font-bold flex items-center justify-between">
                        <span>أفضل نتيجة سابقة:</span>
                        <span className="font-mono font-extrabold">{bestAttempt} / {paper.totalMarks}</span>
                      </div>
                    )}

                    {/* Start Simulation Button */}
                    <button
                      onClick={() => onSelectPaper(paper.id)}
                      className="w-full flex items-center justify-center gap-2 py-3 rounded-xl text-xs sm:text-sm font-extrabold bg-gradient-to-r from-bioteal to-bioemerald text-obsidian-950 hover:brightness-110 shadow-lg shadow-bioteal/20 transition-all"
                    >
                      <Play className="w-4 h-4 fill-obsidian-950" />
                      <span>{hasAttempted ? 'إعادة خوض الامتحان' : 'بدء محاكاة الامتحان'}</span>
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
};
