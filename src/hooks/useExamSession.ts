import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { bioDb } from '../db/bioDatabase';
import {
  MinistryExamScoringEngine,
  type DetailedEvaluationResult,
} from '../services/MinistryExamScoringEngine';
import { PacingEfficiencyEngine, type PacingMetrics } from '../services/PacingEfficiencyEngine';
import {
  ExamAnswerRecord,
  ExamAttempt,
  ExamQuestion,
  OfficialExamPaper,
  OptionKey,
} from '../types/exam';

export interface ExamSessionState {
  paper: OfficialExamPaper | null;
  questions: ExamQuestion[];
  currentIndex: number;
  currentQuestion: ExamQuestion | null;
  answers: Record<string, ExamAnswerRecord>;
  elapsedSeconds: number;
  totalAllocatedSeconds: number;
  remainingSeconds: number;
  isPaused: boolean;
  isSubmitting: boolean;
  evaluationResult: DetailedEvaluationResult | null;
  completedAttempt: ExamAttempt | null;
  isLoading: boolean;
  error: string | null;
  pacing: PacingMetrics;
  answeredCount: number;
  flaggedCount: number;
  progressPercentage: number;
}

export function useExamSession(paperId: string) {
  const [paper, setPaper] = useState<OfficialExamPaper | null>(null);
  const [questions, setQuestions] = useState<ExamQuestion[]>([]);
  const [currentIndex, setCurrentIndex] = useState<number>(0);
  const [answers, setAnswers] = useState<Record<string, ExamAnswerRecord>>({});
  const [elapsedSeconds, setElapsedSeconds] = useState<number>(0);
  const [isPaused, setIsPaused] = useState<boolean>(false);
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);
  const [evaluationResult, setEvaluationResult] = useState<DetailedEvaluationResult | null>(null);
  const [completedAttempt, setCompletedAttempt] = useState<ExamAttempt | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const attemptIdRef = useRef<string>(`attempt_${paperId}_${Date.now()}`);
  const timerRef = useRef<number | null>(null);

  // Total time allocated (in seconds)
  const totalAllocatedSeconds = useMemo(() => {
    return (paper?.durationMinutes ?? 180) * 60;
  }, [paper]);

  // Load Paper & Questions from Local Dexie DB
  useEffect(() => {
    let isMounted = true;
    async function loadData() {
      try {
        setIsLoading(true);
        setError(null);

        await bioDb.ensurePrePopulated();

        const loadedPaper = await bioDb.examPapers.get(paperId);
        if (!isMounted) return;

        if (!loadedPaper) {
          setError('لم يتم العثور على النموذج الامتحاني المطلوب في قاعدة البيانات المحلية.');
          setIsLoading(false);
          return;
        }

        setPaper(loadedPaper);

        // Fetch questions belonging to this paper
        const loadedQuestions = await bioDb.questions
          .where('paperId')
          .equals(paperId)
          .sortBy('questionNumber');

        if (!isMounted) return;

        setQuestions(loadedQuestions);

        // Initialize empty answer records
        const initialAnswers: Record<string, ExamAnswerRecord> = {};
        loadedQuestions.forEach((q: ExamQuestion) => {
          initialAnswers[q.id] = {
            questionId: q.id,
            questionNumber: q.questionNumber,
            selectedOption: null,
            isFlaggedForReview: false,
            timeSpentSeconds: 0,
            isAnswered: false,
          };
        });
        setAnswers(initialAnswers);
      } catch (err: unknown) {
        if (isMounted) {
          setError(err instanceof Error ? err.message : 'حدث خطأ أثناء تحميل بيانات الامتحان.');
        }
      } finally {
        if (isMounted) {
          setIsLoading(false);
        }
      }
    }

    loadData();

    return () => {
      isMounted = false;
    };
  }, [paperId]);

  // Active Timer Interval
  useEffect(() => {
    if (isLoading || isPaused || isSubmitting || evaluationResult !== null) {
      if (timerRef.current) {
        clearInterval(timerRef.current);
        timerRef.current = null;
      }
      return;
    }

    timerRef.current = window.setInterval(() => {
      setElapsedSeconds((prev) => {
        const next = prev + 1;
        // Auto-submit on time expiry
        if (next >= totalAllocatedSeconds) {
          // Time expired, will trigger submit on next render
          return totalAllocatedSeconds;
        }
        return next;
      });
    }, 1000);

    return () => {
      if (timerRef.current) {
        clearInterval(timerRef.current);
      }
    };
  }, [isLoading, isPaused, isSubmitting, evaluationResult, totalAllocatedSeconds]);

  // Current question helper
  const currentQuestion = useMemo(() => {
    return questions[currentIndex] || null;
  }, [questions, currentIndex]);

  // Answered & Flagged stats
  const { answeredCount, flaggedCount } = useMemo(() => {
    let ansCount = 0;
    let flgCount = 0;
    Object.values(answers).forEach((a) => {
      if (a.isAnswered) ansCount++;
      if (a.isFlaggedForReview) flgCount++;
    });
    return { answeredCount: ansCount, flaggedCount: flgCount };
  }, [answers]);

  const progressPercentage = useMemo(() => {
    if (questions.length === 0) return 0;
    return Math.round((answeredCount / questions.length) * 100);
  }, [answeredCount, questions.length]);

  // Real-time pacing metrics
  const pacing = useMemo(() => {
    return PacingEfficiencyEngine.calculatePacing(
      elapsedSeconds,
      totalAllocatedSeconds,
      currentIndex + 1,
      questions.length
    );
  }, [elapsedSeconds, totalAllocatedSeconds, currentIndex, questions.length]);

  // Handlers
  const handleSelectOption = useCallback((questionId: string, optionId: OptionKey) => {
    setAnswers((prev) => {
      const currentRecord = prev[questionId] || {
        questionId,
        questionNumber: 0,
        selectedOption: null,
        isFlaggedForReview: false,
        timeSpentSeconds: 0,
        isAnswered: false,
      };

      const updatedRecord: ExamAnswerRecord = {
        ...currentRecord,
        selectedOption: optionId,
        isAnswered: true,
      };

      return {
        ...prev,
        [questionId]: updatedRecord,
      };
    });
  }, []);

  const handleToggleFlag = useCallback((questionId: string) => {
    setAnswers((prev) => {
      const currentRecord = prev[questionId];
      if (!currentRecord) return prev;
      return {
        ...prev,
        [questionId]: {
          ...currentRecord,
          isFlaggedForReview: !currentRecord.isFlaggedForReview,
        },
      };
    });
  }, []);

  const handleNavigate = useCallback(
    (newIndex: number) => {
      if (newIndex >= 0 && newIndex < questions.length) {
        setCurrentIndex(newIndex);
      }
    },
    [questions.length]
  );

  const handleTogglePause = useCallback(() => {
    setIsPaused((prev) => !prev);
  }, []);

  const handleSubmitExam = useCallback(async () => {
    if (isSubmitting || evaluationResult !== null || !paper) return;

    try {
      setIsSubmitting(true);
      if (timerRef.current) {
        clearInterval(timerRef.current);
      }

      // Perform grading with the scoring engine
      const evalRes = MinistryExamScoringEngine.evaluateExam(questions, answers, elapsedSeconds);
      setEvaluationResult(evalRes);

      // Create permanent attempt record for offline analytics
      const chapterBreakdownMap: ExamAttempt['chapterBreakdown'] = {
        support_and_movement: {
          totalMarks: 0,
          obtainedMarks: 0,
          percentage: 0,
          questionCount: 0,
          correctCount: 0,
        },
        hormonal_coordination: {
          totalMarks: 0,
          obtainedMarks: 0,
          percentage: 0,
          questionCount: 0,
          correctCount: 0,
        },
        reproduction: {
          totalMarks: 0,
          obtainedMarks: 0,
          percentage: 0,
          questionCount: 0,
          correctCount: 0,
        },
        immunity: {
          totalMarks: 0,
          obtainedMarks: 0,
          percentage: 0,
          questionCount: 0,
          correctCount: 0,
        },
        molecular_biology_dna_rna: {
          totalMarks: 0,
          obtainedMarks: 0,
          percentage: 0,
          questionCount: 0,
          correctCount: 0,
        },
      };

      evalRes.chapterBreakdowns.forEach((ch) => {
        chapterBreakdownMap[ch.chapterId] = {
          totalMarks: ch.totalMarks,
          obtainedMarks: ch.obtainedMarks,
          percentage: ch.percentage,
          questionCount: ch.questionCount,
          correctCount: ch.correctCount,
        };
      });

      const attemptRecord: ExamAttempt = {
        id: attemptIdRef.current,
        paperId: paper.id,
        paperTitleArabic: paper.titleArabic,
        startedAtTimestamp: Date.now() - elapsedSeconds * 1000,
        submittedAtTimestamp: Date.now(),
        elapsedSeconds,
        totalTimeAllocatedSeconds: totalAllocatedSeconds,
        isSubmitted: true,
        scoreObtained: evalRes.scoreObtained,
        maxScore: evalRes.maxScore,
        percentage: evalRes.percentage,
        passed: evalRes.isPassed,
        answers,
        chapterBreakdown: chapterBreakdownMap,
        trapsTriggeredCount: evalRes.trapsTriggeredCount,
        pacingSummary: {
          averageSecondsPerQuestion: Math.round(elapsedSeconds / (questions.length || 1)),
          idealSecondsPerQuestion: Math.round(totalAllocatedSeconds / (questions.length || 1)),
          pacingEfficiencyStatus: pacing.status,
        },
      };

      // Persist to Dexie IndexedDB
      await bioDb.attempts.put(attemptRecord);
      setCompletedAttempt(attemptRecord);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'حدث خطأ أثناء رصد وحفظ نتيجة الامتحان.');
    } finally {
      setIsSubmitting(false);
    }
  }, [
    isSubmitting,
    evaluationResult,
    paper,
    questions,
    answers,
    elapsedSeconds,
    totalAllocatedSeconds,
    pacing.status,
  ]);

  return {
    paper,
    questions,
    currentIndex,
    currentQuestion,
    answers,
    elapsedSeconds,
    totalAllocatedSeconds,
    remainingSeconds: Math.max(0, totalAllocatedSeconds - elapsedSeconds),
    isPaused,
    isSubmitting,
    evaluationResult,
    completedAttempt,
    isLoading,
    error,
    pacing,
    answeredCount,
    flaggedCount,
    progressPercentage,
    handleSelectOption,
    handleToggleFlag,
    handleNavigate,
    handleTogglePause,
    handleSubmitExam,
  };
}
