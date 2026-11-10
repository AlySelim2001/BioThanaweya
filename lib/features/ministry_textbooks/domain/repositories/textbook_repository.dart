import '../entities/ministry_textbook.dart';

/// واجهة مستودع كتب الوزارة الرسمية
abstract class TextbookRepository {
  Future<List<MinistryTextbook>> getAvailableTextbooks();
  Future<void> downloadTextbook(MinistryTextbook textbook, {void Function(double progress)? onProgress});
  Future<void> updateReadingProgress(String textbookId, int pageNumber);
  Future<void> deleteDownloadedTextbook(String textbookId);
}
