import '../../domain/entities/calculator_fsrs_item.dart';
import '../../domain/repositories/biology_calculator_repository.dart';
import '../datasources/local_calculator_storage_source.dart';

/// Concrete implementation of BiologyCalculatorRepository adhering to Clean Architecture.
class BiologyCalculatorRepositoryImpl implements BiologyCalculatorRepository {
  final LocalCalculatorStorageSource _storage;

  BiologyCalculatorRepositoryImpl({LocalCalculatorStorageSource? storage})
      : _storage = storage ?? LocalCalculatorStorageSource();

  @override
  Future<List<CalculatorFsrsItem>> getDueFormulaReviews() async {
    final all = await _storage.getFsrsItems();
    return all.where((item) => item.isDueForReview).toList();
  }

  @override
  Future<List<CalculatorFsrsItem>> getAllFormulaItems() async {
    return await _storage.getFsrsItems();
  }

  @override
  Future<void> recordFormulaAttempt(String formulaId, bool wasCorrect) async {
    final all = await _storage.getFsrsItems();
    final item = all.firstWhere(
      (f) => f.formulaId == formulaId,
      orElse: () => CalculatorFsrsItem(
        formulaId: formulaId,
        category: 'general',
        titleAr: 'قانون بيولوجي',
        formulaPromptAr: 'مراجعة القانون',
        nextReviewDate: DateTime.now(),
      ),
    );

    final updated = item.evaluateAttempt(wasCorrect: wasCorrect);
    await _storage.saveFsrsItem(updated);
  }

  @override
  Future<void> saveCalculationHistory({
    required String type,
    required String summaryAr,
    required Map<String, dynamic> data,
  }) async {
    final record = {
      'id': 'calc_${DateTime.now().millisecondsSinceEpoch}',
      'type': type,
      'summaryAr': summaryAr,
      'timestamp': DateTime.now().toIso8601String(),
      'data': data,
    };
    await _storage.addHistoryRecord(record);
  }

  @override
  Future<List<Map<String, dynamic>>> getCalculationHistory() async {
    return await _storage.getHistory();
  }

  @override
  Future<void> clearHistory() async {
    await _storage.clearHistory();
  }
}
