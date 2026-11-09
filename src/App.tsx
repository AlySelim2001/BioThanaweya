import { useState } from 'react';
import { Navbar } from './components/layout/Navbar';
import { ExamPaperSelector } from './components/exam/ExamPaperSelector';
import { ExamSimulatorScreen } from './components/exam/ExamSimulatorScreen';
import { ShieldCheck, Heart } from 'lucide-react';

export function App() {
  const [currentView, setCurrentView] = useState<'selector' | 'exam'>('selector');
  const [selectedPaperId, setSelectedPaperId] = useState<string | null>(null);

  const handleSelectPaper = (paperId: string) => {
    setSelectedPaperId(paperId);
    setCurrentView('exam');
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleNavigateHome = () => {
    setCurrentView('selector');
    setSelectedPaperId(null);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div className="min-h-screen bg-obsidian-900 text-slate-100 flex flex-col font-cairo">
      {/* Top Navigation */}
      <Navbar
        currentView={currentView}
        onNavigateHome={handleNavigateHome}
      />

      {/* Main Content Area */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 py-6">
        {currentView === 'selector' && (
          <ExamPaperSelector onSelectPaper={handleSelectPaper} />
        )}

        {currentView === 'exam' && selectedPaperId && (
          <ExamSimulatorScreen
            paperId={selectedPaperId}
            onBackToSelector={handleNavigateHome}
          />
        )}
      </main>

      {/* Footer */}
      <footer className="mt-auto border-t border-slate-800/80 bg-obsidian-950/80 py-6 px-4 text-center text-xs text-slate-400">
        <div className="max-w-7xl mx-auto flex flex-col sm:flex-row items-center justify-between gap-3">
          <div className="flex items-center gap-1.5 font-medium">
            <ShieldCheck className="w-4 h-4 text-bioteal" />
            <span>نظام محاكي الأحياء للثانوية العامة • متوافق مع نظام التقييم الوزاري المصري</span>
          </div>

          <div className="flex items-center gap-1 text-[11px] text-slate-500">
            <span>صُمم بإتقان لدعم طلاب الثانوية العامة في مصر</span>
            <Heart className="w-3.5 h-3.5 text-red-500 fill-red-500" />
            <span>100% Offline-First</span>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default App;
