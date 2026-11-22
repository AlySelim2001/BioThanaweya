import '../../domain/entities/study_task.dart';
import '../../domain/entities/ai_coach_tip.dart';

/// حالة واجهة خطة المذاكرة اليومية والمساعد الذكي (Immutable State)
class PlannerSessionState {
  final List<StudyTask> tasks;
  final List<AiCoachTip> coachTips;
  final double completionPercentage;
  final bool isLoading;
  final String? activeFilter; // null for all, or 'High', 'Pending', 'Completed'

  const PlannerSessionState({
    required this.tasks,
    required this.coachTips,
    required this.completionPercentage,
    this.isLoading = false,
    this.activeFilter,
  });

  int get totalTasksCount => tasks.length;
  int get completedTasksCount => tasks.where((t) => t.isCompleted).length;
  int get remainingTasksCount => totalTasksCount - completedTasksCount;

  int get totalEstimatedMinutes =>
      tasks.fold<int>(0, (acc, t) => acc + t.estimatedMinutes);

  int get completedMinutes => tasks
      .where((t) => t.isCompleted)
      .fold<int>(0, (acc, t) => acc + t.estimatedMinutes);

  PlannerSessionState copyWith({
    List<StudyTask>? tasks,
    List<AiCoachTip>? coachTips,
    double? completionPercentage,
    bool? isLoading,
    String? activeFilter,
  }) {
    return PlannerSessionState(
      tasks: tasks ?? this.tasks,
      coachTips: coachTips ?? this.coachTips,
      completionPercentage: completionPercentage ?? this.completionPercentage,
      isLoading: isLoading ?? this.isLoading,
      activeFilter: activeFilter ?? this.activeFilter,
    );
  }
}
