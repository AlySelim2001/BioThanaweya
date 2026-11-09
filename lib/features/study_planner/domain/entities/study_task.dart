import 'package:flutter/foundation.dart';

/// كيان مهمة المذاكرة اليومية
@immutable
class StudyTask {
  final String id;
  final String title;
  final String chapterName;
  final String priority; // 'High', 'Medium', 'Low'
  final bool isCompleted;
  final int estimatedMinutes;
  final DateTime? completedAt;

  const StudyTask({
    required this.id,
    required this.title,
    required this.chapterName,
    required this.priority,
    required this.isCompleted,
    required this.estimatedMinutes,
    this.completedAt,
  });

  StudyTask copyWith({
    String? id,
    String? title,
    String? chapterName,
    String? priority,
    bool? isCompleted,
    int? estimatedMinutes,
    DateTime? completedAt,
  }) {
    return StudyTask(
      id: id ?? this.id,
      title: title ?? this.title,
      chapterName: chapterName ?? this.chapterName,
      priority: priority ?? this.priority,
      isCompleted: isCompleted ?? this.isCompleted,
      estimatedMinutes: estimatedMinutes ?? this.estimatedMinutes,
      completedAt: completedAt ?? this.completedAt,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is StudyTask && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}
