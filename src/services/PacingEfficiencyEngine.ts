export interface PacingMetrics {
  elapsedSeconds: number;
  totalAllocatedSeconds: number;
  remainingSeconds: number;
  currentQuestionIndex: number;
  totalQuestions: number;
  expectedElapsedTimeForCurrentIndex: number;
  secondsDifference: number; // positive = spent more time than planned, negative = faster than planned
  status: 'ahead' | 'on_track' | 'lagging';
  statusArabic: string;
  adviceArabic: string;
  colorClass: string;
}

export class PacingEfficiencyEngine {
  /**
   * Computes pacing statistics given the current exam state.
   */
  public static calculatePacing(
    elapsedSeconds: number,
    totalAllocatedSeconds: number,
    currentQuestionIndex: number,
    totalQuestions: number
  ): PacingMetrics {
    const remainingSeconds = Math.max(0, totalAllocatedSeconds - elapsedSeconds);
    const safeTotalQuestions = totalQuestions > 0 ? totalQuestions : 46;

    // Reserve ~20 minutes (1200 seconds) for final review
    const bufferSeconds = Math.min(1200, Math.floor(totalAllocatedSeconds * 0.15));
    const activeSolvingBudget = Math.max(1, totalAllocatedSeconds - bufferSeconds);
    const idealSecondsPerQuestion = activeSolvingBudget / safeTotalQuestions;

    // Expected elapsed time when reaching current question
    const expectedElapsed = currentQuestionIndex * idealSecondsPerQuestion;
    const diff = elapsedSeconds - expectedElapsed;

    let status: 'ahead' | 'on_track' | 'lagging' = 'on_track';
    let statusArabic = 'في المسار المثالي';
    let adviceArabic = 'أنت تسير بمعدل زمني ممتاز، حافظ على هذا الإيقاع الهادئ والدقيق.';
    let colorClass = 'text-bioteal border-bioteal/30 bg-bioteal/10';

    if (diff < -300) {
      status = 'ahead';
      statusArabic = 'سابق للجدول الزمني';
      adviceArabic = 'لديك وفرة في الوقت، تأكد من قراءة كل كلمة في السؤال والخيارات بتأنٍ.';
      colorClass = 'text-bioemerald border-bioemerald/30 bg-bioemerald/10';
    } else if (diff > 300) {
      status = 'lagging';
      statusArabic = 'تأخر في الإيقاع';
      adviceArabic = 'احذر استنزاف الوقت في سؤال واحد؛ قم بتعليم السؤال للمراجعة وانتقل للتالي.';
      colorClass = 'text-biotrap-500 border-biotrap-500/30 bg-biotrap-500/10';
    }

    return {
      elapsedSeconds,
      totalAllocatedSeconds,
      remainingSeconds,
      currentQuestionIndex,
      totalQuestions: safeTotalQuestions,
      expectedElapsedTimeForCurrentIndex: Math.round(expectedElapsed),
      secondsDifference: Math.round(diff),
      status,
      statusArabic,
      adviceArabic,
      colorClass,
    };
  }
}
