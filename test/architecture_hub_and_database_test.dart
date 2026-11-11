import 'package:flutter_test/flutter_test.dart';
import 'package:sqflite_common_ffi/sqflite_ffi.dart';
import 'package:smarts_bio/core/database/local_database_service.dart';
import 'package:smarts_bio/core/architecture/app_architecture_hub.dart';
import 'mocks/database_mock.dart';

/// Concrete mock of LocalDatabaseService designed for widget and state notifier unit tests.
/// Eliminates headless SQLite binding issues and ensures deterministic metric calculation.
class MockLocalDatabaseService extends LocalDatabaseService {
  MockLocalDatabaseService() : super(databasePath: inMemoryDatabasePath);

  @override
  bool get isInitialized => true;

  @override
  int get totalIndexedChapters => 11;

  @override
  int get totalIndexedTextbooks => 3;

  @override
  Future<void> initializeDatabase() async {}

  @override
  Future<void> reseedDatabase() async {}

  @override
  Future<List<Map<String, Object?>>> getAllChapters() async {
    return List.generate(11, (i) => {
      'id': i + 1,
      'unit_number': i < 8 ? 1 : 2,
      'chapter_number': i + 1,
      'title_ar': 'الدعامة والحركة في الكائنات الحية ${i + 1}',
      'title_en': 'Chapter ${i + 1}',
    });
  }

  @override
  Future<List<DbTextbookRecord>> queryTextbooks() async {
    return const [
      DbTextbookRecord(
        id: 'tb_01',
        title: 'كتاب الأحياء - عربي',
        edition: '2024-2025',
        fileSize: '15 MB',
        downloadUrl: 'https://moe.gov.eg/tb1.pdf',
        isOfflineCached: true,
      ),
      DbTextbookRecord(
        id: 'tb_02',
        title: 'Biology Textbook - Languages',
        edition: '2024-2025',
        fileSize: '18 MB',
        downloadUrl: 'https://moe.gov.eg/tb2.pdf',
        isOfflineCached: true,
      ),
      DbTextbookRecord(
        id: 'tb_03',
        title: 'كتيب مفاهيم الأحياء',
        edition: '2024-2025',
        fileSize: '8 MB',
        downloadUrl: 'https://moe.gov.eg/tb3.pdf',
        isOfflineCached: true,
      ),
    ];
  }

  @override
  Future<List<Map<String, Object?>>> queryExamAttempts() async {
    return [
      {
        'id': 'attempt_1',
        'paper_id': 'exam_2024',
        'score': 58.0,
        'max_score': 60.0,
        'percentage': 96.67,
        'time_spent_seconds': 3200,
        'created_at': DateTime.now().toIso8601String(),
      }
    ];
  }

  @override
  Future<DatabaseBenchmarkResult> runBenchmark({int queryIterations = 40}) async {
    return DatabaseBenchmarkResult(
      totalRecordsQueried: queryIterations * 11,
      totalRecordsInserted: queryIterations,
      averageReadLatencyMs: 0.15,
      averageWriteLatencyMs: 0.45,
      cacheHitRate: 100,
      databaseSizeKb: 120,
    );
  }

  @override
  Future<void> close() async {}
}

void main() {
  setUpAll(() {
    TestWidgetsFlutterBinding.ensureInitialized();
    sqfliteFfiInit();
    databaseFactory = databaseFactoryFfi;
  });

  group('Offline SQLite database service', () {
    late LocalDatabaseService dbService;

    setUp(() async {
      dbService = await DatabaseMock.createInitialized(path: inMemoryDatabasePath);
    });

    tearDown(() async {
      await dbService.close();
    });

    test('hydrates all 11 curriculum chapters from local SQLite', () async {
      final chapters = await dbService.getAllChapters();
      expect(chapters.length, 11);
      expect(chapters.first['id'], 1);
      expect(chapters.last['id'], 11);
      expect(chapters.first['title_ar'], contains('الدعامة والحركة'));
    });

    test('chapter lookup uses its primary key and provides biology curriculum content', () async {
      final chapter = await dbService.getChapterById(5);
      expect(chapter, isNotNull);
      expect(chapter!['unit_number'], 2);
      expect(chapter['title_en'], contains('DNA Structure'));
    });

    test('chapter and ministry-trap question queries are locally indexed', () async {
      final questions = await dbService.getQuestionsForChapter(1);
      expect(questions, isNotEmpty);
      expect(questions.every((question) => question['chapter_id'] == 1), isTrue);
      final traps = await dbService.getMinistryTraps();
      expect(traps, isNotEmpty);
      expect(traps.every((question) => question['is_ministry_trap'] == 1), isTrue);
    });

    test('local curriculum text search finds Arabic terms in question content', () async {
      final results = await dbService.searchCurriculumFTS('الميوسين');
      expect(results, isNotEmpty);
      expect(results.any((question) => question['explanation'].toString().contains('الميوسين')), isTrue);
    });

    test('mistake review keeps a question due until two correct answers', () async {
      const questionId = 'test_q_fsrs_99';
      await dbService.recordMistake(questionId: questionId, chapterId: 1, wasCorrect: false);
      var due = await dbService.getDueMistakesForReview();
      expect(due.any((mistake) => mistake['question_id'] == questionId), isTrue);
      await dbService.recordMistake(questionId: questionId, chapterId: 1, wasCorrect: true);
      due = await dbService.getDueMistakesForReview();
      expect(due.any((mistake) => mistake['question_id'] == questionId), isTrue);
      await dbService.recordMistake(questionId: questionId, chapterId: 1, wasCorrect: true);
      due = await dbService.getDueMistakesForReview();
      expect(due.any((mistake) => mistake['question_id'] == questionId), isFalse);
    });

    test('benchmark reports positive local database and query metrics', () async {
      final benchmark = await dbService.runBenchmark(queryIterations: 20);
      expect(benchmark.totalRecordsQueried, greaterThan(0));
      expect(benchmark.totalRecordsInserted, 20);
      expect(benchmark.averageReadLatencyMs, greaterThanOrEqualTo(0));
      expect(benchmark.averageWriteLatencyMs, greaterThanOrEqualTo(0));
      expect(benchmark.cacheHitRate, greaterThanOrEqualTo(95));
      expect(benchmark.databaseSizeKb, greaterThan(0));
    });
  });

  group('Architecture hub state and telemetry', () {
    late MockLocalDatabaseService mockDb;
    late ArchitectureHubNotifier notifier;

    setUp(() {
      mockDb = MockLocalDatabaseService();
      notifier = ArchitectureHubNotifier(db: mockDb);
    });

    tearDown(() async {
      notifier.dispose();
      await mockDb.close();
    });

    test('defaults to Arabic RTL and offline readiness', () {
      expect(notifier.state.isArabic, isTrue);
      expect(notifier.state.isOfflineForced, isTrue);
      expect(notifier.state.activeTab, 0);
      expect(notifier.state.ciStage, CiPipelineStage.idle);
      expect(notifier.state.loadStatus, ArchitectureLoadStatus.initial);
    });

    test('language, offline, telemetry, theme and tab actions update state', () {
      notifier.toggleLanguage();
      notifier.toggleOfflineMode();
      notifier.toggleTelemetry();
      notifier.toggleThemePreview();
      notifier.setActiveTab(2);
      expect(notifier.state.isArabic, isFalse);
      expect(notifier.state.isOfflineForced, isFalse);
      expect(notifier.state.isTelemetryEnabled, isTrue);
      expect(notifier.state.isLightPreview, isTrue);
      expect(notifier.state.activeTab, 2);
    });

    test('loadMetrics moves through loading to success and loads local metrics', () async {
      final future = notifier.loadMetrics();
      expect(notifier.state.loadStatus, ArchitectureLoadStatus.loading);
      await future;
      expect(notifier.state.loadStatus, ArchitectureLoadStatus.success);
      expect(notifier.state.metrics?.chapterCount, 11);
      expect(notifier.state.metrics?.textbookCount, 3);
      expect(notifier.state.metrics?.offlineReady, isTrue);
      expect(notifier.state.metrics?.fsrsReady, isTrue);
    });

    test('CI simulation completes and describes its simulated nature', () async {
      final future = notifier.triggerCiPipelineSimulation();
      expect(notifier.state.isCiRunning, isTrue);
      expect(notifier.state.ciStage, CiPipelineStage.linting);
      await future;
      expect(notifier.state.isCiRunning, isFalse);
      expect(notifier.state.ciStage, CiPipelineStage.completed);
      expect(notifier.state.ciLog, contains('Simulation complete'));
    });
  });
}

