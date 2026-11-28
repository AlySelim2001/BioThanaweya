import '../../domain/entities/study_task.dart';
import '../../domain/entities/ai_coach_tip.dart';

/// خوارزمية توليد خطط المذاكرة التكيفية والمساعد الذكي المحلي بناءً على تحليل أخطاء FSRS
class AdaptivePlanGenerator {
  /// توليد مهام يومية بناءً على الفصول التي بها أخطاء متكررة
  static List<StudyTask> generateAdaptiveTasks(List<String> weakChapters) {
    final List<StudyTask> tasks = [];

    if (weakChapters.isEmpty) {
      tasks.add(
        const StudyTask(
          id: 'task_support_review',
          title: 'مراجعة خيوط الميوسين وحسابات القطعة العضلية',
          chapterName: 'الفصل الأول: الدعامة والحركة',
          priority: 'Medium',
          isCompleted: false,
          estimatedMinutes: 40,
        ),
      );
      tasks.add(
        const StudyTask(
          id: 'task_hormones_review',
          title: 'تثبيت هرمونات التغذية الراجعة والغدة النخامية',
          chapterName: 'الفصل الثاني: التنسيق الهرموني',
          priority: 'Medium',
          isCompleted: false,
          estimatedMinutes: 35,
        ),
      );
    } else {
      for (int i = 0; i < weakChapters.length; i++) {
        final chapter = weakChapters[i];
        tasks.add(
          StudyTask(
            id: 'task_${chapter.replaceAll(' ', '_')}_fsrs_${DateTime.now().millisecondsSinceEpoch + i}',
            title: 'استدراك وعلاج فخاخ FSRS المتكررة في $chapter',
            chapterName: chapter,
            priority: 'High',
            isCompleted: false,
            estimatedMinutes: 50,
          ),
        );
        tasks.add(
          StudyTask(
            id: 'task_lab_${chapter.replaceAll(' ', '_')}_$i',
            title: 'استكشاف النقاط الحساسة في مختبر الرسومات لـ $chapter',
            chapterName: chapter,
            priority: 'Medium',
            isCompleted: false,
            estimatedMinutes: 25,
          ),
        );
      }
    }

    // إضافة مهمة ثابتة للمحاكي الوزاري اليومي
    tasks.add(
      const StudyTask(
        id: 'task_daily_ministry_quiz',
        title: 'إجراء محاكي الاختبارات الوزارية الشامل (20 سؤالاً)',
        chapterName: 'المحاكي الوزاري الفائق',
        priority: 'High',
        isCompleted: false,
        estimatedMinutes: 30,
      ),
    );

    return tasks;
  }

  /// توليد نصائح وتحليلات تشخيصية محلية للمساعد الذكي
  static List<AiCoachTip> generateLocalCoachInsights(List<String> weakChapters) {
    if (weakChapters.isEmpty) {
      return [
        const AiCoachTip(
          id: 'tip_mastery_general',
          title: 'أداء مستقر وممتاز! 🌟',
          diagnosticMessage: 'مستوى استقرار الذاكرة وحفظ المفاهيم لديك مرتفع عبر الفصول الحالية وفق منحنى FSRS.',
          recommendedAction: 'استمر في حل اختبارات التحدي الشاملة لزيادة سرعة التمييز بين بدائل الاختيار من متعدد.',
          targetChapter: 'عام لكافة الفصول',
          urgencyLevel: 'Info',
        ),
        const AiCoachTip(
          id: 'tip_speed_enhancement',
          title: 'كفاءة إدارة زمن الامتحان ⏱️',
          diagnosticMessage: 'معدل حلك للأسئلة يقارب 45 ثانية لكل سؤال، وهو معدل استثنائي يمنحك وقتاً كافياً للمراجعة النهائية.',
          recommendedAction: 'ركز على أسئلة الربط بين الفصول لضمان العلامة الكاملة.',
          targetChapter: 'المراجعات الشاملة',
          urgencyLevel: 'Info',
        ),
      ];
    }

    return weakChapters.map((chapter) {
      return AiCoachTip(
        id: 'tip_${chapter.replaceAll(' ', '_')}',
        title: 'تنبيه تركيز فوري من واضعي الامتحانات ⚠️',
        diagnosticMessage: 'رصدت خوارزمية FSRS تكرار الخطأ في مفاهيم وتفاصيل دقيقة في $chapter.',
        recommendedAction: 'افتح "مختبر الرسومات الحيوية" واقضِ 15 دقيقة في دراسة النقاط الحساسة وفخاخ الوزارة لهذا الفصل ثم أعد اختبار الأخطاء.',
        targetChapter: chapter,
        urgencyLevel: 'Warning',
      );
    }).toList();
  }
}
