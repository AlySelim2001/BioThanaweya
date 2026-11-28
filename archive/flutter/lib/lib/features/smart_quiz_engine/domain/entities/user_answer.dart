import 'package:flutter/foundation.dart';

/// حالة تقييم إجابة الطالب
enum AnswerStatus {
  correct,
  incorrect,
  skipped,
}

/// كيان إجابة الطالب عن سؤال محدد
@immutable
class UserAnswer {
  final String questionId;
  final int selectedOptionIndex;
  final bool isCorrect;
  final int timeSpentSeconds;
  final DateTime answeredAt;
  final AnswerStatus status;

  const UserAnswer({
    required this.questionId,
    required this.selectedOptionIndex,
    required this.isCorrect,
    this.timeSpentSeconds = 0,
    required this.answeredAt,
    this.status = AnswerStatus.correct,
  });

  UserAnswer copyWith({
    String? questionId,
    int? selectedOptionIndex,
    bool? isCorrect,
    int? timeSpentSeconds,
    DateTime? answeredAt,
    AnswerStatus? status,
  }) {
    return UserAnswer(
      questionId: questionId ?? this.questionId,
      selectedOptionIndex: selectedOptionIndex ?? this.selectedOptionIndex,
      isCorrect: isCorrect ?? this.isCorrect,
      timeSpentSeconds: timeSpentSeconds ?? this.timeSpentSeconds,
      answeredAt: answeredAt ?? this.answeredAt,
      status: status ?? this.status,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is UserAnswer &&
          runtimeType == other.runtimeType &&
          questionId == other.questionId &&
          selectedOptionIndex == other.selectedOptionIndex;

  @override
  int get hashCode => questionId.hashCode ^ selectedOptionIndex.hashCode;
}
