import '../entities/question.dart';
import '../entities/user_answer.dart';
import '../entities/exam_session.dart';

/// عقد مستودع الامتحانات وإدارة بنك الأسئلة محلياً (Offline Exam Repository Contract)
abstract class ExamRepository {
  /// جلب أسئلة فصل محدد محلياً
  Future<List<QuizQuestion>> getQuestionsByChapter(int chapterId, {int limit = 20});

  /// إنشاء اختبار شامل عشوائي من فصول المنهج
  Future<List<QuizQuestion>> getRandomExamQuestions({
    int totalCount = 30,
    List<int>? targetChapters,
    bool prioritizeTraps = false,
  });

  /// جلب الأسئلة المستحقة للمراجعة وفق خوارزمية تتبع الأخطاء (FSRS / Mistakes Review)
  Future<List<QuizQuestion>> getDueMistakesForRevision({int limit = 20});

  /// حفظ جلسة الامتحان بعد إنهائها
  Future<void> saveExamSession(ExamSession session);

  /// استرجاع سجل الجلسات الامتحانية السابقة
  Future<List<ExamSession>> getPastExamSessions();

  /// تسجيل وتحديث الأخطاء السابقة للطالب بعد انتهاء الجلسة لتكرارها لاحقاً
  Future<void> recordSessionMistakes({
    required List<QuizQuestion> questions,
    required Map<String, UserAnswer> userAnswers,
  });

  /// استرجاع إحصائيات إتقان الفصول التراكمية
  Future<Map<int, ChapterPerformanceSummary>> getCumulativeCurriculumAnalytics();

  /// عدد الأخطاء النشطة وغير المستدركة بعد
  Future<int> getActiveMistakesCount();

  /// تفريغ أو إعادة ضبط سجل الأخطاء
  Future<void> resetMistakesLog();
}
