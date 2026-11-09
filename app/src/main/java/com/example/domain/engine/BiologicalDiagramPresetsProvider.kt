package com.example.domain.engine

import com.example.domain.model.DiagramPreset

object BiologicalDiagramPresetsProvider {

    private val presetsByTopic = mapOf(
        "sarcomere_contraction" to listOf(
            DiagramPreset(
                id = "p_sarcomere_states",
                topicId = "sarcomere_contraction",
                titleAr = "القطعة العضلية عند الانقباض مقابل الانبساط",
                titleEn = "Sarcomere in Relaxation vs Contraction",
                prompt = "Detailed labeled schematic comparing relaxed sarcomere vs contracted sarcomere. Show Z-lines approaching each other, I-band narrowing, H-zone disappearing during maximal contraction, A-band constant length, actin and myosin filaments.",
                keyLandmarks = listOf(
                    "خط Z (Z-Line): يحدد طرفي القطعة العضلية، يقترب من Z المجاور عند الانقباض",
                    "المنطقة المضيئة I (I-Band): خيوط أكتين فقط، يقل طولها",
                    "المنطقة الداكنة A (A-Band): خيوط أكتين + ميوسين، طولها ثابت دائماً",
                    "المنطقة شبه المضيئة H (H-Zone): خيوط ميوسين فقط، يقل طولها أو تنعدم عند الانقباض التام"
                ),
                examTrapWarning = "احذر: طول خيوط الأكتين والميوسين أنفسهم لا يتغير إطلاقاً، وكذلك طول المنطقة الداكنة A لا يتغير!",
                goldenRule = "طول A ثابت دايماً • طول I و H و Sarcomere بيقلوا عند الانقباض"
            ),
            DiagramPreset(
                id = "p_neuromuscular_junction",
                topicId = "sarcomere_contraction",
                titleAr = "التشابك العصبي العضلي ومستقبلات الأسيتيل كولين",
                titleEn = "Neuromuscular Junction (NMJ) Synapse",
                prompt = "Diagram of neuromuscular junction motor end plate. Axon terminal with synaptic vesicles containing acetylcholine (ACh), voltage-gated calcium channels, synaptic cleft, sarcolemma receptors, and cholinesterase enzyme.",
                keyLandmarks = listOf(
                    "مضخات الكالسيوم Ca²⁺: تسمح بتدفق الكالسيوم لتفجير حويصلات النواقل الكيميائية",
                    "حويصلات الأسيتيل كولين (ACh): تفرغ محتواها في الشق التشابكي",
                    "غشاء الليفة العضلية (صفيحة نهائية حركية): يحتوي على مستقبلات ACh التي تفتح قنوات الصوديوم",
                    "إنزيم كولين استريز: يحطم الأسيتيل كولين إلى كولين وحمض خليك لإعادة الاستقطاب"
                ),
                examTrapWarning = "غياب الكالسيوم في النهاية العصبية يمنع انطلاق الأسيتيل كولين فلا يحدث انقباض، بينما غيابه داخل الليفة العضلية يمنع تكوين الروابط المستعرضة!",
                goldenRule = "كالسيوم العصب = تفجير حويصلات • كالسيوم العضلة = خطاطيف وروابط مستعرضة"
            )
        ),
        "plant_human_support" to listOf(
            DiagramPreset(
                id = "p_vertebra_anatomy",
                topicId = "plant_human_support",
                titleAr = "الفقرة العظمية النموذجية والنتوءات المفصلية",
                titleEn = "Typical Lumbar Vertebra Anatomy",
                prompt = "Superior anatomical diagram of typical human vertebra. Showing thick centrum, neural canal, transverse processes, neural arch, spinous process, and articulating processes.",
                keyLandmarks = listOf(
                    "جسم الفقرة (Centrum): الجزء الأمامي السميك الحامل للوزن",
                    "القناة العصبية (Neural Canal): يمر من خلالها الحبل الشوكي لحمايته",
                    "النتوءان المستعرضان (Transverse Processes): زائدتان عظميتان على الجانبين",
                    "النتوءات المفصلية الأمامية والخلفية: للتمفصل مع الفقرات التي تعلوها والتي تليها"
                ),
                examTrapWarning = "الفقرة تتمفصل مع الفقرة السابقة بنتوءين مفصليين أماميين، ومع الفقرة اللاحقة بنتوءين مفصليين خلفيين!",
                goldenRule = "المفصلي الأمامي للأعلى • المفصلي الخلفي للأسفل"
            ),
            DiagramPreset(
                id = "p_knee_ligaments",
                topicId = "plant_human_support",
                titleAr = "أربطة مفصل الركبة (الصليبي والجانبي)",
                titleEn = "Knee Joint Ligaments & Tendons",
                prompt = "Anterior view of human knee joint showing femur, tibia, fibula, patella, anterior cruciate ligament (ACL), posterior cruciate ligament (PCL), medial collateral ligament, lateral collateral ligament.",
                keyLandmarks = listOf(
                    "الرباط الصليبي الأمامي والخلفي: يربطان عظمة الفخذ بعظمة القصبة على شكل حرف X",
                    "الرباط الوسطي (Medial): يربط الفخذ بالقصبة",
                    "الرباط الجانبي (Lateral): يربط الفخذ بالشظية (الوحيد المتصل بالشظية)"
                ),
                examTrapWarning = "عدد الأربطة التي تربط الفخذ بالقصبة 3، بينما عدد الأربطة التي تربط الفخذ بالشظية 1 فقط (الرباط الجانبي)!",
                goldenRule = "فخذ + قصبة = 3 أربطة • فخذ + شظية = رباط واحد"
            )
        ),
        "nutrition_digestion" to listOf(
            DiagramPreset(
                id = "p_chloroplast_diagram",
                topicId = "nutrition_digestion",
                titleAr = "البلاستيدة الخضراء وتفاعلات البناء الضوئي",
                titleEn = "Chloroplast Structure & Photosynthesis",
                prompt = "Cross section diagram of plant chloroplast showing double membrane, stroma, thylakoid discs organized into grana stacks, chlorophyll pigments, light reactions in thylakoids and dark Calvin cycle in stroma.",
                keyLandmarks = listOf(
                    "أقراص الجرانة (Grana): موقع امتصاص الضوء وشطر الماء وإنتاج ATP و NADPH",
                    "أرضية الستروما (Stroma): موقع التفاعلات اللاضوئية واختزال CO₂ لتكوين الجلوكوز",
                    "جزيئات DNA والريبوسومات وحبيبات النشا في البلاستيدة"
                ),
                examTrapWarning = "مصدر الأكسجين المتصاعد في البناء الضوئي هو شطر الماء (H₂O) وليس ثاني أكسيد الكربون (تجربة فان نيل وكاليفورنيا)!",
                goldenRule = "أكسجين الهواء من الماء • كربون وهيدروجين الجلوكوز من CO₂ و NADPH"
            ),
            DiagramPreset(
                id = "p_villus_absorption",
                topicId = "nutrition_digestion",
                titleAr = "الخملة المعوية والطريقان اللمفاوي والدموي",
                titleEn = "Intestinal Villus Anatomy & Absorption Routes",
                prompt = "Cross section of intestinal villus showing microvilli epithelium, lacteal lymphatic vessel, blood capillary network, and direction of nutrient absorption.",
                keyLandmarks = listOf(
                    "الوعاء اللبني / اللمفاوي: يمتص الأحماض الدهنية والجلسرين وفيتامينات (K, E, D, A)",
                    "الشعيرات الدموية: تمتص السكريات الأحادية، الأحماض الأمينية، الماء والأملاح",
                    "الوريد البابي الكبدي: يمر به الطريق الدموي متجهاً للكبد"
                ),
                examTrapWarning = "الفيتامينات الذائبة في الدهون تذهب عبر الوعاء اللبني ثم القناة اللمفاوية وتصب في الوريد الأجوف العلوي دون المرور بالكبد!",
                goldenRule = "الدهون واللمف = وريد أجوف علوي • السكريات والبروتين = كبد ثم وريد أجوف سفلي"
            )
        ),
        "transport_circulation" to listOf(
            DiagramPreset(
                id = "p_heart_cardiac_cycle",
                topicId = "transport_circulation",
                titleAr = "غرف القلب الأربع والصمامات والعقدة الجيب أذينية",
                titleEn = "Human Heart Anatomy & Conduction System",
                prompt = "Coronal cross section diagram of human heart. Right and left atria, right and left ventricles, tricuspid valve, bicuspid mitral valve, aortic valve, pulmonary valve, sinoatrial SA node and AV node.",
                keyLandmarks = listOf(
                    "العقدة الجيب أذينية (SAN): منظم ضربات القلب ذاتي الإثارة في جدار الأذين الأيمن",
                    "الصمام ثلاثي الشرفات: بين الأذين الأيمن والبطين الأيمن",
                    "الصمام ثنائي الشرفات (المترالي): بين الأذين الأيسر والبطين الأيسر",
                    "جدار البطين الأيسر: أسمك جدار عضلي لضخ الدم المؤكسج لكافة أنحاء الجسم"
                ),
                examTrapWarning = "أعلى ضغط دم يكون في البطين الأيسر والشريان الأورطي لحظة انقباض البطينين، والصمام المترالي يمنع رجوع الدم للأذين الأيسر!",
                goldenRule = "الأذين الأيمن غير مؤكسج • البطين الأيسر أثخن جداراً ومؤكسج"
            )
        ),
        "cellular_respiration" to listOf(
            DiagramPreset(
                id = "p_mitochondria_atp",
                topicId = "cellular_respiration",
                titleAr = "الميتوكوندريا وسلسلة نقل الإلكترون (38 ATP)",
                titleEn = "Mitochondrion Structure & ATP Balance Sheet",
                prompt = "Detailed diagram of mitochondrion showing outer membrane, folded inner membrane cristae with electron transport chain ATP synthase complexes, matrix containing Krebs cycle enzymes.",
                keyLandmarks = listOf(
                    "الأعراف (Cristae): ثنيات الغشاء الداخلي لزيادة مساحة سطح التفاعلات وسلسلة نقل الإلكترون",
                    "حشوة الميتوكوندريا (Matrix): موقع تفاعلات دورة كريبس وأكسدة حمض البيروفيك",
                    "إنزيم تصنيع ATP (ATP Synthase): يعمل بواسطة تدرج البروتونات H⁺ عبر الأعراف"
                ),
                examTrapWarning = "المستقبل النهائي للإلكترونات في سلسلة نقل الإلكترون هو ذرة الأكسجين، ويتكون جزيء ماء (H₂O)!",
                goldenRule = "انشطار في السيتوسول = 2 ATP • كريبس ونقل إلكترون في الميتوكوندريا = 36 ATP"
            )
        ),
        "excretion_homeostasis" to listOf(
            DiagramPreset(
                id = "p_nephron_filtration",
                topicId = "excretion_homeostasis",
                titleAr = "النفرون الكلوي: محفظة بومان وإعادة الامتصاص",
                titleEn = "Nephron Filtration & Tubular Reabsorption",
                prompt = "Diagram of functional nephron unit. Bowman's capsule enclosing glomerulus, proximal convoluted tubule, descending and ascending loop of Henle, distal tubule, and collecting duct with blood capillary bed.",
                keyLandmarks = listOf(
                    "محفظة بومان (Bowman's Capsule): ترشيح فائق لكافة مكونات البلازما عدا خلايا الدم وبروتينات البلازما الكبيرة",
                    "الأنبوبة الملتفة القريبة: إعادة امتصاص كاملة للجلوكوز بالنقل النشط",
                    "القناة الجامعة (Collecting Duct): موقع تأثير هرمون ADH لسحب الماء لتقليل حجم البول"
                ),
                examTrapWarning = "وجود الجلوكوز أو البروتينات في البول يدل على خلل مرضي (سكر أو تلف في محفظة بومان)، فالإنسان السليم يعيد امتصاص 100% من الجلوكوز!",
                goldenRule = "ترشيح في بومان • امتصاص اختياري في الأنابيب"
            )
        ),
        "nervous_coordination" to listOf(
            DiagramPreset(
                id = "p_action_potential_wave",
                topicId = "nervous_coordination",
                titleAr = "الخلية العصبية وجهد الفاعلية (+30mV)",
                titleEn = "Neuron Anatomy & Action Potential Curve",
                prompt = "Motor neuron diagram with dendrites, soma, axon, myelin sheath, nodes of Ranvier, alongside a voltage graph showing resting potential (-70mV), depolarization (+30mV), and repolarization.",
                keyLandmarks = listOf(
                    "جهد الراحة (-70mV): السطح الخارجي موجب والداخلي سالب بفضل مضخة Na⁺/K⁺",
                    "إزالة الاستقطاب (+30mV): اندفاع أيونات الصوديوم Na⁺ للداخل بكثرة",
                    "غمد الميالين وعقد رانفييه: سرعة انتقال السيال عبر النقل الوثبي السريع"
                ),
                examTrapWarning = "قانون الكل أو لا شيء: المثير الضعيف لا يولد سيالاً، وزيادة قوة المثير فوق العتبة لا تزيد من سعة جهد الفاعلية!",
                goldenRule = "راحة = -70mV وخارج موجب • إثارة = +30mV وداخل موجب"
            )
        ),
        "dna_structure_replication" to listOf(
            DiagramPreset(
                id = "p_replication_fork",
                topicId = "dna_structure_replication",
                titleAr = "شوكة تضاعف الـ DNA والإنزيمات الثلاثة",
                titleEn = "DNA Replication Fork & Enzymes",
                prompt = "Molecular diagram of DNA replication fork. Helicase unzipping double helix, single-strand binding proteins, leading strand continuous 5' to 3' by DNA Polymerase, lagging strand with Okazaki fragments and DNA Ligase.",
                keyLandmarks = listOf(
                    "إنزيم اللولب (Helicase): يكسر الروابط الهيدروجينية ويفصل الشريطان",
                    "إنزيم بلمرة DNA: يبني الشريط الجديد في اتجاه '5 إلى '3 فقط",
                    "الشريط المتلكئ (Lagging Strand): يبنى في صورة قطع أوكازاكي",
                    "إنزيم الربط (DNA Ligase): يلحم قطع أوكازاكي ويثبت الروابط التساهمية الفوسفاتية"
                ),
                examTrapWarning = "إنزيم البلمرة لا يستطيع البدء ولا البناء في اتجاه '3 إلى '5؛ لذلك يحتاج الشريط الآخر لقطع أوكازاكي وإنزيمات ربط!",
                goldenRule = "البلمرة تعمل فقط في اتجاه '5 إلى '3 للشريط الجديد"
            ),
            DiagramPreset(
                id = "p_dna_nucleotide_bonds",
                topicId = "dna_structure_replication",
                titleAr = "تركيب النيوكليوتيدة وقواعد شارجاف (A=T و G≡C)",
                titleEn = "DNA Nucleotide Double Helix Chemical Bonds",
                prompt = "Chemical structure diagram of DNA double helix. Deoxyribose sugar, phosphate group at 5' carbon, nitrogenous bases A, T, G, C. Double hydrogen bonds between A and T, triple hydrogen bonds between G and C.",
                keyLandmarks = listOf(
                    "رابطة تساهمية: تربط السكر بمجموعة الفوسفات عند ذرة الكربون رقم 5",
                    "رابطتان هيدروجينيتان: بين الأدينين A والثايمين T (A=T)",
                    "ثلاث روابط هيدروجينية: بين الجوانين G والسيتوزين C (G≡C)",
                    "الشريطان متعاكسان: أحدهما في اتجاه 5' إلى 3' والآخر 3' إلى 5'"
                ),
                examTrapWarning = "كلما زادت نسبة (G+C) في عينة DNA زادت درجة الحرارة اللازمة لفصل الشريطين لأن الروابط ثلاثية وتتطلب طاقة أعلى!",
                goldenRule = "A = T (رابطتان) • G ≡ C (ثلاث روابط)"
            )
        ),
        "rna_transcription_translation" to listOf(
            DiagramPreset(
                id = "p_translation_ribosome",
                topicId = "rna_transcription_translation",
                titleAr = "الريبوسوم وتخليق البروتين وموقعي P و A",
                titleEn = "Ribosomal Translation & tRNA Peptidyl Transfer",
                prompt = "Diagram of active ribosome translating mRNA. Small and large subunits, mRNA strand with codons, P site holding peptidyl-tRNA, A site accepting incoming aminoacyl-tRNA, peptidyl transferase forming peptide bond.",
                keyLandmarks = listOf(
                    "تحت وحدة الريبوسوم الصغيرة: ترتبط بالـ mRNA في البداية",
                    "موقع الببتيديل (P-site): يحمل سلسلة عديد الببتيد النامية",
                    "موقع الأمينوأسيل (A-site): يستقبل الـ tRNA الحامل للحمض الأميني الجديد",
                    "إنزيم ببتيديل ترانسفيريز: جزء من تحت وحدة الريبوسوم الكبيرة يكون الروابط الببتيدية"
                ),
                examTrapWarning = "أول tRNA يحمل الميثيونين يدخل دائماً إلى موقع P مباشرة وليس إلى موقع A!",
                goldenRule = "كودون البدء AUG في موقع P • كودونات الاستطالة في موقع A"
            )
        ),
        "genetic_engineering_recombinant" to listOf(
            DiagramPreset(
                id = "p_recombinant_plasmid",
                topicId = "genetic_engineering_recombinant",
                titleAr = "إنزيمات القصر وتكوين DNA معاد الاتحاد",
                titleEn = "Restriction Endonucleases & Recombinant Plasmid",
                prompt = "Diagram of recombinant DNA engineering. Restriction enzyme cutting target gene and bacterial plasmid at palindromic site leaving complementary sticky ends, DNA ligase annealing them, transformation into bacterium.",
                keyLandmarks = listOf(
                    "موقع التعرف (Palindromic sequence): تتابع من 4-7 نيوكليوتيدات يقرأ من 5' إلى 3' في كلا الشريطين",
                    "الأطراف اللاصقة (Sticky Ends): أطراف مفردة مائلة تتكامل مع أي قطعة قُطعت بنفس الإنزيم",
                    "إنزيم الربط (Ligase): يربط الروابط التساهمية الفوسفاتية لدمج الجين بالبلازميد"
                ),
                examTrapWarning = "يجب استخدام نفس إنزيم القصر لقطع الجين والبلازميد معاً حتى تكون الأطراف اللاصقة متكاملة وتسمح بالتحام الجين!",
                goldenRule = "نفس إنزيم القصر = أطراف لاصقة متكاملة"
            )
        )
    )

    fun getPresetsForTopic(topicId: String): List<DiagramPreset> {
        val direct = presetsByTopic[topicId]
        if (!direct.isNullOrEmpty()) return direct

        // Fallback generic presets based on topic keywords
        return listOf(
            DiagramPreset(
                id = "p_gen_1",
                topicId = topicId,
                titleAr = "المخطط العلمي الشامل والمسار الوظيفي",
                titleEn = "Comprehensive Scientific Mechanism Diagram",
                prompt = "Detailed clear biological textbook schematic for $topicId. High contrast vector illustration with labeled arrows and key anatomical landmarks for Egyptian Thanaweya Amma.",
                keyLandmarks = listOf(
                    "المعالم التركيبية الأساسية للموضوع",
                    "اتجاه المسار الوظيفي والحيوي",
                    "مواقع التأثير والارتباط الكيميائي"
                ),
                examTrapWarning = "ركز في فحص بيانات الرسم: هل يشير السهم إلى التركيب أم العملية أم النواتج؟",
                goldenRule = "تأكد دائماً من مطابقة البيانات الرقمية مع المطلوب في السؤال"
            )
        )
    }
}
