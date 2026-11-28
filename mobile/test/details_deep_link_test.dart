import 'package:biothanaweya_mobile/l10n/app_localizations.dart';
import 'package:biothanaweya_mobile/main.dart';
import 'package:biothanaweya_mobile/router/app_router.dart';
import 'package:biothanaweya_mobile/screens/home_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('deep link to /details/:id resolves the item via fallback lookup', (
    tester,
  ) async {
    await tester.pumpWidget(const ProviderScope(child: BioThanaweyaApp()));
    await tester.pumpAndSettle();

    final item = mockAppItems.first;
    appRouter.go('/details/${item.id}');
    await tester.pumpAndSettle();

    expect(find.text(item.title), findsAtLeastNWidgets(1));
    expect(find.text(item.description), findsOneWidget);

    final l10n = AppLocalizations.of(
      tester.element(find.byType(Scaffold).last),
    )!;
    expect(find.text(l10n.back), findsOneWidget);
  });

  testWidgets('deep link to an unknown id renders the id placeholder', (
    tester,
  ) async {
    await tester.pumpWidget(const ProviderScope(child: BioThanaweyaApp()));
    await tester.pumpAndSettle();

    appRouter.go('/details/does-not-exist');
    await tester.pumpAndSettle();

    final l10n = AppLocalizations.of(
      tester.element(find.byType(Scaffold).last),
    )!;
    expect(find.text('${l10n.detailsTitle}: does-not-exist'), findsOneWidget);
  });
}
