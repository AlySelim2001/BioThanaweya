import '../../domain/entities/lab_diagram.dart';
import '../../domain/entities/student_lab_note.dart';

/// حالة واجهة مختبر الرسوم والصيغ الحيوية (Immutable State)
class LabSessionState {
  final List<LabDiagram> diagrams;
  final LabDiagram? selectedDiagram;
  final String? activeHotspotId;
  final double currentZoomScale;
  final List<StudentLabNote> studentNotes;
  final bool isLoading;

  const LabSessionState({
    required this.diagrams,
    this.selectedDiagram,
    this.activeHotspotId = 'none',
    this.currentZoomScale = 1.0,
    required this.studentNotes,
    this.isLoading = false,
  });

  StudentLabNote? getNoteForHotspot(String diagramId, String hotspotId) {
    try {
      return studentNotes.firstWhere(
        (n) => n.diagramId == diagramId && n.hotspotId == hotspotId,
      );
    } catch (_) {
      return null;
    }
  }

  LabSessionState copyWith({
    List<LabDiagram>? diagrams,
    LabDiagram? selectedDiagram,
    String? activeHotspotId,
    bool clearActiveHotspot = false,
    double? currentZoomScale,
    List<StudentLabNote>? studentNotes,
    bool? isLoading,
  }) {
    return LabSessionState(
      diagrams: diagrams ?? this.diagrams,
      selectedDiagram: selectedDiagram ?? this.selectedDiagram,
      activeHotspotId: clearActiveHotspot ? null : (activeHotspotId ?? this.activeHotspotId),
      currentZoomScale: currentZoomScale ?? this.currentZoomScale,
      studentNotes: studentNotes ?? this.studentNotes,
      isLoading: isLoading ?? this.isLoading,
    );
  }
}
