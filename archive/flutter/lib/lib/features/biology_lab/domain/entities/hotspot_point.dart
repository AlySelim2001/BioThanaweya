import 'package:flutter/foundation.dart';

/// كيان النقطة التفاعلية الحساسة على الرسم الحيوي (Hotspot Point)
@immutable
class HotspotPoint {
  final String id;
  final String title;
  final String detailedExplanation;
  final String ministryTrapNote;
  final double xPositionRatio; // النسبة المئوية للإحداثي الأفقي (0.0 إلى 1.0)
  final double yPositionRatio; // النسبة المئوية للإحداثي الرأسي (0.0 إلى 1.0)

  const HotspotPoint({
    required this.id,
    required this.title,
    required this.detailedExplanation,
    required this.ministryTrapNote,
    required this.xPositionRatio,
    required this.yPositionRatio,
  });

  HotspotPoint copyWith({
    String? id,
    String? title,
    String? detailedExplanation,
    String? ministryTrapNote,
    double? xPositionRatio,
    double? yPositionRatio,
  }) {
    return HotspotPoint(
      id: id ?? this.id,
      title: title ?? this.title,
      detailedExplanation: detailedExplanation ?? this.detailedExplanation,
      ministryTrapNote: ministryTrapNote ?? this.ministryTrapNote,
      xPositionRatio: xPositionRatio ?? this.xPositionRatio,
      yPositionRatio: yPositionRatio ?? this.yPositionRatio,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is HotspotPoint && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}
