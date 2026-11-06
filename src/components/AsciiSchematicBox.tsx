import React, { useState } from 'react';
import { Copy, Check, Terminal } from 'lucide-react';

interface AsciiSchematicBoxProps {
  title: string;
  asciiContent: string;
  caption: string;
}

export const AsciiSchematicBox: React.FC<AsciiSchematicBoxProps> = ({
  title,
  asciiContent,
  caption,
}) => {
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(asciiContent);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="rounded-2xl border border-cyan-500/30 bg-[#070D1E] overflow-hidden shadow-xl shadow-cyan-950/20">
      {/* Schematic Header */}
      <div className="flex items-center justify-between px-4 py-2.5 bg-slate-900 border-b border-slate-800">
        <div className="flex items-center gap-2">
          <Terminal className="w-4 h-4 text-cyan-400" />
          <span className="text-xs font-bold text-slate-200 tracking-wide">{title}</span>
          <span className="text-[10px] font-mono px-1.5 py-0.5 rounded bg-cyan-500/10 text-cyan-300 border border-cyan-500/20">
            LTR Monospace
          </span>
        </div>
        <button
          onClick={handleCopy}
          className="flex items-center gap-1.5 text-xs text-slate-400 hover:text-white px-2 py-1 rounded bg-slate-800 hover:bg-slate-700 transition"
        >
          {copied ? (
            <>
              <Check className="w-3.5 h-3.5 text-emerald-400" />
              <span className="text-emerald-400">Copied</span>
            </>
          ) : (
            <>
              <Copy className="w-3.5 h-3.5" />
              <span>Copy</span>
            </>
          )}
        </button>
      </div>

      {/* Code Container with Strict LTR */}
      <div className="p-4 overflow-x-auto" dir="ltr">
        <pre className="font-mono-code text-xs md:text-sm text-cyan-300 leading-relaxed whitespace-pre font-medium selection:bg-cyan-600 selection:text-white">
          {asciiContent}
        </pre>
      </div>

      {/* Caption Footer */}
      {caption && (
        <div className="px-4 py-2.5 bg-cyan-950/30 border-t border-cyan-900/30 text-xs text-slate-300 flex items-start gap-2">
          <span className="font-bold text-cyan-400 shrink-0">Caption:</span>
          <p className="leading-relaxed">{caption}</p>
        </div>
      )}
    </div>
  );
};
