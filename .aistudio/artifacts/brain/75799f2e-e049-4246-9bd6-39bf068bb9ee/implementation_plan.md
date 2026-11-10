# BioThanaweya (تعلم مع علي) - Mobile Architecture Blueprint & Hub

Production-ready Clean Architecture blueprint, offline-first SQLite database service (`local_database_service.dart`), and interactive architectural telemetry hub (`app_architecture_hub.dart`) for the Egyptian Thanaweya Amma Biology educational ecosystem.

## User Review & Critical Decisions

> [!IMPORTANT]
> The architectural direction has been aligned with the requirements confirmed in Phase 1:
> - **State Management**: Standardized on **Flutter Riverpod** utilizing `StateNotifier` and `AsyncValue` across presentation and domain layers.
> - **Offline Persistence**: Implemented using **sqflite** with versioned migrations, relational tables (Chapters, Topics, Questions, Exam Sessions, FSRS Mistakes, Study Notes), indexing, and DAO design patterns.
> - **Interactive Hub**: Designed as a **Bento Grid Architecture & Telemetry Hub** with live simulated latency/cache benchmarks, CI/CD pipeline triggers, and offline readiness health checks.

- **Confirmed Decision 1**: State Management pattern set to Riverpod (`StateNotifierProvider` + `AsyncValue` reactive streams).
- **Confirmed Decision 2**: Local database engine set to `sqflite` with SQLite transactional safety, full offline seed hydration, and zero network dependency.
- **Confirmed Decision 3**: Architecture Hub widget configured with full interactive Bento Grid UI, live database benchmarks, and GitHub Actions telemetry toggles.

---

## 1. Overview & Core Concept

- **What It Does**: Establishes a production-grade Clean Architecture specification for the BioThanaweya ("تعلم مع علي") mobile application, coupled with two high-fidelity deliverables:
  1. `lib/core/database/local_database_service.dart`: A rock-solid, 100% offline-first SQLite repository service managing schema setup, migrations, transactions, indexing, and high-performance querying for all 11 Egyptian curriculum chapters.
  2. `lib/features/architecture_hub/presentation/screens/app_architecture_hub.dart`: A modern, interactive Bento Grid architectural dashboard displaying live offline telemetry, database benchmarks, cross-platform layer breakdown, and GitHub Actions CI/CD status.
- **Target Audience / Persona**: Egyptian Thanaweya Amma (3rd Secondary - Scientific Branch / علمي علوم) students preparing for high-stakes Ministry of Education Biology exams under poor or zero internet connectivity conditions, as well as lead software engineers maintaining the codebase.
- **Key Value**: Guarantees sub-15ms local query latency, zero external API costs, deterministic offline data integrity, and complete pedagogical coverage across Units 1 & 2.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **Architectural Inspection**: The developer or student opens the Architecture Hub and is greeted by a Bento Grid featuring key architectural pillars: Cross-Platform Engine (Flutter/Dart AOT), Offline SQLite Store, Riverpod Reactive Core, and CI/CD Status.
2. **Interactive Telemetry & Benchmark Execution**: Tapping "Run Offline Benchmark" fires a multi-step query/insert simulation across 500+ biology curriculum records, rendering live read/write latency metrics (ms) and memory footprint.
3. **Pillar Deep-Dive**: Users expand individual Bento cards (UI/UX Design System, Local Database, CI/CD Pipeline) to view schema definitions, code snippets, and active pipeline stages (Lint, Test, Build, Release).
4. **Offline Mode Verification**: Users toggle between simulated offline and sync states to verify instantaneous failover and zero-network operational readiness.

### Visual Identity & Theme
- **Aesthetic Direction**: *Bioluminescent Cyber-Academic* — sleek, modern dark mode with vivid biological accents that inspire focus and technical trust.
- **Color Palette & Tokens**:
  - Background: Deep Navy Void (`#0A192F` / `#0F172A`)
  - Surface Glass: Translucent Indigo Slate (`#112240` / `#1E293B`) with 1px border (`#233554`)
  - Primary Biological Accent: Cyan Teal (`#64FFDA` / `#0D9488`)
  - Warning / Trap Accent: Amber Fire (`#F59E0B` / `#FFAA00`)
  - Molecular Accent: Electric Violet (`#7C3AED` / `#A78BFA`)
  - Success Indicator: Emerald Mint (`#10B981`)
- **Typography & RTL Hierarchy**:
  - Full Arabic RTL directionality with Cairo / Amiri font styling.
  - Clear tabular numerical figures for latencies, question counts, and percentages.
- **Layout & Density**:
  - Asymmetric Bento Grid adapting fluidly to mobile viewports (`SingleChildScrollView` + Staggered Rows/Columns).
  - Minimum touch targets of 48dp on all interactive toggles and benchmark triggers.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Native vs. Cross-Platform (Flutter Choice)**
  - *Chosen Approach*: Single Flutter/Dart codebase compiling natively to ARM64 for Android and iOS.
  - *Why*: Eliminates dual-team maintenance overhead while delivering 60/120 FPS performance, hot reload velocity, and pixel-perfect Arabic RTL layouts.
  - *Alternatives Considered*: Native Kotlin/Jetpack Compose only (limits future iOS/Web deployment) or React Native (slower JSI bridge for complex canvas diagrams).

- **Decision 2: Local Persistence Strategy (SQLite via `sqflite`)**
  - *Chosen Approach*: Relational SQLite database with explicit SQL schema tables, foreign key constraints, composite indexes, and versioned migrations.
  - *Why*: Provides relational integrity for complex educational relationships (Chapters -> Topics -> Questions -> Mistakes -> Spaced Repetition Intervals), full-text search capability, and rock-solid ACID transactions without cloud dependencies.
  - *Alternatives Considered*: SharedPreferences alone (insufficient for relational joins and queries), Isar (heavier binary overhead and compatibility nuances), Firebase (requires internet connection and recurring costs).

- **Decision 3: State Management (Riverpod with `StateNotifier` & `AsyncValue`)**
  - *Chosen Approach*: Compile-safe, testable Riverpod providers with `AsyncValue` for loading/error/data states.
  - *Why*: No `BuildContext` dependency for domain logic, easy mocking in unit tests, and seamless UI binding.

---

## 4. Technical Architecture & Data Strategy

### Clean Architecture & System Flow Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                        PRESENTATION LAYER                              │
│  ┌────────────────────────┐  ┌──────────────────────────────────────┐  │
│  │   AppArchitectureHub   │  │        BoundBioAppSuite / UI         │  │
│  │   (Bento Grid Screen)  │  │   (Dashboard, Quiz, Lab, Planner)    │  │
│  └───────────┬────────────┘  └──────────────────┬───────────────────┘  │
│              │                                  │                      │
│              ▼                                  ▼                      │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │            StateNotifier / Riverpod State Controllers            │  │
│  │       (ArchitectureTelemetryNotifier, QuizSessionController)     │  │
│  └──────────────────────────────────┬───────────────────────────────┘  │
└─────────────────────────────────────┼──────────────────────────────────┘
                                      │
┌─────────────────────────────────────▼──────────────────────────────────┐
│                           DOMAIN LAYER                                 │
│  ┌───────────────────────┐  ┌─────────────────┐  ┌──────────────────┐  │
│  │ Entities & Models     │  │ Repository      │  │ Use Cases        │  │
│  │ - Chapter, Topic      │  │ Interfaces      │  │ - QueryCurriculum│  │
│  │ - Question, Mistake   │  │ - ICurriculumRepo│ │ - RecordAttempt   │  │
│  │ - TelemetryBenchmark  │  │ - IExamRepo     │  │ - RunBenchmark   │  │
│  └───────────────────────┘  └────────┬────────┘  └──────────────────┘  │
└──────────────────────────────────────┼─────────────────────────────────┘
                                       │
┌──────────────────────────────────────▼─────────────────────────────────┐
│                            DATA LAYER                                  │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │                 LocalDatabaseService (SQLite)                    │  │
│  │  - OpenDatabase & Versioned Migrations (v1 -> v2)                │  │
│  │  - Tables: chapters, topics, questions, exam_sessions, mistakes  │  │
│  │  - Indexes on chapter_id, difficulty, is_resolved                │  │
│  │  - Transactional Batch Inserts & Seed Hydration                  │  │
│  └──────────────────────────────────┬───────────────────────────────┘  │
│                                     │                                  │
│  ┌──────────────────────────────────▼───────────────────────────────┐  │
│  │          Local Hardware Storage (SQLite File on Device)          │  │
│  │               100% OFFLINE • ZERO NETWORK CALLS                  │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

### Relational Schema Blueprint (SQLite)
1. **`chapters`**: `(id INTEGER PRIMARY KEY, unit_number INTEGER, title_ar TEXT, title_en TEXT, key_concepts_json TEXT, mastery REAL, cards_due INTEGER)`
2. **`topics`**: `(id TEXT PRIMARY KEY, chapter_id INTEGER, title_ar TEXT, title_en TEXT, summary_markdown TEXT)`
3. **`questions`**: `(id TEXT PRIMARY KEY, chapter_id INTEGER, question_text TEXT, options_json TEXT, correct_index INTEGER, explanation TEXT, is_ministry_trap INTEGER)`
4. **`exam_sessions`**: `(id TEXT PRIMARY KEY, title TEXT, mode TEXT, score_percentage REAL, started_at TEXT, completed_at TEXT, duration_seconds INTEGER)`
5. **`fsrs_mistakes`**: `(question_id TEXT PRIMARY KEY, chapter_id INTEGER, fail_count INTEGER, stability_days REAL, last_failed_at TEXT, next_review_at TEXT, is_resolved INTEGER)`
6. **`study_notes`**: `(id TEXT PRIMARY KEY, chapter_id INTEGER, title TEXT, content TEXT, updated_at TEXT)`

### Deliverable File Mapping
1. **Clean Architecture Directory Guide**: Full textual repository breakdown included in project documentation.
2. **`lib/core/database/local_database_service.dart`**: Complete SQLite engine handling initialization, schemas, indexing, seeds, and CRUD queries.
3. **`lib/features/architecture_hub/presentation/screens/app_architecture_hub.dart`**: Interactive Bento Grid UI with real-time benchmark runner, CI/CD telemetry toggles, and expandable architectural cards.
