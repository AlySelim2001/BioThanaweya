import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../database/local_database_service.dart';

enum CiPipelineStage { idle, linting, testing, building, releasing, completed }

class ArchitectureHubState {
  final bool isArabic;
  final bool isOfflineForced;
  final bool isBenchmarking;
  final bool isCiRunning;
  final bool isTelemetryEnabled;
  final bool isLightPreview;
  final CiPipelineStage ciStage;
  final String ciLog;
  final DatabaseBenchmarkResult? benchmarkResult;
  final int activeTab;

  const ArchitectureHubState({
    this.isArabic = true,
    this.isOfflineForced = true,
    this.isBenchmarking = false,
    this.isCiRunning = false,
    this.isTelemetryEnabled = false,
    this.isLightPreview = false,
    this.ciStage = CiPipelineStage.idle,
    this.ciLog = 'جاهز • Ready',
    this.benchmarkResult,
    this.activeTab = 0,
  });

  ArchitectureHubState copyWith({
    bool? isArabic,
    bool? isOfflineForced,
    bool? isBenchmarking,
    bool? isCiRunning,
    bool? isTelemetryEnabled,
    bool? isLightPreview,
    CiPipelineStage? ciStage,
    String? ciLog,
    DatabaseBenchmarkResult? benchmarkResult,
    int? activeTab,
  }) =>
      ArchitectureHubState(
        isArabic: isArabic ?? this.isArabic,
        isOfflineForced: isOfflineForced ?? this.isOfflineForced,
        isBenchmarking: isBenchmarking ?? this.isBenchmarking,
        isCiRunning: isCiRunning ?? this.isCiRunning,
        isTelemetryEnabled: isTelemetryEnabled ?? this.isTelemetryEnabled,
        isLightPreview: isLightPreview ?? this.isLightPreview,
        ciStage: ciStage ?? this.ciStage,
        ciLog: ciLog ?? this.ciLog,
        benchmarkResult: benchmarkResult ?? this.benchmarkResult,
        activeTab: activeTab ?? this.activeTab,
      );
}

class ArchitectureHubNotifier extends StateNotifier<ArchitectureHubState> {
  ArchitectureHubNotifier(this._dbService) : super(const ArchitectureHubState());

  final LocalDatabaseService _dbService;

  void toggleLanguage() => state = state.copyWith(isArabic: !state.isArabic);
  void toggleOfflineMode() => state = state.copyWith(isOfflineForced: !state.isOfflineForced);
  void toggleTelemetry() => state = state.copyWith(isTelemetryEnabled: !state.isTelemetryEnabled);
  void toggleThemePreview() => state = state.copyWith(isLightPreview: !state.isLightPreview);
  void setActiveTab(int tab) => state = state.copyWith(activeTab: tab.clamp(0, 3));

  Future<void> runDatabaseBenchmark() async {
    if (state.isBenchmarking) return;
    state = state.copyWith(isBenchmarking: true);
    try {
      final result = await _dbService.runBenchmark(queryIterations: 20);
      state = state.copyWith(
        isBenchmarking: false,
        benchmarkResult: result,
        ciLog: 'SQLite OK • ${result.totalRecordsQueried} local records queried',
      );
    } catch (error) {
      state = state.copyWith(isBenchmarking: false, ciLog: 'SQLite check failed: $error');
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
    for (var index = 0; index < stages.length; index++) {
      if (index > 0) {
        await Future<void>.delayed(const Duration(milliseconds: 220));
        state = state.copyWith(ciStage: stages[index].$1, ciLog: stages[index].$2);
      }
    }
    await Future<void>.delayed(const Duration(milliseconds: 220));
    state = state.copyWith(
      isCiRunning: false,
      ciStage: CiPipelineStage.completed,
      ciLog: 'Simulation complete • GitHub Actions status is not queried live',
    );
  }
}

final architectureHubProvider = StateNotifierProvider<ArchitectureHubNotifier, ArchitectureHubState>((ref) {
  return ArchitectureHubNotifier(LocalDatabaseService.instance);
});

class AppArchitectureHub extends ConsumerStatefulWidget {
  const AppArchitectureHub({super.key});

  @override
  ConsumerState<AppArchitectureHub> createState() => _AppArchitectureHubState();
}

class _AppArchitectureHubState extends ConsumerState<AppArchitectureHub> {
  int _chaptersCount = 0;
  String? _databaseError;

  @override
  void initState() {
    super.initState();
    unawaited(_loadMetrics());
  }

  Future<void> _loadMetrics() async {
    try {
      final chapters = await LocalDatabaseService.instance.getAllChapters();
      if (!mounted) return;
      setState(() {
        _chaptersCount = chapters.length;
        _databaseError = null;
      });
    } catch (error) {
      if (!mounted) return;
      setState(() => _databaseError = '$error');
    }
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(architectureHubProvider);
    final controller = ref.read(architectureHubProvider.notifier);
    final useLightPreview = state.isLightPreview;
    final background = useLightPreview ? const Color(0xFFF3F6FA) : const Color(0xFF0B1320);
    final surface = useLightPreview ? Colors.white : const Color(0xFF152234);
    final foreground = useLightPreview ? const Color(0xFF172033) : Colors.white;
    final muted = useLightPreview ? const Color(0xFF526176) : const Color(0xFFB2C0D0);

    return Directionality(
      textDirection: state.isArabic ? TextDirection.rtl : TextDirection.ltr,
      child: Scaffold(
        backgroundColor: background,
        appBar: AppBar(
          backgroundColor: surface,
          foregroundColor: foreground,
          title: Text(state.isArabic ? 'معمارية تعلم مع علي' : 'BioThanaweya Architecture'),
          actions: [
            IconButton(
              tooltip: state.isArabic ? 'تبديل اللغة' : 'Switch language',
              onPressed: controller.toggleLanguage,
              icon: const Icon(Icons.translate_rounded),
            ),
            IconButton(
              tooltip: state.isLightPreview ? 'Dark preview' : 'Light preview',
              onPressed: controller.toggleThemePreview,
              icon: Icon(state.isLightPreview ? Icons.dark_mode_outlined : Icons.light_mode_outlined),
            ),
            const SizedBox(width: 4),
          ],
        ),
        body: SafeArea(
          child: LayoutBuilder(
            builder: (context, constraints) {
              final columns = constraints.maxWidth >= 900 ? 3 : constraints.maxWidth >= 600 ? 2 : 1;
              return ListView(
                padding: const EdgeInsets.all(16),
                children: [
                  _heroCard(state, surface, foreground, muted),
                  const SizedBox(height: 14),
                  _statusRow(state, surface, foreground, muted, controller),
                  const SizedBox(height: 14),
                  _bentoGrid(state, controller, columns, surface, foreground, muted),
                  const SizedBox(height: 14),
                  _databaseCard(state, controller, surface, foreground, muted),
                  const SizedBox(height: 12),
                  _architectureSections(state, surface, foreground, muted),
                  const SizedBox(height: 12),
                  _pipelineCard(state, controller, surface, foreground, muted),
                  const SizedBox(height: 14),
                  _telemetryCard(state, controller, surface, foreground, muted),
                  if (_databaseError != null) ...[
                    const SizedBox(height: 8),
                    Text(_databaseError!, style: const TextStyle(color: Colors.redAccent)),
                  ],
                ],
              );
            },
          ),
        ),
      ),
    );
  }

  Widget _heroCard(ArchitectureHubState state, Color surface, Color foreground, Color muted) {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        gradient: const LinearGradient(colors: [Color(0xFF0F766E), Color(0xFF164E63)]),
        borderRadius: BorderRadius.circular(24),
      ),
      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        Row(children: [
          const Icon(Icons.account_tree_rounded, color: Colors.white),
          const SizedBox(width: 8),
          Expanded(child: Text(state.isArabic ? 'تعلم مع علي • خريطة الأنظمة' : 'Learn with Ali • System Blueprint', style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 18))),
          _statusPill(state.isOfflineForced ? 'OFFLINE READY' : 'SYNC POLICY', state.isOfflineForced ? const Color(0xFF34D399) : const Color(0xFFFBBF24)),
        ]),
        const SizedBox(height: 10),
        Text(
          state.isArabic
              ? 'تجربة تعلم عربية RTL، تعمل بياناتها الأساسية محلياً. المزامنة شبكة اختيارية وليست شرطاً للدراسة.'
              : 'Arabic-first, RTL-ready learning. Core curriculum remains local; online sync is optional, never required for study.',
          style: const TextStyle(color: Colors.white, height: 1.5),
        ),
        const SizedBox(height: 14),
        Wrap(spacing: 8, runSpacing: 8, children: [
          _tag('Flutter / Dart', Colors.white),
          _tag('Riverpod', Colors.white),
          _tag('SQLite', Colors.white),
          _tag('GitHub Actions', Colors.white),
        ]),
      ]),
    );
  }

  Widget _statusRow(ArchitectureHubState state, Color surface, Color foreground, Color muted, ArchitectureHubNotifier controller) {
    return Row(children: [
      Expanded(child: _statusTile('Offline', state.isOfflineForced ? 'Ready' : 'Optional sync', Icons.offline_pin_rounded, state.isOfflineForced ? const Color(0xFF10B981) : const Color(0xFFF59E0B), surface, foreground, muted)),
      const SizedBox(width: 8),
      Expanded(child: _statusTile('SQLite', _databaseError == null ? 'Local store' : 'Needs attention', Icons.storage_rounded, _databaseError == null ? const Color(0xFF06B6D4) : Colors.redAccent, surface, foreground, muted)),
      const SizedBox(width: 8),
      Expanded(child: _statusTile('CI/CD', state.isCiRunning ? 'Simulating' : 'Workflow configured', Icons.rocket_launch_rounded, const Color(0xFFF59E0B), surface, foreground, muted)),
    ]);
  }

  Widget _bentoGrid(ArchitectureHubState state, ArchitectureHubNotifier controller, int columns, Color surface, Color foreground, Color muted) {
    final items = <({String title, String detail, IconData icon, Color accent})>[
      (title: state.isArabic ? 'Flutter UI/UX' : 'Flutter UI/UX', detail: 'Hot reload • adaptive widgets • RTL', icon: Icons.palette_rounded, accent: const Color(0xFF22D3EE)),
      (title: 'SQLite local', detail: 'Indexed chapters • textbooks • progress', icon: Icons.storage_rounded, accent: const Color(0xFF34D399)),
      (title: 'Offline-first', detail: 'Core study flows do not need a connection', icon: Icons.wifi_off_rounded, accent: const Color(0xFFA78BFA)),
      (title: 'GitHub CI/CD', detail: 'Analyze • tests • Android artifacts', icon: Icons.integration_instructions_rounded, accent: const Color(0xFFFBBF24)),
    ];
    return GridView.count(
      crossAxisCount: columns,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      crossAxisSpacing: 10,
      mainAxisSpacing: 10,
      childAspectRatio: columns == 1 ? 2.6 : 1.65,
      children: items.map((item) => _bentoTile(item.title, item.detail, item.icon, item.accent, surface, foreground, muted)).toList(),
    );
  }

  Widget _databaseCard(ArchitectureHubState state, ArchitectureHubNotifier controller, Color surface, Color foreground, Color muted) {
    final result = state.benchmarkResult;
    return _card(surface, child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      Row(children: [
        const Icon(Icons.dataset_rounded, color: Color(0xFF34D399)),
        const SizedBox(width: 8),
        Expanded(child: Text(state.isArabic ? 'قاعدة المنهج المحلية' : 'Local curriculum database', style: TextStyle(color: foreground, fontWeight: FontWeight.bold, fontSize: 16))),
        Text('$_chaptersCount / 11', style: const TextStyle(color: Color(0xFF34D399), fontWeight: FontWeight.bold)),
      ]),
      const SizedBox(height: 6),
      Text(_databaseError == null ? 'Chapters and textbook metadata are read from SQLite; this check makes no network request.' : 'Local database error: $_databaseError', style: TextStyle(color: muted, height: 1.4)),
      const SizedBox(height: 12),
      Wrap(spacing: 8, runSpacing: 8, children: [
        OutlinedButton.icon(onPressed: state.isBenchmarking ? null : () => controller.runDatabaseBenchmark(), icon: state.isBenchmarking ? const SizedBox.square(dimension: 16, child: CircularProgressIndicator(strokeWidth: 2)) : const Icon(Icons.speed_rounded), label: const Text('Run local benchmark')),
        if (result != null) _tag('Read ${result.averageReadLatencyMs.toStringAsFixed(2)} ms', const Color(0xFF34D399)),
        if (result != null) _tag('${result.totalRecordsInserted} writes', const Color(0xFF06B6D4)),
      ]),
    ]));
  }

  Widget _architectureSections(ArchitectureHubState state, Color surface, Color foreground, Color muted) {
    final sections = <({String title, String body, IconData icon, Color color})>[
      (title: 'UI/UX • واجهة المستخدم', body: 'Bento cards keep related learning actions together. Flutter widgets adapt to viewport width; Material themes and Directionality support light/dark styling and Arabic RTL.', icon: Icons.design_services_rounded, color: const Color(0xFF22D3EE)),
      (title: 'Domain • منطق المنهج', body: 'Domain entities, repository contracts, and use cases are framework-independent. Presentation controllers call domain contracts rather than SQL directly.', icon: Icons.layers_rounded, color: const Color(0xFFA78BFA)),
      (title: 'Data • التخزين المحلي', body: 'SQLite stores curriculum, textbook metadata, exam attempts, and review state. Versioned migrations and parameterized queries preserve local integrity.', icon: Icons.storage_rounded, color: const Color(0xFF34D399)),
      (title: 'Sync boundary • المزامنة', body: 'Local data is authoritative for essential study. Any future remote sync belongs behind a repository boundary and must not block local reads or writes.', icon: Icons.sync_alt_rounded, color: const Color(0xFFFBBF24)),
    ];
    return Column(children: sections.map((section) => Card(
      color: surface,
      margin: const EdgeInsets.only(bottom: 8),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18), side: BorderSide(color: section.color.withValues(alpha: 0.3))),
      child: ExpansionTile(
        leading: Icon(section.icon, color: section.color),
        iconColor: section.color,
        collapsedIconColor: section.color,
        title: Text(section.title, style: TextStyle(color: foreground, fontWeight: FontWeight.w700)),
        childrenPadding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
        children: [Text(section.body, style: TextStyle(color: muted, height: 1.5))],
      ),
    )).toList());
  }

  Widget _pipelineCard(ArchitectureHubState state, ArchitectureHubNotifier controller, Color surface, Color foreground, Color muted) {
    final labels = ['Lint', 'Test', 'Build', 'Release'];
    final completedStages = state.ciStage == CiPipelineStage.completed ? 4 : state.ciStage.index.clamp(0, 4);
    return _card(surface, child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      Row(children: [const Icon(Icons.rocket_launch_rounded, color: Color(0xFFFBBF24)), const SizedBox(width: 8), Expanded(child: Text('GitHub Actions CI/CD', style: TextStyle(color: foreground, fontWeight: FontWeight.bold, fontSize: 16))), ElevatedButton(onPressed: state.isCiRunning ? null : () => controller.triggerCiPipelineSimulation(), child: Text(state.isCiRunning ? 'Running' : 'Simulate'))]),
      const SizedBox(height: 12),
      Row(children: List.generate(labels.length, (index) => Expanded(child: Row(children: [
        Expanded(child: Column(children: [Icon(index < completedStages ? Icons.check_circle : Icons.radio_button_unchecked, color: index < completedStages ? const Color(0xFF34D399) : muted), const SizedBox(height: 4), Text(labels[index], style: TextStyle(color: muted, fontSize: 11))])),
        if (index < labels.length - 1) Expanded(child: Divider(color: index < completedStages - 1 ? const Color(0xFF34D399) : muted)),
      ])))),
      const SizedBox(height: 10),
      Text(state.ciLog, style: TextStyle(color: muted, fontSize: 12)),
      const SizedBox(height: 4),
      Text('Pipeline animation is a local demonstration; live workflow results are shown in GitHub Actions.', style: TextStyle(color: muted.withValues(alpha: 0.8), fontSize: 11)),
    ]));
  }

  Widget _telemetryCard(ArchitectureHubState state, ArchitectureHubNotifier controller, Color surface, Color foreground, Color muted) {
    return _card(surface, child: Column(children: [
      SwitchListTile.adaptive(contentPadding: EdgeInsets.zero, title: Text('Offline-ready mode', style: TextStyle(color: foreground)), subtitle: Text('Display preference only; local data stays available either way.', style: TextStyle(color: muted, fontSize: 12)), value: state.isOfflineForced, onChanged: (_) => controller.toggleOfflineMode()),
      const Divider(height: 1),
      SwitchListTile.adaptive(contentPadding: EdgeInsets.zero, title: Text('Quick telemetry', style: TextStyle(color: foreground)), subtitle: Text('Enables local diagnostics and benchmark controls; no data is transmitted.', style: TextStyle(color: muted, fontSize: 12)), value: state.isTelemetryEnabled, onChanged: (_) => controller.toggleTelemetry()),
      if (state.isTelemetryEnabled) Align(alignment: AlignmentDirectional.centerStart, child: TextButton.icon(onPressed: state.isBenchmarking ? null : () => controller.runDatabaseBenchmark(), icon: const Icon(Icons.monitor_heart_outlined), label: const Text('Run local diagnostics'))),
    ]));
  }

  Widget _card(Color color, {required Widget child}) => Container(padding: const EdgeInsets.all(16), decoration: BoxDecoration(color: color, borderRadius: BorderRadius.circular(20), border: Border.all(color: const Color(0xFF53657A).withValues(alpha: 0.25))), child: child);

  Widget _bentoTile(String title, String detail, IconData icon, Color accent, Color surface, Color foreground, Color muted) => _card(surface, child: Column(crossAxisAlignment: CrossAxisAlignment.start, mainAxisAlignment: MainAxisAlignment.center, children: [Icon(icon, color: accent), const SizedBox(height: 8), Text(title, style: TextStyle(color: foreground, fontWeight: FontWeight.bold)), const SizedBox(height: 4), Text(detail, maxLines: 2, overflow: TextOverflow.ellipsis, style: TextStyle(color: muted, fontSize: 12))]));

  Widget _statusTile(String title, String value, IconData icon, Color accent, Color surface, Color foreground, Color muted) => _card(surface, child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [Icon(icon, color: accent, size: 20), const SizedBox(height: 8), Text(title, style: TextStyle(color: muted, fontSize: 11)), const SizedBox(height: 2), Text(value, maxLines: 1, overflow: TextOverflow.ellipsis, style: TextStyle(color: foreground, fontWeight: FontWeight.bold, fontSize: 12))]));

  Widget _statusPill(String label, Color color) => Container(padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 5), decoration: BoxDecoration(color: color.withValues(alpha: 0.15), borderRadius: BorderRadius.circular(20)), child: Text(label, style: TextStyle(color: color, fontSize: 10, fontWeight: FontWeight.bold)));

  Widget _tag(String label, Color color) => Container(padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6), decoration: BoxDecoration(color: color.withValues(alpha: 0.14), borderRadius: BorderRadius.circular(30)), child: Text(label, style: TextStyle(color: color, fontWeight: FontWeight.w600, fontSize: 11)));
}

typedef AppArchitectureHubScreen = AppArchitectureHub;
