import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Activity, Dna, Droplet, Sparkles, BookOpen } from 'lucide-react';
import { SarcomereSimulation } from '../components/simulations/SarcomereSimulation';
import { DnaReplicationSimulation } from '../components/simulations/DnaReplicationSimulation';
import { NephronSimulation } from '../components/simulations/NephronSimulation';
import { haptics } from '../services/haptics';

export type SimulationTab = 'sarcomere' | 'dna_fork' | 'nephron_adh';

export const SimulationLabScreen: React.FC = () => {
  const [activeTab, setActiveTab] = useState<SimulationTab>('sarcomere');

  const simulations = [
    {
      id: 'sarcomere' as SimulationTab,
      title: 'Sarcomere Sliding Filament',
      arabicTitle: 'انقباض اللييفة العضلية',
      chapter: 'Unit 1 • Chapter 1: Support & Movement',
      icon: Activity,
      color: 'teal',
      badge: 'Huxley Model',
    },
    {
      id: 'dna_fork' as SimulationTab,
      title: 'DNA Replication Fork',
      arabicTitle: 'شوكة تضاعف الـ DNA',
      chapter: 'Unit 2 • Chapter 1: Molecular Genetics',
      icon: Dna,
      color: 'indigo',
      badge: 'Leading vs Lagging',
    },
    {
      id: 'nephron_adh' as SimulationTab,
      title: 'Nephron ADH Osmoregulation',
      arabicTitle: 'النفرون وهرمون الـ ADH',
      chapter: 'Unit 1 • Chapter 2: Hormonal Coordination',
      icon: Droplet,
      color: 'sky',
      badge: 'Counter-Current',
    },
  ];

  const handleTabChange = (tab: SimulationTab) => {
    setActiveTab(tab);
    haptics.pop();
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6 pb-20">
      {/* Screen Header Banner */}
      <div className="bg-gradient-to-r from-slate-900 via-teal-950/40 to-slate-900 border border-slate-800 p-5 rounded-2xl">
        <div className="flex items-center gap-2 text-teal-400 text-xs font-bold uppercase tracking-wider mb-1">
          <Sparkles className="w-4 h-4" />
          <span>Interactive Visual Laboratories • مختبر المحاكاة التفاعلية</span>
        </div>
        <h1 className="text-xl md:text-2xl font-black text-slate-100 tracking-tight">
          BioThanaweya Mechanistic Engines
        </h1>
        <p className="text-xs md:text-sm text-slate-400 mt-1 max-w-2xl leading-relaxed">
          High-performance, touch-responsive visual simulators for core Egyptian Thanaweya Amma
          biology mechanisms. Test molecular dynamics and master official ministry grading laws.
        </p>
      </div>

      {/* Interactive Tabs Selector */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-2.5">
        {simulations.map((sim) => {
          const Icon = sim.icon;
          const isActive = activeTab === sim.id;
          return (
            <button
              key={sim.id}
              onClick={() => handleTabChange(sim.id)}
              className={`p-3.5 rounded-2xl border text-left transition-all relative overflow-hidden flex flex-col justify-between ${
                isActive
                  ? 'glass-card border border-accent-cyan-border shadow-soft shadow-glow'
                  : 'bg-slate-900/60 border-slate-800/80 hover:bg-slate-800/40 hover:border-slate-700'
              }`}
            >
              {isActive && (
                <motion.div
                  layoutId="activeSimTabIndicator"
                  className="absolute inset-0 border-2  border border-accent-cyan-border rounded-2xl pointer-events-none"
                  transition={{ type: 'spring', stiffness: 350, damping: 30 }}
                />
              )}
              <div className="flex items-start justify-between">
                <div
                  className={`p-2 rounded-xl ${isActive ? 'glass-card text-accent-cyan' : 'bg-slate-800 text-slate-400'}`}
                >
                  <Icon className="w-5 h-5" />
                </div>
                <span className="text-[10px] font-mono font-bold px-2 py-0.5 rounded-md bg-slate-950/80 text-teal-400 border border-slate-800">
                  {sim.badge}
                </span>
              </div>
              <div className="mt-3">
                <h3
                  className={`text-xs md:text-sm font-bold ${isActive ? 'text-slate-100' : 'text-slate-300'}`}
                >
                  {sim.title}
                </h3>
                <span className="text-[11px] text-teal-400 font-semibold block font-sans">
                  {sim.arabicTitle}
                </span>
                <span className="text-[10px] text-slate-500 mt-1 block truncate">
                  {sim.chapter}
                </span>
              </div>
            </button>
          );
        })}
      </div>

      {/* Active Simulation Container */}
      <div className="bg-slate-900/40 border border-slate-800/90 p-4 md:p-6 rounded-3xl backdrop-blur-md">
        <AnimatePresence mode="wait">
          <motion.div
            key={activeTab}
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            transition={{ duration: 0.2 }}
          >
            {activeTab === 'sarcomere' && <SarcomereSimulation />}
            {activeTab === 'dna_fork' && <DnaReplicationSimulation />}
            {activeTab === 'nephron_adh' && <NephronSimulation />}
          </motion.div>
        </AnimatePresence>
      </div>
    </div>
  );
};
