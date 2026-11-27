import 'package:biothanaweya_mobile/l10n/app_localizations.dart';
import 'package:biothanaweya_mobile/models/app_item.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

/// Shows a single [AppItem].
///
/// [item] is supplied through the router's `extra` on in-app navigation; when
/// the screen is opened from a deep link it is null and only [id] is available.
class DetailsScreen extends StatelessWidget {
  /// Creates the details screen for [id].
  const DetailsScreen({required this.id, this.item, super.key});

  /// Identifier of the item being shown.
  final String id;

  /// The item to display, when available.
  final AppItem? item;

  @override
  Widget build(BuildContext context) {
    final l10n = AppLocalizations.of(context)!;
    final current = item;
    return Scaffold(
      appBar: AppBar(title: Text(current?.title ?? l10n.detailsTitle)),
      body: current == null
          ? Center(child: Text('${l10n.detailsTitle}: $id'))
          : ListView(
              padding: const EdgeInsets.all(16),
              children: [
                Text(
                  current.title,
                  style: Theme.of(context).textTheme.headlineSmall,
                ),
                const SizedBox(height: 12),
                Text(current.description),
                const SizedBox(height: 24),
                ElevatedButton(
                  // `go()` does not stack pages, so fall back to the home route
                  // when there is nothing to pop (deep link).
                  onPressed: () => context.canPop()
                      ? context.pop()
                      : context.go('/'),
                  child: Text(l10n.back),
                ),
              ],
            ),
    );
  }
}
