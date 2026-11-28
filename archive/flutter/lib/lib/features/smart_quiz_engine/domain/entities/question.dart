import 'package:flutter/foundation.dart';

/// نوع السؤال في المنظومة الامتحانية لثانوية عامة
enum QuestionType {
  mcq,
  scientificTerm,
  giveReason,
  diagramAnalysis,
  ministryTrap,
}

/// مستوى الصعوبة التكيفي
enum QuizDifficulty {
  foundational, // تأسيسي
  intermediate, // متوسط
  advanced,     // متقدم / مستويات عليا
  ministryTrap, // فخ وزاري متكرر
}

/// كيان السؤال الأساسي (Domain Question Entity)
@immutable
class QuizQuestion {
  final String id;
  final int chapterId;
  final String chapterTitle;
  final int unitNumber;
  final String questionText;
  final List<String> options;
  final int correctIndex;
  final String explanation;
  final QuestionType type;
  final QuizDifficulty difficulty;
  final List<String> keywords;
  final bool isMinistryOfficial;

  const QuizQuestion({
    required this.id,
    required this.chapterId,
    required this.chapterTitle,
    required this.unitNumber,
    required this.questionText,
    required this.options,
    required this.correctIndex,
    required this.explanation,
    this.type = QuestionType.mcq,
    this.difficulty = QuizDifficulty.intermediate,
    this.keywords = const [],
    this.isMinistryOfficial = true,
  });

  /// التحقق من صحة الخيار المختار
  bool isCorrectChoice(int index) => index == correctIndex;

  /// الخيار النموذجي الصحيح كنص
  String get correctOptionText {
    if (correctIndex >= 0 && correctIndex < options.length) {
      return options[correctIndex];
    }
    return '';
  }

  QuizQuestion copyWith({
    String? id,
    int? chapterId,
    String? chapterTitle,
    int? unitNumber,
    String? questionText,
    List<String>? options,
    int? correctIndex,
    String? explanation,
    QuestionType? type,
    QuizDifficulty? difficulty,
    List<String>? keywords,
    bool? isMinistryOfficial,
  }) {
    return QuizQuestion(
      id: id ?? this.id,
      chapterId: chapterId ?? this.chapterId,
      chapterTitle: chapterTitle ?? this.chapterTitle,
      unitNumber: unitNumber ?? this.unitNumber,
      questionText: questionText ?? this.questionText,
      options: options ?? this.options,
      correctIndex: correctIndex ?? this.correctIndex,
      explanation: explanation ?? this.explanation,
      type: type ?? this.type,
      difficulty: difficulty ?? this.difficulty,
      keywords: keywords ?? this.keywords,
      isMinistryOfficial: isMinistryOfficial ?? this.isMinistryOfficial,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is QuizQuestion &&
          runtimeType == other.runtimeType &&
          id == other.id;

  @override
  int get hashCode => id.hashCode;
}
