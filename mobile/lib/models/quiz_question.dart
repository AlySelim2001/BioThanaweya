/// A simple multiple-choice quiz question.
class QuizQuestion {
  /// Creates a [QuizQuestion].
  const QuizQuestion({
    required this.question,
    required this.options,
    required this.correctIndex,
  });

  /// The question text.
  final String question;

  /// The list of answer options.
  final List<String> options;

  /// The index of the correct option in [options].
  final int correctIndex;
}
