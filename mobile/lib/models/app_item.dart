import 'package:freezed_annotation/freezed_annotation.dart';

part 'app_item.freezed.dart';
part 'app_item.g.dart';

/// A curriculum entry shown on the home screen and opened in the details
/// screen.
@freezed
class AppItem with _$AppItem {
  /// Creates an [AppItem].
  const factory AppItem({
    /// Stable identifier used in the `/details/:id` route.
    required String id,

    /// Display title (Arabic curriculum name).
    required String title,

    /// Short English description of the topic.
    required String description,
  }) = _AppItem;

  /// Deserializes an [AppItem] from JSON.
  factory AppItem.fromJson(Map<String, dynamic> json) =>
      _$AppItemFromJson(json);
}
