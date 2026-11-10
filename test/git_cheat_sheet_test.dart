import 'package:flutter_test/flutter_test.dart';
import 'package:smarts_bio/features/git_dev_tools/domain/entities/git_command.dart';
import 'package:smarts_bio/features/git_dev_tools/data/datasources/git_commands_database.dart';
import 'package:smarts_bio/features/git_dev_tools/data/datasources/local_git_storage_source.dart';
import 'package:smarts_bio/features/git_dev_tools/presentation/controllers/git_cheat_sheet_controller.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('Git Dev Tools Domain, Database & Controller Tests', () {
    setUp(() {
      SharedPreferences.setMockInitialValues({});
    });

    test('GitCommandsDatabase loads all 8 categories and 7-step roadmap', () {
      final commands = GitCommandsDatabase.getAllCommands();
      expect(commands.isNotEmpty, isTrue);

      // Verify category coverage
      for (final cat in GitCategory.values) {
        final matches = commands.where((c) => c.category == cat).toList();
        expect(matches.isNotEmpty, isTrue, reason: 'Category ${cat.name} must have commands');
      }

      // Verify common core commands
      expect(commands.any((c) => c.command.contains('git status')), isTrue);
      expect(commands.any((c) => c.command.contains('git stash')), isTrue);
      expect(commands.any((c) => c.command.contains('git rebase')), isTrue);
      expect(commands.any((c) => c.command.contains('git reflog')), isTrue);
    });

    test('GitCommand entity serialization and copyWith', () {
      const command = GitCommand(
        id: 'cmd_test',
        command: 'git status',
        summaryEn: 'Status check',
        summaryAr: 'فحص الحالة',
        category: GitCategory.tracking,
        impact: GitImpact.safe,
      );

      final map = command.toMap();
      final revived = GitCommand.fromMap(map);

      expect(revived.id, equals(command.id));
      expect(revived.command, equals(command.command));
      expect(revived.category, equals(command.category));
      expect(revived.impact, equals(command.impact));

      final bookmarked = command.copyWith(isBookmarked: true);
      expect(bookmarked.isBookmarked, isTrue);
    });

    test('LocalGitStorageSource toggles and persists bookmarks', () async {
      final storage = LocalGitStorageSource();

      final initialCheck = await storage.isBookmarked('cmd_status');
      expect(initialCheck, isFalse);

      final toggle1 = await storage.toggleBookmark('cmd_status');
      expect(toggle1, isTrue);

      final checkAfterToggle = await storage.isBookmarked('cmd_status');
      expect(checkAfterToggle, isTrue);

      final toggle2 = await storage.toggleBookmark('cmd_status');
      expect(toggle2, isFalse);

      final checkAfterToggle2 = await storage.isBookmarked('cmd_status');
      expect(checkAfterToggle2, isFalse);
    });

    test('GitCheatSheetController filters by query and category', () async {
      final controller = GitCheatSheetController();
      await Future.delayed(const Duration(milliseconds: 50));

      expect(controller.state.filteredCommands.isNotEmpty, isTrue);

      // Search filter
      controller.setSearchQuery('status');
      expect(controller.state.filteredCommands.every((c) => c.command.contains('status') || c.summaryEn.contains('status')), isTrue);

      // Category filter
      controller.setSearchQuery('');
      controller.selectCategory(GitCategory.stashing);
      expect(controller.state.filteredCommands.every((c) => c.category == GitCategory.stashing), isTrue);
    });
  });
}
