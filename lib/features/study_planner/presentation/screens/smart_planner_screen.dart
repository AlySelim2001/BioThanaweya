import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../controllers/planner_providers.dart';
import 'ai_coach_insights_screen.dart';

/// شاشة خطة المذاكرة التكيفية والمساعد الذكي الرئيسي (Smart Study Planner)
class SmartPlannerScreen extends ConsumerWidget {
  const SmartPlannerScreen({super.key});

  void _showAddTaskDialog(BuildContext context, WidgetRef ref) {
    final titleController = TextEditingController();
    final chapterController = TextEditingController(text: 'الفصل الأول: الدعامة والحركة');
    final minutesController = TextEditingController(text: '45');
    String selectedPriority = 'Medium';

    showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (context, setDialogState) {
          return AlertDialog(
            backgroundColor: const Color(0xFF1E1E2C),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
            title: const Row(
              children: [
                Icon(Icons.add_task, color: Colors.greenAccent),
                SizedBox(width: 8),
                Text('إضافة مهمة مذاكرة جديدة', style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold)),
              ],
            ),
            content: SingleChildScrollView(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text('عنوان المهمة أو المفهوم:', style: TextStyle(color: Colors.grey, fontSize: 12)),
                  const SizedBox(height: 6),
                  TextField(
                    controller: titleController,
                    style: const TextStyle(color: Colors.white, fontSize: 13),
                    decoration: InputDecoration(
                      hintText: 'مثال: حل 30 سؤالاً على التوالد البكري...',
                      hintStyle: const TextStyle(color: Colors.grey),
                      filled: true,
                      fillColor: const Color(0xFF12121A),
                      border: OutlineInputBorder(borderRadius: BorderRadius.circular(10), borderSide: BorderSide.none),
                    ),
                  ),
                  const SizedBox(height: 12),
                  const Text('الفصل المرتبط:', style: TextStyle(color: Colors.grey, fontSize: 12)),
                  const SizedBox(height: 6),
                  TextField(
                    controller: chapterController,
                    style: const TextStyle(color: Colors.white, fontSize: 13),
                    decoration: InputDecoration(
                      filled: true,
                      fillColor: const Color(0xFF12121A),
                      border: OutlineInputBorder(borderRadius: BorderRadius.circular(10), borderSide: BorderSide.none),
                    ),
                  ),
                  const SizedBox(height: 12),
                  Row(
                    children: [
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('الوقت المقدر (دقيقة):', style: TextStyle(color: Colors.grey, fontSize: 12)),
                            const SizedBox(height: 6),
                            TextField(
                              controller: minutesController,
                              keyboardType: TextInputType.number,
                              style: const TextStyle(color: Colors.white, fontSize: 13),
                              decoration: InputDecoration(
                                filled: true,
                                fillColor: const Color(0xFF12121A),
                                border: OutlineInputBorder(borderRadius: BorderRadius.circular(10), borderSide: BorderSide.none),
                              ),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('الأولوية:', style: TextStyle(color: Colors.grey, fontSize: 12)),
                            const SizedBox(height: 6),
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 10),
                              decoration: BoxDecoration(
                                color: const Color(0xFF12121A),
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: DropdownButton<String>(
                                value: selectedPriority,
                                isExpanded: true,
                                underline: const SizedBox(),
                                dropdownColor: const Color(0xFF1E1E2C),
                                style: const TextStyle(color: Colors.white, fontSize: 13),
                                items: const [
                                  DropdownMenuItem(value: 'High', child: Text('عالية 🔥', style: TextStyle(color: Colors.redAccent))),
                                  DropdownMenuItem(value: 'Medium', child: Text('متوسطة ⚡', style: TextStyle(color: Colors.amber))),
                                  DropdownMenuItem(value: 'Low', child: Text('عادية 👍', style: TextStyle(color: Colors.greenAccent))),
                                ],
                                onChanged: (val) {
                                  if (val != null) {
                                    setDialogState(() => selectedPriority = val);
                                  }
                                },
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(ctx),
                child: const Text('إلغاء', style: TextStyle(color: Colors.grey)),
              ),
              ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.green,
                  foregroundColor: Colors.white,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                ),
                onPressed: () {
                  final title = titleController.text.trim();
                  if (title.isNotEmpty) {
                    final minutes = int.tryParse(minutesController.text.trim()) ?? 30;
                    ref.read(studyPlannerControllerProvider.notifier).addCustomTask(
                      title: title,
                      chapterName: chapterController.text.trim(),
                      priority: selectedPriority,
                      estimatedMinutes: minutes,
                    );
                  }
                  Navigator.pop(ctx);
                },
                child: const Text('إضافة المهمة', style: TextStyle(fontWeight: FontWeight.bold)),
              ),
            ],
          );
        },
      ),
    );
  }

  void _showRegeneratePlanDialog(BuildContext context, WidgetRef ref) {
    final chapters = [
      'الفصل الأول: الدعامة والحركة',
      'الفصل الثاني: التنسيق الهرموني',
      'الفصل الثالث: التكاثر',
      'الفصل الرابع: المناعة',
      'الفصل الخامس: DNA وتضاعفه',
      'الفصل السادس: RNA وبناء البروتين',
    ];
    final selectedChapters = <String>{'الفصل الثاني: التنسيق الهرموني', 'الفصل الخامس: DNA وتضاعفه'};

    showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (context, setDialogState) {
          return AlertDialog(
            backgroundColor: const Color(0xFF1E1E2C),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
            title: const Row(
              children: [
                Icon(Icons.auto_awesome, color: Colors.amber),
                SizedBox(width: 8),
                Text('توليد خطة تكيفية ذكية', style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold)),
              ],
            ),
            content: SizedBox(
              width: double.maxFinite,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    'اختر الفصول المراد تركيز خطة المذاكرة والاستدراك عليها اليوم:',
                    style: TextStyle(color: Colors.grey, fontSize: 12),
                  ),
                  const SizedBox(height: 12),
                  Flexible(
                    child: ListView(
                      shrinkWrap: true,
                      children: chapters.map((chap) {
                        final isChecked = selectedChapters.contains(chap);
                        return CheckboxListTile(
                          contentPadding: EdgeInsets.zero,
                          title: Text(chap, style: const TextStyle(color: Colors.white, fontSize: 13)),
                          value: isChecked,
                          activeColor: Colors.amber,
                          checkColor: Colors.black,
                          onChanged: (val) {
                            setDialogState(() {
                              if (val == true) {
                                selectedChapters.add(chap);
                              } else {
                                selectedChapters.remove(chap);
                              }
                            });
                          },
                        );
                      }).toList(),
                    ),
                  ),
                ],
              ),
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(ctx),
                child: const Text('إلغاء', style: TextStyle(color: Colors.grey)),
              ),
              ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.amber,
                  foregroundColor: const Color(0xFF12121A),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                ),
                onPressed: () {
                  ref.read(studyPlannerControllerProvider.notifier).generateNewAdaptivePlan(
                    selectedChapters.toList(),
                  );
                  Navigator.pop(ctx);
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('تم توليد وتحديث خطة المذاكرة التكيفية بنجاح!')),
                  );
                },
                icon: const Icon(Icons.flash_on, size: 16),
                label: const Text('توليد الخطة فوراً', style: TextStyle(fontWeight: FontWeight.bold)),
              ),
            ],
          );
        },
      ),
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(studyPlannerControllerProvider);
    final controller = ref.read(studyPlannerControllerProvider.notifier);
    const primaryGreen = Colors.green;
    const darkSurfaceColor = Color(0xFF1E1E2C);

    return Scaffold(
      backgroundColor: const Color(0xFF12121A),
      appBar: AppBar(
        title: const Text(
          'خطة المذاكرة التكيفية والمساعد الذكي',
          style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
        ),
        backgroundColor: darkSurfaceColor,
        elevation: 0,
        iconTheme: const IconThemeData(color: Colors.white),
        actions: [
          IconButton(
            tooltip: 'توليد خطة تكيفية جديدة',
            icon: const Icon(Icons.auto_awesome, color: Colors.amber),
            onPressed: () => _showRegeneratePlanDialog(context, ref),
          ),
          IconButton(
            tooltip: 'تحليلات المساعد الذكي',
            icon: const Icon(Icons.psychology, color: Colors.greenAccent),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const AiCoachInsightsScreen()),
              );
            },
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        backgroundColor: Colors.green,
        foregroundColor: Colors.white,
        tooltip: 'إضافة مهمة مذاكرة',
        onPressed: () => _showAddTaskDialog(context, ref),
        child: const Icon(Icons.add),
      ),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator(color: primaryGreen))
          : ListView(
              padding: const EdgeInsets.all(16),
              children: [
                // 1. بطاقة مؤشر الإنجاز الكلي
                Container(
                  padding: const EdgeInsets.all(20),
                  decoration: BoxDecoration(
                    color: darkSurfaceColor,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: primaryGreen.withValues(alpha: 0.4)),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withValues(alpha: 0.3),
                        blurRadius: 10,
                        offset: const Offset(0, 4),
                      ),
                    ],
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text('معدل الإنجاز اليومي التراكمي', style: TextStyle(color: Colors.grey, fontSize: 13)),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                            decoration: BoxDecoration(
                              color: primaryGreen.withValues(alpha: 0.15),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Text(
                              '${state.completedTasksCount} من أصل ${state.totalTasksCount} مكتملة',
                              style: const TextStyle(color: Colors.greenAccent, fontSize: 11, fontWeight: FontWeight.bold),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 8),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(
                            '${state.completionPercentage.toStringAsFixed(0)}%',
                            style: const TextStyle(color: Colors.white, fontSize: 36, fontWeight: FontWeight.bold),
                          ),
                          Icon(
                            state.completionPercentage >= 100 ? Icons.stars_rounded : Icons.verified_rounded,
                            color: state.completionPercentage >= 100 ? Colors.amber : primaryGreen,
                            size: 40,
                          ),
                        ],
                      ),
                      const SizedBox(height: 12),
                      ClipRRect(
                        borderRadius: BorderRadius.circular(6),
                        child: LinearProgressIndicator(
                          value: state.completionPercentage / 100.0,
                          backgroundColor: Colors.black38,
                          color: primaryGreen,
                          minHeight: 8,
                        ),
                      ),
                      const SizedBox(height: 12),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(
                            'الوقت المنقضي: ${state.completedMinutes} دقيقة',
                            style: const TextStyle(color: Colors.grey, fontSize: 11),
                          ),
                          Text(
                            'المتبقي: ${state.totalEstimatedMinutes - state.completedMinutes} دقيقة',
                            style: const TextStyle(color: Colors.grey, fontSize: 11),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),

                const SizedBox(height: 20),

                // 2. شريط توجيهات المساعد الذكي السريعة (AI Coach Spotlight)
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      'توجيهات المساعد الذكي (FSRS Coach)',
                      style: TextStyle(color: Colors.amber, fontSize: 15, fontWeight: FontWeight.bold),
                    ),
                    TextButton.icon(
                      style: TextButton.styleFrom(padding: EdgeInsets.zero),
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (_) => const AiCoachInsightsScreen()),
                        );
                      },
                      icon: const Icon(Icons.arrow_back_ios, size: 12, color: Colors.amber),
                      label: const Text('كافة التحليلات', style: TextStyle(color: Colors.amber, fontSize: 12)),
                    ),
                  ],
                ),
                const SizedBox(height: 8),

                ...state.coachTips.take(2).map((tip) {
                  return Container(
                    margin: const EdgeInsets.only(bottom: 10),
                    padding: const EdgeInsets.all(14),
                    decoration: BoxDecoration(
                      color: const Color(0xFF252536),
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(color: Colors.amber.withValues(alpha: 0.35)),
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          children: [
                            const Icon(Icons.tips_and_updates, color: Colors.amber, size: 18),
                            const SizedBox(width: 8),
                            Expanded(
                              child: Text(
                                tip.title,
                                style: const TextStyle(color: Colors.amber, fontWeight: FontWeight.bold, fontSize: 13),
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 6),
                        Text(
                          tip.diagnosticMessage,
                          style: const TextStyle(color: Colors.white70, fontSize: 12, height: 1.4),
                        ),
                        const SizedBox(height: 8),
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                          decoration: BoxDecoration(
                            color: primaryGreen.withValues(alpha: 0.12),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Row(
                            children: [
                              const Icon(Icons.task_alt, color: Colors.greenAccent, size: 14),
                              const SizedBox(width: 6),
                              Expanded(
                                child: Text(
                                  tip.recommendedAction,
                                  style: const TextStyle(color: Colors.greenAccent, fontSize: 11, fontWeight: FontWeight.w600),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  );
                }).toList(),

                const SizedBox(height: 16),

                // 3. رأس جدول مهام المذاكرة
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      'مهام المذاكرة والاستدراك اليومية',
                      style: TextStyle(color: Colors.white, fontSize: 15, fontWeight: FontWeight.bold),
                    ),
                    OutlinedButton.icon(
                      style: OutlinedButton.styleFrom(
                        foregroundColor: Colors.white70,
                        side: const BorderSide(color: Colors.white24),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                      ),
                      onPressed: () => _showAddTaskDialog(context, ref),
                      icon: const Icon(Icons.add, size: 14),
                      label: const Text('مهمة مخصصة', style: TextStyle(fontSize: 11)),
                    ),
                  ],
                ),
                const SizedBox(height: 10),

                // 4. قائمة مهام المذاكرة التفاعلية
                if (state.tasks.isEmpty)
                  Container(
                    padding: const EdgeInsets.all(24),
                    alignment: Alignment.center,
                    child: const Text(
                      'لا توجد مهام حالياً. انقر على "توليد خطة تكيفية" لبدء خطة ذكية.',
                      style: TextStyle(color: Colors.grey, fontSize: 13),
                    ),
                  )
                else
                  ...state.tasks.map((task) {
                    final isHigh = task.priority == 'High';

                    return Dismissible(
                      key: Key(task.id),
                      direction: DismissDirection.endToStart,
                      background: Container(
                        alignment: Alignment.centerLeft,
                        padding: const EdgeInsets.symmetric(horizontal: 20),
                        decoration: BoxDecoration(
                          color: Colors.redAccent.withValues(alpha: 0.2),
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: const Icon(Icons.delete, color: Colors.redAccent),
                      ),
                      onDismissed: (_) {
                        controller.deleteTask(task.id);
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(content: Text('تم حذف المهمة: ${task.title}')),
                        );
                      },
                      child: Container(
                        margin: const EdgeInsets.only(bottom: 10),
                        decoration: BoxDecoration(
                          color: darkSurfaceColor,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(
                            color: task.isCompleted
                                ? Colors.transparent
                                : isHigh
                                    ? Colors.redAccent.withValues(alpha: 0.4)
                                    : Colors.white12,
                          ),
                        ),
                        child: CheckboxListTile(
                          contentPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
                          title: Text(
                            task.title,
                            style: TextStyle(
                              color: task.isCompleted ? Colors.grey : Colors.white,
                              fontSize: 13,
                              fontWeight: task.isCompleted ? FontWeight.normal : FontWeight.bold,
                              decoration: task.isCompleted ? TextDecoration.lineThrough : null,
                            ),
                          ),
                          subtitle: Padding(
                            padding: const EdgeInsets.only(top: 4.0),
                            child: Row(
                              children: [
                                Text(
                                  task.chapterName,
                                  style: const TextStyle(color: Colors.grey, fontSize: 11),
                                ),
                                const SizedBox(width: 8),
                                const Text('•', style: TextStyle(color: Colors.grey, fontSize: 11)),
                                const SizedBox(width: 8),
                                Text(
                                  '${task.estimatedMinutes} دقيقة',
                                  style: const TextStyle(color: Colors.grey, fontSize: 11),
                                ),
                                if (isHigh && !task.isCompleted) ...[
                                  const SizedBox(width: 8),
                                  Container(
                                    padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                                    decoration: BoxDecoration(
                                      color: Colors.redAccent.withValues(alpha: 0.18),
                                      borderRadius: BorderRadius.circular(4),
                                    ),
                                    child: const Text('أولوية FSRS', style: TextStyle(color: Colors.redAccent, fontSize: 9, fontWeight: FontWeight.bold)),
                                  ),
                                ],
                              ],
                            ),
                          ),
                          value: task.isCompleted,
                          activeColor: primaryGreen,
                          checkColor: Colors.black,
                          onChanged: (_) => controller.toggleTaskCompletion(task.id),
                        ),
                      ),
                    );
                  }).toList(),

                const SizedBox(height: 40),
              ],
            ),
    );
  }
}
