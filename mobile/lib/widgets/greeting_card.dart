import 'package:biothanaweya_mobile/services/greeting_service.dart';
import 'package:flutter/material.dart';

/// A card that renders the greeting from a [GreetingService].
class GreetingCard extends StatelessWidget {
  /// Creates a greeting card backed by [service].
  const GreetingCard(this.service, {super.key});

  /// Service providing the greeting text.
  final GreetingService service;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Text(service.greet()),
      ),
    );
  }
}
