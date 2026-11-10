import 'dart:async';
import 'package:path/path.dart' as p;
import 'package:sqflite/sqflite.dart';

/// محرك قاعدة البيانات المحلي الصلب لتطبيق BioThanaweya المعتمد على SQLite
class BioSqliteDatabase {
  static final BioSqliteDatabase instance = BioSqliteDatabase._init();
  static Database? _database;

  BioSqliteDatabase._init();

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDB('biothanaweya_curriculum_v2.db');
    return _database!;
  }

  Future<Database> _initDB(String filePath) async {
    final dbPath = await getDatabasesPath();
    final path = p.join(dbPath, filePath);

    return await openDatabase(
      path,
      version: 1,
      onCreate: _createDB,
    );
  }

  Future<void> _createDB(Database db, int version) async {
    // 1. جدول فصول المنهج الدراسي
    await db.execute('''
      CREATE TABLE curriculum_chapters (
        id TEXT PRIMARY KEY,
        unit_number INTEGER NOT NULL,
        title_ar TEXT NOT NULL,
        title_en TEXT NOT NULL,
        topics_count INTEGER NOT NULL,
        mastery_percentage REAL NOT NULL DEFAULT 0.0,
        is_favorite INTEGER NOT NULL DEFAULT 0
      )
    ''');

    // 2. جدول كتب وكتيبات مفاهيم الوزارة الرسمية
    await db.execute('''
      CREATE TABLE ministry_textbooks (
        id TEXT PRIMARY KEY,
        title_ar TEXT NOT NULL,
        file_name TEXT NOT NULL,
        remote_url TEXT NOT NULL,
        local_path TEXT,
        file_size_bytes INTEGER NOT NULL,
        is_downloaded INTEGER NOT NULL DEFAULT 0,
        total_pages INTEGER NOT NULL DEFAULT 0,
        last_read_page INTEGER NOT NULL DEFAULT 1
      )
    ''');

    // 3. جدول بطاقات FSRS للمراجعة المتباعدة
    await db.execute('''
      CREATE TABLE fsrs_cards (
        card_id TEXT PRIMARY KEY,
        chapter_id TEXT NOT NULL,
        front_prompt TEXT NOT NULL,
        back_answer TEXT NOT NULL,
        stability REAL NOT NULL DEFAULT 1.0,
        difficulty REAL NOT NULL DEFAULT 5.0,
        reps INTEGER NOT NULL DEFAULT 0,
        lapses INTEGER NOT NULL DEFAULT 0,
        state INTEGER NOT NULL DEFAULT 0,
        due_date_iso TEXT NOT NULL
      )
    ''');

    // 4. جدول محاولات وسجلات الامتحانات
    await db.execute('''
      CREATE TABLE quiz_attempts (
        attempt_id TEXT PRIMARY KEY,
        exam_title TEXT NOT NULL,
        score INTEGER NOT NULL,
        total_questions INTEGER NOT NULL,
        percentage REAL NOT NULL,
        duration_seconds INTEGER NOT NULL,
        timestamp_iso TEXT NOT NULL
      )
    ''');

    // إدراج البيانات الأولية لفصول المنهج
    await _seedInitialChapters(db);
    // إدراج كتب وكتيبات مفاهيم الوزارة
    await _seedInitialTextbooks(db);
  }

  Future<void> _seedInitialChapters(Database db) async {
    final chapters = [
      {
        'id': 'chap_1',
        'unit_number': 1,
        'title_ar': 'الفصل الأول: الدعامة والحركة في الكائنات الحية',
        'title_en': 'Support and Movement in Living Organisms',
        'topics_count': 4,
        'mastery_percentage': 85.0,
        'is_favorite': 1,
      },
      {
        'id': 'chap_2',
        'unit_number': 1,
        'title_ar': 'الفصل الثاني: التنسيق الهرموني في الكائنات الحية',
        'title_en': 'Hormonal Coordination',
        'topics_count': 3,
        'mastery_percentage': 72.0,
        'is_favorite': 0,
      },
      {
        'id': 'chap_3',
        'unit_number': 1,
        'title_ar': 'الفصل الثالث: التكاثر في الكائنات الحية',
        'title_en': 'Reproduction in Living Organisms',
        'topics_count': 5,
        'mastery_percentage': 90.0,
        'is_favorite': 1,
      },
      {
        'id': 'chap_4',
        'unit_number': 1,
        'title_ar': 'الفصل الرابع: المناعة في الكائنات الحية',
        'title_en': 'Immunity in Living Organisms',
        'topics_count': 4,
        'mastery_percentage': 68.0,
        'is_favorite': 0,
      },
      {
        'id': 'chap_5',
        'unit_number': 2,
        'title_ar': 'الفصل الخامس: الحمض النووي DNA وتضاعفه',
        'title_en': 'DNA Structure and Replication',
        'topics_count': 4,
        'mastery_percentage': 80.0,
        'is_favorite': 1,
      },
      {
        'id': 'chap_6',
        'unit_number': 2,
        'title_ar': 'الفصل السادس: الأحماض النووية وتخليق البروتين',
        'title_en': 'RNA and Protein Synthesis',
        'topics_count': 3,
        'mastery_percentage': 75.0,
        'is_favorite': 0,
      },
    ];

    for (final chap in chapters) {
      await db.insert('curriculum_chapters', chap, conflictAlgorithm: ConflictAlgorithm.replace);
    }
  }

  Future<void> _seedInitialTextbooks(Database db) async {
    final textbooks = [
      {
        'id': 'book_ministry_bio_ar',
        'title_ar': 'كتاب الأحياء الوزاري الرسمي للصف الثالث الثانوي (طبعة 2025/2026)',
        'file_name': 'biology_thanaweya_official_ar.pdf',
        'remote_url': 'https://raw.githubusercontent.com/AlySelim2001/BioThanaweya/main/assets/books/biology_thanaweya_official_ar.pdf',
        'local_path': null,
        'file_size_bytes': 28400000,
        'is_downloaded': 0,
        'total_pages': 196,
        'last_read_page': 1,
      },
      {
        'id': 'book_concepts_sheet_ar',
        'title_ar': 'مجلد مفاهيم الأحياء للثانوية العامة (معتمد من وزارة التربية والتعليم)',
        'file_name': 'biology_concepts_handbook_ar.pdf',
        'remote_url': 'https://raw.githubusercontent.com/AlySelim2001/BioThanaweya/main/assets/books/biology_concepts_handbook_ar.pdf',
        'local_path': null,
        'file_size_bytes': 6500000,
        'is_downloaded': 0,
        'total_pages': 32,
        'last_read_page': 1,
      },
      {
        'id': 'book_diagrams_atlas_ar',
        'title_ar': 'أطلس الرسومات الحيوية والقطاعات التشريحية المقررة',
        'file_name': 'biology_diagrams_atlas_ar.pdf',
        'remote_url': 'https://raw.githubusercontent.com/AlySelim2001/BioThanaweya/main/assets/books/biology_diagrams_atlas_ar.pdf',
        'local_path': null,
        'file_size_bytes': 14200000,
        'is_downloaded': 0,
        'total_pages': 64,
        'last_read_page': 1,
      },
    ];

    for (final tb in textbooks) {
      await db.insert('ministry_textbooks', tb, conflictAlgorithm: ConflictAlgorithm.replace);
    }
  }

  Future<List<Map<String, dynamic>>> getChapters() async {
    final db = await database;
    return await db.query('curriculum_chapters', orderBy: 'unit_number, id ASC');
  }

  Future<List<Map<String, dynamic>>> getTextbooks() async {
    final db = await database;
    return await db.query('ministry_textbooks', orderBy: 'id ASC');
  }

  Future<void> updateTextbookDownloadStatus(String id, String localPath, int isDownloaded) async {
    final db = await database;
    await db.update(
      'ministry_textbooks',
      {'local_path': localPath, 'is_downloaded': isDownloaded},
      where: 'id = ?',
      whereArgs: [id],
    );
  }

  Future<void> updateReadingProgress(String id, int page) async {
    final db = await database;
    await db.update(
      'ministry_textbooks',
      {'last_read_page': page},
      where: 'id = ?',
      whereArgs: [id],
    );
  }

  Future<void> recordQuizAttempt({
    required String examTitle,
    required int score,
    required int totalQuestions,
    required double percentage,
    required int durationSeconds,
  }) async {
    final db = await database;
    await db.insert('quiz_attempts', {
      'attempt_id': 'att_${DateTime.now().millisecondsSinceEpoch}',
      'exam_title': examTitle,
      'score': score,
      'total_questions': totalQuestions,
      'percentage': percentage,
      'duration_seconds': durationSeconds,
      'timestamp_iso': DateTime.now().toIso8601String(),
    });
  }

  Future<List<Map<String, dynamic>>> getRecentAttempts({int limit = 10}) async {
    final db = await database;
    return await db.query('quiz_attempts', orderBy: 'timestamp_iso DESC', limit: limit);
  }

  Future<void> close() async {
    final db = _database;
    if (db != null) {
      await db.close();
      _database = null;
    }
  }
}
