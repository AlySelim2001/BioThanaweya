import 'package:shared_preferences/shared_preferences.dart';

/// Local offline storage contract and implementation for persisting bookmarked commands
class LocalGitStorageSource {
  static const String _bookmarkKey = 'biothanaweya_git_bookmarks_v1';

  /// Retrieves list of bookmarked command IDs
  Future<Set<String>> getBookmarkedCommandIds() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final list = prefs.getStringList(_bookmarkKey) ?? [];
      return list.toSet();
    } catch (_) {
      return <String>{};
    }
  }

  /// Toggles bookmark state for a given command ID
  Future<bool> toggleBookmark(String commandId) async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final currentList = prefs.getStringList(_bookmarkKey) ?? [];
      final currentSet = currentList.toSet();

      final isBookmarked = currentSet.contains(commandId);
      if (isBookmarked) {
        currentSet.remove(commandId);
      } else {
        currentSet.add(commandId);
      }

      await prefs.setStringList(_bookmarkKey, currentSet.toList());
      return !isBookmarked;
    } catch (_) {
      return false;
    }
  }

  /// Checks if a command ID is bookmarked
  Future<bool> isBookmarked(String commandId) async {
    final set = await getBookmarkedCommandIds();
    return set.contains(commandId);
  }
}
