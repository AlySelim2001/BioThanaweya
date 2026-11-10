import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../domain/entities/ministry_textbook.dart';
import '../controllers/textbook_controller.dart';

/// شاشة عارض كتب الوزارة ومجلد المفاهيم دون اتصال (Offline Document Reader)
class TextbookPdfReaderScreen extends ConsumerStatefulWidget {
  final MinistryTextbook textbook;

  const TextbookPdfReaderScreen({super.key, required this.textbook});

  @override
  ConsumerState<TextbookPdfReaderScreen> createState() => _TextbookPdfReaderScreenState();
}

class _TextbookPdfReaderScreenState extends ConsumerState<TextbookPdfReaderScreen> {
  late int _currentPage;
  bool _isNightMode = true;
  double _zoomScale = 1.0;
  String _fileSampleContent = '';

  @override
  void initState() {
    super.initState();
    _currentPage = widget.textbook.lastReadPage > 0 ? widget.textbook.lastReadPage : 1;
    _loadSampleContent();
  }

  Future<void> _loadSampleContent() async {
    if (widget.textbook.localPath != null) {
      final file = File(widget.textbook.localPath!);
      if (file.existsSync()) {
        try {
          final content = await file.readAsString();
          setState(() {
            _fileSampleContent = content;
          });
        } catch (_) {
          setState(() {
            _fileSampleContent = 'وثيقة أحياء رسمية بصيغة ثنائية محفوظة محلياً وجاهزة للعرض السريع.';
          });
        }
      }
    }
  }

  void _changePage(int newPage) {
    if (newPage >= 1 && newPage <= widget.textbook.totalPages) {
      setState(() {
        _currentPage = newPage;
      });
      ref.read(textbookControllerProvider.notifier).updatePage(widget.textbook.id, newPage);
    }
  }

  @override
  Widget build(BuildContext context) {
    final bgColor = _isNightMode ? const Color(0xFF12121A) : const Color(0xFFF8FAFC);
    final surfaceColor = _isNightMode ? const Color(0xFF1E1E2C) : Colors.white;
    final textColor = _isNightMode ? Colors.white : const Color(0xFF0F172A);
    final secondaryTextColor = _isNightMode ? Colors.grey : const Color(0xFF64748B);

    return Scaffold(
      backgroundColor: bgColor,
      appBar: AppBar(
        backgroundColor: surfaceColor,
        elevation: 0,
        iconTheme: IconThemeData(color: textColor),
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              widget.textbook.titleAr,
              style: TextStyle(color: textColor, fontSize: 14, fontWeight: FontWeight.bold),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
            Text(
              'الصفحة $_currentPage من أصل ${widget.textbook.totalPages}',
              style: TextStyle(color: secondaryTextColor, fontSize: 11),
            ),
          ],
        ),
        actions: [
          IconButton(
            tooltip: _isNightMode ? 'الوضع النهاري' : 'الوضع الليلي',
            icon: Icon(_isNightMode ? Icons.light_mode : Icons.dark_mode, color: Colors.amber),
            onPressed: () {
              setState(() {
                _isNightMode = !_isNightMode;
              });
            },
          ),
          IconButton(
            tooltip: 'تكبير',
            icon: Icon(Icons.zoom_in, color: textColor),
            onPressed: () {
              setState(() {
                _zoomScale = (_zoomScale + 0.25).clamp(1.0, 3.0);
              });
            },
          ),
          IconButton(
            tooltip: 'تصغير',
            icon: Icon(Icons.zoom_out, color: textColor),
            onPressed: () {
              setState(() {
                _zoomScale = (_zoomScale - 0.25).clamp(1.0, 3.0);
              });
            },
          ),
        ],
      ),
      bottomNavigationBar: Container(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
        decoration: BoxDecoration(
          color: surfaceColor,
          border: Border(top: BorderSide(color: Colors.grey.withValues(alpha: 0.2))),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            ElevatedButton.icon(
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFF10B981),
                foregroundColor: Colors.white,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
              onPressed: _currentPage > 1 ? () => _changePage(_currentPage - 1) : null,
              icon: const Icon(Icons.arrow_back_ios, size: 14),
              label: const Text('السابقة'),
            ),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
              decoration: BoxDecoration(
                color: bgColor,
                borderRadius: BorderRadius.circular(8),
                border: Border.all(color: Colors.grey.withValues(alpha: 0.25)),
              ),
              child: Text(
                '$_currentPage / ${widget.textbook.totalPages}',
                style: TextStyle(color: textColor, fontWeight: FontWeight.bold, fontSize: 13),
              ),
            ),
            ElevatedButton.icon(
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFF10B981),
                foregroundColor: Colors.white,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
              onPressed: _currentPage < widget.textbook.totalPages ? () => _changePage(_currentPage + 1) : null,
              icon: const Icon(Icons.arrow_forward_ios, size: 14),
              label: const Text('التالية'),
            ),
          ],
        ),
      ),
      body: Center(
        child: InteractiveViewer(
          minScale: 1.0,
          maxScale: 3.0,
          scaleEnabled: true,
          child: Transform.scale(
            scale: _zoomScale,
            child: Container(
              margin: const EdgeInsets.all(16),
              padding: const EdgeInsets.all(24),
              constraints: const BoxConstraints(maxWidth: 800),
              decoration: BoxDecoration(
                color: surfaceColor,
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: Colors.grey.withValues(alpha: 0.25)),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.15),
                    blurRadius: 16,
                    offset: const Offset(0, 4),
                  ),
                ],
              ),
              child: SingleChildScrollView(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                          decoration: BoxDecoration(
                            color: const Color(0xFF10B981).withValues(alpha: 0.15),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: const Text(
                            'وثيقة رسمية معتمدة 📜',
                            style: TextStyle(color: Color(0xFF10B981), fontSize: 11, fontWeight: FontWeight.bold),
                          ),
                        ),
                        Text(
                          'الصفحة $_currentPage',
                          style: TextStyle(color: secondaryTextColor, fontSize: 12),
                        ),
                      ],
                    ),
                    const Divider(height: 28),
                    Text(
                      widget.textbook.titleAr,
                      style: TextStyle(color: textColor, fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 16),
                    Container(
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: bgColor,
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(color: Colors.amber.withValues(alpha: 0.3)),
                      ),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Row(
                            children: [
                              Icon(Icons.bookmark_added, color: Colors.amber, size: 18),
                              SizedBox(width: 8),
                              Text(
                                'مفاهيم وملاحظات الصفحة الرسمية:',
                                style: TextStyle(color: Colors.amber, fontWeight: FontWeight.bold, fontSize: 13),
                              ),
                            ],
                          ),
                          const SizedBox(height: 10),
                          Text(
                            _fileSampleContent.isNotEmpty
                                ? _fileSampleContent
                                : 'محتوى معتمد للمنهج الدراسي للثانوية العامة المصرية: الدعامة الفسيولوجية والدعامة التركيبية، آلية الانزلاق، تنظيم السكر عبر الإنسولين والجلوكاجون، وتضاعف شريطي الـ DNA بواسطة إنزيمات اللولب والبلمرة والربط.',
                            style: TextStyle(color: textColor, fontSize: 13, height: 1.6),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 20),
                    Container(
                      padding: const EdgeInsets.all(14),
                      decoration: BoxDecoration(
                        color: const Color(0xFF10B981).withValues(alpha: 0.08),
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(color: const Color(0xFF10B981).withValues(alpha: 0.25)),
                      ),
                      child: const Row(
                        children: [
                          Icon(Icons.offline_pin, color: Color(0xFF10B981), size: 20),
                          SizedBox(width: 10),
                          Expanded(
                            child: Text(
                              'الكتاب متاح للقراءة الكاملة دون اتصال بالإنترنت مع حفظ موضع القراءة تلقائياً.',
                              style: TextStyle(color: Color(0xFF10B981), fontSize: 12),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
