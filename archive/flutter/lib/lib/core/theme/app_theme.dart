import 'package:flutter/material.dart';

class AppTheme {
  // Brand Colors
  static const Color deepNavy = Color(0xFF0F172A);
  static const Color bioTealPrimary = Color(0xFF0D9488);
  static const Color bioCyanAccent = Color(0xFF06B6D4);
  static const Color molecularPurple = Color(0xFF7C3AED);
  static const Color purpleAccent = Color(0xFFA855F7);
  static const Color examGold = Color(0xFFF59E0B);
  static const Color alertCoral = Color(0xFFEF4444);
  static const Color emeraldSuccess = Color(0xFF10B981);

  static ThemeData lightTheme = ThemeData(
    useMaterial3: true,
    brightness: Brightness.light,
    colorScheme: const ColorScheme.light(
      primary: bioTealPrimary,
      onPrimary: Colors.white,
      secondary: molecularPurple,
      onSecondary: Colors.white,
      surface: Color(0xFFF8FAFC),
      onSurface: Color(0xFF0F172A),
      error: alertCoral,
      onError: Colors.white,
    ),
    scaffoldBackgroundColor: const Color(0xFFF1F5F9),
    cardTheme: CardTheme(
      color: Colors.white,
      elevation: 1,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
    ),
    appBarTheme: const AppBarTheme(
      backgroundColor: Colors.white,
      foregroundColor: Color(0xFF0F172A),
      elevation: 0,
      centerTitle: false,
    ),
  );

  static ThemeData darkTheme = ThemeData(
    useMaterial3: true,
    brightness: Brightness.dark,
    colorScheme: const ColorScheme.dark(
      primary: bioCyanAccent,
      onPrimary: Colors.black,
      secondary: purpleAccent,
      onSecondary: Colors.black,
      surface: Color(0xFF1E293B),
      onSurface: Color(0xFFF8FAFC),
      error: alertCoral,
      onError: Colors.white,
    ),
    scaffoldBackgroundColor: const Color(0xFF0F172A),
    cardTheme: CardTheme(
      color: const Color(0xFF1E293B),
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
    ),
    appBarTheme: const AppBarTheme(
      backgroundColor: Color(0xFF0F172A),
      foregroundColor: Colors.white,
      elevation: 0,
      centerTitle: false,
    ),
  );
}
