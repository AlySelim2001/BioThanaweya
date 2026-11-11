# Test Harness & Database Controller Remediation Plan

Hardening the local SQLite database service and Riverpod state controllers to resolve all failing test cases and achieve 100% clean test execution (`flutter test --coverage` with Exit Code 0).

## User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural decisions were confirmed via interactive clarification:
> - **SQLite Test Isolation**: Standardize unit tests on `sqflite_common_ffi` with temporary directory databases (`Directory.systemTemp.createTemp`) and explicit `databaseFactory = databaseFactoryFfi` initialization per test group.
> - **Controller State Hydration**: Eagerly seed initial state synchronously upon controller instantiation (e.g. from local curriculum datasources), ensuring immediate non-empty state in synchronous test assertions while running asynchronous refreshes in the background.
> - **Service Location & Backward Compatibility**: Maintain the canonical implementation at `lib/core/database/local_database_service.dart` and provide an export alias forwarding file at `lib/core/services/local_database_service.dart`.

---

## 1. Overview & Core Concept

- **What It Does**: Fixes test fixture initialization, mock database bindings, and controller state management so that all database queries, spaced-repetition (FSRS) mistakes persistence, benchmark telemetry, and controller UI interactions pass with zero unhandled exceptions or assertion failures.
- **Target Audience**: Flutter CI/CD automated test runners (GitHub Actions) and developers running local test suites in Termux, Linux, and developer workstations.
- **Key Value**: Guarantees offline database reliability, prevents runtime initialization crashes, and delivers an uncompromising 100% green test pass rate.

---

## 2. Technical Architecture & Test Strategy

```
┌─────────────────────────────────────────────────────────────────┐
│                 Test Execution Context (Dart VM)                │
└────────────────────────────────┬────────────────────────────────┘
                                 │
                 sqfliteFfiInit() & databaseFactoryFfi
                                 │
         ┌───────────────────────┴───────────────────────┐
         ▼                                               ▼
┌────────────────────────────────┐              ┌─────────────────────────────┐
│ Architecture Hub & DB Tests    │              │ Local Database Tests        │
│ (Isolated Temp SQLite DB)      │              │ (Isolated Temp SQLite DB)   │
└────────────────┬───────────────┘              └──────────────┬──────────────┘
                 │                                             │
                 ▼                                             ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│             LocalDatabaseService (lib/core/database/ & services/)           │
│  - 11 Indexed Curriculum Chapters                                           │
│  - 3 Official Ministry Textbooks                                            │
│  - Recursive Transaction Safeguards in _seedCurriculum                      │
│  - Full-Text Search (FTS) & FSRS Spaced-Repetition Tracking                  │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
         ┌───────────────────────┴───────────────────────┐
         ▼                                               ▼
┌────────────────────────────────┐              ┌─────────────────────────────┐
│ BiologyLabController           │              │ StudyPlannerController      │
│ - Synchronous initial diagrams │              │ - Synchronous initial tasks │
│ - Immediate active selection   │              │ - Immediate completion %    │
└────────────────────────────────┘              └─────────────────────────────┘
```

---

## 3. Product & Engineering Decisions

- **Decision 1: SQLite Transaction Nesting in `_seedCurriculum`**
  - *Chosen Approach*: Refactor `_seedCurriculum` to accept a `DatabaseExecutor` (which can be either a `Database` or an active `Transaction`), avoiding nested `db.transaction(...)` calls inside `_createSchema` (which sqflite already executes inside an active transaction).
  - *Why*: Eliminates `SqfliteDatabaseException: cannot start a transaction within a transaction` when creating or reseeding databases.

- **Decision 2: Eager Initial State Seeding in StateNotifiers**
  - *Chosen Approach*: Initialize `BiologyLabController` with `CurriculumLabDatabase.getCoreCurriculumDiagrams()` and `StudyPlannerController` with default adaptive tasks directly in their constructors' `super(...)` calls.
  - *Why*: Resolves race conditions in synchronous unit tests that expect `controller.state.diagrams.isNotEmpty` and `controller.state.tasks.isNotEmpty` immediately upon creation without requiring brittle sleep timers.

- **Decision 3: Canonical Export Forwarder**
  - *Chosen Approach*: Keep `lib/core/database/local_database_service.dart` and add `lib/core/services/local_database_service.dart` containing `export '../database/local_database_service.dart';`.
  - *Why*: Provides 100% backward compatibility for imports expecting `lib/core/services/local_database_service.dart` while preserving the clean architecture directory structure.

---

## 4. Step-by-Step Implementation Steps

1. **`lib/core/database/local_database_service.dart`**:
   - Refactor `_seedCurriculum` to take `DatabaseExecutor db` rather than initiating a redundant nested transaction.
   - Ensure all 11 chapters, 3 textbooks, sample questions, and FSRS tables initialize cleanly.
   - Verify `totalIndexedChapters` (11) and `totalIndexedTextbooks` (3) getters.

2. **`lib/core/services/local_database_service.dart`**:
   - Create export forwarder: `export '../database/local_database_service.dart';`.

3. **`test/local_database_service_test.dart`**:
   - Initialize `sqfliteFfiInit()` and `databaseFactory = databaseFactoryFfi;`.
   - Setup an isolated temporary directory per test run to prevent file contention and state leakage.
   - Run tests against the isolated instance with proper teardown (`close()` and temp directory deletion).

4. **`test/architecture_hub_and_database_test.dart`**:
   - Verify all test assertions align with the robust 11-chapter curriculum schema and clean teardown.

5. **`lib/features/biology_lab/presentation/controllers/biology_lab_controller.dart`**:
   - Eagerly populate `diagrams` in `super(LabSessionState(...))` using `CurriculumLabDatabase.getCoreCurriculumDiagrams()`.

6. **`lib/features/study_planner/presentation/controllers/study_planner_controller.dart`**:
   - Eagerly populate `tasks` in `super(PlannerSessionState(...))` with adaptive baseline tasks so initial state is non-empty.

7. **Verification**:
   - Validate syntax and types across all files.
   - Run verification tools to confirm zero regressions.
