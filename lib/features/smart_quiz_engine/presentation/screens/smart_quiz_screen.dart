import 'package:flutter/material.dart';
import '../../domain/entities/question.dart';
import '../controllers/quiz_session_controller.dart';
import '../controllers/quiz_session_state.dart';
import 'instant_analytics_screen.dart';

/// شاشة محرك الاختبار التفاعلي الذكي (Smart Offline Quiz Screen)
class SmartQuizScreen extends StatefulWidget {
  final QuizSessionController controller;

  const SmartQuizScreen({Key? key, required this.controller}) : super(key: key);

  @override
  State<SmartQuizScreen> createState() => _SmartQuizScreenState();
}

class _SmartQuizScreenState extends State<SmartQuizScreen> {
  @override
  void initState() {
    super.initState();
    widget.controller.addListener(_onControllerStateChange);
  }

  @override
  void dispose() {
    widget.controller.removeListener(_onControllerStateChange);
    super.dispose();
  }

  void _onControllerStateChange() {
    if (widget.controller.state.isCompleted && mounted) {
      final session = widget.controller.state.completedSession;
      if (session != null) {
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (_) => InstantAnalyticsScreen(
              session: session,
              controller: widget.controller,
            ),
          ),
        );
      }
    }
  }

  void _showQuestionJumpModal(QuizSessionState state) {
    showModalBottomSheet(
      context: context,
      backgroundColor: const Color(0xFF112240),
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (ctx) {
        return Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text(
                    'جدول أسئلة الاختبار',
                    style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  Text(
                    '${state.answeredCount} من ${state.totalQuestions} تم حله',
                    style: const TextStyle(color: Color(0xFF64FFDA), fontSize: 12),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              Flexible(
                child: GridView.builder(
                  shrinkWrap: true,
                  gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 6,
                    crossAxisSpacing: 10,
                    mainAxisSpacing: 10,
                  ),
                  itemCount: state.questions.length,
                  itemBuilder: (context, index) {
                    final q = state.questions[index];
                    final isCurrent = index == state.currentIndex;
                    final isAnswered = state.userAnswers.containsKey(q.id);

                    Color bg = const Color(0xFF1D3557);
                    Color text = const Color(0xFFCCD6F6);
                    Border? border;

                    if (isCurrent) {
                      bg = const Color(0xFF64FFDA);
                      text = const Color(0xFF0A192F);
                      border = Border.all(color: Colors.white, width: 2);
                    } else if (isAnswered) {
                      bg = const Color(0xFF047857);
                      text = Colors.white;
                    }

                    return InkWell(
                      onTap: () {
                        widget.controller.jumpToQuestion(index);
                        Navigator.pop(ctx);
                      },
                      borderRadius: BorderRadius.circular(10),
                      child: Container(
                        decoration: BoxDecoration(
                          color: bg,
                          borderRadius: BorderRadius.circular(10),
                          border: border,
                        ),
                        alignment: Alignment.center,
                        child: Text(
                          '${index + 1}',
                          style: TextStyle(color: text, fontWeight: FontWeight.bold, fontSize: 13),
                        ),
                      ),
                    );
                  },
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  void _confirmSubmitDialog() {
    final state = widget.controller.state;
    final unanswered = state.totalQuestions - state.answeredCount;

    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: const Color(0xFF112240),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: const Text(
          'تسليم الاختبار والتقييم الفوري',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16),
        ),
        content: Text(
          unanswered > 0
              ? 'تنبيه: لديك $unanswered سؤالاً دون إجابة. هل تود بالتأكيد إنهاء الاختبار وتسليم النتيجة؟'
              : 'أحسنت! لقد أجبت على جميع الأسئلة. هل تود إنهاء الاختبار وعرض التحليل الفوري؟',
          style: const TextStyle(color: Color(0xFFCCD6F6), fontSize: 13, height: 1.4),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('متابعة الحل', style: TextStyle(color: Color(0xFF8892B0))),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              backgroundColor: const Color(0xFF64FFDA),
              foregroundColor: const Color(0xFF0A192F),
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            ),
            onPressed: () {
              Navigator.pop(ctx);
              widget.controller.submitExam();
            },
            child: const Text('نعم، تسليم النتيجة', style: TextStyle(fontWeight: FontWeight.bold)),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: widget.controller,
      builder: (context, _) {
        final state = widget.controller.state;

        if (state.isLoading) {
          return const Scaffold(
            backgroundColor: Color(0xFF0A192F),
            body: Center(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  CircularProgressIndicator(color: Color(0xFF64FFDA)),
                  SizedBox(height: 16),
                  Text('جاري تحضير بنك الأسئلة محلياً...', style: TextStyle(color: Colors.white)),
                ],
              ),
            ),
          );
        }

        if (state.status == QuizUiStatus.error) {
          return Scaffold(
            backgroundColor: const Color(0xFF0A192F),
            appBar: AppBar(backgroundColor: const Color(0xFF112240)),
            body: Center(
              child: Padding(
                padding: const EdgeInsets.all(24.0),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    const Icon(Icons.error_outline, size: 48, color: Color(0xFFEF4444)),
                    const SizedBox(height: 16),
                    Text(
                      state.errorMessage ?? 'حدث خطأ غير متوقع',
                      textAlign: TextAlign.center,
                      style: const TextStyle(color: Colors.white, fontSize: 14),
                    ),
                    const SizedBox(height: 20),
                    ElevatedButton(
                      onPressed: () => Navigator.pop(context),
                      child: const Text('العودة للرئيسية'),
                    ),
                  ],
                ),
              ),
            ),
          );
        }

        final currentQ = state.currentQuestion;
        if (currentQ == null) return const SizedBox.shrink();

        final currentAnswer = state.answerForQuestion(currentQ.id);

        return Scaffold(
          backgroundColor: const Color(0xFF0A192F),
          appBar: AppBar(
            backgroundColor: const Color(0xFF112240),
            elevation: 0,
            leading: IconButton(
              icon: const Icon(Icons.close, color: Colors.white),
              onPressed: () {
                showDialog(
                  context: context,
                  builder: (ctx) => AlertDialog(
                    backgroundColor: const Color(0xFF112240),
                    title: const Text('الخروج من الاختبار؟', style: TextStyle(color: Colors.white)),
                    content: const Text('سيتم فقدان تقدم الجلسة الحالية ما لم تقم بتسليمها.', style: TextStyle(color: Color(0xFFCCD6F6))),
                    actions: [
                      TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('إلغاء')),
                      ElevatedButton(
                        style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFEF4444)),
                        onPressed: () {
                          Navigator.pop(ctx);
                          Navigator.pop(context);
                        },
                        child: const Text('خروج', style: TextStyle(color: Colors.white)),
                      ),
                    ],
                  ),
                );
              },
            ),
            title: Text(
              'السؤال ${state.currentIndex + 1} من ${state.totalQuestions}',
              style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold, color: Colors.white),
            ),
            actions: [
              // زر جدول الأسئلة
              IconButton(
                icon: const Icon(Icons.grid_view_rounded, color: Color(0xFF64FFDA)),
                tooltip: 'جدول الأسئلة',
                onPressed: () => _showQuestionJumpModal(state),
              ),
              // شارة المؤقت الزمني
              Container(
                margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                decoration: BoxDecoration(
                  color: state.isTimeWarning ? const Color(0xFFEF4444).withOpacity(0.2) : const Color(0xFF1D3557),
                  borderRadius: BorderRadius.circular(16),
                  border: Border.all(
                    color: state.isTimeWarning ? const Color(0xFFEF4444) : const Color(0xFF64FFDA),
                    width: 1,
                  ),
                ),
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(
                      Icons.timer_outlined,
                      size: 14,
                      color: state.isTimeWarning ? const Color(0xFFEF4444) : const Color(0xFF64FFDA),
                    ),
                    const SizedBox(width: 4),
                    Text(
                      state.formattedRemainingTime,
                      style: TextStyle(
                        fontSize: 12,
                        fontWeight: FontWeight.bold,
                        color: state.isTimeWarning ? const Color(0xFFEF4444) : Colors.white,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
          body: Column(
            children: [
              // مؤشر التقدم الأفقي
              LinearProgressIndicator(
                value: state.progressRatio,
                backgroundColor: const Color(0xFF112240),
                valueColor: const AlwaysStoppedAnimation<Color>(Color(0xFF64FFDA)),
                minHeight: 4,
              ),

              // بطاقة السؤال وخيارات الإجابة
              Expanded(
                child: ListView(
                  padding: const EdgeInsets.all(16.0),
                  children: [
                    // شارات الفصل والصعوبة
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                          decoration: BoxDecoration(
                            color: const Color(0xFF1D3557),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Text(
                            currentQ.chapterTitle,
                            style: const TextStyle(color: Color(0xFF64FFDA), fontSize: 11, fontWeight: FontWeight.bold),
                          ),
                        ),
                        if (currentQ.difficulty == QuizDifficulty.ministryTrap)
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                            decoration: BoxDecoration(
                              color: const Color(0xFFF59E0B).withOpacity(0.2),
                              borderRadius: BorderRadius.circular(8),
                              border: Border.all(color: const Color(0xFFF59E0B)),
                            ),
                            child: const Row(
                              children: [
                                Icon(Icons.warning_amber_rounded, size: 12, color: Color(0xFFF59E0B)),
                                SizedBox(width: 4),
                                Text('فخ وزاري متكرر', style: TextStyle(color: Color(0xFFF59E0B), fontSize: 10, fontWeight: FontWeight.bold)),
                              ],
                            ),
                          ),
                      ],
                    ),

                    const SizedBox(height: 14),

                    // نص السؤال
                    Container(
                      padding: const EdgeInsets.all(18),
                      decoration: BoxDecoration(
                        color: const Color(0xFF112240),
                        borderRadius: BorderRadius.circular(16),
                        border: Border.all(color: const Color(0xFF233554)),
                      ),
                      child: Text(
                        currentQ.questionText,
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                          height: 1.5,
                        ),
                      ),
                    ),

                    const SizedBox(height: 20),
                    const Text(
                      'اختر الإجابة النموذجية المعتمدة:',
                      style: TextStyle(color: Color(0xFF8892B0), fontSize: 12, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 10),

                    // قائمة الخيارات المتعددة (أ، ب، ج، د)
                    ...List.generate(currentQ.options.length, (optIdx) {
                      final isSelected = currentAnswer?.selectedOptionIndex == optIdx;
                      final optionLetters = ['أ', 'ب', 'ج', 'د'];
                      final letter = optIdx < optionLetters.length ? optionLetters[optIdx] : '${optIdx + 1}';

                      return Container(
                        margin: const EdgeInsets.only(bottom: 12),
                        child: InkWell(
                          onTap: () => widget.controller.selectOption(optIdx),
                          borderRadius: BorderRadius.circular(14),
                          child: AnimatedContainer(
                            duration: const Duration(milliseconds: 180),
                            padding: const EdgeInsets.all(14),
                            decoration: BoxDecoration(
                              color: isSelected ? const Color(0xFF1D3557) : const Color(0xFF112240),
                              borderRadius: BorderRadius.circular(14),
                              border: Border.all(
                                color: isSelected ? const Color(0xFF64FFDA) : const Color(0xFF233554),
                                width: isSelected ? 1.5 : 1.0,
                              ),
                            ),
                            child: Row(
                              children: [
                                CircleAvatar(
                                  radius: 14,
                                  backgroundColor: isSelected ? const Color(0xFF64FFDA) : const Color(0xFF172A45),
                                  child: Text(
                                    letter,
                                    style: TextStyle(
                                      color: isSelected ? const Color(0xFF0A192F) : Colors.white70,
                                      fontWeight: FontWeight.bold,
                                      fontSize: 12,
                                    ),
                                  ),
                                ),
                                const SizedBox(width: 14),
                                Expanded(
                                  child: Text(
                                    currentQ.options[optIdx],
                                    style: TextStyle(
                                      color: isSelected ? Colors.white : const Color(0xFFCCD6F6),
                                      fontSize: 14,
                                      fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                                      height: 1.3,
                                    ),
                                  ),
                                ),
                                if (isSelected)
                                  const Icon(Icons.check_circle, color: Color(0xFF64FFDA), size: 18),
                              ],
                            ),
                          ),
                        ),
                      );
                    }),
                  ],
                ),
              ),

              // شريط التنقل السفلي
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                decoration: const BoxDecoration(
                  color: Color(0xFF112240),
                  border: Border(top: BorderSide(color: Color(0xFF233554))),
                ),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    OutlinedButton.icon(
                      style: OutlinedButton.styleFrom(
                        foregroundColor: Colors.white,
                        side: const BorderSide(color: Color(0xFF233554)),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                      ),
                      onPressed: state.currentIndex > 0 ? () => widget.controller.previousQuestion() : null,
                      icon: const Icon(Icons.arrow_forward_ios, size: 14),
                      label: const Text('السابق'),
                    ),
                    ElevatedButton.icon(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: state.currentIndex == state.totalQuestions - 1
                            ? const Color(0xFF10B981)
                            : const Color(0xFF64FFDA),
                        foregroundColor: const Color(0xFF0A192F),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                      ),
                      onPressed: () {
                        if (state.currentIndex < state.totalQuestions - 1) {
                          widget.controller.nextQuestion();
                        } else {
                          _confirmSubmitDialog();
                        }
                      },
                      icon: Icon(
                        state.currentIndex < state.totalQuestions - 1 ? Icons.arrow_back_ios : Icons.assignment_turned_in,
                        size: 14,
                      ),
                      label: Text(
                        state.currentIndex < state.totalQuestions - 1 ? 'التالي' : 'تسليم الامتحان',
                        style: const TextStyle(fontWeight: FontWeight.bold),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}
