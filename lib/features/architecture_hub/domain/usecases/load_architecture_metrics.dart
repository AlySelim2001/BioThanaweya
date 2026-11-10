import '../entities/architecture_metrics.dart';
import '../repositories/architecture_hub_repository.dart';

class LoadArchitectureMetrics {
  const LoadArchitectureMetrics(this._repository);

  final ArchitectureHubRepository _repository;

  Future<ArchitectureMetrics> call() => _repository.loadMetrics();
}
