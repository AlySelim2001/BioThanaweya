import 'package:freezed_annotation/freezed_annotation.dart';

part 'item.freezed.dart';
part 'item.g.dart';

/// A minimal placeholder model kept for the freezed/json_serializable
/// example.
@freezed
class Item with _$Item {
  /// Creates an [Item].
  const factory Item({
    /// Numeric identifier.
    required int id,

    /// Display title.
    required String title,
  }) = _Item;

  /// Deserializes an [Item] from JSON.
  factory Item.fromJson(Map<String, dynamic> json) => _$ItemFromJson(json);
}
