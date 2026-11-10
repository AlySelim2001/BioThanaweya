class ArchitectureBenchmarkResult {
  const ArchitectureBenchmarkResult({
    required this.totalRecordsQueried,
    required this.totalRecordsInserted,
    required this.averageReadLatencyMs,
    required this.averageWriteLatencyMs,
    required this.cacheHitRate,
    required this.databaseSizeKb,
  });

  final int totalRecordsQueried;
  final int totalRecordsInserted;
  final double averageReadLatencyMs;
  final double averageWriteLatencyMs;
  final double cacheHitRate;
  final int databaseSizeKb;
}
