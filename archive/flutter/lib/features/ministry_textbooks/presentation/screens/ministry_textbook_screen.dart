import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

/// كائن يمثل بيانات كتاب أو كتيب الوزارة الرسمي
class MinistryTextbookItem {
  final String id;
  final String titleAr;
  final String titleEn;
  final String edition;
  final int totalPages;
  final String fileSize;
  final bool isOfflineReady;
  final int lastReadPage;

  const MinistryTextbookItem({
    required this.id,
    required this.titleAr,
    required this.titleEn,
    required this.edition,
    required this.totalPages,
    required this.fileSize,
    this.isOfflineReady = true,
    this.lastReadPage = 1,
  });

  MinistryTextbookItem copyWith({
    String? id,
    String? titleAr,
    String? titleEn,
    String? edition,
    int? totalPages,
    String? fileSize,
    bool? isOfflineReady,
    int? lastReadPage,
  }) {
    return MinistryTextbookItem(
      id: id ?? this.id,
      titleAr: titleAr ?? this.titleAr,
      titleEn: titleEn ?? this.titleEn,
      edition: edition ?? this.edition,
      totalPages: totalPages ?? this.totalPages,
      fileSize: fileSize ?? this.fileSize,
      isOfflineReady: isOfflineReady ?? this.isOfflineReady,
      lastReadPage: lastReadPage ?? this.lastReadPage,
    );
  }

  double get readingProgressPercentage =>
      totalPages > 0 ? (lastReadPage / totalPages).clamp(0.0, 1.0) : 0.0;
}

/// موفر قائمة كتب وكتيبات الوزارة الرسمية
final ministryTextbooksProvider =
    StateNotifierProvider<MinistryTextbooksNotifier, List<MinistryTextbookItem>>((ref) {
  return MinistryTextbooksNotifier();
});

class MinistryTextbooksNotifier extends StateNotifier<List<MinistryTextbookItem>> {
  MinistryTextbooksNotifier()
      : super(const [
          MinistryTextbookItem(
            id: 'bio_ministry_ar_2025',
            titleAr: 'كتاب الأحياء للثانوية العامة (النسخة الرسمية)',
            titleEn: 'Official Biology Textbook (Arabic)',
            edition: 'وزارة التربية والتعليم - طبعة 2024 / 2025',
            totalPages: 168,
            fileSize: '42 MB',
            isOfflineReady: true,
            lastReadPage: 24,
          ),
          MinistryTextbookItem(
            id: 'bio_ministry_en_2025',
            titleAr: 'كتاب الأحياء لمدارس اللغات (English Edition)',
            titleEn: 'General Secondary Biology (Languages Edition)',
            edition: 'Ministry of Education - Verified Edition',
            totalPages: 172,
            fileSize: '46 MB',
            isOfflineReady: true,
            lastReadPage: 12,
          ),
          MinistryTextbookItem(
            id: 'bio_concepts_booklet',
            titleAr: 'كتيب مفاهيم الأحياء للثانوية العامة',
            titleEn: 'Official Biology Concepts Reference Booklet',
            edition: 'كتيب المفاهيم المعتمد للجان الامتحان',
            totalPages: 48,
            fileSize: '14 MB',
            isOfflineReady: true,
            lastReadPage: 8,
          ),
        ]);

  void updateReadingProgress(String id, int page) {
    state = [
      for (final book in state)
        if (book.id == id) book.copyWith(lastReadPage: page) else book,
    ];
  }
}

/// شاشة مكتبة كتب وكتيبات مفاهيم الوزارة الرسمية (Ministry Official Textbooks Library)
class MinistryTextbookScreen extends ConsumerWidget {
  const MinistryTextbookScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final books = ref.watch(ministryTextbooksProvider);

    const backgroundColor = Color(0xFF0F0F17);
    const surfaceColor = Color(0xFF1E1E2C);
    const cardBgColor = Color(0xFF252536);
    const primaryGreen = Color(0xFF10B981);

    return Scaffold(
      backgroundColor: backgroundColor,
      appBar: AppBar(
        title: const Text(
          'مكتبة كتب وكتيبات الوزارة الرسمية',
          style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
        ),
        backgroundColor: surfaceColor,
        elevation: 0,
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          // 1. بطاقة التعريف بمكتبة المنهج
          Container(
            padding: const EdgeInsets.all(18),
            decoration: BoxDecoration(
              color: surfaceColor,
              borderRadius: BorderRadius.circular(16),
              border: Border.all(color: primaryGreen.withValues(alpha: 0.3)),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withValues(alpha: 0.2),
                  blurRadius: 10,
                  offset: const Offset(0, 4),
                ),
              ],
            ),
            child: Row(
              children: [
                Container(
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    color: primaryGreen.withValues(alpha: 0.15),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: const Icon(Icons.menu_book_rounded, color: primaryGreen, size: 28),
                ),
                const SizedBox(width: 14),
                const Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'وثائق الوزارة المعتمدة للثانوية العامة',
                        style: TextStyle(color: Colors.white, fontSize: 14, fontWeight: FontWeight.bold),
                      ),
                      SizedBox(height: 4),
                      Text(
                        'متاحة 100% دون اتصال بالإنترنت مع حفظ تقدم القراءة التلقائي.',
                        style: TextStyle(color: Colors.grey, fontSize: 12),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),

          const SizedBox(height: 20),

          const Text(
            'الكتب والمجلدات المتاحة:',
            style: TextStyle(color: Colors.white, fontSize: 15, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),

          // 2. بطاقات الكتب المتاحة
          for (final book in books)
            Container(
              margin: const EdgeInsets.only(bottom: 14),
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: cardBgColor,
                borderRadius: BorderRadius.circular(16),
                border: Border.all(
                  color: primaryGreen.withValues(alpha: 0.4),
                ),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.2),
                    blurRadius: 8,
                    offset: const Offset(0, 3),
                  ),
                ],
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: primaryGreen.withValues(alpha: 0.15),
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: const Icon(Icons.cloud_done, color: primaryGreen, size: 24),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              book.titleAr,
                              style: const TextStyle(
                                color: Colors.white,
                                fontWeight: FontWeight.bold,
                                fontSize: 14,
                              ),
                            ),
                            const SizedBox(height: 4),
                            Text(
                              book.edition,
                              style: const TextStyle(color: Colors.white70, fontSize: 11),
                            ),
                            const SizedBox(height: 4),
                            Row(
                              children: [
                                Text(
                                  'الحجم: ${book.fileSize}',
                                  style: const TextStyle(color: Colors.grey, fontSize: 11),
                                ),
                                const SizedBox(width: 8),
                                const Text('•', style: TextStyle(color: Colors.grey, fontSize: 11)),
                                const SizedBox(width: 8),
                                Text(
                                  '${book.totalPages} صفحة',
                                  style: const TextStyle(color: Colors.grey, fontSize: 11),
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),

                  const SizedBox(height: 12),
                  Row(
                    children: [
                      Expanded(
                        child: ClipRRect(
                          borderRadius: BorderRadius.circular(4),
                          child: LinearProgressIndicator(
                            value: book.readingProgressPercentage,
                            backgroundColor: Colors.black26,
                            color: primaryGreen,
                            minHeight: 6,
                          ),
                        ),
                      ),
                      const SizedBox(width: 10),
                      Text(
                        'الصفحة ${book.lastReadPage} / ${book.totalPages}',
                        style: const TextStyle(color: Colors.greenAccent, fontSize: 11),
                      ),
                    ],
                  ),

                  const SizedBox(height: 14),

                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton.icon(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: primaryGreen,
                        foregroundColor: Colors.white,
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                        padding: const EdgeInsets.symmetric(vertical: 10),
                      ),
                      icon: const Icon(Icons.chrome_reader_mode, size: 16),
                      label: const Text(
                        'قراءة الكتاب الآن',
                        style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                      ),
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (_) => TextbookPdfReaderScreen(textbook: book),
                          ),
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
        ],
      ),
    );
  }
}

/// شاشة تصفح وقراءة الكتاب المدمجة دون اتصال
class TextbookPdfReaderScreen extends StatefulWidget {
  final MinistryTextbookItem textbook;

  const TextbookPdfReaderScreen({super.key, required this.textbook});

  @override
  State<TextbookPdfReaderScreen> createState() => _TextbookPdfReaderScreenState();
}

class _TextbookPdfReaderScreenState extends State<TextbookPdfReaderScreen> {
  late int _currentPage;

  @override
  void initState() {
    super.initState();
    _currentPage = widget.textbook.lastReadPage;
  }

  @override
  Widget build(BuildContext context) {
    const surfaceColor = Color(0xFF1E1E2C);
    const backgroundColor = Color(0xFF0F0F17);

    return Scaffold(
      backgroundColor: backgroundColor,
      appBar: AppBar(
        title: Text(
          widget.textbook.titleAr,
          style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold),
        ),
        backgroundColor: surfaceColor,
        elevation: 0,
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Container(
                padding: const EdgeInsets.all(24),
                decoration: BoxDecoration(
                  color: surfaceColor,
                  borderRadius: BorderRadius.circular(20),
                  border: Border.all(color: Colors.white12),
                ),
                child: Column(
                  children: [
                    const Icon(Icons.menu_book, color: Color(0xFF10B981), size: 64),
                    const SizedBox(height: 16),
                    Text(
                      widget.textbook.titleAr,
                      textAlign: TextAlign.center,
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'الصفحة $_currentPage من أصل ${widget.textbook.totalPages}',
                      style: const TextStyle(color: Colors.greenAccent, fontSize: 13),
                    ),
                    const SizedBox(height: 16),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        IconButton(
                          icon: const Icon(Icons.arrow_back, color: Colors.white),
                          onPressed: _currentPage > 1
                              ? () => setState(() => _currentPage--)
                              : null,
                        ),
                        const SizedBox(width: 20),
                        Text(
                          '$_currentPage',
                          style: const TextStyle(
                            color: Colors.white,
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(width: 20),
                        IconButton(
                          icon: const Icon(Icons.arrow_forward, color: Colors.white),
                          onPressed: _currentPage < widget.textbook.totalPages
                              ? () => setState(() => _currentPage++)
                              : null,
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
