package com.example.domain.model

enum class DivisionProcess(val titleArabic: String, val titleEnglish: String) {
    MITOSIS("الإنقسام الميتوزي (غير المباشر)", "Mitosis (Equational Division)"),
    MEIOSIS_1("الإنقسام الميوزي الأول (الاختزالي)", "Meiosis I (Reductional Division)"),
    MEIOSIS_2("الإنقسام الميوزي الثاني", "Meiosis II (Equational Division)")
}

enum class DivisionStage(val stageNameArabic: String, val stageNameEnglish: String, val stepOrder: Int) {
    INTERPHASE("الطور البيني (التحضيري)", "Interphase (G1-S-G2)", 0),
    PROPHASE("الطور التمهيدي", "Prophase", 1),
    METAPHASE("الطور الاستوائي", "Metaphase", 2),
    ANAPHASE("الطور الانفصالي", "Anaphase", 3),
    TELOPHASE("الطور النهائي", "Telophase & Cytokinesis", 4)
}

data class DivisionStageInfo(
    val stage: DivisionStage,
    val process: DivisionProcess,
    val descriptionArabic: String,
    val descriptionEnglish: String,
    val chromosomeCountFormula: String, // e.g. "46 كروموسوم (2n)"
    val chromatidsCountFormula: String, // e.g. "92 كروماتيد"
    val dnaContentFormula: String,       // e.g. "4C (مضاعفة المادة الوراثية)"
    val ministryExamTrapWarning: String, // High-yield exam trap
    val keyMicroscopicEventArabic: String
)

data class ChromosomePair(
    val id: Int,
    val name: String,
    val isMaternal: Boolean, // True = Pink/Red, False = Cyan/Blue
    val isCrossoverActive: Boolean = false,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val rotation: Float = 0f
)
