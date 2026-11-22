import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/question_dto.dart';
import '../models/exam_session_dto.dart';
import '../models/mistake_record_dto.dart';
import 'offline_curriculum_question_bank.dart';

/// واجهة ومزود التخزين المحلي الدائم لجلسات الاختبار وسجل الأخطاء وبنك الأسئلة
class LocalExamStorageDataSource {
  static const String _prefKeySessions = 'biothanaweya_offline_exam_sessions_v1';
  static const String _prefKeyMistakes = 'biothanaweya_offline_mistakes_fsrs_v1';
  static const String _prefKeyCustomBank = 'biothanaweya_offline_custom_bank_v1';

  final Map<String, QuestionDto> _cachedQuestionBank = {};
  final Map<String, MistakeRecordDto> _cachedMistakes = {};
  final List<ExamSessionDto> _cachedSessions = [];

  bool _isInitialized = false;

  /// تهيئة وتحميل البيانات من الذاكرة المحلية (SharedPreferences) إلى الذاكرة السريعة
  Future<void> initialize() async {
    if (_isInitialized) return;

    // 1. شحن بنك الأسئلة الافتراضي الأساسي (11 فصلاً)
    final seedQuestions = OfflineCurriculumQuestionBank.getRawCurriculumQuestions();
    for (final q in seedQuestions) {
      _cachedQuestionBank[q.id] = q;
    }

    try {
      final prefs = await SharedPreferences.getInstance();

      // 2. تحميل أي أسئلة مخصصة أو تم تحديثها
      final rawCustom = prefs.getString(_prefKeyCustomBank);
      if (rawCustom != null) {
        final List<dynamic> list = jsonDecode(rawCustom);
        for (final item in list) {
          final dto = QuestionDto.fromJson(item as Map<String, dynamic>);
          _cachedQuestionBank[dto.id] = dto;
        }
      }

      // 3. تحميل سجل الأخطاء (FSRS Mistake Index)
      final rawMistakes = prefs.getString(_prefKeyMistakes);
      if (rawMistakes != null) {
        final List<dynamic> list = jsonDecode(rawMistakes);
        for (final item in list) {
          final mistake = MistakeRecordDto.fromJson(item as Map<String, dynamic>);
          _cachedMistakes[mistake.questionId] = mistake;
        }
      }

      // 4. تحميل جلسات الامتحانات السابقة
      final rawSessions = prefs.getString(_prefKeySessions);
      if (rawSessions != null) {
        final List<dynamic> list = jsonDecode(rawSessions);
        _cachedSessions.clear();
        for (final item in list) {
          _cachedSessions.add(ExamSessionDto.fromJson(item as Map<String, dynamic>));
        }
      }
    } catch (_) {
      // الاستمرار في العمل بالاعتماد على التخزين المؤقت المحلي في حال حدوث أي استثناء
    }

    _isInitialized = true;
  }

  /// جلب كافة الأسئلة المتوفرة في البنك المحلي
  Future<List<QuestionDto>> getAllQuestions() async {
    await initialize();
    return _cachedQuestionBank.values.toList();
  }

  /// جلب أسئلة فصل محدد
  Future<List<QuestionDto>> getQuestionsByChapter(int chapterId) async {
    await initialize();
    return _cachedQuestionBank.values
        .where((q) => q.chapterId == chapterId)
        .toList();
  }

  /// جلب سؤال محدد عبر المعرف
  Future<QuestionDto?> getQuestionById(String id) async {
    await initialize();
    return _cachedQuestionBank[id];
  }

  /// حفظ جلسة امتحانية جديدة محلياً
  Future<void> saveExamSession(ExamSessionDto session) async {
    await initialize();
    _cachedSessions.insert(0, session);

    try {
      final prefs = await SharedPreferences.getInstance();
      final encoded = jsonEncode(_cachedSessions.map((s) => s.toJson()).toList());
      await prefs.setString(_prefKeySessions, encoded);
    } catch (_) {}
  }

  /// استرجاع كافة الجلسات السابقة
  Future<List<ExamSessionDto>> getPastSessions() async {
    await initialize();
    return List.unmodifiable(_cachedSessions);
  }

  /// استرجاع سجل الأخطاء النشطة (FSRS Mistakes)
  Future<List<MistakeRecordDto>> getActiveMistakes() async {
    await initialize();
    return _cachedMistakes.values.toList();
  }

  /// تحديث أو حفظ سجل الأخطاء
  Future<void> saveMistakeRecord(MistakeRecordDto record) async {
    await initialize();
    _cachedMistakes[record.questionId] = record;
    await _persistMistakes();
  }

  /// حفظ قائمة محدثة من الأخطاء دفعة واحدة
  Future<void> saveMistakeBatch(List<MistakeRecordDto> records) async {
    await initialize();
    for (final r in records) {
      _cachedMistakes[r.questionId] = r;
    }
    await _persistMistakes();
  }

  Future<void> _persistMistakes() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final list = _cachedMistakes.values.map((m) => m.toJson()).toList();
      await prefs.setString(_prefKeyMistakes, jsonEncode(list));
    } catch (_) {}
  }

  /// إعادة ضبط سجل الأخطاء
  Future<void> clearMistakes() async {
    await initialize();
    _cachedMistakes.clear();
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.remove(_prefKeyMistakes);
    } catch (_) {}
  }
}
