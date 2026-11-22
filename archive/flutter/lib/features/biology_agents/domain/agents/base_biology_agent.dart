import '../../../../core/constants/curriculum_data.dart';
import '../../models/agent_models.dart';

import 'biology_agent.dart';

/// Abstract base contract for all autonomous agents in the smarts.bio swarm.
abstract class BaseBiologyAgent<T> implements BiologyAgent {
  final AgentType agentType;
  final String name;
  final String role;
  bool _isInitialized = false;

  BaseBiologyAgent({
    required this.agentType,
    required this.name,
    required this.role,
  });

  bool get isInitialized => _isInitialized;

  /// Initializes local caches, API connections, or prerequisite models.
  Future<void> initialize() async {
    _isInitialized = true;
  }

  /// System-level persona instruction defining the agent's behavior and scope.
  String get systemPersonaInstruction;

  /// Primary execution method for processing a Thanaweya Amma curriculum topic.
  Future<T> processTopic(
    CurriculumTopic topic, {
    Map<String, dynamic>? additionalContext,
  });

  /// Offline or error fallback generator ensuring high-performance offline resilience.
  T getFallbackPayload(CurriculumTopic topic);
}
