# REPOSITORY INVENTORY — SCHOLAR SIDEKICK (BIOTHANAWEYA)

**Project**: BioThanaweya (`smarts.bio`)  
**Audit Scope**: Repository-wide source files, modules, configs, and assets.

---

## 1. Top-Level Directory Layout

```text
/
├── .build-outputs/               # Compilation & build system transient cache
├── .github/                      # GitHub Actions CI/CD workflows
│   └── workflows/
│       └── build.yaml            # Authoritative CI/CD pipeline for Flutter build
├── android/                      # Flutter Android host harness (Gradle & Manifest)
├── app/                          # Native Android Jetpack Compose production application
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/ # Kotlin Clean Architecture source files
│   │   │   └── res/              # Android drawables, layouts, strings, values
│   │   └── test/java/com/example/# JUnit 4 & Robolectric test suites
│   ├── build.gradle.kts          # App-level Gradle configuration (SDK 36, Compose, Room, Hilt)
│   └── proguard-rules.pro        # R8 minification & obfuscation preservation rules
├── assets/                       # Static bundled assets and curriculum imagery
├── gradle/                       # Gradle Version Catalog & wrapper assets
│   └── libs.versions.toml        # Unified version catalog
├── lib/                          # Flutter/Dart cross-platform production application
│   ├── core/                     # Common theme, network, constants
│   ├── features/                 # Modular feature packages (Agents, Flashcards, Exams, Achievements)
│   └── main.dart                 # Production Flutter entrypoint with 11-chapter dashboard
├── build.gradle.kts              # Root-level Gradle build script
├── settings.gradle.kts           # Root Gradle settings & project definitions
├── pubspec.yaml                  # Flutter package definition & dependencies
├── metadata.json                 # Google AI Studio platform identity configuration
└── README.md                     # Official project documentation and build badges
```

---

## 2. Detailed Component Inventory

### A. Native Android Core (`app/src/main/java/com/example/`)

- **`BioThanaweyaApp.kt`**: Application class annotated with `@HiltAndroidApp` for dependency graph initialization.
- **`MainActivity.kt`**: Single-Activity Compose host annotated with `@AndroidEntryPoint`, handling window insets and edge-to-edge rendering.
- **Domain Engines (`domain/engine/`)**:
  - `AnatomicalDissectionProvider.kt`: ASCII and structural anatomy maps.
  - `ArabicTextNormalizer.kt`: Regex and unicode normalizer for Arabic letters (Alef, Taa Marbuta, Ya).
  - `CodonCalculator.kt`: DNA/mRNA to amino acid transcription & translation engine.
  - `DnaChargaffCalculator.kt`: Chargaff's rules calculator for base pairs and hydrogen bonds.
  - `EmpatheticCompanionEngine.kt`: Motivation and student achievement calculations.
  - `MarkSchemeEngine.kt`: Ministry essay rubric keyword matching and partial scoring.
  - `SarcomereCalculator.kt`: Muscle contraction sarcomere length & zone change solver.
  - `FSRS.kt`: Free Spaced Repetition Scheduler v4.5/v5 pure Kotlin implementation.
- **Data Repositories & Local Database (`data/`)**:
  - `local/BioDatabase.kt`: Room database v1 holding flashcards, progress, exam attempts, and mastery.
  - `local/dao/`: `FlashcardDao.kt`, `ProgressDao.kt`, `ExamAttemptDao.kt`, `TopicMasteryDao.kt`.
  - `local/entity/`: `FlashcardEntity.kt`, `StudentProgressEntity.kt`, `ExamAttemptEntity.kt`, `TopicMasteryEntity.kt`.
  - `repository/BiologyCurriculumRepository.kt`: Definitive source of truth for all 11 chapters.
  - Specialized topic curriculum packs: `NutritionCurriculumPack.kt`, `TransportCurriculumPack.kt`, `RespirationCurriculumPack.kt`, `ExcretionCurriculumPack.kt`, `NervousCurriculumPack.kt`.
- **Presentation (`presentation/` & `ui/`)**:
  - `presentation/screens/`: `DashboardScreen.kt`, `TopicDetailScreen.kt`, `SimulatedExamScreen.kt`, `FsrsReviewScreen.kt`, `FormulaLabScreen.kt`, `SettingsScreen.kt`, `ProgressScreen.kt`.
  - `presentation/components/`: `ConfettiCanvasAnimation.kt` (Compose particle physics), `SpecializedComponents.kt`.

### B. Flutter / Dart Application (`lib/`)

- **`main.dart`**: Complete RTL (`TextDirection.rtl`) production application launching `BioDashboardScreen`.
- **`features/biology_agents/`**:
  - `domain/agents/base_agent.dart`: Core agent abstraction with offline resilience.
  - `domain/agents/educator_agent.dart` & `master_biology_educator_agent.dart`: ELI5 & technical breakdown agents.
  - `domain/agents/rubric_grader_agent.dart`: Ministry rubric keyword grading.
  - `presentation/screens/dashboard_screen.dart`: Complete 11-chapter study dashboard.
  - `presentation/widgets/sparkline_chart.dart`: CustomPainter cubic bezier sparkline.
- **`features/flashcards/`**:
  - `models/spaced_repetition_item.dart`: SM-2 spaced repetition data model.
  - `data/spaced_repetition_repository.dart`: Local persistence and review manager.
  - `presentation/screens/flashcard_screen.dart`: 3D flipping card review UI.
- **`features/exam_simulator/`**:
  - `presentation/screens/exam_simulation_screen.dart`: Timed exam simulation with rubric evaluation.
- **`features/achievements/`**:
  - `models/achievement_badge.dart`: 8 Thanawya Amma milestone badges.
  - `presentation/screens/achievements_screen.dart`: Trophy showcase with category filters.
  - `presentation/widgets/confetti_particle_widget.dart`: 75-particle physics celebration animation.

### C. Build & Continuous Integration

- `.github/workflows/build.yaml`: GitHub Actions pipeline with dynamic `PUBSPEC_FILE` search and artifact upload.
- `app/build.gradle.kts`: Configured for Android SDK 36, minSdk 24, Compose, Room, Hilt, R8 minification.
- `gradle/libs.versions.toml`: Centralized Version Catalog.
