import 'package:flutter_test/flutter_test.dart';
import 'package:smarts_bio/core/database/local_database_service.dart';
import 'package:smarts_bio/features/architecture_hub/presentation/screens/app_architecture_hub.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('Offline Local Database Service (SQLite Engine Tests)', () {
    late LocalDatabaseService dbService;

    setUp(() async {
      dbService = LocalDatabaseService();
      await dbService.initialize();
    });

    test('LocalDatabaseService hydrates all 11 Egyptian curriculum chapters', () async {
      final chapters = await dbService.getAllChapters();
      expect(chapters.length, equals(11), reason: 'All 11 official curriculum chapters must be seeded');
      expect(chapters.first['id'], equals(1));
      expect(chapters.last['id'], equals(11));
      expect(chapters.first['title_ar'], contains('الدعامة والحركة'));
    });

    test('Chapters can be fetched by primary key', () async {
      final ch5 = await dbService.getChapterById(5);
      expect(ch5, isNotNull);
      expect(ch5!['unit_number'], equals(2));
      expect(ch5['title_en'], contains('DNA Structure'));
    });

    test('Questions secondary index correctly retrieves chapter questions and ministry traps', () async {
      final ch1Questions = await dbService.getQuestionsForChapter(1);
      expect(ch1Questions.isNotEmpty, isTrue);
      expect(ch1Questions.every((q) => q['chapter_id'] == 1), isTrue);

      final traps = await dbService.getMinistryTraps();
      expect(traps.isNotEmpty, isTrue);
      expect(traps.every((t) => t['is_ministry_trap'] == 1), isTrue);
    });

    test('Full-text search queries question stems and explanations accurately', () async {
      final results = await dbService.searchCurriculumFTS('الميوسين');
      expect(results.isNotEmpty, isTrue);
      expect(results.any((q) => q['explanation'].toString().contains('الميوسين')), isTrue);
    });

    test('FSRS mistake tracking persists failures and resolves upon consecutive successes', () async {
      const qId = 'test_q_fsrs_99';
      await dbService.recordMistake(questionId: qId, chapterId: 1, wasCorrect: false);

      var due = await dbService.getDueMistakesForReview();
      expect(due.any((m) => m['question_id'] == qId), isTrue);

      // First correct answer
      await dbService.recordMistake(questionId: qId, chapterId: 1, wasCorrect: true);
      due = await dbService.getDueMistakesForReview();
      expect(due.any((m) => m['question_id'] == qId), isTrue);

      // Second consecutive correct answer -> marked resolved
      await dbService.recordMistake(questionId: qId, chapterId: 1, wasCorrect: true);
      due = await dbService.getDueMistakesForReview();
      expect(due.any((m) => m['question_id'] == qId), isFalse, reason: 'Consecutive successes resolve the mistake');
    });

    test('Live database benchmark calculates valid read/write throughput and cache hit rate', () async {
      final benchmark = await dbService.runBenchmark(queryIterations: 20);
      expect(benchmark.totalRecordsQueried > 0, isTrue);
      expect(benchmark.totalRecordsInserted, equals(20));
      expect(benchmark.averageReadLatencyMs >= 0.0, isTrue);
      expect(benchmark.averageWriteLatencyMs >= 0.0, isTrue);
      expect(benchmark.cacheHitRate, greaterThanOrEqualTo(95.0));
      expect(benchmark.databaseSizeKb, greaterThan(0));
    });
  });

  group('Architecture Hub StateNotifier & Telemetry Tests', () {
    late LocalDatabaseService dbService;
    late ArchitectureHubNotifier notifier;

    setUp(() async {
      dbService = LocalDatabaseService();
      await dbService.initialize();
      notifier = ArchitectureHubNotifier(dbService);
    });

    test('Initial state defaults to Arabic RTL and 100% offline mode', () {
      expect(notifier.state.isArabic, isTrue);
      expect(notifier.state.isOfflineForced, isTrue);
      expect(notifier.state.activeTab, equals(0));
      expect(notifier.state.ciStage, equals(CiPipelineStage.idle));
    });

    test('Language and offline toggles update state immutably', () {
      notifier.toggleLanguage();
      expect(notifier.state.isArabic, isFalse);

      notifier.toggleOfflineMode();
      expect(notifier.state.isOfflineForced, isFalse);

      notifier.setActiveTab(2);
      expect(notifier.state.activeTab, equals(2));
    });

    test('CI/CD pipeline simulation progresses through all 4 stages', () async {
      final future = notifier.triggerCiPipelineSimulation();
      expect(notifier.state.isCiRunning, isTrue);
      expect(notifier.state.ciStage, equals(CiPipelineStage.linting));

      await future;

      expect(notifier.state.isCiRunning, isFalse);
      expect(notifier.state.ciStage, equals(CiPipelineStage.completed));
      expect(notifier.state.ciLog, contains('Build Succeeded'));
    });
  });
}
