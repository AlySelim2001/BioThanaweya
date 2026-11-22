/// Agent identification and capability enum
enum AgentType {
  educator('Agent 1: Master Biology Educator', 'Dual-Layered Scaffolding & Bloom Progression'),
  spatialArchitect('Agent 2: Visual & 3D Anatomy Architect', 'ASCII Schematics, Mermaid & 3D Dissection'),
  assessmentSpecialist('Agent 3: Assessment Specialist', 'Ministry Exam Traps & High-Yield Items'),
  evaluator('Agent 4: Adaptive Learning Evaluator', 'Mark Scheme Rubric & Active Recall Generator'),
  empatheticCompanion('Agent 5: Empathetic Companion', 'Sentiment Monitoring & Mastery Tracking');

  final String title;
  final String roleDescription;
  const AgentType(this.title, this.roleDescription);
}

/// Agent 1 Scaffolding Output
class MasterEducatorPayload {
  final String eli5Arabic;
  final String deepTechnicalEnglish;
  final String bloomsLevel; // Remembering, Understanding, Applying, Analyzing, Evaluating
  final List<String> coreVocabulary;

  const MasterEducatorPayload({
    required this.eli5Arabic,
    required this.deepTechnicalEnglish,
    required this.bloomsLevel,
    required this.coreVocabulary,
  });
}

/// Agent 2 Spatial Output
class SpatialVisualPayload {
  final String asciiSchematic;
  final String mermaidFlowchart;
  final List<String> dissectionSteps;
  final List<String> youtubeSearchQueries;

  const SpatialVisualPayload({
    required this.asciiSchematic,
    required this.mermaidFlowchart,
    required this.dissectionSteps,
    required this.youtubeSearchQueries,
  });
}

/// Agent 3 Assessment Output
class AssessmentPayload {
  final String question;
  final List<String> choices;
  final int correctChoiceIndex;
  final String rationale;
  final String examTrapWarning;

  const AssessmentPayload({
    required this.question,
    required this.choices,
    required this.correctChoiceIndex,
    required this.rationale,
    required this.examTrapWarning,
  });
}

/// Agent 5 Companion State
class CompanionState {
  final String studentSentiment; // confident, overwhelmed, fatigued, curious
  final String motivationalIntervention;
  final double overallCognitiveMastery;
  final String masteryTier; // Novice, Developing, Competent, Master

  const CompanionState({
    required this.studentSentiment,
    required this.motivationalIntervention,
    required this.overallCognitiveMastery,
    required this.masteryTier,
  });
}

/// Student mastery levels for context-aware agent interactions
enum StudentMasteryLevel {
  novice('مبتدئ / Novice'),
  developing('قيد التأسيس / Developing'),
  competent('متمكن / Competent'),
  master('متقن متفوق / Master');

  final String label;
  const StudentMasteryLevel(this.label);
}

