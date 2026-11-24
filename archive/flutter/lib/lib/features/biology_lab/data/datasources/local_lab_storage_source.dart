import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/student_lab_note_dto.dart';

/// محرك التخزين المحلي الدائم لملاحظات الطالب في مختبر الأحياء (Offline Persistent Storage)
class LocalLabStorageSource {
  static const String _prefKeyLabNotes = 'biothanaweya_student_lab_notes_v1';
  final Map<String, StudentLabNoteDto> _cachedNotes = {};
  bool _isInitialized = false;

  Future<void> initialize() async {
    if (_isInitialized) return;
    try {
      final prefs = await SharedPreferences.getInstance();
      final rawNotes = prefs.getString(_prefKeyLabNotes);
      if (rawNotes != null) {
        final List<dynamic> list = jsonDecode(rawNotes);
        for (final item in list) {
          final note = StudentLabNoteDto.fromJson(item as Map<String, dynamic>);
          _cachedNotes[note.noteId] = note;
        }
      }
    } catch (_) {}
    _isInitialized = true;
  }

  Future<List<StudentLabNoteDto>> getAllNotes() async {
    await initialize();
    return _cachedNotes.values.toList();
  }

  Future<List<StudentLabNoteDto>> getNotesForDiagram(String diagramId) async {
    await initialize();
    return _cachedNotes.values
        .where((n) => n.diagramId == diagramId)
        .toList();
  }

  Future<void> saveNote(StudentLabNoteDto note) async {
    await initialize();
    _cachedNotes[note.noteId] = note;
    await _persist();
  }

  Future<void> deleteNote(String noteId) async {
    await initialize();
    _cachedNotes.remove(noteId);
    await _persist();
  }

  Future<void> _persist() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final list = _cachedNotes.values.map((n) => n.toJson()).toList();
      await prefs.setString(_prefKeyLabNotes, jsonEncode(list));
    } catch (_) {}
  }
}
