import type { FC } from 'react';

interface SvgBioDiagramProps {
  diagramKey: 'sarcomere' | 'antibody' | 'dna_replication' | 'pollen_tube' | 'feedback_loop';
  captionArabic?: string;
  className?: string;
}

export const SvgBioDiagram: FC<SvgBioDiagramProps> = ({
  diagramKey,
  captionArabic,
  className = ''
}) => {
  return (
    <div className={`p-4 rounded-2xl bg-obsidian-950/80 border border-slate-700/60 shadow-inner flex flex-col items-center justify-center my-3 ${className}`}>
      {diagramKey === 'sarcomere' && (
        <svg viewBox="0 0 500 160" className="w-full max-w-md h-auto select-none" xmlns="http://www.w3.org/2000/svg">
          {/* Background and grid */}
          <rect width="500" height="160" rx="12" fill="#0A192F" />
          
          {/* Z lines */}
          <line x1="60" y1="20" x2="60" y2="130" stroke="#EF4444" strokeWidth="5" strokeLinecap="round" />
          <line x1="440" y1="20" x2="440" y2="130" stroke="#EF4444" strokeWidth="5" strokeLinecap="round" />
          <text x="60" y="150" fill="#EF4444" fontSize="12" textAnchor="middle" fontWeight="bold">خط Z</text>
          <text x="440" y="150" fill="#EF4444" fontSize="12" textAnchor="middle" fontWeight="bold">خط Z</text>

          {/* Thin Actin filaments */}
          <line x1="60" y1="45" x2="220" y2="45" stroke="#64FFDA" strokeWidth="3" />
          <line x1="60" y1="105" x2="220" y2="105" stroke="#64FFDA" strokeWidth="3" />
          <line x1="440" y1="45" x2="280" y2="45" stroke="#64FFDA" strokeWidth="3" />
          <line x1="440" y1="105" x2="280" y2="105" stroke="#64FFDA" strokeWidth="3" />

          {/* Thick Myosin filaments */}
          <line x1="160" y1="75" x2="340" y2="75" stroke="#F59E0B" strokeWidth="8" strokeLinecap="round" />
          <text x="250" y="70" fill="#F59E0B" fontSize="12" textAnchor="middle" fontWeight="bold">خيط ميوسين سميك</text>

          {/* Cross-Bridges */}
          <path d="M 180 75 Q 190 60 200 48" stroke="#10B981" strokeWidth="2.5" fill="none" strokeLinecap="round" />
          <path d="M 320 75 Q 310 60 300 48" stroke="#10B981" strokeWidth="2.5" fill="none" strokeLinecap="round" />
          <path d="M 180 75 Q 190 90 200 102" stroke="#10B981" strokeWidth="2.5" fill="none" strokeLinecap="round" />
          <path d="M 320 75 Q 310 90 300 102" stroke="#10B981" strokeWidth="2.5" fill="none" strokeLinecap="round" />
          <circle cx="200" cy="48" r="4" fill="#10B981" />
          <circle cx="300" cy="48" r="4" fill="#10B981" />

          {/* H Zone Bracket */}
          <line x1="220" y1="120" x2="280" y2="120" stroke="#94A3B8" strokeWidth="1.5" strokeDasharray="3,3" />
          <text x="250" y="138" fill="#94A3B8" fontSize="11" textAnchor="middle">المنطقة شبه المضيئة (H)</text>

          {/* Sarcomere label */}
          <text x="250" y="22" fill="#64FFDA" fontSize="13" textAnchor="middle" fontWeight="bold">القطعة العضلية (الساركومير)</text>
        </svg>
      )}

      {diagramKey === 'antibody' && (
        <svg viewBox="0 0 400 200" className="w-full max-w-md h-auto select-none" xmlns="http://www.w3.org/2000/svg">
          <rect width="400" height="200" rx="12" fill="#0A192F" />
          
          {/* Heavy Chains (Orange/Amber) */}
          <path d="M 175 180 L 175 110 L 90 40" stroke="#F59E0B" strokeWidth="7" fill="none" strokeLinecap="round" />
          <path d="M 225 180 L 225 110 L 310 40" stroke="#F59E0B" strokeWidth="7" fill="none" strokeLinecap="round" />

          {/* Light Chains (Cyan) */}
          <path d="M 125 110 L 60 55" stroke="#64FFDA" strokeWidth="6" fill="none" strokeLinecap="round" />
          <path d="M 275 110 L 340 55" stroke="#64FFDA" strokeWidth="6" fill="none" strokeLinecap="round" />

          {/* Disulfide bonds (S-S) */}
          {/* Between heavy chains */}
          <line x1="175" y1="125" x2="225" y2="125" stroke="#EF4444" strokeWidth="3" strokeDasharray="2,2" />
          <line x1="175" y1="145" x2="225" y2="145" stroke="#EF4444" strokeWidth="3" strokeDasharray="2,2" />
          
          {/* Between heavy and light chains */}
          <line x1="140" y1="80" x2="110" y2="70" stroke="#EF4444" strokeWidth="3" strokeDasharray="2,2" />
          <line x1="260" y1="80" x2="290" y2="70" stroke="#EF4444" strokeWidth="3" strokeDasharray="2,2" />

          {/* Antigen binding sites */}
          <circle cx="75" cy="48" r="10" fill="none" stroke="#00F5D4" strokeWidth="2.5" />
          <circle cx="325" cy="48" r="10" fill="none" stroke="#00F5D4" strokeWidth="2.5" />
          <text x="75" y="28" fill="#00F5D4" fontSize="11" textAnchor="middle" fontWeight="bold">موقع الارتباط بالأنتيجين</text>
          <text x="325" y="28" fill="#00F5D4" fontSize="11" textAnchor="middle" fontWeight="bold">موقع الارتباط بالأنتيجين</text>

          {/* Labels */}
          <text x="200" y="195" fill="#F8FAFC" fontSize="12" textAnchor="middle" fontWeight="bold">المنطقة الثابتة (Fc)</text>
          <text x="200" y="137" fill="#EF4444" fontSize="10" textAnchor="middle">روابط كبريتيدية ثنائية (4)</text>
        </svg>
      )}

      {diagramKey === 'dna_replication' && (
        <svg viewBox="0 0 450 180" className="w-full max-w-md h-auto select-none" xmlns="http://www.w3.org/2000/svg">
          <rect width="450" height="180" rx="12" fill="#0A192F" />

          {/* Unwound template strands */}
          <path d="M 50 40 L 200 40 L 380 20" stroke="#64FFDA" strokeWidth="4" fill="none" />
          <path d="M 50 140 L 200 140 L 380 160" stroke="#64FFDA" strokeWidth="4" fill="none" />
          
          {/* Direction labels */}
          <text x="35" y="45" fill="#64FFDA" fontSize="12" fontWeight="bold">5'</text>
          <text x="35" y="145" fill="#64FFDA" fontSize="12" fontWeight="bold">3'</text>
          <text x="395" y="25" fill="#64FFDA" fontSize="12" fontWeight="bold">3'</text>
          <text x="395" y="165" fill="#64FFDA" fontSize="12" fontWeight="bold">5'</text>

          {/* Helicase Enzyme */}
          <polygon points="210,60 250,90 210,120" fill="#EF4444" />
          <text x="205" y="94" fill="#FFFFFF" fontSize="11" textAnchor="middle" fontWeight="bold">إنزيم اللولب</text>

          {/* Hydrogen bonds */}
          <line x1="80" y1="42" x2="80" y2="138" stroke="#94A3B8" strokeWidth="2" strokeDasharray="3,3" />
          <line x1="120" y1="42" x2="120" y2="138" stroke="#94A3B8" strokeWidth="2" strokeDasharray="3,3" />
          <line x1="160" y1="42" x2="160" y2="138" stroke="#94A3B8" strokeWidth="2" strokeDasharray="3,3" />

          {/* Newly synthesized strands */}
          <line x1="260" y1="36" x2="360" y2="28" stroke="#10B981" strokeWidth="3" />
          <text x="310" y="55" fill="#10B981" fontSize="11" textAnchor="middle">الشريط المتقدم (مستمر)</text>

          <text x="225" y="172" fill="#94A3B8" fontSize="12" textAnchor="middle">شوكة التضاعف (Replication Fork)</text>
        </svg>
      )}

      {diagramKey === 'pollen_tube' && (
        <svg viewBox="0 0 400 180" className="w-full max-w-md h-auto select-none" xmlns="http://www.w3.org/2000/svg">
          <rect width="400" height="180" rx="12" fill="#0A192F" />

          {/* Pollen Grain */}
          <circle cx="80" cy="90" r="30" fill="#F59E0B" stroke="#D97706" strokeWidth="3" />
          <text x="80" y="95" fill="#070F1E" fontSize="12" textAnchor="middle" fontWeight="bold">حبة لقاح</text>

          {/* Growing pollen tube */}
          <path d="M 110 90 Q 220 90 320 90" stroke="#F59E0B" strokeWidth="18" strokeLinecap="round" fill="none" />
          <path d="M 110 90 Q 220 90 320 90" stroke="#112240" strokeWidth="10" strokeLinecap="round" fill="none" />

          {/* Two Sperm Nuclei */}
          <circle cx="270" cy="90" r="5" fill="#64FFDA" />
          <circle cx="290" cy="90" r="5" fill="#64FFDA" />
          <text x="280" y="68" fill="#64FFDA" fontSize="11" textAnchor="middle" fontWeight="bold">نواتان زكريتان (n, n)</text>

          {/* Tube Nucleus */}
          <circle cx="312" cy="90" r="4" fill="#EF4444" />
          <text x="312" y="118" fill="#EF4444" fontSize="10" textAnchor="middle">نواة الأنبوبة</text>

          <text x="200" y="165" fill="#F8FAFC" fontSize="12" textAnchor="middle">إنبات حبة اللقاح والإخصاب المزدوج</text>
        </svg>
      )}

      {diagramKey === 'feedback_loop' && (
        <svg viewBox="0 0 450 170" className="w-full max-w-md h-auto select-none" xmlns="http://www.w3.org/2000/svg">
          <rect width="450" height="170" rx="12" fill="#0A192F" />

          {/* Stimulus */}
          <rect x="20" y="30" width="100" height="45" rx="8" fill="#1E3A5F" stroke="#64FFDA" strokeWidth="1.5" />
          <text x="70" y="58" fill="#64FFDA" fontSize="11" textAnchor="middle" fontWeight="bold">ارتفاع أسموزية الدم</text>

          {/* Pituitary */}
          <rect x="175" y="30" width="100" height="45" rx="8" fill="#1E3A5F" stroke="#F59E0B" strokeWidth="1.5" />
          <text x="225" y="58" fill="#F59E0B" fontSize="11" textAnchor="middle" fontWeight="bold">إفراز هرمون ADH</text>

          {/* Kidney Tubules */}
          <rect x="330" y="30" width="100" height="45" rx="8" fill="#1E3A5F" stroke="#10B981" strokeWidth="1.5" />
          <text x="380" y="58" fill="#10B981" fontSize="11" textAnchor="middle" fontWeight="bold">إعادة امتصاص الماء</text>

          {/* Arrows */}
          <line x1="120" y1="52" x2="175" y2="52" stroke="#64FFDA" strokeWidth="2" markerEnd="url(#arrow)" />
          <line x1="275" y1="52" x2="330" y2="52" stroke="#F59E0B" strokeWidth="2" />

          {/* Negative Feedback Loop back */}
          <path d="M 380 75 L 380 125 L 70 125 L 70 75" fill="none" stroke="#EF4444" strokeWidth="2" strokeDasharray="4,4" />
          <text x="225" y="145" fill="#EF4444" fontSize="11" textAnchor="middle" fontWeight="bold">تغذية راجعة سلبية (تثبيط)</text>
        </svg>
      )}

      {captionArabic && (
        <p className="mt-2 text-xs text-slate-400 font-medium text-center">
          {captionArabic}
        </p>
      )}
    </div>
  );
};
