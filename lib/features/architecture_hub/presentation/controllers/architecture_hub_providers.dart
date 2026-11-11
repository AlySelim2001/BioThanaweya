import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../../core/architecture/app_architecture_hub.dart';
import '../../../../core/database/local_database_service.dart';

export '../../../../core/architecture/app_architecture_hub.dart'
    show architectureHubProvider;

final architectureHubRepositoryProvider = Provider<LocalDatabaseService>((ref) {
  return ref.watch(localDatabaseServiceProvider);
});

final loadArchitectureMetricsProvider = FutureProvider<ArchitectureMetrics?>((ref) async {
  final notifier = ref.read(architectureHubProvider.notifier);
  await notifier.loadMetrics();
  return ref.read(architectureHubProvider).metrics;
});

final runLocalDatabaseBenchmarkProvider = FutureProvider<DatabaseBenchmarkResult?>((ref) async {
  final notifier = ref.read(architectureHubProvider.notifier);
  await notifier.runDatabaseBenchmark();
  return ref.read(architectureHubProvider).benchmarkResult;
});
