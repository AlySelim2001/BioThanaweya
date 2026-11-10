import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../controllers/textbook_controller.dart';
import '../../domain/entities/ministry_textbook.dart';
import 'textbook_pdf_reader_screen.dart';

/// شاشة مكتبة كتب وكتيبات مفاهيم الوزارة الرسمية (Ministry Official Textbooks Library)
class MinistryTextbookScreen extends ConsumerWidget {
  const MinistryTextbookScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(textbookControllerProvider);
    final controller = ref.read(textbookControllerProvider.notifier);

    const backgroundColor = Color(0xFF12121A);
    const surfaceColor = Color(0xFF1E1E2C);
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
        actions: [
          IconButton(
            tooltip: 'تحديث المكتبة',
            icon: const Icon(Icons.refresh, color: Colors.white),
            onPressed: () => controller.loadTextbooks(),
          ),
        ],
      ),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator(color: primaryGreen))
          : ListView(
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
                              'تحميل لمرة واحدة وتصفح دائم 100% بدون إنترنت مع حفظ موضع القراءة.',
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

                // 2. بطاقات الكتب المتاحة (Bento Grid Cards)
                ...state.textbooks.map((book) {
                  final progress = state.downloadProgress[book.id];
                  final isDownloading = progress != null;

                  return Container(
                    margin: const EdgeInsets.only(bottom: 14),
                    padding: const EdgeInsets.all(16),
                    decoration: BoxDecoration(
                      color: const Color(0xFF252536),
                      borderRadius: BorderRadius.circular(16),
                      border: Border.all(
                        color: book.isDownloaded
                            ? primaryGreen.withValues(alpha: 0.4)
                            : Colors.white12,
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
                                color: book.isDownloaded
                                    ? primaryGreen.withValues(alpha: 0.15)
                                    : Colors.amber.withValues(alpha: 0.15),
                                borderRadius: BorderRadius.circular(12),
                              ),
                              child: Icon(
                                book.isDownloaded ? Icons.cloud_done : Icons.picture_as_pdf,
                                color: book.isDownloaded ? primaryGreen : Colors.amber,
                                size: 24,
                              ),
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
                                  Row(
                                    children: [
                                      Text(
                                        'الحجم: ${book.formattedFileSize}',
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

                        if (book.isDownloaded) ...[
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
                        ],

                        if (isDownloading) ...[
                          const SizedBox(height: 12),
                          ClipRRect(
                            borderRadius: BorderRadius.circular(4),
                            child: LinearProgressIndicator(
                              value: progress,
                              backgroundColor: Colors.black26,
                              color: Colors.amber,
                              minHeight: 6,
                            ),
                          ),
                          const SizedBox(height: 6),
                          Text(
                            'جاري التنزيل: ${(progress * 100).toStringAsFixed(0)}%',
                            style: const TextStyle(color: Colors.amber, fontSize: 11),
                          ),
                        ],

                        const SizedBox(height: 14),

                        // أزرار التحكم
                        Row(
                          children: [
                            if (book.isDownloaded) ...[
                              Expanded(
                                child: ElevatedButton.icon(
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: primaryGreen,
                                    foregroundColor: Colors.white,
                                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                                    padding: const EdgeInsets.symmetric(vertical: 10),
                                  ),
                                  icon: const Icon(Icons.chrome_reader_mode, size: 16),
                                  label: const Text('قراءة الكتاب الآن', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12)),
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
                              const SizedBox(width: 8),
                              IconButton(
                                tooltip: 'حذف التنزيل لتوفير المساحة',
                                icon: const Icon(Icons.delete_outline, color: Colors.redAccent, size: 20),
                                onPressed: () => controller.deleteBook(book.id),
                              ),
                            ] else ...[
                              Expanded(
                                child: ElevatedButton.icon(
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: Colors.amber,
                                    foregroundColor: Colors.black,
                                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                                    padding: const EdgeInsets.symmetric(vertical: 10),
                                  ),
                                  icon: const Icon(Icons.download, size: 16),
                                  label: Text(
                                    isDownloading ? 'جاري التحميل...' : 'تنزيل للقراءة بدون إنترنت',
                                    style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                                  ),
                                  onPressed: isDownloading ? null : () => controller.downloadBook(book),
                                ),
                              ),
                            ],
                          ],
                        ),
                      ],
                    ),
                  );
                }).toList(),
              ],
            ),
    );
  }
}
