import 'dart:io';

import 'package:flutter_test/flutter_test.dart';
import 'package:smarts_bio/core/database/local_database_service.dart';
import 'package:smarts_bio/core/architecture/app_architecture_hub.dart';
import 'mocks/database_mock.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();
  DatabaseMock.initializeEnvironment();

  group('Offline SQLite database service', () {
    late LocalDatabaseService dbService;
    late Directory temporaryDirectory;

    setUp(() async {
      temporaryDirectory = await Directory.systemTemp.createTemp('biothanaweya_test_');
      dbService = LocalDatabaseService(databasePath: '${temporaryDirectory.path}/test.db');
      await dbService.initialize();
    });

    tearDown(() async {
      await dbService.close();
      await temporaryDirectory.delete(recursive: true);
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
    late LocalDatabaseService database;
    late ArchitectureHubNotifier notifier;
    late Directory temporaryDirectory;

    setUp(() async {
      temporaryDirectory = await Directory.systemTemp.createTemp('biothanaweya_hub_test_');
      database = LocalDatabaseService(databasePath: '${temporaryDirectory.path}/hub.db');
      await database.initialize();
      notifier = ArchitectureHubNotifier(db: database);
    });

    tearDown(() async {
      await database.close();
      await temporaryDirectory.delete(recursive: true);
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
