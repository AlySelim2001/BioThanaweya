package com.example.data.repository

import com.example.data.model.*

/**
 * Curriculum Pack: التغذية في الكائنات الحية (Nutrition & Digestion)
 */
object NutritionCurriculumPack {

    val chapter = Chapter(
        id = "unit1_chap_nutrition",
        unit = UnitCategory.UNIT_1,
        number = 2,
        title = "Nutrition in Living Organisms",
        arabicTitle = "التغذية في الكائنات الحية",
        summary = "Autotrophic photosynthesis, light and dark (Calvin cycle) reactions, digestive enzymes in human alimentary canal, and villi absorption.",
        iconName = "restaurant"
    )

    val subTopic = SubTopic(
        id = "nutrition_digestion",
        chapterId = "unit1_chap_nutrition",
        unit = UnitCategory.UNIT_1,
        title = "Photosynthesis & Human Digestion",
        arabicTitle = "التغذية الذاتية (البناء الضوئي) والهضم والامتصاص في الإنسان",
        summary = "Chloroplast thylakoids vs stroma, photolysis of water, enzymatic breakdown in stomach/duodenum, and lacteal vs blood absorption pathways."
    )

    val pack = MasterStudyPack(
        topicId = "nutrition_digestion",
        topicTitle = "Photosynthesis & Human Digestion",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "التغذية في الكائنات الحية",
        eli5Analogy = "تخيل أن البلاستيدة الخضراء مطبخ شمسي متطور! في النهار، الثايلاكويد بيستقبل أشعة الشمس ويشطر المية عشان يخزن الطاقة في بطاريات ATP و NADPH (التفاعلات الضوئية). وفي الليل أو الظل، الستروما بتاخد الطاقة دي وتثبت ثاني أكسيد الكربون عشان تطبخ سكر الجلوكوز (دورة كالفن). أما في الإنسان، الجهاز الهضمي مصنع تفكيك عملاق يكسر البروتينات والدهون والنشويات بمقصات إنزيمية متخصصة لتمتصها الخملات الدقيقة في الأمعاء.",
        deepTechnicalBreakdown = """
            1. التفاعلات الضوئية (Light Reactions) في الجرانا (Grana/Thylakoids):
               - يمتص صبغ الكلوروفيل أ الطاقة الضوئية وتنشط إلكتروناته إلى مستويات طاقة أعلى.
               - يُستخدم جزء من الطاقة في شطر جزيء الماء H₂O إلى هيدروجين H₂ (يُحمل على مقتنص الإنزيم المساعد NADP⁺ مكوناً NADPH + H⁺) وأكسجين O₂ يتصاعد كناتج ثانوي.
               - يُستخدم الجزء الآخر في تكوين ATP من ADP + P (الفسفرة الضوئية Photophosphorylation).
            2. التفاعلات اللاضوئية (Dark/Calvin Reactions) في الستروما (Stroma):
               - تثبيت غاز CO₂ بواسطة الطاقة المختزنة في ATP وقوة الاختزال في NADPH.
               - يتكون أول مركب كيميائي ثابت وهو ثلاثي الكربون فوسفو جلسرالدهيد (PGAL)، الذي يُبنى منه الجلوكوز والنشا والدهون والبروتينات.
            3. الهضم والامتصاص في الإنسان:
               - الفم: إنزيم الأميليز اللعابي (Ptaylin) يهضم النشا إلى ملتوز في وسط قلوي ضعيف (pH 7.4).
               - المعدة: إفراز حمض HCl (pH 1.5 - 2.5) لتنشيط إنزيم الببسينوجين غير النشط إلى ببسين فعال يكسر البروتينات إلى عديدات ببتيد.
               - الأمعاء الدقيقة (الاثني عشر واللفائفي): إفراز العصارة البنكرياسية (أميليز، تريبسينوجين يُنشطه الإنتروكينيز، وليبيز يهضم الدهون بمساعدة الصفراء).
               - طريق الامتصاص في الخملة (Villus):
                 * الطريق الدموي: الماء والأملاح وسكر الجلوكوز والأحماض الأمينية وفيتامينات (B, C) -> الوريد البابي الكبدي -> الكبد -> الوريد الكبدي -> الوريد الأجوف السفلي.
                 * الطريق الليمفاوي: الأحماض الدهنية والجلسرين وفيتامينات (A, D, E, K) -> الوعاء اللبني -> الجهاز الليمفاوي -> الوريد الأجوف العلوي.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Photophosphorylation", "Synthesis of ATP using light energy in chloroplast thylakoids.", "الفسفرة الضوئية"),
            KeyTerm("PGAL (Phosphoglyceraldehyde)", "First stable 3-carbon intermediate compound in dark photosynthetic reactions.", "فوسفو جلسرالدهيد"),
            KeyTerm("Pepsin", "Gastric protease secreted as inactive pepsinogen, active in pH 1.5-2.5.", "الببسين"),
            KeyTerm("Villus & Microvilli", "Finger-like microscopic projections in the ileum multiplying absorption surface area.", "الخملات والخملات الدقيقة")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Photosynthesis Net Equation",
                formulaLatex = "6CO_2 + 12H_2O \\xrightarrow[Chlorophyll]{Light} C_6H_{12}O_6 + 6H_2O + 6O_2\\uparrow",
                description = "تمثيل انشطار الماء وتثبيت الكربون لإنتاج سكر الجلوكوز والأكسجين.",
                variables = "CO2: carbon dioxide, H2O: water, C6H12O6: glucose",
                sampleCalculation = "لإنتاج 1 جزيء جلوكوز (6C)، نحتاج 2 جزيء من مركب PGAL (3C) ناتجين من 6 دورات كالفن."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "مقارنة بين الطريق الدموي والطريق الليمفاوي للامتصاص",
                columnA = "الطريق الدموي (Blood Route)",
                columnB = "الطريق الليمفاوي (Lymphatic Route)",
                rows = listOf(
                    ComparisonRow("المواد الممتصة", "الجلوكوز، الأحماض الأمينية، الماء، الفيتامينات الذائبة في الماء (B, C)", "الأحماض الدهنية، الجلسرين، الفيتامينات الذائبة في الدهون (A, D, E, K)"),
                    ComparisonRow("الوعاء المستلم في الخملة", "الشعيرات الدموية الوريدية", "الوعاء اللبني (Lacteal Vessel)"),
                    ComparisonRow("المرور بالكبد", "يمر مباشرة بالوريد البابي الكبدي لضبط السكر وتخزين الجليكوجين", "لا يمر بالكبد إطلاقاً"),
                    ComparisonRow("المصب في القلب", "الوريد الأجوف السفلي (Inferior Vena Cava)", "الوريد الأجوف العلوي (Superior Vena Cava)")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "يذكر نواتج التفاعلات الضوئية واللاضوئية.", "ما هو المركب الكيميائي الأول الثابت الناتج عن البناء الضوئي؟"),
            BloomLevelItem("Understanding", 2, "يفسر دور حمض HCl في تنشيط الإنزيمات.", "علل: لا تهضم المعدة نفسها؟"),
            BloomLevelItem("Applying", 3, "يحدد مسار فيتامين D الممتص حتى وصوله إلى القلب.", "تتبع المسار التشريحي لامتصاص قطرة دهنية.")
        ),
        asciiSchematic = """
+-----------------------------------------------------------+
|          CHLOROPLAST ULTRASTRUCTURE & LIGHT REACTIONS      |
|                                                           |
|       Outer Membrane                                      |
|    +-------------------------------------------------+    |
|    |  Stroma (Dark Reactions - Calvin Cycle / CO2)   |    |
|    |                                                 |    |
|    |      [Thylakoid / Grana Disc]                   |    |
|    |   +----------------------------+                |    |
|    |   | H2O + Light ----> O2 ^     |                |    |
|    |   | Chlorophyll a Excitation   |                |    |
|    |   | NADP+ -> NADPH             |                |    |
|    |   | ADP + P -> ATP             |                |    |
|    |   +-------------+--------------+                |    |
|    |                 |  (ATP + NADPH)                |    |
|    |                 v                               |    |
|    |   +----------------------------+                |    |
|    |   | CO2 Fixation in Stroma     |                |    |
|    |   | 2 x PGAL (3C) -> Glucose   |                |    |
|    |   +----------------------------+                |    |
|    +-------------------------------------------------+    |
+-----------------------------------------------------------+
        """.trimIndent(),
        schematicCaption = "مخطط البلاستيدة الخضراء وتكامل التفاعلات الضوئية في الجرانا مع تثبيت ثاني أكسيد الكربون في الستروما.",
        flowcharts = listOf(
            FlowchartStep(1, "سقوط الضوء على الكلوروفيل", "إثارة إلكترونات ذرة المغنسيوم وانتقالها لمدارات طاقة أعلى.", "Light Photons"),
            FlowchartStep(2, "انشطار الماء الضوئي", "انشطار H2O إلى أكسجين يتصاعد وهيدروجين يُختزل به NADP+.", "Water-splitting complex"),
            FlowchartStep(3, "تثبيت CO2 في الستروما", "اندماج الهيدروجين المحمول على NADPH مع CO2 باستخدام ATP لإنتاج PGAL.", "RuBisCO / Calvin Cycle")
        ),
        visualInspectionMarkers = listOf(
            "موقع تحرر غاز الأكسجين: غشاء الثايلاكويد في الجرانا وليس الستروما.",
            "مصدر الأكسجين المتصاعد: الماء H2O (كما أثبت فان نيل وعالم كالفن) وليس ثاني أكسيد الكربون."
        ),
        youtube3DQueries = listOf(
            VideoQuery("Chloroplast light and dark reactions 3D animation", "آلية البناء الضوئي ودورة كالفن ثلاثية الأبعاد", "4-6 min")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "mcq_nutr_1",
                type = QuestionType.MCQ,
                question = "أي الأوعية الدموية التالية يحمل أعلى تركيز من الجلوكوز والأحماض الأمينية بعد تناول وجبة غنية بالكربوهيدرات بساعتين؟",
                options = listOf(
                    "الوريد الأجوف العلوي",
                    "الوريد البابي الكبدي",
                    "الوريد الكبدي",
                    "الشريان الكلوي"
                ),
                correctOptionIndex = 1,
                officialAnswer = "الوريد البابي الكبدي",
                rationale = "يمتص الجلوكوز مباشرة من خملات اللفائفي إلى الوريد البابي الكبدي فيكون تركيزه أعلى ما يمكن فيه، ثم يدخل الكبد فيتم تخزين الفائض كجليكوجين، فيخرج من الوريد الكبدي بتركيز متزن (حوالي 80-120 مجم/100سم³).",
                ministryExamTip = "احذر الخلط بين الوريد البابي الكبدي (أعلى تركيز بعد الهضم مباشرة) والوريد الكبدي (أعلى تركيز أثناء الصيام الطويل بفضل تحلل الجليكوجين)."
            ),
            PracticeQuestion(
                id = "mcq_nutr_2",
                type = QuestionType.MCQ,
                question = "ما هو المركب الكيميائي الأول الثابت الناتج عن عملية البناء الضوئي في تجربة كالفن على طحلب الكلوريلا؟",
                options = listOf(
                    "سكر الجلوكوز (6C)",
                    "حمض البيروفيك (3C)",
                    "فوسفو جلسرالدهيد PGAL (3C)",
                    "سكر السكروز (12C)"
                ),
                correctOptionIndex = 2,
                officialAnswer = "فوسفو جلسرالدهيد PGAL (3C)",
                rationale = "أثبت العالم ملفن كالفن باستخدام نظير الكربون المشع C-14 أن الجلوكوز لا يتكون في خطوة واحدة، بل يتكون أولاً مركب PGAL بعد ثانيتين فقط من التعرض للضوء.",
                ministryExamTip = "فوسفو جلسرالدهيد (PGAL) هو أيضاً أول مركب ناتج عن شطر الفركتوز 1، 6 ثنائي الفوسفات في مرحلة انشطار الجلوكوز بالتنفس الخلوي."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "يعتقد الطالب أن الأكسجين المتصاعد في البناء الضوئي مصدره غاز ثاني أكسيد الكربون CO₂.",
                scientificTruth = "أثبتت تجارب فان نيل وباحثو جامعة كاليفورنيا باستخدام نظير الأكسجين O-18 أن مصدر الأكسجين المتصاعد هو شطر الماء H₂O فقط.",
                examTrapWarning = "إذا استخدمت نباتاً مزوداً بماء يحتوي على O-18 و CO₂ يحتوي على O-16، فإن الأكسجين المتصاعد سيكون O-18 بالكامل.",
                goldenRule = "أكسجين الماء يتصاعد في الهواء، بينما أكسجين CO₂ يدخل في تركيب الجلوكوز والماء الناتج."
            )
        ),
        flashcards = listOf(
            Flashcard(
                id = "fc_nutr_1",
                topicId = "nutrition_digestion",
                frontPrompt = "ما هو العضو الذي يفرز إنزيمات لهضم جميع أنواع الغذاء (كربوهيدرات، بروتينات، دهون)؟",
                backAnswer = "البنكرياس (العصارة البنكرياسية: أميليز بنكرياسي للنشا، تريبسينوجين للبروتين، وليبيز للدهون).",
                category = "Nutrition"
            ),
            Flashcard(
                id = "fc_nutr_2",
                topicId = "nutrition_digestion",
                frontPrompt = "إلى أي وعاء دموي تصب الأوعية اللبنية الحاملة للأحماض الدهنية والفيتامينات الذائبة في الدهون؟",
                backAnswer = "الوريد الأجوف العلوي (عبر الجهاز الليمفاوي دون المرور بالكبد).",
                category = "Nutrition"
            )
        ),
        examSimulationItem = MinistryExamItem(
            id = "exam_nutr_essay",
            questionType = QuestionType.GIVE_REASON,
            prompt = "علل: استئصال الحوصلة الصفراوية (المرارة) يؤثر سلباً على هضم وامتصاص الوجبات الغنية بالمواد الدهنية.",
            maxScore = 2.0,
            requiredKeywords = listOf("العصارة الصفراوية", "مستحلب دهني", "ليبيز", "المساحة السطحية"),
            partialCreditKeywords = listOf("تفتيت الدهون", "الاثني عشر"),
            modelAnswer = "لأن الحوصلة الصفراوية تخزن العصارة الصفراوية التي تفرز على الدهون في الاثني عشر، فتحولها إلى مستحلب دهني (تجزئة الدهون لقطيرات دقيقة) مما يزيد من مساحة السطح المعرضة لعمل إنزيم الليبيز البنكرياسي؛ وبغيابها يصبح التحلل المائي للدهون بطيئاً جداً وغير مكتمل.",
            remediationNotes = "يجب ذكر تحويل الدهون إلى مستحلب دهني وتسهيل عمل إنزيم الليبيز بزيادة مساحة السطح."
        ),
        arabicEli5 = "تخيل أن البلاستيدة الخضراء مطبخ شمسي متطور! في النهار، الثايلاكويد بيستقبل أشعة الشمس ويشطر المية عشان يخزن الطاقة في بطاريات ATP و NADPH (التفاعلات الضوئية). وفي الليل أو الظل، الستروما بتاخد الطاقة دي وتثبت ثاني أكسيد الكربون عشان تطبخ سكر الجلوكوز (دورة كالفن). أما في الإنسان، الجهاز الهضمي مصنع تفكيك عملاق يكسر البروتينات والدهون والنشويات بمقصات إنزيمية متخصصة لتمتصها الخملات الدقيقة في الأمعاء.",
        textbookPageReference = "كتاب الأحياء - التغذية والهضم (الوظائف الحيوية)"
    )
}
