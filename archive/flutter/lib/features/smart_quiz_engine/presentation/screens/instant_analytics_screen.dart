import 'package:flutter/material.dart';
import '../../domain/entities/exam_session.dart';
import '../controllers/quiz_session_controller.dart';
import 'smart_quiz_screen.dart';

/// شاشة تحليل النتائج الفورية (Instant Analytics & Mistakes Review)
class InstantAnalyticsScreen extends StatelessWidget {
  final ExamSession session;
  final QuizSessionController controller;

  const InstantAnalyticsScreen({
    Key? key,
    required this.session,
    required this.controller,
  }) : super(key: key);

  String _formatDuration(int seconds) {
    final m = seconds ~/ 60;
    final s = seconds % 60;
    return '${m}د ${s}ث';
  }

  @override
  Widget build(BuildContext context) {
    final double percentage = session.scorePercentage;
    final bool isPassed = session.isPassed;
    final chapterBreakdown = session.chapterBreakdowns;
    final failedQuestions = session.failedQuestions;

    return Scaffold(
      backgroundColor: const Color(0xFF0A192F),
      appBar: AppBar(
        backgroundColor: const Color(0xFF112240),
        elevation: 0,
        title: const Text(
          'التحليل الفوري والتقييم الوزاري',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white),
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.home, color: Color(0xFF64FFDA)),
            tooltip: 'الرئيسية',
            onPressed: () => Navigator.popUntil(context, (route) => route.isFirst),
          ),
        ],
      ),
      body: ListView(
        padding: const EdgeInsets.all(16.0),
        children: [
          // 1. بطاقة النتيجة الكلية والدرجة الوزارية
          Container(
            padding: const EdgeInsets.all(20),
            decoration: BoxDecoration(
              gradient: LinearGradient(
                colors: isPassed
                    ? [const Color(0xFF064E3B), const Color(0xFF112240)]
                    : [const Color(0xFF7F1D1D), const Color(0xFF112240)],
                begin: Alignment.topRight,
                end: Alignment.bottomLeft,
              ),
              borderRadius: BorderRadius.circular(20),
              border: Border.all(
                color: isPassed ? const Color(0xFF10B981) : const Color(0xFFEF4444),
                width: 1.5,
              ),
            ),
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      session.title,
                      style: const TextStyle(color: Color(0xFFCCD6F6), fontSize: 13, fontWeight: FontWeight.bold),
                    ),
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                      decoration: BoxDecoration(
                        color: (isPassed ? const Color(0xFF10B981) : const Color(0xFFEF4444)).withValues(alpha: 0.2),
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(
                          color: isPassed ? const Color(0xFF10B981) : const Color(0xFFEF4444),
                        ),
                      ),
                      child: Text(
                        isPassed ? 'تم الاجتياز بنجاح' : 'يحتاج لمعالجة الفخاخ',
                        style: TextStyle(
                          color: isPassed ? const Color(0xFF10B981) : const Color(0xFFEF4444),
                          fontSize: 11,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 16),

                // النسبة المئوية
                Text(
                  '${percentage.toStringAsFixed(1)}%',
                  style: TextStyle(
                    fontSize: 48,
                    fontWeight: FontWeight.w900,
                    color: isPassed ? const Color(0xFF64FFDA) : const Color(0xFFF87171),
                  ),
                ),
                const SizedBox(height: 6),
                Text(
                  session.ministryGradeLabel,
                  style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold, color: Colors.white),
                ),
                const SizedBox(height: 16),

                // المؤشرات الرقمية
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    _buildStatCard('إجمالي الأسئلة', '${session.totalQuestions}', const Color(0xFF64FFDA)),
                    _buildStatCard('صحيحة ✅', '${session.correctAnswersCount}', const Color(0xFF10B981)),
                    _buildStatCard('خاطئة ❌', '${session.wrongAnswersCount}', const Color(0xFFEF4444)),
                    _buildStatCard('الزمن المستغرق', _formatDuration(session.elapsedSeconds), const Color(0xFFF59E0B)),
                  ],
                ),
              ],
            ),
          ),

          const SizedBox(height: 20),

          // 2. زر الإجراء التكيفي لإعادة اختبار الأخطاء (FSRS Integration)
          if (failedQuestions.isNotEmpty) ...[
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: const Color(0xFF112240),
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: const Color(0xFFF59E0B).withValues(alpha: 0.5)),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      const Icon(Icons.psychology_alt, color: Color(0xFFF59E0B), size: 24),
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          'خوارزمية الاستدراك التكيفي (FSRS Active): تم رصد ${failedQuestions.length} خطأ امتحاني',
                          style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 13),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 8),
                  const Text(
                    'تمت جدولة الأسئلة الخاطئة محلياً لضمان عدم نسيانها. يمكنك البدء الآن في جلسة استدراك فورية لإتقانها.',
                    style: TextStyle(color: Color(0xFF8892B0), fontSize: 12, height: 1.4),
                  ),
                  const SizedBox(height: 12),
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton.icon(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: const Color(0xFFF59E0B),
                        foregroundColor: const Color(0xFF0A192F),
                        padding: const EdgeInsets.symmetric(vertical: 12),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      ),
                      onPressed: () async {
                        await controller.startMistakesRevisionSession();
                        if (context.mounted) {
                          Navigator.pushReplacement(
                            context,
                            MaterialPageRoute(
                              builder: (_) => SmartQuizScreen(controller: controller),
                            ),
                          );
                        }
                      },
                      icon: const Icon(Icons.replay_rounded, size: 18),
                      label: const Text(
                        'إعادة اختبار الأخطاء الآن في جلسة FSRS',
                        style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 20),
          ],

          // 3. تحليل وتصنيف النتائج حسب فصول المنهج
          const Text(
            'تحليل الأداء حسب فصول المنهج الدراسي:',
            style: TextStyle(color: Colors.white, fontSize: 15, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),

          ...chapterBreakdown.values.map((summary) {
            final double masteryRatio = summary.scorePercentage / 100.0;
            return Container(
              margin: const EdgeInsets.only(bottom: 10),
              padding: const EdgeInsets.all(14),
              decoration: BoxDecoration(
                color: const Color(0xFF112240),
                borderRadius: BorderRadius.circular(14),
                border: Border.all(color: const Color(0xFF233554)),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Expanded(
                        child: Text(
                          'الفصل ${summary.chapterId}: ${summary.chapterTitle}',
                          style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 13),
                        ),
                      ),
                      Text(
                        '${summary.scorePercentage.toInt()}%',
                        style: TextStyle(
                          color: summary.isMastered ? const Color(0xFF10B981) : const Color(0xFFF59E0B),
                          fontWeight: FontWeight.bold,
                          fontSize: 13,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 6),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        '${summary.correctCount} صحيحة من أصل ${summary.totalQuestions} أسئلة',
                        style: const TextStyle(color: Color(0xFF8892B0), fontSize: 11),
                      ),
                      if (summary.wrongCount > 0)
                        Text(
                          '${summary.wrongCount} أخطاء تحتاج مراجعة',
                          style: const TextStyle(color: Color(0xFFEF4444), fontSize: 11),
                        ),
                    ],
                  ),
                  const SizedBox(height: 8),
                  ClipRRect(
                    borderRadius: BorderRadius.circular(4),
                    child: LinearProgressIndicator(
                      value: masteryRatio,
                      backgroundColor: const Color(0xFF1D3557),
                      valueColor: AlwaysStoppedAnimation<Color>(
                        summary.isMastered
                            ? const Color(0xFF10B981)
                            : summary.needsRemediation
                                ? const Color(0xFFEF4444)
                                : const Color(0xFFF59E0B),
                      ),
                      minHeight: 6,
                    ),
                  ),
                ],
              ),
            );
          }),

          const SizedBox(height: 20),

          // 4. مراجعة تفصيلية لسؤال بسؤال
          const Text(
            'مراجعة الأسئلة ونموذج الإجابة الوزاري:',
            style: TextStyle(color: Colors.white, fontSize: 15, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),

          ...List.generate(session.questions.length, (idx) {
            final q = session.questions[idx];
            final ans = session.userAnswers[q.id];
            final bool isCorrect = ans?.isCorrect ?? false;
            final selectedIdx = ans?.selectedOptionIndex ?? -1;

            return Container(
              margin: const EdgeInsets.only(bottom: 12),
              padding: const EdgeInsets.all(14),
              decoration: BoxDecoration(
                color: const Color(0xFF112240),
                borderRadius: BorderRadius.circular(14),
                border: Border.all(
                  color: isCorrect ? const Color(0xFF10B981).withValues(alpha: 0.5) : const Color(0xFFEF4444).withValues(alpha: 0.5),
                ),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      CircleAvatar(
                        radius: 12,
                        backgroundColor: isCorrect ? const Color(0xFF10B981) : const Color(0xFFEF4444),
                        child: Icon(
                          isCorrect ? Icons.check : Icons.close,
                          size: 14,
                          color: Colors.white,
                        ),
                      ),
                      const SizedBox(width: 10),
                      Expanded(
                        child: Text(
                          '${idx + 1}. ${q.questionText}',
                          style: const TextStyle(color: Colors.white, fontSize: 13, fontWeight: FontWeight.bold, height: 1.4),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 10),

                  // إجابة الطالب
                  if (selectedIdx >= 0 && selectedIdx < q.options.length)
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 2.0),
                      child: Text(
                        'إجابتك: ${q.options[selectedIdx]}',
                        style: TextStyle(
                          color: isCorrect ? const Color(0xFF64FFDA) : const Color(0xFFF87171),
                          fontSize: 12,
                        ),
                      ),
                    )
                  else
                    const Text('لم تتم الإجابة على هذا السؤال', style: TextStyle(color: Colors.grey, fontSize: 12)),

                  // الإجابة النموذجية في حال الخطأ
                  if (!isCorrect)
                    Padding(
                      padding: const EdgeInsets.only(top: 4.0),
                      child: Text(
                        'الإجابة النموذجية المعتمدة: ${q.correctOptionText}',
                        style: const TextStyle(color: Color(0xFF10B981), fontSize: 12, fontWeight: FontWeight.bold),
                      ),
                    ),

                  const SizedBox(height: 8),

                  // التفسير العلمي الوزاري
                  Container(
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      color: const Color(0xFF172A45),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Text(
                      'الشرح الوزاري: ${q.explanation}',
                      style: const TextStyle(color: Color(0xFFCCD6F6), fontSize: 11, height: 1.4),
                    ),
                  ),
                ],
              ),
            );
          }),

          const SizedBox(height: 24),

          // زر العودة أو بدء اختبار جديد
          ElevatedButton.icon(
            style: ElevatedButton.styleFrom(
              backgroundColor: const Color(0xFF64FFDA),
              foregroundColor: const Color(0xFF0A192F),
              padding: const EdgeInsets.symmetric(vertical: 14),
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            ),
            onPressed: () => Navigator.popUntil(context, (route) => route.isFirst),
            icon: const Icon(Icons.done_all),
            label: const Text('العودة إلى شاشة الفصول الرئيسية', style: TextStyle(fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 32),
        ],
      ),
    );
  }

  Widget _buildStatCard(String title, String value, Color color) {
    return Column(
      children: [
        Text(value, style: TextStyle(color: color, fontSize: 16, fontWeight: FontWeight.bold)),
        const SizedBox(height: 4),
        Text(title, style: const TextStyle(color: Color(0xFF8892B0), fontSize: 10)),
      ],
    );
  }
}
