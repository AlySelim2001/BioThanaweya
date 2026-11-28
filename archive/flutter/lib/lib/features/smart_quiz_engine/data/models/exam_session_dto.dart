import '../../domain/entities/exam_session.dart';
import '../../domain/entities/user_answer.dart';
import 'question_dto.dart';
import 'user_answer_dto.dart';

/// DTO لجلسة الاختبار وحفظها بصيغة JSON محلياً
class ExamSessionDto {
  final String id;
  final String mode;
  final int? chapterId;
  final String title;
  final List<QuestionDto> questions;
  final Map<String, UserAnswerDto> userAnswers;
  final String startedAtIso;
  final String? completedAtIso;
  final int allottedSeconds;
  final int elapsedSeconds;

  ExamSessionDto({
    required this.id,
    required this.mode,
    this.chapterId,
    required this.title,
    required this.questions,
    required this.userAnswers,
    required this.startedAtIso,
    this.completedAtIso,
    required this.allottedSeconds,
    required this.elapsedSeconds,
  });

  factory ExamSessionDto.fromJson(Map<String, dynamic> json) {
    final rawQuestions = (json['questions'] as List<dynamic>?) ?? [];
    final questionsList = rawQuestions
        .map((q) => QuestionDto.fromJson(q as Map<String, dynamic>))
        .toList();

    final rawAnswers = (json['userAnswers'] as Map<String, dynamic>?) ?? {};
    final answersMap = rawAnswers.map(
      (key, val) => MapEntry(key, UserAnswerDto.fromJson(val as Map<String, dynamic>)),
    );

    return ExamSessionDto(
      id: json['id']?.toString() ?? '',
      mode: json['mode']?.toString() ?? 'fullCurriculumRandom',
      chapterId: (json['chapterId'] as num?)?.toInt(),
      title: json['title']?.toString() ?? 'جلسة تدريبية',
      questions: questionsList,
      userAnswers: answersMap,
      startedAtIso: json['startedAtIso']?.toString() ?? DateTime.now().toIso8601String(),
      completedAtIso: json['completedAtIso']?.toString(),
      allottedSeconds: (json['allottedSeconds'] as num?)?.toInt() ?? 1800,
      elapsedSeconds: (json['elapsedSeconds'] as num?)?.toInt() ?? 0,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'mode': mode,
        'chapterId': chapterId,
        'title': title,
        'questions': questions.map((q) => q.toJson()).toList(),
        'userAnswers': userAnswers.map((key, val) => MapEntry(key, val.toJson())),
        'startedAtIso': startedAtIso,
        'completedAtIso': completedAtIso,
        'allottedSeconds': allottedSeconds,
        'elapsedSeconds': elapsedSeconds,
      };

  ExamSession toDomain() {
    ExamSessionMode m;
    switch (mode) {
      case 'chapterSpecific':
        m = ExamSessionMode.chapterSpecific;
        break;
      case 'mistakesReviewFsrs':
        m = ExamSessionMode.mistakesReviewFsrs;
        break;
      case 'timedMinistrySpeedRun':
        m = ExamSessionMode.timedMinistrySpeedRun;
        break;
      case 'fullCurriculumRandom':
      default:
        m = ExamSessionMode.fullCurriculumRandom;
    }

    final Map<String, UserAnswer> domainAnswers = userAnswers.map(
      (k, v) => MapEntry(k, v.toDomain()),
    );

    return ExamSession(
      id: id,
      mode: m,
      chapterId: chapterId,
      title: title,
      questions: questions.map((q) => q.toDomain()).toList(),
      userAnswers: domainAnswers,
      startedAt: DateTime.tryParse(startedAtIso) ?? DateTime.now(),
      completedAt: completedAtIso != null ? DateTime.tryParse(completedAtIso!) : null,
      allottedSeconds: allottedSeconds,
      elapsedSeconds: elapsedSeconds,
    );
  }

  factory ExamSessionDto.fromDomain(ExamSession s) {
    return ExamSessionDto(
      id: s.id,
      mode: s.mode.name,
      chapterId: s.chapterId,
      title: s.title,
      questions: s.questions.map((q) => QuestionDto.fromDomain(q)).toList(),
      userAnswers: s.userAnswers.map((k, v) => MapEntry(k, UserAnswerDto.fromDomain(v))),
      startedAtIso: s.startedAt.toIso8601String(),
      completedAtIso: s.completedAt?.toIso8601String(),
      allottedSeconds: s.allottedSeconds,
      elapsedSeconds: s.elapsedSeconds,
    );
  }
}
