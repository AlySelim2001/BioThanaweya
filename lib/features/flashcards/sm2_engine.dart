import 'models/flashcard_model.dart';

enum SM2Quality {
  again(1), // Failed (q=1)
  hard(3),  // Difficult (q=3)
  good(4),  // Correct (q=4)
  easy(5);  // Mastered (q=5)

  final int grade;
  const SM2Quality(this.grade);
}

/// Official SuperMemo SM-2 Spaced Repetition calculation engine.
class SM2Engine {
  /// Evaluates review quality and returns updated Flashcard with new interval, ease factor, and review date.
  static Flashcard calculateNextReview(Flashcard card, SM2Quality quality) {
    final int q = quality.grade;
    double ef = card.easeFactor;
    int rep = card.repetitions;
    int interval = card.intervalDays;

    if (q >= 3) {
      // Successful recall
      if (rep == 0) {
        interval = 1;
      } else if (rep == 1) {
        interval = 6;
      } else {
        interval = (interval * ef).round();
      }
      rep += 1;
    } else {
      // Failure / reset
      rep = 0;
      interval = 1;
    }

    // Update Ease Factor (EF)
    ef = ef + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02));
    if (ef < 1.3) ef = 1.3;

    final DateTime nextDate = DateTime.now().add(Duration(days: interval));

    return card.copyWith(
      repetitions: rep,
      easeFactor: ef,
      intervalDays: interval,
      nextReviewDate: nextDate,
    );
  }
}
