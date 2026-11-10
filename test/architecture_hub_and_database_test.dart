import 'package:flutter_test/flutter_test.dart';
import 'package:sqflite_common_ffi/sqflite_ffi.dart';
import 'package:smarts_bio/core/database/local_database_service.dart';
import 'package:smarts_bio/features/architecture_hub/data/datasources/architecture_local_data_source.dart';
import 'package:smarts_bio/features/architecture_hub/data/repositories/architecture_hub_repository_impl.dart';
import 'package:smarts_bio/features/architecture_hub/presentation/screens/app_architecture_hub.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();
  sqfliteFfiInit();
  databaseFactory = databaseFactoryFfi;

  group('Local SQLite database service contracts', () {
    late LocalDatabaseService database;

    setUp(() async {
      database = LocalDatabaseService();
      await database.initialize();
    });

    tearDown(() => database.close());

    test('provides all 11 chapters and textbooks from the local database', () async {
      expect(await database.getAllChapters(), hasLength(11));
      expect(await database.getAllTextbooks(), hasLength(3));
    });

    test('chapter lookup uses the primary key', () async {
      final chapter = await database.getChapterById(5);
      expect(chapter?['unit_number'], 2);
      expect(chapter?['title_en'], contains('DNA Structure'));
    });

    test('questions, ministry traps, and local curriculum search are available', () async {
      expect(await database.getQuestionsForChapter(1), isNotEmpty);
      final traps = await database.getMinistryTraps();
      expect(traps, isNotEmpty);
      expect(traps.every((row) => row['is_ministry_trap'] == 1), isTrue);
      final results = await database.searchCurriculumFTS('الميوسين');
      expect(results, isNotEmpty);
      expect(results.any((row) => row['explanation'].toString().contains('الميوسين')), isTrue);
    });

    test('mistake remains due until resolved by two correct answers', () async {
      const id = 'test_q_fsrs_contract';
      await database.recordMistake(questionId: id, chapterId: 1, wasCorrect: false);
      expect((await database.getDueMistakesForReview()).any((row) => row['question_id'] == id), isTrue);
      await database.recordMistake(questionId: id, chapterId: 1, wasCorrect: true);
      expect((await database.getDueMistakesForReview()).any((row) => row['question_id'] == id), isTrue);
      await database.recordMistake(questionId: id, chapterId: 1, wasCorrect: true);
      expect((await database.getDueMistakesForReview()).any((row) => row['question_id'] == id), isFalse);
    });

    test('runBenchmark returns typed local storage metrics', () async {
      final result = await database.runBenchmark(queryIterations: 2);
      expect(result.totalRecordsQueried, 22);
      expect(result.totalRecordsInserted, 2);
      expect(result.databaseSizeKb, greaterThan(0));
    });
  });

  test('architecture hub repository adapter reports metrics for the notifier', () async {
    final database = LocalDatabaseService();
    await database.initialize();
    final notifier = ArchitectureHubNotifier.fromRepository(
      ArchitectureHubRepositoryImpl(SqliteArchitectureDataSource(database)),
    );
    await notifier.loadMetrics();
    expect(notifier.state.loadStatus, ArchitectureLoadStatus.success);
    expect(notifier.state.metrics?.chapterCount, 11);
    expect(notifier.state.metrics?.textbookCount, 3);
    notifier.dispose();
    await database.close();
  });
}
