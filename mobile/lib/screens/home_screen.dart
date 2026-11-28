import 'package:biothanaweya_mobile/l10n/app_localizations.dart';
import 'package:biothanaweya_mobile/models/app_item.dart';
import 'package:biothanaweya_mobile/providers/theme_provider.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

/// Real Thanaweya Amma biology curriculum chapters (mirrors `CHAPTERS` in
/// the web app's `src/services/curriculumRepository.ts`). Displayed on the
/// home screen; replace with a real data source when backend sync lands.
const List<AppItem> mockAppItems = <AppItem>[
  AppItem(
    id: 'unit1_chap1',
    title: 'الدعامة والحركة في الكائنات الحية',
    description:
        'Plant physiological/structural support, human skeleton, joints, '
        'tendons, ligaments, and muscular contraction mechanism.',
  ),
  AppItem(
    id: 'unit1_chap2',
    title: 'التنسيق الهرموني في الكائنات الحية',
    description:
        'Endocrine glands, master pituitary gland, negative feedback '
        'loops, blood sugar and calcium homeostasis.',
  ),
  AppItem(
    id: 'unit1_chap3',
    title: 'التكاثر في الكائنات الحية',
    description:
        'Alternation of generations (Fern & Plasmodium), double '
        'fertilization in angiosperms, human gametogenesis & menstrual cycle.',
  ),
  AppItem(
    id: 'unit1_chap4',
    title: 'المناعة في الكائنات الحية',
    description:
        'Plant defense, human lymphatic system, antibodies, innate vs '
        'acquired immunity (humoral B-cells & cell-mediated T-cells).',
  ),
  AppItem(
    id: 'unit2_chap1',
    title: 'الحمض النووي والمعلومات الوراثية',
    description:
        'Historical proof experiments (Griffith, Hershey-Chase), DNA '
        'structure & Chargaff ratios, replication, repair & nucleosomes.',
  ),
  AppItem(
    id: 'unit2_chap2',
    title: 'الأحماض النووية وتخليق البروتين',
    description:
        'Types of RNA, transcription, translation, genetic code math, '
        'mutations, and genetic engineering technology.',
  ),
];

/// Entry screen: lists every [AppItem] and opens its details page.
class HomeScreen extends ConsumerWidget {
  /// Creates the home screen.
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final l10n = AppLocalizations.of(context)!;
    return Scaffold(
      appBar: AppBar(
        title: Text(l10n.homeTitle),
        actions: [
          IconButton(
            tooltip: l10n.quizTitle,
            icon: const Icon(Icons.quiz),
            onPressed: () => context.push('/quiz'),
          ),
          IconButton(
            tooltip: l10n.themeToggle,
            icon: const Icon(Icons.brightness_6),
            onPressed: () => ref.read(themeModeProvider.notifier).toggle(),
          ),
        ],
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: mockAppItems.length,
        itemBuilder: (context, index) {
          final item = mockAppItems[index];
          return Card(
            child: ListTile(
              title: Text(item.title),
              subtitle: Text(item.description),
              trailing: const Icon(Icons.chevron_right),
              // `push` keeps home on the stack so back navigation works.
              onTap: () => context.push('/details/${item.id}', extra: item),
            ),
          );
        },
      ),
    );
  }
}
