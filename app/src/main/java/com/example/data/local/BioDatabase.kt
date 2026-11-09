package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.*
import com.example.data.local.entity.*

// Typealiases for seamless backwards compatibility
typealias FlashcardEntity = com.example.data.local.entity.FlashcardEntity
typealias StudentProgressEntity = com.example.data.local.entity.StudentProgressEntity
typealias FlashcardDao = com.example.data.local.dao.FlashcardDao
typealias ProgressDao = com.example.data.local.dao.ProgressDao

@Database(
    entities = [
        FlashcardEntity::class,
        StudentProgressEntity::class,
        ExamAttemptEntity::class,
        ExamMistakeEntity::class,
        TopicMasteryEntity::class,
        StudyNoteEntity::class,
        SavedBiologyDefinitionEntity::class,
        KeyConceptEntity::class,
        ChapterProgress::class,
        ChapterContentEntity::class,
        SearchHistoryEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class BioDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
    abstract fun progressDao(): ProgressDao
    abstract fun examAttemptDao(): ExamAttemptDao
    abstract fun examMistakeDao(): ExamMistakeDao
    abstract fun topicMasteryDao(): TopicMasteryDao
    abstract fun studyNoteDao(): StudyNoteDao
    abstract fun savedDefinitionDao(): SavedDefinitionDao
    abstract fun keyConceptDao(): KeyConceptDao
    abstract fun chapterProgressDao(): ChapterProgressDao
    abstract fun chapterContentDao(): ChapterContentDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: BioDatabase? = null

        fun getInstance(context: Context): BioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BioDatabase::class.java,
                    "bio_thanaweya.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
