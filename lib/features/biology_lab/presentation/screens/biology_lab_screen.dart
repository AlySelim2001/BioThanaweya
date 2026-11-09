import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../controllers/lab_providers.dart';
import 'lab_notes_screen.dart';

/// شاشة مختبر الرسومات والصيغ الحيوية التفاعلية الرسمية
class BiologyLabScreen extends ConsumerWidget {
  const BiologyLabScreen({Key? key}) : super(key: key);

  void _showAddNoteDialog(BuildContext context, WidgetRef ref, String diagramId, String hotspotId, String currentContent) {
    final textController = TextEditingController(text: currentContent);
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: const Color(0xFF1E1E2C),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        title: const Row(
          children: [
            Icon(Icons.edit_note, color: Colors.greenAccent),
            SizedBox(width: 8),
            Text('تدوين ملاحظة شخصية', style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold)),
          ],
        ),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'اكتب ملحوظتك التثبيتية أو الفخ الذي تريد تذكره محلياً:',
              style: TextStyle(color: Colors.grey, fontSize: 12),
            ),
            const SizedBox(height: 10),
            TextField(
              controller: textController,
              maxLines: 4,
              style: const TextStyle(color: Colors.white, fontSize: 13),
              decoration: InputDecoration(
                hintText: 'مثال: ربط كربونات الأسيتيل بـ 2C والأوكسالوستريك بـ 4C...',
                hintStyle: const TextStyle(color: Colors.grey),
                filled: true,
                fillColor: const Color(0xFF12121A),
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide.none),
              ),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('إلغاء', style: TextStyle(color: Colors.grey)),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.green,
              foregroundColor: Colors.white,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
            ),
            onPressed: () {
              final content = textController.text.trim();
              if (content.isNotEmpty) {
                ref.read(biologyLabControllerProvider.notifier).saveStudentNote(diagramId, hotspotId, content);
              }
              Navigator.pop(ctx);
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('تم حفظ الملاحظة محلياً بنجاح!'),
                  backgroundColor: Color(0xFF1E1E2C),
                ),
              );
            },
            child: const Text('حفظ الملاحظة', style: TextStyle(fontWeight: FontWeight.bold)),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(biologyLabControllerProvider);
    final controller = ref.read(biologyLabControllerProvider.notifier);

    if (state.isLoading || state.selectedDiagram == null) {
      return const Scaffold(
        backgroundColor: Color(0xFF12121A),
        body: Center(child: CircularProgressIndicator(color: Colors.green)),
      );
    }

    final diagram = state.selectedDiagram!;
    const activeColor = Colors.green;
    const darkSurfaceColor = Color(0xFF1E1E2C);

    return Scaffold(
      backgroundColor: const Color(0xFF12121A),
      appBar: AppBar(
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(diagram.title, style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 15)),
            Text(diagram.chapter, style: const TextStyle(color: Colors.greenAccent, fontSize: 11)),
          ],
        ),
        backgroundColor: darkSurfaceColor,
        elevation: 0,
        iconTheme: const IconThemeData(color: Colors.white),
        actions: [
          // زر استعراض الملاحظات المحفوظة
          IconButton(
            tooltip: 'دفتر ملاحظاتي الحيوية',
            icon: const Icon(Icons.sticky_note_2_outlined, color: Colors.greenAccent),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const LabNotesScreen()),
              );
            },
          ),
        ],
      ),
      body: Column(
        children: [
          // شريط اختيار المسار الحيوي
          Container(
            height: 56,
            color: const Color(0xFF161622),
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              padding: const EdgeInsets.symmetric(horizontal: 8),
              itemCount: state.diagrams.length,
              itemBuilder: (context, index) {
                final d = state.diagrams[index];
                final isSelected = d.id == diagram.id;
                return Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 8),
                  child: ChoiceChip(
                    label: Text(
                      d.title,
                      style: TextStyle(
                        fontSize: 12,
                        color: isSelected ? Colors.white : Colors.grey.shade400,
                        fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                      ),
                    ),
                    selected: isSelected,
                    selectedColor: activeColor,
                    backgroundColor: darkSurfaceColor,
                    onSelected: (_) => controller.selectDiagram(d.id),
                  ),
                );
              },
            ),
          ),

          // لوحة التكبير والمحاكاة التفاعلية (Interactive Canvas with Hotspots)
          Expanded(
            child: Container(
              margin: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: darkSurfaceColor,
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: Colors.green.withOpacity(0.3)),
              ),
              child: Stack(
                children: [
                  InteractiveViewer(
                    minScale: 1.0,
                    maxScale: 4.0,
                    onInteractionUpdate: (details) => controller.updateZoom(details.scale),
                    child: Center(
                      child: Stack(
                        clipBehavior: Clip.none,
                        children: [
                          // مساحة الرسم التوضيحي البصري الحيوي
                          Container(
                            width: 330,
                            height: 330,
                            decoration: BoxDecoration(
                              gradient: const RadialGradient(
                                colors: [Color(0xFF2A2A3D), Color(0xFF1A1A28)],
                                radius: 0.85,
                              ),
                              borderRadius: BorderRadius.circular(16),
                              border: Border.all(color: Colors.greenAccent.withOpacity(0.2)),
                            ),
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                const Icon(Icons.biotech_outlined, size: 48, color: Colors.greenAccent),
                                const SizedBox(height: 10),
                                Padding(
                                  padding: const EdgeInsets.symmetric(horizontal: 20.0),
                                  child: Text(
                                    diagram.title,
                                    textAlign: TextAlign.center,
                                    style: const TextStyle(
                                      color: Colors.white,
                                      fontSize: 16,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                                const SizedBox(height: 8),
                                Padding(
                                  padding: const EdgeInsets.symmetric(horizontal: 24.0),
                                  child: Text(
                                    diagram.description,
                                    textAlign: TextAlign.center,
                                    style: TextStyle(color: Colors.grey.shade400, fontSize: 11, height: 1.4),
                                  ),
                                ),
                                const SizedBox(height: 14),
                                Container(
                                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                                  decoration: BoxDecoration(
                                    color: Colors.green.withOpacity(0.15),
                                    borderRadius: BorderRadius.circular(12),
                                    border: Border.all(color: Colors.greenAccent.withOpacity(0.3)),
                                  ),
                                  child: const Text(
                                    'انقر على أيقونات النبض للاستكشاف والتحليل',
                                    style: TextStyle(color: Colors.greenAccent, fontSize: 10, fontWeight: FontWeight.bold),
                                  ),
                                ),
                              ],
                            ),
                          ),

                          // رسم النقاط التفاعلية الحساسة (Hotspots Overlay)
                          ...diagram.hotspots.map((hs) {
                            final isActive = state.activeHotspotId == hs.id;
                            final leftPos = hs.xPositionRatio * 330 - 16;
                            final topPos = hs.yPositionRatio * 330 - 16;

                            return Positioned(
                              left: leftPos,
                              top: topPos,
                              child: InkWell(
                                onTap: () => controller.selectHotspot(hs.id),
                                borderRadius: BorderRadius.circular(20),
                                child: AnimatedContainer(
                                  duration: const Duration(milliseconds: 200),
                                  width: isActive ? 36 : 30,
                                  height: isActive ? 36 : 30,
                                  decoration: BoxDecoration(
                                    color: isActive ? Colors.amber : activeColor,
                                    shape: BoxShape.circle,
                                    border: Border.all(color: Colors.white, width: 2),
                                    boxShadow: [
                                      BoxShadow(
                                        color: (isActive ? Colors.amber : activeColor).withOpacity(0.6),
                                        blurRadius: isActive ? 12 : 6,
                                        spreadRadius: isActive ? 2 : 1,
                                      ),
                                    ],
                                  ),
                                  child: Icon(
                                    isActive ? Icons.lightbulb : Icons.ads_click,
                                    color: Colors.black,
                                    size: 16,
                                  ),
                                ),
                              ),
                            );
                          }).toList(),
                        ],
                      ),
                    ),
                  ),

                  // بطاقة معلومات النقطة الفعالة وتنبيهات الوزارة (Hotspot Detail Bottom Card)
                  if (state.activeHotspotId != null) ...[
                    Positioned(
                      bottom: 12,
                      left: 12,
                      right: 12,
                      child: Builder(
                        builder: (context) {
                          final hotspot = diagram.findHotspotById(state.activeHotspotId!);
                          if (hotspot == null) return const SizedBox.shrink();

                          final existingNote = state.getNoteForHotspot(diagram.id, hotspot.id);

                          return Container(
                            padding: const EdgeInsets.all(14),
                            decoration: BoxDecoration(
                              color: const Color(0xFF1E1E2C).withOpacity(0.96),
                              borderRadius: BorderRadius.circular(14),
                              border: Border.all(color: Colors.amber, width: 1.5),
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.black.withOpacity(0.5),
                                  blurRadius: 16,
                                  offset: const Offset(0, 4),
                                ),
                              ],
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    Expanded(
                                      child: Row(
                                        children: [
                                          const Icon(Icons.hub_outlined, color: Colors.amber, size: 20),
                                          const SizedBox(width: 8),
                                          Expanded(
                                            child: Text(
                                              hotspot.title,
                                              style: const TextStyle(
                                                color: Colors.amber,
                                                fontWeight: FontWeight.bold,
                                                fontSize: 15,
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                    IconButton(
                                      padding: EdgeInsets.zero,
                                      constraints: const BoxConstraints(),
                                      icon: const Icon(Icons.close, color: Colors.white70, size: 20),
                                      onPressed: () => controller.selectHotspot(null),
                                    ),
                                  ],
                                ),
                                const SizedBox(height: 6),
                                Text(
                                  hotspot.detailedExplanation,
                                  style: const TextStyle(color: Colors.white, fontSize: 13, height: 1.4),
                                ),
                                const SizedBox(height: 8),

                                // فخ واضعي الامتحان الوزاري
                                Container(
                                  width: double.infinity,
                                  padding: const EdgeInsets.all(8),
                                  decoration: BoxDecoration(
                                    color: Colors.red.withOpacity(0.18),
                                    borderRadius: BorderRadius.circular(8),
                                    border: Border.all(color: Colors.redAccent.withOpacity(0.5)),
                                  ),
                                  child: Row(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      const Icon(Icons.warning_amber_rounded, color: Colors.redAccent, size: 16),
                                      const SizedBox(width: 6),
                                      Expanded(
                                        child: Text(
                                          hotspot.ministryTrapNote,
                                          style: const TextStyle(
                                            color: Colors.redAccent,
                                            fontSize: 12,
                                            fontWeight: FontWeight.w600,
                                            height: 1.3,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),

                                const SizedBox(height: 10),

                                // قسم الملاحظة الذاتية للطالب
                                Row(
                                  children: [
                                    Expanded(
                                      child: existingNote != null
                                          ? Container(
                                              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                                              decoration: BoxDecoration(
                                                color: Colors.green.withOpacity(0.12),
                                                borderRadius: BorderRadius.circular(8),
                                                border: Border.all(color: Colors.green.withOpacity(0.4)),
                                              ),
                                              child: Text(
                                                'ملاحظتك: ${existingNote.content}',
                                                style: const TextStyle(color: Colors.greenAccent, fontSize: 11),
                                                maxLines: 1,
                                                overflow: TextOverflow.ellipsis,
                                              ),
                                            )
                                          : const Text(
                                              'لا توجد ملاحظة مدونة بعد',
                                              style: TextStyle(color: Colors.grey, fontSize: 11),
                                            ),
                                    ),
                                    const SizedBox(width: 8),
                                    ElevatedButton.icon(
                                      style: ElevatedButton.styleFrom(
                                        backgroundColor: const Color(0xFF2A2A3D),
                                        foregroundColor: Colors.white,
                                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                                      ),
                                      onPressed: () {
                                        _showAddNoteDialog(
                                          context,
                                          ref,
                                          diagram.id,
                                          hotspot.id,
                                          existingNote?.content ?? '',
                                        );
                                      },
                                      icon: const Icon(Icons.note_add, size: 14, color: Colors.greenAccent),
                                      label: Text(
                                        existingNote != null ? 'تعديل' : 'تدوين ملاحظة',
                                        style: const TextStyle(fontSize: 11),
                                      ),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          );
                        },
                      ),
                    ),
                  ],
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
