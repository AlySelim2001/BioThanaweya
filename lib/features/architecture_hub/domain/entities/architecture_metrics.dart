class ArchitectureMetrics {
  const ArchitectureMetrics({
    required this.chapterCount,
    required this.textbookCount,
    required this.examAttemptCount,
    this.fsrsReady = true,
    this.offlineReady = true,
  });

  final int chapterCount;
  final int textbookCount;
  final int examAttemptCount;
  final bool fsrsReady;
  final bool offlineReady;
}
