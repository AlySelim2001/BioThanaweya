import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../data/repositories/study_planner_repository_impl.dart';
import '../../domain/repositories/study_planner_repository.dart';
import 'study_planner_controller.dart';
import 'planner_session_state.dart';
export '../../../../core/database/local_database_service.dart' show localDatabaseServiceProvider;

/// مزود مستودع خطط المذاكرة التكيفية
final studyPlannerRepositoryProvider = Provider<StudyPlannerRepository>((ref) {
  return StudyPlannerRepositoryImpl();
});

/// مزود وحدة التحكم الرئيسية بخطة المذاكرة اليومية والمساعد الذكي
final studyPlannerControllerProvider =
    StateNotifierProvider<StudyPlannerController, PlannerSessionState>((ref) {
  final repo = ref.watch(studyPlannerRepositoryProvider);
  return StudyPlannerController(repository: repo);
});
