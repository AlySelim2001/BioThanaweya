# FEATURE TRACEABILITY MATRIX — SCHOLAR SIDEKICK (BIOTHANAWEYA)

| Feature ID | Feature Name | Native Android (`app/`) | Flutter (`lib/`) | Offline Capability | Test Coverage | Status |
|------------|--------------|-------------------------|------------------|--------------------|---------------|--------|
| **FT-01** | 11-Chapter Curriculum Index | `BiologyCurriculumRepository.kt` | `presentation/screens/dashboard_screen.dart` | 100% Bundled | `CurriculumCoverageTest` | **VERIFIED** |
| **FT-02** | Master Biology Educator (Agent 1) | `AgentUseCases.kt`, `BioViewModel.kt` | `domain/agents/master_biology_educator_agent.dart` | 100% Deterministic Fallback | `CurriculumCoverageTest` | **VERIFIED** |
| **FT-03** | Spatial Anatomy Architect (Agent 2)| `AnatomicalDissectionProvider.kt` | `presentation/screens/dashboard_screen.dart` | 100% ASCII/Monospace Schematics | `CurriculumCoverageTest` | **VERIFIED** |
| **FT-04** | Assessment Specialist (Agent 3) | `MinistryExaminerEngine.kt` | `features/exam_simulator/` | 100% Local MCQ Bank | `CurriculumCoverageTest` | **VERIFIED** |
| **FT-05** | Adaptive Rubric Evaluator (Agent 4)| `MarkSchemeEngine.kt` | `domain/agents/rubric_grader_agent.dart` | 100% Regex Keyword Rubrics | Unit tests & CI | **VERIFIED** |
| **FT-06** | Empathetic Companion (Agent 5) | `EmpatheticCompanionEngine.kt` | `features/achievements/services/` | 100% Local Rules Engine | `EmpatheticCompanionTest` | **VERIFIED** |
| **FT-07** | Spaced Repetition Flashcards | `domain/fsrs/FSRS.kt` (FSRS v5) | `features/flashcards/sm2_engine.dart` (SM-2) | 100% Local (Room / Local DB) | Unit tests & CI | **VERIFIED** |
| **FT-08** | Biological Calculators | `BioCalculators.kt`, `CodonCalculator.kt` | Local formula helpers | 100% Pure Functions | Unit tests & CI | **VERIFIED** |
| **FT-09** | Sparkline Progress Charts | Canvas Sparkline in `DashboardScreen.kt` | `CustomPainter` in `sparkline_chart.dart` | 100% Local Rendering | UI tests | **VERIFIED** |
| **FT-10** | Physics Confetti Celebration | `ConfettiCanvasAnimation.kt` | `confetti_particle_widget.dart` | 100% Local Canvas Animation | Frame budget verified | **VERIFIED** |
| **FT-11** | Room Local Persistence | `BioDatabase.kt` (4 DAOs, 4 Entities) | SQLite & SharedPreferences | 100% Local Storage | Room tests | **VERIFIED** |
| **FT-12** | Material 3 & Arabic RTL | `Theme.kt`, `LayoutDirection.Rtl` | `app_theme.dart`, `TextDirection.rtl` | 100% Local UI Framework | Android lint / visual | **VERIFIED** |
