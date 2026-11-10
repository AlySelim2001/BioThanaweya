import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../features/architecture_hub/presentation/bloc/architecture_hub_notifier.dart';
import '../../../features/architecture_hub/presentation/bloc/architecture_hub_providers.dart';

class AppArchitectureHub extends ConsumerStatefulWidget {
  const AppArchitectureHub({super.key});

  @override
  ConsumerState<AppArchitectureHub> createState() => _AppArchitectureHubState();
}

class _AppArchitectureHubState extends ConsumerState<AppArchitectureHub> {
  @override
  void initState() {
    super.initState();
    Future<void>.microtask(() => ref.read(architectureHubProvider.notifier).loadMetrics());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(architectureHubProvider);
    final controller = ref.read(architectureHubProvider.notifier);
    final light = state.isLightPreview;
    final background = light ? const Color(0xFFF2F6FA) : const Color(0xFF0B1320);
    final surface = light ? Colors.white : const Color(0xFF152234);
    final foreground = light ? const Color(0xFF172033) : Colors.white;
    final muted = light ? const Color(0xFF526176) : const Color(0xFFB2C0D0);

    return Directionality(
      textDirection: state.isArabic ? TextDirection.rtl : TextDirection.ltr,
      child: Scaffold(
        backgroundColor: background,
        appBar: AppBar(
          backgroundColor: surface,
          foregroundColor: foreground,
          title: Text(state.isArabic ? 'معمارية تعلم مع علي' : 'BioThanaweya Architecture'),
          actions: [
            IconButton(onPressed: controller.toggleLanguage, tooltip: 'Language', icon: const Icon(Icons.translate_rounded)),
            IconButton(onPressed: controller.toggleThemePreview, tooltip: 'Theme preview', icon: Icon(light ? Icons.dark_mode : Icons.light_mode)),
            IconButton(onPressed: controller.loadMetrics, tooltip: 'Refresh local metrics', icon: const Icon(Icons.refresh_rounded)),
          ],
        ),
        body: SafeArea(
          child: LayoutBuilder(builder: (context, constraints) {
            final columns = constraints.maxWidth >= 900 ? 3 : constraints.maxWidth >= 600 ? 2 : 1;
            return ListView(padding: const EdgeInsets.all(16), children: [
              _hero(state, foreground),
              const SizedBox(height: 12),
              _statusTiles(state, surface, foreground, muted),
              const SizedBox(height: 12),
              _pillarGrid(columns, surface, foreground, muted),
              const SizedBox(height: 12),
              _metricsCard(state, controller, surface, foreground, muted),
              const SizedBox(height: 12),
              _sections(state, surface, foreground, muted),
              const SizedBox(height: 12),
              _pipeline(state, controller, surface, foreground, muted),
              const SizedBox(height: 12),
              _telemetry(state, controller, surface, foreground, muted),
            ]);
          }),
        ),
      ),
    );
  }

  Widget _hero(ArchitectureHubState state, Color foreground) => Container(
        padding: const EdgeInsets.all(20),
        decoration: BoxDecoration(
          gradient: const LinearGradient(colors: [Color(0xFF0F766E), Color(0xFF164E63)]),
          borderRadius: BorderRadius.circular(22),
        ),
        child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
          Row(children: [
            const Icon(Icons.account_tree_rounded, color: Colors.white),
            const SizedBox(width: 8),
            Expanded(child: Text(state.isArabic ? 'تعلم مع علي • خريطة الأنظمة' : 'Learn with Ali • Architecture', style: const TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold))),
            _pill(state.isOfflineForced ? 'OFFLINE READY' : 'LOCAL FIRST', const Color(0xFF34D399)),
          ]),
          const SizedBox(height: 10),
          Text(state.isArabic ? 'منهجك وبيانات تقدمك الأساسية متاحة محلياً؛ لا يتطلب التعلم اتصالاً بالشبكة.' : 'Core curriculum and progress stay on-device; study does not require network access.', style: const TextStyle(color: Colors.white, height: 1.5)),
          const SizedBox(height: 12),
          Wrap(spacing: 8, runSpacing: 8, children: [_pill('Flutter / Dart', Colors.white), _pill('Riverpod', Colors.white), _pill('SQLite', Colors.white), _pill('GitHub Actions', Colors.white)]),
        ]),
      );

  Widget _statusTiles(ArchitectureHubState state, Color surface, Color foreground, Color muted) {
    final metrics = state.metrics;
    final error = state.hasError;
    return Row(children: [
      Expanded(child: _tile('Offline', error ? 'Local-only fallback' : (metrics?.offlineReady == false ? 'Check status' : 'Ready'), Icons.offline_pin_rounded, error ? Colors.orange : const Color(0xFF34D399), surface, foreground, muted)),
      const SizedBox(width: 8),
      Expanded(child: _tile('FSRS', metrics?.fsrsReady == true ? 'Ready' : 'Local review', Icons.psychology_rounded, const Color(0xFFA78BFA), surface, foreground, muted)),
      const SizedBox(width: 8),
      Expanded(child: _tile('CI/CD', state.isCiRunning ? 'Simulating' : 'Workflow configured', Icons.rocket_launch_rounded, const Color(0xFFFBBF24), surface, foreground, muted)),
    ]);
  }

  Widget _pillarGrid(int columns, Color surface, Color foreground, Color muted) {
    final pillars = <(String, String, IconData, Color)>[
      ('UI / UX', 'Bento layout • responsive widgets • Arabic RTL', Icons.palette_rounded, const Color(0xFF22D3EE)),
      ('SQLite', 'Indexed chapters • textbooks • progress', Icons.storage_rounded, const Color(0xFF34D399)),
      ('Offline-first', 'Learning remains available without a connection', Icons.wifi_off_rounded, const Color(0xFFA78BFA)),
      ('GitHub CI/CD', 'Analyze • tests • Android artifacts', Icons.integration_instructions_rounded, const Color(0xFFFBBF24)),
    ];
    return GridView.count(
      crossAxisCount: columns,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      crossAxisSpacing: 10,
      mainAxisSpacing: 10,
      childAspectRatio: columns == 1 ? 2.7 : 1.7,
      children: pillars.map((pillar) => _card(surface, child: Column(crossAxisAlignment: CrossAxisAlignment.start, mainAxisAlignment: MainAxisAlignment.center, children: [Icon(pillar.$3, color: pillar.$4), const SizedBox(height: 8), Text(pillar.$1, style: TextStyle(color: foreground, fontWeight: FontWeight.bold)), Text(pillar.$2, maxLines: 2, overflow: TextOverflow.ellipsis, style: TextStyle(color: muted, fontSize: 12))]))).toList(),
    );
  }

  Widget _metricsCard(ArchitectureHubState state, ArchitectureHubNotifier controller, Color surface, Color foreground, Color muted) {
    final metrics = state.metrics;
    final benchmark = state.benchmarkResult;
    return _card(surface, child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      Row(children: [const Icon(Icons.dataset_rounded, color: Color(0xFF34D399)), const SizedBox(width: 8), Expanded(child: Text(state.isArabic ? 'إحصاءات التخزين المحلي' : 'Local storage telemetry', style: TextStyle(color: foreground, fontWeight: FontWeight.bold, fontSize: 16))), IconButton(onPressed: state.isLoading ? null : controller.loadMetrics, icon: const Icon(Icons.refresh_rounded))]),
      if (state.isLoading) const LinearProgressIndicator(minHeight: 2),
      if (metrics != null) Wrap(spacing: 8, runSpacing: 8, children: [_pill('Chapters ${metrics.chapterCount}', const Color(0xFF34D399)), _pill('Textbooks ${metrics.textbookCount}', const Color(0xFF22D3EE)), _pill('Attempts ${metrics.examAttemptCount}', const Color(0xFFA78BFA))]),
      if (state.hasError) Padding(padding: const EdgeInsets.only(top: 8), child: Text('Could not load local metrics: ${state.errorMessage ?? 'Unknown error'}', style: const TextStyle(color: Colors.redAccent))),
      const SizedBox(height: 8),
      Text('Metrics are obtained through the domain repository; no screen performs direct SQL or network access.', style: TextStyle(color: muted, fontSize: 12)),
      const SizedBox(height: 8),
      Wrap(spacing: 8, runSpacing: 8, children: [
        OutlinedButton.icon(onPressed: state.isBenchmarking ? null : controller.runDatabaseBenchmark, icon: state.isBenchmarking ? const SizedBox.square(dimension: 16, child: CircularProgressIndicator(strokeWidth: 2)) : const Icon(Icons.speed_rounded), label: const Text('Benchmark local DB')),
        if (benchmark != null) _pill('Read ${benchmark.averageReadLatencyMs.toStringAsFixed(2)} ms', const Color(0xFF34D399)),
        if (benchmark != null) _pill('Write ${benchmark.averageWriteLatencyMs.toStringAsFixed(2)} ms', const Color(0xFF22D3EE)),
      ]),
    ]));
  }

  Widget _sections(ArchitectureHubState state, Color surface, Color foreground, Color muted) {
    final sections = <(String, String, IconData, Color)>[
      ('UI/UX • واجهة المستخدم', 'Bento cards group related study actions. Layout adapts to viewport size; Flutter Directionality supports Arabic RTL and a light/dark theme.', Icons.design_services_rounded, const Color(0xFF22D3EE)),
      ('Domain • منطق المنهج', 'Entities, use cases, and repository contracts define business behavior without depending on Flutter or SQLite.', Icons.layers_rounded, const Color(0xFFA78BFA)),
      ('Data • البيانات المحلية', 'The data implementation adapts the SQLite data source to the architecture repository contract. The local database is the source for offline curriculum reads.', Icons.storage_rounded, const Color(0xFF34D399)),
      ('Offline state • دون اتصال', 'Local data is authoritative for essential study. Optional sync belongs behind a repository and must not block local reads or writes.', Icons.sync_alt_rounded, const Color(0xFFFBBF24)),
    ];
    return Column(children: sections.map((section) => Card(
      color: surface,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18), side: BorderSide(color: section.$4.withValues(alpha: 0.3))),
      child: ExpansionTile(
        leading: Icon(section.$3, color: section.$4),
        title: Text(section.$1, style: TextStyle(color: foreground, fontWeight: FontWeight.w700)),
        childrenPadding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
        children: [Text(section.$2, style: TextStyle(color: muted, height: 1.5))],
      ),
    )).toList());
  }

  Widget _pipeline(ArchitectureHubState state, ArchitectureHubNotifier controller, Color surface, Color foreground, Color muted) {
    const labels = ['Lint', 'Test', 'Build', 'Release'];
    final done = state.ciStage == CiPipelineStage.completed ? 4 : state.ciStage.index.clamp(0, 4);
    return _card(surface, child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      Row(children: [const Icon(Icons.rocket_launch_rounded, color: Color(0xFFFBBF24)), const SizedBox(width: 8), Expanded(child: Text('GitHub Actions CI/CD', style: TextStyle(color: foreground, fontWeight: FontWeight.bold))), FilledButton(onPressed: state.isCiRunning ? null : controller.triggerCiPipelineSimulation, child: Text(state.isCiRunning ? 'Running' : 'Simulate'))]),
      const SizedBox(height: 12),
      Row(children: List.generate(labels.length, (index) => Expanded(child: Column(children: [Icon(index < done ? Icons.check_circle : Icons.radio_button_unchecked, color: index < done ? const Color(0xFF34D399) : muted), Text(labels[index], style: TextStyle(color: muted, fontSize: 11))])))),
      const SizedBox(height: 8),
      Text(state.ciLog, style: TextStyle(color: muted, fontSize: 12)),
      Text('Simulation only; live run status is available in GitHub Actions.', style: TextStyle(color: muted, fontSize: 11)),
    ]));
  }

  Widget _telemetry(ArchitectureHubState state, ArchitectureHubNotifier controller, Color surface, Color foreground, Color muted) => _card(surface, child: Column(children: [
        SwitchListTile.adaptive(contentPadding: EdgeInsets.zero, title: Text('Offline-ready display', style: TextStyle(color: foreground)), subtitle: Text('Core data remains local regardless of this display preference.', style: TextStyle(color: muted)), value: state.isOfflineForced, onChanged: (_) => controller.toggleOfflineMode()),
        const Divider(height: 1),
        SwitchListTile.adaptive(contentPadding: EdgeInsets.zero, title: Text('Quick telemetry', style: TextStyle(color: foreground)), subtitle: Text('Local diagnostics only; nothing is transmitted.', style: TextStyle(color: muted)), value: state.isTelemetryEnabled, onChanged: (_) => controller.toggleTelemetry()),
        if (state.isTelemetryEnabled) Align(alignment: AlignmentDirectional.centerStart, child: TextButton.icon(onPressed: controller.runDatabaseBenchmark, icon: const Icon(Icons.monitor_heart_outlined), label: const Text('Run diagnostics'))),
      ]));

  Widget _card(Color color, {required Widget child}) => Container(padding: const EdgeInsets.all(16), decoration: BoxDecoration(color: color, borderRadius: BorderRadius.circular(20), border: Border.all(color: const Color(0xFF64748B).withValues(alpha: 0.22))), child: child);

  Widget _tile(String title, String value, IconData icon, Color accent, Color surface, Color foreground, Color muted) => _card(surface, child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [Icon(icon, color: accent), const SizedBox(height: 6), Text(title, style: TextStyle(color: muted, fontSize: 11)), Text(value, maxLines: 1, overflow: TextOverflow.ellipsis, style: TextStyle(color: foreground, fontWeight: FontWeight.bold, fontSize: 12))]));

  Widget _pill(String text, Color color) => Container(padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 5), decoration: BoxDecoration(color: color.withValues(alpha: 0.15), borderRadius: BorderRadius.circular(30)), child: Text(text, style: TextStyle(color: color, fontWeight: FontWeight.w700, fontSize: 10)));
}
