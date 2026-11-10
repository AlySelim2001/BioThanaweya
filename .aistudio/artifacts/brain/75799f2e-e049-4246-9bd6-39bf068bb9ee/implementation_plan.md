# BioThanaweya Test Suite & Offline Database Mock Remediation

Comprehensive stabilization and clean architecture refactoring for the BioThanaweya Flutter test suite, addressing state propagation in BiologyLabController, reactive async settlement in StudyPlannerController, and thread-safe SQLite mock isolation in LocalDatabaseService.

## User Review & Critical Decisions

> [!IMPORTANT]
> All requested changes adhere strictly to Clean Architecture and offline-first test isolation:
> - **Thread-Safe SQLite Mock (`test/mocks/database_mock.dart`)**: Uses `sqflite_common_ffi` with in-memory or isolated file SQLite instances pre-seeded with all 11 curriculum chapters and 3 official textbooks before any `setUp` lifecycle.
> - **Biology Lab State Initialization (`test/biology_lab_test.dart`)**: Instantiates `BiologyLabController` with guaranteed non-null state defaults (`zoom=1.0`, `activeHotspot='none'`) and adds a widget test helper verifying synchronous/asynchronous state transitions.
> - **Study Planner ProviderContainer Refactoring (`test/study_planner_test.dart`)**: Wraps tests with Riverpod `ProviderContainer` and `ProviderScope` overrides for `LocalDatabaseService`, using pump cycles (`tester.pump()` / `tester.pumpAndSettle()`) to ensure reactive state notifiers settle deterministically.

- **Confirmed Decision 1**: Implement pure, thread-safe SQLite mock environment via `sqflite_common_ffi` in `test/mocks/database_mock.dart` with 11-chapter curriculum pre-seeding.
- **Confirmed Decision 2**: Refactor `BiologyLabController` and its tests to guarantee non-null defaults (`zoom: 1.0`, `activeHotspotId: 'none'`) and verify state mutation propagation via test helpers.
- **Confirmed Decision 3**: Upgrade `StudyPlannerController` tests to use Riverpod `ProviderContainer` overrides with asynchronous pump cycles for full reactive state settlement.

---

## 1. Overview & Core Concept

- **What It Does**: Resolves all test failures across `test/biology_lab_test.dart`, `test/study_planner_test.dart`, and `test/local_database_service_test.dart` by establishing proper database mocks, dependency injection overrides, and event-loop pump settlement.
- **Target Audience / Persona**: Mobile engineers and CI/CD pipelines running `flutter test --coverage` for the BioThanaweya application, ensuring 100% test pass rate with zero flaky tests.
- **Key Value**: Delivers zero-uninitialized-database errors, reliable reactive state assertions, and robust test harness infrastructure across all domain modules.

---

## 2. User Experience & Visual Design

### Test Execution & Developer Experience Flows
1. **SQLite Mock Initialization**: Tests invoke `DatabaseMock.createInitialized()` to obtain an in-memory SQLite database populated with all 11 chapters, textbooks, and seed questions prior to test suite execution.
2. **Biology Lab State Verification**: The lab controller tests verify diagram loading, zoom clamping (1.0x to 3.0x), and hotspot activation with explicit widget harness assertions.
3. **Study Planner Reactive Settlement**: Async task generation and toggles settle cleanly via `tester.pumpAndSettle()`, verifying that state percentage calculations match completed task counts.

### Architecture Tokens & Standards
- Clean separation between Mock Database infrastructure, Domain Repositories, and Presentation Controllers.
- Zero placeholder comments (`// TODO`) across all test and source files.
- Thread-safe, cross-platform SQLite test execution (compatible with local JVM/CLI runner and CI/CD Ubuntu runners).

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: SQLite Mocking Strategy (`sqflite_common_ffi` In-Memory vs. SharedPreferences Mock)**
  - *Chosen Approach*: Implement a thread-safe `MockDatabaseFactory` in `test/mocks/database_mock.dart` that configures `sqflite_common_ffi` with `inMemoryDatabasePath` and runs table creation and 11-chapter seed insertion.
  - *Why*: Directly tests genuine SQL queries (`SELECT`, `INSERT`, `UPDATE`, `LIKE`, transactions) with exact schema alignment, preventing false positives from naive in-memory hash maps.
  - *Alternatives Considered*: Pure mockito/mocktail mocking (brittle, misses SQL syntax errors and constraints).

- **Decision 2: Riverpod Test Harness for Study Planner**
  - *Chosen Approach*: Use `ProviderContainer(overrides: [...])` with `ProviderScope` inside `testWidgets` or pure unit test scopes, awaiting asynchronous state changes with proper microtask draining.
  - *Why*: Riverpod `StateNotifier` requires microtask ticks or pump cycles to notify listeners; this guarantees assertions evaluate only after async loading completes.

- **Decision 3: Non-Null Initial Defaults for Biology Lab**
  - *Chosen Approach*: Default `activeHotspotId` to `'none'` (or valid non-null identifier) and `currentZoomScale` to `1.0` in the initial test state.
  - *Why*: Eliminates null pointer exceptions during early widget rendering and state propagation.

---

## 4. Technical Architecture & Data Strategy

### Test Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                          TEST HARNESS LAYER                            │
│  ┌───────────────────────────┐      ┌───────────────────────────────┐  │
│  │   test/biology_lab_test   │      │   test/study_planner_test     │  │
│  │ - Non-null defaults       │      │ - ProviderContainer override  │  │
│  │ - State propagation helper│      │ - tester.pump() settlement    │  │
│  └─────────────┬─────────────┘      └───────────────┬───────────────┘  │
│                │                                    │                  │
│                ▼                                    ▼                  │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │         test/local_database_service_test.dart                    │  │
│  │         - Clean Architecture Dependency Isolation                │  │
│  └──────────────────────────────────┬───────────────────────────────┘  │
└─────────────────────────────────────┼──────────────────────────────────┘
                                      │
┌─────────────────────────────────────▼──────────────────────────────────┐
│                          MOCK DATABASE LAYER                           │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │                  test/mocks/database_mock.dart                   │  │
│  │  - Thread-Safe sqflite_common_ffi In-Memory Database             │  │
│  │  - Schema Definition: chapters, textbooks, questions, mistakes   │  │
│  │  - Pre-Seed: 11 Curriculum Chapters + 3 Textbooks + Traps        │  │
│  │  - Transactional Isolation & TearDown Cleanup                    │  │
│  └──────────────────────────────────┬───────────────────────────────┘  │
└─────────────────────────────────────┼──────────────────────────────────┘
                                      │
┌─────────────────────────────────────▼──────────────────────────────────┐
│                         CORE APPLICATION LAYER                         │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │              lib/core/database/local_database_service.dart       │  │
│  │              lib/features/biology_lab/...                        │  │
│  │              lib/features/study_planner/...                      │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

### Planned File Modifications & Additions

1. **`test/mocks/database_mock.dart` [NEW]**:
   - Implements `DatabaseMock` initializing `sqfliteFfiInit()`, setting `databaseFactory = databaseFactoryFfi`.
   - Creates in-memory or isolated SQLite databases with full schema (`chapters`, `textbooks`, `questions`, `fsrs_mistakes`, `exam_attempts`).
   - Automatically pre-seeds all 11 Egyptian curriculum chapters and 3 textbooks before test execution.

2. **`test/local_database_service_test.dart` [UPDATE]**:
   - Integrates `DatabaseMock` in the `setUp` lifecycle to guarantee 100% initialized tables and seed data.
   - Verifies chapter retrieval, unit filtering, textbooks, and exam attempt recordings against the isolated database.

3. **`test/biology_lab_test.dart` [UPDATE]**:
   - Updates `BiologyLabController` instantiation with non-null initial state values (`zoom = 1.0`, `activeHotspot = 'none'`).
   - Implements a test helper to explicitly verify widget state propagation after controller operations.

4. **`test/study_planner_test.dart` [UPDATE]**:
   - Initializes `ProviderContainer` with `ProviderScope` override for database and repository dependencies.
   - Uses `testWidgets` with `tester.pump()` / `tester.pumpAndSettle()` to ensure reactive state transitions complete before assertions.
