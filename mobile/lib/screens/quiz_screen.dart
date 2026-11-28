import 'package:biothanaweya_mobile/l10n/app_localizations.dart';
import 'package:biothanaweya_mobile/models/quiz_question.dart';
import 'package:flutter/material.dart';

/// Placeholder quiz questions until a real question bank is wired in.
const List<QuizQuestion> mockQuizQuestions = <QuizQuestion>[
  QuizQuestion(
    question: 'Which organelle is responsible for photosynthesis?',
    options: <String>['Mitochondria', 'Chloroplast', 'Nucleus', 'Ribosome'],
    correctIndex: 1,
  ),
  QuizQuestion(
    question: 'DNA is composed of which sugar?',
    options: <String>['Ribose', 'Deoxyribose', 'Glucose', 'Fructose'],
    correctIndex: 1,
  ),
  QuizQuestion(
    question: 'Which blood cells produce antibodies?',
    options: <String>['T cells', 'B cells', 'Red blood cells', 'Platelets'],
    correctIndex: 1,
  ),
];

/// A simple multiple-choice quiz screen.
class QuizScreen extends StatefulWidget {
  /// Creates a [QuizScreen].
  const QuizScreen({super.key});

  @override
  State<QuizScreen> createState() => _QuizScreenState();
}

class _QuizScreenState extends State<QuizScreen> {
  int _currentIndex = 0;
  int _score = 0;
  int? _selectedIndex;
  bool _answered = false;
  bool _finished = false;

  QuizQuestion get _current => mockQuizQuestions[_currentIndex];

  void _select(int index) {
    if (_answered) return;
    setState(() {
      _selectedIndex = index;
      _answered = true;
      if (index == _current.correctIndex) {
        _score += 1;
      }
    });
  }

  void _next() {
    if (_currentIndex + 1 >= mockQuizQuestions.length) {
      setState(() => _finished = true);
      return;
    }
    setState(() {
      _currentIndex += 1;
      _selectedIndex = null;
      _answered = false;
    });
  }

  void _restart() {
    setState(() {
      _currentIndex = 0;
      _score = 0;
      _selectedIndex = null;
      _answered = false;
      _finished = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    final l10n = AppLocalizations.of(context)!;
    return Scaffold(
      appBar: AppBar(title: Text(l10n.quizTitle)),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: _finished
            ? _buildResult(context, l10n)
            : _buildQuestion(context, l10n),
      ),
    );
  }

  Widget _buildQuestion(BuildContext context, AppLocalizations l10n) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        Text(
          l10n.scoreLabel(_score, mockQuizQuestions.length),
          style: Theme.of(context).textTheme.titleMedium,
        ),
        const SizedBox(height: 16),
        Text(
          _current.question,
          style: Theme.of(context).textTheme.headlineSmall,
        ),
        const SizedBox(height: 16),
        ...List<Widget>.generate(_current.options.length, (i) {
          final isCorrect = i == _current.correctIndex;
          final isSelected = i == _selectedIndex;
          // Use theme colour roles so the feedback works in dark mode too.
          final scheme = Theme.of(context).colorScheme;
          Color? color;
          if (_answered) {
            if (isCorrect) {
              color = scheme.secondaryContainer;
            } else if (isSelected) {
              color = scheme.errorContainer;
            }
          }
          return Card(
            color: color,
            child: ListTile(
              title: Text(_current.options[i]),
              onTap: () => _select(i),
            ),
          );
        }),
        const SizedBox(height: 16),
        if (_answered)
          Text(
            _selectedIndex == _current.correctIndex
                ? l10n.correctAnswer
                : l10n.wrongAnswer(
                    _current.options[_current.correctIndex],
                  ),
            style: Theme.of(context).textTheme.bodyLarge,
          ),
        const Spacer(),
        if (_answered)
          ElevatedButton(
            onPressed: _next,
            child: Text(
              _currentIndex + 1 >= mockQuizQuestions.length
                  ? l10n.finishQuiz
                  : l10n.nextQuestion,
            ),
          ),
      ],
    );
  }

  Widget _buildResult(BuildContext context, AppLocalizations l10n) {
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          Text(
            l10n.quizComplete,
            style: Theme.of(context).textTheme.headlineMedium,
          ),
          const SizedBox(height: 12),
          Text(l10n.scoreLabel(_score, mockQuizQuestions.length)),
          const SizedBox(height: 24),
          ElevatedButton(onPressed: _restart, child: Text(l10n.restartQuiz)),
        ],
      ),
    );
  }
}
