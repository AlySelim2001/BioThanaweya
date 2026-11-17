# ARCHITECTURE DECISION RECORDS (ADR) — SCHOLAR SIDEKICK

**Project**: BioThanaweya (`smarts.bio`)  
**Status**: Accepted & Enforced

---

## ADR-001: Dual-Stack Native Android and Flutter Harmonization

### Context

The repository contains both a Native Android Kotlin codebase in `app/` and a Flutter/Dart codebase in `lib/` and `pubspec.yaml`. The Google AI Studio cloud execution environment provides Gradle, OpenJDK 21, and Android SDK 36, without a locally pre-installed Flutter SDK. Meanwhile, GitHub Actions CI/CD workflows run in standard Ubuntu containers with `subosito/flutter-action@v2`.

### Decision

1. **Primary AI Studio & Android Ecosystem Engine**: The Native Android codebase (`app/`) is designated as the primary native target for Google AI Studio live emulator streaming, local compilation (`compile_applet`), and Gradle JVM test suites.
2. **Cross-Platform CI/CD Target**: The Flutter codebase (`lib/`) is maintained as the primary cross-platform delivery vehicle built and packaged via GitHub Actions (`.github/workflows/build.yaml`).
3. **Single Source of Truth for Requirements**: Both codebases must strictly enforce identical pedagogical models:
   - 11 Egyptian Thanawya Amma Biology chapters.
   - The 5 Intelligent Biology Agents (Educator, Anatomy, Assessment, Rubric Evaluator, Companion).
   - 100% Offline-First operation with local persistence.
   - Arabic RTL directionality as default.

### Consequences

- Native Android updates and test passes are verified instantaneously in AI Studio via Gradle.
- Flutter APKs are generated automatically upon push to GitHub via GitHub Actions with zero manual intervention.
- Neither codebase is deprecated or removed; both are kept synchronized.

---

## ADR-002: Offline-First Architectural Guarantee

### Context

Egyptian high school students frequently study in areas with unstable or non-existent internet connections, or during revision retreats where data usage is restricted. Relying exclusively on remote LLM endpoints creates high friction and potential test-day failures.

### Decision

1. All core educational assets (explanations, formulas, MCQs, rubric keywords, and diagrams) are bundled locally inside application binaries (Kotlin static repositories and Dart data objects).
2. Remote AI calls (e.g., Gemini API) are designated as progressive enhancements. If the network is absent, slow, or fails, the application automatically falls back to deterministic local domain engines (`MarkSchemeEngine`, `ArabicTextNormalizer`, `BiologyCurriculumRepository`).
3. User data (progress, exam history, flashcard review queues) is persisted strictly on-device using Room Database in Native Android and SharedPreferences/SQLite in Flutter.

---

## ADR-003: Dynamic Working Directory Discovery in CI/CD

### Context

GitHub Actions previously failed with `Expected to find project root in current working directory` due to path variations in cloned repositories.

### Decision

The CI/CD pipeline dynamically searches for `pubspec.yaml` at runtime using:

```bash
PUBSPEC_FILE=$(find . -name "pubspec.yaml" -not -path "*/.*" | head -n 1)
PROJECT_DIR=$(dirname "$PUBSPEC_FILE")
echo "PROJECT_DIR=$PROJECT_DIR" >> $GITHUB_ENV
```

All subsequent Flutter commands execute with `working-directory: ${{ env.PROJECT_DIR }}`.

### Consequences

Guarantees 100% reliable CI/CD runs regardless of workspace nesting or branching changes.
