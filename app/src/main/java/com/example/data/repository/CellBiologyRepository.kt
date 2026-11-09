package com.example.data.repository

import com.example.domain.model.*

object CellBiologyDataProvider {

    val organelleDetails: Map<OrganelleType, OrganelleDetail> = mapOf(
        OrganelleType.NUCLEUS to OrganelleDetail(
            type = OrganelleType.NUCLEUS,
            englishName = "Nucleus",
            arabicName = "النواة",
            roleBadge = "مركز التحكم الوراثي والتعبير الجيني",
            primaryFunction = "مستودع المادة الوراثية (DNA) ومركز توجيه كافة الأنشطة الحيوية وبناء البروتينات داخل الخلية الحية.",
            detailedStructure = "محاطة بغشاء نووي مزدوج (Nuclear Envelope) تتخلله ثقوب نووية دقيقة تسمح بمرور جزيئات mRNA والريبوسومات، وتحتوي داخلها على السائل النووي وشبكة الكروماتين المكونة من DNA وبروتينات الهيستونات.",
            biochemicalRole = "تحدث بها عمليتا تضاعف الـ DNA (Replication) أثناء الانقسام، والنسخ (Transcription) لتكوين شفرات الـ mRNA وtRNA وrRNA.",
            ministryExamTrap = "فخ امتحان: تذكر أن كريات الدم الحمراء الناضجة (RBCs) في الثدييات تفقد أنويتها تماماً لزيادة مساحة حمل الهيموجلوبين، لذا لا تستطيع التكاثر أو تعويض بروتيناتها وتعيش نحو 120 يوماً فقط.",
            eli5Analogy = "مثل غرفة التحكم المركزية في المصنع التي تحتوي على المخططات الهندسية الأصلية لجميع المنتجات.",
            keyReactions = listOf(
                "DNA Replication: DNA Polymerase synthesizing identical duplexes",
                "Transcription: RNA Polymerase II generating precursor mRNA",
                "Nuclear Export: Exportin complexes transporting mRNA to cytoplasm"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "أي من التراكيب التالية يسمح بخروج جزيئات mRNA إلى السيتوبلازم لبدء الترجمة؟",
                questionEnglish = "Which structure allows mRNA exit to cytoplasm for translation?",
                options = listOf(
                    "الغشاء البلازمي الخارجي",
                    "الثقوب النووية في الغشاء المزدوج",
                    "أنيبيبات جهاز جولجي",
                    "بلورات السنتريول"
                ),
                correctIndex = 1,
                explanationArabic = "تحتوي النواة على ثقوب نووية بقطر يسمح بخروج الـ mRNA والوحدات البنائية للريبوسومات دون خروج الـ DNA الأصلي.",
                ministryTip = "الثقوب النووية تحافظ على حماية المادة الوراثية وتسمح بالتبادل الانتقائي."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = true
        ),

        OrganelleType.NUCLEOLUS to OrganelleDetail(
            type = OrganelleType.NUCLEOLUS,
            englishName = "Nucleolus",
            arabicName = "النوية",
            roleBadge = "مصنع إنتاج الريبوسومات",
            primaryFunction = "تخليق وتجميع وحدات الريبوسومات (Ribosomal Subunits) من خلال نسخ جينات rRNA ودمجها مع البروتينات القادمة من السيتوبلازم.",
            detailedStructure = "تركيب كثيف غير محاط بغشاء داخل النواة، يظهر واضحاً تحت المجهر الإلكتروني ويزداد حجمه وعدده في الخلايا النشطة إفرازياً.",
            biochemicalRole = "تنسخ إنزيمات RNA Polymerase I جزيئات rRNA وتجمعها مع 78 بروتيناً ريبوسومياً لتكوين الوحيدة الكبرى 60S والصغرى 40S.",
            ministryExamTrap = "فخ امتحان: يزداد عدد النويات في الخلايا الإفرازية كخلايا الغدد الصماء وخلايا بيتا في البنكرياس نظراً لحاجتها المستمرة لتصنيع آلاف الريبوسومات لتخليق الهرمونات البروتينية.",
            eli5Analogy = "مثل خط تجميع عمال البناء داخل الإدارة المركزية.",
            keyReactions = listOf(
                "rRNA synthesis via RNA Polymerase I",
                "Assembly of 40S (Small) & 60S (Large) ribosomal subunits",
                "Export of preribosomal complexes through nuclear pores"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "في أي الخلايا التالية تتوقع وجود أكثر من نوية واحدة بارزة الحجم؟",
                questionEnglish = "In which cell type would you expect multiple prominent nucleoli?",
                options = listOf(
                    "خلية دهنية مخزنة للشحوم",
                    "خلية مفرزة لهرمون الإنسولين في البنكرياس",
                    "خلية عظمية ناضجة هادئة",
                    "خلية بشرة جلدية متقرنة"
                ),
                correctIndex = 1,
                explanationArabic = "الخلايا المفرزة للبروتينات كخلايا البنكرياس تحتاج معدلات هائلة من الريبوسومات، لذا تكبر نوياتها وتتعدد.",
                ministryTip = "العلاقة طردية بين نشاط الإفراز البروتيني وعدد النويات في الخلية."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = true
        ),

        OrganelleType.MITOCHONDRIA to OrganelleDetail(
            type = OrganelleType.MITOCHONDRIA,
            englishName = "Mitochondria",
            arabicName = "الميتوكوندريا",
            roleBadge = "محطة توليد الطاقة الخلوية (ATP)",
            primaryFunction = "استخلاص الطاقة الكيميائية الكامنة في الروابط العضوية (كالجلوكوز والأحماض الدهنية) عبر التنفس الهوائي وتخزينها في جزيئات ATP.",
            detailedStructure = "عضي محاط بغشاءين: غشاء خارجي أملس، وغشاء داخلي ينثني ليكون أعرافاً (Cristae) لزيادة مساحة السطح التفاعلي، ويحتوي بالداخل على الحشوة (Matrix) وإنزيمات دورة كربس وDNA حلقي وريبوسومات 70S.",
            biochemicalRole = "تحدث بها أكسدة حمض البيروفيك، ودورة كربس (Krebs Cycle)، وسلسلة نقل الإلكترون والفسفرة التأكسدية لإنتاج 38 جزيء ATP لكل جزيء جلوكوز.",
            ministryExamTrap = "فخ امتحان: ترتبط كثافة الأعراف (Cristae) طردياً مع المجهود الأيضي للخلية؛ لذا نجد أعراف ميتوكوندريا الخلايا العضلية الهيكلية وعضلة القلب أكثر عدداً وانثناءً بكثير من الخلايا الغضروفية.",
            eli5Analogy = "مثل محطة توليد الكهرباء والمولدات التوربينية التي تشحن بطاريات الخلية.",
            keyReactions = listOf(
                "Pyruvate Decarboxylation: Pyruvate -> Acetyl-CoA + CO2 + NADH",
                "Krebs Citric Acid Cycle: Generating NADH & FADH2 reducing power",
                "Chemiosmosis: ATP Synthase driving ADP + Pi -> ATP using H+ gradient"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "ما الفائدة البيولوجية المباشرة لزيادة انثناءات الأعراف (Cristae) في الغشاء الداخلي للميتوكوندريا؟",
                questionEnglish = "What is the direct biological advantage of mitochondrial cristae folding?",
                options = listOf(
                    "تخزين كميات أكبر من الجليكوجين",
                    "زيادة مساحة سطح تفاعلات سلسلة نقل الإلكترون وتخليق ATP",
                    "حماية الـ DNA الحلقي من إنزيمات التحلل",
                    "تسهيل خروج البروتينات خارج الخلية"
                ),
                correctIndex = 1,
                explanationArabic = "توفر انثناءات الأعراف مساحة شاسعة لتثبيت مجمعات سلسلة نقل الإلكترون وإنزيم ATP Synthase مما يضاعف إنتاج الطاقة.",
                ministryTip = "كلما زاد نشاط العضلة زاد عدد الأعراف والميتوكوندريا بها."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = true
        ),

        OrganelleType.ROUGH_ER to OrganelleDetail(
            type = OrganelleType.ROUGH_ER,
            englishName = "Rough Endoplasmic Reticulum",
            arabicName = "الشبكة الإندوبلازمية الخشنة",
            roleBadge = "معالجة وتعديل البروتينات الإفرازية",
            primaryFunction = "استقبال السلاسل الببتيدية المصنعة بواسطة الريبوسومات المثبتة عليها، والقيام بطيها وتعديلها ونقلها عبر حويصلات ناقلة إلى جهاز جولجي.",
            detailedStructure = "شبكة متفرعة من الأكياس الغشائية المفلطحة (Cisternae) متصلة بالغشاء النووي الخارجي، وتغطي أسطحها الخارجية حبيبات دقيقة هي الريبوسومات.",
            biochemicalRole = "إدخال البروتين عبر مسام Sec61، وتكوين روابط ثاني الكبريتيد (Disulfide bonds)، والبدء بعملية الارتباط بالسكريات (N-linked Glycosylation).",
            ministryExamTrap = "فخ امتحان: البروتينات التي تصنعها الريبوسومات الحرة في السيتوبلازم تستخدم داخل الخلية نفسها (مثل إنزيمات التحلل الجليكولي)، بينما البروتينات المصنعة على الشبكة الخشنة تصدر للخارج أو تذهب للأغشية واللايسوسومات.",
            eli5Analogy = "مثل ورشة الخياطة والتغليف الأولي المتصلة بالمكتب الرئيسي.",
            keyReactions = listOf(
                "Signal peptide recognition and translocation into lumen",
                "Protein folding via chaperone proteins (BiP/Hsp70)",
                "Packaging into COPII-coated transport vesicles destined for Golgi"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "أي من المواد التالية تتوقع أن يُصنع في الشبكة الإندوبلازمية الخشنة وليس بواسطة الريبوسومات الحرة؟",
                questionEnglish = "Which substance is synthesized on Rough ER rather than free ribosomes?",
                options = listOf(
                    "إنزيمات التنفس الهوائي بالسيتوبلازم",
                    "إنزيم الببسين المفرز في المعدة",
                    "بروتين الهيموجلوبين في السيتوسول",
                    "بروتين الأكتين السيتوبلازمي"
                ),
                correctIndex = 1,
                explanationArabic = "إنزيم الببسين بروتين إفرازي مخصص للإخراج من خلايا المعدة، لذا يُصنع على الشبكة الإندوبلازمية الخشنة لتمريره لجهاز جولجي.",
                ministryTip = "البروتينات الإفرازية والغشائية اختصاص حصري للشبكة الإندوبلازمية الخشنة."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = true
        ),

        OrganelleType.SMOOTH_ER to OrganelleDetail(
            type = OrganelleType.SMOOTH_ER,
            englishName = "Smooth Endoplasmic Reticulum",
            arabicName = "الشبكة الإندوبلازمية الملساء",
            roleBadge = "تخليق الدهون وإزالة سمية العقاقير وتخزين الكالسيوم",
            primaryFunction = "تخليق الليبيدات (الدهون، الفسفوليبيدات، والهرمونات الستيرويدية)، وتحويل السموم إلى مواد ذائبة للتخلص منها، وتخزين أيونات الكالسيوم.",
            detailedStructure = "شبكة أنبوبية غشائية ملساء خالية تماماً من الريبوسومات، وتكثر في خلايا الكبد وقشرة الغدة الكظرية وخلايا العضلات.",
            biochemicalRole = "تحتوي على إنزيمات عائلة Cytochrome P450 لإزالة السمية، وإنزيمات تصنيع الكوليسترول والتستوستيرون، ومضخات SERCA لضخ الكالسيوم.",
            ministryExamTrap = "فخ امتحان: في خلايا العضلات الهيكلية، تسمى الشبكة الملساء بالشبكة الساركوبلازمية (Sarcoplasmic Reticulum)، ووظيفتها الجوهرية إطلاق أيونات الكالسيوم لتحفيز انقباض القطعة العضلية (الساركومير) ثم إعادة ضخها للاسترخاء.",
            eli5Analogy = "مثل معمل تدوير النفايات ووحدة تصنيع الزيوت وتخزين البطاريات.",
            keyReactions = listOf(
                "Steroid hormone biosynthesis from cholesterol",
                "Xenobiotic detoxification via Cytochrome P450 hydroxylation",
                "Calcium sequestration via ATP-dependent SERCA pumps"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "ما الدور الحاسم للشبكة الساركوبلازمية (الشبكة الملساء) أثناء انقباض الليفة العضلية الهيكلية؟",
                questionEnglish = "What is the critical role of sarcoplasmic reticulum during muscle contraction?",
                options = listOf(
                    "إنتاج شفرات الحمض النووي",
                    "تحرير أيونات الكالسيوم لترتبط بالتروبونين وتكشف مواقع الارتباط",
                    "تخليق الإنزيمات الهاضمة للجليكوجين",
                    "إفراز مادة الأستيل كولين خارج غشاء الليفة"
                ),
                correctIndex = 1,
                explanationArabic = "تطلق الشبكة الساركوبلازمية مخزونها من أيونات الكالسيوم عند وصول السيال العصبي لترتبط بالخيوط وتنشط الروابط المستعرضة.",
                ministryTip = "اربط دائماً بين الكالسيوم وانقباض العضلات والشبكة الملساء (الساركوبلازمية)."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = true
        ),

        OrganelleType.GOLGI_APPARATUS to OrganelleDetail(
            type = OrganelleType.GOLGI_APPARATUS,
            englishName = "Golgi Apparatus",
            arabicName = "جهاز جولجي",
            roleBadge = "مركز الفرز والتعديل النهائي والتوزيع الإفرازي",
            primaryFunction = "استقبال الحويصلات الناقلة المحملة بالبروتينات والدهون من الشبكة الإندوبلازمية، وتعديلها وإضافة وسوم كيميائية لتوجيهها إلى وجهاتها المحددة.",
            detailedStructure = "مجموعة من الأكياس الغشائية المفلطحة المقوسة ذات الحواف المنتفخة غير المتصلة ببعضها، مقسمة إلى وجه استقبال (Cis-face) ووجه خروج وتصدير (Trans-face).",
            biochemicalRole = "إضافة وتعديل السكريات قليلة التعدد (Glycosylation)، والفسفرة، وتكوين اللايسوسومات الأولية، وإطلاق الحويصلات الإفرازية نحو الغشاء الخلوي.",
            ministryExamTrap = "فخ امتحان: إذا تعطل جهاز جولجي في خلية بيتا بالبنكرياس، فلن يتمكن الإنسولين من التعبئة داخل حويصلات إفرازية أو الوصول للغشاء الخارجي للتحرر في الدم عبر الإخراج الخلوي (Exocytosis).",
            eli5Analogy = "مثل مجمع البريد والشحن المركزي الذي يفرز الطرود ويضع عليها باركود العناوين ثم يشحنها بسيارات التوزيع.",
            keyReactions = listOf(
                "Vesicle fusion at cis-Golgi network",
                "O-linked glycosylation and sulfation in medial cisternae",
                "Budding of secretory granules and primary lysosomes from trans-Golgi"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "أي العضيات التالية مسؤول مباشرة عن تكوين الحويصلات الإفرازية وحويصلات اللايسوسومات في الخلية؟",
                questionEnglish = "Which organelle directly forms secretory vesicles and lysosomes?",
                options = listOf(
                    "الميتوكوندريا",
                    "جهاز جولجي (ديكتيوسوم)",
                    "السنتروسوم",
                    "الغشاء النووي الداخلي"
                ),
                correctIndex = 1,
                explanationArabic = "تبرعم الحويصلات الإفرازية واللايسوسومات الأولية من وجه التصدير (Trans face) لجهاز جولجي بعد اكتمال التعديلات.",
                ministryTip = "في الخلايا النباتية يسمى جهاز جولجي باسم الديكتيوسوم (Dictyosome)."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = true
        ),

        OrganelleType.RIBOSOMES to OrganelleDetail(
            type = OrganelleType.RIBOSOMES,
            englishName = "Ribosomes",
            arabicName = "الريبوسومات",
            roleBadge = "ماكينة قراءة الشفرة الوراثية وتخليق البروتين",
            primaryFunction = "ترجمة تتابع الكودونات المحمولة على جزيء الـ mRNA إلى تتابع من الأحماض الأمينية لبناء سلاسل عديد الببتيد والبروتينات.",
            detailedStructure = "جسيمات غير غشائية تتكون من وحيدتين: وحيدة كبرى ووحيدة صغرى، تتركبان من جزيئات rRNA ونحو 78 نوعاً من البروتينات، بحجم 80S في حقيقيات النواة و70S في بدائيات النواة والميتوكوندريا.",
            biochemicalRole = "ربط الأحماض الأمينية بروابط ببتيدية عبر إنزيم البيبتيديل ترانسفيراز (Peptidyl transferase) المدمج في الـ rRNA للوحيدة الكبرى.",
            ministryExamTrap = "فخ امتحان: الريبوسومات عضيات غير غشائية (Non-membranous)، لذا توجد في حقيقيات النوى وبدائيات النوى على حد سواء، والمضادات الحيوية التي تستهدف ريبوسومات 70S البكتيرية قد تؤثر على ميتوكوندريا الإنسان عند الجرعات السامة لأنها تمتلك ريبوسومات 70S شبيهة بالبكتيريا.",
            eli5Analogy = "مثل الطابعة ثلاثية الأبعاد التي تقرأ كود الشفرة وتبني الجسيم بدقة مليمترية حبة بحبة.",
            keyReactions = listOf(
                "Initiation: Binding of 40S subunit to 5' cap and AUG codon",
                "Elongation: Peptide bond formation catalyzed by 28S rRNA ribozyme",
                "Termination: Release factors recognizing UAA, UAG, or UGA stop codons"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "ما هو الرابط الإنزيمي الأساسي الذي يقوم الريبوسوم بتكوينه أثناء ترجمة سلسلة عديد الببتيد؟",
                questionEnglish = "What basic bond does the ribosome form during polypeptide translation?",
                options = listOf(
                    "رابطة فسفودايستر ثلاثية",
                    "رابطة ببتيدية بين مجموعة الكربوكسيل والأمينو",
                    "رابطة هيدروجينية بين القواعد",
                    "رابطة جليكوسيدية بين جزيئات السكر"
                ),
                correctIndex = 1,
                explanationArabic = "يقوم الريبوسوم بربط مجموعة الكربوكسيل للحمض الأميني الأول مع مجموعة الأمين للحمض الأميني التالي عبر رابطة ببتيدية.",
                ministryTip = "إنزيم ببتيديل ترانسفيراز هو في الواقع حمض نووي ريبوزي ريبوسومي محفز (Ribozyme)."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = true
        ),

        OrganelleType.LYSOSOMES to OrganelleDetail(
            type = OrganelleType.LYSOSOMES,
            englishName = "Lysosomes",
            arabicName = "اللايسوسومات",
            roleBadge = "منظومة الهضم والتحلل الخلوي والدفاع المناعي",
            primaryFunction = "هضم المركبات الغذائية الكبيرة، وإتلاف العضيات الهرمة والتالفة (Autophagy)، وتدمير الميكروبات التي تبتلعها خلايا الدم البيضاء البلعمية.",
            detailedStructure = "حويصلات غشائية مفردة تنشأ من جهاز جولجي، تحتوي بداخلها على أكثر من 50 إنزيماً هاضماً محللاً تعمل في وسط حمضي (pH ~ 5) مدعوم بمضخات البروتون H+.",
            biochemicalRole = "تنشط إنزيمات البروتياز والليبيز والنوكلياز عند اندماج اللايسوسوم مع فجوة بلعمية (Phagosome) لتكوين اللايسوسوم الثانوي (Phagolysosome).",
            ministryExamTrap = "فخ امتحان: لماذا لا تهضم إنزيمات اللايسوسوم الخلية نفسها إذا تسربت إلى السيتوبلازم؟ لأن وسط السيتوبلازم متعادل يميل للقلوية الخفيفة (pH 7.2) مما يعطل نشاط تلك الإنزيمات التي لا تنشط إلا في وسط حمضي منخفض (pH 5.0).",
            eli5Analogy = "مثل أكياس التطهير والمحرقة ومعدة الخلية التي تحلل المواد الضارة لإعادة تدويرها.",
            keyReactions = listOf(
                "Proton pumping by V-ATPase maintaining lumen pH ~ 4.5-5.0",
                "Hydrolytic cleavage of ingested pathogenic antigens",
                "Autophagic recycling of depolarized mitochondria (Mitophagy)"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "أي الخلايا المناعية التالية يعتمد عملها بصورة أساسية على وفرة إنزيمات اللايسوسومات بداخلها؟",
                questionEnglish = "Which immune cell heavily relies on lysosomal abundance for its function?",
                options = listOf(
                    "الخلية البائية البلازمية",
                    "الخلية البلعمية الكبيرة (Macrophage)",
                    "الخلية التائية المساعدة (Th)",
                    "كرية الدم الحمراء غير المنواة"
                ),
                correctIndex = 1,
                explanationArabic = "تقوم الخلايا البلعمية الكبيرة بابتلاع الميكروب ثم دمجه مع اللايسوسومات لتفتيته وعرض أنتيجيناته على بروتين توافق الأنسجة MHC.",
                ministryTip = "وفرة اللايسوسومات في الخلايا البلعمية شرط أساسي لهضم الأجسام الغريبة."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = false
        ),

        OrganelleType.CENTROSOME to OrganelleDetail(
            type = OrganelleType.CENTROSOME,
            englishName = "Centrosome & Centrioles",
            arabicName = "الجسم المركزي (السنتريولان)",
            roleBadge = "منظم ألياف المغزل والانقسام الخلوي",
            primaryFunction = "تكوين خيوط المغزل (Spindle fibers) أثناء الانقسام الميتوزي والميوزي لشد الكروماتيدات نحو قطبي الخلية، ويدخل في بناء الأهداب والأسواط.",
            detailedStructure = "يتكون من جسمين أسطوانيين متعامدين يسميان السنتريولين (Centrioles)، يتكون كل سنتريول من 9 مجموعات ثلاثية من الأنيبيبات الدقيقة (9x3 = 27 أنيبيبة).",
            biochemicalRole = "يعمل كمركز لتنظيم الأنيبيبات الدقيقة (MTOC) بواسطة بلمرة بروتين التيوبيولين (Tubulin) بمساعدة طاقة GTP.",
            ministryExamTrap = "فخ امتحان: الخلايا النباتية ومعظم الفطريات وخلايا الأعصاب الناضجة تخلو من الجسم المركزي! ورغم ذلك تنقسم الخلايا النباتية بنجاح لتجمع خيوط المغزل من تكثف بروتينات السيتوبلازم عند القطبين.",
            eli5Analogy = "مثل الرافعات التي تشد الحبال الفولاذية لسحب أجزاء المبنى وفصلها في نصفين متساويين.",
            keyReactions = listOf(
                "Duplication during S-phase of the cell cycle",
                "Nucleation of alpha/beta tubulin heterodimers into microtubules",
                "Kinetochore attachment and anaphase chromatid segregation"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "كيف تنقسم الخلية النباتية بنجاح على الرغم من غياب الجسم المركزي (السنتروسوم) فيها؟",
                questionEnglish = "How do plant cells divide despite lacking a centrosome?",
                options = listOf(
                    "تعتمد على جهاز جولجي في تكوين ألياف المغزل",
                    "تتجمع خيوط المغزل من تكثف بروتينات السيتوبلازم عند قطبي الخلية",
                    "لا تنقسم الخلايا النباتية ميتوزياً على الإطلاق",
                    "تستخدم البلاستيدات الخضراء كمركز بديل لتنظيم المغزل"
                ),
                correctIndex = 1,
                explanationArabic = "تتميز الخلية النباتية بوجود بروتينات سيتوبلازمية عند القطبين تتكثف وتتبلمر لتكوين ألياف المغزل دون الحاجة لسنتريولات.",
                ministryTip = "الخلية النباتية تنقسم بتكثف السيتوبلازم، وتكوين الصفيحة الوسطى بدلاً من الاختناق."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = false
        ),

        OrganelleType.CELL_MEMBRANE to OrganelleDetail(
            type = OrganelleType.CELL_MEMBRANE,
            englishName = "Plasma Membrane",
            arabicName = "الغشاء البلازمي للخلية",
            roleBadge = "حاجز النفاذية الاختيارية والتواصل الخلوي",
            primaryFunction = "فصل محتويات الخلية عن البيئة الخارجية، والتحكم الصارم في دخول وخروج المواد عبر خاصية النفاذية الاختيارية (Selective Permeability).",
            detailedStructure = "نموذج الفسيفساء السائلة (Fluid Mosaic Model): طبقتان من الفسفوليبيدات برؤوس محبة للماء وذيول كارهة للماء، تتخللها جزيئات بروتينات مدمجة، وجزيئات كوليسترول تحافظ على السيولة.",
            biochemicalRole = "يحتوي على قنوات أيونية، ومستقبلات هرمونية، ومضخات نقل نشط (مثل مضخة Na+/K+ ATPase)، وبروتينات توافق أنسجة للتعرف المناعي.",
            ministryExamTrap = "فخ امتحان: الكوليسترول في غشاء الخلية الحيوانية يعمل كمنظم للسيولة (Fluidity buffer)؛ يمنع تجمد الغشاء في البرودة ويمنع تفككه في الحرارة العالية، ولا يوجد كوليسترول في أغشية البكتيريا والنباتات.",
            eli5Analogy = "مثل بوابات الحراسة الذكية المزودة بحساسات أمنية تسمح بدخول الأعضاء فقط وتمنع الدخلاء.",
            keyReactions = listOf(
                "Active transport via Na+/K+ ATPase maintaining membrane potential",
                "Receptor-mediated signal transduction across the bilayer",
                "Exocytosis and endocytosis vesicular fusion"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "ما الخاصية الفيزيائية-الكيميائية التي تجعل طبقة الفسفوليبيدات في غشاء الخلية حاجزاً فعالاً للماء؟",
                questionEnglish = "What property makes the phospholipid bilayer an effective water barrier?",
                options = listOf(
                    "وجود جزيئات السليلوز الصلبة",
                    "تقابل الذيول الهيدروكربونية الكارهة للماء في منتصف الطبقة المزدوجة",
                    "ارتباط البروتينات بروابط تساهمية قوية",
                    "تشبع الغشاء الكامل بأيونات الصوديوم"
                ),
                correctIndex = 1,
                explanationArabic = "تتجه الذيول الكارهة للماء نحو الداخل مبتعدة عن المحلول المائي، مما يشكل طبقة عازلة تمنع المرور الحر للمواد الذائبة.",
                ministryTip = "المواد القابلة للذوبان في الدهون تعبر الغشاء أسرع بكثير من المواد القطبية والماء."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = true
        ),

        OrganelleType.CYTOSKELETON to OrganelleDetail(
            type = OrganelleType.CYTOSKELETON,
            englishName = "Cytoplasm & Cytoskeleton",
            arabicName = "السيتوبلازم والهيكل الخلوي",
            roleBadge = "الدعامة الهيكلية والمسارات الحركية الداخلية",
            primaryFunction = "إعطاء الخلية شكلاً محدداً، وتثبيت العضيات في مواقعها، وتوفير سكك حركية لانتقال الحويصلات والمواد، وحركة الانسياب السيتوبلازمي المستمرة.",
            detailedStructure = "يتكون من السيتوسول (سائل هلامي مائي غني بالأيونات والإنزيمات)، وشبكة من الخيوط البروتينية: الأنيبيبات الدقيقة (Microtubules)، والخيوط الدقيقة (Microfilaments - Actin)، والخيوط المتوسطة.",
            biochemicalRole = "الحركة الانسيابية السيتوبلازمية (Cyclosis) مدفوعة ببروتينات الميوسين والأكتين، وتتحرك الحويصلات على مسارات التيوبيولين بواسطة بروتينات الكينيسين والداينين.",
            ministryExamTrap = "فخ امتحان: الحركة الدورانية السيتوبلازمية في خلايا أوراق نبات الإيلوديا (Elodea) المائي دليل ساطع على حيوية الخلية، ويمكن الاستدلال عليها بحركة البلاستيدات الخضراء المحمولة مع تيار السيتوبلازم.",
            eli5Analogy = "مثل الأعمدة والسكك الحديدية الداخلية التي تحافظ على تماسك ناطحة السحاب وتنقل المصاعد.",
            keyReactions = listOf(
                "Cytoplasmic streaming (Cyclosis) driving nutrient circulation",
                "Kinesin anterograde transport of secretory vesicles towards plus-end",
                "Actin-myosin contractility during cell motility and amoeboid movement"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "كيف يمكن الاستدلال مجهرياً على حدوث الحركة الدورانية السيتوبلازمية في خلية نبات الإيلوديا؟",
                questionEnglish = "How is cytoplasmic streaming observed microscopically in Elodea plant cells?",
                options = listOf(
                    "بانكماش الجدار الخلوي السليلوزي",
                    "بحركة دوران البلاستيدات الخضراء المنسابة مع تيار السيتوبلازم في اتجاه واحد",
                    "بانقسام النواة إلى نواتين وليدتين",
                    "بخروج الماء عبر الثغور"
                ),
                correctIndex = 1,
                explanationArabic = "البلاستيدات الخضراء جزيئات مرئية بوضوح تنحرف وتدور مع تيار السيتوبلازم المستمر مما يثبت حدوث الحركة الخلوية الدائبة.",
                ministryTip = "الانسياب السيتوبلازمي حركة موضعية مستمرة تميز الخلايا الحية فقط."
            ),
            isPresentInAnimal = true,
            isPresentInPlant = true
        ),

        OrganelleType.CHLOROPLAST to OrganelleDetail(
            type = OrganelleType.CHLOROPLAST,
            englishName = "Chloroplast",
            arabicName = "البلاستيدات الخضراء",
            roleBadge = "محول الطاقة الضوئية إلى كيميائية (البناء الضوئي)",
            primaryFunction = "امتصاص الطاقة الضوئية للشمس بواسطة صبغات الكلوروفيل وتحويلها إلى طاقة كيميائية مخزنة في جزيئات الجلوكوز والنشا.",
            detailedStructure = "غشاء مزدوج يحيط بـ السترومات (أرضية الحشوة عديمة اللون)، وأقراص الثايلاكويد المتراصة في حبيبات الجرانا (Grana)، وتحتوي على DNA حلقي وريبوسومات 70S وحبيبات نشا.",
            biochemicalRole = "تفاعلات الضوء في أغشية الثايلاكويد (انشطار الماء وإنتاج ATP وNADPH)، وتفاعلات الظلام (دورة كالفن) في الستروما لتثبيت CO2 وتكوين سكر PGAL.",
            ministryExamTrap = "فخ امتحان: أول مركب كيميائي ثابت ناتج عن عملية البناء الضوئي هو فوسفوجليسرالدهيد (PGAL) ذو الـ 3 ذرات كربون، وليس سكر الجلوكوز مباشرة كما يعتقد الكثير من الطلاب!",
            eli5Analogy = "مثل الألواح الشمسية ومزارع الطاقة النظيفة التي تحول أشعة الشمس إلى طعام ووقود.",
            keyReactions = listOf(
                "Photolysis of water: 2H2O -> 4H+ + 4e- + O2",
                "Photophosphorylation: Light-driven ATP & NADPH formation",
                "Calvin Cycle: CO2 fixation by RuBisCO into PGAL sugar intermediates"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "ما هو أول مركب كيميائي ثابت ثابت تجريبياً ينتج عن دورة كالفن لتثبيت ثاني أكسيد الكربون؟",
                questionEnglish = "What is the first stable chemical compound formed during the Calvin cycle?",
                options = listOf(
                    "سكر الجلوكوز سداسي الكربون",
                    "فوسفوجليسرالدهيد (PGAL) ثلاثي الكربون",
                    "حمض البيروفيك",
                    "حبيبات النشا المعقدة"
                ),
                correctIndex = 1,
                explanationArabic = "أثبت العالم ملفن كالفن باستخدام طحلب الكلوريلا والكربون المشع C14 أن أول مركب كيميائي ثابت هو PGAL المتكون بعد ثانيتين فقط.",
                ministryTip = "PGAL هو المادة البادئة لتخليق الجلوكوز والنشا والدهون والبروتينات في النبات."
            ),
            isPresentInAnimal = false,
            isPresentInPlant = true
        ),

        OrganelleType.CENTRAL_VACUOLE to OrganelleDetail(
            type = OrganelleType.CENTRAL_VACUOLE,
            englishName = "Central Vacuole",
            arabicName = "الفجوة العصارية المركزية",
            roleBadge = "منظم الضغط الإسموزي والدعامة الفسيولوجية",
            primaryFunction = "تخزين الماء والأملاح والمغذيات، وخلق الضغط الإسموزي الداخلي الذي يولد الدعامة الفسيولوجية في الخلية النباتية ويحفظ استقامتها.",
            detailedStructure = "فجوة عصارية مفردة عملاقة تحتل أكثر من 80% من حجم الخلية النباتية البالغة، محاطة بغشاء تونوبلاست (Tonoplast) اختياري النفاذية وتملؤها عصارة خلوية مركزة.",
            biochemicalRole = "امتصاص الماء بالخاصية الإسموزية فيزداد حجم العصير الخلوي ويضغط على البروتوبلازم ويدفعه للخارج نحو الجدار الخلوي فيتوتر الجدار ويكتسب الدعامة.",
            ministryExamTrap = "فخ امتحان: فقد الدعامة الفسيولوجية عند جفاف التربة سببه خروج الماء من الفجوة العصارية بالإسموزية فيقل حجمها وينكمش البروتوبلازم مبتعداً عن الجدار الخلوي (Plasmolysis البلزمة).",
            eli5Analogy = "مثل بالون الماء الكبير داخل صندوق كرتوني؛ كلما امتلأ بالماء شد جدران الصندوق وجعلها صلبة وقائمة.",
            keyReactions = listOf(
                "Osmotic water influx driven by high internal solute concentration",
                "Generation of turgor pressure against cell wall (Physiological Support)",
                "Proton-coupled secondary active transport of amino acids and ions"
            ),
            microQuiz = CellMicroQuiz(
                questionArabic = "ما هو التغير الذي يحدث في الخلية النباتية عند وضعها في محلول عالي التركيز من السكروز؟",
                questionEnglish = "What happens to a plant cell placed in a hypertonic sucrose solution?",
                options = listOf(
                    "تنتفخ الخلية وتنفجر لزيادة الضغط",
                    "يخرج الماء من الفجوة العصارية فتنكمش وتفقد دعامتها الفسيولوجية (بلزمة)",
                    "تزداد سرعة الانقسام الميتوزي",
                    "يتضاعف سمك الجدار الخلوي فجأة"
                ),
                correctIndex = 1,
                explanationArabic = "يتحرك الماء من الوسط الأقل تركيزاً للذائبات (داخل الفجوة) إلى الوسط الأعلى تركيزاً (المحلول الخارجي) بالإسموزية فتفقد الخلية انتفاخها.",
                ministryTip = "الدعامة الفسيولوجية مؤقتة وتعتمد كلياً على حركة الماء بالإسموزية إلى الفجوة العصارية."
            ),
            isPresentInAnimal = false,
            isPresentInPlant = true
        )
    )

    fun getOrganellesForCell(cellType: CellType): List<OrganelleDetail> {
        return organelleDetails.values.filter { detail ->
            when (cellType) {
                CellType.ANIMAL_CELL -> detail.isPresentInAnimal
                CellType.PLANT_CELL -> detail.isPresentInPlant
            }
        }
    }

    fun get3DInstances(cellType: CellType): List<Organelle3DInstance> {
        val list = mutableListOf<Organelle3DInstance>()

        // 1. Nucleus and Nucleolus at center
        list.add(
            Organelle3DInstance(
                id = "nucleus_main",
                type = OrganelleType.NUCLEUS,
                localPosition = Vector3D(0f, 0f, 0f),
                baseRadius = 52f
            )
        )
        list.add(
            Organelle3DInstance(
                id = "nucleolus_core",
                type = OrganelleType.NUCLEOLUS,
                localPosition = Vector3D(12f, -8f, 15f),
                baseRadius = 18f
            )
        )

        // 2. Rough ER wrapped around nucleus
        list.add(
            Organelle3DInstance(
                id = "rer_upper",
                type = OrganelleType.ROUGH_ER,
                localPosition = Vector3D(-38f, -48f, 10f),
                baseRadius = 34f
            )
        )
        list.add(
            Organelle3DInstance(
                id = "rer_lower",
                type = OrganelleType.ROUGH_ER,
                localPosition = Vector3D(-50f, 25f, -15f),
                baseRadius = 30f
            )
        )

        // 3. Smooth ER nearby
        list.add(
            Organelle3DInstance(
                id = "ser_wing",
                type = OrganelleType.SMOOTH_ER,
                localPosition = Vector3D(-80f, -20f, -25f),
                baseRadius = 26f
            )
        )

        // 4. Golgi Apparatus
        list.add(
            Organelle3DInstance(
                id = "golgi_main",
                type = OrganelleType.GOLGI_APPARATUS,
                localPosition = Vector3D(58f, 38f, 20f),
                baseRadius = 36f
            )
        )

        // 5. Mitochondria (multiple instances in 3D orbit)
        list.add(
            Organelle3DInstance(
                id = "mito_1",
                type = OrganelleType.MITOCHONDRIA,
                localPosition = Vector3D(70f, -45f, -30f),
                baseRadius = 24f,
                rotationOffset = 45f
            )
        )
        list.add(
            Organelle3DInstance(
                id = "mito_2",
                type = OrganelleType.MITOCHONDRIA,
                localPosition = Vector3D(-30f, 75f, 35f),
                baseRadius = 22f,
                rotationOffset = -30f
            )
        )
        list.add(
            Organelle3DInstance(
                id = "mito_3",
                type = OrganelleType.MITOCHONDRIA,
                localPosition = Vector3D(20f, -80f, 15f),
                baseRadius = 20f,
                rotationOffset = 80f
            )
        )

        // 6. Ribosomes clusters
        list.add(
            Organelle3DInstance(
                id = "ribosomes_cluster",
                type = OrganelleType.RIBOSOMES,
                localPosition = Vector3D(-15f, -65f, 40f),
                baseRadius = 16f
            )
        )

        // 7. Animal specifics
        if (cellType == CellType.ANIMAL_CELL) {
            list.add(
                Organelle3DInstance(
                    id = "lyso_1",
                    type = OrganelleType.LYSOSOMES,
                    localPosition = Vector3D(45f, -20f, 60f),
                    baseRadius = 18f,
                    isAnimalOnly = true
                )
            )
            list.add(
                Organelle3DInstance(
                    id = "centrosome_pair",
                    type = OrganelleType.CENTROSOME,
                    localPosition = Vector3D(15f, 48f, -35f),
                    baseRadius = 20f,
                    isAnimalOnly = true
                )
            )
        }

        // 8. Plant specifics
        if (cellType == CellType.PLANT_CELL) {
            list.add(
                Organelle3DInstance(
                    id = "chloroplast_1",
                    type = OrganelleType.CHLOROPLAST,
                    localPosition = Vector3D(-65f, 50f, 30f),
                    baseRadius = 28f,
                    isPlantOnly = true
                )
            )
            list.add(
                Organelle3DInstance(
                    id = "chloroplast_2",
                    type = OrganelleType.CHLOROPLAST,
                    localPosition = Vector3D(65f, -30f, 45f),
                    baseRadius = 26f,
                    isPlantOnly = true
                )
            )
            list.add(
                Organelle3DInstance(
                    id = "central_vacuole_huge",
                    type = OrganelleType.CENTRAL_VACUOLE,
                    localPosition = Vector3D(40f, 20f, -40f),
                    baseRadius = 48f,
                    isPlantOnly = true
                )
            )
        }

        return list
    }
}
