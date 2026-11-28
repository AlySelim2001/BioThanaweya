import 'package:flutter/foundation.dart';
import '../../domain/entities/git_command.dart';
import '../../data/datasources/git_commands_database.dart';
import '../../data/datasources/local_git_storage_source.dart';

class GitCheatSheetState {
  final List<GitCommand> allCommands;
  final List<GitCommand> filteredCommands;
  final Set<String> bookmarkedIds;
  final GitCategory? selectedCategory;
  final String searchQuery;
  final bool onlyBookmarked;
  final bool isLoading;

  const GitCheatSheetState({
    required this.allCommands,
    required this.filteredCommands,
    required this.bookmarkedIds,
    this.selectedCategory,
    this.searchQuery = '',
    this.onlyBookmarked = false,
    this.isLoading = false,
  });

  GitCheatSheetState copyWith({
    List<GitCommand>? allCommands,
    List<GitCommand>? filteredCommands,
    Set<String>? bookmarkedIds,
    GitCategory? Function()? selectedCategory,
    String? searchQuery,
    bool? onlyBookmarked,
    bool? isLoading,
  }) {
    return GitCheatSheetState(
      allCommands: allCommands ?? this.allCommands,
      filteredCommands: filteredCommands ?? this.filteredCommands,
      bookmarkedIds: bookmarkedIds ?? this.bookmarkedIds,
      selectedCategory:
          selectedCategory != null ? selectedCategory() : this.selectedCategory,
      searchQuery: searchQuery ?? this.searchQuery,
      onlyBookmarked: onlyBookmarked ?? this.onlyBookmarked,
      isLoading: isLoading ?? this.isLoading,
    );
  }
}

class GitCheatSheetController extends ChangeNotifier {
  final LocalGitStorageSource _storageSource;
  late GitCheatSheetState state;

  GitCheatSheetController({LocalGitStorageSource? storageSource})
      : _storageSource = storageSource ?? LocalGitStorageSource() {
    final raw = GitCommandsDatabase.getAllCommands();
    state = GitCheatSheetState(
      allCommands: raw,
      filteredCommands: raw,
      bookmarkedIds: {},
      isLoading: true,
    );
    _initBookmarks();
  }

  Future<void> _initBookmarks() async {
    final bookmarks = await _storageSource.getBookmarkedCommandIds();
    final updatedCommands = state.allCommands.map((cmd) {
      return cmd.copyWith(isBookmarked: bookmarks.contains(cmd.id));
    }).toList();

    state = state.copyWith(
      allCommands: updatedCommands,
      bookmarkedIds: bookmarks,
      isLoading: false,
    );
    _applyFilters();
  }

  void setSearchQuery(String query) {
    state = state.copyWith(searchQuery: query);
    _applyFilters();
  }

  void selectCategory(GitCategory? category) {
    state = state.copyWith(selectedCategory: () => category);
    _applyFilters();
  }

  void toggleOnlyBookmarked() {
    state = state.copyWith(onlyBookmarked: !state.onlyBookmarked);
    _applyFilters();
  }

  Future<void> toggleBookmark(String commandId) async {
    final isNowBookmarked = await _storageSource.toggleBookmark(commandId);
    final updatedIds = Set<String>.from(state.bookmarkedIds);
    if (isNowBookmarked) {
      updatedIds.add(commandId);
    } else {
      updatedIds.remove(commandId);
    }

    final updatedCommands = state.allCommands.map((cmd) {
      if (cmd.id == commandId) {
        return cmd.copyWith(isBookmarked: isNowBookmarked);
      }
      return cmd;
    }).toList();

    state = state.copyWith(
      allCommands: updatedCommands,
      bookmarkedIds: updatedIds,
    );
    _applyFilters();
  }

  void _applyFilters() {
    final query = state.searchQuery.toLowerCase().trim();
    final cat = state.selectedCategory;
    final onlySaved = state.onlyBookmarked;

    final results = state.allCommands.filter((cmd) {
      // Category filter
      if (cat != null && cmd.category != cat) {
        return false;
      }

      // Bookmark filter
      if (onlySaved && !cmd.isBookmarked) {
        return false;
      }

      // Search text query
      if (query.isNotEmpty) {
        final matchesCmd = cmd.command.toLowerCase().contains(query);
        final matchesEn = cmd.summaryEn.toLowerCase().contains(query);
        final matchesAr = cmd.summaryAr.toLowerCase().contains(query);
        final matchesFlags = cmd.commonFlags.any((f) => f.toLowerCase().contains(query));
        return matchesCmd || matchesEn || matchesAr || matchesFlags;
      }

      return true;
    }).toList();

    state = state.copyWith(filteredCommands: results);
    notifyListeners();
  }
}

extension _IterableExtension<E> on Iterable<E> {
  Iterable<E> filter(bool Function(E element) test) sync* {
    for (E element in this) {
      if (test(element)) yield element;
    }
  }
}
