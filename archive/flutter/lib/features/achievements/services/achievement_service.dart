import 'package:flutter/material.dart';
import '../models/achievement_badge.dart';

/// خدمة إدارة الإنجازات والأوسمة الرقمية لطلاب أحياء الثانوية العامة
class AchievementService {
  static final AchievementService instance = AchievementService._init();
  AchievementService._init();

  /// قائمة الأوسمة الرقمية الرسمية المعتمدة
  List<AchievementBadge> getInitialBadges({
    double geneticsMastery = 0.91,
    double respirationMastery = 0.95,
    double immunityMastery = 0.92,
    double supportMastery = 0.85,
    double overallMastery = 0.835,
    int streakDays = 7,
    int cardsReviewed = 24,
    double lastExamScore = 100.0,
  }) {
    return [
      // 1. وسام بطل الوراثة (90% فأكثر في البيولوجيا الجزيئية والجينات)
      AchievementBadge(
        id: 'genetics_titan_90',
        titleArabic: 'بطل الوراثة والبيولوجيا الجزيئية',
        titleEnglish: 'Genetics & Molecular Titan',
        descriptionArabic: 'حقق نسبة إتقان 90% فأكثر في فصول الـ DNA والـ RNA والتقنية الحيوية.',
        descriptionEnglish: 'Reach 90% or higher mastery in DNA, RNA, and Genetic Engineering.',
        iconData: Icons.biotech,
        badgeColor: const Color(0xFF7C3AED),
        rarity: BadgeRarity.legendary,
        category: BadgeCategory.genetics,
        threshold: 0.90,
        currentProgress: geneticsMastery,
        isUnlocked: geneticsMastery >= 0.90,
        unlockedDate: geneticsMastery >= 0.90 ? DateTime.now().subtract(const Duration(days: 1)) : null,
      ),

      // 2. وسام خبير الطاقة الخلوية والتنفس
      AchievementBadge(
        id: 'cellular_respiration_90',
        titleArabic: 'خبير الطاقة الخلوية والتنفس',
        titleEnglish: 'Cellular Engine Master',
        descriptionArabic: 'أتقن حسابات الـ 38 ATP ودورة كريبس وسلسلة نقل الإلكترون بنسبة 90%.',
        descriptionEnglish: 'Master 38 ATP glycolysis, Krebs cycle, and ETC calculations at 90%.',
        iconData: Icons.bolt,
        badgeColor: const Color(0xFFF59E0B),
        rarity: BadgeRarity.rare,
        category: BadgeCategory.physiology,
        threshold: 0.90,
        currentProgress: respirationMastery,
        isUnlocked: respirationMastery >= 0.90,
        unlockedDate: respirationMastery >= 0.90 ? DateTime.now().subtract(const Duration(days: 2)) : null,
      ),

      // 3. وسام درع المناعة الفولاذي
      AchievementBadge(
        id: 'immunology_shield_90',
        titleArabic: 'درع المناعة الفولاذي',
        titleEnglish: 'Immunology Shield',
        descriptionArabic: 'تفوق في آليات المناعة الخلطية والخلوية وخلايا T و B بنسبة 90%.',
        descriptionEnglish: 'Excel in Humoral & Cell-Mediated immunity and cytokines at 90%.',
        iconData: Icons.shield,
        badgeColor: const Color(0xFF0284C7),
        rarity: BadgeRarity.epic,
        category: BadgeCategory.physiology,
        threshold: 0.90,
        currentProgress: immunityMastery,
        isUnlocked: immunityMastery >= 0.90,
        unlockedDate: immunityMastery >= 0.90 ? DateTime.now().subtract(const Duration(days: 3)) : null,
      ),

      // 4. وسام مهندس الحركة والدعامة
      AchievementBadge(
        id: 'support_movement_master',
        titleArabic: 'مهندس الدعامة والحركة',
        titleEnglish: 'Skeletal Motion Architect',
        descriptionArabic: 'حقق 85% في ديناميكا انقباض العضلات والدعامة الفسيولوجية والتركيبية.',
        descriptionEnglish: 'Achieve 85% in muscle sliding filaments and plant turgor support.',
        iconData: Icons.fitness_center,
        badgeColor: const Color(0xFF0D9488),
        rarity: BadgeRarity.rare,
        category: BadgeCategory.physiology,
        threshold: 0.85,
        currentProgress: supportMastery,
        isUnlocked: supportMastery >= 0.85,
        unlockedDate: supportMastery >= 0.85 ? DateTime.now().subtract(const Duration(days: 4)) : null,
      ),

      // 5. وسام محارب الاستمرارية اليومية
      AchievementBadge(
        id: 'streak_warrior_7',
        titleArabic: 'محارب الاستمرارية اليومية',
        titleEnglish: '7-Day Streak Warrior',
        descriptionArabic: 'حافظ على وتيرة المذاكرة والمراجعة اليومية لمدة 7 أيام متتالية دون انقطاع.',
        descriptionEnglish: 'Maintain a 7-day continuous daily study and flashcard review streak.',
        iconData: Icons.local_fire_department,
        badgeColor: const Color(0xFFEF4444),
        rarity: BadgeRarity.epic,
        category: BadgeCategory.consistency,
        threshold: 7.0,
        currentProgress: streakDays.toDouble(),
        isUnlocked: streakDays >= 7,
        unlockedDate: streakDays >= 7 ? DateTime.now() : null,
      ),

      // 6. وسام فارس التكرار المتباعد
      AchievementBadge(
        id: 'sm2_virtuoso_20',
        titleArabic: 'فارس التكرار المتباعد (SM-2)',
        titleEnglish: 'Spaced Repetition Virtuoso',
        descriptionArabic: 'أتم مراجعة 20 بطاقة استذكار نشط بنجاح عبر خوارزمية التكرار المتباعد.',
        descriptionEnglish: 'Complete 20 active recall flashcard reviews via the SM-2 engine.',
        iconData: Icons.style,
        badgeColor: const Color(0xFF10B981),
        rarity: BadgeRarity.rare,
        category: BadgeCategory.consistency,
        threshold: 20.0,
        currentProgress: cardsReviewed.toDouble(),
        isUnlocked: cardsReviewed >= 20,
        unlockedDate: cardsReviewed >= 20 ? DateTime.now().subtract(const Duration(hours: 12)) : null,
      ),

      // 7. وسام متقن المعايير الوزارية
      AchievementBadge(
        id: 'ministry_rubric_100',
        titleArabic: 'متقن المعايير الوزارية',
        titleEnglish: 'Ministry Rubric Perfectionist',
        descriptionArabic: 'أحرز 100% في سؤال مقالي معتمد بمطابقة تامة لكافة الكلمات المفتاحية.',
        descriptionEnglish: 'Score 100% on a Ministry essay item matching all model keywords.',
        iconData: Icons.gavel,
        badgeColor: const Color(0xFFD97706),
        rarity: BadgeRarity.legendary,
        category: BadgeCategory.examiner,
        threshold: 100.0,
        currentProgress: lastExamScore,
        isUnlocked: lastExamScore >= 100.0,
        unlockedDate: lastExamScore >= 100.0 ? DateTime.now().subtract(const Duration(hours: 6)) : null,
      ),

      // 8. وسام العلامة الذهبي للأحياء
      AchievementBadge(
        id: 'overall_scholar_85',
        titleArabic: 'العلامة الذهبي للأحياء',
        titleEnglish: 'Grand Scholar of Biology',
        descriptionArabic: 'تجاوز معدل الإتقان العام 85% عبر جميع الفصول الـ 11 لمنهج الثانوية العامة.',
        descriptionEnglish: 'Surpass 85% overall curriculum mastery across all 11 chapters.',
        iconData: Icons.workspace_premium,
        badgeColor: const Color(0xFFEAB308),
        rarity: BadgeRarity.legendary,
        category: BadgeCategory.examiner,
        threshold: 0.85,
        currentProgress: overallMastery,
        isUnlocked: overallMastery >= 0.85,
        unlockedDate: overallMastery >= 0.85 ? DateTime.now() : null,
      ),
    ];
  }
}
