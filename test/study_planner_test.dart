import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smarts_bio/core/database/local_database_service.dart';
import 'package:smarts_bio/features/study_planner/domain/entities/study_task.dart';
import 'package:smarts_bio/features/study_planner/domain/entities/study_schedule.dart';
import 'package:smarts_bio/features/study_planner/data/datasources/adaptive_plan_generator.dart';
import 'package:smarts_bio/features/study_planner/data/repositories/study_planner_repository_impl.dart';
import 'package:smarts_bio/features/study_planner/presentation/controllers/study_planner_controller.dart';
import 'package:smarts_bio/features/study_planner/presentation/controllers/planner_providers.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('Smart Study Planner & AI Coach Tests', () {
    setUp(() {
      SharedPreferences.setMockInitialValues({});
    });

    test('StudySchedule calculates completion percentage accurately', () {
      const task1 = StudyTask(
        id: 't1',
        title: 'مهمة 1',
        chapterName: 'الفصل الأول',
        priority: 'High',
        isCompleted: true,
        estimatedMinutes: 30,
      );
      const task2 = StudyTask(
        id: 't2',
        title: 'مهمة 2',
        chapterName: 'الفصل الثاني',
        priority: 'Medium',
        isCompleted: false,
        estimatedMinutes: 30,
      );

      final schedule = StudySchedule(
        scheduleId: 's1',
        date: DateTime.now(),
        tasks: [task1, task2],
        coachTips: const [],
        totalEstimatedMinutes: 60,
      );

      expect(schedule.totalTasksCount, equals(2));
      expect(schedule.completedTasksCount, equals(1));
      expect(schedule.completionPercentage, equals(50.0));
      expect(schedule.isAllCompleted, isFalse);
    });

    test('AdaptivePlanGenerator generates remediation tasks for weak chapters', () {
      final tasks = AdaptivePlanGenerator.generateAdaptiveTasks(['الفصل الثالث: التكاثر']);
      expect(tasks.isNotEmpty, isTrue);
      expect(tasks.any((t) => t.chapterName.contains('التكاثر')), isTrue);
      expect(tasks.any((t) => t.priority == 'High'), isTrue);

      final tips = AdaptivePlanGenerator.generateLocalCoachInsights(['الفصل الثالث: التكاثر']);
      expect(tips.isNotEmpty, isTrue);
      expect(tips.any((tip) => tip.targetChapter.contains('التكاثر')), isTrue);
    });

    test('StudyPlannerRepositoryImpl generates and toggles tasks', () async {
      final repo = StudyPlannerRepositoryImpl();
      final schedule = await repo.getTodaySchedule();
      expect(schedule.tasks.isNotEmpty, isTrue);

      final firstTaskId = schedule.tasks.first.id;
      final initialStatus = schedule.tasks.first.isCompleted;

      await repo.toggleTaskStatus(firstTaskId);
      final updatedSchedule = await repo.getTodaySchedule();
      final updatedTask = updatedSchedule.tasks.firstWhere((t) => t.id == firstTaskId);

      expect(updatedTask.isCompleted, equals(!initialStatus));
    });

    testWidgets('StudyPlannerController manages reactive state and completion', (WidgetTester tester) async {
      final container = ProviderContainer(
        overrides: [
          localDatabaseServiceProvider.overrideWithValue(LocalDatabaseService.instance),
        ],
      );
      addTearDown(container.dispose);

      await tester.pumpWidget(
        UncontrolledProviderScope(
          container: container,
          child: MaterialApp(
            home: Scaffold(
              body: Consumer(
                builder: (context, ref, _) {
                  final state = ref.watch(studyPlannerControllerProvider);
                  return Column(
                    children: [
                      Text('Completion: ${state.completionPercentage}'),
                      for (final task in state.tasks)
                        Text('Task: ${task.id} ${task.isCompleted}'),
                    ],
                  );
                },
              ),
            ),
          ),
        ),
      );

      // Allow asynchronous state transitions to settle
      await tester.pumpAndSettle();

      final controller = container.read(studyPlannerControllerProvider.notifier);
      final initialTasks = container.read(studyPlannerControllerProvider).tasks;
      expect(initialTasks.isNotEmpty, isTrue);

      final firstId = initialTasks.first.id;
      await controller.toggleTaskCompletion(firstId);

      // Allow reactive state notification and re-render cycles to settle
      await tester.pumpAndSettle();

      final updatedState = container.read(studyPlannerControllerProvider);
      final updatedTask = updatedState.tasks.firstWhere((t) => t.id == firstId);
      expect(updatedTask.isCompleted, isTrue);
      expect(updatedState.completionPercentage, greaterThan(0.0));
    });

    test('StudyPlannerController manages reactive state and completion synchronously', () async {
      final controller = StudyPlannerController();
      expect(controller.state.tasks.isNotEmpty, isTrue);

      final firstId = controller.state.tasks.first.id;
      await controller.toggleTaskCompletion(firstId);

      final updatedTask = controller.state.tasks.firstWhere((t) => t.id == firstId);
      expect(updatedTask.isCompleted, isTrue);
      expect(controller.state.completionPercentage, greaterThan(0.0));
    });
  });
}
