package com.example.domain.model

import kotlin.math.cos
import kotlin.math.sin

/**
 * High School Thanaweya Amma Biology 3D Cell Simulation Domain Models.
 */

enum class CellType(val englishName: String, val arabicName: String) {
    ANIMAL_CELL("Animal Cell", "خلية حيوانية"),
    PLANT_CELL("Plant Cell", "خلية نباتية")
}

enum class OrganelleType(
    val englishName: String,
    val arabicName: String,
    val shortBadge: String,
    val primaryColorHex: Long
) {
    NUCLEUS("Nucleus", "النواة", "DNA", 0xFF673AB7),
    NUCLEOLUS("Nucleolus", "النوية", "rRNA", 0xFF9C27B0),
    MITOCHONDRIA("Mitochondria", "الميتوكوندريا", "ATP", 0xFFFF5722),
    ROUGH_ER("Rough ER", "الشبكة الإندوبلازمية الخشنة", "RER", 0xFF2196F3),
    SMOOTH_ER("Smooth ER", "الشبكة الإندوبلازمية الملساء", "SER", 0xFF00BCD4),
    GOLGI_APPARATUS("Golgi Complex", "جهاز جولجي", "GOLGI", 0xFFFF9800),
    RIBOSOMES("Ribosomes", "الريبوسومات", "70S/80S", 0xFF4CAF50),
    LYSOSOMES("Lysosomes", "اللايسوسومات", "ENZ", 0xFFE91E63),
    CENTROSOME("Centrosome (Centrioles)", "الجسم المركزي (السنتريولان)", "SPINDLE", 0xFF795548),
    CELL_MEMBRANE("Cell Membrane", "غشاء الخلية البلازمي", "BILAYER", 0xFF009688),
    CYTOSKELETON("Cytoplasm & Cytoskeleton", "السيتوبلازم والهيكل الخلوي", "CYTO", 0xFF607D8B),
    CHLOROPLAST("Chloroplast", "البلاستيدات الخضراء", "LIGHT", 0xFF388E3C),
    CENTRAL_VACUOLE("Central Vacuole", "الفجوة العصارية", "TURGOR", 0xFF0288D1)
}

data class CellMicroQuiz(
    val questionArabic: String,
    val questionEnglish: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanationArabic: String,
    val ministryTip: String
)

data class OrganelleDetail(
    val type: OrganelleType,
    val englishName: String,
    val arabicName: String,
    val roleBadge: String,
    val primaryFunction: String,
    val detailedStructure: String,
    val biochemicalRole: String,
    val ministryExamTrap: String,
    val eli5Analogy: String,
    val keyReactions: List<String>,
    val microQuiz: CellMicroQuiz,
    val isPresentInAnimal: Boolean = true,
    val isPresentInPlant: Boolean = true
)

enum class SimulationViewMode(val titleArabic: String, val titleEnglish: String) {
    EXPLORATION("استكشاف حر", "Free 3D"),
    GUIDED_TOUR("جولة إرشادية", "Guided Tour"),
    SECRETORY_PATHWAY("مسار إفراز البروتين", "Secretory Flow"),
    ATP_PRODUCTION("توليد الطاقة ATP", "Cellular Respiration")
}

data class Vector3D(
    val x: Float,
    val y: Float,
    val z: Float
) {
    fun rotateY(angleRad: Float): Vector3D {
        val cosA = cos(angleRad)
        val sinA = sin(angleRad)
        return Vector3D(
            x = x * cosA + z * sinA,
            y = y,
            z = -x * sinA + z * cosA
        )
    }

    fun rotateX(angleRad: Float): Vector3D {
        val cosA = cos(angleRad)
        val sinA = sin(angleRad)
        return Vector3D(
            x = x,
            y = y * cosA - z * sinA,
            z = y * sinA + z * cosA
        )
    }
}

data class Organelle3DInstance(
    val id: String,
    val type: OrganelleType,
    val localPosition: Vector3D,
    val baseRadius: Float,
    val rotationOffset: Float = 0f,
    val isPlantOnly: Boolean = false,
    val isAnimalOnly: Boolean = false
)
