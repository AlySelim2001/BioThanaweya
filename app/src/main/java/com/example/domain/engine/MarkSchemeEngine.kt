package com.example.domain.engine

data class WeightedKeyword(
    val term: String,
    val weight: Double,
    val synonyms: List<String> = emptyList(),
    val requiredDescription: String = ""
)

enum class PenaltyFlag(val code: String, val deduction: Double, val warningArabic: String, val warningEnglish: String) {
    A_BAND_NARROWS(
        code = "A_BAND_NARROWS",
        deduction = 0.5,
        warningArabic = "خطأ فادح: المنطقة الداكنة (A-band) تظل ثابتة الطول دائما ولا تقصر لأنها تمثل طول خيوط الميوسين السميكة.",
        warningEnglish = "Fatal Error: A-band never narrows; its length remains strictly constant during contraction!"
    ),
    CALCIUM_BINDS_MYOSIN(
        code = "CALCIUM_BINDS_MYOSIN",
        deduction = 0.5,
        warningArabic = "خطأ علمي: أيونات الكالسيوم ترتبط بالتروبونين على خيوط الأكتين لتكشف مواقع الارتباط، وليس بالميوسين.",
        warningEnglish = "Scientific Error: Calcium binds to troponin on actin filaments to expose binding sites, not to myosin."
    ),
    STRANDS_SHORTEN(
        code = "STRANDS_SHORTEN",
        deduction = 0.5,
        warningArabic = "خطأ مفاهيمي: خيوط الأكتين والميوسين تنزلق فقط ولا تقصر إطلاقا، ما يقصر هو القطعة العضلية ككل.",
        warningEnglish = "Misconception: Protein filaments slide, they never shrink in physical length."
    )
}

data class DetailedGradingResult(
    val rawScore: Double,
    val finalScore: Double,
    val maxScore: Double,
    val detectedKeywords: List<String>,
    val missingKeywords: List<String>,
    val triggeredPenalties: List<PenaltyFlag>,
    val totalDeduction: Double,
    val examinerFeedback: String,
    val modelAnswer: String,
    val isFullMark: Boolean
)

object MarkSchemeEngine {

    private val PENALTY_PATTERNS = mapOf(
        PenaltyFlag.A_BAND_NARROWS to listOf(
            "a band shortens", "a band narrows", "a band decreases", "تقصر المنطقه الداكنه",
            "يقل طول المنطقه a", "نقص المنطقه الداكنه", "a shortens", "انكماش a"
        ),
        PenaltyFlag.CALCIUM_BINDS_MYOSIN to listOf(
            "calcium binds myosin", "calcium attaches to myosin", "يرتبط الكالسيوم بالميوسين",
            "اتصال الكالسيوم بالميوسين", "ارتباط ca بالميوسين"
        ),
        PenaltyFlag.STRANDS_SHORTEN to listOf(
            "filaments shorten", "strands shorten", "actin shortens", "myosin shortens",
            "تقصر الخيوط", "انكماش خيوط الاكتين", "انكماش الميوسين", "قصر طول الخيوط البروتينيه"
        )
    )

    // Common bilingual synonyms dictionary
    private val SYNONYMS = mapOf(
        "actin" to listOf("اكتين", "خيوط رفيعه", "thin filament", "thin filaments"),
        "myosin" to listOf("ميوسين", "خيوط سميكه", "thick filament", "thick filaments"),
        "cross bridges" to listOf("روابط مستعرضه", "خطاطيف", "transverse hooks", "hooks", "cross-bridges"),
        "atp" to listOf("اي تي بي", "طاقه", "adenosine triphosphate"),
        "calcium" to listOf("كالسيوم", "ca2+", "ca++", "ca ions"),
        "cholinesterase" to listOf("كولينستريز", "كولين استريز", "ache", "acetylcholinesterase"),
        "acetylcholine" to listOf("اسيتيل كولين", "استيل كولين", "ach"),
        "sarcomere" to listOf("قطعه عضليه", "ساركومير"),
        "depolarization" to listOf("ازاله الاستقطاب", "لااستقطاب", "انعكاس الشحنه"),
        "repolarization" to listOf("عوده الاستقطاب", "اعاده الاستقطاب", "جهد الراحه"),
        "perforin" to listOf("بيرفورين", "بروتين صانع الثقوب", "pore forming"),
        "lymphotoxins" to listOf("سموم ليمفاويه", "سم ليمفاوي", "apoptosis genes"),
        "t helper" to listOf("تائيه مساعده", "th", "cd4"),
        "cytotoxic" to listOf("تائيه سامه", "تائيه قاتله", "tc", "cd8"),
        "suppressor" to listOf("تائيه كابحه", "تائيه مثبطه", "ts"),
        "progesterone" to listOf("بروجستيرون", "هرمون الحمل"),
        "estrogen" to listOf("استروجين", "استراديول"),
        "corpus luteum" to listOf("جسم اصفر"),
        "graafian follicle" to listOf("حويصله جراف"),
        "helicase" to listOf("هيليكيز", "انزيم اللولب", "كسر الروابط الهيدروجينيه"),
        "dna ligase" to listOf("انزيم الربط", "ربط اوكازاكي"),
        "okazaki" to listOf("اوكازاكي", "قطع اوكازاكي", "lagging strand")
    )

    fun grade(
        studentInput: String,
        weightedKeywords: List<WeightedKeyword>,
        maxScore: Double = 2.0,
        modelAnswer: String = ""
    ): DetailedGradingResult {
        val normalizedInput = ArabicTextNormalizer.normalize(studentInput)

        if (normalizedInput.length < 4) {
            return DetailedGradingResult(
                rawScore = 0.0,
                finalScore = 0.0,
                maxScore = maxScore,
                detectedKeywords = emptyList(),
                missingKeywords = weightedKeywords.map { it.term },
                triggeredPenalties = emptyList(),
                totalDeduction = 0.0,
                examinerFeedback = "The answer is empty or too brief. Thanaweya Amma mark schemes require specific scientific terms and causality.",
                modelAnswer = modelAnswer,
                isFullMark = false
            )
        }

        val detectedKeywords = mutableListOf<String>()
        val missingKeywords = mutableListOf<String>()
        var earnedWeight = 0.0
        val totalWeight = weightedKeywords.sumOf { it.weight }.coerceAtLeast(1.0)

        // Evaluate keywords
        for (kw in weightedKeywords) {
            val isFound = isKeywordMatch(normalizedInput, kw)
            if (isFound) {
                detectedKeywords.add(kw.term)
                earnedWeight += kw.weight
            } else {
                missingKeywords.add(kw.term)
            }
        }

        var rawScore = (earnedWeight / totalWeight) * maxScore

        // Detect Penalty Flags
        val triggeredPenalties = mutableListOf<PenaltyFlag>()
        var totalDeduction = 0.0

        for ((flag, patterns) in PENALTY_PATTERNS) {
            val hasPenalty = patterns.any { pattern ->
                val normalizedPattern = ArabicTextNormalizer.normalize(pattern)
                normalizedInput.contains(normalizedPattern)
            }
            if (hasPenalty) {
                triggeredPenalties.add(flag)
                totalDeduction += flag.deduction
            }
        }

        val finalScore = maxOf(0.0, rawScore - totalDeduction)
        val isFullMark = finalScore >= (maxScore * 0.95)

        val feedback = buildString {
            if (isFullMark) {
                append("🌟 الدرجة الكاملة! إجابة نموذجية مطابقة لمعايير وزارة التربية والتعليم والتعليم الفني.")
            } else if (finalScore > 0.0) {
                append("جيد، ولكن تم خصم درجات لنقص بعض المصطلحات أو الوقوع في مصائد امتحانية.")
            } else {
                append("إجابة غير مكتملة علميًا. يرجى مراجعة نموذج الإجابة الرسمي والتركيز على التسلسل السببي.")
            }

            if (triggeredPenalties.isNotEmpty()) {
                append("\n\n⚠️ تم تطبيق خصم فوري (-${totalDeduction} درجة) بسبب:")
                triggeredPenalties.forEach { flag ->
                    append("\n• ${flag.warningArabic}")
                }
            }

            if (missingKeywords.isNotEmpty()) {
                append("\n\n❌ كلمات مفتاحية مفقودة ملزمة للدرجة: ")
                append(missingKeywords.joinToString("، "))
            }
        }

        return DetailedGradingResult(
            rawScore = rawScore,
            finalScore = finalScore,
            maxScore = maxScore,
            detectedKeywords = detectedKeywords,
            missingKeywords = missingKeywords,
            triggeredPenalties = triggeredPenalties,
            totalDeduction = totalDeduction,
            examinerFeedback = feedback,
            modelAnswer = modelAnswer,
            isFullMark = isFullMark
        )
    }

    private fun isKeywordMatch(normalizedText: String, keyword: WeightedKeyword): Boolean {
        val cleanTerm = ArabicTextNormalizer.normalize(keyword.term)
        if (normalizedText.contains(cleanTerm)) return true

        // Check configured synonyms
        for (syn in keyword.synonyms) {
            val cleanSyn = ArabicTextNormalizer.normalize(syn)
            if (normalizedText.contains(cleanSyn)) return true
        }

        // Check global dictionary synonyms
        val dictSyns = SYNONYMS[keyword.term.lowercase()] ?: emptyList()
        for (syn in dictSyns) {
            val cleanSyn = ArabicTextNormalizer.normalize(syn)
            if (normalizedText.contains(cleanSyn)) return true
        }

        return false
    }
}
