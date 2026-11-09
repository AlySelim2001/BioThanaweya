import 'package:flutter/material.dart';
import '../../models/achievement_badge.dart';
import '../../services/achievement_service.dart';
import '../widgets/confetti_particle_widget.dart';

class AchievementsScreen extends StatefulWidget {
  const AchievementsScreen({Key? key}) : super(key: key);

  @override
  State<AchievementsScreen> createState() => _AchievementsScreenState();
}

class _AchievementsScreenState extends State<AchievementsScreen> {
  final AchievementService _service = AchievementService.instance;
  final GlobalKey<ConfettiParticleWidgetState> _headerConfettiKey = GlobalKey();
  late List<AchievementBadge> _badges;
  BadgeCategory? _selectedCategory;

  @override
  void initState() {
    super.initState();
    _badges = _service.getInitialBadges();
  }

  void _showBadgeCelebration(AchievementBadge badge) {
    showDialog(
      context: context,
      builder: (ctx) => ConfettiParticleWidget(
        autoStart: badge.isUnlocked,
        child: AlertDialog(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: Row(
          children: [
            CircleAvatar(
              backgroundColor: badge.badgeColor.withOpacity(0.2),
              child: Icon(badge.iconData, color: badge.badgeColor),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    badge.titleArabic,
                    style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  Text(
                    badge.titleEnglish,
                    style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
                  ),
                ],
              ),
            ),
          ],
        ),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
              decoration: BoxDecoration(
                color: badge.rarity.primaryColor.withOpacity(0.15),
                borderRadius: BorderRadius.circular(12),
              ),
              child: Text(
                'الرتبة: ${badge.rarity.label}',
                style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: badge.rarity.primaryColor),
              ),
            ),
            const SizedBox(height: 12),
            Text(
              badge.descriptionArabic,
              style: const TextStyle(fontSize: 13, height: 1.4),
            ),
            const SizedBox(height: 8),
            Text(
              badge.descriptionEnglish,
              style: TextStyle(fontSize: 12, color: Colors.grey.shade700, height: 1.3),
            ),
            const SizedBox(height: 16),
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: badge.isUnlocked ? Colors.green.shade50 : Colors.amber.shade50,
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: badge.isUnlocked ? Colors.green : Colors.amber),
              ),
              child: Row(
                children: [
                  Icon(
                    badge.isUnlocked ? Icons.verified : Icons.lock_clock,
                    color: badge.isUnlocked ? Colors.green.shade800 : Colors.amber.shade800,
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: Text(
                      badge.isUnlocked
                          ? 'مبروك! تم فتح الوسام وتحقيق المعيار الوزاري بنجاح.'
                          : 'الوسام قيد الإنجاز! واصل المذاكرة لتحقيق معيار ${(badge.threshold * 100).toInt()}%.',
                      style: TextStyle(
                        fontSize: 12,
                        fontWeight: FontWeight.bold,
                        color: badge.isUnlocked ? Colors.green.shade900 : Colors.amber.shade900,
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('إغلاق'),
          ),
        ],
      ),
    ),
  );
}

  @override
  Widget build(BuildContext context) {
    final unlockedCount = _badges.where((b) => b.isUnlocked).length;
    final filteredBadges = _selectedCategory == null
        ? _badges
        : _badges.where((b) => b.category == _selectedCategory).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('لوحة الأوسمة والإنجازات - Student Achievements'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Hero Trophy Header Card with Interactive Confetti Celebration
            ConfettiParticleWidget(
              key: _headerConfettiKey,
              autoStart: true,
              child: GestureDetector(
                onTap: () => _headerConfettiKey.currentState?.play(),
                child: Container(
                  padding: const EdgeInsets.all(20),
                  decoration: BoxDecoration(
                    gradient: const LinearGradient(
                      colors: [Color(0xFF0F172A), Color(0xFF1E3A8A), Color(0xFF3B82F6)],
                      begin: Alignment.topLeft,
                      end: Alignment.bottomRight,
                    ),
                    borderRadius: BorderRadius.circular(20),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.blue.withOpacity(0.2),
                        blurRadius: 10,
                        offset: const Offset(0, 4),
                      ),
                    ],
                  ),
                  child: Column(
                    children: [
                      Row(
                        children: [
                          Container(
                            padding: const EdgeInsets.all(14),
                            decoration: BoxDecoration(
                              color: const Color(0xFFF59E0B).withOpacity(0.2),
                              shape: BoxShape.circle,
                              border: Border.all(color: const Color(0xFFF59E0B), width: 2),
                            ),
                            child: const Icon(Icons.emoji_events, color: Color(0xFFF59E0B), size: 36),
                          ),
                          const SizedBox(width: 16),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Text(
                                  'سجل التميز في بيولوجيا الثانوية (اضغط للاحتفال 🎉)',
                                  style: TextStyle(color: Colors.white70, fontSize: 13),
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  '$unlockedCount من أصل ${_badges.length} أوسمة مكتسبة',
                                  style: const TextStyle(
                                    color: Colors.white,
                                    fontSize: 18,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                const SizedBox(height: 4),
                                const Text(
                                  'تحفيز تفاعلي مرتبط بمعدلات الإتقان الوزارية',
                                  style: TextStyle(color: Colors.white60, fontSize: 11),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 16),
                      LinearProgressIndicator(
                        value: unlockedCount / _badges.length,
                        backgroundColor: Colors.white24,
                        color: const Color(0xFFF59E0B),
                        minHeight: 8,
                        borderRadius: BorderRadius.circular(4),
                      ),
                    ],
                  ),
                ),
              ),
            ),
            const SizedBox(height: 20),

            // Category Filter Chips
            SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: Row(
                children: [
                  _buildFilterChip('الكل (${_badges.length})', null),
                  const SizedBox(width: 8),
                  _buildFilterChip('الوراثة والـ DNA', BadgeCategory.genetics),
                  const SizedBox(width: 8),
                  _buildFilterChip('الفسيولوجيا والوظائف', BadgeCategory.physiology),
                  const SizedBox(width: 8),
                  _buildFilterChip('الاستمرارية والمراجعة', BadgeCategory.consistency),
                  const SizedBox(width: 8),
                  _buildFilterChip('الامتحانات الوزارية', BadgeCategory.examiner),
                ],
              ),
            ),
            const SizedBox(height: 18),

            // Badge Cards Grid/List
            ListView.separated(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: filteredBadges.length,
              separatorBuilder: (_, __) => const SizedBox(height: 12),
              itemBuilder: (context, index) {
                final badge = filteredBadges[index];
                return _buildBadgeCard(badge);
              },
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildFilterChip(String label, BadgeCategory? category) {
    final isSelected = _selectedCategory == category;
    return GestureDetector(
      onTap: () => setState(() => _selectedCategory = category),
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
        decoration: BoxDecoration(
          color: isSelected ? const Color(0xFF0D9488) : Colors.white,
          borderRadius: BorderRadius.circular(20),
          border: Border.all(
            color: isSelected ? const Color(0xFF0D9488) : Colors.grey.shade300,
          ),
          boxShadow: [
            if (isSelected)
              BoxShadow(
                color: const Color(0xFF0D9488).withOpacity(0.3),
                blurRadius: 6,
                offset: const Offset(0, 2),
              )
          ],
        ),
        child: Text(
          label,
          style: TextStyle(
            fontSize: 12,
            fontWeight: FontWeight.bold,
            color: isSelected ? Colors.white : Colors.grey.shade800,
          ),
        ),
      ),
    );
  }

  Widget _buildBadgeCard(AchievementBadge badge) {
    return GestureDetector(
      onTap: () => _showBadgeCelebration(badge),
      child: Card(
        elevation: badge.isUnlocked ? 3 : 1,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16),
          side: BorderSide(
            color: badge.isUnlocked
                ? badge.badgeColor.withOpacity(0.6)
                : Colors.grey.shade300,
            width: badge.isUnlocked ? 1.5 : 1.0,
          ),
        ),
        color: badge.isUnlocked ? Colors.white : Colors.grey.shade50,
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Row(
            children: [
              // Badge Icon Circle with Rarity Glow
              Stack(
                alignment: Alignment.center,
                children: [
                  Container(
                    width: 56,
                    height: 56,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      gradient: badge.isUnlocked
                          ? LinearGradient(
                              colors: [badge.badgeColor, badge.badgeColor.withOpacity(0.6)],
                              begin: Alignment.topLeft,
                              end: Alignment.bottomRight,
                            )
                          : LinearGradient(
                              colors: [Colors.grey.shade300, Colors.grey.shade400],
                            ),
                      boxShadow: [
                        if (badge.isUnlocked)
                          BoxShadow(
                            color: badge.badgeColor.withOpacity(0.4),
                            blurRadius: 8,
                            offset: const Offset(0, 3),
                          )
                      ],
                    ),
                    child: Icon(
                      badge.iconData,
                      color: Colors.white,
                      size: 28,
                    ),
                  ),
                  if (badge.isUnlocked)
                    Positioned(
                      bottom: 0,
                      right: 0,
                      child: Container(
                        padding: const EdgeInsets.all(2),
                        decoration: const BoxDecoration(
                          color: Colors.green,
                          shape: BoxShape.circle,
                        ),
                        child: const Icon(Icons.check, size: 12, color: Colors.white),
                      ),
                    ),
                ],
              ),
              const SizedBox(width: 16),

              // Details
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Expanded(
                          child: Text(
                            badge.titleArabic,
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.bold,
                              color: badge.isUnlocked ? Colors.black87 : Colors.grey.shade600,
                            ),
                          ),
                        ),
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                          decoration: BoxDecoration(
                            color: badge.rarity.primaryColor.withOpacity(0.12),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Text(
                            badge.rarity.label.split(' / ').first,
                            style: TextStyle(
                              fontSize: 10,
                              fontWeight: FontWeight.bold,
                              color: badge.rarity.primaryColor,
                            ),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 4),
                    Text(
                      badge.descriptionArabic,
                      style: TextStyle(
                        fontSize: 12,
                        color: badge.isUnlocked ? Colors.grey.shade800 : Colors.grey.shade500,
                        height: 1.3,
                      ),
                    ),
                    const SizedBox(height: 8),

                    // Progress bar or unlocked tag
                    if (badge.isUnlocked)
                      Row(
                        children: [
                          const Icon(Icons.verified, size: 14, color: Colors.green),
                          const SizedBox(width: 4),
                          const Text(
                            'تم الإنجاز بنجاح',
                            style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: Colors.green),
                          ),
                        ],
                      )
                    else
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text(
                                'التقدم: ${(badge.currentProgress * 100).toInt()}%',
                                style: TextStyle(fontSize: 10, color: Colors.grey.shade600),
                              ),
                              Text(
                                'الهدف: ${(badge.threshold * 100).toInt()}%',
                                style: TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: badge.badgeColor),
                              ),
                            ],
                          ),
                          const SizedBox(height: 4),
                          LinearProgressIndicator(
                            value: badge.progressFraction,
                            backgroundColor: Colors.grey.shade200,
                            color: badge.badgeColor,
                            minHeight: 5,
                            borderRadius: BorderRadius.circular(3),
                          ),
                        ],
                      ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
