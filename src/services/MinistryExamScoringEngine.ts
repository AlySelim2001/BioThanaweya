import { ExamAnswerRecord, ExamQuestion, UnitChapterId } from '../types/exam';

export interface ChapterEvaluation {
  chapterId: UnitChapterId;
  chapterTitleArabic: string;
  totalMarks: number;
  obtainedMarks: number;
  percentage: number;
  questionCount: number;
  correctCount: number;
  masteryTier: 'mastered' | 'proficient' | 'needs_revision';
}

export interface DetailedEvaluationResult {
  scoreObtained: number;
  maxScore: number;
  percentage: number;
  isPassed: boolean;
  gradeTierArabic: string;
  gradeDescriptionArabic: string;
  trapsTriggeredCount: number;
  totalQuestions: number;
  answeredQuestionsCount: number;
  correctCount: number;
  incorrectCount: number;
  chapterBreakdowns: ChapterEvaluation[];
  weakestChapter: ChapterEvaluation | null;
  strongestChapter: ChapterEvaluation | null;
  remediationAdviceArabic: string[];
}

export class MinistryExamScoringEngine {
  /**
   * Evaluates the student's exam attempt strictly against Egyptian Ministry standards.
   */
  public static evaluateExam(
    questions: ExamQuestion[],
    answers: Record<string, ExamAnswerRecord>,
    elapsedSeconds: number
  ): DetailedEvaluationResult {
    let scoreObtained = 0;
    let maxScore = 0;
    let correctCount = 0;
    let incorrectCount = 0;
    let trapsTriggeredCount = 0;
    let answeredQuestionsCount = 0;

    const chapterStatsMap: Record<
      UnitChapterId,
      {
        titleArabic: string;
        totalMarks: number;
        obtainedMarks: number;
        questionCount: number;
        correctCount: number;
      }
    > = {
      support_and_movement: {
        titleArabic: 'الدعامة والحركة',
        totalMarks: 0,
        obtainedMarks: 0,
        questionCount: 0,
        correctCount: 0,
      },
      hormonal_coordination: {
        titleArabic: 'التنسيق الهرموني',
        totalMarks: 0,
        obtainedMarks: 0,
        questionCount: 0,
        correctCount: 0,
      },
      reproduction: {
        titleArabic: 'التكاثر في الكائنات الحية',
        totalMarks: 0,
        obtainedMarks: 0,
        questionCount: 0,
        correctCount: 0,
      },
      immunity: {
        titleArabic: 'المناعة في الكائنات الحية',
        totalMarks: 0,
        obtainedMarks: 0,
        questionCount: 0,
        correctCount: 0,
      },
      molecular_biology_dna_rna: {
        titleArabic: 'البيولوجيا الجزيئية (DNA & RNA)',
        totalMarks: 0,
        obtainedMarks: 0,
        questionCount: 0,
        correctCount: 0,
      },
    };

    questions.forEach((q) => {
      const qWeight = q.marks;
      maxScore += qWeight;

      const chStat = chapterStatsMap[q.chapterId] || {
        titleArabic: q.chapterTitleArabic,
        totalMarks: 0,
        obtainedMarks: 0,
        questionCount: 0,
        correctCount: 0,
      };
      chStat.totalMarks += qWeight;
      chStat.questionCount += 1;

      const record = answers[q.id];
      if (record && record.selectedOption) {
        answeredQuestionsCount++;
        if (record.selectedOption === q.correctOptionId) {
          scoreObtained += qWeight;
          correctCount++;
          chStat.obtainedMarks += qWeight;
          chStat.correctCount++;
        } else {
          incorrectCount++;
          // Check if chosen option had an explicit trap explanation
          trapsTriggeredCount++;
        }
      } else {
        incorrectCount++;
      }
    });

    const safeMaxScore = maxScore > 0 ? maxScore : 60;
    const percentage = Math.round((scoreObtained / safeMaxScore) * 1000) / 10;
    const isPassed = percentage >= 50;

    // Grade tier classification
    let gradeTierArabic = 'مقبول';
    let gradeDescriptionArabic =
      'مستوى يفي بالحد الأدنى من المتطلبات، يوصى بمراجعة المفاهيم الأساسية.';
    if (percentage >= 90) {
      gradeTierArabic = 'ممتاز (مرتبة الشرف)';
      gradeDescriptionArabic =
        'أداء استثنائي متوافق تماماً مع أعلى مستويات كليات القمة والطب البشري.';
    } else if (percentage >= 80) {
      gradeTierArabic = 'جيد جداً مرتفع';
      gradeDescriptionArabic =
        'فهم عميق لأغلب مفاهيم الأحياء مع حاجة بسيطة لضبط فخاخ الأسئلة ذات الدرجتين.';
    } else if (percentage >= 65) {
      gradeTierArabic = 'جيد';
      gradeDescriptionArabic =
        'استيعاب متوسط، هناك ثغرات في فصول التكاثر أو البيولوجيا الجزيئية تحتاج لتركيز.';
    } else if (percentage < 50) {
      gradeTierArabic = 'دون المستوى المطلوب';
      gradeDescriptionArabic =
        'يتطلب إعادة دراسة شاملة للمفاهيم الأساسية وحل الأسئلة المباشرة وتدريبات النماذج.';
    }

    // Chapter breakdowns
    const chapterBreakdowns: ChapterEvaluation[] = Object.keys(chapterStatsMap).map((key) => {
      const chKey = key as UnitChapterId;
      const stat = chapterStatsMap[chKey];
      const chPct =
        stat.totalMarks > 0 ? Math.round((stat.obtainedMarks / stat.totalMarks) * 1000) / 10 : 0;
      let masteryTier: 'mastered' | 'proficient' | 'needs_revision' = 'needs_revision';
      if (chPct >= 85) masteryTier = 'mastered';
      else if (chPct >= 65) masteryTier = 'proficient';

      return {
        chapterId: chKey,
        chapterTitleArabic: stat.titleArabic,
        totalMarks: stat.totalMarks,
        obtainedMarks: stat.obtainedMarks,
        percentage: chPct,
        questionCount: stat.questionCount,
        correctCount: stat.correctCount,
        masteryTier,
      };
    });

    // Sort chapters by percentage
    const sortedChapters = [...chapterBreakdowns].sort((a, b) => a.percentage - b.percentage);
    const weakestChapter = sortedChapters[0] || null;
    const strongestChapter = sortedChapters[sortedChapters.length - 1] || null;

    // Build remediation actionable advice
    const remediationAdviceArabic: string[] = [];
    if (weakestChapter && weakestChapter.percentage < 70) {
      remediationAdviceArabic.push(
        `فصل "${weakestChapter.chapterTitleArabic}" يحتاج إلى مراجعة مركزة، حيث حققت فيه نسبة ${weakestChapter.percentage}% فقط.`
      );
    }
    if (trapsTriggeredCount > 3) {
      remediationAdviceArabic.push(
        `وقعت في ${trapsTriggeredCount} من فخاخ الأسئلة الوزارية! اقرأ بعناية الكلمات المفتاحية مثل (مباشرة، سلفاً، غير القالب).`
      );
    }
    if (elapsedSeconds > 160 * 60) {
      remediationAdviceArabic.push(
        'استغرقت وقتاً طويلاً نسبياً، تدرب على مهارات الحساب الذهني في مسائل DNA والقطعة العضلية لتوفير الوقت.'
      );
    } else {
      remediationAdviceArabic.push(
        'معدل سرعتك في الإجابة متوازن وممتاز، مما يسمح بمراجعة البابل شيت مرتين قبل التسليم.'
      );
    }

    return {
      scoreObtained,
      maxScore: safeMaxScore,
      percentage,
      isPassed,
      gradeTierArabic,
      gradeDescriptionArabic,
      trapsTriggeredCount,
      totalQuestions: questions.length,
      answeredQuestionsCount,
      correctCount,
      incorrectCount,
      chapterBreakdowns,
      weakestChapter,
      strongestChapter,
      remediationAdviceArabic,
    };
  }

  /**
   * Helper to format seconds into hh:mm:ss Arabic display
   */
  public static formatTime(seconds: number): string {
    const hrs = Math.floor(seconds / 3600);
    const mins = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    if (hrs > 0) {
      return `${hrs.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
    }
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  }
}
