package com.example.domain.usecase

import com.example.data.model.MasterStudyPack
import com.example.data.model.PracticeQuestion
import com.example.data.repository.BiologyCurriculumRepository
import com.example.domain.engine.DetailedGradingResult
import com.example.domain.engine.MarkSchemeEngine
import com.example.domain.engine.WeightedKeyword
import com.example.domain.fsrs.*
import com.example.domain.model.*
import com.example.domain.repository.AiRepository
import java.util.Date

/**
 * Agent 1: Master Biology Educator & Pedagogical Strategist
 */
class Agent1EducatorUseCase(private val aiRepository: AiRepository) {
    fun getStudyPack(topicId: String): MasterStudyPack {
        return BiologyCurriculumRepository.getMasterStudyPack(topicId)
    }

    suspend fun requestDynamicEli5(topicTitle: String): String {
        val result = aiRepository.generateExplanation(topicTitle, "ELI5 for a 5-year-old with everyday analogies")
        return result.getOrElse { "Focus on comparing molecular filaments to sliding oars powered by ATP energy." }
    }
}

/**
 * Agent 2: Visual & Spatial Learning Architect
 */
class Agent2VisualArchitectUseCase {
    fun getVisualAssets(topicId: String): VisualAssets {
        val pack = BiologyCurriculumRepository.getMasterStudyPack(topicId)
        return VisualAssets(
            topicId = topicId,
            asciiSchematic = pack.asciiSchematic,
            schematicCaption = pack.schematicCaption,
            flowchartSteps = pack.flowcharts,
            inspectionMarkers = pack.visualInspectionMarkers,
            youtube3DQueries = pack.youtube3DQueries
        )
    }
}

data class VisualAssets(
    val topicId: String,
    val asciiSchematic: String,
    val schematicCaption: String,
    val flowchartSteps: List<com.example.data.model.FlowchartStep>,
    val inspectionMarkers: List<String>,
    val youtube3DQueries: List<com.example.data.model.VideoQuery>
)

/**
 * Agent 3: Assessment Specialist & Ministry Exam Simulator
 */
class Agent3AssessmentUseCase {
    fun getAssessmentItems(topicId: String): List<PracticeQuestion> {
        val pack = BiologyCurriculumRepository.getMasterStudyPack(topicId)
        return pack.questions
    }

    fun getExamTraps(topicId: String) = BiologyCurriculumRepository.getMasterStudyPack(topicId).examTraps
}

/**
 * Agent 4: Official Ministry Examiner & FSRS Scheduler
 */
class Agent4ExaminerUseCase(private val fsrs: FSRS = FSRS()) {

    fun gradeStudentSubmission(
        studentAnswer: String,
        weightedKeywords: List<WeightedKeyword>,
        maxScore: Double = 2.0,
        modelAnswer: String = ""
    ): DetailedGradingResult {
        return MarkSchemeEngine.grade(
            studentInput = studentAnswer,
            weightedKeywords = weightedKeywords,
            maxScore = maxScore,
            modelAnswer = modelAnswer
        )
    }

    fun scheduleFsrsReview(card: FSRSCard, rating: FSRSRating, now: Date = Date()): SchedulingRecord {
        val recordMap = fsrs.repeat(card, now)
        return recordMap[rating] ?: SchedulingRecord(card, FSRSItemReviewLog(rating, 1, 0, now, FSRSState.REVIEW))
    }
}

/**
 * Agent 5: Metacognitive Agent
 * Evaluates the Confidence-Accuracy gap to eliminate illusions of competence
 */
class Agent5MetacognitiveUseCase {

    fun analyzeConfidenceAccuracy(confidencePercent: Int, isCorrect: Boolean): MetacognitiveAnalysis {
        val isHighConfidence = confidencePercent >= 70
        val gapType = when {
            isHighConfidence && isCorrect -> MetacognitiveGapType.CALIBRATED_MASTERY
            isHighConfidence && !isCorrect -> MetacognitiveGapType.OVERCONFIDENCE_BIAS
            !isHighConfidence && isCorrect -> MetacognitiveGapType.UNDERCONFIDENCE_DOUBT
            else -> MetacognitiveGapType.CALIBRATED_HUMILITY
        }

        val title: String
        val advice: String

        when (gapType) {
            MetacognitiveGapType.CALIBRATED_MASTERY -> {
                title = "🎯 معايرة مثالية (Calibrated Mastery)"
                advice = "ثقتك العالية تطابقت مع دقتك العلمية. أنت متمكن من هذا المفهوم؛ أضف بطاقته إلى جدول FSRS للمراجعات المتباعدة طويلة الأجل."
            }
            MetacognitiveGapType.OVERCONFIDENCE_BIAS -> {
                title = "⚠️ وهم الإلمام (Dunning-Kruger / Overconfidence Bias)"
                advice = "كانت ثقتك مرتفعة ($confidencePercent%) بينما احتوت إجابتك على نقص في الكلمات المفتاحية أو مصائد امتحانية. احذر من 'وهم المعرفة' وركز على كتابة خطوات الآلية بدقة."
            }
            MetacognitiveGapType.UNDERCONFIDENCE_DOUBT -> {
                title = "💡 شك غير مبرر (Imposter Effect / Underconfidence)"
                advice = "إجابتك كانت صحيحة ودقيقة رغم أن ثقتك كانت منخفضة ($confidencePercent%). ثق في حصيلتك المعرفية واعتمد على استدعاء المفاهيم بثقة أكبر في الامتحان."
            }
            MetacognitiveGapType.CALIBRATED_HUMILITY -> {
                title = "🔍 وعي سليم بالفجوة المعرفية (Calibrated Humility)"
                advice = "كنت واعيًا بنقص الإلمام ($confidencePercent%). هذه نقطة بداية ممتازة: راجع شرح المعلم البصري ثم أعد حل السؤال فورًا لتثبيت المفهوم."
            }
        }

        return MetacognitiveAnalysis(
            confidencePercent = confidencePercent,
            isCorrect = isCorrect,
            gapType = gapType,
            feedbackTitle = title,
            actionableAdvice = advice
        )
    }
}

/**
 * Agent 6: Project-Based Learning (PBL) Agent
 */
class Agent6PblUseCase {

    fun getProjectForTopic(topicId: String): PblProject {
        return when (topicId) {
            "sarcomere_contraction" -> PblProject(
                id = "pbl_sarcomere",
                topicId = topicId,
                title = "Design a Reversible Neuromuscular Blockade Protocol",
                titleArabic = "تصميم بروتوكول تجريبي لفك التشنج العضلي المستمر",
                scenario = "في عيادة طب رياضي، وصل رياضي يعاني من تشنج عضلي شديد ومستمر نتيجة نفاد ATP وتراكم الكالسيوم في الساركوبلازم.",
                mission = "صمم خطة بيولوجية من 3 خطوات لمعادلة الكالسيوم واستعادة ATP وفك ارتباط الروابط المستعرضة بالأكتين دون تمزق الألياف.",
                rubrics = listOf(
                    PblRubricCriterion("دور الكالسيوم وATP", 40, "شرح دور ATP في فصل الروابط المستعرضة بدقة", "تجاهل دور ATP واقتراح راحة فقط"),
                    PblRubricCriterion("الوقاية من التمزق والنزيف", 30, "تحديد خطورة الشد العضلي الزائد ونزيف الألياف", "عدم ذكر مخاطر التمزق"),
                    PblRubricCriterion("إعادة الاستقطاب والإنزيمات", 30, "ذكر دور الكولينستريز ومضخة الكالسيوم", "إغفال إعادة الاستقطاب")
                ),
                tips = listOf("تذكر أن فصل الروابط المستعرضة يتطلب جزيئات ATP جديدة.", "مضخات الكالسيوم في الشبكة الساركوبلازمية تحتاج أيضًا طاقة ATP للعمل ضد التدرج.")
            )
            "dna_structure_replication" -> PblProject(
                id = "pbl_dna",
                topicId = topicId,
                title = "Design a PCR Primer Selection for Sickle Cell Gene",
                titleArabic = "تصميم بادئات PCR لتكثير جين الهيموجلوبين في فقر الدم المنجلي",
                scenario = "مختبر جينات بحاجة لتكثير طفرة نقطية في جين بيتا جلوبين على شريط DNA للكشف عن فقر الدم المنجلي.",
                mission = "حدد اتجاه تخليق البادئ (5' إلى 3') والإنزيم المستخدم ودرجة الحرارة المثلى لعمل إنزيم تاك بوليميريز.",
                rubrics = listOf(
                    PblRubricCriterion("اتجاه التضاعف 5' -> 3'", 40, "تحديد اتجاه التخليق الإجباري 5' إلى 3'", "افتراض اتجاه خاطئ"),
                    PblRubricCriterion("إنزيم تاك بوليميريز المقاوم للحرارة", 35, "توضيح مقاومة الإنزيم للحرارة العالية دون تلف", "اقتراح بوليميريز عادي يتلف بالحرارة"),
                    PblRubricCriterion("قواعد الازدواج النيتروجيني", 25, "تطبيق تكامل A=T و G=C بدقة", "أخطاء في تكامل القواعد")
                ),
                tips = listOf("إنزيم تاك بوليميريز معزول من بكتيريا الينابيع الحارة.", "شريطا DNA المتكونان يكونان متعاكسي الاتجاه.")
            )
            else -> PblProject(
                id = "pbl_generic",
                topicId = topicId,
                title = "Biology Investigation Challenge",
                titleArabic = "مهمة تطبيقية: التحقيق البيولوجي في آليات الخلية",
                scenario = "تطبيق المفاهيم البيولوجية على حالات حيوية أو سريرية في الأحياء التجريبية.",
                mission = "حدد المتغير المستقل، المتغير التابع، والشاهد المعملي للتحقق من المفهوم البيولوجي.",
                rubrics = listOf(
                    PblRubricCriterion("الفرضية والتصميم العلمي", 50, "صياغة فرضية دقيقة مع أدوات مناسبة", "تصميم غير علمي"),
                    PblRubricCriterion("تفسير النتائج بالآليات الجزيئية", 50, "ربط النتائج بالمصطلحات البيولوجية الرسمية", "وصف سطحي دون آليات")
                ),
                tips = listOf("استخدم مصطلحات المنهج المصري بدقة.")
            )
        }
    }
}

/**
 * Agent 7: Wellbeing & Gamification Agent
 */
class Agent7WellbeingUseCase {

    fun getStudentProfile(): WellbeingProfile {
        return WellbeingProfile(
            streakDays = 5,
            totalXp = 680,
            earnedBadges = listOf(
                StudentBadge("b1", "Huxley Master", "سيد خيوط هكسلي", "fitness_center", "حل 10 مسائل ساركومير بنجاح دون أي خطأ."),
                StudentBadge("b2", "Chargaff Code Breaker", "كاسر شفرة شارجاف", "biotech", "حساب نسب القواعد النيتروجينية ولفات الحلزون بدقة تامة."),
                StudentBadge("b3", "Ministry Exam Conqueror", "قاهر امتحانات الوزارة", "verified", "الحصول على الدرجة الكاملة في 5 أسئلة علل وماذا يحدث.")
            ),
            examAnxietyScore = 15,
            studyChunkMinutes = 25,
            motivationalQuote = "تذكر: ثباتك الذهني وتنظيم أوقات المراجعة المتباعدة هما سلاحك الأول للتفوق في امتحان الأحياء!"
        )
    }

    fun getAnxietyReliefTip(anxietyScore: Int): String {
        return if (anxietyScore > 50) {
            "خذ نفسًا عميقًا بطريقة 4-7-8 (شهيق 4 ثوانٍ، حبس 7، زفير 8). لا تنشغل بدرجة الامتحان، وركز فقط على السؤال المعروض أمامك كأنه لغز ممتع."
        } else {
            "طاقتك الذهنية في أفضل مستوياتها! استثمر فترة التركيز الحالية لمراجعة البطاقات الأكثر صعوبة (Hard)."
        }
    }
}

/**
 * Central Orchestrator
 */
class OrchestratorUseCase(
    val educatorAgent: Agent1EducatorUseCase,
    val visualAgent: Agent2VisualArchitectUseCase,
    val assessmentAgent: Agent3AssessmentUseCase,
    val examinerAgent: Agent4ExaminerUseCase,
    val metacognitiveAgent: Agent5MetacognitiveUseCase,
    val pblAgent: Agent6PblUseCase,
    val wellbeingAgent: Agent7WellbeingUseCase
)
