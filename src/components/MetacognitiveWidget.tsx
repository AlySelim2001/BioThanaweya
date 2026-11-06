import React from 'react';
import { Compass, CheckCircle2, AlertTriangle, AlertCircle, Sparkles } from 'lucide-react';
import { MetacognitiveAnalysis, MetacognitiveGapType } from '../types';

interface MetacognitiveWidgetProps {
  confidencePercent: number;
  onConfidenceChange: (val: number) => void;
  analysis?: MetacognitiveAnalysis | null;
  hasAnswered: boolean;
}

export function evaluateMetacognition(confidence: number, isCorrect: boolean): MetacognitiveAnalysis {
  let gapType: MetacognitiveGapType;
  let feedbackTitle = '';
  let actionableAdvice = '';

  if (confidence >= 70 && isCorrect) {
    gapType = 'CALIBRATED_MASTERY';
    feedbackTitle = 'Calibrated Mastery (إتقان متزن وموثوق)';
    actionableAdvice =
      'Outstanding! Your subjective confidence perfectly aligns with your objective mark scheme accuracy.';
  } else if (confidence >= 70 && !isCorrect) {
    gapType = 'OVERCONFIDENCE_BIAS';
    feedbackTitle = 'Overconfidence Bias / Dunning-Kruger (فخ الثقة الزائفة)';
    actionableAdvice =
      'Warning: High confidence was held despite omitting critical Ministry keywords. In Thanaweya Amma, familiarity with the general concept can disguise omitted technical terms.';
  } else if (confidence < 70 && isCorrect) {
    gapType = 'UNDERCONFIDENCE_DOUBT';
    feedbackTitle = 'Underconfidence Doubt (تردد وشك غير مبرر)';
    actionableAdvice =
      'Good result! You scored full credit but doubted yourself. Trust your biological reasoning more firmly.';
  } else {
    gapType = 'CALIBRATED_HUMILITY';
    feedbackTitle = 'Calibrated Humility (وعي سليم بنقاط الضعف)';
    actionableAdvice =
      'You recognized this was a challenging topic. Review the model answer and repeat this card in FSRS review.';
  }

  return {
    confidencePercent: confidence,
    isCorrect,
    gapType,
    feedbackTitle,
    actionableAdvice,
  };
}

export const MetacognitiveWidget: React.FC<MetacognitiveWidgetProps> = ({
  confidencePercent,
  onConfidenceChange,
  analysis,
  hasAnswered,
}) => {
  return (
    <div className="rounded-xl border border-indigo-500/20 bg-slate-900/60 p-4 space-y-3">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Compass className="w-4 h-4 text-indigo-400" />
          <span className="text-xs font-bold text-slate-200">
            Agent 5: Metacognitive Confidence Calibration
          </span>
        </div>
        <span className="text-xs font-extrabold text-indigo-300">
          {confidencePercent}% Confidence
        </span>
      </div>

      {!hasAnswered ? (
        <div>
          <p className="text-[11px] text-slate-400 mb-2">
            Rate your genuine confidence BEFORE viewing the examiner mark scheme:
          </p>
          <div className="flex items-center gap-3">
            <input
              type="range"
              min="10"
              max="100"
              step="10"
              value={confidencePercent}
              onChange={(e) => onConfidenceChange(Number(e.target.value))}
              className="w-full h-1.5 bg-slate-700 rounded-lg appearance-none cursor-pointer accent-indigo-400"
            />
          </div>
          <div className="flex justify-between text-[10px] text-slate-500 mt-1 font-mono">
            <span>10% (Guessing)</span>
            <span>50% (Uncertain)</span>
            <span>100% (Certain)</span>
          </div>
        </div>
      ) : analysis ? (
        <div
          className={`p-3 rounded-lg border text-xs ${
            analysis.gapType === 'CALIBRATED_MASTERY'
              ? 'bg-emerald-950/30 border-emerald-500/30 text-emerald-200'
              : analysis.gapType === 'OVERCONFIDENCE_BIAS'
              ? 'bg-red-950/30 border-red-500/30 text-rose-200'
              : analysis.gapType === 'UNDERCONFIDENCE_DOUBT'
              ? 'bg-cyan-950/30 border-cyan-500/30 text-cyan-200'
              : 'bg-amber-950/30 border-amber-500/30 text-amber-200'
          }`}
        >
          <div className="flex items-center gap-2 font-bold mb-1">
            {analysis.gapType === 'CALIBRATED_MASTERY' ? (
              <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0" />
            ) : analysis.gapType === 'OVERCONFIDENCE_BIAS' ? (
              <AlertTriangle className="w-4 h-4 text-rose-400 shrink-0" />
            ) : analysis.gapType === 'UNDERCONFIDENCE_DOUBT' ? (
              <Sparkles className="w-4 h-4 text-cyan-400 shrink-0" />
            ) : (
              <AlertCircle className="w-4 h-4 text-amber-400 shrink-0" />
            )}
            <span>{analysis.feedbackTitle}</span>
          </div>
          <p className="text-[11px] leading-relaxed text-slate-300">
            {analysis.actionableAdvice}
          </p>
        </div>
      ) : null}
    </div>
  );
};
