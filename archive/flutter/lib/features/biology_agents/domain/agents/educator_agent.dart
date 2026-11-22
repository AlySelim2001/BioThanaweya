import 'dart:convert';
import 'package:flutter/foundation.dart';
import '../../../../core/constants/curriculum_data.dart';
import '../../../../core/network/gemini_service.dart';
import '../../models/agent_models.dart';
import 'biology_agent.dart';

/// Concrete implementation of Agent 1: Master Biology Educator.
/// Formats dual-layered pedagogical responses for Egyptian Thanaweya Amma Biology students
/// distinguishing between simplified ELI5 content (Arabic) and detailed technical anatomical breakdowns (English).
class MasterBiologyEducator implements BiologyAgent {
  final GeminiService geminiService;

  @override
  final AgentType agentType = AgentType.educator;

  @override
  final String name = 'Master Biology Educator';

  @override
  final String role = 'Pedagogical Scaffolding: ELI5 Analogies vs Detailed Anatomical Breakdowns';

  bool _isInitialized = false;

  @override
  bool get isInitialized => _isInitialized;

  MasterBiologyEducator({GeminiService? service})
      : geminiService = service ?? GeminiService();

  @override
  Future<void> initialize() async {
    _isInitialized = true;
  }

  /// System persona enforcing strict distinction between ELI5 and technical anatomical breakdown.
  String get systemPersonaInstruction => '''
You are the Master Biology Educator for Egyptian Thanaweya Amma Experimental Schools Biology (Units 1 & 2: Structure & Function in Living Organisms + Molecular Biology).

Your primary objective is to format responses specifically for Thanaweya Amma students by strictly distinguishing between two distinct pedagogical layers:

================================================================================
LAYER 1: SIMPLIFIED "ELI5" CONTENT (Arabic / باللغة العربية الفصحى المبسطة)
================================================================================
- Target: Intuitive understanding and conceptual anchoring.
- Provide a memorable, real-world analogy (e.g., water balloon turgor pressure, hydraulic cranes for muscle joints, recipe books and photocopiers for DNA/RNA).
- Focus on the "Big Picture" why and how without confusing jargon, explaining in warm, engaging Arabic.

================================================================================
LAYER 2: DETAILED TECHNICAL ANATOMICAL BREAKDOWN (English / بالإنجليزية العلمية)
================================================================================
- Target: Full marks on official Thanaweya Amma examination papers.
- Detail exhaustive anatomical mechanisms, cellular pathways, and biophysical laws.
- Must include precise physiological markers:
  * Musculoskeletal: Actin/Myosin sliding filaments, troponin-tropomyosin, Ca2+ release from sarcoplasmic reticulum, ATP cross-bridge attachment AND detachment, A-band constant length vs I-band/H-zone shortening.
  * Plant Physiology: Dixon & Joly Transpiration-Cohesion-Tension theory, Munch mass flow.
  * Molecular Biology: DNA replication fork, helicase, DNA polymerase 5'->3' synthesis, Okazaki fragments, ligase, codon AUG, peptidyl transferase ribozyme.
- Include Bloom's Taxonomy cognitive progression and 4-8 core technical terms.

Output strictly valid JSON with this exact schema:
{
  "eli5Arabic": "...",
  "deepTechnicalEnglish": "...",
  "bloomsLevel": "Analyzing & Evaluating",
  "coreVocabulary": ["Term 1", "Term 2", "Term 3"]
}
''';

  @override
  Future<MasterEducatorPayload> processTopic(
    CurriculumTopic topic, {
    Map<String, dynamic>? additionalContext,
  }) async {
    return generateDualExplanation(topic, additionalContext: additionalContext);
  }

  /// Generates the dual-layered response (ELI5 Arabic + Detailed Technical English) using Gemini API.
  Future<MasterEducatorPayload> generateDualExplanation(
    CurriculumTopic topic, {
    Map<String, dynamic>? additionalContext,
  }) async {
    if (!geminiService.hasValidApiKey) {
      debugPrint('[MasterBiologyEducator] Gemini API key not present; using verified curriculum fallback.');
      return getFallbackPayload(topic);
    }

    final prompt = '''
Topic: ${topic.titleEnglish} (${topic.titleArabic})
Chapter: ${topic.chapterTitle}
Unit: Unit ${topic.unitNumber}
Curriculum Baseline: ${topic.technicalSummary}

Please format the response specifically for Egyptian Thanaweya Amma students:
1. ELI5 simplified conceptual analogy in Arabic.
2. Detailed technical anatomical and physiological breakdown in English.
3. Bloom's Taxonomy cognitive level.
4. Core scientific vocabulary terms.
''';

    try {
      final rawOutput = await geminiService.generateText(
        prompt: prompt,
        systemInstruction: systemPersonaInstruction,
        temperature: 0.35,
      );

      final cleanJson = _extractJson(rawOutput);
      final decoded = jsonDecode(cleanJson) as Map<String, dynamic>;

      return MasterEducatorPayload(
        eli5Arabic: decoded['eli5Arabic'] as String? ?? topic.eli5Summary,
        deepTechnicalEnglish: decoded['deepTechnicalEnglish'] as String? ?? topic.technicalSummary,
        bloomsLevel: decoded['bloomsLevel'] as String? ?? 'Evaluating & Synthesizing',
        coreVocabulary: (decoded['coreVocabulary'] as List<dynamic>?)
                ?.map((e) => e.toString())
                .toList() ??
            ['Physiological Support', 'Cross-Bridges', 'ATP Detachment'],
      );
    } catch (e, stackTrace) {
      debugPrint('[MasterBiologyEducator] Gemini call failed: $e\n$stackTrace');
      return getFallbackPayload(topic);
    }
  }

  /// Generates only the simplified ELI5 content for quick revision.
  Future<String> generateEli5Only(CurriculumTopic topic) async {
    final payload = await generateDualExplanation(topic);
    return payload.eli5Arabic;
  }

  /// Generates only the detailed technical anatomical breakdown for exam prep.
  Future<String> generateTechnicalBreakdownOnly(CurriculumTopic topic) async {
    final payload = await generateDualExplanation(topic);
    return payload.deepTechnicalEnglish;
  }

  /// Offline resilience fallback ensuring students always have instant access to curriculum material.
  MasterEducatorPayload getFallbackPayload(CurriculumTopic topic) {
    return MasterEducatorPayload(
      eli5Arabic: topic.eli5Summary,
      deepTechnicalEnglish: topic.technicalSummary,
      bloomsLevel: 'Analyzing & Evaluating (Verified Textbook Standard)',
      coreVocabulary: [
        'Osmotic Turgor Pressure',
        'Lignin & Suberin',
        'Sarcomere Sliding Filaments',
        'Troponin & Ca2+',
        'DNA Polymerase 5\'->3\''
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
