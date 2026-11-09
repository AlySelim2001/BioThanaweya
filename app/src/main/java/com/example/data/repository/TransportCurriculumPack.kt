package com.example.data.repository

import com.example.data.model.*

/**
 * Curriculum Pack: النقل في الكائنات الحية (Transport & Circulation)
 */
object TransportCurriculumPack {

    val chapter = Chapter(
        id = "unit1_chap_transport",
        unit = UnitCategory.UNIT_1,
        number = 3,
        title = "Transport in Living Organisms",
        arabicTitle = "النقل في الكائنات الحية",
        summary = "Transpiration-cohesion-tension theory in xylem, phloem food translocation, human heart anatomy, cardiac cycle, and double systemic/pulmonary circulation.",
        iconName = "swap_horiz"
    )

    val subTopic = SubTopic(
        id = "transport_circulation",
        chapterId = "unit1_chap_transport",
        unit = UnitCategory.UNIT_1,
        title = "Xylem/Phloem Transport & Human Cardiovascular System",
        arabicTitle = "النقل في النبات والدورة الدموية والقلب في الإنسان",
        summary = "Dixon and Joly cohesion-adhesion forces, sieve tubes and companion cells, cardiac pacemaker (SAN/AVN), and blood pressure regulation."
    )

    val pack = MasterStudyPack(
        topicId = "transport_circulation",
        topicTitle = "Xylem/Phloem Transport & Human Cardiovascular System",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "النقل في الكائنات الحية",
        eli5Analogy = "النقل في الشجرة عامل زي شرب عصير بقصبة طويلة جداً! لما الورقة تعرق وتبخر مية (الندح)، بتسحب عمود المية لفوق بقوة جبارة، وجزيئات المية ماسكة في بعض بإيديها (قوى التماسك) وماسكة في جدران الخشب (قوى التلاصق). وفي الإنسان، القلب هو موتور مياه أوتوماتيكي رباعي الغرف يضخ الدم للأكسجين في الرئة ويرجع يوزعه بقوة 120/80 لكل خلية من صمامات لا تسمح للدم بالرجوع للخلف أبداً!",
        deepTechnicalBreakdown = """
            1. صعود العصارة النيئة في أوعية الخشب (نبات):
               - نظرية التماسك والتلاصق وقوة الشد الناشئة عن النتح (ديكسون وجولي Dixon & Joly):
                 * قوى التماسك (Cohesion): بين جزيئات الماء وبعضها، فتجعل عمود الماء متصلاً دون انقطاع.
                 * قوى التلاصق (Adhesion): بين جزيئات الماء وجدران أوعية الخشب والقصيبات ذات الطبيعة الغروية المحبة للماء (المبطنة بالسليلوز واللجنين)، لمنع هبوط عمود الماء بتأثير الجاذبية.
                 * قوى الشد الناتجة عن النتح (Transpiration Pull): تبخر الماء من الثغور يقلل الرطوبة في المسافات البينية ويزيد الضغط الإسموزي لخلايا النسيج المتوسط، فيسحب الماء من خلايا الخشب في الورقة ثم الساق ثم الجذر.
            2. نقل الغذاء الجاهز في اللحاء:
               - الأنابيب الغربالية (Sieve Tubes): خالية من الأنوية وتتصل ببعضها عبر ثقوب الصفائح الغربالية بواسطة خيوط سيتوبلازمية.
               - الخلايا المرافقة (Companion Cells): تحتوي على نواة وعدد هائل من الميتوكوندريا لتوليد ATP لنقل الغذاء بالنقل النشط (الانسياب السيتوبلازمي Cytoplasmic Streaming).
            3. الدورة الدموية وجهاز القلب في الإنسان:
               - القلب: 4 حجرات (أذينان رقيقان لاستقبال الدم، وبطينان سميكان لضخ الدم - جدار البطين الأيسر أسمك من الأيمن لأنه يضخ الدم لجميع أنحاء الجسم عبر الأورطي).
               - الصمامات:
                 * الصمام ثلاثي الشرفات (Tricuspid): بين الأذين الأيمن والبطين الأيمن.
                 * الصمام ثنائي الشرفات (المترالي Bicuspid/Mitral): بين الأذين الأيسر والبطين الأيسر.
                 * الصمامات الهلالية (Semilunar): صمام أورطي ورئوي عند مخارج الشرايين.
               - منشأ ضربات القلب: العقدة الجيب أذينية (SAN - Sinoatrial Node) هي منظم ضربات القلب الطبيعي (Pacemaker)، تطلق نبضات ذاتية بمعدل 70 دقة/دقيقة.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Cohesion and Adhesion", "Intermolecular forces maintaining unbroken water columns in xylem vessels.", "قوى التماسك والتلاصق"),
            KeyTerm("Cytoplasmic Streaming", "Active movement of cytoplasm and sucrose within phloem sieve tubes driven by ATP.", "الانسياب السيتوبلازمي"),
            KeyTerm("Sinoatrial Node (SAN)", "Natural cardiac pacemaker located in the right atrial wall initiating heartbeats.", "العقدة الجيب أذينية"),
            KeyTerm("Semilunar Valves", "Pocket-like valves in the aorta and pulmonary artery preventing blood backflow.", "الصمامات الهلالية")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Blood Pressure Ratio",
                formulaLatex = "BP = \\frac{\\text{Systolic (Ventricular Contraction)}}{\\text{Diastolic (Ventricular Relaxation)}} \\approx \\frac{120}{80} \\text{ mmHg}",
                description = "قياس ضغط الدم الانقباضي والانبساطي في الشريان العضدي.",
                variables = "Systolic: 120 mmHg at left ventricular systole, Diastolic: 80 mmHg during relaxation",
                sampleCalculation = "أعلى قراءة لضغط الدم تحدث لحظة انقباض البطين الأيسر وفتح الصمام الأورطي."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "مقارنة بين الشرايين والأوردة في الإنسان",
                columnA = "الشرايين (Arteries)",
                columnB = "الأوردة (Veins)",
                rows = listOf(
                    ComparisonRow("اتجاه الدم", "من القلب إلى جميع أنحاء الجسم", "من الجسم عائداً إلى القلب"),
                    ComparisonRow("نوع الدم المحمول", "دم مؤكسج (عدا الشريان الرئوي)", "دم غير مؤكسج (عدا الأوردة الرئوية الأربعة)"),
                    ComparisonRow("سمك الجدار والمرونة", "سميك ومرن وينبض لوجود طبقة عضلية سميكة", "أقل سمكاً وغير نابض والطبقة العضلية رقيقة"),
                    ComparisonRow("وجود الصمامات", "تخلو من الصمامات (توجد فقط عند قواعد الأورطي والرئوي)", "تحتوي على صمامات تمنع ارتداد الدم خاصة في الأطراف السفلية"),
                    ComparisonRow("الضغط الداخلي", "ضغط دم مرتفع جداً", "ضغط دم منخفض يحتاج حركة العضلات الهيكلية للمساعدة")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "يذكر وظيفة الصمامات في الأوردة والصمام المترالي.", "أين يقع الصمام ثنائي الشرفات وما أهميته؟"),
            BloomLevelItem("Understanding", 2, "يشرح كيف يرتفع الماء لأكثر من 100 متر في أشجار السيكويا العملاقة.", "ما الدور المشترك بين قوى التماسك والنتح في صعود العصارة؟"),
            BloomLevelItem("Analyzing", 3, "يحلل التغير في أصوات القلب (لب - دب) وعلاقتها بالصمامات.", "متى ينشأ الصوت الغليظ والطويل للقلب؟")
        ),
        asciiSchematic = """
+-----------------------------------------------------------+
|          HUMAN HEART & DOUBLE CIRCULATORY PATHWAY         |
|                                                           |
|             [Superior / Inferior Vena Cava]               |
|                           | (Deoxygenated Blood)          |
|                           v                               |
|                  +-----------------+                      |
|                  |   Right Atrium  |                      |
|                  +--------+--------+                      |
|                           | [Tricuspid Valve]             |
|                           v                               |
|                  +-----------------+                      |
|                  |  Right Ventricle|                      |
|                  +--------+--------+                      |
|                           | (Pulmonary Artery to Lungs)   |
|                           v                               |
|                   [LUNGS: Gas Exchange]                   |
|                           | (4 Pulmonary Veins - Oxygen)  |
|                           v                               |
|                  +-----------------+                      |
|                  |   Left Atrium   |                      |
|                  +--------+--------+                      |
|                           | [Bicuspid / Mitral Valve]     |
|                           v                               |
|                  +-----------------+                      |
|                  |  Left Ventricle |  <-- Thickest Wall   |
|                  +--------+--------+                      |
|                           | (Aortic Valve to Body: 120mm) |
|                           v                               |
|                  [AORTA: Systemic Body]                   |
+-----------------------------------------------------------+
        """.trimIndent(),
        schematicCaption = "مسار الدورة الدموية المزدوجة وموقع الصمامات وتدفق الدم عبر حجرات القلب الأربعة.",
        flowcharts = listOf(
            FlowchartStep(1, "بدء النبضة في العقدة الجيب أذينية (SAN)", "إطلاق إشارة كهربائية ذاتية تنقبض على إثرها جدران الأذينين معاً.", "SAN Pacemaker"),
            FlowchartStep(2, "انتقال الإشارة للعقدة الأذينية البطينية (AVN)", "تأخير طفيف للسماح بامتلاء البطينين ثم التوزيع عبر حزمة هيس وألياف باركنجي.", "AVN & Bundle of His"),
            FlowchartStep(3, "انقباض البطينين المتزامن", "غلق الصمامات الأذينية البطينية (صوت لب الغليظ) واندفاع الدم في الشرايين الكبرى.", "Ventricular Systole")
        ),
        visualInspectionMarkers = listOf(
            "جدار البطين الأيسر أسمك بثلاثة أضعاف من جدار البطين الأيمن لمقاومة الضغط الجهازي العالي.",
            "الصمام ثلاثي الشرفات في الجانب الأيمن، والصمام ثنائي الشرفات (المترالي) في الجانب الأيسر."
        ),
        youtube3DQueries = listOf(
            VideoQuery("Human cardiac cycle 3D animation valves", "الدورة القلبية وعمل الصمامات ثلاثية الأبعاد", "3-5 min")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "mcq_trans_1",
                type = QuestionType.MCQ,
                question = "ما هي القوة الأساسية المسؤولة عن رفع عمود الماء المتصل إلى قمم الأشجار الشاهقة وفقاً لنظرية ديكسون وجولي؟",
                options = listOf(
                    "الضغط الجذري والإدماع",
                    "قوى الشد الناشئة عن عملية النتح",
                    "الخاصية الشعرية للقصيبات الضيقة",
                    "التنفس الخلوي في خلايا الجذر"
                ),
                correctOptionIndex = 1,
                officialAnswer = "قوى الشد الناشئة عن عملية النتح",
                rationale = "قوة الشد الناتجة عن تبخر الماء بالنتح من ثغور الأوراق هي القوة الجاذبة الرئيسية، وتعمل بالتكامل مع قوى التماسك بين جزيئات الماء وقوى التلاصق مع الجدر الخشبية.",
                ministryExamTip = "الضغط الجذري لا يفسر صعود الماء في الأشجار العالية لأن قيمته لا تتجاوز 2 ضغط جوي ويتلاشى في عاريات البذور."
            ),
            PracticeQuestion(
                id = "mcq_trans_2",
                type = QuestionType.MCQ,
                question = "أي الصمامات التالية يُغلق مسبباً سماع صوت القلب الأول الغليظ والطويل (LUB)؟",
                options = listOf(
                    "الصمام الأورطي والصمام الرئوي",
                    "الصمام ثنائي الشرفات والصمام ثلاثي الشرفات",
                    "الصمام ثلاثي الشرفات والصمام الأورطي",
                    "الصمام المترالي والصمام الرئوي"
                ),
                correctOptionIndex = 1,
                officialAnswer = "الصمام ثنائي الشرفات والصمام ثلاثي الشرفات",
                rationale = "يحدث صوت القلب الأول (LUB) العميق والطويل عند غلق الصمامين الأذينيين البطينيين (المترالي وثلاثي الشرفات) في بداية انقباض البطينين لمنع ارتداد الدم للأذينين.",
                ministryExamTip = "الصوت الثاني الحاد والقصير (DUB) ينشأ عند غلق الصمامات الهلالية (الأورطي والرئوي) في بداية انبساط البطينين."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "يعتقد الطالب أن الشريان يحمل دائماً دماً مؤكسجاً والوريد يحمل دائماً دماً غير مؤكسج.",
                scientificTruth = "الشريان الرئوي هو الشريان الوحيد الذي يحمل دماً غير مؤكسج (إلى الرئتين)، والأوردة الرئوية الأربعة هي الأوردة الوحيدة التي تحمل دماً مؤكسجاً (إلى الأذين الأيسر).",
                examTrapWarning = "إذا سألك عن وعاء ينقل دماً عالي الأكسجين من الرئة إلى القلب، فالإجابة هي الأوردة الرئوية الأربعة وليست الشرايين.",
                goldenRule = "تعريف الشريان هو 'وعاء ينقل الدم بعيداً عن القلب' بغض النظر عن نسبة الأكسجين."
            )
        ),
        flashcards = listOf(
            Flashcard(
                id = "fc_trans_1",
                topicId = "transport_circulation",
                frontPrompt = "أين تقع العقدة الجيب أذينية (SAN) المنظمة لضربات القلب؟",
                backAnswer = "في جدار الأذين الأيمن قرب موضع اتصاله بالوريدين الأجوفين العلوي والسفلي.",
                category = "Transport"
            ),
            Flashcard(
                id = "fc_trans_2",
                topicId = "transport_circulation",
                frontPrompt = "ما وظيفة الخلايا المرافقة المجاورة للأنابيب الغربالية في اللحاء؟",
                backAnswer = "إمداد الأنابيب الغربالية بطاقة ATP اللازمة للانسياب السيتوبلازمي ونقل السكروز بالنقل النشط.",
                category = "Transport"
            )
        ),
        examSimulationItem = MinistryExamItem(
            id = "exam_trans_essay",
            questionType = QuestionType.GIVE_REASON,
            prompt = "علل: جدار البطين الأيسر في القلب أكثر سمكاً وتطوراً في الكتلة العضلية من جدار البطين الأيمن.",
            maxScore = 2.0,
            requiredKeywords = listOf("البطين الأيسر", "الأورطي", "مقاومة الأوعية الدموية", "جميع أنحاء الجسم"),
            partialCreditKeywords = listOf("الضغط العالي", "الدورة الجهازية"),
            modelAnswer = "لأن البطين الأيسر يضخ الدم المؤكسج تحت ضغط مرتفع (120 مم زئبق) عبر الشريان الأورطي إلى جميع خلايا وأعضاء الجسم البعيدة، مما يتطلب قوة انقباض عضلية جبارة للتغلب على مقاومة الأوعية الدموية الجهازية، بينما يضخ البطين الأيمن الدم لمسافة قصيرة فقط إلى الرئتين المجاورتين عبر الشريان الرئوي تحت ضغط منخفض.",
            remediationNotes = "يجب المقارنة بين المسافة والمقاومة وضغط الدم في الدورة الجهازية الكبرى مقابل الدورة الرئوية الصغرى."
        ),
        arabicEli5 = "النقل في الشجرة عامل زي شرب عصير بقصبة طويلة جداً! لما الورقة تعرق وتبخر مية (الندح)، بتسحب عمود المية لفوق بقوة جبارة، وجزيئات المية ماسكة في بعض بإيديها (قوى التماسك) وماسكة في جدران الخشب (قوى التلاصق). وفي الإنسان، القلب هو موتور مياه أوتوماتيكي رباعي الغرف يضخ الدم للأكسجين في الرئة ويرجع يوزعه بقوة 120/80 لكل خلية من صمامات لا تسمح للدم بالرجوع للخلف أبداً!",
        textbookPageReference = "كتاب الأحياء - النقل والدورة الدموية (الوظائف الحيوية)"
    )
}
