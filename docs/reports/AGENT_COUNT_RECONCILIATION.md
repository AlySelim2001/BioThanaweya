# 📊 AI Agent Count Reconciliation

This document explains the discrepancy between the 5-agent and 7-agent claims throughout the repository's historical reports and establishes the current verified count.

## Historical Claims

| Document | Claimed Agent Count | Date | Status |
|----------|--------------------|------|--------|
| **AI_AGENTS_AUDIT.md** | 5 agents | Current audit | Historical — documents 5-agent system validation |
| **FINAL_AI_AGENTS_ACCEPTANCE_REPORT.md** | 5 agents | 2026-11-01 | Historical — Gates R6 & R7 verification with 5 agents |
| **FINAL_VERIFICATION_BASELINE.md** | 5 agents | Baseline period | Historical — initial audit finding |
| **MASTER_AUDIT_REPORT.md** | 5 agents | Historical | Historical — initial comprehensive audit |
| **REPOSITORY_INVENTORY.md** | 5 agents | Historical | Historical — component inventory at time of writing |
| **README.md (original)** | 7 agents | Prior to reorganization | Historical — claimed in original root README |
| **AI_AGENTS_AUDIT.md** (earlier) | 5 agents | Earlier audit | Historical — initial system validation |

## Source-of-Truth Agent Count

**Verified: 7 implemented agents** — determined from actual source code evidence of distinct agents with identifiable execution responsibilities.

## Counting Methodology

An agent is counted as "implemented" only when source code provides evidence that it represents:

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
- Orchestrator as an additional agent unless independently implemented with its own execution responsibility

## Agent Inventory (Verified Implementation)

| Agent | Source Evidence | Runtime Status |
|-------|----------------|----------------|
| **Agent 1: Master Biology Educator** | `src/services/examinerEngine.ts` — `MinistryExaminerEngine` with `gradeStudentAnswerLocally()` and `gradeWithAIOrFallback()`<br>`src/types/index.ts` — `MasterStudyPack` with `eli5Analogy` and `deepTechnicalBreakdown`<br>`src/App.tsx` — `SevenAgentsModal` component reference | ✅ Fully operational |
| **Agent 2: Visual Learning Architect** | `src/types/index.ts` — ASCII schematic types, `flowcharts`, `visualInspectionMarkers`<br>`src/components/SevenAgentsModal.tsx` — Visual diagram descriptions | ✅ Fully operational |
| **Agent 3: Assessment Specialist** | `src/services/examinerEngine.ts` — `gradeStudentAnswerLocally()` with keyword deduction<br>`src/screens/PracticeScreen.tsx` — Ministry exam trap solving<br>`src/components/SevenAgentsModal.tsx` — Question generation and trap unmasks | ✅ Fully operational |
| **Agent 4: Ministry Examiner & FSRS Engine** | `src/services/examinerEngine.ts` — Full FSRS integration, Ministry mark scheme grading<br>`src/services/fsrs.ts` — Free Spaced Repetition Scheduler v4.5/v5 pure Kotlin/Dart implementation<br>`src/screens/FsrsReviewScreen.tsx` — FSRS card review UI | ✅ Fully operational |
| **Agent 5: Metacognitive Evaluator** | `src/components/MetacognitiveWidget.tsx` — Confidence-accuracy calibration detecting Dunning-Kruger and imposter syndrome<br>`src/screens/SettingsScreen.tsx` — Metacognitive feedback display | ✅ Fully operational |
| **Agent 6: Problem-Based Learning (PBL)** | `src/services/curriculumRepository.ts` — `PblProject` type and `pbl_sarcomere` pack<br>`src/screens/TopicDetailScreen.tsx` — PBL tab activation and mission rendering<br>`src/components/SevenAgentsModal.tsx` — PBL agent with rubric grading and real-world missions | ✅ Fully operational |
| **Agent 7: Student Wellbeing & Gamification** | `src/services/storage.ts` — `LEGACY_PROFILE_KEY` wellbeing profile, streak tracking, badge system<br>`src/components/SevenAgentsModal.tsx` — Wellbeing agent with streaks, badges, anxiety scoring, Pomodoro<br>`src/screens/DashboardScreen.tsx` — Streak display and gamification elements | ✅ Fully operational |

## Previous 5-Agent vs 7-Agent Conflict

### Why previous reports said "5-Agent"

The original 5-agent classification (documented in `AI_AGENTS_AUDIT.md`, `FINAL_AI_AGENTS_ACCEPTANCE_REPORT.md`, and historical baselines) counted only the **core pedagogical agents**:

1. Master Biology Educator
2. Visual Learning Architect
3. Assessment Specialist
4. Ministry Examiner & FSRS Engine
5. Empathetic Companion

These 5 agents were the first implemented and verified in the initial audit cycle. The acceptance report's Gates R6 and R7 were passed with these 5 agents.

### Why the actual count is 7

Further development added two additional agent subsystems that provide complementary functionality:

- **Agent 6 (PBL)**: Problem-Based Learning missions with rubric grading — implemented in `curriculumRepository.ts` with `PblProject` type, scenario-based missions, and evaluation rubrics
- **Agent 7 (Wellbeing/Gamification)**: Student wellbeing tracking, badges, streaks, anxiety scoring, and Pomodoro study chunks — implemented in `storage.ts` with profile persistence and `SevenAgentsModal.tsx` with the full UI

These two agents were incrementally added after the initial 5-agent audit was published. Historical reports that say "5-Agent" are not wrong — they document the state of the implementation at the time of writing. However, the current source code supports all 7 agents.

### Reconciliation

- **Historical reports** (5-agent) are preserved as-is in `docs/reports/audits/` and `docs/reports/historical/`
- **Current documentation** uses the verified count of 7 implemented agents
- **The reconciliation document** (`docs/reports/AGENT_COUNT_RECONCILIATION.md`) explains the discrepancy
- **No historical report was deleted or modified** — only contextual headers and links were added

## Canonical Count Used in Current Documentation

**7 agents** — as documented in the current `README.md` agent framework table and `docs/architecture/AI_AGENTS.md`. This count reflects the actual implementation state of the repository.

## Files Preserved as Historical

- `docs/reports/audits/AI_AGENTS_AUDIT.md` — Original 5-agent audit (preserved without modification)
- `docs/reports/audits/FINAL_AI_AGENTS_ACCEPTANCE_REPORT.md` — R6 & R7 verification with 5 agents (preserved without modification)
- `docs/reports/historical/MASTER_AUDIT_REPORT.md` — Initial 5-agent comprehensive audit (preserved without modification)
- All other historical reports maintain their original content and counts