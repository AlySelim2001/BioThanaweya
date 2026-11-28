import 'package:flutter/foundation.dart';
import 'study_task.dart';
import 'ai_coach_tip.dart';

/// كيان الجدول اليومي الشامل لمذاكرة الأحياء ومؤشرات الإنجاز
@immutable
class StudySchedule {
  final String scheduleId;
  final DateTime date;
  final List<StudyTask> tasks;
  final List<AiCoachTip> coachTips;
  final int totalEstimatedMinutes;

  const StudySchedule({
    required this.scheduleId,
    required this.date,
    required this.tasks,
    required this.coachTips,
    required this.totalEstimatedMinutes,
  });

  int get completedTasksCount => tasks.where((t) => t.isCompleted).length;
  int get totalTasksCount => tasks.length;

  double get completionPercentage {
    if (tasks.isEmpty) return 0.0;
    return (completedTasksCount / totalTasksCount) * 100.0;
  }

  bool get isAllCompleted => tasks.isNotEmpty && completedTasksCount == totalTasksCount;

  StudySchedule copyWith({
    String? scheduleId,
    DateTime? date,
    List<StudyTask>? tasks,
    List<AiCoachTip>? coachTips,
    int? totalEstimatedMinutes,
  }) {
    return StudySchedule(
      scheduleId: scheduleId ?? this.scheduleId,
      date: date ?? this.date,
      tasks: tasks ?? this.tasks,
      coachTips: coachTips ?? this.coachTips,
      totalEstimatedMinutes: totalEstimatedMinutes ?? this.totalEstimatedMinutes,
    );
  }
}
