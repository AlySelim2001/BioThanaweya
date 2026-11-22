import '../entities/calculator_fsrs_item.dart';
import '../entities/dna_calculation_result.dart';
import '../entities/genetics_cross_result.dart';
import '../entities/muscle_energetics_result.dart';

/// Clean Architecture Repository Contract for persisting calculations and FSRS reviews.
abstract class BiologyCalculatorRepository {
  Future<List<CalculatorFsrsItem>> getDueFormulaReviews();
  Future<List<CalculatorFsrsItem>> getAllFormulaItems();
  Future<void> recordFormulaAttempt(String formulaId, bool wasCorrect);
  Future<void> saveCalculationHistory({
    required String type,
    required String summaryAr,
    required Map<String, dynamic> data,
  });
  Future<List<Map<String, dynamic>>> getCalculationHistory();
  Future<void> clearHistory();
}
