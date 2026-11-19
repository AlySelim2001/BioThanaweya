import React, { useState, useEffect } from 'react';
import { ArrowLeft, CheckCircle2, Layers, RotateCw, TrendingUp } from 'lucide-react';
import { defaultFsrs } from '../services/fsrs';
import { StorageService } from '../services/storage';
import { STUDY_PACKS } from '../services/curriculumRepository';
import { FSRSCard, FSRSRating, Flashcard } from '../types';
import confetti from 'canvas-confetti';

interface FsrsReviewScreenProps {
  topicId?: string;
  onBack: () => void;
  onXpEarned: (xp: number) => void;
}

interface EnrichedCard {
  card: FSRSCard;
  content: Flashcard;
}

export const FsrsReviewScreen: React.FC<FsrsReviewScreenProps> = ({
  topicId,
  onBack,
  onXpEarned,
}) => {
  const [queue, setQueue] = useState<EnrichedCard[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isFlipped, setIsFlipped] = useState(false);
  const [reviewedCount, setReviewedCount] = useState(0);
  const [isCompleted, setIsCompleted] = useState(false);

  useEffect(() => {
    // Collect all flashcard content definitions
    const allFlashcards = Object.values(STUDY_PACKS).flatMap((p) => p.flashcards);
    const contentMap = new Map<string, Flashcard>();
    allFlashcards.forEach((fc) => contentMap.set(fc.id, fc));

    // Get persisted FSRS records
    const storedCards = StorageService.getFsrsCards(topicId);

    const enriched: EnrichedCard[] = [];
    for (const sc of storedCards) {
      const content = contentMap.get(sc.cardId);
      if (content) {
        enriched.push({ card: sc, content });
      }
    }

    if (enriched.length === 0) {
      // Fallback: take all flashcards from sarcomere_contraction
      const fallback = (STUDY_PACKS['sarcomere_contraction']?.flashcards || []).map((fc, idx) => ({
        card: {
          cardId: fc.id,
          due: Date.now() + idx * 60000,
          stability: 3.0,
          difficulty: 4.5,
          elapsedDays: 0,
          scheduledDays: 1,
          reps: 0,
          lapses: 0,
          state: 0 as const,
        },
        content: fc,
      }));
      setQueue(fallback);
    } else {
      setQueue(enriched);
    }
  }, [topicId]);

  const handleRate = (rating: FSRSRating) => {
    if (currentIndex >= queue.length) return;

    const current = queue[currentIndex];
    const scheduling = defaultFsrs.schedule(current.card, rating, new Date());

    // Update in local storage
    StorageService.updateCard(scheduling.card);

    // Award XP (10 XP for review, bonus 15 for Easy)
    const earned = rating === 4 ? 25 : 10;
    onXpEarned(earned);

    setReviewedCount((prev) => prev + 1);
    setIsFlipped(false);

    if (currentIndex + 1 < queue.length) {
      setCurrentIndex((prev) => prev + 1);
    } else {
      setIsCompleted(true);
      confetti({
        particleCount: 80,
        spread: 70,
        origin: { y: 0.6 },
      });
    }
  };

  if (isCompleted || queue.length === 0) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-16 text-center space-y-6 animate-in fade-in duration-300">
        <div className="w-16 h-16 rounded-2xl bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 mx-auto flex items-center justify-center">
          <CheckCircle2 className="w-8 h-8" />
        </div>
        <div className="space-y-2">
          <h2 className="text-2xl font-black text-white">Review Session Complete!</h2>
          <p className="text-sm text-slate-300">
            The FSRS-v5 algorithm has updated your memory stability and scheduled optimal future
            review intervals for long-term Thanaweya exam retention.
          </p>
        </div>
        <div className="p-4 rounded-xl bg-slate-900 border border-slate-800 text-xs text-slate-400">
          Reviewed: <strong className="text-teal-400">{reviewedCount} flashcards</strong> • Earned:{' '}
          <strong className="text-amber-400">+{reviewedCount * 15} XP</strong>
        </div>
        <button
          onClick={onBack}
          className="px-6 py-2.5 rounded-xl glass-card hover:bg-accent-cyan-border text-slate-950 font-bold text-sm transition"
        >
          Return to Dashboard
        </button>
      </div>
    );
  }

  const current = queue[currentIndex];
  const progressPercent = Math.round(((currentIndex + 1) / queue.length) * 100);
  const retrievability = defaultFsrs.currentRetrievability(current.card);

  return (
    <div className="max-w-2xl mx-auto px-4 sm:px-6 py-8 space-y-6 animate-in fade-in duration-300">
      {/* Header */}
      <div className="flex items-center justify-between pb-4 border-b border-slate-800">
        <div className="flex items-center gap-3">
          <button
            onClick={onBack}
            className="p-2 rounded-xl bg-slate-900 border border-slate-800 hover:bg-slate-800 text-slate-300 hover:text-white transition"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div>
            <h1 className="text-lg font-bold text-white flex items-center gap-2">
              <Layers className="w-5 h-5 text-cyan-400" />
              FSRS Spaced Memory Review
            </h1>
            <p className="text-[11px] text-slate-400">
              Agent 4 • Free Spaced Repetition Scheduler v5 Standard
            </p>
          </div>
        </div>

        <span className="text-xs font-mono font-bold text-teal-400">
          Card {currentIndex + 1} / {queue.length}
        </span>
      </div>

      {/* Progress Bar */}
      <div className="space-y-1">
        <div className="h-1.5 w-full bg-slate-800 rounded-full overflow-hidden">
          <div
            className="h-full bg-gradient-to-r from-teal-400 to-cyan-400 transition-all duration-300"
            style={{ width: `${progressPercent}%` }}
          />
        </div>
        <div className="flex justify-between text-[10px] text-slate-500 font-mono">
          <span>{current.content.category.toUpperCase()}</span>
          <span>{progressPercent}% Complete</span>
        </div>
      </div>

      {/* 3D Flip Flashcard */}
      <div
        onClick={() => setIsFlipped(!isFlipped)}
        className="relative min-h-[260px] rounded-3xl border border-slate-700 bg-gradient-to-b from-slate-900 to-[#0A1128] p-8 flex flex-col justify-between cursor-pointer hover:border-cyan-500/50 shadow-2xl transition group"
      >
        <div className="flex items-center justify-between">
          <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-cyan-500/10 text-cyan-300 border border-cyan-500/20">
            {isFlipped ? 'MODEL ANSWER' : 'ACTIVE RECALL PROMPT'}
          </span>
          <span className="flex items-center gap-1 text-xs text-slate-400 group-hover:text-cyan-300 transition">
            <RotateCw className="w-3.5 h-3.5" />
            Tap to Flip
          </span>
        </div>

        <div className="py-6 text-center space-y-2">
          <p className="text-base sm:text-lg font-bold text-white leading-relaxed">
            {isFlipped ? current.content.backAnswer : current.content.frontPrompt}
          </p>
          {isFlipped && (
            <p className="text-xs text-teal-400 font-medium">
              Rate your recall difficulty below to calibrate FSRS memory weights
            </p>
          )}
        </div>

        <div className="flex items-center justify-between text-[11px] text-slate-500 font-mono pt-3 border-t border-slate-800/80">
          <span>Reps: {current.card.reps}</span>
          <span className="flex items-center gap-1">
            <TrendingUp className="w-3.5 h-3.5 text-teal-400" />
            Retrievability: {Math.round(retrievability * 100)}%
          </span>
        </div>
      </div>

      {/* Rating Buttons */}
      <div className="space-y-2 pt-2">
        <div className="text-xs font-bold text-slate-400 text-center">Rate Recall Difficulty:</div>
        <div className="grid grid-cols-4 gap-2.5">
          <button
            onClick={() => handleRate(1)}
            className="flex flex-col items-center justify-center py-2.5 px-1 rounded-xl glass-card border border-accent-cyan-border hover:bg-accent-emerald text-accent-emerald text-xs font-bold transition"
          >
            <span className="text-sm font-black">Again</span>
            <span className="text-[10px] text-rose-300/80">Forgot</span>
          </button>

          <button
            onClick={() => handleRate(2)}
            className="flex flex-col items-center justify-center py-2.5 px-1 rounded-xl glass-card border border-accent-emerald-border hover:bg-accent-emerald text-amber-300 text-xs font-bold transition"
          >
            <span className="text-sm font-black">Hard</span>
            <span className="text-[10px] text-amber-300/80">Struggled</span>
          </button>

          <button
            onClick={() => handleRate(3)}
            className="flex flex-col items-center justify-center py-2.5 px-1 rounded-xl bg-teal-500/15 hover:bg-teal-500/25 border border-teal-500/30 text-teal-300 text-xs font-bold transition"
          >
            <span className="text-sm font-black">Good</span>
            <span className="text-[10px] text-teal-300/80">Recalled</span>
          </button>

          <button
            onClick={() => handleRate(4)}
            className="flex flex-col items-center justify-center py-2.5 px-1 rounded-xl bg-emerald-500/15 hover:bg-emerald-500/25 border border-emerald-500/30 text-emerald-300 text-xs font-bold transition"
          >
            <span className="text-sm font-black">Easy</span>
            <span className="text-[10px] text-emerald-300/80">Instant</span>
          </button>
        </div>
      </div>
    </div>
  );
};
