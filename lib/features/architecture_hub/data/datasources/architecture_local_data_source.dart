import '../../../../core/database/local_database_service.dart';

abstract interface class ArchitectureLocalDataSource {
  Future<int> countChapters();
  Future<int> countTextbooks();
  Future<int> countExamAttempts();
  Future<DatabaseBenchmarkResult> benchmark({required int queryIterations});
}

class SqliteArchitectureDataSource implements ArchitectureLocalDataSource {
  const SqliteArchitectureDataSource(this._database);

  final LocalDatabaseService _database;

  @override
  Future<int> countChapters() async => (await _database.getAllChapters()).length;

  @override
  Future<int> countTextbooks() async => (await _database.getAllTextbooks()).length;

  @override
  Future<int> countExamAttempts() async => (await _database.queryExamAttempts()).length;

  @override
  Future<DatabaseBenchmarkResult> benchmark({required int queryIterations}) {
    return _database.runBenchmark(queryIterations: queryIterations);
  }
}
