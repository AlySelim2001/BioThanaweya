package com.example.domain.model

enum class MindMapNodeType(val labelArabic: String, val labelEnglish: String) {
    ROOT("المفهوم المحوري", "Core Axis"),
    MECHANISM("الآلية الحيوية", "Mechanism"),
    REGULATION("التنظيم والتغذية الراجعة", "Regulation"),
    EXAM_TRAP("فخ الوزارة الشائع", "Exam Trap"),
    KEY_MOLECULE("الجزيء أو الإنزيم", "Key Molecule")
}

data class MindMapNode(
    val id: String,
    val labelArabic: String,
    val labelEnglish: String,
    val type: MindMapNodeType,
    val scientificSummary: String,
    val xRatio: Float = 0.5f,
    val yRatio: Float = 0.5f
)

data class MindMapEdge(
    val fromId: String,
    val toId: String,
    val relationLabelArabic: String,
    val relationLabelEnglish: String = "",
    val isHighlighted: Boolean = false
)

data class BiologicalMindMap(
    val topicId: String,
    val titleArabic: String,
    val titleEnglish: String,
    val centralQuestionArabic: String,
    val mermaidCode: String,
    val nodes: List<MindMapNode>,
    val edges: List<MindMapEdge>,
    val highYieldExamTips: List<String>
)
