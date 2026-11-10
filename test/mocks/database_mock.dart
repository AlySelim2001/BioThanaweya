import 'dart:io';
import 'package:flutter_test/flutter_test.dart';
import 'package:sqflite_common_ffi/sqflite_ffi.dart';
import 'package:smarts_bio/core/database/local_database_service.dart';

/// Robust, thread-safe SQLite mock environment for local unit and integration tests.
/// Guarantees clean architecture dependency isolation and deterministic database testing.
class DatabaseMock {
  static bool _ffiInitialized = false;
  static Directory? _tempDir;
  static LocalDatabaseService? _mockService;

  /// Ensures SQLite FFI is initialized once across all test suites in a thread-safe manner.
  static void initializeEnvironment() {
    if (!_ffiInitialized) {
      TestWidgetsFlutterBinding.ensureInitialized();
      sqfliteFfiInit();
      databaseFactory = databaseFactoryFfi;
      _ffiInitialized = true;
    }
  }

  /// Creates and returns an isolated, fully initialized and seeded LocalDatabaseService instance.
  /// Uses an in-memory database by default, or an isolated temporary file if a custom path is specified.
  static Future<LocalDatabaseService> createInitialized({String? path}) async {
    initializeEnvironment();

    final dbPath = path ?? inMemoryDatabasePath;
    final service = LocalDatabaseService(databasePath: dbPath);
    await service.initializeDatabase();
    await service.reseedDatabase();
    return service;
  }

  /// Initializes a global mock database and sets it onto LocalDatabaseService.instance.
  /// Injects all 11 curriculum chapters, 3 official textbooks, and trap questions.
  static Future<LocalDatabaseService> createInitializedAndSetGlobal() async {
    initializeEnvironment();
    _tempDir ??= await Directory.systemTemp.createTemp('biothanaweya_mock_db_');
    final dbFile = '${_tempDir!.path}/mock_curriculum.db';

    _mockService = LocalDatabaseService(databasePath: dbFile);
    await _mockService!.initializeDatabase();
    await _mockService!.reseedDatabase();
    LocalDatabaseService.setMockInstance(_mockService!);
    return _mockService!;
  }

  /// Ensures the global database instance is completely initialized and seeded before any setUp block runs.
  static Future<void> ensureGlobalSeeded() async {
    if (_mockService == null || !_mockService!.isInitialized) {
      await createInitializedAndSetGlobal();
    } else {
      await _mockService!.reseedDatabase();
    }
  }

  /// Injects the 11-chapter curriculum seed data directly into an active LocalDatabaseService.
  static Future<void> injectSeedData(LocalDatabaseService service) async {
    await service.initializeDatabase();
    await service.reseedDatabase();
  }

  /// Closes database connections and cleans up temporary directories.
  static Future<void> disposeGlobal() async {
    if (_mockService != null) {
      await _mockService!.close();
      _mockService = null;
    }
    if (_tempDir != null && await _tempDir!.exists()) {
      await _tempDir!.delete(recursive: true);
      _tempDir = null;
    }
    LocalDatabaseService.resetInstance();
  }
}
