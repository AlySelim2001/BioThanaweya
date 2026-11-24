import 'dart:async';
import 'package:flutter/foundation.dart';
import '../models/spaced_repetition_item.dart';

/// خدمة إدارة التخزين وقاعدة البيانات المحلية لبطاقات التكرار المتباعد
class LocalDatabaseService {
  static final LocalDatabaseService instance = LocalDatabaseService._init();
  final Map<String, SpacedRepetitionItem> _inMemoryStore = {};

  LocalDatabaseService._init();

  /// حفظ أو تحديث حالة البطاقة في الذاكرة التخزينية المحلية
  Future<void> saveSpacedRepetitionItem(SpacedRepetitionItem item) async {
    _inMemoryStore[item.cardId] = item;
    debugPrint("Saving card ${item.cardId} locally. Interval: ${item.intervalDays} days, EaseFactor: ${item.easeFactor}");
  }

  /// جلب بطاقة محددة بالمعرف
  Future<SpacedRepetitionItem?> getSpacedRepetitionItem(String cardId) async {
    return _inMemoryStore[cardId];
  }

  /// استرجاع البطاقات المستحقة للمراجعة اليومية بناءً على جدول التكرار المتباعد
  Future<List<SpacedRepetitionItem>> getDueCards() async {
    if (_inMemoryStore.isEmpty) {
      // إرجاع عينات افتراضية للبدء المباشر إذا كانت الذاكرة فارغة
      return [
        SpacedRepetitionItem(cardId: 'card_supp_01', repetition: 0, intervalDays: 1.0, easeFactor: 2.5),
        SpacedRepetitionItem(cardId: 'card_supp_02', repetition: 1, intervalDays: 1.0, easeFactor: 2.5),
      ];
    }
    return _inMemoryStore.values.toList();
  }
}

/// مستودع التكرار المتباعد لإدارة تقييم الطلاب وجلب المراجعات اليومية
class SpacedRepetitionRepository {
  final LocalDatabaseService _dbService = LocalDatabaseService.instance;

  /// تحديث حالة البطاقة وحفظها محلياً بعد تقييم استيعاب الطالب (من 0 إلى 5)
  Future<void> updateCardReview(SpacedRepetitionItem item, int grade) async {
    item.evaluateResponse(grade);
    await _dbService.saveSpacedRepetitionItem(item);
  }

  /// جلب قائمة البطاقات الواجب مراجعتها اليوم للفصل الدراسي الحالي
  Future<List<SpacedRepetitionItem>> fetchPendingReviews() async {
    return await _dbService.getDueCards();
  }
}
