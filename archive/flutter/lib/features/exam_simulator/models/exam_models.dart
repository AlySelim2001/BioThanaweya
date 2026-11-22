enum QuestionType {
  scientificTerm,
  giveReason,
  whatHappensIf,
  diagramAnalysis,
  mcq
}

class ExamQuestion {
  final String id;
  final String topicId;
  final QuestionType type;
  final String promptArabic;
  final String promptEnglish;
  final String modelAnswerArabic;
  final String modelAnswerEnglish;
  final List<String> requiredKeywords;
  final int maxMarks;

  const ExamQuestion({
    required this.id,
    required this.topicId,
    required this.type,
    required this.promptArabic,
    required this.promptEnglish,
    required this.modelAnswerArabic,
    required this.modelAnswerEnglish,
    required this.requiredKeywords,
    this.maxMarks = 2,
  });
}

class RubricEvaluationResult {
  final double scoreEarned;
  final int maxMarks;
  final double percentage;
  final List<String> matchedKeywords;
  final List<String> missingKeywords;
  final String examinerFeedback;
  final bool fullCreditAwarded;

  const RubricEvaluationResult({
    required this.scoreEarned,
    required this.maxMarks,
    required this.percentage,
    required this.matchedKeywords,
    required this.missingKeywords,
    required this.examinerFeedback,
    required this.fullCreditAwarded,
  });
}
