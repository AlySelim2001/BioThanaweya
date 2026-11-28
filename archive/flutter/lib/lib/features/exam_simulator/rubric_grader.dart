import 'models/exam_models.dart';

/// Ministry Mark Scheme Keyword-Based Evaluation Engine.
/// Analyzes student open-ended answers against official Thanaweya Amma rubrics.
class RubricGrader {
  static RubricEvaluationResult gradeAnswer({
    required ExamQuestion question,
    required String studentAnswer,
  }) {
    if (studentAnswer.trim().isEmpty) {
      return RubricEvaluationResult(
        scoreEarned: 0.0,
        maxMarks: question.maxMarks,
        percentage: 0.0,
        matchedKeywords: [],
        missingKeywords: question.requiredKeywords,
        examinerFeedback: 'لم يتم تقديم أي إجابة. يرجى كتابة المفاهيم العلمية الأساسية للحصول على درجات نموذج الإجابة الرسمي.',
        fullCreditAwarded: false,
      );
    }

    final normalizedStudent = studentAnswer.toLowerCase().trim();
    final matched = <String>[];
    final missing = <String>[];

    for (final keyword in question.requiredKeywords) {
      final normalizedKeyword = keyword.toLowerCase().trim();
      if (normalizedStudent.contains(normalizedKeyword)) {
        matched.add(keyword);
      } else {
        missing.add(keyword);
      }
    }

    final ratio = question.requiredKeywords.isEmpty
        ? 1.0
        : (matched.length / question.requiredKeywords.length);

    double score;
    if (ratio >= 0.85) {
      score = question.maxMarks.toDouble();
    } else if (ratio >= 0.50) {
      score = question.maxMarks * 0.5;
    } else if (ratio >= 0.25) {
      score = question.maxMarks * 0.25;
    } else {
      score = 0.0;
    }

    final percentage = (score / question.maxMarks) * 100.0;
    final isFull = score == question.maxMarks;

    String feedback;
    if (isFull) {
      feedback = 'ممتاز! إجابة علمية دقيقة ومتوافقة تماماً مع نموذج تصحيح وزارة التربية والتعليم. لقد قمت بذكر كافة المصطلحات المفتاحية المطلوبة.';
    } else if (score > 0) {
      feedback = 'إجابة جيدة ولكنها ناقصة في معايير التصحيح الوزاري. لقد أغفلت بعض الكلمات المحورية: (${missing.join(", ")}). في امتحانات الثانوية العامة، يتم توزيع الدرجات على كل مصطلح علمي محدد.';
    } else {
      feedback = 'الإجابة تفتقر إلى المصطلحات العلمية الدقيقة المعتمدة في نموذج الإجابة. يجب تضمين: (${missing.join(", ")}).';
    }

    return RubricEvaluationResult(
      scoreEarned: score,
      maxMarks: question.maxMarks,
      percentage: percentage,
      matchedKeywords: matched,
      missingKeywords: missing,
      examinerFeedback: feedback,
      fullCreditAwarded: isFull,
    );
  }
}
