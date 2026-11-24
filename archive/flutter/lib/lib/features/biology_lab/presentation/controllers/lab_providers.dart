import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../data/repositories/biology_lab_repository_impl.dart';
import '../../domain/repositories/biology_lab_repository.dart';
import 'biology_lab_controller.dart';
import 'lab_session_state.dart';

/// مزود مستودع مختبر الرسومات الحيوية
final biologyLabRepositoryProvider = Provider<BiologyLabRepository>((ref) {
  return BiologyLabRepositoryImpl();
});

/// مزود وحدة التحكم الرئيسية بمختبر الأحياء
final biologyLabControllerProvider =
    StateNotifierProvider<BiologyLabController, LabSessionState>((ref) {
  final repo = ref.watch(biologyLabRepositoryProvider);
  return BiologyLabController(repository: repo);
});
