import 'dart:convert';

/// Primary classification categories for developer resources
enum ResourceCategory {
  learn('Learn & Roadmaps', 'التعلم والخرائط', 0xFF0D9488),
  build('Build & Projects', 'المشاريع والتطبيق', 0xFF06B6D4),
  systemDesign('System Design & Code', 'المعمارية والكود النظيف', 0xFF8B5CF6),
  apisTools('APIs & Tools', 'الواجهات والأدوات', 0xFFF59E0B);

  final String titleEn;
  final String titleAr;
  final int colorHex;

  const ResourceCategory(this.titleEn, this.titleAr, this.colorHex);
}

/// Difficulty or audience level
enum ResourceLevel {
  beginner('Beginner Friendly', 'مناسب للمبتدئين'),
  intermediate('Intermediate', 'متوسط'),
  advanced('Advanced / Enterprise', 'متقدم ومحترف');

  final String labelEn;
  final String labelAr;

  const ResourceLevel(this.labelEn, this.labelAr);
}

/// Immutable Entity representing a Curated GitHub Developer Repository
class DevResourceItem {
  final String id;
  final String name;
  final String owner;
  final String repoUrl;
  final String summaryEn;
  final String summaryAr;
  final ResourceCategory category;
  final ResourceLevel level;
  final String starsApprox;
  final List<String> tags;
  final bool isBookmarked;

  const DevResourceItem({
    required this.id,
    required this.name,
    required this.owner,
    required this.repoUrl,
    required this.summaryEn,
    required this.summaryAr,
    required this.category,
    this.level = ResourceLevel.beginner,
    required this.starsApprox,
    this.tags = const [],
    this.isBookmarked = false,
  });

  DevResourceItem copyWith({
    String? id,
    String? name,
    String? owner,
    String? repoUrl,
    String? summaryEn,
    String? summaryAr,
    ResourceCategory? category,
    ResourceLevel? level,
    String? starsApprox,
    List<String>? tags,
    bool? isBookmarked,
  }) {
    return DevResourceItem(
      id: id ?? this.id,
      name: name ?? this.name,
      owner: owner ?? this.owner,
      repoUrl: repoUrl ?? this.repoUrl,
      summaryEn: summaryEn ?? this.summaryEn,
      summaryAr: summaryAr ?? this.summaryAr,
      category: category ?? this.category,
      level: level ?? this.level,
      starsApprox: starsApprox ?? this.starsApprox,
      tags: tags ?? this.tags,
      isBookmarked: isBookmarked ?? this.isBookmarked,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'owner': owner,
      'repoUrl': repoUrl,
      'summaryEn': summaryEn,
      'summaryAr': summaryAr,
      'category': category.name,
      'level': level.name,
      'starsApprox': starsApprox,
      'tags': tags,
      'isBookmarked': isBookmarked,
    };
  }

  factory DevResourceItem.fromMap(Map<String, dynamic> map) {
    return DevResourceItem(
      id: map['id'] as String,
      name: map['name'] as String,
      owner: map['owner'] as String,
      repoUrl: map['repoUrl'] as String,
      summaryEn: map['summaryEn'] as String,
      summaryAr: map['summaryAr'] as String,
      category: ResourceCategory.values.firstWhere(
        (c) => c.name == map['category'],
        orElse: () => ResourceCategory.learn,
      ),
      level: ResourceLevel.values.firstWhere(
        (l) => l.name == map['level'],
        orElse: () => ResourceLevel.beginner,
      ),
      starsApprox: map['starsApprox'] as String? ?? '100k+',
      tags: List<String>.from(map['tags'] ?? []),
      isBookmarked: map['isBookmarked'] as bool? ?? false,
    );
  }

  String toJson() => json.encode(toMap());

  factory DevResourceItem.fromJson(String source) =>
      DevResourceItem.fromMap(json.decode(source) as Map<String, dynamic>);
}
