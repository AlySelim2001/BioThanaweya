import 'package:biothanaweya_mobile/core/theme.dart';
import 'package:biothanaweya_mobile/l10n/app_localizations.dart';
import 'package:biothanaweya_mobile/providers/theme_provider.dart';
import 'package:biothanaweya_mobile/router/app_router.dart';
import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

/// Entry point: boots the app inside a Riverpod [ProviderScope].
void main() {
  runApp(const ProviderScope(child: BioThanaweyaApp()));
}

/// Root widget: wires theme, router, and localization into a
/// [MaterialApp.router].
class BioThanaweyaApp extends ConsumerWidget {
  /// Creates the app root.
  const BioThanaweyaApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // The provider starts on ThemeMode.system and remembers the user's choice.
    final themeMode = ref.watch(themeModeProvider);
    return MaterialApp.router(
      onGenerateTitle: (context) => AppLocalizations.of(context)!.appTitle,
      theme: AppTheme.light,
      darkTheme: AppTheme.dark,
      themeMode: themeMode,
      routerConfig: appRouter,
      localizationsDelegates: const <LocalizationsDelegate<dynamic>>[
        AppLocalizations.delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      supportedLocales: AppLocalizations.supportedLocales,
    );
  }
}
