# Project knowledge — BioThanaweya Mobile

## Scope / boundaries

- This file describes **`mobile/`** only: the Flutter app `biothanaweya_mobile`, the mobile client of BioThanaweya (Egyptian Thanaweya Amma biology revision + ministry-exam simulator).
- **Do not touch anything outside `mobile/`.** The repository root holds a separate React 19 + Vite + Capacitor PWA (`src/`, `package.json`, `server.ts`, `android/`). It is a different codebase with its own tooling; treat it as read-only context.
- All Flutter/Dart commands run with `mobile/` as the working directory.
- Never hand-create `android/` or `ios/` scaffolding; let `flutter create` generate them.
- Current app id / namespace: `com.biothanaweya.app`.

## Layout

```
mobile/
├── lib/
│   ├── main.dart              # runApp(ProviderScope(child: BioThanaweyaApp)) — MaterialApp.router
│   ├── core/theme.dart        # AppTheme.light / AppTheme.dark (Material 3, teal seed)
│   ├── router/app_router.dart # single go_router instance: "/", "/details/:id", "/quiz"
│   ├── providers/             # Riverpod providers (theme_provider.dart: ThemeMode + SharedPreferences)
│   ├── screens/               # full-page widgets (home_screen, details_screen, quiz_screen)
│   ├── widgets/               # reusable widgets (greeting_card.dart)
│   ├── services/              # abstract service + default impl (greeting_service.dart)
│   ├── models/                # freezed + json_serializable models (item.dart, app_item.dart); plain model quiz_question.dart
│   └── l10n/                  # ARB sources *and* generated localization Dart (committed)
├── test/                      # widget/unit tests (mocktail mocks)
├── l10n.yaml                  # gen-l10n config
└── analysis_options.yaml      # includes package:very_good_analysis
```

Data flow: `main.dart` → `MaterialApp.router` (theme from `themeModeProvider`, router from `appRouter`, delegates from `AppLocalizations`) → screens read strings via `AppLocalizations.of(context)!` and state via `ref.watch`.

Mock content: `mockAppItems` (top-level const in `lib/screens/home_screen.dart`) feeds the home list; `/details/:id` reads the item from `state.extra`, falling back to a lookup in that same list so deep links work. `mockQuizQuestions` (top-level const in `lib/screens/quiz_screen.dart`) feeds the `/quiz` screen, which is stateful and keeps the score locally. Replace both with a real data source later.

## Commands (run inside `mobile/`)

```bash
flutter pub get                                          # install deps
flutter gen-l10n                                         # regenerate lib/l10n/app_localizations*.dart after ARB edits
dart run build_runner build --delete-conflicting-outputs # regenerate *.freezed.dart / *.g.dart
flutter analyze                                          # lint (very_good_analysis)
flutter test                                             # unit + widget tests
flutter run                                              # debug on device/emulator
flutter build apk --debug                                # Android build (iOS needs macOS)
```

> Environment gotcha: the Flutter SDK **3.22.3 (Dart 3.4.4) lives at `/root/flutter` and is not on `PATH`**. `dart` on PATH is a standalone Termux Dart, so always use the SDK's tools:
>
> ```bash
> export PATH=/root/flutter/bin:$PATH
> ```
>
> The "running flutter as root" banner is harmless here. `flutter analyze`, `flutter test`, `flutter pub get`, `gen-l10n` and `build_runner` all work; there is no device/emulator and Chrome is absent, so no visual run (`flutter run` / screenshots).

## Conventions

- **State management:** Riverpod (`ConsumerWidget` / `ConsumerStatefulWidget` + `ref.watch`/`ref.read`). No `setState` for app-wide state; add a provider under `lib/providers/`.
- **Navigation:** go_router. Add new routes to `app_router.dart`; navigate with `context.go(...)`, not `Navigator.push` (the detail screen's back button uses `Navigator.of(context).pop()` to return).
- **Models:** `@freezed` + `json_serializable`, `part 'x.freezed.dart'; part 'x.g.dart';`. Never edit generated `*.freezed.dart` / `*.g.dart` by hand — regenerate.
- **Services:** define an abstract interface plus a `Default*` implementation so tests can mock it with `mocktail` (see `GreetingService` / `MockGreetingService`).
- **Theming:** Material 3, `ColorScheme.fromSeed(seedColor: Colors.teal)`; dark mode mirrors it with `Brightness.dark`. User choice is persisted in `SharedPreferences` under key `theme_mode` (`light`/`dark`/`system`).
- **Lint:** `package:very_good_analysis` is strict — single quotes, mandatory trailing commas, `const` constructors, explicit types on public APIs, `super.key`. Fix violations instead of suppressing them.
- **Tests:** `flutter_test` + `mocktail`; widget tests wrap the widget in `MaterialApp(home: Scaffold(...))`. `integration_test` is available as a dev dependency.

## Localization gotchas

- Strings are **only** in `lib/l10n/app_en.arb` (template, with `@key` description metadata) and `lib/l10n/app_ar.arb`. Add every new key to **both** files.
- `l10n.yaml` uses `synthetic-package: false` + `output-dir: lib/l10n`, so the generated `app_localizations*.dart` files **are committed and imported by path** (`package:biothanaweya_mobile/l10n/app_localizations.dart`). Run `flutter gen-l10n` and commit the regenerated files.
- `lib/l10n/untranslated.txt` is a generated report of missing translations; regenerate it, don't hand-edit.
- Supported locales come from `AppLocalizations.supportedLocales`; Arabic is a first-class locale (RTL), so avoid hard-coded left/right padding and keep text in `EdgeInsetsDirectional`/`AlignmentDirectional` where practical.

## Dependency set (pubspec.yaml)

State/nav: `flutter_riverpod`, `go_router`. Networking: `dio`, `connectivity_plus`, `cached_network_image`. Storage/security: `shared_preferences`, `flutter_secure_storage`. Codegen: `freezed`, `json_serializable`, `build_runner`. Platform: `url_launcher`, `package_info_plus`. i18n: `flutter_localizations`, `intl`. SDK constraint `>=3.4.4 <4.0.0`; `flutter: generate: true`.
