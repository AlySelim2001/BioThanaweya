import 'package:flutter_test/flutter_test.dart';
import 'package:sqflite_common_ffi/sqflite_ffi.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smarts_bio/core/database/local_database_service.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();
  sqfliteFfiInit();
  databaseFactory = databaseFactoryFfi;

  setUpAll(() {
    TestWidgetsFlutterBinding.ensureInitialized();
    sqfliteFfiInit();
    databaseFactory = databaseFactoryFfi;
  });

  group('Pure Dart SQL-Compatible Local Database Engine Tests', () {
    late LocalDatabaseService db;

    setUp(() async {
      SharedPreferences.setMockInitialValues({});
      db = LocalDatabaseService(databasePath: inMemoryDatabasePath);
      await db.initialize();
      await db.reseedDatabase();
      LocalDatabaseService.setMockInstance(db);
    });

    tearDown(() async {
      await db.close();
      LocalDatabaseService.resetInstance();
    });

    test('LocalDatabaseService initializes schemas and seeds default chapters', () async {
      expect(db.isInitialized, isTrue);
      expect(db.totalIndexedChapters, equals(11));
      expect(db.totalIndexedTextbooks, equals(3));

      // Query all chapters
      final chapters = await db.queryChapters();
      expect(chapters.length, equals(11));

      // Query by Unit 1 filter
      final unit1Chapters = await db.queryChapters(unitCode: 'UNIT_1');
      expect(unit1Chapters.length, equals(8));
      expect(unit1Chapters.any((c) => c.id == 'ch_01'), isTrue);

      // Query by Unit 2 filter
      final unit2Chapters = await db.queryChapters(unitCode: 'UNIT_2');
      expect(unit2Chapters.length, equals(3));
      expect(unit2Chapters.any((c) => c.id == 'ch_05'), isTrue);
    });

    test('LocalDatabaseService queries official textbooks', () async {
      final textbooks = await db.queryTextbooks();
      expect(textbooks.length, equals(3));
      expect(textbooks.any((t) => t.title.contains('Languages')), isTrue);
      expect(textbooks.any((t) => t.title.contains('مفاهيم')), isTrue);
    });

    test('LocalDatabaseService records and queries student exam attempts', () async {
      await db.recordExamAttempt(
        paperId: 'exam_ministry_2024_s1',
        score: 56.0,
        maxScore: 60.0,
        timeSpentSeconds: 2400,
      );

      final attempts = await db.queryExamAttempts();
      expect(attempts.isNotEmpty, isTrue);
      expect(attempts.first['score'], equals(56.0));
      expect(attempts.first['percentage'], closeTo(93.33, 0.1));
    });
  });
}

