import 'package:biothanaweya_mobile/l10n/app_localizations.dart';
import 'package:biothanaweya_mobile/screens/quiz_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('quiz screen shows first question and advances', (tester) async {
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

    expect(
      find.text('Which organelle is responsible for photosynthesis?'),
      findsOneWidget,
    );

    await tester.tap(find.text('Chloroplast'));
    await tester.pumpAndSettle();
    expect(find.text('Correct!'), findsOneWidget);

    await tester.tap(find.text('Next'));
    await tester.pumpAndSettle();
    expect(find.text('DNA is composed of which sugar?'), findsOneWidget);
  });
}
