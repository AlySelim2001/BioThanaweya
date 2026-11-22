import '../../domain/entities/student_lab_note.dart';

/// DTO لملاحظات الطالب الشخصية مع التسلسل لـ JSON
class StudentLabNoteDto {
  final String noteId;
  final String diagramId;
  final String hotspotId;
  final String content;
  final String updatedAtIso;

  const StudentLabNoteDto({
    required this.noteId,
    required this.diagramId,
    required this.hotspotId,
    required this.content,
    required this.updatedAtIso,
  });

  factory StudentLabNoteDto.fromJson(Map<String, dynamic> json) {
    return StudentLabNoteDto(
      noteId: json['noteId']?.toString() ?? '',
      diagramId: json['diagramId']?.toString() ?? '',
      hotspotId: json['hotspotId']?.toString() ?? '',
      content: json['content']?.toString() ?? '',
      updatedAtIso: json['updatedAtIso']?.toString() ?? DateTime.now().toIso8601String(),
    );
  }

  Map<String, dynamic> toJson() => {
        'noteId': noteId,
        'diagramId': diagramId,
        'hotspotId': hotspotId,
        'content': content,
        'updatedAtIso': updatedAtIso,
      };

  StudentLabNote toEntity() {
    return StudentLabNote(
      noteId: noteId,
      diagramId: diagramId,
      hotspotId: hotspotId,
      content: content,
      updatedAt: DateTime.tryParse(updatedAtIso) ?? DateTime.now(),
    );
  }

  factory StudentLabNoteDto.fromEntity(StudentLabNote entity) {
    return StudentLabNoteDto(
      noteId: entity.noteId,
      diagramId: entity.diagramId,
      hotspotId: entity.hotspotId,
      content: entity.content,
      updatedAtIso: entity.updatedAt.toIso8601String(),
    );
  }
}
