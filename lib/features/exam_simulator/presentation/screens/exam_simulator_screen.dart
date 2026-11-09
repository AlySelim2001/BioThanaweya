import 'package:flutter/material.dart';
import '../../../../core/constants/curriculum_data.dart';
import '../../../../core/theme/app_theme.dart';
import '../../models/exam_models.dart';
import '../../rubric_grader.dart';

class ExamSimulatorScreen extends StatefulWidget {
  final CurriculumTopic topic;

  const ExamSimulatorScreen({super.key, required this.topic});

  @override
  State<ExamSimulatorScreen> createState() => _ExamSimulatorScreenState();
}

class _ExamSimulatorScreenState extends State<ExamSimulatorScreen> {
  final TextEditingController _answerController = TextEditingController();
  RubricEvaluationResult? _evaluationResult;

  late final ExamQuestion _question;

  @override
  void initState() {
    super.initState();
    _question = ExamQuestion(
      id: '${widget.topic.id}_exam_1',
      topicId: widget.topic.id,
      type: QuestionType.giveReason,
      promptArabic: 'علل لما يأتي: استمرار انقباض العضلة الهيكلية وحدوث الشد العضلي المؤلم عند تناقص جزيئات ATP.',
      promptEnglish: 'Give reason for: Continuous contraction of skeletal muscle and occurrence of painful muscle cramp upon the depletion of ATP molecules.',
      modelAnswerArabic: 'لأن جزيئات ATP لازمة لفصل الروابط المستعرضة الممتدة من خيوط الميوسين عن خيوط الأكتين؛ فعند تناقص ATP تظل الروابط المستعرضة متصلة بخيوط الأكتين وتظل العضلة في حالة انقباض مستمر ولا تنبسط مما يؤدي للشد العضلي.',
      modelAnswerEnglish: 'Because ATP molecules are required to detach the cross-bridges originating from myosin filaments from the actin filaments. Upon ATP depletion, the cross-bridges remain attached to actin, preventing muscle relaxation and causing painful muscle cramp.',
      requiredKeywords: ['ATP', 'الروابط المستعرضة', 'cross-bridges', 'الميوسين', 'myosin', 'الأكتين', 'actin', 'انفصال', 'detach', 'relaxation', 'انبساط'],
      maxMarks: 2,
    );
  }

  void _submitAnswer() {
    setState(() {
      _evaluationResult = RubricGrader.gradeAnswer(
        question: _question,
        studentAnswer: _answerController.text,
      );
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
        title: const Text(
          'Ministry Exam Simulator (Agent 4 Grader)',
          style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Exam Banner
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: AppTheme.examGold.withOpacity(0.1),
                borderRadius: BorderRadius.circular(10),
                border: Border.all(color: AppTheme.examGold.withOpacity(0.4)),
              ),
              child: const Row(
                children: [
                  Icon(Icons.gavel, color: AppTheme.examGold, size: 20),
                  SizedBox(width: 8),
                  Expanded(
                    child: Text(
                      'Thanaweya Amma Official Mark Scheme Grader • تصحيح وفق نموذج الإجابة الوزاري',
                      style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),

            // Question Box
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                          decoration: BoxDecoration(
                            color: AppTheme.molecularPurple.withOpacity(0.1),
                            borderRadius: BorderRadius.circular(6),
                          ),
                          child: const Text('Give Reason (2 Marks)', style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: AppTheme.molecularPurple)),
                        ),
                        Text(widget.topic.titleArabic, style: const TextStyle(fontSize: 11, color: Colors.grey)),
                      ],
                    ),
                    const SizedBox(height: 12),
                    Text(
                      _question.promptArabic,
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                      textAlign: TextAlign.right,
                    ),
                    const SizedBox(height: 6),
                    Text(
                      _question.promptEnglish,
                      style: TextStyle(fontSize: 12, color: Colors.grey.shade700),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),

            // Student Answer Input
            const Text(
              'Your Answer (اكتب إجابتك هنا بدقة علمية):',
              style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            TextField(
              controller: _answerController,
              maxLines: 5,
              decoration: InputDecoration(
                hintText: 'Enter complete scientific explanation including key terms...',
                filled: true,
                fillColor: Colors.white,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                  borderSide: BorderSide(color: Colors.grey.shade300),
                ),
              ),
            ),
            const SizedBox(height: 14),

            ElevatedButton.icon(
              onPressed: _submitAnswer,
              icon: const Icon(Icons.check_circle),
              label: const Text('Grade with Ministry Rubric (تصحيح الإجابة)'),
              style: ElevatedButton.styleFrom(
                minimumSize: const Size.fromHeight(48),
                backgroundColor: AppTheme.bioTealPrimary,
                foregroundColor: Colors.white,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
            ),

            // Evaluation Results
            if (_evaluationResult != null) ...[
              const SizedBox(height: 20),
              _buildEvaluationCard(_evaluationResult!),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildEvaluationCard(RubricEvaluationResult result) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: result.fullCreditAwarded ? AppTheme.emeraldSuccess.withOpacity(0.08) : Colors.orange.shade50,
        borderRadius: BorderRadius.circular(14),
        border: Border.all(
          color: result.fullCreditAwarded ? AppTheme.emeraldSuccess : Colors.orange.shade300,
          width: 1.5,
        ),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                'الدرجة المستحقة: ${result.scoreEarned} / ${result.maxMarks} (${result.percentage.toInt()}%)',
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 14,
                  color: result.fullCreditAwarded ? AppTheme.emeraldSuccess : Colors.orange.shade900,
                ),
              ),
              Icon(
                result.fullCreditAwarded ? Icons.verified : Icons.error_outline,
                color: result.fullCreditAwarded ? AppTheme.emeraldSuccess : Colors.orange.shade800,
              ),
            ],
          ),
          const SizedBox(height: 10),
          Text(
            result.examinerFeedback,
            style: const TextStyle(fontSize: 12, height: 1.4),
            textAlign: TextAlign.right,
          ),
          const SizedBox(height: 14),
          if (result.matchedKeywords.isNotEmpty) ...[
            const Text('Matched Model Keywords (تم احتسابها):', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 11)),
            const SizedBox(height: 4),
            Wrap(
              spacing: 6,
              children: result.matchedKeywords.map((kw) => Chip(
                label: Text(kw, style: const TextStyle(fontSize: 10, color: Colors.white)),
                backgroundColor: AppTheme.emeraldSuccess,
                padding: EdgeInsets.zero,
              )).toList(),
            ),
            const SizedBox(height: 8),
          ],
          if (result.missingKeywords.isNotEmpty) ...[
            const Text('Missing Required Keywords (كلمات مفتاحية مفقودة):', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 11, color: AppTheme.alertCoral)),
            const SizedBox(height: 4),
            Wrap(
              spacing: 6,
              children: result.missingKeywords.map((kw) => Chip(
                label: Text(kw, style: const TextStyle(fontSize: 10, color: Colors.white)),
                backgroundColor: AppTheme.alertCoral,
                padding: EdgeInsets.zero,
              )).toList(),
            ),
          ]
        ],
      ),
    );
  }
}
