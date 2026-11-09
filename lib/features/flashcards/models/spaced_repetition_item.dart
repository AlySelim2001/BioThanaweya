class SpacedRepetitionItem {
  final String cardId;
  int repetition;
  double intervalDays;
  double easeFactor;

  SpacedRepetitionItem({
    required this.cardId,
    this.repetition = 0,
    this.intervalDays = 1.0,
    this.easeFactor = 2.5,
  });

  /// تحديث حالة البطاقة بناءً على تقييم الطالب (من 0 إلى 5)
  void evaluateResponse(int grade) {
    if (grade >= 3) {
      if (repetition == 0) {
        intervalDays = 1.0;
      } else if (repetition == 1) {
        intervalDays = 6.0;
      } else {
        intervalDays = intervalDays * easeFactor;
      }
      repetition++;
    } else {
      repetition = 0;
      intervalDays = 1.0;
    }

    // تحديث معامل السهولة (Ease Factor)
    easeFactor = easeFactor + (0.1 - (5 - grade) * (0.08 + (5 - grade) * 0.02));
    if (easeFactor < 1.3) easeFactor = 1.3;
  }

  Map<String, dynamic> toJson() => {
        'cardId': cardId,
        'repetition': repetition,
        'intervalDays': intervalDays,
        'easeFactor': easeFactor,
      };

  factory SpacedRepetitionItem.fromJson(Map<String, dynamic> json) =>
      SpacedRepetitionItem(
        cardId: json['cardId'] as String,
        repetition: json['repetition'] as int? ?? 0,
        intervalDays: (json['intervalDays'] as num?)?.toDouble() ?? 1.0,
        easeFactor: (json['easeFactor'] as num?)?.toDouble() ?? 2.5,
      );
}
