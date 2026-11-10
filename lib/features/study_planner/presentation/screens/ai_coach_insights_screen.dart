import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../controllers/planner_providers.dart';
import '../../domain/entities/ai_coach_tip.dart';
import '../../../biology_lab/presentation/screens/biology_lab_screen.dart';
import '../../../smart_quiz_engine/presentation/controllers/quiz_providers.dart';
import '../../../smart_quiz_engine/presentation/screens/smart_quiz_screen.dart';

/// شاشة تحليلات وتوجيهات المساعد الذكي (AI Coach Insights Screen)
/// تعرض التحليلات التشخيصية المستندة إلى خوارزمية FSRS المتقدمة
class AiCoachInsightsScreen extends ConsumerStatefulWidget {
  const AiCoachInsightsScreen({super.key});

  @override
  ConsumerState<AiCoachInsightsScreen> createState() => _AiCoachInsightsScreenState();
}

class _AiCoachInsightsScreenState extends ConsumerState<AiCoachInsightsScreen> {
  String _selectedFilter = 'All'; // 'All', 'Critical', 'Warning', 'Info'

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(studyPlannerControllerProvider);
    final allTips = state.coachTips;

    final filteredTips = allTips.where((tip) {
      if (_selectedFilter == 'All') return true;
      return tip.urgencyLevel == _selectedFilter;
    }).toList();

    const backgroundColor = Color(0xFF12121A);
    const surfaceColor = Color(0xFF1E1E2C);
    const cardBgColor = Color(0xFF252536);
    const primaryGreen = Colors.green;

    final criticalCount = allTips.where((t) => t.urgencyLevel == 'Critical').length;
    final warningCount = allTips.where((t) => t.urgencyLevel == 'Warning').length;

    return Scaffold(
      backgroundColor: backgroundColor,
      appBar: AppBar(
        title: const Text(
          'تحليلات وتوجيهات المساعد الذكي',
          style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
        ),
        backgroundColor: surfaceColor,
        elevation: 0,
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator(color: primaryGreen))
          : ListView(
              padding: const EdgeInsets.all(16),
              children: [
                // 1. بطاقات ملخص المؤشرات التشخيصية (Bento Grid Overview)
                Row(
                  children: [
                    Expanded(
                      child: Container(
                        padding: const EdgeInsets.all(16),
                        decoration: BoxDecoration(
                          color: surfaceColor,
                          borderRadius: BorderRadius.circular(16),
                          border: Border.all(color: Colors.amber.withValues(alpha: 0.3)),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withValues(alpha: 0.25),
                              blurRadius: 8,
                              offset: const Offset(0, 3),
                            ),
                          ],
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Row(
                              children: [
                                Icon(Icons.psychology, color: Colors.amber, size: 20),
                                SizedBox(width: 6),
                                Text('إجمالي التوجيهات', style: TextStyle(color: Colors.grey, fontSize: 11)),
                              ],
                            ),
                            const SizedBox(height: 10),
                            Text(
                              '${allTips.length}',
                              style: const TextStyle(color: Colors.white, fontSize: 26, fontWeight: FontWeight.bold),
                            ),
                            const SizedBox(height: 4),
                            const Text('تحليلات نشطة مستمرة', style: TextStyle(color: Colors.amber, fontSize: 10)),
                          ],
                        ),
                      ),
                    ),
                    const SizedBox(width: 12),
                    Expanded(
                      child: Container(
                        padding: const EdgeInsets.all(16),
                        decoration: BoxDecoration(
                          color: surfaceColor,
                          borderRadius: BorderRadius.circular(16),
                          border: Border.all(
                            color: criticalCount > 0
                                ? Colors.redAccent.withValues(alpha: 0.4)
                                : Colors.greenAccent.withValues(alpha: 0.3),
                          ),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withValues(alpha: 0.25),
                              blurRadius: 8,
                              offset: const Offset(0, 3),
                            ),
                          ],
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              children: [
                                Icon(
                                  criticalCount > 0 ? Icons.error_outline : Icons.verified_rounded,
                                  color: criticalCount > 0 ? Colors.redAccent : Colors.greenAccent,
                                  size: 20,
                                ),
                                const SizedBox(width: 6),
                                const Text('تنبيهات عاجلة', style: TextStyle(color: Colors.grey, fontSize: 11)),
                              ],
                            ),
                            const SizedBox(height: 10),
                            Text(
                              '${criticalCount + warningCount}',
                              style: TextStyle(
                                color: (criticalCount + warningCount) > 0 ? Colors.redAccent : Colors.greenAccent,
                                fontSize: 26,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            const SizedBox(height: 4),
                            Text(
                              (criticalCount + warningCount) > 0 ? 'تتطلب استدراكاً فورياً' : 'أداء مستقر ومثالي',
                              style: TextStyle(
                                color: (criticalCount + warningCount) > 0 ? Colors.redAccent : Colors.greenAccent,
                                fontSize: 10,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),

                const SizedBox(height: 18),

                // 2. شريط تصنيف التوجيهات حسب الأهمية (Urgency Filter Chips)
                SingleChildScrollView(
                  scrollDirection: Axis.horizontal,
                  child: Row(
                    children: [
                      _buildFilterChip(label: 'الكل (${allTips.length})', value: 'All'),
                      const SizedBox(width: 8),
                      _buildFilterChip(label: 'حرجة ($criticalCount)', value: 'Critical'),
                      const SizedBox(width: 8),
                      _buildFilterChip(label: 'تحذيرات FSRS ($warningCount)', value: 'Warning'),
                      const SizedBox(width: 8),
                      _buildFilterChip(
                        label: 'إرشادات عامة (${allTips.where((t) => t.urgencyLevel == 'Info').length})',
                        value: 'Info',
                      ),
                    ],
                  ),
                ),

                const SizedBox(height: 16),

                // 3. قائمة التوجيهات التشخيصية المفصلة
                if (filteredTips.isEmpty)
                  Container(
                    padding: const EdgeInsets.symmetric(vertical: 40, horizontal: 20),
                    alignment: Alignment.center,
                    child: Column(
                      children: [
                        Icon(Icons.check_circle_outline, color: Colors.greenAccent.withValues(alpha: 0.6), size: 48),
                        const SizedBox(height: 12),
                        const Text(
                          'لا توجد توجيهات ضمن هذا التصنيف حالياً.',
                          style: TextStyle(color: Colors.grey, fontSize: 13),
                        ),
                      ],
                    ),
                  )
                else
                  ...filteredTips.map((tip) {
                    final isCritical = tip.urgencyLevel == 'Critical';
                    final isWarning = tip.urgencyLevel == 'Warning';

                    final borderColor = isCritical
                        ? Colors.redAccent.withValues(alpha: 0.5)
                        : isWarning
                            ? Colors.amber.withValues(alpha: 0.4)
                            : Colors.blueAccent.withValues(alpha: 0.3);

                    final accentColor = isCritical
                        ? Colors.redAccent
                        : isWarning
                            ? Colors.amber
                            : Colors.blueAccent;

                    return Container(
                      margin: const EdgeInsets.only(bottom: 14),
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: cardBgColor,
                        borderRadius: BorderRadius.circular(14),
                        border: Border.all(color: borderColor),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withValues(alpha: 0.2),
                            blurRadius: 6,
                            offset: const Offset(0, 3),
                          ),
                        ],
                      ),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Container(
                                padding: const EdgeInsets.all(8),
                                decoration: BoxDecoration(
                                  color: accentColor.withValues(alpha: 0.15),
                                  borderRadius: BorderRadius.circular(10),
                                ),
                                child: Icon(
                                  isCritical
                                      ? Icons.warning_rounded
                                      : isWarning
                                          ? Icons.tips_and_updates
                                          : Icons.info_outline,
                                  color: accentColor,
                                  size: 20,
                                ),
                              ),
                              const SizedBox(width: 10),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      tip.title,
                                      style: TextStyle(
                                        color: accentColor,
                                        fontWeight: FontWeight.bold,
                                        fontSize: 14,
                                      ),
                                    ),
                                    const SizedBox(height: 3),
                                    Text(
                                      tip.targetChapter,
                                      style: const TextStyle(color: Colors.grey, fontSize: 11),
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 12),
                          Text(
                            tip.diagnosticMessage,
                            style: const TextStyle(color: Colors.white70, fontSize: 13, height: 1.45),
                          ),
                          const SizedBox(height: 12),
                          Container(
                            padding: const EdgeInsets.all(10),
                            decoration: BoxDecoration(
                              color: primaryGreen.withValues(alpha: 0.12),
                              borderRadius: BorderRadius.circular(10),
                              border: Border.all(color: primaryGreen.withValues(alpha: 0.3)),
                            ),
                            child: Row(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Icon(Icons.recommend, color: Colors.greenAccent, size: 16),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: Text(
                                    tip.recommendedAction,
                                    style: const TextStyle(
                                      color: Colors.greenAccent,
                                      fontSize: 12,
                                      fontWeight: FontWeight.w600,
                                      height: 1.35,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 14),
                          Row(
                            children: [
                              Expanded(
                                child: OutlinedButton.icon(
                                  style: OutlinedButton.styleFrom(
                                    foregroundColor: Colors.greenAccent,
                                    side: BorderSide(color: Colors.greenAccent.withValues(alpha: 0.5)),
                                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                                    padding: const EdgeInsets.symmetric(vertical: 8),
                                  ),
                                  icon: const Icon(Icons.biotech, size: 16),
                                  label: const Text('مختبر الرسومات', style: TextStyle(fontSize: 12)),
                                  onPressed: () {
                                    Navigator.push(
                                      context,
                                      MaterialPageRoute(builder: (_) => const BiologyLabScreen()),
                                    );
                                  },
                                ),
                              ),
                              const SizedBox(width: 10),
                              Expanded(
                                child: ElevatedButton.icon(
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: accentColor,
                                    foregroundColor: Colors.black,
                                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                                    padding: const EdgeInsets.symmetric(vertical: 8),
                                  ),
                                  icon: const Icon(Icons.quiz, size: 16),
                                  label: const Text(
                                    'اختبار فوري',
                                    style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold),
                                  ),
                                  onPressed: () {
                                    Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                        builder: (_) => SmartQuizScreen(
                                          controller: QuizEngineProviders.createController(),
                                        ),
                                      ),
                                    );
                                  },
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    );
                  }).toList(),

                const SizedBox(height: 30),
              ],
            ),
    );
  }

  Widget _buildFilterChip({required String label, required String value}) {
    final isSelected = _selectedFilter == value;
    return ChoiceChip(
      label: Text(
        label,
        style: TextStyle(
          color: isSelected ? Colors.black : Colors.white70,
          fontSize: 12,
          fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
        ),
      ),
      selected: isSelected,
      selectedColor: Colors.amber,
      backgroundColor: const Color(0xFF1E1E2C),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(10),
        side: BorderSide(color: isSelected ? Colors.amber : Colors.white12),
      ),
      onSelected: (selected) {
        if (selected) {
          setState(() {
            _selectedFilter = value;
          });
        }
      },
    );
  }
}
