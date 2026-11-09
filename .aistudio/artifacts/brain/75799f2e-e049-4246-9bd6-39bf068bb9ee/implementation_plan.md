# BioThanaweya: Offline-First PWA & Capacitor Exam Simulator

A production-grade, local-first educational web and mobile application for Egyptian Thanaweya Amma Biology students, engineered with React, TypeScript, Tailwind CSS, Vite, Dexie.js (IndexedDB), and Capacitor for seamless Android bundling and zero-latency offline performance.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The architectural directions below were confirmed in Phase 1 clarification:
> - **Target Stack**: Production React + TypeScript + Vite + Tailwind CSS PWA, architected cleanly to package into Android via Capacitor and Gradle.
> - **Priority Module**: Comprehensive Ministry Exam Simulator (محاكي الامتحانات الوزارية للثانوية العامة) with 60-mark timing, offline instant scoring, bubble sheet view, exam trap analysis, and detailed rubrics.
> - **Local-First Storage Engine**: IndexedDB powered by **Dexie.js** with schema versioning, reactive live queries (`useLiveQuery`), real-time auto-save, and offline resilience.

- **Confirmed Decision 1**: Establish a modular **Clean Architecture** structure (`types/`, `db/`, `services/`, `hooks/`, `components/`) ensuring strict separation of concerns, complete type safety, zero `any`, and zero `// TODO` stubs.
- **Confirmed Decision 2**: Implement an offline-first **Dexie.js** database pre-populated with official Egyptian Ministry Biology exam papers (الدور الأول، الدور الثاني، والنماذج الاسترشادية)، question banks, trap explanations, and student attempt histories.
- **Confirmed Decision 3**: Deliver an immersive **OLED Dark / Glassmorphism** interface adhering to Egyptian Ministry standards with full RTL Arabic typography (Cairo), interactive bubble sheet, countdown pacing timer, and instant diagnostic report cards.
- **Confirmed Decision 4**: Maintain 100% build integrity across both the web toolchain (`npm run build`) and the Android Gradle pipeline (`gradle :app:assembleDebug`).

---

## 1. Overview & Core Concept

- **What It Does**: Provides Egyptian 3rd Secondary (Thanaweya Amma) biology students with an authentic, offline-first exam simulation environment. Students can take timed 60-mark official ministerial exams, fill in digital bubble sheets, review trap questions (فخاخ الامتحانات), and receive instant rubric-aligned grading and performance analytics—even in airplane mode.
- **Target Audience / Persona**: High school biology students across Egypt, science teachers, and tutors who need reliable, zero-latency exam practice without relying on unstable internet connections.
- **Key Value**: 100% offline autonomy, sub-millisecond answer recording, authentic ministerial exam formats, and zero risk of exam progress loss during sudden disconnects or power cuts.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **Exam Selection & Setup**: Student selects an official exam paper (e.g., *ثانوية عامة 2024 - الدور الأول* or *النموذج الاسترشادي الوزاري*), configures the countdown timer (standard 180 min or custom quick drill), and toggles pacing alerts.
2. **Active Simulation & Bubble Sheet**: 
   - Split view or toggleable layout displaying the active biology question with high-contrast diagrams, bilingual scientific terms, and 4 radio choices.
   - Interactive 50-item **Bubble Sheet** allowing rapid jumping, flagging doubtful questions (تعليم للمراجعة), and immediate visual feedback on answered vs. unattempted items.
3. **Real-time Local Auto-Save**: Every selection is instantly committed to Dexie.js in the background without UI stutter. If the browser or app closes, the session is resumed seamlessly.
4. **Submission & Instant Scoring**:
   - Immediate calculation of the official score out of 60 marks (weighted 1-mark and 2-mark questions per Ministry specifications).
   - Chapter-by-chapter mastery breakdown (الدعامة والحركة، التنسيق الهرموني، التكاثر، المناعة، البيولوجيا الجزيئية DNA & RNA).
5. **Trap Analysis & Remediation**: Detailed post-exam review highlighting *Why this trap was set* (فخ السؤال), the official ministry reasoning, and bookmarking mistakes for spaced repetition review.

### Visual Identity & Theme
- **Color Palette & Atmosphere**:
  - Background: Obsidian Navy (`#0A192F` / `#070F1E`)
  - Surface & Glass Cards: Deep Slate (`#112240` with `backdrop-blur-md` and `rgba(100, 255, 218, 0.08)` borders)
  - Primary Accent: Neon Cyan / Bio-Teal (`#64FFDA` / `#00F5D4`)
  - Secondary Accent: Electric Emerald (`#10B981`)
  - Warning / Trap Accent: Amber Coral (`#F59E0B` / `#EF4444`)
  - Text Primary: Pure White / Frost (`#F8FAFC`), Text Muted: Slate (`#94A3B8`)
- **Typography & Hierarchy**:
  - Font Family: Arabic Cairo (`font-cairo`), crisp geometric sans-serif for numbers and Latin terminology (`font-mono` for DNA/RNA codons).
  - High accessibility contrast ratio (WCAG AAA compliant on OLED dark surfaces).
  - Clear, large touch targets (minimum 48px touch bounding box on mobile).
- **Interactive Feedback & Motion**:
  - Smooth bubble sheet selection ripples.
  - Urgency pulsing timer when less than 15 minutes remain.
  - Animated progress bars and SVG circular mastery gauges on the final result sheet.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Architecture Pattern (Clean Layering)**
  - *Chosen Approach*: Strict 5-tier architecture:
    1. `src/types/`: Pure TypeScript interfaces and domain models (no dependencies).
    2. `src/db/`: Dexie database class, schema migrations, and seed loaders.
    3. `src/services/`: Pure business logic and scoring algorithms (`MinistryExamScoringEngine`, `PacingEngine`).
    4. `src/hooks/`: Reactive React hooks (`useExamSession`, `useExamHistory`, `useOfflineSync`).
    5. `src/components/`: Modular presentation components with Tailwind CSS.
  - *Why*: Ensures maximum maintainability, zero circular dependencies, easy unit testing, and instant code portability.

- **Decision 2: Persistence (Dexie.js IndexedDB vs. LocalStorage)**
  - *Chosen Approach*: Dexie.js IndexedDB with reactive live queries.
  - *Why*: `localStorage` is synchronous, limited to 5MB, and easily exhausted by exam diagrams, attempt history, and question banks. Dexie provides asynchronous non-blocking storage, multi-gigabyte capacity, compound indexing, and reactive React state synchronization.

- **Decision 3: Android & Gradle Integration via Capacitor**
  - *Chosen Approach*: Structure the web application output directory (`dist`) to link cleanly into Capacitor Android configuration (`capacitor.config.ts`), ensuring standard Gradle builds (`gradle :app:assembleDebug`) succeed alongside web bundling.
  - *Why*: Allows single-codebase development for both offline PWA in browser and standalone Android APK.

---

## 4. Technical Architecture & Data Strategy

### System Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                              BioThanaweya Presentation Layer                           │
│  ┌───────────────────────┐  ┌───────────────────────┐  ┌────────────────────────────┐  │
│  │ ExamSimulatorScreen   │  │ DigitalBubbleSheet    │  │ ExamResultAnalyticsScreen  │  │
│  │ (Question View & Nav) │  │ (50-Item Quick Matrix)│  │ (Score /60 & Trap Review)  │  │
│  └───────────┬───────────┘  └───────────┬───────────┘  └─────────────┬──────────────┘  │
└──────────────┼──────────────────────────┼────────────────────────────┼─────────────────┘
               │                          │                            │
               ▼                          ▼                            ▼
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                               Custom React Hooks Layer                                 │
│     useExamSession(paperId)   │   useExamScoring()   │   useLiveExamHistory()          │
└──────────────────────────────┬─────────────────────────────────────────────────────────┘
                               │
                               ▼
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                               Services & Domain Logic                                  │
│  ┌───────────────────────────────────┐       ┌──────────────────────────────────────┐  │
│  │ MinistryExamScoringEngine         │       │ PacingEfficiencyEngine               │  │
│  │ - 46 MCQs (32x1 pt + 14x2 pt)     │       │ - Time per question tracking         │  │
│  │ - 4 Essay Questions (4x2 pt)      │       │ - Target velocity calculation        │  │
│  │ - Official Trap & Distractor Map  │       │ - Pacing color indicators            │  │
│  └─────────────────┬─────────────────┘       └──────────────────┬───────────────────┘  │
└────────────────────┼────────────────────────────────────────────┼──────────────────────┘
                     │                                            │
                     ▼                                            ▼
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                          Data Access & Persistence Layer (Dexie.js)                    │
│   BioDexieDatabase (IndexedDB v1)                                                      │
│   ├── examPapers: id, title, year, durationMinutes, totalMarks                         │
│   ├── questions: id, paperId, chapterId, questionNumber, marks, trapExplanation        │
│   ├── examAttempts: id, paperId, startTime, submitTime, score, isCompleted             │
│   └── userAnswers: id, attemptId, questionId, selectedOption, isFlagged                │
└────────────────────────────────────────────────────────────────────────────────────────┘
```

### Data Models & State Schema

```typescript
// Core Data Contract Summary
interface Question {
  id: string;
  paperId: string;
  chapterId: string; // e.g., "movement_support", "hormones", "dna_rna"
  questionNumber: number;
  marks: 1 | 2;
  textArabic: string;
  textEnglish?: string;
  options: {
    id: 'A' | 'B' | 'C' | 'D';
    textArabic: string;
    textEnglish?: string;
  }[];
  correctOptionId: 'A' | 'B' | 'C' | 'D';
  trapExplanationArabic: string;
  scientificReference: string;
  diagramSvgKey?: string;
}

interface ExamAttempt {
  id: string;
  paperId: string;
  startedAt: number;
  completedAt?: number;
  elapsedSeconds: number;
  scoreObtained: number;
  maxScore: number; // 60 for official Thanawya Amma
  isSubmitted: boolean;
  answers: Record<string, 'A' | 'B' | 'C' | 'D'>;
  flaggedQuestionIds: string[];
}
```

---

## 5. Phased Implementation Roadmap

1. **Step 1: Frontend Workspace & Build Configuration**
   - Configure `package.json` with React 18/19, TypeScript, Vite, Tailwind CSS, Lucide icons, and Dexie.js.
   - Configure `vite.config.ts`, `tailwind.config.js`, `postcss.config.js`, and `tsconfig.json`.
   - Setup Capacitor configuration (`capacitor.config.ts`) targeting Android assets.

2. **Step 2: Clean Architecture Domain & Database Layer**
   - Create `src/types/exam.ts` with strict domain interfaces.
   - Create `src/db/bioDatabase.ts` with Dexie.js schema and reactive live queries.
   - Create `src/db/seeds/officialExamsSeed.ts` containing authentic ministerial biology exams (2023, 2024, experimental models) with full questions, options, traps, and diagrams.

3. **Step 3: Scoring & Pacing Domain Services**
   - Build `src/services/MinistryExamScoringEngine.ts` strictly calculating marks, percentages, trap hit rates, and chapter-by-chapter breakdowns.
   - Build `src/services/PacingEfficiencyEngine.ts` to guide students on time management.

4. **Step 4: Interactive UI Components & Exam Simulator**
   - Build `src/components/exam/DigitalBubbleSheet.tsx` for quick 50-item navigation.
   - Build `src/components/exam/QuestionCard.tsx` with high-contrast typography, options, and diagram rendering.
   - Build `src/components/exam/ExamHeader.tsx` with timer, pacing gauge, and progress bar.
   - Build `src/components/exam/ExamResultAnalytics.tsx` with chapter mastery rings, score card, and trap reviews.
   - Build `src/components/exam/ExamPaperSelector.tsx` for browsing and launching available exam papers.

5. **Step 5: Full Application Integration & Verification**
   - Connect main app shell in `src/App.tsx` with state routing (Home/Selector -> Active Exam -> Result Analytics).
   - Verify build pipeline (`npm run build`) and ensure no regressions on the existing Android Gradle configuration.
