import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/services/bio_app_controller.dart';
import '../../core/services/curriculum_data_service.dart';
import '../smart_quiz_engine/presentation/controllers/quiz_providers.dart';
import '../smart_quiz_engine/presentation/screens/smart_quiz_screen.dart';
import '../biology_lab/presentation/screens/biology_lab_screen.dart';
import '../study_planner/presentation/screens/smart_planner_screen.dart';
import '../architecture_hub/presentation/screens/app_architecture_hub.dart';

// ==============================================================================
// 🧬 BOUND BIO APP ROOT (Controller-Driven UI Suite)
// ==============================================================================
// Bridges BioAppController reactive state management with production UI components
// for Dashboard navigation, real-time Exam Simulation, and Offline Search.
// ==============================================================================

class BoundBioAppRoot extends StatelessWidget {
  BoundBioAppRoot({Key? key}) : super(key: key);

  final BioAppController _appController = BioAppController();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Scholar Sidekick - BioThanaweya',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        brightness: Brightness.dark,
        scaffoldBackgroundColor: const Color(0xFF0A192F),
        primaryColor: const Color(0xFF64FFDA),
        colorScheme: const ColorScheme.dark(
          primary: Color(0xFF64FFDA),
          secondary: Color(0xFF1D3557),
          surface: Color(0xFF112240),
          background: Color(0xFF0A192F),
        ),
        fontFamily: 'Cairo',
        useMaterial3: true,
      ),
      locale: const Locale('ar', 'EG'),
      builder: (context, child) {
        return Directionality(
          textDirection: TextDirection.rtl,
          child: child ?? const SizedBox.shrink(),
        );
      },
      home: BoundDashboardScreen(controller: _appController),
    );
  }
}

// تعريف الألوان الأساسية لمنظومة Bioluminescent Dark Theme
class BioTheme {
  static const Color background = Color(0xFF0F0F17);
  static const Color surfaceGlass = Color(0xFF1E1E2C);
  static const Color bioGreen = Color(0xFF00FF66);
  static const Color warningAmber = Color(0xFFFFB800);
  static const Color molecularViolet = Color(0xFF7C3AED);
  static const Color textPrimary = Color(0xFFFFFFFF);
  static const Color textSecondary = Color(0xFF94A3B8);
}

// ==============================================================================
// 1. BOUND DASHBOARD SCREEN (Bilingual Core Engines & 11 Curriculum Chapters)
// ==============================================================================
class BoundDashboardScreen extends ConsumerStatefulWidget {
  final BioAppController? controller;
  const BoundDashboardScreen({Key? key, this.controller}) : super(key: key);

  @override
  ConsumerState<BoundDashboardScreen> createState() => _BoundDashboardScreenState();
}

class _BoundDashboardScreenState extends ConsumerState<BoundDashboardScreen> {
  bool isArabic = true; // تبديل اللغات الديناميكي (RTL / LTR)
  late final BioAppController _controller;

  static const List<Map<String, String>> _chapterMeta = [
    {
      'titleAr': 'الدعامة والحركة في الكائنات الحية',
      'titleEn': 'Support and Movement in Living Organisms',
      'unitAr': 'الوحدة الأولى: التركيب والوظيفة',
      'unitEn': 'Unit 1: Structure & Function',
    },
    {
      'titleAr': 'التنسيق الهرموني في الكائنات الحية',
      'titleEn': 'Hormonal Coordination & Endocrine System',
      'unitAr': 'الوحدة الأولى: التركيب والوظيفة',
      'unitEn': 'Unit 1: Structure & Function',
    },
    {
      'titleAr': 'التكاثر في الكائنات الحية',
      'titleEn': 'Reproduction in Living Organisms',
      'unitAr': 'الوحدة الأولى: التركيب والوظيفة',
      'unitEn': 'Unit 1: Structure & Function',
    },
    {
      'titleAr': 'المناعة في الكائنات الحية',
      'titleEn': 'Immunity in Living Organisms',
      'unitAr': 'الوحدة الأولى: التركيب والوظيفة',
      'unitEn': 'Unit 1: Structure & Function',
    },
    {
      'titleAr': 'DNA والمحمل الوراثي وتضاعفه',
      'titleEn': 'DNA Structure, Code and Replication',
      'unitAr': 'الوحدة الثانية: البيولوجيا الجزيئية',
      'unitEn': 'Unit 2: Molecular Biology',
    },
    {
      'titleAr': 'الأحماض النووية RNA وبناء البروتين',
      'titleEn': 'RNA, Protein Synthesis and Genetic Code',
      'unitAr': 'الوحدة الثانية: البيولوجيا الجزيئية',
      'unitEn': 'Unit 2: Molecular Biology',
    },
    {
      'titleAr': 'الهندسة الوراثية وتكنولوجيا الجينات',
      'titleEn': 'Biotechnology & Genetic Engineering',
      'unitAr': 'الوحدة الثانية: البيولوجيا الجزيئية',
      'unitEn': 'Unit 2: Molecular Biology',
    },
    {
      'titleAr': 'التغذية والهضم والتمثيل الغذائي',
      'titleEn': 'Nutrition and Digestion in Living Organisms',
      'unitAr': 'الوحدة الأولى: التركيب والوظيفة',
      'unitEn': 'Unit 1: Structure & Function',
    },
    {
      'titleAr': 'النقل والتنفس الخلوي وإنتاج الطاقة',
      'titleEn': 'Transport, Respiration and Energy Production',
      'unitAr': 'الوحدة الأولى: التركيب والوظيفة',
      'unitEn': 'Unit 1: Structure & Function',
    },
    {
      'titleAr': 'الإخراج والتنظيم الأسموزي والحراري',
      'titleEn': 'Excretion and Osmoregulation',
      'unitAr': 'الوحدة الأولى: التركيب والوظيفة',
      'unitEn': 'Unit 1: Structure & Function',
    },
    {
      'titleAr': 'المراجعات الشاملة والمحاكي الوزاري الفائق',
      'titleEn': 'Comprehensive Ministry Exam Simulator',
      'unitAr': 'الوحدة الثانية: المراجعات الشاملة',
      'unitEn': 'Unit 2: Comprehensive Reviews',
    },
  ];

  @override
  void initState() {
    super.initState();
    _controller = widget.controller ?? BioAppController();
  }

  void _openQuizModeSelector() {
    showModalBottomSheet(
      context: context,
      backgroundColor: BioTheme.surfaceGlass,
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
                const Icon(Icons.quiz_rounded, color: BioTheme.warningAmber, size: 24),
                const SizedBox(width: 10),
                Text(
                  isArabic ? 'اختر نمط جلسة الاختبار الذكي' : 'Select Smart Quiz Mode',
                  style: const TextStyle(color: BioTheme.textPrimary, fontSize: 16, fontWeight: FontWeight.bold),
                ),
              ],
            ),
            const SizedBox(height: 18),
            Container(
              decoration: BoxDecoration(
                color: const Color(0xFF161622),
                borderRadius: BorderRadius.circular(14),
                border: Border.all(color: BioTheme.warningAmber.withOpacity(0.3)),
              ),
              child: ListTile(
                leading: CircleAvatar(
                  backgroundColor: BioTheme.warningAmber.withOpacity(0.18),
                  child: const Icon(Icons.assignment_turned_in, color: BioTheme.warningAmber, size: 20),
                ),
                title: Text(
                  isArabic ? 'المحاكي الوزاري الشامل (20 سؤالاً • 30 دقيقة)' : 'Full Ministry Exam Simulator (20 Qs • 30m)',
                  style: const TextStyle(color: BioTheme.textPrimary, fontWeight: FontWeight.bold, fontSize: 13),
                ),
                subtitle: Text(
                  isArabic ? 'أسئلة معتمدة لكافة الفصول الـ 11 مع التركيز على الفخاخ' : 'Authentic questions across 11 chapters with traps priority',
                  style: const TextStyle(color: BioTheme.textSecondary, fontSize: 11),
                ),
                trailing: const Icon(Icons.arrow_forward_ios_rounded, size: 14, color: BioTheme.warningAmber),
                onTap: () async {
                  Navigator.pop(ctx);
                  final quizCtrl = QuizEngineProviders.createController();
                  await quizCtrl.startComprehensiveMinistryExam(count: 20, durationMinutes: 30);
                  if (context.mounted) {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => SmartQuizScreen(controller: quizCtrl)),
                    );
                  }
                },
              ),
            ),
            const SizedBox(height: 12),
            Container(
              decoration: BoxDecoration(
                color: const Color(0xFF161622),
                borderRadius: BorderRadius.circular(14),
                border: Border.all(color: BioTheme.bioGreen.withOpacity(0.3)),
              ),
              child: ListTile(
                leading: CircleAvatar(
                  backgroundColor: BioTheme.bioGreen.withOpacity(0.18),
                  child: const Icon(Icons.history_edu_rounded, color: BioTheme.bioGreen, size: 20),
                ),
                title: Text(
                  isArabic ? 'جلسة استدراك الأخطاء السابقة (FSRS Review)' : 'FSRS Past Mistakes Remediation Session',
                  style: const TextStyle(color: BioTheme.textPrimary, fontWeight: FontWeight.bold, fontSize: 13),
                ),
                subtitle: Text(
                  isArabic ? 'إعادة اختبار المفاهيم التي لم تُتقنها بعد لتثبيت الذاكرة' : 'Adaptive re-testing for failed questions to ensure retention',
                  style: const TextStyle(color: BioTheme.textSecondary, fontSize: 11),
                ),
                trailing: const Icon(Icons.arrow_forward_ios_rounded, size: 14, color: BioTheme.bioGreen),
                onTap: () async {
                  Navigator.pop(ctx);
                  final quizCtrl = QuizEngineProviders.createController();
                  await quizCtrl.startMistakesRevisionSession();
                  if (context.mounted) {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => SmartQuizScreen(controller: quizCtrl)),
                    );
                  }
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: isArabic ? TextDirection.rtl : TextDirection.ltr,
      child: Scaffold(
        backgroundColor: BioTheme.background,
        appBar: AppBar(
          backgroundColor: BioTheme.surfaceGlass,
          elevation: 0,
          title: Row(
            children: [
              Container(
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(
                  color: BioTheme.bioGreen.withOpacity(0.15),
                  shape: BoxShape.circle,
                ),
                child: const Icon(Icons.biotech, color: BioTheme.bioGreen, size: 22),
              ),
              const SizedBox(width: 12),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    isArabic ? 'بيولوجي الثانوية العامة' : 'BioThanaweya OS',
                    style: const TextStyle(color: BioTheme.textPrimary, fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  Text(
                    isArabic ? 'نظام التعلم الذكي (Offline-First)' : 'Smart Autonomous Learning',
                    style: const TextStyle(color: BioTheme.textSecondary, fontSize: 11),
                  ),
                ],
              ),
            ],
          ),
          actions: [
            // زر البحث الشامل
            IconButton(
              tooltip: isArabic ? 'البحث الشامل بالمنهج' : 'Curriculum Search',
              icon: const Icon(Icons.search, color: BioTheme.bioGreen),
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => BoundSearchScreen(controller: _controller)),
              ),
            ),
            // زر مركز المعمارية والأنظمة (Architecture Hub)
            IconButton(
              tooltip: isArabic ? 'مركز المعمارية والأنظمة' : 'Architecture Hub',
              icon: const Icon(Icons.account_tree_rounded, color: BioTheme.bioGreen),
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const AppArchitectureHub()),
              ),
            ),
            // زر تبديل اللغة الثنائي (AR / EN)
            TextButton.icon(
              onPressed: () => setState(() => isArabic = !isArabic),
              icon: const Icon(Icons.language, color: BioTheme.bioGreen, size: 18),
              label: Text(
                isArabic ? 'EN' : 'عربي',
                style: const TextStyle(color: BioTheme.bioGreen, fontWeight: FontWeight.bold),
              ),
            ),
            const SizedBox(width: 8),
          ],
        ),
        body: SingleChildScrollView(
          physics: const BouncingScrollPhysics(),
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // 1. بطاقة الإنجاز الكلي المضيئة (Bento Grid Header)
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: BioTheme.surfaceGlass,
                  borderRadius: BorderRadius.circular(20),
                  border: Border.all(color: BioTheme.bioGreen.withOpacity(0.3)),
                  boxShadow: [
                    BoxShadow(
                      color: BioTheme.bioGreen.withOpacity(0.05),
                      blurRadius: 20,
                      spreadRadius: 2,
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
                          isArabic ? 'معدل الإنجاز واستقرار الذاكرة' : 'Memory Stability & Progress',
                          style: const TextStyle(color: BioTheme.textSecondary, fontSize: 13),
                        ),
                        const Icon(Icons.insights, color: BioTheme.bioGreen, size: 20),
                      ],
                    ),
                    const SizedBox(height: 12),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          '84%',
                          style: TextStyle(color: BioTheme.textPrimary, fontSize: 36, fontWeight: FontWeight.bold),
                        ),
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                          decoration: BoxDecoration(
                            color: BioTheme.bioGreen.withOpacity(0.2),
                            borderRadius: BorderRadius.circular(20),
                          ),
                          child: Text(
                            isArabic ? 'جاهز للوزارية 🌟' : 'Exam Ready 🌟',
                            style: const TextStyle(color: BioTheme.bioGreen, fontSize: 12, fontWeight: FontWeight.bold),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 14),
                    ClipRRect(
                      borderRadius: BorderRadius.circular(10),
                      child: LinearProgressIndicator(
                        value: 0.84,
                        backgroundColor: Colors.black26,
                        color: BioTheme.bioGreen,
                        minHeight: 8,
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 24),

              // 2. عنوان قسم المحركات الأساسية
              Text(
                isArabic ? 'المنظومة التعليمية المتكاملة (Core Engines)' : 'Integrated Core Engines',
                style: const TextStyle(color: BioTheme.textPrimary, fontSize: 16, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 14),

              // 3. شبكة المحركات الثلاثة الكبرى (Bento Grid Cards)
              
              // أ. محرك الامتحانات الذكي & FSRS
              _buildEngineCard(
                title: isArabic ? 'محرك الامتحانات وفخاخ الوزارة (FSRS)' : 'Smart Quiz & Ministry Traps (FSRS)',
                subtitle: isArabic ? 'جلسات تفاعلية، توقيت حي، واستدراك ذكي للأخطاء.' : 'Interactive sessions, live timers, & adaptive error review.',
                icon: Icons.quiz_rounded,
                accentColor: BioTheme.warningAmber,
                onTap: _openQuizModeSelector,
              ),
              const SizedBox(height: 14),

              // ب. مختبر الرسومات الحيوية والتكبير التفاعلي
              _buildEngineCard(
                title: isArabic ? 'مختبر الرسومات الحيوية والمسارات (Lab)' : 'Biology Diagram & Formula Lab',
                subtitle: isArabic ? 'دورة كريبس، شوكة التضاعف DNA، ونقاط تفاعلية.' : 'Krebs cycle, DNA replication fork, & interactive hotspots.',
                icon: Icons.science_rounded,
                accentColor: BioTheme.bioGreen,
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const BiologyLabScreen()),
                  );
                },
              ),
              const SizedBox(height: 14),

              // ج. خطط المذاكرة التكيفية والمساعد الذكي
              _buildEngineCard(
                title: isArabic ? 'خطة المذاكرة التكيفية والمساعد الذكي' : 'Smart Study Planner & AI Coach',
                subtitle: isArabic ? 'جدول يومي آلي، إنجاز المهام، وتشخيص FSRS.' : 'Automated daily schedule, tasks tracking, & FSRS diagnostics.',
                icon: Icons.psychology_rounded,
                accentColor: BioTheme.molecularViolet,
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const SmartPlannerScreen()),
                  );
                },
              ),
              const SizedBox(height: 14),

              // د. مركز المعمارية والأنظمة والمحاكي الهندسي
              _buildEngineCard(
                title: isArabic ? 'مركز المعمارية والأنظمة (Clean Architecture)' : 'Architecture Hub & Systems Telemetry',
                subtitle: isArabic ? 'استكشاف SQLite، فحص الأداء الحي، وأتمتة GitHub CI/CD.' : 'SQLite DDL explorer, live latency benchmarks, & CI/CD pipeline.',
                icon: Icons.account_tree_rounded,
                accentColor: const Color(0xFF64FFDA),
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const AppArchitectureHub()),
                  );
                },
              ),

              const SizedBox(height: 28),

              // 4. قسم فصول المنهج الـ 11
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    isArabic ? 'فصول منهج الأحياء الـ 11 (الرسمي):' : 'Official 11 Curriculum Chapters:',
                    style: const TextStyle(color: BioTheme.textPrimary, fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  Text(
                    isArabic ? '100% متاح دون إنترنت' : '100% Offline Active',
                    style: const TextStyle(color: BioTheme.bioGreen, fontSize: 11, fontWeight: FontWeight.bold),
                  ),
                ],
              ),
              const SizedBox(height: 14),

              // قائمة الفصول الدراسية
              ...List.generate(_chapterMeta.length, (index) {
                final int chapterId = index + 1;
                final meta = _chapterMeta[index];
                final title = isArabic ? meta['titleAr']! : meta['titleEn']!;
                final unit = isArabic ? meta['unitAr']! : meta['unitEn']!;

                return Container(
                  margin: const EdgeInsets.only(bottom: 12),
                  decoration: BoxDecoration(
                    color: BioTheme.surfaceGlass,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: const Color(0xFF233554)),
                  ),
                  child: ListTile(
                    contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
                    leading: CircleAvatar(
                      backgroundColor: const Color(0xFF172A45),
                      child: Text(
                        '$chapterId',
                        style: const TextStyle(
                          color: BioTheme.bioGreen,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    title: Text(
                      '${isArabic ? 'الفصل' : 'Chapter'} $chapterId: $title',
                      style: const TextStyle(
                        color: BioTheme.textPrimary,
                        fontWeight: FontWeight.bold,
                        fontSize: 14,
                      ),
                    ),
                    subtitle: Padding(
                      padding: const EdgeInsets.only(top: 4.0),
                      child: Text(
                        unit,
                        style: const TextStyle(color: BioTheme.textSecondary, fontSize: 12),
                      ),
                    ),
                    trailing: const Icon(Icons.play_circle_outline_rounded, size: 22, color: BioTheme.bioGreen),
                    onTap: () async {
                      final quizCtrl = QuizEngineProviders.createController();
                      await quizCtrl.startChapterQuiz(chapterId, count: 10, durationMinutes: 15);
                      if (context.mounted) {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (_) => SmartQuizScreen(controller: quizCtrl),
                          ),
                        );
                      }
                    },
                  ),
                );
              }),
              const SizedBox(height: 24),
            ],
          ),
        ),
      ),
    );
  }

  // ودجت موحد لبناء بطاقات المحركات بتصميم عائم (Glassmorphic Bento Card)
  Widget _buildEngineCard({
    required String title,
    required String subtitle,
    required IconData icon,
    required Color accentColor,
    required VoidCallback onTap,
  }) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(16),
        child: Container(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: BioTheme.surfaceGlass,
            borderRadius: BorderRadius.circular(16),
            border: Border.all(color: accentColor.withOpacity(0.3)),
          ),
          child: Row(
            children: [
              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: accentColor.withOpacity(0.15),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Icon(icon, color: accentColor, size: 26),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      title,
                      style: const TextStyle(color: BioTheme.textPrimary, fontSize: 14, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      subtitle,
                      style: const TextStyle(color: BioTheme.textSecondary, fontSize: 12),
                    ),
                  ],
                ),
              ),
              Icon(Icons.arrow_forward_ios_rounded, color: BioTheme.textSecondary.withOpacity(0.5), size: 16),
            ],
          ),
        ),
      ),
    );
  }
}

// ==============================================================================
// 2. BOUND EXAM SCREEN (Interactive Exam Simulator)
// ==============================================================================
class BoundExamScreen extends StatefulWidget {
  final BioAppController controller;
  const BoundExamScreen({Key? key, required this.controller}) : super(key: key);

  @override
  State<BoundExamScreen> createState() => _BoundExamScreenState();
}

class _BoundExamScreenState extends State<BoundExamScreen> {
  int currentIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF0A192F),
      appBar: AppBar(
        title: Text(
          'محاكي الفصل ${widget.controller.selectedChapterId}',
          style: const TextStyle(fontSize: 15, fontWeight: FontWeight.bold),
        ),
        backgroundColor: const Color(0xFF112240),
        elevation: 0,
      ),
      body: ListenableBuilder(
        listenable: widget.controller,
        builder: (context, _) {
          if (widget.controller.isLoading) {
            return const Center(child: CircularProgressIndicator(color: Color(0xFF64FFDA)));
          }

          final List<QuestionModel> questions = widget.controller.activeChapterQuestions;
          if (questions.isEmpty) {
            return const Center(
              child: Text(
                'لا توجد أسئلة متاحة لهذا الفصل محلياً.',
                style: TextStyle(color: Colors.white),
              ),
            );
          }

          if (currentIndex >= questions.length) {
            currentIndex = 0;
          }

          final currentQ = questions[currentIndex];
          final selectedOption = widget.controller.userExamAnswers[currentIndex];

          return Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                ClipRRect(
                  borderRadius: BorderRadius.circular(6),
                  child: LinearProgressIndicator(
                    value: (currentIndex + 1) / questions.length,
                    backgroundColor: const Color(0xFF112240),
                    valueColor: const AlwaysStoppedAnimation<Color>(Color(0xFF64FFDA)),
                    minHeight: 6,
                  ),
                ),
                const SizedBox(height: 16),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      'السؤال ${currentIndex + 1} من ${questions.length}',
                      style: const TextStyle(color: Color(0xFF8892B0), fontSize: 13),
                    ),
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                      decoration: BoxDecoration(
                        color: const Color(0xFF172A45),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: const Text('سؤال وزاري', style: TextStyle(color: Color(0xFF64FFDA), fontSize: 10)),
                    ),
                  ],
                ),
                const SizedBox(height: 12),
                Text(
                  currentQ.questionText,
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                    height: 1.4,
                  ),
                ),
                const SizedBox(height: 20),
                Expanded(
                  child: ListView.builder(
                    itemCount: currentQ.options.length,
                    itemBuilder: (context, optIndex) {
                      final isSelected = selectedOption == optIndex;
                      return GestureDetector(
                        onTap: () => widget.controller.recordAnswer(currentIndex, optIndex),
                        child: AnimatedContainer(
                          duration: const Duration(milliseconds: 180),
                          margin: const EdgeInsets.only(bottom: 12),
                          padding: const EdgeInsets.all(16),
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
                              Container(
                                width: 20,
                                height: 20,
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
                                  currentQ.options[optIndex],
                                  style: TextStyle(
                                    color: isSelected ? Colors.white : const Color(0xFFCCD6F6),
                                    fontSize: 14,
                                    fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
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
                      onPressed: currentIndex > 0 ? () => setState(() => currentIndex--) : null,
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
                        if (currentIndex < questions.length - 1) {
                          setState(() => currentIndex++);
                        } else {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(
                              content: Text('تم حفظ الإجابات وتخزينها محلياً بنجاح تام!'),
                              backgroundColor: Color(0xFF112240),
                            ),
                          );
                        }
                      },
                      icon: Icon(
                        currentIndex < questions.length - 1 ? Icons.arrow_back_ios : Icons.check_circle_outline,
                        size: 14,
                      ),
                      label: Text(
                        currentIndex < questions.length - 1 ? 'التالي' : 'إنهاء التسليم',
                        style: const TextStyle(fontWeight: FontWeight.bold),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}

// ==============================================================================
// 3. BOUND SEARCH SCREEN (Instant Full-Text Offline Search)
// ==============================================================================
class BoundSearchScreen extends StatelessWidget {
  final BioAppController controller;
  const BoundSearchScreen({Key? key, required this.controller}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF0A192F),
      appBar: AppBar(
        title: const Text('بحث المنهج الفوري (Offline FTS)', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
        backgroundColor: const Color(0xFF112240),
        elevation: 0,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              onChanged: (val) => controller.searchCurriculumDatabase(val),
              style: const TextStyle(color: Colors.white),
              decoration: InputDecoration(
                hintText: 'ابحث عن أي مصطلح، هرمون، أو سؤال...',
                hintStyle: const TextStyle(color: Color(0xFF8892B0), fontSize: 13),
                prefixIcon: const Icon(Icons.search, color: Color(0xFF64FFDA)),
                filled: true,
                fillColor: const Color(0xFF112240),
                contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(14),
                  borderSide: const BorderSide(color: Color(0xFF233554)),
                ),
                enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(14),
                  borderSide: const BorderSide(color: Color(0xFF233554)),
                ),
                focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(14),
                  borderSide: const BorderSide(color: Color(0xFF64FFDA)),
                ),
              ),
            ),
            const SizedBox(height: 16),
            Expanded(
              child: ListenableBuilder(
                listenable: controller,
                builder: (context, _) {
                  if (controller.isLoading) {
                    return const Center(child: CircularProgressIndicator(color: Color(0xFF64FFDA)));
                  }
                  final results = controller.searchResults;
                  if (results.isEmpty) {
                    return const Center(
                      child: Text(
                        'لا توجد نتائج مطابقة للبحث المحلي.',
                        style: TextStyle(color: Color(0xFF8892B0), fontSize: 13),
                      ),
                    );
                  }
                  return ListView.builder(
                    itemCount: results.length,
                    itemBuilder: (context, index) {
                      final q = results[index];
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
                                Text(
                                  'الفصل: ${q.chapterId}',
                                  style: const TextStyle(color: Color(0xFF64FFDA), fontSize: 11, fontWeight: FontWeight.bold),
                                ),
                                const Icon(Icons.quiz_outlined, size: 14, color: Color(0xFF8892B0)),
                              ],
                            ),
                            const SizedBox(height: 6),
                            Text(
                              q.questionText,
                              style: const TextStyle(
                                color: Colors.white,
                                fontWeight: FontWeight.bold,
                                fontSize: 13,
                                height: 1.4,
                              ),
                            ),
                            if (q.explanation.isNotEmpty) ...[
                              const SizedBox(height: 6),
                              Text(
                                q.explanation,
                                style: const TextStyle(color: Color(0xFF8892B0), fontSize: 11, height: 1.3),
                              ),
                            ],
                          ],
                        ),
                      );
                    },
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
