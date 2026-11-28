import 'package:flutter/foundation.dart';
import 'curriculum_data_service.dart';

/// BioAppController:
/// Central reactive state controller managing curriculum data fetching,
/// offline full-text search, and active exam attempt states.
class BioAppController extends ChangeNotifier {
  bool _isLoading = false;
  bool get isLoading => _isLoading;

  // قائمة أسئلة الفصل الحالي (Fully Typed)
  List<QuestionModel> _activeChapterQuestions = [];
  List<QuestionModel> get activeChapterQuestions => _activeChapterQuestions;

  // نتائج البحث الفوري
  List<QuestionModel> _searchResults = [];
  List<QuestionModel> get searchResults => _searchResults;

  // تخزين إجابات الطالب المؤقتة (للمحاكي الوزاري)
  final Map<int, int> _userExamAnswers = {};
  Map<int, int> get userExamAnswers => _userExamAnswers;

  int _selectedChapterId = 1;
  int get selectedChapterId => _selectedChapterId;

  // تحميل أسئلة فصل معين وتحديث الحالة بصورة تفاعلية
  Future<void> fetchChapterData(int chapterId) async {
    _isLoading = true;
    _selectedChapterId = chapterId;
    notifyListeners();

    try {
      // استدعاء الخدمة المحلية الفورية
      _activeChapterQuestions = await CurriculumDataService.loadQuestionsForChapter(chapterId);
    } catch (e) {
      _activeChapterQuestions = [];
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // تنفيذ البحث الفوري في نصوص المنهج (Offline Full-Text Search)
  Future<void> searchCurriculumDatabase(String query) async {
    if (query.trim().isEmpty) {
      _searchResults = [];
      notifyListeners();
      return;
    }

    _isLoading = true;
    notifyListeners();

    try {
      _searchResults = await CurriculumDataService.searchCurriculum(query);
    } catch (e) {
      _searchResults = [];
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // تسجيل وتحديث إجابة السؤال مع التخزين المحلي الفوري
  void recordAnswer(int questionIndex, int optionIndex) {
    _userExamAnswers[questionIndex] = optionIndex;
    notifyListeners();
  }

  // إعادة تعيين محاكي الامتحان
  void resetExam() {
    _userExamAnswers.clear();
    notifyListeners();
  }
}
