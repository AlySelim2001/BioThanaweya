import '../entities/architecture_benchmark_result.dart';
import '../entities/architecture_metrics.dart';

abstract interface class ArchitectureHubRepository {
  Future<ArchitectureMetrics> loadMetrics();
  Future<ArchitectureBenchmarkResult> runLocalBenchmark({required int queryIterations});
}
