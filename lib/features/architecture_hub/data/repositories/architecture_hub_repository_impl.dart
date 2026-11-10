import '../../domain/entities/architecture_metrics.dart';
import '../../domain/repositories/architecture_hub_repository.dart';
import '../../../../core/database/local_database_service.dart';
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
    );
  }

  @override
  Future<DatabaseBenchmarkResult> runLocalBenchmark({required int queryIterations}) {
    return _dataSource.benchmark(queryIterations: queryIterations);
  }
}
