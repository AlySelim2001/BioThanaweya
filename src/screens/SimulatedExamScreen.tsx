import React, { useState, useEffect } from 'react';
import {
  ArrowLeft,
  Clock,
  Flag,
  Award,
  ChevronRight,
  ChevronLeft,
  Mic,
  MicOff,
} from 'lucide-react';
import { getSimulatedMinistryExamPool } from '../services/curriculumRepository';
import { MinistryExaminerEngine } from '../services/examinerEngine';
import { StorageService } from '../services/storage';
import { PracticeQuestion, ExamQuestionSubmission, ExamResultReport } from '../types';
import confetti from 'canvas-confetti';

interface SimulatedExamScreenProps {
  onBack: () => void;
  onXpEarned: (xp: number) => void;
}

export const SimulatedExamScreen: React.FC<SimulatedExamScreenProps> = ({ onBack, onXpEarned }) => {
  const [questions] = useState<PracticeQuestion[]>(() => getSimulatedMinistryExamPool());
  const [currentIndex, setCurrentIndex] = useState(0);

  // 45 minutes = 2700 seconds
  const [timeLeft, setTimeLeft] = useState(45 * 60);
  const [answers, setAnswers] = useState<Record<number, string>>({});
  const [selectedOptions, setSelectedOptions] = useState<Record<number, number>>({});
  const [flagged, setFlagged] = useState<Record<number, boolean>>({});

  const [isSubmitted, setIsSubmitted] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [examReport, setExamReport] = useState<ExamResultReport | null>(null);

  // Speech Recognition state
  const [isListening, setIsListening] = useState(false);

  // Timer Countdown
  useEffect(() => {
    if (isSubmitted) return;
    const interval = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(interval);
          handleSubmitExam();
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(interval);
  }, [isSubmitted]);

  const formatTime = (secs: number) => {
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  };

  const handleToggleVoice = () => {
    const SpeechRecognition =
      (window as any).SpeechRecognition || (window as any).webkitSpeechRecognition;

    if (!SpeechRecognition) {
      alert('Speech recognition is not supported in this browser. Please type your answer.');
      return;
    }

    if (isListening) {
      setIsListening(false);
      return;
    }

    try {
      const recognition = new SpeechRecognition();
      recognition.lang = 'en-US';
      recognition.continuous = false;
      recognition.interimResults = false;

      recognition.onstart = () => setIsListening(true);
      recognition.onend = () => setIsListening(false);
      recognition.onerror = () => setIsListening(false);

      recognition.onresult = (event: unknown) => {
        const results = event as { results: { [index: number]: { [index: number]: { transcript: string } } } };
        const transcript = results.results[0][0].transcript;
        setAnswers((prev) => ({
          ...prev,
          [currentIndex]: prev[currentIndex] ? `${prev[currentIndex]} ${transcript}` : transcript,
        }));
      };

      recognition.start();
    } catch {
      setIsListening(false);
    }
  };

  const handleSubmitExam = async () => {
    if (isSubmitting) return;
    setIsSubmitting(true);

    const submissions: ExamQuestionSubmission[] = [];
    let totalScore = 0;
    const maxScore = questions.length * 2.0;

    for (let i = 0; i < questions.length; i++) {
      const q = questions[i];
      if (q.type === 'MCQ') {
        const chosen = selectedOptions[i];
        const isCorrect = chosen !== undefined && chosen === q.correctOptionIndex;
        const score = isCorrect ? 2.0 : 0.0;
        totalScore += score;
        submissions.push({
          questionId: q.id,
          question: q,
          studentAnswer: chosen !== undefined ? q.options?.[chosen] || '' : 'No answer',
          selectedOptionIndex: chosen,
          scoreAwarded: score,
          maxScore: 2.0,
          feedback: isCorrect
            ? 'Correct choice according to Ministry mark scheme.'
            : `Incorrect choice. Expected: ${q.officialAnswer}`,
          modelAnswer: q.officialAnswer,
        });
      } else {
        const studentText = answers[i] || '';
        const syntheticExamItem = {
          id: q.id,
          questionType: q.type,
          prompt: q.question,
          maxScore: 2.0,
          requiredKeywords: q.officialAnswer
            .toLowerCase()
            .split(/[,.;]/)
            .map((s) => s.trim())
            .filter((s) => s.length > 3),
          partialCreditKeywords: [],
          modelAnswer: q.officialAnswer,
          remediationNotes: q.rationale,
        };
        const res = MinistryExaminerEngine.gradeStudentAnswerLocally(
          syntheticExamItem,
          studentText
        );
        totalScore += res.scoreAwarded;
        submissions.push({
          questionId: q.id,
          question: q,
          studentAnswer: studentText || 'No answer submitted',
          scoreAwarded: res.scoreAwarded,
          maxScore: 2.0,
          feedback: res.examinerFeedback,
          modelAnswer: q.officialAnswer,
          detectedKeywords: res.detectedKeywords,
          missingKeywords: res.missingKeywords,
        });
      }
    }

    const unit1Questions = questions.filter((q) => (q as PracticeQuestion & { chapterId: string }).chapterId?.startsWith('unit1')).length;
        const unit2Questions = questions.filter((q) => (q as PracticeQuestion & { chapterId: string }).chapterId?.startsWith('unit2')).length;

        let unit1Score = 0;
        let unit2Score = 0;

        for (let i = 0; i < questions.length; i++) {
          const q = questions[i];
          const chapterId = (q as PracticeQuestion & { chapterId: string }).chapterId;
          if (chapterId?.startsWith('unit1')) {
            unit1Score += (selectedOptions?.[i] !== undefined && selectedOptions?.[i] === q.correctOptionIndex) ? 2.0 : 0.0;
          } else if (chapterId?.startsWith('unit2')) {
            unit2Score += (selectedOptions?.[i] !== undefined && selectedOptions?.[i] === q.correctOptionIndex) ? 2.0 : 0.0;
          }
        }

        const unit1Total = unit1Questions * 2.0;
        const unit2Total = unit2Questions * 2.0;

        const unit1Percentage = unit1Total > 0 ? Math.round((unit1Score / unit1Total) * 10) / 10 : 0;
        const unit2Percentage = unit2Total > 0 ? Math.round((unit2Score / unit2Total) * 10) / 10 : 0;

    const percentage = Math.round((totalScore / maxScore) * 100);
    let letterGrade = 'F';
    if (percentage >= 95) letterGrade = 'A*';
    else if (percentage >= 85) letterGrade = 'A';
    else if (percentage >= 75) letterGrade = 'B';
    else if (percentage >= 65) letterGrade = 'C';
    else if (percentage >= 50) letterGrade = 'D';

    const report: ExamResultReport = {
      totalScore: Math.round(totalScore * 10) / 10,
      maxScore,
      percentage,
      letterGrade,
      passed: percentage >= 50,
      timeSpentSeconds: 45 * 60 - timeLeft,
      chapterBreakdown: {
          'Unit 1: Structure & Function': unit1Percentage,
          'Unit 2: Molecular Biology': unit2Percentage,
        },
      questionsReviewed: submissions,
    };

    StorageService.saveExamReport(report);
    setExamReport(report);
    setIsSubmitted(true);
    setIsSubmitting(false);

    onXpEarned(Math.round(totalScore * 15));

    if (percentage >= 70) {
      confetti({
        particleCount: 100,
        spread: 80,
        origin: { y: 0.6 },
      });
    }
  };

  if (isSubmitted && examReport) {
    return (
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6 animate-in fade-in duration-300">
        <div className="rounded-3xl border border-teal-500/30 bg-gradient-to-br from-[#071329] to-[#0E2045] p-8 text-center space-y-4 shadow-2xl">
          <div className="w-16 h-16 rounded-2xl glass-card text-accent-cyan border border-accent-cyan-border mx-auto flex items-center justify-center">
            <Award className="w-8 h-8" />
          </div>

          <div>
            <span className="text-xs font-bold px-3 py-1 rounded-full bg-teal-500/10 text-teal-300 border border-teal-500/20">
              OFFICIAL MINISTRY SIMULATION RESULT
            </span>
            <h1 className="text-3xl font-black text-white mt-2">
              Grade: {examReport.letterGrade} ({examReport.percentage}%)
            </h1>
            <p className="text-xs text-slate-200> mt-1">
              Total Score: {examReport.totalScore} / {examReport.maxScore} Marks • Time Spent:{' '}
              {formatTime(examReport.timeSpentSeconds)}
            </p>
          </div>

          <div className="flex justify-center gap-4 pt-2">
            <button
              onClick={onBack}
              className="px-6 py-2.5 rounded-xl glass-card hover:bg-accent-cyan-border text-white font-bold text-xs transition"
            >
              Back to Dashboard
            </button>
          </div>
        </div>

        {/* Detailed Question Review */}
        <div className="space-y-4">
          <h2 className="text-base font-bold text-white">Item-by-Item Mark Scheme Breakdown:</h2>
          {examReport.questionsReviewed.map((sub, idx) => (
            <div
              key={idx}
              className={`rounded-2xl border p-5 space-y-3 ${
                sub.scoreAwarded === sub.maxScore
                  ? 'bg-slate-800/40 backdrop-blur-md border border-white/10 shadow-xl border-emerald-500/30'
                  : sub.scoreAwarded > 0
                    ? 'glass-card border border-accent-emerald-border'
                    : 'bg-slate-800/40 backdrop-blur-md border border-white/10 shadow-xl border-rose-500/30'
              }`}
            >
              <div className="flex items-center justify-between text-xs">
                <span className="font-bold text-slate-200>">
                  Item #{idx + 1} ({sub.question.type})
                </span>
                <span className="font-mono font-bold text-white px-2 py-0.5 rounded bg-slate-800">
                  {sub.scoreAwarded} / {sub.maxScore} Marks
                </span>
              </div>

              <p className="text-xs font-bold text-white">{sub.question.question}</p>

              <div className="p-3 rounded-xl bg-slate-950 text-xs space-y-1">
                <span className="text-slate-200> font-semibold">Your Answer:</span>
                <p className="text-slate-200">{sub.studentAnswer}</p>
              </div>

              <div className="p-3 rounded-xl glass-card border border-accent-cyan-border text-xs space-y-1">
                <span className="text-teal-400 font-semibold">Official Model Answer:</span>
                <p className="text-slate-200">{sub.modelAnswer}</p>
              </div>

              <p className="text-xs text-slate-200> whitespace-pre-line leading-relaxed">
                {sub.feedback}
              </p>
            </div>
          ))}
        </div>
      </div>
    );
  }

  const currentQ = questions[currentIndex];
  const answeredCount = Object.keys(answers).length + Object.keys(selectedOptions).length;

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6 animate-in fade-in duration-300">
      {/* Timer and Top Status */}
      <div className="flex items-center justify-between p-4 rounded-2xl bg-slate-900 border border-slate-800 shadow-md">
        <div className="flex items-center gap-3">
          <button
            onClick={onBack}
            className="p-1.5 rounded-lg text-slate-200> hover:text-white transition"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div>
            <h1 className="text-sm font-bold text-white">Egyptian Ministry Biology Simulation</h1>
            <p className="text-[11px] text-slate-200>">
              Answered: {answeredCount} / {questions.length} Items
            </p>
          </div>
        </div>

        {/* 45-min Clock */}
        <div
          className={`flex items-center gap-2 px-3 py-1.5 rounded-xl font-mono text-sm font-black border ${
            timeLeft < 300
              ? 'text-accent-cyan border border-accent-cyan-border hover:bg-accent-emerald text-accent-emerald animate-pulse'
              : 'bg-slate-800 text-teal-300 border-slate-700'
          }`}
        >
          <Clock className="w-4 h-4" />
          <span>{formatTime(timeLeft)}</span>
        </div>
      </div>

      {/* Question Palette */}
      <div className="p-3.5 rounded-2xl bg-[#0E1A35]/80 border border-slate-800 flex items-center gap-1.5 overflow-x-auto">
        {questions.map((_, idx) => {
          const isAnswered = answers[idx] !== undefined || selectedOptions[idx] !== undefined;
          const isFlag = flagged[idx];
          const isCurrent = currentIndex === idx;

          return (
            <button
              key={idx}
              onClick={() => setCurrentIndex(idx)}
              className={`w-8 h-8 rounded-lg text-xs font-bold shrink-0 transition flex items-center justify-center relative ${
                isCurrent
                  ? 'glass-card text-slate-950 ring-2 ring-accent-cyan-border'
                  : isAnswered
                    ? 'glass-card text-accent-cyan border border-accent-cyan-border'
                    : 'bg-slate-800 text-slate-200> hover:text-white'
              }`}
            >
              {idx + 1}
              {isFlag && (
                <span className="absolute -top-1 -right-1 w-2.5 h-2.5 rounded-full bg-amber-400" />
              )}
            </button>
          );
        })}
      </div>

      {/* Active Question Box */}
      <div className="rounded-2xl border border-slate-800 bg-[#0E1A35]/90 p-6 space-y-4 shadow-xl">
        <div className="flex items-center justify-between">
          <span className="text-xs font-bold px-2.5 py-1 rounded-lg glass-card text-accent-cyan border border-accent-cyan-border">
            {currentQ.type.replace(/_/g, ' ')}
          </span>

          <button
            onClick={() => setFlagged((prev) => ({ ...prev, [currentIndex]: !prev[currentIndex] }))}
            className={`flex items-center gap-1 text-xs px-2.5 py-1 rounded-lg border transition ${
              flagged[currentIndex]
                ? 'glass-card text-accent-emerald border border-accent-emerald-border'
                : 'bg-slate-800 text-slate-200> border-slate-700 hover:text-white'
            }`}
          >
            <Flag className="w-3.5 h-3.5" />
            <span>{flagged[currentIndex] ? 'Flagged' : 'Flag'}</span>
          </button>
        </div>

        <p className="text-base font-bold text-white leading-relaxed">{currentQ.question}</p>

        {/* MCQ Mode */}
        {currentQ.type === 'MCQ' && currentQ.options && (
          <div className="space-y-2 pt-2">
            {currentQ.options.map((opt, optIdx) => (
              <button
                key={optIdx}
                onClick={() => setSelectedOptions((prev) => ({ ...prev, [currentIndex]: optIdx }))}
                className={`w-full p-3.5 rounded-xl text-left text-xs font-semibold border transition flex items-center justify-between ${
                  selectedOptions[currentIndex] === optIdx
                    ? 'glass-card border border-accent-cyan-border text-white'
                    : 'bg-slate-800/40 backdrop-blur-md border border-white/10 shadow-xl text-slate-200> hover:bg-slate-800'
                }`}
              >
                <span>{opt}</span>
                <span className="w-5 h-5 rounded-full border border-slate-600 flex items-center justify-center text-[10px]">
                  {String.fromCharCode(65 + optIdx)}
                </span>
              </button>
            ))}
          </div>
        )}

        {/* Free-Response with Voice Input */}
        {currentQ.type !== 'MCQ' && (
          <div className="space-y-2 pt-2">
            <div className="flex items-center justify-between text-xs text-slate-200>">
              <label>Your Examination Answer:</label>
              <button
                type="button"
                onClick={handleToggleVoice}
                className={`flex items-center gap-1.5 px-2.5 py-1 rounded-lg text-xs font-bold transition ${
                  isListening
                    ? 'bg-rose-500 text-white animate-pulse'
                    : 'bg-slate-800 hover:bg-slate-700 text-teal-300'
                }`}
              >
                {isListening ? (
                  <>
                    <MicOff className="w-3.5 h-3.5" />
                    Listening...
                  </>
                ) : (
                  <>
                    <Mic className="w-3.5 h-3.5 text-teal-400" />
                    Voice Dictate
                  </>
                )}
              </button>
            </div>
            <textarea
              rows={4}
              value={answers[currentIndex] || ''}
              onChange={(e) => setAnswers((prev) => ({ ...prev, [currentIndex]: e.target.value }))}
              placeholder="State your answer with proper physiological causality and keywords..."
              className="w-full p-4 rounded-xl bg-slate-900 border border-slate-700 text-white text-xs leading-relaxed focus:outline-none focus:border-teal-400 resize-none"
            />
          </div>
        )}

        {/* Navigation Row */}
        <div className="flex items-center justify-between pt-4 border-t border-slate-800">
          <button
            onClick={() => setCurrentIndex((prev) => Math.max(0, prev - 1))}
            disabled={currentIndex === 0}
            className="flex items-center gap-1 px-4 py-2 rounded-xl bg-slate-800 text-slate-200> disabled:opacity-40 text-xs font-bold hover:text-white transition"
          >
            <ChevronLeft className="w-4 h-4" />
            Previous
          </button>

          {currentIndex === questions.length - 1 ? (
            <button
              onClick={handleSubmitExam}
              disabled={isSubmitting}
              className="flex items-center gap-2 px-6 py-2 rounded-xl bg-rose-600 hover:bg-rose-500 text-white text-xs font-extrabold shadow-lg shadow-rose-600/30 transition"
            >
              <Award className="w-4 h-4" />
              {isSubmitting ? 'Grading Exam...' : 'Finish & Submit Exam'}
            </button>
          ) : (
            <button
              onClick={() => setCurrentIndex((prev) => Math.min(questions.length - 1, prev + 1))}
              className="flex items-center gap-1 px-4 py-2 rounded-xl bg-teal-500 text-white text-xs font-bold hover:bg-teal-400 transition"
            >
              Next Item
              <ChevronRight className="w-4 h-4" />
            </button>
          )}
        </div>
      </div>
    </div>
  );
};
