import 'package:flutter/foundation.dart';
import '../../domain/entities/question.dart';
import '../../domain/entities/user_answer.dart';
import '../../domain/entities/exam_session.dart';

enum QuizUiStatus {
  initial,
  loading,
  inProgress,
  completed,
  error,
}

/// حالة واجهة جلسة الاختبار التفاعلية (Immutable UI State)
@immutable
class QuizSessionState {
  final QuizUiStatus status;
  final List<QuizQuestion> questions;
  final int currentIndex;
  final Map<String, UserAnswer> userAnswers;
  final int remainingSeconds;
  final int totalAllottedSeconds;
  final bool isTimerRunning;
  final String? errorMessage;
  final ExamSession? completedSession;

  const QuizSessionState({
    this.status = QuizUiStatus.initial,
    this.questions = const [],
    this.currentIndex = 0,
    this.userAnswers = const {},
    this.remainingSeconds = 1800,
    this.totalAllottedSeconds = 1800,
    this.isTimerRunning = false,
    this.errorMessage,
    this.completedSession,
  });

  bool get isLoading => status == QuizUiStatus.loading;
  bool get isInProgress => status == QuizUiStatus.inProgress;
  bool get isCompleted => status == QuizUiStatus.completed;

  QuizQuestion? get currentQuestion {
    if (questions.isNotEmpty && currentIndex >= 0 && currentIndex < questions.length) {
      return questions[currentIndex];
    }
    return null;
  }

  int get answeredCount => userAnswers.length;
  int get totalQuestions => questions.length;
  double get progressRatio => totalQuestions == 0 ? 0.0 : (currentIndex + 1) / totalQuestions;
  double get answeredRatio => totalQuestions == 0 ? 0.0 : answeredCount / totalQuestions;

  bool get isTimeWarning => remainingSeconds <= 120 && remainingSeconds > 0;
  bool get isTimeExpired => remainingSeconds <= 0 && isInProgress;

  String get formattedRemainingTime {
    final minutes = remainingSeconds ~/ 60;
    final seconds = remainingSeconds % 60;
    return '${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}';
  }

  UserAnswer? answerForQuestion(String questionId) => userAnswers[questionId];

  QuizSessionState copyWith({
    QuizUiStatus? status,
    List<QuizQuestion>? questions,
    int? currentIndex,
    Map<String, UserAnswer>? userAnswers,
    int? remainingSeconds,
    int? totalAllottedSeconds,
    bool? isTimerRunning,
    String? errorMessage,
    ExamSession? completedSession,
  }) {
    return QuizSessionState(
      status: status ?? this.status,
      questions: questions ?? this.questions,
      currentIndex: currentIndex ?? this.currentIndex,
      userAnswers: userAnswers ?? this.userAnswers,
      remainingSeconds: remainingSeconds ?? this.remainingSeconds,
      totalAllottedSeconds: totalAllottedSeconds ?? this.totalAllottedSeconds,
      isTimerRunning: isTimerRunning ?? this.isTimerRunning,
      errorMessage: errorMessage ?? this.errorMessage,
      completedSession: completedSession ?? this.completedSession,
    );
  }
}
