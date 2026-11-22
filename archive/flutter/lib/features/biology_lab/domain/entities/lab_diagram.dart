import 'package:flutter/foundation.dart';
import 'hotspot_point.dart';

/// كيان الرسم التوضيحي الحيوي والمسار التفاعلي (Lab Diagram Entity)
@immutable
class LabDiagram {
  final String id;
  final String title;
  final String chapter;
  final String description;
  final String assetImagePath;
  final List<HotspotPoint> hotspots;

  const LabDiagram({
    required this.id,
    required this.title,
    required this.chapter,
    required this.description,
    required this.assetImagePath,
    required this.hotspots,
  });

  HotspotPoint? findHotspotById(String hotspotId) {
    try {
      return hotspots.firstWhere((h) => h.id == hotspotId);
    } catch (_) {
      return null;
    }
  }

  LabDiagram copyWith({
    String? id,
    String? title,
    String? chapter,
    String? description,
    String? assetImagePath,
    List<HotspotPoint>? hotspots,
  }) {
    return LabDiagram(
      id: id ?? this.id,
      title: title ?? this.title,
      chapter: chapter ?? this.chapter,
      description: description ?? this.description,
      assetImagePath: assetImagePath ?? this.assetImagePath,
      hotspots: hotspots ?? this.hotspots,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LabDiagram && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}
