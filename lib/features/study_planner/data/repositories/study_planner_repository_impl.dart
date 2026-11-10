import '../../domain/entities/study_task.dart';
import '../../domain/entities/study_schedule.dart';
import '../../domain/entities/ai_coach_tip.dart';
import '../../domain/repositories/study_planner_repository.dart';
import '../datasources/adaptive_plan_generator.dart';
import '../datasources/local_planner_storage_source.dart';
import '../models/study_schedule_dto.dart';
import '../models/study_task_dto.dart';

/// التنفيذ العملي لمستودع خطة المذاكرة التكيفية والمساعد الذكي
class StudyPlannerRepositoryImpl implements StudyPlannerRepository {
  final LocalPlannerStorageSource _storageSource;

  StudyPlannerRepositoryImpl({LocalPlannerStorageSource? storageSource})
      : _storageSource = storageSource ?? LocalPlannerStorageSource();

  @override
  Future<StudySchedule> getTodaySchedule() async {
    final cached = await _storageSource.getCurrentSchedule();
    if (cached != null) {
      return cached.toEntity();
    }
    // في حال عدم وجود جدول مسبق، نقوم بتوليد جدول تكيفي أولي
    return await generateAdaptiveDailyPlan();
  }

  @override
  Future<StudySchedule> generateAdaptiveDailyPlan({List<String>? priorityChapters}) async {
    final weakList = priorityChapters ?? ['الفصل الثاني: التنسيق الهرموني'];
    final tasks = AdaptivePlanGenerator.generateAdaptiveTasks(weakList);
    final tips = AdaptivePlanGenerator.generateLocalCoachInsights(weakList);

    final totalMinutes = tasks.fold(0, (acc, t) => acc + t.estimatedMinutes);
    final schedule = StudySchedule(
      scheduleId: 'schedule_${DateTime.now().millisecondsSinceEpoch}',
      date: DateTime.now(),
      tasks: tasks,
      coachTips: tips,
      totalEstimatedMinutes: totalMinutes,
    );

    final dto = StudyScheduleDto.fromEntity(schedule);
    await _storageSource.saveSchedule(dto);
    return schedule;
  }

  @override
  Future<void> toggleTaskStatus(String taskId) async {
    final tasks = await _storageSource.getTasks();
    final updated = tasks.map((t) {
      if (t.id == taskId) {
        final newCompleted = !t.isCompleted;
        return StudyTaskDto(
          id: t.id,
          title: t.title,
          chapterName: t.chapterName,
          priority: t.priority,
          isCompleted: newCompleted,
          estimatedMinutes: t.estimatedMinutes,
          completedAtIso: newCompleted ? DateTime.now().toIso8601String() : null,
        );
      }
      return t;
    }).toList();

    await _storageSource.saveTasks(updated);
  }

  @override
  Future<void> addTask(StudyTask task) async {
    final tasks = List<StudyTaskDto>.from(await _storageSource.getTasks());
    tasks.add(StudyTaskDto.fromEntity(task));
    await _storageSource.saveTasks(tasks);
  }

  @override
  Future<void> deleteTask(String taskId) async {
    final tasks = await _storageSource.getTasks();
    final updated = tasks.where((t) => t.id != taskId).toList();
    await _storageSource.saveTasks(updated);
  }

  @override
  Future<List<AiCoachTip>> getCoachInsights() async {
    final schedule = await getTodaySchedule();
    return schedule.coachTips;
  }

  @override
  Future<void> saveSchedule(StudySchedule schedule) async {
    final dto = StudyScheduleDto.fromEntity(schedule);
    await _storageSource.saveSchedule(dto);
  }
}
