/// Thanaweya Amma Experimental Schools Biology Curriculum (Units 1 & 2)
/// Complete 11 chapters and topics for high-yield mastery.
class CurriculumTopic {
  final String id;
  final int unitNumber;
  final String chapterTitle;
  final String titleArabic;
  final String titleEnglish;
  final String eli5Summary;
  final String technicalSummary;
  final String asciiDiagram;
  final List<String> examTraps;
  final double defaultMastery;

  const CurriculumTopic({
    required this.id,
    required this.unitNumber,
    required this.chapterTitle,
    required this.titleArabic,
    required this.titleEnglish,
    required this.eli5Summary,
    required this.technicalSummary,
    required this.asciiDiagram,
    required this.examTraps,
    this.defaultMastery = 0.65,
  });
}

class CurriculumData {
  static const List<CurriculumTopic> topics = [
    // Unit 1: Structure and Function in Living Organisms
    CurriculumTopic(
      id: 'support_movement',
      unitNumber: 1,
      chapterTitle: 'Chapter 1: Support and Movement',
      titleArabic: 'الدعامة والحركة في الكائنات الحية',
      titleEnglish: 'Support and Movement in Living Organisms',
      eli5Summary: 'تخيل الخلية النباتية كالبالون الممتلئ بالماء (دعامة فسيولوجية مؤقتة) أو كجدار الطوب الإسمنتي (دعامة تركيبية دائمة ترسب فيها اللجنين والكيوتين). في الإنسان، العضلات والأوتار والأربطة تعمل كالروافع الميكانيكية لتحريك الهيكل العظمي بدقة متناهية.',
      technicalSummary: 'Physiological support relies on water osmosis inflating the vacuole to press cytoplasm against the cellulose wall (turgor pressure). Structural support involves irreversible deposition of cellulose, lignin, suberin, and cutin. In muscles, sliding filament theory involves actin and myosin cross-bridges energized by ATP hydrolysis and regulated by Ca2+ ions binding to troponin.',
      asciiDiagram: '''
  [Plant Cell Osmosis: Physiological Support]
     H2O IN ---> [ Vacuole Expands ] ---> [ Turgor Pressure on Wall ]
  
  [Sarcomere Muscle Contraction]
  |--- Z-Line --- [ Actin Filament ] <== Cross-Bridges (Myosin + ATP + Ca2+) ==> [ Actin ] --- Z-Line ---|
''',
      examTraps: [
        'Cutin and Suberin prevent water loss, but Lignin also provides mechanical hardness and lines xylem vessels.',
        'During muscle contraction, A-band length NEVER changes, while I-band and H-zone shorten.',
        'ATP is required for BOTH cross-bridge attachment and detachment. Depletion leads to severe muscle fatigue and tetany.'
      ],
      defaultMastery: 0.78,
    ),
    CurriculumTopic(
      id: 'nutrition',
      unitNumber: 1,
      chapterTitle: 'Chapter 2: Nutrition and Digestion',
      titleArabic: 'التغذية والهضم في الكائنات الحية',
      titleEnglish: 'Nutrition and Digestion in Living Organisms',
      eli5Summary: 'النبات يصنع طعامه في مصنع الورقة باستخدام أشعة الشمس وثاني أكسيد الكربون (تغذية ذاتية)، بينما الإنسان يهضم جزيئات الطعام المعقدة بواسطة إنزيمات متخصصة ليمتصها الدم عبر الخملات المعوية.',
      technicalSummary: 'Autotrophic photosynthesis: Light-dependent reactions photolyze H2O in thylakoids yielding ATP and NADPH; Dark reactions (Calvin cycle) in stroma synthesize PGAL (phosphoglyceraldehyde) as the first stable 3-carbon carbohydrate. Heterotrophic digestion involves ptyalin, pepsinogen activation by HCl to pepsin, trypsin, lipase, and bile emulsification.',
      asciiDiagram: '''
  [Photosynthesis Light vs Dark Reaction]
  Light + H2O ---> [Thylakoid Grana] ---> O2 + ATP + NADPH
                        |
                        V
  CO2 + ATP + NADPH ---> [Stroma Calvin Cycle] ---> PGAL ---> Glucose
''',
      examTraps: [
        'PGAL (3 carbons) is the first stable chemical compound produced in photosynthesis, NOT glucose.',
        'Bile contains NO digestive enzymes; it merely emulsifies fats into micro-droplets to increase lipase surface area.'
      ],
      defaultMastery: 0.72,
    ),
    CurriculumTopic(
      id: 'transport',
      unitNumber: 1,
      chapterTitle: 'Chapter 3: Transport in Living Organisms',
      titleArabic: 'النقل في الكائنات الحية',
      titleEnglish: 'Transport in Living Organisms',
      eli5Summary: 'خشب النبات ينقل الماء لأعلى مثل مصاصة عملاقة بفضل قوة الشد الناتجة عن النتح والتماسك بين جزيئات الماء. وفي الإنسان، القلب يضخ الدم المحمل بالأكسجين عبر شبكة شرايين وأوردة متكاملة.',
      technicalSummary: 'Xylem sap ascent is governed by Dixon and Joly\'s Transpiration-Cohesion-Tension theory. Phloem translocation follows Munch\'s Mass Flow Hypothesis using active sieve tube loading via companion cell plasmodesmata. Human circulation involves double closed circulation, cardiac pacemaker nodal rhythm (SA node -> AV node -> Bundle of His -> Purkinje fibers).',
      asciiDiagram: '''
  [Cardiac Conduction Pathway]
  SA Node (Pacemaker) ---> AV Node ---> Bundle of His ---> Purkinje Fibers ---> Ventricular Systole
''',
      examTraps: [
        'Xylem vessels are dead hollow syncytial tubes lignified internally; Phloem sieve tubes lack nuclei but are living and maintained by companion cells.',
        'Tricuspid valve is on the RIGHT side of the heart, while Bicuspid (Mitral) valve is on the LEFT.'
      ],
      defaultMastery: 0.70,
    ),
    CurriculumTopic(
      id: 'respiration',
      unitNumber: 1,
      chapterTitle: 'Chapter 4: Cellular Respiration',
      titleArabic: 'التنفس الخلوي وإنتاج الطاقة',
      titleEnglish: 'Cellular Respiration and Energy Production',
      eli5Summary: 'الخلية تأخذ سكر الجلوكوز وتكسره داخل الميتوكوندريا لاستخراج عملة الطاقة العالمية (ATP). كل جزيء جلوكوز ينتج حوالي 38 جزيء ATP في وجود الأكسجين.',
      technicalSummary: 'Complete aerobic respiration yields 38 ATP per glucose: Glycolysis in cytosol (net 2 ATP + 2 NADH), Acetyl-CoA link reaction (2 NADH), Krebs cycle in mitochondrial matrix (2 ATP + 6 NADH + 2 FADH2), and Electron Transport Chain (ETC) across cristae via oxidative phosphorylation driven by proton motive chemiosmotic gradient.',
      asciiDiagram: '''
  [Glucose Catabolism: 38 ATP Breakdown]
  Glucose (6C) ---> 2 Pyruvate (3C) [Cytosol: 2 ATP + 2 NADH]
                         |
  Mitochondrial Matrix: 2 Acetyl-CoA (2C) + 2 NADH + 2 CO2
                         |
  Krebs Cycle: 2 ATP + 6 NADH + 2 FADH2 + 4 CO2
                         |
  Cristae ETC (Oxidative Phosphorylation) ===> Total = 38 ATP
''',
      examTraps: [
        'Each NADH produces 3 ATP in the ETC, while each FADH2 produces only 2 ATP.',
        'Glycolysis occurs under BOTH aerobic and anaerobic conditions in the cytosol without requiring oxygen.'
      ],
      defaultMastery: 0.81,
    ),
    CurriculumTopic(
      id: 'excretion',
      unitNumber: 1,
      chapterTitle: 'Chapter 5: Excretion in Living Organisms',
      titleArabic: 'الإخراج في الكائنات الحية',
      titleEnglish: 'Excretion in Living Organisms',
      eli5Summary: 'الإخراج ليس مجرد التخلص من الفضلات، بل هو التخلص من المواد التي تعبر الأغشية البلازمية لخلايا الجسم؛ فالكليتان تصفيان الدم، والجلد يفرز العرق، والرئتان تخرجان ثاني أكسيد الكربون.',
      technicalSummary: 'Excretion requires metabolites to pass across semi-permeable cell membranes. Bowmans capsule undergoes non-selective ultrafiltration of blood plasma, followed by active selective reabsorption of glucose, salts, and water in proximal and distal convoluted tubules and Loop of Henle under ADH and Aldosterone endocrine regulation.',
      asciiDiagram: '''
  [Nephron Filtration & Reabsorption]
  Renal Artery -> Glomerulus -> Bowman's Capsule (Ultrafiltration)
                                      |
                               Proximal Tubule (Selective Reabsorption)
                                      |
                               Loop of Henle -> Distal Tubule -> Collecting Duct
''',
      examTraps: [
        'Feces passage is NOT biological excretion because it does not cross cellular plasma membranes.',
        'High urea concentrations in renal vein are filtered; renal vein has the LOWEST urea concentration in the entire body.'
      ],
      defaultMastery: 0.69,
    ),
    CurriculumTopic(
      id: 'nervous_coordination',
      unitNumber: 1,
      chapterTitle: 'Chapter 6: Nervous Coordination',
      titleArabic: 'التنسيق العصبي والسيال العصبي',
      titleEnglish: 'Nervous Coordination and Nerve Impulse',
      eli5Summary: 'الخلية العصبية تنقل الإشارات الكهربائية السريعة عبر غشائها، وفي نهاية الخلية تفرز نواقل كيميائية (الأسيتيل كولين) لتقفز الإشارة إلى الخلية التالية.',
      technicalSummary: 'Resting potential is maintained at -70 mV by Na+/K+ ATPase pumps and selective K+ outward leakage. Depolarization reaches +40 mV upon threshold opening of voltage-gated Na+ channels. Chemical synapse transmission involves Ca2+ influx triggering exocytosis of acetylcholine into synaptic cleft, terminated by acetylcholinesterase.',
      asciiDiagram: '''
  [Action Potential Graph]
     +40mV |      /\
           |     /  \  Repolarization (K+ Outflow)
       0mV |    /    \
     -70mV |-- /------\_______ Resting Potential (-70mV)
           Depolarization (Na+ Influx)
''',
      examTraps: [
        'Resting potential is negative inside due to excessive K+ membrane permeability outwards compared to Na+.',
        'Refractory period prevents reverse transmission of the nerve impulse, ensuring strictly unidirectional propagation.'
      ],
      defaultMastery: 0.74,
    ),
    CurriculumTopic(
      id: 'hormonal_coordination',
      unitNumber: 1,
      chapterTitle: 'Chapter 7: Hormonal Coordination',
      titleArabic: 'التنسيق الهرموني والغدد الصماء',
      titleEnglish: 'Hormonal Coordination and Endocrine System',
      eli5Summary: 'الغدة النخامية هي المايسترو الذي يقود أوركسترا الغدد الصماء في الجسم؛ تفرز هرمونات توجه الغدة الدرقية، الكظرية، والغدد التناسلية لضبط الاتزان الداخلي.',
      technicalSummary: 'Pituitary gland acts as the endocrine master: Anterior lobe secretes TSH, ACTH, FSH, LH, and GH (Growth Hormone). Posterior neurohypophysis releases hypothalamic ADH (vasopressin) and Oxytocin. Thyroid secretes thyroxine (iodine-dependent) and calcitonin (antagonistic to parathormone for Ca2+ homeostasis).',
      asciiDiagram: '''
  [Calcium Homeostasis Feedback Loop]
  High Blood Ca2+ ---> Thyroid releases Calcitonin ---> Bones store Ca2+
  Low Blood Ca2+  ---> Parathyroid releases Parathormone ---> Bones release Ca2+
''',
      examTraps: [
        'ADH is synthesized in the HYPOTHALAMUS and only stored and released by the posterior pituitary.',
        'Calcitonin LOWERS blood calcium levels, whereas Parathormone RAISES blood calcium.'
      ],
      defaultMastery: 0.85,
    ),
    CurriculumTopic(
      id: 'reproduction',
      unitNumber: 1,
      chapterTitle: 'Chapter 8: Reproduction in Living Organisms',
      titleArabic: 'التكاثر في الكائنات الحية',
      titleEnglish: 'Reproduction in Living Organisms',
      eli5Summary: 'التكاثر يحافظ على استمرار النوع؛ يتنوع من لاجنسي سريع (كالانشطار والتجدد والتوالد البكري) إلى جنسي يزيد التنوع الوراثي (كالاقتران في الإسبيروجيرا وتعاقب الأجيال في البلازموديوم وسرخس الفوجير).',
      technicalSummary: 'Covers asexual modes (fission, budding, sporogenesis, regeneration, parthenogenesis in honeybees and aphids). Alternation of generations in Plasmodium malariae and Polypodium ferns. Angiosperm double fertilization yields diploid zygote (2n) and triploid endosperm (3n). Human menstrual cycle regulated by FSH, Estrogen, LH (triggers ovulation at day 14), and Progesterone.',
      asciiDiagram: '''
  [Menstrual Cycle Hormonal Timeline]
  Day 1-5: Menses -> Day 5-13: FSH & Estrogen (Proliferative) -> Day 14: LH Surge (Ovulation) -> Day 15-28: Progesterone (Secretory)
''',
      examTraps: [
        'Male honeybee drones are haploid (n) produced by parthenogenesis and produce sperm by MITOSIS, not meiosis.',
        'Double fertilization in plants produces diploid embryo (2n) AND triploid endosperm (3n).'
      ],
      defaultMastery: 0.88,
    ),
    CurriculumTopic(
      id: 'immunity',
      unitNumber: 1,
      chapterTitle: 'Chapter 9: Immunity in Living Organisms',
      titleArabic: 'المناعة في الكائنات الحية',
      titleEnglish: 'Immunity in Living Organisms',
      eli5Summary: 'جهازك المناعي جيش متكامل: خط الدفاع الأول يمنع الدخول (الجلد والمخاط)، وخط الدفاع الثاني يهاجم الغزاة فورا (الالتهاب والإنترفيرون)، وخط الدفاع الثالث أسلحة ذكية متخصصة (الأجسام المضادة وخلايا T القاتلة).',
      technicalSummary: 'Innate immunity lines of defense (skin, lysozyme, stomach HCl, inflammatory response via histamine from mast cells, interferons, NK cells). Acquired immunity divided into Humoral (B-cells producing IgM, IgG, IgA, IgE, IgD) and Cell-Mediated (Helper T CD4+ secreting interleukins and cytokines, Cytotoxic T CD8+ secreting perforin and granzymes, Suppressor T CD8+).',
      asciiDiagram: '''
  [B-Cell & T-Cell Immune Response Cascade]
  Macrophage (Antigen + MHC) ---> Helper T (CD4+) Secrete Cytokines
                                        |
                 +----------------------+----------------------+
                 |                                             |
                 V                                             V
  B-Cell Activated -> Plasma B -> Antibodies (Humoral)     Cytotoxic T (CD8+) -> Perforin (Cell-Mediated)
''',
      examTraps: [
        'Suppressor T cells (Ts) carry CD8 receptors just like Cytotoxic T cells (Tc), but suppress immunity via lymphokines.',
        'Antibody variable region determines epitope antigen specificity; constant region determines antibody isotype class.'
      ],
      defaultMastery: 0.83,
    ),

    // Unit 2: Molecular Biology
    CurriculumTopic(
      id: 'dna_replication',
      unitNumber: 2,
      chapterTitle: 'Chapter 10: DNA and Replication',
      titleArabic: 'الحمض النووي DNA وتضاعفه',
      titleEnglish: 'DNA Structure, Genetic Code and Replication',
      eli5Summary: 'الـ DNA هو كتاب التعليمات الوراثية الملتف في حلزون مزدوج؛ عند انقسام الخلية، ينفك الحبل المزدوج لتقوم إنزيمات البلمرة ببناء نسخة طبق الأصل من كل شريط.',
      technicalSummary: 'DNA double helix antiparallel structure (5\' to 3\' opposite 3\' to 5\'). Chargaff\'s rule: A=T with 2 hydrogen bonds, G=C with 3 hydrogen bonds. Semi-conservative replication: Helicase unwinds helix, DNA Polymerase III synthesizes leading strand continuously 5\'->3\' and lagging strand discontinuously as Okazaki fragments, joined by DNA Ligase.',
      asciiDiagram: '''
  [DNA Double Helix Replication Fork]
  5' -----------------------------> 3' (Leading Strand: Continuous DNA Polymerase)
         \\   /  (Helicase Unwinding)
  3' <--- \\ / <--- [Okazaki] <--- 5' (Lagging Strand: DNA Ligase joins)
''',
      examTraps: [
        'DNA Polymerase can ONLY add nucleotides to the 3\'-OH free end (synthesizing in the 5\' to 3\' direction).',
        'G-C base pairs have 3 hydrogen bonds, making GC-rich DNA more thermostable than AT-rich DNA.'
      ],
      defaultMastery: 0.91,
    ),
    CurriculumTopic(
      id: 'rna_protein_synthesis',
      unitNumber: 2,
      chapterTitle: 'Chapter 11: RNA, Translation and Genetic Engineering',
      titleArabic: 'الأحماض النووية RNA وبناء البروتين والتقنية الحيوية',
      titleEnglish: 'RNA, Protein Synthesis, Mutations and Biotechnology',
      eli5Summary: 'الرسول (mRNA) ينسخ الشفرة من الـ DNA في النواة وينقلها إلى مصنع الريبوسوم، حيث يقرأ كل 3 حروف (كودون) ويحضر الناقل (tRNA) الحمض الأميني المناسب لتركيب البروتين.',
      technicalSummary: 'Transcription: RNA Polymerase synthesizes mRNA from antisense DNA template. Translation: Ribosome initiation at AUG codon (Methionine), elongation at A and P sites with peptide bond synthesis via peptidyl transferase ribozyme, termination at UAA, UAG, or UGA stop codons by release factor. Genetic engineering: Restriction endonucleases, reverse transcriptase, and PCR amplification.',
      asciiDiagram: '''
  [Protein Synthesis Central Dogma]
  DNA (Nucleus) --[RNA Polymerase]--> mRNA --[Nuclear Pore]--> Ribosome (Cytoplasm)
                                                                   |
                                          tRNA with Anticodon + Amino Acid
                                                                   |
                                                         Peptide Chain (Protein)
''',
      examTraps: [
        'Stop codons (UAA, UAG, UGA) do NOT code for any amino acid; they bind a Release Factor protein, NOT a tRNA.',
        'Restriction endonucleases recognize specific palindromic 4 to 8 nucleotide sequences with identical 5\' to 3\' symmetry on both strands.'
      ],
      defaultMastery: 0.86,
    ),
  ];
}
