import 'package:biothanaweya_mobile/l10n/app_localizations.dart';
import 'package:biothanaweya_mobile/screens/quiz_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('completing the quiz shows the result page and restarts', (
    tester,
  ) async {
    await tester.pumpWidget(
      const MaterialApp(
        localizationsDelegates: <LocalizationsDelegate<dynamic>>[
          AppLocalizations.delegate,
          GlobalMaterialLocalizations.delegate,
          GlobalWidgetsLocalizations.delegate,
          GlobalCupertinoLocalizations.delegate,
        ],
        supportedLocales: AppLocalizations.supportedLocales,
        home: QuizScreen(),
      ),
    );
    await tester.pumpAndSettle();

    // Answer all three questions correctly, advancing each time.
    await tester.tap(find.text('Chloroplast'));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Next'));
    await tester.pumpAndSettle();

    await tester.tap(find.text('Deoxyribose'));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Next'));
    await tester.pumpAndSettle();

    await tester.tap(find.text('B cells'));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Finish'));
    await tester.pumpAndSettle();

    expect(find.text('Quiz complete!'), findsOneWidget);
    expect(find.text('Score: 3/3'), findsOneWidget);

    // Restart returns to the first question.
    await tester.tap(find.text('Restart'));
    await tester.pumpAndSettle();
    expect(
      find.text('Which organelle is responsible for photosynthesis?'),
      findsOneWidget,
    );
  });
}
