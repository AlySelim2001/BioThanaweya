import 'dart:convert';
import 'package:flutter/foundation.dart';
import '../../../../core/constants/curriculum_data.dart';
import '../../../../core/network/gemini_service.dart';
import '../../models/agent_models.dart';
import 'base_biology_agent.dart';

/// Concrete implementation of Agent 1: Master Biology Educator & Pedagogical Strategist.
/// Generates dual-layered explanations (ELI5 Arabic analogies + Deep Technical English breakdowns)
/// and Bloom's Taxonomy progression using the Gemini API with zero-downtime offline fallbacks.
class MasterBiologyEducatorAgent extends BaseBiologyAgent<MasterEducatorPayload> {
  final GeminiService geminiService;

  MasterBiologyEducatorAgent({GeminiService? service})
      : geminiService = service ?? GeminiService(),
        super(
          agentType: AgentType.educator,
          name: 'Master Biology Educator',
          role: 'Core Concept Architecture, Dual-Layered Scaffolding & Bloom Progression',
        );

  @override
  String get systemPersonaInstruction => '''
You are Agent 1: Master Biology Educator & Pedagogical Strategist for the "Scholar Sidekick" (smarts.bio) ecosystem, specialized in the Egyptian Thanaweya Amma Experimental Schools Biology Curriculum (Units 1 & 2: Structure & Function + Molecular Biology).

Your mission is to provide rigorous, dual-layered biological instruction:
1. "Explain Like I'm 5" (ELI5) Layer in Arabic:
   - Provide an intuitive, vivid, real-world analogy grounded in everyday experiences.
   - Explain what the process is and why it matters in accessible Arabic without losing core scientific accuracy.
2. "Deep Technical Breakdown" Layer in English:
   - Provide an exhaustive, university-grade physiological and biophysical explanation.
   - Explicitly detail all anatomical pathways, chemical cascades (ATP, Ca2+, enzymes), membrane potentials, and biophysical laws.
   - Never omit vital Thanaweya Amma textbook details (e.g., sarcomere band dynamics, Dixon & Joly cohesion-tension theory, DNA polymerase 5'->3' directionality).
3. Bloom's Taxonomy Progression:
   - Identify the target cognitive level (Remembering, Understanding, Applying, Analyzing, Evaluating, or Creating).
4. Core Scientific Vocabulary:
   - List 4 to 8 essential scientific terms and concepts.

Always format your response as strict valid JSON with the following structure:
{
  "eli5Arabic": "...",
  "deepTechnicalEnglish": "...",
  "bloomsLevel": "...",
  "coreVocabulary": ["term1", "term2", "term3"]
}
''';

  @override
  Future<MasterEducatorPayload> processTopic(
    CurriculumTopic topic, {
    Map<String, dynamic>? additionalContext,
  }) async {
    // If no valid Gemini API key is configured, immediately return verified offline seed data
    if (!geminiService.hasValidApiKey) {
      debugPrint('[MasterBiologyEducatorAgent] No API key detected; using verified curriculum fallback.');
      return getFallbackPayload(topic);
    }

    final prompt = '''
Curriculum Topic: ${topic.titleEnglish} (${topic.titleArabic})
Chapter: ${topic.chapterTitle}
Unit: Unit ${topic.unitNumber}
Context Summary: ${topic.technicalSummary}

Generate the dual-layer scaffolding (ELI5 in Arabic + Deep Technical in English), Bloom's Taxonomy level, and core vocabulary in JSON format.
''';

    try {
      final rawResponse = await geminiService.generateText(
        prompt: prompt,
        systemInstruction: systemPersonaInstruction,
        temperature: 0.4,
      );

      final cleanJson = _extractJson(rawResponse);
      final decoded = jsonDecode(cleanJson) as Map<String, dynamic>;

      final eli5 = decoded['eli5Arabic'] as String? ?? topic.eli5Summary;
      final technical = decoded['deepTechnicalEnglish'] as String? ?? topic.technicalSummary;
      final blooms = decoded['bloomsLevel'] as String? ?? 'Analyzing & Evaluating';
      final vocabList = (decoded['coreVocabulary'] as List<dynamic>?)
              ?.map((e) => e.toString())
              .toList() ??
          ['Physiological Support', 'Turgor Pressure', 'ATP Hydrolysis'];

      return MasterEducatorPayload(
        eli5Arabic: eli5,
        deepTechnicalEnglish: technical,
        bloomsLevel: blooms,
        coreVocabulary: vocabList,
      );
    } catch (e, stackTrace) {
      debugPrint('[MasterBiologyEducatorAgent] Gemini generation failed: $e\n$stackTrace');
      // Graceful offline fallback
      return getFallbackPayload(topic);
    }
  }

  @override
  MasterEducatorPayload getFallbackPayload(CurriculumTopic topic) {
    return MasterEducatorPayload(
      eli5Arabic: topic.eli5Summary,
      deepTechnicalEnglish: topic.technicalSummary,
      bloomsLevel: 'Analyzing & Evaluating (Curriculum Baseline)',
      coreVocabulary: [
        'Physiological Turgor',
        'Cross-Bridges',
        'Active Transport',
        'Semi-conservative Replication',
        'Palindromic Sequences'
      ],
    );
  }

  String _extractJson(String response) {
    var text = response.trim();
    if (text.startsWith('```json')) {
      text = text.substring(7);
    } else if (text.startsWith('```')) {
      text = text.substring(3);
    }
    if (text.endsWith('```')) {
      text = text.substring(0, text.length - 3);
    }
    return text.trim();
  }
}
