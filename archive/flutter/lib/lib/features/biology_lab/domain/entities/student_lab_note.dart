import 'package:flutter/foundation.dart';

/// كيان الملاحظات الشخصية للطالب المرتبطة بالرسم الحيوي أو بنقطة تفاعلية
@immutable
class StudentLabNote {
  final String noteId;
  final String diagramId;
  final String hotspotId;
  final String content;
  final DateTime updatedAt;

  const StudentLabNote({
    required this.noteId,
    required this.diagramId,
    required this.hotspotId,
    required this.content,
    required this.updatedAt,
  });

  StudentLabNote copyWith({
    String? noteId,
    String? diagramId,
    String? hotspotId,
    String? content,
    DateTime? updatedAt,
  }) {
    return StudentLabNote(
      noteId: noteId ?? this.noteId,
      diagramId: diagramId ?? this.diagramId,
      hotspotId: hotspotId ?? this.hotspotId,
      content: content ?? this.content,
      updatedAt: updatedAt ?? this.updatedAt,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is StudentLabNote && runtimeType == other.runtimeType && noteId == other.noteId;

  @override
  int get hashCode => noteId.hashCode;
}
