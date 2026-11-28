import 'app_localizations.dart';

/// The translations for Arabic (`ar`).
class AppLocalizationsAr extends AppLocalizations {
  AppLocalizationsAr([String locale = 'ar']) : super(locale);

  @override
  String get appTitle => 'بيو ثانوية موبايل';

  @override
  String get homeTitle => 'الرئيسية';

  @override
  String get detailsTitle => 'التفاصيل';

  @override
  String get welcomeMessage => 'مرحبًا بك في بيو ثانوية';

  @override
  String get openDetail => 'فتح التفاصيل';

  @override
  String get back => 'رجوع';

  @override
  String get themeToggle => 'تبديل السمة';

  @override
  String itemLabel(int index) {
    return 'عنصر $index';
  }

  @override
  String get quizTitle => 'اختبار';

  @override
  String scoreLabel(int score, int total) {
    return 'النتيجة: $score/$total';
  }

  @override
  String get nextQuestion => 'التالي';

  @override
  String get finishQuiz => 'إنهاء';

  @override
  String get correctAnswer => 'صحيح!';

  @override
  String wrongAnswer(String answer) {
    return 'خطأ. الإجابة الصحيحة: $answer';
  }

  @override
  String get quizComplete => 'اكتمل الاختبار!';

  @override
  String get restartQuiz => 'إعادة';
}
