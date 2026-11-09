import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/study_task_dto.dart';
import '../models/study_schedule_dto.dart';
import '../models/ai_coach_tip_dto.dart';

/// محرك التخزين الدائم لخطط المذاكرة والمهام اليومية محلياً
class LocalPlannerStorageSource {
  static const String _prefKeyCurrentSchedule = 'biothanaweya_current_study_schedule_v1';
  static const String _prefKeyPlannerTasks = 'biothanaweya_planner_tasks_v1';

  StudyScheduleDto? _cachedSchedule;
  final List<StudyTaskDto> _cachedTasks = [];
  bool _isInitialized = false;

  Future<void> initialize() async {
    if (_isInitialized) return;
    try {
      final prefs = await SharedPreferences.getInstance();

      final rawSchedule = prefs.getString(_prefKeyCurrentSchedule);
      if (rawSchedule != null) {
        _cachedSchedule = StudyScheduleDto.fromJson(jsonDecode(rawSchedule) as Map<String, dynamic>);
      }

      final rawTasks = prefs.getString(_prefKeyPlannerTasks);
      if (rawTasks != null) {
        final List<dynamic> list = jsonDecode(rawTasks);
        _cachedTasks.clear();
        for (final item in list) {
          _cachedTasks.add(StudyTaskDto.fromJson(item as Map<String, dynamic>));
        }
      }
    } catch (_) {}
    _isInitialized = true;
  }

  Future<StudyScheduleDto?> getCurrentSchedule() async {
    await initialize();
    return _cachedSchedule;
  }

  Future<void> saveSchedule(StudyScheduleDto schedule) async {
    await initialize();
    _cachedSchedule = schedule;
    _cachedTasks.clear();
    _cachedTasks.addAll(schedule.tasks);

    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(_prefKeyCurrentSchedule, jsonEncode(schedule.toJson()));
      await prefs.setString(_prefKeyPlannerTasks, jsonEncode(_cachedTasks.map((t) => t.toJson()).toList()));
    } catch (_) {}
  }

  Future<List<StudyTaskDto>> getTasks() async {
    await initialize();
    if (_cachedTasks.isNotEmpty) {
      return List.unmodifiable(_cachedTasks);
    }
    if (_cachedSchedule != null) {
      return List.unmodifiable(_cachedSchedule!.tasks);
    }
    return [];
  }

  Future<void> saveTasks(List<StudyTaskDto> tasks) async {
    await initialize();
    _cachedTasks.clear();
    _cachedTasks.addAll(tasks);

    if (_cachedSchedule != null) {
      _cachedSchedule = StudyScheduleDto(
        scheduleId: _cachedSchedule!.scheduleId,
        dateIso: _cachedSchedule!.dateIso,
        tasks: tasks,
        coachTips: _cachedSchedule!.coachTips,
        totalEstimatedMinutes: tasks.fold(0, (acc, t) => acc + t.estimatedMinutes),
      );
    }

    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(_prefKeyPlannerTasks, jsonEncode(tasks.map((t) => t.toJson()).toList()));
      if (_cachedSchedule != null) {
        await prefs.setString(_prefKeyCurrentSchedule, jsonEncode(_cachedSchedule!.toJson()));
      }
    } catch (_) {}
  }
}
