import '../../domain/entities/architecture_benchmark_result.dart';
import '../../domain/entities/architecture_metrics.dart';
import '../../domain/repositories/architecture_hub_repository.dart';
import '../datasources/architecture_local_data_source.dart';

class ArchitectureHubRepositoryImpl implements ArchitectureHubRepository {
  const ArchitectureHubRepositoryImpl(this._dataSource);

  final ArchitectureLocalDataSource _dataSource;

  @override
  Future<ArchitectureMetrics> loadMetrics() async {
    final results = await Future.wait<int>([
      _dataSource.countChapters(),
      _dataSource.countTextbooks(),
      _dataSource.countExamAttempts(),
    ]);
    return ArchitectureMetrics(
      chapterCount: results[0],
      textbookCount: results[1],
      examAttemptCount: results[2],
      fsrsReady: true,
      offlineReady: true,
    );
  }

  @override
  Future<ArchitectureBenchmarkResult> runLocalBenchmark({required int queryIterations}) async {
    final result = await _dataSource.benchmark(queryIterations: queryIterations);
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
