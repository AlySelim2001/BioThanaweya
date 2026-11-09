import { useLiveQuery } from 'dexie-react-hooks';
import { bioDb } from '../db/bioDatabase';
import { ExamAttempt } from '../types/exam';

export interface ExamHistorySummary {
  attempts: ExamAttempt[];
  totalAttempts: number;
  averageScorePercentage: number;
  highestScore: number;
  maxPossibleScore: number;
  totalTimeSpentSeconds: number;
  isLoading: boolean;
}

export function useLiveExamHistory(paperIdFilter?: string): ExamHistorySummary {
  const attempts = useLiveQuery(
    async () => {
      if (paperIdFilter) {
        return await bioDb.attempts
          .where('paperId')
          .equals(paperIdFilter)
          .reverse()
          .sortBy('submittedAtTimestamp');
      }
      return await bioDb.attempts
        .orderBy('submittedAtTimestamp')
        .reverse()
        .toArray();
    },
    [paperIdFilter]
  );

  if (attempts === undefined) {
    return {
      attempts: [],
      totalAttempts: 0,
      averageScorePercentage: 0,
      highestScore: 0,
      maxPossibleScore: 60,
      totalTimeSpentSeconds: 0,
      isLoading: true
    };
  }

  const totalAttempts = attempts.length;
  let totalPctSum = 0;
  let highestScore = 0;
  let totalTime = 0;

  attempts.forEach((att) => {
    totalPctSum += att.percentage;
    if (att.scoreObtained > highestScore) {
      highestScore = att.scoreObtained;
    }
    totalTime += att.elapsedSeconds;
  });

  const averageScorePercentage = totalAttempts > 0 ? Math.round((totalPctSum / totalAttempts) * 10) / 10 : 0;

  return {
    attempts,
    totalAttempts,
    averageScorePercentage,
    highestScore,
    maxPossibleScore: 60,
    totalTimeSpentSeconds: totalTime,
    isLoading: false
  };
}
