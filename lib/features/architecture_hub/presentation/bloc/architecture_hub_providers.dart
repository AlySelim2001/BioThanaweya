import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:smarts_bio/core/database/local_database_service.dart';
import 'package:smarts_bio/features/architecture_hub/data/datasources/architecture_local_data_source.dart';
import 'package:smarts_bio/features/architecture_hub/data/repositories/architecture_hub_repository_impl.dart';
import 'package:smarts_bio/features/architecture_hub/domain/repositories/architecture_hub_repository.dart';
import 'package:smarts_bio/features/architecture_hub/domain/usecases/load_architecture_metrics.dart';
import 'package:smarts_bio/features/architecture_hub/domain/usecases/run_local_database_benchmark.dart';
import 'package:smarts_bio/features/architecture_hub/presentation/bloc/architecture_hub_notifier.dart';

final architectureHubRepositoryProvider = Provider<ArchitectureHubRepository>((ref) {
  return ArchitectureHubRepositoryImpl(SqliteArchitectureDataSource(LocalDatabaseService.instance));
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
