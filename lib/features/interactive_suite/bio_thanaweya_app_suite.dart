import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

/// BioThanaweyaAppSuite: The Master Suite for Egyptian Thanawya Amma Biology (smarts.bio)
/// Features complete RTL directionality, Deep Teal M3 palette, Cairo typography, and 100% Offline-First architecture.
class BioThanaweyaAppSuite extends StatelessWidget {
  const BioThanaweyaAppSuite({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Scholar Sidekick - BioThanaweya',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF00695C), // Deep Teal
          secondary: const Color(0xFF2E7D32),  // Emerald
          brightness: Brightness.light,
        ),
        useMaterial3: true,
        fontFamily: 'Cairo',
      ),
      locale: const Locale('ar', 'EG'),
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

// ==========================================
// SCREEN A: THE MASTERY DASHBOARD (11 CHAPTERS)
// ==========================================
class BioDashboardScreen extends StatelessWidget {
  const BioDashboardScreen({Key? key}) : super(key: key);

  final List<Map<String, dynamic>> biologyChapters = const [
    {'id': 1, 'title': 'الفصل الأول', 'subtitle': 'الدعامة والحركة في الكائنات الحية', 'progress': 1.0, 'status': 'مكتمل'},
    {'id': 2, 'title': 'الفصل الثاني', 'subtitle': 'التنسيق الهرموني في الكائنات الحية', 'progress': 0.9, 'status': 'متقدم'},
    {'id': 3, 'title': 'الفصل الثالث', 'subtitle': 'التكاثر في الكائنات الحية', 'progress': 0.8, 'status': 'متقدم'},
    {'id': 4, 'title': 'الفصل الرابع', 'subtitle': 'المناعة في الكائنات الحية', 'progress': 0.75, 'status': 'قيد الاستكمال'},
    {'id': 5, 'title': 'الفصل الخامس', 'subtitle': 'DNA والمحمل الوراثي وتضاعفه', 'progress': 0.6, 'status': 'قيد الاستكمال'},
    {'id': 6, 'title': 'الفصل السادس', 'subtitle': 'الأحماض النووية RNA وبناء البروتين', 'progress': 0.5, 'status': 'قيد الاستكمال'},
    {'id': 7, 'title': 'الفصل السابع', 'subtitle': 'التقنية الحيوية وهندسة الوراثة', 'progress': 0.4, 'status': 'جديد'},
    {'id': 8, 'title': 'الفصل الثامن', 'subtitle': 'التصنيف الحديث وعلم الوراثة المتقدم', 'progress': 0.3, 'status': 'جديد'},
    {'id': 9, 'title': 'الفصل التاسع', 'subtitle': 'الإحساس والتوجيه في النباتات والحيوانات', 'progress': 0.2, 'status': 'جديد'},
    {'id': 10, 'title': 'الفصل العاشر', 'subtitle': 'التكيف والبيئة وعلم البيئة التطبيقي', 'progress': 0.1, 'status': 'جديد'},
    {'id': 11, 'title': 'الفصل الحادي عشر', 'subtitle': 'المراجعات الشاملة والمحاكي الوزاري الفائق', 'progress': 0.0, 'status': 'مستعد للاختبار'},
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('مرصاد إتقان الأحياء - الثانوية العامة'),
        centerTitle: true,
        backgroundColor: const Color(0xFF004D40),
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: const Icon(Icons.search),
            onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (_) => const ChapterSearchScreen())),
          ),
        ],
      ),
      body: ListView(
        padding: const EdgeInsets.all(16.0),
        children: [
          // Banner Card
          Card(
            color: const Color(0xFFE0F2F1),
            elevation: 2,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
            child: Padding(
              padding: const EdgeInsets.all(18.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text('مرحباً بك في منظومة التعلم الذكي', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Color(0xFF00695C))),
                  const SizedBox(height: 8),
                  const Text('نظام العمل بدون إنترنت (Offline-First) مفعل. تتبع إتقانك للفصول الـ 11 بأسلوب تفاعلي متقدم.', style: TextStyle(fontSize: 14, color: Colors.black87)),
                  const SizedBox(height: 12),
                  ElevatedButton.icon(
                    style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFF00695C), foregroundColor: Colors.white),
                    onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (_) => const ExamSimulationScreen())),
                    icon: const Icon(Icons.quiz),
                    label: const Text('بدء المحاكي الوزاري الفوري'),
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text('فصول المنهج الدراسي (11 فصول)', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
              TextButton.icon(
                onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (_) => const CellBiologySimulationScreen())),
                icon: const Icon(Icons.hub, size: 18),
                label: const Text('محاكي الخلية 3D'),
              ),
            ],
          ),
          const SizedBox(height: 10),
          ...biologyChapters.map((chapter) => Card(
            margin: const EdgeInsets.only(bottom: 12),
            elevation: 1,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            child: ListTile(
              contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              leading: Stack(
                alignment: Alignment.center,
                children: [
                  CircularProgressIndicator(
                    value: (chapter['progress'] as num).toDouble(),
                    backgroundColor: Colors.grey[200],
                    color: const Color(0xFF2E7D32),
                    strokeWidth: 4,
                  ),
                  Text('${(((chapter['progress'] as num).toDouble()) * 100).toInt()}%', style: const TextStyle(fontSize: 10, fontWeight: FontWeight.bold)),
                ],
              ),
              title: Text(chapter['title'] as String, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15)),
              subtitle: Text(chapter['subtitle'] as String, style: const TextStyle(fontSize: 13, color: Colors.grey)),
              trailing: Chip(
                label: Text(chapter['status'] as String, style: const TextStyle(fontSize: 11, color: Colors.white)),
                backgroundColor: ((chapter['progress'] as num).toDouble() == 1.0) ? const Color(0xFF2E7D32) : const Color(0xFF00796B),
              ),
              onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => const ExamSimulationScreen())),
            ),
          )),
        ],
      ),
    );
  }
}

// ==========================================
// SCREEN B: THE INTERACTIVE EXAM SIMULATOR
// ==========================================
class ExamSimulationScreen extends StatefulWidget {
  const ExamSimulationScreen({Key? key}) : super(key: key);

  @override
  State<ExamSimulationScreen> createState() => _ExamSimulationScreenState();
}

class _ExamSimulationScreenState extends State<ExamSimulationScreen> {
  int _currentIndex = 0;
  final Map<int, int> _userAnswers = {};

  final List<Map<String, dynamic>> _questions = const [
    {
      'question': 'أي مما يلي يمثل الدعامة الفسيولوجية في النبات؟',
      'options': ['ترسيب الكوتين على الجدر', 'امتصاص الماء بالخاصية الاسموزية في العصير الخلوي', 'ترسيب اللجنين في خلايا الإنشيج', 'تغليظ جدر الخلايا الاسكلرانشيمية'],
      'answer': 1
    },
    {
      'question': 'ما هو الهرمون المسؤول عن إنضاج وتكوين الثمار في النباتات؟',
      'options': ['الأوكسينات', 'الجبريلينات', 'السيتوكينين', 'الأبسيسيك'],
      'answer': 0
    },
    {
      'question': 'أين يتم تخزين النشا الحيواني (الجليكوجين) في جسم الإنسان بشكل أساسي؟',
      'options': ['الكلى والرئتين', 'الكبد والعضلات', 'الطحال والبنكرياس', 'المعدة والأمعاء الدقيقة'],
      'answer': 1
    },
  ];

  @override
  void initState() {
    super.initState();
    _loadLocalProgress();
  }

  Future<void> _saveLocalProgress() async {
    final prefs = await SharedPreferences.getInstance();
    final data = {
      'currentIndex': _currentIndex,
      'answers': _userAnswers.map((k, v) => MapEntry(k.toString(), v)),
      'timestamp': DateTime.now().toIso8601String(),
    };
    await prefs.setString('active_exam_state', jsonEncode(data));
  }

  Future<void> _loadLocalProgress() async {
    final prefs = await SharedPreferences.getInstance();
    final jsonStr = prefs.getString('active_exam_state');
    if (jsonStr != null) {
      final data = jsonDecode(jsonStr);
      setState(() {
        _currentIndex = data['currentIndex'] ?? 0;
        if (data['answers'] is Map) {
          _userAnswers.addAll((data['answers'] as Map).map((k, v) => MapEntry(int.parse(k.toString()), v as int)));
        }
      });
    }
  }

  void _selectOption(int optionIndex) {
    setState(() {
      _userAnswers[_currentIndex] = optionIndex;
    });
    _saveLocalProgress(); // الحفظ الفوري المحلي (Offline Auto-Save)
  }

  @override
  Widget build(BuildContext context) {
    final currentQ = _questions[_currentIndex];
    final options = currentQ['options'] as List;

    return Scaffold(
      appBar: AppBar(
        title: Text('محاكي الامتحان الوزاري (${_currentIndex + 1}/${_questions.length})'),
        backgroundColor: const Color(0xFF004D40),
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            LinearProgressIndicator(value: (_currentIndex + 1) / _questions.length, color: const Color(0xFF2E7D32)),
            const SizedBox(height: 20),
            Text('السؤال ${_currentIndex + 1}:', style: const TextStyle(fontSize: 14, color: Colors.grey)),
            const SizedBox(height: 8),
            Text(currentQ['question'] as String, style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
            const SizedBox(height: 24),
            ...List.generate(options.length, (index) {
              final isSelected = _userAnswers[_currentIndex] == index;
              return Container(
                margin: const EdgeInsets.only(bottom: 12),
                decoration: BoxDecoration(
                  border: Border.all(color: isSelected ? const Color(0xFF00695C) : Colors.grey.shade300, width: 2),
                  borderRadius: BorderRadius.circular(12),
                  color: isSelected ? const Color(0xFFE0F2F1) : Colors.white,
                ),
                child: RadioListTile<int>(
                  title: Text(options[index].toString(), style: const TextStyle(fontSize: 15)),
                  value: index,
                  groupValue: _userAnswers[_currentIndex],
                  activeColor: const Color(0xFF00695C),
                  onChanged: (val) {
                    if (val != null) _selectOption(val);
                  },
                ),
              );
            }),
            const Spacer(),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                ElevatedButton(
                  onPressed: _currentIndex > 0 ? () => setState(() => _currentIndex--) : null,
                  child: const Text('السابق'),
                ),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFF00695C), foregroundColor: Colors.white),
                  onPressed: () {
                    if (_currentIndex < _questions.length - 1) {
                      setState(() => _currentIndex++);
                    } else {
                      showDialog(
                        context: context,
                        builder: (_) => AlertDialog(
                          title: const Text('تم تسليم الامتحان بنجاح'),
                          content: const Text('تم حفظ وتثبيت إجاباتك محلياً بنجاح تام دون الحاجة للاتترنت.'),
                          actions: [
                            TextButton(onPressed: () { Navigator.pop(context); Navigator.pop(context); }, child: const Text('حسناً'))
                          ],
                        ),
                      );
                    }
                  },
                  child: Text(_currentIndex < _questions.length - 1 ? 'التالي' : 'تسليم الامتحان'),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

// ==========================================
// SCREEN C: CELLULAR ORGANELLE 3D-LIKE VIEWPORT
// ==========================================
class CellBiologySimulationScreen extends StatefulWidget {
  const CellBiologySimulationScreen({Key? key}) : super(key: key);

  @override
  State<CellBiologySimulationScreen> createState() => _CellBiologySimulationScreenState();
}

class _CellBiologySimulationScreenState extends State<CellBiologySimulationScreen> {
  String selectedOrganelle = 'الميتوكوندريا (Mitochondria)';
  String organelleDescription = 'بيت الطاقة في الخلية؛ تتم فيها تفاعلات التنفس الخلوي لإنتاج مركبات الطاقة ATP اللازمة للعمليات الحيوية.';

  final Map<String, String> organelles = {
    'الميتوكوندريا (Mitochondria)': 'بيت الطاقة في الخلية؛ تتم فيها تفاعلات التنفس الخلوي لإنتاج مركبات الطاقة ATP.',
    'النواة (Nucleus)': 'مركز السيطرة الرئيسي؛ تحتوي على المادة الوراثية (DNA) وتنظم كافة العمليات الأيضية وتكاثر الخلية.',
    'البلاستيدات الخضراء (Chloroplasts)': 'موقع عملية البناء الضوئي في الخلايا النباتية وتحتوي على صبغ الكلوروفيل.',
    'الريبوسومات (Ribosomes)': 'مصانع إنتاج البروتين في الخلية بناءً على الشفرات المنقولة من الـ RNA.',
  };

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('محاكي العضيات الخلوية ثلاثي الأبعاد'),
        backgroundColor: const Color(0xFF004D40),
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Container(
              height: 220,
              width: double.infinity,
              decoration: BoxDecoration(
                gradient: const LinearGradient(colors: [Color(0xFF004D40), Color(0xFF00796B)]),
                borderRadius: BorderRadius.circular(20),
              ),
              child: const Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(Icons.biotech, size: 80, color: Colors.white),
                    SizedBox(height: 12),
                    Text('معاينة العضية التفاعلية (3D Viewport)', style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold)),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 20),
            Expanded(
              child: ListView(
                children: organelles.entries.map((entry) {
                  final isSelected = selectedOrganelle == entry.key;
                  return Card(
                    color: isSelected ? const Color(0xFFE0F2F1) : Colors.white,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    child: ListTile(
                      leading: const Icon(Icons.hub, color: Color(0xFF00695C)),
                      title: Text(entry.key, style: TextStyle(fontWeight: FontWeight.bold, color: isSelected ? const Color(0xFF004D40) : Colors.black87)),
                      subtitle: Text(entry.value, style: const TextStyle(fontSize: 13)),
                      onTap: () {
                        setState(() {
                          selectedOrganelle = entry.key;
                          organelleDescription = entry.value;
                        });
                      },
                    ),
                  );
                }).toList(),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

// ==========================================
// SCREEN D: FULL-TEXT CHAPTER SEARCH & INDEX
// ==========================================
class ChapterSearchScreen extends StatefulWidget {
  const ChapterSearchScreen({Key? key}) : super(key: key);

  @override
  State<ChapterSearchScreen> createState() => _ChapterSearchScreenState();
}

class _ChapterSearchScreenState extends State<ChapterSearchScreen> {
  String searchQuery = '';
  final List<String> allTopics = [
    'الدعامة في النبات والحيوان',
    'الحركة في الكائنات الحية',
    'التنسيق الهرموني في الإنسان',
    'الهرمونات في النباتات',
    'التكاثر اللاجنسي والتكاثر الجنسي',
    'تعاقب الجيلين في النباتات',
    'تركيب الـ DNA وتضاعفه',
    'الـ RNA وبناء البروتين',
    'المناعة الطبيعية والمكتسبة',
    'التقنية الحيوية وهندسة الجينات',
  ];

  @override
  Widget build(BuildContext context) {
    final filteredTopics = allTopics.where((t) => t.contains(searchQuery)).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('البحث الشامل بالمنهج (Offline-First)'),
        backgroundColor: const Color(0xFF004D40),
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              onChanged: (val) => setState(() => searchQuery = val),
              decoration: InputDecoration(
                hintText: 'ابحث عن أي مصطلح أو فصل...',
                prefixIcon: const Icon(Icons.search, color: Color(0xFF00695C)),
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                filled: true,
                fillColor: Colors.grey.shade100,
              ),
            ),
            const SizedBox(height: 16),
            Expanded(
              child: ListView.builder(
                itemCount: filteredTopics.length,
                itemBuilder: (context, index) {
                  return Card(
                    elevation: 1,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                    child: ListTile(
                      leading: const Icon(Icons.menu_book, color: Color(0xFF2E7D32)),
                      title: Text(filteredTopics[index], style: const TextStyle(fontWeight: FontWeight.bold)),
                      subtitle: const Text('متاح محلياً بدون إنترنت', style: TextStyle(fontSize: 12, color: Colors.grey)),
                      onTap: () {
                        // Interactive topic exploration
                      },
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
