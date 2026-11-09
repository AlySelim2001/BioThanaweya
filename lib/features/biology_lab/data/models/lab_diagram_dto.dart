import '../../domain/entities/lab_diagram.dart';
import 'hotspot_point_dto.dart';

/// DTO للرسومات الحيوية والمسارات المنهجية
class LabDiagramDto {
  final String id;
  final String title;
  final String chapter;
  final String description;
  final String assetImagePath;
  final List<HotspotPointDto> hotspots;

  const LabDiagramDto({
    required this.id,
    required this.title,
    required this.chapter,
    required this.description,
    required this.assetImagePath,
    required this.hotspots,
  });

  factory LabDiagramDto.fromJson(Map<String, dynamic> json) {
    final rawHotspots = (json['hotspots'] as List<dynamic>?) ?? [];
    return LabDiagramDto(
      id: json['id']?.toString() ?? '',
      title: json['title']?.toString() ?? '',
      chapter: json['chapter']?.toString() ?? '',
      description: json['description']?.toString() ?? '',
      assetImagePath: json['assetImagePath']?.toString() ?? '',
      hotspots: rawHotspots
          .map((h) => HotspotPointDto.fromJson(h as Map<String, dynamic>))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'title': title,
        'chapter': chapter,
        'description': description,
        'assetImagePath': assetImagePath,
        'hotspots': hotspots.map((h) => h.toJson()).toList(),
      };

  LabDiagram toEntity() {
    return LabDiagram(
      id: id,
      title: title,
      chapter: chapter,
      description: description,
      assetImagePath: assetImagePath,
      hotspots: hotspots.map((h) => h.toEntity()).toList(),
    );
  }

  factory LabDiagramDto.fromEntity(LabDiagram entity) {
    return LabDiagramDto(
      id: entity.id,
      title: entity.title,
      chapter: entity.chapter,
      description: entity.description,
      assetImagePath: entity.assetImagePath,
      hotspots: entity.hotspots.map((h) => HotspotPointDto.fromEntity(h)).toList(),
    );
  }
}
