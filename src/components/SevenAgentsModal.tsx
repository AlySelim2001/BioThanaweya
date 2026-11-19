import React from 'react';
import {
  X,
  Bot,
  BookOpen,
  Eye,
  Target,
  CheckCircle2,
  Compass,
  FlaskRound as Flask,
  Heart,
} from 'lucide-react';

interface SevenAgentsModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const SevenAgentsModal: React.FC<SevenAgentsModalProps> = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  const agents = [
    {
      num: 1,
      icon: BookOpen,
      color: 'from-accent-teal to-accent-emerald',
      title: 'Master Biology Educator',
      titleArabic: 'معلم الأحياء المرجعي',
      desc: "Delivers dual-layer cognitive scaffolding: deep English physiological mechanics alongside relatable Egyptian Arabic ELI5 analogies, with Bloom's taxonomy progression.",
    },
    {
      num: 2,
      icon: Eye,
      color: 'from-accent-cyan to-accent-teal',
      title: 'Visual Learning Architect',
      titleArabic: 'مهندس الإدراك البصري',
      desc: 'Builds strictly monospace LTR structural ASCII schematics, visual inspection markers for diagram identification in exams, and curated 3D animation queries.',
    },
    {
      num: 3,
      icon: Target,
      color: 'from-accent-emerald to-orange-500',
      title: 'Assessment Specialist',
      titleArabic: 'خبير التقييم والفخاخ الوزارية',
      desc: 'Curates authentic Thanaweya questions (Scientific Term, Give Reason, What Happens If, MCQ) and unmasks Ministry Exam Traps and golden rules.',
    },
    {
      num: 4,
      icon: CheckCircle2,
      color: 'from-slate-600 to-slate-500',
      title: 'Ministry Examiner & FSRS Engine',
      titleArabic: 'المصحح الوزاري وخوارزمية التكرار',
      desc: 'Performs keyword-based mark scheme grading, calculates mark deductions for missing terms, and schedules card reviews via the FSRS v4.5/v5 algorithm.',
    },
    {
      num: 5,
      icon: Compass,
      color: 'from-accent-cyan to-accent-teal',
      title: 'Metacognitive Evaluator',
      titleArabic: 'مقيم الوعي الذاتي المعرفي',
      desc: 'Analyzes student confidence against answer accuracy to detect overconfidence bias (Dunning-Kruger) and imposter syndrome doubt.',
    },
    {
      num: 6,
      icon: Flask,
      color: 'from-accent-emerald to-accent-teal',
      title: 'Problem-Based Learning Agent',
      titleArabic: 'وكيل المشروعات والمحاكاة المعملية',
      desc: 'Assigns real-world clinical toxicology and biotechnology missions with clear evaluation rubrics and scientific investigations.',
    },
    {
      num: 7,
      icon: Heart,
      color: 'from-rose-500 to-amber-500',
      title: 'Student Wellbeing & Gamification',
      titleArabic: 'مرشد الصحة النفسية والتحفيز',
      desc: 'Tracks study streaks, awards mastery badges, monitors exam anxiety scores, and structures Pomodoro study chunks for peak retention.',
    },
  ];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-in fade-in duration-200">
      <div className="relative w-full max-w-3xl max-h-[90vh] bg-secondary border border-slate-700 rounded-2xl shadow-2xl flex flex-col overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between p-5 border-b border-slate-800 bg-[#0B132B]">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl glass-card border border-accent-cyan-border flex items-center justify-center">
              <Bot className="w-6 h-6 text-accent-cyan" />
            </div>
            <div>
              <h2 className="text-lg font-bold text-white flex items-center gap-2">
                7-Agent Collaborative AI Ecosystem
              </h2>
              <p className="text-xs text-slate-400 font-arabic">
                منظومة الوكلاء الأذكياء المتخصصة لطلاب الثانوية العامة (لغات)
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 overflow-y-auto space-y-4">
          <p className="text-sm text-slate-300 leading-relaxed">
            BioThanaweya operates with seven specialized AI cognitive roles working in synergy to
            ensure complete conceptual mastery, exam trap vigilance, and official Egyptian Ministry
            mark scheme compliance.
          </p>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-3.5 pt-2">
            {agents.map((agent) => {
              const Icon = agent.icon;
              return (
                <div
                  key={agent.num}
                  className="p-3.5 rounded-xl glass-card border border-accent-cyan-border hover:border-accent-cyan-border transition flex flex-col justify-between"
                >
                  <div className="flex items-start gap-3">
                    <div
                      className={`w-8 h-8 rounded-lg bg-gradient-to-tr ${agent.color} flex items-center justify-center shrink-0 shadow-md`}
                    >
                      <Icon className="w-4 h-4 text-white" />
                    </div>
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="text-xs font-black text-slate-400">#{agent.num}</span>
                        <h3 className="text-sm font-bold text-slate-100">{agent.title}</h3>
                      </div>
                      <p className="text-xs text-accent-emerald font-arabic font-semibold">
                        {agent.titleArabic}
                      </p>
                    </div>
                  </div>
                  <p className="text-xs text-slate-400 mt-2.5 leading-relaxed">{agent.desc}</p>
                </div>
              );
            })}
          </div>
        </div>

        {/* Footer */}
        <div className="p-4 border-t border-slate-800 bg-[#0B132B] flex justify-end">
          <button
            onClick={onClose}
            className="px-5 py-2 rounded-xl btn-glass hover:text-white transition"
          >
            Close Overview
          </button>
        </div>
      </div>
    </div>
  );
};
