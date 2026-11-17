import { useState, useEffect, type FC } from 'react';
import { BookOpen, Dna, Wifi, WifiOff } from 'lucide-react';

interface NavbarProps {
  currentView: 'selector' | 'exam';
  paperTitle?: string;
  onNavigateHome: () => void;
}

export const Navbar: FC<NavbarProps> = ({ currentView, paperTitle, onNavigateHome }) => {
  const [isOnline, setIsOnline] = useState<boolean>(navigator.onLine);

  useEffect(() => {
    const handleOnline = () => setIsOnline(true);
    const handleOffline = () => setIsOnline(false);

    window.addEventListener('online', handleOnline);
    window.addEventListener('offline', handleOffline);

    return () => {
      window.removeEventListener('online', handleOnline);
      window.removeEventListener('offline', handleOffline);
    };
  }, []);

  return (
    <header className="sticky top-0 z-40 w-full backdrop-blur-xl bg-obsidian-900/90 border-b border-slate-800/80 px-4 py-3 transition-all">
      <div className="max-w-7xl mx-auto flex items-center justify-between gap-4">
        {/* Brand & Logo */}
        <div
          onClick={onNavigateHome}
          className="flex items-center gap-3 cursor-pointer group select-none"
        >
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-bioteal/20 via-bioteal/10 to-transparent border border-bioteal/40 flex items-center justify-center text-bioteal shadow-lg shadow-bioteal/10 group-hover:scale-105 transition-transform">
            <Dna className="w-6 h-6 animate-pulse" />
          </div>
          <div>
            <div className="flex items-center gap-2">
              <span className="font-extrabold text-lg tracking-tight bg-gradient-to-r from-white via-slate-200 to-bioteal bg-clip-text text-transparent">
                BioThanaweya
              </span>
              <span className="text-[10px] px-1.5 py-0.5 rounded-full font-bold bg-bioteal/20 text-bioteal border border-bioteal/30">
                PWA
              </span>
            </div>
            <p className="text-[11px] text-slate-400 font-medium">
              محاكي امتحانات الأحياء للثانوية العامة
            </p>
          </div>
        </div>

        {/* Breadcrumb / Title display if in exam */}
        {currentView !== 'selector' && paperTitle && (
          <div className="hidden md:flex items-center gap-2 px-3 py-1.5 rounded-lg bg-obsidian-800/80 border border-slate-700/60 max-w-md truncate">
            <BookOpen className="w-4 h-4 text-bioteal shrink-0" />
            <span className="text-xs font-semibold text-slate-200 truncate">{paperTitle}</span>
          </div>
        )}

        {/* Offline Badge & Status */}
        <div className="flex items-center gap-2.5">
          <div
            title={isOnline ? 'متصل بالشبكة (يتم العمل محلياً)' : 'وضع عدم الاتصال نشط 100%'}
            className={`flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-bold border transition-colors ${
              isOnline
                ? 'bg-bioemerald/10 border-bioemerald/30 text-bioemerald'
                : 'bg-biotrap-500/10 border-biotrap-500/30 text-biotrap-500'
            }`}
          >
            {isOnline ? (
              <>
                <Wifi className="w-3.5 h-3.5" />
                <span className="hidden sm:inline">محلي نشط</span>
              </>
            ) : (
              <>
                <WifiOff className="w-3.5 h-3.5 animate-bounce" />
                <span>دون إنترنت</span>
              </>
            )}
          </div>

          {currentView !== 'selector' && (
            <button
              onClick={onNavigateHome}
              className="text-xs font-bold text-slate-300 hover:text-white px-3 py-1.5 rounded-lg bg-obsidian-800 border border-slate-700 hover:border-slate-600 transition-colors"
            >
              الرئيسية
            </button>
          )}
        </div>
      </div>
    </header>
  );
};
