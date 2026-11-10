import 'package:flutter_test/flutter_test.dart';
import 'package:smarts_bio/core/database/local_database_service.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('Pure Dart SQL-Compatible Local Database Engine Tests', () {
    setUp(() {
      SharedPreferences.setMockInitialValues({});
    });

    test('LocalDatabaseService initializes schemas and seeds default chapters', () async {
      final db = LocalDatabaseService.instance;
      await db.reseedDatabase();

      expect(db.isInitialized, isTrue);
      expect(db.totalIndexedChapters, equals(6));
      expect(db.totalIndexedTextbooks, equals(3));

      // Query all chapters
      final chapters = await db.queryChapters();
      expect(chapters.length, equals(6));

      // Query by Unit 1 filter
      final unit1Chapters = await db.queryChapters(unitCode: 'UNIT_1');
      expect(unit1Chapters.length, equals(4));
      expect(unit1Chapters.any((c) => c.id == 'chap_support_movement'), isTrue);

      // Query by Unit 2 filter
      final unit2Chapters = await db.queryChapters(unitCode: 'UNIT_2');
      expect(unit2Chapters.length, equals(2));
      expect(unit2Chapters.any((c) => c.id == 'chap_dna'), isTrue);
    });

    test('LocalDatabaseService queries official textbooks', () async {
      final db = LocalDatabaseService.instance;
      await db.initializeDatabase();

      final textbooks = await db.queryTextbooks();
      expect(textbooks.length, equals(3));
      expect(textbooks.any((t) => t.title.contains('Languages')), isTrue);
      expect(textbooks.any((t) => t.title.contains('المفاهيم')), isTrue);
    });

    test('LocalDatabaseService records and queries student exam attempts', () async {
      final db = LocalDatabaseService.instance;
      await db.initializeDatabase();

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
