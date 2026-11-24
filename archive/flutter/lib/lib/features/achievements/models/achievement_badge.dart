import 'package:flutter/material.dart';

enum BadgeRarity {
  common('شائع / Common', Color(0xFF64748B)),
  rare('نادر / Rare', Color(0xFF0284C7)),
  epic('ملحمي / Epic', Color(0xFF7C3AED)),
  legendary('أسطوري / Legendary', Color(0xFFF59E0B));

  final String label;
  final Color primaryColor;
  const BadgeRarity(this.label, this.primaryColor);
}

enum BadgeCategory {
  genetics('الوراثة والبيولوجيا الجزيئية / Genetics'),
  physiology('التركيب والوظيفة / Physiology'),
  consistency('الاستمرارية والمراجعة / Consistency'),
  examiner('الامتحانات والتفوق / Ministry Exams');

  final String title;
  const BadgeCategory(this.title);
}

class AchievementBadge {
  final String id;
  final String titleArabic;
  final String titleEnglish;
  final String descriptionArabic;
  final String descriptionEnglish;
  final IconData iconData;
  final Color badgeColor;
  final BadgeRarity rarity;
  final BadgeCategory category;
  final double threshold;
  final double currentProgress;
  final bool isUnlocked;
  final DateTime? unlockedDate;

  const AchievementBadge({
    required this.id,
    required this.titleArabic,
    required this.titleEnglish,
    required this.descriptionArabic,
    required this.descriptionEnglish,
    required this.iconData,
    required this.badgeColor,
    required this.rarity,
    required this.category,
    required this.threshold,
    required this.currentProgress,
    this.isUnlocked = false,
    this.unlockedDate,
  });

  AchievementBadge copyWith({
    String? id,
    String? titleArabic,
    String? titleEnglish,
    String? descriptionArabic,
    String? descriptionEnglish,
    IconData? iconData,
    Color? badgeColor,
    BadgeRarity? rarity,
    BadgeCategory? category,
    double? threshold,
    double? currentProgress,
    bool? isUnlocked,
    DateTime? unlockedDate,
  }) {
    return AchievementBadge(
      id: id ?? this.id,
      titleArabic: titleArabic ?? this.titleArabic,
      titleEnglish: titleEnglish ?? this.titleEnglish,
      descriptionArabic: descriptionArabic ?? this.descriptionArabic,
      descriptionEnglish: descriptionEnglish ?? this.descriptionEnglish,
      iconData: iconData ?? this.iconData,
      badgeColor: badgeColor ?? this.badgeColor,
      rarity: rarity ?? this.rarity,
      category: category ?? this.category,
      threshold: threshold ?? this.threshold,
      currentProgress: currentProgress ?? this.currentProgress,
      isUnlocked: isUnlocked ?? this.isUnlocked,
      unlockedDate: unlockedDate ?? this.unlockedDate,
    );
  }

  double get progressFraction {
    if (threshold == 0) return 1.0;
    final frac = currentProgress / threshold;
    return frac > 1.0 ? 1.0 : frac;
  }
}
