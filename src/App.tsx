import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence, useReducedMotion, type Variants } from 'framer-motion';
import { Navbar } from './components/Navbar';
import { SevenAgentsModal } from './components/SevenAgentsModal';
import { StorageVaultModal } from './components/StorageVaultModal';
import { DashboardScreen } from './screens/DashboardScreen';
import { TopicDetailScreen } from './screens/TopicDetailScreen';
import { PracticeScreen } from './screens/PracticeScreen';
import { FormulaLabScreen } from './screens/FormulaLabScreen';
import { SimulationLabScreen } from './screens/SimulationLabScreen';
import { FsrsReviewScreen } from './screens/FsrsReviewScreen';
import { SimulatedExamScreen } from './screens/SimulatedExamScreen';
import { SettingsScreen } from './screens/SettingsScreen';
import { StorageService } from './services/storage';
import { haptics } from './services/haptics';
import { TopicMastery, WellbeingProfile } from './types';

// Hierarchical depth map for direction-aware sliding
const SCREEN_DEPTH: Record<string, number> = {
  dashboard: 0,
  simulations: 1,
  formula_lab: 1,
  fsrs: 1,
  simulated_exam: 1,
  settings: 1,
  topic_detail: 1,
  practice: 2,
};

export function App() {
  const [currentScreen, setCurrentScreen] = useState<string>('dashboard');
  const [activeTopicId, setActiveTopicId] = useState<string>('sarcomere_contraction');
  const [navDirection, setNavDirection] = useState<number>(1);
  const [profile, setProfile] = useState<WellbeingProfile>(() => StorageService.getProfile());
  const [topicMasteries, setTopicMasteries] = useState<TopicMastery[]>(() =>
    StorageService.getTopicMasteries()
  );
  const [isAgentsModalOpen, setIsAgentsModalOpen] = useState(false);
  const [isStorageModalOpen, setIsStorageModalOpen] = useState(false);

  const shouldReduceMotion = useReducedMotion();

  useEffect(() => {
    StorageService.init().then(() => {
      setProfile(StorageService.getProfile());
      setTopicMasteries(StorageService.getTopicMasteries());
    });
  }, []);

  const handleNavigate = (screen: string, topicId?: string) => {
    haptics.tick();
    const prevDepth = SCREEN_DEPTH[currentScreen] ?? 0;
    const nextDepth = SCREEN_DEPTH[screen] ?? 0;
    setNavDirection(nextDepth >= prevDepth ? 1 : -1);

    if (topicId) setActiveTopicId(topicId);
    setCurrentScreen(screen);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleXpEarned = (amount: number) => {
    haptics.celebrate();
    const updated = StorageService.addXp(amount);
    setProfile(updated);
  };

  const handleOpenAgentsModal = () => {
    haptics.pop();
    setIsAgentsModalOpen(true);
  };

  const handleCloseAgentsModal = () => {
    haptics.pop();
    setIsAgentsModalOpen(false);
  };

  const handleOpenStorageModal = () => {
    haptics.pop();
    setIsStorageModalOpen(true);
  };

  const handleCloseStorageModal = () => {
    haptics.pop();
    setIsStorageModalOpen(false);
  };

  const handleDataRestored = () => {
    haptics.pop();
    setProfile(StorageService.getProfile());
    setTopicMasteries(StorageService.getTopicMasteries());
  };

  // Subtle horizontal slide with spring damping & accessibility fallback
  const screenVariants: Variants = {
    initial: (direction: number) => ({
      opacity: 0,
      x: shouldReduceMotion ? 0 : direction > 0 ? 32 : -32,
    }),
    animate: {
      opacity: 1,
      x: 0,
      transition: {
        x: { type: 'spring' as const, stiffness: 280, damping: 28 },
        opacity: { duration: 0.22, ease: 'easeOut' as const },
      },
    },
    exit: (direction: number) => ({
      opacity: 0,
      x: shouldReduceMotion ? 0 : direction > 0 ? -24 : 24,
      transition: {
        x: { type: 'spring' as const, stiffness: 280, damping: 28 },
        opacity: { duration: 0.16, ease: 'easeIn' as const },
      },
    }),
  };

  return (
    <div className="min-h-screen bg-[#0B132B] text-slate-100 flex flex-col font-sans">
      <Navbar
        currentScreen={currentScreen}
        onNavigate={(screen, tId) => handleNavigate(screen, tId)}
        profile={profile}
        onOpenAgentsModal={handleOpenAgentsModal}
        onOpenStorageModal={handleOpenStorageModal}
      />

      <main className="flex-1 relative overflow-x-hidden min-h-[calc(100vh-8rem)]">
        <AnimatePresence mode="wait" custom={navDirection}>
          <motion.div
            key={currentScreen}
            custom={navDirection}
            variants={screenVariants}
            initial="initial"
            animate="animate"
            exit="exit"
            className="w-full"
          >
            {currentScreen === 'dashboard' && (
              <DashboardScreen
                onNavigateToTopic={(tId) => handleNavigate('topic_detail', tId)}
                onNavigateToPractice={(tId) => handleNavigate('practice', tId)}
                onNavigateToFsrs={(tId) => handleNavigate('fsrs', tId)}
                onNavigateToFormulaLab={() => handleNavigate('formula_lab')}
                onNavigateToSimulatedExam={() => handleNavigate('simulated_exam')}
                onOpenAgentsModal={handleOpenAgentsModal}
                topicMasteries={topicMasteries}
                profile={profile}
              />
            )}

            {currentScreen === 'topic_detail' && (
              <TopicDetailScreen
                topicId={activeTopicId}
                onBack={() => handleNavigate('dashboard')}
                onNavigateToPractice={(tId) => handleNavigate('practice', tId)}
              />
            )}

            {currentScreen === 'practice' && (
              <PracticeScreen
                topicId={activeTopicId}
                onBack={() => handleNavigate('dashboard')}
                onXpEarned={handleXpEarned}
              />
            )}

            {currentScreen === 'formula_lab' && (
              <FormulaLabScreen onBack={() => handleNavigate('dashboard')} />
            )}

            {currentScreen === 'simulations' && (
              <SimulationLabScreen />
            )}

            {currentScreen === 'fsrs' && (
              <FsrsReviewScreen
                topicId={activeTopicId}
                onBack={() => handleNavigate('dashboard')}
                onXpEarned={handleXpEarned}
              />
            )}

            {currentScreen === 'simulated_exam' && (
              <SimulatedExamScreen
                onBack={() => handleNavigate('dashboard')}
                onXpEarned={handleXpEarned}
              />
            )}

            {currentScreen === 'settings' && (
              <SettingsScreen
                onBack={() => handleNavigate('dashboard')}
                profile={profile}
                onProfileUpdated={setProfile}
                onOpenStorageModal={handleOpenStorageModal}
              />
            )}
          </motion.div>
        </AnimatePresence>
      </main>

      {/* 7 Agents Modal */}
      <SevenAgentsModal
        isOpen={isAgentsModalOpen}
        onClose={handleCloseAgentsModal}
      />

      {/* Storage Vault & Database Modal */}
      <StorageVaultModal
        isOpen={isStorageModalOpen}
        onClose={handleCloseStorageModal}
        onDataRestored={handleDataRestored}
      />

      {/* Footer */}
      <footer className="border-t border-slate-800/80 bg-[#070D1E] py-6 text-center text-xs text-slate-500">
        <div className="max-w-7xl mx-auto px-4 space-y-1">
          <p>
            BioThanaweya • Official Egyptian Thanaweya Amma (Languages) Biology Learning Platform
          </p>
          <p className="text-[11px] text-slate-600 font-arabic">
            تم التطوير لدعم طلاب الثانوية العامة وفق معايير ونماذج إجابة وزارة التربية والتعليم المصرية
          </p>
        </div>
      </footer>
    </div>
  );
}

export default App;
