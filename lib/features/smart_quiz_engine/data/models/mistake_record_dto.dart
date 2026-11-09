/// سجل تتبع الخطأ في بنك الأسئلة وفق خوارزمية التكرار التكيفي (FSRS Mistakes Tracking)
class MistakeRecordDto {
  final String questionId;
  final int chapterId;
  final int failCount;
  final int consecutiveCorrectCount;
  final String lastFailedAtIso;
  final String nextReviewDueIso;
  final double stabilityDays;
  final bool isResolved;

  MistakeRecordDto({
    required this.questionId,
    required this.chapterId,
    required this.failCount,
    required this.consecutiveCorrectCount,
    required this.lastFailedAtIso,
    required this.nextReviewDueIso,
    required this.stabilityDays,
    required this.isResolved,
  });

  factory MistakeRecordDto.fromJson(Map<String, dynamic> json) {
    return MistakeRecordDto(
      questionId: json['questionId']?.toString() ?? '',
      chapterId: (json['chapterId'] as num?)?.toInt() ?? 1,
      failCount: (json['failCount'] as num?)?.toInt() ?? 1,
      consecutiveCorrectCount: (json['consecutiveCorrectCount'] as num?)?.toInt() ?? 0,
      lastFailedAtIso: json['lastFailedAtIso']?.toString() ?? DateTime.now().toIso8601String(),
      nextReviewDueIso: json['nextReviewDueIso']?.toString() ?? DateTime.now().toIso8601String(),
      stabilityDays: (json['stabilityDays'] as num?)?.toDouble() ?? 1.0,
      isResolved: json['isResolved'] as bool? ?? false,
    );
  }

  Map<String, dynamic> toJson() => {
        'questionId': questionId,
        'chapterId': chapterId,
        'failCount': failCount,
        'consecutiveCorrectCount': consecutiveCorrectCount,
        'lastFailedAtIso': lastFailedAtIso,
        'nextReviewDueIso': nextReviewDueIso,
        'stabilityDays': stabilityDays,
        'isResolved': isResolved,
      };

  /// خوارزمية تسجيل إجابة جديدة على السؤال الخطأ
  /// إذا أجاب الطالب خطأ مجدداً: تزداد الأولوية وتقل فترة الاستقرار
  /// إذا أجاب صح في جلسة الاستدراك لمرتين متتاليتين: يعتبر الخطأ مُعالجاً (Resolved)
  MistakeRecordDto registerAttempt({required bool wasCorrect}) {
    final now = DateTime.now();
    if (!wasCorrect) {
      return MistakeRecordDto(
        questionId: questionId,
        chapterId: chapterId,
        failCount: failCount + 1,
        consecutiveCorrectCount: 0,
        lastFailedAtIso: now.toIso8601String(),
        // إعادة الجدولة الفورية خلال الجلسة القادمة (أقل من يوم)
        nextReviewDueIso: now.add(const Duration(hours: 4)).toIso8601String(),
        stabilityDays: 0.5,
        isResolved: false,
      );
    } else {
      final newConsecutive = consecutiveCorrectCount + 1;
      final newStability = stabilityDays * 2.2;
      final resolved = newConsecutive >= 2;
      return MistakeRecordDto(
        questionId: questionId,
        chapterId: chapterId,
        failCount: failCount,
        consecutiveCorrectCount: newConsecutive,
        lastFailedAtIso: lastFailedAtIso,
        nextReviewDueIso: now.add(Duration(hours: (newStability * 24).toInt())).toIso8601String(),
        stabilityDays: newStability,
        isResolved: resolved,
      );
    }
  }

  /// التحقق مما إذا كان السؤال مستحقاً للمراجعة الآن
  bool isDueForReview() {
    if (isResolved) return false;
    final due = DateTime.tryParse(nextReviewDueIso) ?? DateTime.now();
    return DateTime.now().isAfter(due) || DateTime.now().isAtSameMomentAs(due);
  }
}
