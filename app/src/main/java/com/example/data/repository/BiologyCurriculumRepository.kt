package com.example.data.repository

import com.example.data.model.*

object BiologyCurriculumRepository {

    val chapters: List<Chapter> = listOf(
        Chapter(
            id = "unit1_chap1",
            unit = UnitCategory.UNIT_1,
            number = 1,
            title = "Support and Movement",
            arabicTitle = "الدعامة والحركة في الكائنات الحية",
            summary = "Plant physiological/structural support, human skeleton, joints, tendons, ligaments, and muscular contraction mechanism.",
            iconName = "fitness_center"
        ),
        Chapter(
            id = "unit1_chap2",
            unit = UnitCategory.UNIT_1,
            number = 2,
            title = "Hormonal Coordination",
            arabicTitle = "التنسيق الهرموني في الكائنات الحية",
            summary = "Endocrine glands, master pituitary gland, negative feedback loops, blood sugar and calcium homeostasis.",
            iconName = "water_drop"
        ),
        Chapter(
            id = "unit1_chap3",
            unit = UnitCategory.UNIT_1,
            number = 3,
            title = "Reproduction in Living Organisms",
            arabicTitle = "التكاثر في الكائنات الحية",
            summary = "Alternation of generations (Fern & Plasmodium), double fertilization in angiosperms, human gametogenesis & menstrual cycle.",
            iconName = "all_inclusive"
        ),
        Chapter(
            id = "unit1_chap4",
            unit = UnitCategory.UNIT_1,
            number = 4,
            title = "Immunity in Living Organisms",
            arabicTitle = "المناعة في الكائنات الحية",
            summary = "Plant defense, human lymphatic system, antibodies, innate vs acquired immunity (humoral B-cells & cell-mediated T-cells).",
            iconName = "shield"
        ),
        Chapter(
            id = "unit2_chap1",
            unit = UnitCategory.UNIT_2,
            number = 1,
            title = "DNA & Genetic Information",
            arabicTitle = "الحمض النووي والمعلومات الوراثية",
            summary = "Historical proof experiments (Griffith, Hershey-Chase), DNA structure & Chargaff ratios, replication, repair & nucleosomes.",
            iconName = "biotech"
        ),
        Chapter(
            id = "unit2_chap2",
            unit = UnitCategory.UNIT_2,
            number = 2,
            title = "RNA & Protein Synthesis",
            arabicTitle = "الأحماض النووية وتخليق البروتين",
            summary = "Types of RNA, transcription, translation, genetic code math, mutations, and genetic engineering technology.",
            iconName = "science"
        )
    )

    val subTopics: List<SubTopic> = listOf(
        SubTopic(
            id = "sarcomere_contraction",
            chapterId = "unit1_chap1",
            unit = UnitCategory.UNIT_1,
            title = "Muscle Contraction & Sliding Filament Theory",
            arabicTitle = "انقباض العضلة الهيكلية ونظرية الخيوط المنزلقة",
            summary = "Huxley's sliding filament mechanism, role of Ca²⁺ and ATP, motor units, and sarcomere calculations."
        ),
        SubTopic(
            id = "plant_human_support",
            chapterId = "unit1_chap1",
            unit = UnitCategory.UNIT_1,
            title = "Plant Support & Human Skeletal System",
            arabicTitle = "الدعامة في النبات والجهاز الهيكلي في الإنسان",
            summary = "Physiological vs structural support in plants, axial vs appendicular skeleton, joints, tendons vs ligaments."
        ),
        SubTopic(
            id = "hormonal_coordination_endocrine",
            chapterId = "unit1_chap2",
            unit = UnitCategory.UNIT_1,
            title = "Pituitary Master Gland & Hormone Feedback",
            arabicTitle = "الغدة النخامية والتغذية الراجعة الهرمونية",
            summary = "Anterior vs posterior pituitary lobes, FSH/LH, TSH, ACTH, ADH/vasopressin, and calcium/glucose regulation."
        ),
        SubTopic(
            id = "reproduction_double_fertilization",
            chapterId = "unit1_chap3",
            unit = UnitCategory.UNIT_1,
            title = "Plant Reproduction & Alternation of Generations",
            arabicTitle = "تعاقب الأجيال والإخصاب المزدوج في النباتات الزهرية",
            summary = "Life cycle of Polypodium & Plasmodium, micro/megasporogenesis, double fertilization and 3n endosperm formation."
        ),
        SubTopic(
            id = "human_reproduction_menstrual",
            chapterId = "unit1_chap3",
            unit = UnitCategory.UNIT_1,
            title = "Human Reproduction & The Menstrual Cycle",
            arabicTitle = "التكاثر في الإنسان ودورة الطمث",
            summary = "Spermatogenesis vs oogenesis, 28-day ovarian cycle, estrogen & progesterone curves, fertilization, and twins."
        ),
        SubTopic(
            id = "immunity_humoral_cellular",
            chapterId = "unit1_chap4",
            unit = UnitCategory.UNIT_1,
            title = "Humoral vs Cell-Mediated Immunity",
            arabicTitle = "المناعة الخلطية بالخلايا البائية والمناعة الخلوية بالخلايا التائية",
            summary = "Antibody anatomy & mechanisms, B-cells & plasma cells, helper TH (CD4+), cytotoxic TC (CD8+), perforin & lymphokines."
        ),
        SubTopic(
            id = "dna_structure_replication",
            chapterId = "unit2_chap1",
            unit = UnitCategory.UNIT_2,
            title = "DNA Structure, Chargaff Ratios & Replication",
            arabicTitle = "تركيب DNA وقواعد شارجاف وتضاعف المادة الوراثية",
            summary = "Avery & Hershey-Chase proofs, double helix 2nm dimensions, helicase, DNA polymerase 5'->3', ligase, Okazaki fragments."
        ),
        SubTopic(
            id = "rna_transcription_translation",
            chapterId = "unit2_chap2",
            unit = UnitCategory.UNIT_2,
            title = "RNA Transcription, Translation & Genetic Code",
            arabicTitle = "تخليق RNA وترجمة البروتين والشفرة الوراثية",
            summary = "mRNA processing, tRNA cloverleaf & anticodon, ribosomal subunits, peptidyl transferase, codon calculations, and mutations."
        ),
        SubTopic(
            id = "genetic_engineering_recombinant",
            chapterId = "unit2_chap2",
            unit = UnitCategory.UNIT_2,
            title = "Genetic Engineering & Recombinant DNA",
            arabicTitle = "الهندسة الوراثية و DNA معاد الاتحاد",
            summary = "Restriction endonucleases, sticky ends, DNA ligase, reverse transcriptase, cloning vectors/plasmids, PCR & Taq polymerase."
        )
    )

    fun getMasterStudyPack(topicId: String): MasterStudyPack {
        val rawPack = when (topicId) {
            "sarcomere_contraction" -> packSarcomereContraction
            "dna_structure_replication" -> packDnaStructureReplication
            "immunity_humoral_cellular" -> packImmunityHumoralCellular
            "rna_transcription_translation" -> packRnaTranscriptionTranslation
            "human_reproduction_menstrual" -> packHumanReproductionMenstrual
            "plant_human_support" -> packPlantHumanSupport
            "hormonal_coordination_endocrine" -> packHormonalCoordination
            "reproduction_double_fertilization" -> packReproductionDoubleFertilization
            "genetic_engineering_recombinant" -> packGeneticEngineering
            else -> packSarcomereContraction
        }
        return rawPack.copy(
            arabicEli5 = getArabicEli5ForTopic(rawPack.topicId),
            textbookPageReference = getTextbookPageForTopic(rawPack.topicId)
        )
    }

    fun getArabicEli5ForTopic(topicId: String): String {
        return when (topicId) {
            "sarcomere_contraction" ->
                "تخيل إنك في مركبين تجديف كبار (الميوسين) بينهم حبال رفيعة (الأكتين). أول ما يضرب جرس الإنذار (انطلاق أيونات الكالسيوم Ca²⁺)، تطلع مجاديف وخطاطيف من مركب الميوسين تمسك في حبال الأكتين وتسحبها ناحية المركز بطاقة البطارية (ATP). لا المراكب قصرت ولا الحبال قصرت، هما اتزحلقوا فوق بعض فقصر طول الرصيف بالكامل (القطعة العضلية Sarcomere)! ولما يخلص الـ ATP، المجاديف تفضل مكلبشة في الحبال وما تفكش وده هو الشد العضلي المؤلم."
            "plant_human_support" ->
                "الدعامة في النبات عاملة زي بلونة منفوخة بالمية (دعامة فسيولوجية مؤقتة)، لو العطش زاد البلونة تكش وتدبل، لكن أول ما تسقيها تنتفخ وترجع صلبة! وفيه دعامة تانية زي سور الأسمنت (دعامة تركيبية دائمة) بترسيب مواد قوية زي السليلوز واللجنين والكيوتين. أما في الإنسان، الجهاز الهيكلي 206 عظمة بيشتغلوا كشاسيه عربية صلب، والأربطة بتمسك العظم في العظم، والأوتار بتمسك العضلات في العظم زي كابلات الشد."
            "hormonal_coordination_endocrine" ->
                "جهاز الهرمونات عامل زي شبكة بريد سريع في الجسم. الغدة النخامية هي 'المايسترو' أو المدير العام، بتبعت خطابات وهرمونات تأمر بيها باقي الغدد (زي الدرقية والكظرية). ولما السكر في الدم يعلى، البنكرياس يفرز الأنسولين كأنه بواب يفتح خلايا الجسم عشان تدخل السكر وتخزنه، ولما السكر يقل يطلع الجلوكاجون يفك الوديعة من الكبد عشان يحافظ على التوازن الدقيق."
            "reproduction_double_fertilization" ->
                "الإخصاب المزدوج في النباتات الزهرية عامل زي حفل زفاف لشخصين في نفس اللحظة! حبة اللقاح بتبعت حبتين نواة ذكريتين (n): الأولى بتتجوز البيضة (n) وتكون الجنين (2n)، والتانية بتندمج مع نواتي الكيس الجنيني (n+n) وتعمل كنز غذائي ثلاثي (3n) اسمه الإندوسبيرم عشان يأكل البيبي وهو بيكبر جوة البذرة!"
            "human_reproduction_menstrual" ->
                "دورة الطمث الشهرية (28 يوم) عاملة زي تجديد دهان وفرش شقة لاستقبال ضيف مهم (الجنين). في أول أسبوعين هرمون الإستروجين بيفرش بطانة الرحم بالدم والمغذيات، وفي اليوم 14 بتخرج البويضة (التبويض) بفضل هرمون LH. بعد كده البروجسترون يزود سمك البطانة. لو ما حصلش إخصاب، العقد يتلغي، ومستوى الهرمونات ينهار، وتتهدم بطانة الرحم وتنزل في صورة دم الطمث."
            "immunity_humoral_cellular" ->
                "جهاز المناعة جيش متكامل لحماية وطنك (الجسم)! الخلايا البائية بتصنع أسلحة صواريخ موجهة اسمها 'الأجسام المضادة' تصطاد الميكروبات السايبة في الدم (المناعة الخلطية). أما الخلايا التائية (T-cells) دي قوات الصاعقة الخاصة اللي بتقتحم الخلايا المصابة بالفيروسات وخلايا السرطان وتفجرها ببروتين البيرفورين (المناعة الخلوية)، وخلايا الذاكرة بتفضل حافظة وش العدو لعشرات السنين!"
            "dna_structure_replication" ->
                "الـ DNA عامل زي سوستة حلزونية ملفوفة بدقة متناهية. كل سنّة من السوستة على جنب ليها شريك محدد ومقفول بالمفتاح: الأزرق (A) دايماً مع الأصفر (T)، والأخضر (G) دايماً مع الأحمر (C). ولما الخلية تقرر تتكاثر، يجي إنزيم اللولب (Helicase) يفك السوستة زي سحاب الجاكيت، وإنزيم بلمرة DNA يبني نسختين متطابقتين تماماً، وإنزيم الربط (Ligase) يلحم القطع المفكوكة."
            "rna_transcription_translation" ->
                "تخيل إن الـ DNA هو كتاب الطبخ السري النادر المحفوظ في خزنة المكتبة (النواة) وممنوع يخرج منها. بنعمل منه صورة ضوئية بورقة صغيرة اسمها mRNA (عملية النسخ)، الورقة تخرج للمطبخ (الريبوسوم في السيتوبلازم)، ويجي شيف التوصيل tRNA شايل المقادير (الأحماض الأمينية)، يقرا الكودونات تلاتة تلاتة، ويطبخ أحلى وجبة بروتين يحتاجها جسمك!"
            "genetic_engineering_recombinant" ->
                "الهندسة الوراثية عامله زي برنامج مونتاج فيديو بدقة النانو! إنزيمات القصر البكتيرية بتشتغل مقصات دقيقة بتقص عند تسلسلات معينة وتسيب 'أطراف لاصقة'، نجيب جين الأنسولين البشري ونلزقه جوة حلقة بكتيرية (بلازميد) بإنزيم الربط، ونحطه جوة بكتيريا سريعة التكاثر، فنحول البكتيريا لمصنع حيوي بينتج أدوية منقذة للحياة!"
            else -> "مفهوم بيولوجي أساسي في منهج الثانوية العامة المصرية لشرح الآلية الحيوية بدقة وتشبيهات ملموسة."
        }
    }

    fun getTextbookPageForTopic(topicId: String): String {
        return when (topicId) {
            "sarcomere_contraction" -> "Ministry Textbook Unit 1, Chapter 1, pp. 14–20"
            "plant_human_support" -> "Ministry Textbook Unit 1, Chapter 1, pp. 3–14"
            "hormonal_coordination_endocrine" -> "Ministry Textbook Unit 1, Chapter 2, pp. 23–37"
            "reproduction_double_fertilization" -> "Ministry Textbook Unit 1, Chapter 3, pp. 41–55"
            "human_reproduction_menstrual" -> "Ministry Textbook Unit 1, Chapter 3, pp. 56–76"
            "immunity_humoral_cellular" -> "Ministry Textbook Unit 1, Chapter 4, pp. 83–110"
            "dna_structure_replication" -> "Ministry Textbook Unit 2, Chapter 1, pp. 113–134"
            "rna_transcription_translation" -> "Ministry Textbook Unit 2, Chapter 2, pp. 135–144"
            "genetic_engineering_recombinant" -> "Ministry Textbook Unit 2, Chapter 2, pp. 145–151"
            else -> "Ministry Textbook Review pp. 21-22 & pp. 148-151"
        }
    }

    fun getAllExamQuestions(): List<PracticeQuestion> {
        return listOf(
            packSarcomereContraction,
            packPlantHumanSupport,
            packHormonalCoordination,
            packReproductionDoubleFertilization,
            packHumanReproductionMenstrual,
            packImmunityHumoralCellular,
            packDnaStructureReplication,
            packRnaTranscriptionTranslation,
            packGeneticEngineering
        ).flatMap { it.questions }
    }

    fun getSimulatedMinistryExamPool(): List<PracticeQuestion> {
        return getAllExamQuestions().take(15)
    }

    fun getTopicMasteries(): List<TopicMastery> {
        val masteries = mutableListOf<TopicMastery>()
        val defaultStabilities = mapOf(
            "sarcomere_contraction" to (16.5 to 3.2),
            "plant_human_support" to (8.4 to 4.1),
            "hormonal_coordination_endocrine" to (12.0 to 4.8),
            "reproduction_double_fertilization" to (5.2 to 5.6),
            "human_reproduction_menstrual" to (14.2 to 3.9),
            "immunity_humoral_cellular" to (1.8 to 6.2),
            "dna_structure_replication" to (18.4 to 2.8),
            "rna_transcription_translation" to (6.8 to 5.1),
            "genetic_engineering_recombinant" to (1.5 to 7.0)
        )
        for (sub in subTopics) {
            val (stab, diff) = defaultStabilities[sub.id] ?: (5.0 to 5.0)
            val level = when {
                stab >= 15.0 -> MasteryLevel.MASTERED
                stab >= 7.0 -> MasteryLevel.GOOD
                stab >= 2.5 -> MasteryLevel.LEARNING
                else -> MasteryLevel.NEEDS_REVIEW
            }
            masteries.add(
                TopicMastery(
                    topicId = sub.id,
                    title = sub.title,
                    chapterTitle = chapters.find { it.id == sub.chapterId }?.title ?: "",
                    unit = sub.unit,
                    stability = stab,
                    difficulty = diff,
                    reps = if (stab > 10.0) 6 else if (stab > 5.0) 3 else 1,
                    masteryLevel = level
                )
            )
        }
        return masteries
    }


    // -------------------------------------------------------------
    // PACK 1: Muscle Contraction & Huxley's Sliding Filament Theory
    // -------------------------------------------------------------
    val packSarcomereContraction = MasterStudyPack(
        topicId = "sarcomere_contraction",
        topicTitle = "Muscle Contraction & Sliding Filament Theory",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "Chapter 1: Support and Movement",
        eli5Analogy = "Imagine two rows of rowing oars reaching across from thick wooden boats (Myosin). When an alarm sounds (Ca²⁺ release), the oars grab onto adjacent thin ropes (Actin) and pull them toward the center using battery power (ATP). The boats and ropes do NOT shrink in length; they simply slide past one another, making the entire dock (Sarcomere) shorter!",
        deepTechnicalBreakdown = """
### 1. Neuromuscular Junction Activation
1. **Nerve Impulse Arrival**: Action potential travels down motor neuron axon to the terminal arborization (axon terminal).
2. **Ca²⁺ Influx**: Voltage-gated Ca²⁺ channels open in presynaptic membrane; Ca²⁺ enters synaptic knob and ruptures synaptic vesicles containing **Acetylcholine (ACh)**.
3. **Depolarization**: ACh diffuses across synaptic cleft (20 nm) and binds nicotinic receptors on the **sarcolemma (motor end plate)**, opening ligand-gated Na⁺ channels. Massive Na⁺ influx causes **Depolarization** (+30 mV), generating a muscle action potential.

### 2. Excitation-Contraction Coupling
1. Action potential propagates down **Transverse Tubules (T-tubules)** deep into muscle sarcoplasm.
2. Triggers terminal cisternae of the **Sarcoplasmic Reticulum (SR)** to release stored **Ca²⁺ ions** into sarcoplasm around myofibrils.
3. Ca²⁺ binds to troponin, causing a conformational shift in tropomyosin, unshielding the active binding sites on **Actin filaments**.

### 3. Huxley's Sliding Filament Mechanism
1. **Cross-Bridge Formation**: Projections emerging from thick **Myosin filaments** (assisted by Ca²⁺) act as transverse hooks and bind to actin.
2. **Power Stroke**: Bound ATP on myosin head hydrolyzes to ADP + Pi. Myosin heads pivot, pulling actin filaments toward the center of the sarcomere (**M-line**).
3. **Detachment**: A new **ATP molecule** must bind to the myosin head to break the cross-bridge. If ATP is depleted, cross-bridges stay locked, causing **Muscle Spasm**.

### 4. Recovery & Repolarization
1. **Cholinesterase (AChE)** enzyme located on post-synaptic membrane instantly hydrolyzes acetylcholine into **Acetic acid + Choline**.
2. Sarcolemma permeability to Na⁺ ceases; membrane repolarizes to resting state (-70 mV).
3. Ca²⁺ is pumped back into the sarcoplasmic reticulum by active transport (requiring ATP).
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Sarcomere", "The distance between two successive Z-lines; structural and functional contractile unit of myofibril.", "القطعة العضلية"),
            KeyTerm("Myosin", "Thick protein filament bearing cross-bridges (transverse hooks) that interact with actin.", "الميوسين"),
            KeyTerm("Actin", "Thin protein filament anchored to the Z-line.", "الأكتين"),
            KeyTerm("Cholinesterase", "Enzyme that destroys acetylcholine into acetic acid and choline to terminate contraction.", "إنزيم كولين إستريز"),
            KeyTerm("Motor Unit", "One motor neuron and all the muscle fibers it innervates (ranging from 5 to 100 fibers).", "الوحدة الحركية")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Sarcomere to Z-Line Relationship",
                formulaLatex = "Z = n + 1",
                description = "Number of Z-lines always equals number of sarcomeres plus 1.",
                variables = "n = Number of Sarcomeres; Z = Number of Z-lines",
                sampleCalculation = "For 5 sarcomeres: Z-lines = 5 + 1 = 6 lines."
            ),
            FormulaItem(
                name = "A-Band Count",
                formulaLatex = "A = n",
                description = "Dark bands (A-bands) equal number of sarcomeres and NEVER change length during contraction.",
                variables = "A = Number of Dark bands; n = Number of Sarcomeres",
                sampleCalculation = "For 5 sarcomeres: A-bands = 5."
            ),
            FormulaItem(
                name = "Complete I-Band Count",
                formulaLatex = "I_{complete} = n - 1 = Z - 2",
                description = "Only internal light bands are complete; 2 incomplete bands exist at the outer ends.",
                variables = "n = Number of Sarcomeres; Z = Number of Z-lines",
                sampleCalculation = "For 5 sarcomeres: Complete I-bands = 5 - 1 = 4."
            ),
            FormulaItem(
                name = "H-Zone Disappearance Rule",
                formulaLatex = "H_{maximal} = 0",
                description = "During complete/strong contraction, actin filaments meet and overlap completely, obliterating the H-zone.",
                variables = "H = Semi-light zone width",
                sampleCalculation = "Relaxed: H = n; Complete contraction: H = 0."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "Sarcomere Bands Behavior During Contraction",
                columnA = "Relaxed State",
                columnB = "Contracted State",
                rows = listOf(
                    ComparisonRow("Z-lines Distance", "Normal / Far apart", "Decreases / Move closer together"),
                    ComparisonRow("I-Band (Light Band)", "Wide length", "Decreases in width"),
                    ComparisonRow("H-Zone (Semi-Light)", "Visible (Myosin only)", "Decreases, vanishes completely in maximal contraction"),
                    ComparisonRow("A-Band (Dark Band)", "Constant length", "UNALTERED (Constant, length of myosin)")
                )
            ),
            ComparisonTable(
                title = "Muscle Fatigue vs Muscle Spasm (Ministry Exam Trap)",
                columnA = "Muscle Fatigue (إجهاد)",
                columnB = "Muscle Spasm (شد عضلي)",
                rows = listOf(
                    ComparisonRow("Primary Cause", "Anaerobic respiration due to oxygen shortage; lactic acid accumulates", "Absolute depletion of ATP OR erratic motor nerve impulses"),
                    ComparisonRow("Cross-Bridges", "Detach slowly due to low ATP rate", "Remain permanently attached to actin filaments"),
                    ComparisonRow("Contraction Force", "Weak, sluggish contraction", "Severe sustained involuntary contraction; risk of muscle tearing and hemorrhage"),
                    ComparisonRow("Remedy", "Rest & deep breathing to supply O₂", "Immediate rest, anti-spasmodic, restore ATP synthesis")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "Recall the structural proteins of myofibril and role of Ca²⁺.", "What are the two protein filaments forming the sarcomere?"),
            BloomLevelItem("Understanding", 2, "Explain why the A-band length does not change while the I-band shortens.", "Why does the dark band remain constant during Huxley's sliding mechanism?"),
            BloomLevelItem("Applying", 3, "Calculate band counts given 8 consecutive Z-lines.", "Given 8 Z-lines: find Sarcomeres, A-bands, Complete I-bands, and H-zones during full contraction."),
            BloomLevelItem("Analyzing", 4, "Differentiate between fatigue caused by lactic acid vs spasm caused by ATP absence.", "Analyze why a marathon runner suffers fatigue vs a sudden spasm."),
            BloomLevelItem("Evaluating", 5, "Assess the physiological impact of a synthetic organophosphate poison inhibiting cholinesterase.", "Predict the consequence of sarin gas inhibiting AChE on breathing muscles."),
            BloomLevelItem("Creating", 6, "Formulate a therapeutic intervention to reverse muscle spasm safely.", "Design a rapid recovery protocol to uncouple locked cross-bridges without tissue damage.")
        ),
        asciiSchematic = """
================ RELAXED SARCOMERE ================
 Z-line                                        Z-line
   |       ==ACTIN==              ==ACTIN==      |
   |----->                      <----------|
   |          ========================           |
   |               ====MYOSIN====                |
   |          ========================           |
   |----->                      <----------|
   |       ==ACTIN==              ==ACTIN==      |
   |<--I-band-->|<----- H-zone ------>|<--I-band>|
   |<------------------ A-band ----------------->|

============== FULLY CONTRACTED SARCOMERE ==========
 Z-line                                  Z-line
   |==ACTIN============ACTIN==             |
   |-----><--------------------------------|
   |     ========================          |
   |          ====MYOSIN====               |
   |     ========================          |
   |-----><--------------------------------|
   |==ACTIN============ACTIN==             |
   |   (H-Zone completely vanished: 0)     |
   |<------------- A-band (Unchanged) ---->|
        """.trimIndent(),
        schematicCaption = "Huxley's Sliding Filament Model: Notice how Z-lines approximate, I-band shortens, H-zone disappears, while A-band length remains strictly unchanged.",
        flowcharts = listOf(
            FlowchartStep(1, "Nerve Impulse reaches Terminal Arborization", "Depolarizes axonal membrane", "Action Potential"),
            FlowchartStep(2, "Voltage-gated Ca²⁺ channels open", "Ca²⁺ enters synaptic knob and ruptures vesicles", "Ca²⁺ ions"),
            FlowchartStep(3, "Acetylcholine released into Synaptic Cleft", "Diffuses across cleft and binds sarcolemma receptors", "ACh neurotransmitter"),
            FlowchartStep(4, "Sarcolemma Depolarization (+30 mV)", "T-tubules carry potential to Sarcoplasmic Reticulum", "Na⁺ influx"),
            FlowchartStep(5, "SR releases Ca²⁺ into Sarcoplasm", "Ca²⁺ uncovers active sites on actin by moving tropomyosin", "Ca²⁺ from SR"),
            FlowchartStep(6, "Myosin Cross-Bridges bind Actin", "ATP hydrolyzes to ADP + Pi; power stroke pulls actin", "Myosin ATPase"),
            FlowchartStep(7, "Cholinesterase Hydrolyzes ACh", "Converts ACh to Acetic acid + Choline; sarcolemma repolarizes", "AChE Enzyme")
        ),
        visualInspectionMarkers = listOf(
            "Check Z-line distance: If narrow, muscle is contracted; if wide, muscle is relaxed.",
            "Inspect the H-zone in the center: In full contraction, H-zone is absent (width = 0).",
            "Examine Myosin transverse hooks (cross-bridges): Attached to actin = contracted state; detached = relaxed state.",
            "Verify A-band bracket: Must span entire length of thick myosin filaments."
        ),
        youtube3DQueries = listOf(
            VideoQuery("3D Animation Sliding Filament Theory Muscle Contraction", "Shows dynamic motion of myosin heads pulling actin and ATP hydrolysis in high definition.", "3-5 mins"),
            VideoQuery("Neuromuscular Junction Acetylcholine Release 3D Animation", "Depicts synaptic vesicles rupturing, acetylcholine diffusing, and cholinesterase breakdown.", "2-4 mins")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "q1",
                type = QuestionType.SCIENTIFIC_TERM,
                question = "Write the scientific term: The structural and functional contractile unit located between two successive Z-lines in a skeletal muscle fiber.",
                officialAnswer = "Sarcomere (القطعة العضلية)",
                rationale = "By definition in the Egyptian Ministry curriculum, the sarcomere is bounded by two successive Z-lines.",
                ministryExamTip = "Never confuse 'Sarcomere' with 'Myofibril' or 'Motor Unit'."
            ),
            PracticeQuestion(
                id = "q2",
                type = QuestionType.GIVE_REASON,
                question = "Give reason: The A-band (Dark band) length remains constant during skeletal muscle contraction while the I-band length shortens.",
                officialAnswer = "Because the A-band corresponds to the entire length of the thick myosin filaments, which do not change their physical length during contraction. The I-band shortens because actin filaments slide over myosin filaments toward the center of the sarcomere, reducing the uncovered light zone.",
                rationale = "Huxley's sliding theory states filaments slide past one another without shortening themselves.",
                ministryExamTip = "Must mention: 'A-band equals the length of thick myosin filaments' for full mark."
            ),
            PracticeQuestion(
                id = "q3",
                type = QuestionType.WHAT_HAPPENS_IF,
                question = "What happens if: Total absence of cholinesterase enzyme at the motor end plate of a skeletal muscle fiber?",
                officialAnswer = "Acetylcholine will not be hydrolyzed into acetic acid and choline. The sarcolemma will remain permanently depolarized, keeping the muscle in continuous state of contraction (unable to relax), which may lead to muscle spasm and tearing.",
                rationale = "Cholinesterase is indispensable for muscle relaxation and resting potential restoration.",
                ministryExamTip = "Keywords required: 'Acetylcholine persists', 'No repolarization', 'Continuous contraction/spasm'."
            ),
            PracticeQuestion(
                id = "q4",
                type = QuestionType.MCQ,
                question = "If a myofibril contains 6 Z-lines, what is the number of complete I-bands and H-zones during maximal contraction respectively?",
                options = listOf("5 and 5", "4 and 0", "5 and 0", "4 and 5"),
                correctOptionIndex = 1,
                officialAnswer = "4 and 0",
                rationale = "Sarcomeres = Z - 1 = 6 - 1 = 5. Complete I-bands = Sarcomeres - 1 = 5 - 1 = 4. During maximal contraction, H-zones vanish completely, so H = 0.",
                ministryExamTip = "Watch out for 'maximal contraction' trap! H-zone becomes 0."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "Thinking muscle filaments shrink like an accordion during contraction.",
                scientificTruth = "Actin and Myosin filaments maintain their exact length! They only SLIDE past one another, reducing the distance between Z-lines.",
                examTrapWarning = "Ministry MCQ trap: 'Which of the following shortens during contraction?' Option A: Myosin, Option B: Actin, Option C: Sarcomere. The correct answer is ONLY Sarcomere/I-band/H-zone!",
                goldenRule = "Filaments slide; Sarcomeres shorten."
            ),
            ExamTrap(
                misconception = "Confusing the role of Calcium ions in the nerve terminal vs in the muscle sarcoplasm.",
                scientificTruth = "Ca²⁺ in nerve knob ruptures synaptic vesicles to release ACh. Ca²⁺ in muscle sarcoplasm unmasks actin binding sites by shifting troponin/tropomyosin.",
                examTrapWarning = "Questions specifying 'Role of Ca²⁺ at the neuromuscular junction' require mentioning synaptic vesicle rupture!",
                goldenRule = "Synaptic knob Ca²⁺ = Neurotransmitter release; Sarcoplasmic Ca²⁺ = Cross-bridge attachment."
            )
        ),
        flashcards = listOf(
            Flashcard("fc1", "sarcomere_contraction", "What is the structural and functional contractile unit of a muscle fiber?", "The Sarcomere (distance between two successive Z-lines).", "Terminology"),
            Flashcard("fc2", "sarcomere_contraction", "What happens to the H-zone during maximal muscle contraction?", "It completely disappears (becomes zero) as actin filaments slide and overlap entirely.", "Mechanism"),
            Flashcard("fc3", "sarcomere_contraction", "What are the dual requirements for both muscle contraction AND relaxation?", "ATP and Calcium ions (ATP is required to attach AND detach cross-bridges; Ca²⁺ is pumped back by ATP).", "Physiology"),
            Flashcard("fc4", "sarcomere_contraction", "What is the mathematical formula for number of complete I-bands given Z-lines?", "Complete I-bands = Z - 2 (or Sarcomeres - 1).", "Formula")
        ),
        examSimulationItem = MinistryExamItem(
            id = "sim_sarcomere",
            questionType = QuestionType.GIVE_REASON,
            prompt = "Give Reason: The absence of ATP in a skeletal muscle fiber leads to sustained muscle spasm.",
            maxScore = 2.0,
            requiredKeywords = listOf("atp", "cross bridges", "detach", "actin", "spasm"),
            partialCreditKeywords = listOf("myosin", "relaxation", "tear"),
            modelAnswer = "Because ATP is necessary for the detachment of myosin cross-bridges (transverse hooks) from actin filaments. In the absence of ATP, cross-bridges remain locked to actin filaments, preventing muscle relaxation and causing painful, sustained muscle spasm.",
            remediationNotes = "You must explicitly explain that ATP is required to BREAK the cross-bridge bond, not just for contraction."
        )
    )

    // -------------------------------------------------------------
    // PACK 2: DNA Structure, Chargaff Ratios & Replication
    // -------------------------------------------------------------
    val packDnaStructureReplication = MasterStudyPack(
        topicId = "dna_structure_replication",
        topicTitle = "DNA Structure, Chargaff Ratios & Replication",
        unit = UnitCategory.UNIT_2,
        chapterTitle = "Chapter 1: DNA & Genetic Information",
        eli5Analogy = "Think of DNA as a twisted spiral zipper (Double Helix). Each tooth of the zipper on one side only matches a specific tooth on the other: Blue (A) always pairs with Yellow (T), Green (G) always pairs with Red (C). To copy the book of life, a molecular unzipper (Helicase) opens the strands, master typists (DNA Polymerase) build the twin copies, and glue masters (Ligase) seal any disjointed fragments!",
        deepTechnicalBreakdown = """
### 1. Evidence of DNA as the Genetic Material
1. **Griffith's Bacterial Transformation (1928)**: Heat-killed virulent S-strain Pneumococcus transformed harmless live R-strain into virulent S-strain.
2. **Avery, MacLeod & McCarty (1944)**: Purified the transforming factor and proved it is DNA. Conclusive proof achieved via **Deoxyribonuclease enzyme**, which completely destroys DNA and abolished transformation, while proteases and ribonucleases had no effect.
3. **Hershey & Chase Bacteriophage Experiment (1952)**: Labeled bacteriophage DNA with radioactive phosphorus ($^{32}\text{P}$) and protein coat with radioactive sulfur ($^{35}\text{S}$). Only $^{32}\text{P}$ entered bacterial cells (>97%), proving DNA is the genetic material.

### 2. Molecular Architecture of Watson & Crick Double Helix (1953)
- DNA consists of two antiparallel polynucleotide strands running in opposite directions (5' -> 3' and 3' -> 5').
- **Sugar-Phosphate Backbone**: Forms the outer sides of the ladder; hydrophilic deoxyribose sugars alternating with phosphate groups linked by phosphodiester bonds.
- **Nitrogenous Bases**: Projected inward perpendicular to the long axis.
  - Purines (double-ring: Adenine, Guanine) pair with Pyrimidines (single-ring: Thymine, Cytosine).
  - A = T linked by **2 Hydrogen Bonds**.
  - G ≡ C linked by **3 Hydrogen Bonds**.
- **Dimensions**: Helix diameter is constant (2 nm) because every rung consists of 1 Purine + 1 Pyrimidine. Each complete helical turn contains **10 base pairs** (20 nucleotides) and spans 3.4 nm.

### 3. Chargaff's Equivalence Rules
- In double-stranded DNA:
  %A = %T and %G = %C
  %Purines (A+G) = %Pyrimidines (T+C) = 50%
  (A+G)/(T+C) = 1

### 4. Semi-Conservative DNA Replication
1. **Helicase**: Moves along the replication fork, uncoiling the double helix and breaking hydrogen bonds between base pairs.
2. **DNA Polymerase**: Synthesizes new complementary strands strictly in the **5' -> 3' direction**.
   - **Leading Strand**: Synthesized continuously toward the replication fork.
   - **Lagging Strand**: Synthesized discontinuously away from fork in short segments called **Okazaki fragments**.
3. **DNA Ligase**: Connects and seals Okazaki fragments by forming phosphodiester bonds.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Deoxyribonuclease", "Enzyme that specifically hydrolyzes DNA completely without affecting proteins or RNA.", "إنزيم دي أوكسي ريبونيوكليز"),
            KeyTerm("Antiparallel", "Arrangement where one DNA strand runs 5' to 3' while the complementary runs 3' to 5', allowing hydrogen bond formation.", "متعاكس الاتجاه"),
            KeyTerm("Okazaki Fragments", "Short segments of newly synthesized DNA on the lagging strand joined by ligase.", "قطع أوكازاكي"),
            KeyTerm("DNA Ligase", "Enzyme that seals nicks in the sugar-phosphate backbone by creating phosphodiester bonds.", "إنزيم الربط")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Chargaff's Base Ratio",
                formulaLatex = "%A = %T, %G = %C",
                description = "In double-stranded DNA, adenine equals thymine and guanine equals cytosine.",
                variables = "A, T, G, C = Percentage of bases",
                sampleCalculation = "If %A = 20%, then %T = 20%. Remaining = 60%, so %G = 30%, %C = 30%."
            ),
            FormulaItem(
                name = "DNA Turns Formula",
                formulaLatex = "Turns = \\frac{\\text{Base Pairs}}{10} = \\frac{\\text{Total Nucleotides}}{20}",
                description = "Each complete turn of the double helix contains 10 base pairs or 20 nucleotides.",
                variables = "Turns = Number of complete coils",
                sampleCalculation = "For a piece of DNA with 200 nucleotides: Turns = 200 / 20 = 10 complete turns."
            ),
            FormulaItem(
                name = "Hydrogen Bonds Total",
                formulaLatex = "H_{bonds} = 2(A) + 3(G)",
                description = "Calculates total hydrogen bonds across the DNA segment.",
                variables = "A = Number of Adenine bases; G = Number of Guanine bases",
                sampleCalculation = "For 40 A-T pairs and 60 G-C pairs: H = (2 × 40) + (3 × 60) = 80 + 180 = 260 bonds."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "DNA Replication Enzymes Comparison",
                columnA = "DNA Polymerase",
                columnB = "DNA Ligase",
                rows = listOf(
                    ComparisonRow("Catalytic Direction", "Synthesizes exclusively 5' to 3'", "Seals nicks on lagging strand"),
                    ComparisonRow("Action on Leading Strand", "Synthesizes continuously without interruption", "Not required on leading strand"),
                    ComparisonRow("Action on Lagging Strand", "Synthesizes Okazaki fragments discontinuously", "Binds and connects Okazaki fragments into continuous strand"),
                    ComparisonRow("Role in DNA Repair", "Fills in gap with correct nucleotide complementary to template", "Seals the phosphodiester backbone permanently")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "Recall the 3 enzymes involved in DNA replication.", "Name the enzymes responsible for unwinding, synthesizing, and joining DNA."),
            BloomLevelItem("Understanding", 2, "Explain why DNA replication is termed semi-conservative.", "Why does each daughter DNA molecule contain one parental and one newly synthesized strand?"),
            BloomLevelItem("Applying", 3, "Calculate base ratios and number of turns for a 400-nucleotide gene.", "If a gene has 400 nucleotides and %Cytosine is 30%, find number of Adenine bases and turns."),
            BloomLevelItem("Analyzing", 4, "Compare the rate of mutations in DNA vs RNA viruses (e.g., Influenza vs Hepatitis B).", "Why do RNA viruses mutate at a vastly higher rate than DNA viruses?"),
            BloomLevelItem("Evaluating", 5, "Assess the effect of Deoxyribonuclease on bacterial transformation.", "Why did Griffith fail to discover the exact biochemical nature of the transforming principle?"),
            BloomLevelItem("Creating", 6, "Propose a theoretical model for DNA replication if strands were parallel instead of antiparallel.", "How would hydrogen bond angles and replication mechanisms differ if strands both ran 5' to 3'?")
        ),
        asciiSchematic = """
============== DNA REPLICATION FORK =============
 3' ---------------------------------------------> 5' (Template)
                     \   <==== Helicase (Unwinds)
                      \
                       =========================== 3' (Leading Strand: continuous)
                       <-------------------------- 5' [DNA Polymerase]
 5' ------------------/
          [Lagging]  /
          <--Frag2-- <--Frag1-- (Okazaki fragments joined by Ligase)
 3' ----------------/
        """.trimIndent(),
        schematicCaption = "DNA Replication Fork: Helicase separates strands, Leading strand synthesized continuously, Lagging strand discontinuously in Okazaki fragments.",
        flowcharts = listOf(
            FlowchartStep(1, "Helicase uncoils double helix", "Breaks hydrogen bonds at replication origin", "Helicase"),
            FlowchartStep(2, "Single-Strand Binding Proteins stabilize", "Prevents premature re-annealing of single strands", "SSB Proteins"),
            FlowchartStep(3, "DNA Polymerase builds Leading strand", "Continuous 5' to 3' synthesis toward fork", "DNA Polymerase III"),
            FlowchartStep(4, "DNA Polymerase builds Lagging strand", "Discontinuous synthesis creating Okazaki fragments", "DNA Polymerase III"),
            FlowchartStep(5, "DNA Ligase connects Okazaki fragments", "Forms phosphodiester bonds to complete the strand", "DNA Ligase")
        ),
        visualInspectionMarkers = listOf(
            "Check strand polarity: 5' end has free phosphate group; 3' end has free OH hydroxyl group.",
            "Verify base pairing width: Purine (2 rings) always pairs with Pyrimidine (1 ring) ensuring uniform 2nm diameter.",
            "Inspect Okazaki arrows: Arrows point away from the replication fork for the lagging strand."
        ),
        youtube3DQueries = listOf(
            VideoQuery("3D Animation DNA Replication Fork Okazaki Fragments", "Shows continuous leading strand and discontinuous lagging strand with polymerases.", "3-5 mins"),
            VideoQuery("Hershey and Chase Experiment 3D Animation Bacteriophage", "Visualizes viral injection of radioactive phosphorus vs sulfur.", "2-3 mins")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "q_dna_1",
                type = QuestionType.SCIENTIFIC_TERM,
                question = "Write the scientific term: The enzyme that specifically degrades DNA completely and was used to conclusively confirm DNA as the transforming substance.",
                officialAnswer = "Deoxyribonuclease enzyme (إنزيم دي أوكسي ريبونيوكليز)",
                rationale = "Avery's experiment was finalized using deoxyribonuclease, which eliminated transformation.",
                ministryExamTip = "Do not write 'Ribonuclease' or just 'Nuclease'."
            ),
            PracticeQuestion(
                id = "q_dna_2",
                type = QuestionType.GIVE_REASON,
                question = "Give reason: The two strands of the DNA double helix are arranged in an antiparallel orientation.",
                officialAnswer = "So that the nitrogenous bases face inward correctly and allow the formation of hydrogen bonds between complementary base pairs (A=T and G≡C) with proper steric alignment.",
                rationale = "If parallel, nitrogenous bases could not form hydrogen bonds due to chemical geometry.",
                ministryExamTip = "Must mention: 'Allow correct formation of hydrogen bonds between complementary base pairs'."
            ),
            PracticeQuestion(
                id = "q_dna_3",
                type = QuestionType.MCQ,
                question = "A sample of double-stranded DNA contains 600 nucleotides. If adenine constitutes 20% of the bases, what is the number of guanine bases in this sample?",
                options = listOf("120", "180", "240", "300"),
                correctOptionIndex = 1,
                officialAnswer = "180",
                rationale = "%A = 20% → %T = 20%. Therefore, %G + %C = 60%, so %G = 30%. Total Guanine bases = 600 × 0.30 = 180.",
                ministryExamTip = "Always compute percentage first, then multiply by total nucleotides."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "Applying Chargaff's rules to single-stranded RNA or single-stranded viral DNA.",
                scientificTruth = "%A = %T and %G = %C ONLY applies to double-stranded nucleic acids! In single-stranded DNA/RNA, base proportions are unpredictable.",
                examTrapWarning = "If the exam states: 'In a sample of viral RNA, %A = 30%...', do NOT say %U = 30% unless specified double-stranded!",
                goldenRule = "Chargaff rules require DOUBLE strands."
            )
        ),
        flashcards = listOf(
            Flashcard("fc_dna_1", "dna_structure_replication", "How many hydrogen bonds connect Guanine and Cytosine in DNA?", "Three hydrogen bonds (G ≡ C).", "Structure"),
            Flashcard("fc_dna_2", "dna_structure_replication", "How many base pairs are present in one complete helical turn of DNA?", "10 base pairs (or 20 nucleotides).", "Dimensions"),
            Flashcard("fc_dna_3", "dna_structure_replication", "What is the direction of DNA synthesis by DNA Polymerase?", "Strictly in the 5' to 3' direction.", "Replication"),
            Flashcard("fc_dna_4", "dna_structure_replication", "Which isotope was used by Hershey and Chase to label viral DNA?", "Radioactive Phosphorus-32 (^32P).", "Historical")
        ),
        examSimulationItem = MinistryExamItem(
            id = "sim_dna",
            questionType = QuestionType.GIVE_REASON,
            prompt = "Give Reason: The width of the DNA double helix is uniform (2 nm) along its entire length.",
            maxScore = 2.0,
            requiredKeywords = listOf("purine", "pyrimidine", "two rings", "single ring", "base pair"),
            partialCreditKeywords = listOf("adenine", "thymine", "guanine", "cytosine"),
            modelAnswer = "Because every base pair in the double helix consists of one purine base (two-ring structure: A or G) paired with one pyrimidine base (single-ring structure: T or C), ensuring the sum of rings in every rung is always three rings, yielding a constant width of 2 nm.",
            remediationNotes = "You must mention that a double-ring purine always pairs with a single-ring pyrimidine."
        )
    )

    // -------------------------------------------------------------
    // PACK 3: Immunity (Humoral vs Cell-Mediated)
    // -------------------------------------------------------------
    val packImmunityHumoralCellular = MasterStudyPack(
        topicId = "immunity_humoral_cellular",
        topicTitle = "Humoral vs Cell-Mediated Immunity",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "Chapter 4: Immunity in Living Organisms",
        eli5Analogy = "Your immune system has two elite specialized forces: Humoral Immunity is the Navy launching guided cruise missiles (Antibodies) into bodily fluids to tag or destroy floating pathogens without touching your own cells. Cell-Mediated Immunity is the SWAT team (T-cells) that inspects infected cells directly and drills holes into enemy-harboring cells (Perforin) to neutralize hidden intracellular invaders!",
        deepTechnicalBreakdown = """
### 1. The 3 Lines of Defense
- **1st Line (Innate)**: Physical and chemical barriers (Skin, keratin, sebum, sweat, tears with lysozyme, ear wax, saliva, stomach HCl, respiratory cilia & mucus).
- **2nd Line (Innate)**: Rapid non-specific internal response (Inflammation triggered by **Histamine** from mast cells & basophils, **Natural Killer (NK) cells** using enzymes, **Interferons** protecting neighboring uninfected cells).
- **3rd Line (Acquired / Specific)**: Initiated when 2nd line fails. Divided into Humoral and Cell-Mediated immunity.

### 2. Humoral Immunity (Antibody-Mediated)
1. **Macrophage Role**: Engulfs pathogen, lysosomal enzymes break down antigen into fragments. Antigen fragments bind to **MHC (Major Histocompatibility Complex)** protein and are displayed on macrophage surface (Antigen-MHC complex).
2. **T-Helper Activation**: Inactive TH cell binds with its **CD4 receptor** to the displayed Antigen-MHC complex, activating into Active TH.
3. **B-Cell Activation**: Active TH secretes **Interleukins**, stimulating specific B-lymphocytes (which previously bound the same antigen via surface antibodies) to divide and differentiate into:
   - **Plasma B-cells**: Produce massive amounts of specific antibodies (2000 antibodies/sec).
   - **Memory B-cells**: Persist for decades (20-30 years) to provide rapid secondary immune response.
4. **Antibody Action Mechanisms**:
   - **Neutralization**: Coating viruses and neutralizing toxins.
   - **Agglutination**: Most effective with **IgM** (pentamer, 10 binding sites) clumping pathogens for easy phagocytosis.
   - **Precipitation**: Insoluble lattice formation with soluble antigens.
   - **Lysis**: Triggered by **Complements** boring holes in bacterial cell walls.
   - **Opsonization / Antitoxins**: Facilitates engulfment by macrophages.

### 3. Cell-Mediated Immunity (T-Cell Mediated)
1. Active TH cell secretes **Cytokines**, which recruit macrophages, activate NK cells, stimulate B-cells, and activate **Cytotoxic T-cells (TC, CD8+)**.
2. **TC Action**: Recognizes foreign antigens on cancer cells, transplanted organs, or virus-infected cells.
   - Secretes **Perforin protein**: Creates pores/holes in target cell membrane.
   - Secretes **Lymphotoxins**: Activate specific genes inside target cell nucleus to self-destruct (apoptosis / programmed cell death).
3. **Immune Suppression**: When infection is eradicated, **Suppressor T-cells (TS, CD8+)** secrete **Lymphokines** to inhibit plasma cells (stop antibody synthesis), cause apoptosis of excess TH and TC, and restore homeostasis.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("MHC", "Major Histocompatibility Complex; protein inside macrophage and B-cells that binds digested antigen and displays it on the surface.", "بروتين التوافق النسيجي"),
            KeyTerm("Perforin", "Pore-forming protein secreted by Cytotoxic T-cells (TC) to perforate foreign cell membranes.", "بروتين البيرفورين"),
            KeyTerm("Lymphotoxins", "Chemical messengers secreted by TC that activate apoptotic suicide genes in target nuclei.", "السموم الليمفاوية"),
            KeyTerm("Suppressor T-Cells (TS)", "Lymphocytes bearing CD8 receptors that secrete lymphokines to halt the immune response.", "الخلايا التائية المثبطة")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Antibody Valency (Binding Sites)",
                formulaLatex = "\\text{Sites} = 2 \\times \\text{Monomers}",
                description = "IgG, IgA, IgE, IgD have 2 antigen-binding sites; IgM pentamer has 10 antigen-binding sites.",
                variables = "IgM = Pentamer (5 monomers)",
                sampleCalculation = "For 1 IgM molecule: Antigen binding sites = 5 × 2 = 10 sites."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "Humoral Immunity vs Cell-Mediated Immunity",
                columnA = "Humoral Immunity (B-cells)",
                columnB = "Cell-Mediated Immunity (T-cells)",
                rows = listOf(
                    ComparisonRow("Effector Cells", "Plasma B-cells", "Cytotoxic T-cells (TC) and NK cells"),
                    ComparisonRow("Target Pathogens", "Extracellular pathogens in body fluids (blood and lymph)", "Intracellular pathogens (viruses inside cells, cancer cells, transplanted tissues)"),
                    ComparisonRow("Chemical Weapons", "Antibodies (IgG, IgM, IgA, IgE, IgD)", "Perforin and Lymphotoxins"),
                    ComparisonRow("Signaling Molecules", "Interleukins (from TH)", "Cytokines (from TH)")
                )
            ),
            ComparisonTable(
                title = "T-Lymphocyte Receptors & Functions",
                columnA = "CD4+ Receptors",
                columnB = "CD8+ Receptors",
                rows = listOf(
                    ComparisonRow("Cell Types", "Helper T-cells (TH)", "Cytotoxic T-cells (TC) & Suppressor T-cells (TS)"),
                    ComparisonRow("Primary Function", "Activate B-cells and TC cells via interleukins & cytokines", "TC destroys foreign cells; TS suppresses immune response via lymphokines")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "State the CD receptor on T-helper vs Cytotoxic T-cells.", "Which CD marker is found on TH vs TC and TS cells?"),
            BloomLevelItem("Understanding", 2, "Explain why IgM is the most effective antibody in agglutination.", "How does the pentameric structure of IgM enhance pathogen clumping?"),
            BloomLevelItem("Applying", 3, "Predict immune response outcome in a patient with severe CD4+ deficiency (HIV).", "Explain why HIV destroys both humoral AND cell-mediated immunity."),
            BloomLevelItem("Analyzing", 4, "Analyze the difference between primary and secondary immune response graphs.", "Contrast antibody titers and lag phase between 1st and 2nd exposures."),
            BloomLevelItem("Evaluating", 5, "Evaluate the danger of Suppressor T-cells failing to activate after recovery.", "What happens if TS fails to secrete lymphokines after infection is cleared?"),
            BloomLevelItem("Creating", 6, "Design a targeted monoclonal antibody therapy to neutralize snake venom.", "Outline the mechanism of venom neutralization by administered immunoglobulin.")
        ),
        asciiSchematic = """
================== ANTIBODY Y-SHAPE STRUCTURE ==================
   [Antigen Binding Site]             [Antigen Binding Site]
         \      /                             \      /
          \    /                               \    /
       +---V--+                             +---V--+
       |Variable|                            |Variable|
       | Region |                            | Region |
       +---+----+                            +---+----+
           |                                     |
       +---+----+                            +---+----+
       |Constant|    S-S Disulfide Bridge    |Constant|
       | (Light)|============================| (Light)|
       +---+----+                            +---+----+
           |      \                        /     |
           |       \                      /      |
           |        === S-S Bond ========        |
           |                  |                  |
           |                  |                  |
           +------------------+------------------+
           |           Constant Heavy            |
           |               Region                |
           |                 Fc                  |
           +-------------------------------------+
        """.trimIndent(),
        schematicCaption = "Antibody (Immunoglobulin) Architecture: 2 Light Chains, 2 Heavy Chains, connected by Disulfide bonds (S-S). Variable regions (Fab) provide antigen specificity.",
        flowcharts = listOf(
            FlowchartStep(1, "Macrophage engulfs & digests pathogen", "Lysosomes fragment antigen; binds to MHC", "Phagocytosis"),
            FlowchartStep(2, "Antigen-MHC presented on macrophage surface", "Recognized by Helper T-cell (TH)", "Antigen Presentation"),
            FlowchartStep(3, "TH binds via CD4 receptor and activates", "Secretes Interleukins and Cytokines", "CD4 Activation"),
            FlowchartStep(4, "Interleukins stimulate B-cells to proliferate", "Differentiate into Plasma B-cells & Memory B-cells", "Clonal Expansion"),
            FlowchartStep(5, "Plasma B-cells secrete specific Antibodies", "Neutralize, agglutinate, precipitate, or lyse via complement", "Humoral Defense"),
            FlowchartStep(6, "Cytokines activate TC (CD8+) cells", "TC secretes Perforin and Lymphotoxins against infected cells", "Cell-Mediated Defense"),
            FlowchartStep(7, "TS (CD8+) secretes Lymphokines after clearance", "Halts antibody production and eliminates excess lymphocytes", "Immune Homeostasis")
        ),
        visualInspectionMarkers = listOf(
            "Locate the Variable region (tip of Y arms): Shape must match the specific antigen epitope like lock-and-key.",
            "Count Disulfide bonds: 2 between heavy chains in hinge region, and 1 connecting each light chain to heavy chain.",
            "Identify the receptor on the cell: CD4 for TH, CD8 for TC and TS."
        ),
        youtube3DQueries = listOf(
            VideoQuery("3D Animation Clonal Selection Humoral Immunity B Cells", "Watch B cells encounter antigen, divide into plasma cells, and produce antibodies.", "3-4 mins"),
            VideoQuery("Cytotoxic T Cell Perforin Killing Cancer 3D Animation", "Real-time visualization of TC cell releasing perforin and destroying tumor cells.", "2-4 mins")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "q_imm_1",
                type = QuestionType.SCIENTIFIC_TERM,
                question = "Write the scientific term: The pore-forming protein secreted by cytotoxic T-cells that perforates the membrane of foreign or virus-infected cells.",
                officialAnswer = "Perforin protein (بروتين البيرفورين / صانع الثقوب)",
                rationale = "Perforin is exclusively secreted by TC cells to create physical pores.",
                ministryExamTip = "Do not confuse 'Perforin' (pore maker) with 'Lymphotoxins' (activates suicide genes)."
            ),
            PracticeQuestion(
                id = "q_imm_2",
                type = QuestionType.GIVE_REASON,
                question = "Give reason: Destruction of T-helper lymphocytes (TH) causes complete collapse of the specific immune system.",
                officialAnswer = "Because TH cells are the master switch of both branches of acquired immunity: they activate B-lymphocytes by secreting Interleukins (humoral immunity) and activate Cytotoxic T-cells (TC), macrophages, and NK cells by secreting Cytokines (cell-mediated immunity). Without TH, neither antibodies nor cytotoxic killer responses can be initiated.",
                rationale = "TH controls both Humoral and Cellular specific immunity.",
                ministryExamTip = "Must mention BOTH interleukins for B-cells AND cytokines for TC cells."
            ),
            PracticeQuestion(
                id = "q_imm_3",
                type = QuestionType.WHAT_HAPPENS_IF,
                question = "What happens if: Suppressor T-lymphocytes (TS) fail to function after recovery from a bacterial infection?",
                officialAnswer = "Plasma cells will continue secreting antibodies unnecessarily, and active effector T-cells will not undergo apoptosis, leading to severe hyper-immune reaction and potential autoimmune attack against the body's own tissues.",
                rationale = "TS and lymphokines are essential for immune system termination.",
                ministryExamTip = "Mention failure to secrete lymphokines and persistence of immune activity."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "Believing antibodies can destroy viruses inside infected body cells.",
                scientificTruth = "Antibodies CANNOT cross the cell membrane! They only neutralize free extracellular viruses in blood and lymph. Intracellular viruses require Cytotoxic T-cells (TC) and NK cells.",
                examTrapWarning = "Ministry trap: 'Which cell eliminates a virus already inside a lung cell?' Correct answer is TC cell, NOT B-cell or antibody!",
                goldenRule = "Antibodies = Extracellular; TC cells = Intracellular."
            )
        ),
        flashcards = listOf(
            Flashcard("fc_imm_1", "immunity_humoral_cellular", "What receptor is found on T-helper cells?", "CD4 receptor.", "Receptors"),
            Flashcard("fc_imm_2", "immunity_humoral_cellular", "What chemical is secreted by Suppressor T-cells (TS) to halt immunity?", "Lymphokines.", "Signaling"),
            Flashcard("fc_imm_3", "immunity_humoral_cellular", "Which antibody is most effective in agglutination and why?", "IgM, because it is a pentamer with 10 antigen-binding sites.", "Antibodies"),
            Flashcard("fc_imm_4", "immunity_humoral_cellular", "What chemical substance triggers the 2nd line inflammatory response?", "Histamine (from mast cells and basophils).", "Innate")
        ),
        examSimulationItem = MinistryExamItem(
            id = "sim_imm",
            questionType = QuestionType.GIVE_REASON,
            prompt = "Give Reason: The secondary immune response is much faster and stronger than the primary immune response.",
            maxScore = 2.0,
            requiredKeywords = listOf("memory cells", "b cells", "t cells", "antibodies", "faster"),
            partialCreditKeywords = listOf("lag phase", "titer", "antigen"),
            modelAnswer = "Because upon re-exposure to the same pathogen, Memory B-cells and Memory T-cells that were stored during the primary response immediately recognize the antigen and rapidly divide and differentiate into active plasma and cytotoxic cells, producing a massive quantity of antibodies in a very short time with virtually no lag phase, destroying the pathogen before symptoms appear.",
            remediationNotes = "You must mention the presence of Memory cells (Memory B and T cells) and rapid production of high antibody titers."
        )
    )

    // Other pre-baked study packs for the rest of chapters
    val packRnaTranscriptionTranslation = MasterStudyPack(
        topicId = "rna_transcription_translation",
        topicTitle = "RNA Transcription, Translation & Genetic Code",
        unit = UnitCategory.UNIT_2,
        chapterTitle = "Chapter 2: RNA & Protein Synthesis",
        eli5Analogy = "The Master Recipe Book (DNA) is safely locked in the kitchen library (Nucleus). To cook a dish (Protein), an assistant photocopies one recipe page into working paper (mRNA). The paper travels to the cooking station (Ribosome), where delivery carts (tRNA) read the 3-letter codes and drop off the matching ingredients (Amino Acids) in order, stringing them together into a completed meal!",
        deepTechnicalBreakdown = """
### 1. Types of RNA
- **mRNA (Messenger RNA)**: Carries genetic code from DNA in nucleus to ribosomes in cytoplasm.
  - Features: $5'$ Cap (ribosome binding site), Start Codon (AUG coding for Methionine), Coding sequence, Stop Codon (UAA, UAG, UGA), and Poly-A Tail (about 200 adenine residues protecting mRNA from enzymatic breakdown by cytoplasm nucleases).
- **tRNA (Transfer RNA)**: Transports specific amino acids to ribosomes.
  - Features: Cloverleaf secondary structure, $3'$ terminal CCA sequence (where amino acid binds), and **Anticodon loop** complementary to mRNA codon.
- **rRNA (Ribosomal RNA)**: 4 different types synthesized in the nucleolus, combining with 70 polypeptides to form small and large ribosomal subunits.

### 2. Transcription Mechanism
- Catalyzed by **RNA Polymerase** (Eukaryotes have 3 distinct RNA polymerases for mRNA, tRNA, rRNA; Prokaryotes have a single RNA polymerase).
- RNA Polymerase binds the **Promoter** sequence on the DNA template strand and transcribes strictly $5' \to 3'$.

### 3. Translation Stages
1. **Initiation**: Small ribosomal subunit binds to mRNA $5'$ cap. Initiator tRNA carrying Methionine (anticodon UAC) binds to start codon AUG in the P-site. Large subunit joins.
2. **Elongation**: Next tRNA binds to the A-site. **Peptidyl Transferase** enzyme (an RNA component of the large subunit) forms a peptide bond between amino acids, releasing a water molecule. Ribosome shifts 1 codon ($3$ nucleotides) toward the $3'$ end.
3. **Termination**: When a Stop Codon (UAA, UAG, or UGA) reaches the A-site, a protein called **Release Factor** binds instead of tRNA, causing the synthesized polypeptide to detach and the ribosomal subunits to dissociate.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Peptidyl Transferase", "Catalytic rRNA component in large ribosomal subunit that catalyzes peptide bond formation.", "إنزيم ببتيديل ترانسفيريز"),
            KeyTerm("Release Factor", "Protein that binds stop codons (UAA, UAG, UGA) in the A-site, terminating translation.", "عامل الإطلاق"),
            KeyTerm("Poly-A Tail", "Tail of ~200 adenine nucleotides at the 3' end of mRNA protecting it from enzymatic degradation.", "ذيل عديد الأدنين"),
            KeyTerm("Promoter", "Specific DNA sequence that directs RNA polymerase where to start transcription.", "المحفز")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Amino Acid to Codon / Nucleotide Conversion",
                formulaLatex = "\\text{Codons} = N_{aa} + 1, \\quad \\text{Nucleotides} = 3(N_{aa} + 1)",
                description = "Each amino acid requires a 3-base codon plus 1 stop codon.",
                variables = "N_{aa} = Number of amino acids",
                sampleCalculation = "For 50 amino acids: Codons = 51; mRNA nucleotides = 51 × 3 = 153 nucleotides."
            ),
            FormulaItem(
                name = "Peptide Bonds Count",
                formulaLatex = "\\text{Peptide Bonds} = N_{aa} - 1 = \\text{Water Molecules}",
                description = "Condensation synthesis releases 1 water molecule per peptide bond.",
                variables = "N_{aa} = Number of amino acids",
                sampleCalculation = "For 50 amino acids: Peptide bonds = 49 bonds (49 H₂O released)."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "DNA Polymerase vs RNA Polymerase",
                columnA = "DNA Polymerase",
                columnB = "RNA Polymerase",
                rows = listOf(
                    ComparisonRow("Substrate Product", "Synthesizes DNA strand", "Synthesizes RNA molecule (mRNA, tRNA, rRNA)"),
                    ComparisonRow("Requirement for Primer", "Requires RNA primer to start", "Can initiate synthesis directly without primer"),
                    ComparisonRow("Complementary Base to A", "Adds Thymine (T)", "Adds Uracil (U)")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "List the three stop codons.", "Name the three stop codons that do not code for any amino acid."),
            BloomLevelItem("Understanding", 2, "Explain the protective role of the Poly-A tail.", "Why would mRNA rapidly degrade in cytoplasm without its poly-A tail?"),
            BloomLevelItem("Applying", 3, "Calculate minimum nucleotides for a polypeptide of 100 amino acids.", "Find mRNA codons, mRNA nucleotides, and double-stranded DNA nucleotides for 100 amino acids."),
            BloomLevelItem("Analyzing", 4, "Analyze the effect of a single nucleotide deletion (frameshift mutation).", "Why is a deletion mutation far more disruptive to protein structure than a substitution mutation?"),
            BloomLevelItem("Evaluating", 5, "Evaluate why the genetic code is nearly universal across all living organisms.", "What does the universality of the genetic code prove regarding evolution?"),
            BloomLevelItem("Creating", 6, "Derive the mRNA and amino acid sequence from a given template DNA sequence.", "Translate 3'-TAC-GGC-TTA-ACT-5' into mRNA and peptide sequence.")
        ),
        asciiSchematic = """
================ TRANSLATION ELONGATION STEP ================
                       [ Large Subunit ]
                      /                 \
                     /    [P]     [A]    \
                    /   |tRNA|  |tRNA|    \
                    |   |Met |  |Pro |    |
                    |    \  /    \  /     |
                    |     ||      ||      |
  5' ===[Cap]=========[AUG]=====[CCU]=====[UAA]======[Poly-A] 3'
                     \                  /
                      \[Small Subunit ]/
        """.trimIndent(),
        schematicCaption = "Ribosome during translation: P-site carries growing peptide chain; A-site receives incoming aminoacyl-tRNA. Peptidyl transferase creates the peptide bond.",
        flowcharts = listOf(
            FlowchartStep(1, "RNA Polymerase binds Promoter", "Unwinds localized DNA transcription bubble", "Promoter DNA"),
            FlowchartStep(2, "Transcription in 5' to 3' direction", "Complementary ribonucleotides added (Uracil replaces Thymine)", "RNA Polymerase"),
            FlowchartStep(3, "mRNA processing and capping", "5' cap and 3' poly-A tail added before nuclear export", "Enzymatic capping"),
            FlowchartStep(4, "Initiation complex assembly", "Small subunit binds 5' cap; initiator tRNA (UAC) binds AUG", "AUG start codon"),
            FlowchartStep(5, "Elongation & Peptide bond formation", "Peptidyl transferase joins amino acids; ribosome translocates", "Peptidyl transferase"),
            FlowchartStep(6, "Termination via Release Factor", "Stop codon binds Release Factor; peptide dissociates", "Release Factor")
        ),
        visualInspectionMarkers = listOf(
            "Check start and stop codons: AUG at 5' end; UAA/UAG/UGA at 3' end.",
            "Inspect tRNA binding sites: P-site holds peptidyl-tRNA; A-site holds incoming aminoacyl-tRNA.",
            "Note the CCA sequence: Always located at the 3' end of every tRNA molecule."
        ),
        youtube3DQueries = listOf(
            VideoQuery("3D Animation Protein Synthesis Transcription and Translation", "Detailed molecular animation of ribosome translation mechanism.", "4-6 mins")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "q_rna_1",
                type = QuestionType.SCIENTIFIC_TERM,
                question = "Write the scientific term: The chemical site on the tRNA molecule that recognizes and binds complementary codons on mRNA.",
                officialAnswer = "Anticodon loop (موقع مضاد الكودون)",
                rationale = "Anticodon is a triplet of bases specific to each tRNA.",
                ministryExamTip = "Do not confuse Anticodon (on tRNA) with Codon (on mRNA)."
            ),
            PracticeQuestion(
                id = "q_rna_2",
                type = QuestionType.GIVE_REASON,
                question = "Give reason: A single amino acid may have more than one codon, but a single codon never codes for more than one amino acid.",
                officialAnswer = "Because the genetic code is redundant (degenerate): there are 61 coding codons for only 20 amino acids, so multiple codons code for the same amino acid. However, the genetic code is unambiguous, so each specific codon directs the incorporation of only one specific amino acid.",
                rationale = "61 codons > 20 amino acids = degeneracy without ambiguity.",
                ministryExamTip = "Use the terms '61 codons code for 20 amino acids'."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "Counting stop codons as coding for amino acids or tRNA.",
                scientificTruth = "Stop codons (UAA, UAG, UGA) do NOT code for any amino acid and have NO corresponding tRNA! They bind a protein called Release Factor.",
                examTrapWarning = "If a question asks: 'How many tRNA molecules are needed to synthesize a peptide of 50 amino acids?', the answer is 50, NOT 51!",
                goldenRule = "Stop codons do not bind tRNA."
            )
        ),
        flashcards = listOf(
            Flashcard("fc_rna_1", "rna_transcription_translation", "What amino acid is specified by the start codon AUG?", "Methionine.", "Codons"),
            Flashcard("fc_rna_2", "rna_transcription_translation", "What enzyme catalyzes peptide bond formation inside the ribosome?", "Peptidyl transferase.", "Enzymes"),
            Flashcard("fc_rna_3", "rna_transcription_translation", "How many adenine nucleotides form the poly-A tail?", "About 200 adenine nucleotides.", "Structure")
        ),
        examSimulationItem = MinistryExamItem(
            id = "sim_rna",
            questionType = QuestionType.GIVE_REASON,
            prompt = "Give Reason: The absence of the Poly-A tail causes rapid destruction of the mRNA molecule in the cytoplasm.",
            maxScore = 2.0,
            requiredKeywords = listOf("poly-a tail", "cellular enzymes", "breakdown", "cytoplasm", "nucleases"),
            partialCreditKeywords = listOf("adenine", "degradation"),
            modelAnswer = "Because the Poly-A tail (composed of ~200 adenine nucleotides) functions to protect mRNA from enzymatic degradation by cytoplasmic nucleolytic enzymes. Without this tail, enzymes rapidly attack and hydrolyze the mRNA coding sequence before translation can occur.",
            remediationNotes = "Highlight protection against cytoplasmic destructive enzymes."
        )
    )

    // Human Reproduction & Menstrual Cycle
    val packHumanReproductionMenstrual = MasterStudyPack(
        topicId = "human_reproduction_menstrual",
        topicTitle = "Human Reproduction & The Menstrual Cycle",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "Chapter 3: Reproduction in Living Organisms",
        eli5Analogy = "The human reproductive cycle is an orchestra conducted by the brain (Pituitary gland). Act 1 (Days 1-14): FSH acts as a scout helping one egg mature inside an estrogen greenhouse (Graafian follicle). Act 2 (Day 14): A sudden burst of fireworks (LH surge) pops the follicle, launching the egg into the fallopian tube. Act 3 (Days 14-28): The leftover shell turns into a yellow heating blanket (Corpus Luteum) producing progesterone to warm up the uterine nest. If no baby arrives, the blanket withers, hormone levels crash, and the uterine lining sheds to start fresh!",
        deepTechnicalBreakdown = """
### 1. The 28-Day Menstrual Cycle Phases
1. **Proliferative (Follicular) Phase (Days 5 to 14 - Duration ~10 days)**:
   - Anterior pituitary secretes **FSH (Follicle-Stimulating Hormone)**.
   - Stimulates growth of several primordial follicles in the ovary; usually one matures into a **Graafian follicle**.
   - Graafian follicle secretes **Estrogen**, which thickens the endometrium (uterine lining) and promotes capillary growth.
2. **Ovulatory Phase (Day 14)**:
   - High estrogen triggers positive feedback causing a sharp **LH (Luteinizing Hormone) Surge**.
   - LH induces rupture of the Graafian follicle, expelling the **Secondary Oocyte** into the Fallopian tube (Ovulation).
   - Remnants of ruptured Graafian follicle transform into the **Corpus Luteum (الجسـم الأصفر)**.
3. **Secretory (Luteal) Phase (Days 14 to 28 - Duration ~14 days)**:
   - Corpus Luteum secretes large amounts of **Progesterone** (and some estrogen).
   - Progesterone causes extensive thickening of endometrium, increases glandular secretions, and enhances vascularization in preparation for implantation.
   - High progesterone exerts **negative feedback** on pituitary, inhibiting further FSH and LH secretion (preventing new ovulation).
4. **Menstruation Phase (Days 1 to 5 - Duration 3-5 days)**:
   - If fertilization does NOT occur, secondary oocyte dies within 24-48 hours.
   - Corpus Luteum degenerates at the end of cycle, progesterone drops precipitously.
   - Endometrial blood vessels spasm and rupture; lining sheds along with blood as **Menstruation**.

### 2. If Fertilization Occurs
- Fertilization takes place in the **upper third of the Fallopian tube**.
- Embryo produces **hCG (human Chorionic Gonadotropin)**, maintaining the Corpus Luteum to continue progesterone secretion until the **end of the 3rd month**.
- Beginning from the **4th month**, the **Placenta** is fully developed and takes over progesterone secretion; the Corpus Luteum degenerates safely.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Graafian Follicle", "Mature ovarian follicle enclosing the secondary oocyte and secreting estrogen.", "حويصلة جراف"),
            KeyTerm("Corpus Luteum", "Yellow glandular body formed from ruptured follicle, secreting progesterone.", "الجسم الأصفر"),
            KeyTerm("LH Surge", "Sudden peak of Luteinizing Hormone on day 14 triggering ovulation.", "قمة هرمون LH"),
            KeyTerm("Placenta", "Organ formed by fusion of chorionic villi with uterine wall; secretes progesterone from 4th month.", "المشيمة")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Day of Ovulation Formula",
                formulaLatex = "\\text{Ovulation Day} = \\text{Cycle Length} - 14",
                description = "Luteal phase is strictly fixed at 14 days; ovulation occurs 14 days before menstruation starts.",
                variables = "Cycle Length = Duration in days (e.g. 28 days)",
                sampleCalculation = "For a 28-day cycle: Ovulation = 28 - 14 = Day 14. For a 32-day cycle: 32 - 14 = Day 18."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "Estrogen vs Progesterone",
                columnA = "Estrogen (Graafian Follicle)",
                columnB = "Progesterone (Corpus Luteum & Placenta)",
                rows = listOf(
                    ComparisonRow("Peak Secretion", "Day 12-13 (pre-ovulation)", "Day 21-23 (mid-luteal phase)"),
                    ComparisonRow("Primary Uterine Action", "Proliferation and repair of endometrium", "Increases thickness, vascularity, and glandular secretions"),
                    ComparisonRow("Action on Pituitary", "Triggers LH surge at threshold", "Inhibits FSH and LH release (prevents ovulation)")
                )
            ),
            ComparisonTable(
                title = "Monozygotic (Identical) vs Dizygotic (Fraternal) Twins",
                columnA = "Identical Twins (أحادية اللقاحة)",
                columnB = "Fraternal Twins (ثنائية اللقاحة)",
                rows = listOf(
                    ComparisonRow("Origin", "One ovum fertilized by one sperm, splitting into 2", "Two separate ova fertilized by two separate sperms"),
                    ComparisonRow("Sex & Genetics", "Always identical sex, identical genome", "May be same or different sex, genetically like regular siblings"),
                    ComparisonRow("Placenta", "Share one common placenta", "Two distinct separate placentas")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "State the hormone responsible for ovulation on day 14.", "Which hormone surges to rupture the Graafian follicle?"),
            BloomLevelItem("Understanding", 2, "Explain why ovulation stops completely during pregnancy.", "Why do pregnant women never ovulate during gestation?"),
            BloomLevelItem("Applying", 3, "Calculate the exact ovulation day for a female with a 30-day regular cycle.", "Find the day of ovulation and highest progesterone level in a 30-day cycle."),
            BloomLevelItem("Analyzing", 4, "Analyze what happens if Corpus Luteum is surgically removed during the 2nd month of pregnancy vs the 5th month.", "Contrast the consequence of ovariectomy at month 2 vs month 5."),
            BloomLevelItem("Evaluating", 5, "Assess the biological mechanism of contraceptive pills containing estrogen and progesterone.", "How do birth control pills prevent pregnancy at the endocrine level?"),
            BloomLevelItem("Creating", 6, "Construct a clinical hormone monitoring chart for diagnosing female infertility.", "Formulate a diagnostic timeline measuring FSH, LH, and progesterone.")
        ),
        asciiSchematic = """
================== 28-DAY MENSTRUAL HORMONE CURVES ==================
 Concentration
       ^
       |                [LH SURGE]
       |                    /\
       |        [ESTROGEN] /  \       [PROGESTERONE]
       |           /\     /    \            /\
       |   [FSH]  /  \   /      \   _      /  \
       |    /\   /    \_/        \_/ \    /    \
       |---/--\_/---------------------\--/------\----------> Days
       0       4              14             21        28
       | Menstruation | Proliferation |    Luteal Phase   |
        """.trimIndent(),
        schematicCaption = "Hormone dynamics across 28 days: FSH stimulates follicle; Estrogen rises; LH surges at Day 14 causing ovulation; Progesterone peaks during luteal phase.",
        flowcharts = listOf(
            FlowchartStep(1, "FSH stimulates primordial follicle", "Graafian follicle grows in ovary", "FSH from Pituitary"),
            FlowchartStep(2, "Graafian follicle secretes Estrogen", "Endometrium repairs and thickens", "Estrogen"),
            FlowchartStep(3, "LH Surge on Day 14 triggers Ovulation", "Secondary oocyte released; Corpus Luteum forms", "LH Surge"),
            FlowchartStep(4, "Corpus Luteum secretes Progesterone", "Endometrium becomes highly vascularized", "Progesterone"),
            FlowchartStep(5, "If no fertilization: Corpus Luteum degenerates", "Progesterone level collapses; menstruation occurs", "Drop in Progesterone")
        ),
        visualInspectionMarkers = listOf(
            "Look for the sharpest spike around day 13-14: That is LH.",
            "Look for the hormone that peaks in the second half (days 20-22): That is Progesterone.",
            "Check twin diagrams: 1 shared placenta = Identical; 2 separate placentas = Fraternal."
        ),
        youtube3DQueries = listOf(
            VideoQuery("3D Animation Menstrual Cycle Ovarian and Uterine Cycles", "Complete synchrony of pituitary hormones, ovarian cycle, and uterine endometrium.", "4-6 mins"),
            VideoQuery("Human Fertilization and Implantation 3D Animation", "Watch sperm capacitation, acrosome reaction, and blastocyst implantation.", "3-5 mins")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "q_rep_1",
                type = QuestionType.SCIENTIFIC_TERM,
                question = "Write the scientific term: The yellow glandular structure formed in the ovary from the remnants of the ruptured Graafian follicle.",
                officialAnswer = "Corpus Luteum (الجسم الأصفر)",
                rationale = "Formed under influence of LH after ovulation.",
                ministryExamTip = "Spelled Corpus Luteum."
            ),
            PracticeQuestion(
                id = "q_rep_2",
                type = QuestionType.GIVE_REASON,
                question = "Give reason: Surgical removal of both ovaries in the 2nd month of pregnancy causes abortion, whereas in the 5th month it does not.",
                officialAnswer = "In the 2nd month, the Corpus Luteum in the ovary is the sole source of progesterone required to maintain the endometrium; removing the ovaries causes progesterone to collapse and induces miscarriage. In the 5th month, the fully developed placenta has already taken over progesterone secretion, so removing ovaries does not disrupt pregnancy.",
                rationale = "Placenta takes over progesterone secretion beginning from the 4th month.",
                ministryExamTip = "Must mention: 'Placenta secretes progesterone starting in the 4th month'."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "Thinking the ovum released at ovulation is already a mature ovum.",
                scientificTruth = "The released gamete is a SECONDARY OOCYTE arrested in Metaphase II! It completes meiosis II ONLY upon fertilization by a sperm.",
                examTrapWarning = "Ministry trick question: 'What is released at ovulation on Day 14?' Option: Secondary Oocyte, NOT mature ovum!",
                goldenRule = "Secondary oocyte is released at ovulation; mature ovum forms only at fertilization."
            )
        ),
        flashcards = listOf(
            Flashcard("fc_rep_1", "human_reproduction_menstrual", "Where does fertilization normally occur in the female reproductive tract?", "In the upper third of the Fallopian tube (oviduct).", "Anatomy"),
            Flashcard("fc_rep_2", "human_reproduction_menstrual", "When does the placenta take over progesterone secretion from the Corpus Luteum?", "At the beginning of the 4th month of pregnancy.", "Pregnancy"),
            Flashcard("fc_rep_3", "human_reproduction_menstrual", "What is the chromosomal number of the Secondary Oocyte?", "Haploid (n).", "Genetics")
        ),
        examSimulationItem = MinistryExamItem(
            id = "sim_rep",
            questionType = QuestionType.GIVE_REASON,
            prompt = "Give Reason: Ovulation ceases completely in pregnant women throughout the gestation period.",
            maxScore = 2.0,
            requiredKeywords = listOf("progesterone", "corpus luteum", "placenta", "inhibits", "fsh"),
            partialCreditKeywords = listOf("pituitary", "lh", "follicle"),
            modelAnswer = "Because throughout pregnancy, continuous high levels of progesterone secreted initially by the Corpus Luteum and subsequently by the Placenta exert strong negative feedback on the anterior pituitary gland, preventing the secretion of FSH and LH, which halts the maturation of new ovarian follicles and prevents ovulation.",
            remediationNotes = "Clarify negative feedback exerted by progesterone on FSH/LH."
        )
    )

    // Other topic fallback packs for remaining subtopics
    val packPlantHumanSupport = MasterStudyPack(
        topicId = "plant_human_support",
        topicTitle = "Plant Support & Human Skeletal System",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "Chapter 1: Support and Movement",
        eli5Analogy = "Plant physiological support is like a water balloon: pump it full of water (osmosis into vacuole), and it stands stiff; let it dry, and it wilts. Plant structural support is like the wooden bricks of the house: dead, permanent thick walls (lignin, suberin) that never wilt even without water!",
        deepTechnicalBreakdown = """
### 1. Support in Plants
- **Physiological Support (دعامة فسيولوجية)**: Temporary support. Water enters plant cell sap vacuole by **osmosis**, vacuole expands, pushes cytoplasm outward against cell wall, generating **Turgor Pressure**. Cell becomes turgid and erect. Lost when plant loses water.
- **Structural Support (دعامة تركيبية)**: Permanent support. Deposition of tough, impermeable substances on cell walls:
  - **Cellulose & Lignin**: In collenchyma and sclerenchyma (fibers & stone cells) providing rigidity and tensile strength.
  - **Cutin & Suberin**: In epidermal cells and cork tissue preventing water loss.

### 2. Human Skeletal System (206 Bones)
- **Axial Skeleton (80 bones)**:
  - **Skull (29 bones)**: Cerebral/cranial part (8 serrated interlocked bones protecting brain) + Facial part (14 bones) + Associated bones (6 auditory ossicles + 1 hyoid bone).
  - **Vertebral Column (33 vertebrae, 26 articulated bones)**: 7 cervical, 12 thoracic, 5 lumbar (largest and bears most body weight), 5 fused sacral (1 bone), 4 fused coccygeal (1 bone).
  - **Thoracic Cage (37 bones)**: 12 thoracic vertebrae + Sternum + 12 pairs of ribs (first 7 pairs true ribs, 8-10 false ribs, 11-12 floating ribs).
- **Appendicular Skeleton (126 bones)**:
  - **Pectoral Girdle & Upper Limbs**: Scapula + Clavicle, Humerus, Radius & Ulna, Carpals (8), Metacarpals (5), Phalanges (14).
  - **Pelvic Girdle & Lower Limbs**: Ilium, Ischium, Pubis fused into 1 hip bone per side, Femur, Patella, Tibia & Fibula, Tarsals (7), Metatarsals (5), Phalanges (14).
- **Joints, Cartilage, Ligaments & Tendons**:
  - **Ligaments**: Connect bone to bone; strong and elastic to allow joint movement.
  - **Tendons**: Connect muscle to bone; strong and non-elastic (e.g. **Achilles Tendon**).
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Physiological Support", "Temporary turgor-driven support dependent on water absorption by osmosis into sap vacuole.", "دعامة فسيولوجية"),
            KeyTerm("Structural Support", "Permanent support achieved by deposition of cellulose, lignin, cutin, or suberin on cell walls.", "دعامة تركيبية"),
            KeyTerm("Achilles Tendon", "Strong fibrous cord connecting the gastrocnemius (calf muscle) to the heel bone (calcaneus).", "وتر أخيل"),
            KeyTerm("Ligament", "Strong elastic band of fibrous connective tissue connecting bones at joints.", "رباط")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Vertebral Column Bone Count",
                formulaLatex = "33 \\text{ vertebrae} = 26 \\text{ bones}",
                description = "24 articulating vertebrae + 1 sacrum (5 fused) + 1 coccyx (4 fused) = 26 bones.",
                variables = "Vertebrae = 33; Bones = 26",
                sampleCalculation = "Articulating bones = 7 (cervical) + 12 (thoracic) + 5 (lumbar) + 1 (sacrum) + 1 (coccyx) = 26 bones."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "Physiological Support vs Structural Support",
                columnA = "Physiological Support",
                columnB = "Structural Support",
                rows = listOf(
                    ComparisonRow("Permanence", "Temporary (depends on water presence)", "Permanent (deposition of physical substances)"),
                    ComparisonRow("Cell Coverage", "Affects the entire cell as a whole", "Affects the cell wall or parts of it"),
                    ComparisonRow("Mechanism", "Osmosis and turgor pressure in sap vacuole", "Deposition of cutin, suberin, cellulose, or lignin")
                )
            ),
            ComparisonTable(
                title = "Tendons vs Ligaments (Ministry Classic Trap)",
                columnA = "Tendons (أوتار)",
                columnB = "Ligaments (أربطة)",
                rows = listOf(
                    ComparisonRow("Connection", "Connects muscle to bone", "Connects bone to bone at joints"),
                    ComparisonRow("Elasticity", "Strong and INELASTIC (transfers pull force directly)", "Strong with marked ELASTICITY (allows bones to yield under stress)"),
                    ComparisonRow("Classic Example", "Achilles tendon connecting calf muscle to calcaneus", "Cruciate ligaments (anterior, posterior) in knee joint")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "Recall the number of bones in adult human skeleton.", "How many bones make up the adult human skeleton? (206)"),
            BloomLevelItem("Understanding", 2, "Explain why physiological support is considered temporary.", "Why does a plant lose physiological support during drought?"),
            BloomLevelItem("Applying", 3, "Calculate the number of floating ribs in the human thoracic cage.", "How many pairs of ribs do not connect to the sternum? (2 pairs, 4 ribs)"),
            BloomLevelItem("Analyzing", 4, "Analyze the clinical consequences of a complete rupture of the Achilles tendon.", "What causes Achilles rupture and how is it treated?"),
            BloomLevelItem("Evaluating", 5, "Evaluate why ligaments must possess elasticity while tendons must not.", "What would happen if tendons were elastic like rubber bands?"),
            BloomLevelItem("Creating", 6, "Design an artificial knee joint replacement model replicating cruciate ligaments.", "How do synthetic polymers replicate ligament elasticity?")
        ),
        asciiSchematic = """
============== VERTEBRAL COLUMN ARCHITECTURE ==============
 Cervical (7)   --- [1-7]   Neck vertebrae (medium size, articulating)
 Thoracic (12)  --- [8-19]  Chest vertebrae (articulate with 12 pairs of ribs)
 Lumbar (5)     --- [20-24] Abdominal vertebrae (LARGEST, face abdominal cavity)
 Sacral (5)     --- [25-29] Fused into 1 flat bone (Sacrum)
 Coccygeal (4)  --- [30-33] Fused into 1 small bone (Coccyx)
 Total Vertebrae: 33  |  Total Bones: 26
        """.trimIndent(),
        schematicCaption = "Vertebral column organization: 24 movable articulating vertebrae + 2 fused bones (sacrum & coccyx). Lumbar vertebrae bear the highest body weight.",
        flowcharts = listOf(
            FlowchartStep(1, "Water enters plant cell sap vacuole", "Driven by osmotic gradient", "Osmosis"),
            FlowchartStep(2, "Vacuole volume increases", "Generates hydrostatic turgor pressure", "Turgor pressure"),
            FlowchartStep(3, "Protoplasm pushed against cell wall", "Cell becomes tense and rigid", "Physiological support")
        ),
        visualInspectionMarkers = listOf(
            "Identify lumbar vertebra: Thickest centrum, broad spinous process.",
            "Inspect Achilles tendon: Anchored between calf muscle (gastrocnemius) and heel bone (calcaneus).",
            "Knee joint ligaments: Look for cross shape (Anterior and Posterior Cruciate Ligaments)."
        ),
        youtube3DQueries = listOf(
            VideoQuery("Human Skeletal System 3D Anatomy Axial and Appendicular", "Detailed anatomical overview of skull, vertebral column, ribs, and limb girdles.", "5-7 mins")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "q_sup_1",
                type = QuestionType.SCIENTIFIC_TERM,
                question = "Write the scientific term: Tough, non-elastic fibrous tissue that connects the gastrocnemius muscle to the calcaneus bone.",
                officialAnswer = "Achilles tendon (وتر أخيل)",
                rationale = "Achilles tendon connects calf muscle to heel bone.",
                ministryExamTip = "Spelled Achilles tendon."
            ),
            PracticeQuestion(
                id = "q_sup_2",
                type = QuestionType.GIVE_REASON,
                question = "Give reason: Ligaments possess a degree of elasticity, whereas tendons are non-elastic.",
                officialAnswer = "Ligaments must be elastic to allow slight elongation during joint movement, preventing tears when joint angle changes and allowing bones to move smoothly without dislocating. Tendons must be non-elastic so that muscle contraction force is transmitted immediately and without loss to the bone to cause movement.",
                rationale = "Elasticity in tendons would absorb and waste muscle contractile force.",
                ministryExamTip = "Contrast: Ligaments allow joint yielding; tendons transmit pull force directly."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "Counting 33 bones in the vertebral column.",
                scientificTruth = "There are 33 VERTEBRAE, but only 26 BONES! The 5 sacral vertebrae are fused into 1 bone (sacrum), and the 4 coccygeal are fused into 1 bone (coccyx).",
                examTrapWarning = "Always check whether the question asks for 'vertebrae' or 'bones'!",
                goldenRule = "33 vertebrae = 26 bones."
            )
        ),
        flashcards = listOf(
            Flashcard("fc_sup_1", "plant_human_support", "Which vertebrae are the largest in size in the human spine?", "The Lumbar vertebrae (vertebrae 20 to 24).", "Anatomy"),
            Flashcard("fc_sup_2", "plant_human_support", "What impermeable substance is deposited on epidermal cells to prevent water loss?", "Cutin.", "Botany"),
            Flashcard("fc_sup_3", "plant_human_support", "What is the total number of bones in the human thoracic cage?", "37 bones (12 thoracic vertebrae + 24 ribs + 1 sternum).", "Counting")
        ),
        examSimulationItem = MinistryExamItem(
            id = "sim_sup",
            questionType = QuestionType.GIVE_REASON,
            prompt = "Give Reason: Physiological support is considered a temporary support, while structural support is permanent.",
            maxScore = 2.0,
            requiredKeywords = listOf("water", "osmosis", "turgor pressure", "deposition", "permanent"),
            partialCreditKeywords = listOf("temporary", "cellulose", "lignin", "cutin"),
            modelAnswer = "Because physiological support depends entirely on the presence of water entering the sap vacuole by osmosis to generate turgor pressure, disappearing when water is lost during transpiration. Structural support depends on the permanent chemical deposition of tough solid substances (cellulose, lignin, cutin, suberin) on cell walls, which remains unchanged regardless of water availability.",
            remediationNotes = "Contrast osmotic dependency vs physical substance deposition."
        )
    )

    // Hormonal Coordination
    val packHormonalCoordination = MasterStudyPack(
        topicId = "hormonal_coordination_endocrine",
        topicTitle = "Pituitary Master Gland & Hormone Feedback",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "Chapter 2: Hormonal Coordination",
        eli5Analogy = "The Pituitary Gland is the CEO of the endocrine corporation. It sends managerial memos (TSH, ACTH, FSH/LH) to branch managers (Thyroid, Adrenal, Gonads) ordering them to produce products. If branch output is too high, the CEO senses it and stops sending memos (Negative Feedback) so the company never overheats!",
        deepTechnicalBreakdown = """
### 1. The Pituitary Gland (Master Gland / Hypophysis)
- Connected to the hypothalamus via the infundibulum (pituitary stalk).
- **Adenohypophysis (Anterior & Intermediate Lobes)** - True endocrine gland:
  - **GH (Growth Hormone)**: Protein synthesis and bone elongation. Hypersecretion in childhood = Gigantism; Hyposecretion in childhood = Dwarfism; Hypersecretion in adults = **Acromegaly** (renewal of growth of distal parts of bones: hands, feet, lower jaw).
  - **TSH (Thyroid Stimulating Hormone)**: Stimulates thyroid gland.
  - **ACTH (Adrenocorticotropic Hormone)**: Stimulates adrenal cortex.
  - **FSH & LH**: Gonadotropins controlling gametogenesis and sex hormones.
  - **Prolactin**: Stimulates milk production in mammary glands.
- **Neurohypophysis (Posterior Lobe & Infundibulum)** - Stores neurohormones produced by hypothalamic neurosecretory cells:
  - **ADH (Antidiuretic Hormone / Vasopressin)**: Reabsorbs water from kidney nephrons (collecting ducts), decreasing urine volume, increasing urine concentration, and raising blood pressure by constricting blood vessels.
  - **Oxytocin**: Causes strong uterine contractions during childbirth and ejection of milk during lactation.

### 2. Blood Calcium Homeostasis (Thyroid vs Parathyroid)
- **Calcitonin** (Thyroid gland): Lowers blood Ca²⁺ when high by inhibiting bone resorption and depositing Ca²⁺ into bones.
- **Parathormone** (Parathyroid glands): Increases blood Ca²⁺ when low by withdrawing Ca²⁺ from bones, increasing kidney reabsorption and intestinal absorption.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Acromegaly", "Condition caused by hypersecretion of Growth Hormone in adults characterized by enlargement of distal bones.", "الأكروميجالي"),
            KeyTerm("ADH (Vasopressin)", "Neurohormone from hypothalamus/posterior pituitary that increases water reabsorption in nephrons and raises blood pressure.", "الهرمون المضاد لإدرار البول"),
            KeyTerm("Calcitonin", "Thyroid hormone that decreases blood calcium levels and deposits it into bones.", "الكالسيتونين"),
            KeyTerm("Parathormone", "Parathyroid hormone that increases blood calcium levels by pulling it from bones.", "الباراثورمون")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Blood Calcium Balance Equation",
                formulaLatex = "\\text{Normal Ca}^{2+} = [9.0 - 11.0]\\text{ mg/dL}",
                description = "High Ca²⁺ triggers Calcitonin; Low Ca²⁺ triggers Parathormone.",
                variables = "Calcitonin (lowers Ca²⁺) vs Parathormone (raises Ca²⁺)",
                sampleCalculation = "If blood Ca²⁺ drops to 7.0 mg/dL: Parathormone surges to pull calcium from bones."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "Calcitonin vs Parathormone",
                columnA = "Calcitonin (Thyroid)",
                columnB = "Parathormone (Parathyroids)",
                rows = listOf(
                    ComparisonRow("Trigger", "High blood calcium level", "Low blood calcium level"),
                    ComparisonRow("Action on Blood Calcium", "Decreases blood Ca²⁺ level", "Increases blood Ca²⁺ level"),
                    ComparisonRow("Action on Bone", "Deposits calcium into bone matrix", "Withdraws calcium from bones into blood")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "Name the hormones secreted by the posterior pituitary lobe.", "Which two neurohormones are released by the posterior pituitary? (ADH & Oxytocin)"),
            BloomLevelItem("Understanding", 2, "Explain how ADH affects blood pressure and urine volume.", "Why does ADH increase blood pressure while decreasing urine volume?"),
            BloomLevelItem("Applying", 3, "Predict hormone levels in a patient suffering from Acromegaly.", "What hormone is elevated in an adult showing enlarged jaw and hands? (GH)"),
            BloomLevelItem("Analyzing", 4, "Analyze what happens when parathyroid glands are accidentally removed in thyroidectomy.", "What clinical symptoms emerge when parathyroid glands are excised?"),
            BloomLevelItem("Evaluating", 5, "Evaluate negative feedback loops regulating TSH and Thyroxine.", "Explain how elevated thyroxine turns off pituitary TSH secretion."),
            BloomLevelItem("Creating", 6, "Design an emergency treatment protocol for severe hypocalcemic tetany.", "Formulate an intervention supplying intravenous calcium gluconate.")
        ),
        asciiSchematic = """
============== CALCIUM HOMEOSTASIS FEEDBACK LOOP ==============
                     [Normal Blood Ca²⁺ Level]
                     /                       \
        [Ca²⁺ High] /                         \ [Ca²⁺ Low]
                   v                           v
            (Thyroid Gland)            (Parathyroid Glands)
                   |                           |
            [ CALCITONIN ]              [ PARATHORMONE ]
                   |                           |
                   v                           v
          Deposits Ca²⁺ in bones      Pulls Ca²⁺ from bones
                   \                           /
                    ---> Return to Normal <---
        """.trimIndent(),
        schematicCaption = "Antagonistic regulation of blood calcium: Calcitonin decreases blood Ca²⁺, Parathormone increases blood Ca²⁺.",
        flowcharts = listOf(
            FlowchartStep(1, "Blood Osmolarity rises (Dehydration)", "Hypothalamic osmoreceptors stimulated", "Dehydration"),
            FlowchartStep(2, "Posterior Pituitary releases ADH", "ADH travels in bloodstream to kidneys", "ADH release"),
            FlowchartStep(3, "Nephron collecting ducts increase permeability", "Water reabsorbed into blood; blood pressure restored", "Water retention")
        ),
        visualInspectionMarkers = listOf(
            "Pituitary diagram: Anterior lobe connects to blood vessels; Posterior lobe connects directly to hypothalamic neurons.",
            "Thyroid vs Parathyroids: Thyroid is 2 lobes on anterior trachea; Parathyroids are 4 small round glands on posterior surface."
        ),
        youtube3DQueries = listOf(
            VideoQuery("Endocrine System Pituitary Gland Feedback Loops 3D", "Detailed exploration of pituitary hormones and target endocrine organs.", "4-6 mins")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "q_horm_1",
                type = QuestionType.SCIENTIFIC_TERM,
                question = "Write the scientific term: The hormone whose hypersecretion in adults causes enlargement of the distal parts of bones in hands, feet, and lower jaw.",
                officialAnswer = "Growth Hormone (GH) / Acromegaly condition (هرمون النمو)",
                rationale = "Adult GH hypersecretion causes acromegaly.",
                ministryExamTip = "Specify Growth Hormone."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "Thinking the posterior pituitary produces ADH and Oxytocin.",
                scientificTruth = "The posterior pituitary DOES NOT SYNTHESIZE ANY HORMONES! ADH and Oxytocin are synthesized by neurosecretory cells in the HYPOTHALAMUS and only stored and released by the posterior pituitary.",
                examTrapWarning = "Ministry trap: 'Where is ADH synthesized?' Answer is Hypothalamus, NOT posterior pituitary!",
                goldenRule = "Hypothalamus synthesizes; Posterior lobe stores and releases."
            )
        ),
        flashcards = listOf(
            Flashcard("fc_horm_1", "hormonal_coordination_endocrine", "Where are ADH and Oxytocin synthesized?", "In neurosecretory cells of the Hypothalamus.", "Origins"),
            Flashcard("fc_horm_2", "hormonal_coordination_endocrine", "What gland secretes Calcitonin?", "The Thyroid gland.", "Calcium")
        ),
        examSimulationItem = MinistryExamItem(
            id = "sim_horm",
            questionType = QuestionType.GIVE_REASON,
            prompt = "Give Reason: ADH is also known as Vasopressin.",
            maxScore = 2.0,
            requiredKeywords = listOf("constricts", "blood vessels", "smooth muscle", "blood pressure", "vasoconstrictor"),
            partialCreditKeywords = listOf("arterioles", "kidney"),
            modelAnswer = "Because in addition to increasing water reabsorption in kidney tubules, ADH acts on the smooth muscles of the walls of arterioles, causing vasoconstriction, which directly raises systemic blood pressure.",
            remediationNotes = "Explain the vasoconstrictor action raising blood pressure."
        )
    )

    // Reproduction: Double fertilization & Alternation of generations
    val packReproductionDoubleFertilization = MasterStudyPack(
        topicId = "reproduction_double_fertilization",
        topicTitle = "Plant Reproduction & Alternation of Generations",
        unit = UnitCategory.UNIT_1,
        chapterTitle = "Chapter 3: Reproduction in Living Organisms",
        eli5Analogy = "In flowering plants, reproduction is a 2-in-1 combo meal (Double Fertilization). One sperm marries the egg to make the baby plant (Zygote 2n), while the second sperm marries TWO twin sisters (Polar nuclei) to bake a nutritious 3-course pantry (Endosperm 3n) to feed the baby as it grows inside the seed!",
        deepTechnicalBreakdown = """
### 1. Alternation of Generations (تعاقب الأجيال)
- Phenomenon where an organism alternates between a sexual generation reproducing by gametes and an asexual generation reproducing by spores within one life cycle.
- **Polypodium Fern**: Sporophyte (2n) produces spores (n) by meiosis inside sporangia on the lower surface of leaves. Spore germinates into heart-shaped Gametophyte (n) bearing Antheridia (male) and Archegonia (female). Fertilization produces new Sporophyte (2n).
- **Plasmodium malariae**:
  - Sexual reproduction in **Anopheles mosquito stomach**: Gametocytes unite to form Zygote (2n), becomes mobile **Ookinete (2n)**, penetrates stomach wall, divides by meiosis into **Oocyst (n)**, divides by sporogony into **Sporozoites (n)**.
  - Asexual reproduction in **Human host**: Sporozoites enter liver cells and divide by schizogony into **Merozoites (n)**. Merozoites infect Red Blood Cells, dividing repeatedly by schizogony and releasing toxins that cause malaria fever symptoms (fever, chills, sweating) every 48 hours.

### 2. Double Fertilization in Angiosperms (Flowering Plants)
- Pollen grain germinates on stigma, pollen tube penetrates micropyle carrying **2 Male Nuclei (n)**.
1. **Syngamy**: 1st male nucleus (n) fertilizes egg cell (n) -> **Zygote (2n)** -> develops into Embryo.
2. **Triple Fusion (الاندماج الثلاثي)**: 2nd male nucleus (n) fuses with the **Two Polar Nuclei (n + n)** in the embryo sac -> **Endosperm Nucleus (3n)** -> divides to form nutritive **Endosperm tissue (3n)**.
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Triple Fusion", "Fusion of the second male nucleus (n) with the two polar nuclei (2n) to form the triploid endosperm nucleus (3n).", "الاندماج الثلاثي"),
            KeyTerm("Sporozoite", "Infective stage of Plasmodium malariae transmitted by Anopheles mosquito saliva into human blood.", "السبوروزويت"),
            KeyTerm("Ookinete", "Elongated mobile zygote (2n) of Plasmodium that penetrates the mosquito stomach wall.", "الأوكينيت / الطور الحركي")
        ),
        formulas = listOf(
            FormulaItem(
                name = "Endosperm Ploidy Formula",
                formulaLatex = "n + n + n = 3n",
                description = "Triploid tissue formed from 1 male nucleus + 2 polar nuclei.",
                variables = "Endosperm = 3n; Zygote = 2n",
                sampleCalculation = "If plant has 2n = 24 chromosomes: Male nucleus n = 12. Endosperm 3n = 3 × 12 = 36 chromosomes."
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "Sporophyte vs Gametophyte in Polypodium",
                columnA = "Sporophyte (2n)",
                columnB = "Gametophyte (n)",
                rows = listOf(
                    ComparisonRow("Ploidy", "Diploid (2n)", "Haploid (n)"),
                    ComparisonRow("Reproduction Mode", "Asexual by spores (via meiosis)", "Sexual by gametes (via mitosis)"),
                    ComparisonRow("Structure", "Stem, roots, and compound leaves with sori", "Heart-shaped prothallus with rhizoids, antheridia, and archegonia")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "What is the infective stage of malaria to humans?", "Sporozoites (n)."),
            BloomLevelItem("Understanding", 2, "Explain why double fertilization is unique to flowering plants.", "Why is endosperm formation (3n) coupled with embryo formation (2n)?"),
            BloomLevelItem("Applying", 3, "Calculate chromosome number in endosperm if leaf cell has 18 chromosomes.", "Leaf cell = 2n = 18 → n = 9 → Endosperm 3n = 27 chromosomes."),
            BloomLevelItem("Analyzing", 4, "Analyze why malaria fever symptoms occur every 48 hours.", "Why do chills and fever coincide with RBC rupture?"),
            BloomLevelItem("Evaluating", 5, "Evaluate the biological significance of alternation of generations.", "Why combine benefits of sexual (diversity) and asexual (speed) reproduction?"),
            BloomLevelItem("Creating", 6, "Map the complete life cycle of Plasmodium from mosquito bite to transmission.", "Diagram host transitions and ploidy changes.")
        ),
        asciiSchematic = """
================ DOUBLE FERTILIZATION IN EMBRYO SAC ================
 Pollen Tube delivers 2 Male Nuclei (n, n) through Micropyle:

     Male Nucleus (n) + Egg Cell (n) ========> Zygote (2n) ===> Embryo

     Male Nucleus (n) + 2 Polar Nuclei (2n) => Endosperm (3n) => Nutritive Tissue
        """.trimIndent(),
        schematicCaption = "Double fertilization: Syngamy produces diploid embryo (2n); Triple fusion produces triploid endosperm (3n).",
        flowcharts = listOf(
            FlowchartStep(1, "Mosquito injects Sporozoites into blood", "Travels to human liver", "Sporozoites (n)"),
            FlowchartStep(2, "Schizogony in liver produces Merozoites", "Merozoites enter Red Blood Cells", "Merozoites (n)"),
            FlowchartStep(3, "RBCs rupture every 48 hours", "Releases toxins causing fever, chills, sweating", "Toxins released"),
            FlowchartStep(4, "Gametocytes taken up by mosquito", "Fertilization in mosquito stomach forms Zygote (2n)", "Mosquito bite"),
            FlowchartStep(5, "Ookinete penetrates stomach wall", "Undergoes meiosis forming Oocyst (n) and Sporozoites", "Sporogony")
        ),
        visualInspectionMarkers = listOf(
            "Identify Plasmodium stage: Ookinete is vermiform/elongated (2n); Oocyst is round on mosquito stomach (n).",
            "Identify Embryo sac cells: 3 antipodal cells at chalaza, 2 synergids + 1 egg at micropyle, 2 polar nuclei in center."
        ),
        youtube3DQueries = listOf(
            VideoQuery("Double Fertilization in Angiosperms 3D Animation", "Watch pollen tube penetration and triple fusion in 3D.", "3-5 mins")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "q_df_1",
                type = QuestionType.SCIENTIFIC_TERM,
                question = "Write the scientific term: The fusion between one male nucleus (n) and the two polar nuclei (2n) in the embryo sac of flowering plants.",
                officialAnswer = "Triple Fusion (الاندماج الثلاثي)",
                rationale = "Forms the triploid endosperm nucleus (3n).",
                ministryExamTip = "Spelled Triple Fusion."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "Thinking Plasmodium gametocytes fertilize inside human blood.",
                scientificTruth = "Fertilization NEVER occurs in human blood! Human blood temperature and acidity prevent it. Fertilization occurs strictly inside the female Anopheles mosquito's stomach cavity.",
                examTrapWarning = "Ministry trap: 'Where does sexual reproduction of Plasmodium take place?' Answer is Mosquito Stomach, NOT human blood!",
                goldenRule = "Sexual reproduction in mosquito; Asexual in human."
            )
        ),
        flashcards = listOf(
            Flashcard("fc_df_1", "reproduction_double_fertilization", "What is the chromosomal number (ploidy) of the endosperm tissue?", "Triploid (3n).", "Botany"),
            Flashcard("fc_df_2", "reproduction_double_fertilization", "What Plasmodium stage penetrates the mosquito stomach wall?", "The Ookinete (2n).", "Parasitology")
        ),
        examSimulationItem = MinistryExamItem(
            id = "sim_df",
            questionType = QuestionType.GIVE_REASON,
            prompt = "Give Reason: The symptoms of malaria (chills, high fever, and profuse sweating) appear in the patient every two days.",
            maxScore = 2.0,
            requiredKeywords = listOf("merozoites", "red blood cells", "rupture", "toxins", "every two days"),
            partialCreditKeywords = listOf("48 hours", "schizogony", "fever"),
            modelAnswer = "Because merozoites complete their asexual cycle of schizogony inside infected red blood cells every 48 hours (two days). At the end of each cycle, massive numbers of red blood cells rupture simultaneously, releasing swarms of new merozoites and toxic metabolic wastes into the bloodstream, triggering the classic paroxysms of fever, chills, and sweating.",
            remediationNotes = "Correlate symptoms with the periodic rupture of red blood cells releasing toxins."
        )
    )

    // Genetic Engineering & Recombinant DNA
    val packGeneticEngineering = MasterStudyPack(
        topicId = "genetic_engineering_recombinant",
        topicTitle = "Genetic Engineering & Recombinant DNA",
        unit = UnitCategory.UNIT_2,
        chapterTitle = "Chapter 2: RNA & Protein Synthesis",
        eli5Analogy = "Genetic engineering is molecular cut-and-paste. Restriction Endonucleases are molecular scissors that only cut at specific palindrome passwords (Sticky Ends). DNA Ligase is molecular tape. Plasmids are postal envelopes that deliver the new gene into bacterial factories to print out life-saving insulin at massive scale!",
        deepTechnicalBreakdown = """
### 1. Recombinant DNA Tools
- **Restriction Endonucleases (إنزيمات القصر)**: Discovered in bacteria as defense against bacteriophages.
  - Recognize specific palindromic sequences (4 to 7 base pairs) that read identically in the 5' -> 3' direction on both strands (e.g. 5'-GAATTC-3').
  - Cut the sugar-phosphate backbone between specific bases, leaving single-stranded staggered ends called **Sticky Ends (أطراف لاصقة)**.
- **Bacterial Modification Enzymes (إنزيمات التعديل)**: Add protective methyl groups (-CH3) to the bacterium's own DNA recognition sites so its restriction enzymes don't digest its own chromosome!
- **DNA Ligase**: Connects complementary sticky ends by forming phosphodiester bonds.
- **Reverse Transcriptase (إنزيم النسخ العكسي)**: Extracted from retroviruses (RNA viruses). Transcribes RNA into complementary single-stranded DNA (cDNA).
- **Cloning Vectors**: Plasmids or bacteriophages that carry recombinant DNA into host bacterial cells (e.g. *E. coli*).
- **PCR (Polymerase Chain Reaction)**: Rapid in vitro gene amplification using the **Taq Polymerase enzyme** (isolated from thermophilic bacteria) in a thermocycler machine at elevated temperatures.
- **DNA Hybridization**: Measuring degree of evolutionary kinship between species by heating mixed DNA strands and observing melting temperature (Tm).
        """.trimIndent(),
        keyTerms = listOf(
            KeyTerm("Restriction Endonuclease", "Bacterial enzyme that cuts DNA at specific palindromic recognition sites, leaving sticky ends.", "إنزيم القصر / التحديد"),
            KeyTerm("Reverse Transcriptase", "Viral enzyme that synthesizes DNA from an RNA template.", "إنزيم النسخ العكسي"),
            KeyTerm("Taq Polymerase", "Heat-stable DNA polymerase used in PCR thermocyclers to replicate DNA at high temperatures.", "إنزيم تاك بوليميريز"),
            KeyTerm("Sticky Ends", "Single-stranded overhangs produced by restriction enzyme digestion that readily hydrogen-bond with complementary ends.", "الأطراف اللاصقة")
        ),
        formulas = listOf(
            FormulaItem(
                name = "PCR Amplification Formula",
                formulaLatex = "N = N_0 \\times 2^n",
                description = "Each thermal cycle doubles the number of DNA copies.",
                variables = "N_0 = Initial copies; n = Number of PCR cycles",
                sampleCalculation = "Starting with 1 DNA molecule after 30 cycles: N = 1 × 2^30 ≈ 1.07 billion copies!"
            )
        ),
        comparisonTables = listOf(
            ComparisonTable(
                title = "Restriction Endonucleases vs Modification Enzymes",
                columnA = "Restriction Enzymes",
                columnB = "Modification Enzymes",
                rows = listOf(
                    ComparisonRow("Function", "Cuts foreign viral DNA at recognition sites", "Adds methyl (-CH3) groups to bacterial DNA sites"),
                    ComparisonRow("Target", "Foreign viral DNA", "Host bacterial chromosome"),
                    ComparisonRow("Biological Role", "Destroys viral invader", "Protects host bacterial genome from self-digestion")
                )
            )
        ),
        bloomProgression = listOf(
            BloomLevelItem("Remembering", 1, "What enzyme synthesizes cDNA from mRNA?", "Reverse Transcriptase."),
            BloomLevelItem("Understanding", 2, "Explain why sticky ends are essential in recombinant DNA technology.", "Why are staggered ends preferable to blunt ends for gene insertion?"),
            BloomLevelItem("Applying", 3, "Identify palindromic sequence among DNA sequences.", "Identify which is palindromic: 5'-GAATTC-3' or 5'-GATACA-3'."),
            BloomLevelItem("Analyzing", 4, "Compare obtaining a gene using mRNA vs isolating it from a whole genome.", "Why is using mRNA and reverse transcriptase preferred for human insulin production in bacteria?"),
            BloomLevelItem("Evaluating", 5, "Evaluate how DNA hybridization determines evolutionary relationships.", "How does the melting temperature of hybrid DNA reflect evolutionary divergence?"),
            BloomLevelItem("Creating", 6, "Design a step-by-step protocol to clone the human growth hormone gene.", "Map mRNA extraction, reverse transcription, plasmid ligation, and transformation.")
        ),
        asciiSchematic = """
================ RESTRICTION ENZYME PALINDROME CUT ================
 Recognition Site (e.g., EcoRI):
   5' --- G | A  A  T  T  C --- 3'
   3' --- C  T  T  A  A | G --- 5'
               |
          Cuts Backbone
               v
   5' --- G                  A  A  T  T  C --- 3'
   3' --- C  T  T  A  A                  G --- 5'
         [Sticky End]      [Sticky End]
        """.trimIndent(),
        schematicCaption = "Restriction cleavage: Leaves complementary single-stranded 'Sticky Ends' that allow foreign genes to anneal seamlessly.",
        flowcharts = listOf(
            FlowchartStep(1, "Isolate mRNA of desired gene", "e.g. from beta cells of pancreas for insulin", "mRNA isolation"),
            FlowchartStep(2, "Reverse Transcriptase builds cDNA", "Converts mRNA into single-stranded DNA, then DNA polymerase makes double strand", "Reverse Transcriptase"),
            FlowchartStep(3, "Cut Plasmid and Gene with same Restriction Enzyme", "Produces identical complementary sticky ends", "Restriction Enzyme"),
            FlowchartStep(4, "DNA Ligase joins gene into plasmid", "Forms recombinant plasmid vector", "DNA Ligase"),
            FlowchartStep(5, "Transform into host bacteria (E. coli)", "Bacteria replicates and synthesizes human insulin", "Bacterial transformation")
        ),
        visualInspectionMarkers = listOf(
            "Look for Palindromic sequence: Reads identically in 5' to 3' on both complementary strands.",
            "Verify same restriction enzyme used on both gene and plasmid vector to ensure matching sticky ends."
        ),
        youtube3DQueries = listOf(
            VideoQuery("Recombinant DNA Technology Process 3D Animation", "Watch restriction cutting, plasmid insertion, and transformation in 3D.", "4-5 mins")
        ),
        questions = listOf(
            PracticeQuestion(
                id = "q_ge_1",
                type = QuestionType.SCIENTIFIC_TERM,
                question = "Write the scientific term: The viral enzyme used by geneticists to synthesize complementary DNA (cDNA) from an mRNA template.",
                officialAnswer = "Reverse Transcriptase enzyme (إنزيم النسخ العكسي)",
                rationale = "Found in retroviruses like HIV, used in genetic engineering.",
                ministryExamTip = "Spelled Reverse Transcriptase."
            ),
            PracticeQuestion(
                id = "q_ge_2",
                type = QuestionType.GIVE_REASON,
                question = "Give reason: In producing human insulin using bacteria, geneticists prefer isolating mRNA and using reverse transcriptase rather than isolating the gene directly from the human chromosome.",
                officialAnswer = "Because human chromosomal genes contain non-coding sequences (introns). Bacteria lack the splicing machinery to remove introns. Isolating mature mRNA from pancreatic beta cells provides a pre-spliced sequence containing only coding exons, ensuring bacteria synthesize functional insulin.",
                rationale = "Bacterial cells cannot splice eukaryotic introns.",
                ministryExamTip = "Mention that bacteria cannot remove introns / non-coding sequences."
            )
        ),
        examTraps = listOf(
            ExamTrap(
                misconception = "Thinking any restriction enzyme can join with any cut plasmid.",
                scientificTruth = "You MUST use the EXACT SAME restriction endonuclease on both the plasmid vector and foreign gene! Otherwise, sticky ends will not have complementary base sequences and ligase cannot join them.",
                examTrapWarning = "Ministry trap: 'Why was the experiment unsuccessful when EcoRI cut the gene but BamHI cut the plasmid?' Because sticky ends are not complementary!",
                goldenRule = "Same enzyme = Complementary sticky ends."
            )
        ),
        flashcards = listOf(
            Flashcard("fc_ge_1", "genetic_engineering_recombinant", "What protects bacterial DNA from digestion by its own restriction enzymes?", "Bacterial Modification enzymes adding methyl (-CH3) groups.", "Defense"),
            Flashcard("fc_ge_2", "genetic_engineering_recombinant", "What heat-stable enzyme is used in PCR amplification?", "Taq Polymerase.", "Biotech")
        ),
        examSimulationItem = MinistryExamItem(
            id = "sim_ge",
            questionType = QuestionType.GIVE_REASON,
            prompt = "Give Reason: The bacterium's own DNA is not digested by its own restriction endonucleases.",
            maxScore = 2.0,
            requiredKeywords = listOf("modification enzymes", "methyl", "recognition sites", "protect", "incompatible"),
            partialCreditKeywords = listOf("bacterial chromosome", "resistance"),
            modelAnswer = "Because the bacterium produces Modification Enzymes that add methyl groups (-CH3) to the nitrogenous bases within the recognition sites on its own chromosome, masking the sites and preventing the restriction endonucleases from recognizing and cutting its own DNA.",
            remediationNotes = "Explain the action of modification enzymes adding methyl groups."
        )
    )
}
