# 🤖 AI Agents Documentation

**BioThanaweya** implements a verified AI agent architecture with 7 specialized educational agents providing cognitive scaffolding for Thanaweya Amma students.

## Verified Agent Count

**7 agents** — determined from actual source code evidence of distinct agents with identifiable execution responsibilities (see `docs/reports/AGENT_COUNT_RECONCILIATION.md` for the reconciliation of historical 5-agent vs current 7-agent count).

## Agent Framework

| Agent | Role | Responsibility |
|-------|------|----------------|
| **Agent 1** | **Master Biology Educator** | Dual-layer explanations: deep technical molecular mechanics paired with Egyptian Arabic ELI5 analogies, with Bloom's Taxonomy progression. |
| **Agent 2** | **Visual Learning Architect** | Monospace LTR ASCII schematics, visual inspection cues, and diagram recognition markers. |
| **Agent 3** | **Assessment Specialist** | Authentic Ministry question types (Scientific Term, Give Reason, What Happens If, MCQ), unmasking Exam Traps and golden rules. |
| **Agent 4** | **Ministry Examiner & FSRS Engine** | Keyword deduction grading and modern spaced repetition scheduling based on FSRS v4.5/v5 parameters. |
| **Agent 5** | **Metacognitive Evaluator** | Confidence-accuracy calibration widget detecting overconfidence bias (Dunning-Kruger) and underconfidence imposter doubt. |
| **Agent 6** | **Problem-Based Learning (PBL)** | Real-world toxicology cases, autoimmune disorders, and genetic engineering missions with rubric grading. |
| **Agent 7** | **Student Wellbeing & Gamification** | Active study streaks, XP progression, achievement badges, anxiety scoring, and Pomodoro study intervals. |

## Counting Methodology

An agent is counted when source code provides evidence of a distinct agent with:

1. **Unique identity or name** — Agent has a distinguishable name/identifier
2. **Defined responsibility or role** — Clear educational or technical function described
3. **Actual implementation or executable handler** — Source code implements the agent's core logic
4. **Registration or invocation path** — Agent is registered, configured, or invoked in the application
5. **Runtime participation** — Agent participates in the current application runtime

Items not counted:
- UI labels without implementation
- TypeScript interfaces alone
- Test mocks
- Archived implementations
- Planned agents
- Multiple aliases for the same agent
- Generic utilities or services
- Orchestrator as an additional agent unless independently implemented

## Source Evidence per Agent

### Agent 1: Master Biology Educator
- **Source**: `src/services/examinerEngine.ts` — `MinistryExaminerEngine.gradeStudentAnswerLocally()` and `gradeWithAIOrFallback()`
- **Types**: `src/types/index.ts` — `MasterStudyPack` with `eli5Analogy` and `deepTechnicalBreakdown`
- **UI**: `src/components/SevenAgentsModal.tsx` — Agent 1 display

### Agent 2: Visual Learning Architect
- **Source**: `src/types/index.ts` — ASCII schematic types, `flowcharts`, `visualInspectionMarkers`
- **UI**: `src/components/SevenAgentsModal.tsx` — Agent 2 display with monospace schematic descriptions

### Agent 3: Assessment Specialist
- **Source**: `src/services/examinerEngine.ts` — Keyword-based mark scheme grading with deduction calculation
- **UI**: `src/screens/PracticeScreen.tsx` — Ministry exam practice and trap solver
- **Reference**: `docs/reports/audits/AI_AGENTS_AUDIT.md` — 100% operational assessment agent

### Agent 4: Ministry Examiner & FSRS Engine
- **Source**: `src/services/examinerEngine.ts` — Full FSRS integration, Ministry mark scheme grading
- **Source**: `src/services/fsrs.ts` — Free Spaced Repetition Scheduler v4.5/v5 implementation
- **UI**: `src/screens/FsrsReviewScreen.tsx` — FSRS card review interface

### Agent 5: Metacognitive Evaluator
- **Source**: `src/components/MetacognitiveWidget.tsx` — Confidence-accuracy calibration detecting Dunning-Kruger and imposter syndrome
- **UI**: `src/screens/SettingsScreen.tsx` — Metacognitive feedback display

### Agent 6: Problem-Based Learning (PBL)
- **Source**: `src/services/curriculumRepository.ts` — `PblProject` type with `id`, `title`, `scenario`, `mission`, `rubrics`
- **UI**: `src/screens/TopicDetailScreen.tsx` — PBL tab activation and mission rendering
- **Reference**: `docs/reports/builds/FINAL_DUAL_STACK_ARCHITECTURE_VERIFICATION.md` — PBL mission implementation

### Agent 7: Student Wellbeing & Gamification
- **Source**: `src/services/storage.ts` — `LEGACY_PROFILE_KEY` wellbeing profile, streak tracking, badge system (`b_streak7`, earned badges)
- **UI**: `src/components/SevenAgentsModal.tsx` — Agent 7 display with streaks, badges, anxiety scoring, Pomodoro
- **UI**: `src/screens/DashboardScreen.tsx` — Streak display and gamification elements

## Current vs Historical Agent Count

- **Current verified count**: 7 agents (all have actual implementation in source code)
- **Historical claim**: 5 agents (documented in `AI_AGENTS_AUDIT.md`, `FINAL_AI_AGENTS_ACCEPTANCE_REPORT.md`, and earlier audits)
- **Reconciliation**: `docs/reports/AGENT_COUNT_RECONCILIATION.md` explains that Agents 6 (PBL) and 7 (Wellbeing) were incrementally added after the initial 5-agent audit was published. Historical reports are preserved as-is.

## Runtime Status

All 7 agents are **fully operational** with production code, rigorous models, and zero placeholder stubs. Each agent participates in the application runtime through defined registration/invocation paths.

## See Also

- [Agent Count Reconciliation](docs/reports/AGENT_COUNT_RECONCILIATION.md) — Full explanation of 5-agent vs 7-agent discrepancy
- [AI Agents Audit](docs/reports/audits/AI_AGENTS_AUDIT.md) — Original 5-agent system validation
- [AI Agents Acceptance Report](docs/reports/audits/FINAL_AI_AGENTS_ACCEPTANCE_REPORT.md) — Gates R6 & R7 verification
- [README.md](README.md) — Project overview with agent framework table