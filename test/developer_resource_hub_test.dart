import 'package:flutter_test/flutter_test.dart';
import 'package:smarts_bio/features/developer_resources/domain/entities/developer_resource_model.dart';
import 'package:smarts_bio/features/developer_resources/data/datasources/developer_resources_database.dart';
import 'package:smarts_bio/features/developer_resources/data/datasources/local_resource_storage_source.dart';
import 'package:smarts_bio/features/developer_resources/presentation/controllers/developer_resource_hub_controller.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('Developer Resource Hub Domain, Database & Controller Tests', () {
    setUp(() {
      SharedPreferences.setMockInitialValues({});
    });

    test('DeveloperResourcesDatabase loads all 25 curated repositories across 4 categories', () {
      final repos = DeveloperResourcesDatabase.getAllResources();
      expect(repos.length, equals(25));

      // Verify category coverage
      for (final cat in ResourceCategory.values) {
        final matches = repos.where((r) => r.category == cat).toList();
        expect(matches.isNotEmpty, isTrue, reason: 'Category ${cat.name} must have repositories');
      }

      // Check key infographic repositories
      expect(repos.any((r) => r.name == 'freeCodeCamp'), isTrue);
      expect(repos.any((r) => r.name == 'developer-roadmap'), isTrue);
      expect(repos.any((r) => r.name == 'public-apis'), isTrue);
      expect(repos.any((r) => r.name == 'system-design-primer'), isTrue);
      expect(repos.any((r) => r.name == 'build-your-own-x'), isTrue);
      expect(repos.any((r) => r.name == 'app-ideas'), isTrue);
      expect(repos.any((r) => r.name == 'clean-code-javascript'), isTrue);
    });

    test('DevResourceItem entity serialization and copyWith', () {
      const item = DevResourceItem(
        id: 'repo_test',
        name: 'test-repo',
        owner: 'test-owner',
        repoUrl: 'https://github.com/test-owner/test-repo',
        summaryEn: 'Test summary',
        summaryAr: 'ملخص تجريبي',
        category: ResourceCategory.learn,
        starsApprox: '100k+',
      );

      final map = item.toMap();
      final revived = DevResourceItem.fromMap(map);

      expect(revived.id, equals(item.id));
      expect(revived.name, equals(item.name));
      expect(revived.category, equals(item.category));
      expect(revived.starsApprox, equals(item.starsApprox));

      final bookmarked = item.copyWith(isBookmarked: true);
      expect(bookmarked.isBookmarked, isTrue);
    });

    test('LocalResourceStorageSource toggles and persists bookmarks', () async {
      final storage = LocalResourceStorageSource();

      final initialCheck = await storage.isBookmarked('repo_freecodecamp');
      expect(initialCheck, isFalse);

      final toggle1 = await storage.toggleBookmark('repo_freecodecamp');
      expect(toggle1, isTrue);

      final checkAfterToggle = await storage.isBookmarked('repo_freecodecamp');
      expect(checkAfterToggle, isTrue);

      final toggle2 = await storage.toggleBookmark('repo_freecodecamp');
      expect(toggle2, isFalse);

      final checkAfterToggle2 = await storage.isBookmarked('repo_freecodecamp');
      expect(checkAfterToggle2, isFalse);
    });

    test('DeveloperResourceHubController filters by search query and category', () async {
      final controller = DeveloperResourceHubController();
      await Future.delayed(const Duration(milliseconds: 50));

      expect(controller.state.filteredResources.length, equals(25));

      // Filter by search query
      controller.setSearchQuery('roadmap');
      expect(controller.state.filteredResources.any((r) => r.name.contains('roadmap')), isTrue);

      // Filter by category
      controller.setSearchQuery('');
      controller.selectCategory(ResourceCategory.apisTools);
      expect(controller.state.filteredResources.every((r) => r.category == ResourceCategory.apisTools), isTrue);
    });
  });
}
