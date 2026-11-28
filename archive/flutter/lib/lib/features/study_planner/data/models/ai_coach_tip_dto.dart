import '../../domain/entities/ai_coach_tip.dart';

/// DTO لنصائح وتوجيهات المساعد الذكي
class AiCoachTipDto {
  final String id;
  final String title;
  final String diagnosticMessage;
  final String recommendedAction;
  final String targetChapter;
  final String urgencyLevel;

  const AiCoachTipDto({
    required this.id,
    required this.title,
    required this.diagnosticMessage,
    required this.recommendedAction,
    required this.targetChapter,
    required this.urgencyLevel,
  });

  factory AiCoachTipDto.fromJson(Map<String, dynamic> json) {
    return AiCoachTipDto(
      id: json['id']?.toString() ?? '',
      title: json['title']?.toString() ?? '',
      diagnosticMessage: json['diagnosticMessage']?.toString() ?? '',
      recommendedAction: json['recommendedAction']?.toString() ?? '',
      targetChapter: json['targetChapter']?.toString() ?? 'عام',
      urgencyLevel: json['urgencyLevel']?.toString() ?? 'Warning',
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'title': title,
        'diagnosticMessage': diagnosticMessage,
        'recommendedAction': recommendedAction,
        'targetChapter': targetChapter,
        'urgencyLevel': urgencyLevel,
      };

  AiCoachTip toEntity() {
    return AiCoachTip(
      id: id,
      title: title,
      diagnosticMessage: diagnosticMessage,
      recommendedAction: recommendedAction,
      targetChapter: targetChapter,
      urgencyLevel: urgencyLevel,
    );
  }

  factory AiCoachTipDto.fromEntity(AiCoachTip entity) {
    return AiCoachTipDto(
      id: entity.id,
      title: entity.title,
      diagnosticMessage: entity.diagnosticMessage,
      recommendedAction: entity.recommendedAction,
      targetChapter: entity.targetChapter,
      urgencyLevel: entity.urgencyLevel,
    );
  }
}
