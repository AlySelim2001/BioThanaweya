import React from 'react';
import {
  BookOpen,
  Download,
  ExternalLink,
  FileText,
  ShieldCheck,
  Sparkles,
  X,
  CheckCircle2,
  HardDriveDownload,
} from 'lucide-react';
import { useLanguage } from '../context/LanguageContext';

interface MinistryTextbookModalProps {
  isOpen: boolean;
  onClose: () => void;
  targetChapterId?: string;
}

interface TextbookResource {
  id: string;
  titleEn: string;
  titleAr: string;
  category: 'textbook' | 'concepts' | 'questions';
  edition: string;
  size: string;
  url: string;
  descriptionEn: string;
  descriptionAr: string;
  highYieldTag: boolean;
}

export const MINISTRY_RESOURCES: TextbookResource[] = [
  {
    id: 'tb_languages_full',
    titleEn: 'Official Biology Textbook (Languages Track)',
    titleAr: 'كتاب الأحياء الرسمي لمدارس اللغات (Biology)',
    category: 'textbook',
    edition: '2025/2026 Edition',
    size: '34.2 MB',
    url: 'https://moe.gov.eg/media/q0rfeip5/biology_3sec.pdf',
    descriptionEn: 'Complete ministerial curriculum covering Units 1 & 2 with all diagrams, clinical boxes, and review questions.',
    descriptionAr: 'المنهج الكامل المعتمد من وزارة التربية والتعليم للثانوية العامة لغات متضمناً الرسوم والمصطلحات والأسئلة.',
    highYieldTag: true,
  },
  {
    id: 'tb_arabic_full',
    titleEn: 'Official Biology Textbook (Arabic Track)',
    titleAr: 'كتاب الأحياء الرسمي للثانوية العامة (النسخة العربية)',
    category: 'textbook',
    edition: '2025/2026 Edition',
    size: '31.8 MB',
    url: 'https://moe.gov.eg/media/biology_arabic_3sec.pdf',
    descriptionEn: 'The authoritative Egyptian Ministry curriculum textbook in Arabic for standard secondary school candidates.',
    descriptionAr: 'الكتاب المدرسي الأصلي المعتمد باللغة العربية مع كل التجارب والرسوم البيانية المعتمدة في امتحانات الثانوية.',
    highYieldTag: true,
  },
  {
    id: 'concepts_booklet_2026',
    titleEn: 'Official Concepts Booklet (مفاهيم الأحياء)',
    titleAr: 'كتيب المفاهيم الرسمي لامتحان الثانوية العامة',
    category: 'concepts',
    edition: 'Official Exam Booklet',
    size: '12.5 MB',
    url: 'https://moe.gov.eg/media/biology_concepts_booklet.pdf',
    descriptionEn: 'The exact open-book reference booklet provided to students inside the official Thanaweya examination hall.',
    descriptionAr: 'الكتيب الرسمي الذي يتم تسليمه للطالب داخل لجنة الامتحان ويحتوي على كافة القوانين والرسوم والتصنيفات.',
    highYieldTag: true,
  },
  {
    id: 'unit1_support_movement',
    titleEn: 'Unit 1: Support, Movement & Hormones Guide',
    titleAr: 'الوحدة الأولى: الدعامة والحركة والتنسيق الهرموني',
    category: 'questions',
    edition: 'Ministry Question Bank',
    size: '8.4 MB',
    url: 'https://moe.gov.eg/media/unit1_biology_guide.pdf',
    descriptionEn: 'Targeted chapter excerpts with official Egyptian mark schemes and model answer keyword distribution.',
    descriptionAr: 'مذكرة الوزارة التوجيهية للوحدة الأولى مع توزيع الدرجات ونماذج الإجابة لأسئلة المقال والاختيار.',
    highYieldTag: false,
  },
  {
    id: 'unit2_molecular_dna',
    titleEn: 'Unit 2: Molecular Biology & Genetic Engineering',
    titleAr: 'الوحدة الثانية: البيولوجيا الجزيئية والهندسة الوراثية',
    category: 'questions',
    edition: 'Ministry Question Bank',
    size: '9.1 MB',
    url: 'https://moe.gov.eg/media/unit2_molecular_biology.pdf',
    descriptionEn: 'Complete DNA/RNA molecular biology chapter with Chargaff formulas, codons, and PCR techniques.',
    descriptionAr: 'ملخص الوزارة للبيولوجيا الجزيئية ومسائل شارجاف ومضادات الكودونات وتطبيقات الهندسة الوراثية.',
    highYieldTag: false,
  },
];

export const MinistryTextbookModal: React.FC<MinistryTextbookModalProps> = ({
  isOpen,
  onClose,
}) => {
  const { language, isRtl, t } = useLanguage();

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-md animate-in fade-in duration-200">
      <div 
        className="relative w-full max-w-3xl max-h-[90vh] flex flex-col rounded-3xl bg-slate-900 border border-teal-500/30 shadow-2xl shadow-teal-500/10 overflow-hidden"
        dir={isRtl ? 'rtl' : 'ltr'}
      >
        {/* Glow Header */}
        <div className="relative px-6 py-5 border-b border-slate-800 bg-gradient-to-r from-teal-950/40 via-slate-900 to-cyan-950/40 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-11 h-11 rounded-2xl bg-teal-500/20 border border-teal-500/30 flex items-center justify-center text-teal-300 shadow-inner">
              <BookOpen className="w-6 h-6" />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <h2 className="text-lg md:text-xl font-bold text-white tracking-tight">
                  {t('modalTitle')}
                </h2>
                <span className="hidden sm:inline-flex items-center gap-1 text-[11px] font-bold px-2 py-0.5 rounded-full bg-emerald-500/15 text-emerald-300 border border-emerald-500/30">
                  <ShieldCheck className="w-3 h-3" />
                  {language === 'ar' ? 'معتمد رسمياً' : 'Verified MOE'}
                </span>
              </div>
              <p className="text-xs text-slate-400 mt-0.5">
                {t('modalDesc')}
              </p>
            </div>
          </div>

          <button
            onClick={onClose}
            className="p-2 rounded-xl text-slate-400 hover:text-white hover:bg-slate-800/80 transition"
            aria-label="Close"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Content Body */}
        <div className="flex-1 overflow-y-auto p-6 space-y-4">
          {/* Offline Architecture Callout */}
          <div className="rounded-2xl bg-cyan-950/30 border border-cyan-500/30 p-4 flex items-start gap-3">
            <Sparkles className="w-5 h-5 text-cyan-400 shrink-0 mt-0.5" />
            <div className="text-xs text-cyan-200/90 leading-relaxed">
              <p className="font-semibold text-cyan-100 mb-1">
                {t('offlineTip')}
              </p>
              <p className="text-slate-400">
                {language === 'ar'
                  ? 'بإمكانك تنزيل ملفات الـ PDF وحفظها في جهازك للرجوع إليها في أي وقت دون الحاجة لشبكة الإنترنت أثناء المذاكرة.'
                  : 'You can download the PDF files directly to your device for uninterrupted offline revision.'}
              </p>
            </div>
          </div>

          {/* Resources List */}
          <div className="space-y-3 pt-1">
            {MINISTRY_RESOURCES.map((item) => (
              <div
                key={item.id}
                className="group relative rounded-2xl bg-slate-950/60 hover:bg-slate-800/50 border border-slate-800 hover:border-teal-500/40 p-4 transition duration-200 flex flex-col sm:flex-row sm:items-center justify-between gap-4"
              >
                <div className="flex items-start gap-3.5">
                  <div className="w-10 h-10 rounded-xl bg-slate-800/80 border border-slate-700/60 flex items-center justify-center text-teal-400 group-hover:scale-105 transition shrink-0 mt-0.5">
                    <FileText className="w-5 h-5" />
                  </div>
                  <div className="space-y-1">
                    <div className="flex flex-wrap items-center gap-2">
                      <h3 className="font-bold text-sm text-slate-100 group-hover:text-teal-300 transition">
                        {language === 'ar' ? item.titleAr : item.titleEn}
                      </h3>
                      {item.highYieldTag && (
                        <span className="text-[10px] font-extrabold px-2 py-0.5 rounded-full bg-amber-500/15 text-amber-300 border border-amber-500/30">
                          {language === 'ar' ? 'الأهم للامتحان' : 'High-Yield'}
                        </span>
                      )}
                    </div>
                    <p className="text-xs text-slate-400 line-clamp-2">
                      {language === 'ar' ? item.descriptionAr : item.descriptionEn}
                    </p>
                    <div className="flex items-center gap-3 text-[11px] text-slate-500 font-medium pt-1">
                      <span>{item.edition}</span>
                      <span>•</span>
                      <span>{item.size}</span>
                      <span>•</span>
                      <span className="text-emerald-400 flex items-center gap-1">
                        <CheckCircle2 className="w-3 h-3" />
                        {language === 'ar' ? 'جاهز للتحميل' : 'Ready'}
                      </span>
                    </div>
                  </div>
                </div>

                {/* Direct Action Link */}
                <a
                  href={item.url}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl bg-teal-500/20 hover:bg-teal-500 text-teal-300 hover:text-slate-950 border border-teal-500/40 font-bold text-xs shadow-md transition shrink-0 self-stretch sm:self-center"
                >
                  <Download className="w-4 h-4" />
                  <span>{language === 'ar' ? 'تحميل مباشر PDF' : 'Direct Download'}</span>
                  <ExternalLink className="w-3 h-3 opacity-60" />
                </a>
              </div>
            ))}
          </div>
        </div>

        {/* Footer */}
        <div className="px-6 py-4 border-t border-slate-800 bg-slate-950/70 flex items-center justify-between">
          <span className="text-xs text-slate-500">
            {language === 'ar' ? 'وزارة التربية والتعليم والتعليم الفني - جمهورية مصر العربية' : 'Ministry of Education & Technical Education - Egypt'}
          </span>
          <button
            onClick={onClose}
            className="px-5 py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-semibold transition"
          >
            {t('closeBtn')}
          </button>
        </div>
      </div>
    </div>
  );
};
