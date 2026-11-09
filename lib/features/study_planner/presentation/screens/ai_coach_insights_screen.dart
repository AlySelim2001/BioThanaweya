import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../controllers/planner_providers.dart';
import '../../biology_lab/presentation/screens/biology_lab_screen.dart';
import '../../smart_quiz_engine/presentation/controllers/quiz_providers.dart';
import '../../smart_quiz_engine/presentation/screens/smart_quiz_screen.dart';

/// شاشة تحليلات المساعد الذكي والنصائح العلاجية للفصول الضعيفة
class AiCoachInsightsScreen extends ConsumerWidget {
  const AiCoachInsightsScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(studyPlannerControllerProvider);
    final tips = state.coachTips;
    const darkSurfaceColor = Color(0xFF1E1E2C);

    return Scaffold(
      backgroundColor: const Color(0xFF12121A),
      appBar: AppBar(
        title: const Text(
          'تحليلات واستشارات المساعد الذكي (AI Coach)',
          style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
        ),
        backgroundColor: darkSurfaceColor,
        elevation: 0,
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16.0),
        children: [
          // بطاقة رأس المساعد الذكي
          Container(
            padding: const EdgeInsets.all(18),
            decoration: BoxDecoration(
              gradient: const LinearGradient(
                colors: [Color(0xFF2E1065), Color(0xFF1E1E2C)],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
              ),
              borderRadius: BorderRadius.circular(16),
              border: Border.all(color: Colors.purpleAccent.withOpacity(0.4)),
            ),
            child: Row(
              children: [
                const CircleAvatar(
                  radius: 26,
                  backgroundColor: Color(0xFF7C3AED),
                  child: Icon(Icons.psychology, size: 28, color: Colors.white),
                ),
                const SizedBox(width: 14),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        'المستشار التحليلي لمنهج الأحياء 🧬',
                        style: TextStyle(color: Colors.white, fontSize: 15, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        'يقوم بتحليل أنماط أخطاء FSRS وجلسات الاختبار دون أي خوادم خارجية (100% On-Device).',
                        style: TextStyle(color: Colors.grey.shade300, fontSize: 11, height: 1.3),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),

          const SizedBox(height: 20),

          // قسم الإجراءات العلاجية السريعة
          const Text(
            'المسارات العلاجية الفورية الموصى بها:',
            style: TextStyle(color: Colors.white, fontSize: 15, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 10),

          Row(
            children: [
              Expanded(
                child: ElevatedButton.icon(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFF10B981),
                    foregroundColor: Colors.white,
                    padding: const EdgeInsets.symmetric(vertical: 12),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  ),
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => const BiologyLabScreen()),
                    );
                  },
                  icon: const Icon(Icons.biotech, size: 18),
                  label: const Text('مختبر الرسومات', style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold)),
                ),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: ElevatedButton.icon(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFFF59E0B),
                    foregroundColor: const Color(0xFF0A192F),
                    padding: const EdgeInsets.symmetric(vertical: 12),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  ),
                  onPressed: () async {
                    final quizCtrl = QuizEngineProviders.createController();
                    await quizCtrl.startMistakesRevisionSession();
                    if (context.mounted) {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => SmartQuizScreen(controller: quizCtrl),
                        ),
                      );
                    }
                  },
                  icon: const Icon(Icons.history_edu, size: 18),
                  label: const Text('استدراك FSRS', style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold)),
                ),
              ),
            ],
          ),

          const SizedBox(height: 24),

          // قائمة النصائح التشخيصية الكاملة
          const Text(
            'تقارير التشخيص المعرفي والذاكرة:',
            style: TextStyle(color: Colors.amber, fontSize: 15, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),

          if (tips.isEmpty)
            const Center(
              child: Padding(
                padding: EdgeInsets.all(24.0),
                child: Text('لا توجد تقارير حالياً.', style: TextStyle(color: Colors.grey)),
              ),
            )
          else
            ...tips.map((tip) {
              final isCritical = tip.urgencyLevel == 'Critical' || tip.urgencyLevel == 'Warning';

              return Container(
                margin: const EdgeInsets.only(bottom: 14),
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: darkSurfaceColor,
                  borderRadius: BorderRadius.circular(14),
                  border: Border.all(
                    color: isCritical ? Colors.amber.withOpacity(0.5) : Colors.green.withOpacity(0.3),
                  ),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Expanded(
                          child: Row(
                            children: [
                              Icon(
                                isCritical ? Icons.warning_amber_rounded : Icons.check_circle_outline,
                                color: isCritical ? Colors.amber : Colors.greenAccent,
                                size: 20,
                              ),
                              const SizedBox(width: 8),
                              Expanded(
                                child: Text(
                                  tip.title,
                                  style: TextStyle(
                                    color: isCritical ? Colors.amber : Colors.greenAccent,
                                    fontWeight: FontWeight.bold,
                                    fontSize: 14,
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                          decoration: BoxDecoration(
                            color: const Color(0xFF12121A),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Text(
                            tip.targetChapter,
                            style: const TextStyle(color: Colors.white70, fontSize: 10),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 10),
                    Text(
                      tip.diagnosticMessage,
                      style: const TextStyle(color: Colors.white, fontSize: 13, height: 1.4),
                    ),
                    const SizedBox(height: 12),
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(10),
                      decoration: BoxDecoration(
                        color: const Color(0xFF12121A),
                        borderRadius: BorderRadius.circular(10),
                        border: Border.all(color: Colors.green.withOpacity(0.3)),
                      ),
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Icon(Icons.lightbulb_outline, size: 16, color: Colors.greenAccent),
                          const SizedBox(width: 8),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Text(
                                  'الإجراء العلاجي المقترح:',
                                  style: TextStyle(color: Colors.greenAccent, fontSize: 11, fontWeight: FontWeight.bold),
                                ),
                                const SizedBox(height: 2),
                                Text(
                                  tip.recommendedAction,
                                  style: const TextStyle(color: Colors.white70, fontSize: 12, height: 1.3),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              );
            }).toList(),
        ],
      ),
    );
  }
}
