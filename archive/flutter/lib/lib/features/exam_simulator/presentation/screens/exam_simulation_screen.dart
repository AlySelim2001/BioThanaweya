import 'package:flutter/material.dart';
import '../../../biology_agents/domain/agents/rubric_grader_agent.dart';

class ExamSimulationScreen extends StatefulWidget {
  const ExamSimulationScreen({Key? key}) : super(key: key);

  @override
  State<ExamSimulationScreen> createState() => _ExamSimulationScreenState();
}

class _ExamSimulationScreenState extends State<ExamSimulationScreen> {
  final TextEditingController _answerController = TextEditingController();
  final RubricGraderAgent _graderAgent = RubricGraderAgent();
  bool _isSubmitted = false;
  String _feedbackResult = '';
  double _scoreResult = 0.0;
  List<String> _matchedKeywords = [];
  List<String> _missingKeywords = [];

  // نموذج سؤال مقالي تجريبي من منهج الأحياء (الفصل الحادي عشر - DNA والتقنية الحيوية)
  final String _currentQuestion = 'وضح بأسلوب علمي دور إنزيمات القصر في الدفاع عن البكتيريا ضد الفيروسات؟';
  final List<String> _officialKeywords = ['متتاليات', 'نيوكليوتيدات', 'فيروسي', 'قص', 'دفاعياً'];

  void _submitAndEvaluate() {
    final studentAnswer = _answerController.text.trim();
    if (studentAnswer.isEmpty) return;

    // تقييم الإجابة باستخدام RubricGraderAgent محلياً
    final result = _graderAgent.evaluateEssayAnswer(
      studentAnswer: studentAnswer,
      officialKeywords: _officialKeywords,
      passingThreshold: 60.0,
    );

    setState(() {
      _isSubmitted = true;
      _scoreResult = result.scorePercentage;
      _matchedKeywords = result.matchedKeywords;
      _missingKeywords = result.missingKeywords;
      _feedbackResult = result.pedagogicalFeedback;
    });
  }

  void _resetExam() {
    setState(() {
      _isSubmitted = false;
      _answerController.clear();
      _feedbackResult = '';
      _scoreResult = 0.0;
      _matchedKeywords.clear();
      _missingKeywords.clear();
    });
  }

  @override
  void dispose() {
    _answerController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('محاكي الامتحانات والتدقيق الوزاري - الأحياء'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // بطاقة السؤال
            Card(
              elevation: 4,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      'سؤال مقالي تجريبي (الثانوية العامة):',
                      style: TextStyle(fontWeight: FontWeight.bold, color: Colors.blue),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      _currentQuestion,
                      style: const TextStyle(fontSize: 18, height: 1.4),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 20),

            // حقل إجابة الطالب
            TextField(
              controller: _answerController,
              maxLines: 5,
              enabled: !_isSubmitted,
              decoration: InputDecoration(
                hintText: 'اكتب إجابتك العلمية هنا مستخدماً المصطلحات الدقيقة...',
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                filled: true,
                fillColor: Colors.grey[50],
              ),
            ),
            const SizedBox(height: 20),

            // زر الإرسال أو إعادة المحاولة
            if (!_isSubmitted)
              ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 14),
                  backgroundColor: Colors.green[700],
                ),
                onPressed: _submitAndEvaluate,
                icon: const Icon(Icons.check_circle, color: Colors.white),
                label: const Text('إرسال وتقييم الإجابة محلياً', style: TextStyle(fontSize: 16, color: Colors.white)),
              )
            else ...[
              // عرض التغذية الراجعة والنتيجة
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: _scoreResult >= 60 ? Colors.green[50] : Colors.orange[50],
                  borderRadius: BorderRadius.circular(12),
                  border: Border.all(color: _scoreResult >= 60 ? Colors.green : Colors.orange),
                ),
                child: Column(
                  children: [
                    Text(
                      'النتيجة الوزارية: ${_scoreResult.toStringAsFixed(0)}%',
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        color: _scoreResult >= 60 ? Colors.green[800] : Colors.orange[800],
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      _feedbackResult,
                      textAlign: TextAlign.center,
                      style: const TextStyle(fontSize: 16, height: 1.3),
                    ),
                    if (_matchedKeywords.isNotEmpty) ...[
                      const SizedBox(height: 12),
                      Wrap(
                        spacing: 6,
                        children: _matchedKeywords.map((kw) => Chip(
                          label: Text(kw, style: const TextStyle(fontSize: 11, color: Colors.white)),
                          backgroundColor: Colors.green[700],
                        )).toList(),
                      ),
                    ],
                    if (_missingKeywords.isNotEmpty) ...[
                      const SizedBox(height: 6),
                      Wrap(
                        spacing: 6,
                        children: _missingKeywords.map((kw) => Chip(
                          label: Text(kw, style: const TextStyle(fontSize: 11, color: Colors.white)),
                          backgroundColor: Colors.orange[800],
                        )).toList(),
                      ),
                    ],
                  ],
                ),
              ),
              const SizedBox(height: 20),
              OutlinedButton.icon(
                onPressed: _resetExam,
                icon: const Icon(Icons.refresh),
                label: const Text('محاولة سؤال آخر'),
                style: OutlinedButton.styleFrom(padding: const EdgeInsets.symmetric(vertical: 12)),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
