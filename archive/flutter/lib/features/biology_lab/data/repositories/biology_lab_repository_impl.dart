import '../../domain/entities/lab_diagram.dart';
import '../../domain/entities/student_lab_note.dart';
import '../../domain/repositories/biology_lab_repository.dart';
import '../datasources/curriculum_lab_database.dart';
import '../datasources/local_lab_storage_source.dart';
import '../models/student_lab_note_dto.dart';

/// التنفيذ العملي لمستودع مختبر الرسومات الحيوية
class BiologyLabRepositoryImpl implements BiologyLabRepository {
  final LocalLabStorageSource _storageSource;

  BiologyLabRepositoryImpl({LocalLabStorageSource? storageSource})
      : _storageSource = storageSource ?? LocalLabStorageSource();

  @override
  Future<List<LabDiagram>> getAllDiagrams() async {
    final dtos = CurriculumLabDatabase.getCoreCurriculumDiagrams();
    return dtos.map((dto) => dto.toEntity()).toList();
  }

  @override
  Future<LabDiagram?> getDiagramById(String id) async {
    final all = await getAllDiagrams();
    try {
      return all.firstWhere((d) => d.id == id);
    } catch (_) {
      return null;
    }
  }

  @override
  Future<List<StudentLabNote>> getNotesForDiagram(String diagramId) async {
    final dtos = await _storageSource.getNotesForDiagram(diagramId);
    return dtos.map((d) => d.toEntity()).toList();
  }

  @override
  Future<List<StudentLabNote>> getAllNotes() async {
    final dtos = await _storageSource.getAllNotes();
    return dtos.map((d) => d.toEntity()).toList();
  }

  @override
  Future<void> saveNote(StudentLabNote note) async {
    final dto = StudentLabNoteDto.fromEntity(note);
    await _storageSource.saveNote(dto);
  }

  @override
  Future<void> deleteNote(String noteId) async {
    await _storageSource.deleteNote(noteId);
  }
}
