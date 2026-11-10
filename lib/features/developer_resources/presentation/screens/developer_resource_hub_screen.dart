import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../../domain/entities/developer_resource_model.dart';
import '../controllers/developer_resource_hub_controller.dart';

/// Bento Grid Screen rendering the curated 25 essential GitHub repositories with offline search and interactive tools.
class DeveloperResourceHubScreen extends StatefulWidget {
  const DeveloperResourceHubScreen({super.key});

  @override
  State<DeveloperResourceHubScreen> createState() => _DeveloperResourceHubScreenState();
}

class _DeveloperResourceHubScreenState extends State<DeveloperResourceHubScreen> {
  late final DeveloperResourceHubController _controller;
  final TextEditingController _searchTextController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _controller = DeveloperResourceHubController();
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

  void _copyToClipboard(String text, String title, BuildContext context) {
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
                'تم نسخ رابط المستودع: $text',
                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                overflow: TextOverflow.ellipsis,
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

  void _showResourceDetails(DevResourceItem item, BuildContext context, bool isDark) {
    final catColor = Color(item.category.colorHex);

    showDialog(
      context: context,
      builder: (dialogCtx) => Dialog(
        backgroundColor: isDark ? const Color(0xFF1C2541) : Colors.white,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Header
              Row(
                children: [
                  Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: catColor.withValues(alpha: 0.15),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Icon(Icons.hub_rounded, color: catColor, size: 24),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          item.name,
                          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                        ),
                        Text(
                          'by ${item.owner} • ⭐ ${item.starsApprox}',
                          style: TextStyle(
                            fontSize: 12,
                            color: isDark ? Colors.white60 : Colors.black54,
                          ),
                        ),
                      ],
                    ),
                  ),
                  IconButton(
                    icon: const Icon(Icons.close_rounded),
                    onPressed: () => Navigator.of(dialogCtx).pop(),
                  ),
                ],
              ),
              const SizedBox(height: 16),

              // Arabic Summary
              Text(
                item.summaryAr,
                style: const TextStyle(fontSize: 13, height: 1.5, fontWeight: FontWeight.w600),
              ),
              const SizedBox(height: 8),

              // English Summary
              Text(
                item.summaryEn,
                style: TextStyle(
                  fontSize: 12,
                  height: 1.4,
                  color: isDark ? Colors.white70 : Colors.black87,
                ),
              ),
              const SizedBox(height: 14),

              // Tags
              Wrap(
                spacing: 6,
                runSpacing: 6,
                children: item.tags.map((tag) {
                  return Container(
                    padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                    decoration: BoxDecoration(
                      color: isDark ? const Color(0xFF0B132B) : const Color(0xFFF1F5F9),
                      borderRadius: BorderRadius.circular(8),
                      border: Border.all(color: Colors.white10),
                    ),
                    child: Text(
                      '#$tag',
                      style: TextStyle(
                        fontSize: 11,
                        color: catColor,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  );
                }).toList(),
              ),
              const SizedBox(height: 20),

              // Action Buttons
              Row(
                children: [
                  Expanded(
                    child: ElevatedButton.icon(
                      icon: const Icon(Icons.copy_rounded, size: 16),
                      label: const Text('نسخ الرابط المباشر'),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: const Color(0xFF0D9488),
                        foregroundColor: Colors.white,
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                        padding: const EdgeInsets.symmetric(vertical: 12),
                      ),
                      onPressed: () {
                        Navigator.of(dialogCtx).pop();
                        _copyToClipboard(item.repoUrl, item.name, context);
                      },
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
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
            Icon(Icons.terminal_rounded, color: Color(0xFF06B6D4)),
            SizedBox(width: 8),
            Text(
              'مركز موارد المطورين | GitHub Hub',
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
            tooltip: 'المستودعات المحفوظة',
            onPressed: () => _controller.toggleOnlyBookmarked(),
          ),
        ],
      ),
      body: Column(
        children: [
          // 1. Search Bar
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 12, 16, 8),
            child: TextField(
              controller: _searchTextController,
              onChanged: (val) => _controller.setSearchQuery(val),
              decoration: InputDecoration(
                hintText: 'ابحث في المستودعات (مثال: roadmap, apis, clean code)...',
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

          // 2. Category Filter Chips
          SizedBox(
            height: 44,
            child: ListView.separated(
              scrollDirection: Axis.horizontal,
              padding: const EdgeInsets.symmetric(horizontal: 16),
              itemCount: ResourceCategory.values.length + 1,
              separatorBuilder: (_, __) => const SizedBox(width: 8),
              itemBuilder: (context, index) {
                if (index == 0) {
                  final isSelected = state.selectedCategory == null;
                  return ChoiceChip(
                    label: const Text('الكل (All)', style: TextStyle(fontWeight: FontWeight.bold)),
                    selected: isSelected,
                    onSelected: (_) => _controller.selectCategory(null),
                    selectedColor: const Color(0xFF06B6D4),
                    labelStyle: TextStyle(
                      color: isSelected ? Colors.white : (isDark ? Colors.white70 : Colors.black87),
                    ),
                  );
                }

                final cat = ResourceCategory.values[index - 1];
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

          // 3. Bento Grid View
          Expanded(
            child: state.filteredResources.isEmpty
                ? const Center(
                    child: Text(
                      'لا توجد مستودعات مطابقة لمعايير البحث',
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
                          childAspectRatio: isWide ? 2.1 : 1.75,
                        ),
                        itemCount: state.filteredResources.length,
                        itemBuilder: (context, index) {
                          final item = state.filteredResources[index];
                          return _buildResourceBentoCard(item, context, isDark);
                        },
                      );
                    },
                  ),
          ),
        ],
      ),
    );
  }

  Widget _buildResourceBentoCard(DevResourceItem item, BuildContext context, bool isDark) {
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
          // Header Row: Category Badge, Stars, Bookmark Icon
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
              const SizedBox(width: 8),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                decoration: BoxDecoration(
                  color: Colors.amber.withValues(alpha: 0.12),
                  borderRadius: BorderRadius.circular(6),
                ),
                child: Row(
                  children: [
                    const Icon(Icons.star_rounded, size: 12, color: Color(0xFFF59E0B)),
                    const SizedBox(width: 3),
                    Text(
                      item.starsApprox,
                      style: const TextStyle(
                        fontSize: 10,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFFF59E0B),
                      ),
                    ),
                  ],
                ),
              ),
              const Spacer(),
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

          // Repository Name & Details Trigger
          InkWell(
            onTap: () => _showResourceDetails(item, context, isDark),
            borderRadius: BorderRadius.circular(8),
            child: Row(
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        item.name,
                        style: const TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 14,
                        ),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                      Text(
                        'github.com/${item.owner}/${item.name}',
                        style: TextStyle(
                          fontSize: 11,
                          fontFamily: 'monospace',
                          color: isDark ? Colors.cyanAccent : const Color(0xFF0D9488),
                        ),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ],
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.copy_rounded, size: 16),
                  tooltip: 'نسخ رابط GitHub',
                  onPressed: () => _copyToClipboard(item.repoUrl, item.name, context),
                ),
              ],
            ),
          ),

          // Arabic Summary & Level Tag
          Row(
            children: [
              Expanded(
                child: Text(
                  item.summaryAr,
                  style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 11),
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
              const SizedBox(width: 6),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                decoration: BoxDecoration(
                  color: isDark ? Colors.white10 : Colors.black12,
                  borderRadius: BorderRadius.circular(6),
                ),
                child: Text(
                  item.level.labelAr,
                  style: TextStyle(
                    fontSize: 9,
                    color: isDark ? Colors.white70 : Colors.black87,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
