import { useState, useEffect } from 'react';
import { motion, AnimatePresence, useReducedMotion, type Variants } from 'framer-motion';
import { SevenAgentsModal } from './components/SevenAgentsModal';
import { StorageVaultModal } from './components/StorageVaultModal';
import { MinistryTextbookModal } from './components/MinistryTextbookModal';
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
import { LanguageProvider, useLanguage } from './context/LanguageContext';

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

function MainApp() {
  const [currentScreen, setCurrentScreen] = useState<string>('dashboard');
  const [activeTopicId, setActiveTopicId] = useState<string>('sarcomere_contraction');
  const [navDirection, setNavDirection] = useState<number>(1);
  const [profile, setProfile] = useState<WellbeingProfile>(() => StorageService.getProfile());
  const [topicMasteries, setTopicMasteries] = useState<TopicMastery[]>(() =>
    StorageService.getTopicMasteries()
  );
  const [isAgentsModalOpen, setIsAgentsModalOpen] = useState(false);
  const [isStorageModalOpen, setIsStorageModalOpen] = useState(false);
  const [isTextbookModalOpen, setIsTextbookModalOpen] = useState(false);
  const [theme, setTheme] = useState<'light' | 'dark'>('light');
  const toggleTheme = () => setTheme(prev => prev === 'light' ? 'dark' : 'light');

  useEffect(() => {
    const setThemeClass = () => {
      if (theme === 'dark') {
        document.documentElement.classList.add('dark');
      } else {
        document.documentElement.classList.remove('dark');
      }
    };
    setThemeClass();
    const handleStorageChange = (e: StorageEvent) => {
      if (e.key === 'theme') {
        setTheme(e.newValue as 'light' | 'dark');
        setThemeClass();
      }
    };
    window.addEventListener('storage', handleStorageChange);
    return () => window.removeEventListener('storage', handleStorageChange);
  }, [theme]);

  const shouldReduceMotion = useReducedMotion();
  const { isRtl } = useLanguage();

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

  const handleOpenTextbookModal = () => {
    haptics.pop();
    setIsTextbookModalOpen(true);
  };

  const handleCloseTextbookModal = () => {
    haptics.pop();
    setIsTextbookModalOpen(false);
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
      x: shouldReduceMotion ? 0 : direction > 0 ? (isRtl ? -32 : 32) : isRtl ? 32 : -32,
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
      x: shouldReduceMotion ? 0 : direction > 0 ? (isRtl ? 24 : -24) : isRtl ? -24 : 24,
      transition: {
        x: { type: 'spring' as const, stiffness: 280, damping: 28 },
        opacity: { duration: 0.16, ease: 'easeIn' as const },
      },
    }),
  };

  return (
    <div className="min-h-screen bg-[#0B132B] text-slate-100 flex flex-col font-sans transition-all duration-200">
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
                onOpenTextbookModal={handleOpenTextbookModal}
                topicMasteries={topicMasteries}
                profile={profile}
              />
            )}

            {currentScreen === 'simulations' && <SimulationLabScreen />}

            {currentScreen === 'formula_lab' && (
              <FormulaLabScreen onBack={() => handleNavigate('dashboard')} />
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

            {currentScreen === 'topic_detail' && (
              <TopicDetailScreen
                topicId={activeTopicId}
                onBack={() => handleNavigate('dashboard')}
                onNavigateToPractice={(tId: string) => handleNavigate('practice', tId)}
              />
            )}

            {currentScreen === 'practice' && (
              <PracticeScreen
                topicId={activeTopicId}
                onBack={() => handleNavigate('dashboard')}
                onXpEarned={handleXpEarned}
              />
            )}

            {currentScreen === 'settings' && (
              <SettingsScreen
                profile={profile}
                onBack={() => handleNavigate('dashboard')}
                onProfileUpdated={(updatedProfile: WellbeingProfile) => setProfile(updatedProfile)}
                onOpenStorageModal={handleOpenStorageModal}
              />
            )}
          </motion.div>
        </AnimatePresence>
      </main>

      {/* 7 AI Agents Dialog */}
      <SevenAgentsModal isOpen={isAgentsModalOpen} onClose={handleCloseAgentsModal} />

      {/* Persistent Storage Vault Dialog */}
      <StorageVaultModal
        isOpen={isStorageModalOpen}
        onClose={handleCloseStorageModal}
        onDataRestored={handleDataRestored}
      />

      {/* Official Ministry Textbook Download Dialog */}
      <MinistryTextbookModal isOpen={isTextbookModalOpen} onClose={handleCloseTextbookModal} />
    </div>
  );
}

export function App() {
  return (
    <LanguageProvider>
      <MainApp />
    </LanguageProvider>
  );
}

export default App;
