import 'dart:async';
import 'package:flutter/foundation.dart';
import '../../domain/entities/question.dart';
import '../../domain/entities/user_answer.dart';
import '../../domain/entities/exam_session.dart';
import '../../domain/repositories/exam_repository.dart';
import 'quiz_session_state.dart';

/// وحدة التحكم التفاعلية لجلسة الاختبار وإدارة الحالة والوقت والأخطاء
class QuizSessionController extends ChangeNotifier {
  final ExamRepository _repository;

  QuizSessionState _state = const QuizSessionState();
  QuizSessionState get state => _state;

  Timer? _countdownTimer;
  DateTime? _sessionStartTime;
  DateTime? _questionStartTime;
  ExamSessionMode _currentMode = ExamSessionMode.fullCurriculumRandom;
  int? _currentChapterId;
  String _sessionTitle = 'محاكي الامتحان الوزاري';

  QuizSessionController({required ExamRepository repository})
      : _repository = repository;

  /// بدء اختبار مركز على فصل معين
  Future<void> startChapterQuiz(int chapterId, {int count = 10, int durationMinutes = 15}) async {
    _currentMode = ExamSessionMode.chapterSpecific;
    _currentChapterId = chapterId;
    _sessionTitle = 'اختبار الفصل $chapterId';
    await _loadAndStartQuiz(
      () => _repository.getQuestionsByChapter(chapterId, limit: count),
      allottedSeconds: durationMinutes * 60,
    );
  }

  /// بدء محاكي امتحاني وزاري شامل لكافة الفصول الـ 11
  Future<void> startComprehensiveMinistryExam({int count = 20, int durationMinutes = 30}) async {
    _currentMode = ExamSessionMode.fullCurriculumRandom;
    _currentChapterId = null;
    _sessionTitle = 'المحاكي الشامل لشهادة الثانوية العامة';
    await _loadAndStartQuiz(
      () => _repository.getRandomExamQuestions(totalCount: count, prioritizeTraps: true),
      allottedSeconds: durationMinutes * 60,
    );
  }

  /// بدء جلسة مراجعة واستدراك الأخطاء السابقة (FSRS Mistakes Session)
  Future<void> startMistakesRevisionSession({int count = 15, int durationMinutes = 20}) async {
    _currentMode = ExamSessionMode.mistakesReviewFsrs;
    _currentChapterId = null;
    _sessionTitle = 'جلسة استدراك وتصحيح الأخطاء السابقة (FSRS)';
    await _loadAndStartQuiz(
      () => _repository.getDueMistakesForRevision(limit: count),
      allottedSeconds: durationMinutes * 60,
    );
  }

  Future<void> _loadAndStartQuiz(
    Future<List<QuizQuestion>> Function() questionLoader, {
    required int allottedSeconds,
  }) async {
    _countdownTimer?.cancel();
    _state = _state.copyWith(status: QuizUiStatus.loading, errorMessage: null);
    notifyListeners();

    try {
      final questions = await questionLoader();
      if (questions.isEmpty) {
        _state = _state.copyWith(
          status: QuizUiStatus.error,
          errorMessage: 'لم يتم العثور على أسئلة متاحة في هذا التصنيف حالياً.',
        );
        notifyListeners();
        return;
      }

      _sessionStartTime = DateTime.now();
      _questionStartTime = DateTime.now();

      _state = QuizSessionState(
        status: QuizUiStatus.inProgress,
        questions: questions,
        currentIndex: 0,
        userAnswers: {},
        remainingSeconds: allottedSeconds,
        totalAllottedSeconds: allottedSeconds,
        isTimerRunning: true,
      );
      notifyListeners();

      _startTimer();
    } catch (e) {
      _state = _state.copyWith(
        status: QuizUiStatus.error,
        errorMessage: 'حدث خطأ أثناء تحميل بنك الأسئلة محلياً: $e',
      );
      notifyListeners();
    }
  }

  void _startTimer() {
    _countdownTimer?.cancel();
    _countdownTimer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (_state.remainingSeconds > 0) {
        _state = _state.copyWith(remainingSeconds: _state.remainingSeconds - 1);
        notifyListeners();
      } else {
        timer.cancel();
        _state = _state.copyWith(isTimerRunning: false);
        notifyListeners();
        // إنهاء وتسليم الامتحان تلقائياً عند نفاذ الوقت
        submitExam();
      }
    });
  }

  /// تسجيل إجابة الطالب على السؤال الحالي
  void selectOption(int optionIndex) {
    if (!_state.isInProgress) return;
    final currentQ = _state.currentQuestion;
    if (currentQ == null) return;

    final now = DateTime.now();
    final timeSpent = _questionStartTime != null
        ? now.difference(_questionStartTime!).inSeconds
        : 5;

    final isCorrect = currentQ.isCorrectChoice(optionIndex);
    final answer = UserAnswer(
      questionId: currentQ.id,
      selectedOptionIndex: optionIndex,
      isCorrect: isCorrect,
      timeSpentSeconds: timeSpent,
      answeredAt: now,
      status: isCorrect ? AnswerStatus.correct : AnswerStatus.incorrect,
    );

    final updatedAnswers = Map<String, UserAnswer>.from(_state.userAnswers);
    updatedAnswers[currentQ.id] = answer;

    _state = _state.copyWith(userAnswers: updatedAnswers);
    notifyListeners();
  }

  /// الانتقال إلى السؤال التالي
  void nextQuestion() {
    if (_state.currentIndex < _state.questions.length - 1) {
      _questionStartTime = DateTime.now();
      _state = _state.copyWith(currentIndex: _state.currentIndex + 1);
      notifyListeners();
    }
  }

  /// العودة إلى السؤال السابق
  void previousQuestion() {
    if (_state.currentIndex > 0) {
      _questionStartTime = DateTime.now();
      _state = _state.copyWith(currentIndex: _state.currentIndex - 1);
      notifyListeners();
    }
  }

  /// القفز المباشر إلى رقم سؤال محدد
  void jumpToQuestion(int index) {
    if (index >= 0 && index < _state.questions.length) {
      _questionStartTime = DateTime.now();
      _state = _state.copyWith(currentIndex: index);
      notifyListeners();
    }
  }

  /// تسليم الاختبار وحساب النتائج وحفظ الأخطاء
  Future<ExamSession?> submitExam() async {
    if (_state.status == QuizUiStatus.completed) {
      return _state.completedSession;
    }

    _countdownTimer?.cancel();
    final endTime = DateTime.now();
    final elapsed = _sessionStartTime != null
        ? endTime.difference(_sessionStartTime!).inSeconds
        : (_state.totalAllottedSeconds - _state.remainingSeconds);

    final session = ExamSession(
      id: 'session_${DateTime.now().millisecondsSinceEpoch}',
      mode: _currentMode,
      chapterId: _currentChapterId,
      title: _sessionTitle,
      questions: _state.questions,
      userAnswers: _state.userAnswers,
      startedAt: _sessionStartTime ?? DateTime.now(),
      completedAt: endTime,
      allottedSeconds: _state.totalAllottedSeconds,
      elapsedSeconds: elapsed,
    );

    _state = _state.copyWith(
      status: QuizUiStatus.completed,
      isTimerRunning: false,
      completedSession: session,
    );
    notifyListeners();

    // حفظ الجلسة وتحديث الأخطاء في المستودع المحلي
    try {
      await _repository.saveExamSession(session);
    } catch (_) {}

    return session;
  }

  @override
  void dispose() {
    _countdownTimer?.cancel();
    super.dispose();
  }
}
