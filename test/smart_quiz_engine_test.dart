import 'package:flutter_test/flutter_test.dart';
import 'package:smarts_bio/features/smart_quiz_engine/domain/entities/question.dart';
import 'package:smarts_bio/features/smart_quiz_engine/domain/entities/user_answer.dart';
import 'package:smarts_bio/features/smart_quiz_engine/domain/entities/exam_session.dart';
import 'package:smarts_bio/features/smart_quiz_engine/data/models/mistake_record_dto.dart';
import 'package:smarts_bio/features/smart_quiz_engine/data/datasources/offline_curriculum_question_bank.dart';
import 'package:smarts_bio/features/smart_quiz_engine/data/repositories/exam_repository_impl.dart';

void main() {
  group('Smart Quiz Engine Domain Entities & Calculations', () {
    test('QuizQuestion entity correctly validates choices', () {
      const question = QuizQuestion(
        id: 'test_q1',
        chapterId: 1,
        chapterTitle: 'الدعامة والحركة',
        unitNumber: 1,
        questionText: 'ما الأيون المسؤول عن تحرير النواقل العصبية؟',
        options: ['الكالسيوم', 'الصوديوم', 'البوتاسيوم', 'الكلور'],
        correctIndex: 0,
        explanation: 'أيونات الكالسيوم تفجر الحويصلات.',
      );

      expect(question.isCorrectChoice(0), isTrue);
      expect(question.isCorrectChoice(1), isFalse);
      expect(question.correctOptionText, equals('الكالسيوم'));
    });

    test('ExamSession accurately computes scores and ministry grade', () {
      const q1 = QuizQuestion(
        id: 'q1',
        chapterId: 1,
        chapterTitle: 'الدعامة والحركة',
        unitNumber: 1,
        questionText: 'سؤال 1',
        options: ['أ', 'ب'],
        correctIndex: 0,
        explanation: 'شرح 1',
      );
      const q2 = QuizQuestion(
        id: 'q2',
        chapterId: 2,
        chapterTitle: 'التنسيق الهرموني',
        unitNumber: 1,
        questionText: 'سؤال 2',
        options: ['أ', 'ب'],
        correctIndex: 1,
        explanation: 'شرح 2',
      );

      final session = ExamSession(
        id: 'test_session',
        mode: ExamSessionMode.fullCurriculumRandom,
        title: 'اختبار تجريبي',
        questions: [q1, q2],
        userAnswers: {
          'q1': UserAnswer(
            questionId: 'q1',
            selectedOptionIndex: 0,
            isCorrect: true,
            answeredAt: DateTime.now(),
          ),
          'q2': UserAnswer(
            questionId: 'q2',
            selectedOptionIndex: 0,
            isCorrect: false, // chose 0 instead of 1
            answeredAt: DateTime.now(),
          ),
        },
        startedAt: DateTime.now(),
        completedAt: DateTime.now().add(const Duration(minutes: 5)),
        allottedSeconds: 600,
        elapsedSeconds: 300,
      );

      expect(session.totalQuestions, equals(2));
      expect(session.correctAnswersCount, equals(1));
      expect(session.wrongAnswersCount, equals(1));
      expect(session.scorePercentage, equals(50.0));
      expect(session.isPassed, isFalse);
      expect(session.failedQuestions.length, equals(1));
      expect(session.failedQuestions.first.id, equals('q2'));

      final breakdowns = session.chapterBreakdowns;
      expect(breakdowns.containsKey(1), isTrue);
      expect(breakdowns.containsKey(2), isTrue);
      expect(breakdowns[1]?.scorePercentage, equals(100.0));
      expect(breakdowns[2]?.scorePercentage, equals(0.0));
    });

    test('FSRS Mistake Record adapts stability on failure and resolution', () {
      final initial = MistakeRecordDto(
        questionId: 'trap_dna_1',
        chapterId: 5,
        failCount: 1,
        consecutiveCorrectCount: 0,
        lastFailedAtIso: DateTime.now().toIso8601String(),
        nextReviewDueIso: DateTime.now().toIso8601String(),
        stabilityDays: 0.5,
        isResolved: false,
      );

      // Failed again -> increments failCount, resets consecutive successes
      final failedAgain = initial.registerAttempt(wasCorrect: false);
      expect(failedAgain.failCount, equals(2));
      expect(failedAgain.consecutiveCorrectCount, equals(0));
      expect(failedAgain.isResolved, isFalse);

      // Answered correct once -> consecutive increases
      final correct1 = failedAgain.registerAttempt(wasCorrect: true);
      expect(correct1.consecutiveCorrectCount, equals(1));
      expect(correct1.isResolved, isFalse);

      // Answered correct second time -> marked resolved
      final correct2 = correct1.registerAttempt(wasCorrect: true);
      expect(correct2.consecutiveCorrectCount, equals(2));
      expect(correct2.isResolved, isTrue);
    });

    test('Offline question bank supplies all 11 curriculum chapters', () {
      final questions = OfflineCurriculumQuestionBank.getRawCurriculumQuestions();
      expect(questions.isNotEmpty, isTrue);

      final chapterIds = questions.map((q) => q.chapterId).toSet();
      for (int ch = 1; ch <= 11; ch++) {
        expect(chapterIds.contains(ch), isTrue, reason: 'Chapter $ch must have offline questions');
      }
    });

    test('ExamRepository filters and randomizes questions deterministically', () async {
      final repo = ExamRepositoryImpl();
      final chapter1Questions = await repo.getQuestionsByChapter(1, limit: 5);
      expect(chapter1Questions.isNotEmpty, isTrue);
      expect(chapter1Questions.every((q) => q.chapterId == 1), isTrue);

      final randomExam = await repo.getRandomExamQuestions(totalCount: 10);
      expect(randomExam.isNotEmpty, isTrue);
      expect(randomExam.length <= 10, isTrue);
    });
  });
}
