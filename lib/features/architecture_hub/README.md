# Architecture Hub module

The module follows Clean Architecture dependency direction: presentation depends on domain contracts and use cases; data implements domain contracts and depends on the local SQLite source. Domain contains no Flutter or storage implementation imports.

```text
architecture_hub/
  data/
    datasources/architecture_local_data_source.dart
    repositories/architecture_hub_repository_impl.dart
  domain/
    entities/architecture_metrics.dart
    repositories/architecture_hub_repository.dart
    usecases/load_architecture_metrics.dart
    usecases/run_local_database_benchmark.dart
  presentation/
    bloc/                         # Riverpod notifier/controller
    screens/app_architecture_hub.dart
    widgets/                      # reusable presentation widgets
```

The app's database service is an infrastructure/data-source component. Feature repositories adapt it to domain contracts; widgets never issue SQL or invoke the database service directly. Core curriculum, chapter metadata, exam attempts, and textbook metadata remain locally queryable without a network.
