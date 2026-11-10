import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../domain/entities/ministry_textbook.dart';
import '../../domain/repositories/textbook_repository.dart';
import '../../data/repositories/textbook_repository_impl.dart';

class TextbookState {
  final List<MinistryTextbook> textbooks;
  final Map<String, double> downloadProgress;
  final bool isLoading;

  const TextbookState({
    required this.textbooks,
    this.downloadProgress = const {},
    this.isLoading = false,
  });

  TextbookState copyWith({
    List<MinistryTextbook>? textbooks,
    Map<String, double>? downloadProgress,
    bool? isLoading,
  }) {
    return TextbookState(
      textbooks: textbooks ?? this.textbooks,
      downloadProgress: downloadProgress ?? this.downloadProgress,
      isLoading: isLoading ?? this.isLoading,
    );
  }
}

class TextbookController extends StateNotifier<TextbookState> {
  final TextbookRepository _repository;

  TextbookController({TextbookRepository? repository})
      : _repository = repository ?? TextbookRepositoryImpl(),
        super(const TextbookState(textbooks: [])) {
    loadTextbooks();
  }

  Future<void> loadTextbooks() async {
    state = state.copyWith(isLoading: true);
    final books = await _repository.getAvailableTextbooks();
    state = state.copyWith(textbooks: books, isLoading: false);
  }

  Future<void> downloadBook(MinistryTextbook book) async {
    final updatedProgress = Map<String, double>.from(state.downloadProgress);
    updatedProgress[book.id] = 0.05;
    state = state.copyWith(downloadProgress: updatedProgress);

    await _repository.downloadTextbook(
      book,
      onProgress: (p) {
        final currentMap = Map<String, double>.from(state.downloadProgress);
        currentMap[book.id] = p;
        state = state.copyWith(downloadProgress: currentMap);
      },
    );

    final finalProgress = Map<String, double>.from(state.downloadProgress);
    finalProgress.remove(book.id);
    final refreshedBooks = await _repository.getAvailableTextbooks();
    state = state.copyWith(textbooks: refreshedBooks, downloadProgress: finalProgress);
  }

  Future<void> updatePage(String bookId, int page) async {
    await _repository.updateReadingProgress(bookId, page);
    final refreshedBooks = await _repository.getAvailableTextbooks();
    state = state.copyWith(textbooks: refreshedBooks);
  }

  Future<void> deleteBook(String bookId) async {
    await _repository.deleteDownloadedTextbook(bookId);
    final refreshedBooks = await _repository.getAvailableTextbooks();
    state = state.copyWith(textbooks: refreshedBooks);
  }
}

final textbookRepositoryProvider = Provider<TextbookRepository>((ref) {
  return TextbookRepositoryImpl();
});

final textbookControllerProvider =
    StateNotifierProvider<TextbookController, TextbookState>((ref) {
  final repo = ref.watch(textbookRepositoryProvider);
  return TextbookController(repository: repo);
});
