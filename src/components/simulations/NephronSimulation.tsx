import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import {
  Droplet,
  Sun,
  Activity,
  RotateCcw,
  AlertCircle,
  CheckCircle2,
  ShieldCheck,
  Zap,
} from 'lucide-react';
import { haptics } from '../../services/haptics';

export const NephronSimulation: React.FC = () => {
  // Explicit ADH toggle switch as requested
  const [adhPresent, setAdhPresent] = useState<boolean>(true);
  // Blood Hydration Level (10% severe dehydration to 90% hyperhydration)
  const [hydration, setHydration] = useState<number>(45);

  // Biological parameters computed deterministically:
  // ADH presence directly dictates collecting duct aquaporin water channels permeability
  const effectiveOsmolarity = Math.round(290 + (100 - hydration) * 0.9);

  // If ADH is absent (e.g. Diabetes Insipidus or suppressed by cold/alcohol),
  // collecting duct is impermeable to water -> massive volume of dilute filtrate
  const urineVolumeL = adhPresent
    ? Math.max(0.5, Number((2.4 - (effectiveOsmolarity / 380) * 1.5).toFixed(2)))
    : 18.0; // Hallmark symptom: up to 18-20L daily!

  const urineConcentrationOsm = adhPresent
    ? Math.min(1200, Math.round(300 + (effectiveOsmolarity - 280) * 8.5))
    : 50; // Severely hypotonic dilute urine

  const handleHydrationChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setHydration(Number(e.target.value));
    haptics.tick();
  };

  const toggleAdh = () => {
    setAdhPresent(!adhPresent);
    haptics.pop();
  };

  const handleReset = () => {
    setAdhPresent(true);
    setHydration(50);
    haptics.pop();
  };

  return (
    <div className="space-y-6">
      {/* Simulation Header & Fast Controls */}
      <div className="flex flex-wrap items-center justify-between gap-3 bg-slate-800/40 backdrop-blur-md border border-white/10 shadow-xl p-4 rounded-2xl">
        <div className="flex flex-wrap items-center gap-2">
          {/* ADH Presence Toggle Button */}
          <button
            onClick={toggleAdh}
            className={`px-4 py-2 rounded-xl text-xs font-bold flex items-center gap-2 border transition-all ${
              adhPresent
                ? 'glass-card text-accent-emerald border border-accent-emerald-border'
                : 'bg-slate-800 text-slate-400 border-slate-700'
            }`}
          >
            <Zap
              className={`w-3.5 h-3.5 ${adhPresent ? 'text-amber-400 fill-amber-400' : 'text-slate-500'}`}
            />
            <span>
              ADH Hormone (Vasopressin): {adhPresent ? 'PRESENT (Active)' : 'ABSENT (Suppressed)'}
            </span>
          </button>

          <div className="px-3 py-1.5 rounded-xl glass-card border border-accent-cyan-border text-accent-cyan text-xs font-mono font-semibold flex items-center gap-1.5">
            <Activity className="w-3.5 h-3.5" />
            <span>Blood Osmolarity: {effectiveOsmolarity} mOsm/L</span>
          </div>
        </div>

        <button
          onClick={handleReset}
          className="p-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-300 transition-all"
          title="Reset"
        >
          <RotateCcw className="w-4 h-4" />
        </button>
      </div>

      {/* Counter-Current Loop of Henle & Nephron Canvas */}
      <div className="relative w-full bg-slate-950 border border-white/10 shadow-xl p-4 rounded-2xl p-4 overflow-hidden shadow-2xl">
        <div className="flex justify-between items-center text-xs text-slate-400 mb-2 font-mono">
          <span className="flex items-center gap-1.5">
            <Droplet className="w-3.5 h-3.5 text-teal-400" />
            Nephron Counter-Current Multiplication System
          </span>
          <span className={adhPresent ? 'text-emerald-400 font-bold' : 'text-rose-400 font-bold'}>
            Daily Urine Output: {urineVolumeL} L/day
          </span>
        </div>

        <svg viewBox="0 0 620 280" className="w-full h-auto select-none">
          <defs>
            <linearGradient id="medullaGrad" x1="0%" y1="0%" x2="0%" y2="100%">
              <stop offset="0%" stopColor="#0F172A" stopOpacity="0.4" />
              <stop offset="100%" stopColor="#1E293B" stopOpacity="0.9" />
            </linearGradient>
          </defs>

          {/* Medullary Interstitial Osmotic Gradient (300 at top to 1200 mOsm at hairpin bend) */}
          <rect
            x="20"
            y="85"
            width="580"
            height="180"
            rx="8"
            fill="url(#medullaGrad)"
            stroke="#334155"
            strokeDasharray="3 3"
          />
          <text x="30" y="75" fill="#64748B" fontSize="10" fontWeight="bold">
            Renal Cortex (300 mOsm/L)
          </text>
          <text x="30" y="105" fill="#64748B" fontSize="10" fontWeight="bold">
            Outer Medulla (600 mOsm/L)
          </text>
          <text x="30" y="240" fill="#64748B" fontSize="10" fontWeight="bold">
            Inner Medulla (1200 mOsm/L - Hyperosmotic)
          </text>

          {/* Bowman's Capsule & Glomerulus */}
          <circle cx="85" cy="45" r="16" fill="#BE123C" opacity="0.85" />
          <path d="M 68 35 C 68 62, 102 62, 102 35" fill="none" stroke="#F59E0B" strokeWidth="4" />
          <text x="50" y="22" fill="#FDA4AF" fontSize="9" fontWeight="bold">
            Bowman's Capsule
          </text>

          {/* Proximal Convoluted Tubule */}
          <path
            d="M 100 48 Q 120 35 140 48 T 175 48"
            fill="none"
            stroke="#14B8A6"
            strokeWidth="5"
            strokeLinecap="round"
          />

          {/* Descending Limb of Loop of Henle (Permeable to H2O, Impermeable to Solutes) */}
          <path
            d="M 175 48 L 175 220"
            fill="none"
            stroke="#0EA5E9"
            strokeWidth="5"
            strokeLinecap="round"
          />
          <text x="110" y="140" fill="#38BDF8" fontSize="9" fontWeight="bold">
            Descending Limb
          </text>
          <text x="110" y="152" fill="#7DD3FC" fontSize="8">
            (Permeable to H₂O)
          </text>

          {/* Osmotic Water Efflux from Descending Limb */}
          <g>
            <motion.path
              animate={{ opacity: [0.4, 1, 0.4], x: [-2, 4, -2] }}
              transition={{ repeat: Infinity, duration: 1.5 }}
              d="M 175 120 L 140 120 M 175 170 L 140 170"
              stroke="#38BDF8"
              strokeWidth="2"
              strokeDasharray="2 2"
            />
            <text x="125" y="115" fill="#38BDF8" fontSize="8" fontWeight="bold">
              H₂O
            </text>
            <text x="125" y="165" fill="#38BDF8" fontSize="8" fontWeight="bold">
              H₂O
            </text>
          </g>

          {/* Hairpin Turn at 1200 mOsm/L */}
          <path
            d="M 175 220 Q 210 255 245 220"
            fill="none"
            stroke="#0EA5E9"
            strokeWidth="5"
            strokeLinecap="round"
          />
          <text x="185" y="248" fill="#FBBF24" fontSize="10" fontWeight="bold">
            1200 mOsm
          </text>

          {/* Ascending Limb of Loop of Henle (Impermeable to H2O, Active Transport of NaCl) */}
          <path
            d="M 245 220 L 245 48"
            fill="none"
            stroke="#6366F1"
            strokeWidth="5"
            strokeLinecap="round"
          />
          <text x="255" y="140" fill="#818CF8" fontSize="9" fontWeight="bold">
            Ascending Limb
          </text>
          <text x="255" y="152" fill="#A5B4FC" fontSize="8">
            (Active NaCl Efflux)
          </text>

          {/* Solute (NaCl) active pumping out of Ascending Limb into Medulla */}
          <motion.path
            animate={{ opacity: [0.4, 1, 0.4], x: [0, 4, 0] }}
            transition={{ repeat: Infinity, duration: 1.5 }}
            d="M 245 120 L 280 120 M 245 170 L 280 170"
            stroke="#818CF8"
            strokeWidth="2"
            strokeDasharray="2 2"
          />
          <text x="285" y="123" fill="#A5B4FC" fontSize="8" fontWeight="bold">
            NaCl
          </text>
          <text x="285" y="173" fill="#A5B4FC" fontSize="8" fontWeight="bold">
            NaCl
          </text>

          {/* Distal Convoluted Tubule */}
          <path
            d="M 245 48 Q 280 35 320 48 T 370 48"
            fill="none"
            stroke="#14B8A6"
            strokeWidth="5"
            strokeLinecap="round"
          />

          {/* Collecting Duct (Major Site of ADH Water Reabsorption) */}
          <path
            d="M 370 48 L 450 48 L 450 250"
            fill="none"
            stroke={adhPresent ? '#F59E0B' : '#64748B'}
            strokeWidth="7"
            strokeLinecap="round"
          />
          <text
            x="460"
            y="100"
            fill={adhPresent ? '#FCD34D' : '#94A3B8'}
            fontSize="11"
            fontWeight="bold"
          >
            Collecting Duct (القناة الجامعة)
          </text>
          <text x="460" y="115" fill="#CBD5E1" fontSize="8">
            {adhPresent ? 'Aquaporin Channels OPEN (ADH Active)' : 'Channels CLOSED (No ADH)'}
          </text>

          {/* ADH Water Reabsorption Efflux */}
          {adhPresent && (
            <motion.g
              animate={{ opacity: [0.3, 1, 0.3], x: [0, 6, 0] }}
              transition={{ repeat: Infinity, duration: 1.4 }}
            >
              <line x1="450" y1="140" x2="495" y2="140" stroke="#38BDF8" strokeWidth="2.5" />
              <polygon points="495,136 502,140 495,144" fill="#38BDF8" />
              <text x="508" y="144" fill="#7DD3FC" fontSize="9" fontWeight="bold">
                H₂O back to blood
              </text>

              <line x1="450" y1="180" x2="495" y2="180" stroke="#38BDF8" strokeWidth="2.5" />
              <polygon points="495,176 502,180 495,184" fill="#38BDF8" />
              <text x="508" y="184" fill="#7DD3FC" fontSize="9" fontWeight="bold">
                H₂O reabsorbed
              </text>
            </motion.g>
          )}

          {/* Urine Output Vessel Indicator */}
          <rect x="425" y="252" width="50" height="18" rx="4" fill="#1E293B" stroke="#475569" />
          <text x="410" y="265" fill="#F8FAFC" fontSize="9" fontWeight="bold">
            Urine ({urineConcentrationOsm} mOsm)
          </text>
        </svg>

        {/* Live Osmotic Gauges Strip */}
        <div className="mt-3 grid grid-cols-2 md:grid-cols-4 gap-2 text-xs">
          <div className="bg-slate-900 p-2.5 rounded-xl border border-slate-800">
            <span className="text-slate-400 block text-[10px]">Blood Osmolarity</span>
            <span className="font-bold text-teal-300 font-mono">{effectiveOsmolarity} mOsm/L</span>
          </div>

          <div className="bg-slate-900 p-2.5 rounded-xl border border-slate-800">
            <span className="text-slate-400 block text-[10px]">ADH Hormonal State</span>
            <span
              className={`font-bold font-mono ${adhPresent ? 'text-amber-300' : 'text-slate-400'}`}
            >
              {adhPresent ? 'Active Reabsorption' : 'Zero Reabsorption'}
            </span>
          </div>

          <div className="bg-slate-900 p-2.5 rounded-xl border border-slate-800">
            <span className="text-slate-400 block text-[10px]">Filtrate Excretion Rate</span>
            <span
              className={`font-bold font-mono ${urineVolumeL > 5 ? 'text-rose-400' : 'text-sky-300'}`}
            >
              {urineVolumeL} Liters / Day
            </span>
          </div>

          <div className="bg-slate-900 p-2.5 rounded-xl border border-slate-800">
            <span className="text-slate-400 block text-[10px]">Urine Osmotic Tonicity</span>
            <span
              className={`font-bold font-mono ${urineConcentrationOsm > 800 ? 'text-amber-400' : 'text-emerald-400'}`}
            >
              {urineConcentrationOsm} mOsm/L{' '}
              {urineConcentrationOsm > 800 ? '(Hypertonic)' : '(Hypotonic)'}
            </span>
          </div>
        </div>
      </div>

      {/* Hydration Slider */}
      <div className="bg-slate-800/40 backdrop-blur-md border border-white/10 shadow-xl p-4 rounded-2xl space-y-2">
        <div className="flex justify-between text-xs">
          <span className="text-slate-300 font-medium flex items-center gap-1.5">
            <Droplet className="w-4 h-4 text-teal-400" />
            Systemic Hydration & Plasma Water Fraction
          </span>
          <span className="font-mono text-teal-400 font-bold">{hydration}%</span>
        </div>
        <input
          type="range"
          min="10"
          max="90"
          value={hydration}
          onChange={handleHydrationChange}
          className="w-full accent-teal-400 cursor-pointer h-2 bg-slate-800 rounded-lg"
        />
        <div className="flex justify-between text-[10px] text-slate-500">
          <span>Severe Dehydration (10%)</span>
          <span>Euhydrated (50%)</span>
          <span>Overhydrated (90%)</span>
        </div>
      </div>

      {/* Curriculum Exam Rule Card */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
        <div className="bg-slate-800/40 backdrop-blur-md border border-white/10 shadow-xl p-3.5 rounded-2xl space-y-1">
          <div className="flex items-center gap-1.5 text-xs font-bold text-teal-400">
            <CheckCircle2 className="w-4 h-4" />
            <span>Counter-Current Multiplication Principle</span>
          </div>
          <p className="text-[11px] text-slate-300 leading-relaxed">
            Descending limb is <strong className="text-teal-200">permeable to water</strong>, while
            the ascending limb actively pumps <strong className="text-indigo-200">NaCl</strong> out
            without water, creating a hyperosmotic medullary gradient (up to 1200 mOsm/L).
          </p>
        </div>

        <div className="bg-slate-800/40 backdrop-blur-md border border-white/10 shadow-xl p-3.5 rounded-2xl space-y-1">
          <div className="flex items-center gap-1.5 text-xs font-bold text-amber-400">
            <AlertCircle className="w-4 h-4" />
            <span>ADH (Vasopressin) Regulation Law</span>
          </div>
          <p className="text-[11px] text-slate-300 leading-relaxed">
            When ADH is present, collecting duct aquaporins open ➔ Water is reabsorbed back into
            peritubular capillaries ➔{' '}
            <strong className="text-emerald-300">Blood pressure rises</strong>,{' '}
            <strong className="text-rose-300">Urine volume drops</strong>, and{' '}
            <strong className="text-amber-300">Urine concentration skyrockets</strong>!
          </p>
        </div>
      </div>
    </div>
  );
};
