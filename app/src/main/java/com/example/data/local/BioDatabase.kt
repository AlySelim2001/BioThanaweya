package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.ExamAttemptDao
import com.example.data.local.dao.FlashcardDao
import com.example.data.local.dao.ProgressDao
import com.example.data.local.dao.TopicMasteryDao
import com.example.data.local.entity.ExamAttemptEntity
import com.example.data.local.entity.FlashcardEntity
import com.example.data.local.entity.StudentProgressEntity
import com.example.data.local.entity.TopicMasteryEntity

import com.example.data.local.dao.StudyNoteDao
import com.example.data.local.entity.StudyNoteEntity

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
        TopicMasteryEntity::class,
        StudyNoteEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class BioDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
    abstract fun progressDao(): ProgressDao
    abstract fun examAttemptDao(): ExamAttemptDao
    abstract fun topicMasteryDao(): TopicMasteryDao
    abstract fun studyNoteDao(): StudyNoteDao

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
