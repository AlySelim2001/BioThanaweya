import '../../core/constants/curriculum_data.dart';
import '../flashcards/models/flashcard_model.dart';
import 'domain/agents/master_biology_educator_agent.dart';
import 'models/agent_models.dart';

/// Orchestrates the 5-Agent Autonomous Swarm for Egyptian Thanaweya Biology.
class AgentSwarmOrchestrator {
  static final MasterBiologyEducatorAgent educatorAgent = MasterBiologyEducatorAgent();

  // AGENT 1: Master Biology Educator & Pedagogical Strategist (Async Gemini Integration)
  static Future<MasterEducatorPayload> fetchEducatorPayloadAsync(CurriculumTopic topic) {
    return educatorAgent.processTopic(topic);
  }

  // AGENT 1: Master Biology Educator & Pedagogical Strategist (Sync Baseline)
  static MasterEducatorPayload generateEducatorPayload(CurriculumTopic topic) {
    return educatorAgent.getFallbackPayload(topic);
  }

  // AGENT 2: Visual, 3D Anatomy & Spatial Learning Architect
  static SpatialVisualPayload generateSpatialPayload(CurriculumTopic topic) {
    final mermaidGraph = '''
graph TD
    A[Cellular Stimulus] --> B[Membrane Permeability Shift]
    B --> C[Active Ion Transport]
    C --> D[Physiological Response / Energy Release]
''';

    return SpatialVisualPayload(
      asciiSchematic: topic.asciiDiagram,
      mermaidFlowchart: mermaidGraph,
      dissectionSteps: [
        'Step 1: Identify outer anatomical boundaries and protective membranes.',
        'Step 2: Trace vascular bundles (xylem/phloem) or neuromuscular junctions.',
        'Step 3: Analyze sub-microscopic myofibrils, sarcomeres, or DNA nucleotide pairings.'
      ],
      youtubeSearchQueries: [
        '${topic.titleEnglish} 3D animation high school biology',
        'Egyptian Thanaweya Amma ${topic.titleEnglish} microscopic view'
      ],
    );
  }

  // AGENT 3: Assessment Specialist & Ministry Exam Simulator
  static AssessmentPayload generateAssessmentItem(CurriculumTopic topic) {
    return AssessmentPayload(
      question: 'Which of the following remains constant during the active sliding filament contraction of a skeletal muscle sarcomere?',
      choices: [
        'A) The length of the H-Zone',
        'B) The width of the I-Band',
        'C) The length of the A-Band',
        'D) The distance between two successive Z-lines'
      ],
      correctChoiceIndex: 2,
      rationale: 'According to Huxley sliding filament theory, the dark A-band represents the constant length of myosin thick filaments which do not shorten; only the actin filaments slide over them, causing I-band and H-zone shortening.',
      examTrapWarning: topic.examTraps.isNotEmpty ? topic.examTraps.first : 'Avoid confusing anatomical resting lengths with dynamic contraction changes.',
    );
  }

  // AGENT 4: Adaptive Learning & Evaluator Agent (Active Recall Conversion)
  static List<Flashcard> generateFlashcardsForTopic(CurriculumTopic topic) {
    return [
      Flashcard(
        id: '${topic.id}_fc_1',
        topicId: topic.id,
        frontQuestion: 'What is the primary difference between physiological support and structural support in plants?',
        backAnswer: 'Physiological support is temporary and depends on water entering the cell vacuole by osmosis (turgor pressure). Structural support is permanent and relies on the deposition of cellulose, lignin, suberin, or cutin on cell walls.',
        scientificKeywords: 'osmosis, turgor pressure, lignin, cellulose, permanent, temporary',
      ),
      Flashcard(
        id: '${topic.id}_fc_2',
        topicId: topic.id,
        frontQuestion: 'Why does the A-band length remain unchanged during muscle contraction?',
        backAnswer: 'Because the A-band corresponds to the entire length of the thick myosin filaments, which do not change length when actin filaments slide past them.',
        scientificKeywords: 'A-band, myosin filaments, sliding, unchanged length',
      ),
      Flashcard(
        id: '${topic.id}_fc_3',
        topicId: topic.id,
        frontQuestion: 'Explain the role of ATP in both contraction and relaxation of skeletal muscle.',
        backAnswer: 'ATP is required by myosin heads to attach to actin filaments (cross-bridge formation and power stroke) AND to detach from actin to allow relaxation. ATP absence results in muscle cramp/tetany.',
        scientificKeywords: 'ATP, cross-bridge detachment, actin, myosin, cramp',
      ),
    ];
  }

  // AGENT 5: Empathetic Student Companion & Progress Tracker
  static CompanionState evaluateStudentProgress({
    required double currentMastery,
    required int streakDays,
  }) {
    String sentiment;
    String intervention;
    String tier;

    if (currentMastery >= 0.85) {
      tier = 'Master (متقن متفوق)';
      sentiment = 'Confident & High-Yield';
      intervention = 'أداء استثنائي! أنت مستعد لاجتياز امتحانات الثانوية العامة التجريبية والرسمية بأعلى الدرجات. حافظ على تدريب التكرار المتباعد لتثبيت الشفرات الوراثية.';
    } else if (currentMastery >= 0.70) {
      tier = 'Competent (مستوى جيد جداً)';
      sentiment = 'Focused & Progressing';
      intervention = 'تقدم ممتاز ومستقر. ركز على فخاخ الامتحانات (Exam Traps) الخاصة بكل فصل لضمان الدرجة النهائية.';
    } else {
      tier = 'Developing (قيد التأسيس)';
      sentiment = 'Requires Focused Recall';
      intervention = 'لا تقلق من التحديات في البداية؛ راجع الشرح المبسط (ELI5) ثم ابدأ بحفظ بطاقات الاستذكار الفعالة خطوة بخطوة.';
    }

    return CompanionState(
      studentSentiment: sentiment,
      motivationalIntervention: intervention,
      overallCognitiveMastery: currentMastery,
      masteryTier: tier,
    );
  }
}
