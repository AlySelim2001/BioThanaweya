package com.example.data.repository

import com.example.data.local.dao.*
import com.example.data.local.entity.*
import com.example.data.model.MasterStudyPack
import com.example.domain.fsrs.*
import com.example.domain.repository.BiologyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiologyRepositoryImpl @Inject constructor(
    private val flashcardDao: FlashcardDao,
    private val progressDao: ProgressDao,
    private val examAttemptDao: ExamAttemptDao,
    private val topicMasteryDao: TopicMasteryDao,
    private val savedDefinitionDao: SavedDefinitionDao,
    private val keyConceptDao: KeyConceptDao,
    private val examMistakeDao: ExamMistakeDao? = null,
    private val fsrs: FSRS = FSRS()
) : BiologyRepository {

    override fun getAllTopics(): List<MasterStudyPack> {
        return BiologyCurriculumRepository.subTopics.map {
            BiologyCurriculumRepository.getMasterStudyPack(it.id)
        }
    }

    override fun getTopicById(topicId: String): MasterStudyPack? {
        return BiologyCurriculumRepository.getMasterStudyPack(topicId)
    }

    override fun searchCurriculum(query: String): List<MasterStudyPack> {
        val topics = getAllTopics()
        if (query.isBlank()) return topics
        val q = query.trim().lowercase()
        return topics.filter { pack ->
            pack.topicTitle.lowercase().contains(q) ||
            pack.chapterTitle.lowercase().contains(q) ||
            pack.deepTechnicalBreakdown.lowercase().contains(q) ||
            pack.eli5Analogy.lowercase().contains(q) ||
            pack.arabicEli5.contains(query.trim()) ||
            pack.keyTerms.any { it.term.lowercase().contains(q) || it.arabicTerm.contains(query.trim()) }
        }
    }

    override fun getDueCards(nowEpoch: Long): Flow<List<FlashcardEntity>> {
        return flashcardDao.getDueCards(nowEpoch)
    }

    override fun getCardsByTopic(topicId: String): Flow<List<FlashcardEntity>> {
        return flashcardDao.getCardsByTopic(topicId)
    }

    override suspend fun recordCardReview(cardId: String, rating: FSRSRating): SchedulingInfo {
        val card = flashcardDao.getCardById(cardId) ?: throw IllegalArgumentException("Card not found: $cardId")
        val stateEnum = when (card.state) {
            0 -> FSRSState.NEW
            1 -> FSRSState.LEARNING
            2 -> FSRSState.REVIEW
            3 -> FSRSState.RELEARNING
            else -> FSRSState.NEW
        }
        val currentFsrsCard = FSRSCard(
            cardId = card.id,
            due = Date(card.dueEpoch),
            stability = card.stability,
            difficulty = card.difficulty,
            reps = card.reps,
            lapses = card.lapses,
            state = stateEnum,
            lastReview = card.lastReviewEpoch?.let { Date(it) }
        )
        val now = Date()
        val scheduledRecord = fsrs.repeat(currentFsrsCard, now)
        val item = scheduledRecord[rating] ?: scheduledRecord[FSRSRating.GOOD]!!
        val updatedCard = card.copy(
            dueEpoch = item.card.due.time,
            stability = item.card.stability,
            difficulty = item.card.difficulty,
            reps = item.card.reps,
            lapses = item.card.lapses,
            state = item.card.state.value,
            lastReviewEpoch = now.time
        )
        flashcardDao.updateCard(updatedCard)
        val xpBonus = when (rating) {
            FSRSRating.AGAIN -> 5
            FSRSRating.HARD -> 10
            FSRSRating.GOOD -> 15
            FSRSRating.EASY -> 20
        }
        addXpAndStreak(xpBonus)
        return item
    }

    override suspend fun seedDefaultCardsIfEmpty() {
        val count = flashcardDao.getTotalCardCountSync()
        if (count == 0) {
            val defaultCards = mutableListOf<FlashcardEntity>()
            val now = System.currentTimeMillis()
            val topics = getAllTopics()
            for (pack in topics) {
                for (term in pack.keyTerms) {
                    val promptText = if (term.arabicTerm.isNotBlank()) {
                        "${term.term} (${term.arabicTerm})"
                    } else {
                        term.term
                    }
                    defaultCards.add(
                        FlashcardEntity(
                            id = "${pack.topicId}_${term.term.lowercase().replace(" ", "_")}",
                            topicId = pack.topicId,
                            frontPrompt = "Explain and define: $promptText\n[Thanaweya Official Syllabus]",
                            backAnswer = term.definition,
                            category = pack.chapterTitle,
                            stability = 2.0,
                            difficulty = 5.0,
                            reps = 0,
                            lapses = 0,
                            state = 0,
                            dueEpoch = now,
                            lastReviewEpoch = null
                        )
                    )
                }
            }
            if (defaultCards.isNotEmpty()) {
                flashcardDao.insertCards(defaultCards)
            }
        }
    }

    override fun getStudentProgress(): Flow<StudentProgressEntity?> {
        return progressDao.getProgress()
    }

    override suspend fun addXpAndStreak(xpToAdd: Int) {
        val current = progressDao.getProgress().firstOrNull() ?: StudentProgressEntity()
        val now = System.currentTimeMillis()
        val oneDayMs = 24L * 60 * 60 * 1000
        val daysSinceLastActive = (now - current.lastActiveEpoch) / oneDayMs
        val newStreak = when {
            daysSinceLastActive <= 1 -> current.streakDays
            daysSinceLastActive == 2L -> current.streakDays + 1
            else -> 1
        }
        val updated = current.copy(
            streakDays = newStreak,
            totalXp = current.totalXp + xpToAdd,
            lastActiveEpoch = now
        )
        progressDao.saveProgress(updated)
    }

    override suspend fun recordGradedQuestion(xpBonus: Int) {
        progressDao.recordGradedQuestion(xpBonus = xpBonus)
    }

    override suspend fun recordExamCompletionProgress(durationMinutes: Int, newMastery: Float, newPercentile: Float) {
        progressDao.recordExamCompletion(
            durationMinutes = durationMinutes,
            newMastery = newMastery,
            newPercentile = newPercentile
        )
    }

    override fun getExamAttempts(): Flow<List<ExamAttemptEntity>> {
        return examAttemptDao.getAllAttempts()
    }

    override fun getLatestExamAttempt(): Flow<ExamAttemptEntity?> {
        return examAttemptDao.getLatestAttempt()
    }

    override suspend fun recordExamAttempt(attempt: ExamAttemptEntity): Long {
        val id = examAttemptDao.insertAttempt(attempt)
        val xpEarned = (attempt.totalMarks * 25).toInt()
        addXpAndStreak(xpEarned)
        val minutes = (attempt.durationSeconds / 60).toInt().coerceAtLeast(1)
        val mastery = (attempt.percentage / 100.0).toFloat().coerceIn(0f, 1f)
        val percentile = (attempt.percentage * 0.98 + 2.0).toFloat().coerceIn(60f, 99.8f)
        recordExamCompletionProgress(minutes, mastery, percentile)
        return id
    }

    override fun getAllMistakes(): Flow<List<ExamMistakeEntity>> {
        return examMistakeDao?.getAllMistakes() ?: kotlinx.coroutines.flow.emptyFlow()
    }

    override fun getUnresolvedMistakes(): Flow<List<ExamMistakeEntity>> {
        return examMistakeDao?.getUnresolvedMistakes() ?: kotlinx.coroutines.flow.emptyFlow()
    }

    override fun getUnresolvedMistakesCount(): Flow<Int> {
        return examMistakeDao?.getUnresolvedMistakesCount() ?: kotlinx.coroutines.flow.flowOf(0)
    }

    override suspend fun recordMistakes(mistakes: List<ExamMistakeEntity>) {
        if (mistakes.isNotEmpty()) {
            examMistakeDao?.insertMistakes(mistakes)
        }
    }

    override suspend fun resolveMistake(mistakeId: Long) {
        examMistakeDao?.markResolved(mistakeId)
    }

    override suspend fun clearResolvedMistakes() {
        examMistakeDao?.clearResolvedMistakes()
    }

    override suspend fun deleteMistake(mistakeId: Long) {
        examMistakeDao?.deleteMistake(mistakeId)
    }

    override fun getTopicMasteries(): Flow<List<TopicMasteryEntity>> {
        return topicMasteryDao.getAllMasteries()
    }

    override suspend fun updateTopicMastery(topicId: String, scoreEarned: Float) {
        val existing = topicMasteryDao.getMasteryForTopic(topicId).firstOrNull()
        val topic = getTopicById(topicId)
        val unitNum = if (topic?.unit?.name?.contains("2") == true) 2 else 1
        val newMastery = if (existing != null) {
            val updatedLevel = (existing.masteryLevel * 0.7f + scoreEarned * 0.3f).coerceIn(0.0f, 1.0f)
            existing.copy(
                masteryLevel = updatedLevel,
                cardsReviewed = existing.cardsReviewed + 1,
                lastUpdatedEpoch = System.currentTimeMillis()
            )
        } else {
            TopicMasteryEntity(
                topicId = topicId,
                topicTitle = topic?.topicTitle ?: topicId,
                unitNumber = unitNum,
                masteryLevel = scoreEarned.coerceIn(0.0f, 1.0f),
                cardsReviewed = 1,
                averageStability = 3.0,
                lastUpdatedEpoch = System.currentTimeMillis()
            )
        }
        topicMasteryDao.upsertMastery(newMastery)
    }

    override suspend fun initializeTopicMasteriesIfEmpty() {
        val existing = topicMasteryDao.getAllMasteries().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            val defaults = getAllTopics().mapIndexed { index, pack ->
                val unitNum = if (pack.unit.name.contains("2")) 2 else 1
                val baseline = when (index % 4) {
                    0 -> 0.88f // Mastered (Green)
                    1 -> 0.72f // Good (Emerald)
                    2 -> 0.55f // Moderate (Orange)
                    else -> 0.35f // Needs review (Coral)
                }
                TopicMasteryEntity(
                    topicId = pack.topicId,
                    topicTitle = pack.topicTitle,
                    unitNumber = unitNum,
                    masteryLevel = baseline,
                    cardsReviewed = 4 + index * 2,
                    averageStability = 4.2,
                    lastUpdatedEpoch = System.currentTimeMillis()
                )
            }
            topicMasteryDao.upsertMasteries(defaults)
        }
    }

    // --- Saved Biology Definitions Implementation ---

    override fun getSavedDefinitions(): Flow<List<SavedBiologyDefinitionEntity>> {
        return savedDefinitionDao.getAllSavedDefinitions()
    }

    override fun getSavedDefinitionsByUnit(unitCategory: String): Flow<List<SavedBiologyDefinitionEntity>> {
        return savedDefinitionDao.getSavedDefinitionsByUnit(unitCategory)
    }

    override fun isDefinitionSaved(term: String): Flow<Boolean> {
        return savedDefinitionDao.isTermSaved(term)
    }

    override suspend fun saveDefinition(definition: SavedBiologyDefinitionEntity): Long {
        val id = savedDefinitionDao.insertOrUpdate(definition)
        updateSavedCountTelemetry()
        return id
    }

    override suspend fun toggleSaveDefinition(
        term: String,
        arabicTerm: String,
        definition: String,
        arabicDefinition: String,
        chapterTitle: String,
        unitCategory: String
    ): Boolean {
        val existing = savedDefinitionDao.getDefinitionByTerm(term)
        return if (existing != null) {
            savedDefinitionDao.delete(existing)
            updateSavedCountTelemetry()
            false
        } else {
            savedDefinitionDao.insertOrUpdate(
                SavedBiologyDefinitionEntity(
                    term = term,
                    arabicTerm = arabicTerm,
                    definition = definition,
                    arabicDefinition = arabicDefinition,
                    chapterTitle = chapterTitle,
                    unitCategory = unitCategory,
                    isBookmarked = true
                )
            )
            updateSavedCountTelemetry()
            true
        }
    }

    override suspend fun removeSavedDefinition(id: Long) {
        savedDefinitionDao.deleteById(id)
        updateSavedCountTelemetry()
    }

    override suspend fun updateDefinitionNotes(id: Long, notes: String) {
        savedDefinitionDao.updateStudentNotes(id, notes)
    }

    override suspend fun updateDefinitionMastery(id: Long, masteryLevel: String) {
        savedDefinitionDao.updateMasteryLevel(id, masteryLevel)
    }

    private suspend fun updateSavedCountTelemetry() {
        val count = savedDefinitionDao.getSavedCount().firstOrNull() ?: 0
        progressDao.updateSavedDefinitionsCount(count)
    }

    // --- Offline Key Concepts Implementation ---

    override fun getAllKeyConcepts(): Flow<List<KeyConceptEntity>> {
        return keyConceptDao.getAllKeyConcepts()
    }

    override fun getKeyConceptsByTopic(topicId: String): Flow<List<KeyConceptEntity>> {
        return keyConceptDao.getKeyConceptsByTopic(topicId)
    }

    override fun getKeyConceptsByUnit(unitCategory: String): Flow<List<KeyConceptEntity>> {
        return keyConceptDao.getKeyConceptsByUnit(unitCategory)
    }

    override fun getKeyConceptById(conceptId: String): Flow<KeyConceptEntity?> {
        return keyConceptDao.getKeyConceptById(conceptId)
    }

    override suspend fun upsertKeyConcept(concept: KeyConceptEntity) {
        keyConceptDao.upsert(concept)
    }

    override suspend fun seedDefaultKeyConceptsIfEmpty() {
        val count = keyConceptDao.getConceptsCount().firstOrNull() ?: 0
        if (count == 0) {
            val defaultConcepts = listOf(
                KeyConceptEntity(
                    conceptId = "sarcomere_huxley_sliding",
                    topicId = "sarcomere_contraction",
                    chapterId = "unit1_chap1",
                    unitCategory = "UNIT_1",
                    title = "Sliding Filament Theory (Huxley's Mechanism)",
                    arabicTitle = "نظرية الخيوط المنزلقة (آلية هكسلي للانقباض العضلي)",
                    summary = "Myosin cross-bridges bind actin filaments via Ca²⁺ and hydrolyze ATP to pull Z-lines closer together. H-zone disappears in complete contraction; A-band length remains constant.",
                    arabicEli5 = "تخيل أن خيوط الميوسين كأذرع تجديف تسحب حبال الأكتين نحو المنتصف، مما يُقصر المسافة بين خطوط Z دون أن يتغير طول خيوط الميوسين نفسها.",
                    keyTakeawaysJson = "طول المنطقة الداكنة (A) ثابت لا يتغير|المنطقة المضيئة (I) وشبه المضيئة (H) يقل طولهما|أيونات الكالسيوم تكشف مواقع الارتباط|ATP يفصل الروابط المستعرضة",
                    formulaOrRule = "عدد القطع العضلية = عدد خطوط Z - 1 | عدد المناطق الداكنة A = عدد القطع العضلية",
                    examTrapWarning = "احذر: لا يتغير طول خيوط الأكتين أو الميوسين إطلاقاً، بل تنزلق فوق بعضها فقط!"
                ),
                KeyConceptEntity(
                    conceptId = "pituitary_negative_feedback",
                    topicId = "hormonal_coordination_endocrine",
                    chapterId = "unit1_chap2",
                    unitCategory = "UNIT_1",
                    title = "Pituitary Master Gland & Negative Feedback",
                    arabicTitle = "سيدة الغدد الصماء والتغذية الراجعة السلبية",
                    summary = "Anterior pituitary secretes trophic hormones (TSH, ACTH, FSH, LH, GH). Peripheral hormone levels suppress pituitary secretion via negative feedback loops.",
                    arabicEli5 = "الغدة النخامية مثل ثرموستات المكيف الذكي، حين ترتفع نسبة الهرمونات في الدم تقلل إفرازها تلقائياً للحفاظ على الاتزان الداخلي.",
                    keyTakeawaysJson = "الفص الأمامي غدي يفرز TSH, ACTH, FSH, LH, GH|الفص الخلفي عصبي يخزن ويفرز ADH والأوكسيتوسين|التغذية الراجعة تمنع التضخم الهرموني",
                    formulaOrRule = "ارتفاع الثيروكسين في الدم يؤدي إلى تثبيط إفراز TSH من الفص الأمامي للنخامية",
                    examTrapWarning = "الفص الخلفي لا يُصنع الهرمونات، بل الخلايا العصبية المفرزة في تحت المهاد (Hypothalamus) هي التي تصنع ADH والأوكسيتوسين!"
                ),
                KeyConceptEntity(
                    conceptId = "double_fertilization_angiosperms",
                    topicId = "reproduction_double_fertilization",
                    chapterId = "unit1_chap3",
                    unitCategory = "UNIT_1",
                    title = "Double Fertilization in Flowering Plants",
                    arabicTitle = "الإخصاب المزدوج وتكوين الإندوسبيرم في النباتات الزهرية",
                    summary = "Pollen grain delivers two male generative nuclei: One fuses with egg nucleus (n + n = 2n Zygote), the other fuses with two polar nuclei of embryo sac (n + n + n = 3n Endosperm).",
                    arabicEli5 = "إخصاب ثنائي في خطوة واحدة: عريس للعروسة لتكوين الجنين (2n)، وعريس آخر لغذاء الجنين المحفوظ في الإندوسبيرم (3n).",
                    keyTakeawaysJson = "النواة الأنبوبية تخترق النسم لتصل للمنقير|النواة الذكرية الأولى تخصب البيضة (2n)|النواة الذكرية الثانية تندمج مع نواتي الكيس الجنيني (3n)",
                    formulaOrRule = "عدد الأنوية المشاركة في الإخصاب المزدوج = 5 أنوية (نواتان ذكريتان + نواة البيضة + نواتا الكيس الجنيني)",
                    examTrapWarning = "الإندوسبيرم نسيج ثلاثي الصبغيات (3n)، بينما غلاف البذرة والسمرة خلايا جسدية أنثوية (2n)!"
                ),
                KeyConceptEntity(
                    conceptId = "humoral_vs_cell_mediated_immunity",
                    topicId = "immunity_humoral_cellular",
                    chapterId = "unit1_chap4",
                    unitCategory = "UNIT_1",
                    title = "Humoral (B-Cell) vs Cell-Mediated (T-Cell) Immunity",
                    arabicTitle = "المناعة الخلطية بالخلايا البائية والمناعة الخلوية بالخلايا التائية",
                    summary = "Humoral: B-lymphocytes produce specific antibodies against extracellular antigens in body fluids. Cell-mediated: Cytotoxic TC cells and Natural Killer (NK) cells destroy infected and cancerous cells via perforin and granzymes.",
                    arabicEli5 = "المناعة الخلطية مثل سلاح الطيران يقصف الأعداء بالصواريخ (الأجسام المضادة) في سوائل الدم، بينما المناعة الخلوية مثل القوات الخاصة تقتحم الخلايا المصابة وتدمرها مباشرة بالبيرفورين.",
                    keyTakeawaysJson = "الخلايا التائية المساعدة TH تنشط كلا المسارين عبر الإنترلوكينات والسيتوكينات|الأجسام المضادة غير قادرة على دخول الخلايا المصابة لكبر حجمها|خلايا TC تفرز بروتين البيرفورين المثقب للغشاء والسموم اللمفاوية",
                    formulaOrRule = "مستقبل TH هو CD4 | مستقبل TC و TS هو CD8",
                    examTrapWarning = "الأجسام المضادة لا تدمر الميكروبات داخل الخلايا، بل تحيدها خارجياً فقط في الدم واللمف!"
                ),
                KeyConceptEntity(
                    conceptId = "dna_chargaff_replication_enzymes",
                    topicId = "dna_structure_replication",
                    chapterId = "unit2_chap1",
                    unitCategory = "UNIT_2",
                    title = "DNA Structure, Chargaff's Rules & Replication Enzymes",
                    arabicTitle = "تركيب لولب DNA المزدوج، قواعد شارجاف، وإنزيمات التضاعف",
                    summary = "Antiparallel double helix held by hydrogen bonds (A=T with 2 bonds, G≡C with 3 bonds). Replication requires Helicase (unwinds), DNA Polymerase (synthesizes 5'->3'), and Ligase (joins Okazaki fragments).",
                    arabicEli5 = "شريطا DNA سلالم متقابلة ومتعاكسة؛ إنزيم اللولب يفتح السوستة، وإنزيم البلمرة يضع درجات السلم الجديدة، وإنزيم الربط يخيط القطع الصغيرة في الشريط المتأخر.",
                    keyTakeawaysJson = "شريطا اللولب متعاكسان (5' إلى 3' يقابله 3' إلى 5')|نسبة A+G تساوي نسبة T+C دائماً في اللولب المزدوج|إنزيم البلمرة يضيف نيوكليوتيدات جديدة في اتجاه 5' إلى 3' فقط",
                    formulaOrRule = "A = T | G = C | (A + G) / (T + C) = 1 | عدد لفات DNA = عدد النيوكليوتيدات في الشريطين / 20",
                    examTrapWarning = "قواعد شارجاف تنطبق فقط على اللولب المزدوج (Double Stranded DNA)، ولا تنطبق على RNA أو الفيروسات أحادية الشريط!"
                ),
                KeyConceptEntity(
                    conceptId = "rna_translation_genetic_code",
                    topicId = "rna_transcription_translation",
                    chapterId = "unit2_chap2",
                    unitCategory = "UNIT_2",
                    title = "RNA Transcription, Genetic Code Math & Protein Synthesis",
                    arabicTitle = "تخليق البروتين، شفرات الأحماض الأمينية، ومضاد الكودون",
                    summary = "mRNA is transcribed from 3'->5' DNA template. Ribosome translates mRNA codons from AUG start to stop codons (UAA, UAG, UGA). tRNA brings specific amino acids using anticodons.",
                    arabicEli5 = "الخلية تستنسخ نسخة عمل رخيصة (mRNA) من المخطط الأصلي الثمين (DNA)، ثم يقرأها مصنع الريبوسوم كلمة بكلمة (كل كلمة 3 حروف كودون) ليركب سلسلة البروتين.",
                    keyTakeawaysJson = "كودون البدء AUG يشفر حمض الميثيونين دائماً|كودونات الوقف UAA, UAG, UGA لا تشفر أحماض أمينية ويرتبط بها عامل الإطلاق|الريبوسوم يتكون من تحت وحدتين وله موقعان: P (ببتيديل) و A (أمينوأسيل)",
                    formulaOrRule = "عدد الأحماض الأمينية = (عدد كودونات mRNA) - 1 (كودون الوقف)|أقل عدد نيوكليوتيدات = عدد الأحماض الأمينية × 3 + 3",
                    examTrapWarning = "كودون الوقف يُحسب ضمن كودونات mRNA، لكنه لا يُترجم إلى أي حمض أميني ويقابله عامل الإطلاق وليس tRNA!"
                )
            )
            keyConceptDao.upsertAll(defaultConcepts)
        }
    }
}
