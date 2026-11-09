package com.example.domain.model

import android.graphics.Bitmap

/**
 * Visual presentation style for generated biological diagrams.
 */
enum class DiagramStyle(
    val labelAr: String,
    val labelEn: String,
    val description: String,
    val promptModifier: String
) {
    LABELED_TEXTBOOK(
        labelAr = "مخطط كتاب مدرسي معتمد",
        labelEn = "Official Labeled Textbook",
        description = "High-contrast 2D biological vector illustration with sharp callout labels and English/Arabic scientific terms.",
        promptModifier = "Clear 2D biological textbook illustration, crisp high-contrast vector art, clean background, sharp callout arrows pointing to numbered structures, precise anatomical labeling, Egyptian Ministry syllabus biology style."
    ),
    MINISTRY_EXAM_LINE_ART(
        labelAr = "رسم امتحاني أبيض وأسود",
        labelEn = "Exam Monochrome Line Art",
        description = "Clean black-and-white line drawing mimicking official Egyptian Thanaweya Amma examination booklets.",
        promptModifier = "Clean black and white biological line art drawing, high contrast, pure white background, distinct structural lines, numbered labels (1, 2, 3, 4) for exam identification questions, official Thanaweya Amma exam paper style."
    ),
    ANATOMICAL_CROSS_SECTION(
        labelAr = "مقطع تشريحي دقيق",
        labelEn = "Detailed Cross-Section",
        description = "Realistic cross-sectional histological and spatial arrangement of tissues and membranes.",
        promptModifier = "Detailed histological cross section diagram, layered anatomical structures, cellular membranes and lumen clearly delineated, clean medical textbook quality, labeled tissue layers."
    )
}

/**
 * Curated preset tailored to specific high-yield topics in the curriculum.
 */
data class DiagramPreset(
    val id: String,
    val topicId: String,
    val titleAr: String,
    val titleEn: String,
    val prompt: String,
    val keyLandmarks: List<String>,
    val examTrapWarning: String,
    val goldenRule: String
)

/**
 * State container for a generated or reviewed diagram.
 */
data class GeneratedBiologicalDiagram(
    val id: String,
    val topicId: String,
    val title: String,
    val bitmap: Bitmap?,
    val promptUsed: String,
    val style: DiagramStyle,
    val keyLandmarks: List<String>,
    val examTrapWarning: String,
    val goldenRule: String,
    val timestampEpoch: Long = System.currentTimeMillis()
)
