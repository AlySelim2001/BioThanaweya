import 'dart:async';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

// ==============================================================================
// 🧬 SCHOLAR SIDEKICK (BioThanaweya / smarts.bio) - ULTRA-MODERN FLUTTER UI SUITE
// ==============================================================================
// Design Language: Cyber-Biology Dark Theme, Bento-Grid Layout, Glassmorphism,
// Native Arabic RTL First, 100% Offline-First Zero Network Latency.
// ==============================================================================

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const UltraModernBioThanaweyaApp());
}

/// Root Application Wrapper with strict RTL and Deep OLED Dark Theme
class UltraModernBioThanaweyaApp extends StatelessWidget {
  const UltraModernBioThanaweyaApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Scholar Sidekick - BioThanaweya',
      debugShowCheckedModeBanner: false,
      locale: const Locale('ar', 'EG'),
      theme: ThemeData(
        brightness: Brightness.dark,
        scaffoldBackgroundColor: const Color(0xFF0A192F),
        primaryColor: const Color(0xFF64FFDA),
        colorScheme: const ColorScheme.dark(
          primary: Color(0xFF64FFDA),
          secondary: Color(0xFF00B4D8),
          surface: Color(0xFF112240),
          background: Color(0xFF0A192F),
          tertiary: Color(0xFF2E7D32),
        ),
        fontFamily: 'Cairo',
        useMaterial3: true,
      ),
      builder: (context, child) {
        return Directionality(
          textDirection: TextDirection.rtl,
          child: child ?? const SizedBox.shrink(),
        );
      },
      home: const ModernBioDashboardScreen(),
    );
  }
}

// ==============================================================================
// SCREEN 1: THE ULTRA-MODERN BENTO DASHBOARD (11 CHAPTERS)
// ==============================================================================
class ModernBioDashboardScreen extends StatefulWidget {
  const ModernBioDashboardScreen({Key? key}) : super(key: key);

  @override
  State<ModernBioDashboardScreen> createState() => _ModernBioDashboardScreenState();
}

class _ModernBioDashboardScreenState extends State<ModernBioDashboardScreen> {
  // Complete 11 Thanawya Amma Biology Curriculum Chapters
  final List<Map<String, dynamic>> _chapters = const [
    {
      'id': 1,
      'unit': 1,
      'title': 'الفصل الأول',
      'name': 'الدعامة والحركة في الكائنات الحية',
      'progress': 0.95,
      'questions': 160,
      'tag': 'مكتمل بنجاح',
      'icon': Icons.accessibility_new,
    },
    {
      'id': 2,
      'unit': 1,
      'title': 'الفصل الثاني',
      'name': 'التنسيق الهرموني في الكائنات الحية',
      'progress': 0.88,
      'questions': 140,
      'tag': 'متقدم',
      'icon': Icons.water_drop,
    },
    {
      'id': 3,
      'unit': 1,
      'title': 'الفصل الثالث',
      'name': 'التكاثر في الكائنات الحية',
      'progress': 0.75,
      'questions': 210,
      'tag': 'متقدم',
      'icon': Icons.all_inclusive,
    },
    {
      'id': 4,
      'unit': 1,
      'title': 'الفصل الرابع',
      'name': 'المناعة في الكائنات الحية',
      'progress': 0.82,
      'questions': 185,
      'tag': 'متقدم',
      'icon': Icons.shield,
    },
    {
      'id': 5,
      'unit': 2,
      'title': 'الفصل الخامس',
      'name': 'الحمض النووي DNA وتضاعفه',
      'progress': 0.65,
      'questions': 195,
      'tag': 'قيد الإتقان',
      'icon': Icons.timeline,
    },
    {
      'id': 6,
      'unit': 2,
      'title': 'الفصل السادس',
      'name': 'الأحماض النووية RNA وتخليق البروتين',
      'progress': 0.50,
      'questions': 155,
      'tag': 'قيد الإتقان',
      'icon': Icons.scatter_plot,
    },
    {
      'id': 7,
      'unit': 2,
      'title': 'الفصل السابع',
      'name': 'الهندسة الوراثية وتكنولوجيا الجينات',
      'progress': 0.40,
      'questions': 130,
      'tag': 'جديد',
      'icon': Icons.precision_manufacturing,
    },
    {
      'id': 8,
      'unit': 1,
      'title': 'الفصل الثامن',
      'name': 'التغذية والهضم والتمثيل الغذائي',
      'progress': 0.90,
      'questions': 168,
      'tag': 'مكتمل بنجاح',
      'icon': Icons.eco,
    },
    {
      'id': 9,
      'unit': 1,
      'title': 'الفصل التاسع',
      'name': 'النقل والتنفس الخلوي وإنتاج الطاقة',
      'progress': 0.70,
      'questions': 175,
      'tag': 'متقدم',
      'icon': Icons.bolt,
    },
    {
      'id': 10,
      'unit': 1,
      'title': 'الفصل العاشر',
      'name': 'الإخراج والتنظيم الأسموزي والحراري',
      'progress': 0.80,
      'questions': 110,
      'tag': 'متقدم',
      'icon': Icons.filter_alt,
    },
    {
      'id': 11,
      'unit': 2,
      'title': 'الفصل الحادي عشر',
      'name': 'المراجعات الشاملة والمحاكي الوزاري الفائق',
      'progress': 0.30,
      'questions': 320,
      'tag': 'جاهز للاختبار',
      'icon': Icons.workspace_premium,
    },
  ];

  int _selectedFilter = 0; // 0: الكل, 1: الوحدة الأولى, 2: الوحدة الثانية

  @override
  Widget build(BuildContext context) {
    final filteredChapters = _chapters.where((chap) {
      if (_selectedFilter == 1) return chap['unit'] == 1;
      if (_selectedFilter == 2) return chap['unit'] == 2;
      return true;
    }).toList();

    final double overallProgress =
        _chapters.fold<double>(0.0, (acc, item) => acc + (item['progress'] as double)) / _chapters.length;

    return Scaffold(
      backgroundColor: const Color(0xFF0A192F),
      body: SafeArea(
        child: CustomScrollView(
          physics: const BouncingScrollPhysics(),
          slivers: [
            // Sliver App Bar
            SliverAppBar(
              pinned: true,
              backgroundColor: const Color(0xFF0A192F).withOpacity(0.95),
              elevation: 0,
              expandedHeight: 80,
              flexibleSpace: FlexibleSpaceBar(
                background: Container(
                  decoration: const BoxDecoration(
                    border: Border(
                      bottom: BorderSide(color: Color(0x1A64FFDA), width: 1),
                    ),
                  ),
                ),
                titlePadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                title: Row(
                  children: [
                    Container(
                      padding: const EdgeInsets.all(8),
                      decoration: BoxDecoration(
                        gradient: const LinearGradient(
                          colors: [Color(0xFF112240), Color(0xFF1D3557)],
                          begin: Alignment.topLeft,
                          end: Alignment.bottomRight,
                        ),
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(color: const Color(0xFF64FFDA).withOpacity(0.4)),
                      ),
                      child: const Icon(Icons.biotech, color: Color(0xFF64FFDA), size: 22),
                    ),
                    const SizedBox(width: 12),
                    Column(
                      mainAxisSize: MainAxisSize.min,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: const [
                        Text(
                          'مرصاد الأحياء الذكي',
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Text(
                          'الثانوية العامة • 100% Offline-First',
                          style: TextStyle(
                            color: Color(0xFF8892B0),
                            fontSize: 10,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              actions: [
                IconButton(
                  tooltip: 'البحث الشامل بالمنهج',
                  icon: const Icon(Icons.search, color: Color(0xFF64FFDA)),
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => const ModernChapterSearchScreen()),
                    );
                  },
                ),
                IconButton(
                  tooltip: 'محاكي العضيات 3D',
                  icon: const Icon(Icons.view_in_ar, color: Color(0xFF00B4D8)),
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => const ModernCellSimulationScreen()),
                    );
                  },
                ),
                const SizedBox(width: 8),
              ],
            ),

            // Bento Content Body
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // HERO BENTO CARD
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(20),
                      decoration: BoxDecoration(
                        gradient: const LinearGradient(
                          colors: [Color(0xFF112240), Color(0xFF0F3460)],
                          begin: Alignment.topLeft,
                          end: Alignment.bottomRight,
                        ),
                        borderRadius: BorderRadius.circular(24),
                        border: Border.all(color: const Color(0xFF64FFDA).withOpacity(0.35)),
                        boxShadow: [
                          BoxShadow(
                            color: const Color(0xFF64FFDA).withOpacity(0.12),
                            blurRadius: 24,
                            spreadRadius: 2,
                            offset: const Offset(0, 8),
                          ),
                        ],
                      ),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Row(
                                children: [
                                  Container(
                                    width: 8,
                                    height: 8,
                                    decoration: const BoxDecoration(
                                      color: Color(0xFF64FFDA),
                                      shape: BoxShape.circle,
                                    ),
                                  ),
                                  const SizedBox(width: 8),
                                  const Text(
                                    'معدل الإتقان التراكمي الشامل',
                                    style: TextStyle(color: Color(0xFF8892B0), fontSize: 13),
                                  ),
                                ],
                              ),
                              Container(
                                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                                decoration: BoxDecoration(
                                  color: const Color(0xFF64FFDA).withOpacity(0.12),
                                  borderRadius: BorderRadius.circular(20),
                                  border: Border.all(color: const Color(0xFF64FFDA).withOpacity(0.3)),
                                ),
                                child: const Text(
                                  'أوفلاين مفعل ⚡',
                                  style: TextStyle(color: Color(0xFF64FFDA), fontSize: 11, fontWeight: FontWeight.bold),
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 16),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text(
                                '${(overallProgress * 100).toInt()}%',
                                style: const TextStyle(
                                  color: Colors.white,
                                  fontSize: 38,
                                  fontWeight: FontWeight.w900,
                                  letterSpacing: -1,
                                ),
                              ),
                              ElevatedButton.icon(
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: const Color(0xFF64FFDA),
                                  foregroundColor: const Color(0xFF0A192F),
                                  elevation: 4,
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(14),
                                  ),
                                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                                ),
                                onPressed: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(builder: (_) => const ModernExamSimulationScreen()),
                                  );
                                },
                                icon: const Icon(Icons.bolt, size: 20),
                                label: const Text(
                                  'المحاكي الوزاري',
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 16),
                          ClipRRect(
                            borderRadius: BorderRadius.circular(10),
                            child: LinearProgressIndicator(
                              value: overallProgress,
                              backgroundColor: const Color(0xFF0A192F),
                              valueColor: const AlwaysStoppedAnimation<Color>(Color(0xFF64FFDA)),
                              minHeight: 8,
                            ),
                          ),
                          const SizedBox(height: 12),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: const [
                              Text('11 فصول دراسية معتمدة', style: TextStyle(color: Color(0xFF8892B0), fontSize: 11)),
                              Text('1,980 سؤال تدريبي مخزن', style: TextStyle(color: Color(0xFF8892B0), fontSize: 11)),
                            ],
                          ),
                        ],
                      ),
                    ),

                    const SizedBox(height: 24),

                    // Filter Selector Chips
                    SingleChildScrollView(
                      scrollDirection: Axis.horizontal,
                      child: Row(
                        children: [
                          _buildFilterTab(0, 'جميع الفصول (11)'),
                          const SizedBox(width: 8),
                          _buildFilterTab(1, 'الوحدة الأولى: التركيب والوظيفة'),
                          const SizedBox(width: 8),
                          _buildFilterTab(2, 'الوحدة الثانية: البيولوجيا الجزيئية'),
                        ],
                      ),
                    ),

                    const SizedBox(height: 20),

                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          'فصول المنهج التفاعلية',
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Text(
                          '${filteredChapters.length} فصول معروضة',
                          style: const TextStyle(color: Color(0xFF8892B0), fontSize: 12),
                        ),
                      ],
                    ),
                    const SizedBox(height: 14),

                    // 2-Column Bento Grid of Chapters
                    GridView.builder(
                      shrinkWrap: true,
                      physics: const NeverScrollableScrollPhysics(),
                      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                        crossAxisCount: 2,
                        crossAxisSpacing: 14,
                        mainAxisSpacing: 14,
                        childAspectRatio: 0.82,
                      ),
                      itemCount: filteredChapters.length,
                      itemBuilder: (context, index) {
                        final chapter = filteredChapters[index];
                        final double progress = (chapter['progress'] as num).toDouble();

                        return InkWell(
                          borderRadius: BorderRadius.circular(20),
                          onTap: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (_) => const ModernExamSimulationScreen(),
                              ),
                            );
                          },
                          child: Container(
                            padding: const EdgeInsets.all(16),
                            decoration: BoxDecoration(
                              color: const Color(0xFF112240),
                              borderRadius: BorderRadius.circular(20),
                              border: Border.all(
                                color: (progress == 1.0)
                                    ? const Color(0xFF2E7D32).withOpacity(0.6)
                                    : const Color(0xFF64FFDA).withOpacity(0.15),
                              ),
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.black.withOpacity(0.2),
                                  blurRadius: 10,
                                  offset: const Offset(0, 4),
                                ),
                              ],
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    Container(
                                      padding: const EdgeInsets.all(6),
                                      decoration: BoxDecoration(
                                        color: const Color(0xFF0A192F),
                                        borderRadius: BorderRadius.circular(10),
                                      ),
                                      child: Icon(
                                        chapter['icon'] as IconData,
                                        color: const Color(0xFF64FFDA),
                                        size: 18,
                                      ),
                                    ),
                                    SizedBox(
                                      width: 32,
                                      height: 32,
                                      child: Stack(
                                        alignment: Alignment.center,
                                        children: [
                                          CircularProgressIndicator(
                                            value: progress,
                                            backgroundColor: const Color(0xFF0A192F),
                                            color: progress >= 0.8 ? const Color(0xFF2E7D32) : const Color(0xFF64FFDA),
                                            strokeWidth: 3.5,
                                          ),
                                          Text(
                                            '${(progress * 100).toInt()}%',
                                            style: const TextStyle(
                                              color: Colors.white,
                                              fontSize: 9,
                                              fontWeight: FontWeight.bold,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  ],
                                ),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      chapter['title'] as String,
                                      style: const TextStyle(
                                        color: Color(0xFF64FFDA),
                                        fontSize: 11,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                    const SizedBox(height: 4),
                                    Text(
                                      chapter['name'] as String,
                                      style: const TextStyle(
                                        color: Colors.white,
                                        fontSize: 13,
                                        fontWeight: FontWeight.bold,
                                        height: 1.3,
                                      ),
                                      maxLines: 2,
                                      overflow: TextOverflow.ellipsis,
                                    ),
                                  ],
                                ),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    Row(
                                      children: [
                                        const Icon(Icons.quiz_outlined, color: Color(0xFF8892B0), size: 12),
                                        const SizedBox(width: 4),
                                        Text(
                                          '${chapter['questions']}',
                                          style: const TextStyle(color: Color(0xFF8892B0), fontSize: 11),
                                        ),
                                      ],
                                    ),
                                    Container(
                                      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                                      decoration: BoxDecoration(
                                        color: const Color(0xFF172A45),
                                        borderRadius: BorderRadius.circular(6),
                                      ),
                                      child: Text(
                                        chapter['tag'] as String,
                                        style: const TextStyle(
                                          color: Color(0xFF64FFDA),
                                          fontSize: 9,
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                        );
                      },
                    ),
                    const SizedBox(height: 32),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildFilterTab(int index, String label) {
    final isSelected = _selectedFilter == index;
    return InkWell(
      borderRadius: BorderRadius.circular(20),
      onTap: () => setState(() => _selectedFilter = index),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
        decoration: BoxDecoration(
          color: isSelected ? const Color(0xFF64FFDA) : const Color(0xFF112240),
          borderRadius: BorderRadius.circular(20),
          border: Border.all(
            color: isSelected ? const Color(0xFF64FFDA) : const Color(0xFF233554),
          ),
        ),
        child: Text(
          label,
          style: TextStyle(
            color: isSelected ? const Color(0xFF0A192F) : Colors.white,
            fontSize: 11,
            fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
          ),
        ),
      ),
    );
  }
}

// ==============================================================================
// SCREEN 2: THE IMMERSIVE EXAM SIMULATOR (WITH LOCAL AUTO-SAVE)
// ==============================================================================
class ModernExamSimulationScreen extends StatefulWidget {
  const ModernExamSimulationScreen({Key? key}) : super(key: key);

  @override
  State<ModernExamSimulationScreen> createState() => _ModernExamSimulationScreenState();
}

class _ModernExamSimulationScreenState extends State<ModernExamSimulationScreen> {
  int _currentIndex = 0;
  final Map<int, int> _userAnswers = {};
  Timer? _countdownTimer;
  int _secondsLeft = 3600; // 60 minutes official exam timer

  final List<Map<String, dynamic>> _questions = const [
    {
      'question': 'ما السبب الأساسي في استعادة الخلية النباتية لانتفاخها وامتلاء فجوتها العصارية بعد الذبول المؤقت؟',
      'options': [
        'دخول الماء بالخاصية الأسموزية نتيجة زيادة تركيز الذائبات داخل الفجوة العصارية',
        'ترسيب مادة الكيوتين غير المنفذة على الجدران الخلوية الخارجية للبشرة',
        'انتقال الماء بالنقل النشط ضد التدرج في التركيز باستهلاك جزيئات ATP',
        'تغلظ جدران الخلايا النباتية بمادتي اللجنين والسليلوز غير المائي'
      ],
      'answer': 0,
      'explanation': 'الدعامة الفسيولوجية دعامة مؤقتة تتناول الخلية ككل وتعتمد أساساً على دخول الماء بالخاصية الأسموزية للفجوة العصارية.'
    },
    {
      'question': 'أي الهرمونات الآتية يفرز من منطقة تحت المهاد ويؤثر مباشرة على الكليتين لتنظيم الضغط الأسموزي للدم؟',
      'options': [
        'هرمون الألدوستيرون من قشرة الكظرية',
        'الهرمون المضاد لإدرار البول (ADH / الفازوبرسين)',
        'الهرمون المنبه للغدة الدرقية (TSH)',
        'هرمون الباراثورمون المنظم للكالسيوم'
      ],
      'answer': 1,
      'explanation': 'يفرز ADH من الخلايا العصبية المفرزة بتحت المهاد ويصل للدم عبر الفص الخلفي للنخامية ليعيد امتصاص الماء بالنفرون.'
    },
    {
      'question': 'كم عدد مجموعات الفوسفات الحرة ومجموعات الهيدروكسيل الحرة في جزيء DNA حلقي (دائري) ببلازميد بكتيري؟',
      'options': [
        'مجموعتان فوسفات ومجموعتان هيدروكسيل (2, 2)',
        'صفر مجموعة فوسفات حرة وصفر مجموعة هيدروكسيل حرة (0, 0)',
        'مجموعة فوسفات واحدة ومجموعة هيدروكسيل واحدة (1, 1)',
        'أربع مجموعات فوسفات حرة'
      ],
      'answer': 1,
      'explanation': 'في الحمض النووي الحلقي أو الدائري تلتحم نهايات السلاسل تساهمياً فلا توجد أي نهايات حرة 3\' أو 5\'.'
    },
    {
      'question': 'عند إخصاب حبة لقاح لبويضة نبات زهري، ينتج نسيج الإندوسبيرم المغذي للجنين. ما عدد المجموعات الصبغية له؟',
      'options': [
        'أحادي المجموعة الصبغية (n)',
        'ثنائي المجموعة الصبغية (2n)',
        'ثلاثي المجموعة الصبغية (3n)',
        'رباعي المجموعة الصبغية (4n)'
      ],
      'answer': 2,
      'explanation': 'ينتج الإندوسبيرم من اندماج نواة زكرية (n) مع النواة الناتجة من اندماج نواتي الكيس الجنيني (2n) فيكون (3n).'
    },
  ];

  @override
  void initState() {
    super.initState();
    _startTimer();
    _loadExamState();
  }

  @override
  void dispose() {
    _countdownTimer?.cancel();
    super.dispose();
  }

  void _startTimer() {
    _countdownTimer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (_secondsLeft > 0) {
        setState(() => _secondsLeft--);
      } else {
        timer.cancel();
      }
    });
  }

  String _formatTimer(int totalSeconds) {
    final minutes = totalSeconds ~/ 60;
    final seconds = totalSeconds % 60;
    return '${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}';
  }

  Future<void> _saveExamState() async {
    final prefs = await SharedPreferences.getInstance();
    final payload = {
      'currentIndex': _currentIndex,
      'answers': _userAnswers.map((k, v) => MapEntry(k.toString(), v)),
      'timestamp': DateTime.now().toIso8601String(),
    };
    await prefs.setString('ultra_modern_exam_state', jsonEncode(payload));
  }

  Future<void> _loadExamState() async {
    final prefs = await SharedPreferences.getInstance();
    final raw = prefs.getString('ultra_modern_exam_state');
    if (raw != null) {
      final decoded = jsonDecode(raw);
      setState(() {
        _currentIndex = decoded['currentIndex'] ?? 0;
        if (decoded['answers'] is Map) {
          _userAnswers.addAll((decoded['answers'] as Map).map(
            (k, v) => MapEntry(int.parse(k.toString()), v as int),
          ));
        }
      });
    }
  }

  void _onOptionPicked(int optionIndex) {
    setState(() {
      _userAnswers[_currentIndex] = optionIndex;
    });
    _saveExamState();
  }

  @override
  Widget build(BuildContext context) {
    final currentQ = _questions[_currentIndex];
    final options = currentQ['options'] as List;

    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        backgroundColor: const Color(0xFF0A192F),
        appBar: AppBar(
          backgroundColor: const Color(0xFF112240),
          elevation: 0,
          title: Text(
            'محاكي الامتحان الوزاري (${_currentIndex + 1}/${_questions.length})',
            style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
          ),
          actions: [
            Container(
              margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
              decoration: BoxDecoration(
                color: const Color(0xFF0A192F),
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: const Color(0xFF64FFDA).withOpacity(0.4)),
              ),
              child: Row(
                children: [
                  const Icon(Icons.timer_outlined, color: Color(0xFF64FFDA), size: 16),
                  const SizedBox(width: 6),
                  Text(
                    _formatTimer(_secondsLeft),
                    style: const TextStyle(
                      color: Color(0xFF64FFDA),
                      fontSize: 12,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
        body: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Progress Bar
              ClipRRect(
                borderRadius: BorderRadius.circular(8),
                child: LinearProgressIndicator(
                  value: (_currentIndex + 1) / _questions.length,
                  backgroundColor: const Color(0xFF112240),
                  color: const Color(0xFF64FFDA),
                  minHeight: 6,
                ),
              ),
              const SizedBox(height: 20),

              // Question Glassmorphic Card
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(18),
                decoration: BoxDecoration(
                  color: const Color(0xFF112240),
                  borderRadius: BorderRadius.circular(20),
                  border: Border.all(color: const Color(0xFF233554)),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.2),
                      blurRadius: 10,
                    ),
                  ],
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          'سؤال رقم #${_currentIndex + 1}',
                          style: const TextStyle(color: Color(0xFF64FFDA), fontSize: 12, fontWeight: FontWeight.bold),
                        ),
                        const Text('درجتان (وزاري)', style: TextStyle(color: Color(0xFF8892B0), fontSize: 11)),
                      ],
                    ),
                    const SizedBox(height: 10),
                    Text(
                      currentQ['question'] as String,
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 15,
                        fontWeight: FontWeight.bold,
                        height: 1.5,
                      ),
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 18),
              const Text('خيارات الإجابة:', style: TextStyle(color: Color(0xFF8892B0), fontSize: 12)),
              const SizedBox(height: 10),

              // Options List
              Expanded(
                child: ListView.builder(
                  itemCount: options.length,
                  itemBuilder: (context, idx) {
                    final isSelected = _userAnswers[_currentIndex] == idx;
                    return InkWell(
                      borderRadius: BorderRadius.circular(14),
                      onTap: () => _onOptionPicked(idx),
                      child: AnimatedContainer(
                        duration: const Duration(milliseconds: 200),
                        margin: const EdgeInsets.only(bottom: 10),
                        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
                        decoration: BoxDecoration(
                          color: isSelected ? const Color(0xFF1E3A5F) : const Color(0xFF112240),
                          borderRadius: BorderRadius.circular(14),
                          border: Border.all(
                            color: isSelected ? const Color(0xFF64FFDA) : const Color(0xFF233554),
                            width: isSelected ? 1.5 : 1.0,
                          ),
                        ),
                        child: Row(
                          children: [
                            Container(
                              width: 22,
                              height: 22,
                              decoration: BoxDecoration(
                                shape: BoxShape.circle,
                                color: isSelected ? const Color(0xFF64FFDA) : Colors.transparent,
                                border: Border.all(
                                  color: isSelected ? const Color(0xFF64FFDA) : const Color(0xFF8892B0),
                                  width: 2,
                                ),
                              ),
                              child: isSelected
                                  ? const Icon(Icons.check, size: 14, color: Color(0xFF0A192F))
                                  : null,
                            ),
                            const SizedBox(width: 12),
                            Expanded(
                              child: Text(
                                options[idx].toString(),
                                style: TextStyle(
                                  color: isSelected ? Colors.white : const Color(0xFFCCD6F6),
                                  fontSize: 13,
                                  fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                                  height: 1.3,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                ),
              ),

              // Nav Buttons
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  OutlinedButton.icon(
                    style: OutlinedButton.styleFrom(
                      foregroundColor: Colors.white,
                      side: const BorderSide(color: Color(0xFF233554)),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                    ),
                    onPressed: _currentIndex > 0 ? () => setState(() => _currentIndex--) : null,
                    icon: const Icon(Icons.arrow_forward_ios, size: 14),
                    label: const Text('السابق'),
                  ),
                  ElevatedButton.icon(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFF64FFDA),
                      foregroundColor: const Color(0xFF0A192F),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                    ),
                    onPressed: () {
                      if (_currentIndex < _questions.length - 1) {
                        setState(() => _currentIndex++);
                      } else {
                        showDialog(
                          context: context,
                          builder: (ctx) => AlertDialog(
                            backgroundColor: const Color(0xFF112240),
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                            title: Row(
                              children: const [
                                Icon(Icons.verified, color: Color(0xFF64FFDA)),
                                SizedBox(width: 8),
                                Text('اكتمال تسليم الامتحان', style: TextStyle(color: Colors.white, fontSize: 16)),
                              ],
                            ),
                            content: const Text(
                              'تم رصد وحفظ جميع إجاباتك محلياً في ذاكرة التخزين الدائمة دون أي اتصال بالإنترنت.',
                              style: TextStyle(color: Color(0xFF8892B0), fontSize: 13),
                            ),
                            actions: [
                              TextButton(
                                onPressed: () {
                                  Navigator.pop(ctx);
                                  Navigator.pop(context);
                                },
                                child: const Text('العودة للرئيسية', style: TextStyle(color: Color(0xFF64FFDA))),
                              ),
                            ],
                          ),
                        );
                      }
                    },
                    icon: Icon(_currentIndex < _questions.length - 1 ? Icons.arrow_back_ios : Icons.done_all, size: 14),
                    label: Text(
                      _currentIndex < _questions.length - 1 ? 'التالي' : 'تسليم الامتحان',
                      style: const TextStyle(fontWeight: FontWeight.bold),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// ==============================================================================
// SCREEN 3: THE 3D-LIKE CELLULAR ORGANELLE VIEWPORT
// ==============================================================================
class ModernCellSimulationScreen extends StatefulWidget {
  const ModernCellSimulationScreen({Key? key}) : super(key: key);

  @override
  State<ModernCellSimulationScreen> createState() => _ModernCellSimulationScreenState();
}

class _ModernCellSimulationScreenState extends State<ModernCellSimulationScreen> {
  int _selectedOrganelleIndex = 0;

  final List<Map<String, dynamic>> _organelles = const [
    {
      'title': 'الميتوكوندريا (Mitochondria)',
      'english': 'Powerhouse of the Cell',
      'function': 'موقع إنتاج جزيئات ATP عبر تفاعلات دورة كريبس وسلسلة نقل الإلكترون والأكسدة الفوسفورية.',
      'icon': Icons.bolt,
      'gradient': [Color(0xFFE63946), Color(0xFFF1FAEE)],
      'details': [
        'الغشاء الداخلي ممتد ومطوي ليشكل الأعراف (Cristae).',
        'الأعراف تزيد من مساحة السطح المخصصة لإنزيمات التنفس الخلوي.',
        'تحتوي على DNA وميتوريبوسومات خاصة بها.',
      ],
    },
    {
      'title': 'النواة والمادة الوراثية (Nucleus & DNA)',
      'english': 'Master Genetic Control',
      'function': 'تخزين الكروموسومات والجينات، وضبط جميع الأنشطة الحيوية والتضاعف الخلوي وتخليق RNA.',
      'icon': Icons.center_focus_strong,
      'gradient': [Color(0xFF64FFDA), Color(0xFF00B4D8)],
      'details': [
        'غشاء نووي مزدوج مثقب بمسام نووية دقيقة لمرور جزيئات mRNA و tRNA.',
        'نوية غنية بالبروتينات و rRNA لتكوين الريبوسومات.',
        'شبكة كروماتينية تلتف حول بروتينات الهستونات.',
      ],
    },
    {
      'title': 'البلاستيدات الخضراء (Chloroplasts)',
      'english': 'Photosynthetic Machinery',
      'function': 'موقع التفاعلات الضوئية واللاضوئية لتحويل الطاقة الشمسية إلى طاقة كيميائية بمركب PGAL.',
      'icon': Icons.eco,
      'gradient': [Color(0xFF2E7D32), Color(0xFF64FFDA)],
      'details': [
        'أقراص الجرانا (Grana) المحتوية على أصباغ الكلوروفيل أ وب.',
        'حشوة الستروما (Stroma) السائلة لمسار تفاعلات كالفن وإنتاج الجلوكوز.',
        'حبيبات نشا دقيقة كمخزن احتياطي مؤقت للطاقة.',
      ],
    },
    {
      'title': 'الريبوسومات (Ribosomes)',
      'english': 'Protein Synthesizing Units',
      'function': 'قراءة شفرات mRNA وترجمة كودونات الأحماض الأمينية لبناء السلاسل الببتيدية للبروتينات.',
      'icon': Icons.scatter_plot,
      'gradient': [Color(0xFFFFB703), Color(0xFFFB8500)],
      'details': [
        'تتكون من تحت وحدتي ريبوسوم: كبرى وصغرى.',
        'تحتوي على موقعي الربط P (الببتيديل) و A (الأمينوأسيل).',
        'موزعة حرة في السيتوبلازم أو مرتبطة بالشبكة الإندوبلازمية الخشنة.',
      ],
    },
  ];

  @override
  Widget build(BuildContext context) {
    final active = _organelles[_selectedOrganelleIndex];

    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        backgroundColor: const Color(0xFF0A192F),
        appBar: AppBar(
          backgroundColor: const Color(0xFF112240),
          elevation: 0,
          title: const Text('محاكي العضيات الخلوية ثلاثي الأبعاد', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
        ),
        body: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // 3D-Like Viewport Card
              Container(
                height: 220,
                width: double.infinity,
                decoration: BoxDecoration(
                  gradient: const RadialGradient(
                    center: Alignment(0, -0.2),
                    radius: 1.2,
                    colors: [Color(0xFF1D3557), Color(0xFF0A192F)],
                  ),
                  borderRadius: BorderRadius.circular(24),
                  border: Border.all(color: const Color(0xFF64FFDA).withOpacity(0.3)),
                  boxShadow: [
                    BoxShadow(
                      color: const Color(0xFF64FFDA).withOpacity(0.1),
                      blurRadius: 20,
                      offset: const Offset(0, 4),
                    ),
                  ],
                ),
                child: Stack(
                  alignment: Alignment.center,
                  children: [
                    Positioned(
                      top: 12,
                      right: 16,
                      child: Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        decoration: BoxDecoration(
                          color: const Color(0xFF112240).withOpacity(0.8),
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(color: const Color(0xFF64FFDA).withOpacity(0.3)),
                        ),
                        child: const Text('3D Viewport Active', style: TextStyle(color: Color(0xFF64FFDA), fontSize: 10)),
                      ),
                    ),
                    Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Container(
                          padding: const EdgeInsets.all(20),
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            color: const Color(0xFF112240),
                            border: Border.all(color: const Color(0xFF64FFDA), width: 2),
                            boxShadow: [
                              BoxShadow(
                                color: const Color(0xFF64FFDA).withOpacity(0.3),
                                blurRadius: 30,
                              ),
                            ],
                          ),
                          child: Icon(active['icon'] as IconData, size: 54, color: const Color(0xFF64FFDA)),
                        ),
                        const SizedBox(height: 12),
                        Text(
                          active['title'] as String,
                          style: const TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
                        ),
                        Text(
                          active['english'] as String,
                          style: const TextStyle(color: Color(0xFF8892B0), fontSize: 11),
                        ),
                      ],
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 18),
              const Text('اختر العضية للمعاينة الحيوية:', style: TextStyle(color: Color(0xFF8892B0), fontSize: 12)),
              const SizedBox(height: 10),

              // Organelle Chips
              SizedBox(
                height: 40,
                child: ListView.builder(
                  scrollDirection: Axis.horizontal,
                  itemCount: _organelles.length,
                  itemBuilder: (context, i) {
                    final isSel = _selectedOrganelleIndex == i;
                    return InkWell(
                      onTap: () => setState(() => _selectedOrganelleIndex = i),
                      borderRadius: BorderRadius.circular(12),
                      child: Container(
                        margin: const EdgeInsets.only(left: 8),
                        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
                        decoration: BoxDecoration(
                          color: isSel ? const Color(0xFF64FFDA) : const Color(0xFF112240),
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(
                            color: isSel ? const Color(0xFF64FFDA) : const Color(0xFF233554),
                          ),
                        ),
                        child: Text(
                          _organelles[i]['title'].toString().split(' ')[0],
                          style: TextStyle(
                            color: isSel ? const Color(0xFF0A192F) : Colors.white,
                            fontSize: 12,
                            fontWeight: isSel ? FontWeight.bold : FontWeight.normal,
                          ),
                        ),
                      ),
                    );
                  },
                ),
              ),

              const SizedBox(height: 16),

              // Function & Details Card
              Expanded(
                child: Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(18),
                  decoration: BoxDecoration(
                    color: const Color(0xFF112240),
                    borderRadius: BorderRadius.circular(20),
                    border: Border.all(color: const Color(0xFF233554)),
                  ),
                  child: ListView(
                    children: [
                      const Text(
                        'الوظيفة الفسيولوجية الأساسية:',
                        style: TextStyle(color: Color(0xFF64FFDA), fontSize: 13, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        active['function'] as String,
                        style: const TextStyle(color: Colors.white, fontSize: 13, height: 1.5),
                      ),
                      const Divider(color: Color(0xFF233554), height: 28),
                      const Text(
                        'نقاط الامتحانات الوزارية المركزة:',
                        style: TextStyle(color: Color(0xFF64FFDA), fontSize: 13, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 8),
                      ...(active['details'] as List<String>).map((point) => Padding(
                            padding: const EdgeInsets.only(bottom: 8.0),
                            child: Row(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Text('• ', style: TextStyle(color: Color(0xFF64FFDA), fontSize: 16)),
                                Expanded(
                                  child: Text(point, style: const TextStyle(color: Color(0xFFCCD6F6), fontSize: 12, height: 1.4)),
                                ),
                              ],
                            ),
                          )),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// ==============================================================================
// SCREEN 4: THE INSTANT OFFLINE SEARCH ENGINE
// ==============================================================================
class ModernChapterSearchScreen extends StatefulWidget {
  const ModernChapterSearchScreen({Key? key}) : super(key: key);

  @override
  State<ModernChapterSearchScreen> createState() => _ModernChapterSearchScreenState();
}

class _ModernChapterSearchScreenState extends State<ModernChapterSearchScreen> {
  final TextEditingController _searchController = TextEditingController();
  String _query = '';

  final List<Map<String, String>> _glossary = const [
    {'title': 'الدعامة الفسيولوجية', 'chap': 'الفصل الأول', 'desc': 'دعامة مؤقتة تعتمد على امتلاء الفجوة العصارية بالماء بالخاصية الأسموزية.'},
    {'title': 'الدعامة التركيبية', 'chap': 'الفصل الأول', 'desc': 'دعامة دائمة تعتمد على ترسيب مواد صلبة مثل السليلوز، اللجنين، الكيوتين، والسوبرين.'},
    {'title': 'نظرية الخيوط المنزلقة', 'chap': 'الفصل الأول', 'desc': 'تفسير هكسلي لانقباض العضلة الهيكلية عبر الروابط المستعرضة بمساعدة أيونات الكالسيوم وATP.'},
    {'title': 'الغدة النخامية (المايسترو)', 'chap': 'الفصل الثاني', 'desc': 'غدة صماء تتحكم في إفراز باقي الغدد، تفرز هرمون النمو GH وهرمونات التنبيه TSH وACTH وFSH وLH.'},
    {'title': 'الهرمون المضاد لإدرار البول ADH', 'chap': 'الفصل الثاني', 'desc': 'يعمل على تقليل كمية البول المفرز برفع إعادة امتصاص الماء في النفرونات.'},
    {'title': 'التوالد البكري Parthenogenesis', 'chap': 'الفصل الثالث', 'desc': 'قدرة البويضة على النمو لتكوين فرد جديد بدون إخصاب من المشيج الذكري كذكور نحل العسل وحشرة المن.'},
    {'title': 'تعاقب الأجيال Alternation of Generations', 'chap': 'الفصل الثالث', 'desc': 'دورة حياة يتعاقب فيها جيل جنسي يتكاثر بالأمشاج مع جيل لاجنسي يتكاثر بالجراثيم كالفوجير وبلازموديوم الملاريا.'},
    {'title': 'المناعة الخلطية Humoral Immunity', 'chap': 'الفصل الرابع', 'desc': 'استجابة مناعية تتوسطها الخلايا البائية المنتجة للأجسام المضادة في سوائل الجسم (الدم والليمف).'},
    {'title': 'المناعة الخلوية Cell-Mediated Immunity', 'chap': 'الفصل الرابع', 'desc': 'استجابة مناعية تتوسطها الخلايا التائية T وخاصة Tc وTh لمهاجمة الخلايا المصابة بالسرطان أو الفيروسات.'},
    {'title': 'إنزيم بلمرة DNA (DNA Polymerase)', 'chap': 'الفصل الخامس', 'desc': 'يقوم ببناء شريط DNA الجديد بإضافة نيوكليوتيدات متتالية في الاتجاه من 5\' إلى 3\' فقط.'},
    {'title': 'إنزيمات القصر البكتيرية (Restriction Enzymes)', 'chap': 'الفصل السادس', 'desc': 'إنزيمات تتعرف على تتابعات محددة (4-7 نيوكليوتيدات) على DNA وتقوم بقطعه تاركة أطرافاً لاصقة.'},
  ];

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final results = _glossary.where((item) {
      final q = _query.trim().toLowerCase();
      if (q.isEmpty) return true;
      return item['title']!.toLowerCase().contains(q) ||
          item['desc']!.toLowerCase().contains(q) ||
          item['chap']!.toLowerCase().contains(q);
    }).toList();

    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        backgroundColor: const Color(0xFF0A192F),
        appBar: AppBar(
          backgroundColor: const Color(0xFF112240),
          elevation: 0,
          title: const Text('محرك البحث المنهجي الفوري (Offline)', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
        ),
        body: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            children: [
              // Search Input
              Container(
                decoration: BoxDecoration(
                  color: const Color(0xFF112240),
                  borderRadius: BorderRadius.circular(16),
                  border: Border.all(color: const Color(0xFF64FFDA).withOpacity(0.3)),
                ),
                child: TextField(
                  controller: _searchController,
                  style: const TextStyle(color: Colors.white, fontSize: 14),
                  onChanged: (val) => setState(() => _query = val),
                  decoration: InputDecoration(
                    hintText: 'ابحث عن أي مصطلح، إنزيم، هرمون، أو قانون...',
                    hintStyle: const TextStyle(color: Color(0xFF8892B0), fontSize: 13),
                    prefixIcon: const Icon(Icons.search, color: Color(0xFF64FFDA)),
                    suffixIcon: _query.isNotEmpty
                        ? IconButton(
                            icon: const Icon(Icons.clear, color: Color(0xFF8892B0)),
                            onPressed: () {
                              _searchController.clear();
                              setState(() => _query = '');
                            },
                          )
                        : null,
                    border: InputBorder.none,
                    contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                  ),
                ),
              ),

              const SizedBox(height: 16),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    'النتائج المتطابقة (${results.length})',
                    style: const TextStyle(color: Color(0xFF8892B0), fontSize: 12),
                  ),
                  const Text('مخزنة محلياً 100%', style: TextStyle(color: Color(0xFF64FFDA), fontSize: 11)),
                ],
              ),
              const SizedBox(height: 12),

              // Results List
              Expanded(
                child: results.isEmpty
                    ? Center(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: const [
                            Icon(Icons.search_off, size: 48, color: Color(0xFF8892B0)),
                            SizedBox(height: 12),
                            Text('لم يتم العثور على نتائج مطابقة للبحث', style: TextStyle(color: Color(0xFF8892B0), fontSize: 14)),
                          ],
                        ),
                      )
                    : ListView.builder(
                        itemCount: results.length,
                        itemBuilder: (context, i) {
                          final item = results[i];
                          return Container(
                            margin: const EdgeInsets.only(bottom: 12),
                            padding: const EdgeInsets.all(16),
                            decoration: BoxDecoration(
                              color: const Color(0xFF112240),
                              borderRadius: BorderRadius.circular(16),
                              border: Border.all(color: const Color(0xFF233554)),
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    Text(
                                      item['title']!,
                                      style: const TextStyle(
                                        color: Color(0xFF64FFDA),
                                        fontSize: 15,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                    Container(
                                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                                      decoration: BoxDecoration(
                                        color: const Color(0xFF172A45),
                                        borderRadius: BorderRadius.circular(8),
                                      ),
                                      child: Text(
                                        item['chap']!,
                                        style: const TextStyle(color: Color(0xFF8892B0), fontSize: 10),
                                      ),
                                    ),
                                  ],
                                ),
                                const SizedBox(height: 8),
                                Text(
                                  item['desc']!,
                                  style: const TextStyle(color: Color(0xFFCCD6F6), fontSize: 13, height: 1.4),
                                ),
                              ],
                            ),
                          );
                        },
                      ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
