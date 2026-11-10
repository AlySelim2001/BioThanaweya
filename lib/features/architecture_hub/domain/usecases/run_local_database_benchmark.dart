import '../../../../core/database/local_database_service.dart';
import '../repositories/architecture_hub_repository.dart';

class RunLocalDatabaseBenchmark {
  const RunLocalDatabaseBenchmark(this._repository);

  final ArchitectureHubRepository _repository;

  Future<DatabaseBenchmarkResult> call({int queryIterations = 20}) {
    return _repository.runLocalBenchmark(queryIterations: queryIterations);
  }
}
