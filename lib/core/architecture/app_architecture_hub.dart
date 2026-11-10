import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../database/local_database_service.dart';

/// Interactive Architectural Blueprint & Mobile Diagnostics Screen.
/// Highlights the 4 pillars of the app: Flutter Architecture, Offline SQLite Database, UI/UX Bento System, and GitHub Actions CI/CD.
class AppArchitectureHubScreen extends StatefulWidget {
  const AppArchitectureHubScreen({super.key});

  @override
  State<AppArchitectureHubScreen> createState() => _AppArchitectureHubScreenState();
}

class _AppArchitectureHubScreenState extends State<AppArchitectureHubScreen> {
  final LocalDatabaseService _db = LocalDatabaseService.instance;
  bool _isLoading = true;
  int _chaptersCount = 0;
  int _textbooksCount = 0;
  int _attemptsCount = 0;
  String _lastDbMessage = 'Database initializing...';

  @override
  void initState() {
    super.initState();
    _refreshDbMetrics();
  }

  Future<void> _refreshDbMetrics() async {
    setState(() => _isLoading = true);
    try {
      await _db.initializeDatabase();
      final chapters = await _db.queryChapters();
      final textbooks = await _db.queryTextbooks();
      final attempts = await _db.queryExamAttempts();

      setState(() {
        _chaptersCount = chapters.length;
        _textbooksCount = textbooks.length;
        _attemptsCount = attempts.length;
        _lastDbMessage = 'Local database online • 0ms network latency';
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _lastDbMessage = 'Database error: $e';
        _isLoading = false;
      });
    }
  }

  Future<void> _runSimulatedQuery() async {
    HapticFeedback.mediumImpact();
    setState(() => _isLoading = true);
    await Future.delayed(const Duration(milliseconds: 120));
    final chapters = await _db.queryChapters(unitCode: 'UNIT_1');
    setState(() {
      _lastDbMessage = 'Query Executed: SELECT * FROM chapters WHERE unitCode="UNIT_1" -> Found ${chapters.length} rows';
      _isLoading = false;
    });
    if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(_lastDbMessage),
          backgroundColor: const Color(0xFF0F172A),
          behavior: SnackBarBehavior.floating,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final isDark = Theme.of(context).brightness == Brightness.dark;

    return Scaffold(
      backgroundColor: isDark ? const Color(0xFF0B132B) : const Color(0xFFF1F5F9),
      appBar: AppBar(
        title: const Row(
          children: [
            Icon(Icons.layers_rounded, color: Color(0xFF14B8A6)),
            SizedBox(width: 8),
            Text(
              'معمارية المنظومة | Architecture Blueprint',
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
            ),
          ],
        ),
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh_rounded),
            tooltip: 'تحديث مؤشرات المعمارية',
            onPressed: _refreshDbMetrics,
          ),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // 1. Architecture Hero Banner
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(20),
                    decoration: BoxDecoration(
                      gradient: const LinearGradient(
                        colors: [Color(0xFF071329), Color(0xFF0E2045), Color(0xFF123060)],
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                      ),
                      borderRadius: BorderRadius.circular(24),
                      border: Border.all(color: const Color(0xFF14B8A6).withValues(alpha: 0.3)),
                      boxShadow: [
                        BoxShadow(
                          color: const Color(0xFF14B8A6).withValues(alpha: 0.1),
                          blurRadius: 16,
                          offset: const Offset(0, 6),
                        ),
                      ],
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          children: [
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                              decoration: BoxDecoration(
                                color: const Color(0xFF14B8A6).withValues(alpha: 0.2),
                                borderRadius: BorderRadius.circular(10),
                                border: Border.all(color: const Color(0xFF14B8A6).withValues(alpha: 0.4)),
                              ),
                              child: const Text(
                                'CLEAN ARCHITECTURE BLUEPRINT',
                                style: TextStyle(
                                  color: Color(0xFF5EEAD4),
                                  fontWeight: FontWeight.bold,
                                  fontSize: 11,
                                  letterSpacing: 1.0,
                                ),
                              ),
                            ),
                            const Spacer(),
                            const Icon(Icons.verified_rounded, color: Color(0xFF10B981), size: 18),
                            const SizedBox(width: 4),
                            const Text(
                              'Production Ready',
                              style: TextStyle(color: Color(0xFF10B981), fontSize: 11, fontWeight: FontWeight.bold),
                            ),
                          ],
                        ),
                        const SizedBox(height: 14),
                        const Text(
                          'معمارية تطبيق "تعلم مع علي" للثانوية العامة',
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 18,
                            fontWeight: FontWeight.w900,
                          ),
                        ),
                        const SizedBox(height: 6),
                        const Text(
                          'تصميم هندسي متكامل يجمع بين قوة محرك Flutter، والتخزين المحلي دون اتصال (Offline-First Local Database)، ودعم الأجهزة المتعددة، ومسارات البناء المؤتمتة عبر GitHub Actions.',
                          style: TextStyle(color: Colors.white70, fontSize: 13, height: 1.5),
                        ),
                      ],
                    ),
                  ),

                  const SizedBox(height: 18),

                  // 2. Bento Grid of 4 Architectural Pillars
                  LayoutBuilder(
                    builder: (context, constraints) {
                      final isWide = constraints.maxWidth >= 700;
                      return GridView.count(
                        shrinkWrap: true,
                        physics: const NeverScrollableScrollPhysics(),
                        crossAxisCount: isWide ? 2 : 1,
                        crossAxisSpacing: 14,
                        mainAxisSpacing: 14,
                        childAspectRatio: isWide ? 1.6 : 1.4,
                        children: [
                          _buildPillarCard(
                            titleAr: '1. النواة وواجهة المستخدم (UI/UX)',
                            titleEn: 'Flutter Engine & Bento System',
                            accentColor: const Color(0xFF06B6D4),
                            icon: Icons.palette_rounded,
                            isDark: isDark,
                            bullets: [
                              'تصميم Bento Grid عالي التجاوب مع شاشات الهواتف.',
                              'دعم اتجاهين كامل (RTL عربي / LTR إنجليزي).',
                              'معدل إطارات 60-120fps وسلاسة في الرسوم الحركية.',
                              'تنسيق الألوان للمذاكرة الطويلة مع الوضع المظلم.',
                            ],
                          ),
                          _buildPillarCard(
                            titleAr: '2. التخزين المحلي وقواعد البيانات',
                            titleEn: 'Offline-First Local DB Engine',
                            accentColor: const Color(0xFF10B981),
                            icon: Icons.storage_rounded,
                            isDark: isDark,
                            bullets: [
                              'محرك SQL-Compatible صافٍ بلغة Dart بدون تعقيدات C.',
                              '$_chaptersCount فصول منهجية مفهرسة محلياً.',
                              '$_textbooksCount كتب رسمية ومذكرات مفاهيم محفوظة.',
                              'استجابة فورية 0ms وزمن تحميل فائق السرعة.',
                            ],
                            actionButton: ElevatedButton.icon(
                              icon: const Icon(Icons.flash_on_rounded, size: 14),
                              label: const Text('تشغيل استعلام محلي تجريبي'),
                              style: ElevatedButton.styleFrom(
                                backgroundColor: const Color(0xFF10B981),
                                foregroundColor: Colors.black87,
                                textStyle: const TextStyle(fontWeight: FontWeight.bold, fontSize: 11),
                                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                              ),
                              onPressed: _runSimulatedQuery,
                            ),
                          ),
                          _buildPillarCard(
                            titleAr: '3. إدارة الحالة والمعمارية النظيفة',
                            titleEn: 'Clean Architecture & State',
                            accentColor: const Color(0xFF8B5CF6),
                            icon: Icons.account_tree_rounded,
                            isDark: isDark,
                            bullets: [
                              'فصل صارم بين Presentation و Domain و Data.',
                              'عزل تام للمنطق البرمجي عن واجهات العرض.',
                              'دعم كامل لـ Riverpod و ChangeNotifier.',
                              'اختبارات تلقائية معتمدة بدون أي اعتماد خارجي.',
                            ],
                          ),
                          _buildPillarCard(
                            titleAr: '4. الأتمتة والنشر السحابي (CI/CD)',
                            titleEn: 'GitHub Actions & APK Pipelines',
                            accentColor: const Color(0xFFF59E0B),
                            icon: Icons.rocket_launch_rounded,
                            isDark: isDark,
                            bullets: [
                              'بناء آلي لحزم APK مقسمة للمعالجات (arm64/armeabi).',
                              'توليد حزمة متجر Google Play Bundle (AAB).',
                              'احتساب بصمات التشفير التكاملي SHA-256 تلقائياً.',
                              'أرشفة الملفات لمدة 30 يوماً ونشر الإصدارات المؤتمتة.',
                            ],
                          ),
                        ],
                      );
                    },
                  ),

                  const SizedBox(height: 18),

                  // 3. Database Diagnostics Footer
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(14),
                    decoration: BoxDecoration(
                      color: isDark ? const Color(0xFF1C2541) : Colors.white,
                      borderRadius: BorderRadius.circular(16),
                      border: Border.all(color: isDark ? Colors.white10 : Colors.black12),
                    ),
                    child: Row(
                      children: [
                        const Icon(Icons.dns_rounded, color: Color(0xFF14B8A6), size: 20),
                        const SizedBox(width: 10),
                        Expanded(
                          child: Text(
                            _lastDbMessage,
                            style: TextStyle(
                              fontSize: 12,
                              fontWeight: FontWeight.bold,
                              color: isDark ? Colors.white70 : Colors.black87,
                            ),
                            overflow: TextOverflow.ellipsis,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
    );
  }

  Widget _buildPillarCard({
    required String titleAr,
    required String titleEn,
    required Color accentColor,
    required IconData icon,
    required bool isDark,
    required List<String> bullets,
    Widget? actionButton,
  }) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: isDark ? const Color(0xFF1C2541) : Colors.white,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: accentColor.withValues(alpha: 0.3)),
        boxShadow: [
          BoxShadow(
            color: accentColor.withValues(alpha: 0.05),
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
              Container(
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(
                  color: accentColor.withValues(alpha: 0.15),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Icon(icon, color: accentColor, size: 20),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      titleAr,
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                      overflow: TextOverflow.ellipsis,
                    ),
                    Text(
                      titleEn,
                      style: TextStyle(fontSize: 11, color: isDark ? Colors.white60 : Colors.black54),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ],
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          Expanded(
            child: ListView(
              physics: const NeverScrollableScrollPhysics(),
              children: bullets
                  .map(
                    (b) => Padding(
                      padding: const EdgeInsets.only(bottom: 6),
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Icon(Icons.check_circle_rounded, size: 14, color: accentColor),
                          const SizedBox(width: 6),
                          Expanded(
                            child: Text(
                              b,
                              style: TextStyle(
                                fontSize: 11,
                                height: 1.3,
                                color: isDark ? Colors.white70 : Colors.black87,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  )
                  .toList(),
            ),
          ),
          if (actionButton != null) ...[
            const SizedBox(height: 8),
            actionButton,
          ],
        ],
      ),
    );
  }
}
