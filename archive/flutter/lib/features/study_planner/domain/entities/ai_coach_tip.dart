import 'package:flutter/foundation.dart';

/// كيان توجيهات المساعد الذكي المحلي المستند إلى تحليل أخطاء FSRS
@immutable
class AiCoachTip {
  final String id;
  final String title;
  final String diagnosticMessage;
  final String recommendedAction;
  final String targetChapter;
  final String urgencyLevel; // 'Critical', 'Warning', 'Info'

  const AiCoachTip({
    required this.id,
    required this.title,
    required this.diagnosticMessage,
    required this.recommendedAction,
    required this.targetChapter,
    this.urgencyLevel = 'Warning',
  });

  AiCoachTip copyWith({
    String? id,
    String? title,
    String? diagnosticMessage,
    String? recommendedAction,
    String? targetChapter,
    String? urgencyLevel,
  }) {
    return AiCoachTip(
      id: id ?? this.id,
      title: title ?? this.title,
      diagnosticMessage: diagnosticMessage ?? this.diagnosticMessage,
      recommendedAction: recommendedAction ?? this.recommendedAction,
      targetChapter: targetChapter ?? this.targetChapter,
      urgencyLevel: urgencyLevel ?? this.urgencyLevel,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is AiCoachTip && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}
