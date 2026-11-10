import 'package:smarts_bio/features/architecture_hub/domain/entities/architecture_benchmark_result.dart';
import 'package:smarts_bio/features/architecture_hub/domain/entities/architecture_metrics.dart';
import 'package:smarts_bio/features/architecture_hub/domain/repositories/architecture_hub_repository.dart';
import 'package:smarts_bio/features/architecture_hub/data/datasources/architecture_local_data_source.dart';

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
  Future<ArchitectureBenchmarkResult> runLocalBenchmark({required int queryIterations}) {
    return _dataSource.benchmark(queryIterations: queryIterations);
  }
}
