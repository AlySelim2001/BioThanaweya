import '../entities/architecture_metrics.dart';
import '../../../../core/database/local_database_service.dart';

abstract interface class ArchitectureHubRepository {
  Future<ArchitectureMetrics> loadMetrics();
  Future<DatabaseBenchmarkResult> runLocalBenchmark({required int queryIterations});
}
