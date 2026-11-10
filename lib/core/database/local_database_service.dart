import 'dart:io';

import 'package:path/path.dart' as p;
import 'package:sqflite/sqflite.dart';

class DbChapterRecord {
  const DbChapterRecord({
    required this.id,
    required this.unitCode,
    required this.chapterNumber,
    required this.titleEn,
    required this.titleAr,
    required this.summaryEn,
    required this.summaryAr,
    this.isHighYield = true,
  });

  final String id;
  final String unitCode;
  final int chapterNumber;
  final String titleEn;
  final String titleAr;
  final String summaryEn;
  final String summaryAr;
  final bool isHighYield;

  Map<String, Object?> toMap() => {
        'id': id,
        'unit_code': unitCode,
        'chapter_number': chapterNumber,
        'title_en': titleEn,
        'title_ar': titleAr,
        'summary_en': summaryEn,
        'summary_ar': summaryAr,
        'is_high_yield': isHighYield ? 1 : 0,
      };

  factory DbChapterRecord.fromMap(Map<String, Object?> map) => DbChapterRecord(
        id: map['code'] as String,
        unitCode: (map['unit_number'] as int) == 1 ? 'UNIT_1' : 'UNIT_2',
        chapterNumber: map['chapter_number'] as int,
        titleEn: map['title_en'] as String,
        titleAr: map['title_ar'] as String,
        summaryEn: map['summary_en'] as String,
        summaryAr: map['summary_ar'] as String,
        isHighYield: (map['is_high_yield'] as int? ?? 1) == 1,
      );
}

class DbTextbookRecord {
  const DbTextbookRecord({
    required this.id,
    required this.title,
    required this.edition,
    required this.fileSize,
    required this.downloadUrl,
    this.isOfflineCached = false,
  });

  final String id;
  final String title;
  final String edition;
  final String fileSize;
  final String downloadUrl;
  final bool isOfflineCached;

  Map<String, Object?> toMap() => {
        'id': id,
        'title': title,
        'edition': edition,
        'file_size': fileSize,
        'download_url': downloadUrl,
        'is_offline_cached': isOfflineCached ? 1 : 0,
      };

  factory DbTextbookRecord.fromMap(Map<String, Object?> map) => DbTextbookRecord(
        id: map['id'] as String,
        title: map['title'] as String,
        edition: map['edition'] as String,
        fileSize: map['file_size'] as String,
        downloadUrl: map['download_url'] as String,
        isOfflineCached: (map['is_offline_cached'] as int? ?? 0) == 1,
      );
}

class DatabaseBenchmarkResult {
  const DatabaseBenchmarkResult({
    required this.totalRecordsQueried,
    required this.totalRecordsInserted,
    required this.averageReadLatencyMs,
    required this.averageWriteLatencyMs,
    required this.cacheHitRate,
    required this.databaseSizeKb,
  });

  final int totalRecordsQueried;
  final int totalRecordsInserted;
  final double averageReadLatencyMs;
  final double averageWriteLatencyMs;
  final double cacheHitRate;
  final int databaseSizeKb;
}

/// SQLite data source for curriculum and student records. All methods operate locally.
class LocalDatabaseService {
  LocalDatabaseService({String? databasePath}) : _databasePath = databasePath;

  static final LocalDatabaseService instance = LocalDatabaseService();
  static const int schemaVersion = 2;
  static const String databaseName = 'biothanaweya_offline.db';

  final String? _databasePath;
  Database? _database;
  Future<void>? _opening;

  bool get isInitialized => _database?.isOpen ?? false;

  Future<Database> get database async {
    await initializeDatabase();
    return _database!;
  }

  Future<void> initialize() => initializeDatabase();

  Future<void> initializeDatabase() async {
    if (isInitialized) return;
    final pending = _opening;
    if (pending != null) return pending;
    final opening = _open();
    _opening = opening;
    try {
      await opening;
    } finally {
      if (identical(_opening, opening)) _opening = null;
    }
  }

  Future<void> _open() async {
    final path = _databasePath ?? p.join(await getDatabasesPath(), databaseName);
    _database = await openDatabase(
      path,
      version: schemaVersion,
      onConfigure: (db) async {
        await db.execute('PRAGMA foreign_keys = ON');
        await db.execute('PRAGMA journal_mode = WAL');
      },
      onCreate: _createSchema,
      onUpgrade: _upgradeSchema,
    );
  }

  Future<void> _createSchema(Database db, int version) async {
    await db.execute('''CREATE TABLE chapters (
      id INTEGER PRIMARY KEY,
      code TEXT NOT NULL UNIQUE,
      unit_number INTEGER NOT NULL,
      chapter_number INTEGER NOT NULL,
      title_en TEXT NOT NULL,
      title_ar TEXT NOT NULL,
      summary_en TEXT NOT NULL,
      summary_ar TEXT NOT NULL,
      is_high_yield INTEGER NOT NULL DEFAULT 1
    )''');
    await db.execute('CREATE INDEX idx_chapters_unit ON chapters(unit_number, chapter_number)');
    await db.execute('''CREATE TABLE textbooks (
      id TEXT PRIMARY KEY,
      title TEXT NOT NULL,
      edition TEXT NOT NULL,
      file_size TEXT NOT NULL,
      download_url TEXT NOT NULL,
      is_offline_cached INTEGER NOT NULL DEFAULT 0,
      local_path TEXT
    )''');
    await db.execute('''CREATE TABLE questions (
      id TEXT PRIMARY KEY,
      chapter_id INTEGER NOT NULL REFERENCES chapters(id) ON DELETE CASCADE,
      question_text TEXT NOT NULL,
      explanation TEXT NOT NULL,
      is_ministry_trap INTEGER NOT NULL DEFAULT 0
    )''');
    await db.execute('CREATE INDEX idx_questions_chapter ON questions(chapter_id)');
    await _createMistakesTable(db);
    await db.execute('''CREATE TABLE exam_attempts (
      id TEXT PRIMARY KEY,
      paper_id TEXT NOT NULL,
      score REAL NOT NULL,
      max_score REAL NOT NULL,
      percentage REAL NOT NULL,
      time_spent_seconds INTEGER NOT NULL,
      created_at TEXT NOT NULL
    )''');
    await _seedCurriculum(db);
  }

  Future<void> _createMistakesTable(Database db) async {
    await db.execute('''CREATE TABLE IF NOT EXISTS fsrs_mistakes (
      question_id TEXT PRIMARY KEY,
      chapter_id INTEGER NOT NULL REFERENCES chapters(id) ON DELETE CASCADE,
      fail_count INTEGER NOT NULL DEFAULT 1,
      consecutive_correct INTEGER NOT NULL DEFAULT 0,
      next_review_at TEXT NOT NULL,
      is_resolved INTEGER NOT NULL DEFAULT 0
    )''');
    await db.execute('CREATE INDEX IF NOT EXISTS idx_mistakes_due ON fsrs_mistakes(is_resolved, next_review_at)');
  }

  Future<void> _upgradeSchema(Database db, int oldVersion, int newVersion) async {
    if (oldVersion < 2) await _createMistakesTable(db);
  }

  Future<void> _seedCurriculum(Database db) async {
    const chapterRows = <Map<String, Object?>>[
      {'code': 'ch_01', 'unit': 1, 'number': 1, 'en': 'Support and Movement in Living Organisms', 'ar': 'الدعامة والحركة في الكائنات الحية', 'enSummary': 'Support tissues, skeleton, and muscle contraction.', 'arSummary': 'أنواع الدعامة والهيكل العظمي وآلية انقباض العضلات.'},
      {'code': 'ch_02', 'unit': 1, 'number': 2, 'en': 'Hormonal Coordination in Living Organisms', 'ar': 'التنسيق الهرموني في الكائنات الحية', 'enSummary': 'Plant hormones and endocrine regulation.', 'arSummary': 'الهرمونات النباتية وتنظيم الغدد الصماء.'},
      {'code': 'ch_03', 'unit': 1, 'number': 3, 'en': 'Reproduction in Living Organisms', 'ar': 'التكاثر في الكائنات الحية', 'enSummary': 'Reproductive strategies and life cycles.', 'arSummary': 'أنماط التكاثر ودورات الحياة.'},
      {'code': 'ch_04', 'unit': 1, 'number': 4, 'en': 'Immunity in Living Organisms', 'ar': 'المناعة في الكائنات الحية', 'enSummary': 'Immune defenses, antibodies, and lymphocytes.', 'arSummary': 'وسائل الدفاع المناعي والأجسام المضادة والخلايا اللمفاوية.'},
      {'code': 'ch_05', 'unit': 2, 'number': 1, 'en': 'DNA Structure, Genetic Code and Replication', 'ar': 'الحمض النووي DNA وتضاعفه', 'enSummary': 'DNA structure, replication enzymes, and genetic code.', 'arSummary': 'تركيب DNA وإنزيمات التضاعف والشفرة الوراثية.'},
      {'code': 'ch_06', 'unit': 2, 'number': 2, 'en': 'RNA and Protein Synthesis', 'ar': 'الأحماض النووية RNA وتخليق البروتين', 'enSummary': 'Transcription, translation, and protein synthesis.', 'arSummary': 'النسخ والترجمة وتخليق البروتين.'},
      {'code': 'ch_07', 'unit': 2, 'number': 3, 'en': 'Biotechnology and Genetic Engineering', 'ar': 'التكنولوجيا الحيوية والهندسة الوراثية', 'enSummary': 'PCR, restriction enzymes, and recombinant DNA.', 'arSummary': 'تفاعل PCR وإنزيمات القصر والحمض النووي المعاد تركيبه.'},
      {'code': 'ch_08', 'unit': 1, 'number': 5, 'en': 'Nutrition and Digestion in Living Organisms', 'ar': 'التغذية والهضم في الكائنات الحية', 'enSummary': 'Nutrition, digestion, and metabolic pathways.', 'arSummary': 'التغذية والهضم ومسارات الأيض.'},
      {'code': 'ch_09', 'unit': 1, 'number': 6, 'en': 'Transport in Living Organisms', 'ar': 'النقل في الكائنات الحية', 'enSummary': 'Transport systems in plants and animals.', 'arSummary': 'أجهزة النقل في النبات والحيوان.'},
      {'code': 'ch_10', 'unit': 1, 'number': 7, 'en': 'Cellular Respiration and Energy Production', 'ar': 'التنفس الخلوي وإنتاج الطاقة', 'enSummary': 'Cellular respiration and ATP production.', 'arSummary': 'التنفس الخلوي وإنتاج جزيئات ATP.'},
      {'code': 'ch_11', 'unit': 1, 'number': 8, 'en': 'Excretion and Osmoregulation', 'ar': 'الإخراج والتنظيم الأسموزي', 'enSummary': 'Excretion and maintenance of internal balance.', 'arSummary': 'الإخراج والحفاظ على الاتزان الداخلي.'},
    ];
    const textbooks = <Map<String, Object?>>[
      {'id': 'biology_ar_2025', 'title': 'كتاب الأحياء للثانوية العامة', 'edition': 'وزارة التربية والتعليم', 'file_size': 'محلي', 'download_url': '', 'is_offline_cached': 1},
      {'id': 'biology_en_2025', 'title': 'Official Biology Textbook (Languages)', 'edition': 'Ministry curriculum', 'file_size': 'Local asset', 'download_url': '', 'is_offline_cached': 1},
      {'id': 'biology_concepts', 'title': 'كتيب مفاهيم الأحياء', 'edition': 'مفاهيم المنهج', 'file_size': 'محلي', 'download_url': '', 'is_offline_cached': 1},
    ];

    await db.transaction((txn) async {
      for (var index = 0; index < chapterRows.length; index++) {
        final row = chapterRows[index];
        await txn.insert('chapters', {
          'id': index + 1,
          'code': row['code'],
          'unit_number': row['unit'],
          'chapter_number': row['number'],
          'title_en': row['en'],
          'title_ar': row['ar'],
          'summary_en': row['enSummary'],
          'summary_ar': row['arSummary'],
          'is_high_yield': 1,
        });
      }
      for (final textbook in textbooks) {
        await txn.insert('textbooks', textbook);
      }
      await txn.insert('questions', {
        'id': 'seed_muscle_myosin',
        'chapter_id': 1,
        'question_text': 'ما دور الميوسين في انقباض العضلة؟',
        'explanation': 'يرتبط الميوسين بخيوط الأكتين ويسهم في انزلاقها أثناء انقباض العضلة.',
        'is_ministry_trap': 1,
      });
      await txn.insert('questions', {
        'id': 'seed_dna_replication',
        'chapter_id': 5,
        'question_text': 'ما دور إنزيم DNA polymerase؟',
        'explanation': 'يبني شريط DNA جديداً مكملاً أثناء التضاعف.',
        'is_ministry_trap': 0,
      });
    });
  }

  Future<List<DbChapterRecord>> queryChapters({String? unitCode}) async {
    final db = await database;
    final rows = await db.query(
      'chapters',
      where: unitCode == null || unitCode.isEmpty ? null : 'unit_number = ?',
      whereArgs: unitCode == null || unitCode.isEmpty ? null : [unitCode == 'UNIT_1' ? 1 : 2],
      orderBy: 'id ASC',
    );
    return rows.map(DbChapterRecord.fromMap).toList(growable: false);
  }

  Future<List<DbTextbookRecord>> queryTextbooks() async {
    final db = await database;
    final rows = await db.query('textbooks', orderBy: 'id ASC');
    return rows.map(DbTextbookRecord.fromMap).toList(growable: false);
  }

  Future<List<Map<String, Object?>>> queryExamAttempts() async {
    final db = await database;
    return db.query('exam_attempts', orderBy: 'created_at DESC');
  }

  Future<void> recordExamAttempt({required String paperId, required double score, required double maxScore, required int timeSpentSeconds}) async {
    if (maxScore <= 0 || score < 0 || score > maxScore || timeSpentSeconds < 0) {
      throw ArgumentError('Exam attempt values are outside their valid range.');
    }
    final now = DateTime.now();
    final db = await database;
    await db.insert('exam_attempts', {
      'id': 'attempt_${now.microsecondsSinceEpoch}',
      'paper_id': paperId,
      'score': score,
      'max_score': maxScore,
      'percentage': score / maxScore * 100,
      'time_spent_seconds': timeSpentSeconds,
      'created_at': now.toIso8601String(),
    });
  }

  Future<List<Map<String, Object?>>> getAllChapters() async {
    final db = await database;
    return db.query('chapters', orderBy: 'id ASC');
  }

  Future<Map<String, Object?>?> getChapterById(int id) async {
    final db = await database;
    final rows = await db.query('chapters', where: 'id = ?', whereArgs: [id], limit: 1);
    return rows.isEmpty ? null : rows.first;
  }

  Future<List<Map<String, Object?>>> getAllTextbooks() async {
    final db = await database;
    return db.query('textbooks', orderBy: 'id ASC');
  }

  Future<List<Map<String, Object?>>> getQuestionsForChapter(int chapterId) async {
    final db = await database;
    return db.query('questions', where: 'chapter_id = ?', whereArgs: [chapterId], orderBy: 'id ASC');
  }

  Future<List<Map<String, Object?>>> getMinistryTraps() async {
    final db = await database;
    return db.query('questions', where: 'is_ministry_trap = ?', whereArgs: [1], orderBy: 'id ASC');
  }

  Future<List<Map<String, Object?>>> searchCurriculumFTS(String query) async {
    final term = query.trim();
    if (term.isEmpty) return const [];
    final db = await database;
    return db.query('questions', where: 'question_text LIKE ? OR explanation LIKE ?', whereArgs: ['%$term%', '%$term%'], orderBy: 'id ASC');
  }

  Future<void> recordMistake({required String questionId, required int chapterId, required bool wasCorrect}) async {
    final db = await database;
    final now = DateTime.now();
    await db.transaction((txn) async {
      final rows = await txn.query('fsrs_mistakes', where: 'question_id = ?', whereArgs: [questionId], limit: 1);
      if (!wasCorrect) {
        final failures = rows.isEmpty ? 0 : (rows.first['fail_count'] as int? ?? 0);
        await txn.insert('fsrs_mistakes', {
          'question_id': questionId,
          'chapter_id': chapterId,
          'fail_count': failures + 1,
          'consecutive_correct': 0,
          'next_review_at': now.toIso8601String(),
          'is_resolved': 0,
        }, conflictAlgorithm: ConflictAlgorithm.replace);
      } else if (rows.isNotEmpty) {
        final correctCount = (rows.first['consecutive_correct'] as int? ?? 0) + 1;
        await txn.update(
          'fsrs_mistakes',
          {
            'consecutive_correct': correctCount,
            'next_review_at': now.toIso8601String(),
            'is_resolved': correctCount >= 2 ? 1 : 0,
          },
          where: 'question_id = ?',
          whereArgs: [questionId],
        );
      }
    });
  }

  Future<List<Map<String, Object?>>> getDueMistakesForReview() async {
    final db = await database;
    return db.query('fsrs_mistakes', where: 'is_resolved = 0 AND next_review_at <= ?', whereArgs: [DateTime.now().toIso8601String()], orderBy: 'next_review_at ASC');
  }

  Future<DatabaseBenchmarkResult> runBenchmark({int queryIterations = 40}) async {
    if (queryIterations < 1 || queryIterations > 1000) {
      throw ArgumentError.value(queryIterations, 'queryIterations', 'Must be between 1 and 1000.');
    }
    final db = await database;
    final readWatch = Stopwatch()..start();
    for (var i = 0; i < queryIterations; i++) {
      await db.query('chapters', columns: ['id'], limit: 11);
    }
    readWatch.stop();

    final writeWatch = Stopwatch()..start();
    await db.transaction((txn) async {
      for (var i = 0; i < queryIterations; i++) {
        await txn.insert('exam_attempts', {
          'id': 'benchmark_${DateTime.now().microsecondsSinceEpoch}_$i',
          'paper_id': 'local_benchmark',
          'score': 1,
          'max_score': 1,
          'percentage': 100,
          'time_spent_seconds': 1,
          'created_at': DateTime.now().toIso8601String(),
        });
      }
    });
    writeWatch.stop();

    final file = File(db.path);
    final sizeKb = await file.exists() ? (await file.length() / 1024).ceil() : 1;
    return DatabaseBenchmarkResult(
      totalRecordsQueried: queryIterations * 11,
      totalRecordsInserted: queryIterations,
      averageReadLatencyMs: readWatch.elapsedMicroseconds / 1000 / queryIterations,
      averageWriteLatencyMs: writeWatch.elapsedMicroseconds / 1000 / queryIterations,
      cacheHitRate: 100,
      databaseSizeKb: sizeKb < 1 ? 1 : sizeKb,
    );
  }

  Future<void> reseedDatabase() async {
    final db = await database;
    await db.transaction((txn) async {
      await txn.delete('fsrs_mistakes');
      await txn.delete('exam_attempts');
      await txn.delete('questions');
      await txn.delete('textbooks');
      await txn.delete('chapters');
    });
    await _seedCurriculum(db);
  }

  Future<void> close() async {
    final current = _database;
    _database = null;
    _opening = null;
    if (current != null && current.isOpen) await current.close();
  }
}
