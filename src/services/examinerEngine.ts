import { ExamGradingResult, GradeStatus, MinistryExamItem } from '../types';

export const MinistryExaminerEngine = {
  gradeStudentAnswerLocally(examItem: MinistryExamItem, studentAnswer: string): ExamGradingResult {
    const cleanAnswer = studentAnswer.trim().toLowerCase();

    if (cleanAnswer.length < 5) {
      return {
        scoreAwarded: 0.0,
        maxScore: examItem.maxScore,
        detectedKeywords: [],
        missingKeywords: examItem.requiredKeywords,
        examinerFeedback:
          'The submitted answer is too brief or empty. Thanaweya Amma mark schemes require specific physiological terms, ions, enzymes, and causative reasoning.',
        modelAnswer: examItem.modelAnswer,
        gradeStatus: 'NEEDS_REVISION',
        remediationAdvice: examItem.remediationNotes,
      };
    }

    const detectedKeywords: string[] = [];
    const missingKeywords: string[] = [];

    for (const kw of examItem.requiredKeywords) {
      if (this.isKeywordPresent(cleanAnswer, kw)) {
        detectedKeywords.push(kw);
      } else {
        missingKeywords.push(kw);
      }
    }

    const totalRequired = Math.max(1, examItem.requiredKeywords.length);
    const matchRatio = detectedKeywords.length / totalRequired;

    let score = 0;
    if (matchRatio >= 0.85) {
      score = examItem.maxScore;
    } else if (matchRatio >= 0.5) {
      score = examItem.maxScore * 0.75;
    } else if (matchRatio >= 0.3) {
      score = examItem.maxScore * 0.5;
    } else if (detectedKeywords.length > 0) {
      score = examItem.maxScore * 0.25;
    }

    let gradeStatus: GradeStatus = 'NEEDS_REVISION';
    if (score >= examItem.maxScore * 0.9) {
      gradeStatus = 'FULL_MARK';
    } else if (score >= examItem.maxScore * 0.4) {
      gradeStatus = 'PARTIAL_CREDIT';
    }

    let feedback = '';
    if (gradeStatus === 'FULL_MARK') {
      feedback =
        'Excellent! High-scoring response fully adhering to the Egyptian Ministry mark scheme. You correctly integrated the core physiological mechanisms and terminology.';
    } else if (gradeStatus === 'PARTIAL_CREDIT') {
      feedback =
        'Good attempt, but missing critical Ministry mark scheme keywords. In Thanaweya Amma, examiners strictly deduct 0.5 to 1.0 mark if specific enzymes, ions, or structural names are omitted.';
    } else {
      feedback =
        'Sub-standard response. You missed essential scientific terms. Read the official model answer and focus on exact biological causality.';
    }

    if (missingKeywords.length > 0) {
      feedback += `\n\nPenalty Warning: Egyptian Ministry examiners would deduct marks for missing: ${missingKeywords.join(', ')}.`;
    }
    if (examItem.remediationNotes) {
      feedback += `\n\nRemediation Focus: ${examItem.remediationNotes}`;
    }

    return {
      scoreAwarded: Math.round(score * 10) / 10,
      maxScore: examItem.maxScore,
      detectedKeywords,
      missingKeywords,
      examinerFeedback: feedback,
      modelAnswer: examItem.modelAnswer,
      gradeStatus,
      remediationAdvice: examItem.remediationNotes,
    };
  },

  async gradeWithAIOrFallback(examItem: MinistryExamItem, studentAnswer: string): Promise<ExamGradingResult> {
    try {
      const response = await fetch('/api/examiner/grade', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          questionPrompt: examItem.prompt,
          studentAnswer,
          modelAnswer: examItem.modelAnswer,
          requiredKeywords: examItem.requiredKeywords,
          maxScore: examItem.maxScore,
        }),
      });

      if (response.ok) {
        const data = await response.json();
        if (data && !data.fallback && typeof data.scoreAwarded === 'number') {
          return {
            scoreAwarded: data.scoreAwarded,
            maxScore: examItem.maxScore,
            detectedKeywords: data.detectedKeywords || [],
            missingKeywords: data.missingKeywords || [],
            examinerFeedback: data.examinerFeedback || 'Evaluated by Senior Ministry AI Examiner.',
            modelAnswer: examItem.modelAnswer,
            gradeStatus: data.gradeStatus || 'PARTIAL_CREDIT',
            remediationAdvice: data.remediationAdvice || examItem.remediationNotes,
          };
        }
      }
    } catch {
      // Offline or network error - gracefully fall back
    }

    return this.gradeStudentAnswerLocally(examItem, studentAnswer);
  },

  isKeywordPresent(studentText: string, keyword: string): boolean {
    const kw = keyword.toLowerCase().trim();
    if (studentText.includes(kw)) return true;

    // Normalizing spaces, hyphens
    const cleanKw = kw.replace(/-/g, ' ').replace(/\s+/g, ' ');
    const cleanText = studentText.replace(/-/g, ' ').replace(/\s+/g, ' ');
    if (cleanText.includes(cleanKw)) return true;

    // Biological synonym checks
    if (kw.includes('calcium') && (studentText.includes('ca2+') || studentText.includes('ca++') || studentText.includes('ca2') || studentText.includes('ca ions'))) return true;
    if (kw.includes('acetylcholine') && (studentText.includes('ach') || studentText.includes('acetyl-choline'))) return true;
    if (kw.includes('cholinesterase') && (studentText.includes('acetylcholinesterase') || studentText.includes('ache'))) return true;
    if (kw.includes('atp') && studentText.includes('adenosine triphosphate')) return true;
    if (kw.includes('cross bridge') && (studentText.includes('cross-bridge') || studentText.includes('transverse hook') || studentText.includes('transverse hooks'))) return true;
    if (kw.includes('sarcoplasmic reticulum') && studentText.includes('sr')) return true;
    if (kw.includes('t helper') && (studentText.includes('th') || studentText.includes('cd4') || studentText.includes('cd4+'))) return true;
    if (kw.includes('cytotoxic') && (studentText.includes('tc') || studentText.includes('cd8') || studentText.includes('cd8+'))) return true;
    if (kw.includes('perforin') && studentText.includes('perforating')) return true;
    if (kw.includes('repolarization') && (studentText.includes('resting potential') || studentText.includes('polarization'))) return true;
    if (kw.includes('helicase') && (studentText.includes('unwind') || studentText.includes('unzip'))) return true;
    if (kw.includes('okazaki') && (studentText.includes('lagging') || studentText.includes('fragments'))) return true;

    const words = kw.split(' ').filter((w) => w.length > 3);
    if (words.length >= 2) {
      if (words.every((w) => studentText.includes(w))) return true;
    }

    return false;
  },
};
