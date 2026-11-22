import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../controllers/lab_providers.dart';

/// شاشة إدارة واستعراض الملاحظات الشخصية المحفوظة محلياً للطالب
class LabNotesScreen extends ConsumerWidget {
  const LabNotesScreen({Key? key}) : super(key: key);

  String _formatDate(DateTime dt) {
    return '${dt.year}/${dt.month}/${dt.day} - ${dt.hour}:${dt.minute.toString().padLeft(2, '0')}';
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(biologyLabControllerProvider);
    final controller = ref.read(biologyLabControllerProvider.notifier);
    final notes = state.studentNotes;

    const darkSurfaceColor = Color(0xFF1E1E2C);

    return Scaffold(
      backgroundColor: const Color(0xFF12121A),
      appBar: AppBar(
        title: const Text(
          'دفتر ملاحظاتي الحيوية (Offline Notes)',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16),
        ),
        backgroundColor: darkSurfaceColor,
        elevation: 0,
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: notes.isEmpty
          ? Center(
              child: Padding(
                padding: const EdgeInsets.all(24.0),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.notes_rounded, size: 64, color: Colors.grey.shade700),
                    const SizedBox(height: 16),
                    const Text(
                      'لا توجد ملاحظات مدونة بعد',
                      style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'افتح أي مسار في مختبر الرسومات وانقر على النقاط التفاعلية لتدوين ملاحظاتك وتثبيت الفخاخ الوزارية محلياً.',
                      textAlign: TextAlign.center,
                      style: TextStyle(color: Colors.grey.shade400, fontSize: 13, height: 1.4),
                    ),
                  ],
                ),
              ),
            )
          : ListView.builder(
              padding: const EdgeInsets.all(16.0),
              itemCount: notes.length,
              itemBuilder: (context, index) {
                final note = notes[index];
                final diagram = state.diagrams.where((d) => d.id == note.diagramId).firstOrNull;
                final hotspot = diagram?.findHotspotById(note.hotspotId);

                return Container(
                  margin: const EdgeInsets.only(bottom: 12),
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: darkSurfaceColor,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: Colors.green.withValues(alpha: 0.3)),
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Expanded(
                            child: Row(
                              children: [
                                const Icon(Icons.hub_outlined, color: Colors.greenAccent, size: 18),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: Text(
                                    diagram?.title ?? 'رسم حيوي',
                                    style: const TextStyle(
                                      color: Colors.white,
                                      fontWeight: FontWeight.bold,
                                      fontSize: 14,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                          IconButton(
                            icon: const Icon(Icons.delete_outline, color: Colors.redAccent, size: 20),
                            onPressed: () {
                              controller.deleteStudentNote(note.noteId);
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text('تم حذف الملاحظة')),
                              );
                            },
                          ),
                        ],
                      ),
                      if (hotspot != null) ...[
                        const SizedBox(height: 4),
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                          decoration: BoxDecoration(
                            color: Colors.amber.withValues(alpha: 0.15),
                            borderRadius: BorderRadius.circular(6),
                          ),
                          child: Text(
                            'النقطة: ${hotspot.title}',
                            style: const TextStyle(color: Colors.amber, fontSize: 11, fontWeight: FontWeight.bold),
                          ),
                        ),
                      ],
                      const SizedBox(height: 12),
                      Container(
                        width: double.infinity,
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: const Color(0xFF12121A),
                          borderRadius: BorderRadius.circular(10),
                        ),
                        child: Text(
                          note.content,
                          style: const TextStyle(color: Colors.white, fontSize: 13, height: 1.4),
                        ),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        'آخر تعديل: ${_formatDate(note.updatedAt)}',
                        style: TextStyle(color: Colors.grey.shade500, fontSize: 10),
                      ),
                    ],
                  ),
                );
              },
            ),
    );
  }
}
