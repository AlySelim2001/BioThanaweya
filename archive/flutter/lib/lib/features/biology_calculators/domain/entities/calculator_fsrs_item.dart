/// Represents an FSRS spaced repetition item for tracking formula mastery.
class CalculatorFsrsItem {
  final String formulaId;
  final String category; // 'dna', 'genetics', 'sarcomere', 'respiration'
  final String titleAr;
  final String formulaPromptAr;
  final int repetition;
  final double intervalDays;
  final double easeFactor;
  final DateTime nextReviewDate;
  final int totalMistakes;

  const CalculatorFsrsItem({
    required this.formulaId,
    required this.category,
    required this.titleAr,
    required this.formulaPromptAr,
    this.repetition = 0,
    this.intervalDays = 1.0,
    this.easeFactor = 2.5,
    required this.nextReviewDate,
    this.totalMistakes = 0,
  });

  bool get isDueForReview => DateTime.now().isAfter(nextReviewDate);

  CalculatorFsrsItem evaluateAttempt({required bool wasCorrect}) {
    if (wasCorrect) {
      final newRepetition = repetition + 1;
      final newInterval = repetition == 0
          ? 1.0
          : repetition == 1
              ? 6.0
              : intervalDays * easeFactor;
      final newEase = (easeFactor + 0.1).clamp(1.3, 3.0);
      return CalculatorFsrsItem(
        formulaId: formulaId,
        category: category,
        titleAr: titleAr,
        formulaPromptAr: formulaPromptAr,
        repetition: newRepetition,
        intervalDays: newInterval,
        easeFactor: newEase,
        nextReviewDate:
            DateTime.now().add(Duration(days: newInterval.round().clamp(1, 365))),
        totalMistakes: totalMistakes,
      );
    } else {
      return CalculatorFsrsItem(
        formulaId: formulaId,
        category: category,
        titleAr: titleAr,
        formulaPromptAr: formulaPromptAr,
        repetition: 0,
        intervalDays: 1.0,
        easeFactor: (easeFactor - 0.2).clamp(1.3, 3.0),
        nextReviewDate: DateTime.now().add(const Duration(days: 1)),
        totalMistakes: totalMistakes + 1,
      );
    }
  }

  Map<String, dynamic> toMap() => {
        'formulaId': formulaId,
        'category': category,
        'titleAr': titleAr,
        'formulaPromptAr': formulaPromptAr,
        'repetition': repetition,
        'intervalDays': intervalDays,
        'easeFactor': easeFactor,
        'nextReviewDate': nextReviewDate.toIso8601String(),
        'totalMistakes': totalMistakes,
      };

  factory CalculatorFsrsItem.fromMap(Map<String, dynamic> map) =>
      CalculatorFsrsItem(
        formulaId: map['formulaId'] as String,
        category: map['category'] as String,
        titleAr: map['titleAr'] as String,
        formulaPromptAr: map['formulaPromptAr'] as String,
        repetition: (map['repetition'] as num).toInt(),
        intervalDays: (map['intervalDays'] as num).toDouble(),
        easeFactor: (map['easeFactor'] as num).toDouble(),
        nextReviewDate: DateTime.parse(map['nextReviewDate'] as String),
        totalMistakes: (map['totalMistakes'] as num).toInt(),
      );
}
