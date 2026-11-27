import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';

/// Holds the app-wide [ThemeMode] and persists the user's choice.
class ThemeModeNotifier extends StateNotifier<ThemeMode> {
  /// Starts on [ThemeMode.system] and loads the saved preference.
  ThemeModeNotifier() : super(ThemeMode.system) {
    _load();
  }

  static const _key = 'theme_mode';

  Future<void> _load() async {
    final prefs = await SharedPreferences.getInstance();
    final value = prefs.getString(_key);
    state = switch (value) {
      'light' => ThemeMode.light,
      'dark' => ThemeMode.dark,
      _ => ThemeMode.system,
    };
  }

  /// Switches between light and dark mode and persists the result.
  Future<void> toggle() async {
    final next = state == ThemeMode.dark ? ThemeMode.light : ThemeMode.dark;
    state = next;
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_key, next.name);
  }
}

/// Exposes the current [ThemeMode] to the widget tree.
final themeModeProvider =
    StateNotifierProvider<ThemeModeNotifier, ThemeMode>(
  (ref) => ThemeModeNotifier(),
);
