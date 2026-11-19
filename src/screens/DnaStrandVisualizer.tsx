import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  Dna, 
  ArrowRight, 
  RotateCcw, 
  CheckCircle2, 
  ShieldAlert, 
  Sparkles,
  Layers,
  Heart,
  Palette
} from 'lucide-react';
import { useDnaReplication } from '../hooks/useDnaReplication';
import { NitrogenousBase } from '../types/dna.types';

const ERROR_BOUNDARY_FALLBACK = () => (
  <div className="p-6 glass-card border border-accent-cyan-border rounded-2xl text-center">
    <ShieldAlert className="w-12 h-12 mx-auto text-rose-400 mb-3" />
    <h3 className="text-slate-100">DNA Simulation</h3>
    <p className="text-slate-300">Loading DNA replication visualization...</p>
  </div>
);

export const DnaStrandVisualizer: React.FC = () => {
  const {
    currentStep,
    setCurrentStep,
    currentBase,
    setCurrentBase,
    complementaryBase,
    hydrogenBondCount,
    enzymeState,
    strandDirection,
    simulateStep,
    resetSimulation,
    validateChargaffRule,
  } = useDnaReplication();

  const stepsMetadata = [
    {
      id: 0,
      title: 'Quiescent Parent Double Helix',
      arabicTitle: 'جزيء الـ DNA الأبوي المزدوج',
      desc: 'Antiparallel complementary strands held together by weak hydrogen bonds (A=T double, G≡C triple).',
      enzyme: 'Resting Duplex',
      color: 'slate',
    },
    {
      id: 1,
      title: 'Helicase Unwinding',
      arabicTitle: 'كسر الروابط الهيدروجينية وفتح الشوكة',
      desc: 'Helicase advances along the helix, severing hydrogen bonds between complementary base pairs to expose template strands.',
      enzyme: 'Helicase',
      color: 'amber',
    },
    {
      id: 2,
      title: 'RNA Primer Synthesis',
      arabicTitle: 'تكوين بادئ الـ RNA',
      desc: "RNA Primase lays down a short RNA primer providing the free 3'-OH group strictly required by DNA Polymerase.",
      enzyme: 'RNA Primase',
      color: 'pink',
    },
    {
      id: 3,
      title: 'Leading Strand Synthesis',
      arabicTitle: 'بناء الشريط القائد',
      desc: 'DNA Polymerase synthesizes continuously towards the fork. No Okazaki fragments formed.',
      enzyme: 'DNA Polymerase',
      color: 'teal',
    },
    {
      id: 4,
      title: 'Lagging Strand: Okazaki Fragments',
      arabicTitle: 'بناء قطع أوكازاكي',
      desc: 'Synthesized discontinuously as Okazaki fragments moving in reverse away from the fork. Requires DNA Ligase.',
      enzyme: 'DNA Ligase',
      color: 'indigo',
    },
  ];

  // Chargaff rule validation state
  const [aPercent, setAPercent] = useState('');
  const [tPercent, setTPercent] = useState('');
  const [gPercent, setGPercent] = useState('');
  const [cPercent, setCPercents] = useState('');
  const [chargaffValid, setChargaffValid] = useState<boolean | null>(null);

  // Handle Chargaff validation
  const handleChargaffValidate = () => {
    const a = parseFloat(aPercent) || 0;
    const t = parseFloat(tPercent) || 0;
    const g = parseFloat(gPercent) || 0;
    const c = parseFloat(cPercent) || 0;
    setChargaffValid(validateChargaffRule(a, t, g, c));
  };

  return (
    <div className="space-y-6">
      {/* Error Boundary Wrapper */}
      <div className="glass-card border border-accent-cyan-border rounded-2xl p-6">
        <ERROR_BOUNDARY_FALLBACK />
      </div>

      {/* Simulation Controls */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="glass-card border border-accent-cyan-border rounded-2xl p-4">
          <h4 className="text-sm font-bold text-teal-300 mb-3">DNA Simulation Controls</h4>
          
          <div className="space-y-3">
            <button
              onClick={() => simulateStep(0)}
              className="px-4 py-2 rounded-xl glass-card border border-accent-cyan-border hover:bg-accent-cyan text-slate-950 text-xs font-bold transition-all"
              disabled={currentStep === 0}
            >
              Reset to Step 0: Quiescent Parent DNA
            </button>
            
            <button
              onClick={() => simulateStep(1)}
              className="px-4 py-2 rounded-xl glass-card border border-accent-emerald-border hover:bg-accent-emerald text-slate-950 text-xs font-bold transition-all shadow-lg shadow-emerald-500/20"
              disabled={currentStep >= 1}
            >
              Step 1: Helicase Unwinding
            </button>
            
            <button
              onClick={() => simulateStep(2)}
              className="px-4 py-2 rounded-xl glass-card border border-teal-500/30 hover:bg-teal-500/25 border border-teal-500/30 text-slate-950 text-xs font-bold transition-all shadow-lg shadow-teal-500/20"
              disabled={currentStep >= 2}
            >
              Step 2: RNA Primer Synthesis
            </button>
            
            <button
              onClick={() => simulateStep(3)}
              className="px-4 py-2 rounded-xl glass-card border border-accent-cyan-border hover:bg-accent-cyan-border text-slate-950 text-xs font-bold transition-all shadow-lg shadow-teal-500/20"
              disabled={currentStep >= 3}
            >
              Step 3: Leading Strand Synthesis
            </button>
            
            <button
              onClick={() => simulateStep(4)}
              className="px-4 py-2 rounded-xl glass-card border border-indigo-500/30 hover:bg-indigo-500/25 border border-indigo-500/30 text-slate-950 text-xs font-bold transition-all shadow-lg shadow-indigo-500/20"
              disabled={currentStep >= 4}
            >
              Step 4: Lagging Strand Okazaki Fragments
            </button>
            
            <button
              onClick={() => simulateStep(5)}
              className="px-4 py-2 rounded-xl glass-card border border-purple-500/30 hover:bg-purple-500/25 border border-purple-500/30 text-slate-950 text-xs font-bold transition-all shadow-lg shadow-purple-500/20"
              disabled={currentStep >= 5}
            >
              Step 5: DNA Ligase Sealing
            </button>
          </div>
        </div>

        <div className="bg-slate-900/80 border border-slate-800 p-4 rounded-2xl">
          <h4 className="text-sm font-bold text-teal-300 mb-3">Chargaff's Rules Validation</h4>
          
          <div className="space-y-3">
            <div>
              <label className="text-slate-300 text-xs font-medium">% Adenine (A): </label>
              <input
                type="number"
                value={aPercent}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setAPercent(e.target.value)}
                className="w-full bg-slate-800 border border-slate-600 rounded px-3 py-2 text-slate-100 focus:outline-none focus:border-teal-400"
              />
            </div>
            
            <div>
              <label className="text-slate-300 text-xs font-medium">% Thymine (T): </label>
              <input
                type="number"
                value={tPercent}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setTPercent(e.target.value)}
                className="w-full bg-slate-800 border border-slate-600 rounded px-3 py-2 text-slate-100 focus:outline-none focus:border-teal-400"
              />
            </div>
            
            <div>
              <label className="text-slate-300 text-xs font-medium">% Guanine (G): </label>
              <input
                type="number"
                value={gPercent}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setGPercent(e.target.value)}
                className="w-full bg-slate-800 border border-slate-600 rounded px-3 py-2 text-slate-100 focus:outline-none focus:border-teal-400"
              />
            </div>
            
            <div>
              <label className="text-slate-300 text-xs font-medium">% Cytosine (C): </label>
              <input
                type="number"
                value={cPercent}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setCPercents(e.target.value)}
                className="w-full bg-slate-800 border border-slate-600 rounded px-3 py-2 text-slate-100 focus:outline-none focus:border-teal-400"
              />
            </div>
            
            <button
              onClick={handleChargaffValidate}
              className="w-full px-4 py-2 rounded-xl bg-gradient-to-r from-emerald-500 to-green-600 hover:from-emerald-400 hover:to-green-400 text-slate-950 text-xs font-bold transition-all shadow-lg shadow-emerald-500/20"
            >
              Validate Chargaff Rules
            </button>
            
            {chargaffValid !== null && (
              <div className="mt-2 text-center">
                {chargaffValid ? '✓ Chargaff rules validated successfully' : '✗ Rules violated - check percentages'}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* SVG DNA Visualization Canvas */}
      <div className="relative w-full bg-slate-950 border border-slate-800/80 rounded-2xl p-4 overflow-hidden shadow-2xl">
        <motion.path
          animate={{
            d: currentStep === 0 
              ? 'M 80 110 L 540 110 | M 80 150 L 540 150'
              : currentStep <= 2
                ? 'M 80 60 L 260 60 L 370 110 L 540 110 | M 80 220 L 260 220 L 370 150 L 540 150'
                : currentStep <= 4
                  ? 'M 80 80 L 300 80 L 300 180 L 540 150 | M 80 200 L 120 200 L 120 280 L 540 150'
                  : 'M 80 80 L 200 80 L 200 200 L 350 150 | M 80 200 L 100 200 L 100 280 L 540 150'
          }}
          transition={{ type: 'spring', stiffness: 200 }}
          fill="none"
          stroke="currentColor"
          strokeWidth={5}
          strokeLinecap="round"
        />
        
        {/* Base Pairs Visualization */}
        {['A', 'T', 'C', 'G'].map((base, idx) => (
          <motion.text
            key={base}
            x={80 + idx * 110}
            y={140}
            fill={base === currentBase ? 'teal-400' : 'slate-400'}
            fontSize={12}
            fontWeight={currentStep >= 3 && currentBase === base ? 'bold' : 'normal'}
            transition={{ type: 'spring', delay: idx * 0.1 }}
          >
            {base}
          </motion.text>
        ))}
        
        {/* Hydrogen Bond Visualization */}
        {[2, 3].map((count, idx) => (
          <motion.line
            key={idx}
            x1={100 + idx * 80}
            y1={110}
            x2={100 + idx * 80}
            y2={140}
            stroke={hydrogenBondCount === count ? 'teal-300' : 'slate-600'}
            strokeWidth={count === 3 ? 3 : 2.5}
            strokeDasharray='2, 2'
          />
        ))}
        
        {/* Enzyme Annotations */}
        {currentStep >= 1 && (
          <motion.g
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ type: 'spring', stiffness: 220 }}
          >
            <polygon
              points="340,95 385,120 340,145"
              fill={enzymeState === 'HelicaseUnwinding' ? 'amber-500' : 'slate-500'}
            />
            <text x="325" y="124" fill="#0B132B" fontSize={9} fontWeight="bold">
              Helicase
            </text>
          </motion.g>
        )}
        
        {currentStep >= 3 && (
          <motion.g
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ type: 'spring', stiffness: 220 }}
          >
            <line
              x1={115}
              y1={70}
              x2={270}
              y2={70}
              stroke={enzymeState === 'DNAPolymeraseLeading' ? 'teal-400' : 'slate-500'}
              strokeWidth={4}
            />
            <circle cx="265" cy="70" r={9} fill={enzymeState === 'DNAPolymeraseLeading' ? '#3B82F6' : '#64748B'} />
            <text x="258" y="73" fill={enzymeState === 'DNAPolymeraseLeading' ? '#FFFFFF' : '#CBD5E1'} fontSize={8} fontWeight="bold">
              Pol
            </text>
          </motion.g>
        )}
        
        {currentStep >= 5 && (
          <motion.g
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            transition={{ type: 'spring' }}
          >
            <rect x="180" y="188" width={18} height={24} rx={4} fill={enzymeState === 'DNALigaseSealing' ? '#8B5CF6' : '#64748B'} />
            <text x="168" y="180" fill="#C4B5FD" fontSize={9} fontWeight="bold">
              DNA Ligase
            </text>
          </motion.g>
        )}
      </div>

      {/* Dynamic Step Context */}
      <div className="mt-4 p-3 bg-slate-900 rounded-xl border border-slate-800 text-xs text-slate-300 leading-relaxed flex items-start gap-2.5">
        <Sparkles className="w-4 h-4 text-teal-400 shrink-0 mt-0.5" />
        <div>
          <strong className="text-teal-300 font-bold">{stepsMetadata[currentStep]?.title}: </strong>
          <span>{stepsMetadata[currentStep]?.desc}</span>
        </div>
      </div>

      {/* Curriculum Summary */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-3 mt-4">
        <div className="bg-slate-900/80 border border-slate-800 p-3.5 rounded-2xl space-y-1">
          <div className="flex items-center gap-1.5 text-xs font-bold text-teal-400">
            <CheckCircle2 className="w-4 h-4" />
            <span>Leading Strand Rules</span>
          </div>
          <p className="text-[11px] text-slate-300 leading-relaxed">
            Polymerase builds continuously upon the parent template. No Okazaki fragments.
          </p>
        </div>

        <div className="bg-slate-900/80 border border-slate-800 p-3.5 rounded-2xl space-y-1">
          <div className="flex items-center gap-1.5 text-xs font-bold text-rose-400">
            <ShieldAlert className="w-4 h-4" />
            <span>Lagging Strand Rules</span>
          </div>
          <p className="text-[11px] text-slate-300 leading-relaxed">
            Built upon the parent 5' to 3' template. Synthesized discontinuously as Okazaki fragments.
            Strictly requires DNA Ligase for completion.
          </p>
        </div>
      </div>
    </div>
  );
};