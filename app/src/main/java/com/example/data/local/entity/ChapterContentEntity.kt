package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.util.ArabicSearchUtils

/**
 * ChapterContentEntity:
 * Room entity holding curriculum sections, concepts, and detailed high-yield syllabus
 * text for full-text offline search across the 11 Egyptian Thanawya Amma Biology chapters.
 */
@Entity(
    tableName = "chapter_contents",
    indices = [
        Index(value = ["chapterId"]),
        Index(value = ["unitNumber"]),
        Index(value = ["chapterKey"])
    ]
)
data class ChapterContentEntity(
    @PrimaryKey
    val id: String,                         // e.g. "chap1_sec1_plant_support"
    val chapterId: Int,                     // 1 to 11
    val chapterKey: String,                 // e.g. "support_movement"
    val unitNumber: Int,                    // 1 = التركيب والوظيفة, 2 = البيولوجيا الجزيئية
    val chapterTitleAr: String,             // e.g. "الدعامة والحركة في الكائنات الحية"
    val chapterTitleEn: String,             // e.g. "Support and Movement"
    val sectionTitleAr: String,             // e.g. "الدعامة الفسيولوجية والتركيبية في النبات"
    val sectionTitleEn: String,             // e.g. "Physiological & Structural Support in Plants"
    val contentAr: String,                  // Full comprehensive Arabic explanation
    val keywordsAr: String,                 // High-yield search terms
    val examNotesAr: String = "",           // Ministry traps & exam secrets
    val scientificFormulas: String = "",    // Rules / calculations
    val normalizedSearchIndex: String = "", // Pre-computed search index string
    val importanceScore: Int = 1            // Search ranking weight
) {
    companion object {
        fun createWithIndex(
            id: String,
            chapterId: Int,
            chapterKey: String,
            unitNumber: Int,
            chapterTitleAr: String,
            chapterTitleEn: String,
            sectionTitleAr: String,
            sectionTitleEn: String,
            contentAr: String,
            keywordsAr: String,
            examNotesAr: String = "",
            scientificFormulas: String = "",
            importanceScore: Int = 1
        ): ChapterContentEntity {
            val rawCombined = "$chapterTitleAr $chapterTitleEn $sectionTitleAr $sectionTitleEn $keywordsAr $contentAr $examNotesAr $scientificFormulas"
            val index = ArabicSearchUtils.normalize(rawCombined)
            return ChapterContentEntity(
                id = id,
                chapterId = chapterId,
                chapterKey = chapterKey,
                unitNumber = unitNumber,
                chapterTitleAr = chapterTitleAr,
                chapterTitleEn = chapterTitleEn,
                sectionTitleAr = sectionTitleAr,
                sectionTitleEn = sectionTitleEn,
                contentAr = contentAr,
                keywordsAr = keywordsAr,
                examNotesAr = examNotesAr,
                scientificFormulas = scientificFormulas,
                normalizedSearchIndex = index,
                importanceScore = importanceScore
            )
        }
    }
}
