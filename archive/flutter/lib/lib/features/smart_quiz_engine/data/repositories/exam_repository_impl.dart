import 'dart:math';
import '../../domain/entities/question.dart';
import '../../domain/entities/user_answer.dart';
import '../../domain/entities/exam_session.dart';
import '../../domain/repositories/exam_repository.dart';
import '../datasources/local_exam_storage_data_source.dart';
import '../models/exam_session_dto.dart';
import '../models/mistake_record_dto.dart';

/// التنفيذ العملي للإنتاج لمستودع الامتحانات وإدارة الأخطاء محلياً
class ExamRepositoryImpl implements ExamRepository {
  final LocalExamStorageDataSource _storageDataSource;

  ExamRepositoryImpl({LocalExamStorageDataSource? storageDataSource})
      : _storageDataSource = storageDataSource ?? LocalExamStorageDataSource();

  @override
  Future<List<QuizQuestion>> getQuestionsByChapter(int chapterId, {int limit = 20}) async {
    final dtos = await _storageDataSource.getQuestionsByChapter(chapterId);
    final questions = dtos.map((d) => d.toDomain()).toList();
    questions.shuffle(Random());
    return questions.take(limit).toList();
  }

  @override
  Future<List<QuizQuestion>> getRandomExamQuestions({
    int totalCount = 30,
    List<int>? targetChapters,
    bool prioritizeTraps = false,
  }) async {
    final allDtos = await _storageDataSource.getAllQuestions();
    var filtered = allDtos;

    if (targetChapters != null && targetChapters.isNotEmpty) {
      filtered = filtered.where((q) => targetChapters.contains(q.chapterId)).toList();
    }

    final List<QuizQuestion> domainQuestions = filtered.map((d) => d.toDomain()).toList();

    if (prioritizeTraps) {
      domainQuestions.sort((a, b) {
        if (a.difficulty == QuizDifficulty.ministryTrap && b.difficulty != QuizDifficulty.ministryTrap) {
          return -1;
        }
        if (b.difficulty == QuizDifficulty.ministryTrap && a.difficulty != QuizDifficulty.ministryTrap) {
          return 1;
        }
        return 0;
      });
    } else {
      domainQuestions.shuffle(Random());
    }

    return domainQuestions.take(totalCount).toList();
  }

  @override
  Future<List<QuizQuestion>> getDueMistakesForRevision({int limit = 20}) async {
    final mistakes = await _storageDataSource.getActiveMistakes();

    // فرز الأخطاء المستحقة للمراجعة بناءً على شدة التكرار والحاجة للاستدراك
    final dueMistakes = mistakes.where((m) => m.isDueForReview()).toList();
    dueMistakes.sort((a, b) => b.failCount.compareTo(a.failCount));

    final List<QuizQuestion> result = [];
    for (final m in dueMistakes) {
      final dto = await _storageDataSource.getQuestionById(m.questionId);
      if (dto != null) {
        result.add(dto.toDomain());
      }
      if (result.length >= limit) break;
    }

    // إذا لم تكن هناك أخطاء مجدولة بدقة، نجلب أي أسئلة لم تُحل بنجاح تام بعد
    if (result.isEmpty && mistakes.isNotEmpty) {
      for (final m in mistakes.where((m) => !m.isResolved).take(limit)) {
        final dto = await _storageDataSource.getQuestionById(m.questionId);
        if (dto != null) {
          result.add(dto.toDomain());
        }
      }
    }

    return result;
  }

  @override
  Future<void> saveExamSession(ExamSession session) async {
    final dto = ExamSessionDto.fromDomain(session);
    await _storageDataSource.saveExamSession(dto);

    // تسجيل وتحديث الأخطاء السابقة للطالب تلقائياً
    await recordSessionMistakes(
      questions: session.questions,
      userAnswers: session.userAnswers,
    );
  }

  @override
  Future<List<ExamSession>> getPastExamSessions() async {
    final dtos = await _storageDataSource.getPastSessions();
    return dtos.map((d) => d.toDomain()).toList();
  }

  @override
  Future<void> recordSessionMistakes({
    required List<QuizQuestion> questions,
    required Map<String, UserAnswer> userAnswers,
  }) async {
    final existingMistakes = await _storageDataSource.getActiveMistakes();
    final Map<String, MistakeRecordDto> mistakeMap = {
      for (final m in existingMistakes) m.questionId: m
    };

    final List<MistakeRecordDto> updatedRecords = [];

    for (final q in questions) {
      final ans = userAnswers[q.id];
      if (ans == null) continue;

      final existing = mistakeMap[q.id];
      if (!ans.isCorrect) {
        // إجابة خاطئة: إضافة سجل جديد أو تحديث السجل القائم
        if (existing == null) {
          final now = DateTime.now();
          updatedRecords.add(MistakeRecordDto(
            questionId: q.id,
            chapterId: q.chapterId,
            failCount: 1,
            consecutiveCorrectCount: 0,
            lastFailedAtIso: now.toIso8601String(),
            nextReviewDueIso: now.add(const Duration(hours: 4)).toIso8601String(),
            stabilityDays: 0.5,
            isResolved: false,
          ));
        } else {
          updatedRecords.add(existing.registerAttempt(wasCorrect: false));
        }
      } else {
        // إجابة صحيحة: إذا كان السؤال مسجلاً كخطأ سابق، نحدث العداد
        if (existing != null && !existing.isResolved) {
          updatedRecords.add(existing.registerAttempt(wasCorrect: true));
        }
      }
    }

    if (updatedRecords.isNotEmpty) {
      await _storageDataSource.saveMistakeBatch(updatedRecords);
    }
  }

  @override
  Future<Map<int, ChapterPerformanceSummary>> getCumulativeCurriculumAnalytics() async {
    final pastSessions = await getPastExamSessions();
    final Map<int, List<QuizQuestion>> chapterQuestions = {};
    final Map<int, List<UserAnswer>> chapterAnswers = {};

    for (final session in pastSessions) {
      for (final q in session.questions) {
        chapterQuestions.putIfAbsent(q.chapterId, () => []).add(q);
        final ans = session.userAnswers[q.id];
        if (ans != null) {
          chapterAnswers.putIfAbsent(q.chapterId, () => []).add(ans);
        }
      }
    }

    final Map<int, ChapterPerformanceSummary> summaries = {};
    for (int i = 1; i <= 11; i++) {
      final qList = chapterQuestions[i] ?? [];
      final aList = chapterAnswers[i] ?? [];
      final correctCount = aList.where((a) => a.isCorrect).length;
      final wrongCount = aList.where((a) => !a.isCorrect && a.status != AnswerStatus.skipped).length;
      final double pct = aList.isEmpty ? 0.0 : (correctCount / aList.length) * 100.0;

      final title = qList.isNotEmpty ? qList.first.chapterTitle : 'الفصل $i من المنهج';
      summaries[i] = ChapterPerformanceSummary(
        chapterId: i,
        chapterTitle: title,
        totalQuestions: aList.length,
        correctCount: correctCount,
        wrongCount: wrongCount,
        scorePercentage: pct,
      );
    }

    return summaries;
  }

  @override
  Future<int> getActiveMistakesCount() async {
    final mistakes = await _storageDataSource.getActiveMistakes();
    return mistakes.where((m) => !m.isResolved).length;
  }

  @override
  Future<void> resetMistakesLog() async {
    await _storageDataSource.clearMistakes();
  }
}
