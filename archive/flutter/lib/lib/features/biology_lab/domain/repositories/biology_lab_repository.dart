import '../entities/lab_diagram.dart';
import '../entities/student_lab_note.dart';

/// العقد المجرد لمستودع مختبر الرسومات والصيغ الحيوية
abstract class BiologyLabRepository {
  /// جلب كافة الرسوم والمسارات الحيوية المتوفرة محلياً
  Future<List<LabDiagram>> getAllDiagrams();

  /// جلب رسم محدد عبر المعرف
  Future<LabDiagram?> getDiagramById(String id);

  /// جلب كافة الملاحظات المحفوظة لرسم حيوي محدد
  Future<List<StudentLabNote>> getNotesForDiagram(String diagramId);

  /// جلب كافة الملاحظات الذاتية التي دونها الطالب
  Future<List<StudentLabNote>> getAllNotes();

  /// حفظ أو تحديث ملاحظة شخصية محلياً
  Future<void> saveNote(StudentLabNote note);

  /// حذف ملاحظة محددة
  Future<void> deleteNote(String noteId);
}
