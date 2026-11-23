import React, { createContext, useContext, useState, useEffect } from 'react';

export type Language = 'ar' | 'en';

interface LanguageContextType {
  language: Language;
  setLanguage: (lang: Language) => void;
  toggleLanguage: () => void;
  isRtl: boolean;
  t: (key: string) => string;
}

const STORAGE_KEY = 'PLACEHOLDER_KEY';

const DICTIONARY: Record<string, { ar: string; en: string }> = {
  // Brand & Header
  appName: { ar: 'تعلم مع علي | أحياء الثانوية العامة', en: 'Learn with Aly | BioThanaweya' },
  appSub: {
    ar: 'منظومة الأحياء التفاعلية للثانوية العامة • 7 وكلاء ذكاء اصطناعي',
    en: 'Thanaweya Amma Biology Ecosystem • 7 AI Agents',
  },
  textbookBtn: { ar: 'تحميل كتاب الوزارة', en: 'Ministry Textbooks' },
  textbookBadge: { ar: 'الرسمي', en: 'Official' },
  languageLabel: { ar: 'English', en: 'العربية' },

  // Navigation
  dashboard: { ar: 'الرئيسية', en: 'Dashboard' },
  simLab: { ar: 'معمل المحاكاة', en: 'Sim Lab' },
  formulaLab: { ar: 'معمل القوانين', en: 'Formula Lab' },
  fsrsCards: { ar: 'بطاقات التكرار', en: 'FSRS Cards' },
  ministryExam: { ar: 'امتحان الوزارة', en: 'Ministry Exam' },
  agents: { ar: 'وكلاء AI', en: 'AI Agents' },
  vault: { ar: 'الخزينة', en: 'Vault' },

  // Hero Section
  heroBadge: {
    ar: 'منظومة الذكاء الاصطناعي التعليمية • 7 وكلاء متاحون',
    en: '7-AGENT AI LEARNING ECOSYSTEM • ACTIVE',
  },
  heroTitle: {
    ar: 'أحياء الثانوية العامة (الوحدتان الأولى والثانية)',
    en: 'Thanaweya Biology (Units 1 & 2)',
  },
  heroSubtitle: {
    ar: 'مبني ومطابق حرفياً لكتاب الوزارة الرسمي مع نماذج الإجابة المعتمدة، وتحليلات عميقة باللغتين العربية والإنجليزية، وتكرار متباعد FSRS لحفظ المنهج حتى يوم الامتحان.',
    en: 'Strictly grounded in the official Egyptian Ministry textbook. Deep English technical breakdowns, Arabic analogies, official Mark Scheme rubrics & FSRS memory retention.',
  },
  practiceItemsBtn: { ar: 'بدء التدريب الامتحاني', en: 'Practice Exam Items' },
  fsrsCardsBtn: { ar: 'بطاقات الاسترجاع FSRS', en: 'FSRS Flashcards' },
  bioLabBtn: { ar: 'معمل القوانين والمسائل', en: 'Bio-Law Lab' },

  // Simulated Exam Hero
  examHeroTitle: {
    ar: 'شاشة امتحان الثانوية العامة التجريبي 2026',
    en: 'Simulated Ministry Exam Mode 2026',
  },
  examHeroSpecs: {
    ar: 'مؤقت 45 دقيقة • 15 سؤالاً وزارياً منتقى • تصحيح بنموذج الإجابة • إجابة صوتية',
    en: '45-Min Timed • 15 Authentic Ministry Items • Mark Scheme Grading • Voice Answering',
  },
  examHeroDesc: {
    ar: 'عِش تجربة الامتحان الفعلي مع مؤقت العد التنازلي التفاعلي، ورصد الكلمات المفتاحية الوزارية، وتنبيهات الأخطاء الشائعة واستشهادات كتاب الوزارة.',
    en: 'Experience the authentic Egyptian exam environment with strict countdown timer, Ministry keyword evaluation, trap warnings, and official textbook citations.',
  },
  launchExamBtn: { ar: 'دخول الامتحان التجريبي', en: 'Launch Ministry Simulation' },

  // Bento Units Header
  curriculumBentoTitle: {
    ar: 'خريطة فصول وموضوعات المنهج الوزاري',
    en: 'Curriculum Bento Grid & Chapter Navigator',
  },
  curriculumBentoSub: {
    ar: 'اختر أي موضوع لاستكشاف الأسئلة الوزارية، بطاقات FSRS، والآليات التفاعلية:',
    en: 'Select any high-yield topic to access questions, flashcards, and conceptual breakdowns:',
  },
  allFilter: { ar: 'جميع الفصول', en: 'All Chapters' },
  unit1Filter: { ar: 'الوحدة الأولى: التركيب والوظيفة', en: 'Unit 1: Structure & Function' },
  unit2Filter: { ar: 'الوحدة الثانية: البيولوجيا الجزيئية', en: 'Unit 2: Molecular Biology' },
  highYieldBadge: { ar: '⚡ عالي الأهمية', en: '⚡ High-Yield' },

  // Bento Card Actions
  btnSolve: { ar: 'حل أسئلة', en: 'Practice' },
  btnCards: { ar: 'بطاقات', en: 'Flashcards' },
  btnExplore: { ar: 'الشرح والمعمل', en: 'Explore' },
  btnDownloadPdf: { ar: 'كتاب الوزارة', en: 'PDF Book' },

  // Textbook Modal
  modalTitle: {
    ar: 'كتب الوزارة الرسمية وكتيب المفاهيم (تحميل مباشر)',
    en: 'Official Ministry Textbooks & Concepts Booklet',
  },
  modalDesc: {
    ar: 'حمّل أحدث نسخ معتمدة من وزارة التربية والتعليم المصرية للمراجعة الذاتية والحفظ دون إنترنت.',
    en: 'Download the latest official Egyptian Ministry of Education PDF textbooks for offline study.',
  },
  offlineTip: {
    ar: '💡 نصيحة الأوفلاين: جميع هذه الكتب والأسئلة يتم حفظها تلقائياً داخل ذاكرة التطبيق (IndexedDB) للعمل دون اتصال.',
    en: '💡 Offline Tip: All study packs and exam items are cached locally via IndexedDB for zero-data offline review.',
  },
  closeBtn: { ar: 'إغلاق', en: 'Close' },
};

const LanguageContext = createContext<LanguageContextType | undefined>(undefined);

export const LanguageProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [language, setLanguageState] = useState<Language>(() => {
    if (typeof window !== 'undefined') {
      const saved = localStorage.getItem(STORAGE_KEY) as Language;
      if (saved === 'ar' || saved === 'en') return saved;
    }
    return 'ar'; // Default to Arabic for Egyptian students
  });

  const isRtl = language === 'ar';

  useEffect(() => {
    if (typeof window !== 'undefined') {
      localStorage.setItem(STORAGE_KEY, language);
      document.documentElement.dir = isRtl ? 'rtl' : 'ltr';
      document.documentElement.lang = language;
    }
  }, [language, isRtl]);

  const setLanguage = (lang: Language) => {
    setLanguageState(lang);
  };

  const toggleLanguage = () => {
    setLanguageState((prev) => (prev === 'ar' ? 'en' : 'ar'));
  };

  const t = (key: string): string => {
    const entry = DICTIONARY[key];
    if (!entry) return key;
    return entry[language] || entry.en || key;
  };

  return (
    <LanguageContext.Provider value={{ language, setLanguage, toggleLanguage, isRtl, t }}>
      {children}
    </LanguageContext.Provider>
  );
};

export const useLanguage = (): LanguageContextType => {
  const context = useContext(LanguageContext);
  if (!context) {
    throw new Error('useLanguage must be used within a LanguageProvider');
  }
  return context;
};
