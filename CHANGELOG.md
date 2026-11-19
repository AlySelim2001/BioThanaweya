# Changelog

All notable changes to the **BioThanaweya** project will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.4] - 2026-11-19

### Fixed

- **Launcher icons were silently dropped from the APK**: the density folders were named
  `mipmap-48dpi`, `mipmap-72dpi`, `mipmap-96dpi`, `mipmap-144dpi` and `mipmap-192dpi`.
  Those are not valid Android resource qualifiers, so aapt2 ignored them and only the
  `mipmap-anydpi-v26` adaptive XMLs ended up in the package. Renamed to the standard
  `mipmap-mdpi/hdpi/xhdpi/xxhdpi/xxxhdpi` and regenerated square `ic_launcher.png`
  plus `ic_launcher_round.png` at every density.
- `mipmap-anydpi-v26/ic_launcher_round.xml` pointed at the nonexistent
  `@drawable/ic_launcher_round`; it now references `@drawable/ic_launcher_foreground`.
- `FreeLLMAPIConfig.provider` is now optional and the AI client uses
  `AbortSignal.timeout()` instead of the non-standard `RequestInit.timeout`,
  so `tsc --noEmit` passes again.
- Fallback explanation tables are typed as indexed maps, removing implicit-`any` errors.

### Changed

- Updated versionCode/versionName to 114 / "1.1.4" and `package.json` to 1.1.4.

## [1.1.3] - 2026-11-19

### Changed

- Updated to v1.1.3 release with FreeLLMAPI integration, ts-fsrs spaced repetition, and Glassmorphic HUD styling throughout
- Generated crisp launcher icons for all mipmap folders
- Updated versionCode/versionName to 1.1.3
- Fixed legacy boxed UI overrides across all screens
- Verified SHA-256 checksums for release artifacts

## [1.0.0] - 2026-11-06

### Added

- **7-Agent AI Learning Framework**: 
  - Master Biology Educator with dual English/Egyptian Arabic ELI5 scaffolding.
  - Visual Learning Architect with structural monospace ASCII schematics.
  - Assessment Specialist unmasking Ministry exam traps.
  - Ministry Examiner with keyword deduction grading.
  - FSRS Spaced Repetition engine implementing FSRS v4.5/v5.
  - Metacognitive Evaluator detecting overconfidence and imposter syndrome bias.
  - Student Wellbeing & Gamification with streak tracking, XP awards, and anxiety scoring.
- **Interactive Bio-Law Calculators**: 
  - Sarcomere landmark solver ($Z = n + 1$, $A = n$, $I_{\text{complete}} = n - 1$, maximal contraction $H = 0$).
  - Chargaff DNA helix calculator enforcing $A=T, G=C$, purine/pyrimidine ratios, and hydrogen bond counting ($2A + 3G$).
  - Codon & Protein translation solver excluding stop codons from amino acids and calculating peptide/water counts.
- **Client-Side Persistent Storage Vault**: 
  - 100% FOSS native IndexedDB transactional database engine (`BioThanaweyaDB_v1`).
  - 1-click JSON backup export and import with schema integrity validation.
  - Rolling auto-snapshots maintaining the latest 5 milestones.
- **Mobile First & Termux Runtime**: 
  - Express backend server with dynamic 128MB resident memory circuit breaker.
  - Progressive Web App (PWA) with full offline caching via Service Worker.
- **Quality Assurance & CI/CD**: 
  - Automated GitHub Actions workflow on Node.js 22 LTS.
  - Vitest test suites verifying biological calculations.
  - Mobile verification script (`scripts/verify-local.sh`) and release tool (`scripts/release.sh`).