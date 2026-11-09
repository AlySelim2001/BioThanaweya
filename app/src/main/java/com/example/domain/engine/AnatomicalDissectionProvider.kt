package com.example.domain.engine

import com.example.domain.model.AnatomicalDissectionLayer
import com.example.domain.model.AnatomicalDissectionProtocol

object AnatomicalDissectionProvider {

    private val protocols = mapOf(
        "sarcomere_contraction" to AnatomicalDissectionProtocol(
            topicId = "sarcomere_contraction",
            organOrSystemArabic = "التشريح المجهري المتدرج للحزم والألياف العضلية والقطعة العضلية",
            organOrSystemEnglish = "Multi-Layered Skeletal Muscle Bundle & Sarcomere Architecture",
            macroscopicOverview = "من العضلة الهيكلية الكاملة المغلفة بنسيج ضام (Epimysium)، مروراً بالحزم العضلية المحاطة بغشاء الحزمة (Perimysium)، إلى الليفة العضلية المدمجة خلوياً (Sarcolemma) وصولاً للييفات والقطع العضلية.",
            dissectionSteps = listOf(
                "الخطوة 1: إجراء شق طولي عبر غشاء العضلة الخارجي (Epimysium) لفصل حزم الألياف العضلية (Fascicles).",
                "الخطوة 2: عزل ليفة عضلية فردية محاطة بغشاء الساركوليما الحساس للمحفزات العصبية، وملاحظة تعدد الأنوية الطرفية.",
                "الخطوة 3: إزالة الساركوبلازم للكشف عن مصفوفة اللييفات العضلية (Myofibrils) المتراصة (1000 - 2000 لييفة لكل ليفة).",
                "الخطوة 4: تحت المجهر الإلكتروني، تكبير القطعة العضلية (Sarcomere) المحصورة بين خطي Z متتاليين."
            ),
            microscopicLayers = listOf(
                AnatomicalDissectionLayer(
                    layerNumber = 1,
                    layerNameArabic = "المنطقة المضيئة (I-Band) وخط Z",
                    layerNameEnglish = "Light I-Band & Z-Line Anchor",
                    histologicalDescription = "تتكون فقط من خيوط الأكتين الرفيعة المرتبطة ببروتين خط Z المتعرج، خالية تماماً من الميوسين.",
                    magnificationLevel = "15,000x TEM",
                    clinicalExamRelevance = "يقل طولها أثناء الانقباض وتقترب خطوط Z من بعضها؛ والمنطقة I تقع نصفها في قطعة عضلية ونصفها في المجاورة."
                ),
                AnatomicalDissectionLayer(
                    layerNumber = 2,
                    layerNameArabic = "المنطقة الداكنة (A-Band) مع خيوط الميوسين",
                    layerNameEnglish = "Dark A-Band & Thick Myosin Filaments",
                    histologicalDescription = "تحتوي على خيوط الميوسين السميكة ذات الرؤوس المزدوجة المتداخلة جزئياً مع أطراف خيوط الأكتين.",
                    magnificationLevel = "25,000x TEM",
                    clinicalExamRelevance = "يظل طولها ثابتاً تماماً في حالتي الانقباض والانبساط لأن طول خيوط الميوسين ذاتها لا يتغير."
                ),
                AnatomicalDissectionLayer(
                    layerNumber = 3,
                    layerNameArabic = "المنطقة شبه المضيئة (H-Zone) وخط M",
                    layerNameEnglish = "H-Zone & M-Line Center",
                    histologicalDescription = "منطقة مركزية تتكون من خيوط الميوسين السميكة فقط بدون أي تداخل مع خيوط الأكتين.",
                    magnificationLevel = "30,000x TEM",
                    clinicalExamRelevance = "يقل اتساعها تدريجياً أثناء الانقباض العضلي حتى تنعدم كلياً عند الانقباض التام (أقصى تقارب)."
                ),
                AnatomicalDissectionLayer(
                    layerNumber = 4,
                    layerNameArabic = "الصهاريج العضلية والأنيبيبات T",
                    layerNameEnglish = "Sarcoplasmic Reticulum & T-Tubules Triad",
                    histologicalDescription = "شبكة مفرطة التفرع تخزن أيونات Ca²⁺ بمضخات نشطة، ملاصقة لانغمادات غشاء الساركوليما.",
                    magnificationLevel = "40,000x Cryo-EM",
                    clinicalExamRelevance = "مكان تحرر الكالسيوم عند وصول السيال؛ نفاد ATP يمنع المضخة من استعادة الكالسيوم مسبباً الشد العضلي."
                )
            ),
            openSource3DParameters = "Three.js / Blender 3D Model: Skeletal_Muscle_Sarcomere_Rigged.gltf | Camera: OrbitControls(fov: 45, distance: 3.2m) | MorphTargets: [ContractionState: 0.0 -> 1.0] | Shaders: PBR Muscle Striation with Actin-Myosin normal maps."
        ),
        "plant_human_support" to AnatomicalDissectionProtocol(
            topicId = "plant_human_support",
            organOrSystemArabic = "التشريح المقطعي للفقرة العظمية العظمية وجدار الخلية النباتية",
            organOrSystemEnglish = "Vertebral Column Anatomy & Plant Cell Wall Layering",
            macroscopicOverview = "المقارنة البنيوية بين طبقات جدار الخلية النباتية المترسبة بمواد السليلوز واللجنين، والتشريح العظمي للفقرة العظمية النموذجية الحامية للحبل الشوكي.",
            dissectionSteps = listOf(
                "الخطوة 1: فحص جسم الفقرة العظمية (Centrum) السميك المتجه للأمام لتحمل ثقل الجسم وتثبيت الأقراص الغضروفية.",
                "الخطوة 2: تتبع القوس العصبي العظمي (Neural Arch) المتجه للخلف والمحيط بالقناة العصبية لحماية النخاع الشوكي.",
                "الخطوة 3: عزل النتوءات السبعة: نتوءان مستعرضان جانبيان، نتوء شوكي خلفي مائل لأسفل، وأربعة نتوءات مفصلية (2 أماميان و2 خلفيان).",
                "الخطوة 4: في الخلية النباتية، تشريح الطبقات الثلاث لجدار الخلية: الصفيحة الوسطى، الجدار الابتدائي، والجدار الثانوي المدعم باللجنين."
            ),
            microscopicLayers = listOf(
                AnatomicalDissectionLayer(
                    layerNumber = 1,
                    layerNameArabic = "القناة العصبية والحبل الشوكي",
                    layerNameEnglish = "Neural Canal & Spinal Cord Enclosure",
                    histologicalDescription = "فراغ أنبوبي عظمي تشكله أقواس الفقرات المتتالية، مبطن بالأغشية السحائية ومملوء بالسائل الدماغي الشوكي.",
                    magnificationLevel = "Gross Anatomy & 100x Histology",
                    clinicalExamRelevance = "حماية الحبل الشوكي ومسار خروج الأعصاب الشوكية؛ انزلاق الغضروف يضغط على الأعصاب ويسبب الشلل أو الألم."
                ),
                AnatomicalDissectionLayer(
                    layerNumber = 2,
                    layerNameArabic = "النتوءات المفصلية الأمامية والخلفية",
                    layerNameEnglish = "Articular Processes (Zygapophyses)",
                    histologicalDescription = "أسطح تمفصل زلالية ملساء مغطاة بغضروف زجاجي تمنع انزلاق الفقرات وتحدد مدى انحناء العمود الفقري.",
                    magnificationLevel = "Macro 5x",
                    clinicalExamRelevance = "الفقرة رقم n تتمفصل مع الفقرة (n-1) بالنتوءين المفصليين الأماميين، ومع الفقرة (n+1) بالنتوءين المفصليين الخلفيين."
                ),
                AnatomicalDissectionLayer(
                    layerNumber = 3,
                    layerNameArabic = "الجدار الثانوي والألياف الإسكلرنشيمية",
                    layerNameEnglish = "Secondary Wall & Sclerenchyma Fibers",
                    histologicalDescription = "ترسيب كثيف لمادة اللجنين غير المنفذة للماء على شبكة السليلوز، مع موت البروتوبلازم الداخلي.",
                    magnificationLevel = "1,000x Polarized Light",
                    clinicalExamRelevance = "دعامة تركيبية دائمة غير قابلة للذبول؛ تعطي قوة ميكانيكية لأوعية الخشب والألياف والخلايا الحجرية."
                )
            ),
            openSource3DParameters = "Three.js / Model-Viewer: Lumbar_Vertebra_Anatomy_L3.glb | Annotations: [Centrum, Neural_Canal, Transverse_Process, Spinous_Process, Articular_Facets] | Lighting: HemisphereLight + Directional Accent."
        ),
        "dna_structure_replication" to AnatomicalDissectionProtocol(
            topicId = "dna_structure_replication",
            organOrSystemArabic = "التشريح الفراغي النانوي للولب المزدوج وشوكة التضاعف",
            organOrSystemEnglish = "Nanoscale Structural Anatomy of DNA Double Helix & Replication Complex",
            macroscopicOverview = "الهندسة النانوية للولب المزدوج (عرض 2 نانومتر، اللفة الكاملة 3.4 نانومتر تحوي 10 أزواج من النيوكليوتيدات)، مع تشريح مركب إنزيمات التضاعف (Replisome).",
            dissectionSteps = listOf(
                "الخطوة 1: فحص هيكل سكر-فوسفات الخارجي غير المتماثل، وروابط الفوسفات ثنائية الإستر بين ذرة الكربون 3' و 5'.",
                "الخطوة 2: الدخول للتجويف الداخلي للحلزون لرصد درجات السلم المكونة من القواعد النيتروجينية المتعامدة على الهيكل.",
                "الخطوة 3: تشريح إنزيم اللولب (Helicase) الحلقي أثناء اختراقه وفكه للروابط الهيدروجينية بدقة وسرعة.",
                "الخطوة 4: رصد إنزيم بلمرة DNA وهو يوجه نيوكليوتيدات ثلاثية الفوسفات نحو الطرف 3' الحر للشريط النامي."
            ),
            microscopicLayers = listOf(
                AnatomicalDissectionLayer(
                    layerNumber = 1,
                    layerNameArabic = "البيورينات والبيريميدينات المتكاملة",
                    layerNameEnglish = "Purine-Pyrimidine Base Pairs (A=T, G≡C)",
                    histologicalDescription = "قاعدة ثنائية الحلقة (بيورين: A أو G) ترتبط دائماً بقاعدة أحادية الحلقة (بيريميدين: T أو C) لتثبيت عرض اللولب عند 2 نانومتر.",
                    magnificationLevel = "Atomic Resolution (1.2 Å X-ray Crystallography)",
                    clinicalExamRelevance = "ثبات قطر جزيء DNA يفسر لماذا لا يرتبط بيورين ببيورين (يكون عريضاً) أو بيريميدين ببيريميدين (يكون ضيقاً)."
                ),
                AnatomicalDissectionLayer(
                    layerNumber = 2,
                    layerNameArabic = "هيكل سكر-فوسفات المتعاكس (Antiparallel)",
                    layerNameEnglish = "Antiparallel Sugar-Phosphate Backbones",
                    histologicalDescription = "الشريط الأول يتجه من 5' (فوسفات طليقة) إلى 3' (هيدروكسيل طليقة)، والآخر معاكس من 3' إلى 5'.",
                    magnificationLevel = "Atomic Force Microscopy (AFM)",
                    clinicalExamRelevance = "التعاكس ضروري لتكوين روابط هيدروجينية صحيحة ومستقرة بين القواعد المتجهة للداخل."
                )
            ),
            openSource3DParameters = "Three.js / NGL Molecular Viewer: PDB_ID_1BNA_B_DNA.cif | Representation: Cartoon + Spacefill Nucleotides | ColorScheme: Purine(Blue), Pyrimidine(Orange), Phosphate(Gold)."
        )
    )

    fun getProtocolForTopic(topicId: String): AnatomicalDissectionProtocol {
        return protocols[topicId] ?: protocols["sarcomere_contraction"]!!
    }
}
