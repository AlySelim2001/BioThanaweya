import 'package:flutter/foundation.dart';
import 'question.dart';
import 'user_answer.dart';

/// أنماط جلسات الاختبار المتوفرة
enum ExamSessionMode {
  chapterSpecific,       // تدريب مركز على فصل معين
  fullCurriculumRandom,  // محاكي وزاري شامل (عشوائي من كل الفصول)
  mistakesReviewFsrs,    // جلسة استدراك الأخطاء السابقة وفق FSRS
  timedMinistrySpeedRun, // سباق سرعة زمني مشدد
}

/// ملخص أداء فصل محدد في الجلسة الامتحانية
@immutable
class ChapterPerformanceSummary {
  final int chapterId;
  final String chapterTitle;
  final int totalQuestions;
  final int correctCount;
  final int wrongCount;
  final double scorePercentage;

  const ChapterPerformanceSummary({
    required this.chapterId,
    required this.chapterTitle,
    required this.totalQuestions,
    required this.correctCount,
    required this.wrongCount,
    required this.scorePercentage,
  });

  bool get isMastered => scorePercentage >= 80.0;
  bool get needsRemediation => scorePercentage < 60.0;
}

/// كيان جلسة الامتحان المتكاملة (ExamSession Entity)
@immutable
class ExamSession {
  final String id;
  final ExamSessionMode mode;
  final int? chapterId;
  final String title;
  final List<QuizQuestion> questions;
  final Map<String, UserAnswer> userAnswers; // questionId -> UserAnswer
  final DateTime startedAt;
  final DateTime? completedAt;
  final int allottedSeconds;
  final int elapsedSeconds;

  const ExamSession({
    required this.id,
    required this.mode,
    this.chapterId,
    required this.title,
    required this.questions,
    this.userAnswers = const {},
    required this.startedAt,
    this.completedAt,
    required this.allottedSeconds,
    this.elapsedSeconds = 0,
  });

  /// إجمالي عدد الأسئلة
  int get totalQuestions => questions.length;

  /// عدد الإجابات الصحيحة
  int get correctAnswersCount {
    return userAnswers.values.where((ans) => ans.isCorrect).length;
  }

  /// عدد الإجابات الخاطئة
  int get wrongAnswersCount {
    return userAnswers.values.where((ans) => !ans.isCorrect && ans.status != AnswerStatus.skipped).length;
  }

  /// عدد الأسئلة التي تم تخطيها أو لم يتم الإجابة عليها
  int get unansweredCount {
    return totalQuestions - userAnswers.length;
  }

  /// النسبة المئوية المكتسبة (0.0 إلى 100.0)
  double get scorePercentage {
    if (totalQuestions == 0) return 0.0;
    return (correctAnswersCount / totalQuestions) * 100.0;
  }

  /// التحقق من اجتياز الاختبار (60% فما فوق)
  bool get isPassed => scorePercentage >= 60.0;

  /// التقدير الوزاري المكافئ
  String get ministryGradeLabel {
    if (scorePercentage >= 95.0) return 'امتياز مع مرتبة الشرف 🏆';
    if (scorePercentage >= 85.0) return 'ممتاز جداً 🌟';
    if (scorePercentage >= 75.0) return 'جيد جداً ⚡';
    if (scorePercentage >= 65.0) return 'جيد 👍';
    if (scorePercentage >= 50.0) return 'مقبول (يحتاج تركيز)';
    return 'بحاجة لإعادة استدراك الفخاخ الوزارية ⚠️';
  }

  /// تفصيل أداء الطالب لكل فصل وردت منه أسئلة في هذه الجلسة
  Map<int, ChapterPerformanceSummary> get chapterBreakdowns {
    final Map<int, List<QuizQuestion>> chapterGroup = {};
    for (final q in questions) {
      chapterGroup.putIfAbsent(q.chapterId, () => []).add(q);
    }

    final Map<int, ChapterPerformanceSummary> summaries = {};
    chapterGroup.forEach((chapId, qList) {
      int correct = 0;
      int wrong = 0;
      for (final q in qList) {
        final ans = userAnswers[q.id];
        if (ans != null) {
          if (ans.isCorrect) {
            correct++;
          } else {
            wrong++;
          }
        }
      }
      final double pct = qList.isEmpty ? 0.0 : (correct / qList.length) * 100.0;
      summaries[chapId] = ChapterPerformanceSummary(
        chapterId: chapId,
        chapterTitle: qList.first.chapterTitle,
        totalQuestions: qList.length,
        correctCount: correct,
        wrongCount: wrong,
        scorePercentage: pct,
      );
    });

    return summaries;
  }

  /// قائمة الأسئلة التي أجاب عليها الطالب بشكل خاطئ (لاستخدامها في FSRS)
  List<QuizQuestion> get failedQuestions {
    return questions.where((q) {
      final ans = userAnswers[q.id];
      return ans != null && !ans.isCorrect;
    }).toList();
  }

  ExamSession copyWith({
    String? id,
    ExamSessionMode? mode,
    int? chapterId,
    String? title,
    List<QuizQuestion>? questions,
    Map<String, UserAnswer>? userAnswers,
    DateTime? startedAt,
    DateTime? completedAt,
    int? allottedSeconds,
    int? elapsedSeconds,
  }) {
    return ExamSession(
      id: id ?? this.id,
      mode: mode ?? this.mode,
      chapterId: chapterId ?? this.chapterId,
      title: title ?? this.title,
      questions: questions ?? this.questions,
      userAnswers: userAnswers ?? this.userAnswers,
      startedAt: startedAt ?? this.startedAt,
      completedAt: completedAt ?? this.completedAt,
      allottedSeconds: allottedSeconds ?? this.allottedSeconds,
      elapsedSeconds: elapsedSeconds ?? this.elapsedSeconds,
    );
  }
}
