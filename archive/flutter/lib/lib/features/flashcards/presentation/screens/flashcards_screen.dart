import 'package:flutter/material.dart';
import '../../../../core/constants/curriculum_data.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../biology_agents/agent_swarm.dart';
import '../../models/flashcard_model.dart';
import '../../sm2_engine.dart';

class FlashcardsScreen extends StatefulWidget {
  final CurriculumTopic topic;

  const FlashcardsScreen({super.key, required this.topic});

  @override
  State<FlashcardsScreen> createState() => _FlashcardsScreenState();
}

class _FlashcardsScreenState extends State<FlashcardsScreen> {
  late List<Flashcard> _cards;
  int _currentIndex = 0;
  bool _isFlipped = false;

  @override
  void initState() {
    super.initState();
    _cards = AgentSwarmOrchestrator.generateFlashcardsForTopic(widget.topic);
  }

  void _rateCard(SM2Quality quality) {
    setState(() {
      final updated = SM2Engine.calculateNextReview(_cards[_currentIndex], quality);
      _cards[_currentIndex] = updated;
      _isFlipped = false;
      if (_currentIndex < _cards.length - 1) {
        _currentIndex++;
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Completed all due flashcards for this topic! SM-2 intervals updated.'),
            backgroundColor: AppTheme.emeraldSuccess,
          ),
        );
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final currentCard = _cards[_currentIndex];

    return Scaffold(
      appBar: AppBar(
        title: Text(
          'SM-2 Flashcards: ${widget.topic.titleArabic}',
          style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            // Progress Indicator
            LinearProgressIndicator(
              value: (_currentIndex + 1) / _cards.length,
              backgroundColor: Colors.grey.shade200,
              color: AppTheme.bioTealPrimary,
            ),
            const SizedBox(height: 8),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'Card ${_currentIndex + 1} of ${_cards.length}',
                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                ),
                Text(
                  'Ease Factor: ${currentCard.easeFactor.toStringAsFixed(2)} | Interval: ${currentCard.intervalDays}d',
                  style: const TextStyle(fontSize: 11, color: Colors.grey),
                ),
              ],
            ),
            const SizedBox(height: 20),

            // Flip Card
            Expanded(
              child: GestureDetector(
                onTap: () => setState(() => _isFlipped = !_isFlipped),
                child: AnimatedSwitcher(
                  duration: const Duration(milliseconds: 300),
                  child: Container(
                    key: ValueKey<bool>(_isFlipped),
                    width: double.infinity,
                    padding: const EdgeInsets.all(24),
                    decoration: BoxDecoration(
                      color: _isFlipped ? const Color(0xFF0F172A) : Colors.white,
                      borderRadius: BorderRadius.circular(20),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withValues(alpha: 0.08),
                          blurRadius: 10,
                          offset: const Offset(0, 4),
                        ),
                      ],
                      border: Border.all(
                        color: _isFlipped ? AppTheme.bioCyanAccent : AppTheme.bioTealPrimary.withValues(alpha: 0.3),
                        width: 2,
                      ),
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                          decoration: BoxDecoration(
                            color: _isFlipped ? AppTheme.bioCyanAccent.withValues(alpha: 0.2) : AppTheme.bioTealPrimary.withValues(alpha: 0.1),
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: Text(
                            _isFlipped ? 'ANSWER (RECALL)' : 'QUESTION (PROMPT)',
                            style: TextStyle(
                              fontSize: 11,
                              fontWeight: FontWeight.bold,
                              color: _isFlipped ? AppTheme.bioCyanAccent : AppTheme.bioTealPrimary,
                            ),
                          ),
                        ),
                        const SizedBox(height: 20),
                        Text(
                          _isFlipped ? currentCard.backAnswer : currentCard.frontQuestion,
                          style: TextStyle(
                            fontSize: 15,
                            height: 1.5,
                            fontWeight: FontWeight.w600,
                            color: _isFlipped ? Colors.white : const Color(0xFF0F172A),
                          ),
                          textAlign: TextAlign.center,
                        ),
                        if (_isFlipped) ...[
                          const SizedBox(height: 24),
                          Container(
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(
                              color: Colors.white.withValues(alpha: 0.1),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Text(
                              'Keywords: ${currentCard.scientificKeywords}',
                              style: const TextStyle(fontSize: 11, color: Color(0xFF38BDF8)),
                              textAlign: TextAlign.center,
                            ),
                          ),
                        ],
                        const SizedBox(height: 20),
                        Text(
                          _isFlipped ? 'Tap to flip back' : 'Tap to reveal answer',
                          style: TextStyle(fontSize: 11, color: Colors.grey.shade500),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
            const SizedBox(height: 20),

            // SM-2 Rating Controls
            if (_isFlipped) ...[
              const Text(
                'Rate your recall (SuperMemo SM-2)',
                style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              Row(
                children: [
                  _buildSm2Button('Again (1)', AppTheme.alertCoral, () => _rateCard(SM2Quality.again)),
                  const SizedBox(width: 8),
                  _buildSm2Button('Hard (3)', AppTheme.examGold, () => _rateCard(SM2Quality.hard)),
                  const SizedBox(width: 8),
                  _buildSm2Button('Good (4)', AppTheme.bioTealPrimary, () => _rateCard(SM2Quality.good)),
                  const SizedBox(width: 8),
                  _buildSm2Button('Easy (5)', AppTheme.emeraldSuccess, () => _rateCard(SM2Quality.easy)),
                ],
              ),
            ] else ...[
              ElevatedButton.icon(
                onPressed: () => setState(() => _isFlipped = true),
                icon: const Icon(Icons.visibility),
                label: const Text('Show Answer'),
                style: ElevatedButton.styleFrom(
                  minimumSize: const Size.fromHeight(48),
                  backgroundColor: AppTheme.bioTealPrimary,
                  foregroundColor: Colors.white,
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildSm2Button(String label, Color color, VoidCallback onTap) {
    return Expanded(
      child: ElevatedButton(
        onPressed: onTap,
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(vertical: 12),
          backgroundColor: color,
          foregroundColor: Colors.white,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
        ),
        child: Text(label, style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold)),
      ),
    );
  }
}
