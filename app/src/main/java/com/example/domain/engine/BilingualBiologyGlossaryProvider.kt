package com.example.domain.engine

import com.example.data.model.UnitCategory

data class GlossaryEntry(
    val id: String,
    val termAr: String,
    val termEn: String,
    val chapterTitleAr: String,
    val unit: UnitCategory,
    val definitionAr: String,
    val definitionEn: String,
    val ministryTrap: String,
    val relatedTopicId: String,
    val formulaOrKeyRule: String? = null,
    val highYield: Boolean = true
)

object BilingualBiologyGlossaryProvider {

    val entries: List<GlossaryEntry> = listOf(
        GlossaryEntry(
            id = "sarcomere",
            termAr = "القطعة العضلية (الساركومير)",
            termEn = "Sarcomere",
            chapterTitleAr = "الدعامة والحركة",
            unit = UnitCategory.UNIT_1,
            definitionAr = "المسافة بين كل خطين داكنين متتاليين (Z-lines) في اللييفة العضلية، وتعتبر أصغر وحدة انقباض وظيفية في العضلة الهيكلية.",
            definitionEn = "The repeating structural and functional unit between two successive Z-discs in a myofibril, consisting of actin and myosin myofilaments.",
            ministryTrap = "في حالة الانقباض العضلي التام: ينعدم طول المنطقة شبه المضيئة (H-zone)، بينما يظل طول المنطقة الداكنة (A-band) ثابتاً لا يتغير على الإطلاق لأن طول خيوط الميوسين لا يقصر.",
            relatedTopicId = "sarcomere_contraction",
            formulaOrKeyRule = "عدد القطع العضلية = عدد خطوط Z - 1 = عدد المناطق الداكنة A"
        ),
        GlossaryEntry(
            id = "acetylcholine",
            termAr = "أسيتيل كولين",
            termEn = "Acetylcholine (ACh)",
            chapterTitleAr = "الدعامة والحركة",
            unit = UnitCategory.UNIT_1,
            definitionAr = "الناقل العصبي الكيميائي المخزن داخل الحويصلات في التفرعات النهائية للخلية العصبية الحركية، المسؤول عن إزالة استقطاب غشاء الليفة العضلية.",
            definitionEn = "The neurotransmitter released at the neuromuscular junction that binds to nicotinic receptors on the motor end plate, triggering muscle depolarization.",
            ministryTrap = "لا يحدث انقباض بدون أيونات الكالسيوم (Ca²⁺) المسؤولة عن تحرير الأستيل كولين وتكوين الروابط المستعرضة، وبدون إنزيم كولين استريز (Cholinesterase) تظل العضلة في حالة شد عضلي مستمر.",
            relatedTopicId = "sarcomere_contraction",
            formulaOrKeyRule = "تفاعل التحطيم: أسيتيل كولين + كولين استريز -> حمض الخليك + كولين"
        ),
        GlossaryEntry(
            id = "parthenogenesis",
            termAr = "التوالد البكري",
            termEn = "Parthenogenesis",
            chapterTitleAr = "التكاثر في الكائنات الحية",
            unit = UnitCategory.UNIT_1,
            definitionAr = "نوع خاص من التكاثر اللاجنسي يتم فيه إنتاج أفراد جديدة من بويضة غير مخصبة بدون مشاركة مشيج مذكر.",
            definitionEn = "A specialized form of asexual reproduction where an embryo develops from an unfertilized egg cell without male gamete fusion.",
            ministryTrap = "في نحل العسل: ينتج التوالد البكري ذكوراً فقط (1n) من بويضات ناتجة بالانقسام الميوزي. في حشرة المن: ينتج إناثاً فقط (2n) من بويضات ناتجة بالانقسام الميتوزي في الربيع والصيف.",
            relatedTopicId = "human_reproduction_menstrual",
            formulaOrKeyRule = "ذكور نحل العسل: تنتج حيواناتها المنوية بالانقسام الميتوزي (لأنها 1n أصلاً)."
        ),
        GlossaryEntry(
            id = "double_fertilization",
            termAr = "الإخصاب المزدوج",
            termEn = "Double Fertilization",
            chapterTitleAr = "التكاثر في النباتات الزهرية",
            unit = UnitCategory.UNIT_1,
            definitionAr = "عملية إخصاب مميزة للنباتات الزهرية تتضمن اندماج إحدى النواتين الزكريتين مع نواة البيضة (لتكوين الزيجوت 2n)، واندماج النواة الزكرية الأخرى مع النواتين القطبيتين (اندماج ثلاثي لتكوين نواة الإندوسبيرم 3n).",
            definitionEn = "Characteristic of angiosperms: one sperm nucleus fuses with the egg nucleus (2n zygote), while the second fuses with two polar nuclei forming the triploid endosperm (3n).",
            ministryTrap = "عدد الأنوية المشاركة في عملية الإخصاب المزدوج هو 5 أنوية: (نواتان ذكريتان + نواة بيضة + نواتان قطبيتان).",
            relatedTopicId = "human_reproduction_menstrual",
            formulaOrKeyRule = "المجموع الوراثي للإندوسبيرم = 3n (ثلثا المادة الوراثية من الأم وثلث من الأب)"
        ),
        GlossaryEntry(
            id = "graafian_follicle",
            termAr = "حويصلة جراف والجسم الأصفر",
            termEn = "Graafian Follicle & Corpus Luteum",
            chapterTitleAr = "التكاثر في الإنسان",
            unit = UnitCategory.UNIT_1,
            definitionAr = "حويصلة في مبيض الأنثى تنمو تحت تأثير هرمون FSH وتفرز الإستروجين؛ بعد التبويض (بفعل LH) تتحول بقاياها إلى الجسم الأصفر المفرز للبروجسترون.",
            definitionEn = "The mature ovarian follicle that secretes estrogen and releases the secondary oocyte during ovulation, transforming into the progesterone-producing corpus luteum.",
            ministryTrap = "الخلية التي تخرج أثناء عملية التبويض ليست بويضة ناضجة، بل هي (خلية بيضية ثانوية) موقوفة عند الطور الاستوائي للانقسام الميوزي الثاني حتى لحظة الإخصاب (الانقسام المؤجل المشروط).",
            relatedTopicId = "human_reproduction_menstrual",
            formulaOrKeyRule = "قمة هرمون LH تكون قبل التبويض بحوالي 24-36 ساعة (اليوم 13-14 من بدء الطمث)"
        ),
        GlossaryEntry(
            id = "humoral_immunity",
            termAr = "المناعة الخلطية",
            termEn = "Humoral (Antibody-Mediated) Immunity",
            chapterTitleAr = "المناعة في الكائنات الحية",
            unit = UnitCategory.UNIT_1,
            definitionAr = "الاستجابة المناعية التي تتوسطها الأجسام المضادة المفرزة من الخلايا البلازمية البائية لمهاجمة الميكروبات والسموم في سوائل الجسم (الدم والليمف).",
            definitionEn = "Immune response mediated by antibodies secreted by plasma B cells to eliminate extracellular pathogens and toxins in body fluids (blood and lymph).",
            ministryTrap = "الأجسام المضادة غير قادرة على دخول الخلايا الحية المصابة بالفيروسات لكبر حجمها الجزيئي، لذلك تتولى الخلايا التائية السامة (Tc) والمناعة الخلوية القضاء على الخلايا المصابة.",
            relatedTopicId = "excretion_homeostasis",
            formulaOrKeyRule = "منطقة الارتباط بالأنتيجين (Fab) متغيرة، بينما المنطقة الثابتة (Fc) موحدة في نفس الصنف"
        ),
        GlossaryEntry(
            id = "cytotoxic_t_cells",
            termAr = "الخلايا التائية السامة (Tc)",
            termEn = "Cytotoxic T Lymphocytes (CD8+)",
            chapterTitleAr = "المناعة في الكائنات الحية",
            unit = UnitCategory.UNIT_1,
            definitionAr = "خلايا ليمفاوية ناضجة في الغدة التيموسية، تحمل مستقبلات CD8، تفرز بروتين البيرفورين (صانع الثقوب) والسموم الليمفاوية لتدمير الخلايا السرطانية والأعضاء المزروعة والمصابة بالفيروس.",
            definitionEn = "Effector T cells expressing CD8 coreceptors that destroy virally infected cells and tumors through perforin pore-formation and granzyme/lymphotoxin apoptosis.",
            ministryTrap = "الخلايا التائية المساعدة (Th) تحمل CD4، بينما الخلايا التائية السامة (Tc) والتائية المثبطة (Ts) تحملان كلاهما مستقبل CD8.",
            relatedTopicId = "excretion_homeostasis"
        ),
        GlossaryEntry(
            id = "dna_polymerase",
            termAr = "إنزيم بلمرة DNA",
            termEn = "DNA Polymerase",
            chapterTitleAr = "الحمض النووي DNA وتضاعفه",
            unit = UnitCategory.UNIT_2,
            definitionAr = "الإنزيم المسؤول عن بناء شريط DNA الجديد عن طريق إضافة النيوكليوتيدات واحدة تلو الأخرى في اتجاه واحد فقط: من '5 إلى '3.",
            definitionEn = "The enzyme that synthesizes new DNA strands by adding deoxynucleotides complementary to the template exclusively in the 5' to 3' direction.",
            ministryTrap = "لأن إنزيم البلمرة يعمل فقط في اتجاه 5' -> 3'، فإنه يبني الشريط القائد بشكل متصل، بينما يبني الشريط المتخلف في صورة قطع صغيرة (أوكازاكي) تحتاج إنزيم الربط (Ligase) لدمجها.",
            relatedTopicId = "dna_structure_replication",
            formulaOrKeyRule = "اتجاه حركة إنزيم البلمرة على القالب هو دائماً 3' -> 5'"
        ),
        GlossaryEntry(
            id = "dna_ligase",
            termAr = "إنزيم الربط",
            termEn = "DNA Ligase",
            chapterTitleAr = "الحمض النووي DNA وتضاعفه",
            unit = UnitCategory.UNIT_2,
            definitionAr = "إنزيم يقوم بتكوين روابط تساهمية بين قطع DNA غير المتصلة (قطع أوكازاكي)، وله دور جوهري في إصلاح عيوب الـ DNA وفي تقنية الهندسة الوراثية وربط البلازميدات.",
            definitionEn = "The molecular glue enzyme that catalyzes phosphodiester bond formation between adjacent 3'-OH and 5'-phosphate ends during replication and DNA repair.",
            ministryTrap = "يصلح إنزيم الربط آلاف التلفيات اليومية في DNA بشرط ألا يكون التلف في نيوكليوتيدتين متقابلتين في نفس الموقع ونفس الوقت.",
            relatedTopicId = "dna_structure_replication"
        ),
        GlossaryEntry(
            id = "chargaff_rules",
            termAr = "قواعد شارجاف للنيوكليوتيدات",
            termEn = "Chargaff's Base-Pairing Rules",
            chapterTitleAr = "الحمض النووي DNA وتضاعفه",
            unit = UnitCategory.UNIT_2,
            definitionAr = "قانون رياضي يقرر أنه في جزيء DNA مزدوج الشريط، تتساوى كمية الأدنين مع الثايمين (A = T)، وكمية الجوانين مع السيتوزين (G = C).",
            definitionEn = "In any double-stranded DNA molecule, the ratio of A to T is 1:1 and G to C is 1:1; consequently Purines equal Pyrimidines (A+G = T+C).",
            ministryTrap = "قواعد شارجاف تطبق حصرياً على اللولب المزدوج (dsDNA). إذا كانت نسبة A لا تساوي T، فهذا دليل قاطع على أن العينة عبارة عن شريط مفرد (ssDNA) أو RNA.",
            relatedTopicId = "dna_structure_replication",
            formulaOrKeyRule = "A = T | G = C | البيورينات (A+G) = البيريميدينات (T+C) = 50%"
        ),
        GlossaryEntry(
            id = "promoter",
            termAr = "المحفز (Promoter)",
            termEn = "Promoter",
            chapterTitleAr = "الأحماض النووية RNA وتخليق البروتين",
            unit = UnitCategory.UNIT_2,
            definitionAr = "تتابع محدد من النيوكليوتيدات على جزيء DNA يوجه إنزيم بلمرة RNA إلى الشريط الذي سيتم نسخه لبدء عملية النسخ.",
            definitionEn = "A specific regulatory nucleotide sequence on DNA that signals RNA polymerase where to bind and initiate transcription in the correct orientation.",
            ministryTrap = "المحفز لا يترجم إلى بروتين، وتتابعه لا يظهر في شريط mRNA المنسوخ؛ بل وظيفته توجيهية وإرشادية لإنزيم بلمرة RNA فقط.",
            relatedTopicId = "rna_transcription_translation"
        ),
        GlossaryEntry(
            id = "stop_codons",
            termAr = "كودونات الوقف (UAA, UAG, UGA)",
            termEn = "Stop (Nonsense) Codons",
            chapterTitleAr = "الأحماض النووية RNA وتخليق البروتين",
            unit = UnitCategory.UNIT_2,
            definitionAr = "ثلاثة كودونات على mRNA لا تشفر أي حمض أميني، وعند وصول الريبوسوم لأحدها يرتبط به (عامل الإطلاق) لتتفكك وحدتا الريبوسوم وتنتهي الترجمة.",
            definitionEn = "The three codons (UAA, UAG, UGA) that do not encode any amino acid; recognized by Release Factor to terminate polypeptide synthesis.",
            ministryTrap = "لا يوجد أي جزيء tRNA يحمل مضاد كودون لكودون الوقف! الذي يرتبط به هو بروتين يسمى (عامل الإطلاق - Release Factor).",
            relatedTopicId = "rna_transcription_translation",
            formulaOrKeyRule = "عدد الأحماض الأمينية في سلسلة عديد الببتيد = عدد كودونات mRNA - 1 (كودون الوقف)"
        ),
        GlossaryEntry(
            id = "restriction_enzymes",
            termAr = "إنزيمات القصر البكتيرية",
            termEn = "Restriction Endonucleases",
            chapterTitleAr = "الطفرات والهندسة الوراثية",
            unit = UnitCategory.UNIT_2,
            definitionAr = "إنزيمات دفاعية توجد في البكتيريا تتعرف على تتابعات نيوكليوتيدية محددة (موقع التعرف 4-7 نيوكليوتيدات) وتقطع DNA عندها، تاركة أطرافاً لاصقة مائلة.",
            definitionEn = "Bacterial defensive enzymes that recognize specific palindromic sequences (4-7 bp) and cleave DNA, generating cohesive sticky ends.",
            ministryTrap = "موقع التعرف هو تتابع متماثل القراءة من اتجاه 5' -> 3' على كلا الشريطين. تحمي البكتيريا نفسها بإضافة مجموعات ميثيل (-CH3) إلى مواقع التعرف بواسطة إنزيمات معدلة.",
            relatedTopicId = "genetic_engineering_recombinant",
            formulaOrKeyRule = "تنتج نهايات لاصقة أحادية الشريط (Single-stranded sticky ends) تتكامل مع قطع أخرى"
        ),
        GlossaryEntry(
            id = "reverse_transcriptase",
            termAr = "إنزيم النسخ العكسي",
            termEn = "Reverse Transcriptase",
            chapterTitleAr = "الطفرات والهندسة الوراثية",
            unit = UnitCategory.UNIT_2,
            definitionAr = "إنزيم تستخدمه الفيروسات ذات المحتوى الجيني RNA لتحويل مادتها الوراثية إلى شريط DNA مفرد مكمل، ويستخدم مخبرياً لعزل الجينات من خلايا الجسم النشطة.",
            definitionEn = "An enzyme used by retroviruses and in biotechnology to synthesize complementary DNA (cDNA) using an mRNA template.",
            ministryTrap = "الجين المستنسخ باستخدام النسخ العكسي من mRNA لا يحتوي على إنترونات (Introns) ولذلك يمكن التعبير عنه بسهولة داخل البكتيريا في الهندسة الوراثية.",
            relatedTopicId = "genetic_engineering_recombinant"
        )
    )

    fun search(query: String, unitFilter: UnitCategory? = null): List<GlossaryEntry> {
        val q = query.trim().lowercase()
        return entries.filter { entry ->
            val matchesUnit = unitFilter == null || entry.unit == unitFilter
            val matchesQuery = q.isEmpty() ||
                    entry.termAr.lowercase().contains(q) ||
                    entry.termEn.lowercase().contains(q) ||
                    entry.definitionAr.lowercase().contains(q) ||
                    entry.chapterTitleAr.lowercase().contains(q) ||
                    entry.ministryTrap.lowercase().contains(q)
            matchesUnit && matchesQuery
        }
    }
}
