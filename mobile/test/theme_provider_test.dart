import 'package:biothanaweya_mobile/providers/theme_provider.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  test('loads the persisted theme mode', () async {
    SharedPreferences.setMockInitialValues(<String, Object>{
      'theme_mode': 'dark',
    });

    final notifier = ThemeModeNotifier();
    expect(notifier.state, ThemeMode.system);

    await Future<void>.delayed(Duration.zero);
    expect(notifier.state, ThemeMode.dark);
  });

  test('toggle flips between light and dark and persists', () async {
    SharedPreferences.setMockInitialValues(<String, Object>{});

    final notifier = ThemeModeNotifier();
    await Future<void>.delayed(Duration.zero);
    expect(notifier.state, ThemeMode.system);

    await notifier.toggle();
    expect(notifier.state, ThemeMode.dark);

    final prefs = await SharedPreferences.getInstance();
    expect(prefs.getString('theme_mode'), 'dark');

    await notifier.toggle();
    expect(notifier.state, ThemeMode.light);
    expect(prefs.getString('theme_mode'), 'light');
  });
}
