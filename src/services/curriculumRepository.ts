import { Chapter, MasterStudyPack, PracticeQuestion, SubTopic, TopicMastery, MasteryLevel } from '../types';

export const CHAPTERS: Chapter[] = [
  {
    id: 'unit1_chap1',
    unit: 'UNIT_1',
    number: 1,
    title: 'Support and Movement',
    arabicTitle: 'الدعامة والحركة في الكائنات الحية',
    summary: 'Plant physiological/structural support, human skeleton, joints, tendons, ligaments, and muscular contraction mechanism.',
    iconName: 'Activity',
  },
  {
    id: 'unit1_chap2',
    unit: 'UNIT_1',
    number: 2,
    title: 'Hormonal Coordination',
    arabicTitle: 'التنسيق الهرموني في الكائنات الحية',
    summary: 'Endocrine glands, master pituitary gland, negative feedback loops, blood sugar and calcium homeostasis.',
    iconName: 'Droplet',
  },
  {
    id: 'unit1_chap3',
    unit: 'UNIT_1',
    number: 3,
    title: 'Reproduction in Living Organisms',
    arabicTitle: 'التكاثر في الكائنات الحية',
    summary: 'Alternation of generations (Fern & Plasmodium), double fertilization in angiosperms, human gametogenesis & menstrual cycle.',
    iconName: 'Repeat',
  },
  {
    id: 'unit1_chap4',
    unit: 'UNIT_1',
    number: 4,
    title: 'Immunity in Living Organisms',
    arabicTitle: 'المناعة في الكائنات الحية',
    summary: 'Plant defense, human lymphatic system, antibodies, innate vs acquired immunity (humoral B-cells & cell-mediated T-cells).',
    iconName: 'Shield',
  },
  {
    id: 'unit2_chap1',
    unit: 'UNIT_2',
    number: 1,
    title: 'DNA & Genetic Information',
    arabicTitle: 'الحمض النووي والمعلومات الوراثية',
    summary: 'Historical proof experiments (Griffith, Hershey-Chase), DNA structure & Chargaff ratios, replication, repair & nucleosomes.',
    iconName: 'Dna',
  },
  {
    id: 'unit2_chap2',
    unit: 'UNIT_2',
    number: 2,
    title: 'RNA & Protein Synthesis',
    arabicTitle: 'الأحماض النووية وتخليق البروتين',
    summary: 'Types of RNA, transcription, translation, genetic code math, mutations, and genetic engineering technology.',
    iconName: 'FlaskConical',
  },
];

export const SUB_TOPICS: SubTopic[] = [
  {
    id: 'sarcomere_contraction',
    chapterId: 'unit1_chap1',
    unit: 'UNIT_1',
    title: 'Muscle Contraction & Sliding Filament Theory',
    arabicTitle: 'انقباض العضلة الهيكلية ونظرية الخيوط المنزلقة',
    summary: "Huxley's sliding filament mechanism, role of Ca²⁺ and ATP, motor units, and sarcomere calculations.",
    highYieldFlag: true,
  },
  {
    id: 'plant_human_support',
    chapterId: 'unit1_chap1',
    unit: 'UNIT_1',
    title: 'Plant Support & Human Skeletal System',
    arabicTitle: 'الدعامة في النبات والجهاز الهيكلي في الإنسان',
    summary: 'Physiological vs structural support in plants, axial vs appendicular skeleton, joints, tendons vs ligaments.',
    highYieldFlag: true,
  },
  {
    id: 'hormonal_coordination_endocrine',
    chapterId: 'unit1_chap2',
    unit: 'UNIT_1',
    title: 'Pituitary Master Gland & Hormone Feedback',
    arabicTitle: 'الغدة النخامية والتغذية الراجعة الهرمونية',
    summary: 'Anterior vs posterior pituitary lobes, FSH/LH, TSH, ACTH, ADH/vasopressin, and calcium/glucose regulation.',
    highYieldFlag: true,
  },
  {
    id: 'reproduction_double_fertilization',
    chapterId: 'unit1_chap3',
    unit: 'UNIT_1',
    title: 'Plant Reproduction & Alternation of Generations',
    arabicTitle: 'تعاقب الأجيال والإخصاب المزدوج في النباتات الزهرية',
    summary: 'Life cycle of Polypodium & Plasmodium, micro/megasporogenesis, double fertilization and 3n endosperm formation.',
    highYieldFlag: true,
  },
  {
    id: 'human_reproduction_menstrual',
    chapterId: 'unit1_chap3',
    unit: 'UNIT_1',
    title: 'Human Reproduction & The Menstrual Cycle',
    arabicTitle: 'التكاثر في الإنسان ودورة الطمث',
    summary: 'Spermatogenesis vs oogenesis, 28-day ovarian cycle, estrogen & progesterone curves, fertilization, and twins.',
    highYieldFlag: true,
  },
  {
    id: 'immunity_humoral_cellular',
    chapterId: 'unit1_chap4',
    unit: 'UNIT_1',
    title: 'Humoral vs Cell-Mediated Immunity',
    arabicTitle: 'المناعة الخلطية بالخلايا البائية والمناعة الخلوية بالخلايا التائية',
    summary: 'Antibody anatomy & mechanisms, B-cells & plasma cells, helper TH (CD4+), cytotoxic TC (CD8+), perforin & lymphokines.',
    highYieldFlag: true,
  },
  {
    id: 'dna_structure_replication',
    chapterId: 'unit2_chap1',
    unit: 'UNIT_2',
    title: 'DNA Structure, Chargaff Ratios & Replication',
    arabicTitle: 'تركيب DNA وقواعد شارجاف وتضاعف المادة الوراثية',
    summary: 'Avery & Hershey-Chase proofs, double helix 2nm dimensions, helicase, DNA polymerase 5\'->3\', ligase, Okazaki fragments.',
    highYieldFlag: true,
  },
  {
    id: 'rna_transcription_translation',
    chapterId: 'unit2_chap2',
    unit: 'UNIT_2',
    title: 'RNA Transcription, Translation & Genetic Code',
    arabicTitle: 'تخليق RNA وترجمة البروتين والشفرة الوراثية',
    summary: 'mRNA processing, tRNA cloverleaf & anticodon, ribosomal subunits, peptidyl transferase, codon calculations, and mutations.',
    highYieldFlag: true,
  },
  {
    id: 'genetic_engineering_recombinant',
    chapterId: 'unit2_chap2',
    unit: 'UNIT_2',
    title: 'Genetic Engineering & Recombinant DNA',
    arabicTitle: 'الهندسة الوراثية و DNA معاد الاتحاد',
    summary: 'Restriction endonucleases, sticky ends, DNA ligase, reverse transcriptase, cloning vectors/plasmids, PCR & Taq polymerase.',
    highYieldFlag: true,
  },
];

export const STUDY_PACKS: Record<string, MasterStudyPack> = {
  sarcomere_contraction: {
    topicId: 'sarcomere_contraction',
    topicTitle: 'Muscle Contraction & Sliding Filament Theory',
    unit: 'UNIT_1',
    chapterTitle: 'Chapter 1: Support and Movement',
    eli5Analogy:
      'Imagine two rows of rowing oars reaching across from thick wooden boats (Myosin). When an alarm sounds (Ca²⁺ release), the oars grab onto adjacent thin ropes (Actin) and pull them toward the center using battery power (ATP). The boats and ropes do NOT shrink in length; they simply slide past one another, making the entire dock (Sarcomere) shorter!',
    arabicEli5:
      'تخيل إنك في مركبين تجديف كبار (الميوسين) بينهم حبال رفيعة (الأكتين). أول ما يضرب جرس الإنذار (انطلاق أيونات الكالسيوم Ca²⁺)، تطلع مجاديف وخطاطيف من مركب الميوسين تمسك في حبال الأكتين وتسحبها ناحية المركز بطاقة البطارية (ATP). لا المراكب قصرت ولا الحبال قصرت، هما اتزحلقوا فوق بعض فقصر طول الرصيف بالكامل (القطعة العضلية Sarcomere)! ولما يخلص الـ ATP، المجاديف تفضل مكلبشة في الحبال وما تفكش وده هو الشد العضلي المؤلم.',
    textbookPageReference: 'Ministry Textbook Unit 1, Chapter 1, pp. 14–20',
    deepTechnicalBreakdown: `### 1. Neuromuscular Junction Activation
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
3. Ca²⁺ is pumped back into the sarcoplasmic reticulum by active transport (requiring ATP).`,
    keyTerms: [
      {
        term: 'Sarcomere',
        definition: 'The distance between two successive Z-lines; structural and functional contractile unit of myofibril.',
        arabicTerm: 'القطعة العضلية',
      },
      {
        term: 'Myosin',
        definition: 'Thick protein filament bearing cross-bridges (transverse hooks) that interact with actin.',
        arabicTerm: 'الميوسين',
      },
      {
        term: 'Actin',
        definition: 'Thin protein filament anchored to the Z-line.',
        arabicTerm: 'الأكتين',
      },
      {
        term: 'Cholinesterase',
        definition: 'Enzyme that destroys acetylcholine into acetic acid and choline to terminate contraction.',
        arabicTerm: 'إنزيم كولين إستريز',
      },
      {
        term: 'Motor Unit',
        definition: 'One motor neuron and all the muscle fibers it innervates (ranging from 5 to 100 fibers).',
        arabicTerm: 'الوحدة الحركية',
      },
    ],
    formulas: [
      {
        name: 'Sarcomere to Z-Line Relationship',
        formulaLatex: 'Z = n + 1',
        description: 'Number of Z-lines always equals number of sarcomeres plus 1.',
        variables: 'n = Number of Sarcomeres; Z = Number of Z-lines',
        sampleCalculation: 'For 5 sarcomeres: Z-lines = 5 + 1 = 6 lines.',
      },
      {
        name: 'A-Band Count',
        formulaLatex: 'A = n',
        description: 'Dark bands (A-bands) equal number of sarcomeres and NEVER change length during contraction.',
        variables: 'A = Number of Dark bands; n = Number of Sarcomeres',
        sampleCalculation: 'For 5 sarcomeres: A-bands = 5.',
      },
      {
        name: 'Complete I-Band Count',
        formulaLatex: 'I_{complete} = n - 1 = Z - 2',
        description: 'Only internal light bands are complete; 2 incomplete bands exist at the outer ends.',
        variables: 'n = Number of Sarcomeres; Z = Number of Z-lines',
        sampleCalculation: 'For 5 sarcomeres: Complete I-bands = 5 - 1 = 4.',
      },
      {
        name: 'H-Zone Disappearance Rule',
        formulaLatex: 'H_{maximal} = 0',
        description: 'During complete/strong contraction, actin filaments meet and overlap completely, obliterating the H-zone.',
        variables: 'H = Semi-light zone width',
        sampleCalculation: 'Relaxed: H = n; Complete contraction: H = 0.',
      },
    ],
    comparisonTables: [
      {
        title: 'Sarcomere Bands Behavior During Contraction',
        columnA: 'Relaxed State',
        columnB: 'Contracted State',
        rows: [
          { criteria: 'Z-lines Distance', valueA: 'Normal / Far apart', valueB: 'Decreases / Move closer together' },
          { criteria: 'I-Band (Light Band)', valueA: 'Wide length', valueB: 'Decreases in width' },
          { criteria: 'H-Zone (Semi-Light)', valueA: 'Visible (Myosin only)', valueB: 'Decreases, vanishes completely in maximal contraction' },
          { criteria: 'A-Band (Dark Band)', valueA: 'Constant length', valueB: 'UNALTERED (Constant, length of myosin)' },
        ],
      },
      {
        title: 'Muscle Fatigue vs Muscle Spasm (Ministry Exam Trap)',
        columnA: 'Muscle Fatigue (إجهاد)',
        columnB: 'Muscle Spasm (شد عضلي)',
        rows: [
          { criteria: 'Primary Cause', valueA: 'Anaerobic respiration due to oxygen shortage; lactic acid accumulates', valueB: 'Absolute depletion of ATP OR erratic motor nerve impulses' },
          { criteria: 'Cross-Bridges', valueA: 'Detach slowly due to low ATP rate', valueB: 'Remain permanently attached to actin filaments' },
          { criteria: 'Contraction Force', valueA: 'Weak, sluggish contraction', valueB: 'Severe sustained involuntary contraction; risk of muscle tearing and hemorrhage' },
          { criteria: 'Remedy', valueA: 'Rest & deep breathing to supply O₂', valueB: 'Immediate rest, anti-spasmodic, restore ATP synthesis' },
        ],
      },
    ],
    bloomProgression: [
      { levelName: 'Remembering', levelNumber: 1, objective: 'Recall structural proteins of myofibril and role of Ca²⁺.', masteryPrompt: 'What are the two protein filaments forming the sarcomere?' },
      { levelName: 'Understanding', levelNumber: 2, objective: 'Explain why the A-band length does not change while the I-band shortens.', masteryPrompt: 'Why does the dark band remain constant during Huxley\'s sliding mechanism?' },
      { levelName: 'Applying', levelNumber: 3, objective: 'Calculate band counts given 8 consecutive Z-lines.', masteryPrompt: 'Given 8 Z-lines: find Sarcomeres, A-bands, Complete I-bands, and H-zones during full contraction.' },
      { levelName: 'Analyzing', levelNumber: 4, objective: 'Differentiate between fatigue caused by lactic acid vs spasm caused by ATP absence.', masteryPrompt: 'Analyze why a marathon runner suffers fatigue vs a sudden spasm.' },
      { levelName: 'Evaluating', levelNumber: 5, objective: 'Assess physiological impact of synthetic organophosphate poisons inhibiting cholinesterase.', masteryPrompt: 'Predict the consequence of sarin gas inhibiting AChE on breathing muscles.' },
      { levelName: 'Creating', levelNumber: 6, objective: 'Formulate a therapeutic intervention to reverse muscle spasm safely.', masteryPrompt: 'Design a rapid recovery protocol to uncouple locked cross-bridges without tissue damage.' },
    ],
    asciiSchematic: `================ RELAXED SARCOMERE ================
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
   |<------------- A-band (Unchanged) ---->|`,
    schematicCaption:
      "Huxley's Sliding Filament Model: Notice how Z-lines approximate, I-band shortens, H-zone disappears, while A-band length remains strictly unchanged.",
    flowcharts: [
      { stepNumber: 1, action: 'Nerve Impulse reaches Terminal Arborization', detail: 'Depolarizes axonal membrane', triggerOrEnzyme: 'Action Potential' },
      { stepNumber: 2, action: 'Voltage-gated Ca²⁺ channels open', detail: 'Ca²⁺ enters synaptic knob and ruptures vesicles', triggerOrEnzyme: 'Ca²⁺ ions' },
      { stepNumber: 3, action: 'Acetylcholine released into Synaptic Cleft', detail: 'Diffuses across cleft and binds sarcolemma receptors', triggerOrEnzyme: 'ACh neurotransmitter' },
      { stepNumber: 4, action: 'Sarcolemma Depolarization (+30 mV)', detail: 'T-tubules carry potential to Sarcoplasmic Reticulum', triggerOrEnzyme: 'Na⁺ influx' },
      { stepNumber: 5, action: 'SR releases Ca²⁺ into Sarcoplasm', detail: 'Ca²⁺ uncovers active sites on actin by moving tropomyosin', triggerOrEnzyme: 'Ca²⁺ from SR' },
      { stepNumber: 6, action: 'Myosin Cross-Bridges bind Actin', detail: 'ATP hydrolyzes to ADP + Pi; power stroke pulls actin', triggerOrEnzyme: 'Myosin ATPase' },
      { stepNumber: 7, action: 'Cholinesterase Hydrolyzes ACh', detail: 'Converts ACh to Acetic acid + Choline; sarcolemma repolarizes', triggerOrEnzyme: 'AChE Enzyme' },
    ],
    visualInspectionMarkers: [
      'Check Z-line distance: If narrow, muscle is contracted; if wide, muscle is relaxed.',
      'Inspect the H-zone in the center: In full contraction, H-zone is absent (width = 0).',
      'Examine Myosin transverse hooks (cross-bridges): Attached to actin = contracted state; detached = relaxed state.',
      'Verify A-band bracket: Must span entire length of thick myosin filaments.',
    ],
    youtube3DQueries: [
      { query: '3D Animation Sliding Filament Theory Muscle Contraction', topicDescription: 'Shows dynamic motion of myosin heads pulling actin and ATP hydrolysis in high definition.', durationRecommendation: '3-5 mins' },
      { query: 'Neuromuscular Junction Acetylcholine Release 3D Animation', topicDescription: 'Depicts synaptic vesicles rupturing, acetylcholine diffusing, and cholinesterase breakdown.', durationRecommendation: '2-4 mins' },
    ],
    questions: [
      {
        id: 'q1',
        type: 'SCIENTIFIC_TERM',
        question: 'Write the scientific term: The structural and functional contractile unit located between two successive Z-lines in a skeletal muscle fiber.',
        officialAnswer: 'Sarcomere (القطعة العضلية)',
        rationale: 'By definition in the Egyptian Ministry curriculum, the sarcomere is bounded by two successive Z-lines.',
        ministryExamTip: "Never confuse 'Sarcomere' with 'Myofibril' or 'Motor Unit'.",
      },
      {
        id: 'q2',
        type: 'GIVE_REASON',
        question: 'Give reason: The A-band (Dark band) length remains constant during skeletal muscle contraction while the I-band length shortens.',
        officialAnswer: 'Because the A-band corresponds to the entire length of the thick myosin filaments, which do not change their physical length during contraction. The I-band shortens because actin filaments slide over myosin filaments toward the center of the sarcomere, reducing the uncovered light zone.',
        rationale: "Huxley's sliding theory states filaments slide past one another without shortening themselves.",
        ministryExamTip: "Must mention: 'A-band equals the length of thick myosin filaments' for full mark.",
      },
      {
        id: 'q3',
        type: 'WHAT_HAPPENS_IF',
        question: 'What happens if: Total absence of cholinesterase enzyme at the motor end plate of a skeletal muscle fiber?',
        officialAnswer: 'Acetylcholine will not be hydrolyzed into acetic acid and choline. The sarcolemma will remain permanently depolarized, keeping the muscle in continuous state of contraction (unable to relax), which may lead to muscle spasm and tearing.',
        rationale: 'Cholinesterase is indispensable for muscle relaxation and resting potential restoration.',
        ministryExamTip: "Keywords required: 'Acetylcholine persists', 'No repolarization', 'Continuous contraction/spasm'.",
      },
      {
        id: 'q4',
        type: 'MCQ',
        question: 'If a myofibril contains 6 Z-lines, what is the number of complete I-bands and H-zones during maximal contraction respectively?',
        options: ['5 and 5', '4 and 0', '5 and 0', '4 and 5'],
        correctOptionIndex: 1,
        officialAnswer: '4 and 0',
        rationale: 'Sarcomeres = Z - 1 = 6 - 1 = 5. Complete I-bands = Sarcomeres - 1 = 5 - 1 = 4. During maximal contraction, H-zones vanish completely, so H = 0.',
        ministryExamTip: "Watch out for 'maximal contraction' trap! H-zone becomes 0.",
      },
    ],
    examTraps: [
      {
        misconception: 'Thinking muscle filaments shrink like an accordion during contraction.',
        scientificTruth: 'Actin and Myosin filaments maintain their exact length! They only SLIDE past one another, reducing the distance between Z-lines.',
        examTrapWarning: "Ministry MCQ trap: 'Which of the following shortens during contraction?' Option A: Myosin, Option B: Actin, Option C: Sarcomere. The correct answer is ONLY Sarcomere/I-band/H-zone!",
        goldenRule: 'Filaments slide; Sarcomeres shorten.',
      },
      {
        misconception: 'Confusing the role of Calcium ions in the nerve terminal vs in the muscle sarcoplasm.',
        scientificTruth: 'Ca²⁺ in nerve knob ruptures synaptic vesicles to release ACh. Ca²⁺ in muscle sarcoplasm unmasks actin binding sites by shifting troponin/tropomyosin.',
        examTrapWarning: "Questions specifying 'Role of Ca²⁺ at the neuromuscular junction' require mentioning synaptic vesicle rupture!",
        goldenRule: 'Synaptic knob Ca²⁺ = Neurotransmitter release; Sarcoplasmic Ca²⁺ = Cross-bridge attachment.',
      },
    ],
    flashcards: [
      { id: 'fc1', topicId: 'sarcomere_contraction', frontPrompt: 'What is the structural and functional contractile unit of a muscle fiber?', backAnswer: 'The Sarcomere (distance between two successive Z-lines).', category: 'Terminology' },
      { id: 'fc2', topicId: 'sarcomere_contraction', frontPrompt: 'What happens to the H-zone during maximal muscle contraction?', backAnswer: 'It completely disappears (becomes zero) as actin filaments slide and overlap entirely.', category: 'Mechanism' },
      { id: 'fc3', topicId: 'sarcomere_contraction', frontPrompt: 'What are the dual requirements for both muscle contraction AND relaxation?', backAnswer: 'ATP and Calcium ions (ATP is required to attach AND detach cross-bridges; Ca²⁺ is pumped back by ATP).', category: 'Physiology' },
      { id: 'fc4', topicId: 'sarcomere_contraction', frontPrompt: 'What is the mathematical formula for number of complete I-bands given Z-lines?', backAnswer: 'Complete I-bands = Z - 2 (or Sarcomeres - 1).', category: 'Formula' },
    ],
    examSimulationItem: {
      id: 'sim_sarcomere',
      questionType: 'GIVE_REASON',
      prompt: 'Give Reason: The absence of ATP in a skeletal muscle fiber leads to sustained muscle spasm.',
      maxScore: 2.0,
      requiredKeywords: ['atp', 'cross bridges', 'detach', 'actin', 'spasm'],
      partialCreditKeywords: ['myosin', 'relaxation', 'tear'],
      modelAnswer: 'Because ATP is necessary for the detachment of myosin cross-bridges (transverse hooks) from actin filaments. In the absence of ATP, cross-bridges remain locked to actin filaments, preventing muscle relaxation and causing painful, sustained muscle spasm.',
      remediationNotes: 'You must explicitly explain that ATP is required to BREAK the cross-bridge bond, not just for contraction.',
    },
    pblProject: {
      id: 'pbl_sarcomere',
      topicId: 'sarcomere_contraction',
      title: 'Neuromuscular Toxin Investigation',
      titleArabic: 'مهمة فك شفرة السموم العصبية العضلية',
      scenario: 'A patient presents with severe rigidity and respiratory muscle lockdown after exposure to an unknown pesticide.',
      mission: 'Identify whether the toxin targets voltage-gated Ca²⁺ channels, acetylcholine receptors, or cholinesterase enzyme.',
      rubrics: [
        { criterion: 'Diagnostic Accuracy', weightPoints: 40, excellentDescription: 'Pinpoints cholinesterase inhibition with biochemical mechanism.', poorDescription: 'Confuses pre-synaptic and post-synaptic targets.' },
        { criterion: 'Therapeutic Plan', weightPoints: 30, excellentDescription: 'Recommends pralidoxime and atropine to restore receptor equilibrium.', poorDescription: 'Fails to address neurotransmitter degradation.' },
        { criterion: 'Ministry Mark Scheme Terminology', weightPoints: 30, excellentDescription: 'Uses acetylcholine, depolarization, transverse hooks accurately.', poorDescription: 'Uses vague colloquial language without ions.' },
      ],
      tips: ['Remember: Irreversible AChE inhibitors cause continuous depolarization!'],
    },
  },

  dna_structure_replication: {
    topicId: 'dna_structure_replication',
    topicTitle: 'DNA Structure, Chargaff Ratios & Replication',
    unit: 'UNIT_2',
    chapterTitle: 'Chapter 1: DNA & Genetic Information',
    eli5Analogy:
      'Think of DNA as a twisted spiral zipper (Double Helix). Each tooth of the zipper on one side only matches a specific tooth on the other: Blue (A) always pairs with Yellow (T), Green (G) always pairs with Red (C). To copy the book of life, a molecular unzipper (Helicase) opens the strands, master typists (DNA Polymerase) build the twin copies, and glue masters (Ligase) seal any disjointed fragments!',
    arabicEli5:
      'الـ DNA عامل زي سوستة حلزونية ملفوفة بدقة متناهية. كل سنّة من السوستة على جنب ليها شريك محدد ومقفول بالمفتاح: الأزرق (A) دايماً مع الأصفر (T)، والأخضر (G) دايماً مع الأحمر (C). ولما الخلية تقرر تتكاثر، يجي إنزيم اللولب (Helicase) يفك السوستة زي سحاب الجاكيت، وإنزيم بلمرة DNA يبني نسختين متطابقتين تماماً، وإنزيم الربط (Ligase) يلحم القطع المفكوكة.',
    textbookPageReference: 'Ministry Textbook Unit 2, Chapter 1, pp. 113–134',
    deepTechnicalBreakdown: `### 1. Evidence of DNA as Genetic Material
- **Griffith (1928)**: Discovered bacterial transformation between virulent S-strain and avirulent R-strain Pneumococcus.
- **Avery, MacLeod & McCarty (1944)**: Proved the transforming principle is DNA using **Deoxyribonuclease enzyme**, which permanently abolishes transformation.
- **Hershey & Chase (1952)**: Used bacteriophage labeled with $^{32}\\text{P}$ (DNA) and $^{35}\\text{S}$ (protein coat). Only $^{32}\\text{P}$ entered bacteria, verifying DNA is the hereditary molecule.

### 2. Double Helix Architecture
- Antiparallel strands: 5' to 3' and 3' to 5'.
- Outer backbone: Deoxyribose sugar alternating with phosphate groups via phosphodiester bonds.
- Inner rungs: Purines pair with Pyrimidines ($A=T$ by 2 H-bonds; $G\\equiv C$ by 3 H-bonds).
- Diameter is strictly constant at 2 nm (1 double-ring Purine + 1 single-ring Pyrimidine).
- Each helical turn = 10 base pairs = 20 nucleotides = 3.4 nm.

### 3. Semi-Conservative Replication
1. **Helicase**: Unwinds double helix and breaks hydrogen bonds.
2. **DNA Polymerase**: Synthesizes new complementary strand strictly 5' -> 3'. Leading strand continuous; Lagging strand discontinuous (Okazaki fragments).
3. **DNA Ligase**: Connects Okazaki fragments by forming phosphodiester bonds.`,
    keyTerms: [
      { term: 'Deoxyribonuclease', definition: 'Enzyme that completely degrades DNA without hydrolyzing RNA or proteins.', arabicTerm: 'دي أوكسي ريبونيوكليز' },
      { term: 'Antiparallel', definition: 'Arrangement where one DNA strand runs 5\' to 3\' and the other 3\' to 5\'.', arabicTerm: 'متعاكس الاتجاه' },
      { term: 'Okazaki Fragments', definition: 'Short DNA fragments synthesized discontinuously on the lagging strand.', arabicTerm: 'قطع أوكازاكي' },
      { term: 'DNA Ligase', definition: 'Enzyme that seals nicks in the sugar-phosphate backbone.', arabicTerm: 'إنزيم الربط' },
    ],
    formulas: [
      { name: "Chargaff's Rule", formulaLatex: '%A = %T, %G = %C', description: 'In double-stranded DNA, purines equal pyrimidines.', variables: 'A, T, G, C', sampleCalculation: 'If %A = 20%, %T = 20%, %G = 30%, %C = 30%.' },
      { name: 'DNA Turns Formula', formulaLatex: 'Turns = BasePairs / 10 = TotalNucleotides / 20', description: 'Complete turns in a DNA double helix.', variables: 'Turns', sampleCalculation: 'For 200 nucleotides: 200 / 20 = 10 turns.' },
      { name: 'Hydrogen Bonds', formulaLatex: 'H = 2(A) + 3(G)', description: 'Total hydrogen bonds between complementary strands.', variables: 'A, G count', sampleCalculation: 'For 40 A and 60 G: 2(40) + 3(60) = 260 bonds.' },
    ],
    comparisonTables: [
      {
        title: 'DNA Polymerase vs DNA Ligase',
        columnA: 'DNA Polymerase',
        columnB: 'DNA Ligase',
        rows: [
          { criteria: 'Catalytic Direction', valueA: 'Strictly 5\' to 3\'', valueB: 'Seals nicks on lagging strand' },
          { criteria: 'Leading Strand Action', valueA: 'Continuous synthesis toward replication fork', valueB: 'Not required on leading strand' },
          { criteria: 'Lagging Strand Action', valueA: 'Synthesizes Okazaki fragments discontinuously', valueB: 'Joins Okazaki fragments into continuous strand' },
          { criteria: 'Role in DNA Repair', valueA: 'Adds correct complementary nucleotide into gap', valueB: 'Seals phosphodiester backbone' },
        ],
      },
    ],
    bloomProgression: [
      { levelName: 'Remembering', levelNumber: 1, objective: 'Recall the 3 enzymes involved in DNA replication.', masteryPrompt: 'Name the enzymes that unwind, polymerize, and seal DNA.' },
      { levelName: 'Understanding', levelNumber: 2, objective: 'Explain why replication is semi-conservative.', masteryPrompt: 'Why does each daughter molecule have one template and one new strand?' },
      { levelName: 'Applying', levelNumber: 3, objective: 'Calculate base pairs and turns for a 400-nucleotide gene with 30% C.', masteryPrompt: 'If %C = 30% in 400 nucleotides, find number of Adenine bases and turns.' },
      { levelName: 'Analyzing', levelNumber: 4, objective: 'Analyze why RNA viruses mutate faster than DNA organisms.', masteryPrompt: 'Why do single-stranded RNA viruses lack proofreading?' },
      { levelName: 'Evaluating', levelNumber: 5, objective: 'Evaluate Avery vs Griffith experiments.', masteryPrompt: 'Why was Deoxyribonuclease essential to prove DNA is the transforming principle?' },
      { levelName: 'Creating', levelNumber: 6, objective: 'Design an experimental probe using radioactive labeling.', masteryPrompt: 'How could you prove whether mitochondria replicate their own DNA?' },
    ],
    asciiSchematic: `============== DNA REPLICATION FORK =============
 3' ---------------------------------------------> 5' (Template)
                     \\   <==== Helicase (Unwinds)
                      \\
                       =========================== 3' (Leading Strand: continuous)
                       <-------------------------- 5' [DNA Polymerase]
 5' ------------------/
          [Lagging]  /
          <--Frag2-- <--Frag1-- (Okazaki fragments joined by Ligase)
 3' ----------------/`,
    schematicCaption: 'DNA Replication Fork: Helicase separates strands, Leading strand synthesized continuously, Lagging strand discontinuously in Okazaki fragments.',
    flowcharts: [
      { stepNumber: 1, action: 'Helicase uncoils double helix', detail: 'Breaks hydrogen bonds at replication origin', triggerOrEnzyme: 'Helicase' },
      { stepNumber: 2, action: 'SSB Proteins stabilize single strands', detail: 'Prevents premature re-annealing', triggerOrEnzyme: 'SSB Proteins' },
      { stepNumber: 3, action: 'DNA Polymerase synthesizes Leading Strand', detail: 'Continuous 5\' to 3\' synthesis toward fork', triggerOrEnzyme: 'DNA Polymerase' },
      { stepNumber: 4, action: 'DNA Polymerase synthesizes Lagging Strand', detail: 'Discontinuous synthesis forming Okazaki fragments', triggerOrEnzyme: 'DNA Polymerase' },
      { stepNumber: 5, action: 'DNA Ligase seals fragments', detail: 'Creates phosphodiester bonds to complete strand', triggerOrEnzyme: 'DNA Ligase' },
    ],
    visualInspectionMarkers: [
      'Check strand polarity: 5\' end has free phosphate group; 3\' end has free OH hydroxyl group.',
      'Verify base pairing: Purine (2 rings) pairs with Pyrimidine (1 ring) ensuring constant 2nm width.',
      'Inspect Okazaki arrows: Points away from replication fork for lagging strand.',
    ],
    youtube3DQueries: [
      { query: '3D Animation DNA Replication Fork Okazaki Fragments', topicDescription: 'Shows leading and lagging strand polymerase actions in high definition.', durationRecommendation: '3-5 mins' },
      { query: 'Hershey and Chase Experiment 3D Animation Bacteriophage', topicDescription: 'Visualizes viral injection of radioactive phosphorus vs sulfur.', durationRecommendation: '2-3 mins' },
    ],
    questions: [
      {
        id: 'q_dna_1',
        type: 'SCIENTIFIC_TERM',
        question: 'Write the scientific term: The enzyme that specifically degrades DNA completely and was used to conclusively confirm DNA as the transforming substance.',
        officialAnswer: 'Deoxyribonuclease enzyme (إنزيم دي أوكسي ريبونيوكليز)',
        rationale: "Avery's experiment was finalized using deoxyribonuclease, which eliminated transformation.",
        ministryExamTip: "Do not write 'Ribonuclease' or just 'Nuclease'.",
      },
      {
        id: 'q_dna_2',
        type: 'GIVE_REASON',
        question: 'Give reason: The two strands of the DNA double helix are antiparallel (one 5\'->3\' and the other 3\'->5\').',
        officialAnswer: 'To ensure that the nitrogenous bases face inward correctly and form hydrogen bonds between complementary base pairs (A=T with 2 bonds and G≡C with 3 bonds).',
        rationale: 'If parallel, the hydrogen bonding angles between purines and pyrimidines would not fit stably.',
        ministryExamTip: 'Keywords: hydrogen bonds, bases face inwards, complementary base pairing.',
      },
      {
        id: 'q_dna_3',
        type: 'MCQ',
        question: 'A DNA sample of 600 nucleotides contains 120 Adenine bases. What is the number of complete turns and total hydrogen bonds?',
        options: ['30 turns and 780 bonds', '60 turns and 600 bonds', '30 turns and 660 bonds', '20 turns and 800 bonds'],
        correctOptionIndex: 0,
        officialAnswer: '30 turns and 780 bonds',
        rationale: 'Turns = 600 / 20 = 30. A = 120, so T = 120. Total A+T = 240. Remaining G+C = 600 - 240 = 360, so G = 180, C = 180. H-bonds = 2(120) + 3(180) = 240 + 540 = 780 bonds.',
        ministryExamTip: 'Divide total nucleotides by 20 for turns in double helix!',
      },
    ],
    examTraps: [
      {
        misconception: 'Calculating complete turns by dividing nucleotides by 10 instead of 20.',
        scientificTruth: 'Each turn consists of 10 BASE PAIRS, which equals 20 nucleotides in double-stranded DNA.',
        examTrapWarning: 'If the question says "single strand", 10 nucleotides per turn. If "double-stranded DNA", divide total nucleotides by 20!',
        goldenRule: 'Turns = Base Pairs / 10 = Total Nucleotides / 20.',
      },
    ],
    flashcards: [
      { id: 'fc_dna_1', topicId: 'dna_structure_replication', frontPrompt: 'What enzyme proved conclusively that DNA is the transforming factor?', backAnswer: 'Deoxyribonuclease enzyme.', category: 'Experiments' },
      { id: 'fc_dna_2', topicId: 'dna_structure_replication', frontPrompt: 'How many hydrogen bonds connect Guanine and Cytosine?', backAnswer: '3 Hydrogen Bonds (A=T has 2).', category: 'Structure' },
      { id: 'fc_dna_3', topicId: 'dna_structure_replication', frontPrompt: 'In which direction does DNA Polymerase synthesize new strands?', backAnswer: "Strictly in the 5' to 3' direction.", category: 'Replication' },
    ],
    examSimulationItem: {
      id: 'sim_dna',
      questionType: 'GIVE_REASON',
      prompt: "Give Reason: The synthesis of the lagging strand during DNA replication occurs discontinuously.",
      maxScore: 2.0,
      requiredKeywords: ['dna polymerase', '5\' to 3\'', 'replication fork', 'okazaki', 'ligase'],
      partialCreditKeywords: ['antiparallel', 'unwinding'],
      modelAnswer: 'Because DNA Polymerase can only add nucleotides in the 5\' to 3\' direction. Since the lagging template strand runs 5\' to 3\' toward the fork, the newly synthesized strand must be built away from the replication fork in short segments called Okazaki fragments, which are subsequently sealed by DNA ligase.',
      remediationNotes: 'Emphasize that DNA Polymerase is strictly unidirectional (5\' to 3\').',
    },
  },

  immunity_humoral_cellular: {
    topicId: 'immunity_humoral_cellular',
    topicTitle: 'Humoral vs Cell-Mediated Immunity',
    unit: 'UNIT_1',
    chapterTitle: 'Chapter 4: Immunity in Living Organisms',
    eli5Analogy:
      'The immune system is like national defense. B-cells are the air force launching precision guided cruise missiles (Antibodies) to neutralize enemies swimming freely in the bloodstream (Humoral Immunity). T-cells are the special tactical forces (Cell-Mediated Immunity) that kick open doors of infected host cells and cancer cells, detonating them with perforin proteins!',
    arabicEli5:
      'جهاز المناعة جيش متكامل لحماية وطنك (الجسم)! الخلايا البائية بتصنع أسلحة صواريخ موجهة اسمها "الأجسام المضادة" تصطاد الميكروبات السايبة في الدم (المناعة الخلطية). أما الخلايا التائية (T-cells) دي قوات الصاعقة الخاصة اللي بتقتحم الخلايا المصابة بالفيروسات وخلايا السرطان وتفجرها ببروتين البيرفورين (المناعة الخلوية)، وخلايا الذاكرة بتفضل حافظة وش العدو لعشرات السنين!',
    textbookPageReference: 'Ministry Textbook Unit 1, Chapter 4, pp. 83–110',
    deepTechnicalBreakdown: `### 1. Humoral Immunity (B-Cell Dependent)
- Involves antigens dissolved in body fluids (blood and lymph).
- Macrophages engulf antigen, break it into peptides, and present them on cell membrane coupled with **MHC (Major Histocompatibility Complex)**.
- **T-Helper (TH / CD4+)** binds to Macrophage antigen-MHC complex and secretes **Interleukins**.
- Interleukins activate B-lymphocytes to proliferate and differentiate into:
  1. **Plasma B-cells**: Produce massive specific antibodies.
  2. **Memory B-cells**: Persist for 20-30 years for rapid secondary immune response.

### 2. Cell-Mediated Immunity (T-Cell Dependent)
- Activated T-Helper cells secrete **Interleukins & Cytokines**.
- Cytokines attract macrophages and activate **Cytotoxic T-cells (TC / CD8+)** and **Natural Killer (NK) cells**.
- **TC Cells** attack virus-infected cells, cancer cells, and transplanted organs by secreting:
  1. **Perforin protein**: Pierces and perforates the target cell membrane.
  2. **Lymphotoxins**: Activate latent death genes in target cell nucleus, causing apoptosis.

### 3. Suppression of Immune Response
- After pathogen eradication, **Suppressor T-cells (TS / CD8+)** secrete **Lymphokines**, inhibiting plasma B-cells and inducing apoptosis in excess TH and TC cells to restore immune homeostasis.`,
    keyTerms: [
      { term: 'Perforin', definition: 'Specialized protein secreted by TC cells that creates pores in target infected cell membrane.', arabicTerm: 'بروتين البيرفورين' },
      { term: 'Lymphokines', definition: 'Inhibitory chemical messengers secreted by TS cells to deactivate and terminate the immune response.', arabicTerm: 'الليمفوكاينات' },
      { term: 'MHC', definition: 'Major Histocompatibility Complex; protein that binds antigen fragments for presentation.', arabicTerm: 'بروتين التوافق النسيجي' },
    ],
    formulas: [],
    comparisonTables: [
      {
        title: 'Humoral Immunity vs Cell-Mediated Immunity',
        columnA: 'Humoral Immunity (خلطية)',
        columnB: 'Cell-Mediated Immunity (خلوية)',
        rows: [
          { criteria: 'Effector Cells', valueA: 'Plasma B-cells', valueB: 'Cytotoxic T-cells (TC) & NK cells' },
          { criteria: 'Weapons Used', valueA: 'Antibodies (Immunoglobulins)', valueB: 'Perforin & Lymphotoxins' },
          { criteria: 'Target Location', valueA: 'Extracellular pathogens in blood/lymph', valueB: 'Intracellular viral infections, cancer, transplanted tissues' },
          { criteria: 'Cell Entry', valueA: 'Antibodies CANNOT penetrate cell membranes (too large)', valueB: 'TC cells directly engage and destroy altered host cells' },
        ],
      },
    ],
    bloomProgression: [
      { levelName: 'Remembering', levelNumber: 1, objective: 'Recall CD markers for TH, TC, and TS.', masteryPrompt: 'Identify which T-cells bear CD4 vs CD8.' },
      { levelName: 'Understanding', levelNumber: 2, objective: 'Explain why antibodies cannot destroy intracellular viruses directly.', masteryPrompt: 'Why is humoral immunity ineffective against viruses hidden inside host cells?' },
      { levelName: 'Applying', levelNumber: 3, objective: 'Predict immune response to organ transplantation rejection.', masteryPrompt: 'Which specific immune cells mediate kidney graft rejection?' },
      { levelName: 'Analyzing', levelNumber: 4, objective: 'Analyze the mechanism of Suppressor T-cells.', masteryPrompt: 'How do lymphokines prevent autoimmune overreaction?' },
      { levelName: 'Evaluating', levelNumber: 5, objective: 'Evaluate the efficacy of vaccines vs antivenom serums.', masteryPrompt: 'Differentiate between active immunity (vaccine) and passive immunity (serum).' },
      { levelName: 'Creating', levelNumber: 6, objective: 'Formulate an immunotherapy targeting cancer markers.', masteryPrompt: 'How can monoclonal antibodies be conjugated to cytotoxic agents?' },
    ],
    asciiSchematic: `============= ANTIBODY (Y-SHAPE) ARCHITECTURE ============
           [Antigen-Binding Site]    [Antigen-Binding Site]
                \\    /                   \\    /
               (Variable)              (Variable)
                 |  |                    |  |
                 |  |  [S-S]      [S-S]  |  |
           [Light Chain]                 [Light Chain]
                   \\                      /
                    \\--[Disulfide Bonds]-/
                            |    |
                            |    |
                       [Heavy Chains]
                         (Constant)
                            |    |
                            |    |`,
    schematicCaption: 'Antibody Structure: 2 Heavy Chains + 2 Light Chains held by Disulfide bonds (S-S), with 2 identical Antigen-Binding sites.',
    flowcharts: [
      { stepNumber: 1, action: 'Macrophage engulfs pathogen', detail: 'Digests antigen and couples with MHC protein', triggerOrEnzyme: 'Phagocytosis' },
      { stepNumber: 2, action: 'TH Cell binds Antigen-MHC', detail: 'Receptor CD4 recognizes complex on macrophage', triggerOrEnzyme: 'CD4 Receptor' },
      { stepNumber: 3, action: 'TH secretes Interleukins', detail: 'Activates B-cells and triggers clonal expansion', triggerOrEnzyme: 'Interleukins' },
      { stepNumber: 4, action: 'B-cells differentiate', detail: 'Produce Plasma B-cells (Antibodies) and Memory cells', triggerOrEnzyme: 'Clonal Selection' },
      { stepNumber: 5, action: 'TC destroys infected cells', detail: 'Secretes Perforin to pierce membrane and Lymphotoxins', triggerOrEnzyme: 'Perforin' },
      { stepNumber: 6, action: 'TS stops immune response', detail: 'Secretes Lymphokines when pathogen is cleared', triggerOrEnzyme: 'Lymphokines' },
    ],
    visualInspectionMarkers: [
      'Verify 4 polypeptide chains: 2 identical heavy chains and 2 identical light chains.',
      'Check Disulfide bonds: 4 disulfide bonds hold the antibody structure.',
      'Identify Variable regions at tips forming 2 identical antigen-binding pockets.',
    ],
    youtube3DQueries: [
      { query: '3D Animation Cell Mediated Immunity Cytotoxic T cell Perforin', topicDescription: 'Shows TC engagement and perforin pore formation.', durationRecommendation: '3-4 mins' },
      { query: 'Humoral Immunity Antibody Production B Cell Plasma 3D', topicDescription: 'Visualizes macrophage antigen presentation and antibody secretion.', durationRecommendation: '3-5 mins' },
    ],
    questions: [
      {
        id: 'q_imm_1',
        type: 'SCIENTIFIC_TERM',
        question: 'Write the scientific term: The chemical protein secreted by Cytotoxic T-cells that perforates the membrane of infected cells.',
        officialAnswer: 'Perforin protein (بروتين البيرفورين / البروتين صانع الثقوب)',
        rationale: 'Secreted by activated TC cells to punch holes in the target membrane.',
        ministryExamTip: 'Do not confuse with Lymphotoxins which digest nuclear DNA.',
      },
      {
        id: 'q_imm_2',
        type: 'GIVE_REASON',
        question: 'Give reason: Antibodies are ineffective in eradicating intracellular viruses like COVID-19 or Influenza.',
        officialAnswer: 'Because antibodies are relatively large molecular protein structures that cannot penetrate the cell membrane to reach viruses multiplying inside host cells; destroying intracellular viruses requires cell-mediated immunity via Cytotoxic T-cells (TC) and NK cells.',
        rationale: 'Antibody size prevents cytoplasmic entry.',
        ministryExamTip: 'Keywords: large molecular size, cannot penetrate cell membrane, requires TC / NK cells.',
      },
    ],
    examTraps: [
      {
        misconception: 'Believing antibodies enter host cells to kill viruses inside the cytoplasm.',
        scientificTruth: 'Antibodies are too large to cross the plasma membrane! Intracellular pathogens can ONLY be eliminated by Cell-Mediated Immunity (TC and NK cells).',
        examTrapWarning: 'Whenever an exam question mentions "virus inside liver cell" or "intracellular pathogen", the answer is Cytotoxic T-cells / Perforin, NEVER Antibodies alone!',
        goldenRule: 'Extracellular = Humoral Antibodies; Intracellular = TC Cells & Perforin.',
      },
    ],
    flashcards: [
      { id: 'fc_imm_1', topicId: 'immunity_humoral_cellular', frontPrompt: 'What surface receptor identifies T-Helper vs Cytotoxic T-cells?', backAnswer: 'TH has CD4; TC has CD8.', category: 'Cell Markers' },
      { id: 'fc_imm_2', topicId: 'immunity_humoral_cellular', frontPrompt: 'What substance terminates the immune response after pathogen eradication?', backAnswer: 'Lymphokines secreted by Suppressor T-cells (TS).', category: 'Homeostasis' },
    ],
    examSimulationItem: {
      id: 'sim_immunity',
      questionType: 'GIVE_REASON',
      prompt: 'Give Reason: Suppressor T-cells (TS) play an indispensable role in maintaining immunological tolerance and body safety.',
      maxScore: 2.0,
      requiredKeywords: ['suppressor t-cells', 'lymphokines', 'inhibit', 'plasma b-cells', 'homeostasis'],
      partialCreditKeywords: ['stop', 'apoptosis'],
      modelAnswer: 'Because Suppressor T-cells (TS) secrete chemical substances called lymphokines upon eradication of the pathogen. Lymphokines inhibit plasma B-cells from producing more antibodies and cause apoptosis of excess helper and cytotoxic T-cells, thus preventing autoimmune damage and returning the immune system to resting homeostasis.',
      remediationNotes: 'Mention lymphokines and the inhibition of plasma B-cells.',
    },
  },
};

// Return a study pack by topic ID with fallback
export function getMasterStudyPack(topicId: string): MasterStudyPack {
  return STUDY_PACKS[topicId] || STUDY_PACKS['sarcomere_contraction'];
}

export function getAllExamQuestions(): PracticeQuestion[] {
  return Object.values(STUDY_PACKS).flatMap((p) => p.questions);
}

export function getSimulatedMinistryExamPool(): PracticeQuestion[] {
  const all = getAllExamQuestions();
  // Return pool of diverse questions
  return all.slice(0, 15);
}

export function getDefaultTopicMasteries(): TopicMastery[] {
  const stabilities: Record<string, [number, number]> = {
    sarcomere_contraction: [16.5, 3.2],
    plant_human_support: [8.4, 4.1],
    hormonal_coordination_endocrine: [12.0, 4.8],
    reproduction_double_fertilization: [5.2, 5.6],
    human_reproduction_menstrual: [14.2, 3.9],
    immunity_humoral_cellular: [1.8, 6.2],
    dna_structure_replication: [18.4, 2.8],
    rna_transcription_translation: [6.8, 5.1],
    genetic_engineering_recombinant: [1.5, 7.0],
  };

  return SUB_TOPICS.map((sub) => {
    const [stab, diff] = stabilities[sub.id] || [5.0, 5.0];
    let level: MasteryLevel = 'NEEDS_REVIEW';
    if (stab >= 15.0) level = 'MASTERED';
    else if (stab >= 7.0) level = 'GOOD';
    else if (stab >= 2.5) level = 'LEARNING';

    const chap = CHAPTERS.find((c) => c.id === sub.chapterId);
    return {
      topicId: sub.id,
      title: sub.title,
      chapterTitle: chap ? chap.title : '',
      unit: sub.unit,
      stability: stab,
      difficulty: diff,
      reps: stab > 10 ? 6 : stab > 5 ? 3 : 1,
      masteryLevel: level,
    };
  });
}
