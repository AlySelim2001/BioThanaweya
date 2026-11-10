import 'dart:async';

import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../domain/entities/architecture_benchmark_result.dart';
import '../../domain/entities/architecture_metrics.dart';
import '../../domain/usecases/load_architecture_metrics.dart';
import '../../domain/usecases/run_local_database_benchmark.dart';
import '../../domain/repositories/architecture_hub_repository.dart';

// A narrow test seam that lets tests inject a fake repository without a platform database.
ArchitectureHubNotifier notifierForRepository(ArchitectureHubRepository repository) {
  return ArchitectureHubNotifier(
    loadMetricsUseCase: LoadArchitectureMetrics(repository),
    benchmarkUseCase: RunLocalDatabaseBenchmark(repository),
  );
}

enum ArchitectureLoadStatus { initial, loading, success, error }
enum CiPipelineStage { idle, linting, testing, building, releasing, completed }

class ArchitectureHubState {
  const ArchitectureHubState({
    this.isArabic = true,
    this.isOfflineForced = true,
    this.isBenchmarking = false,
    this.isCiRunning = false,
    this.isTelemetryEnabled = false,
    this.isLightPreview = false,
    this.loadStatus = ArchitectureLoadStatus.initial,
    this.metrics,
    this.benchmarkResult,
    this.errorMessage,
    this.ciStage = CiPipelineStage.idle,
    this.ciLog = 'جاهز • Ready',
    this.activeTab = 0,
  });

  final bool isArabic;
  final bool isOfflineForced;
  final bool isBenchmarking;
  final bool isCiRunning;
  final bool isTelemetryEnabled;
  final bool isLightPreview;
  final ArchitectureLoadStatus loadStatus;
  final ArchitectureMetrics? metrics;
  final ArchitectureBenchmarkResult? benchmarkResult;
  final String? errorMessage;
  final CiPipelineStage ciStage;
  final String ciLog;
  final int activeTab;

  bool get isLoading => loadStatus == ArchitectureLoadStatus.loading;
  bool get hasError => loadStatus == ArchitectureLoadStatus.error;

  ArchitectureHubState copyWith({
    bool? isArabic,
    bool? isOfflineForced,
    bool? isBenchmarking,
    bool? isCiRunning,
    bool? isTelemetryEnabled,
    bool? isLightPreview,
    ArchitectureLoadStatus? loadStatus,
    ArchitectureMetrics? metrics,
    ArchitectureBenchmarkResult? benchmarkResult,
    String? errorMessage,
    bool clearError = false,
    CiPipelineStage? ciStage,
    String? ciLog,
    int? activeTab,
  }) {
    return ArchitectureHubState(
      isArabic: isArabic ?? this.isArabic,
      isOfflineForced: isOfflineForced ?? this.isOfflineForced,
      isBenchmarking: isBenchmarking ?? this.isBenchmarking,
      isCiRunning: isCiRunning ?? this.isCiRunning,
      isTelemetryEnabled: isTelemetryEnabled ?? this.isTelemetryEnabled,
      isLightPreview: isLightPreview ?? this.isLightPreview,
      loadStatus: loadStatus ?? this.loadStatus,
      metrics: metrics ?? this.metrics,
      benchmarkResult: benchmarkResult ?? this.benchmarkResult,
      errorMessage: clearError ? null : errorMessage ?? this.errorMessage,
      ciStage: ciStage ?? this.ciStage,
      ciLog: ciLog ?? this.ciLog,
      activeTab: activeTab ?? this.activeTab,
    );
  }
}

class ArchitectureHubNotifier extends StateNotifier<ArchitectureHubState> {
  ArchitectureHubNotifier({
    required LoadArchitectureMetrics loadMetricsUseCase,
    required RunLocalDatabaseBenchmark benchmarkUseCase,
  })  : _loadMetricsUseCase = loadMetricsUseCase,
        _benchmarkUseCase = benchmarkUseCase,
        super(const ArchitectureHubState());

  ArchitectureHubNotifier.fromRepository(ArchitectureHubRepository repository)
      : this(
          loadMetricsUseCase: LoadArchitectureMetrics(repository),
          benchmarkUseCase: RunLocalDatabaseBenchmark(repository),
        );

  final LoadArchitectureMetrics _loadMetricsUseCase;
  final RunLocalDatabaseBenchmark _benchmarkUseCase;

  void toggleLanguage() => state = state.copyWith(isArabic: !state.isArabic);
  void toggleOfflineMode() => state = state.copyWith(isOfflineForced: !state.isOfflineForced);
  void toggleTelemetry() => state = state.copyWith(isTelemetryEnabled: !state.isTelemetryEnabled);
  void toggleThemePreview() => state = state.copyWith(isLightPreview: !state.isLightPreview);
  void setActiveTab(int tab) => state = state.copyWith(activeTab: tab.clamp(0, 3));

  Future<void> loadMetrics() async {
    state = state.copyWith(loadStatus: ArchitectureLoadStatus.loading, clearError: true);
    try {
      final metrics = await _loadMetricsUseCase();
      state = state.copyWith(
        loadStatus: ArchitectureLoadStatus.success,
        metrics: metrics,
        isOfflineForced: metrics.offlineReady,
        clearError: true,
      );
    } catch (error) {
      state = state.copyWith(
        loadStatus: ArchitectureLoadStatus.error,
        errorMessage: error.toString(),
        isOfflineForced: true,
      );
    }
  }

  Future<void> runDatabaseBenchmark() async {
    if (state.isBenchmarking) return;
    state = state.copyWith(isBenchmarking: true, clearError: true);
    try {
      final result = await _benchmarkUseCase(queryIterations: 20);
      state = state.copyWith(
        isBenchmarking: false,
        benchmarkResult: result,
        ciLog: 'SQLite OK • ${result.totalRecordsQueried} local records queried',
        clearError: true,
      );
    } catch (error) {
      state = state.copyWith(isBenchmarking: false, errorMessage: error.toString());
    }
  }

  Future<void> triggerCiPipelineSimulation() async {
    if (state.isCiRunning) return;
    const stages = <(CiPipelineStage, String)>[
      (CiPipelineStage.linting, '1/4 • Static analysis'),
      (CiPipelineStage.testing, '2/4 • Automated tests'),
      (CiPipelineStage.building, '3/4 • Android build'),
      (CiPipelineStage.releasing, '4/4 • Release artifacts'),
    ];
    state = state.copyWith(isCiRunning: true, ciStage: stages.first.$1, ciLog: stages.first.$2);
    for (var index = 1; index < stages.length; index++) {
      await Future<void>.delayed(const Duration(milliseconds: 220));
      state = state.copyWith(ciStage: stages[index].$1, ciLog: stages[index].$2);
    }
    await Future<void>.delayed(const Duration(milliseconds: 220));
    state = state.copyWith(
      isCiRunning: false,
      ciStage: CiPipelineStage.completed,
      ciLog: 'Simulation complete • GitHub Actions status is not queried live',
    );
  }
}
