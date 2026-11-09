import 'package:flutter/material.dart';

/// ModernBioDashboardScreen:
/// High-end dark futuristic Bento-grid dashboard for Egyptian Thanawya Amma Biology.
/// Supports 100% offline state, Arabic RTL layout, and responsive card sizing.
class ModernBioDashboardScreen extends StatelessWidget {
  const ModernBioDashboardScreen({Key? key}) : super(key: key);

  final List<Map<String, dynamic>> chapters = const [
    {'id': 1, 'title': 'الفصل الأول', 'name': 'الدعامة والحركة في الكائنات الحية', 'progress': 1.0, 'questions': 12},
    {'id': 2, 'title': 'الفصل الثاني', 'name': 'التنسيق الهرموني في الكائنات الحية', 'progress': 0.85, 'questions': 18},
    {'id': 3, 'title': 'الفصل الثالث', 'name': 'التكاثر في الكائنات الحية', 'progress': 0.70, 'questions': 15},
    {'id': 4, 'title': 'الفصل الرابع', 'name': 'المناعة في الكائنات الحية', 'progress': 0.40, 'questions': 20},
    {'id': 5, 'title': 'الفصل الخامس', 'name': 'DNA والمحمل الوراثي وتضاعفه', 'progress': 0.20, 'questions': 25},
    {'id': 6, 'title': 'الفصل السادس', 'name': 'الأحماض النووية RNA وبناء البروتين', 'progress': 0.0, 'questions': 22},
  ];

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        backgroundColor: const Color(0xFF0A192F), // Dark futuristic background
        body: SafeArea(
          child: CustomScrollView(
            slivers: [
              // Futuristic Sliver App Bar
              SliverAppBar(
                floating: true,
                backgroundColor: const Color(0xFF0A192F),
                elevation: 0,
                title: Row(
                  children: [
                    Container(
                      padding: const EdgeInsets.all(8),
                      decoration: BoxDecoration(
                        color: const Color(0xFF172A45),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: const Icon(Icons.biotech, color: Color(0xFF64FFDA), size: 22),
                    ),
                    const SizedBox(width: 12),
                    const Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('منظومة الأحياء الذكية', style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold)),
                        Text('الثانوية العامة المصرية • 100% Offline', style: TextStyle(color: Color(0xFF8892B0), fontSize: 11)),
                      ],
                    ),
                  ],
                ),
                actions: [
                  IconButton(
                    icon: const Icon(Icons.notifications_active_outlined, color: Color(0xFF64FFDA)),
                    onPressed: () {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(
                          content: Text('المنظومة محدثة ومتصلة محلياً 100% بدون انترنت.'),
                          backgroundColor: Color(0xFF112240),
                        ),
                      );
                    },
                  ),
                ],
              ),
              
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Bento Hero Card
                      Container(
                        width: double.infinity,
                        padding: const EdgeInsets.all(20),
                        decoration: BoxDecoration(
                          gradient: const LinearGradient(
                            colors: [Color(0xFF112240), Color(0xFF1D3557)],
                            begin: Alignment.topLeft,
                            end: Alignment.bottomRight,
                          ),
                          borderRadius: BorderRadius.circular(24),
                          border: Border.all(color: const Color(0xFF64FFDA).withOpacity(0.3)),
                          boxShadow: [
                            BoxShadow(
                              color: const Color(0xFF64FFDA).withOpacity(0.1),
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
                                const Text('معدل الإتقان الشامل', style: TextStyle(color: Color(0xFF8892B0), fontSize: 13)),
                                Container(
                                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                                  decoration: BoxDecoration(
                                    color: const Color(0xFF64FFDA).withOpacity(0.1),
                                    borderRadius: BorderRadius.circular(20),
                                  ),
                                  child: const Text('نشط الآن (أوفلاين)', style: TextStyle(color: Color(0xFF64FFDA), fontSize: 10)),
                                ),
                              ],
                            ),
                            const SizedBox(height: 12),
                            const Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text('52%', style: TextStyle(color: Colors.white, fontSize: 32, fontWeight: FontWeight.bold)),
                                Icon(Icons.trending_up, color: Color(0xFF64FFDA), size: 36),
                              ],
                            ),
                            const SizedBox(height: 12),
                            LinearProgressIndicator(
                              value: 0.52,
                              backgroundColor: const Color(0xFF0A192F),
                              color: const Color(0xFF64FFDA),
                              minHeight: 8,
                            ),
                          ],
                        ),
                      ),
                      
                      const SizedBox(height: 24),
                      const Text('فصول المنهج التفاعلية', style: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold)),
                      const SizedBox(height: 12),

                      // Bento Grid
                      GridView.builder(
                        shrinkWrap: true,
                        physics: const NeverScrollableScrollPhysics(),
                        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                          crossAxisCount: 2,
                          crossAxisSpacing: 12,
                          mainAxisSpacing: 12,
                          childAspectRatio: 0.85,
                        ),
                        itemCount: chapters.length,
                        itemBuilder: (context, index) {
                          final chapter = chapters[index];
                          final double progress = (chapter['progress'] as num).toDouble();
                          return Container(
                            padding: const EdgeInsets.all(16),
                            decoration: BoxDecoration(
                              color: const Color(0xFF112240),
                              borderRadius: BorderRadius.circular(20),
                              border: Border.all(color: Colors.white.withOpacity(0.05)),
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    Text(chapter['title'] as String, style: const TextStyle(color: Color(0xFF64FFDA), fontSize: 12, fontWeight: FontWeight.bold)),
                                    SizedBox(
                                      width: 28,
                                      height: 28,
                                      child: CircularProgressIndicator(
                                        value: progress,
                                        backgroundColor: const Color(0xFF0A192F),
                                        color: const Color(0xFF64FFDA),
                                        strokeWidth: 3,
                                      ),
                                    ),
                                  ],
                                ),
                                Text(
                                  chapter['name'] as String,
                                  style: const TextStyle(color: Colors.white, fontSize: 14, fontWeight: FontWeight.bold),
                                  maxLines: 2,
                                  overflow: TextOverflow.ellipsis,
                                ),
                                Row(
                                  children: [
                                    const Icon(Icons.quiz_outlined, color: Color(0xFF8892B0), size: 14),
                                    const SizedBox(width: 4),
                                    Text('${chapter['questions']} سؤال متاح', style: const TextStyle(color: Color(0xFF8892B0), fontSize: 11)),
                                  ],
                                ),
                              ],
                            ),
                          );
                        },
                      ),
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
