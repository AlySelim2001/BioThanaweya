import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/database/local_database_service.dart';

// ==============================================================================
// 🏛️ STATE MODEL & RIVERPOD NOTIFIER (Architecture Telemetry)
// ==============================================================================

enum CiPipelineStage { idle, linting, testing, building, releasing, completed }

class ArchitectureHubState {
  final bool isArabic;
  final bool isOfflineForced;
  final bool isBenchmarking;
  final bool isCiRunning;
  final CiPipelineStage ciStage;
  final String ciLog;
  final DatabaseBenchmarkResult? benchmarkResult;
  final int activeTab; // 0: Overview, 1: Database Schema, 2: Riverpod State, 3: CI/CD

  const ArchitectureHubState({
    this.isArabic = true,
    this.isOfflineForced = true,
    this.isBenchmarking = false,
    this.isCiRunning = false,
    this.ciStage = CiPipelineStage.idle,
    this.ciLog = 'Pipeline ready for trigger.',
    this.benchmarkResult,
    this.activeTab = 0,
  });

  ArchitectureHubState copyWith({
    bool? isArabic,
    bool? isOfflineForced,
    bool? isBenchmarking,
    bool? isCiRunning,
    CiPipelineStage? ciStage,
    String? ciLog,
    DatabaseBenchmarkResult? benchmarkResult,
    int? activeTab,
  }) {
    return ArchitectureHubState(
      isArabic: isArabic ?? this.isArabic,
      isOfflineForced: isOfflineForced ?? this.isOfflineForced,
      isBenchmarking: isBenchmarking ?? this.isBenchmarking,
      isCiRunning: isCiRunning ?? this.isCiRunning,
      ciStage: ciStage ?? this.ciStage,
      ciLog: ciLog ?? this.ciLog,
      benchmarkResult: benchmarkResult ?? this.benchmarkResult,
      activeTab: activeTab ?? this.activeTab,
    );
  }
}

class ArchitectureHubNotifier extends StateNotifier<ArchitectureHubState> {
  final LocalDatabaseService _dbService;

  ArchitectureHubNotifier(this._dbService) : super(const ArchitectureHubState());

  void toggleLanguage() {
    state = state.copyWith(isArabic: !state.isArabic);
  }

  void toggleOfflineMode() {
    state = state.copyWith(isOfflineForced: !state.isOfflineForced);
  }

  void setActiveTab(int tab) {
    state = state.copyWith(activeTab: tab);
  }

  /// Runs an interactive live benchmark on the local SQLite service
  Future<void> runDatabaseBenchmark() async {
    if (state.isBenchmarking) return;
    state = state.copyWith(isBenchmarking: true);

    try {
      final result = await _dbService.runBenchmark(queryIterations: 60);
      state = state.copyWith(
        isBenchmarking: false,
        benchmarkResult: result,
      );
    } catch (_) {
      state = state.copyWith(isBenchmarking: false);
    }
  }

  /// Simulates a 4-stage GitHub Actions CI/CD pipeline
  Future<void> triggerCiPipelineSimulation() async {
    if (state.isCiRunning) return;

    state = state.copyWith(
      isCiRunning: true,
      ciStage: CiPipelineStage.linting,
      ciLog: '[CI/CD] Step 1/4: Running flutter analyze & strict linter...',
    );
    await Future.delayed(const Duration(milliseconds: 900));

    state = state.copyWith(
      ciStage: CiPipelineStage.testing,
      ciLog: '[CI/CD] Step 2/4: Executing Unit & Widget tests (All 14 suites passed)...',
    );
    await Future.delayed(const Duration(milliseconds: 1000));

    state = state.copyWith(
      ciStage: CiPipelineStage.building,
      ciLog: '[CI/CD] Step 3/4: Compiling Release APK & AAB with ARM64 AOT optimizations...',
    );
    await Future.delayed(const Duration(milliseconds: 1200));

    state = state.copyWith(
      ciStage: CiPipelineStage.releasing,
      ciLog: '[CI/CD] Step 4/4: Deploying artifacts to GitHub Releases (v1.0.0+1)...',
    );
    await Future.delayed(const Duration(milliseconds: 900));

    state = state.copyWith(
      isCiRunning: false,
      ciStage: CiPipelineStage.completed,
      ciLog: '[CI/CD] Build Succeeded! All checks passed, production release verified.',
    );
  }
}

final architectureHubProvider =
    StateNotifierProvider<ArchitectureHubNotifier, ArchitectureHubState>((ref) {
  return ArchitectureHubNotifier(LocalDatabaseService());
});

// ==============================================================================
// 📱 APP ARCHITECTURE HUB (Interactive Bento Grid Screen)
// ==============================================================================

class AppArchitectureHub extends ConsumerWidget {
  const AppArchitectureHub({Key? key}) : super(key: key);

  static const Color bgDark = Color(0xFF0A192F);
  static const Color surfaceCard = Color(0xFF112240);
  static const Color cyanAccent = Color(0xFF64FFDA);
  static const Color amberAccent = Color(0xFFF59E0B);
  static const Color violetAccent = Color(0xFF8B5CF6);
  static const Color emeraldAccent = Color(0xFF10B981);
  static const Color borderSubtle = Color(0xFF233554);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(architectureHubProvider);
    final notifier = ref.read(architectureHubProvider.notifier);
    final isAr = state.isArabic;

    return Directionality(
      textDirection: isAr ? TextDirection.rtl : TextDirection.ltr,
      child: Scaffold(
        backgroundColor: bgDark,
        appBar: AppBar(
          backgroundColor: surfaceCard,
          elevation: 0,
          title: Row(
            children: [
              Container(
                padding: const EdgeInsets.all(6),
                decoration: BoxDecoration(
                  color: cyanAccent.withOpacity(0.15),
                  shape: BoxShape.circle,
                ),
                child: const Icon(Icons.account_tree_rounded, color: cyanAccent, size: 20),
              ),
              const SizedBox(width: 10),
              Text(
                isAr ? 'مركز المعمارية والأنظمة | BioThanaweya' : 'Architecture & Systems Hub',
                style: const TextStyle(color: Colors.white, fontSize: 15, fontWeight: FontWeight.bold),
              ),
            ],
          ),
          actions: [
            // Language Switcher
            TextButton(
              onPressed: () => notifier.toggleLanguage(),
              child: Text(
                isAr ? 'EN' : 'عربي',
                style: const TextStyle(color: cyanAccent, fontWeight: FontWeight.bold),
              ),
            ),
          ],
        ),
        body: SingleChildScrollView(
          physics: const BouncingScrollPhysics(),
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // 1. System Health & Platform Header Bento Card
              _buildSystemHeaderCard(state, notifier),
              const SizedBox(height: 16),

              // 2. Navigation Tabs for Pillars
              _buildPillarTabs(state, notifier),
              const SizedBox(height: 16),

              // 3. Dynamic Pillar Content (Bento Grid)
              if (state.activeTab == 0) ...[
                _buildOverviewBentoGrid(state, notifier),
              ] else if (state.activeTab == 1) ...[
                _buildDatabaseSchemaExplorer(state, notifier),
              ] else if (state.activeTab == 2) ...[
                _buildRiverpodStateExplorer(state),
              ] else ...[
                _buildCiCdPipelineExplorer(state, notifier),
              ],

              const SizedBox(height: 32),
            ],
          ),
        ),
      ),
    );
  }

  // ============================================================================
  // HEADER BENTO CARD (Cross-Platform & Offline Status)
  // ============================================================================
  Widget _buildSystemHeaderCard(ArchitectureHubState state, ArchitectureHubNotifier notifier) {
    final isAr = state.isArabic;

    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(18),
      decoration: BoxDecoration(
        color: surfaceCard,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: cyanAccent.withOpacity(0.3)),
        boxShadow: [
          BoxShadow(
            color: cyanAccent.withOpacity(0.06),
            blurRadius: 18,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Row(
                children: [
                  const Icon(Icons.bolt_rounded, color: cyanAccent, size: 24),
                  const SizedBox(width: 8),
                  Text(
                    isAr ? 'المعمارية النظيفة ومنظومة التشغيل' : 'Clean Architecture & Runtime OS',
                    style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 15),
                  ),
                ],
              ),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                decoration: BoxDecoration(
                  color: emeraldAccent.withOpacity(0.18),
                  borderRadius: BorderRadius.circular(20),
                  border: Border.all(color: emeraldAccent),
                ),
                child: Row(
                  children: [
                    const Icon(Icons.offline_pin, color: emeraldAccent, size: 14),
                    const SizedBox(width: 4),
                    Text(
                      state.isOfflineForced ? (isAr ? '100% أوفلاين' : '100% Offline') : 'Online Sync',
                      style: const TextStyle(color: emeraldAccent, fontSize: 11, fontWeight: FontWeight.bold),
                    ),
                  ],
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          Text(
            isAr
                ? 'تطبيق تعليمي مصري لطلاب الثانوية العامة (دفعة علمي علوم) مبني بـ Flutter Cross-Platform و Dart AOT Native مع قاعدة بيانات SQLite محلية لا تعتمد على السيرفرات.'
                : 'Offline-first educational app for Egyptian Thanaweya Amma students built with Flutter Cross-Platform, Dart AOT, and zero-network SQLite persistence.',
            style: const TextStyle(color: Color(0xFF8892B0), fontSize: 12, height: 1.5),
          ),
          const SizedBox(height: 16),
          Wrap(
            spacing: 8,
            runSpacing: 8,
            children: [
              _buildBadge('Flutter 3.x Native AOT', cyanAccent),
              _buildBadge('Clean Architecture (Data/Domain/UI)', violetAccent),
              _buildBadge('Riverpod StateNotifier', emeraldAccent),
              _buildBadge('SQLite Relational Engine', amberAccent),
            ],
          ),
        ],
      ),
    );
  }

  // ============================================================================
  // PILLAR NAVIGATION TABS
  // ============================================================================
  Widget _buildPillarTabs(ArchitectureHubState state, ArchitectureHubNotifier notifier) {
    final isAr = state.isArabic;
    final tabs = [
      {'title': isAr ? 'نظرة عامة (Overview)' : 'Overview', 'icon': Icons.dashboard_customize_rounded},
      {'title': isAr ? 'قاعدة SQLite (Database)' : 'SQLite DB', 'icon': Icons.storage_rounded},
      {'title': isAr ? 'إدارة الحالة (Riverpod)' : 'State Flow', 'icon': Icons.sync_alt_rounded},
      {'title': isAr ? 'أتمتة النشر (CI/CD)' : 'GitHub CI/CD', 'icon': Icons.rocket_launch_rounded},
    ];

    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: Row(
        children: List.generate(tabs.length, (index) {
          final isSelected = state.activeTab == index;
          return Padding(
            padding: const EdgeInsets.only(left: 8.0),
            child: ChoiceChip(
              avatar: Icon(
                tabs[index]['icon'] as IconData,
                size: 16,
                color: isSelected ? const Color(0xFF0A192F) : cyanAccent,
              ),
              label: Text(tabs[index]['title'] as String),
              selected: isSelected,
              selectedColor: cyanAccent,
              backgroundColor: surfaceCard,
              labelStyle: TextStyle(
                color: isSelected ? const Color(0xFF0A192F) : Colors.white,
                fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                fontSize: 12,
              ),
              side: BorderSide(color: isSelected ? cyanAccent : borderSubtle),
              onSelected: (val) {
                if (val) notifier.setActiveTab(index);
              },
            ),
          );
        }),
      ),
    );
  }

  // ============================================================================
  // TAB 1: OVERVIEW BENTO GRID
  // ============================================================================
  Widget _buildOverviewBentoGrid(ArchitectureHubState state, ArchitectureHubNotifier notifier) {
    final isAr = state.isArabic;

    return Column(
      children: [
        // Top 2 Bento Cards Row
        Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Card 1: Native vs Cross-Platform
            Expanded(
              child: _buildBentoCard(
                title: isAr ? 'الهندسة المتقاطعة (Cross-Platform)' : 'Cross-Platform Engine',
                subtitle: isAr ? 'كود Dart موحد يترجم لـ ARM64 Native للأندرويد و iOS.' : 'Single Dart codebase compiling to native ARM64 binaries.',
                icon: Icons.devices_rounded,
                accentColor: cyanAccent,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _buildMetricRow(isAr ? 'السرعة والإطارات' : 'Frame Rate', '60 / 120 FPS'),
                    _buildMetricRow(isAr ? 'زمن التحديث' : 'Hot Reload', '< 400 ms'),
                    _buildMetricRow(isAr ? 'دعم العربية' : 'RTL Support', '100% Native'),
                  ],
                ),
              ),
            ),
            const SizedBox(width: 12),
            // Card 2: Local SQLite Performance
            Expanded(
              child: _buildBentoCard(
                title: isAr ? 'محرك SQLite المحلي' : 'Offline SQLite Engine',
                subtitle: isAr ? 'تخزين علائقي على الجهاز لـ 11 فصلاً ومحاكي الامتحانات.' : 'On-device relational storage with zero network reliance.',
                icon: Icons.dns_rounded,
                accentColor: emeraldAccent,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _buildMetricRow(isAr ? 'فصول المنهج' : 'Curriculum', '11 Chapters'),
                    _buildMetricRow(isAr ? 'زمن الاستعلام' : 'Query Latency', '< 8 ms'),
                    _buildMetricRow(isAr ? 'التكلفة السحابية' : 'Cloud Cost', '0.00 \$ (Free)'),
                  ],
                ),
              ),
            ),
          ],
        ),
        const SizedBox(height: 12),

        // Live Benchmark Interactive Card
        Container(
          width: double.infinity,
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: surfaceCard,
            borderRadius: BorderRadius.circular(16),
            border: Border.all(color: amberAccent.withOpacity(0.3)),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Row(
                    children: [
                      const Icon(Icons.speed_rounded, color: amberAccent, size: 22),
                      const SizedBox(width: 8),
                      Text(
                        isAr ? 'فحص الأداء المحلي الحي (Live Benchmark)' : 'Live SQLite Query Benchmark',
                        style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14),
                      ),
                    ],
                  ),
                  ElevatedButton.icon(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: amberAccent,
                      foregroundColor: const Color(0xFF0A192F),
                      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                    ),
                    onPressed: state.isBenchmarking ? null : () => notifier.runDatabaseBenchmark(),
                    icon: state.isBenchmarking
                        ? const SizedBox(
                            width: 14,
                            height: 14,
                            child: CircularProgressIndicator(strokeWidth: 2, color: Color(0xFF0A192F)),
                          )
                        : const Icon(Icons.play_arrow_rounded, size: 18),
                    label: Text(
                      isAr ? 'بدء الفحص' : 'Run Benchmark',
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              if (state.benchmarkResult != null) ...[
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    _buildStatPill(
                      label: isAr ? 'زمن القراءة' : 'Read Speed',
                      value: '${state.benchmarkResult!.averageReadLatencyMs} ms',
                      color: emeraldAccent,
                    ),
                    _buildStatPill(
                      label: isAr ? 'زمن الكتابة' : 'Write Speed',
                      value: '${state.benchmarkResult!.averageWriteLatencyMs} ms',
                      color: cyanAccent,
                    ),
                    _buildStatPill(
                      label: isAr ? 'دقة الكاش' : 'Cache Hit',
                      value: '${state.benchmarkResult!.cacheHitRate}%',
                      color: amberAccent,
                    ),
                    _buildStatPill(
                      label: isAr ? 'حجم القاعدة' : 'DB Size',
                      value: '${state.benchmarkResult!.databaseSizeKb} KB',
                      color: violetAccent,
                    ),
                  ],
                ),
              ] else ...[
                Text(
                  isAr
                      ? 'اضغط على "بدء الفحص" لقياس سرعة القراءة والكتابة وعمليات البحث الفورية على قاعدة بيانات الأحياء المحلية.'
                      : 'Tap "Run Benchmark" to execute simulated concurrent queries against the 11 chapters curriculum database.',
                  style: const TextStyle(color: Color(0xFF8892B0), fontSize: 12),
                ),
              ],
            ],
          ),
        ),
      ],
    );
  }

  // ============================================================================
  // TAB 2: DATABASE SCHEMA EXPLORER
  // ============================================================================
  Widget _buildDatabaseSchemaExplorer(ArchitectureHubState state, ArchitectureHubNotifier notifier) {
    final isAr = state.isArabic;

    final tables = [
      {
        'name': 'chapters',
        'desc': isAr ? 'فصول منهج الثانوية العامة الـ 11' : '11 Curriculum Chapters',
        'columns': 'id (PK), unit_number, title_ar, title_en, key_concepts, mastery, cards_due',
      },
      {
        'name': 'topics',
        'desc': isAr ? 'موضوعات الفصول التفصيلية' : 'Granular Topics per Chapter',
        'columns': 'id (PK), chapter_id (FK), title_ar, title_en, summary_markdown',
      },
      {
        'name': 'questions',
        'desc': isAr ? 'بنك الأسئلة المعتمد وفخاخ الامتحانات الوزارية' : 'Standardized Questions & Ministry Traps',
        'columns': 'id (PK), chapter_id (FK), question_text, options_json, correct_index, explanation, is_ministry_trap',
      },
      {
        'name': 'exam_sessions',
        'desc': isAr ? 'جلسات المحاكي الوزاري ودرجات الطالب' : 'Exam Simulator Sessions & Scores',
        'columns': 'id (PK), title, mode, score_percentage, total_questions, elapsed_seconds, started_at',
      },
      {
        'name': 'fsrs_mistakes',
        'desc': isAr ? 'فهرس استدراك الأخطاء ومحرك التكرار المتباعد' : 'FSRS Spaced Repetition Remediation Index',
        'columns': 'question_id (PK), chapter_id, fail_count, consecutive_correct, stability_days, next_review_at, is_resolved',
      },
    ];

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          isAr ? 'جداول قاعدة البيانات العلائقية (Relational SQLite Schema v2):' : 'Relational SQLite Schema v2:',
          style: const TextStyle(color: Colors.white, fontSize: 14, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 12),
        ...tables.map((t) => Container(
              margin: const EdgeInsets.only(bottom: 10),
              padding: const EdgeInsets.all(14),
              decoration: BoxDecoration(
                color: surfaceCard,
                borderRadius: BorderRadius.circular(14),
                border: Border.all(color: borderSubtle),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Row(
                        children: [
                          const Icon(Icons.table_chart_rounded, color: cyanAccent, size: 18),
                          const SizedBox(width: 8),
                          Text(
                            t['name']!,
                            style: const TextStyle(color: cyanAccent, fontWeight: FontWeight.bold, fontSize: 14),
                          ),
                        ],
                      ),
                      Text(t['desc']!, style: const TextStyle(color: Color(0xFF8892B0), fontSize: 11)),
                    ],
                  ),
                  const SizedBox(height: 8),
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: const Color(0xFF0A192F),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Text(
                      t['columns']!,
                      style: const TextStyle(color: Color(0xFFCCD6F6), fontSize: 11, fontFamily: 'monospace'),
                    ),
                  ),
                ],
              ),
            )),
      ],
    );
  }

  // ============================================================================
  // TAB 3: RIVERPOD STATE EXPLORER
  // ============================================================================
  Widget _buildRiverpodStateExplorer(ArchitectureHubState state) {
    final isAr = state.isArabic;

    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: surfaceCard,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: borderSubtle),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              const Icon(Icons.account_tree_rounded, color: violetAccent, size: 22),
              const SizedBox(width: 8),
              Text(
                isAr ? 'شجرة إدارة الحالة عبر Riverpod' : 'Riverpod Reactive State Tree',
                style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14),
              ),
            ],
          ),
          const SizedBox(height: 12),
          Text(
            isAr
                ? 'تعتمد منظومة BioThanaweya على Clean Architecture مع StateNotifier و AsyncValue لضمان فصل كامل بين منطق البيانات وواجهة المستخدم، ومقاومة استنزاف الذاكرة.'
                : 'BioThanaweya uses Riverpod StateNotifiers and immutable state classes to ensure unidirectional data flow, memory safety, and 100% deterministic testability.',
            style: const TextStyle(color: Color(0xFF8892B0), fontSize: 12, height: 1.4),
          ),
          const SizedBox(height: 16),
          _buildStateLayerRow('Presentation Layer', 'ConsumerWidgets, Bento Grids, Arabic RTL', cyanAccent),
          _buildStateLayerRow('Controller Layer', 'QuizSessionController, PlannerController, HubNotifier', violetAccent),
          _buildStateLayerRow('Domain Layer', 'ExamSession, FSRS Engine, Use Cases', amberAccent),
          _buildStateLayerRow('Data Layer', 'LocalDatabaseService, SharedPreferences Cache', emeraldAccent),
        ],
      ),
    );
  }

  // ============================================================================
  // TAB 4: CI/CD PIPELINE EXPLORER
  // ============================================================================
  Widget _buildCiCdPipelineExplorer(ArchitectureHubState state, ArchitectureHubNotifier notifier) {
    final isAr = state.isArabic;

    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: surfaceCard,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: borderSubtle),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Row(
                children: [
                  const Icon(Icons.alt_route_rounded, color: cyanAccent, size: 22),
                  const SizedBox(width: 8),
                  Text(
                    isAr ? 'خط أنابيب GitHub Actions CI/CD' : 'GitHub Actions CI/CD Automation',
                    style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14),
                  ),
                ],
              ),
              ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  backgroundColor: cyanAccent,
                  foregroundColor: const Color(0xFF0A192F),
                  padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                ),
                onPressed: state.isCiRunning ? null : () => notifier.triggerCiPipelineSimulation(),
                icon: state.isCiRunning
                    ? const SizedBox(
                        width: 14,
                        height: 14,
                        child: CircularProgressIndicator(strokeWidth: 2, color: Color(0xFF0A192F)),
                      )
                    : const Icon(Icons.rocket_launch_rounded, size: 16),
                label: Text(
                  isAr ? 'محاكاة النشر' : 'Trigger CI/CD',
                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),
          // 4 Stages Pipeline Display
          Row(
            children: [
              _buildPipelineStep('1. Lint', state.ciStage.index >= 1, state.ciStage == CiPipelineStage.linting),
              _buildPipelineConnector(state.ciStage.index >= 2),
              _buildPipelineStep('2. Test', state.ciStage.index >= 2, state.ciStage == CiPipelineStage.testing),
              _buildPipelineConnector(state.ciStage.index >= 3),
              _buildPipelineStep('3. Build', state.ciStage.index >= 3, state.ciStage == CiPipelineStage.building),
              _buildPipelineConnector(state.ciStage.index >= 4),
              _buildPipelineStep('4. Release', state.ciStage.index >= 4, state.ciStage == CiPipelineStage.releasing),
            ],
          ),
          const SizedBox(height: 16),
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: const Color(0xFF0A192F),
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: borderSubtle),
            ),
            child: Row(
              children: [
                const Icon(Icons.terminal_rounded, color: cyanAccent, size: 18),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    state.ciLog,
                    style: const TextStyle(color: Color(0xFFCCD6F6), fontSize: 11, fontFamily: 'monospace'),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  // ============================================================================
  // HELPER WIDGETS
  // ============================================================================

  Widget _buildBentoCard({
    required String title,
    required String subtitle,
    required IconData icon,
    required Color accentColor,
    required Widget child,
  }) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: surfaceCard,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: accentColor.withOpacity(0.3)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(icon, color: accentColor, size: 18),
              const SizedBox(width: 6),
              Expanded(
                child: Text(
                  title,
                  style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 12),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
            ],
          ),
          const SizedBox(height: 6),
          Text(
            subtitle,
            style: const TextStyle(color: Color(0xFF8892B0), fontSize: 10, height: 1.3),
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
          const SizedBox(height: 10),
          child,
        ],
      ),
    );
  }

  Widget _buildMetricRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 4.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(label, style: const TextStyle(color: Color(0xFF8892B0), fontSize: 10)),
          Text(value, style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 11)),
        ],
      ),
    );
  }

  Widget _buildStatPill({required String label, required String value, required Color color}) {
    return Column(
      children: [
        Text(value, style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 14)),
        const SizedBox(height: 2),
        Text(label, style: const TextStyle(color: Color(0xFF8892B0), fontSize: 10)),
      ],
    );
  }

  Widget _buildBadge(String text, Color color) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
      decoration: BoxDecoration(
        color: color.withOpacity(0.12),
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: color.withOpacity(0.3)),
      ),
      child: Text(text, style: TextStyle(color: color, fontSize: 10, fontWeight: FontWeight.bold)),
    );
  }

  Widget _buildStateLayerRow(String layer, String desc, Color color) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
            decoration: BoxDecoration(
              color: color.withOpacity(0.2),
              borderRadius: BorderRadius.circular(6),
            ),
            child: Text(layer, style: TextStyle(color: color, fontSize: 10, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(width: 10),
          Expanded(
            child: Text(desc, style: const TextStyle(color: Color(0xFFCCD6F6), fontSize: 11)),
          ),
        ],
      ),
    );
  }

  Widget _buildPipelineStep(String label, bool isDone, bool isActive) {
    final color = isDone
        ? emeraldAccent
        : isActive
            ? amberAccent
            : const Color(0xFF8892B0);

    return Expanded(
      child: Column(
        children: [
          Container(
            padding: const EdgeInsets.all(6),
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: color.withOpacity(0.2),
              border: Border.all(color: color),
            ),
            child: Icon(
              isDone ? Icons.check : (isActive ? Icons.sync : Icons.radio_button_unchecked),
              size: 14,
              color: color,
            ),
          ),
          const SizedBox(height: 4),
          Text(label, style: TextStyle(color: color, fontSize: 9, fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }

  Widget _buildPipelineConnector(bool isDone) {
    return Container(
      width: 16,
      height: 2,
      color: isDone ? emeraldAccent : borderSubtle,
      margin: const EdgeInsets.symmetric(horizontal: 2),
    );
  }
}
