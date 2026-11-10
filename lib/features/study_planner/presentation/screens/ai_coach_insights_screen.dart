import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:smarts_bio/features/study_planner/presentation/controllers/planner_providers.dart';
import 'package:smarts_bio/features/biology_lab/presentation/screens/biology_lab_screen.dart';
import 'package:smarts_bio/features/smart_quiz_engine/presentation/controllers/quiz_providers.dart';
import 'package:smarts_bio/features/smart_quiz_engine/presentation/screens/smart_quiz_screen.dart';

/// Local diagnostic insights and study actions based on stored planner state.
class AiCoachInsightsScreen extends ConsumerWidget {
  const AiCoachInsightsScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(studyPlannerControllerProvider);
    final tips = state.coachTips;
    const background = Color(0xFF12121A);
    const surface = Color(0xFF1E1E2C);

    return Scaffold(
      backgroundColor: background,
      appBar: AppBar(
        title: const Text('تحليلات واستشارات المساعد الذكي'),
        backgroundColor: surface,
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Container(
            padding: const EdgeInsets.all(18),
            decoration: BoxDecoration(
              gradient: const LinearGradient(colors: [Color(0xFF2E1065), surface]),
              borderRadius: BorderRadius.circular(16),
              border: Border.all(color: Colors.purpleAccent.withValues(alpha: 0.4)),
            ),
            child: const Row(children: [
              CircleAvatar(
                backgroundColor: Color(0xFF7C3AED),
                child: Icon(Icons.psychology, color: Colors.white),
              ),
              SizedBox(width: 14),
              Expanded(
                child: Text(
                  'تحليلات محلية لأنماط المراجعة والتقدم، دون الحاجة إلى خادم خارجي.',
                  style: TextStyle(color: Colors.white, height: 1.4),
                ),
              ),
            ]),
          ),
          const SizedBox(height: 20),
          const Text('المسارات العلاجية السريعة', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
          const SizedBox(height: 10),
          Row(children: [
            Expanded(
              child: ElevatedButton.icon(
                onPressed: () => Navigator.push(context, MaterialPageRoute<void>(builder: (_) => const BiologyLabScreen())),
                icon: const Icon(Icons.biotech),
                label: const Text('مختبر الرسومات'),
              ),
            ),
            const SizedBox(width: 10),
            Expanded(
              child: ElevatedButton.icon(
                onPressed: () async {
                  final quizController = QuizEngineProviders.createController();
                  await quizController.startMistakesRevisionSession();
                  if (!context.mounted) return;
                  await Navigator.push<void>(
                    context,
                    MaterialPageRoute<void>(builder: (_) => SmartQuizScreen(controller: quizController)),
                  );
                },
                icon: const Icon(Icons.history_edu),
                label: const Text('مراجعة الأخطاء'),
              ),
            ),
          ]),
          const SizedBox(height: 24),
          const Text('تقارير التشخيص والذاكرة', style: TextStyle(color: Colors.amber, fontWeight: FontWeight.bold)),
          const SizedBox(height: 12),
          if (state.isLoading)
            const Center(child: CircularProgressIndicator())
          else if (tips.isEmpty)
            const Center(child: Padding(padding: EdgeInsets.all(24), child: Text('لا توجد تقارير حالياً.', style: TextStyle(color: Colors.grey))))
          else
            ...tips.map((tip) {
              final isCritical = tip.urgencyLevel == 'Critical' || tip.urgencyLevel == 'Warning';
              final accent = isCritical ? Colors.amber : Colors.greenAccent;
              return Container(
                margin: const EdgeInsets.only(bottom: 14),
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: surface,
                  borderRadius: BorderRadius.circular(14),
                  border: Border.all(color: accent.withValues(alpha: 0.35)),
                ),
                child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
                  Row(children: [
                    Icon(isCritical ? Icons.warning_amber_rounded : Icons.check_circle_outline, color: accent),
                    const SizedBox(width: 8),
                    Expanded(child: Text(tip.title, style: TextStyle(color: accent, fontWeight: FontWeight.bold))),
                    Text(tip.targetChapter, style: const TextStyle(color: Colors.white70, fontSize: 11)),
                  ]),
                  const SizedBox(height: 10),
                  Text(tip.diagnosticMessage, style: const TextStyle(color: Colors.white, height: 1.4)),
                  const SizedBox(height: 10),
                  Text('الإجراء المقترح: ${tip.recommendedAction}', style: const TextStyle(color: Colors.white70, height: 1.4)),
                ]),
              );
            }),
        ],
      ),
    );
  }
}
