package com.example.data.repository

import com.example.data.local.dao.ChapterProgressDao
import com.example.data.local.entity.ChapterProgress
import com.example.domain.repository.ChapterProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ChapterProgressRepositoryImpl:
 * Local repository implementation using Room Database to manage and persist
 * progress and completion status for all 11 Egyptian Thanawya Amma Biology chapters.
 */
@Singleton
class ChapterProgressRepositoryImpl @Inject constructor(
    private val chapterProgressDao: ChapterProgressDao
) : ChapterProgressRepository {

    override fun getAllChaptersProgress(): Flow<List<ChapterProgress>> {
        return chapterProgressDao.getAllChapterProgress()
    }

    override fun getChapterProgress(chapterId: Int): Flow<ChapterProgress?> {
        return chapterProgressDao.getChapterProgressById(chapterId)
    }

    override fun getChapterProgressByKey(key: String): Flow<ChapterProgress?> {
        return chapterProgressDao.getChapterProgressByKey(key)
    }

    override fun getCompletedChapters(): Flow<List<ChapterProgress>> {
        return chapterProgressDao.getCompletedChapters()
    }

    override fun getCompletedChaptersCount(): Flow<Int> {
        return chapterProgressDao.getCompletedChaptersCount()
    }

    override fun getAverageCompletionPercentage(): Flow<Float> {
        return chapterProgressDao.getAverageCompletionPercentage().map { avg ->
            avg ?: 0f
        }
    }

    override fun getChaptersByUnit(unitNumber: Int): Flow<List<ChapterProgress>> {
        return chapterProgressDao.getChapterProgressByUnit(unitNumber)
    }

    override suspend fun markChapterCompleted(chapterId: Int, isCompleted: Boolean) {
        chapterProgressDao.updateCompletionStatus(
            chapterId = chapterId,
            isCompleted = isCompleted,
            timestamp = System.currentTimeMillis()
        )
    }

    override suspend fun updateChapterProgressPercentage(chapterId: Int, percentage: Float) {
        val clamped = percentage.coerceIn(0f, 100f)
        chapterProgressDao.updateProgressPercentage(
            chapterId = chapterId,
            percentage = clamped,
            timestamp = System.currentTimeMillis()
        )
    }

    override suspend fun recordQuestionSolved(chapterId: Int, count: Int) {
        chapterProgressDao.incrementQuestionsSolved(
            chapterId = chapterId,
            count = count,
            timestamp = System.currentTimeMillis()
        )
        // Automatically adjust completion percentage based on questions solved
        val current = chapterProgressDao.getChapterProgressByIdSync(chapterId)
        if (current != null) {
            val calculated = ((current.questionsSolved.toFloat() / current.totalQuestions) * 60f +
                    (current.flashcardsMastered.toFloat() / current.totalFlashcards) * 40f).coerceIn(0f, 100f)
            val newPercentage = maxOf(current.completionPercentage, calculated)
            chapterProgressDao.updateProgressPercentage(
                chapterId = chapterId,
                percentage = newPercentage,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    override suspend fun recordFlashcardMastered(chapterId: Int, count: Int) {
        chapterProgressDao.incrementFlashcardsMastered(
            chapterId = chapterId,
            count = count,
            timestamp = System.currentTimeMillis()
        )
        // Automatically adjust completion percentage based on flashcards mastered
        val current = chapterProgressDao.getChapterProgressByIdSync(chapterId)
        if (current != null) {
            val calculated = ((current.questionsSolved.toFloat() / current.totalQuestions) * 60f +
                    (current.flashcardsMastered.toFloat() / current.totalFlashcards) * 40f).coerceIn(0f, 100f)
            val newPercentage = maxOf(current.completionPercentage, calculated)
            chapterProgressDao.updateProgressPercentage(
                chapterId = chapterId,
                percentage = newPercentage,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    override suspend fun addStudyDuration(chapterId: Int, minutes: Int) {
        chapterProgressDao.addStudyMinutes(
            chapterId = chapterId,
            minutes = minutes,
            timestamp = System.currentTimeMillis()
        )
    }

    override suspend fun saveChapterProgress(progress: ChapterProgress) {
        chapterProgressDao.insertOrUpdate(progress)
    }

    override suspend fun seedDefault11ChaptersIfEmpty() {
        val count = chapterProgressDao.getTotalChapterCount()
        if (count == 0) {
            chapterProgressDao.insertAll(DEFAULT_11_CHAPTERS)
        }
    }

    override suspend fun resetAllProgress() {
        chapterProgressDao.resetAllProgress(System.currentTimeMillis())
    }

    companion object {
        /**
         * Pre-configured initial dataset representing all 11 Egyptian Thanawya Amma
         * Biology chapters according to the official Ministry of Education curriculum.
         */
        val DEFAULT_11_CHAPTERS: List<ChapterProgress> = listOf(
            ChapterProgress(
                chapterId = 1,
                chapterKey = "support_movement",
                unitNumber = 1,
                titleAr = "الدعامة والحركة في الكائنات الحية",
                titleEn = "Support and Movement",
                summaryAr = "الدعامة الفسيولوجية والتركيبية في النبات، الهيكل العظمي والمفاصل، وآلية انقباض العضلة الهيكلية ونظرية الخيوط المنزلقة.",
                iconName = "fitness_center",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 12,
                flashcardsMastered = 0,
                totalFlashcards = 18
            ),
            ChapterProgress(
                chapterId = 2,
                chapterKey = "nutrition_digestion",
                unitNumber = 1,
                titleAr = "التغذية في الكائنات الحية",
                titleEn = "Nutrition in Living Organisms",
                summaryAr = "التغذية الذاتية، التفاعلات الضوئية واللاضوئية في البلاستيدات، والإنزيمات الهاضمة وامتصاص الغذاء في خملات الأمعاء الدقيقة.",
                iconName = "restaurant",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 10,
                flashcardsMastered = 0,
                totalFlashcards = 15
            ),
            ChapterProgress(
                chapterId = 3,
                chapterKey = "transport_circulation",
                unitNumber = 1,
                titleAr = "النقل في الكائنات الحية",
                titleEn = "Transport in Living Organisms",
                summaryAr = "صعود العصارة النيئة في النبات (قوى التماسك والتلاصق والنتح)، الجهاز الدوري، تركيب القلب، ودورة ضغط الدم.",
                iconName = "swap_horiz",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 10,
                flashcardsMastered = 0,
                totalFlashcards = 14
            ),
            ChapterProgress(
                chapterId = 4,
                chapterKey = "cellular_respiration",
                unitNumber = 1,
                titleAr = "التنفس الخلوي وإنتاج الطاقة",
                titleEn = "Cellular Respiration & Bioenergetics",
                summaryAr = "انشطار الجلوكوز في السيتوبلازم، دورة كريبس في الميتوكوندريا، سلسلة نقل الإلكترون وحساب 38 جزيء ATP، والتنفس اللاهوائي.",
                iconName = "bolt",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 10,
                flashcardsMastered = 0,
                totalFlashcards = 16
            ),
            ChapterProgress(
                chapterId = 5,
                chapterKey = "excretion_homeostasis",
                unitNumber = 1,
                titleAr = "الإخراج في الكائنات الحية",
                titleEn = "Excretion in Living Organisms",
                summaryAr = "إخراج الجلد والعرق، تكوين اليوريا في الكبد، تركيب النيفرون في الكلية ومحفظة بومان وإعادة الامتصاص الاختياري.",
                iconName = "filter_alt",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 10,
                flashcardsMastered = 0,
                totalFlashcards = 14
            ),
            ChapterProgress(
                chapterId = 6,
                chapterKey = "nervous_coordination",
                unitNumber = 1,
                titleAr = "التنسيق العصبي والسيال العصبي",
                titleEn = "Nervous Coordination & Impulses",
                summaryAr = "أنواع الخلايا العصبية والغراء العصبي، جهد الراحة (-70mV)، انعكاس الاستقطاب وجهد الفعالية (+30mV)، ونواقل التشابك العصبي.",
                iconName = "psychology",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 10,
                flashcardsMastered = 0,
                totalFlashcards = 15
            ),
            ChapterProgress(
                chapterId = 7,
                chapterKey = "hormonal_coordination",
                unitNumber = 1,
                titleAr = "التنسيق الهرموني في الكائنات الحية",
                titleEn = "Hormonal Coordination",
                summaryAr = "الغدة النخامية (المايسترو)، الغدة الدرقية، الغدد جارات الدرقية، الكظريتان، البنكرياس، وآليات التغذية الراجعة السلبية.",
                iconName = "water_drop",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 12,
                flashcardsMastered = 0,
                totalFlashcards = 18
            ),
            ChapterProgress(
                chapterId = 8,
                chapterKey = "reproduction",
                unitNumber = 1,
                titleAr = "التكاثر في الكائنات الحية",
                titleEn = "Reproduction in Living Organisms",
                summaryAr = "التكاثر اللاجنسي والجنسي، تعاقب الأجيال في البلازموديوم وسرخس الفوجير، الإخصاب المزدوج في النباتات، والتكاثر ودورة الطمث في الإنسان.",
                iconName = "all_inclusive",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 14,
                flashcardsMastered = 0,
                totalFlashcards = 20
            ),
            ChapterProgress(
                chapterId = 9,
                chapterKey = "immunity",
                unitNumber = 1,
                titleAr = "المناعة في الكائنات الحية",
                titleEn = "Immunity in Living Organisms",
                summaryAr = "المناعة في النبات، الجهاز اللمفاوي البشري، الأجسام المضادة وآليات عملها، المناعة الطبيعية والمكتسبة (الخلطية والخلوية).",
                iconName = "shield",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 12,
                flashcardsMastered = 0,
                totalFlashcards = 18
            ),
            ChapterProgress(
                chapterId = 10,
                chapterKey = "dna_replication",
                unitNumber = 2,
                titleAr = "الحمض النووي (DNA وتضاعفه)",
                titleEn = "DNA Structure & Replication",
                summaryAr = "تجارب جريفيث وأفري وهيرشي وتشيس، نموذج واطسون وكريك للولب المزدوج، قواعد شارجاف، إنزيمات التضاعف وإصلاح عيوب DNA.",
                iconName = "biotech",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 14,
                flashcardsMastered = 0,
                totalFlashcards = 20
            ),
            ChapterProgress(
                chapterId = 11,
                chapterKey = "rna_protein_biotechnology",
                unitNumber = 2,
                titleAr = "الأحماض النووية (RNA والتقنية الحيوية)",
                titleEn = "RNA, Translation & Biotechnology",
                summaryAr = "أنواع RNA (mRNA, tRNA, rRNA)، الشفرة الوراثية وتخليق البروتين، الطفرات الجينية والصبغية، إنزيمات القصر وتطبيقات الهندسة الوراثية.",
                iconName = "science",
                isCompleted = false,
                completionPercentage = 0f,
                questionsSolved = 0,
                totalQuestions = 14,
                flashcardsMastered = 0,
                totalFlashcards = 20
            )
        )
    }
}
