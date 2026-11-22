import '../../domain/entities/study_schedule.dart';
import 'study_task_dto.dart';
import 'ai_coach_tip_dto.dart';

/// DTO للجدول اليومي الشامل للمذاكرة
class StudyScheduleDto {
  final String scheduleId;
  final String dateIso;
  final List<StudyTaskDto> tasks;
  final List<AiCoachTipDto> coachTips;
  final int totalEstimatedMinutes;

  const StudyScheduleDto({
    required this.scheduleId,
    required this.dateIso,
    required this.tasks,
    required this.coachTips,
    required this.totalEstimatedMinutes,
  });

  factory StudyScheduleDto.fromJson(Map<String, dynamic> json) {
    final rawTasks = (json['tasks'] as List<dynamic>?) ?? [];
    final rawTips = (json['coachTips'] as List<dynamic>?) ?? [];

    return StudyScheduleDto(
      scheduleId: json['scheduleId']?.toString() ?? '',
      dateIso: json['dateIso']?.toString() ?? DateTime.now().toIso8601String(),
      tasks: rawTasks.map((t) => StudyTaskDto.fromJson(t as Map<String, dynamic>)).toList(),
      coachTips: rawTips.map((tip) => AiCoachTipDto.fromJson(tip as Map<String, dynamic>)).toList(),
      totalEstimatedMinutes: (json['totalEstimatedMinutes'] as num?)?.toInt() ?? 120,
    );
  }

  Map<String, dynamic> toJson() => {
        'scheduleId': scheduleId,
        'dateIso': dateIso,
        'tasks': tasks.map((t) => t.toJson()).toList(),
        'coachTips': coachTips.map((tip) => tip.toJson()).toList(),
        'totalEstimatedMinutes': totalEstimatedMinutes,
      };

  StudySchedule toEntity() {
    return StudySchedule(
      scheduleId: scheduleId,
      date: DateTime.tryParse(dateIso) ?? DateTime.now(),
      tasks: tasks.map((t) => t.toEntity()).toList(),
      coachTips: coachTips.map((tip) => tip.toEntity()).toList(),
      totalEstimatedMinutes: totalEstimatedMinutes,
    );
  }

  factory StudyScheduleDto.fromEntity(StudySchedule entity) {
    return StudyScheduleDto(
      scheduleId: entity.scheduleId,
      dateIso: entity.date.toIso8601String(),
      tasks: entity.tasks.map((t) => StudyTaskDto.fromEntity(t)).toList(),
      coachTips: entity.coachTips.map((tip) => AiCoachTipDto.fromEntity(tip)).toList(),
      totalEstimatedMinutes: entity.totalEstimatedMinutes,
    );
  }
}
