import '../../../../core/constants/curriculum_data.dart';
import '../../models/agent_models.dart';

/// Top-level contract for any autonomous agent in the Scholar Sidekick biology swarm.
abstract class BiologyAgent {
  AgentType get agentType;
  String get name;
  String get role;
  bool get isInitialized;

  Future<void> initialize();
  Future<dynamic> processTopic(
    CurriculumTopic topic, {
    Map<String, dynamic>? additionalContext,
  });
}
