import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Dna, ArrowRight, RotateCcw, CheckCircle2, ShieldAlert, Sparkles, Layers } from 'lucide-react';
import { haptics } from '../../services/haptics';

export const DnaReplicationSimulation: React.FC = () => {
  // Step sequence:
  // 0: Quiescent Parent DNA (Hydrogen bonds intact)
  // 1: Helicase unwinding fork (breaking H-bonds)
  // 2: RNA Primase placing RNA primer on leading & lagging templates
  // 3: DNA Polymerase synthesizing leading strand continuously & Okazaki Fragment #1
  // 4: DNA Polymerase synthesizing Okazaki Fragment #2 on lagging strand
  // 5: DNA Ligase sealing phosphodiester backbones into continuous duplexes
  const [step, setStep] = useState<number>(0);

  const stepsMetadata = [
    {
      title: 'Quiescent Parent Double Helix',
      arabicTitle: 'جزيء الـ DNA الأبوي المزدوج',
      desc: 'Antiparallel complementary strands held together by weak hydrogen bonds (A=T double, G≡C triple).',
      activeEnzyme: 'Resting Duplex',
      activeColor: 'slate',
    },
    {
      title: 'Helicase Unwinding (إنزيم اللولب)',
      arabicTitle: 'كسر الروابط الهيدروجينية وفتح الشوكة',
      desc: 'Helicase advances along the helix, severing hydrogen bonds between complementary base pairs to expose template strands.',
      activeEnzyme: 'Helicase (إنزيم اللولب)',
      activeColor: 'amber',
    },
    {
      title: 'RNA Primase Primer Synthesis (إنزيم البادئ)',
      arabicTitle: 'تكوين بادئ الـ RNA',
      desc: 'RNA Primase lays down a short RNA primer providing the free 3\'-OH group strictly required by DNA Polymerase.',
      activeEnzyme: 'RNA Primase (إنزيم البادئ)',
      activeColor: 'pink',
    },
    {
      title: 'Leading Strand & Okazaki Fragment #1',
      arabicTitle: 'بناء الشريط القائد وقطعة أوكازاكي الأولى',
      desc: 'DNA Polymerase synthesizes continuously 5\' → 3\' towards the fork on the 3\' → 5\' template. On the lagging template, Okazaki Fragment #1 is synthesized in reverse.',
      activeEnzyme: 'DNA Polymerase (إنزيم بلمرة DNA)',
      activeColor: 'teal',
    },
    {
      title: 'Lagging Strand: Okazaki Fragment #2',
      arabicTitle: 'بناء قطعة أوكازاكي الثانية على الشريط المتخلف',
      desc: 'As the fork continues unwinding, DNA Polymerase initiates another discontinuous segment (Okazaki Fragment #2) away from the fork.',
      activeEnzyme: 'DNA Polymerase (إنزيم بلمرة DNA)',
      activeColor: 'teal',
    },
    {
      title: 'DNA Ligase Sealing (إنزيم الربط)',
      arabicTitle: 'ربط قطع أوكازاكي بالروابط التساهمية',
      desc: 'DNA Ligase seals nick gaps by forming covalent phosphodiester bonds, completing continuous lagging strand replication.',
      activeEnzyme: 'DNA Ligase (إنزيم الربط)',
      activeColor: 'indigo',
    },
  ];

  const handleAdvanceStep = () => {
    if (step < 5) {
      setStep(step + 1);
      haptics.pop();
    } else {
      setStep(0);
      haptics.celebrate();
    }
  };

  const handleReset = () => {
    setStep(0);
    haptics.pop();
  };

  return (
    <div className="space-y-6">
      {/* Simulation Navigation Bar */}
      <div className="flex flex-wrap items-center justify-between gap-3 bg-slate-900/90 border border-slate-800 p-4 rounded-2xl">
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 rounded-xl bg-teal-500/15 border border-teal-500/30 flex items-center justify-center text-teal-400 font-bold font-mono text-sm">
            {step + 1}/6
          </div>
          <div>
            <h4 className="text-xs md:text-sm font-bold text-slate-100 flex items-center gap-2">
              <span>{stepsMetadata[step].title}</span>
              <span className="text-[11px] text-teal-400 font-semibold font-sans hidden sm:inline">
                ({stepsMetadata[step].arabicTitle})
              </span>
            </h4>
            <span className="text-[11px] text-teal-400 font-mono">
              Enzyme: {stepsMetadata[step].activeEnzyme}
            </span>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={handleAdvanceStep}
            className="px-4 py-2 rounded-xl bg-gradient-to-r from-teal-500 to-cyan-500 hover:from-teal-400 hover:to-cyan-400 text-slate-950 text-xs font-bold flex items-center gap-1.5 transition-all shadow-lg shadow-teal-500/20"
          >
            <span>{step === 5 ? 'Reset Simulation' : 'Next Step'}</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
          <button
            onClick={handleReset}
            className="p-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-300 transition-all"
            title="Reset to beginning"
          >
            <RotateCcw className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* SVG Replication Fork Canvas with Dynamic Paths */}
      <div className="relative w-full bg-slate-950 border border-slate-800/80 rounded-2xl p-4 overflow-hidden shadow-2xl">
        <div className="flex justify-between items-center text-xs text-slate-400 mb-2 font-mono">
          <span className="flex items-center gap-1.5">
            <Dna className="w-3.5 h-3.5 text-teal-400" />
            Replication Fork Architecture (Fork Movement: ← Leftwards)
          </span>
          <span className="text-indigo-400 font-semibold">Polymerase Direction: 5' → 3' Strict</span>
        </div>

        <svg viewBox="0 0 620 280" className="w-full h-auto select-none">
          <defs>
            <marker id="arrowTeal" viewBox="0 0 10 10" refX="5" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
              <path d="M 0 0 L 10 5 L 0 10 z" fill="#14B8A6" />
            </marker>
            <marker id="arrowRose" viewBox="0 0 10 10" refX="5" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
              <path d="M 0 0 L 10 5 L 0 10 z" fill="#F43F5E" />
            </marker>
          </defs>

          {/* Top Parent Template Strand (3' Left to 5' Right) */}
          <motion.path
            animate={{
              d: step === 0 ? "M 80 110 L 540 110" : "M 80 60 L 260 60 L 370 110 L 540 110"
            }}
            transition={{ type: 'spring', stiffness: 180, damping: 20 }}
            fill="none"
            stroke="#94A3B8"
            strokeWidth="5"
            strokeLinecap="round"
          />
          <text x="60" y="55" fill="#CBD5E1" fontSize="12" fontWeight="bold">3'</text>
          <text x="550" y="115" fill="#CBD5E1" fontSize="12" fontWeight="bold">5'</text>
          <text x="90" y="45" fill="#94A3B8" fontSize="10">Leading Template Strand (3' → 5')</text>

          {/* Bottom Parent Template Strand (5' Left to 3' Right) */}
          <motion.path
            animate={{
              d: step === 0 ? "M 80 150 L 540 150" : "M 80 220 L 260 220 L 370 150 L 540 150"
            }}
            transition={{ type: 'spring', stiffness: 180, damping: 20 }}
            fill="none"
            stroke="#94A3B8"
            strokeWidth="5"
            strokeLinecap="round"
          />
          <text x="60" y="230" fill="#CBD5E1" fontSize="12" fontWeight="bold">5'</text>
          <text x="550" y="155" fill="#CBD5E1" fontSize="12" fontWeight="bold">3'</text>
          <text x="90" y="245" fill="#94A3B8" fontSize="10">Lagging Template Strand (5' → 3')</text>

          {/* Hydrogen Bonds in Unreplicated Duplex (Right Side) */}
          {[390, 420, 450, 480, 510].map((xPos, idx) => (
            <line
              key={idx}
              x1={xPos}
              y1="113"
              x2={xPos}
              y2="147"
              stroke="#64748B"
              strokeWidth="2.5"
              strokeDasharray={idx % 2 === 0 ? "2 2" : "3 1"}
            />
          ))}

          {/* Step 1+: Helicase Enzyme Animation */}
          {step >= 1 && (
            <motion.g
              initial={{ scale: 0, x: 50 }}
              animate={{ scale: 1, x: 0 }}
              transition={{ type: 'spring', stiffness: 220 }}
            >
              <polygon points="340,105 385,130 340,155" fill="#F59E0B" />
              <text x="325" y="134" fill="#0B132B" fontSize="9" fontWeight="bold">Helicase</text>
            </motion.g>
          )}

          {/* Step 2+: RNA Primers */}
          {step >= 2 && (
            <motion.g initial={{ opacity: 0 }} animate={{ opacity: 1 }}>
              {/* Leading Strand Primer */}
              <line x1="85" y1="80" x2="115" y2="80" stroke="#EC4899" strokeWidth="4" />
              <text x="85" y="95" fill="#EC4899" fontSize="9" fontWeight="bold">RNA Primer (3'-OH)</text>

              {/* Lagging Strand Primer #1 */}
              <line x1="190" y1="200" x2="210" y2="200" stroke="#EC4899" strokeWidth="4" />
              <text x="190" y="215" fill="#EC4899" fontSize="9" fontWeight="bold">Primer 1</text>
            </motion.g>
          )}

          {/* Step 3+: Continuous Leading Strand Synthesis & Okazaki #1 */}
          {step >= 3 && (
            <motion.g initial={{ opacity: 0 }} animate={{ opacity: 1 }}>
              {/* Continuous Leading Strand synthesizing towards fork */}
              <line x1="115" y1="80" x2="270" y2="80" stroke="#14B8A6" strokeWidth="4" markerEnd="url(#arrowTeal)" />
              <text x="135" y="95" fill="#2DD4BF" fontSize="10" fontWeight="bold">
                Continuous Leading Strand (5' → 3')
              </text>
              <circle cx="265" cy="80" r="9" fill="#3B82F6" />
              <text x="258" y="83" fill="#FFFFFF" fontSize="8" fontWeight="bold">Pol</text>

              {/* Okazaki Fragment #1 (Lagging Strand) */}
              <line x1="210" y1="200" x2="265" y2="200" stroke="#F43F5E" strokeWidth="4" markerEnd="url(#arrowRose)" />
              <text x="205" y="190" fill="#FDA4AF" fontSize="9" fontWeight="bold">
                Okazaki #1 (قطع أوكازاكي)
              </text>
            </motion.g>
          )}

          {/* Step 4+: Okazaki Fragment #2 */}
          {step >= 4 && (
            <motion.g initial={{ opacity: 0, x: -10 }} animate={{ opacity: 1, x: 0 }}>
              {/* Primer #2 */}
              <line x1="100" y1="200" x2="120" y2="200" stroke="#EC4899" strokeWidth="4" />
              <text x="95" y="215" fill="#EC4899" fontSize="9" fontWeight="bold">Primer 2</text>

              {/* Okazaki Fragment #2 */}
              <line x1="120" y1="200" x2="185" y2="200" stroke="#F43F5E" strokeWidth="4" markerEnd="url(#arrowRose)" />
              <text x="115" y="190" fill="#FDA4AF" fontSize="9" fontWeight="bold">
                Okazaki #2
              </text>
            </motion.g>
          )}

          {/* Step 5: DNA Ligase Sealing Phosphodiester Gaps */}
          {step >= 5 && (
            <motion.g initial={{ scale: 0 }} animate={{ scale: 1 }} transition={{ type: 'spring' }}>
              <rect x="180" y="188" width="18" height="24" rx="4" fill="#8B5CF6" />
              <text x="168" y="180" fill="#C4B5FD" fontSize="9" fontWeight="bold">DNA Ligase (إنزيم الربط)</text>
              <line x1="185" y1="200" x2="210" y2="200" stroke="#10B981" strokeWidth="4" />
            </motion.g>
          )}
        </svg>

        {/* Dynamic Context Explanation */}
        <div className="mt-3 p-3 bg-slate-900 rounded-xl border border-slate-800 text-xs text-slate-300 leading-relaxed flex items-start gap-2.5">
          <Sparkles className="w-4 h-4 text-teal-400 shrink-0 mt-0.5" />
          <div>
            <strong className="text-teal-300 font-bold">{stepsMetadata[step].title}: </strong>
            <span>{stepsMetadata[step].desc}</span>
          </div>
        </div>
      </div>

      {/* Curriculum Exam Rules Summary */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
        <div className="bg-slate-900/80 border border-slate-800 p-3.5 rounded-2xl space-y-1">
          <div className="flex items-center gap-1.5 text-xs font-bold text-teal-400">
            <CheckCircle2 className="w-4 h-4" />
            <span>Leading Strand (الشريط القائد)</span>
          </div>
          <p className="text-[11px] text-slate-300 leading-relaxed">
            Built continuously upon the parent <strong className="text-teal-200">3' → 5'</strong> template. Polymerase travels in the exact same direction as the unwinding Helicase fork. No Okazaki fragments formed.
          </p>
        </div>

        <div className="bg-slate-900/80 border border-slate-800 p-3.5 rounded-2xl space-y-1">
          <div className="flex items-center gap-1.5 text-xs font-bold text-rose-400">
            <ShieldAlert className="w-4 h-4" />
            <span>Lagging Strand (الشريط المتخلف)</span>
          </div>
          <p className="text-[11px] text-slate-300 leading-relaxed">
            Built upon the parent <strong className="text-rose-200">5' → 3'</strong> template. Synthesized discontinuously as <strong className="text-rose-300">Okazaki fragments</strong> moving in reverse away from the fork. Strictly requires <strong className="text-indigo-300">DNA Ligase</strong>.
          </p>
        </div>
      </div>
    </div>
  );
};
