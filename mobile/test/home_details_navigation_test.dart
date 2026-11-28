import 'package:biothanaweya_mobile/l10n/app_localizations.dart';
import 'package:biothanaweya_mobile/main.dart';
import 'package:biothanaweya_mobile/screens/home_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('tapping a home item opens its details screen', (tester) async {
    await tester.pumpWidget(const ProviderScope(child: BioThanaweyaApp()));
    await tester.pumpAndSettle();

    final first = mockAppItems.first;
    expect(find.text(first.title), findsOneWidget);

    await tester.tap(find.widgetWithText(ListTile, first.title));
    await tester.pumpAndSettle();

    // The title now appears on both the home card and the details screen.
    expect(find.text(first.title), findsAtLeastNWidgets(1));
    expect(find.text(first.description), findsOneWidget);
    // The back action is rendered as an ElevatedButton labelled "Back".
    final l10n = AppLocalizations.of(
      tester.element(find.byType(Scaffold).last),
    )!;
    expect(find.text(l10n.back), findsOneWidget);
  });
}
