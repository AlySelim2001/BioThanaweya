import 'dart:convert';

/// Categories reflecting the 8 core Git domains plus the 7-step roadmap.
enum GitCategory {
  tracking('Tracking Changes', 'تتبع التغييرات', 0xFF0D9488),
  branching('Branching', 'الفروع والمسارات', 0xFF06B6D4),
  collaboration('Remote Collaboration', 'التعاون السحابي والمستودعات', 0xFF3B82F6),
  undoRecovery('Undo & Recovery', 'التراجع والإنقاذ', 0xFFEF4444),
  advancedBranching('Advanced Branching', 'التفريع المتقدم والدمج', 0xFF8B5CF6),
  stashing('Stashing', 'التخزين المؤقت (Stash)', 0xFFF59E0B),
  debuggingInspection('Debugging & Inspection', 'الفحص والتحري البرمجي', 0xFF10B981),
  powerCommands('Power Commands', 'أوامر الخبراء والأتمتة', 0xFFEC4899),
  roadmap('7-Step Git Roadmap', 'خارطة طريق إتقان Git السبعة', 0xFF6366F1);

  final String titleEn;
  final String titleAr;
  final int colorHex;

  const GitCategory(this.titleEn, this.titleAr, this.colorHex);
}

/// Danger or impact level for a Git command
enum GitImpact {
  safe('Safe / Non-destructive', 'آمن / للقراءة فقط'),
  modifying('Modifying / Working Tree', 'يعدل الملفات'),
  dangerous('Destructive / Rewrites History', 'خطير / يمسح أو يعيد كتابة التاريخ');

  final String labelEn;
  final String labelAr;

  const GitImpact(this.labelEn, this.labelAr);
}

/// Immutable Entity representing a single Git Command from the infographic.
class GitCommand {
  final String id;
  final String command;
  final String summaryEn;
  final String summaryAr;
  final GitCategory category;
  final GitImpact impact;
  final List<String> commonFlags;
  final String? exampleUsage;
  final bool isBookmarked;

  const GitCommand({
    required this.id,
    required this.command,
    required this.summaryEn,
    required this.summaryAr,
    required this.category,
    this.impact = GitImpact.safe,
    this.commonFlags = const [],
    this.exampleUsage,
    this.isBookmarked = false,
  });

  GitCommand copyWith({
    String? id,
    String? command,
    String? summaryEn,
    String? summaryAr,
    GitCategory? category,
    GitImpact? impact,
    List<String>? commonFlags,
    String? exampleUsage,
    bool? isBookmarked,
  }) {
    return GitCommand(
      id: id ?? this.id,
      command: command ?? this.command,
      summaryEn: summaryEn ?? this.summaryEn,
      summaryAr: summaryAr ?? this.summaryAr,
      category: category ?? this.category,
      impact: impact ?? this.impact,
      commonFlags: commonFlags ?? this.commonFlags,
      exampleUsage: exampleUsage ?? this.exampleUsage,
      isBookmarked: isBookmarked ?? this.isBookmarked,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'command': command,
      'summaryEn': summaryEn,
      'summaryAr': summaryAr,
      'category': category.name,
      'impact': impact.name,
      'commonFlags': commonFlags,
      'exampleUsage': exampleUsage,
      'isBookmarked': isBookmarked,
    };
  }

  factory GitCommand.fromMap(Map<String, dynamic> map) {
    return GitCommand(
      id: map['id'] as String,
      command: map['command'] as String,
      summaryEn: map['summaryEn'] as String,
      summaryAr: map['summaryAr'] as String,
      category: GitCategory.values.firstWhere(
        (c) => c.name == map['category'],
        orElse: () => GitCategory.tracking,
      ),
      impact: GitImpact.values.firstWhere(
        (i) => i.name == map['impact'],
        orElse: () => GitImpact.safe,
      ),
      commonFlags: List<String>.from(map['commonFlags'] ?? []),
      exampleUsage: map['exampleUsage'] as String?,
      isBookmarked: map['isBookmarked'] as bool? ?? false,
    );
  }

  String toJson() => json.encode(toMap());

  factory GitCommand.fromJson(String source) =>
      GitCommand.fromMap(json.decode(source) as Map<String, dynamic>);
}
