import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../domain/entities/student_lab_note.dart';
import '../../domain/repositories/biology_lab_repository.dart';
import '../../data/repositories/biology_lab_repository_impl.dart';
import '../../data/datasources/curriculum_lab_database.dart';
import 'lab_session_state.dart';

/// وحدة التحكم التفاعلية في مختبر الرسومات الحيوية عبر Riverpod StateNotifier
class BiologyLabController extends StateNotifier<LabSessionState> {
  final BiologyLabRepository _repository;

  BiologyLabController({
    BiologyLabRepository? repository,
    LabSessionState? initialState,
  })  : _repository = repository ?? BiologyLabRepositoryImpl(),
        super(
          initialState ??
              LabSessionState(
                diagrams: CurriculumLabDatabase.getCoreCurriculumDiagrams()
                    .map((dto) => dto.toEntity())
                    .toList(),
                selectedDiagram: CurriculumLabDatabase.getCoreCurriculumDiagrams().isNotEmpty
                    ? CurriculumLabDatabase.getCoreCurriculumDiagrams().first.toEntity()
                    : null,
                activeHotspotId: 'none',
                currentZoomScale: 1.0,
                studentNotes: const [],
                isLoading: false,
              ),
        ) {
    loadDiagrams();
  }

  Future<void> loadDiagrams() async {
    state = state.copyWith(isLoading: true);
    final dtos = CurriculumLabDatabase.getCoreCurriculumDiagrams();
    final entities = dtos.map((dto) => dto.toEntity()).toList();
    final savedNotes = await _repository.getAllNotes();

    state = state.copyWith(
      diagrams: entities,
      selectedDiagram: state.selectedDiagram ?? (entities.isNotEmpty ? entities.first : null),
      studentNotes: savedNotes,
      isLoading: false,
    );
  }

  void selectDiagram(String diagramId) {
    final diagram = state.diagrams.firstWhere((d) => d.id == diagramId);
    state = state.copyWith(
      selectedDiagram: diagram,
      clearActiveHotspot: true,
      currentZoomScale: 1.0,
    );
  }

  void selectHotspot(String? hotspotId) {
    if (hotspotId == null) {
      state = state.copyWith(clearActiveHotspot: true);
    } else {
      state = state.copyWith(activeHotspotId: hotspotId);
    }
  }

  void updateZoom(double scale) {
    state = state.copyWith(currentZoomScale: scale);
  }

  Future<void> saveStudentNote(String diagramId, String hotspotId, String content) async {
    final newNote = StudentLabNote(
      noteId: 'note_${DateTime.now().millisecondsSinceEpoch}',
      diagramId: diagramId,
      hotspotId: hotspotId,
      content: content,
      updatedAt: DateTime.now(),
    );

    final updatedNotes = [
      ...state.studentNotes.where((n) => !(n.diagramId == diagramId && n.hotspotId == hotspotId)),
      newNote,
    ];
    state = state.copyWith(studentNotes: updatedNotes);

    // الحفظ الدائم محلياً
    await _repository.saveNote(newNote);
  }

  Future<void> deleteStudentNote(String noteId) async {
    final updated = state.studentNotes.where((n) => n.noteId != noteId).toList();
    state = state.copyWith(studentNotes: updated);
    await _repository.deleteNote(noteId);
  }
}
