# ADR-003: Flutter Module — Archive and Complete, Not Delete

## Context
The Flutter module resides in `lib/` with the following structure:
- `lib/main.dart` — entry point, uses `ProviderScope` + `BoundBioAppRoot`
- `lib/core/`, `lib/features/` — Dart packages for theming, data service, architecture, database, and UI
- `pubspec.yaml` — declares `flutter sdk`, dependencies: `flutter_localizations`, `cupertino_icons`, `google_fonts`, `provider`, `flutter_riverpod`, `flutter_markdown`, `shared_preferences`, `intl`, `http`, `sqflite`, `path`, `path_provider`
- `dev_dependencies`: `flutter_test`, `flutter_lints`, `sqflite_common_ffi: ^2.3.4`

No Android platform files exist (`android/`, `build.gradle`, `.gradle`). The module cannot be built to an APK in its current state.

## Decision
**Archive `lib/` to `archive/flutter/` and implement a recovery plan.** Rationale:
- The Flutter codebase contains non-trivial Dart logic (Riverpod state management, sqflite database, FSRS spaced-repetition algorithm via `ts-fsrs`, curriculum repository, examiner engine, formula calculators).
- Archiving preserves the code without deleting it; a future engineer can recover and complete the Flutter module.
- The Web/PWA stack (`src/`) is the confirmed, built, and tested delivery mechanism; Flutter is a parallel path that may be reactivated if platform requirements change.

## Consequences
- **Positive**: Flutter source code is preserved (not deleted); repo focus remains on the verified Capacitor + Web stack; clear migration path if Flutter is needed later.
- **Negative**: `lib/` remains in the working tree; must be moved to `archive/flutter/` per the constraint "Do not delete lib — move it only to archive/." Build infrastructure for Flutter is removed from the active repo until recovery.
- **Alternative rejected**: Deleting `lib/` would lose the Flutter implementation entirely, including the FSRS spaced-repetition engine, curriculum data service, and custom formula calculators that have no equivalent in the React/Web stack.

## Alternatives Rejected
1. **Delete `lib/`** — Contravenes the explicit constraint "Do not delete lib — move it only to archive/." Also destroys the FSRS implementation and curriculum service.
2. **Build Flutter as primary Android without archiving** — Would require adding `android/`, platform channels, and signing config with no guarantee of success; high risk for little reward since the Capacitor + Web stack is already verified.
3. **Leave `lib/` as-is** — The module remains "unbuildable" and creates confusion about the repository's delivery stack; the ADR explicitly addresses this by archiving with intent to recover.

---
*Decision: Archive `lib/` to `archive/flutter/` with a recovery plan; keep focus on the verified Capacitor + Web delivery stack.*