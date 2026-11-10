import 'package:smarts_bio/core/database/local_database_service.dart';
import 'package:smarts_bio/features/architecture_hub/domain/entities/architecture_benchmark_result.dart';

abstract interface class ArchitectureLocalDataSource {
  Future<int> countChapters();
  Future<int> countTextbooks();
  Future<int> countExamAttempts();
  Future<ArchitectureBenchmarkResult> benchmark({required int queryIterations});
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
  Future<ArchitectureBenchmarkResult> benchmark({required int queryIterations}) async {
    final result = await _database.runBenchmark(queryIterations: queryIterations);
    return ArchitectureBenchmarkResult(
      totalRecordsQueried: result.totalRecordsQueried,
      totalRecordsInserted: result.totalRecordsInserted,
      averageReadLatencyMs: result.averageReadLatencyMs,
      averageWriteLatencyMs: result.averageWriteLatencyMs,
      cacheHitRate: result.cacheHitRate,
      databaseSizeKb: result.databaseSizeKb,
    );
  }
}
