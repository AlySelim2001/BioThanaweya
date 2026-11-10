import '../entities/architecture_benchmark_result.dart';
import '../repositories/architecture_hub_repository.dart';

class RunLocalDatabaseBenchmark {
  const RunLocalDatabaseBenchmark(this._repository);

  final ArchitectureHubRepository _repository;

  Future<ArchitectureBenchmarkResult> call({int queryIterations = 20}) {
    return _repository.runLocalBenchmark(queryIterations: queryIterations);
  }
}
