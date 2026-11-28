import 'package:biothanaweya_mobile/models/app_item.dart';
import 'package:biothanaweya_mobile/screens/details_screen.dart';
import 'package:biothanaweya_mobile/screens/home_screen.dart';
import 'package:biothanaweya_mobile/screens/quiz_screen.dart';
import 'package:go_router/go_router.dart';

AppItem? _findMockItem(String id) {
  for (final item in mockAppItems) {
    if (item.id == id) {
      return item;
    }
  }
  return null;
}

/// The single go_router instance: `/`, `/details/:id`, and `/quiz`.
final appRouter = GoRouter(
  routes: <RouteBase>[
    GoRoute(
      path: '/',
      builder: (context, state) => const HomeScreen(),
    ),
    GoRoute(
      path: '/details/:id',
      builder: (context, state) {
        final id = state.pathParameters['id'] ?? '';
        final extra = state.extra;
        return DetailsScreen(
          id: id,
          item: extra is AppItem ? extra : _findMockItem(id),
        );
      },
    ),
    GoRoute(
      path: '/quiz',
      builder: (context, state) => const QuizScreen(),
    ),
  ],
);
