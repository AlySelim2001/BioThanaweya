// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'app_item.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

AppItem _$AppItemFromJson(Map<String, dynamic> json) {
  return _AppItem.fromJson(json);
}

/// @nodoc
mixin _$AppItem {
  /// Stable identifier used in the `/details/:id` route.
  String get id => throw _privateConstructorUsedError;

  /// Display title (Arabic curriculum name).
  String get title => throw _privateConstructorUsedError;

  /// Short English description of the topic.
  String get description => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AppItemCopyWith<AppItem> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AppItemCopyWith<$Res> {
  factory $AppItemCopyWith(AppItem value, $Res Function(AppItem) then) =
      _$AppItemCopyWithImpl<$Res, AppItem>;
  @useResult
  $Res call({String id, String title, String description});
}

/// @nodoc
class _$AppItemCopyWithImpl<$Res, $Val extends AppItem>
    implements $AppItemCopyWith<$Res> {
  _$AppItemCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? title = null,
    Object? description = null,
  }) {
    return _then(_value.copyWith(
      id: null == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      title: null == title
          ? _value.title
          : title // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$AppItemImplCopyWith<$Res> implements $AppItemCopyWith<$Res> {
  factory _$$AppItemImplCopyWith(
          _$AppItemImpl value, $Res Function(_$AppItemImpl) then) =
      __$$AppItemImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({String id, String title, String description});
}

/// @nodoc
class __$$AppItemImplCopyWithImpl<$Res>
    extends _$AppItemCopyWithImpl<$Res, _$AppItemImpl>
    implements _$$AppItemImplCopyWith<$Res> {
  __$$AppItemImplCopyWithImpl(
      _$AppItemImpl _value, $Res Function(_$AppItemImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? title = null,
    Object? description = null,
  }) {
    return _then(_$AppItemImpl(
      id: null == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String,
      title: null == title
          ? _value.title
          : title // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AppItemImpl implements _AppItem {
  const _$AppItemImpl(
      {required this.id, required this.title, required this.description});

  factory _$AppItemImpl.fromJson(Map<String, dynamic> json) =>
      _$$AppItemImplFromJson(json);

  /// Stable identifier used in the `/details/:id` route.
  @override
  final String id;

  /// Display title (Arabic curriculum name).
  @override
  final String title;

  /// Short English description of the topic.
  @override
  final String description;

  @override
  String toString() {
    return 'AppItem(id: $id, title: $title, description: $description)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AppItemImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.title, title) || other.title == title) &&
            (identical(other.description, description) ||
                other.description == description));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, id, title, description);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AppItemImplCopyWith<_$AppItemImpl> get copyWith =>
      __$$AppItemImplCopyWithImpl<_$AppItemImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AppItemImplToJson(
      this,
    );
  }
}

abstract class _AppItem implements AppItem {
  const factory _AppItem(
      {required final String id,
      required final String title,
      required final String description}) = _$AppItemImpl;

  factory _AppItem.fromJson(Map<String, dynamic> json) = _$AppItemImpl.fromJson;

  @override

  /// Stable identifier used in the `/details/:id` route.
  String get id;
  @override

  /// Display title (Arabic curriculum name).
  String get title;
  @override

  /// Short English description of the topic.
  String get description;
  @override
  @JsonKey(ignore: true)
  _$$AppItemImplCopyWith<_$AppItemImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
