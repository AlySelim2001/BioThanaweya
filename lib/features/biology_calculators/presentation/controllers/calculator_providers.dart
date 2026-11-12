import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../data/repositories/biology_calculator_repository_impl.dart';
import '../../domain/repositories/biology_calculator_repository.dart';
import 'biology_calculator_controller.dart';
import 'calculator_session_state.dart';

/// Provider for the Biology Calculator Repository
final biologyCalculatorRepositoryProvider = Provider<BiologyCalculatorRepository>((ref) {
  return BiologyCalculatorRepositoryImpl();
});

/// Provider for the Biology Calculator StateNotifier
final biologyCalculatorControllerProvider =
    StateNotifierProvider<BiologyCalculatorController, CalculatorSessionState>((ref) {
  final repo = ref.watch(biologyCalculatorRepositoryProvider);
  return BiologyCalculatorController(repository: repo);
});
