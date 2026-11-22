import 'package:flutter/material.dart';
import '../../../../core/constants/curriculum_data.dart';
import '../../../achievements/presentation/screens/achievements_screen.dart';
import '../../../flashcards/presentation/screens/flashcard_screen.dart';
import '../../../exam_simulator/presentation/screens/exam_simulation_screen.dart';
import '../widgets/sparkline_chart.dart';
import 'topic_detail_screen.dart';

class DashboardScreen extends StatelessWidget {
  const DashboardScreen({Key? key}) : super(key: key);

  // قائمة الفصول الـ 11 المكتملة منهجياً مع نسب الإتقان ومنحنى التطور عبر الزمن
  final List<Map<String, dynamic>> _chapters = const [
    {'id': 1, 'title': 'الدعامة والحركة في الكائنات الحية', 'mastery': 0.85, 'cardsDue': 4, 'topicId': 'support_movement', 'trend': [0.45, 0.58, 0.65, 0.76, 0.85]},
    {'id': 2, 'title': 'التغذية والهضم في الكائنات الحية', 'mastery': 0.90, 'cardsDue': 0, 'topicId': 'nutrition', 'trend': [0.55, 0.68, 0.78, 0.85, 0.90]},
    {'id': 3, 'title': 'النقل في الكائنات الحية', 'mastery': 0.75, 'cardsDue': 2, 'topicId': 'transport', 'trend': [0.40, 0.50, 0.60, 0.70, 0.75]},
    {'id': 4, 'title': 'التنفس الخلوي وإنتاج الطاقة', 'mastery': 0.95, 'cardsDue': 1, 'topicId': 'respiration', 'trend': [0.60, 0.72, 0.84, 0.90, 0.95]},
    {'id': 5, 'title': 'الإخراج في الكائنات الحية', 'mastery': 0.80, 'cardsDue': 0, 'topicId': 'excretion', 'trend': [0.50, 0.60, 0.72, 0.78, 0.80]},
    {'id': 6, 'title': 'التنسيق العصبي والسيال العصبي', 'mastery': 0.70, 'cardsDue': 5, 'topicId': 'nervous_coordination', 'trend': [0.35, 0.48, 0.55, 0.62, 0.70]},
    {'id': 7, 'title': 'التنسيق الهرموني والغدد الصماء', 'mastery': 0.88, 'cardsDue': 3, 'topicId': 'hormonal_coordination', 'trend': [0.52, 0.64, 0.75, 0.82, 0.88]},
    {'id': 8, 'title': 'التكاثر في الكائنات الحية', 'mastery': 0.65, 'cardsDue': 8, 'topicId': 'reproduction', 'trend': [0.30, 0.40, 0.48, 0.58, 0.65]},
    {'id': 9, 'title': 'المناعة في الكائنات الحية', 'mastery': 0.92, 'cardsDue': 0, 'topicId': 'immunity', 'trend': [0.55, 0.68, 0.80, 0.88, 0.92]},
    {'id': 10, 'title': 'الحمض النووي DNA وتضاعفه', 'mastery': 0.82, 'cardsDue': 2, 'topicId': 'dna_replication', 'trend': [0.42, 0.55, 0.68, 0.75, 0.82]},
    {'id': 11, 'title': 'الحمض النووي RNA وبناء البروتين والتقنية الحيوية', 'mastery': 0.78, 'cardsDue': 6, 'topicId': 'rna_protein_synthesis', 'trend': [0.38, 0.50, 0.62, 0.70, 0.78]},
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('مرصاد الإتقان - Scholar Sidekick'),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.emoji_events, color: Colors.amberAccent),
            tooltip: 'لوحة الأوسمة والإنجازات (Badges)',
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const AchievementsScreen()),
              );
            },
          ),
          IconButton(
            icon: const Icon(Icons.offline_pin, color: Colors.greenAccent),
            tooltip: 'وضع العمل بدون إنترنت مفعل',
            onPressed: () {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('وضع العمل دون إنترنت مفعل: كافة الفصول الـ 11 والبطاقات مخزنة محلياً.'),
                  backgroundColor: Colors.green,
                ),
              );
            },
          ),
          IconButton(
            icon: const Icon(Icons.quiz, color: Colors.amberAccent),
            tooltip: 'محاكي الامتحانات والتدقيق الوزاري',
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const ExamSimulationScreen()),
              );
            },
          ),
          IconButton(
            icon: const Icon(Icons.style, color: Colors.cyanAccent),
            tooltip: 'فلاش كاردز التكرار المتباعد',
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const FlashcardScreen()),
              );
            },
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // بطاقة الملخص العام للإتقان
            Card(
              elevation: 4,
              color: Colors.blue[900],
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
              child: Padding(
                padding: const EdgeInsets.all(20.0),
                child: Row(
                  children: [
                    const CircleAvatar(
                      radius: 32,
                      backgroundColor: Colors.white24,
                      child: Icon(Icons.psychology, size: 36, color: Colors.white),
                    ),
                    const SizedBox(width: 16),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: const [
                          Text(
                            'المستوى العام في الأحياء',
                            style: TextStyle(color: Colors.white70, fontSize: 14),
                          ),
                          SizedBox(height: 4),
                          Text(
                            'متقن متقدم (83.5%)',
                            style: TextStyle(
                              color: Colors.white,
                              fontSize: 20,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          SizedBox(height: 4),
                          Text(
                            '11 فصل مكتمل بالكامل (منهج وزارة التربية والتعليم)',
                            style: TextStyle(color: Colors.white60, fontSize: 11),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),

            // أزرار الوصول السريع للأدوات التفاعلية
            Row(
              children: [
                Expanded(
                  child: ElevatedButton.icon(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.teal[700],
                      padding: const EdgeInsets.symmetric(vertical: 12),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                    ),
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (_) => const FlashcardScreen()),
                      );
                    },
                    icon: const Icon(Icons.style, color: Colors.white, size: 18),
                    label: const Text('مراجعة البطاقات', style: TextStyle(color: Colors.white, fontSize: 13, fontWeight: FontWeight.bold)),
                  ),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: ElevatedButton.icon(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.deepPurple[700],
                      padding: const EdgeInsets.symmetric(vertical: 12),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                    ),
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (_) => const ExamSimulationScreen()),
                      );
                    },
                    icon: const Icon(Icons.assignment, color: Colors.white, size: 18),
                    label: const Text('محاكي الامتحانات', style: TextStyle(color: Colors.white, fontSize: 13, fontWeight: FontWeight.bold)),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 24),

            const Text(
              'الفصول الدراسية وتغطية المنهج (11 فصل):',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 12),

            // قائمة الفصول الـ 11
            ListView.builder(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: _chapters.length,
              itemBuilder: (context, index) {
                final chapter = _chapters[index];
                final double mastery = chapter['mastery'];
                final int cardsDue = chapter['cardsDue'];
                final String topicId = chapter['topicId'];

                return Card(
                  margin: const EdgeInsets.only(bottom: 12),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  child: ListTile(
                    contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                    leading: CircleAvatar(
                      backgroundColor: Colors.blue[50],
                      child: Text(
                        '${chapter['id']}',
                        style: TextStyle(fontWeight: FontWeight.bold, color: Colors.blue[900]),
                      ),
                    ),
                    title: Text(
                      chapter['title'],
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                    ),
                    subtitle: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const SizedBox(height: 6),
                        LinearProgressIndicator(
                          value: mastery,
                          backgroundColor: Colors.grey[200],
                          color: mastery > 0.8 ? Colors.green : Colors.orange,
                        ),
                        const SizedBox(height: 6),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              'نسبة الإتقان: ${(mastery * 100).toStringAsFixed(0)}%',
                              style: const TextStyle(fontSize: 12),
                            ),
                            Row(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                const Text('منحنى التقدم: ', style: TextStyle(fontSize: 11, color: Colors.grey)),
                                SparklineChart(
                                  data: (chapter['trend'] as List<dynamic>).map((e) => (e as num).toDouble()).toList(),
                                  lineColor: mastery > 0.8 ? const Color(0xFF10B981) : const Color(0xFFF59E0B),
                                  width: 65,
                                  height: 22,
                                ),
                              ],
                            ),
                          ],
                        ),
                      ],
                    ),
                    trailing: cardsDue > 0
                        ? Chip(
                            label: Text('$cardsDue مراجعة', style: const TextStyle(color: Colors.white, fontSize: 11)),
                            backgroundColor: Colors.deepOrange,
                          )
                        : const Icon(Icons.check_circle, color: Colors.green),
                    onTap: () {
                      final matchedTopic = CurriculumData.topics.firstWhere(
                        (t) => t.id == topicId,
                        orElse: () => CurriculumData.topics.first,
                      );

                      showModalBottomSheet(
                        context: context,
                        shape: const RoundedRectangleBorder(
                          borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
                        ),
                        builder: (ctx) => Padding(
                          padding: const EdgeInsets.all(20.0),
                          child: Column(
                            mainAxisSize: MainAxisSize.min,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                chapter['title'],
                                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                'نسبة الإتقان الحالية: ${(mastery * 100).toInt()}% • البطاقات المستحقة: $cardsDue',
                                style: const TextStyle(fontSize: 12, color: Colors.grey),
                              ),
                              const Divider(height: 24),
                              ListTile(
                                leading: const CircleAvatar(
                                  backgroundColor: Color(0xFF0D9488),
                                  child: Icon(Icons.school, color: Colors.white, size: 20),
                                ),
                                title: const Text('دراسة شاملة (منظومة الوكلاء الـ 5)', style: TextStyle(fontWeight: FontWeight.bold)),
                                subtitle: const Text('شرح مبسط ELI5 + تحليل تشريحي 3D + فخاخ الامتحانات'),
                                onTap: () {
                                  Navigator.pop(ctx);
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(builder: (_) => TopicDetailScreen(topic: matchedTopic)),
                                  );
                                },
                              ),
                              ListTile(
                                leading: const CircleAvatar(
                                  backgroundColor: Color(0xFF7C3AED),
                                  child: Icon(Icons.style, color: Colors.white, size: 20),
                                ),
                                title: const Text('مراجعة الفلاش كاردز التكيفية (SM-2)', style: TextStyle(fontWeight: FontWeight.bold)),
                                subtitle: const Text('استدعاء نشط مع خوارزمية التكرار المتباعد'),
                                onTap: () {
                                  Navigator.pop(ctx);
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(builder: (_) => const FlashcardScreen()),
                                  );
                                },
                              ),
                              ListTile(
                                leading: const CircleAvatar(
                                  backgroundColor: Color(0xFFF59E0B),
                                  child: Icon(Icons.assignment, color: Colors.white, size: 20),
                                ),
                                title: const Text('محاكي الامتحانات والتدقيق الوزاري', style: TextStyle(fontWeight: FontWeight.bold)),
                                subtitle: const Text('تصحيح مقالي بالكلمات المفتاحية ونموذج الوزارة'),
                                onTap: () {
                                  Navigator.pop(ctx);
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(builder: (_) => const ExamSimulationScreen()),
                                  );
                                },
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
