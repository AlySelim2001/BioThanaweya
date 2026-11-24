import 'package:flutter/foundation.dart';
import '../../domain/entities/developer_resource_model.dart';
import '../../data/datasources/developer_resources_database.dart';
import '../../data/datasources/local_resource_storage_source.dart';

class DeveloperResourceHubState {
  final List<DevResourceItem> allResources;
  final List<DevResourceItem> filteredResources;
  final Set<String> bookmarkedIds;
  final ResourceCategory? selectedCategory;
  final String searchQuery;
  final bool onlyBookmarked;
  final bool isLoading;

  const DeveloperResourceHubState({
    required this.allResources,
    required this.filteredResources,
    required this.bookmarkedIds,
    this.selectedCategory,
    this.searchQuery = '',
    this.onlyBookmarked = false,
    this.isLoading = false,
  });

  DeveloperResourceHubState copyWith({
    List<DevResourceItem>? allResources,
    List<DevResourceItem>? filteredResources,
    Set<String>? bookmarkedIds,
    ResourceCategory? Function()? selectedCategory,
    String? searchQuery,
    bool? onlyBookmarked,
    bool? isLoading,
  }) {
    return DeveloperResourceHubState(
      allResources: allResources ?? this.allResources,
      filteredResources: filteredResources ?? this.filteredResources,
      bookmarkedIds: bookmarkedIds ?? this.bookmarkedIds,
      selectedCategory:
          selectedCategory != null ? selectedCategory() : this.selectedCategory,
      searchQuery: searchQuery ?? this.searchQuery,
      onlyBookmarked: onlyBookmarked ?? this.onlyBookmarked,
      isLoading: isLoading ?? this.isLoading,
    );
  }
}

class DeveloperResourceHubController extends ChangeNotifier {
  final LocalResourceStorageSource _storageSource;
  late DeveloperResourceHubState state;

  DeveloperResourceHubController({LocalResourceStorageSource? storageSource})
      : _storageSource = storageSource ?? LocalResourceStorageSource() {
    final raw = DeveloperResourcesDatabase.getAllResources();
    state = DeveloperResourceHubState(
      allResources: raw,
      filteredResources: raw,
      bookmarkedIds: {},
      isLoading: true,
    );
    _initBookmarks();
  }

  Future<void> _initBookmarks() async {
    final bookmarks = await _storageSource.getBookmarkedResourceIds();
    final updatedList = state.allResources.map((item) {
      return item.copyWith(isBookmarked: bookmarks.contains(item.id));
    }).toList();

    state = state.copyWith(
      allResources: updatedList,
      bookmarkedIds: bookmarks,
      isLoading: false,
    );
    _applyFilters();
  }

  void setSearchQuery(String query) {
    state = state.copyWith(searchQuery: query);
    _applyFilters();
  }

  void selectCategory(ResourceCategory? category) {
    state = state.copyWith(selectedCategory: () => category);
    _applyFilters();
  }

  void toggleOnlyBookmarked() {
    state = state.copyWith(onlyBookmarked: !state.onlyBookmarked);
    _applyFilters();
  }

  Future<void> toggleBookmark(String resourceId) async {
    final isNowBookmarked = await _storageSource.toggleBookmark(resourceId);
    final updatedIds = Set<String>.from(state.bookmarkedIds);
    if (isNowBookmarked) {
      updatedIds.add(resourceId);
    } else {
      updatedIds.remove(resourceId);
    }

    final updatedList = state.allResources.map((item) {
      if (item.id == resourceId) {
        return item.copyWith(isBookmarked: isNowBookmarked);
      }
      return item;
    }).toList();

    state = state.copyWith(
      allResources: updatedList,
      bookmarkedIds: updatedIds,
    );
    _applyFilters();
  }

  void _applyFilters() {
    final query = state.searchQuery.toLowerCase().trim();
    final cat = state.selectedCategory;
    final onlySaved = state.onlyBookmarked;

    final results = state.allResources.where((item) {
      // Category filter
      if (cat != null && item.category != cat) {
        return false;
      }

      // Bookmark filter
      if (onlySaved && !item.isBookmarked) {
        return false;
      }

      // Search query filter (matches name, owner, summaries, or tags)
      if (query.isNotEmpty) {
        final matchesName = item.name.toLowerCase().contains(query);
        final matchesOwner = item.owner.toLowerCase().contains(query);
        final matchesEn = item.summaryEn.toLowerCase().contains(query);
        final matchesAr = item.summaryAr.toLowerCase().contains(query);
        final matchesTags = item.tags.any((t) => t.toLowerCase().contains(query));
        return matchesName || matchesOwner || matchesEn || matchesAr || matchesTags;
      }

      return true;
    }).toList();

    state = state.copyWith(filteredResources: results);
    notifyListeners();
  }
}
