import 'package:shared_preferences/shared_preferences.dart';

/// Local offline storage contract and implementation for persisting bookmarked developer resources
class LocalResourceStorageSource {
  static const String _bookmarkKey = 'biothanaweya_dev_resource_bookmarks_v1';

  /// Retrieves set of bookmarked resource IDs
  Future<Set<String>> getBookmarkedResourceIds() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final list = prefs.getStringList(_bookmarkKey) ?? [];
      return list.toSet();
    } catch (_) {
      return <String>{};
    }
  }

  /// Toggles bookmark state for a given resource ID
  Future<bool> toggleBookmark(String resourceId) async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final currentList = prefs.getStringList(_bookmarkKey) ?? [];
      final currentSet = currentList.toSet();

      final isBookmarked = currentSet.contains(resourceId);
      if (isBookmarked) {
        currentSet.remove(resourceId);
      } else {
        currentSet.add(resourceId);
      }

      await prefs.setStringList(_bookmarkKey, currentSet.toList());
      return !isBookmarked;
    } catch (_) {
      return false;
    }
  }

  /// Checks if a resource is bookmarked
  Future<bool> isBookmarked(String resourceId) async {
    final set = await getBookmarkedResourceIds();
    return set.contains(resourceId);
  }
}
