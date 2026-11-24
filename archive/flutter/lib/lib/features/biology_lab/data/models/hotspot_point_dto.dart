import '../../domain/entities/hotspot_point.dart';

/// DTO لنقاط التفاعل على الرسم الحيوي
class HotspotPointDto {
  final String id;
  final String title;
  final String detailedExplanation;
  final String ministryTrapNote;
  final double xPositionRatio;
  final double yPositionRatio;

  const HotspotPointDto({
    required this.id,
    required this.title,
    required this.detailedExplanation,
    required this.ministryTrapNote,
    required this.xPositionRatio,
    required this.yPositionRatio,
  });

  factory HotspotPointDto.fromJson(Map<String, dynamic> json) {
    return HotspotPointDto(
      id: json['id']?.toString() ?? '',
      title: json['title']?.toString() ?? '',
      detailedExplanation: json['detailedExplanation']?.toString() ?? '',
      ministryTrapNote: json['ministryTrapNote']?.toString() ?? '',
      xPositionRatio: (json['xPositionRatio'] as num?)?.toDouble() ?? 0.0,
      yPositionRatio: (json['yPositionRatio'] as num?)?.toDouble() ?? 0.0,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'title': title,
        'detailedExplanation': detailedExplanation,
        'ministryTrapNote': ministryTrapNote,
        'xPositionRatio': xPositionRatio,
        'yPositionRatio': yPositionRatio,
      };

  HotspotPoint toEntity() {
    return HotspotPoint(
      id: id,
      title: title,
      detailedExplanation: detailedExplanation,
      ministryTrapNote: ministryTrapNote,
      xPositionRatio: xPositionRatio,
      yPositionRatio: yPositionRatio,
    );
  }

  factory HotspotPointDto.fromEntity(HotspotPoint entity) {
    return HotspotPointDto(
      id: entity.id,
      title: entity.title,
      detailedExplanation: entity.detailedExplanation,
      ministryTrapNote: entity.ministryTrapNote,
      xPositionRatio: entity.xPositionRatio,
      yPositionRatio: entity.yPositionRatio,
    );
  }
}
