import 'package:flutter/material.dart';
import '../../../../core/constants/curriculum_data.dart';
import '../../../../core/theme/app_theme.dart';
import '../../agent_swarm.dart';
import '../../models/agent_models.dart';
import '../../../exam_simulator/presentation/screens/exam_simulator_screen.dart';

class TopicDetailScreen extends StatefulWidget {
  final CurriculumTopic topic;

  const TopicDetailScreen({super.key, required this.topic});

  @override
  State<TopicDetailScreen> createState() => _TopicDetailScreenState();
}

class _TopicDetailScreenState extends State<TopicDetailScreen> {
  late MasterEducatorPayload _educatorPayload;
  bool _isGeneratingWithAi = false;

  @override
  void initState() {
    super.initState();
    _educatorPayload = AgentSwarmOrchestrator.generateEducatorPayload(widget.topic);
  }

  Future<void> _regenerateWithGemini() async {
    setState(() => _isGeneratingWithAi = true);
    try {
      final newPayload = await AgentSwarmOrchestrator.fetchEducatorPayloadAsync(widget.topic);
      if (mounted) {
        setState(() {
          _educatorPayload = newPayload;
          _isGeneratingWithAi = false;
        });
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Agent 1 (Master Biology Educator) generated fresh pedagogical breakdowns via Gemini!'),
            backgroundColor: AppTheme.bioTealPrimary,
          ),
        );
      }
    } catch (e) {
      if (mounted) {
        setState(() => _isGeneratingWithAi = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final spatialPayload = AgentSwarmOrchestrator.generateSpatialPayload(widget.topic);
    final assessmentItem = AgentSwarmOrchestrator.generateAssessmentItem(widget.topic);

    return DefaultTabController(
      length: 3,
      child: Scaffold(
        appBar: AppBar(
          title: Text(
            widget.topic.titleArabic,
            style: const TextStyle(fontSize: 15, fontWeight: FontWeight.bold),
          ),
          bottom: const TabBar(
            indicatorColor: AppTheme.bioTealPrimary,
            labelColor: AppTheme.bioTealPrimary,
            unselectedLabelColor: Colors.grey,
            tabs: [
              Tab(icon: Icon(Icons.school), text: 'Agent 1: Educator'),
              Tab(icon: Icon(Icons.view_in_ar), text: 'Agent 2: 3D/Visual'),
              Tab(icon: Icon(Icons.quiz), text: 'Agent 3: Traps & MCQ'),
            ],
          ),
          actions: [
            IconButton(
              icon: const Icon(Icons.assignment, color: AppTheme.examGold),
              tooltip: 'Exam Simulator (Agent 4)',
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => ExamSimulatorScreen(topic: widget.topic),
                  ),
                );
              },
            )
          ],
        ),
        body: TabBarView(
          children: [
            // TAB 1: Master Biology Educator
            SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Agent 1 Header Badge & AI Generator Trigger
                  Container(
                    padding: const EdgeInsets.all(12),
                    decoration: BoxDecoration(
                      color: AppTheme.bioTealPrimary.withValues(alpha: 0.08),
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(color: AppTheme.bioTealPrimary.withValues(alpha: 0.25)),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Row(
                          children: [
                            Icon(Icons.auto_awesome, color: AppTheme.bioTealPrimary, size: 20),
                            SizedBox(width: 8),
                            Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  'Agent 1: Master Biology Educator',
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                                ),
                                Text(
                                  'Dual-Layered Scaffolding (Gemini API Integration)',
                                  style: TextStyle(fontSize: 11, color: AppTheme.bioTealPrimary),
                                ),
                              ],
                            ),
                          ],
                        ),
                        if (_isGeneratingWithAi)
                          const SizedBox(
                            width: 20,
                            height: 20,
                            child: CircularProgressIndicator(strokeWidth: 2, color: AppTheme.bioTealPrimary),
                          )
                        else
                          IconButton(
                            onPressed: _regenerateWithGemini,
                            icon: const Icon(Icons.refresh, color: AppTheme.bioTealPrimary),
                            tooltip: 'Regenerate Scaffolding with Gemini API',
                          ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 16),

                  _buildSectionHeader('ELI5: الشرح المبسط والتشبيه اليومي', Icons.child_care, AppTheme.bioTealPrimary),
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(14),
                    decoration: BoxDecoration(
                      color: AppTheme.bioTealPrimary.withValues(alpha: 0.06),
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(color: AppTheme.bioTealPrimary.withValues(alpha: 0.2)),
                    ),
                    child: Text(
                      _educatorPayload.eli5Arabic,
                      style: const TextStyle(fontSize: 13, height: 1.5),
                      textAlign: TextAlign.right,
                    ),
                  ),
                  const SizedBox(height: 16),

                  _buildSectionHeader('Deep Technical Breakdown (Biophysics & Mechanisms)', Icons.biotech, AppTheme.molecularPurple),
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(14),
                    decoration: BoxDecoration(
                      color: AppTheme.molecularPurple.withValues(alpha: 0.06),
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(color: AppTheme.molecularPurple.withValues(alpha: 0.2)),
                    ),
                    child: Text(
                      _educatorPayload.deepTechnicalEnglish,
                      style: const TextStyle(fontSize: 13, height: 1.5),
                    ),
                  ),
                  const SizedBox(height: 16),

                  Row(
                    children: [
                      const Icon(Icons.psychology, color: AppTheme.examGold, size: 18),
                      const SizedBox(width: 8),
                      Text(
                        'Bloom\'s Taxonomy Level: ${_educatorPayload.bloomsLevel}',
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                      ),
                    ],
                  ),
                  const SizedBox(height: 12),

                  if (_educatorPayload.coreVocabulary.isNotEmpty) ...[
                    const Text(
                      'Core Scientific Vocabulary:',
                      style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                    ),
                    const SizedBox(height: 6),
                    Wrap(
                      spacing: 8,
                      runSpacing: 6,
                      children: _educatorPayload.coreVocabulary
                          .map((vocab) => Chip(
                                label: Text(vocab, style: const TextStyle(fontSize: 11)),
                                backgroundColor: Colors.teal.shade50,
                                side: BorderSide(color: AppTheme.bioTealPrimary.withValues(alpha: 0.3)),
                              ))
                          .toList(),
                    ),
                  ],
                ],
              ),
            ),

            // TAB 2: Spatial & 3D Anatomy Architect
            SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _buildSectionHeader('ASCII Architectural Schematic Diagram', Icons.code, AppTheme.deepNavy),
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(12),
                    decoration: BoxDecoration(
                      color: const Color(0xFF1E293B),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: SelectableText(
                      spatialPayload.asciiSchematic,
                      style: const TextStyle(
                        fontFamily: 'monospace',
                        color: Color(0xFF38BDF8),
                        fontSize: 11,
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  _buildSectionHeader('Anatomical Dissection Protocol', Icons.science, AppTheme.bioCyanAccent),
                  ...spatialPayload.dissectionSteps.map(
                    (step) => Padding(
                      padding: const EdgeInsets.symmetric(vertical: 4.0),
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Icon(Icons.check_circle_outline, size: 16, color: AppTheme.emeraldSuccess),
                          const SizedBox(width: 8),
                          Expanded(
                            child: Text(step, style: const TextStyle(fontSize: 12)),
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  _buildSectionHeader('High-Yield 3D Animation Search Prompts', Icons.ondemand_video, AppTheme.alertCoral),
                  ...spatialPayload.youtubeSearchQueries.map(
                    (query) => Container(
                      margin: const EdgeInsets.symmetric(vertical: 4),
                      padding: const EdgeInsets.all(10),
                      decoration: BoxDecoration(
                        color: Colors.grey.shade100,
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Row(
                        children: [
                          const Icon(Icons.search, size: 16, color: Colors.grey),
                          const SizedBox(width: 8),
                          Expanded(
                            child: Text(query, style: const TextStyle(fontSize: 11, fontWeight: FontWeight.w500)),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),

            // TAB 3: Assessment Specialist & Ministry Exam Traps
            SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _buildSectionHeader('Thanaweya Amma Exam Trap Warning', Icons.warning_amber, AppTheme.alertCoral),
                  Container(
                    padding: const EdgeInsets.all(12),
                    decoration: BoxDecoration(
                      color: AppTheme.alertCoral.withValues(alpha: 0.1),
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(color: AppTheme.alertCoral.withValues(alpha: 0.4)),
                    ),
                    child: Text(
                      assessmentItem.examTrapWarning,
                      style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w600, color: Color(0xFF991B1B)),
                    ),
                  ),
                  const SizedBox(height: 18),
                  _buildSectionHeader('High-Yield Ministry Practice Item', Icons.help_outline, AppTheme.bioTealPrimary),
                  Text(
                    assessmentItem.question,
                    style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                  ),
                  const SizedBox(height: 10),
                  ...assessmentItem.choices.asMap().entries.map(
                    (entry) {
                      final isCorrect = entry.key == assessmentItem.correctChoiceIndex;
                      return Container(
                        margin: const EdgeInsets.symmetric(vertical: 4),
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: isCorrect ? AppTheme.emeraldSuccess.withValues(alpha: 0.15) : Colors.transparent,
                          borderRadius: BorderRadius.circular(8),
                          border: Border.all(
                            color: isCorrect ? AppTheme.emeraldSuccess : Colors.grey.shade300,
                          ),
                        ),
                        child: Row(
                          children: [
                            Icon(
                              isCorrect ? Icons.check_circle : Icons.radio_button_unchecked,
                              size: 16,
                              color: isCorrect ? AppTheme.emeraldSuccess : Colors.grey,
                            ),
                            const SizedBox(width: 8),
                            Expanded(
                              child: Text(
                                entry.value,
                                style: TextStyle(
                                  fontSize: 12,
                                  fontWeight: isCorrect ? FontWeight.bold : FontWeight.normal,
                                ),
                              ),
                            ),
                          ],
                        ),
                      );
                    },
                  ),
                  const SizedBox(height: 12),
                  Container(
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      color: Colors.blue.shade50,
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Text(
                      'Rationale: ${assessmentItem.rationale}',
                      style: TextStyle(fontSize: 11, color: Colors.blue.shade900),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSectionHeader(String title, IconData icon, Color color) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8.0),
      child: Row(
        children: [
          Icon(icon, size: 18, color: color),
          const SizedBox(width: 8),
          Text(
            title,
            style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: color),
          ),
        ],
      ),
    );
  }
}
