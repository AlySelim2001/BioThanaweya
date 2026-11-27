import 'package:biothanaweya_mobile/services/greeting_service.dart';
import 'package:biothanaweya_mobile/widgets/greeting_card.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mocktail/mocktail.dart';

class MockGreetingService extends Mock implements GreetingService {}

void main() {
  testWidgets('GreetingCard displays the greeting', (tester) async {
    final service = MockGreetingService();
    when(service.greet).thenReturn('Hello mock');

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(body: GreetingCard(service)),
      ),
    );

    expect(find.text('Hello mock'), findsOneWidget);
  });
}
