import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Zap, Play, RotateCcw, AlertTriangle, Activity } from 'lucide-react';
import { haptics } from '../../services/haptics';

export const SarcomereSimulation: React.FC = () => {
  // Independent Sliders as requested: ATP supply % and Calcium concentration %
  const [atpLevel, setAtpLevel] = useState<number>(75);
  const [calciumLevel, setCalciumLevel] = useState<number>(80);
  const [isPowerStroking, setIsPowerStroking] = useState<boolean>(false);

  // Cross-bridge cycle dynamics:
  // - Calcium binds to troponin, exposing binding sites on actin
  // - ATP hydrolysis (ADP + Pi) cocks myosin head
  // - If Calcium > 20% and ATP > 10%, cross-bridges attach and power stroke initiates
  const isCaSufficient = calciumLevel >= 25;
  const isAtpSufficient = atpLevel >= 20;
  const isCrampState = calciumLevel >= 40 && atpLevel < 15; // Sustained contracture with no ATP to detach

  // Contraction degree is determined by Calcium (activation) and ATP (energy cycle)
  const contraction = isCrampState
    ? 90
    : isCaSufficient && isAtpSufficient
      ? Math.min(100, Math.round(calciumLevel * 0.6 + atpLevel * 0.4))
      : Math.max(5, Math.round(calciumLevel * 0.2));

  // Sarcomere biological measurements (µm)
  const sarcomereLengthUm = Number((2.6 - (contraction / 100) * 0.9).toFixed(2));
  const hZoneWidth = Math.max(0, Math.round(110 - contraction * 1.15));
  const iBandWidth = Math.max(10, Math.round(85 - contraction * 0.75));
  const aBandWidth = 140; // Invariant length of Myosin thick filaments

  const halfLength = 180 - (contraction / 100) * 55;
  const leftZ = 300 - halfLength;
  const rightZ = 300 + halfLength;

  const handleAtpChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const val = Number(e.target.value);
    setAtpLevel(val);
    haptics.tick();
    if (val < 15 && calciumLevel >= 40) {
      haptics.warning(); // Warning of muscular cramp
    }
  };

  const handleCalciumChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const val = Number(e.target.value);
    setCalciumLevel(val);
    haptics.tick();
    if (val > 50 && isAtpSufficient) {
      haptics.pop(); // Cross-bridge attachment tactile pulse
    }
  };

  const triggerPowerStroke = () => {
    if (!isAtpSufficient) {
      haptics.warning();
      return;
    }
    haptics.pop();
    setIsPowerStroking(true);
    setCalciumLevel(95);
    setTimeout(() => {
      setIsPowerStroking(false);
      haptics.celebrate();
    }, 700);
  };

  const resetAll = () => {
    haptics.pop();
    setAtpLevel(75);
    setCalciumLevel(70);
  };

  return (
    <div className="space-y-6">
      {/* Real-Time Telemetry Bar */}
      <div className="flex flex-wrap items-center justify-between gap-3 bg-slate-900/90 border border-slate-800 p-4 rounded-2xl">
        <div className="flex flex-wrap items-center gap-2">
          {/* ATP Status Pill */}
          <div
            className={`px-3 py-1.5 rounded-xl text-xs font-semibold flex items-center gap-1.5 border transition-all ${
              atpLevel >= 20
                ? 'glass-card border border-accent-emerald-border text-accent-emerald'
                : 'bg-rose-500/20 border-rose-500/40 text-rose-300 animate-pulse'
            }`}
          >
            <Zap className="w-3.5 h-3.5 text-amber-400" />
            <span>ATP Supply: {atpLevel}%</span>
          </div>

          {/* Calcium Status Pill */}
          <div
            className={`px-3 py-1.5 rounded-xl text-xs font-semibold flex items-center gap-1.5 border transition-all ${
              calciumLevel >= 25
                ? 'glass-card border border-accent-cyan-border text-accent-cyan'
                : 'bg-slate-800 border-slate-700 text-slate-400'
            }`}
          >
            <div className="w-2 h-2 rounded-full bg-accent-cyan" />
            <span>Ca²⁺ Concentration: {calciumLevel}%</span>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={triggerPowerStroke}
            disabled={isPowerStroking || !isAtpSufficient}
            className="px-4 py-2 rounded-xl bg-gradient-to-r from-accent-cyan to-accent-emerald hover:from-accent-cyan-border hover:to-accent-emerald-border text-slate-950 text-xs font-bold flex items-center gap-1.5 transition-all shadow-lg shadow-teal-500/20 disabled:opacity-40"
          >
            <Play className="w-3.5 h-3.5 fill-current" />
            <span>Power Stroke Pulse</span>
          </button>
          <button
            onClick={resetAll}
            className="p-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-300 transition-all"
            title="Reset to Physiological Resting State"
          >
            <RotateCcw className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Interactive SVG Sarcomere Canvas */}
      <div className="relative w-full glass-card border border-slate-700/60 rounded-2xl p-4 overflow-hidden shadow-soft shadow-glow">
        <div className="flex justify-between items-center text-xs text-slate-400 mb-2 font-mono">
          <span className="flex items-center gap-1.5">
            <Activity className="w-3.5 h-3.5 text-teal-400" />
            Sliding Filament Cycle (Huxley Model)
          </span>
          <span className="text-teal-400 font-bold">Sarcomere Length: {sarcomereLengthUm} µm</span>
        </div>

        <svg viewBox="0 0 600 240" className="w-full h-auto select-none">
          <defs>
            <linearGradient id="myosinGrad" x1="0%" y1="0%" x2="100%" y2="0%">
              <stop offset="0%" stopColor="#818CF8" />
              <stop offset="50%" stopColor="#6366F1" />
              <stop offset="100%" stopColor="#818CF8" />
            </linearGradient>
            <linearGradient id="actinGrad" x1="0%" y1="0%" x2="100%" y2="0%">
              <stop offset="0%" stopColor="#2DD4BF" />
              <stop offset="100%" stopColor="#14B8A6" />
            </linearGradient>
          </defs>

          {/* Central M-Line */}
          <line
            x1="300"
            y1="20"
            x2="300"
            y2="220"
            stroke="#EC4899"
            strokeWidth="2"
            strokeDasharray="3 3"
          />
          <text
            x="305"
            y="32"
            fill="#EC4899"
            fontSize="10"
            fontFamily="sans-serif"
            fontWeight="bold"
          >
            M-Line
          </text>

          {/* Left Z-Line */}
          <motion.line
            animate={{ x1: leftZ, x2: leftZ }}
            transition={{ type: 'spring', stiffness: 220, damping: 25 }}
            y1="20"
            y2="220"
            stroke="#F59E0B"
            strokeWidth="4"
          />
          <text x={leftZ - 28} y="32" fill="#F59E0B" fontSize="11" fontWeight="bold">
            Z-Line
          </text>

          {/* Right Z-Line */}
          <motion.line
            animate={{ x1: rightZ, x2: rightZ }}
            transition={{ type: 'spring', stiffness: 220, damping: 25 }}
            y1="20"
            y2="220"
            stroke="#F59E0B"
            strokeWidth="4"
          />
          <text x={rightZ + 8} y="32" fill="#F59E0B" fontSize="11" fontWeight="bold">
            Z-Line
          </text>

          {/* Central Thick Myosin Filaments (Stationary, 140px Invariant) */}
          <rect x="230" y="105" width={aBandWidth} height="30" rx="4" fill="url(#myosinGrad)" />
          <rect x="230" y="60" width={aBandWidth} height="20" rx="3" fill="url(#myosinGrad)" />
          <rect x="230" y="160" width={aBandWidth} height="20" rx="3" fill="url(#myosinGrad)" />

          {/* Cross-Bridges cycling with Calcium and ATP */}
          {[-45, -20, 20, 45].map((offset, idx) => {
            const isAttached = isCaSufficient;
            const hookAngle = isAttached ? (offset < 0 ? 12 : -12) : 0;
            const headYUpper = isAttached ? 47 : 53;
            const headYLower = isAttached ? 193 : 187;

            return (
              <g key={idx}>
                {/* Upper Cross-Bridge */}
                <motion.line
                  animate={{
                    x2: 300 + offset + hookAngle,
                    y2: headYUpper,
                  }}
                  transition={{ type: 'spring', stiffness: 260, damping: 20 }}
                  x1={300 + offset}
                  y1="60"
                  stroke={isAttached ? '#38BDF8' : '#818CF8'}
                  strokeWidth="3.5"
                  strokeLinecap="round"
                />
                <motion.circle
                  animate={{
                    cx: 300 + offset + hookAngle,
                    cy: headYUpper,
                  }}
                  transition={{ type: 'spring', stiffness: 260, damping: 20 }}
                  r={isAttached ? 4.5 : 3.5}
                  fill={isAttached ? '#38BDF8' : '#C7D2FE'}
                />

                {/* Lower Cross-Bridge */}
                <motion.line
                  animate={{
                    x2: 300 + offset + hookAngle,
                    y2: headYLower,
                  }}
                  transition={{ type: 'spring', stiffness: 260, damping: 20 }}
                  x1={300 + offset}
                  y1="180"
                  stroke={isAttached ? '#38BDF8' : '#818CF8'}
                  strokeWidth="3.5"
                  strokeLinecap="round"
                />
                <motion.circle
                  animate={{
                    cx: 300 + offset + hookAngle,
                    cy: headYLower,
                  }}
                  transition={{ type: 'spring', stiffness: 260, damping: 20 }}
                  r={isAttached ? 4.5 : 3.5}
                  fill={isAttached ? '#38BDF8' : '#C7D2FE'}
                />
              </g>
            );
          })}

          {/* Left Actin Filaments attached to Left Z-line */}
          <motion.g
            animate={{ x: leftZ }}
            transition={{ type: 'spring', stiffness: 220, damping: 25 }}
          >
            <rect x="0" y="44" width="130" height="8" rx="2" fill="url(#actinGrad)" />
            <rect x="0" y="90" width="130" height="8" rx="2" fill="url(#actinGrad)" />
            <rect x="0" y="142" width="130" height="8" rx="2" fill="url(#actinGrad)" />
            <rect x="0" y="188" width="130" height="8" rx="2" fill="url(#actinGrad)" />
          </motion.g>

          {/* Right Actin Filaments attached to Right Z-line */}
          <motion.g
            animate={{ x: rightZ - 130 }}
            transition={{ type: 'spring', stiffness: 220, damping: 25 }}
          >
            <rect x="0" y="44" width="130" height="8" rx="2" fill="url(#actinGrad)" />
            <rect x="0" y="90" width="130" height="8" rx="2" fill="url(#actinGrad)" />
            <rect x="0" y="142" width="130" height="8" rx="2" fill="url(#actinGrad)" />
            <rect x="0" y="188" width="130" height="8" rx="2" fill="url(#actinGrad)" />
          </motion.g>

          {/* A-Band Invariant Overlay */}
          <line x1="230" y1="228" x2="370" y2="228" stroke="#818CF8" strokeWidth="2.5" />
          <text x="278" y="238" fill="#A5B4FC" fontSize="10" fontWeight="bold">
            A-Band (Constant 140px)
          </text>

          {/* H-Zone Indicator (Narrows and vanishes during full contraction) */}
          {hZoneWidth > 6 && (
            <g>
              <line
                x1={300 - hZoneWidth / 2}
                y1="12"
                x2={300 + hZoneWidth / 2}
                y2="12"
                stroke="#38BDF8"
                strokeWidth="2.5"
              />
              <text x={285} y="10" fill="#38BDF8" fontSize="10" fontWeight="bold">
                H-Zone
              </text>
            </g>
          )}
        </svg>

        {/* Live Feedback Strip */}
        <div className="mt-3 flex flex-wrap items-center justify-between text-xs px-3 py-2 bg-slate-900 rounded-xl border border-slate-800">
          <div className="flex items-center gap-2">
            <span className="text-slate-400">Contraction:</span>
            <span className="font-bold text-teal-300 font-mono">{contraction}%</span>
            <span className="text-slate-500">|</span>
            <span className="text-slate-400">H-Zone:</span>
            <span
              className={`font-semibold ${hZoneWidth === 0 ? 'text-rose-400' : 'text-sky-300'}`}
            >
              {hZoneWidth === 0 ? 'Disappeared (Full Contraction)' : `${hZoneWidth} px`}
            </span>
          </div>
          <div className="flex items-center gap-1.5 text-indigo-300">
            <span className="text-slate-400">Cross-Bridges:</span>
            <span
              className={`font-semibold ${isCaSufficient ? 'text-teal-300' : 'text-slate-400'}`}
            >
              {isCaSufficient ? 'Hooked (Active Sites Exposed)' : 'Detached (Tropomyosin Blocking)'}
            </span>
          </div>
        </div>
      </div>

      {/* Two Interactive Sliders: ATP Supply & Calcium Concentration */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {/* ATP Slider */}
        <div className="bg-slate-900/80 border border-slate-800 p-4 rounded-2xl space-y-2">
          <div className="flex justify-between text-xs">
            <span className="text-slate-300 font-medium flex items-center gap-1.5">
              <Zap className="w-4 h-4 text-amber-400" />
              ATP Supply (Hydrolysis & Detachment)
            </span>
            <span className="font-mono text-amber-400 font-bold">{atpLevel}%</span>
          </div>
          <input
            type="range"
            min="0"
            max="100"
            value={atpLevel}
            onChange={handleAtpChange}
            className="w-full accent-amber-400 cursor-pointer h-2 bg-slate-800 rounded-lg"
          />
          <div className="flex justify-between text-[10px] text-slate-500">
            <span>Depleted (Cramp!)</span>
            <span>Normal Tonus</span>
            <span>Excess Energy</span>
          </div>
        </div>

        {/* Calcium Slider */}
        <div className="bg-slate-900/80 border border-slate-800 p-4 rounded-2xl space-y-2">
          <div className="flex justify-between text-xs">
            <span className="text-slate-300 font-medium flex items-center gap-1.5">
              <div className="w-2.5 h-2.5 rounded-full bg-accent-cyan" />
              Calcium [Ca²⁺] Concentration
            </span>
            <span className="font-mono text-teal-400 font-bold">{calciumLevel}%</span>
          </div>
          <input
            type="range"
            min="0"
            max="100"
            value={calciumLevel}
            onChange={handleCalciumChange}
            className="w-full accent-teal-400 cursor-pointer h-2 bg-slate-800 rounded-lg"
          />
          <div className="flex justify-between text-[10px] text-slate-500">
            <span>Pumped into SR</span>
            <span>Threshold</span>
            <span>Maximal Depolarization</span>
          </div>
        </div>
      </div>

      {/* Exam Traps & Mark Scheme Alert */}
      {isCrampState && (
        <motion.div
          initial={{ opacity: 0, y: 5 }}
          animate={{ opacity: 1, y: 0 }}
          className="glass-card border border-accent-cyan-border p-4 rounded-2xl flex items-start gap-3"
        >
          <AlertTriangle className="w-5 h-5 text-rose-400 shrink-0 mt-0.5" />
          <div className="text-xs text-rose-200 leading-relaxed">
            <strong className="text-rose-100 font-bold text-sm block">
              Ministry Exam Trap: Muscular Cramp vs Fatigue
            </strong>
            <p className="mt-1 text-slate-300">
              When Calcium remains elevated while ATP is depleted, transverse cross-bridges cannot
              detach from actin filaments. This causes an involuntary sustained contracture (
              <strong className="text-rose-300">Muscle Cramp / شد عضلي</strong>) which may result in
              fiber rupture and internal hemorrhage if forcefully stretched.
            </p>
          </div>
        </motion.div>
      )}
    </div>
  );
};
