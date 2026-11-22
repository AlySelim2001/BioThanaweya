import '../entities/study_task.dart';
import '../entities/study_schedule.dart';
import '../entities/ai_coach_tip.dart';

/// العقد المجرد لمستودع خطط المذاكرة التكيفية والمساعد الذكي
abstract class StudyPlannerRepository {
  /// جلب جدول المذاكرة اليومي الحالي
  Future<StudySchedule> getTodaySchedule();

  /// توليد خطة مذاكرة ذكية بناءً على إخفاقات FSRS السابقة
  Future<StudySchedule> generateAdaptiveDailyPlan({List<String>? priorityChapters});

  /// تبديل حالة إنجاز مهمة محددة وحفظ التغيير محلياً
  Future<void> toggleTaskStatus(String taskId);

  /// إضافة مهمة مذاكرة مخصصة من قبل الطالب
  Future<void> addTask(StudyTask task);

  /// حذف مهمة
  Future<void> deleteTask(String taskId);

  /// جلب التوجيهات التشخيصية والعلاجية الحالية للمساعد الذكي
  Future<List<AiCoachTip>> getCoachInsights();

  /// حفظ وتحديث الجدول كاملاً محلياً
  Future<void> saveSchedule(StudySchedule schedule);
}
