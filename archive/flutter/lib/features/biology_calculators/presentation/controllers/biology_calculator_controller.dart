import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../domain/engines/dna_calculator_engine.dart';
import '../../domain/engines/genetics_inheritance_engine.dart';
import '../../domain/engines/muscle_energetics_engine.dart';
import '../../domain/repositories/biology_calculator_repository.dart';
import '../../data/repositories/biology_calculator_repository_impl.dart';
import 'calculator_session_state.dart';

/// Riverpod StateNotifier for coordinating reactive biology calculations and FSRS spaced repetition
class BiologyCalculatorController extends StateNotifier<CalculatorSessionState> {
  final BiologyCalculatorRepository _repository;

  BiologyCalculatorController({
    BiologyCalculatorRepository? repository,
    CalculatorSessionState? initialState,
  })  : _repository = repository ?? BiologyCalculatorRepositoryImpl(),
        super(initialState ?? const CalculatorSessionState()) {
    initialize();
  }

  Future<void> initialize() async {
    // Generate initial calculations so UI is immediately rich with realistic biology data
    calculateDna(baseName: 'A', percent: 28.0, totalNucleotides: 1000);
    calculateMonohybrid(parent1: 'Aa', parent2: 'Aa');
    calculateSarcomere(sarcomeres: 5, isFullyContracted: false);
    calculateAtp(glucoseMolecules: 1);
    await loadFsrsItems();
    await loadHistory();
  }

  void setActiveTab(CalculatorTab tab) {
    state = state.copyWith(activeTab: tab, clearError: true);
  }

  void calculateDna({
    required String baseName,
    required double percent,
    int totalNucleotides = 1000,
  }) {
    try {
      final result = DnaCalculatorEngine.calculateFromBasePercent(
        baseName: baseName,
        percent: percent,
        totalNucleotides: totalNucleotides,
      );
      state = state.copyWith(dnaResult: result, clearError: true);

      _repository.saveCalculationHistory(
        type: 'DNA',
        summaryAr: 'نسبة القاعدة ${result.baseName}: ${percent.toStringAsFixed(1)}% → الروابط: ${result.hydrogenBondsTotal}',
        data: result.toMap(),
      );
    } catch (e) {
      state = state.copyWith(errorMessage: e.toString());
    }
  }

  void calculateMonohybrid({
    required String parent1,
    required String parent2,
    bool isIncompleteDominance = false,
    bool isDominantLethal = false,
    bool isRecessiveLethal = false,
  }) {
    try {
      final result = GeneticsInheritanceEngine.crossMonohybrid(
        parent1: parent1,
        parent2: parent2,
        isIncompleteDominance: isIncompleteDominance,
        isDominantLethal: isDominantLethal,
        isRecessiveLethal: isRecessiveLethal,
      );
      state = state.copyWith(geneticsResult: result, clearError: true);

      _repository.saveCalculationHistory(
        type: 'GENETICS_MONOHYBRID',
        summaryAr: 'تهجين أحادي ($parent1 × $parent2) → ${result.ratioSummary}',
        data: result.toMap(),
      );
    } catch (e) {
      state = state.copyWith(errorMessage: e.toString());
    }
  }

  void calculateDihybrid({
    required String parent1,
    required String parent2,
  }) {
    try {
      final result = GeneticsInheritanceEngine.crossDihybrid(
        parent1: parent1,
        parent2: parent2,
      );
      state = state.copyWith(geneticsResult: result, clearError: true);

      _repository.saveCalculationHistory(
        type: 'GENETICS_DIHYBRID',
        summaryAr: 'تهجين ثنائي ($parent1 × $parent2) → 9:3:3:1',
        data: result.toMap(),
      );
    } catch (e) {
      state = state.copyWith(errorMessage: e.toString());
    }
  }

  void calculateSarcomere({
    required int sarcomeres,
    required bool isFullyContracted,
  }) {
    try {
      final result = MuscleEnergeticsEngine.calculateSarcomere(
        sarcomeres: sarcomeres,
        isFullyContracted: isFullyContracted,
      );
      state = state.copyWith(sarcomereResult: result, clearError: true);

      _repository.saveCalculationHistory(
        type: 'SARCOMERE',
        summaryAr: '$sarcomeres قطع عضلية → ${result.zLinesCount} خطوط Z (الانقباض: ${isFullyContracted ? "تام" : "انبساط"})',
        data: result.toMap(),
      );
    } catch (e) {
      state = state.copyWith(errorMessage: e.toString());
    }
  }

  void calculateAtp({int glucoseMolecules = 1}) {
    try {
      final result = MuscleEnergeticsEngine.calculateCellularAtp(
        glucoseMolecules: glucoseMolecules,
      );
      state = state.copyWith(atpResult: result, clearError: true);

      _repository.saveCalculationHistory(
        type: 'ATP_ENERGETICS',
        summaryAr: '$glucoseMolecules جزيء جلوكوز → ${result.totalNetAtp} ATP صافي',
        data: result.toMap(),
      );
    } catch (e) {
      state = state.copyWith(errorMessage: e.toString());
    }
  }

  Future<void> loadFsrsItems() async {
    final due = await _repository.getDueFormulaReviews();
    final all = await _repository.getAllFormulaItems();
    state = state.copyWith(dueFsrsItems: due, allFsrsItems: all);
  }

  Future<void> submitFsrsAttempt({
    required String formulaId,
    required bool wasCorrect,
  }) async {
    await _repository.recordFormulaAttempt(formulaId, wasCorrect);
    await loadFsrsItems();
  }

  Future<void> loadHistory() async {
    final hist = await _repository.getCalculationHistory();
    state = state.copyWith(calculationHistory: hist);
  }

  Future<void> clearHistory() async {
    await _repository.clearHistory();
    state = state.copyWith(calculationHistory: const []);
  }
}
