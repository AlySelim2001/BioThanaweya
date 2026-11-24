import '../../domain/entities/user_answer.dart';

/// DTO لإجابة الطالب مع التحويل من وإلى JSON
class UserAnswerDto {
  final String questionId;
  final int selectedOptionIndex;
  final bool isCorrect;
  final int timeSpentSeconds;
  final String answeredAtIso;
  final String status;

  UserAnswerDto({
    required this.questionId,
    required this.selectedOptionIndex,
    required this.isCorrect,
    required this.timeSpentSeconds,
    required this.answeredAtIso,
    required this.status,
  });

  factory UserAnswerDto.fromJson(Map<String, dynamic> json) {
    return UserAnswerDto(
      questionId: json['questionId']?.toString() ?? '',
      selectedOptionIndex: (json['selectedOptionIndex'] as num?)?.toInt() ?? -1,
      isCorrect: json['isCorrect'] as bool? ?? false,
      timeSpentSeconds: (json['timeSpentSeconds'] as num?)?.toInt() ?? 0,
      answeredAtIso: json['answeredAtIso']?.toString() ?? DateTime.now().toIso8601String(),
      status: json['status']?.toString() ?? 'correct',
    );
  }

  Map<String, dynamic> toJson() => {
        'questionId': questionId,
        'selectedOptionIndex': selectedOptionIndex,
        'isCorrect': isCorrect,
        'timeSpentSeconds': timeSpentSeconds,
        'answeredAtIso': answeredAtIso,
        'status': status,
      };

  UserAnswer toDomain() {
    AnswerStatus s;
    switch (status.toLowerCase()) {
      case 'incorrect':
        s = AnswerStatus.incorrect;
        break;
      case 'skipped':
        s = AnswerStatus.skipped;
        break;
      case 'correct':
      default:
        s = AnswerStatus.correct;
    }

    return UserAnswer(
      questionId: questionId,
      selectedOptionIndex: selectedOptionIndex,
      isCorrect: isCorrect,
      timeSpentSeconds: timeSpentSeconds,
      answeredAt: DateTime.tryParse(answeredAtIso) ?? DateTime.now(),
      status: s,
    );
  }

  factory UserAnswerDto.fromDomain(UserAnswer ans) {
    return UserAnswerDto(
      questionId: ans.questionId,
      selectedOptionIndex: ans.selectedOptionIndex,
      isCorrect: ans.isCorrect,
      timeSpentSeconds: ans.timeSpentSeconds,
      answeredAtIso: ans.answeredAt.toIso8601String(),
      status: ans.status.name,
    );
  }
}
