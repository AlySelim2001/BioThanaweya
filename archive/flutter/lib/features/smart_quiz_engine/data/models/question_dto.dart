import '../../domain/entities/question.dart';

/// كائن نقل البيانات (Data Transfer Object) للأسئلة مع التحويل من وإلى JSON
class QuestionDto {
  final String id;
  final int chapterId;
  final String chapterTitle;
  final int unitNumber;
  final String questionText;
  final List<String> options;
  final int correctIndex;
  final String explanation;
  final String type;
  final String difficulty;
  final List<String> keywords;
  final bool isMinistryOfficial;

  QuestionDto({
    required this.id,
    required this.chapterId,
    required this.chapterTitle,
    required this.unitNumber,
    required this.questionText,
    required this.options,
    required this.correctIndex,
    required this.explanation,
    required this.type,
    required this.difficulty,
    required this.keywords,
    required this.isMinistryOfficial,
  });

  factory QuestionDto.fromJson(Map<String, dynamic> json) {
    return QuestionDto(
      id: json['id']?.toString() ?? '',
      chapterId: (json['chapterId'] as num?)?.toInt() ?? 1,
      chapterTitle: json['chapterTitle']?.toString() ?? 'فصل دراسي',
      unitNumber: (json['unitNumber'] as num?)?.toInt() ?? 1,
      questionText: json['questionText']?.toString() ?? '',
      options: (json['options'] as List<dynamic>?)
              ?.map((e) => e.toString())
              .toList() ??
          const [],
      correctIndex: (json['correctIndex'] as num?)?.toInt() ?? 0,
      explanation: json['explanation']?.toString() ?? '',
      type: json['type']?.toString() ?? 'mcq',
      difficulty: json['difficulty']?.toString() ?? 'intermediate',
      keywords: (json['keywords'] as List<dynamic>?)
              ?.map((e) => e.toString())
              .toList() ??
          const [],
      isMinistryOfficial: json['isMinistryOfficial'] as bool? ?? true,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'chapterId': chapterId,
        'chapterTitle': chapterTitle,
        'unitNumber': unitNumber,
        'questionText': questionText,
        'options': options,
        'correctIndex': correctIndex,
        'explanation': explanation,
        'type': type,
        'difficulty': difficulty,
        'keywords': keywords,
        'isMinistryOfficial': isMinistryOfficial,
      };

  QuizQuestion toDomain() {
    QuestionType qType;
    switch (type.toLowerCase()) {
      case 'scientificterm':
        qType = QuestionType.scientificTerm;
        break;
      case 'givereason':
        qType = QuestionType.giveReason;
        break;
      case 'diagramanalysis':
        qType = QuestionType.diagramAnalysis;
        break;
      case 'ministrytrap':
        qType = QuestionType.ministryTrap;
        break;
      case 'mcq':
      default:
        qType = QuestionType.mcq;
    }

    QuizDifficulty qDiff;
    switch (difficulty.toLowerCase()) {
      case 'foundational':
        qDiff = QuizDifficulty.foundational;
        break;
      case 'advanced':
        qDiff = QuizDifficulty.advanced;
        break;
      case 'ministrytrap':
        qDiff = QuizDifficulty.ministryTrap;
        break;
      case 'intermediate':
      default:
        qDiff = QuizDifficulty.intermediate;
    }

    return QuizQuestion(
      id: id,
      chapterId: chapterId,
      chapterTitle: chapterTitle,
      unitNumber: unitNumber,
      questionText: questionText,
      options: options,
      correctIndex: correctIndex,
      explanation: explanation,
      type: qType,
      difficulty: qDiff,
      keywords: keywords,
      isMinistryOfficial: isMinistryOfficial,
    );
  }

  factory QuestionDto.fromDomain(QuizQuestion q) {
    return QuestionDto(
      id: q.id,
      chapterId: q.chapterId,
      chapterTitle: q.chapterTitle,
      unitNumber: q.unitNumber,
      questionText: q.questionText,
      options: q.options,
      correctIndex: q.correctIndex,
      explanation: q.explanation,
      type: q.type.name,
      difficulty: q.difficulty.name,
      keywords: q.keywords,
      isMinistryOfficial: q.isMinistryOfficial,
    );
  }
}
