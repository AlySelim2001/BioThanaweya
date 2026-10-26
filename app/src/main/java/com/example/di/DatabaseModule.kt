package com.example.di

import android.content.Context
import com.example.data.local.BioDatabase
import com.example.data.local.dao.ExamAttemptDao
import com.example.data.local.dao.FlashcardDao
import com.example.data.local.dao.ProgressDao
import com.example.data.local.dao.StudyNoteDao
import com.example.data.local.dao.TopicMasteryDao
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
    fun provideTopicMasteryDao(database: BioDatabase): TopicMasteryDao {
        return database.topicMasteryDao()
    }

    @Provides
    fun provideStudyNoteDao(database: BioDatabase): com.example.data.local.dao.StudyNoteDao {
        return database.studyNoteDao()
    }
}
