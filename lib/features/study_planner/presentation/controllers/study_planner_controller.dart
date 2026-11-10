import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../domain/entities/study_task.dart';
import '../../domain/repositories/study_planner_repository.dart';
import '../../data/repositories/study_planner_repository_impl.dart';
import '../../data/datasources/adaptive_plan_generator.dart';
import 'planner_session_state.dart';

/// وحدة التحكم التفاعلية في خطة المذاكرة التكيفية والمساعد الذكي عبر Riverpod StateNotifier
class StudyPlannerController extends StateNotifier<PlannerSessionState> {
  final StudyPlannerRepository _repository;

  StudyPlannerController({
    StudyPlannerRepository? repository,
    PlannerSessionState? initialState,
  })  : _repository = repository ?? StudyPlannerRepositoryImpl(),
        super(
          initialState ??
              PlannerSessionState(
                tasks: AdaptivePlanGenerator.generateAdaptiveTasks(['الفصل الثاني: التنسيق الهرموني']),
                coachTips: AdaptivePlanGenerator.generateLocalCoachInsights(['الفصل الثاني: التنسيق الهرموني']),
                completionPercentage: 0.0,
                isLoading: false,
              ),
        ) {
    loadInitialPlan();
  }

  Future<void> loadInitialPlan() async {
    state = state.copyWith(isLoading: true);
    try {
      final schedule = await _repository.getTodaySchedule();
      state = state.copyWith(
        tasks: schedule.tasks,
        coachTips: schedule.coachTips,
        completionPercentage: _calculateCompletion(schedule.tasks),
        isLoading: false,
      );
    } catch (_) {
      final fallbackTasks = AdaptivePlanGenerator.generateAdaptiveTasks(['الفصل الثاني: التنسيق الهرموني']);
      final fallbackTips = AdaptivePlanGenerator.generateLocalCoachInsights(['الفصل الثاني: التنسيق الهرموني']);
      state = state.copyWith(
        tasks: fallbackTasks,
        coachTips: fallbackTips,
        completionPercentage: _calculateCompletion(fallbackTasks),
        isLoading: false,
      );
    }
  }

  Future<void> generateNewAdaptivePlan(List<String> weakChapters) async {
    state = state.copyWith(isLoading: true);
    final schedule = await _repository.generateAdaptiveDailyPlan(priorityChapters: weakChapters);
    state = state.copyWith(
      tasks: schedule.tasks,
      coachTips: schedule.coachTips,
      completionPercentage: _calculateCompletion(schedule.tasks),
      isLoading: false,
    );
  }

  Future<void> toggleTaskCompletion(String taskId) async {
    final updatedTasks = state.tasks.map((task) {
      if (task.id == taskId) {
        final newStatus = !task.isCompleted;
        return task.copyWith(isCompleted: newStatus);
      }
      return task;
    }).toList();

    state = state.copyWith(
      tasks: updatedTasks,
      completionPercentage: _calculateCompletion(updatedTasks),
    );

    await _repository.toggleTaskStatus(taskId);
  }

  Future<void> addCustomTask({
    required String title,
    required String chapterName,
    required String priority,
    required int estimatedMinutes,
  }) async {
    final newTask = StudyTask(
      id: 'task_custom_${DateTime.now().millisecondsSinceEpoch}',
      title: title,
      chapterName: chapterName,
      priority: priority,
      isCompleted: false,
      estimatedMinutes: estimatedMinutes,
    );

    final updatedTasks = [...state.tasks, newTask];
    state = state.copyWith(
      tasks: updatedTasks,
      completionPercentage: _calculateCompletion(updatedTasks),
    );

    await _repository.addTask(newTask);
  }

  Future<void> deleteTask(String taskId) async {
    final updatedTasks = state.tasks.where((t) => t.id != taskId).toList();
    state = state.copyWith(
      tasks: updatedTasks,
      completionPercentage: _calculateCompletion(updatedTasks),
    );

    await _repository.deleteTask(taskId);
  }

  double _calculateCompletion(List<StudyTask> tasks) {
    if (tasks.isEmpty) return 0.0;
    int completedCount = tasks.where((t) => t.isCompleted).length;
    return (completedCount / tasks.length) * 100.0;
  }
}
