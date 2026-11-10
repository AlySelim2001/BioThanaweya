import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../../core/database/local_database_service.dart';
import '../../data/datasources/architecture_local_data_source.dart';
import '../../data/repositories/architecture_hub_repository_impl.dart';
import '../../domain/repositories/architecture_hub_repository.dart';
import '../../domain/usecases/load_architecture_metrics.dart';
import '../../domain/usecases/run_local_database_benchmark.dart';
import 'architecture_hub_notifier.dart';

final architectureHubRepositoryProvider = Provider<ArchitectureHubRepository>((ref) {
  return ArchitectureHubRepositoryImpl(
    SqliteArchitectureDataSource(LocalDatabaseService.instance),
  );
});

final loadArchitectureMetricsProvider = Provider<LoadArchitectureMetrics>((ref) {
  return LoadArchitectureMetrics(ref.watch(architectureHubRepositoryProvider));
});

final runLocalDatabaseBenchmarkProvider = Provider<RunLocalDatabaseBenchmark>((ref) {
  return RunLocalDatabaseBenchmark(ref.watch(architectureHubRepositoryProvider));
});

final architectureHubProvider = StateNotifierProvider<ArchitectureHubNotifier, ArchitectureHubState>((ref) {
  return ArchitectureHubNotifier(
    loadMetricsUseCase: ref.watch(loadArchitectureMetricsProvider),
    benchmarkUseCase: ref.watch(runLocalDatabaseBenchmarkProvider),
  );
});
