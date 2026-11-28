import '../../domain/entities/study_task.dart';

/// DTO لمهمة المذاكرة مع التحويل لـ JSON
class StudyTaskDto {
  final String id;
  final String title;
  final String chapterName;
  final String priority;
  final bool isCompleted;
  final int estimatedMinutes;
  final String? completedAtIso;

  const StudyTaskDto({
    required this.id,
    required this.title,
    required this.chapterName,
    required this.priority,
    required this.isCompleted,
    required this.estimatedMinutes,
    this.completedAtIso,
  });

  factory StudyTaskDto.fromJson(Map<String, dynamic> json) {
    return StudyTaskDto(
      id: json['id']?.toString() ?? '',
      title: json['title']?.toString() ?? '',
      chapterName: json['chapterName']?.toString() ?? '',
      priority: json['priority']?.toString() ?? 'Medium',
      isCompleted: json['isCompleted'] as bool? ?? false,
      estimatedMinutes: (json['estimatedMinutes'] as num?)?.toInt() ?? 30,
      completedAtIso: json['completedAtIso']?.toString(),
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'title': title,
        'chapterName': chapterName,
        'priority': priority,
        'isCompleted': isCompleted,
        'estimatedMinutes': estimatedMinutes,
        'completedAtIso': completedAtIso,
      };

  StudyTask toEntity() {
    return StudyTask(
      id: id,
      title: title,
      chapterName: chapterName,
      priority: priority,
      isCompleted: isCompleted,
      estimatedMinutes: estimatedMinutes,
      completedAt: completedAtIso != null ? DateTime.tryParse(completedAtIso!) : null,
    );
  }

  factory StudyTaskDto.fromEntity(StudyTask entity) {
    return StudyTaskDto(
      id: entity.id,
      title: entity.title,
      chapterName: entity.chapterName,
      priority: entity.priority,
      isCompleted: entity.isCompleted,
      estimatedMinutes: entity.estimatedMinutes,
      completedAtIso: entity.completedAt?.toIso8601String(),
    );
  }
}
