import React, { useState } from 'react';
import {
  ArrowLeft,
  ArrowRight,
  Mic,
  MicOff,
  CheckCircle2,
  AlertTriangle,
  Lightbulb,
  Award,
} from 'lucide-react';
import { getMasterStudyPack } from '../services/curriculumRepository';
import { MinistryExaminerEngine } from '../services/examinerEngine';
import { MetacognitiveWidget, evaluateMetacognition } from '../components/MetacognitiveWidget';
import { PracticeQuestion, ExamGradingResult, MetacognitiveAnalysis } from '../types';

interface PracticeScreenProps {
  topicId: string;
  onBack: () => void;
  onXpEarned: (xp: number) => void;
}

export const PracticeScreen: React.FC<PracticeScreenProps> = ({ topicId, onBack, onXpEarned }) => {
  const pack = getMasterStudyPack(topicId);
  const questions: PracticeQuestion[] = pack.questions;

  const [currentIndex, setCurrentIndex] = useState(0);
  const [studentAnswer, setStudentAnswer] = useState('');
  const [selectedOption, setSelectedOption] = useState<number | null>(null);
  const [isGrading, setIsGrading] = useState(false);
  const [result, setResult] = useState<ExamGradingResult | null>(null);

  // Metacognitive state
  const [confidence, setConfidence] = useState(70);
  const [metacognitiveAnalysis, setMetacognitiveAnalysis] = useState<MetacognitiveAnalysis | null>(
    null
  );

  // Speech Recognition state
  const [isListening, setIsListening] = useState(false);

  const currentQ = questions[currentIndex] || questions[0];

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

      recognition.onresult = (event: any) => {
        const transcript = event.results[0][0].transcript;
        setStudentAnswer((prev) => (prev ? `${prev} ${transcript}` : transcript));
      };

      recognition.start();
    } catch {
      setIsListening(false);
    }
  };

  const handleGrade = async () => {
    setIsGrading(true);

    let answerToGrade = studentAnswer;
    let isCorrect = false;

    if (currentQ.type === 'MCQ' && selectedOption !== null) {
      isCorrect = selectedOption === currentQ.correctOptionIndex;
      const res: ExamGradingResult = {
        scoreAwarded: isCorrect ? 2.0 : 0.0,
        maxScore: 2.0,
        detectedKeywords: isCorrect ? [currentQ.officialAnswer] : [],
        missingKeywords: isCorrect ? [] : [currentQ.officialAnswer],
        examinerFeedback: isCorrect
          ? 'Correct! Selected the accurate option matching Egyptian Ministry mark scheme criteria.'
          : `Incorrect choice. The official answer is: ${currentQ.officialAnswer}.`,
        modelAnswer: currentQ.officialAnswer,
        gradeStatus: isCorrect ? 'FULL_MARK' : 'NEEDS_REVISION',
        remediationAdvice: currentQ.rationale,
      };
      setResult(res);
      setMetacognitiveAnalysis(evaluateMetacognition(confidence, isCorrect));
      if (isCorrect) onXpEarned(20);
      setIsGrading(false);
      return;
    }

    // For written items, build synthetic exam item and grade
    const examItem = {
      id: currentQ.id,
      questionType: currentQ.type,
      prompt: currentQ.question,
      maxScore: 2.0,
      requiredKeywords: currentQ.officialAnswer
        .toLowerCase()
        .split(/[,.;]/)
        .map((s) => s.trim())
        .filter((s) => s.length > 3),
      partialCreditKeywords: [],
      modelAnswer: currentQ.officialAnswer,
      remediationNotes: currentQ.rationale,
    };

    const gradeRes = await MinistryExaminerEngine.gradeWithAIOrFallback(examItem, answerToGrade);
    setResult(gradeRes);

    isCorrect = gradeRes.gradeStatus === 'FULL_MARK';
    setMetacognitiveAnalysis(evaluateMetacognition(confidence, isCorrect));

    if (gradeRes.scoreAwarded > 0) {
      onXpEarned(Math.round(gradeRes.scoreAwarded * 10));
    }
    setIsGrading(false);
  };

  const handleNext = () => {
    if (currentIndex + 1 < questions.length) {
      setCurrentIndex((prev) => prev + 1);
      setStudentAnswer('');
      setSelectedOption(null);
      setResult(null);
      setMetacognitiveAnalysis(null);
    }
  };

  const handlePrev = () => {
    if (currentIndex > 0) {
      setCurrentIndex((prev) => prev - 1);
      setStudentAnswer('');
      setSelectedOption(null);
      setResult(null);
      setMetacognitiveAnalysis(null);
    }
  };

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6 animate-in fade-in duration-300">
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
            <div className="flex items-center gap-2">
              <span className="text-xs font-bold text-teal-400 font-mono">{pack.topicTitle}</span>
              <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-slate-800 text-slate-400">
                Item {currentIndex + 1} of {questions.length}
              </span>
            </div>
            <h1 className="text-lg font-black text-white">Official Ministry Exam Practice</h1>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={handlePrev}
            disabled={currentIndex === 0}
            className="p-2 rounded-xl bg-slate-900 border border-slate-800 text-slate-400 disabled:opacity-30 hover:text-white transition"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <button
            onClick={handleNext}
            disabled={currentIndex === questions.length - 1}
            className="p-2 rounded-xl bg-slate-900 border border-slate-800 text-slate-400 disabled:opacity-30 hover:text-white transition"
          >
            <ArrowRight className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Question Card */}
      <div className="rounded-2xl border border-slate-800 bg-[#0E1A35]/90 p-6 space-y-4 shadow-xl">
        <div className="flex items-center justify-between">
          <span className="text-xs font-bold px-2.5 py-1 rounded-lg bg-teal-500/15 text-teal-300 border border-teal-500/30">
            {currentQ.type.replace(/_/g, ' ')}
          </span>
          <span className="text-xs font-mono text-slate-400">2 Marks</span>
        </div>

        <p className="text-base font-bold text-white leading-relaxed">{currentQ.question}</p>

        {/* MCQ Options */}
        {currentQ.type === 'MCQ' && currentQ.options && (
          <div className="space-y-2 pt-2">
            {currentQ.options.map((opt, optIdx) => (
              <button
                key={optIdx}
                onClick={() => setSelectedOption(optIdx)}
                className={`w-full p-3.5 rounded-xl text-left text-xs font-semibold border transition flex items-center justify-between ${
                  selectedOption === optIdx
                    ? 'bg-teal-500/20 border-teal-500 text-white'
                    : 'bg-slate-900/80 border-slate-800 text-slate-300 hover:bg-slate-800'
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

        {/* Free-Response Text Area with Voice Input */}
        {currentQ.type !== 'MCQ' && (
          <div className="space-y-2 pt-2">
            <div className="flex items-center justify-between text-xs text-slate-400">
              <label>Your Answer (Mark Scheme Terminology):</label>
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
                    Voice Answer
                  </>
                )}
              </button>
            </div>
            <textarea
              rows={4}
              value={studentAnswer}
              onChange={(e) => setStudentAnswer(e.target.value)}
              placeholder="Type your biological explanation using exact ions, enzymes, and mechanisms..."
              className="w-full p-4 rounded-xl bg-slate-900 border border-slate-700 text-white text-xs leading-relaxed focus:outline-none focus:border-teal-400 resize-none"
            />
          </div>
        )}

        {/* Metacognitive Calibration Slider */}
        <MetacognitiveWidget
          confidencePercent={confidence}
          onConfidenceChange={setConfidence}
          analysis={metacognitiveAnalysis}
          hasAnswered={result !== null}
        />

        {/* Grade Action Button */}
        {!result && (
          <button
            onClick={handleGrade}
            disabled={
              isGrading ||
              (currentQ.type === 'MCQ'
                ? selectedOption === null
                : studentAnswer.trim().length === 0)
            }
            className="w-full py-3 rounded-xl bg-teal-500 hover:bg-teal-400 disabled:opacity-40 text-slate-950 font-bold text-xs shadow-lg transition flex items-center justify-center gap-2"
          >
            <Award className="w-4 h-4" />
            {isGrading ? 'Grading Answer with Mark Scheme...' : 'Submit to Ministry Examiner'}
          </button>
        )}
      </div>

      {/* Examiner Grading Results */}
      {result && (
        <div
          className={`rounded-2xl border p-6 space-y-4 shadow-xl animate-in fade-in duration-200 ${
            result.gradeStatus === 'FULL_MARK'
              ? 'bg-emerald-950/20 border-emerald-500/40'
              : result.gradeStatus === 'PARTIAL_CREDIT'
                ? 'bg-amber-950/20 border-amber-500/40'
                : 'bg-rose-950/20 border-rose-500/40'
          }`}
        >
          <div className="flex items-center justify-between pb-3 border-b border-slate-800">
            <div className="flex items-center gap-2 font-bold text-sm text-white">
              <CheckCircle2
                className={`w-5 h-5 ${
                  result.gradeStatus === 'FULL_MARK'
                    ? 'text-emerald-400'
                    : result.gradeStatus === 'PARTIAL_CREDIT'
                      ? 'text-amber-400'
                      : 'text-rose-400'
                }`}
              />
              <span>Ministry Examiner Mark Scheme Report</span>
            </div>
            <span className="text-sm font-mono font-black text-white px-3 py-1 rounded-lg bg-slate-900 border border-slate-700">
              Score: {result.scoreAwarded} / {result.maxScore}
            </span>
          </div>

          <div className="space-y-1.5">
            <div className="text-xs font-bold text-slate-300">Examiner Feedback:</div>
            <p className="text-xs text-slate-300 leading-relaxed whitespace-pre-line">
              {result.examinerFeedback}
            </p>
          </div>

          {result.missingKeywords.length > 0 && (
            <div className="p-3 rounded-xl bg-rose-500/10 border border-rose-500/20 text-xs text-rose-300 space-y-1">
              <span className="font-bold flex items-center gap-1.5">
                <AlertTriangle className="w-3.5 h-3.5" />
                Ministry Penalty Deduction Warning:
              </span>
              <p>Omitted mandatory keywords: {result.missingKeywords.join(', ')}</p>
            </div>
          )}

          <div className="p-4 rounded-xl bg-slate-900/90 border border-slate-800 space-y-1.5">
            <div className="text-xs font-bold text-teal-400">Official Ministry Model Answer:</div>
            <p className="text-xs text-slate-200 leading-relaxed">{result.modelAnswer}</p>
          </div>

          <div className="flex items-center justify-end gap-3 pt-2">
            <button
              onClick={() => {
                setResult(null);
                setMetacognitiveAnalysis(null);
              }}
              className="px-4 py-2 rounded-xl text-xs font-bold bg-slate-800 text-slate-300 hover:text-white transition"
            >
              Try Again
            </button>
            {currentIndex + 1 < questions.length && (
              <button
                onClick={handleNext}
                className="px-5 py-2 rounded-xl text-xs font-bold bg-teal-500 text-slate-950 hover:bg-teal-400 transition"
              >
                Next Item
              </button>
            )}
          </div>
        </div>
      )}

      {/* Exam Traps Section */}
      {pack.examTraps.length > 0 && (
        <div className="rounded-2xl border border-amber-500/30 bg-amber-950/20 p-5 space-y-3">
          <h3 className="text-sm font-bold text-amber-300 flex items-center gap-2">
            <Lightbulb className="w-4 h-4 text-amber-400" />
            Agent 3: Egyptian Ministry Exam Traps & Misconceptions
          </h3>
          <div className="space-y-3">
            {pack.examTraps.map((trap, idx) => (
              <div
                key={idx}
                className="p-4 rounded-xl bg-slate-900/90 border border-amber-500/20 space-y-2 text-xs"
              >
                <div className="text-rose-400">
                  <strong>Common Student Misconception:</strong> {trap.misconception}
                </div>
                <div className="text-emerald-300">
                  <strong>Scientific Reality:</strong> {trap.scientificTruth}
                </div>
                <div className="text-amber-200 italic">
                  <strong>Exam Warning:</strong> {trap.examTrapWarning}
                </div>
                <div className="p-2 rounded-lg bg-amber-500/10 text-amber-300 font-bold">
                  Golden Rule: {trap.goldenRule}
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};
