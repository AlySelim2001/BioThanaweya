import 'package:flutter/material.dart';
import '../../data/spaced_repetition_repository.dart';
import '../../models/spaced_repetition_item.dart';

class FlashcardScreen extends StatefulWidget {
  const FlashcardScreen({Key? key}) : super(key: key);

  @override
  State<FlashcardScreen> createState() => _FlashcardScreenState();
}

class _FlashcardScreenState extends State<FlashcardScreen> {
  final SpacedRepetitionRepository _repository = SpacedRepetitionRepository();
  bool _isFlipped = false;
  int _currentIndex = 0;

  // قائمة بطاقات الفصل الحادي عشر (أحياء الثانوية العامة: DNA والتقنية الحيوية)
  final List<Map<String, String>> _flashcards = [
    {
      'id': 'card_biotech_01',
      'question': 'ما دور إنزيمات القصر والبكتيريا في التقنية الحيوية؟',
      'answer': 'تتعرف على متتاليات نيوكليوتيدات معينة في DNA الفيروسي وتعمل على قصره دفاعياً عند مواقع التعرف المتناظرة.',
    },
    {
      'id': 'card_dna_poly_02',
      'question': 'ما وظيفة إنزيم البلمرة في تضاعف الـ DNA؟',
      'answer': 'بناء الشريط الجديد في الاتجاه (5\' إلى 3\') بإضافة النيوكليوتيدات التكميلية باستخدام الشريط الأصلي كقالب.',
    },
    {
      'id': 'card_ligase_03',
      'question': 'ما دور إنزيم الربط (DNA Ligase) في تضاعف الشريط المتأخر؟',
      'answer': 'ربط قطع أوكازاكي غير المتصلة على الشريط القالب (5\' إلى 3\') لتكوين شريط DNA متصل.',
    },
  ];

  void _flipCard() {
    setState(() {
      _isFlipped = !_isFlipped;
    });
  }

  void _rateCard(int grade) async {
    final currentCardData = _flashcards[_currentIndex];
    final cardId = currentCardData['id'] ?? 'card_$_currentIndex';

    // استدعاء مستودع التكرار المتباعد لتحديث وحفظ الحالة محلياً
    final item = SpacedRepetitionItem(cardId: cardId);
    await _repository.updateCardReview(item, grade);

    if (mounted) {
      setState(() {
        _isFlipped = false;
        if (_currentIndex < _flashcards.length - 1) {
          _currentIndex++;
        } else {
          _currentIndex = 0; // إعادة البدء كمحاكاة لنهاية الجلسة
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('اكتملت جلسة مراجعة الفلاش كاردز! تم تحديث فترات التكرار المتباعد.'),
              backgroundColor: Colors.green,
            ),
          );
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final currentCard = _flashcards[_currentIndex];

    return Scaffold(
      appBar: AppBar(
        title: const Text('مراجعة الفلاش كاردز التكيفية - الأحياء'),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            // مؤشر التقدم
            LinearProgressIndicator(
              value: (_currentIndex + 1) / _flashcards.length,
              backgroundColor: Colors.grey[300],
              color: Colors.green,
            ),
            const SizedBox(height: 24),

            // بطاقة السؤال/الإجابة التفاعلية
            Expanded(
              child: GestureDetector(
                onTap: _flipCard,
                child: Card(
                  elevation: 6,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(16),
                  ),
                  child: Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(24.0),
                    alignment: Alignment.center,
                    child: SingleChildScrollView(
                      child: Text(
                        _isFlipped ? currentCard['answer']! : currentCard['question']!,
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          fontSize: 22,
                          fontWeight: FontWeight.bold,
                          color: _isFlipped ? Colors.blue[800] : Colors.black87,
                        ),
                      ),
                    ),
                  ),
                ),
              ),
            ),
            const SizedBox(height: 16),
            Text(
              _isFlipped ? 'اضغط للرجوع للسؤال' : 'اضغط على البطاقة لعرض الإجابة النموذجية',
              style: const TextStyle(color: Colors.grey),
            ),
            const SizedBox(height: 24),

            // أزرار تقييم التذكر (خوارزمية FSRS / SM-2)
            if (_isFlipped) ...[
              const Text('قيم مدى تذكرك للإجابة:', style: TextStyle(fontWeight: FontWeight.bold)),
              const SizedBox(height: 12),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
                    onPressed: () => _rateCard(1),
                    child: const Text('صعب', style: TextStyle(color: Colors.white)),
                  ),
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(backgroundColor: Colors.orange),
                    onPressed: () => _rateCard(3),
                    child: const Text('جيد', style: TextStyle(color: Colors.white)),
                  ),
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(backgroundColor: Colors.green),
                    onPressed: () => _rateCard(5),
                    child: const Text('سهل جداً', style: TextStyle(color: Colors.white)),
                  ),
                ],
              ),
            ],
            const SizedBox(height: 20),
          ],
        ),
      ),
    );
  }
}
