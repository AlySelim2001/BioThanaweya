import React, { useState, useEffect } from 'react';
import {
  ArrowLeft,
  BookOpen,
  Volume2,
  Square,
  Eye,
  FlaskRound as Flask,
  ExternalLink,
  Zap,
  Bookmark,
  CheckCircle,
  HelpCircle,
} from 'lucide-react';
import { getMasterStudyPack } from '../services/curriculumRepository';
import { AsciiSchematicBox } from '../components/AsciiSchematicBox';
import { MasterStudyPack } from '../types';

interface TopicDetailScreenProps {
  topicId: string;
  onBack: () => void;
  onNavigateToPractice: (topicId: string) => void;
}

export const TopicDetailScreen: React.FC<TopicDetailScreenProps> = ({
  topicId,
  onBack,
  onNavigateToPractice,
}) => {
  const [pack, setPack] = useState<MasterStudyPack>(() => getMasterStudyPack(topicId));
  const [activeTab, setActiveTab] = useState<'educator' | 'visuals' | 'pbl'>('educator');
  const [explanationMode, setExplanationMode] = useState<'technical' | 'eli5'>('technical');
  const [isSpeaking, setIsSpeaking] = useState(false);

  useEffect(() => {
    setPack(getMasterStudyPack(topicId));
  }, [topicId]);

  // Handle Text-To-Speech
  const handleToggleSpeech = () => {
    if (!('speechSynthesis' in window)) {
      alert('Text-to-speech is not supported in this browser.');
      return;
    }

    if (isSpeaking) {
      window.speechSynthesis.cancel();
      setIsSpeaking(false);
      return;
    }

    const isArabic = explanationMode === 'eli5';
    const textToSpeak = isArabic
      ? pack.arabicEli5 || pack.eli5Analogy
      : pack.deepTechnicalBreakdown.replace(/###|\*\*|\*/g, '');

    const utterance = new SpeechSynthesisUtterance(textToSpeak);
    utterance.lang = isArabic ? 'ar-SA' : 'en-US';
    utterance.rate = 0.95;

    utterance.onend = () => setIsSpeaking(false);
    utterance.onerror = () => setIsSpeaking(false);

    window.speechSynthesis.cancel();
    window.speechSynthesis.speak(utterance);
    setIsSpeaking(true);
  };

  useEffect(() => {
    return () => {
      if ('speechSynthesis' in window) {
        window.speechSynthesis.cancel();
      }
    };
  }, []);

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6 animate-in fade-in duration-300">
      {/* Top Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 pb-4 border-b border-slate-800">
        <div className="flex items-center gap-4">
          <button
            onClick={onBack}
            className="p-2 rounded-xl bg-slate-900 border border-slate-800 hover:bg-slate-800 text-slate-300 hover:text-white transition"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div>
            <div className="flex items-center gap-2">
              <span className="text-xs font-bold px-2 py-0.5 rounded-full bg-teal-500/15 text-teal-300 border border-teal-500/30">
                {pack.chapterTitle}
              </span>
              <span className="text-xs text-slate-400 font-mono">
                {pack.unit === 'UNIT_1' ? 'Unit 1' : 'Unit 2'}
              </span>
            </div>
            <h1 className="text-xl md:text-2xl font-black text-white mt-1">
              {pack.topicTitle}
            </h1>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={() => onNavigateToPractice(pack.topicId)}
            className="flex items-center gap-2 px-4 py-2 rounded-xl bg-teal-500 hover:bg-teal-400 text-slate-950 font-bold text-xs shadow-md transition"
          >
            <Zap className="w-4 h-4" />
            Practice Questions ({pack.questions.length})
          </button>
        </div>
      </div>

      {/* Official Textbook Page Citation */}
      <div className="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-slate-900/80 border border-slate-800 text-xs text-slate-300">
        <Bookmark className="w-4 h-4 text-teal-400 shrink-0" />
        <span className="font-semibold text-slate-200">Official Ministry Textbook Reference:</span>
        <span className="text-teal-300 font-mono">{pack.textbookPageReference}</span>
      </div>

      {/* Tabs */}
      <div className="flex items-center gap-2 p-1 rounded-xl bg-slate-900 border border-slate-800">
        <button
          onClick={() => setActiveTab('educator')}
          className={`flex items-center gap-2 flex-1 justify-center py-2.5 rounded-lg text-xs font-bold transition ${
            activeTab === 'educator'
              ? 'bg-teal-500 text-slate-950 shadow-sm'
              : 'text-slate-400 hover:text-white'
          }`}
        >
          <BookOpen className="w-4 h-4" />
          Educator (Deep / ELI5)
        </button>
        <button
          onClick={() => setActiveTab('visuals')}
          className={`flex items-center gap-2 flex-1 justify-center py-2.5 rounded-lg text-xs font-bold transition ${
            activeTab === 'visuals'
              ? 'bg-cyan-500 text-slate-950 shadow-sm'
              : 'text-slate-400 hover:text-white'
          }`}
        >
          <Eye className="w-4 h-4" />
          Visuals (ASCII / 3D)
        </button>
        <button
          onClick={() => setActiveTab('pbl')}
          className={`flex items-center gap-2 flex-1 justify-center py-2.5 rounded-lg text-xs font-bold transition ${
            activeTab === 'pbl'
              ? 'bg-indigo-500 text-white shadow-sm'
              : 'text-slate-400 hover:text-white'
          }`}
        >
          <Flask className="w-4 h-4" />
          Projects (PBL Lab)
        </button>
      </div>

      {/* TAB 1: Educator */}
      {activeTab === 'educator' && (
        <div className="space-y-6">
          {/* Dual Mode Switcher & TTS */}
          <div className="flex flex-col sm:flex-row items-center justify-between gap-3 p-3 rounded-2xl bg-[#0E1A35] border border-slate-800">
            <div className="flex items-center gap-2 w-full sm:w-auto">
              <button
                onClick={() => setExplanationMode('technical')}
                className={`flex-1 sm:flex-none px-4 py-2 rounded-xl text-xs font-bold transition ${
                  explanationMode === 'technical'
                    ? 'bg-purple-600 text-white shadow-md'
                    : 'bg-slate-800 text-slate-300 hover:text-white'
                }`}
              >
                🔬 English Deep Breakdown
              </button>
              <button
                onClick={() => setExplanationMode('eli5')}
                className={`flex-1 sm:flex-none px-4 py-2 rounded-xl text-xs font-bold transition font-arabic ${
                  explanationMode === 'eli5'
                    ? 'bg-teal-600 text-white shadow-md'
                    : 'bg-slate-800 text-slate-300 hover:text-white'
                }`}
              >
                🧸 Arabic ELI5 (التشبيه المصري)
              </button>
            </div>

            <button
              onClick={handleToggleSpeech}
              className={`w-full sm:w-auto flex items-center justify-center gap-2 px-4 py-2 rounded-xl text-xs font-bold transition ${
                isSpeaking
                  ? 'bg-rose-500 hover:bg-rose-400 text-white animate-pulse'
                  : 'bg-slate-800 hover:bg-slate-700 text-cyan-300 border border-cyan-500/30'
              }`}
            >
              {isSpeaking ? (
                <>
                  <Square className="w-3.5 h-3.5 fill-white" />
                  Stop Audio Narration
                </>
              ) : (
                <>
                  <Volume2 className="w-4 h-4 text-cyan-400" />
                  Listen to Audio ({explanationMode === 'eli5' ? 'Arabic' : 'English'})
                </>
              )}
            </button>
          </div>

          {/* Explanation Content Box */}
          {explanationMode === 'eli5' ? (
            <div
              dir="rtl"
              className="rounded-2xl border border-teal-500/30 bg-teal-950/20 p-6 shadow-xl space-y-4 font-arabic"
            >
              <div className="flex items-center gap-2 text-teal-300 font-bold text-sm">
                <span>🧸 تشبيه مبسط من واقع الحياة اليومية:</span>
              </div>
              <p className="text-base text-slate-100 leading-loose text-justify font-medium">
                {pack.arabicEli5 || pack.eli5Analogy}
              </p>
            </div>
          ) : (
            <div
              dir="ltr"
              className="rounded-2xl border border-purple-500/30 bg-slate-900/90 p-6 shadow-xl space-y-4"
            >
              <div className="flex items-center gap-2 text-purple-300 font-bold text-sm">
                <span>🔬 Rigorous Scientific Mechanism (Official Curriculum):</span>
              </div>
              <div className="prose prose-invert prose-sm max-w-none space-y-3 text-slate-200 leading-relaxed">
                {pack.deepTechnicalBreakdown.split('\n\n').map((paragraph, idx) => (
                  <p key={idx} className="whitespace-pre-line">
                    {paragraph}
                  </p>
                ))}
              </div>
            </div>
          )}

          {/* Key Terminology */}
          {pack.keyTerms.length > 0 && (
            <div className="space-y-3">
              <h3 className="text-sm font-bold text-white flex items-center gap-2">
                <Bookmark className="w-4 h-4 text-teal-400" />
                Key Scientific Terminology
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                {pack.keyTerms.map((term, idx) => (
                  <div
                    key={idx}
                    className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 space-y-1.5"
                  >
                    <div className="flex items-center justify-between">
                      <span className="text-sm font-bold text-teal-300">{term.term}</span>
                      {term.arabicTerm && (
                        <span className="text-xs font-bold text-purple-400 font-arabic">
                          {term.arabicTerm}
                        </span>
                      )}
                    </div>
                    <p className="text-xs text-slate-300 leading-relaxed">{term.definition}</p>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Comparison Tables */}
          {pack.comparisonTables.map((table, tIdx) => (
            <div
              key={tIdx}
              className="rounded-2xl border border-slate-800 bg-slate-900/90 overflow-hidden shadow-lg space-y-0"
            >
              <div className="px-5 py-3.5 bg-slate-800/80 border-b border-slate-700/80 font-bold text-sm text-teal-300">
                {table.title}
              </div>
              <div className="overflow-x-auto">
                <table className="w-full text-left text-xs text-slate-300">
                  <thead className="bg-slate-950 text-slate-400 font-bold uppercase tracking-wider text-[11px]">
                    <tr>
                      <th className="px-4 py-3">Criteria</th>
                      <th className="px-4 py-3 text-teal-300">{table.columnA}</th>
                      <th className="px-4 py-3 text-purple-300">{table.columnB}</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-800">
                    {table.rows.map((row, rIdx) => (
                      <tr key={rIdx} className="hover:bg-slate-800/40">
                        <td className="px-4 py-3 font-semibold text-slate-200">{row.criteria}</td>
                        <td className="px-4 py-3">{row.valueA}</td>
                        <td className="px-4 py-3">{row.valueB}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          ))}

          {/* Bloom's Progression */}
          {pack.bloomProgression.length > 0 && (
            <div className="space-y-3">
              <h3 className="text-sm font-bold text-white flex items-center gap-2">
                <CheckCircle className="w-4 h-4 text-teal-400" />
                Bloom's Taxonomy Cognitive Progression
              </h3>
              <div className="space-y-2">
                {pack.bloomProgression.map((item, idx) => (
                  <div
                    key={idx}
                    className="p-3.5 rounded-xl bg-slate-900/60 border border-slate-800 flex flex-col sm:flex-row sm:items-center justify-between gap-2"
                  >
                    <div className="flex items-center gap-3">
                      <span className="w-6 h-6 rounded-full bg-teal-500/20 text-teal-300 flex items-center justify-center text-xs font-bold shrink-0">
                        {item.levelNumber}
                      </span>
                      <div>
                        <span className="text-xs font-bold text-slate-200">{item.levelName}: </span>
                        <span className="text-xs text-slate-400">{item.objective}</span>
                      </div>
                    </div>
                    <div className="text-[11px] font-mono text-cyan-300/90 pl-9 sm:pl-0 italic">
                      "{item.masteryPrompt}"
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}

      {/* TAB 2: Visuals */}
      {activeTab === 'visuals' && (
        <div className="space-y-6">
          {/* ASCII Schematic */}
          {pack.asciiSchematic && (
            <AsciiSchematicBox
              title={`${pack.topicTitle} (ASCII Schematic)`}
              asciiContent={pack.asciiSchematic}
              caption={pack.schematicCaption}
            />
          )}

          {/* Diagram Inspection Markers */}
          {pack.visualInspectionMarkers.length > 0 && (
            <div className="rounded-2xl border border-amber-500/30 bg-amber-950/20 p-5 space-y-3">
              <h3 className="text-sm font-bold text-amber-300 flex items-center gap-2">
                <Eye className="w-4 h-4 text-amber-400" />
                Diagram Inspection Markers for Ministry Exam
              </h3>
              <ul className="space-y-2 text-xs text-slate-300">
                {pack.visualInspectionMarkers.map((marker, idx) => (
                  <li key={idx} className="flex items-start gap-2">
                    <span className="text-amber-400 font-bold shrink-0">•</span>
                    <span>{marker}</span>
                  </li>
                ))}
              </ul>
            </div>
          )}

          {/* Biological Flowchart */}
          {pack.flowcharts.length > 0 && (
            <div className="rounded-2xl border border-slate-800 bg-slate-900/80 p-5 space-y-4">
              <h3 className="text-sm font-bold text-white flex items-center gap-2">
                <Zap className="w-4 h-4 text-cyan-400" />
                Step-by-Step Biological Sequence
              </h3>
              <div className="space-y-3">
                {pack.flowcharts.map((step) => (
                  <div
                    key={step.stepNumber}
                    className="flex items-start gap-3 p-3 rounded-xl bg-slate-950/70 border border-slate-800/80"
                  >
                    <span className="w-7 h-7 rounded-lg bg-cyan-500/20 text-cyan-300 border border-cyan-500/30 flex items-center justify-center text-xs font-black shrink-0">
                      {step.stepNumber}
                    </span>
                    <div className="space-y-0.5 flex-1">
                      <div className="text-xs font-bold text-slate-200">{step.action}</div>
                      <div className="text-xs text-slate-400">{step.detail}</div>
                    </div>
                    {step.triggerOrEnzyme && (
                      <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-purple-500/15 text-purple-300 border border-purple-500/30">
                        {step.triggerOrEnzyme}
                      </span>
                    )}
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Curated 3D YouTube Animations */}
          {pack.youtube3DQueries.length > 0 && (
            <div className="space-y-3">
              <h3 className="text-sm font-bold text-white flex items-center gap-2">
                <ExternalLink className="w-4 h-4 text-red-400" />
                Curated 3D Animations & Virtual Lab Models
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                {pack.youtube3DQueries.map((v, idx) => (
                  <div
                    key={idx}
                    className="p-4 rounded-xl bg-slate-900 border border-slate-800 flex items-center justify-between gap-3"
                  >
                    <div className="space-y-1">
                      <div className="text-xs font-bold text-slate-200">{v.query}</div>
                      <div className="text-[11px] text-slate-400">{v.topicDescription}</div>
                      <span className="text-[10px] font-mono text-teal-400">
                        Duration: {v.durationRecommendation}
                      </span>
                    </div>
                    <a
                      href={`https://www.youtube.com/results?search_query=${encodeURIComponent(
                        v.query
                      )}`}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="px-3 py-1.5 rounded-lg bg-red-600 hover:bg-red-500 text-white font-bold text-xs shrink-0 transition"
                    >
                      Watch 3D
                    </a>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}

      {/* TAB 3: PBL Lab */}
      {activeTab === 'pbl' && (
        <div className="space-y-6">
          {pack.pblProject ? (
            <div className="space-y-6">
              <div className="rounded-2xl border border-emerald-500/30 bg-emerald-950/20 p-6 space-y-4">
                <div className="flex items-center gap-2 text-emerald-400 text-xs font-bold">
                  <Flask className="w-4 h-4" />
                  <span>Agent 6: Problem-Based Learning Mission</span>
                </div>
                <h3 className="text-lg font-bold text-white">{pack.pblProject.title}</h3>
                <p className="text-sm text-teal-300 font-arabic font-semibold">
                  {pack.pblProject.titleArabic}
                </p>

                <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 space-y-2">
                  <span className="text-xs font-bold text-slate-300">Clinical Scenario:</span>
                  <p className="text-xs text-slate-300 leading-relaxed">
                    {pack.pblProject.scenario}
                  </p>
                </div>

                <div className="p-4 rounded-xl bg-emerald-900/30 border border-emerald-700/40 space-y-2">
                  <span className="text-xs font-bold text-emerald-300">Target Investigation:</span>
                  <p className="text-xs text-emerald-100 leading-relaxed">
                    {pack.pblProject.mission}
                  </p>
                </div>
              </div>

              {/* Rubrics */}
              <div className="space-y-3">
                <h4 className="text-sm font-bold text-white">Evaluation Rubrics & Standards:</h4>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  {pack.pblProject.rubrics.map((r, idx) => (
                    <div
                      key={idx}
                      className="p-4 rounded-xl bg-slate-900 border border-slate-800 space-y-2 flex flex-col justify-between"
                    >
                      <div>
                        <div className="flex items-center justify-between">
                          <span className="text-xs font-bold text-teal-300">{r.criterion}</span>
                          <span className="text-xs font-mono font-extrabold text-amber-400">
                            {r.weightPoints}%
                          </span>
                        </div>
                        <div className="mt-2 text-[11px] text-emerald-300">
                          <strong>Exceeds:</strong> {r.excellentDescription}
                        </div>
                      </div>
                      <div className="text-[11px] text-rose-300 pt-2 border-t border-slate-800">
                        <strong>Revision Needed:</strong> {r.poorDescription}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          ) : (
            <div className="p-8 text-center text-slate-400 text-sm">
              Problem-based laboratory project is being compiled for this topic.
            </div>
          )}
        </div>
      )}
    </div>
  );
};
