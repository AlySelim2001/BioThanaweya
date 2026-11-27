import 'package:flutter/material.dart';

/// Material 3 themes for the app.
class AppTheme {
  /// Light theme built from the teal seed colour.
  static ThemeData light = ThemeData(
    useMaterial3: true,
    colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
  );

  /// Dark theme mirroring [light] with dark brightness.
  static ThemeData dark = ThemeData(
    useMaterial3: true,
    colorScheme: ColorScheme.fromSeed(
      seedColor: Colors.teal,
      brightness: Brightness.dark,
    ),
  );
}
