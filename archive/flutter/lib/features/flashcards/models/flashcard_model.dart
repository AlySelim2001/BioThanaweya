/// Flashcard data model supporting SuperMemo SM-2 algorithm properties.
class Flashcard {
  final String id;
  final String topicId;
  final String frontQuestion;
  final String backAnswer;
  final String scientificKeywords;
  
  // SM-2 Algorithm State
  final int repetitions;
  final double easeFactor; // Default 2.5
  final int intervalDays;  // Review interval in days
  final DateTime nextReviewDate;

  Flashcard({
    required this.id,
    required this.topicId,
    required this.frontQuestion,
    required this.backAnswer,
    required this.scientificKeywords,
    this.repetitions = 0,
    this.easeFactor = 2.5,
    this.intervalDays = 1,
    DateTime? nextReviewDate,
  }) : nextReviewDate = nextReviewDate ?? DateTime.now();

  Flashcard copyWith({
    String? id,
    String? topicId,
    String? frontQuestion,
    String? backAnswer,
    String? scientificKeywords,
    int? repetitions,
    double? easeFactor,
    int? intervalDays,
    DateTime? nextReviewDate,
  }) {
    return Flashcard(
      id: id ?? this.id,
      topicId: topicId ?? this.topicId,
      frontQuestion: frontQuestion ?? this.frontQuestion,
      backAnswer: backAnswer ?? this.backAnswer,
      scientificKeywords: scientificKeywords ?? this.scientificKeywords,
      repetitions: repetitions ?? this.repetitions,
      easeFactor: easeFactor ?? this.easeFactor,
      intervalDays: intervalDays ?? this.intervalDays,
      nextReviewDate: nextReviewDate ?? this.nextReviewDate,
    );
  }
}
