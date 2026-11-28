import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../../domain/entities/git_command.dart';
import '../controllers/git_cheat_sheet_controller.dart';

/// Bento Grid Screen rendering the 130+ Git command infographic with offline search and interactive tools.
class GitCheatSheetScreen extends StatefulWidget {
  const GitCheatSheetScreen({super.key});

  @override
  State<GitCheatSheetScreen> createState() => _GitCheatSheetScreenState();
}

class _GitCheatSheetScreenState extends State<GitCheatSheetScreen> {
  late final GitCheatSheetController _controller;
  final TextEditingController _searchTextController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _controller = GitCheatSheetController();
    _controller.addListener(_onStateChanged);
  }

  void _onStateChanged() {
    if (mounted) setState(() {});
  }

  @override
  void dispose() {
    _controller.removeListener(_onStateChanged);
    _controller.dispose();
    _searchTextController.dispose();
    super.dispose();
  }

  void _copyToClipboard(String text, BuildContext context) {
    Clipboard.setData(ClipboardData(text: text));
    HapticFeedback.lightImpact();
    ScaffoldMessenger.of(context).clearSnackBars();
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Row(
          children: [
            const Icon(Icons.check_circle_rounded, color: Color(0xFF10B981), size: 20),
            const SizedBox(width: 10),
            Expanded(
              child: Text(
                'تم نسخ الأمر بنجاح: $text',
                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
              ),
            ),
          ],
        ),
        backgroundColor: const Color(0xFF0F172A),
        behavior: SnackBarBehavior.floating,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        duration: const Duration(seconds: 2),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final state = _controller.state;
    final isDark = Theme.of(context).brightness == Brightness.dark;

    return Scaffold(
      backgroundColor: isDark ? const Color(0xFF0B132B) : const Color(0xFFF1F5F9),
      appBar: AppBar(
        title: const Row(
          children: [
            Icon(Icons.terminal_rounded, color: Color(0xFF0D9488)),
            SizedBox(width: 8),
            Text(
              'دليل أوامر Git وخارطة الطريق البرمجية',
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
            ),
          ],
        ),
        elevation: 0,
        actions: [
          IconButton(
            icon: Icon(
              state.onlyBookmarked ? Icons.bookmark_rounded : Icons.bookmark_border_rounded,
              color: state.onlyBookmarked ? const Color(0xFFF59E0B) : null,
            ),
            tooltip: 'المفضلة المحفوظة',
            onPressed: () => _controller.toggleOnlyBookmarked(),
          ),
        ],
      ),
      body: Column(
        children: [
          // 1. Search Bar & Summary Hero
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 12, 16, 8),
            child: TextField(
              controller: _searchTextController,
              onChanged: (val) => _controller.setSearchQuery(val),
              decoration: InputDecoration(
                hintText: 'ابحث عن أي أمر (مثال: reset, stash, merge)...',
                prefixIcon: const Icon(Icons.search_rounded),
                suffixIcon: _searchTextController.text.isNotEmpty
                    ? IconButton(
                        icon: const Icon(Icons.clear_rounded, size: 18),
                        onPressed: () {
                          _searchTextController.clear();
                          _controller.setSearchQuery('');
                        },
                      )
                    : null,
                filled: true,
                fillColor: isDark ? const Color(0xFF1C2541) : Colors.white,
                contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(16),
                  borderSide: BorderSide.none,
                ),
              ),
            ),
          ),

          // 2. Category Filter Pills
          SizedBox(
            height: 44,
            child: ListView.separated(
              scrollDirection: Axis.horizontal,
              padding: const EdgeInsets.symmetric(horizontal: 16),
              itemCount: GitCategory.values.length + 1,
              separatorBuilder: (_, __) => const SizedBox(width: 8),
              itemBuilder: (context, index) {
                if (index == 0) {
                  final isSelected = state.selectedCategory == null;
                  return ChoiceChip(
                    label: const Text('الكل (All)', style: TextStyle(fontWeight: FontWeight.bold)),
                    selected: isSelected,
                    onSelected: (_) => _controller.selectCategory(null),
                    selectedColor: const Color(0xFF0D9488),
                    labelStyle: TextStyle(
                      color: isSelected ? Colors.white : (isDark ? Colors.white70 : Colors.black87),
                    ),
                  );
                }

                final cat = GitCategory.values[index - 1];
                final isSelected = state.selectedCategory == cat;
                return ChoiceChip(
                  label: Text(cat.titleAr, style: const TextStyle(fontWeight: FontWeight.bold)),
                  selected: isSelected,
                  onSelected: (_) => _controller.selectCategory(cat),
                  selectedColor: Color(cat.colorHex),
                  labelStyle: TextStyle(
                    color: isSelected ? Colors.white : (isDark ? Colors.white70 : Colors.black87),
                  ),
                );
              },
            ),
          ),

          const SizedBox(height: 8),

          // 3. Bento Grid Command View
          Expanded(
            child: state.filteredCommands.isEmpty
                ? const Center(
                    child: Text(
                      'لا توجد أوامر مطابقة لخيارات البحث الحالية',
                      style: TextStyle(fontWeight: FontWeight.w600, color: Colors.grey),
                    ),
                  )
                : LayoutBuilder(
                    builder: (context, constraints) {
                      final isWide = constraints.maxWidth >= 700;
                      final crossAxisCount = isWide ? 2 : 1;

                      return GridView.builder(
                        padding: const EdgeInsets.all(16),
                        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                          crossAxisCount: crossAxisCount,
                          crossAxisSpacing: 14,
                          mainAxisSpacing: 14,
                          childAspectRatio: isWide ? 2.0 : 1.7,
                        ),
                        itemCount: state.filteredCommands.length,
                        itemBuilder: (context, index) {
                          final item = state.filteredCommands[index];
                          return _buildBentoCard(item, context, isDark);
                        },
                      );
                    },
                  ),
          ),
        ],
      ),
    );
  }

  Widget _buildBentoCard(GitCommand item, BuildContext context, bool isDark) {
    final catColor = Color(item.category.colorHex);

    return Container(
      decoration: BoxDecoration(
        color: isDark ? const Color(0xFF1C2541) : Colors.white,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(
          color: item.isBookmarked
              ? const Color(0xFFF59E0B).withValues(alpha: 0.6)
              : (isDark ? Colors.white12 : Colors.black12),
          width: item.isBookmarked ? 1.5 : 1.0,
        ),
        boxShadow: [
          BoxShadow(
            color: catColor.withValues(alpha: 0.08),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      padding: const EdgeInsets.all(14),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          // Header: Category Tag & Bookmark Button
          Row(
            children: [
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                decoration: BoxDecoration(
                  color: catColor.withValues(alpha: 0.15),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Text(
                  item.category.titleAr,
                  style: TextStyle(
                    color: catColor,
                    fontSize: 11,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              const Spacer(),
              // Impact indicator
              if (item.impact == GitImpact.dangerous)
                Container(
                  margin: const EdgeInsets.symmetric(horizontal: 6),
                  padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                  decoration: BoxDecoration(
                    color: Colors.red.withValues(alpha: 0.15),
                    borderRadius: BorderRadius.circular(6),
                  ),
                  child: const Text(
                    '⚠️ تعديل تاريخ',
                    style: TextStyle(color: Colors.redAccent, fontSize: 10, fontWeight: FontWeight.bold),
                  ),
                ),
              // Bookmark Button
              InkWell(
                onTap: () => _controller.toggleBookmark(item.id),
                borderRadius: BorderRadius.circular(20),
                child: Padding(
                  padding: const EdgeInsets.all(4),
                  child: Icon(
                    item.isBookmarked ? Icons.bookmark_rounded : Icons.bookmark_border_rounded,
                    size: 20,
                    color: item.isBookmarked ? const Color(0xFFF59E0B) : Colors.grey,
                  ),
                ),
              ),
            ],
          ),

          // Command Box (Copyable Terminal Look)
          InkWell(
            onTap: () => _copyToClipboard(item.command, context),
            borderRadius: BorderRadius.circular(10),
            child: Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
              decoration: BoxDecoration(
                color: isDark ? const Color(0xFF0B132B) : const Color(0xFF0F172A),
                borderRadius: BorderRadius.circular(10),
                border: Border.all(color: Colors.white10),
              ),
              child: Row(
                children: [
                  const Text(
                    '\$ ',
                    style: TextStyle(color: Color(0xFF10B981), fontWeight: FontWeight.bold, fontFamily: 'monospace'),
                  ),
                  Expanded(
                    child: Text(
                      item.command,
                      style: const TextStyle(
                        color: Colors.white,
                        fontFamily: 'monospace',
                        fontWeight: FontWeight.bold,
                        fontSize: 12,
                      ),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                  const Icon(Icons.copy_rounded, color: Colors.white54, size: 14),
                ],
              ),
            ),
          ),

          // Bilingual Summaries
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                item.summaryAr,
                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
              ),
              const SizedBox(height: 2),
              Text(
                item.summaryEn,
                style: TextStyle(fontSize: 11, color: isDark ? Colors.white60 : Colors.black54),
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
            ],
          ),
        ],
      ),
    );
  }
}
