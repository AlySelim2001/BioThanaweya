import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'core/constants/curriculum_data.dart';
import 'core/theme/app_theme.dart';
import 'features/achievements/presentation/screens/achievements_screen.dart';
import 'features/biology_agents/presentation/screens/topic_detail_screen.dart';
import 'features/biology_agents/presentation/widgets/sparkline_chart.dart';
import 'features/exam_simulator/presentation/screens/exam_simulation_screen.dart';
import 'features/flashcards/presentation/screens/flashcard_screen.dart';
import 'features/interactive_suite/bound_bio_app_suite.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const ProviderScope(child: BoundBioAppRoot()));
}

/// Root Application configured for Egyptian Thanawya Amma Biology (smarts.bio)
/// Features: Full Arabic RTL layout, Material 3, Offline-First state, and full 11-chapter curriculum.
class BioThanaweyaApp extends StatelessWidget {
  const BioThanaweyaApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Scholar Sidekick - مرصاد أحياء الثانوية العامة',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      darkTheme: AppTheme.darkTheme,
      themeMode: ThemeMode.light,
      locale: const Locale('ar', 'EG'),
      supportedLocales: const [
        Locale('ar', 'EG'),
        Locale('ar', ''),
        Locale('en', ''),
      ],
      localizationsDelegates: const [
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      builder: (context, child) {
        return Directionality(
          textDirection: TextDirection.rtl,
          child: child ?? const SizedBox.shrink(),
        );
      },
      home: const BioDashboardScreen(),
    );
  }
}

/// Comprehensive Biology Mastery Dashboard
/// Displays overall mastery metrics, spaced repetition status, high-yield traps,
/// and complete interactive access to all 11 Egyptian Curriculum chapters.
class BioDashboardScreen extends StatefulWidget {
  const BioDashboardScreen({super.key});

  @override
  State<BioDashboardScreen> createState() => _BioDashboardScreenState();
}

class _BioDashboardScreenState extends State<BioDashboardScreen> {
  final TextEditingController _searchController = TextEditingController();
  int _selectedFilterIndex = 0; // 0: الكل, 1: الوحدة الأولى, 2: الوحدة الثانية, 3: مستحق مراجعة
  String _searchQuery = '';

  // بيانات الفصول الـ 11 الكاملة مع المؤشرات التعليمية ومنحنيات التطور
  final List<Map<String, dynamic>> _chapterMetrics = const [
    {
      'id': 1,
      'topicId': 'support_movement',
      'unit': 1,
      'unitTitle': 'الوحدة الأولى: التركيب والوظيفة',
      'title': 'الفصل الأول: الدعامة والحركة في الكائنات الحية',
      'englishTitle': 'Support and Movement in Living Organisms',
      'keyConcepts': ['الدعامة الفسيولوجية', 'الدعامة التركيبية', 'انقباض القطعة العضلية', 'نظرية الخيوط المنزلقة'],
      'mastery': 0.85,
      'cardsDue': 4,
      'solvedQuestions': 142,
      'trend': [0.45, 0.58, 0.65, 0.76, 0.85],
    },
    {
      'id': 2,
      'topicId': 'nutrition',
      'unit': 1,
      'unitTitle': 'الوحدة الأولى: التركيب والوظيفة',
      'title': 'الفصل الثاني: التغذية والهضم في الكائنات الحية',
      'englishTitle': 'Nutrition and Digestion in Living Organisms',
      'keyConcepts': ['البناء الضوئي', 'تفاعلات كالفن', 'مركب PGAL', 'الإنزيمات الهاضمة'],
      'mastery': 0.90,
      'cardsDue': 0,
      'solvedQuestions': 168,
      'trend': [0.55, 0.68, 0.78, 0.85, 0.90],
    },
    {
      'id': 3,
      'topicId': 'transport',
      'unit': 1,
      'unitTitle': 'الوحدة الأولى: التركيب والوظيفة',
      'title': 'الفصل الثالث: النقل في الكائنات الحية',
      'englishTitle': 'Transport in Living Organisms',
      'keyConcepts': ['صعود العصارة النيئة', 'قوة الشد بالنتح', 'التماسك والتلاصق', 'الدورة الدموية والقلب'],
      'mastery': 0.75,
      'cardsDue': 2,
      'solvedQuestions': 110,
      'trend': [0.40, 0.50, 0.60, 0.70, 0.75],
    },
    {
      'id': 4,
      'topicId': 'respiration',
      'unit': 1,
      'unitTitle': 'الوحدة الأولى: التركيب والوظيفة',
      'title': 'الفصل الرابع: التنفس الخلوي وإنتاج الطاقة',
      'englishTitle': 'Cellular Respiration and Energy Production',
      'keyConcepts': ['انشطار الجلوكوز', 'دورة كريبس', 'سلسلة نقل الإلكترون', 'حساب 38 ATP'],
      'mastery': 0.95,
      'cardsDue': 1,
      'solvedQuestions': 195,
      'trend': [0.60, 0.72, 0.84, 0.90, 0.95],
    },
    {
      'id': 5,
      'topicId': 'excretion',
      'unit': 1,
      'unitTitle': 'الوحدة الأولى: التركيب والوظيفة',
      'title': 'الفصل الخامس: الإخراج في الكائنات الحية',
      'englishTitle': 'Excretion in Living Organisms',
      'keyConcepts': ['الترشيح الفائق في بومان', 'إعادة الامتصاص الاختياري', 'وظائف النفرون', 'الجلد والعرق'],
      'mastery': 0.80,
      'cardsDue': 0,
      'solvedQuestions': 98,
      'trend': [0.50, 0.60, 0.72, 0.78, 0.80],
    },
    {
      'id': 6,
      'topicId': 'nervous_coordination',
      'unit': 1,
      'unitTitle': 'الوحدة الأولى: التركيب والوظيفة',
      'title': 'الفصل السادس: التنسيق العصبي والسيال العصبي',
      'englishTitle': 'Nervous Coordination and Nerve Impulse',
      'keyConcepts': ['جهد الراحة (-70mV)', 'إزالة الاستقطاب (+40mV)', 'التشابك العصبي', 'الأسيتيل كولين'],
      'mastery': 0.70,
      'cardsDue': 5,
      'solvedQuestions': 125,
      'trend': [0.35, 0.48, 0.55, 0.62, 0.70],
    },
    {
      'id': 7,
      'topicId': 'hormonal_coordination',
      'unit': 1,
      'unitTitle': 'الوحدة الأولى: التركيب والوظيفة',
      'title': 'الفصل السابع: التنسيق الهرموني والغدد الصماء',
      'englishTitle': 'Hormonal Coordination and Endocrine System',
      'keyConcepts': ['الغدة النخامية (المايسترو)', 'هرمون الثيروكسين', 'الكالسيتونين والباراثورمون', 'الأنسولين والجلوكاجون'],
      'mastery': 0.88,
      'cardsDue': 3,
      'solvedQuestions': 174,
      'trend': [0.52, 0.64, 0.75, 0.82, 0.88],
    },
    {
      'id': 8,
      'topicId': 'reproduction',
      'unit': 1,
      'unitTitle': 'الوحدة الأولى: التركيب والوظيفة',
      'title': 'الفصل الثامن: التكاثر في الكائنات الحية',
      'englishTitle': 'Reproduction in Living Organisms',
      'keyConcepts': ['التوالد البكري في النحل', 'تعاقب الأجيال في الفوجير', 'الإخصاب المزدوج', 'دورة الطمث في الإنسان'],
      'mastery': 0.65,
      'cardsDue': 8,
      'solvedQuestions': 210,
      'trend': [0.30, 0.40, 0.48, 0.58, 0.65],
    },
    {
      'id': 9,
      'topicId': 'immunity',
      'unit': 1,
      'unitTitle': 'الوحدة الأولى: التركيب والوظيفة',
      'title': 'الفصل التاسع: المناعة في الكائنات الحية',
      'englishTitle': 'Immunity in Living Organisms',
      'keyConcepts': ['خطوط الدفاع الثلاثة', 'الأجسام المضادة (Ig)', 'المناعة الخلطية', 'المناعة الخلوية وخلايا T و B'],
      'mastery': 0.92,
      'cardsDue': 0,
      'solvedQuestions': 188,
      'trend': [0.55, 0.68, 0.80, 0.88, 0.92],
    },
    {
      'id': 10,
      'topicId': 'dna_replication',
      'unit': 2,
      'unitTitle': 'الوحدة الثانية: البيولوجيا الجزيئية',
      'title': 'الفصل العاشر: الحمض النووي DNA وتضاعفه',
      'englishTitle': 'DNA Structure, Genetic Code and Replication',
      'keyConcepts': ['اللولب المزدوج (واتسون وكريك)', 'إنزيمات اللولب والبلمرة والربط', 'قطع أوكازاكي', 'قواعد تشارجاف'],
      'mastery': 0.82,
      'cardsDue': 2,
      'solvedQuestions': 160,
      'trend': [0.42, 0.55, 0.68, 0.75, 0.82],
    },
    {
      'id': 11,
      'topicId': 'rna_protein_synthesis',
      'unit': 2,
      'unitTitle': 'الوحدة الثانية: البيولوجيا الجزيئية',
      'title': 'الفصل الحادي عشر: الأحماض النووية RNA والتقنية الحيوية',
      'englishTitle': 'RNA, Protein Synthesis and Biotechnology',
      'keyConcepts': ['النسخ والترجمة (AUG)', 'إنزيمات القصر البكتيرية', 'تفاعل البلمرة PCR', 'الهندسة الوراثية'],
      'mastery': 0.78,
      'cardsDue': 6,
      'solvedQuestions': 148,
      'trend': [0.38, 0.50, 0.62, 0.70, 0.78],
    },
  ];

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  List<Map<String, dynamic>> get _filteredChapters {
    return _chapterMetrics.where((chapter) {
      // 1. تصفية حسب الوحدة أو المراجعة المستحقة
      if (_selectedFilterIndex == 1 && chapter['unit'] != 1) return false;
      if (_selectedFilterIndex == 2 && chapter['unit'] != 2) return false;
      if (_selectedFilterIndex == 3 && (chapter['cardsDue'] as int) == 0) return false;

      // 2. تصفية حسب نص البحث
      if (_searchQuery.trim().isNotEmpty) {
        final query = _searchQuery.trim().toLowerCase();
        final title = (chapter['title'] as String).toLowerCase();
        final englishTitle = (chapter['englishTitle'] as String).toLowerCase();
        final concepts = (chapter['keyConcepts'] as List<String>).join(' ').toLowerCase();

        return title.contains(query) || englishTitle.contains(query) || concepts.contains(query);
      }
      return true;
    }).toList();
  }

  void _showOfflineInfoDialog() {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: Row(
          children: const [
            Icon(Icons.offline_pin, color: Color(0xFF10B981), size: 28),
            SizedBox(width: 8),
            Text('منظومة 100% Offline-First', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
          ],
        ),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: const [
            Text(
              'أهلاً بك يا بطل! التطبيق مصمم للعمل بالكامل دون الحاجة إلى اتصال بالإنترنت:\n\n'
              '• كافة فصول منهج الأحياء الـ 11 ومصطلحاتها متوفرة محلياً.\n'
              '• محرك التكرار المتباعد (SM-2) يحسب فترات المراجعة على جهازك.\n'
              '• محاكي الامتحانات الوزاري ونماذج التصحيح المخزنة جاهزة دوماً.\n'
              '• بيانات إتقانك وسجلات تقدمك مشفرة ومحفوظة على ذاكرة الهاتف.',
              style: TextStyle(fontSize: 13, height: 1.5),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('فهمت ذلك', style: TextStyle(fontWeight: FontWeight.bold)),
          ),
        ],
      ),
    );
  }

  void _openChapterActionsModal(Map<String, dynamic> chapter) {
    final String topicId = chapter['topicId'];
    final matchedTopic = CurriculumData.topics.firstWhere(
      (t) => t.id == topicId,
      orElse: () => CurriculumData.topics.first,
    );

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (ctx) => Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                CircleAvatar(
                  backgroundColor: AppTheme.bioTealPrimary.withOpacity(0.15),
                  radius: 22,
                  child: Text(
                    '${chapter['id']}',
                    style: const TextStyle(fontWeight: FontWeight.bold, color: AppTheme.bioTealPrimary, fontSize: 16),
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        chapter['title'],
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                      ),
                      Text(
                        chapter['englishTitle'],
                        style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
                      ),
                    ],
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Colors.grey.shade100,
                borderRadius: BorderRadius.circular(12),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  Column(
                    children: [
                      const Text('نسبة الإتقان', style: TextStyle(fontSize: 11, color: Colors.grey)),
                      const SizedBox(height: 2),
                      Text(
                        '${((chapter['mastery'] as double) * 100).toInt()}%',
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15, color: AppTheme.bioTealPrimary),
                      ),
                    ],
                  ),
                  Container(width: 1, height: 28, color: Colors.grey.shade300),
                  Column(
                    children: [
                      const Text('أسئلة منجزة', style: TextStyle(fontSize: 11, color: Colors.grey)),
                      const SizedBox(height: 2),
                      Text(
                        '${chapter['solvedQuestions']}',
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                      ),
                    ],
                  ),
                  Container(width: 1, height: 28, color: Colors.grey.shade300),
                  Column(
                    children: [
                      const Text('مراجعة اليوم', style: TextStyle(fontSize: 11, color: Colors.grey)),
                      const SizedBox(height: 2),
                      Text(
                        '${chapter['cardsDue']} بطاقات',
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 15,
                          color: (chapter['cardsDue'] as int) > 0 ? AppTheme.alertCoral : AppTheme.emeraldSuccess,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),
            const Text('المسارات التفاعلية المتاحة لهذا الفصل:', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
            const SizedBox(height: 8),
            ListTile(
              contentPadding: EdgeInsets.zero,
              leading: const CircleAvatar(
                backgroundColor: Color(0xFF0D9488),
                child: Icon(Icons.psychology, color: Colors.white, size: 20),
              ),
              title: const Text('دراسة شاملة مع الوكلاء الـ 5 (Agent Swarm)', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
              subtitle: const Text('شرح ELI5 + تشريح 3D + محاورة سقراطية + صائد الفخاخ'),
              trailing: const Icon(Icons.arrow_forward_ios, size: 14),
              onTap: () {
                Navigator.pop(ctx);
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (_) => TopicDetailScreen(topic: matchedTopic)),
                );
              },
            ),
            ListTile(
              contentPadding: EdgeInsets.zero,
              leading: const CircleAvatar(
                backgroundColor: Color(0xFF7C3AED),
                child: Icon(Icons.style, color: Colors.white, size: 20),
              ),
              title: const Text('مراجعة الفلاش كاردز التكيفية (SM-2)', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
              subtitle: const Text('تثبيت المفاهيم الصعبة بجدولة التكرار المتباعد'),
              trailing: const Icon(Icons.arrow_forward_ios, size: 14),
              onTap: () {
                Navigator.pop(ctx);
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (_) => const FlashcardScreen()),
                );
              },
            ),
            ListTile(
              contentPadding: EdgeInsets.zero,
              leading: const CircleAvatar(
                backgroundColor: Color(0xFFF59E0B),
                child: Icon(Icons.assignment_turned_in, color: Colors.white, size: 20),
              ),
              title: const Text('محاكي الامتحانات والتدقيق الوزاري', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
              subtitle: const Text('تدريب على صياغة الإجابة النموذجية بالكلمات المفتاحية'),
              trailing: const Icon(Icons.arrow_forward_ios, size: 14),
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
  }

  @override
  Widget build(BuildContext context) {
    // حساب الإحصائيات العامة للمنهج
    final double overallMastery = _chapterMetrics.fold<double>(0.0, (acc, item) => acc + (item['mastery'] as double)) / _chapterMetrics.length;
    final int totalDueCards = _chapterMetrics.fold<int>(0, (acc, item) => acc + (item['cardsDue'] as int));
    final int totalSolvedQuestions = _chapterMetrics.fold<int>(0, (acc, item) => acc + (item['solvedQuestions'] as int));

    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'مرصاد الأحياء | Scholar Sidekick',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
        ),
        centerTitle: false,
        actions: [
          // شارة وضع العمل بدون إنترنت
          InkWell(
            onTap: _showOfflineInfoDialog,
            borderRadius: BorderRadius.circular(20),
            child: Container(
              margin: const EdgeInsets.symmetric(horizontal: 4, vertical: 10),
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
              decoration: BoxDecoration(
                color: const Color(0xFF10B981).withOpacity(0.15),
                borderRadius: BorderRadius.circular(20),
                border: Border.all(color: const Color(0xFF10B981), width: 1),
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: const [
                  Icon(Icons.offline_pin, color: Color(0xFF10B981), size: 16),
                  SizedBox(width: 4),
                  Text('أوفلاين', style: TextStyle(color: Color(0xFF10B981), fontSize: 11, fontWeight: FontWeight.bold)),
                ],
              ),
            ),
          ),
          // زر لوحة الأوسمة والإنجازات
          IconButton(
            icon: const Icon(Icons.emoji_events, color: Color(0xFFF59E0B)),
            tooltip: 'الأوسمة والإنجازات',
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const AchievementsScreen()),
              );
            },
          ),
        ],
      ),
      body: CustomScrollView(
        slivers: [
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // بطاقة ترحيبية ورسالة تحفيزية لدفعة الثانوية العامة
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(16),
                    decoration: BoxDecoration(
                      gradient: const LinearGradient(
                        colors: [Color(0xFF0F172A), Color(0xFF1E293B)],
                        begin: Alignment.topRight,
                        end: Alignment.bottomLeft,
                      ),
                      borderRadius: BorderRadius.circular(16),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withOpacity(0.12),
                          blurRadius: 10,
                          offset: const Offset(0, 4),
                        ),
                      ],
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          children: [
                            const CircleAvatar(
                              radius: 20,
                              backgroundColor: Color(0xFF0D9488),
                              child: Icon(Icons.biotech, color: Colors.white, size: 22),
                            ),
                            const SizedBox(width: 10),
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: const [
                                  Text(
                                    'أهلاً بك يا بطل الثانوية العامة! 🧬',
                                    style: TextStyle(color: Colors.white, fontSize: 15, fontWeight: FontWeight.bold),
                                  ),
                                  Text(
                                    'شعبة علمي علوم • دفعة 2025/2026',
                                    style: TextStyle(color: Colors.white70, fontSize: 11),
                                  ),
                                ],
                              ),
                            ),
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                              decoration: BoxDecoration(
                                color: const Color(0xFFF59E0B).withOpacity(0.2),
                                borderRadius: BorderRadius.circular(12),
                                border: Border.all(color: const Color(0xFFF59E0B)),
                              ),
                              child: Row(
                                children: const [
                                  Icon(Icons.local_fire_department, color: Color(0xFFF59E0B), size: 14),
                                  SizedBox(width: 4),
                                  Text('14 يوم متواصل', style: TextStyle(color: Colors.white, fontSize: 11, fontWeight: FontWeight.bold)),
                                ],
                              ),
                            ),
                          ],
                        ),
                        const Divider(color: Colors.white12, height: 24),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            _buildStatItem('إتقان المنهج', '${(overallMastery * 100).toStringAsFixed(1)}%', const Color(0xFF10B981)),
                            _buildStatItem('بطاقات مستحقة', '$totalDueCards بطاقة', const Color(0xFFEF4444)),
                            _buildStatItem('أسئلة محلولة', '$totalSolvedQuestions', const Color(0xFF06B6D4)),
                            _buildStatItem('الهدف الوزاري', '60 / 60', const Color(0xFFF59E0B)),
                          ],
                        ),
                      ],
                    ),
                  ),

                  const SizedBox(height: 16),

                  // أزرار الوصول السريع للأدوات المركزية
                  Row(
                    children: [
                      Expanded(
                        child: ElevatedButton.icon(
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(0xFF0D9488),
                            foregroundColor: Colors.white,
                            padding: const EdgeInsets.symmetric(vertical: 12),
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                          ),
                          onPressed: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(builder: (_) => const FlashcardScreen()),
                            );
                          },
                          icon: const Icon(Icons.style, size: 18),
                          label: const Text('مراجعة الفلاش كاردز', style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold)),
                        ),
                      ),
                      const SizedBox(width: 10),
                      Expanded(
                        child: ElevatedButton.icon(
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(0xFF7C3AED),
                            foregroundColor: Colors.white,
                            padding: const EdgeInsets.symmetric(vertical: 12),
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                          ),
                          onPressed: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(builder: (_) => const ExamSimulationScreen()),
                            );
                          },
                          icon: const Icon(Icons.assignment, size: 18),
                          label: const Text('محاكي الامتحانات', style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold)),
                        ),
                      ),
                    ],
                  ),

                  const SizedBox(height: 16),

                  // فخاخ الامتحانات الوزارية الأكثر تكراراً (High-Yield Traps)
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(12),
                    decoration: BoxDecoration(
                      color: const Color(0xFFFEF3C7),
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(color: const Color(0xFFF59E0B).withOpacity(0.4)),
                    ),
                    child: Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: const [
                        Icon(Icons.warning_amber_rounded, color: Color(0xFFD97706), size: 22),
                        SizedBox(width: 8),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                'تنبيه تدقيقي من واضعي الامتحانات:',
                                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12, color: Color(0xFF92400E)),
                              ),
                              SizedBox(height: 2),
                              Text(
                                'كودونات الوقف (UAA, UAG, UGA) لا ترتبط بأحماض أمينية بل بعامل الإطلاق؛ وبيلة السكر لا تعني تلف النفرون دوماً بل قد ترتبط بعتبة الكلية لتركيز الجلوكوز.',
                                style: TextStyle(fontSize: 11, color: Color(0xFF78350F), height: 1.3),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),

                  const SizedBox(height: 20),

                  // حقل البحث التفاعلي
                  TextField(
                    controller: _searchController,
                    onChanged: (val) => setState(() => _searchQuery = val),
                    decoration: InputDecoration(
                      hintText: 'ابحث عن فصل، مفهوم وراثي، أو مصطلح تشريحي...',
                      hintStyle: const TextStyle(fontSize: 13),
                      prefixIcon: const Icon(Icons.search, size: 20),
                      suffixIcon: _searchQuery.isNotEmpty
                          ? IconButton(
                              icon: const Icon(Icons.clear, size: 18),
                              onPressed: () {
                                _searchController.clear();
                                setState(() => _searchQuery = '');
                              },
                            )
                          : null,
                      filled: true,
                      fillColor: Colors.white,
                      contentPadding: const EdgeInsets.symmetric(vertical: 0, horizontal: 16),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(12),
                        borderSide: BorderSide.none,
                      ),
                    ),
                  ),

                  const SizedBox(height: 12),

                  // شرائح التصفية (Filter Chips)
                  SingleChildScrollView(
                    scrollDirection: Axis.horizontal,
                    child: Row(
                      children: [
                        _buildFilterChip(0, 'الكل (11 فصلاً)'),
                        const SizedBox(width: 8),
                        _buildFilterChip(1, 'الوحدة 1: التركيب والوظيفة'),
                        const SizedBox(width: 8),
                        _buildFilterChip(2, 'الوحدة 2: البيولوجيا الجزيئية'),
                        const SizedBox(width: 8),
                        _buildFilterChip(3, 'مستحق للمراجعة اليوم'),
                      ],
                    ),
                  ),

                  const SizedBox(height: 16),

                  // رأس قسم الفصول
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'فصول المنهج المعتمدة (${_filteredChapters.length} من 11):',
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                      ),
                      Text(
                        'منهج الثانوية العامة الرسمي',
                        style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),

          // قائمة الفصول الدراسية الـ 11
          SliverPadding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            sliver: SliverList(
              delegate: SliverChildBuilderDelegate(
                (context, index) {
                  final chapter = _filteredChapters[index];
                  final double mastery = chapter['mastery'];
                  final int cardsDue = chapter['cardsDue'];
                  final List<String> concepts = chapter['keyConcepts'];
                  final List<double> trend = (chapter['trend'] as List<dynamic>).map((e) => (e as num).toDouble()).toList();

                  return Card(
                    margin: const EdgeInsets.only(bottom: 12),
                    elevation: 1,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                    child: InkWell(
                      borderRadius: BorderRadius.circular(16),
                      onTap: () => _openChapterActionsModal(chapter),
                      child: Padding(
                        padding: const EdgeInsets.all(14.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                CircleAvatar(
                                  radius: 20,
                                  backgroundColor: mastery > 0.8
                                      ? const Color(0xFF10B981).withOpacity(0.15)
                                      : const Color(0xFFF59E0B).withOpacity(0.15),
                                  child: Text(
                                    '${chapter['id']}',
                                    style: TextStyle(
                                      fontWeight: FontWeight.bold,
                                      color: mastery > 0.8 ? const Color(0xFF047857) : const Color(0xFFB45309),
                                    ),
                                  ),
                                ),
                                const SizedBox(width: 12),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        chapter['unitTitle'],
                                        style: TextStyle(fontSize: 10, color: Colors.grey.shade600, fontWeight: FontWeight.bold),
                                      ),
                                      const SizedBox(height: 2),
                                      Text(
                                        chapter['title'],
                                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                                      ),
                                      Text(
                                        chapter['englishTitle'],
                                        style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
                                      ),
                                    ],
                                  ),
                                ),
                                if (cardsDue > 0)
                                  Container(
                                    padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                                    decoration: BoxDecoration(
                                      color: const Color(0xFFEF4444),
                                      borderRadius: BorderRadius.circular(12),
                                    ),
                                    child: Text(
                                      '$cardsDue مراجعة',
                                      style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold),
                                    ),
                                  )
                                else
                                  const Icon(Icons.check_circle, color: Color(0xFF10B981), size: 20),
                              ],
                            ),
                            const SizedBox(height: 10),

                            // شريط المفاهيم الرئيسية
                            Wrap(
                              spacing: 6,
                              runSpacing: 4,
                              children: concepts.take(3).map((concept) {
                                return Container(
                                  padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                                  decoration: BoxDecoration(
                                    color: Colors.grey.shade100,
                                    borderRadius: BorderRadius.circular(6),
                                  ),
                                  child: Text(concept, style: TextStyle(fontSize: 10, color: Colors.grey.shade700)),
                                );
                              }).toList(),
                            ),

                            const SizedBox(height: 12),

                            // شريط الإتقان ومنحنى التطور (Sparkline)
                            Row(
                              children: [
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Row(
                                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                        children: [
                                          Text(
                                            'الإتقان: ${(mastery * 100).toInt()}%',
                                            style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold),
                                          ),
                                          Text(
                                            '${chapter['solvedQuestions']} سؤال منجز',
                                            style: TextStyle(fontSize: 10, color: Colors.grey.shade600),
                                          ),
                                        ],
                                      ),
                                      const SizedBox(height: 4),
                                      ClipRRect(
                                        borderRadius: BorderRadius.circular(4),
                                        child: LinearProgressIndicator(
                                          value: mastery,
                                          minHeight: 6,
                                          backgroundColor: Colors.grey.shade200,
                                          valueColor: AlwaysStoppedAnimation<Color>(
                                            mastery > 0.8
                                                ? const Color(0xFF10B981)
                                                : mastery > 0.65
                                                    ? const Color(0xFFF59E0B)
                                                    : const Color(0xFFEF4444),
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                                const SizedBox(width: 16),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.end,
                                  children: [
                                    const Text('منحنى التقدم', style: TextStyle(fontSize: 9, color: Colors.grey)),
                                    const SizedBox(height: 2),
                                    SparklineChart(
                                      data: trend,
                                      width: 60,
                                      height: 22,
                                      lineColor: mastery > 0.8 ? const Color(0xFF10B981) : const Color(0xFFF59E0B),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                    ),
                  );
                },
                childCount: _filteredChapters.length,
              ),
            ),
          ),
          const SliverToBoxAdapter(
            child: SizedBox(height: 32),
          ),
        ],
      ),
    );
  }

  Widget _buildStatItem(String label, String value, Color color) {
    return Column(
      children: [
        Text(value, style: TextStyle(color: color, fontSize: 14, fontWeight: FontWeight.bold)),
        const SizedBox(height: 2),
        Text(label, style: const TextStyle(color: Colors.white70, fontSize: 10)),
      ],
    );
  }

  Widget _buildFilterChip(int index, String label) {
    final isSelected = _selectedFilterIndex == index;
    return ChoiceChip(
      label: Text(label),
      selected: isSelected,
      selectedColor: AppTheme.bioTealPrimary.withOpacity(0.2),
      labelStyle: TextStyle(
        fontSize: 11,
        color: isSelected ? AppTheme.bioTealPrimary : Colors.black87,
        fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
      ),
      onSelected: (val) {
        if (val) setState(() => _selectedFilterIndex = index);
      },
    );
  }
}
