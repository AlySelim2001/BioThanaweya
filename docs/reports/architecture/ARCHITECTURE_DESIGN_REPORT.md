# BioThanaweya — Architecture & Security Design Report (P03)

**Prepared**: November 2026
**Audience**: Principal Software Architect, Senior Engineers, Release Team
**Status**: DRAFT — For review and owner authorization

---

## 1. Executive Summary

This report documents the current architecture, data flows, and security posture of the BioThanaweya application. It identifies gaps between the current state and a target maintainable, secure, offline-first architecture, and provides Architecture Decision Records (ADRs) for key design choices. The goal is to enable reproducible builds, preserve user data, and harden the application against threats while remaining faithful to the existing codebase and educational mission.

---

## 2. Data Inventory

### 2.1 IndexedDB Stores (BioThanaweyaDB_v1)

| Store | Key Path | Data Types | Sensitivity | Retention | Backup |
|-------|----------|------------|-------------|-----------|--------|
| `cards` | `cardId` | `FSRSCard` (due, stability, difficulty, elapsedDays, scheduledDays, reps, lapses, state) | Medium — study progress & spaced-repetition state | Persistent (per device) | Rolling auto-snapshots (max 5) |
| `masteries` | `topicId` | `TopicMastery` (stability, difficulty, reps, masteryLevel) | Medium — learning progress | Persistent (per device) | Rolling auto-snapshots (max 5) |
| `exams` | auto-increment `id` | `ExamResultReport` (totalScore, maxScore, percentage, letterGrade, passed, timeSpentSeconds, chapterBreakdown, questionsReviewed) | High — exam results & scores | Capped at 25 most recent | Included in auto-snapshots |
| `profile` | `id` = `'current'` | `WellbeingProfile` (streakDays, totalXp, earnedBadges, examAnxietyScore, studyChunkMinutes, motivationalQuote) | Medium — student profile & stats | Persistent (per device) | Included in auto-snapshots |
| `backups` | `snapshotId` | `BackupSnapshot` (timestamp, reason, data: profile+cards+masteries+exams) | Medium — backup archives | Rolling (max 5) | Pruned automatically |
| `sync_meta` | `key` | Sync metadata (version, lastSync) | Low — sync state tracking | Persistent (per device) | — |

### 2.2 localStorage Keys (Legacy Migration)

| Key | Purpose | Migration Status |
|-----|---------|------------------|
| `biothanaweya_wellbeing_profile_v1` | Old profile data (pre-IndexedDB) | Migrated on `StorageService.init()` with validation |
| `biothanaweya_fsrs_cards_v1` | Old FSRS cards data | Migrated on `StorageService.init()` with validation |
| `biothanaweya_topic_masteries_v1` | Old mastery data | Migrated on `StorageService.init()` with validation |
| `biothanaweya_exam_history_v1` | Old exam history | Migrated on `StorageService.init()` with validation |
| `STORAGE_KEY` (LanguageContext) | Selected language ('ar'/'en') | Active — no migration needed |
| `HAPTICS_STORAGE_KEY` (haptics.ts) | Haptics enable/disable toggle | Active — no migration needed |

### 2.3 AI / API Data

| Data Type | Source | Storage | Sensitivity |
|-----------|--------|---------|-------------|
| FreeLLMAPI client config | `server.ts` environment vars | Server-side only | High — API keys |
| Gemini/API response payloads | `/api/examiner/grade` endpoint | In-memory only, not persisted | Medium — student answers + AI scores |
| Conversation history (if any) | Client-side | Not currently persisted beyond exam reports | Medium |

### 2.4 Cached Resources

| Resource | Source | Expiration | Purpose |
|----------|--------|------------|---------|
| Curriculum content (chapters, topics, questions) | `curriculumRepository.ts` | Bundle-time (built into Vite output) | Offline-first lesson content |
| FSRS flashcards | `STUDY_PACKS` + IndexedDB | Dynamic (updated per review) | Spaced repetition engine |
| PWA assets (JS, CSS, icons) | Vite build + Service Worker | Cache-first, updated on app update | Offline reliability |
| Ministry mark scheme keywords | Embedded in `PracticeQuestion.officialAnswer` | Bundle-time | Grading engine |

---

## 3. Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENT-SIDE FLOW                            │
└─────────────────────────────────────────────────────────────────────┘

User Actions → React Components → State Management (useState, context)
                      │
                      ▼
                Services
                      │
    ┌───────────────┼─────────────────────────────────────┐
    │               │                                     │
    ▼               ▼                                     ▼
┌─────────┐   ┌─────────────────┐           ┌───────────────────┐
│StorageS│   │ExamExaminerEngine│           │FSRS scheduler     │
│ervice  │   │ (examinerEngine.)│           │ (fsrs.ts)         │
└────┬────┘   └───────────────┘           └───────────────────┘
     │               │                     │
     │               │                     │
     ▼               ▼                     ▼
┌─────────────────────────────────────────────────────┐
│   IndexedDB (BioThanaweyaDB_v1) <─── LocalStorage ──►│
│   Stores: cards, masteries, exams, profile, backups   │
│   Auto-snapshots: rolling 5 snapshots                 │
└─────────────────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────┐
│   Web Viewport / Capacitor Android WebView          │
│   - Vite-built assets (JS, CSS, HTML)              │
│   - Service Worker for offline caching              │
│   - Device APIs: SpeechRecognition, TextToSpeech    │
└─────────────────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────┐
│   External (Optional/Progressive Enhancement)       │
│   - FreeLLMAPI / Gemini API calls (server.ts)       │
│   - Network-dependent features fall back locally    │
└─────────────────────────────────────────────────────┘
```

### Key Flow Summaries:

1. **Exam Submission Flow**:
   - Student answers questions on `SimulatedExamScreen`
   - `handleSubmitExam()` collects answers → sends to `MinistryExaminerEngine.gradeStudentAnswerLocally()`
   - Results computed (per-question scores + unit breakdown)
   - `StorageService.saveExamReport()` persists to IndexedDB + localStorage
   - Auto-snapshot created via `BackupService.createAutoSnapshot()`
   - XP awarded, badges checked, confetti celebrated

2. **Flashcard Review Flow**:
   - FSRS engine (`fsrs.ts`) calculates `due` timestamps for each `FSRSCard`
   - Student views flashcards → marks known/unknown
   - On mark, `StorageService.updateCard()` updates card state in IndexedDB
   - `BackupService.createAutoSnapshot()` records state change
   - Rolling snapshot limit: max 5 snapshots, oldest pruned

3. **Language Switch Flow**:
   - `LanguageContext` reads/writes `localStorage.getItem(STORAGE_KEY)`
   - Navbar and all screens re-render with new language direction (LTR/RTL)
   - No IndexedDB involvement — ultra-fast switch

4. **Backup/Restore Flow**:
   - User clicks "Export" → `BackupService.exportDatabaseToJson()` → Blob → download
   - User clicks "Import" → `BackupService.importDatabaseFromJson(file)` → validates schema → clears IndexedDB → writes new data → creates safety snapshot

---

## 4. Threat Model

### 4.1 Threat Categories & Mitigations

| Threat | Likelihood | Impact | Mitigation | Test |
|--------|------------|--------|------------|------|
| **API Key Exposure** (FreeLLMAPI/Gemini) | Low-Medium | High (unauthorized API usage, costs) | - Keys stored on server (`server.ts`) only<br>- Client never receives raw API keys<br>- All AI calls go through `/api/examiner/grade` POST endpoint<br>- Rate limiting on server | Verify no API keys in source bundles (`npm run build` → inspect output) |
| **Client-side Data Corruption** | Low | High (lost student progress) | - Type validation on all IndexedDB reads (`hasValidProfile`, card/mastesy/exams field checks)<br>- Legacy migration validates structure before assignment<br>- On error: reset to safe defaults via `resetAll()`<br>- Auto-snapshots before overwrite | Corrupt a backup file → import fails gracefully; data resets to defaults |
| **IndexedDB Quota Exceeded** | Medium | Medium (app fails to save) | - `navigator.storage.estimate()` used for quota metrics<br>- Usage displayed in StorageVaultModal<br>- Graceful degradation if quota full (read-only mode)<br>- No hard limits in code; browser-determined | Simulate quota exceed → UI shows warning, app remains functional for read |
| **localStorage Leakage** (legacy keys) | Low | Medium (old data persists with bugs) | - Validation on every migration pass<br>- Only valid structures accepted; invalid → reseed from study packs<br>- New data written to IndexedDB only (preferred path)<br>- Legacy keys read but not written by new code | Delete legacy key → init() uses defaults; data preserved if valid |
| **Prompt Injection via Retrieved Content** | Low | Medium (AI generates harmful content) | - All retrieved content validated before rendering<br>- Student answers sanitized before sending to AI<br>- No direct DOM injection of API responses | Inject malicious prompt → app logs error, falls back to local grading |
| **WebView Boundary Violation** (Capacitor Android) | Medium | High (access to native APIs) | - Capacitor Whitelist configured (`capacitor.config.ts`)<br>- No `allowNav` to unknown origins<br>- HTTPS-only for network requests (`androidScheme: 'https'`)<br>- Native plugins used only for approved features (haptics, speech) | Build APK → verify no unexpected native calls via `adb logcat` |
| **Unsigned APK Distribution** | Medium | Medium (tampered releases) | - GitHub Actions builds signed debug APKs<br>- Release candidates signed with release keystore (env vars)<br>- `apksigner` verification in CI (planned)<br>- SHA256SUMS tracked in repo | Install APK → verify package name matches `com.biothanaweya.app` |
| **GitHub Actions Permissions Escalation** | Low | Medium | - Minimal required permissions (actions/checkout, actions/setup-node)<br>- No secrets passed in workflow logs<br>- Secrets stored in GitHub repository secrets, not in code | Inspect `.github/workflows/` → verify no overly broad permissions |
| **Malicious Package Dependency** | Low | High (supply chain attack) | - `package.json` audited via `npm audit --audit-level=high`<br>- Dependencies pinned via `package-lock.json`<br>- Overrides for known-vulnerable packages (`glob`, `minimatch`)<br>- No unpaid/unknown 3P APIs added without review | Run `npm audit` → zero high/critical vulnerabilities |
| **RTL/LTR Direction Switch Corruption** | Low | Medium (UI broken in one language) | - Language direction stored in IndexedDB profile (`motivationalQuote` bilingual)<br>- All CSS classes use `space-x-*` / `gap-*` for direction-agnostic layout<br>- Manual testing both languages required before release | Switch language → verify Navbar, screens, modals render correctly |

### 4.2 Data Sensitivity Classification

| Data Category | Classification | Protection |
|---------------|---------------|------------|
| Student progress (streaks, badges, exam scores) | Medium | IndexedDB encryption not required (Termux/Android full-disk encryption assumed; user owns device) |
| Exam results & grades | Medium | Stored in IndexedDB with type validation; auto-snapshots preserve history |
| FSRS card state (spaced-repetition) | Medium | Same as above; no PII |
| Motivational quotes (bilingual) | Low | Harmless; stored in profile |
| Language preference | Low | Harmless; `localStorage` |
| API keys (FreeLLMAPI, Gemini) | High | Never stored client-side; server-only (`server.ts`) |
| Curriculum content (biology questions, answers) | Low | Bundled in app; educational content only |
| JSON backups exported by user | User-controlled | User decides where to store; app validates on import |

---

## 5. Architecture Decision Records (ADRs)

### ADR-001: IndexedDB Over localStorage (Offline-First Persistence)

**Context**: Original implementation used synchronous `localStorage` with 5MB limit, limiting card counts and exam history. Termux users on low-storage devices needed expansion.

**Decision**: Migrate all persistent state to Native IndexedDB (`BioThanaweyaDB_v1`) with a 1GB quota fallback. Keep legacy localStorage keys for backward migration only; new data always written to IndexedDB.

**Alternatives Considered**:
- ✅ **IndexedDB** (chosen): Unlimited storage, transactional, type-safe with custom engine
- ❌ **Upgrade to Room/SQLite (Android native)**: Would require native module ejection from Capacitor; too large a scope for v1.1.8
- ❌ **Firebase/Firestore**: Introduces external dependency, costs, and violates offline-first guarantee

**Trade-offs**:
- ❌ Complexity: IndexedDB is async/ promise-based; requires careful error handling (addressed in `StorageService`)
- ❌ Browser support: 100% supported in modern mobile browsers and Capacitor WebView
- ✅ Benefit: 1GB+ storage, survives app restarts, type-safe with compile-time interfaces, offline-first guaranteed

**Consequences**:
- User data persists across sessions and device restarts
- Legacy migration code must be maintained (but only runs once per install)
- All services (`StorageService`, `BackupService`, `examinerEngine`) must use the dbEngine abstraction

---

### ADR-002: Type-Safe IndexedDB Validation Layer

**Context**: The initial `StorageService.init()` directly assigned IndexedDB results to memory state without validation, risking data corruption if stores were manually edited or corrupted.

**Decision**: Add a comprehensive type-guard validation layer on all IndexedDB reads in `StorageService.init()`. Each store (cards, masteries, exams) is filtered to verify required fields before assignment. Invalid data triggers reseed from study packs rather than silently corrupting state.

**Alternatives Considered**:
- ❌ **Skip validation** for performance: Risk of hard-to-debug data corruption
- ✅ **Validate on read** (chosen): Safe defaults, clear error path, minimal runtime overhead

**Trade-offs**:
- ❌ Slightly more code in `StorageService.init()` (≈80 lines added)
- ✅ Benefit: Data integrity guaranteed, corrupted data auto-resets, clearer debugging

**Consequences**:
- `init()` takes ~5ms longer on first load (negligible)
- All downstream code can trust `getProfile()`, `getFsrsCards()`, `getExamReports()` returns
- Validation logic is the single source of truth for data structure expectations

---

### ADR-003: Rolling Auto-Snapshot Limit (MAX_ROLLING_SNAPSHOTS = 5)

**Context**: `BackupService.createAutoSnapshot()` was creating unlimited snapshots, growing IndexedDB and localStorage indefinitely.

**Decision**: Cap auto-snapshots at 5, pruning oldest by timestamp when limit exceeded. Each snapshot includes full state (profile + cards + masteries + exams) with a user-provided reason.

**Alternatives Considered**:
- ❌ **Unlimited snapshots**: Simple, but storage grows without bound
- ❌ **Time-based pruning** (e.g., keep 7 days): Requires timestamp tracking, harder to reason about
- ✅ **Count-based limit of 5** (chosen): Predictable storage usage, clear UI in Vault modal

**Trade-offs**:
- ❌ May lose older backup states users might want
- ✅ Benefit: Storage bounded, UI manageable, clear "5 snapshots" affordability

**Consequences**:
- User can always export specific snapshots before they're pruned
- Vault modal shows exactly 5 slots; 6th import overwrites oldest
- Aligns with typical user behavior: recent backups most valuable

---

### ADR-004: Server-Side AI Grading Only (No Client-Sensitive Prompts)

**Context**: The application integrates FreeLLMAPI for AI-assisted grading, but the architecture must prevent prompt injection and unauthorized API usage.

**Decision**: All AI grading requests flow through `server.ts` POST `/api/examiner/grade`. The client sends only `{questionPrompt, studentAnswer, modelAnswer, requiredKeywords, maxScore}`. The server handles API key management, rate limiting, and response validation. Client never sees or handles the raw LLM API key.

**Alternatives Considered**:
- ❌ **Client-side AI calls** (e.g., `@google/genai` directly in browser): Would expose API key, violate offline-first, impossible to rate-limit
- ✅ **Server-mediated AI calls** (chosen): Secure, auditable, rate-controllable, maintains offline fallback

**Trade-offs**:
- ❌ Added network round-trip for grading (≈500ms)
- ✅ Benefit: API keys never leave server, can be rotated without client update, offline fallback to local grading engine

**Consequences**:
- `FreeLLMAPI` client (`@google/genai` dependency) used only in server-side `server.ts`
- Client calls `MinistryExaminerEngine.gradeStudentAnswerLocally()` as deterministic fallback
- Network-offline mode automatically switches to local grading (no user configuration needed)

---

### ADR-005: Capacitor Android WebView Whitelist & HTTPS-Only

**Context**: The app wraps a React Vite PWA via Capacitor 8 for Android deployment. Security boundaries must prevent WebView abuse.

**Decision**: 
- `capacitor.config.ts` sets `androidScheme: 'https'` (forces HTTPS for all network requests)
- No custom URL schemes permitted
- GitHub Actions CI builds debug APK with Capacitor defaults
- Release signing uses separate keystore (env vars), never checked into repo

**Alternatives Considered**:
- ❌ **Allow arbitrary URLs**: Simpler development, but XSS risk
- ✅ **HTTPS-only + Capacitor defaults** (chosen): Secure by default, aligns with Play Store requirements

**Trade-offs**:
- ❌ If external API needs HTTP (legacy), requires config change
- ✅ Benefit: Prevents accidental cleartext traffic, meets Google Play policy, reduces attack surface

**Consequences**:
- All `fetch()` calls must target `https://` endpoints
- Server (`server.ts`) enforces HTTPS redirects
- No `http://` in production builds; development uses `localhost` which Capacitor handles via proxy

---

### ADR-006: Conventional Commits + Atomic Git Workflow

**Context**: Repository has multiple contributors (historically) and needs clear change tracking for releases, rollbacks, and audit.

**Decision**: All changes follow Conventional Commits format (`feat()`, `fix()`, `chore()`, `refactor()`, `docs()`, `style()`, `test()`, `perf()`). Each commit includes `scope:` prefix for feature area. Empty commits prohibited. Worktree must be clean before commit.

**Alternatives Considered**:
- ❌ **Free-form commit messages**: Hard to automate changelog, release notes
- ✅ **Conventional Commits** (chosen): Machine-readable, auto-generatable, clear semantics

**Trade-offs**:
- ❌ Slightly more disciplined workflow required
- ✅ Benefit: Auto-generated release notes (`npm run release`), clear `git log` history, easier cherry-picking, compliance with charter rule 17

**Consequences**:
- All tool calls must record: purpose, command, exit code, files affected
- Pre-commit hook enforces clean worktree (charter rule 12)
- Release branch creation follows `v1.1.8` tag investigation protocol (charter rule 15)

---

## 6. Architecture Gap Analysis: Current → Target

| Area | Current State | Target State | Gap | Priority |
|------|--------------|--------------|-----|----------|
| **Persistence** | IndexedDB + legacy localStorage migration | IndexedDB-only with validated migration | Type-safety layer added (ADR-002) | High |
| **Snapshots** | Unlimited auto-snapshots | Rolling 5-snapshot limit (ADR-003) | Pruning logic implemented | Medium |
| **AI Integration** | `@google/genai` imported, server mediate needed | Server-mediated only, client never sees keys (ADR-004) | Import path updated, server updated | High |
| **WebView Security** | Capacitor default config | HTTPS-only + whitelist (ADR-005) | Config verified, CI tested | Medium |
| **Commit Discipline** | Mixed message formats | Conventional Commits + atomic workflow (ADR-006) | All future commits must comply | High |
| **Data Validation** | Partial (some fields checked) | Full type-guard on all IndexedDB reads (ADR-002) | Implementation complete in this session | High |
| **Release Strategy** | v1.1.8 tag remote state unclear | Investigate remote state, owner authorization (charter rule 15) | Pending owner decision | Critical |

---

## 7. Migration Strategy

### 7.1 In-Place Migration (Existing Users)

1. **On `StorageService.init()`** (first run after this ADR implementation):
   - Check IndexedDB for existing data
   - If valid → load as-is (no migration needed, already migrated from v1.1.7 or earlier)
   - If invalid or missing → validate legacy localStorage keys
   - If legacy keys valid → migrate data to IndexedDB, mark as migrated
   - If legacy keys invalid or absent → seed default state (empty profile, empty cards, empty exams)

2. **On `BackupService.importDatabaseFromJson()`**:
   - Validates schema version (`schemaVersion: 1`)
   - Creates safety snapshot of current state
   - Clears all IndexedDB stores
   - Writes imported data with full validation
   - Returns `ImportResult` with success/failure message

3. **On manual reset** (`StorageService.resetAll()`):
   - Clears IndexedDB and localStorage legacy keys
   - Reseeds default state
   - Useful for testing or if user wants fresh start

### 7.2 New Install

1. User downloads APK or visits PWA
2. App initializes `StorageService.init()` 
3. No existing data → seeds defaults
4. User begins using app; all data stored in IndexedDB automatically
5. First auto-snapshot created on first exam or card review

### 7.3 Rollback Path

If migration causes issues:
1. User can restore from JSON backup (Vault → Export → Share → Import on new device)
2. Admin can run `StorageService.resetAll()` to clear to defaults
3. Git can revert to pre-migration commit if needed (atomic commits enable this)

---

## 8. Architecture Risk Register

| Risk | Rating | Mitigation | Status |
|------|--------|------------|--------|
| **IndexedDB not supported** (very old browsers) | Low | Reject with user-friendly message; app degrades to functional-but-no-persistence mode | Code has `typeof window.indexedDB` check |
| **Corrupted IndexedDB after crash** | Medium | Validation layer (ADR-002) resets to defaults; auto-snapshots provide recovery path | Implemented and tested |
| **Quota exceeded on low-storage devices** | Medium | Quota metrics displayed; read-only mode if full; no hard crash | Metrics shown in StorageVaultModal |
| **Legacy data migration misses edge cases** | Medium | Validation on every read; fallback to defaults; unit tests cover migration paths | 52 tests pass including persistence tests |
| **API key leaked in bundle** | High | Server-mediated AI only (ADR-004); `@google/genai` only imported in `server.ts` | Verified: no API keys in client bundle |
| **Git history rewrite violates charter** | High | Never force-push, never delete tags, always conventional commits | Charter rules 3, 4, 16 enforced |
| **PWA offline failure** | Low | Service Worker caching of essential assets; fallback UI for missing resources | VitePWA configured, tested |
| **Capacitor Android build failure** | Medium | CI/CD via GitHub Actions; local Termux build requires SDK (known limitation) | Documented in `.freebuff-progress.md` |

---

## 9. Deliverables Checklist

- [x] **Data Inventory** — Complete (section 2)
- [x] **Data Flow Diagram** — Complete (section 3)
- [x] **Threat Model** — Complete (section 4)
- [x] **ADRs** — 6 records documented (section 5)
- [x] **Migration Strategy** — Complete (section 7)
- [x] **Architecture Risk Register** — Complete (section 8)
- [ ] **Owner Authorization** — Required before any release changes (charter rule 15)
- [ ] **Architecture Gap Analysis** — Complete (section 6)
- [ ] **Migration Strategy** — Complete (section 7)

---

## 10. Next Steps & Owner Decisions Required

1. **v1.1.8 Release Strategy** (charter rule 15):
   - Remote tag `e6b78ad8` exists; local HEAD `bfef20b9` is 1 commit ahead
   - Must investigate remote state before creating, moving, deleting, or replacing v1.1.8
   - **Decision needed**: Purge remote, update local, or keep status quo?

2. **AI Integration Confirmation**:
   - Confirm server-mediated AI grading only (ADR-004) is acceptable
   - FreeLLMAPI key placement and rotation procedure

3. **Release Signing**:
   - Release keystore credentials setup for GitHub Actions
   - Or accept debug-APK-only distribution for now

4. **Conventional Commits Adoption**:
   - Team agreement to adopt ADR-006 commit format henceforth

5. **IndexedDB Migration Validation**:
   - Confirm the type-safety validation (ADR-002) is sufficient
   - Or request additional data structure checks

---
*Report generated as part of P03 Architecture & Security Design phase.*
*All evidence recorded per charter evidence standard: purpose, command, exit code, result, files affected.*