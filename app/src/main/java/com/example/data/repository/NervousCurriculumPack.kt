package com.example.data.repository

import com.example.data.model.*

/**
 * Curriculum Pack: التنسيق العصبي والسيال العصبي (Nervous Coordination & Action Potential)
 */
object NervousCurriculumPack {

    val chapter = Chapter(
        id = "unit1_chap_nervous",
        unit = UnitCategory.UNIT_1,
        number = 6,
        title = "Nervous Coordination & Impulses",
        arabicTitle = "التنسيق العصبي والسيال العصبي",
        summary = "Neuron structure, resting membrane potential (-70mV), sodium-potassium ATPase pump, generation of action potential (+30mV), and chemical synapse transmission.",
        iconName = "psychology"
    )

    val subTopic = SubTopic(
        id = "nervous_coordination",
        chapterId = "unit1_chap_nervous",
        unit = UnitCategory.UNIT_1,
        title = "Membrane Potential & Synaptic Transmission",
        arabicTitle = "جهد الراحة وجهد الفعالية ونقل السيال عبر التشابك العصبي",
        summary = "Depolarization wave, Na+ voltage-gated influx, repolarization, refractory period, acetylcholine release by Ca2+, and cholinesterase degradation."
    )

    val pack = MasterStudyPack(
        topicId = "nervous_coordination",
        topicTitle = "Membrane Potential & Synaptic Transmission",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "التنسيق العصبي والسيال العصبي",
        eli5Analogy = "الخلية العصبية عاملة زي سلك كهرباء ذكي مشحون بطاقة كامنة! في وقت الراحة (-70mV)، الغشاء برة موجب بالصوديوم وجوة سالب بالبوتاسيوم والبروتينات الكبيرة، ومضخة الصوديوم/بوتاسيوم عمالة تدفع 3 صوديوم لبرة وتدخل 2 بوتاسيوم لجوة عشان تحافظ على الشحنة. أول ما يجي تنبيه كافي، بوابات الصوديوم تفتح فجأة وتدخل شحنات موجبة كأنها تسونامي وينقلب الجهد لموجب (+30mV). ولما الإشارة توصل لآخر السلك عند التشابك، الكالسيوم يفرقع أكياس الأستيل كولين عشان تعوم في الفراغ وتصحى الخلية اللي بعدها، وفي ثواني إنزيم الكولين استريز يكسر الناقل عشان الخلية تستريح وترجع تشحن من جديد!",
        deepTechnicalBreakdown = """
            1. جهد الراحة (Resting Potential) في الخلية العصبية (-70 ميلي فولت):
               - الغشاء الخارجي موجب والشحنة الداخلية سالبة (حالة الاستقطاب Polarization).
               - الأسباب الفسيولوجية للاستقطاب:
                 * النفاذية الاختيارية: غشاء الخلية أثناء الراحة أكثر نفاذية لأيونات البوتاسيوم K⁺ للخارج بمقدار 40 مرة مقارنة بنفاذية أيونات الصوديوم Na⁺ للداخل.
                 * تراكم أيونات سالبة ضخمة (بروتينات متأينة وأيونات كلور Cl⁻) في السيتوبلازم لا تستطيع النفاذ لكبر حجمها.
                 * مضخة الصوديوم والبوتاسيوم (Na⁺/K⁺ ATPase): تضخ بنشاط 3 أيونات Na⁺ للخارج مقابل 2 أيونات K⁺ للداخل باستهلاك ATP.
            2. جهد الفعالية وإزالة الاستقطاب (Action Potential & Depolarization):
               - عند وصول مؤثر كافٍ يفوق جهد العتبة (Threshold -55mV):
                 * تفتح بوابات الصوديوم الحساسة للجهد (Voltage-gated Na⁺ channels) باندفاع هائل للداخل.
                 * تنعكس شحنة الغشاء فيصبح الداخل موجباً والخارج سالباً (+30 to +40 mV) وتسمى إزالة الاستقطاب (Depolarization).
               - العودة للاستقطاب (Repolarization):
                 * تغلق بوابات الصوديوم وتفتح بوابات البوتاسيوم K⁺ فيندفع البوتاسيوم للخارج ليعيد الشحنة الموجبة للخارج.
               - فترة الجموح أو الامتناع (Refractory Period): فترة زمنية وجيزة (0.001 - 0.003 ثانية) تستعيد فيها المضخة التوزيع الأيوني الأصلي ولا تستجيب لأي مؤثر مهما بلغت قوته.
            3. آلية انتقال السيال العصبي عبر التشابك العصبي (Synapse):
               - عند وصول السيال للأزرار الطرفية (Synaptic Knobs)، تفتح بوابات الكالسيوم Ca²⁺ وتدخل الأيونات محفزة تفجير حويصلات التشابك.
               - يتحرر الناقل الكيميائي (مثل الأسيتيل كولين Acetylcholine أو النورأدرينالين).
               - يعبر الناقل شق التشابك (Synaptic Cleft) ويرتبط بمستقبلات خاصة على غشاء الخلية بعد التشابكية، فتفتح قنوات الصوديوم متسببة في إثارتها.
               - إنزيم الكولين استريز (Cholinesterase): يحلل الأسيتيل كولين فوراً إلى كولين وحمض خليك لإبطال مفعوله وعودة الغشاء لحالة الراحة.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Resting Potential", "Membrane potential of -70mV maintained by high K+ efflux and Na+/K+ pump.", "جهد الراحة"),
            KeyTerm("Action Potential", "Transient reversal of membrane polarity (+30mV) caused by rapid Na+ influx.", "جهد الفعالية"),
            KeyTerm("Refractory Period", "Brief recovery interval (1-3 ms) where the neuron is completely unresponsive.", "فترة الجموح"),
            KeyTerm("Cholinesterase", "Enzyme hydrolyzing acetylcholine in synaptic cleft to stop continuous stimulation.", "كولين استريز")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Action Potential Amplitude",
                formulaLatex = "\\Delta V = V_{\\text{peak}} - V_{\\text{rest}} = (+30\\text{ mV}) - (-70\\text{ mV}) = 100\\text{ mV}",
                description = "فرق الجهد الكلي لجهد الفعالية الذي ينتقل كموجة عبر المحور العصبي.",
                variables = "V_rest: -70 mV; V_peak: +30 to +40 mV",
                sampleCalculation = "قانون الكل أو لا شيء (All-or-None Law): المؤثر الكافي ينتج دائماً جهد فعالية بنفس السعة (100 mV) مهما زادت شدة المؤثر."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "مقارنة بين حالة الراحة (الاستقطاب) وحالة الإثارة (إزالة الاستقطاب)",
                columnA = "حالة الراحة (الاستقطاب Polarization)",
                columnB = "حالة الإثارة (إزالة الاستقطاب Depolarization)",
                rows = listOf(
                    ComparisonRow("فرق الجهد الكهربي", "-70 ميلي فولت (-70 mV)", "+30 إلى +40 ميلي فولت (+30 mV)"),
                    ComparisonRow("الشحنة على الغشاء", "السطح الخارجي موجب والسطح الداخلي سالب", "السطح الخارجي سالب والسطح الداخلي موجب"),
                    ComparisonRow("حالة بوابات الصوديوم", "مغلقة تماماً (نفاذية ضعيفة جداً)", "مفتوحة على مصراعيها وتندفع لداخل الخلية"),
                    ComparisonRow("الآلية المهيمنة", "خروج K+ السريع عبر قنوات التسريب ومضخة Na+/K+", "دخول Na+ الجارف عبر القنوات الحساسة لفرق الجهد")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "يذكر الأسباب الثلاثة لنشوء جهد الراحة (-70mV).", "ما وظيفة مضخة الصوديوم والبوتاسيوم في المحور العصبي؟"),
            BloomLevelItem("Understanding", 2, "يفسر دور أيونات الكالسيوم في انتقال السيال عبر الشق التشابكي.", "ماذا يحدث عند غياب أيونات الكالسيوم في السائل بين الخلوي؟"),
            BloomLevelItem("Evaluating", 3, "يتوقع تأثير تثبيط إنزيم الكولين استريز بواسطة الغازات العصبية أو المبيدات.", "ما أثر استمرار ارتباط الأسيتيل كولين بالمستقبلات؟")
        ),
        asciiSchematic = """
+-----------------------------------------------------------+
|          ACTION POTENTIAL WAVE & SYNAPTIC TRANSMISSION    |
|                                                           |
|    Potential (mV)                                         |
|      +40 |             /\  <-- Peak Depolarization (Na+ in)|
|        0 |------------/--\----------------------------    |
|      -55 |---Threshold---  \                              |
|      -70 |-------\          \-- Repolarization (K+ out)   |
|                 Stimulus     \___ Hyperpolarization       |
|                                                           |
|             [CHEMICAL SYNAPSE ULTRASTRUCTURE]             |
|                                                           |
|       Terminal Knob (Axon)                                |
|    +-----------------------------+                        |
|    |  [Ca2+ Influx via Channels] |                        |
|    |            v                |                        |
|    |   (O) (O) Synaptic Vesicles |                        |
|    |    v   v  (Acetylcholine)   |                        |
|    +----+---+--------------------+                        |
|         |   |  (Neurotransmitters released)               |
|    ~~~~~v~~~v~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    |
|              SYNAPTIC CLEFT (Cholinesterase acts here)    |
|    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    |
|         |   |                                             |
|         v   v                                             |
|    +----+---+--------------------+                        |
|    |  [Receptors on Post-Synapse]|                        |
|    |  --> Na+ influx triggers new action potential!       |
|    +-----------------------------+                        |
+-----------------------------------------------------------+
        """.trimIndent(),
        schematicCaption = "منحنى جهد الفعالية ومخطط انتقال النواقل العصبية (الأسيتيل كولين) عبر الشق التشابكي.",
        flowcharts = listOf(
            FlowchartStep(1, "وصول السيال لنهاية المحور العصبي", "فتح قنوات الكالسيوم وتدفق Ca2+ لداخل الزر العصبي.", "Voltage-gated Ca2+"),
            FlowchartStep(2, "انفجار حويصلات الأسيتيل كولين", "هجرة الحويصلات والتحامها بالغشاء قبل التشابكي وتحرر الناقل.", "Exocytosis"),
            FlowchartStep(3, "تنبيه الخلية التالية وتحطيم الناقل", "ارتباط الناقل بالمستقبلات وتحليله الفوري بكولين استريز.", "Cholinesterase")
        ),
        visualInspectionMarkers = listOf(
            "أيونات الكالسيوم تلعب دور المحفز لانفجار الحويصلات في الزر العصبي وفي انقباض العضلة.",
            "إنزيم الكولين استريز يوجد في منطقة التشابك العصبي العضلي ليعيد الاستقطاب ويمنع التشنج العضلي المستمر."
        ),
        youtube3DQueries = listOf(
            VideoQuery("Action potential neuron synaptic transmission 3D", "جهد الفعالية والتشابك العصبي الكيميائي ثلاثي الأبعاد", "3-5 min")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "mcq_nerv_1",
                type = QuestionType.MCQ,
                question = "ما هو الدور الرئيسي لأيونات الكالسيوم (Ca²⁺) عند وصول السيال العصبي إلى النهايات الطرفية للمحور العصبي؟",
                options = listOf(
                    "تثبيط عمل إنزيم الكولين استريز",
                    "تحفيز انفجار حويصلات التشابك وتحرر الناقل العصبي",
                    "الدخول عبر غشاء الخلية بعد التشابكية لإنشاء استقطاب جديد",
                    "تنشيط مضخة الصوديوم والبوتاسيوم لإعادة جهد الراحة"
                ),
                correctOptionIndex = 1,
                officialAnswer = "تحفيز انفجار حويصلات التشابك وتحرر الناقل العصبي",
                rationale = "أيونات الكالسيوم تنفذ عبر القنوات الحساسة للجهد في النهايات العصبية (الأزرار)، وتعمل كإشارة بيوكيميائية تؤدي لانفجار حويصلات التشابك وخروج الأسيتيل كولين إلى الشق التشابكي.",
                ministryExamTip = "غياب الكالسيوم في التشابك العصبي يمنع انتقال السيال العصبي تماماً إلى الخلية التالية."
            ),
            PracticeQuestion(
                id = "mcq_nerv_2",
                type = QuestionType.MCQ,
                question = "ماذا يحدث عند تعطل أو تثبيط عمل إنزيم الكولين استريز في منطقة التشابك العصبي العضلي؟",
                options = listOf(
                    "تتوقف النواقل العصبية عن الخروج من الحويصلات",
                    "يستمر ارتباط الأسيتيل كولين بالمستقبلات وتظل العضلة في حالة انقباض مستمر (شد عضلي)",
                    "تعود الخلية العضلية فوراً لحالة الراحة والاستقطاب (-70mV)",
                    "تفقد غشاء الليفة العضلية نفاذيته لأيونات الصوديوم"
                ),
                correctOptionIndex = 1,
                officialAnswer = "يستمر ارتباط الأسيتيل كولين بالمستقبلات وتظل العضلة في حالة انقباض مستمر (شد عضلي)",
                rationale = "إنزيم الكولين استريز مسؤول عن تكسير الأسيتيل كولين فور أداء وظيفته؛ وبغيابه يستمر فتح قنوات الصوديوم وتظل الخلية منبهة ولا تتمكن من العودة للاستقطاب أو الانبساط، مما يؤدي للشد العضلي والتشنج.",
                ministryExamTip = "هذه الآلية تفسر عمل بعض غازات الأعصاب والمبيدات الحشرية التي تثبط إنزيم الكولين استريز."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "يعتقد الطالب أن السيال العصبي ينتقل في كلا الاتجاهين عبر التشابك العصبي.",
                scientificTruth = "السيال العصبي ينتقل في اتجاه واحد فقط (من الخلية قبل التشابكية إلى الخلية بعد التشابكية) لأن حويصلات النواقل توجد فقط في الأزرار الطرفية، والمستقبلات توجد فقط على الغشاء بعد التشابكي.",
                examTrapWarning = "إذا نبهت محوراً عصبياً في منتصفه ينتقل في الاتجاهين، لكن عند التشابك العصبي يسير في اتجاه إجباري واحد فقط.",
                goldenRule = "التشابك العصبي يعمل كصمام كهربائي ذي اتجاه وحيد (One-way Valve)."
            )
        ),
        flashcards = listOf(
            Flashcard(
                id = "fc_nerv_1",
                topicId = "nervous_coordination",
                frontPrompt = "ما قيمة فرق الجهد الكهربي على غشاء الليفة العصبية في حالة الراحة (الاستقطاب) وفي قمة الإثارة (إزالة الاستقطاب)؟",
                backAnswer = "في حالة الراحة: -70 ميلي فولت (-70mV). وفي قمة الإثارة: +30 إلى +40 ميلي فولت (+30mV).",
                category = "Nervous"
            ),
            Flashcard(
                id = "fc_nerv_2",
                topicId = "nervous_coordination",
                frontPrompt = "ما وظيفة إنزيم الكولين استريز في منطقة التشابك العصبي؟",
                backAnswer = "تحليل الأسيتيل كولين مائياً إلى كولين وحمض خليك لإبطال مفعوله وعودة الغشاء لحالة الراحة والاستقطاب.",
                category = "Nervous"
            )
        ),
        examSimulationItem = MinistryExamItem(
            id = "exam_nerv_essay",
            questionType = QuestionType.GIVE_REASON,
            prompt = "علل: استهلاك طاقة ATP ضروري للخلية العصبية أثناء فترات الراحة (الاستقطاب) وكذلك أثناء العودة للاستقطاب.",
            maxScore = 2.0,
            requiredKeywords = listOf("مضخة الصوديوم والبوتاسيوم", "النقل النشط", "تدرج التركيز", "فترة الجموح"),
            partialCreditKeywords = listOf("3 Na+ للخارج", "2 K+ للداخل"),
            modelAnswer = "لأن الحفاظ على جهد الراحة (-70mV) واستعادة التوزيع الأيوني الطبيعي بعد جهد الفعالية (خلال فترة الجموح) يتطلبان عمل مضخة الصوديوم والبوتاسيوم (Na⁺/K⁺ ATPase) التي تنقل الأيونات بالنقل النشط ضد تدرج التركيز (طرد 3 أيونات صوديوم للخارج مقابل إدخال أيوني بوتاسيوم للداخل)، وهو عمل ميكانيكي كيميائي يستهلك جزيئات ATP باستمرار.",
            remediationNotes = "يجب توضيح النقل النشط ضد التدرج في التركيز لمضخة Na+/K+ واستهلاك ATP."
        ),
        arabicEli5 = "الخلية العصبية عاملة زي سلك كهرباء ذكي مشحون بطاقة كامنة! في وقت الراحة (-70mV)، الغشاء برة موجب بالصوديوم وجوة سالب بالبوتاسيوم والبروتينات الكبيرة، ومضخة الصوديوم/بوتاسيوم عمالة تدفع 3 صوديوم لبرة وتدخل 2 بوتاسيوم لجوة عشان تحافظ على الشحنة. أول ما يجي تنبيه كافي، بوابات الصوديوم تفتح فجأة وتدخل شحنات موجبة كأنها تسونامي وينقلب الجهد لموجب (+30mV). ولما الإشارة توصل لآخر السلك عند التشابك، الكالسيوم يفرقع أكياس الأستيل كولين عشان تعوم في الفراغ وتصحى الخلية اللي بعدها، وفي ثواني إنزيم الكولين استريز يكسر الناقل عشان الخلية تستريح وترجع تشحن من جديد!",
        textbookPageReference = "كتاب الأحياء - التنسيق العصبي والسيال العصبي (الوظائف الحيوية)"
    )
}
