/**
 * @module src/components/PWAInstallButton.tsx
 * @description In-app installation trigger for Progressive Web App (PWA) standalone home screen launch.
 */

import React, { useState } from 'react';
import { DownloadCloud, Smartphone, X, CheckCircle2 } from 'lucide-react';
import { usePWAInstall } from './usePWAInstall';

interface PWAInstallButtonProps {
  className?: string;
  variant?: 'nav' | 'card' | 'badge';
}

export const PWAInstallButton: React.FC<PWAInstallButtonProps> = ({
  className = '',
  variant = 'nav',
}) => {
  const { isInstallable, isInstalled, isIOS, install } = usePWAInstall();
  const [showIOSGuide, setShowIOSGuide] = useState(false);

  // If already running inside standalone PWA mode, suppress install prompt
  if (isInstalled) {
    return null;
  }

  // Chromium / Android / Desktop flow
  if (isInstallable) {
    if (variant === 'nav') {
      return (
        <button
          onClick={install}
          title="Install BioThanaweya to Home Screen (Offline PWA)"
          className={`flex items-center gap-1.5 px-2.5 py-1.5 rounded-lg glass-card border border-accent-cyan-border text-accent-cyan hover:bg-glass-dim transition text-xs font-bold ${className}`}
        >
          <DownloadCloud className="w-3.5 h-3.5 text-accent-cyan" />
          <span className="hidden sm:inline">Install App</span>
        </button>
      );
    }

    return (
      <button
        onClick={install}
        className={`flex items-center justify-center gap-2 px-4 py-2 rounded-xl btn-glass hover:text-white font-extrabold text-xs transition shadow-lg ${className}`}
      >
        <Smartphone className="w-4 h-4" />
        <span>Install to Android Home Screen (Offline)</span>
      </button>
    );
  }

  // iOS Safari flow
  if (isIOS) {
    return (
      <>
        <button
          onClick={() => setShowIOSGuide(true)}
          title="Install on iPhone / iPad"
          className={`flex items-center gap-1.5 px-2.5 py-1.5 rounded-lg glass-card border border-accent-cyan-border text-accent-cyan hover:bg-cyan-500/20 transition text-xs font-bold ${className}`}
        >
          <Smartphone className="w-3.5 h-3.5 text-cyan-400" />
          <span className="hidden sm:inline">Install iOS</span>
        </button>

        {showIOSGuide && (
          <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-sm p-4 animate-in fade-in">
            <div className="w-full max-w-sm rounded-2xl bg-secondary border border-cyan-500/30 p-6 shadow-2xl space-y-4">
              <div className="flex items-center justify-between pb-2 border-b border-slate-800">
                <div className="flex items-center gap-2 text-cyan-300 font-bold text-sm">
                  <Smartphone className="w-4 h-4 text-cyan-400" />
                  <span>Install on iPhone / iPad</span>
                </div>
                <button
                  onClick={() => setShowIOSGuide(false)}
                  className="p-1 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800"
                >
                  <X className="w-4 h-4" />
                </button>
              </div>

              <div className="space-y-3 text-xs text-slate-300 leading-relaxed">
                <div className="flex items-start gap-2.5">
                  <div className="w-5 h-5 rounded-full bg-cyan-500/20 text-cyan-300 flex items-center justify-center text-[10px] font-bold flex-shrink-0 mt-0.5">
                    1
                  </div>
                  <div>
                    Tap the <strong>Share</strong> icon (مشاركة) in the bottom Safari toolbar.
                  </div>
                </div>

                <div className="flex items-start gap-2.5">
                  <div className="w-5 h-5 rounded-full bg-cyan-500/20 text-cyan-300 flex items-center justify-center text-[10px] font-bold flex-shrink-0 mt-0.5">
                    2
                  </div>
                  <div>
                    Scroll down and select <strong>Add to Home Screen</strong> (إضافة إلى الصفحة
                    الرئيسية).
                  </div>
                </div>

                <div className="flex items-start gap-2.5">
                  <CheckCircle2 className="w-4 h-4 text-emerald-400 flex-shrink-0 mt-0.5" />
                  <div className="text-emerald-300 font-medium">
                    Enjoy offline biology revision with zero browser address bars!
                  </div>
                </div>
              </div>

              <button
                onClick={() => setShowIOSGuide(false)}
                className="w-full py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-white text-xs font-bold transition"
              >
                Done
              </button>
            </div>
          </div>
        )}
      </>
    );
  }

  return null;
};
