import 'package:flutter_test/flutter_test.dart';
import 'package:smarts_bio/features/biology_lab/domain/entities/lab_diagram.dart';
import 'package:smarts_bio/features/biology_lab/domain/entities/hotspot_point.dart';
import 'package:smarts_bio/features/biology_lab/domain/entities/student_lab_note.dart';
import 'package:smarts_bio/features/biology_lab/data/datasources/curriculum_lab_database.dart';
import 'package:smarts_bio/features/biology_lab/data/repositories/biology_lab_repository_impl.dart';
import 'package:smarts_bio/features/biology_lab/presentation/controllers/biology_lab_controller.dart';

void main() {
  group('Interactive Biology Lab Domain & Data Tests', () {
    test('CurriculumLabDatabase provides required curriculum diagrams', () {
      final diagrams = CurriculumLabDatabase.getCoreCurriculumDiagrams();
      expect(diagrams.isNotEmpty, isTrue);

      final krebs = diagrams.firstWhere((d) => d.id == 'diag_krebs_cycle');
      expect(krebs.title, contains('كريبس'));
      expect(krebs.hotspots.length, greaterThanOrEqualTo(3));

      final dna = diagrams.firstWhere((d) => d.id == 'diag_dna_replication');
      expect(dna.title, contains('DNA'));
      expect(dna.hotspots.any((h) => h.id == 'hs_helicase'), isTrue);
    });

    test('LabDiagram entity finds hotspot point accurately', () {
      const hotspot = HotspotPoint(
        id: 'hs_1',
        title: 'حمض الستريك',
        detailedExplanation: 'شرح تفصيلي',
        ministryTrapNote: 'فخ وزاري',
        xPositionRatio: 0.4,
        yPositionRatio: 0.2,
      );

      const diagram = LabDiagram(
        id: 'diag_1',
        title: 'رسم تجريبي',
        chapter: 'الفصل الأول',
        description: 'الوصف',
        assetImagePath: 'assets/image.png',
        hotspots: [hotspot],
      );

      expect(diagram.findHotspotById('hs_1'), isNotNull);
      expect(diagram.findHotspotById('hs_1')?.title, equals('حمض الستريك'));
      expect(diagram.findHotspotById('non_existent'), isNull);
    });

    test('BiologyLabRepositoryImpl loads diagrams and manages notes offline', () async {
      final repo = BiologyLabRepositoryImpl();
      final all = await repo.getAllDiagrams();
      expect(all.length, greaterThanOrEqualTo(2));

      final testNote = StudentLabNote(
        noteId: 'note_test_1',
        diagramId: 'diag_krebs_cycle',
        hotspotId: 'hs_acetyl_co_a',
        content: 'ملاحظة تثبيت كربونات الأسيتيل',
        updatedAt: DateTime.now(),
      );

      await repo.saveNote(testNote);
      final notes = await repo.getNotesForDiagram('diag_krebs_cycle');
      expect(notes.any((n) => n.noteId == 'note_test_1'), isTrue);

      await repo.deleteNote('note_test_1');
      final notesAfterDelete = await repo.getNotesForDiagram('diag_krebs_cycle');
      expect(notesAfterDelete.any((n) => n.noteId == 'note_test_1'), isFalse);
    });

    test('BiologyLabController updates zoom, diagram, and active hotspot', () {
      final controller = BiologyLabController();
      expect(controller.state.diagrams.isNotEmpty, isTrue);

      controller.selectHotspot('hs_acetyl_co_a');
      expect(controller.state.activeHotspotId, equals('hs_acetyl_co_a'));

      controller.updateZoom(2.5);
      expect(controller.state.currentZoomScale, equals(2.5));

      controller.selectHotspot(null);
      expect(controller.state.activeHotspotId, isNull);
    });
  });
}
