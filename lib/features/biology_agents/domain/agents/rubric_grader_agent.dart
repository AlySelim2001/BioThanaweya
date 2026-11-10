import 'base_agent.dart';

class GraderResult {
  final double scorePercentage;
  final bool isPassed;
  final List<String> matchedKeywords;
  final List<String> missingKeywords;
  final String pedagogicalFeedback;

  GraderResult({
    required this.scorePercentage,
    required this.isPassed,
    required this.matchedKeywords,
    required this.missingKeywords,
    required this.pedagogicalFeedback,
  });
}

class RubricGraderAgent implements BaseAgent {
  @override
  String get agentId => 'agent_rubric_grader_02';

  @override
  String get agentName => 'وكيل التصحيح ومطابقة المعايير الوزارية';

  /// تقييم الإجابة المقالية للطالب محلياً بناءً على مفتاح الدقة والكلمات المفتاحية
  GraderResult evaluateEssayAnswer({
    required String studentAnswer,
    required List<String> officialKeywords,
    required double passingThreshold,
  }) {
    final sanitizedStudentAnswer = studentAnswer.toLowerCase();
    final matched = <String>[];
    final missing = <String>[];

    for (var keyword in officialKeywords) {
      if (sanitizedStudentAnswer.contains(keyword.toLowerCase())) {
        matched.add(keyword);
      } else {
        missing.add(keyword);
      }
    }

    final score = officialKeywords.isNotEmpty
        ? (matched.length / officialKeywords.length) * 100.0
        : 0.0;

    final passed = score >= passingThreshold;

    String feedback;
    if (passed) {
      feedback = "إجابة ممتازة ومتوافقة بدقة مع النموذج الوزاري. تم رصد الكلمات المفتاحية الأساسية.";
    } else {
      feedback = "الإجابة ناقصة وعلمياً تحتاج إلى تضمين المصطلحات التالية: ${missing.join(', ')}.";
    }

    return GraderResult(
      scorePercentage: score,
      isPassed: passed,
      matchedKeywords: matched,
      missingKeywords: missing,
      pedagogicalFeedback: feedback,
    );
  }

  @override
  Future<AgentResponse> processRequest(AgentContext context) async {
    // استخراج الكلمات المفتاحية من السياق إذا تم تمريرها، أو استخدام كلمات مفتاحية قياسية
    final keywords = (context.metadata['officialKeywords'] as List<dynamic>?)
            ?.map((e) => e.toString())
            .toList() ??
        ['الدعامة', 'الفسيولوجية', 'التركيبية', 'الماء', 'الجدار الخلوي'];

    final result = evaluateEssayAnswer(
      studentAnswer: context.studentQuery,
      officialKeywords: keywords,
      passingThreshold: 60.0,
    );

    return AgentResponse(
      content: result.pedagogicalFeedback,
      keyInsights: [
        "النسبة المئوية المحققة: ${result.scorePercentage.toStringAsFixed(1)}%",
        "حالة الاجتياز: ${result.isPassed ? 'تم الاجتياز بنجاح' : 'لم يتم الاجتياز'}",
        if (result.missingKeywords.isNotEmpty)
          "المصطلحات الواجب التركيز عليها: ${result.missingKeywords.join(' - ')}"
      ],
      isOfflineGenerated: true,
    );
  }
}
