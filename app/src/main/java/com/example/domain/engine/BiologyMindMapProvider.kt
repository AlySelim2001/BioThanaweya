package com.example.domain.engine

import com.example.domain.model.BiologicalMindMap
import com.example.domain.model.MindMapEdge
import com.example.domain.model.MindMapNode
import com.example.domain.model.MindMapNodeType

object BiologyMindMapProvider {

    private val mindMaps = mapOf(
        "sarcomere_contraction" to BiologicalMindMap(
            topicId = "sarcomere_contraction",
            titleArabic = "خريطة انقباض القطعة العضلية (نظرية هكسلي)",
            titleEnglish = "Sarcomere Contraction (Sliding Filament Theory)",
            centralQuestionArabic = "كيف يتحول السيال العصبي عبر أيونات الكالسيوم وATP إلى قوة ميكانيكية تُقصر القطعة العضلية؟",
            mermaidCode = """
                graph TD
                    A["عصبون حركي: سيال عصبي"] -->|تحرر أسيتيل كولين| B["إزالة استقطاب غشاء الليفة العضلية"]
                    B -->|انتقال عبر الأنيبيبات المستعرضة T-tubules| C["الشبكة الإندوبلازمية العضلية"]
                    C -->|ضخ أيونات Ca²⁺| D["ارتباط Ca²⁺ بالتروبونين"]
                    D -->|إزاحة التروبومايوسين| E["كشف مواقع الارتباط على الأكتين"]
                    E -->|تكوين الروابط المستعرضة بواسطة ATP| F["جذب خيوط الأكتين باتجاه المركز (خط M)"]
                    F -->|تقارب خطوط Z| G["انقباض القطعة العضلية SARCOMERE"]
                    G -.->|تحلل ATP الجديد| H["فصل الروابط المستعرضة والانبساط"]
                    style A fill:#004D40,stroke:#00796B,stroke-width:2px,color:#fff
                    style C fill:#311B92,stroke:#512DA8,stroke-width:2px,color:#fff
                    style G fill:#B71C1C,stroke:#D32F2F,stroke-width:3px,color:#fff
                    style H fill:#E65100,stroke:#F57C00,stroke-width:2px,color:#fff
            """.trimIndent(),
            nodes = listOf(
                MindMapNode("m1", "السيال الحركي وأسيتيل كولين", "Motor Action Potential", MindMapNodeType.ROOT, "وصول السيال لنهايات العصبون الحركي وتحرر الناقل الكيميائي في الشق التشابكي.", 0.5f, 0.10f),
                MindMapNode("m2", "تحرر أيونات Ca²⁺", "Calcium Ion Flux", MindMapNodeType.KEY_MOLECULE, "خروج الكالسيوم من الصهاريج العضلية وكشف مواقع ارتباط الميوسين على الأكتين.", 0.25f, 0.30f),
                MindMapNode("m3", "الروابط المستعرضة لـ هكسلي", "Cross-Bridge Hooking", MindMapNodeType.MECHANISM, "تمتد خطاطيف الميوسين بمساعدة ATP للارتباط بخيوط الأكتين المجاورة.", 0.75f, 0.30f),
                MindMapNode("m4", "انزلاق الأكتين وتقارب خطوط Z", "Filament Sliding & Z-lines", MindMapNodeType.MECHANISM, "سحب خيوط الأكتين نحو مركز القطعة، فيختفي النطاق H عند الانقباض التام.", 0.5f, 0.55f),
                MindMapNode("m5", "فخ استهلاك ATP في الانبساط", "ATP in Detachment (Exam Trap)", MindMapNodeType.EXAM_TRAP, "انفصال الروابط المستعرضة يحتاج جزيئات ATP جديدة؛ ونقصها يسبب الشد العضلي المؤلم.", 0.25f, 0.80f),
                MindMapNode("m6", "إنزيم كولين إستريز", "Cholinesterase Regulation", MindMapNodeType.REGULATION, "يحطم أسيتيل كولين إلى كولين وحمض خليك لعودة الاستقطاب والراحة.", 0.75f, 0.80f)
            ),
            edges = listOf(
                MindMapEdge("m1", "m2", "يفرز الكالسيوم"),
                MindMapEdge("m1", "m6", "يُثبَّط بواسطة", isHighlighted = true),
                MindMapEdge("m2", "m3", "يكشف مواقع الارتباط"),
                MindMapEdge("m3", "m4", "يستهلك ATP لسحب الأكتين"),
                MindMapEdge("m4", "m5", "يتطلب ATP جديداً للفك", isHighlighted = true)
            ),
            highYieldExamTips = listOf(
                "في الانقباض التام: طول المنطقة المضيئة (I) يقل، والمنطقة شبه المضيئة (H) تنعدم، وطول المنطقة الداكنة (A) يظل ثابتاً تماماً لأن طول خيوط الميوسين لا يتغير.",
                "نقص ATP مع وصول سيال خاطئ يؤدي إلى الشد العضلي العنيف (Muscle Cramp)."
            )
        ),
        "dna_structure_replication" to BiologicalMindMap(
            topicId = "dna_structure_replication",
            titleArabic = "خريطة شوكة تضاعف DNA والروابط النيتروجينية",
            titleEnglish = "DNA Replication Fork & Molecular Architecture",
            centralQuestionArabic = "كيف يُحافظ تضاعف DNA شبه المحافظ (Semi-Conservative) على دقة الشفرة دون أخطاء؟",
            mermaidCode = """
                graph TD
                    A["لولب مزدوج أصلي 5' إلى 3'"] -->|إنزيم اللولب Helicase| B["كسر الروابط الهيدروجينية وفصل الشريطين"]
                    B --> C["الشريط القالب 3' إلى 5'"]
                    B --> D["الشريط القالب 5' إلى 3'"]
                    C -->|إنزيم بلمرة DNA في اتجاه واحد| E["الشريط القائد (بناء متصل Continuous)"]
                    D -->|إنزيم البلمرة لا يعمل إلا 5'->3'| F["قطع أوكازاكي غير متصلة (Lagging)"]
                    F -->|إنزيم الربط DNA Ligase| G["ربط القطع بشريط مكتمل"]
                    E & G --> H["جزيئان متطابقان: شريط أصلي + شريط جديد"]
                    style A fill:#1A237E,stroke:#283593,stroke-width:2px,color:#fff
                    style B fill:#311B92,stroke:#4527A0,stroke-width:2px,color:#fff
                    style G fill:#00695C,stroke:#00897B,stroke-width:2px,color:#fff
                    style H fill:#1B5E20,stroke:#2E7D32,stroke-width:3px,color:#fff
            """.trimIndent(),
            nodes = listOf(
                MindMapNode("d1", "إنزيم اللولب Helicase", "Helicase Enzyme", MindMapNodeType.KEY_MOLECULE, "يفك الالتفاف ويكسر الروابط الهيدروجينية بين القواعد النيتروجينية.", 0.5f, 0.12f),
                MindMapNode("d2", "الشريط القائد (Leading)", "Leading Strand 5'->3'", MindMapNodeType.MECHANISM, "يبنيه إنزيم البلمرة باتجاه حركة شوكة التضاعف بصورة مستمرة وسريعة.", 0.20f, 0.40f),
                MindMapNode("d3", "قطع أوكازاكي (Lagging)", "Okazaki Fragments", MindMapNodeType.MECHANISM, "تُبنى في صورة قطع صغيرة متعاكسة بسبب اتجاه عمل البلمرة الإلزامي.", 0.80f, 0.40f),
                MindMapNode("d4", "إنزيم الربط Ligase", "DNA Ligase Repair & Join", MindMapNodeType.KEY_MOLECULE, "يصل قطع أوكازاكي بروابط تساهمية فوسفاتية ثنائية الإستر ويصلح العيوب.", 0.65f, 0.70f),
                MindMapNode("d5", "قواعد شارجاف (A=T, G≡C)", "Chargaff Equivalence", MindMapNodeType.ROOT, "نسبة البيورينات ثنائية الحلقة = نسبة البيريميدينات أحادية الحلقة دائماً في اللولب المزدوج.", 0.35f, 0.70f),
                MindMapNode("d6", "فخ الطفرة وتلف الشريطين معاً", "Dual Strand Damage Trap", MindMapNodeType.EXAM_TRAP, "إذا حدث التلف في نفس الموقع ونفس الوقت على الشريطين المتقابلين، يعجز إنزيم الربط عن الإصلاح فتحدث طفرة.", 0.5f, 0.90f)
            ),
            edges = listOf(
                MindMapEdge("d1", "d2", "يفتح القالب لـ"),
                MindMapEdge("d1", "d3", "يفتح القالب لـ"),
                MindMapEdge("d3", "d4", "تُربط بواسطة"),
                MindMapEdge("d4", "d6", "يعجز إذا تلف الشريطان", isHighlighted = true),
                MindMapEdge("d5", "d1", "تحدد التزاوج الأصلي")
            ),
            highYieldExamTips = listOf(
                "إنزيم بلمرة DNA يضيف نيوكليوتيدات جديدة فقط للطرف '3 الحر (مجموعة الهيدروكسيل OH-).",
                "عدد روابط G≡C هو 3 روابط هيدروجينية، بينما A=T رابطتان فقط، مما يجعل المناطق الغنية بـ GC أكثر ثباتاً ومقاومة للحرارة."
            )
        ),
        "immunity_humoral_cellular" to BiologicalMindMap(
            topicId = "immunity_humoral_cellular",
            titleArabic = "خريطة المناعة الخلطية والخلوية والتنسيق الليمفاوي",
            titleEnglish = "Humoral vs Cell-Mediated Immunity Cascade",
            centralQuestionArabic = "كيف تنسق الخلايا التائية المساعدة (TH) الاستجابة المناعية وتفصل بين الأجسام المضادة والموت المبرمج؟",
            mermaidCode = """
                graph TD
                    A["بلعمية كبيرة تبتلع الميكروب"] -->|تفكيك وعرض بروتين التوافق النسيجي MHC| B["البلعمية العارضة APC"]
                    B -->|تتعرف بواسطة مستقبل CD4| C["خلية تائية مساعدة TH نشطة"]
                    C -->|إفراز إنترلوكين IL| D["مناعة خلطية: خلايا بائية B"]
                    C -->|إفراز سيتوكينات Cytokines| E["مناعة خلوية: خلايا TC & NK"]
                    D -->|انقسام وتمايز| F["خلايا بائية بلازمية تنتج أجساماً مضادة IgG/IgM"]
                    D -->|تخزين| G["خلايا بائية ذاكرة (عقود)"]
                    E -->|إفراز بيرفورين + سموم ليمفاوية| H["تثقيب غشاء الخلية المصابة وتدمير DNA النواة"]
                    style A fill:#004D40,stroke:#00796B,stroke-width:2px,color:#fff
                    style C fill:#4A148C,stroke:#6A1B9A,stroke-width:3px,color:#fff
                    style F fill:#0D47A1,stroke:#1565C0,stroke-width:2px,color:#fff
                    style H fill:#B71C1C,stroke:#C62828,stroke-width:2px,color:#fff
            """.trimIndent(),
            nodes = listOf(
                MindMapNode("im1", "البلعمية العارضة و MHC", "Macrophage & MHC-II", MindMapNodeType.ROOT, "هضم الميكروب بواسطة الليسوسوم ودمج الأنتيجين مع بروتين التوافق النسيجي.", 0.5f, 0.10f),
                MindMapNode("im2", "الخلية التائية المساعدة TH", "Helper T-Cell (CD4+)", MindMapNodeType.KEY_MOLECULE, "دينامو المناعة المكتسبة، تفرز الإنترلوكينات لتنشيط المناعتين الخلطية والخلوية.", 0.5f, 0.32f),
                MindMapNode("im3", "المناعة الخلطية (الأجسام المضادة)", "Humoral Branch (B-Cells)", MindMapNodeType.MECHANISM, "تتعامل مع السموم والميكروبات الحرة في سوائل الجسم (الدم والليمف).", 0.20f, 0.55f),
                MindMapNode("im4", "المناعة الخلوية (السامة TC)", "Cellular Branch (Cytotoxic T)", MindMapNodeType.MECHANISM, "مستقبل CD8، تقضي على الخلايا السرطانية والأعضاء المزروعة والفيروسات المختبئة بالداخل.", 0.80f, 0.55f),
                MindMapNode("im5", "البيرفورين والسموم الليمفاوية", "Perforin & Lymphotoxins", MindMapNodeType.KEY_MOLECULE, "البيرفورين يثقب الغشاء والسموم تنشط جينات الموت المبرمج داخل النواة.", 0.80f, 0.80f),
                MindMapNode("im6", "التائية الكابحة TS والليمفوكينات", "Suppressor TS & Lymphokines", MindMapNodeType.REGULATION, "تفرز الليمفوكينات لتثبيط المناعة وإيقاف إنتاج الأجسام المضادة بعد القضاء على الميكروب.", 0.20f, 0.80f)
            ),
            edges = listOf(
                MindMapEdge("im1", "im2", "تنشيط عبر CD4"),
                MindMapEdge("im2", "im3", "إنترلوكينات تنشط B"),
                MindMapEdge("im2", "im4", "سيتوكينات تنشط TC"),
                MindMapEdge("im4", "im5", "إفراز أسلحة الهجوم"),
                MindMapEdge("im6", "im3", "كبح وتثبيط", isHighlighted = true),
                MindMapEdge("im6", "im4", "كبح وتثبيط", isHighlighted = true)
            ),
            highYieldExamTips = listOf(
                "الأجسام المضادة عاجزة تماماً عن اختراق أغشية الخلايا المصابة بالفيروسات لكبر حجمها؛ لذا تستلم المناعة الخلوية (TC و NK) المهمة فور دخول الفيروس للخلية.",
                "مستقبل CD4 يخص TH، بينما مستقبل CD8 مشترك بين الخلايا التائية السامة (TC) والتائية الكابحة (TS)."
            )
        ),
        "rna_transcription_translation" to BiologicalMindMap(
            topicId = "rna_transcription_translation",
            titleArabic = "خريطة النسخ وترجمة البروتين والريبوسوم",
            titleEnglish = "RNA Transcription & Translation Machinery",
            centralQuestionArabic = "كيف تتحول لغة النيوكليوتيدات المكونة من 4 أحرف إلى لغة 20 حمضاً أمينياً بتوافق ثلاثي دقيق؟",
            mermaidCode = """
                graph TD
                    A["جين DNA في النواة (شريط قالب 3'->5')"] -->|إنزيم بلمرة RNA| B["شريط mRNA أولي 5'->3'"]
                    B -->|إضافة كاب 5' وذيل عديد الأدنين 3'| C["خروج mRNA للسيتوبلازم"]
                    C -->|ارتباط بتحت وحدة الريبوسوم الصغيرة| D["كودون البدء AUG في موقع P"]
                    D -->|دخول أول tRNA يحمل ميثيونين| E["اكتمال ارتباط تحت وحدة الريبوسوم الكبيرة"]
                    E -->|استقبال tRNA التالي في موقع A| F["إنزيم نقل الببتيديل وتكوين الرابطة الببتيدية"]
                    F -->|إزاحة الريبوسوم وتكرار الدورة| G["كودون الوقف UAA / UAG / UGA"]
                    G -->|عامل الإطلاق Release Factor| H["تحرر السلسلة الببتيدية وتفكك الريبوسوم"]
                    style A fill:#283593,stroke:#1A237E,stroke-width:2px,color:#fff
                    style B fill:#4A148C,stroke:#311B92,stroke-width:2px,color:#fff
                    style F fill:#004D40,stroke:#00796B,stroke-width:2px,color:#fff
                    style H fill:#B71C1C,stroke:#C62828,stroke-width:3px,color:#fff
            """.trimIndent(),
            nodes = listOf(
                MindMapNode("r1", "بلمرة RNA والمحفز", "RNA Pol & Promoter", MindMapNodeType.KEY_MOLECULE, "يتعرف إنزيم بلمرة RNA على تتابع المحفز على شريط القالب ليبدأ النسخ.", 0.5f, 0.10f),
                MindMapNode("r2", "كاب 5' وذيل عديد الأدنين", "5' Cap & Poly-A Tail", MindMapNodeType.REGULATION, "الكاب يرتبط بالريبوسوم، والذيل (حوالي 200 نيوكليوتيدة) يحمي mRNA من الإنزيمات الهاضمة.", 0.20f, 0.35f),
                MindMapNode("r3", "كودون البدء AUG والميثيونين", "AUG Start Codon & Met", MindMapNodeType.ROOT, "أول كودون يُترجم دائماً ويدخل موقع الببتيديل (P) في الريبوسوم.", 0.80f, 0.35f),
                MindMapNode("r4", "إنزيم تفاعل نقل الببتيديل", "Peptidyl Transferase", MindMapNodeType.MECHANISM, "نشاط إنزيمي تابع للريبوسوم يُكوّن الرابطة الببتيدية بين الأحماض الأمينية.", 0.5f, 0.60f),
                MindMapNode("r5", "كودونات الوقف وعامل الإطلاق", "Stop Codons & Release Factor", MindMapNodeType.KEY_MOLECULE, "ثلاثة كودونات (UAA, UAG, UGA) ليس لها tRNA بل يرتبط بها بروتين عامل الإطلاق.", 0.25f, 0.85f),
                MindMapNode("r6", "فخ حساب الكودونات والأحماض", "Codon Math Trap", MindMapNodeType.EXAM_TRAP, "عدد الأحماض الأمينية = (عدد كودونات mRNA) - 1، لأن كودون الوقف لا يترجم لحمض أميني.", 0.75f, 0.85f)
            ),
            edges = listOf(
                MindMapEdge("r1", "r2", "ينتج شريط يحتاج حماية"),
                MindMapEdge("r2", "r3", "يوجه الارتباط بالريبوسوم"),
                MindMapEdge("r3", "r4", "بدء الترجمة وموقع P"),
                MindMapEdge("r4", "r5", "استمرار حتى كودون الوقف"),
                MindMapEdge("r5", "r6", "تطبيق القوانين الحسابية", isHighlighted = true)
            ),
            highYieldExamTips = listOf(
                "في أوليات النواة (البكتيريا) يحدث النسخ والترجمة معاً في آن واحد لعدم وجود غشاء نووي فاصل.",
                "مضاد الكودون لـ UAG هو AUC (مع مراعاة الاتجاه المعاكس 3' إلى 5')."
            )
        ),
        "plant_human_support" to BiologicalMindMap(
            topicId = "plant_human_support",
            titleArabic = "خريطة الدعامة الفسيولوجية والتركيبية والهيكل العظمي",
            titleEnglish = "Physiological vs Structural Support & Human Skeleton",
            centralQuestionArabic = "ما الفرق الجوهري بين دعامة مؤقتة تتأثر بالخاصية الأسموزية ودعامة دائمة تتأثر بترسيب المواد؟",
            mermaidCode = """
                graph TD
                    A["الدعامة في النبات"] --> B["دعامة فسيولوجية (مؤقتة تناضحية)"]
                    A --> C["دعامة تركيبية (دائمة جدارية)"]
                    B -->|دخول الماء بالخاصية الأسموزية| D["امتلاء الفجوة العصارانتفاخ وضغط الامتلاء"]
                    C -->|ترسيب السليلوز واللجنين| E["الخلايا الكولنشيمية والإسكلرنشيمية"]
                    C -->|ترسيب الكيوتين والسيبرين| F["منع فقد الماء والبشرة"]
                    style A fill:#1B5E20,stroke:#2E7D32,stroke-width:2px,color:#fff
                    style B fill:#00796B,stroke:#004D40,stroke-width:2px,color:#fff
                    style C fill:#BF360C,stroke:#D84315,stroke-width:2px,color:#fff
            """.trimIndent(),
            nodes = listOf(
                MindMapNode("s1", "الخاصية الأسموزية وضغط الامتلاء", "Osmosis & Turgor Pressure", MindMapNodeType.MECHANISM, "تعتمد على حركة الماء حسب تدرج تركيز الذائبات؛ تفقد بالذبول وتكتسب بالري.", 0.25f, 0.30f),
                MindMapNode("s2", "الكيوتين والسيبرين", "Cutin & Suberin", MindMapNodeType.KEY_MOLECULE, "مواد شمعية غير منفذة للماء تحافظ على الدعامة الفسيولوجية بوقف النتح.", 0.75f, 0.30f),
                MindMapNode("s3", "السليلوز واللجنين", "Cellulose & Lignin", MindMapNodeType.KEY_MOLECULE, "تكسب الأنسجة النباتية الصلابة والمرونة والقوة (مثل الألياف والخلايا الحجرية).", 0.5f, 0.55f),
                MindMapNode("s4", "الهيكل المحوري (80 عظمة)", "Axial Skeleton", MindMapNodeType.ROOT, "الجمجمة، القفص الصدري، والعمود الفقري (33 فقرة تحمي الحبل الشوكي).", 0.30f, 0.80f),
                MindMapNode("s5", "الأربطة مقابل الأوتار", "Ligaments vs Tendons Trap", MindMapNodeType.EXAM_TRAP, "الأربطة تربط عظماً بعظم وتتميز بالمرونة (الرباط الصليبي)، والأوتار تربط عضلة بعظم (وتر أخيل).", 0.70f, 0.80f)
            ),
            edges = listOf(
                MindMapEdge("s2", "s1", "تحمي من فقدان الماء"),
                MindMapEdge("s3", "s2", "دعامات تركيبية متكاملة"),
                MindMapEdge("s4", "s5", "مفاصل الحركة والأربطة")
            ),
            highYieldExamTips = listOf(
                "الكيوتين يلعب دوراً مزدوجاً: دعامة تركيبية بترسيبه على البشرة، ويحافظ على الدعامة الفسيولوجية بمنع تبخر الماء.",
                "الفقرة رقم 17 هي فقرة ظهرية (صدرية) نموذجية وتتصل بزوج الضلوع العاشر."
            )
        ),
        "hormonal_coordination_endocrine" to BiologicalMindMap(
            topicId = "hormonal_coordination_endocrine",
            titleArabic = "خريطة الغدة النخامية وشبكة التغذية الراجعة السلبية",
            titleEnglish = "Pituitary Master Gland & Negative Feedback Loops",
            centralQuestionArabic = "كيف تحافظ الهرمونات المتضادة (الأنسولين/الجلوكاجون، الكالسيتونين/الباراثورمون) على الاستتباب الداخلي؟",
            mermaidCode = """
                graph TD
                    A["تحت المهاد Hypothalamus"] -->|هرمونات عصبية مطلقة| B["الفص الأمامي للغدة النخامية"]
                    B -->|TSH| C["الغدة الدرقية: الثيروكسين"]
                    B -->|ACTH| D["قشرة الكظرية: الكورتيزون والألدوستيرون"]
                    B -->|FSH & LH| E["المناسل (المبيضان والخصيتان)"]
                    C -.->|تغذية راجعة سلبية Negative Feedback| B
                    D -.->|تغذية راجعة سلبية| B
                    style A fill:#311B92,stroke:#4527A0,stroke-width:2px,color:#fff
                    style B fill:#0D47A1,stroke:#1565C0,stroke-width:3px,color:#fff
                    style C fill:#004D40,stroke:#00695C,stroke-width:2px,color:#fff
            """.trimIndent(),
            nodes = listOf(
                MindMapNode("h1", "الفص الأمامي للغدة النخامية", "Anterior Pituitary Lobe", MindMapNodeType.ROOT, "سيدة الغدد الصماء؛ يفرز الهرمونات المنبهة للدرقية والكظرية والمناسل بالإضافة لـ GH و Prolactin.", 0.5f, 0.15f),
                MindMapNode("h2", "الفص الخلفي (هرمونات عصبية)", "Posterior Lobe (ADH/OXT)", MindMapNodeType.KEY_MOLECULE, "يخزن ويفرز هرموني ADH (المضاد لإدرار البول/رافع لضغط الدم) والأوكسيتوسين المفرزين من تحت المهاد.", 0.18f, 0.40f),
                MindMapNode("h3", "الأنسولين مقابل الجلوكاجون", "Insulin vs Glucagon Homeostasis", MindMapNodeType.REGULATION, "الأنسولين (خلايا بيتا) يخفض جلوكوز الدم؛ الجلوكاجون (خلايا ألفا) يرفع جلوكوز الدم من جليكوجين الكبد فقط.", 0.82f, 0.40f),
                MindMapNode("h4", "الكالسيتونين والباراثورمون", "Calcitonin vs Parathormone", MindMapNodeType.REGULATION, "الباراثورمون يسحب الكالسيوم من العظام للدم؛ الكالسيتونين يمنع سحبه ويرسبه في العظام.", 0.5f, 0.65f),
                MindMapNode("h5", "فخ الألدوستيرون والكلى", "Aldosterone Mineral Balance Trap", MindMapNodeType.EXAM_TRAP, "يعيد امتصاص الصوديوم Na+ ويتخلص من البوتاسيوم K+ الزائد في البول للحفاظ على الضغط التناضحي.", 0.5f, 0.88f)
            ),
            edges = listOf(
                MindMapEdge("h1", "h3", "تنسيق أيضي"),
                MindMapEdge("h1", "h5", "تنبيه عبر ACTH"),
                MindMapEdge("h4", "h5", "ضبط الأملاح الحيوية")
            ),
            highYieldExamTips = listOf(
                "الجلوكاجون يحفز تحلل الجليكوجين المخزن في الكبد فقط وليس جليكوجين العضلات.",
                "زيادة إفراز هرمون النمو GH في البالغين تسبب مرض الأكروميجالي (تضخم عظام الأطراف والوجه)."
            )
        ),
        "reproduction_double_fertilization" to BiologicalMindMap(
            topicId = "reproduction_double_fertilization",
            titleArabic = "خريطة الإخصاب المزدوج وتكوين البذرة والثمرة في النبات",
            titleEnglish = "Double Fertilization & Angiosperm Embryogenesis",
            centralQuestionArabic = "لماذا يُعتبر الإخصاب في مغطاة البذور 'مزدوجاً' وما مصير أنوية الكيس الجنيني السبعة؟",
            mermaidCode = """
                graph TD
                    A["حبة لقاح ناضجة (نواة أنبوبية + نواة مولدة)"] -->|سقوط على الميسم| B["نمو أنبوبة اللقاح"]
                    B -->|انقسام ميتوزي للنواة المولدة| C["نواتان زكريتان ن"]
                    C -->|النواة الزكرية الأولى + نواة البيضة| D["الزيجوت (2ن) -> الجنين"]
                    C -->|النواة الزكرية الثانية + نواتا الكيس الجنيني| E["نواة الإندوسبرم (3ن) -> نسيج غذائي"]
                    D & E --> F["بذرة داخل مبيض ناضج (ثمرة)"]
                    style A fill:#F57F17,stroke:#FBC02D,stroke-width:2px,color:#000
                    style C fill:#E65100,stroke:#F57C00,stroke-width:2px,color:#fff
                    style D fill:#1B5E20,stroke:#2E7D32,stroke-width:2px,color:#fff
                    style E fill:#0D47A1,stroke:#1565C0,stroke-width:2px,color:#fff
            """.trimIndent(),
            nodes = listOf(
                MindMapNode("rf1", "حبة اللقاح والإنبات", "Pollen Grain Germination", MindMapNodeType.ROOT, "تتكون من جدار سميك ونواة أنبوبية تخترق القلم ونواة مولدة تنقسم لنواتين زكريتين.", 0.5f, 0.15f),
                MindMapNode("rf2", "إخصاب البيضة (تكوين الزيجوت 2n)", "Syngamy -> Zygote 2n", MindMapNodeType.MECHANISM, "اندماج نواة زكرية (ن) مع نواة المشيج المؤنث (البيضة ن) لتكوين جنين البذرة المستقبلي.", 0.25f, 0.45f),
                MindMapNode("rf3", "الاندماج الثلاثي (إندوسبرم 3n)", "Triple Fusion -> Endosperm 3n", MindMapNodeType.MECHANISM, "اندماج نواة زكرية (ن) مع نواتي الكيس الجنيني (ن + ن) لتكوين النسيج المغذي للجنين.", 0.75f, 0.45f),
                MindMapNode("rf4", "بذور ذات فلقة vs فلقتين", "Endospermic vs Non-endospermic", MindMapNodeType.REGULATION, "في ذات الفلقتين يلتهم الجنين الإندوسبرم فيخزن غذاءه في فلقتين؛ في ذات الفلقة يحتفظ بالإندوسبرم (الحبة).", 0.5f, 0.70f),
                MindMapNode("rf5", "فخ الإثمار العذري", "Parthenocarpy Exam Trap", MindMapNodeType.EXAM_TRAP, "تكوين ثمرة بدون بذور لأن المبيض تشحم دون حدوث إخصاب (مثل الموز والأناناس)، وهو ليس تكاثراً.", 0.5f, 0.90f)
            ),
            edges = listOf(
                MindMapEdge("rf1", "rf2", "النواة الزكرية الأولى"),
                MindMapEdge("rf1", "rf3", "النواة الزكرية الثانية"),
                MindMapEdge("rf2", "rf4", "تكوين الجنين"),
                MindMapEdge("rf3", "rf4", "توفير الغذاء للجنين"),
                MindMapEdge("rf4", "rf5", "مقارنة مع الثمار العذرية", isHighlighted = true)
            ),
            highYieldExamTips = listOf(
                "عدد الأنوية المشاركة في عملية الإخصاب المزدوج = 5 أنوية (نواتان زكريتان + نواة البيضة + نواتا الكيس الجنيني).",
                "الثمرة الكاذبة (مثل التفاح) هي ثمرة يتشحم فيها أي جزء زهري آخر غير المبيض (كالتخت)."
            )
        ),
        "human_reproduction_menstrual" to BiologicalMindMap(
            topicId = "human_reproduction_menstrual",
            titleArabic = "خريطة دورة الطمث والهرمونات وتكوين الأمشاج",
            titleEnglish = "Menstrual Cycle & Hormonal Feedback",
            centralQuestionArabic = "كيف تنظم قمم هرمونات FSH وLH والإستروجين والبروجسترون دورة الـ 28 يوماً في مبيض المرأة؟",
            mermaidCode = """
                graph TD
                    A["اليوم 1-4: مرحلة الطمث"] -->|إفراز FSH من النخامية| B["اليوم 5-13: مرحلة نضج البويضة (حويصلة جراف)"]
                    B -->|إفراز الإستروجين من جراف| C["إعادة بناء وإنماء بطانة الرحم"]
                    C -->|انفجار قمة LH في اليوم 14| D["مرحلة التبويض وخروج الخلية البيضية الثانوية"]
                    D -->|تحول بقايا جراف إلى الجسم الأصفر| E["اليوم 15-28: إفراز البروجسترون وزيادة سمك البطانة"]
                    E -->|عدم حدوث إخصاب| F["ضمور الجسم الأصفر وهبوط البروجسترون وتمزق البطانة (طمث جديد)"]
                    style B fill:#C2185B,stroke:#AD1457,stroke-width:2px,color:#fff
                    style D fill:#7B1FA2,stroke:#6A1B9A,stroke-width:3px,color:#fff
                    style E fill:#F57C00,stroke:#EF6C00,stroke-width:2px,color:#fff
                    style F fill:#B71C1C,stroke:#C62828,stroke-width:2px,color:#fff
            """.trimIndent(),
            nodes = listOf(
                MindMapNode("hr1", "مرحلة نضج البويضة و FSH", "Follicular Phase & FSH", MindMapNodeType.ROOT, "إنماء حويصلة جراف في المبيض واستغراق حوالي 10 أيام مع إفراز الإستروجين.", 0.20f, 0.25f),
                MindMapNode("hr2", "قمة هرمون LH والتبويض", "LH Surge & Ovulation", MindMapNodeType.KEY_MOLECULE, "تفجر حويصلة جراف في اليوم الـ 14 من بدء الطمث لتحرير الخلية البيضية الثانوية.", 0.50f, 0.25f),
                MindMapNode("hr3", "الجسم الأصفر والبروجسترون", "Corpus Luteum & Progesterone", MindMapNodeType.MECHANISM, "زيادة سمك بطانة الرحم وزيادة إمدادها الدموي استعداداً لاستقبال البويضة المخصبة.", 0.80f, 0.25f),
                MindMapNode("hr4", "الخلية البيضية الثانوية والانقسام الميوزي II", "Secondary Oocyte Trap", MindMapNodeType.EXAM_TRAP, "الانقسام الميوزي الثاني انقسام مؤجل أو مشروط؛ لا يكتمل إلا بلحظة دخول رأس الحيوان المنوي للإخصاب.", 0.35f, 0.70f),
                MindMapNode("hr5", "المشيمة وهرمون ريلاكسين", "Placenta & Relaxin", MindMapNodeType.REGULATION, "تفرز البروجسترون من الشهر الرابع بعد ضمور الجسم الأصفر، وتفرز الريلاكسين لتليين الارتفاق العاني.", 0.65f, 0.70f)
            ),
            edges = listOf(
                MindMapEdge("hr1", "hr2", "ارتفاع الإستروجين يحفز LH"),
                MindMapEdge("hr2", "hr3", "بقايا الجدار تكون الجسم الأصفر"),
                MindMapEdge("hr2", "hr4", "تحرير بويضة لم تكمل الميوزي الثاني", isHighlighted = true),
                MindMapEdge("hr3", "hr5", "في حالة حدوث حمل")
            ),
            highYieldExamTips = listOf(
                "الانقسام الميوزي الثاني للبويضة يحدث في الثلث الأول من قناة فالوب لحظة الإخصاب فقط.",
                "أعلى تركيز لهرمون الإستروجين يكون قبل التبويض مباشرة (اليوم 12-13)، وأعلى تركيز للبروجسترون يكون في اليوم 21 تقريباً."
            )
        ),
        "genetic_engineering_recombinant" to BiologicalMindMap(
            topicId = "genetic_engineering_recombinant",
            titleArabic = "خريطة الهندسة الوراثية وإنزيمات القصر والـ PCR",
            titleEnglish = "Genetic Engineering, Plasmids & PCR",
            centralQuestionArabic = "كيف تُقص الجينات بدقة النانو باستخدام إنزيمات القصر وتُنسخ ملايين المرات بجهاز الثيرموسايكلر؟",
            mermaidCode = """
                graph TD
                    A["خلية مانحة: استخلاص DNA الجين المرغوب"] --> B["إنزيم القصر البكتيري Restriction Enzyme"]
                    C["بكتيريا: استخلاص البلازميد الناقل Plasmid"] --> B
                    B -->|قطع عند موقع التعرف 4-7 نيوكليوتيدات| D["أطراف لاصقة مائلة متكاملة Sticky Ends"]
                    D -->|إنزيم الربط DNA Ligase| E["بلازميد معاد الاتحاد Recombinant DNA"]
                    E -->|إدخال البلازميد في بكتيريا E. coli| F["تكاثر البكتيريا واستنساخ الجين"]
                    G["عينة DNA ضئيلة الحجم"] -->|جهاز PCR + إنزيم Taq Polymerase في حرارة عالية| H["ملايين النسخ في دقائق معدودة"]
                    style B fill:#311B92,stroke:#512DA8,stroke-width:2px,color:#fff
                    style E fill:#004D40,stroke:#00796B,stroke-width:3px,color:#fff
                    style H fill:#E65100,stroke:#F57C00,stroke-width:2px,color:#fff
            """.trimIndent(),
            nodes = listOf(
                MindMapNode("ge1", "إنزيمات القصر ومواقع التعرف", "Restriction Enzymes & Palindromes", MindMapNodeType.KEY_MOLECULE, "تتعرف على تتابعات متناظرة (4 إلى 7 نيوكليوتيدات) وتقطع لتترك أطرافاً لاصقة مفردة.", 0.25f, 0.25f),
                MindMapNode("ge2", "إنزيم الربط DNA Ligase", "DNA Ligase in Cloning", MindMapNodeType.KEY_MOLECULE, "يربط الأطراف اللاصقة المتكاملة لبناء بلازميد مهجن ومعاد الاتحاد.", 0.75f, 0.25f),
                MindMapNode("ge3", "إنزيم النسخ العكسي Reverse Transcriptase", "Reverse Transcriptase Trap", MindMapNodeType.EXAM_TRAP, "يستخلص من الفيروسات القهقرية المحتوية على RNA لبناء شريط DNA مفرد من قالب mRNA معزول.", 0.5f, 0.55f),
                MindMapNode("ge4", "جهاز PCR وإنزيم Taq Polymerase", "PCR & Thermostable Taq", MindMapNodeType.MECHANISM, "يضاعف قطع DNA آلاف المرات باستخدام إنزيم يتحمل درجات الحرارة المرتفعة جداً.", 0.5f, 0.82f)
            ),
            edges = listOf(
                MindMapEdge("ge1", "ge2", "تجهيز الأطراف اللاصقة للربط"),
                MindMapEdge("ge3", "ge1", "إنتاج DNA خالي من الإنترونات للقصر", isHighlighted = true),
                MindMapEdge("ge2", "ge4", "مقارنة بين طرق الاستنساخ البكتيري والسريع")
            ),
            highYieldExamTips = listOf(
                "إنزيم النسخ العكسي يبني شريط DNA من قالب mRNA، ثم يقوم إنزيم بلمرة DNA ببناء الشريط المكمل لإنتاج لولب مزدوج.",
                "البكتيريا تحمي حمضها النووي من إنزيمات القصر الخاصة بها عن طريق إضافة مجموعات ميثيل (-CH3) إلى نيوكليوتيدات مواقع التعرف بواسطة إنزيمات معدِّلة."
            )
        )
    )

    fun getMindMapForTopic(topicId: String): BiologicalMindMap {
        return mindMaps[topicId] ?: mindMaps["sarcomere_contraction"]!!
    }

    fun getAllTopicIdsWithMindMaps(): Set<String> = mindMaps.keys
}
