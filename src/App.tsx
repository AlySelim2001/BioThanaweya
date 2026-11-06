import React, { useState } from 'react';
import { Navbar } from './components/Navbar';
import { SevenAgentsModal } from './components/SevenAgentsModal';
import { DashboardScreen } from './screens/DashboardScreen';
import { TopicDetailScreen } from './screens/TopicDetailScreen';
import { PracticeScreen } from './screens/PracticeScreen';
import { FormulaLabScreen } from './screens/FormulaLabScreen';
import { FsrsReviewScreen } from './screens/FsrsReviewScreen';
import { SimulatedExamScreen } from './screens/SimulatedExamScreen';
import { SettingsScreen } from './screens/SettingsScreen';
import { StorageService } from './services/storage';
import { TopicMastery, WellbeingProfile } from './types';

export function App() {
  const [currentScreen, setCurrentScreen] = useState<string>('dashboard');
  const [activeTopicId, setActiveTopicId] = useState<string>('sarcomere_contraction');
  const [profile, setProfile] = useState<WellbeingProfile>(() => StorageService.getProfile());
  const [topicMasteries, setTopicMasteries] = useState<TopicMastery[]>(() =>
    StorageService.getTopicMasteries()
  );
  const [isAgentsModalOpen, setIsAgentsModalOpen] = useState(false);

  const handleNavigate = (screen: string, topicId?: string) => {
    if (topicId) setActiveTopicId(topicId);
    setCurrentScreen(screen);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleXpEarned = (amount: number) => {
    const updated = StorageService.addXp(amount);
    setProfile(updated);
  };

  return (
    <div className="min-h-screen bg-[#0B132B] text-slate-100 flex flex-col font-sans">
      <Navbar
        currentScreen={currentScreen}
        onNavigate={(screen, tId) => handleNavigate(screen, tId)}
        profile={profile}
        onOpenAgentsModal={() => setIsAgentsModalOpen(true)}
      />

      <main className="flex-1">
        {currentScreen === 'dashboard' && (
          <DashboardScreen
            onNavigateToTopic={(tId) => handleNavigate('topic_detail', tId)}
            onNavigateToPractice={(tId) => handleNavigate('practice', tId)}
            onNavigateToFsrs={(tId) => handleNavigate('fsrs', tId)}
            onNavigateToFormulaLab={() => handleNavigate('formula_lab')}
            onNavigateToSimulatedExam={() => handleNavigate('simulated_exam')}
            onOpenAgentsModal={() => setIsAgentsModalOpen(true)}
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
          />
        )}
      </main>

      {/* 7 Agents Modal */}
      <SevenAgentsModal
        isOpen={isAgentsModalOpen}
        onClose={() => setIsAgentsModalOpen(false)}
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
