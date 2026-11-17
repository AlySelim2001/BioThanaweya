import { describe, it, expect } from 'vitest';
import { PacingEfficiencyEngine } from '../PacingEfficiencyEngine';

describe('PacingEfficiencyEngine', () => {
  const totalAllocatedSeconds = 180 * 60; // 10,800 seconds (3 hours)
  const totalQuestions = 46;

  it('identifies on-track pacing correctly', () => {
    // Roughly question 10 at ~2,000 seconds
    const pacing = PacingEfficiencyEngine.calculatePacing(
      2000,
      totalAllocatedSeconds,
      10,
      totalQuestions
    );
    expect(pacing.status).toBe('on_track');
    expect(pacing.statusArabic).toBe('في المسار المثالي');
    expect(pacing.remainingSeconds).toBe(8800);
  });

  it('detects ahead of schedule status when solving faster than expected', () => {
    // Question 20 at only 1,000 seconds (very fast)
    const pacing = PacingEfficiencyEngine.calculatePacing(
      1000,
      totalAllocatedSeconds,
      20,
      totalQuestions
    );
    expect(pacing.status).toBe('ahead');
    expect(pacing.statusArabic).toBe('سابق للجدول الزمني');
  });

  it('triggers lagging warning when taking significantly longer than allocated', () => {
    // Only at question 3 after 3,000 seconds (falling behind)
    const pacing = PacingEfficiencyEngine.calculatePacing(
      3000,
      totalAllocatedSeconds,
      3,
      totalQuestions
    );
    expect(pacing.status).toBe('lagging');
    expect(pacing.statusArabic).toBe('تأخر في الإيقاع');
  });
});
