# biothanaweya_mobile

Flutter client for BioThanaweya (Egyptian Thanaweya Amma biology revision + ministry-exam simulator).

## Setup & commands

Run all commands from `mobile/` with the Flutter SDK on `PATH` (the SDK lives at `/root/flutter` and is not on `PATH` by default):

```bash
export PATH=/root/flutter/bin:$PATH
```

### Install dependencies

```bash
flutter pub get
```

### Regenerate codegen (freezed / json_serializable)

```bash
dart run build_runner build --delete-conflicting-outputs
```

### Lint

```bash
flutter analyze
```

### Tests

```bash
flutter test
```

### Release build

```bash
flutter build apk --release
```

For a per-ABI split release build:

```bash
flutter build apk --release --split-per-abi
```

### Localizations

After editing `lib/l10n/app_en.arb` or `lib/l10n/app_ar.arb`:

```bash
flutter gen-l10n
```
