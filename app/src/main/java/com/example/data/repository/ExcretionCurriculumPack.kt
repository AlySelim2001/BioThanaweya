package com.example.data.repository

import com.example.data.model.*

/**
 * Curriculum Pack: الإخراج في الكائنات الحية (Excretion & Nephron Physiology)
 */
object ExcretionCurriculumPack {

    val chapter = Chapter(
        id = "unit1_chap_excretion",
        unit = UnitCategory.UNIT_1,
        number = 5,
        title = "Excretion in Living Organisms",
        arabicTitle = "الإخراج في الكائنات الحية",
        summary = "Skin sweat glands, kidney anatomy, nephron ultrafiltration in Bowman's capsule, selective reabsorption in renal tubules, and liver urea cycle.",
        iconName = "filter_alt"
    )

    val subTopic = SubTopic(
        id = "excretion_homeostasis",
        chapterId = "unit1_chap_excretion",
        unit = UnitCategory.UNIT_1,
        title = "Nephron Filtration & Osmoregulation",
        arabicTitle = "الترشيح وإعادة الامتصاص الاختياري في النفرون والإخراج في الجلد والكبد",
        summary = "Glomerular filtration, active tubular reabsorption of glucose/water, Loop of Henle countercurrent, ADH hormone action, and artificial kidney hemodialysis."
    )

    val pack = MasterStudyPack(
        topicId = "excretion_homeostasis",
        topicTitle = "Nephron Filtration & Osmoregulation",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "الإخراج في الكائنات الحية",
        eli5Analogy = "الكلية عاملة زي مصفاة فائقة الذكاء وموظف جمارك دقيق جداً! كل دقيقة، الدم بيدخل محفظة بومان بالملايين، ويتصفى منه كل حاجة صغيرة (مية، جلوكوز، أملاح، بولينا) ما عدا خلايا الدم وبروتينات البلازما الكبيرة عشان متتسربش في البول. بعد كده في الأنابيب الملتفة، موظف الجمارك (إعادة الامتصاص الاختياري) يرجع كل نقطة جلوكوز وكل المية اللي الجسم محتاجها للدم بالنقل النشط، ويرمي البولينا والسموم برة في البول!",
        deepTechnicalBreakdown = """
            1. مفهوم الإخراج العلمي في الأحياء:
               - لا تعتبر الفضلات إخراجاً إلا إذا عبرت الأغشية البلازمية شبه المنفذة للخلايا.
               - التبرز والنيتروجين في هواء الزفير لا يعتبران إخراجاً بالمعنى العلمي لأنهما لم يعبرا أغشية بلازمية.
            2. الكلية والوحدة الوظيفية (النفرون Nephron):
               - كل كلية في الإنسان تحتوي على حوالي مليون نفرون.
               - محفظة بومان (Bowman's Capsule) في القشرة: يحدث فيها الترشيح الفائق (Ultrafiltration) لمكونات بلازما الدم السائلة (ماء، جلوكوز، أملاح، أحماض أمينية، يوريا) بينما لا تترشح خلايا الدم ولا جزيئات البروتين الكبيرة لكبر حجمها.
               - الأنبوبة الملتفة القريبة (PCT)، ثنية هنل (Loop of Henle) في النخاع، والأنبوبة الملتفة البعيدة (DCT):
                 * يحدث فيها إعادة الامتصاص الاختياري (Selective Reabsorption) بالنقل النشط.
                 * استعادة 100% من سكر الجلوكوز والأحماض الأمينية، ومعظم الماء والأملاح الحيوية إلى شبكة الشعيرات الدموية المحيطة.
                 * إذا زاد الجلوكوز في الدم عن العتبة الكلوية (300 مجم/100سم³ كما في مرضى السكري)، يفشل النفرون في إعادة امتصاص الفائض فيظهر الجلوكوز في البول (مدر للبول بالخاصية الإسموزية).
            3. الكبد وجهاز الغسيل الكلوي (Hemodialysis):
               - الكبد ينزع مجموعة الأمين (-NH2) من الأحماض الأمينية الزائدة ويحولها إلى يوريا (بولينا) غير سامة نسبياً ترسل للكلية.
               - جهاز الكلى الصناعي: يعتمد على ظاهرة الانتشار (Diffusion) عبر غشاء شبه منفذ بين دم المريض وسائل تنقية يحتوي على نفس تركيزات البلازما الطبيعية عدا اليوريا، فتنتقل اليوريا من دم المريض إلى السائل.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Ultrafiltration", "Non-selective filtration of blood plasma under high pressure into Bowman's capsule.", "الترشيح الفائق"),
            KeyTerm("Selective Reabsorption", "Active uptake of glucose, water, and essential ions from nephron tubules back into blood.", "إعادة الامتصاص الاختياري"),
            KeyTerm("Renal Threshold", "Maximum plasma concentration of a substance (e.g. glucose) above which it spills into urine.", "العتبة الكلوية"),
            KeyTerm("Hemodialysis", "Purification of blood through a semipermeable membrane based on concentration gradient.", "الغسيل الكلوي")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Daily Renal Blood Flow Ratio",
                formulaLatex = "\\text{Renal Blood Flow} = 1.2 \\text{ to } 1.3 \\text{ L/min} \\approx \\frac{1}{4} \\text{ of total Cardiac Output}",
                description = "كمية الدم التي تمر بالكلية يومياً لترشيحها وتنقية البلازما.",
                variables = "Cardiac Output: 5 L/min; Renal share: ~25%",
                sampleCalculation = "يمر بالكلية يومياً حوالي 1600 لتر دم، ويرشح منها النفرون حوالي 180 لتر سائل يومياً يُعاد امتصاص 99% منها ويخرج 1.5 لتر بول فقط."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "مقارنة بين محتويات بلازما الدم، رشيح بومان، والبول الطبيعي",
                columnA = "المادة الكيميائية",
                columnB = "الوجود في البلازما vs رشيح بومان vs البول",
                rows = listOf(
                    ComparisonRow("بروتينات البلازما الكبيرة", "موجودة في بلازما الدم", "تغيب تماماً عن رشيح بومان والبول الطبيعي لكبر حجمها"),
                    ComparisonRow("خلايا الدم الحمراء والبيضاء", "موجودة في الدم", "تغيب عن رشيح بومان والبول في الشخص السليم"),
                    ComparisonRow("سكر الجلوكوز", "100 مجم% في البلازما ورشيح بومان", "صفر في البول الطبيعي (امتصاص كامل 100% بالنقل النشط)"),
                    ComparisonRow("اليوريا (البولينا)", "تركيز منخفض في بلازما الدم ورشيح بومان", "تركيز مرتفع جداً في البول كفضلات نيتروجينية مطرودة")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "يذكر الأعضاء الإخراجية في الإنسان والفضلات التي تطردها.", "ما هي الفضلات النيتروجينية ومن أين تنشأ؟"),
            BloomLevelItem("Understanding", 2, "يفسر خلو بول الشخص السليم من الجلوكوز والبروتين.", "علل: لا يحتوي بول الإنسان السليم على الألبومين؟"),
            BloomLevelItem("Applying", 3, "يستنتج التغير في حجم البول عند زيادة إفراز هرمون ADH.", "ماذا يحدث لشخص ضل في الصحراء بدون ماء ليومين؟")
        ),
        asciiSchematic = """
+-----------------------------------------------------------+
|          NEPHRON ANATOMY & SELECTIVE REABSORPTION         |
|                                                           |
|    Renal Artery -> Afferent Arteriole                     |
|                         |                                 |
|                         v                                 |
|                 +---------------+                         |
|                 | Glomerulus    |  (High Blood Pressure)  |
|                 +-------+-------+                         |
|                         |                                 |
|            [Bowman's Capsule Filtration]                  |
|       (Water, Glucose, Urea, Salts pass)                  |
|       (Proteins & Blood Cells BLOCKED)                    |
|                         |                                 |
|                         v                                 |
|            [Proximal Convoluted Tubule]                   |
|       (100% Glucose reabsorbed actively)                  |
|                         |                                 |
|                         v                                 |
|            [Loop of Henle in Medulla]                     |
|       (Water & NaCl Osmotic Reabsorption)                 |
|                         |                                 |
|                         v                                 |
|            [Distal Tubule & Collecting Duct]              |
|       (ADH regulates final water retention)               |
|                         |                                 |
|                         v                                 |
|                     [URINE] -> Renal Pelvis -> Ureter     |
+-----------------------------------------------------------+
        """.trimIndent(),
        schematicCaption = "رسم توضيحي للنفرون ومراحل الترشيح الفائق في محفظة بومان وإعادة الامتصاص في الأنابيب وثنية هنل.",
        flowcharts = listOf(
            FlowchartStep(1, "الترشيح الفائق في محفظة بومان", "مرور الجزء السائل من البلازما عدا البروتينات وخلايا الدم.", "Glomerular Pressure"),
            FlowchartStep(2, "إعادة الامتصاص الاختياري", "استعادة الجلوكوز والأيونات والماء بالنقل النشط إلى الشعيرات الدموية.", "Active Transport ATP"),
            FlowchartStep(3, "تجميع البول وطرد الفضلات", "انتقال سائل البول المركز المحتوي على اليوريا إلى القناة الجامعة ثم الحالب.", "Collecting Duct")
        ),
        visualInspectionMarkers = listOf(
            "محفظة بومان والأنابيب الملتفة القريبة والبعيدة تقع في منطقة القشرة (Cortex).",
            "ثنية هنل والقنوات الجامعة تمتد في منطقة النخاع (Medulla)."
        ),
        youtube3DQueries = listOf(
            VideoQuery("Nephron physiology filtration reabsorption 3D", "فسيولوجيا النفرون والترشيح وإعادة الامتصاص ثلاثية الأبعاد", "4-6 min")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "mcq_excr_1",
                type = QuestionType.MCQ,
                question = "أي المواد التالية يكون تركيزها متماثلاً تماماً في بلازما الشريان الكلوي ورشيح محفظة بومان في شخص سليم؟",
                options = listOf("بروتين الألبومين", "خلايا الدم الحمراء", "سكر الجلوكوز", "الأحماض النووية"),
                correctOptionIndex = 2,
                officialAnswer = "سكر الجلوكوز",
                rationale = "الجلوكوز جزيء صغير الحجم ينفذ بحرية تامة أثناء عملية الترشيح غير الاختياري عبر مسام محفظة بومان، فيكون تركيزه في الرشيح مساوياً لتركيزه في بلازما الدم، قبل أن يُعاد امتصاصه في الأنبوبة القريبة.",
                ministryExamTip = "البروتينات وخلايا الدم لا تمر في الرشيح، بينما اليوريا والأملاح والجلوكوز تمر بنفس تركيز البلازما."
            ),
            PracticeQuestion(
                id = "mcq_excr_2",
                type = QuestionType.MCQ,
                question = "لماذا لا يعتبر خروج غاز النيتروجين في هواء الزفير أو خروج البراز عملية إخراج بالمعنى العلمي والبيولوجي؟",
                options = listOf(
                    "لأنها مواد غير سامة إطلاقاً للجسم",
                    "لأنها لا تعبر الأغشية البلازمية لخلايا الجسم",
                    "لأنها ناتجة عن هدم الكربوهيدرات",
                    "لأنها لا تخرج عبر الجهاز البولي"
                ),
                correctOptionIndex = 1,
                officialAnswer = "لأنها لا تعبر الأغشية البلازمية لخلايا الجسم",
                rationale = "يشترط في مادة الإخراج البيولوجي أن تنفذ وتعبر الأغشية البلازمية الخلوية؛ النيتروجين يدخل ويخرج من الرئة دون نفاذ، والبراز بقايا طعام لم تمتص تمر في تجويف القناة الهضمية دون عبور الأغشية.",
                ministryExamTip = "هذا سؤال تقليدي متكرر في امتحانات الأحياء؛ الشرط الجوهري للإخراج هو عبور الأغشية الخلوية."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "يعتقد الطالب أن ظهور الجلوكوز في البول يعني دائماً وجود تلف أو فشل في محفظة بومان.",
                scientificTruth = "ظهور الجلوكوز في البول سببه عجز خلايا الأنابيب الملتفة عن إعادة امتصاص الكمية الفائضة لتخطيها العتبة الكلوية (كما في السكري)، أو خلل في النقل النشط بالأنابيب وليس محفظة بومان.",
                examTrapWarning = "تلف محفظة بومان يؤدي لظهور بروتين الألبومين أو خلايا الدم الحمراء في البول وليس الجلوكوز.",
                goldenRule = "وجود البروتين في البول = خلل في محفظة بومان (الترشيح). وجود الجلوكوز = خلل في الأنابيب الملتفة أو هرمون الأنسولين."
            )
        ),
        flashcards = listOf(
            Flashcard(
                id = "fc_excr_1",
                topicId = "excretion_homeostasis",
                frontPrompt = "ما العضو المسؤول عن نزع مجموعة الأمين (-NH2) من الأحماض الأمينية الزائدة وتكوين اليوريا؟",
                backAnswer = "الكبد (Liver)، ثم تنتقل اليوريا عبر الدم إلى الكليتين لتُطرد في البول.",
                category = "Excretion"
            ),
            Flashcard(
                id = "fc_excr_2",
                topicId = "excretion_homeostasis",
                frontPrompt = "ما الظاهرة الفيزيائية التي يعتمد عليها جهاز الكلى الصناعي (الغسيل الكلوي) في تنقية الدم؟",
                backAnswer = "ظاهرة الانتشار (Diffusion) للمواد الإخراجية عبر غشاء شبه منفذ وفقاً لاختلاف التركيز.",
                category = "Excretion"
            )
        ),
        examSimulationItem = MinistryExamItem(
            id = "exam_excr_essay",
            questionType = QuestionType.GIVE_REASON,
            prompt = "علل: ظهور بروتين الألبومين أو كريات الدم الحمراء في تحليل البول يُعد دليلاً على خلل وظيفي في محفظة بومان وليس الأنابيب الكلوية.",
            maxScore = 2.0,
            requiredKeywords = listOf("محفظة بومان", "الترشيح الفائق", "كبر حجم الجزيئات", "مسام الغشاء"),
            partialCreditKeywords = listOf("عدم النفاذ", "شعيرات الجمع"),
            modelAnswer = "لأن محفظة بومان في الحالة الطبيعية تعمل كحاجز ترشيح فائق يمنع مرور الجزيئات البيولوجية الكبيرة وخلايا الدم لكبر حجمها الجزيئي؛ وظهور الألبومين أو كرات الدم الحمراء في البول يعني وجود تلف أو تمزق في مسام غشاء محفظة بومان سمح بنفاذ هذه الجزيئات الضخمة إلى سائل الرشيح، وهو ما لا علاقة له بآلية إعادة الامتصاص في الأنابيب.",
            remediationNotes = "يجب التمييز الدقيق بين الترشيح الحاجز في بومان وإعادة الامتصاص في الأنابيب."
        ),
        arabicEli5 = "الكلية عاملة زي مصفاة فائقة الذكاء وموظف جمارك دقيق جداً! كل دقيقة، الدم بيدخل محفظة بومان بالملايين، ويتصفى منه كل حاجة صغيرة (مية، جلوكوز، أملاح، بولينا) ما عدا خلايا الدم وبروتينات البلازما الكبيرة عشان متتسربش في البول. بعد كده في الأنابيب الملتفة، موظف الجمارك (إعادة الامتصاص الاختياري) يرجع كل نقطة جلوكوز وكل المية اللي الجسم محتاجها للدم بالنقل النشط، ويرمي البولينا والسموم برة في البول!",
        textbookPageReference = "كتاب الأحياء - الإخراج والكلى (الوظائف الحيوية)"
    )
}
