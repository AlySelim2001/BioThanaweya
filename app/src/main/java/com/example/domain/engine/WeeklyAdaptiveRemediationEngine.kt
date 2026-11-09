package com.example.domain.engine

import com.example.data.model.UnitCategory
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * Diagnostic and Remediation Engine for Phase 2:
 * Evaluates current mastery levels, pinpoints weak spots across the 11 Egyptian Biology chapters,
 * and formulates a 7-Day adaptive personalized recovery plan.
 */
data class RemedialDayTask(
    val dayNumber: Int, // 1 to 7
    val dayNameAr: String,
    val titleAr: String,
    val titleEn: String,
    val descriptionAr: String,
    val topicId: String,
    val topicTitleAr: String,
    val taskType: RemedialTaskType,
    val estimatedMinutes: Int,
    val xpReward: Int,
    val isCompleted: Boolean = false,
    val targetCompetency: String // e.g. "فخاخ الوزارة", "مسائل وقوانين", "مفاهيم أساسية"
)

enum class RemedialTaskType(val labelAr: String, val iconName: String) {
    CONCEPT_CLARIFICATION("تفكيك المفاهيم المعقدة", "School"),
    FORMULA_CALCULATION("مسائل وقوانين رياضية", "Calculate"),
    MINISTRY_TRAPS("فخاخ وأخطاء شائعة", "Warning"),
    FSRS_SPACED_REPETITION("مراجعة البطاقات الذكية", "Style"),
    COMPARISON_SYNTHESIS("مقارنات وتحليل تركيبي", "CompareArrows"),
    TARGETED_QUIZ("تدريب جزئي مركز", "FactCheck"),
    MINI_SIMULATED_EXAM("محاكاة امتحان تشخيصي", "Timer")
}

data class WeeklyRemediationPlan(
    val generationDate: String,
    val overallStatusAr: String,
    val weakestTopicId: String,
    val weakestTopicTitleAr: String,
    val weakestTopicMasteryPct: Int,
    val secondWeakestTopicId: String,
    val secondWeakestTopicTitleAr: String,
    val dailyTasks: List<RemedialDayTask>,
    val completedTasksCount: Int,
    val totalXpAvailable: Int,
    val predictedScoreBoostMarks: Double // Expected mark jump in Ministry Exam (e.g. +4.5 marks)
)

object WeeklyAdaptiveRemediationEngine {

    /**
     * Builds an evidence-driven 7-day adaptive study plan based on student topic masteries.
     */
    fun generatePlan(
        topicMasteries: Map<String, Float>, // topicId -> rawMastery (0.0 to 1.0)
        completedTaskIndices: Set<Int> = emptySet()
    ): WeeklyRemediationPlan {
        val topicCatalog = listOf(
            TopicMeta("sarcomere_contraction", "الدعامة والحركة", "Support & Movement", UnitCategory.UNIT_1),
            TopicMeta("nutrition_digestion", "التغذية في الكائنات الحية", "Nutrition", UnitCategory.UNIT_1),
            TopicMeta("transport_circulation", "النقل في الكائنات الحية", "Transport", UnitCategory.UNIT_1),
            TopicMeta("cellular_respiration", "التنفس الخلوي وإنتاج ATP", "Cellular Respiration", UnitCategory.UNIT_1),
            TopicMeta("excretion_homeostasis", "الإخراج في الكائنات الحية", "Excretion", UnitCategory.UNIT_1),
            TopicMeta("nervous_coordination", "التنسيق العصبي والسيال العصبي", "Nervous Coordination", UnitCategory.UNIT_1),
            TopicMeta("hormonal_coordination_endocrine", "التنسيق الهرموني", "Hormonal Coordination", UnitCategory.UNIT_1),
            TopicMeta("human_reproduction_menstrual", "التكاثر في الكائنات الحية", "Reproduction", UnitCategory.UNIT_1),
            TopicMeta("dna_structure_replication", "الحمض النووي DNA وتضاعفه", "DNA Structure", UnitCategory.UNIT_2),
            TopicMeta("rna_transcription_translation", "الأحماض النووية RNA وتخليق البروتين", "RNA & Protein Synthesis", UnitCategory.UNIT_2),
            TopicMeta("genetic_engineering_recombinant", "الطفرات والهندسة الوراثية", "Genetic Engineering", UnitCategory.UNIT_2)
        )

        // Sort topics by mastery ascending (weakest first)
        val sortedTopics = topicCatalog.map { meta ->
            val mastery = topicMasteries[meta.id] ?: 0.65f
            meta to mastery
        }.sortedBy { it.second }

        val weakest = sortedTopics.firstOrNull() ?: (topicCatalog[0] to 0.50f)
        val secondWeakest = sortedTopics.getOrNull(1) ?: (topicCatalog[1] to 0.60f)
        val thirdWeakest = sortedTopics.getOrNull(2) ?: (topicCatalog[2] to 0.70f)

        val dayNames = listOf("السبت", "الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")

        val tasks = listOf(
            RemedialDayTask(
                dayNumber = 1,
                dayNameAr = dayNames[0],
                titleAr = "تفكيك الغموض المفاهيمي: ${weakest.first.titleAr}",
                titleEn = "Deep Conceptual Scaffolding: ${weakest.first.titleEn}",
                descriptionAr = "مراجعة الشرح التفصيلي للركائز الأساسية وحل الالتباس العلمي مع التفسير العلمي المبسط.",
                topicId = weakest.first.id,
                topicTitleAr = weakest.first.titleAr,
                taskType = RemedialTaskType.CONCEPT_CLARIFICATION,
                estimatedMinutes = 20,
                xpReward = 40,
                isCompleted = completedTaskIndices.contains(1),
                targetCompetency = "المفاهيم والنظريات الأساسية"
            ),
            RemedialDayTask(
                dayNumber = 2,
                dayNameAr = dayNames[1],
                titleAr = "معمل القوانين والمسائل الرياضية",
                titleEn = "Calculations & Mathematical Rules",
                descriptionAr = "إتقان القوانين الحسابية الصعبة (مثل حسابات القطعة العضلية، قواعد شارجاف، أو كودونات mRNA).",
                topicId = if (weakest.first.id in listOf("sarcomere_contraction", "dna_structure_replication", "rna_transcription_translation", "cellular_respiration")) weakest.first.id else "dna_structure_replication",
                topicTitleAr = if (weakest.first.id in listOf("sarcomere_contraction", "dna_structure_replication", "rna_transcription_translation", "cellular_respiration")) weakest.first.titleAr else "الحمض النووي DNA وتضاعفه",
                taskType = RemedialTaskType.FORMULA_CALCULATION,
                estimatedMinutes = 25,
                xpReward = 50,
                isCompleted = completedTaskIndices.contains(2),
                targetCompetency = "المسائل والقوانين الرياضية"
            ),
            RemedialDayTask(
                dayNumber = 3,
                dayNameAr = dayNames[2],
                titleAr = "كاشف فخاخ امتحانات الثانوية العامة",
                titleEn = "Ministry Exam Traps & Misconceptions",
                descriptionAr = "فحص الفخاخ الوزارية الشهيرة وصيغ الأسئلة الخادعة وكيفية تمييز الإجابة النموذجية الدقيقة.",
                topicId = weakest.first.id,
                topicTitleAr = weakest.first.titleAr,
                taskType = RemedialTaskType.MINISTRY_TRAPS,
                estimatedMinutes = 15,
                xpReward = 35,
                isCompleted = completedTaskIndices.contains(3),
                targetCompetency = "تجنب الفخاخ الوزارية"
            ),
            RemedialDayTask(
                dayNumber = 4,
                dayNameAr = dayNames[3],
                titleAr = "جلسة التكرار المتباعد الذكي (FSRS)",
                titleEn = "FSRS Spaced Repetition Flashcards",
                descriptionAr = "مراجعة 15 بطاقة ذكية وفق خوارزمية FSRS لضمان الانتقال إلى الذاكرة طويلة المدى.",
                topicId = secondWeakest.first.id,
                topicTitleAr = secondWeakest.first.titleAr,
                taskType = RemedialTaskType.FSRS_SPACED_REPETITION,
                estimatedMinutes = 15,
                xpReward = 30,
                isCompleted = completedTaskIndices.contains(4),
                targetCompetency = "الذاكرة طويلة المدى والمصطلحات"
            ),
            RemedialDayTask(
                dayNumber = 5,
                dayNameAr = dayNames[4],
                titleAr = "مقارنات وجداول الفروق التركيبية",
                titleEn = "Comparative & Synthetic Analysis",
                descriptionAr = "دراسة جداول المقارنة بين التراكيب المتشابهة لتفادي الخلط في أسئلة (ماذا يحدث إذا).",
                topicId = secondWeakest.first.id,
                topicTitleAr = secondWeakest.first.titleAr,
                taskType = RemedialTaskType.COMPARISON_SYNTHESIS,
                estimatedMinutes = 20,
                xpReward = 45,
                isCompleted = completedTaskIndices.contains(5),
                targetCompetency = "التحليل التركيبي والمقارنات"
            ),
            RemedialDayTask(
                dayNumber = 6,
                dayNameAr = dayNames[5],
                titleAr = "تدريب أسئلة الوزارة الموجهة (10 MCQs)",
                titleEn = "Targeted Ministry Practice Quiz",
                descriptionAr = "حل 10 أسئلة اختيار من متعدد مع التحليل الفوري للمشتتات وتفسير نموذج الإجابة.",
                topicId = weakest.first.id,
                topicTitleAr = weakest.first.titleAr,
                taskType = RemedialTaskType.TARGETED_QUIZ,
                estimatedMinutes = 25,
                xpReward = 60,
                isCompleted = completedTaskIndices.contains(6),
                targetCompetency = "حل وتطبيق فوري"
            ),
            RemedialDayTask(
                dayNumber = 7,
                dayNameAr = dayNames[6],
                titleAr = "المحاكاة التشخيصية الأسبوعية الشاملة",
                titleEn = "Weekly Comprehensive Diagnostic Simulation",
                descriptionAr = "اختبار سريع يغطي كافة نقاط الضعف المعالجة خلال الأسبوع لقياس نسبة الارتفاع في الإتقان.",
                topicId = thirdWeakest.first.id,
                topicTitleAr = "كافة موضوعات المعالجة",
                taskType = RemedialTaskType.MINI_SIMULATED_EXAM,
                estimatedMinutes = 30,
                xpReward = 100,
                isCompleted = completedTaskIndices.contains(7),
                targetCompetency = "جاهزية الامتحان النهائي"
            )
        )

        val completedCount = tasks.count { it.isCompleted }
        val totalXp = tasks.sumOf { it.xpReward }

        val statusText = when {
            weakest.second < 0.6f -> "⚠️ تم اكتشاف فجوات إتقان تتطلب معالجة أسبوعية مكثفة"
            weakest.second < 0.75f -> "📈 أداء واعد: الخطة تركز على صقل الفخاخ والقوانين لرفع الدرجة"
            else -> "🌟 مستوى إتقان متقدم: الخطة تركز على تدريبات التثبيت والسرعة"
        }

        // Expected score boost in Egyptian Ministry Exam (out of 60 marks)
        val scoreBoost = ((1.0f - weakest.second) * 5.0 + 1.5).coerceIn(1.5, 7.0)

        return WeeklyRemediationPlan(
            generationDate = "أسبوع مراجعة الثانوية العامة التكيفي",
            overallStatusAr = statusText,
            weakestTopicId = weakest.first.id,
            weakestTopicTitleAr = weakest.first.titleAr,
            weakestTopicMasteryPct = (weakest.second * 100).toInt(),
            secondWeakestTopicId = secondWeakest.first.id,
            secondWeakestTopicTitleAr = secondWeakest.first.titleAr,
            dailyTasks = tasks,
            completedTasksCount = completedCount,
            totalXpAvailable = totalXp,
            predictedScoreBoostMarks = (scoreBoost * 10).toInt() / 10.0
        )
    }

    private data class TopicMeta(
        val id: String,
        val titleAr: String,
        val titleEn: String,
        val unit: UnitCategory
    )
}
