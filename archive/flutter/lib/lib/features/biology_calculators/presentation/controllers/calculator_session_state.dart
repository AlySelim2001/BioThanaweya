import '../../domain/entities/calculator_fsrs_item.dart';
import '../../domain/entities/dna_calculation_result.dart';
import '../../domain/entities/genetics_cross_result.dart';
import '../../domain/entities/muscle_energetics_result.dart';

enum CalculatorTab { dna, genetics, sarcomere, respiration, fsrsQueue }

/// Immutable state for the Biology Calculators Bento Suite
class CalculatorSessionState {
  final CalculatorTab activeTab;
  final DnaCalculationResult? dnaResult;
  final GeneticsCrossResult? geneticsResult;
  final SarcomereCalculationResult? sarcomereResult;
  final AtpEnergeticsResult? atpResult;
  final List<CalculatorFsrsItem> dueFsrsItems;
  final List<CalculatorFsrsItem> allFsrsItems;
  final List<Map<String, dynamic>> calculationHistory;
  final bool isLoading;
  final String? errorMessage;

  const CalculatorSessionState({
    this.activeTab = CalculatorTab.dna,
    this.dnaResult,
    this.geneticsResult,
    this.sarcomereResult,
    this.atpResult,
    this.dueFsrsItems = const [],
    this.allFsrsItems = const [],
    this.calculationHistory = const [],
    this.isLoading = false,
    this.errorMessage,
  });

  CalculatorSessionState copyWith({
    CalculatorTab? activeTab,
    DnaCalculationResult? dnaResult,
    GeneticsCrossResult? geneticsResult,
    SarcomereCalculationResult? sarcomereResult,
    AtpEnergeticsResult? atpResult,
    List<CalculatorFsrsItem>? dueFsrsItems,
    List<CalculatorFsrsItem>? allFsrsItems,
    List<Map<String, dynamic>>? calculationHistory,
    bool? isLoading,
    String? errorMessage,
    bool clearError = false,
  }) {
    return CalculatorSessionState(
      activeTab: activeTab ?? this.activeTab,
      dnaResult: dnaResult ?? this.dnaResult,
      geneticsResult: geneticsResult ?? this.geneticsResult,
      sarcomereResult: sarcomereResult ?? this.sarcomereResult,
      atpResult: atpResult ?? this.atpResult,
      dueFsrsItems: dueFsrsItems ?? this.dueFsrsItems,
      allFsrsItems: allFsrsItems ?? this.allFsrsItems,
      calculationHistory: calculationHistory ?? this.calculationHistory,
      isLoading: isLoading ?? this.isLoading,
      errorMessage: clearError ? null : (errorMessage ?? this.errorMessage),
    );
  }
}
