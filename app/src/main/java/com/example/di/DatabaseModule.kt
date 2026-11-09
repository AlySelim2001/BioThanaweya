package com.example.di

import android.content.Context
import com.example.data.local.BioDatabase
import com.example.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBioDatabase(@ApplicationContext context: Context): BioDatabase {
        return BioDatabase.getInstance(context)
    }

    @Provides
    fun provideFlashcardDao(database: BioDatabase): FlashcardDao {
        return database.flashcardDao()
    }

    @Provides
    fun provideProgressDao(database: BioDatabase): ProgressDao {
        return database.progressDao()
    }

    @Provides
    fun provideExamAttemptDao(database: BioDatabase): ExamAttemptDao {
        return database.examAttemptDao()
    }

    @Provides
    fun provideExamMistakeDao(database: BioDatabase): ExamMistakeDao {
        return database.examMistakeDao()
    }

    @Provides
    fun provideTopicMasteryDao(database: BioDatabase): TopicMasteryDao {
        return database.topicMasteryDao()
    }

    @Provides
    fun provideStudyNoteDao(database: BioDatabase): StudyNoteDao {
        return database.studyNoteDao()
    }

    @Provides
    fun provideSavedDefinitionDao(database: BioDatabase): SavedDefinitionDao {
        return database.savedDefinitionDao()
    }

    @Provides
    fun provideKeyConceptDao(database: BioDatabase): KeyConceptDao {
        return database.keyConceptDao()
    }

    @Provides
    fun provideChapterProgressDao(database: BioDatabase): ChapterProgressDao {
        return database.chapterProgressDao()
    }

    @Provides
    fun provideChapterContentDao(database: BioDatabase): ChapterContentDao {
        return database.chapterContentDao()
    }

    @Provides
    fun provideSearchHistoryDao(database: BioDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}
