import '../../data/datasources/local_exam_storage_data_source.dart';
import '../../data/repositories/exam_repository_impl.dart';
import '../../domain/repositories/exam_repository.dart';
import 'quiz_session_controller.dart';

/// مصفوفة المزودات والحاويات (Dependency Injection & State Providers)
/// تدعم استهلاك الحالة بنمط Riverpod Provider أو Provider التقليدي
class QuizEngineProviders {
  static final LocalExamStorageDataSource _storageDataSource = LocalExamStorageDataSource();
  static final ExamRepository _repository = ExamRepositoryImpl(storageDataSource: _storageDataSource);

  /// مستودع الامتحانات الأساسي
  static ExamRepository get repository => _repository;

  /// مزود خدمة التخزين المحلي
  static LocalExamStorageDataSource get storageDataSource => _storageDataSource;

  /// إنشاء وحدة تحكم جديدة لاختبار تفاعلي
  static QuizSessionController createController() {
    return QuizSessionController(repository: _repository);
  }
}
