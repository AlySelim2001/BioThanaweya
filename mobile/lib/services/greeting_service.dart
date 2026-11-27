// Kept as an abstract class (rather than a typedef) so tests can mock it
// with mocktail, matching the GreetingService/MockGreetingService pattern.
// ignore_for_file: one_member_abstracts

/// Supplies the greeting text shown by the greeting card.
abstract class GreetingService {
  /// Returns the greeting to display.
  String greet();
}

/// Default implementation used by the running app.
class DefaultGreetingService implements GreetingService {
  @override
  String greet() => 'Hello from BioThanaweya';
}
