import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../domain/entities/calculator_fsrs_item.dart';
import '../../domain/entities/dna_calculation_result.dart';
import '../../domain/entities/genetics_cross_result.dart';
import '../../domain/entities/muscle_energetics_result.dart';
import '../controllers/biology_calculator_controller.dart';
import '../controllers/calculator_providers.dart';
import '../controllers/calculator_session_state.dart';
import '../widgets/punnett_matrix_table.dart';
import '../widgets/sarcomere_diagram_view.dart';

/// Bento Grid screen for Egyptian Biology Calculators & FSRS formula memory drills.
class BiologyCalculatorsBentoScreen extends ConsumerStatefulWidget {
  const BiologyCalculatorsBentoScreen({Key? key}) : super(key: key);

  @override
  ConsumerState<BiologyCalculatorsBentoScreen> createState() =>
      _BiologyCalculatorsBentoScreenState();
}

class _BiologyCalculatorsBentoScreenState
    extends ConsumerState<BiologyCalculatorsBentoScreen> {
  // DNA form state
  String _selectedDnaBase = 'A';
  final TextEditingController _dnaPercentController =
      TextEditingController(text: '28.0');
  final TextEditingController _dnaTotalController =
      TextEditingController(text: '1000');

  // Genetics form state
  final TextEditingController _parent1Controller =
      TextEditingController(text: 'Aa');
  final TextEditingController _parent2Controller =
      TextEditingController(text: 'Aa');
  bool _isIncompleteDominance = false;
  bool _isLethalCross = false;
  bool _isDihybridMode = false;

  // Sarcomere form state
  int _sarcomereCount = 5;
  bool _isContracted = false;

  // Respiration form state
  int _glucoseCount = 1;

  @override
  void dispose() {
    _dnaPercentController.dispose();
    _dnaTotalController.dispose();
    _parent1Controller.dispose();
    _parent2Controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(biologyCalculatorControllerProvider);
    final controller = ref.read(biologyCalculatorControllerProvider.notifier);

    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        backgroundColor: const Color(0xFF0A192F),
        appBar: AppBar(
          backgroundColor: const Color(0xFF112240),
          elevation: 0,
          title: const Text(
            'معمل القوانين والحسابات الحيوية',
            style: TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.bold,
              fontSize: 18,
            ),
          ),
          actions: [
            IconButton(
              icon: const Icon(Icons.history_rounded, color: Color(0xFF64FFDA)),
              tooltip: 'سجل العمليات الحسابية',
              onPressed: () => _showHistorySheet(context, state, controller),
            ),
          ],
        ),
        body: SafeArea(
          child: Column(
            children: [
              _buildCategoryTabBar(state, controller),
              if (state.errorMessage != null)
                _buildErrorBanner(state.errorMessage!),
              Expanded(
                child: ListView(
                  padding: const EdgeInsets.all(16),
                  children: [
                    if (state.activeTab == CalculatorTab.dna)
                      _buildDnaBentoSection(state, controller)
                    else if (state.activeTab == CalculatorTab.genetics)
                      _buildGeneticsBentoSection(state, controller)
                    else if (state.activeTab == CalculatorTab.sarcomere)
                      _buildSarcomereBentoSection(state, controller)
                    else if (state.activeTab == CalculatorTab.respiration)
                      _buildRespirationBentoSection(state, controller)
                    else if (state.activeTab == CalculatorTab.fsrsQueue)
                      _buildFsrsQueueSection(state, controller),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildCategoryTabBar(
    CalculatorSessionState state,
    BiologyCalculatorController controller,
  ) {
    final tabs = [
      (CalculatorTab.dna, 'DNA وشارجاف', Icons.biotech_rounded),
      (CalculatorTab.genetics, 'مربع بانيت', Icons.grid_4x4_rounded),
      (CalculatorTab.sarcomere, 'القطع العضلية', Icons.linear_scale_rounded),
      (CalculatorTab.respiration, 'طاقة ATP', Icons.bolt_rounded),
      (CalculatorTab.fsrsQueue, 'مراجعات FSRS', Icons.psychology_rounded),
    ];

    return Container(
      color: const Color(0xFF112240),
      height: 52,
      child: ListView.separated(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
        scrollDirection: Axis.horizontal,
        itemCount: tabs.length,
        separatorBuilder: (_, __) => const SizedBox(width: 8),
        itemBuilder: (context, index) {
          final tab = tabs[index];
          final isSelected = state.activeTab == tab.$1;
          final badgeCount = tab.$1 == CalculatorTab.fsrsQueue
              ? state.dueFsrsItems.length
              : 0;

          return InkWell(
            onTap: () => controller.setActiveTab(tab.$1),
            borderRadius: BorderRadius.circular(20),
            child: Container(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
              decoration: BoxDecoration(
                color: isSelected
                    ? const Color(0xFF64FFDA).withValues(alpha: 0.15)
                    : const Color(0xFF0F172A),
                borderRadius: BorderRadius.circular(20),
                border: Border.all(
                  color: isSelected
                      ? const Color(0xFF64FFDA)
                      : const Color(0xFF334155),
                ),
              ),
              child: Row(
                children: [
                  Icon(
                    tab.$3,
                    size: 16,
                    color: isSelected
                        ? const Color(0xFF64FFDA)
                        : const Color(0xFF94A3B8),
                  ),
                  const SizedBox(width: 6),
                  Text(
                    tab.$2,
                    style: TextStyle(
                      color: isSelected ? const Color(0xFF64FFDA) : Colors.white,
                      fontSize: 12,
                      fontWeight:
                          isSelected ? FontWeight.bold : FontWeight.normal,
                    ),
                  ),
                  if (badgeCount > 0) ...[
                    const SizedBox(width: 6),
                    Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 6, vertical: 2),
                      decoration: BoxDecoration(
                        color: const Color(0xFFEF4444),
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: Text(
                        '$badgeCount',
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 10,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ],
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildErrorBanner(String message) {
    return Container(
      width: double.infinity,
      color: const Color(0x33EF4444),
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: Row(
        children: [
          const Icon(Icons.error_outline_rounded,
              color: Color(0xFFF87171), size: 18),
          const SizedBox(width: 8),
          Expanded(
            child: Text(
              message,
              style: const TextStyle(color: Color(0xFFFCA5A5), fontSize: 12),
            ),
          ),
        ],
      ),
    );
  }

  // ==========================================
  // DNA & CHARGAFF BENTO SECTION
  // ==========================================
  Widget _buildDnaBentoSection(
    CalculatorSessionState state,
    BiologyCalculatorController controller,
  ) {
    final res = state.dnaResult;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        // Input Bento Card
        _bentoCard(
          title: 'مدخلات تحليل لولب DNA',
          icon: Icons.tune_rounded,
          iconColor: const Color(0xFF64FFDA),
          child: Column(
            children: [
              Row(
                children: [
                  Expanded(
                    flex: 2,
                    child: DropdownButtonFormField<String>(
                      value: _selectedDnaBase,
                      decoration: _inputDecoration('القاعدة النيتروجينية المعلومة'),
                      dropdownColor: const Color(0xFF1E293B),
                      style: const TextStyle(color: Colors.white),
                      items: const [
                        DropdownMenuItem(value: 'A', child: Text('أدينين (Adenine A)')),
                        DropdownMenuItem(value: 'T', child: Text('ثايمين (Thymine T)')),
                        DropdownMenuItem(value: 'G', child: Text('جوانين (Guanine G)')),
                        DropdownMenuItem(value: 'C', child: Text('سيتوزين (Cytosine C)')),
                      ],
                      onChanged: (val) {
                        if (val != null) setState(() => _selectedDnaBase = val);
                      },
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    flex: 1,
                    child: TextField(
                      controller: _dnaPercentController,
                      keyboardType: const TextInputType.numberWithOptions(decimal: true),
                      style: const TextStyle(color: Colors.white),
                      decoration: _inputDecoration('النسبة %'),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              TextField(
                controller: _dnaTotalController,
                keyboardType: TextInputType.number,
                style: const TextStyle(color: Colors.white),
                decoration: _inputDecoration('إجمالي عدد النيوكليوتيدات بالقطعة'),
              ),
              const SizedBox(height: 12),
              ElevatedButton.icon(
                onPressed: () {
                  final p = double.tryParse(_dnaPercentController.text) ?? 25.0;
                  final total = int.tryParse(_dnaTotalController.text) ?? 1000;
                  controller.calculateDna(
                    baseName: _selectedDnaBase,
                    percent: p,
                    totalNucleotides: total,
                  );
                },
                icon: const Icon(Icons.calculate_rounded),
                label: const Text('احسب نسب القواعد والروابط واللفات'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFF0D9488),
                  foregroundColor: Colors.white,
                  minimumSize: const Size.fromHeight(48),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
              ),
            ],
          ),
        ),
        const SizedBox(height: 12),

        if (res != null) ...[
          // Metrics Grid Bento
          Row(
            children: [
              Expanded(
                child: _metricTile(
                  'أزواج القواعد',
                  '${res.basePairsCount} زوج',
                  Icons.link_rounded,
                  const Color(0xFF38BDF8),
                ),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: _metricTile(
                  'اللفات التامة',
                  '${res.completeTurnsCount} لفة',
                  Icons.loop_rounded,
                  const Color(0xFFA78BFA),
                ),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: _metricTile(
                  'روابط هيدروجينية',
                  '${res.hydrogenBondsTotal}',
                  Icons.electric_bolt_rounded,
                  const Color(0xFFF59E0B),
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),

          // Chargaff Parity Visual Card
          _bentoCard(
            title: 'توزيع شارجاف (البيورينات = البيريميدينات = 50%)',
            icon: Icons.pie_chart_rounded,
            iconColor: const Color(0xFF38BDF8),
            child: Column(
              children: [
                _barMetric('أدينين (A)', res.adeninePercent, '${res.adenineCount}', const Color(0xFF64FFDA)),
                _barMetric('ثايمين (T)', res.thyminePercent, '${res.thymineCount}', const Color(0xFF2DD4BF)),
                _barMetric('جوانين (G)', res.guaninePercent, '${res.guanineCount}', const Color(0xFFA78BFA)),
                _barMetric('سيتوزين (C)', res.cytosinePercent, '${res.cytosineCount}', const Color(0xFF818CF8)),
              ],
            ),
          ),
          const SizedBox(height: 12),

          // Step by step derivations
          _explanationsCard(res.explanationsAr),
        ],
      ],
    );
  }

  // ==========================================
  // GENETICS PUNNETT BENTO SECTION
  // ==========================================
  Widget _buildGeneticsBentoSection(
    CalculatorSessionState state,
    BiologyCalculatorController controller,
  ) {
    final res = state.geneticsResult;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        _bentoCard(
          title: 'إعدادات التهجين ومربع بانيت',
          icon: Icons.hub_rounded,
          iconColor: const Color(0xFFA78BFA),
          child: Column(
            children: [
              Wrap(
                spacing: 8,
                runSpacing: 8,
                children: [
                  FilterChip(
                    label: const Text('أحادي الهجين'),
                    selected: !_isDihybridMode,
                    onSelected: (val) {
                      setState(() {
                        _isDihybridMode = !val;
                        if (!_isDihybridMode) {
                          _parent1Controller.text = 'Aa';
                          _parent2Controller.text = 'Aa';
                        }
                      });
                    },
                  ),
                  FilterChip(
                    label: const Text('ثنائي الهجين (مندل 2)'),
                    selected: _isDihybridMode,
                    onSelected: (val) {
                      setState(() {
                        _isDihybridMode = val;
                        if (_isDihybridMode) {
                          _parent1Controller.text = 'AaBb';
                          _parent2Controller.text = 'AaBb';
                        }
                      });
                    },
                  ),
                  if (!_isDihybridMode) ...[
                    FilterChip(
                      label: const Text('انعدام سيادة (شاب الليل)'),
                      selected: _isIncompleteDominance,
                      onSelected: (val) => setState(() {
                        _isIncompleteDominance = val;
                        if (val) _isLethalCross = false;
                      }),
                    ),
                    FilterChip(
                      label: const Text('جينات مميتة (2:1)'),
                      selected: _isLethalCross,
                      onSelected: (val) => setState(() {
                        _isLethalCross = val;
                        if (val) _isIncompleteDominance = false;
                      }),
                    ),
                  ],
                ],
              ),
              const SizedBox(height: 12),
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: _parent1Controller,
                      style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                      decoration: _inputDecoration('تركيب الأب (P1)'),
                    ),
                  ),
                  const SizedBox(width: 8),
                  const Text('×', style: TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.bold)),
                  const SizedBox(width: 8),
                  Expanded(
                    child: TextField(
                      controller: _parent2Controller,
                      style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                      decoration: _inputDecoration('تركيب الأم (P2)'),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              ElevatedButton.icon(
                onPressed: () {
                  if (_isDihybridMode) {
                    controller.calculateDihybrid(
                      parent1: _parent1Controller.text,
                      parent2: _parent2Controller.text,
                    );
                  } else {
                    controller.calculateMonohybrid(
                      parent1: _parent1Controller.text,
                      parent2: _parent2Controller.text,
                      isIncompleteDominance: _isIncompleteDominance,
                      isDominantLethal: _isLethalCross,
                    );
                  }
                },
                icon: const Icon(Icons.hub_rounded),
                label: const Text('حل التهجين وتوليد مربع بانيت التفاعلي'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFF7C3AED),
                  foregroundColor: Colors.white,
                  minimumSize: const Size.fromHeight(48),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
              ),
            ],
          ),
        ),
        const SizedBox(height: 12),

        if (res != null) ...[
          PunnettMatrixTable(result: res),
          const SizedBox(height: 12),
          // Ministry Trap Callout
          Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: const Color(0x22F59E0B),
              borderRadius: BorderRadius.circular(14),
              border: Border.all(color: const Color(0xFFF59E0B)),
            ),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Icon(Icons.warning_amber_rounded, color: Color(0xFFF59E0B), size: 22),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    res.ministryExamTrapNote,
                    style: const TextStyle(color: Color(0xFFFDE68A), fontSize: 12, height: 1.4),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 12),
          _explanationsCard(res.explanationsAr),
        ],
      ],
    );
  }

  // ==========================================
  // SARCOMERE MUSCLE BENTO SECTION
  // ==========================================
  Widget _buildSarcomereBentoSection(
    CalculatorSessionState state,
    BiologyCalculatorController controller,
  ) {
    final res = state.sarcomereResult;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        _bentoCard(
          title: 'التحكم في اللييفة والقطع العضلية',
          icon: Icons.fitness_center_rounded,
          iconColor: const Color(0xFFF59E0B),
          child: Column(
            children: [
              Row(
                children: [
                  const Text('عدد القطع العضلية: ', style: TextStyle(color: Colors.white, fontSize: 13)),
                  Text('$_sarcomereCount', style: const TextStyle(color: Color(0xFF64FFDA), fontSize: 16, fontWeight: FontWeight.bold)),
                ],
              ),
              Slider(
                value: _sarcomereCount.toDouble(),
                min: 1,
                max: 15,
                divisions: 14,
                activeColor: const Color(0xFFF59E0B),
                label: '$_sarcomereCount قطع',
                onChanged: (val) {
                  setState(() => _sarcomereCount = val.toInt());
                  controller.calculateSarcomere(
                    sarcomeres: _sarcomereCount,
                    isFullyContracted: _isContracted,
                  );
                },
              ),
              SwitchListTile.adaptive(
                contentPadding: EdgeInsets.zero,
                title: const Text('حالة انقباض تام (تداخل الأكتين واختفاء H)', style: TextStyle(color: Colors.white, fontSize: 13)),
                value: _isContracted,
                activeColor: const Color(0xFFEF4444),
                onChanged: (val) {
                  setState(() => _isContracted = val);
                  controller.calculateSarcomere(
                    sarcomeres: _sarcomereCount,
                    isFullyContracted: _isContracted,
                  );
                },
              ),
            ],
          ),
        ),
        const SizedBox(height: 12),

        if (res != null) ...[
          SarcomereDiagramView(result: res),
          const SizedBox(height: 12),
          // Metric Cards
          Row(
            children: [
              Expanded(child: _metricTile('خطوط Z', '${res.zLinesCount}', Icons.straighten_rounded, const Color(0xFFEF4444))),
              const SizedBox(width: 8),
              Expanded(child: _metricTile('المناطق الداكنة A', '${res.aBandsCount}', Icons.view_column_rounded, const Color(0xFFF59E0B))),
              const SizedBox(width: 8),
              Expanded(child: _metricTile('المضيئة الكاملة I', '${res.completeIBandsCount}', Icons.wb_sunny_outlined, const Color(0xFF38BDF8))),
            ],
          ),
          const SizedBox(height: 12),
          _explanationsCard(res.explanationsAr),
        ],
      ],
    );
  }

  // ==========================================
  // CELLULAR RESPIRATION ATP BENTO SECTION
  // ==========================================
  Widget _buildRespirationBentoSection(
    CalculatorSessionState state,
    BiologyCalculatorController controller,
  ) {
    final res = state.atpResult;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        _bentoCard(
          title: 'حاسبة أكسدة الجلوكوز وإنتاج ATP',
          icon: Icons.bolt_rounded,
          iconColor: const Color(0xFFEAB308),
          child: Column(
            children: [
              Row(
                children: [
                  const Text('جزيئات الجلوكوز المؤكسدة: ', style: TextStyle(color: Colors.white, fontSize: 13)),
                  Text('$_glucoseCount', style: const TextStyle(color: Color(0xFFFACC15), fontSize: 16, fontWeight: FontWeight.bold)),
                ],
              ),
              Slider(
                value: _glucoseCount.toDouble(),
                min: 1,
                max: 10,
                divisions: 9,
                activeColor: const Color(0xFFEAB308),
                label: '$_glucoseCount جلوكوز',
                onChanged: (val) {
                  setState(() => _glucoseCount = val.toInt());
                  controller.calculateAtp(glucoseMolecules: _glucoseCount);
                },
              ),
            ],
          ),
        ),
        const SizedBox(height: 12),

        if (res != null) ...[
          Row(
            children: [
              Expanded(
                child: _metricTile('الصافي الهوائي', '${res.totalNetAtp} ATP', Icons.rocket_launch_rounded, const Color(0xFF10B981)),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: _metricTile('اللاهوائي (تخمر)', '${res.anaerobicLacticAtp} ATP', Icons.warning_rounded, const Color(0xFFEF4444)),
              ),
            ],
          ),
          const SizedBox(height: 12),
          _bentoCard(
            title: 'تفصيل مسار تحويل الطاقة',
            icon: Icons.account_tree_rounded,
            iconColor: const Color(0xFF38BDF8),
            child: Column(
              children: [
                _barMetric('انشطار الجلوكوز (مباشر)', 5.2, '${res.glycolysisDirectAtp} ATP', const Color(0xFF38BDF8)),
                _barMetric('دورة كريبس (مباشر)', 5.2, '${res.krebsDirectAtp} ATP', const Color(0xFF818CF8)),
                _barMetric('فسفرة تأكسدية (سلسلة النقل)', 89.6, '${res.oxidativePhosphorylationAtp} ATP', const Color(0xFF10B981)),
              ],
            ),
          ),
          const SizedBox(height: 12),
          _explanationsCard(res.explanationsAr),
        ],
      ],
    );
  }

  // ==========================================
  // FSRS MEMORY REVIEW QUEUE SECTION
  // ==========================================
  Widget _buildFsrsQueueSection(
    CalculatorSessionState state,
    BiologyCalculatorController controller,
  ) {
    final due = state.dueFsrsItems;
    final all = state.allFsrsItems;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        _bentoCard(
          title: 'جدول التكرار المتباعد لقوانين الأحياء (FSRS Engine)',
          icon: Icons.psychology_rounded,
          iconColor: const Color(0xFFA78BFA),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'يقوم خوارزم FSRS بحساب فترات المراجعة المثالية لكل قانون وملاحظة فخ وزاري بناءً على دقة إجاباتك السابقة.',
                style: TextStyle(color: Colors.grey[300], fontSize: 12, height: 1.4),
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  _pill('مستحقة للمراجعة الآن: ${due.length}', const Color(0xFFEF4444)),
                  const SizedBox(width: 8),
                  _pill('إجمالي القوانين المجدولة: ${all.length}', const Color(0xFF38BDF8)),
                ],
              ),
            ],
          ),
        ),
        const SizedBox(height: 12),

        if (due.isEmpty)
          Container(
            padding: const EdgeInsets.all(24),
            decoration: BoxDecoration(
              color: const Color(0xFF112240),
              borderRadius: BorderRadius.circular(16),
            ),
            child: Column(
              children: const [
                Icon(Icons.check_circle_outline_rounded, color: Color(0xFF10B981), size: 48),
                SizedBox(height: 8),
                Text('رائع! لا توجد قوانين مستحقة للمراجعة حالياً.', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                SizedBox(height: 4),
                Text('تم تثبيت القوانين في الذاكرة طويلة المدى بنجاح.', style: TextStyle(color: Color(0xFF94A3B8), fontSize: 12)),
              ],
            ),
          )
        else
          for (final item in due)
            _fsrsReviewCard(item, controller),
      ],
    );
  }

  Widget _fsrsReviewCard(
    CalculatorFsrsItem item,
    BiologyCalculatorController controller,
  ) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFF112240),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: const Color(0xFF334155)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                decoration: BoxDecoration(
                  color: const Color(0x33A78BFA),
                  borderRadius: BorderRadius.circular(6),
                ),
                child: Text(
                  item.category.toUpperCase(),
                  style: const TextStyle(color: Color(0xFFA78BFA), fontSize: 10, fontWeight: FontWeight.bold),
                ),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Text(
                  item.titleAr,
                  style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14),
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          Text(
            item.formulaPromptAr,
            style: const TextStyle(color: Color(0xFFCBD5E1), fontSize: 13, height: 1.4),
          ),
          const SizedBox(height: 12),
          Row(
            children: [
              Expanded(
                child: OutlinedButton.icon(
                  onPressed: () => controller.submitFsrsAttempt(
                    formulaId: item.formulaId,
                    wasCorrect: false,
                  ),
                  icon: const Icon(Icons.close_rounded, color: Color(0xFFEF4444), size: 16),
                  label: const Text('أحتاج تدريباً', style: TextStyle(color: Color(0xFFEF4444), fontSize: 12)),
                  style: OutlinedButton.styleFrom(
                    side: const BorderSide(color: Color(0xFFEF4444)),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                  ),
                ),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: ElevatedButton.icon(
                  onPressed: () => controller.submitFsrsAttempt(
                    formulaId: item.formulaId,
                    wasCorrect: true,
                  ),
                  icon: const Icon(Icons.check_rounded, color: Colors.white, size: 16),
                  label: const Text('أتقنت القانون', style: TextStyle(color: Colors.white, fontSize: 12)),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFF10B981),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                  ),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  // ==========================================
  // SHARED BENTO COMPONENTS
  // ==========================================
  Widget _bentoCard({
    required String title,
    required IconData icon,
    required Color iconColor,
    required Widget child,
  }) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFF112240),
        borderRadius: BorderRadius.circular(18),
        border: Border.all(color: const Color(0xFF233554)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Row(
            children: [
              Icon(icon, color: iconColor, size: 18),
              const SizedBox(width: 8),
              Expanded(
                child: Text(
                  title,
                  style: const TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                    fontSize: 14,
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          child,
        ],
      ),
    );
  }

  Widget _metricTile(String label, String value, IconData icon, Color color) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: const Color(0xFF112240),
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: color.withValues(alpha: 0.3)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, color: color, size: 18),
          const SizedBox(height: 6),
          Text(label, style: const TextStyle(color: Color(0xFF94A3B8), fontSize: 11)),
          const SizedBox(height: 2),
          Text(
            value,
            style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 14),
          ),
        ],
      ),
    );
  }

  Widget _barMetric(String name, double percent, String count, Color color) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(name, style: const TextStyle(color: Colors.white, fontSize: 12)),
              Text('$count (${percent.toStringAsFixed(1)}%)',
                  style: TextStyle(color: color, fontSize: 12, fontWeight: FontWeight.bold)),
            ],
          ),
          const SizedBox(height: 4),
          LinearProgressIndicator(
            value: (percent / 100.0).clamp(0.0, 1.0),
            backgroundColor: const Color(0xFF1E293B),
            color: color,
            minHeight: 6,
            borderRadius: BorderRadius.circular(4),
          ),
        ],
      ),
    );
  }

  Widget _explanationsCard(List<String> explanations) {
    return _bentoCard(
      title: 'خطوات الاستنتاج الرياضي وفخاخ الامتحان',
      icon: Icons.lightbulb_outline_rounded,
      iconColor: const Color(0xFFFBBF24),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          for (int i = 0; i < explanations.length; i++)
            Padding(
              padding: const EdgeInsets.only(bottom: 8),
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    width: 20,
                    height: 20,
                    margin: const EdgeInsets.only(left: 8),
                    decoration: BoxDecoration(
                      color: const Color(0xFF1E293B),
                      shape: BoxShape.circle,
                      border: Border.all(color: const Color(0xFF64FFDA)),
                    ),
                    child: Center(
                      child: Text(
                        '${i + 1}',
                        style: const TextStyle(color: Color(0xFF64FFDA), fontSize: 10, fontWeight: FontWeight.bold),
                      ),
                    ),
                  ),
                  Expanded(
                    child: Text(
                      explanations[i],
                      style: const TextStyle(color: Color(0xFFE2E8F0), fontSize: 12, height: 1.4),
                    ),
                  ),
                ],
              ),
            ),
        ],
      ),
    );
  }

  Widget _pill(String text, Color color) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.15),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: color.withValues(alpha: 0.4)),
      ),
      child: Text(text, style: TextStyle(color: color, fontSize: 11, fontWeight: FontWeight.bold)),
    );
  }

  InputDecoration _inputDecoration(String label) {
    return InputDecoration(
      labelText: label,
      labelStyle: const TextStyle(color: Color(0xFF94A3B8), fontSize: 12),
      filled: true,
      fillColor: const Color(0xFF1E293B),
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(10),
        borderSide: const BorderSide(color: Color(0xFF475569)),
      ),
      enabledBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(10),
        borderSide: const BorderSide(color: Color(0xFF475569)),
      ),
      focusedBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(10),
        borderSide: const BorderSide(color: Color(0xFF64FFDA)),
      ),
      contentPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
    );
  }

  void _showHistorySheet(
    BuildContext context,
    CalculatorSessionState state,
    BiologyCalculatorController controller,
  ) {
    showModalBottomSheet(
      context: context,
      backgroundColor: const Color(0xFF0F172A),
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (context) {
        return Directionality(
          textDirection: TextDirection.rtl,
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      'سجل العمليات الحسابية السابقة',
                      style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16),
                    ),
                    TextButton(
                      onPressed: () {
                        controller.clearHistory();
                        Navigator.pop(context);
                      },
                      child: const Text('مسح السجل', style: TextStyle(color: Color(0xFFEF4444))),
                    ),
                  ],
                ),
                const Divider(color: Color(0xFF334155)),
                Expanded(
                  child: state.calculationHistory.isEmpty
                      ? const Center(
                          child: Text(
                            'لا توجد عمليات سابقة مسجلة.',
                            style: TextStyle(color: Color(0xFF94A3B8)),
                          ),
                        )
                      : ListView.separated(
                          itemCount: state.calculationHistory.length,
                          separatorBuilder: (_, __) => const Divider(color: Color(0xFF1E293B)),
                          itemBuilder: (context, index) {
                            final h = state.calculationHistory[index];
                            return ListTile(
                              contentPadding: EdgeInsets.zero,
                              leading: const Icon(Icons.calculate_outlined, color: Color(0xFF64FFDA)),
                              title: Text(
                                h['summaryAr'] as String? ?? 'عملية حسابية',
                                style: const TextStyle(color: Colors.white, fontSize: 13),
                              ),
                              subtitle: Text(
                                (h['timestamp'] as String? ?? '').split('T').first,
                                style: const TextStyle(color: Color(0xFF64748B), fontSize: 11),
                              ),
                            );
                          },
                        ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
