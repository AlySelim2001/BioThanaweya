import 'app_localizations.dart';

/// The translations for English (`en`).
class AppLocalizationsEn extends AppLocalizations {
  AppLocalizationsEn([String locale = 'en']) : super(locale);

  @override
  String get appTitle => 'BioThanaweya Mobile';

  @override
  String get homeTitle => 'Home';

  @override
  String get detailsTitle => 'Details';

  @override
  String get welcomeMessage => 'Welcome to BioThanaweya';

  @override
  String get openDetail => 'Open Detail';

  @override
  String get back => 'Back';

  @override
  String get themeToggle => 'Toggle theme';

  @override
  String itemLabel(int index) {
    return 'Item $index';
  }

  @override
  String get quizTitle => 'Quiz';

  @override
  String scoreLabel(int score, int total) {
    return 'Score: $score/$total';
  }

  @override
  String get nextQuestion => 'Next';

  @override
  String get finishQuiz => 'Finish';

  @override
  String get correctAnswer => 'Correct!';

  @override
  String wrongAnswer(String answer) {
    return 'Wrong. Correct answer: $answer';
  }

  @override
  String get quizComplete => 'Quiz complete!';

  @override
  String get restartQuiz => 'Restart';
}
