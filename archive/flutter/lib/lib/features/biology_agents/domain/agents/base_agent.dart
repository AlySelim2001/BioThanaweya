import '../../../../core/constants/curriculum_data.dart';
import '../../../../core/network/gemini_service.dart';
import '../../models/agent_models.dart';

abstract class BaseAgent {
  String get agentId;
  String get agentName;
  
  Future<AgentResponse> processRequest(AgentContext context);
}

class AgentContext {
  final String currentChapter;
  final StudentMasteryLevel masteryLevel;
  final String studentQuery;
  final Map<String, dynamic> metadata;

  AgentContext({
    required this.currentChapter,
    required this.masteryLevel,
    required this.studentQuery,
    this.metadata = const {},
  });
}

class AgentResponse {
  final String content;
  final List<String> keyInsights;
  final bool isOfflineGenerated;

  AgentResponse({
    required this.content,
    required this.keyInsights,
    required this.isOfflineGenerated,
  });
}

// مثال لتنفيذ وكيل الشرح المعماري
class ExplainerAgent implements BaseAgent {
  final GeminiService geminiService;

  ExplainerAgent({GeminiService? service})
      : geminiService = service ?? GeminiService();

  @override
  String get agentId => 'agent_explainer_01';
  
  @override
  String get agentName => 'الشارح الذكي لمنهج الأحياء';

  @override
  Future<AgentResponse> processRequest(AgentContext context) async {
    try {
      // محاولة استدعاء Gemini API عبر Google AI Studio إذا توفر الاتصال
      // مع آلية السقوط التلقائي (Fallback) للقاعدة المحلية عند انقطاع الإنترنت
      return await _fetchCloudExplanation(context);
    } catch (_) {
      return _generateLocalFallbackExplanation(context);
    }
  }

  Future<AgentResponse> _fetchCloudExplanation(AgentContext context) async {
    if (!geminiService.hasValidApiKey) {
      throw StateError('Gemini API key not configured');
    }

    final prompt = '''
Chapter: ${context.currentChapter}
Student Mastery Level: ${context.masteryLevel.label}
Student Query: ${context.studentQuery}

Provide a targeted explanation tailored for an Egyptian Thanaweya Amma biology student.
Distinguish between ELI5 simple concepts and technical anatomical details.
Highlight 2-4 key scientific insights.
''';

    final text = await geminiService.generateText(
      prompt: prompt,
      temperature: 0.5,
    );

    return AgentResponse(
      content: text.isNotEmpty ? text : "شرح تفصيلي مدعوم بالذكاء الاصطناعي للفصل: ${context.currentChapter}",
      keyInsights: [
        "التدرج وفق مستوى الاستيعاب: ${context.masteryLevel.label}",
        "التركيز على نواتج التعلم الوزارية لفصل: ${context.currentChapter}"
      ],
      isOfflineGenerated: false,
    );
  }

  AgentResponse _generateLocalFallbackExplanation(AgentContext context) {
    final matchedTopic = CurriculumData.topics.firstWhere(
      (t) =>
          t.chapterTitle.contains(context.currentChapter) ||
          t.titleArabic.contains(context.currentChapter) ||
          context.currentChapter.contains(t.titleArabic),
      orElse: () => CurriculumData.topics.first,
    );

    return AgentResponse(
      content: "${matchedTopic.eli5Summary}\n\n[التحليل العلمي الدقيق]:\n${matchedTopic.technicalSummary}",
      keyInsights: matchedTopic.examTraps.isNotEmpty
          ? matchedTopic.examTraps
          : ["مراجعة الدعامة والاتزان"],
      isOfflineGenerated: true,
    );
  }
}
