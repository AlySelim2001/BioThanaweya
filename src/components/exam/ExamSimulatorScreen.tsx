import { useEffect, type FC } from 'react';
import { useExamSession } from '../../hooks/useExamSession';
import { DigitalBubbleSheet } from './DigitalBubbleSheet';
import { ExamHeader } from './ExamHeader';
import { ExamResultAnalytics } from './ExamResultAnalytics';
import { QuestionCard } from './QuestionCard';
import { AlertTriangle, Loader2 } from 'lucide-react';

interface ExamSimulatorScreenProps {
  paperId: string;
  onBackToSelector: () => void;
}

export const ExamSimulatorScreen: FC<ExamSimulatorScreenProps> = ({
  paperId,
  onBackToSelector
}) => {
  const {
    paper,
    questions,
    currentIndex,
    currentQuestion,
    answers,
    elapsedSeconds,
    remainingSeconds,
    isPaused,
    pacing,
    answeredCount,
    flaggedCount,
    evaluationResult,
    isLoading,
    error,
    handleSelectOption,
    handleToggleFlag,
    handleNavigate,
    handleTogglePause,
    handleSubmitExam
  } = useExamSession(paperId);

  // Keyboard navigation shortcuts
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      // Arrow Right = Previous in RTL
      if (e.key === 'ArrowRight') {
        if (currentIndex > 0) handleNavigate(currentIndex - 1);
      }
      // Arrow Left = Next in RTL
      if (e.key === 'ArrowLeft') {
        if (currentIndex < questions.length - 1) handleNavigate(currentIndex + 1);
      }
      // Keys 1, 2, 3, 4 for options A, B, C, D
      if (currentQuestion) {
        if (e.key === '1' || e.key === 'a' || e.key === 'A') handleSelectOption(currentQuestion.id, 'A');
        if (e.key === '2' || e.key === 'b' || e.key === 'B') handleSelectOption(currentQuestion.id, 'B');
        if (e.key === '3' || e.key === 'c' || e.key === 'C') handleSelectOption(currentQuestion.id, 'C');
        if (e.key === '4' || e.key === 'd' || e.key === 'D') handleSelectOption(currentQuestion.id, 'D');
        if (e.key === 'f' || e.key === 'F') handleToggleFlag(currentQuestion.id);
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [currentIndex, questions.length, currentQuestion, handleNavigate, handleSelectOption, handleToggleFlag]);

  if (isLoading) {
    return (
      <div className="min-h-[70vh] flex flex-col items-center justify-center gap-3">
        <Loader2 className="w-10 h-10 text-bioteal animate-spin" />
        <p className="text-sm font-bold text-slate-300">
          جاري إعداد ورقة الامتحان والأسئلة الوزارية دون إنترنت...
        </p>
      </div>
    );
  }

  if (error || !paper || questions.length === 0) {
    return (
      <div className="max-w-md mx-auto my-12 p-6 rounded-2xl bg-obsidian-800 border border-red-500/40 text-center space-y-4">
        <AlertTriangle className="w-10 h-10 text-red-400 mx-auto" />
        <h3 className="text-lg font-bold text-white">تعذر تحميل الامتحان</h3>
        <p className="text-xs text-slate-300">{error || 'لا توجد أسئلة مسجلة لهذا النموذج.'}</p>
        <button
          onClick={onBackToSelector}
          className="px-5 py-2 rounded-xl text-xs font-bold bg-obsidian-900 text-white border border-slate-700 hover:bg-obsidian-700"
        >
          العودة لقائمة النماذج
        </button>
      </div>
    );
  }

  // If already submitted and evaluated, render the detailed report card
  if (evaluationResult) {
    return (
      <ExamResultAnalytics
        paper={paper}
        questions={questions}
        answers={answers}
        evaluation={evaluationResult}
        elapsedSeconds={elapsedSeconds}
        onRetakeExam={() => window.location.reload()}
        onBackToSelector={onBackToSelector}
      />
    );
  }

  return (
    <div className="space-y-4 pb-12 animate-in fade-in">
      {/* Sticky Exam Progress & Countdown Header */}
      <ExamHeader
        paperTitle={paper.titleArabic}
        currentIndex={currentIndex}
        totalQuestions={questions.length}
        currentMarks={currentQuestion?.marks ?? 1}
        elapsedSeconds={elapsedSeconds}
        remainingSeconds={remainingSeconds}
        isPaused={isPaused}
        pacing={pacing}
        answeredCount={answeredCount}
        flaggedCount={flaggedCount}
        onTogglePause={handleTogglePause}
        onSubmitExam={handleSubmitExam}
      />

      {/* Main Split Grid: Question View on Left, Digital Bubble Sheet on Right */}
      <div className="max-w-7xl mx-auto px-4 grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
        {/* Active Question View (8 Columns on desktop) */}
        <div className="lg:col-span-8">
          {currentQuestion && (
            <QuestionCard
              question={currentQuestion}
              answerRecord={answers[currentQuestion.id]}
              totalQuestions={questions.length}
              currentIndex={currentIndex}
              onSelectOption={(optId) => handleSelectOption(currentQuestion.id, optId)}
              onToggleFlag={() => handleToggleFlag(currentQuestion.id)}
              onPrevious={() => currentIndex > 0 && handleNavigate(currentIndex - 1)}
              onNext={() => currentIndex < questions.length - 1 && handleNavigate(currentIndex + 1)}
            />
          )}
        </div>

        {/* Digital Bubble Sheet Sidebar (4 Columns on desktop) */}
        <div className="lg:col-span-4 sticky top-[150px]">
          <DigitalBubbleSheet
            questions={questions}
            answers={answers}
            currentIndex={currentIndex}
            onSelectIndex={handleNavigate}
          />
        </div>
      </div>
    </div>
  );
};
