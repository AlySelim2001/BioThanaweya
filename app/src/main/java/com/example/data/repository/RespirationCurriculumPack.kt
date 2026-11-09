package com.example.data.repository

import com.example.data.model.*

/**
 * Curriculum Pack: التنفس الخلوي وإنتاج الطاقة (Cellular Respiration & ATP)
 */
object RespirationCurriculumPack {

    val chapter = Chapter(
        id = "unit1_chap_respiration",
        unit = UnitCategory.UNIT_1,
        number = 4,
        title = "Cellular Respiration & Bioenergetics",
        arabicTitle = "التنفس الخلوي وإنتاج الطاقة",
        summary = "Aerobic glycolysis in cytosol, Krebs citric acid cycle in mitochondrial matrix, oxidative phosphorylation in cristae, and 38 ATP balance sheet.",
        iconName = "bolt"
    )

    val subTopic = SubTopic(
        id = "cellular_respiration",
        chapterId = "unit1_chap_respiration",
        unit = UnitCategory.UNIT_1,
        title = "Glycolysis, Krebs Cycle & Electron Transport Chain",
        arabicTitle = "انشطار الجلوكوز ودورة كريبس وسلسلة نقل الإلكترون",
        summary = "Substrate-level vs oxidative phosphorylation, NADH/FADH2 oxidation yields, oxygen as terminal electron acceptor, and lactic acid vs alcoholic fermentation."
    )

    val pack = MasterStudyPack(
        topicId = "cellular_respiration",
        topicTitle = "Glycolysis, Krebs Cycle & Electron Transport Chain",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "التنفس الخلوي وإنتاج الطاقة",
        eli5Analogy = "التنفس الخلوي عامل زي تكسير صخرة فحم كبيرة (سكر الجلوكوز) وتحويلها لعملات معدنية صغيرة جاهزة للصرف فوراً (جزيئات ATP)! في البداية برة الميتوكوندريا، بنكسر الجلوكوز نصين في السيتوسول ونكسب 2 ATP و 2 عربية نقل إلكترونات (NADH). بعدين ندخل جوة الميتوكوندريا (المحرقة الكبرى) في دورة كريبس نلف ونفك الروابط ونملى عربيات النقل (NADH و FADH2). وفي المحطة الأخيرة على أعراف الميتوكوندريا (سلسلة نقل الإلكترون)، العربيات تفرغ شحنتها عند الأكسجين وناخد من كل جزيء جلوكوز 38 عملة ATP!",
        deepTechnicalBreakdown = """
            1. مرحلة انشطار الجلوكوز (Glycolysis) في السيتوسول:
               - تحدث في غياب أو وجود الأكسجين وتستهلك 2 ATP لتنشيط الجلوكوز.
               - ينشطر الفركتوز 1، 6 ثنائي الفوسفات إلى 2 PGAL، يتأكسدان إلى 2 حمض بيروفيك (3C).
               - الحصيلة المباشرة: 2 ATP مباشر + 2 NADH + 2 حمض بيروفيك.
            2. دورة كريبس (Krebs Cycle) في مادة الأساس (Matrix):
               - أكسدة البيروفيك لمجموعة أسيتيل (2C) مع خروج CO2 وإنتاج NADH.
               - يرتبط الأسيتيل مع حمض أوكسالواسيتيك (4C) ليعطي حمض الستريك (6C).
               - حصيلة دورتي كريبس: 2 ATP مباشر + 6 NADH + 2 FADH2 + 4 CO2.
            3. سلسلة نقل الإلكترون في الأعراف:
               - مرور الإلكترونات عبر السيتوكرومات، والمستقبل الأخير هو الأكسجين O2 لإنتاج H2O.
               - كل NADH يعطي 3 ATP، وكل FADH2 يعطي 2 ATP.
               - الحصيلة الكلية للأكسدة الهوائية = 38 جزيء ATP.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Glycolysis", "Splitting of glucose in cytosol yielding net 2 ATP and 2 NADH.", "انشطار الجلوكوز"),
            KeyTerm("Cytochromes", "Electron carrier proteins in mitochondrial cristae transferring electrons to O2.", "السيتوكرومات"),
            KeyTerm("Terminal Electron Acceptor", "Molecular oxygen accepting electrons and protons to form water.", "المستقبل الأخير للإلكترونات")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Total Aerobic ATP Yield",
                formulaLatex = "\\text{Total ATP} = 2_{\\text{Gly}} + 2_{\\text{Krebs}} + (10 \\times 3)_{\\text{NADH}} + (2 \\times 2)_{\\text{FADH}_2} = 38 \\text{ ATP}",
                description = "حساب الطاقة الكلية للأكسدة الهوائية الكاملة لجزيء جلوكوز.",
                variables = "10 NADH = 30 ATP, 2 FADH2 = 4 ATP, 4 direct ATP = 38 ATP total",
                sampleCalculation = "في غياب الأكسجين (تخمر لاكتيكي)، ينتج 2 جزيء ATP فقط."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "مقارنة بين التنفس الهوائي والتنفس اللاهوائي",
                columnA = "التنفس الهوائي",
                columnB = "التنفس اللاهوائي (التخمر)",
                rows = listOf(
                    ComparisonRow("الحاجة للأكسجين", "يتطلب وجود الأكسجين كمستقبل نهائي", "يحدث عند غياب أو نقص الأكسجين"),
                    ComparisonRow("كمية ATP الناتجة", "38 جزيء ATP لكل جزيء جلوكوز", "2 جزيء ATP فقط لكل جزيء جلوكوز"),
                    ComparisonRow("النواتج في العضلات", "ثاني أكسيد الكربون وماء وطاقة عالية", "حمض لاكتيك يسبب إجهاد العضلة")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "يذكر نواتج أكسدة جزيء NADH و FADH2.", "كم ATP ينتج عن أكسدة NADH؟"),
            BloomLevelItem("Understanding", 2, "يفسر توقف دورة كريبس عند غياب الأكسجين.", "علل: تتوقف دورة كريبس لاهوائياً؟"),
            BloomLevelItem("Applying", 3, "يحسب عدد جزيئات ATP الناتجة من أكسدة 3 جزيئات جلوكوز.", "احسب ATP من 2 جزيء مالتوز.")
        ),
        asciiSchematic = """
+-----------------------------------------------------------+
|          CELLULAR RESPIRATION: 38 ATP SUMMARY MAP         |
|  Glucose (6C) -> 2 Pyruvate (3C)  [+2 ATP direct, +2 NADH]|
|  Mitochondrial Matrix: 2 Krebs    [+2 ATP direct, +6 NADH]|
|  Cristae ETC: 10 NADH x 3 = 30, 2 FADH2 x 2 = 4           |
|  GRAND TOTAL PER GLUCOSE = 38 ATP + 6 CO2 + 6 H2O         |
+-----------------------------------------------------------+
        """.trimIndent(),
        schematicCaption = "مخطط مسار التنفس الخلوي وإنتاج 38 جزيء ATP في الميتوكوندريا والسيتوسول.",
        flowcharts = listOf(
            FlowchartStep(1, "انشطار الجلوكوز في السيتوسول", "شطر الجلوكوز إلى 2 حمض بيروفيك مع إنتاج 2 ATP و 2 NADH.", "Cytosol"),
            FlowchartStep(2, "دورة كريبس في مادة الأساس", "أكسدة الأسيتيل وإطلاق CO2 واختزال NAD+ و FAD.", "Matrix"),
            FlowchartStep(3, "سلسلة نقل الإلكترون", "مرور الإلكترونات للأكسجين وبناء 34 ATP عبر إنزيم بناء ATP.", "Cristae")
        ),
        visualInspectionMarkers = listOf(
            "الأكسجين هو المستقبل الأخير للإلكترونات في نهاية سلسلة نقل الإلكترون.",
            "الماء ينتج في الأعراف، بينما CO2 ينتج في مادة الأساس للميتوكوندريا."
        ),
        youtube3DQueries = listOf(
            VideoQuery("Cellular respiration Krebs cycle 3D", "دورة كريبس وسلسلة نقل الإلكترون ثلاثية الأبعاد", "4-5 min")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "mcq_resp_1",
                type = QuestionType.MCQ,
                question = "كم عدد جزيئات ATP الناتجة بصورة مباشرة على مستوى مادة التفاعل من أكسدة جزيء جلوكوز واحد داخل الميتوكوندريا فقط؟",
                options = listOf("2 جزيء ATP", "4 جزيئات ATP", "34 جزيء ATP", "38 جزيء ATP"),
                correctOptionIndex = 0,
                officialAnswer = "2 جزيء ATP",
                rationale = "داخل الميتوكوندريا ينتج ATP المباشر فقط من دورتي كريبس (جزيء واحد لكل دورة = 2 ATP)، بينما الـ 2 ATP الأخريان نتجتا في السيتوسول خارج الميتوكوندريا.",
                ministryExamTip = "احذر الخلط بين الناتج المباشر في الخلية كاملة (4) والناتج المباشر داخل الميتوكوندريا فقط (2)."
            ),
            PracticeQuestion(
                id = "mcq_resp_2",
                type = QuestionType.MCQ,
                question = "ما هو المستقبل الأخير للإلكترونات والبروتونات في نهاية سلسلة نقل الإلكترون؟",
                options = listOf("مرافق الإنزيم NAD+", "مرافق الإنزيم FAD", "غاز الأكسجين O2", "حمض البيروفيك"),
                correctOptionIndex = 2,
                officialAnswer = "غاز الأكسجين O2",
                rationale = "يستقبل الأكسجين الإلكترونات الهابطة من السيتوكرومات ويتحد مع بروتونات الهيدروجين لإنتاج الماء H2O.",
                ministryExamTip = "غياب الأكسجين يوقف تدفق الإلكترونات في السلسلة بالكامل."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "يعتقد الطالب أن دورة كريبس تستهلك الأكسجين مباشرة.",
                scientificTruth = "دورة كريبس لا تستهلك الأكسجين في أي خطوة، لكنها تتوقف لاهوائياً لنفاد NAD+ و FAD المؤكسدين.",
                examTrapWarning = "الأكسجين يستهلك فقط في سلسلة نقل الإلكترون وليس في كريبس.",
                goldenRule = "الأكسجين هو المستقبل النهائي للإلكترونات فقط."
            )
        ),
        flashcards = listOf(
            Flashcard(
                id = "fc_resp_1",
                topicId = "cellular_respiration",
                frontPrompt = "كم جزيء ATP ينتج عن أكسدة جزيء NADH واحد في سلسلة نقل الإلكترون؟",
                backAnswer = "3 جزيئات ATP (بينما FADH2 ينتج 2 جزيء ATP).",
                category = "Respiration"
            ),
            Flashcard(
                id = "fc_resp_2",
                topicId = "cellular_respiration",
                frontPrompt = "ما الحصيلة الكلية من ATP لأكسدة جزيء جلوكوز أكسدة هوائية تامة؟",
                backAnswer = "38 جزيء ATP.",
                category = "Respiration"
            )
        ),
        examSimulationItem = MinistryExamItem(
            id = "exam_resp_essay",
            questionType = QuestionType.GIVE_REASON,
            prompt = "علل: تتوقف دورة كريبس عند غياب الأكسجين رغم أن تفاعلاتها لا تستهلك الأكسجين مباشرة.",
            maxScore = 2.0,
            requiredKeywords = listOf("سلسلة نقل الإلكترون", "المستقبل الأخير", "أكسدة NADH", "نفاد NAD+"),
            partialCreditKeywords = listOf("تراكم الإلكترونات", "غياب FAD"),
            modelAnswer = "لأن تفاعلات دورة كريبس تتطلب توفر مرافقات الإنزيم في صورتها المؤكسدة (NAD+ و FAD) لاستقبال الإلكترونات؛ وعند غياب الأكسجين (المستقبل النهائي للإلكترونات) تتوقف سلسلة نقل الإلكترون، وتظل جميع جزيئات NADH و FADH2 مختزلة دون أكسدة، مما يؤدي لنفاد NAD+ و FAD الحرة فيتوقف تفاعل دورة كريبس.",
            remediationNotes = "يجب الربط بين توقف السلسلة وعدم أكسدة النواقل ونفاد NAD+."
        ),
        arabicEli5 = "التنفس الخلوي عامل زي تكسير صخرة فحم كبيرة (سكر الجلوكوز) وتحويلها لعملات معدنية صغيرة جاهزة للصرف فوراً (جزيئات ATP)! في البداية برة الميتوكوندريا، بنكسر الجلوكوز نصين في السيتوسول ونكسب 2 ATP و 2 عربية نقل إلكترونات (NADH). بعدين ندخل جوة الميتوكوندريا (المحرقة الكبرى) في دورة كريبس نلف ونفك الروابط ونملى عربيات النقل (NADH و FADH2). وفي المحطة الأخيرة على أعراف الميتوكوندريا (سلسلة نقل الإلكترون)، العربيات تفرغ شحنتها عند الأكسجين وناخد من كل جزيء جلوكوز 38 عملة ATP!",
        textbookPageReference = "كتاب الأحياء - التنفس الخلوي وإنتاج ATP"
    )
}
