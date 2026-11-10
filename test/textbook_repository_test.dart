import 'package:flutter_test/flutter_test.dart';
import 'package:smarts_bio/features/ministry_textbooks/domain/entities/ministry_textbook.dart';
import 'package:smarts_bio/features/ministry_textbooks/domain/repositories/textbook_repository.dart';
import 'package:smarts_bio/features/ministry_textbooks/presentation/controllers/textbook_controller.dart';

class MockTextbookRepository implements TextbookRepository {
  List<MinistryTextbook> mockBooks = [
    const MinistryTextbook(
      id: 'mock_1',
      titleAr: 'كتاب الأحياء التجريبي',
      fileName: 'mock_bio.pdf',
      remoteUrl: 'https://example.com/mock_bio.pdf',
      fileSizeBytes: 20 * 1024 * 1024,
      isDownloaded: false,
      totalPages: 100,
      lastReadPage: 25,
    ),
  ];

  @override
  Future<List<MinistryTextbook>> getAvailableTextbooks() async {
    return mockBooks;
  }

  @override
  Future<void> downloadTextbook(MinistryTextbook textbook, {void Function(double progress)? onProgress}) async {
    if (onProgress != null) onProgress(1.0);
    mockBooks = mockBooks.map((b) => b.id == textbook.id ? b.copyWith(isDownloaded: true, localPath: '/tmp/${b.fileName}') : b).toList();
  }

  @override
  Future<void> updateReadingProgress(String textbookId, int pageNumber) async {
    mockBooks = mockBooks.map((b) => b.id == textbookId ? b.copyWith(lastReadPage: pageNumber) : b).toList();
  }

  @override
  Future<void> deleteDownloadedTextbook(String textbookId) async {
    mockBooks = mockBooks.map((b) => b.id == textbookId ? b.copyWith(isDownloaded: false, localPath: null) : b).toList();
  }
}

void main() {
  group('Ministry Textbook Offline Architecture Tests', () {
    test('MinistryTextbook formats file size accurately in MB and KB', () {
      const mbBook = MinistryTextbook(
        id: '1',
        titleAr: 'كتاب كبير',
        fileName: 'large.pdf',
        remoteUrl: '',
        fileSizeBytes: 15 * 1024 * 1024,
        isDownloaded: false,
        totalPages: 200,
        lastReadPage: 50,
      );
      expect(mbBook.formattedFileSize, equals('15.0 ميجابايت'));

      const kbBook = MinistryTextbook(
        id: '2',
        titleAr: 'كتيب صغير',
        fileName: 'small.pdf',
        remoteUrl: '',
        fileSizeBytes: 500 * 1024,
        isDownloaded: false,
        totalPages: 20,
        lastReadPage: 10,
      );
      expect(kbBook.formattedFileSize, equals('500 كيلوبايت'));
    });

    test('MinistryTextbook computes reading progress accurately', () {
      const book = MinistryTextbook(
        id: '1',
        titleAr: 'كتاب الأحياء',
        fileName: 'bio.pdf',
        remoteUrl: '',
        fileSizeBytes: 1024,
        isDownloaded: true,
        totalPages: 100,
        lastReadPage: 40,
      );
      expect(book.readingProgressPercentage, equals(0.4));
    });

    test('TextbookController loads textbooks and handles download toggle', () async {
      final mockRepo = MockTextbookRepository();
      final controller = TextbookController(repository: mockRepo);

      await Future.delayed(const Duration(milliseconds: 10));
      expect(controller.state.textbooks.isNotEmpty, isTrue);
      expect(controller.state.textbooks.first.isDownloaded, isFalse);

      await controller.downloadBook(controller.state.textbooks.first);
      expect(controller.state.textbooks.first.isDownloaded, isTrue);

      await controller.updatePage('mock_1', 60);
      expect(controller.state.textbooks.first.lastReadPage, equals(60));

      await controller.deleteBook('mock_1');
      expect(controller.state.textbooks.first.isDownloaded, isFalse);
    });
  });
}
