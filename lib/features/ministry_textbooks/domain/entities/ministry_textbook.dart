import 'package:flutter/foundation.dart';

/// كيان كتاب الوزارة الرسمي أو مجلد المفاهيم
@immutable
class MinistryTextbook {
  final String id;
  final String titleAr;
  final String fileName;
  final String remoteUrl;
  final String? localPath;
  final int fileSizeBytes;
  final bool isDownloaded;
  final int totalPages;
  final int lastReadPage;

  const MinistryTextbook({
    required this.id,
    required this.titleAr,
    required this.fileName,
    required this.remoteUrl,
    this.localPath,
    required this.fileSizeBytes,
    required this.isDownloaded,
    required this.totalPages,
    required this.lastReadPage,
  });

  String get formattedFileSize {
    if (fileSizeBytes >= 1024 * 1024) {
      final mb = fileSizeBytes / (1024 * 1024);
      return '${mb.toStringAsFixed(1)} ميجابايت';
    } else {
      final kb = fileSizeBytes / 1024;
      return '${kb.toStringAsFixed(0)} كيلوبايت';
    }
  }

  double get readingProgressPercentage {
    if (totalPages <= 0) return 0.0;
    return (lastReadPage / totalPages).clamp(0.0, 1.0);
  }

  MinistryTextbook copyWith({
    String? id,
    String? titleAr,
    String? fileName,
    String? remoteUrl,
    String? localPath,
    int? fileSizeBytes,
    bool? isDownloaded,
    int? totalPages,
    int? lastReadPage,
  }) {
    return MinistryTextbook(
      id: id ?? this.id,
      titleAr: titleAr ?? this.titleAr,
      fileName: fileName ?? this.fileName,
      remoteUrl: remoteUrl ?? this.remoteUrl,
      localPath: localPath ?? this.localPath,
      fileSizeBytes: fileSizeBytes ?? this.fileSizeBytes,
      isDownloaded: isDownloaded ?? this.isDownloaded,
      totalPages: totalPages ?? this.totalPages,
      lastReadPage: lastReadPage ?? this.lastReadPage,
    );
  }
}
