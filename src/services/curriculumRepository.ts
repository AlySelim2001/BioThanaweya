import {
  Chapter,
  MasterStudyPack,
  PracticeQuestion,
  SubTopic,
  TopicMastery,
  MasteryLevel,
} from '../types';

export const CHAPTERS: Chapter[] = [
  {
    id: 'unit1_chap1',
    unit: 'UNIT_1',
    number: 1,
    title: 'Support and Movement',
    arabicTitle: 'الدعامة والحركة في الكائنات الحية',
    summary:
      'Plant physiological/structural support, human skeleton, joints, tendons, ligaments, and muscular contraction mechanism.',
    iconName: 'Activity',
  },
  {
    id: 'unit1_chap2',
    unit: 'UNIT_1',
    number: 2,
    title: 'Hormonal Coordination',
    arabicTitle: 'التنسيق الهرموني في الكائنات الحية',
    summary:
      'Endocrine glands, master pituitary gland, negative feedback loops, blood sugar and calcium homeostasis.',
    iconName: 'Droplet',
  },
  {
    id: 'unit1_chap3',
    unit: 'UNIT_1',
    number: 3,
    title: 'Reproduction in Living Organisms',
    arabicTitle: 'التكاثر في الكائنات الحية',
    summary:
      'Alternation of generations (Fern & Plasmodium), double fertilization in angiosperms, human gametogenesis & menstrual cycle.',
    iconName: 'Repeat',
  },
  {
    id: 'unit1_chap4',
    unit: 'UNIT_1',
    number: 4,
    title: 'Immunity in Living Organisms',
    arabicTitle: 'المناعة في الكائنات الحية',
    summary:
      'Plant defense, human lymphatic system, antibodies, innate vs acquired immunity (humoral B-cells & cell-mediated T-cells).',
    iconName: 'Shield',
  },
  {
    id: 'unit2_chap1',
    unit: 'UNIT_2',
    number: 1,
    title: 'DNA & Genetic Information',
    arabicTitle: 'الحمض النووي والمعلومات الوراثية',
    summary:
      'Historical proof experiments (Griffith, Hershey-Chase), DNA structure & Chargaff ratios, replication, repair & nucleosomes.',
    iconName: 'Dna',
  },
  {
    id: 'unit2_chap2',
    unit: 'UNIT_2',
    number: 2,
    title: 'RNA & Protein Synthesis',
    arabicTitle: 'الأحماض النووية وتخليق البروتين',
    summary:
      'Types of RNA, transcription, translation, genetic code math, mutations, and genetic engineering technology.',
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
    summary:
      "Huxley's sliding filament mechanism, role of Ca²⁺ and ATP, motor units, and sarcomere calculations.",
    highYieldFlag: true,
  },
  {
    id: 'plant_human_support',
    chapterId: 'unit1_chap1',
    unit: 'UNIT_1',
    title: 'Plant Support & Human Skeletal System',
    arabicTitle: 'الدعامة في النبات والجهاز الهيكلي في الإنسان',
    summary:
      'Physiological vs structural support in plants, axial vs appendicular skeleton, joints, tendons vs ligaments.',
    highYieldFlag: true,
  },
  {
    id: 'hormonal_coordination_endocrine',
    chapterId: 'unit1_chap2',
    unit: 'UNIT_1',
    title: 'Pituitary Master Gland & Hormone Feedback',
    arabicTitle: 'الغدة النخامية والتغذية الراجعة الهرمونية',
    summary:
      'Anterior vs posterior pituitary lobes, FSH/LH, TSH, ACTH, ADH/vasopressin, and calcium/glucose regulation.',
    highYieldFlag: true,
  },
  {
    id: 'reproduction_double_fertilization',
    chapterId: 'unit1_chap3',
    unit: 'UNIT_1',
    title: 'Plant Reproduction & Alternation of Generations',
    arabicTitle: 'تعاقب الأجيال والإخصاب المزدوج في النباتات الزهرية',
    summary:
      'Life cycle of Polypodium & Plasmodium, micro/megasporogenesis, double fertilization and 3n endosperm formation.',
    highYieldFlag: true,
  },
  {
    id: 'human_reproduction_menstrual',
    chapterId: 'unit1_chap3',
    unit: 'UNIT_1',
    title: 'Human Reproduction & The Menstrual Cycle',
    arabicTitle: 'التكاثر في الإنسان ودورة الطمث',
    summary:
      'Spermatogenesis vs oogenesis, 28-day ovarian cycle, estrogen & progesterone curves, fertilization, and twins.',
    highYieldFlag: true,
  },
  {
    id: 'immunity_humoral_cellular',
    chapterId: 'unit1_chap4',
    unit: 'UNIT_1',
    title: 'Humoral vs Cell-Mediated Immunity',
    arabicTitle: 'المناعة الخلطية بالخلايا البائية والمناعة الخلوية بالخلايا التائية',
    summary:
      'Antibody anatomy & mechanisms, B-cells & plasma cells, helper TH (CD4+), cytotoxic TC (CD8+), perforin & lymphokines.',
    highYieldFlag: true,
  },
  {
    id: 'dna_structure_replication',
    chapterId: 'unit2_chap1',
    unit: 'UNIT_2',
    title: 'DNA Structure, Chargaff Ratios & Replication',
    arabicTitle: 'تركيب DNA وقواعد شارجاف وتضاعف المادة الوراثية',
    summary:
      "Avery & Hershey-Chase proofs, double helix 2nm dimensions, helicase, DNA polymerase 5'->3', ligase, Okazaki fragments.",
    highYieldFlag: true,
  },
  {
    id: 'rna_transcription_translation',
    chapterId: 'unit2_chap2',
    unit: 'UNIT_2',
    title: 'RNA Transcription, Translation & Genetic Code',
    arabicTitle: 'تخليق RNA وترجمة البروتين والشفرة الوراثية',
    summary:
      'mRNA processing, tRNA cloverleaf & anticodon, ribosomal subunits, peptidyl transferase, codon calculations, and mutations.',
    highYieldFlag: true,
  },
  {
    id: 'genetic_engineering_recombinant',
    chapterId: 'unit2_chap2',
    unit: 'UNIT_2',
    title: 'Genetic Engineering & Recombinant DNA',
    arabicTitle: 'الهندسة الوراثية و DNA معاد الاتحاد',
    summary:
      'Restriction endonucleases, sticky ends, DNA ligase, reverse transcriptase, cloning vectors/plasmids, PCR & Taq polymerase.',
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
        definition:
          'The distance between two successive Z-lines; structural and functional contractile unit of myofibril.',
        arabicTerm: 'القطعة العضلية',
      },
      {
        term: 'Myosin',
        definition:
          'Thick protein filament bearing cross-bridges (transverse hooks) that interact with actin.',
        arabicTerm: 'الميوسين',
      },
      {
        term: 'Actin',
        definition: 'Thin protein filament anchored to the Z-line.',
        arabicTerm: 'الأكتين',
      },
      {
        term: 'Cholinesterase',
        definition:
          'Enzyme that destroys acetylcholine into acetic acid and choline to terminate contraction.',
        arabicTerm: 'إنزيم كولين إستريز',
      },
      {
        term: 'Motor Unit',
        definition:
          'One motor neuron and all the muscle fibers it innervates (ranging from 5 to 100 fibers).',
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
        description:
          'Dark bands (A-bands) equal number of sarcomeres and NEVER change length during contraction.',
        variables: 'A = Number of Dark bands; n = Number of Sarcomeres',
        sampleCalculation: 'For 5 sarcomeres: A-bands = 5.',
      },
      {
        name: 'Complete I-Band Count',
        formulaLatex: 'I_{complete} = n - 1 = Z - 2',
        description:
          'Only internal light bands are complete; 2 incomplete bands exist at the outer ends.',
        variables: 'n = Number of Sarcomeres; Z = Number of Z-lines',
        sampleCalculation: 'For 5 sarcomeres: Complete I-bands = 5 - 1 = 4.',
      },
      {
        name: 'H-Zone Disappearance Rule',
        formulaLatex: 'H_{maximal} = 0',
        description:
          'During complete/strong contraction, actin filaments meet and overlap completely, obliterating the H-zone.',
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
          {
            criteria: 'Z-lines Distance',
            valueA: 'Normal / Far apart',
            valueB: 'Decreases / Move closer together',
          },
          { criteria: 'I-Band (Light Band)', valueA: 'Wide length', valueB: 'Decreases in width' },
          {
            criteria: 'H-Zone (Semi-Light)',
            valueA: 'Visible (Myosin only)',
            valueB: 'Decreases, vanishes completely in maximal contraction',
          },
          {
            criteria: 'A-Band (Dark Band)',
            valueA: 'Constant length',
            valueB: 'UNALTERED (Constant, length of myosin)',
          },
        ],
      },
      {
        title: 'Muscle Fatigue vs Muscle Spasm (Ministry Exam Trap)',
        columnA: 'Muscle Fatigue (إجهاد)',
        columnB: 'Muscle Spasm (شد عضلي)',
        rows: [
          {
            criteria: 'Primary Cause',
            valueA: 'Anaerobic respiration due to oxygen shortage; lactic acid accumulates',
            valueB: 'Absolute depletion of ATP OR erratic motor nerve impulses',
          },
          {
            criteria: 'Cross-Bridges',
            valueA: 'Detach slowly due to low ATP rate',
            valueB: 'Remain permanently attached to actin filaments',
          },
          {
            criteria: 'Contraction Force',
            valueA: 'Weak, sluggish contraction',
            valueB:
              'Severe sustained involuntary contraction; risk of muscle tearing and hemorrhage',
          },
          {
            criteria: 'Remedy',
            valueA: 'Rest & deep breathing to supply O₂',
            valueB: 'Immediate rest, anti-spasmodic, restore ATP synthesis',
          },
        ],
      },
    ],
    bloomProgression: [
      {
        levelName: 'Remembering',
        levelNumber: 1,
        objective: 'Recall structural proteins of myofibril and role of Ca²⁺.',
        masteryPrompt: 'What are the two protein filaments forming the sarcomere?',
      },
      {
        levelName: 'Understanding',
        levelNumber: 2,
        objective: 'Explain why the A-band length does not change while the I-band shortens.',
        masteryPrompt: "Why does the dark band remain constant during Huxley's sliding mechanism?",
      },
      {
        levelName: 'Applying',
        levelNumber: 3,
        objective: 'Calculate band counts given 8 consecutive Z-lines.',
        masteryPrompt:
          'Given 8 Z-lines: find Sarcomeres, A-bands, Complete I-bands, and H-zones during full contraction.',
      },
      {
        levelName: 'Analyzing',
        levelNumber: 4,
        objective:
          'Differentiate between fatigue caused by lactic acid vs spasm caused by ATP absence.',
        masteryPrompt: 'Analyze why a marathon runner suffers fatigue vs a sudden spasm.',
      },
      {
        levelName: 'Evaluating',
        levelNumber: 5,
        objective:
          'Assess physiological impact of synthetic organophosphate poisons inhibiting cholinesterase.',
        masteryPrompt: 'Predict the consequence of sarin gas inhibiting AChE on breathing muscles.',
      },
      {
        levelName: 'Creating',
        levelNumber: 6,
        objective: 'Formulate a therapeutic intervention to reverse muscle spasm safely.',
        masteryPrompt:
          'Design a rapid recovery protocol to uncouple locked cross-bridges without tissue damage.',
      },
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
      {
        stepNumber: 1,
        action: 'Nerve Impulse reaches Terminal Arborization',
        detail: 'Depolarizes axonal membrane',
        triggerOrEnzyme: 'Action Potential',
      },
      {
        stepNumber: 2,
        action: 'Voltage-gated Ca²⁺ channels open',
        detail: 'Ca²⁺ enters synaptic knob and ruptures vesicles',
        triggerOrEnzyme: 'Ca²⁺ ions',
      },
      {
        stepNumber: 3,
        action: 'Acetylcholine released into Synaptic Cleft',
        detail: 'Diffuses across cleft and binds sarcolemma receptors',
        triggerOrEnzyme: 'ACh neurotransmitter',
      },
      {
        stepNumber: 4,
        action: 'Sarcolemma Depolarization (+30 mV)',
        detail: 'T-tubules carry potential to Sarcoplasmic Reticulum',
        triggerOrEnzyme: 'Na⁺ influx',
      },
      {
        stepNumber: 5,
        action: 'SR releases Ca²⁺ into Sarcoplasm',
        detail: 'Ca²⁺ uncovers active sites on actin by moving tropomyosin',
        triggerOrEnzyme: 'Ca²⁺ from SR',
      },
      {
        stepNumber: 6,
        action: 'Myosin Cross-Bridges bind Actin',
        detail: 'ATP hydrolyzes to ADP + Pi; power stroke pulls actin',
        triggerOrEnzyme: 'Myosin ATPase',
      },
      {
        stepNumber: 7,
        action: 'Cholinesterase Hydrolyzes ACh',
        detail: 'Converts ACh to Acetic acid + Choline; sarcolemma repolarizes',
        triggerOrEnzyme: 'AChE Enzyme',
      },
    ],
    visualInspectionMarkers: [
      'Check Z-line distance: If narrow, muscle is contracted; if wide, muscle is relaxed.',
      'Inspect the H-zone in the center: In full contraction, H-zone is absent (width = 0).',
      'Examine Myosin transverse hooks (cross-bridges): Attached to actin = contracted state; detached = relaxed state.',
      'Verify A-band bracket: Must span entire length of thick myosin filaments.',
    ],
    youtube3DQueries: [
      {
        query: '3D Animation Sliding Filament Theory Muscle Contraction',
        topicDescription:
          'Shows dynamic motion of myosin heads pulling actin and ATP hydrolysis in high definition.',
        durationRecommendation: '3-5 mins',
      },
      {
        query: 'Neuromuscular Junction Acetylcholine Release 3D Animation',
        topicDescription:
          'Depicts synaptic vesicles rupturing, acetylcholine diffusing, and cholinesterase breakdown.',
        durationRecommendation: '2-4 mins',
      },
    ],
    questions: [
      {
        id: 'q1',
        type: 'SCIENTIFIC_TERM',
        question:
          'Write the scientific term: The structural and functional contractile unit located between two successive Z-lines in a skeletal muscle fiber.',
        officialAnswer: 'Sarcomere (القطعة العضلية)',
        rationale:
          'By definition in the Egyptian Ministry curriculum, the sarcomere is bounded by two successive Z-lines.',
        ministryExamTip: "Never confuse 'Sarcomere' with 'Myofibril' or 'Motor Unit'.",
      },
      {
        id: 'q2',
        type: 'GIVE_REASON',
        question:
          'Give reason: The A-band (Dark band) length remains constant during skeletal muscle contraction while the I-band length shortens.',
        officialAnswer:
          'Because the A-band corresponds to the entire length of the thick myosin filaments, which do not change their physical length during contraction. The I-band shortens because actin filaments slide over myosin filaments toward the center of the sarcomere, reducing the uncovered light zone.',
        rationale:
          "Huxley's sliding theory states filaments slide past one another without shortening themselves.",
        ministryExamTip:
          "Must mention: 'A-band equals the length of thick myosin filaments' for full mark.",
      },
      {
        id: 'q3',
        type: 'WHAT_HAPPENS_IF',
        question:
          'What happens if: Total absence of cholinesterase enzyme at the motor end plate of a skeletal muscle fiber?',
        officialAnswer:
          'Acetylcholine will not be hydrolyzed into acetic acid and choline. The sarcolemma will remain permanently depolarized, keeping the muscle in continuous state of contraction (unable to relax), which may lead to muscle spasm and tearing.',
        rationale:
          'Cholinesterase is indispensable for muscle relaxation and resting potential restoration.',
        ministryExamTip:
          "Keywords required: 'Acetylcholine persists', 'No repolarization', 'Continuous contraction/spasm'.",
      },
      {
        id: 'q4',
        type: 'MCQ',
        question:
          'If a myofibril contains 6 Z-lines, what is the number of complete I-bands and H-zones during maximal contraction respectively?',
        options: ['5 and 5', '4 and 0', '5 and 0', '4 and 5'],
        correctOptionIndex: 1,
        officialAnswer: '4 and 0',
        rationale:
          'Sarcomeres = Z - 1 = 6 - 1 = 5. Complete I-bands = Sarcomeres - 1 = 5 - 1 = 4. During maximal contraction, H-zones vanish completely, so H = 0.',
        ministryExamTip: "Watch out for 'maximal contraction' trap! H-zone becomes 0.",
      },
    ],
    examTraps: [
      {
        misconception: 'Thinking muscle filaments shrink like an accordion during contraction.',
        scientificTruth:
          'Actin and Myosin filaments maintain their exact length! They only SLIDE past one another, reducing the distance between Z-lines.',
        examTrapWarning:
          "Ministry MCQ trap: 'Which of the following shortens during contraction?' Option A: Myosin, Option B: Actin, Option C: Sarcomere. The correct answer is ONLY Sarcomere/I-band/H-zone!",
        goldenRule: 'Filaments slide; Sarcomeres shorten.',
      },
      {
        misconception:
          'Confusing the role of Calcium ions in the nerve terminal vs in the muscle sarcoplasm.',
        scientificTruth:
          'Ca²⁺ in nerve knob ruptures synaptic vesicles to release ACh. Ca²⁺ in muscle sarcoplasm unmasks actin binding sites by shifting troponin/tropomyosin.',
        examTrapWarning:
          "Questions specifying 'Role of Ca²⁺ at the neuromuscular junction' require mentioning synaptic vesicle rupture!",
        goldenRule:
          'Synaptic knob Ca²⁺ = Neurotransmitter release; Sarcoplasmic Ca²⁺ = Cross-bridge attachment.',
      },
    ],
    flashcards: [
      {
        id: 'fc1',
        topicId: 'sarcomere_contraction',
        frontPrompt: 'What is the structural and functional contractile unit of a muscle fiber?',
        backAnswer: 'The Sarcomere (distance between two successive Z-lines).',
        category: 'Terminology',
      },
      {
        id: 'fc2',
        topicId: 'sarcomere_contraction',
        frontPrompt: 'What happens to the H-zone during maximal muscle contraction?',
        backAnswer:
          'It completely disappears (becomes zero) as actin filaments slide and overlap entirely.',
        category: 'Mechanism',
      },
      {
        id: 'fc3',
        topicId: 'sarcomere_contraction',
        frontPrompt: 'What are the dual requirements for both muscle contraction AND relaxation?',
        backAnswer:
          'ATP and Calcium ions (ATP is required to attach AND detach cross-bridges; Ca²⁺ is pumped back by ATP).',
        category: 'Physiology',
      },
      {
        id: 'fc4',
        topicId: 'sarcomere_contraction',
        frontPrompt:
          'What is the mathematical formula for number of complete I-bands given Z-lines?',
        backAnswer: 'Complete I-bands = Z - 2 (or Sarcomeres - 1).',
        category: 'Formula',
      },
    ],
    examSimulationItem: {
      id: 'sim_sarcomere',
      questionType: 'GIVE_REASON',
      prompt:
        'Give Reason: The absence of ATP in a skeletal muscle fiber leads to sustained muscle spasm.',
      maxScore: 2.0,
      requiredKeywords: ['atp', 'cross bridges', 'detach', 'actin', 'spasm'],
      partialCreditKeywords: ['myosin', 'relaxation', 'tear'],
      modelAnswer:
        'Because ATP is necessary for the detachment of myosin cross-bridges (transverse hooks) from actin filaments. In the absence of ATP, cross-bridges remain locked to actin filaments, preventing muscle relaxation and causing painful, sustained muscle spasm.',
      remediationNotes:
        'You must explicitly explain that ATP is required to BREAK the cross-bridge bond, not just for contraction.',
    },
    pblProject: {
      id: 'pbl_sarcomere',
      topicId: 'sarcomere_contraction',
      title: 'Neuromuscular Toxin Investigation',
      titleArabic: 'مهمة فك شفرة السموم العصبية العضلية',
      scenario:
        'A patient presents with severe rigidity and respiratory muscle lockdown after exposure to an unknown pesticide.',
      mission:
        'Identify whether the toxin targets voltage-gated Ca²⁺ channels, acetylcholine receptors, or cholinesterase enzyme.',
      rubrics: [
        {
          criterion: 'Diagnostic Accuracy',
          weightPoints: 40,
          excellentDescription: 'Pinpoints cholinesterase inhibition with biochemical mechanism.',
          poorDescription: 'Confuses pre-synaptic and post-synaptic targets.',
        },
        {
          criterion: 'Therapeutic Plan',
          weightPoints: 30,
          excellentDescription:
            'Recommends pralidoxime and atropine to restore receptor equilibrium.',
          poorDescription: 'Fails to address neurotransmitter degradation.',
        },
        {
          criterion: 'Ministry Mark Scheme Terminology',
          weightPoints: 30,
          excellentDescription: 'Uses acetylcholine, depolarization, transverse hooks accurately.',
          poorDescription: 'Uses vague colloquial language without ions.',
        },
      ],
      tips: ['Remember: Irreversible AChE inhibitors cause continuous depolarization!'],
    },
  },

  plant_human_support: {
    topicId: 'plant_human_support',
    topicTitle: 'Support in Plants & the Human Skeleton',
    unit: 'UNIT_1',
    chapterTitle: 'Chapter 1: Support and Movement',
    eli5Analogy:
      'A plant cell is like a water balloon inside a cardboard box. Fill the balloon and it pushes on the box so everything stands firm (physiological support, temporary). Now reinforce the box walls with wood or cement: even if the water dries up, the box stays hard (structural support, permanent). The human skeleton is the steel frame of a skyscraper: a central beam (axial skeleton) carrying outer arms and scaffolding (appendicular skeleton), with ligaments as tape between bones and tendons as cables from the muscle motors.',
    arabicEli5:
      'خلية النبات زي بالونة مية جوه علبة كرتون. لما البالونة تمتلي بتدفع على العلبة فتبقى مشدودة وواقفة (دعامة فسيولوجية مؤقتة). ولو بطّنت جدران العلبة بخشب أو أسمنت، حتى لو المية نشفت تفضل العلبة صلبة (دعامة تركيبية دائمة). أما هيكل الإنسان فهو عمود مبنى عالي: العمود الأوسط (الهيكل المحوري) والأذرع والسقالات الخارجية (الهيكل الطرفي)، والأربطة شريط لاصق بين العظام، والأوتار كابلات تشد العظام من محركات العضلات.',
    textbookPageReference: 'Ministry Textbook Unit 1, Chapter 1, pp. 4–13',
    deepTechnicalBreakdown: `### 1. Physiological Support in Plants
Temporary, reversible, and involves the **entire living cell as a whole**.
1. **Mechanism**: Water enters the cell sap vacuole by **osmosis** due to higher solute concentration inside the vacuole.
2. **Turgor Pressure**: The vacuole swells, pressing the protoplasm against the elastic cellulose cell wall. The wall stretches and pushes back (**wall pressure**).
3. **Equilibrium**: The cell becomes fully **turgid**, imparting firmness to herbaceous stems and leaves.
4. **Wilting & Plasmolysis**: In dry soil or hypertonic environments, water exits by osmosis; turgor pressure drops to zero, the protoplasm shrinks, and the plant wilts.

### 2. Structural Support in Plants
Permanent, irreversible chemical deposition of dead or thickened mechanical substances in or on cell walls:
- **Cutin**: Waxy layer deposited on the outer tangential walls of the **epidermis**. It is **impermeable to water**; prevents transpirational water loss and thus indirectly preserves physiological support.
- **Suberin**: Impermeable fatty substance deposited in the cell walls of secondary **cork tissue**.
- **Cellulose**: Deposited in **collenchyma** cell walls; **permeable to water**; provides tensile strength with flexibility (e.g. celery petioles).
- **Lignin**: Extremely hard, **impermeable** polymer impregnated with cellulose in **sclerenchyma** (fibres and stone cells) and **xylem vessels/tracheids**; confers rigidity and compressive strength.

### 3. The Human Skeleton (Total = 206 Bones in Adults)
Divided into two primary divisions:

#### A. Axial Skeleton (80 Bones)
1. **Vertebral Column (33 vertebrae = 26 functional bone units)**:
   - **7 Cervical vertebrae**: Neck region; moderate size; first is the *Atlas* (articulates with skull occipital condyles), second is the *Axis*.
   - **12 Thoracic vertebrae**: Chest region; larger size; articulate posteriorly with 12 pairs of ribs via vertebral body and transverse processes.
   - **5 Lumbar vertebrae**: Abdominal region; largest and heaviest vertebrae bearing bodily weight.
   - **5 Sacral vertebrae**: Fused into **1 single wedge-shaped bone** (Sacrum); broad and flat; articulates with pelvic ilium.
   - **4 Coccygeal vertebrae**: Fused into **1 small triangular bone** (Coccyx).
   *(Total articulating vertebrae = 24; Total fused vertebrae = 9; Total bones = 24 + 1 + 1 = 26 bones).*
2. **Skull and Associated Bones (29 Bones)**:
   - **Cerebral / Cranial part (8 bones)**: Flat bones interlocking via serrated sutures; forms cranial cavity protecting brain; base contains the **Foramen Magnum** for spinal cord passage.
   - **Facial part (14 bones)**: Frontal bones of face, two jaws, and sockets for sense organs (eyes, ears, nose).
   - **Auditory ossicles (6 bones)**: Malleus, Incus, Stapes (3 in each middle ear).
   - **Hyoid bone (1 bone)**: U-shaped bone in the throat.
3. **Thoracic Cage (25 Bones)**:
   - **Sternum (1 bone)**: Broad, flat bone; lower tip is cartilaginous.
   - **Ribs (12 pairs = 24 bones)**: Curved bones curving downward and forward.
     - Pairs 1–7: True ribs (connect directly to sternum via costal cartilages).
     - Pairs 8–10: False ribs (connect indirectly to sternum via cartilage of 7th rib).
     - Pairs 11–12: **Floating ribs (ضلوع عائمة)**: Short; do not reach sternum; protect kidneys and allow diaphragm expansion.

#### B. Appendicular Skeleton (126 Bones)
1. **Pectoral Girdle & Upper Limbs (64 Bones)**:
   - **Pectoral Girdle (4 bones)**: 2 Scapulae (triangular back bone with spine and **Glenoid cavity**) + 2 Clavicles (slender ventral collar bones).
   - **Upper Limbs (30 × 2 = 60 bones)**: Humerus (1), Radius (1, rotates semi-circularly around stationary ulna), Ulna (1, has upper cavity for humerus trochlea), Carpals (8 in two rows), Metacarpals (5), Phalanges (14: 2 in thumb, 3 in other digits).
2. **Pelvic Girdle & Lower Limbs (62 Bones)**:
   - **Pelvic Girdle (2 bones)**: 2 symmetrical halves fused at the ventral **Pubic Symphysis**; each half consists of Ilium, Ischium, and Pubis fused together. Deep **Acetabulum cavity** receives femur head.
   - **Lower Limbs (30 × 2 = 60 bones)**: Femur (1, large upper head and two lower condyles), Patella (1, kneecap), Tibia (1, medial shin bone), Fibula (1, lateral slender bone), Tarsals (7 bones, largest is the posterior **Calcaneus / heel bone**), Metatarsals (5), Phalanges (14).

### 4. Skeletal Connective Tissues & Articulations
- **Cartilage**: Avascular connective tissue nourished by **diffusion** from surrounding bone tissue; lines articulating ends to reduce friction.
- **Joints**:
  1. *Fibrous joints*: Non-movable; bones joined by fibrous connective tissue that ossifies with age (e.g. skull sutures).
  2. *Cartilaginous joints*: Limited movement; disc of cartilage separates bone ends (e.g. intervertebral discs, pubic symphysis).
  3. *Synovial joints*: Freely movable; covered by smooth articular cartilage and lubricated by **synovial fluid**.
     - Limited movement: Knee and Elbow joints (hinge, moves in 1 plane).
     - Wide movement: Shoulder and Hip joints (ball-and-socket, moves in multiple planes).
- **Ligaments**: Strong, highly **elastic** bundles connecting **bone to bone**; determine and limit the directional range of joint motion (e.g. Cruciate ligaments in knee: Anterior, Posterior, Medial, Lateral).
- **Tendons**: Extremely tough, non-elastic fibrous cords connecting **muscle to bone**; transmit muscular pulling force (e.g. **Achilles tendon / وتر أخيل** connecting the gastrocnemius calf muscle to the calcaneus heel bone).
  - *Rupture causes*: Excessive physical effort, sudden calf muscle contraction, or loss of muscular elasticity.
  - *Treatment*: Anti-inflammatory drugs and splints if partial; surgical intervention if complete tear.`,
    keyTerms: [
      {
        term: 'Physiological Support',
        definition:
          'Temporary osmotic support involving the entire plant cell via turgor pressure exerted by the swollen sap vacuole on the cell wall.',
        arabicTerm: 'الدعامة الفسيولوجية',
      },
      {
        term: 'Structural Support',
        definition:
          'Permanent mechanical support achieved by deposition of impermeable or rigid chemical substances (cutin, suberin, cellulose, lignin) in or on cell walls.',
        arabicTerm: 'الدعامة التركيبية',
      },
      {
        term: 'Turgor Pressure',
        definition:
          'Outward hydrostatic pressure exerted by the fluid contents of the vacuole against the elastic cell wall.',
        arabicTerm: 'ضغط الامتلاء',
      },
      {
        term: 'Axial Skeleton',
        definition:
          'The central longitudinal axis of the human skeleton comprising 80 bones: vertebral column (26 bones), skull & accessories (29 bones), and thoracic cage (25 bones).',
        arabicTerm: 'الهيكل المحوري',
      },
      {
        term: 'Appendicular Skeleton',
        definition:
          'The peripheral framework comprising 126 bones: pectoral girdle and upper limbs (64 bones) plus pelvic girdle and lower limbs (62 bones).',
        arabicTerm: 'الهيكل الطرفي',
      },
      {
        term: 'Ligament',
        definition:
          'Dense fibrous connective tissue connecting bone to bone with high elasticity to control the direction of joint movement.',
        arabicTerm: 'الرباط',
      },
      {
        term: 'Tendon',
        definition:
          'Strong, non-elastic fibrous cord connecting muscle to bone to transmit muscle contraction force, such as the Achilles tendon.',
        arabicTerm: 'الوتر',
      },
    ],
    formulas: [
      {
        name: 'Vertebral Column Bones vs Vertebrae Count',
        formulaLatex:
          '\\text{Bones} = 24 + 1 + 1 = 26 \\quad (\\text{from } 33 \\text{ vertebrae})',
        description:
          'While humans have 33 total vertebrae, the 5 sacral vertebrae fuse into 1 bone and 4 coccygeal fuse into 1 bone, yielding 26 adult functional bones.',
        variables: '24 articulating vertebrae + 1 sacrum + 1 coccyx = 26 bones',
        sampleCalculation:
          'Number of articulating vertebrae = 7 + 12 + 5 = 24. Number of fused vertebral bones = 2.',
      },
      {
        name: 'Rib Attachment Number to Vertebra Number',
        formulaLatex: '\\text{Vertebra Number} = \\text{Rib Pair Number} + 7',
        description:
          'Connects the anatomical pair number of ribs to its corresponding thoracic vertebra in the vertebral column (adding the 7 cervical vertebrae).',
        variables: 'Rib pair (1 to 12) + 7 cervical = Dorsal vertebra sequence number (8 to 19)',
        sampleCalculation:
          'Rib pair 10 connects to vertebra number = 10 + 7 = 17 (which is the 10th thoracic vertebra). Floating rib pair 1 connects to vertebra 11 + 7 = 18.',
      },
      {
        name: 'Appendicular Skeleton Division',
        formulaLatex: '126 = (4 + 60) + (2 + 60)',
        description:
          'Pectoral girdle (4) + Upper limbs (60) + Pelvic girdle (2) + Lower limbs (60) = 126 bones.',
        variables: 'Upper division = 64 bones; Lower division = 62 bones',
        sampleCalculation:
          'Single limb bones = 30 bones. Both upper + both lower = 30 * 4 = 120 bones.',
      },
    ],
    comparisonTables: [
      {
        title: 'Physiological Support vs Structural Support in Plants',
        columnA: 'Physiological Support (دعامة فسيولوجية)',
        columnB: 'Structural Support (دعامة تركيبية)',
        rows: [
          {
            criteria: 'Permanence',
            valueA: 'Temporary (depends on water availability)',
            valueB: 'Permanent (irreversible chemical deposits)',
          },
          {
            criteria: 'Scope',
            valueA: 'Involves the entire living cell as a whole',
            valueB: 'Involves cell walls or parts of the wall',
          },
          {
            criteria: 'Key Mechanism',
            valueA: 'Osmosis ➔ Vacuole swells ➔ Turgor pressure',
            valueB: 'Chemical precipitation of structural polymers',
          },
          {
            criteria: 'Substances Involved',
            valueA: 'Water entering the cell sap',
            valueB: 'Cellulose, Lignin, Cutin, Suberin',
          },
          {
            criteria: 'Living Status of Cells',
            valueA: 'Requires living protoplasm and intact vacuole',
            valueB: 'Can exist in living (collenchyma) or dead cells (sclerenchyma, cork, xylem)',
          },
        ],
      },
      {
        title: 'Ligaments vs Tendons (Ministry Exam Criteria)',
        columnA: 'Ligaments (الأربطة)',
        columnB: 'Tendons (الأوتار)',
        rows: [
          {
            criteria: 'Connection',
            valueA: 'Connects Bone to Bone at joints',
            valueB: 'Connects Muscle to Bone',
          },
          {
            criteria: 'Elasticity',
            valueA: 'High degree of elasticity (allows joint stretch without dislocation)',
            valueB: 'Strong, non-elastic fibrous tissue (transmits pull directly)',
          },
          {
            criteria: 'Primary Function',
            valueA: 'Determines and limits movement directions at joints',
            valueB: 'Transmits muscular contraction force to move the skeleton',
          },
          {
            criteria: 'Classic Clinical Injury',
            valueA: 'Cruciate ligament sprain or tear (تمزق الرباط الصليبي)',
            valueB: 'Achilles tendon rupture (تمزق وتر أخيل)',
          },
        ],
      },
      {
        title: 'Joint Types in the Human Body',
        columnA: 'Joint Classification',
        columnB: 'Mobility & Ministry Examples',
        rows: [
          {
            criteria: 'Fibrous Joints (مفاصل ليفية)',
            valueA: 'Bones locked by fibrous connective tissue that turns to bone with age',
            valueB: 'Immovable; Serrated sutures between cranial skull bones',
          },
          {
            criteria: 'Cartilaginous Joints (مفاصل غضروفية)',
            valueA: 'Cartilaginous pad separates adjacent articulating bones',
            valueB: 'Very limited movement; Intervertebral discs, Pubic symphysis',
          },
          {
            criteria: 'Synovial Joints (مفاصل زلالية)',
            valueA: 'Cartilage-coated ends bathed in lubricating synovial fluid',
            valueB: 'Freely movable: Hinge (knee/elbow) & Ball-and-Socket (shoulder/hip)',
          },
        ],
      },
    ],
    bloomProgression: [
      {
        levelName: 'Remembering',
        levelNumber: 1,
        objective: 'Recall total bone counts of axial (80) and appendicular (126) skeletons.',
        masteryPrompt:
          'How many total bones form the adult human skeleton, and how many belong to the axial division?',
      },
      {
        levelName: 'Understanding',
        levelNumber: 2,
        objective:
          'Explain why physiological support in plants is considered temporary while structural is permanent.',
        masteryPrompt:
          'Why does a wilted basil plant regain firmness within hours of watering, while boiled wood remains rigid forever?',
      },
      {
        levelName: 'Applying',
        levelNumber: 3,
        objective: 'Calculate the number of the vertebra articulating with the 2nd floating rib.',
        masteryPrompt:
          'Which vertebra number in the vertebral column articulates with the second pair of floating ribs?',
      },
      {
        levelName: 'Analyzing',
        levelNumber: 4,
        objective:
          'Differentiate between the structural consequences of ligament damage versus tendon rupture.',
        masteryPrompt:
          'Compare the joint dysfunction caused by an anterior cruciate ligament tear to that caused by an Achilles tendon rupture.',
      },
      {
        levelName: 'Evaluating',
        levelNumber: 5,
        objective:
          'Judge the physiological relationship between Cutin deposition and plant turgor retention.',
        masteryPrompt:
          'Assess the statement: "Structural support can protect and maintain physiological support."',
      },
      {
        levelName: 'Creating',
        levelNumber: 6,
        objective:
          'Formulate an anatomical rationale for the structural differences between the pectoral and pelvic girdles.',
        masteryPrompt:
          'Hypothesize why the pelvic girdle consists of 2 fused bones with a deep socket, while the pectoral girdle has 4 bones with a shallow cavity.',
      },
    ],
    asciiSchematic: `================= PLANT CELL SUPPORT DYNAMICS =================
   PLASMOLYZED (Flaccid)                TURGID (Firm Support)
   +-----------------------+           +-----------------------+
   |   CELL WALL (Rigid)   |           |   CELL WALL (Tensed)  |
   |  +-----------------+  |           | +-------------------+ |
   |  | Shrunken Plasma |  |  +H2O     | | Plasma Membrane   | |
   |  |   Membrane      |  | ======>   | | Pressed firmly    | |
   |  |  [Small Vacuole]|  | (Osmosis) | | [Turgid Vacuole]  | |
   |  +-----------------+  |           | | (High Turgor P.)  | |
   +-----------------------+           | +-------------------+ |
     Water exits / Wilts               +-----------------------+
                                        Stem & Leaves stand erect

=================== HUMAN SKELETON (206 BONES) ===================
            TOTAL SKELETON: 206 BONES
                 /             \\
      AXIAL (80 Bones)         APPENDICULAR (126 Bones)
      |-- Vertebral Col: 26    |-- Pectoral Girdle: 4 (2 Scapula, 2 Clavicle)
      |   (33 Vertebrae)       |-- Upper Limbs: 60 (30 x 2)
      |-- Skull & Acc: 29      |-- Pelvic Girdle: 2 (2 Innominate Halves)
      |-- Thoracic Cage: 25    \\-- Lower Limbs: 60 (30 x 2)
          (1 Sternum + 24 Ribs)`,
    schematicCaption:
      'Plant Turgor Dynamics & Human Skeleton Hierarchy: Notice that 33 vertebrae fuse into 26 distinct functional bones in adults, while each limb contains precisely 30 bones.',
    flowcharts: [
      {
        stepNumber: 1,
        action: 'Water absorption from soil',
        detail: 'Enters root hair sap vacuole via osmosis due to high osmotic pressure',
        triggerOrEnzyme: 'Osmotic gradient',
      },
      {
        stepNumber: 2,
        action: 'Endosmosis into cortical parenchyma',
        detail: 'Water moves cell to cell until herbaceous stem cells swell',
        triggerOrEnzyme: 'Turgor pressure',
      },
      {
        stepNumber: 3,
        action: 'Transpiration through stomata',
        detail: 'If transpiration exceeds uptake, cell sap volume diminishes',
        triggerOrEnzyme: 'Stomatal water loss',
      },
      {
        stepNumber: 4,
        action: 'Cutin & Suberin barrier activation',
        detail: 'Waxy deposits prevent excessive cuticular transpiration',
        triggerOrEnzyme: 'Structural barrier',
      },
      {
        stepNumber: 5,
        action: 'Skeletal weight bearing',
        detail: 'Lumbar vertebrae bear vertical torso weight down to sacrum and pelvic ilium',
        triggerOrEnzyme: 'Gravity & Axial skeleton',
      },
      {
        stepNumber: 6,
        action: 'Muscular force transmission',
        detail: 'Gastrocnemius calf muscle pulls Achilles tendon, elevating the calcaneus heel',
        triggerOrEnzyme: 'Tendon tension',
      },
    ],
    visualInspectionMarkers: [
      'In cross-sections: Check epidermis wall thickening (Cutin = outer wall only; Collenchyma = uneven cellulose angles; Sclerenchyma = uniform lignin).',
      'Vertebra diagrams: Distinguish Centrum (anterior body), Neural arch, Neural spine (directed downward), Transverse processes (with rib facets).',
      'Rib identification: Floating ribs 11 and 12 are very short and have NO anterior connection to the sternum.',
      'Limb bone counting: Both Upper and Lower limbs have 30 bones each (Upper has 8 carpals; Lower has 7 tarsals + 1 patella).',
    ],
    youtube3DQueries: [
      {
        query: 'Plant cell turgor pressure and plasmolysis 3D animation',
        topicDescription:
          'Visualizes sap vacuole swelling, cell wall elasticity, and plant wilting at the microscopic level.',
        durationRecommendation: '3-4 mins',
      },
      {
        query: 'Human skeleton 3D anatomy axial and appendicular',
        topicDescription:
          'Detailed walkthrough of skull sutures, vertebral column articulation, and pectoral vs pelvic girdles.',
        durationRecommendation: '5-7 mins',
      },
    ],
    questions: [
      {
        id: 'q_supp_1',
        type: 'SCIENTIFIC_TERM',
        question:
          'Write the scientific term: A waxy chemical substance deposited on the outer epidermal walls of plants that is impermeable to water and indirectly preserves physiological support.',
        officialAnswer: 'Cutin (الكيوتين)',
        rationale:
          'Cutin forms a waterproof cuticle on epidermal surfaces, preventing evaporation and maintaining cell turgor.',
        ministryExamTip: 'Do not confuse Cutin (on epidermis) with Suberin (in cork cell walls).',
      },
      {
        id: 'q_supp_2',
        type: 'GIVE_REASON',
        question:
          'Give reason: The adult human vertebral column is counted as 26 bones, despite consisting of 33 vertebrae.',
        officialAnswer:
          'Because the 24 cervical (7), thoracic (12), and lumbar (5) vertebrae are separate articulating bones, while the 5 sacral vertebrae are completely fused into 1 single bone (the sacrum) and the 4 coccygeal vertebrae are fused into 1 single bone (the coccyx): 24 + 1 + 1 = 26 bones.',
        rationale: 'Fusion in adults reduces 9 lower vertebrae into 2 functional bone units.',
        ministryExamTip:
          'Must mention the exact breakdown: 24 separate + 1 sacral unit + 1 coccygeal unit.',
      },
      {
        id: 'q_supp_3',
        type: 'WHAT_HAPPENS_IF',
        question:
          'What happens if: A competitive athlete suffers a complete tear of the Achilles tendon (وتر أخيل)?',
        officialAnswer:
          'The athlete will be unable to walk or flex the foot downward, accompanied by acute pain and severe swelling in the heel area; this condition requires urgent surgical intervention to suture the ruptured tendon ends.',
        rationale:
          'Tendons lack elasticity; a complete tear severs the connection between the calf muscle and the calcaneus.',
        ministryExamTip: 'Keywords: inability to walk, acute pain, surgical intervention required.',
      },
      {
        id: 'q_supp_4',
        type: 'MCQ',
        question:
          'Which dorsal vertebra in the human vertebral column articulates with the second pair of floating ribs (rib pair 12)?',
        options: ['Vertebra 17', 'Vertebra 18', 'Vertebra 19', 'Vertebra 12'],
        correctOptionIndex: 2,
        officialAnswer: 'Vertebra 19',
        rationale:
          'Vertebra number = Rib pair number + 7 cervical vertebrae. Second floating rib is pair 12: 12 + 7 = Vertebra 19 (which is thoracic vertebra 12).',
        ministryExamTip:
          'Always add 7 cervical vertebrae to the rib pair number to determine the absolute vertebra sequence!',
      },
    ],
    examTraps: [
      {
        misconception:
          'Equating the number of vertebrae with the number of bones in the vertebral column.',
        scientificTruth:
          'Vertebrae count = 33; Bone units count = 26 (because 5 sacral = 1 bone, and 4 coccygeal = 1 bone).',
        examTrapWarning:
          'Read the question carefully: If it asks for "number of vertebrae", write 33. If it asks for "number of bones in the vertebral column", write 26!',
        goldenRule: '33 Vertebrae = 26 Bones.',
      },
      {
        misconception: 'Thinking ligaments and tendons have identical physical elasticity.',
        scientificTruth:
          'Ligaments are highly elastic (allowing joint leeway); tendons are strong and non-elastic (transmitting pull directly).',
        examTrapWarning:
          'If asked why a joint dislocates: Ligament rupture or overstretching. If asked why foot movement fails despite muscle contraction: Tendon rupture (Achilles)!',
        goldenRule: 'Ligaments = Elastic (Bone to Bone); Tendons = Inelastic (Muscle to Bone).',
      },
      {
        misconception:
          'Forgetting that both upper and lower human limbs have the exact same total number of bones (30 bones).',
        scientificTruth:
          'Upper limb has 30 bones (Humerus 1 + Radius/Ulna 2 + Carpals 8 + Metacarpals 5 + Phalanges 14). Lower limb has 30 bones (Femur 1 + Patella 1 + Tibia/Fibula 2 + Tarsals 7 + Metatarsals 5 + Phalanges 14).',
        examTrapWarning:
          'Lower limb has 1 fewer tarsal (7 vs 8 carpals), but gains 1 patella kneecap, keeping the sum at 30!',
        goldenRule: 'Single limb = 30 bones always.',
      },
    ],
    flashcards: [
      {
        id: 'fc_supp_1',
        topicId: 'plant_human_support',
        frontPrompt:
          'What is the only plant structural substance that is completely PERMEABLE to water?',
        backAnswer: 'Cellulose (found in collenchyma and parenchyma walls).',
        category: 'Plant Biology',
      },
      {
        id: 'fc_supp_2',
        topicId: 'plant_human_support',
        frontPrompt: 'How many total bones are in the axial skeleton vs the appendicular skeleton?',
        backAnswer: 'Axial = 80 bones; Appendicular = 126 bones (Total = 206).',
        category: 'Anatomy',
      },
      {
        id: 'fc_supp_3',
        topicId: 'plant_human_support',
        frontPrompt:
          'What formula calculates the vertebral column sequence number for a given rib pair?',
        backAnswer: 'Vertebra Sequence Number = Rib Pair Number + 7 (cervical vertebrae).',
        category: 'Formulas',
      },
      {
        id: 'fc_supp_4',
        topicId: 'plant_human_support',
        frontPrompt:
          'What structure connects the gastrocnemius calf muscle to the calcaneus heel bone?',
        backAnswer: 'Achilles tendon (وتر أخيل).',
        category: 'Connective Tissue',
      },
    ],
    examSimulationItem: {
      id: 'sim_plant_support',
      questionType: 'GIVE_REASON',
      prompt:
        'Give Reason: Deposition of Cutin on the epidermal cells of plant leaves plays a dual role in supporting the plant.',
      maxScore: 2.0,
      requiredKeywords: [
        'structural support',
        'impermeable',
        'transpiration',
        'physiological support',
        'turgor',
      ],
      partialCreditKeywords: ['water loss', 'wilting', 'epidermis'],
      modelAnswer:
        'Because Cutin itself is a permanent chemical substance deposited on the outer walls of epidermal cells, acting directly as structural support. Furthermore, being impermeable to water, it prevents excessive transpirational water loss, which indirectly maintains the cell turgor and preserves physiological support.',
      remediationNotes:
        'Full mark requires explaining both: 1) Its direct structural role, and 2) Its indirect preservation of physiological turgor.',
    },
  },

  hormonal_coordination_endocrine: {
    topicId: 'hormonal_coordination_endocrine',
    topicTitle: 'Plant Auxins & the Human Endocrine System',
    unit: 'UNIT_1',
    chapterTitle: 'Chapter 2: Hormonal Coordination',
    eli5Analogy:
      'Hormones are chemical letters. In a plant, auxin is a broadcast message sent from the growing tip telling cells on the shaded side to grow longer, so the stem bends toward the light. In humans, endocrine glands post targeted letters into the bloodstream; only cells with the matching mailbox (receptor) open them. A thermostat is the best picture of negative feedback: when the output reaches the set level, it switches the source off.',
    arabicEli5:
      'الهرمونات رسايل كيميائية. في النبات، الأوكسين رسالة بتطلع من القمة النامية وتقول لخلايا الجانب المظلل تطول أكتر، فالساق تنحني ناحية الضوء. وفي الإنسان، الغدد الصماء بتبعت جوابات مخصوصة في الدم، ومفيش غير الخلايا اللي معاها صندوق البريد المناسب (المستقبل) اللي تفتحها. والتغذية الراجعة السالبة زي الترموستات: لما الناتج يوصل للمستوى المطلوب يوقف المصدر.',
    textbookPageReference:
      'Ministry Textbook Unit 1, Chapter 2 (page numbers to be filled from the printed book)',
    deepTechnicalBreakdown: `### 1. Auxins (Plant Hormones)
- Organic substances (mainly **indole acetic acid, IAA**) made in the **apical meristems** (shoot tip, young leaves) and transported to the zones of response.
- **Stems**: auxin stimulates cell elongation.
- **Roots**: the concentration that stimulates stems **inhibits** root elongation.
- **Phototropism**: with one-sided light, auxin moves from the lit side to the shaded side; the shaded side elongates more and the stem bends toward the light.
- **Apical dominance**: auxin from the apical bud inhibits lateral buds.
- **Parthenocarpy**: auxin applied to the stigma can produce seedless fruit.

### 2. The Endocrine System
Endocrine glands are **ductless**; hormones pass into the blood. Hormones are either **steroids** (lipid soluble, enter cells) or **amino-acid based** (peptides and proteins, bind surface receptors). Control is mainly by **negative feedback**.

### 3. Pituitary Gland (master gland)
- **Anterior lobe** (glandular): GH, TSH, ACTH, FSH, LH, prolactin.
- **GH disorders**: hyposecretion in childhood = dwarfism; hypersecretion in childhood = gigantism; hypersecretion in adults = acromegaly.
- **Posterior lobe** (neural): stores and releases **ADH** and **oxytocin**, which are made by neurosecretory cells of the **hypothalamus**. ADH raises water reabsorption in the kidney tubules; oxytocin causes uterine contraction and milk ejection.

### 4. Thyroid and Parathyroids
- **Thyroxine** (needs iodine) raises the metabolic rate. Iodine lack = simple goiter; hypersecretion = exophthalmic goiter; hyposecretion = cretinism in childhood and myxedema in adults.
- **Calcitonin** (thyroid) lowers blood calcium; **parathormone, PTH** (parathyroids) raises blood calcium. Parathyroid removal lowers calcium and causes muscle spasms (tetany).

### 5. Adrenal Glands
- **Cortex**: glucocorticoids (cortisol; carbohydrate metabolism), mineralocorticoids (aldosterone; retains Na+, excretes K+), sex hormones.
- **Medulla**: adrenaline and noradrenaline for the emergency (fight or flight) response.

### 6. Pancreas (mixed gland)
**Islets of Langerhans**: beta cells secrete **insulin** (lowers blood glucose, promotes glycogen storage); alpha cells secrete **glucagon** (raises blood glucose by converting liver glycogen to glucose). Diabetes mellitus (insulin deficiency) gives hyperglycemia, glucosuria, polyuria and thirst. Diabetes insipidus is caused by ADH deficiency and gives large dilute urine without glucose.`,
    keyTerms: [
      {
        term: 'Auxin',
        definition:
          'Plant hormone (mainly IAA) made in apical meristems that stimulates stem cell elongation.',
        arabicTerm: 'الأوكسين',
      },
      {
        term: 'Endocrine Gland',
        definition: 'Ductless gland that releases hormones directly into the blood.',
        arabicTerm: 'الغدة الصماء',
      },
      {
        term: 'Negative Feedback',
        definition: 'Regulation in which the output inhibits further secretion of the hormone.',
        arabicTerm: 'التغذية الراجعة السالبة',
      },
      {
        term: 'Phototropism',
        definition: 'Directional growth of a plant organ in response to one-sided light.',
        arabicTerm: 'الانتحاء الضوئي',
      },
      {
        term: 'Neurohypophysis',
        definition:
          'Posterior pituitary: stores and releases ADH and oxytocin made in the hypothalamus.',
        arabicTerm: 'النخامية الخلفية',
      },
      {
        term: 'Goiter',
        definition: 'Enlargement of the thyroid gland (iodine deficiency or hyperthyroidism).',
        arabicTerm: 'الجويتر',
      },
      {
        term: 'Acromegaly',
        definition: 'Overgrowth of hands, feet and jaw due to GH hypersecretion in adults.',
        arabicTerm: 'ضخامة الأطراف',
      },
      {
        term: 'Islets of Langerhans',
        definition: 'Endocrine clusters in the pancreas secreting insulin and glucagon.',
        arabicTerm: 'جزر لانجرهانز',
      },
    ],
    formulas: [
      {
        name: 'Blood Glucose Homeostasis',
        formulaLatex: 'Glucose \\uparrow \\rightarrow Insulin \\rightarrow Glucose \\downarrow',
        description:
          'High glucose stimulates beta cells; low glucose stimulates alpha cells to release glucagon, completing the loop.',
        variables: 'Insulin lowers glucose; glucagon raises glucose (liver)',
        sampleCalculation:
          'After a sugary meal: glucose rises, insulin is released, glucose is stored as glycogen, glucose falls, insulin release decreases.',
      },
      {
        name: 'Blood Calcium Homeostasis',
        formulaLatex:
          'Ca^{2+} \\uparrow \\rightarrow Calcitonin;\\; Ca^{2+} \\downarrow \\rightarrow PTH',
        description: 'Calcitonin lowers blood calcium; parathormone raises it.',
        variables: 'Calcitonin: thyroid; PTH: parathyroids',
        sampleCalculation:
          'Low blood calcium: PTH is released, calcium moves from bone to blood, level returns to normal.',
      },
    ],
    comparisonTables: [
      {
        title: 'Anterior vs Posterior Pituitary',
        columnA: 'Anterior Lobe',
        columnB: 'Posterior Lobe',
        rows: [
          {
            criteria: 'Type',
            valueA: 'Glandular tissue',
            valueB: 'Neural tissue (neurosecretory)',
          },
          {
            criteria: 'Hormones',
            valueA: 'GH, TSH, ACTH, FSH, LH, prolactin (made here)',
            valueB: 'ADH and oxytocin (made in hypothalamus, stored and released here)',
          },
          {
            criteria: 'Control',
            valueA: 'Hypothalamic releasing factors',
            valueB: 'Nerve impulses from the hypothalamus',
          },
        ],
      },
      {
        title: 'Calcitonin vs Parathormone',
        columnA: 'Calcitonin',
        columnB: 'Parathormone (PTH)',
        rows: [
          { criteria: 'Gland', valueA: 'Thyroid', valueB: 'Parathyroids' },
          { criteria: 'Stimulus', valueA: 'High blood calcium', valueB: 'Low blood calcium' },
          {
            criteria: 'Effect',
            valueA: 'Lowers blood calcium (deposition in bone)',
            valueB: 'Raises blood calcium (release from bone)',
          },
        ],
      },
      {
        title: 'Insulin vs Glucagon',
        columnA: 'Insulin',
        columnB: 'Glucagon',
        rows: [
          { criteria: 'Cells', valueA: 'Beta cells', valueB: 'Alpha cells' },
          { criteria: 'Stimulus', valueA: 'High blood glucose', valueB: 'Low blood glucose' },
          {
            criteria: 'Effect',
            valueA: 'Lowers glucose; stores glycogen',
            valueB: 'Raises glucose; liver glycogen to glucose',
          },
        ],
      },
    ],
    bloomProgression: [
      {
        levelName: 'Remembering',
        levelNumber: 1,
        objective: 'List the hormones of the anterior and posterior pituitary.',
        masteryPrompt: 'Which pituitary lobe stores ADH?',
      },
      {
        levelName: 'Understanding',
        levelNumber: 2,
        objective: 'Explain how auxin causes a stem to bend toward light.',
        masteryPrompt: 'Why does the shaded side elongate more?',
      },
      {
        levelName: 'Applying',
        levelNumber: 3,
        objective: 'Predict the effect of hormone excess or deficiency.',
        masteryPrompt: 'What does iodine deficiency cause, and why?',
      },
      {
        levelName: 'Analyzing',
        levelNumber: 4,
        objective: 'Differentiate diabetes mellitus from diabetes insipidus.',
        masteryPrompt: 'Both cause thirst and polyuria. How do urine tests differ?',
      },
      {
        levelName: 'Evaluating',
        levelNumber: 5,
        objective: 'Judge the effect of removing the parathyroids.',
        masteryPrompt: 'Predict calcium level and symptoms after parathyroid removal.',
      },
      {
        levelName: 'Creating',
        levelNumber: 6,
        objective: 'Design an experiment testing auxin and phototropism.',
        masteryPrompt: 'Design an experiment using coleoptiles with the tip removed or covered.',
      },
    ],
    asciiSchematic: `PHOTOTROPISM (one-sided light)
      light  --->
   +-----------+
   | lit side  |  less auxin  -> grows less
   |-----------|
   | shaded    |  more auxin  -> grows more
   +-----------+
   stem bends TOWARD the light

NEGATIVE FEEDBACK
 glucose HIGH -> insulin -> glucose LOW
 glucose LOW  -> glucagon -> glucose HIGH

 hypothalamus -> pituitary -> target gland
      ^                          |
      +------ hormone level -----+  (inhibits)`,
    schematicCaption:
      'Auxin redistribution in phototropism and the negative feedback loops of glucose and the hypothalamus-pituitary axis.',
    flowcharts: [
      {
        stepNumber: 1,
        action: 'Blood glucose rises after a meal',
        detail: 'Beta cells of the islets detect the rise',
        triggerOrEnzyme: 'High glucose',
      },
      {
        stepNumber: 2,
        action: 'Insulin is secreted',
        detail: 'Carried by the blood to liver, muscle and other cells',
        triggerOrEnzyme: 'Insulin',
      },
      {
        stepNumber: 3,
        action: 'Cells take up glucose',
        detail: 'Liver and muscle store it as glycogen',
        triggerOrEnzyme: 'Insulin',
      },
      {
        stepNumber: 4,
        action: 'Blood glucose falls',
        detail: 'Insulin secretion decreases (negative feedback)',
        triggerOrEnzyme: 'Negative feedback',
      },
      {
        stepNumber: 5,
        action: 'Glucose falls too low',
        detail: 'Alpha cells are stimulated',
        triggerOrEnzyme: 'Low glucose',
      },
      {
        stepNumber: 6,
        action: 'Glucagon converts liver glycogen to glucose',
        detail: 'Blood glucose returns to normal',
        triggerOrEnzyme: 'Glucagon',
      },
    ],
    visualInspectionMarkers: [
      'Phototropism diagram: look for more auxin dots on the shaded side and the stem curving toward the light.',
      'Pituitary diagram: separate the anterior glandular lobe from the posterior neural lobe attached to the hypothalamus.',
      'Pancreas diagram: find the pale islets among darker exocrine tissue.',
      'Calcium loop diagram: calcitonin lowers calcium; PTH raises it.',
    ],
    youtube3DQueries: [
      {
        query: 'Auxin phototropism animation plant hormones',
        topicDescription: 'Shows auxin moving to the shaded side and the stem bending.',
        durationRecommendation: '2-4 mins',
      },
      {
        query: 'Endocrine system hormones negative feedback animation',
        topicDescription: 'Overview of glands, hormones and feedback loops.',
        durationRecommendation: '3-6 mins',
      },
      {
        query: 'Insulin and glucagon blood glucose regulation animation',
        topicDescription: 'Shows beta and alpha cell responses and liver glycogen.',
        durationRecommendation: '2-4 mins',
      },
    ],
    questions: [
      {
        id: 'hce_q1',
        type: 'SCIENTIFIC_TERM',
        question:
          'Write the scientific term: Clusters of endocrine cells in the pancreas that secrete insulin and glucagon.',
        officialAnswer: 'Islets of Langerhans (جزر لانجرهانز)',
        rationale: 'They form the endocrine part of the pancreas, a mixed gland.',
        ministryExamTip:
          'Do not confuse them with the exocrine acini that secrete digestive enzymes.',
      },
      {
        id: 'hce_q2',
        type: 'GIVE_REASON',
        question:
          'Give reason: The posterior lobe of the pituitary is described as neurosecretory.',
        officialAnswer:
          'Because it does not make its own hormones; ADH and oxytocin are made by neurosecretory cells in the hypothalamus, travel down their axons and are stored in the posterior lobe until released into the blood.',
        rationale: 'The posterior lobe is nervous tissue acting as a storage and release site.',
        ministryExamTip: 'State that the hormones are made in the hypothalamus.',
      },
      {
        id: 'hce_q3',
        type: 'GIVE_REASON',
        question: 'Give reason: Iodized salt is recommended in the diet.',
        officialAnswer:
          'Because iodine is needed to make thyroxine; iodine deficiency causes enlargement of the thyroid gland (simple goiter).',
        rationale:
          'Low thyroxine removes feedback inhibition, so TSH stays high and the gland enlarges.',
        ministryExamTip: 'Link iodine to thyroxine and then to goiter.',
      },
      {
        id: 'hce_q4',
        type: 'WHAT_HAPPENS_IF',
        question: 'What happens if the parathyroid glands are removed?',
        officialAnswer:
          'Parathormone is no longer secreted, blood calcium falls, and the person suffers muscle spasms (tetany).',
        rationale: 'PTH is the hormone that raises blood calcium.',
        ministryExamTip: 'Keywords: PTH absent, calcium falls, muscle spasm.',
      },
      {
        id: 'hce_q5',
        type: 'DIAGRAM_ANALYSIS',
        question:
          'A seedling is lit from the right only and bends to the right. Explain the bending using auxin.',
        officialAnswer:
          'Auxin moves from the lit side to the shaded (left) side of the tip; the higher auxin concentration on the shaded side makes those cells elongate more, so the stem bends toward the light (positive phototropism).',
        rationale: 'Auxin stimulates elongation of stem cells.',
        ministryExamTip: 'Mention the lateral movement of auxin and unequal elongation.',
      },
      {
        id: 'hce_q6',
        type: 'MCQ',
        question: 'Which hormone raises blood calcium?',
        options: ['Calcitonin', 'Parathormone', 'Insulin', 'Aldosterone'],
        correctOptionIndex: 1,
        officialAnswer: 'Parathormone',
        rationale:
          'PTH from the parathyroids releases calcium from bone; calcitonin does the opposite.',
        ministryExamTip: 'Calcitonin lowers; PTH raises.',
      },
      {
        id: 'hce_q7',
        type: 'MCQ',
        question:
          'A patient has extreme thirst and large urine volume, but urine tests show no glucose. The likely cause is:',
        options: ['Insulin deficiency', 'ADH deficiency', 'Excess thyroxine', 'Excess cortisol'],
        correctOptionIndex: 1,
        officialAnswer: 'ADH deficiency (diabetes insipidus)',
        rationale:
          'Without ADH the kidney tubules reabsorb less water, giving large dilute urine without glucose.',
        ministryExamTip: 'No glucose in the urine rules out diabetes mellitus.',
      },
      {
        id: 'hce_q8',
        type: 'MCQ',
        question: 'Glucagon raises blood glucose by acting mainly on:',
        options: ['Skeletal muscle', 'The liver', 'The kidney', 'Fat tissue'],
        correctOptionIndex: 1,
        officialAnswer: 'Glucagon raises blood glucose by acting mainly on the liver.',
        rationale: 'It converts stored liver glycogen into glucose.',
        ministryExamTip: 'Insulin acts on liver and muscle; glucagon acts on the liver.',
      },
    ],
    examTraps: [
      {
        misconception: 'The posterior pituitary makes ADH and oxytocin.',
        scientificTruth:
          'They are made in the hypothalamus and only stored and released by the posterior lobe.',
        examTrapWarning: 'Questions ask where a hormone is made versus released.',
        goldenRule: 'Made in the hypothalamus, released from the posterior pituitary.',
      },
      {
        misconception: 'Stems and roots respond to the same auxin concentration in the same way.',
        scientificTruth:
          'A concentration that stimulates stem elongation inhibits root elongation.',
        examTrapWarning: 'Questions compare the response of stems and roots.',
        goldenRule: 'Same auxin level: stem grows, root is inhibited.',
      },
      {
        misconception: 'All patients with excessive thirst and urination have diabetes mellitus.',
        scientificTruth:
          'Diabetes insipidus (ADH deficiency) gives the same symptoms but without glucose in the urine.',
        examTrapWarning: 'Case questions state that the urine contains no glucose.',
        goldenRule: 'Glucose in urine: mellitus. No glucose: insipidus.',
      },
    ],
    flashcards: [
      {
        id: 'hce_fc1',
        topicId: 'hormonal_coordination_endocrine',
        frontPrompt: 'Where are ADH and oxytocin made and where are they released?',
        backAnswer: 'Made in the hypothalamus, released from the posterior pituitary.',
        category: 'Physiology',
      },
      {
        id: 'hce_fc2',
        topicId: 'hormonal_coordination_endocrine',
        frontPrompt: 'Effect of GH hypersecretion in childhood vs adults?',
        backAnswer: 'Childhood: gigantism. Adults: acromegaly.',
        category: 'Disorders',
      },
      {
        id: 'hce_fc3',
        topicId: 'hormonal_coordination_endocrine',
        frontPrompt: 'Which hormone lowers blood calcium?',
        backAnswer: 'Calcitonin (PTH raises it).',
        category: 'Terminology',
      },
      {
        id: 'hce_fc4',
        topicId: 'hormonal_coordination_endocrine',
        frontPrompt: 'Why does the stem bend toward light?',
        backAnswer: 'Auxin moves to the shaded side, which elongates more.',
        category: 'Mechanism',
      },
      {
        id: 'hce_fc5',
        topicId: 'hormonal_coordination_endocrine',
        frontPrompt: 'Hormones of the adrenal medulla?',
        backAnswer: 'Adrenaline and noradrenaline (emergency response).',
        category: 'Terminology',
      },
      {
        id: 'hce_fc6',
        topicId: 'hormonal_coordination_endocrine',
        frontPrompt: 'Thyroxine deficiency in children vs adults?',
        backAnswer: 'Children: cretinism. Adults: myxedema.',
        category: 'Disorders',
      },
    ],
    examSimulationItem: {
      id: 'sim_hormonal_coordination_endocrine',
      questionType: 'GIVE_REASON',
      prompt:
        'Give reason: A patient with exophthalmic goiter loses weight and has a high body temperature and a fast heartbeat.',
      maxScore: 2,
      requiredKeywords: ['thyroxine', 'metabolic rate', 'oxidation'],
      partialCreditKeywords: ['hypersecretion', 'heat', 'energy', 'heart'],
      modelAnswer:
        'Because hypersecretion of thyroxine raises the basal metabolic rate and the oxidation of food, which releases extra heat (high temperature), consumes stored food (weight loss) and speeds the heart.',
      remediationNotes:
        'Revise the functions of thyroxine and the disorders of its hypo- and hypersecretion.',
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
      {
        term: 'Deoxyribonuclease',
        definition: 'Enzyme that completely degrades DNA without hydrolyzing RNA or proteins.',
        arabicTerm: 'دي أوكسي ريبونيوكليز',
      },
      {
        term: 'Antiparallel',
        definition: "Arrangement where one DNA strand runs 5' to 3' and the other 3' to 5'.",
        arabicTerm: 'متعاكس الاتجاه',
      },
      {
        term: 'Okazaki Fragments',
        definition: 'Short DNA fragments synthesized discontinuously on the lagging strand.',
        arabicTerm: 'قطع أوكازاكي',
      },
      {
        term: 'DNA Ligase',
        definition: 'Enzyme that seals nicks in the sugar-phosphate backbone.',
        arabicTerm: 'إنزيم الربط',
      },
    ],
    formulas: [
      {
        name: "Chargaff's Rule",
        formulaLatex: '%A = %T, %G = %C',
        description: 'In double-stranded DNA, purines equal pyrimidines.',
        variables: 'A, T, G, C',
        sampleCalculation: 'If %A = 20%, %T = 20%, %G = 30%, %C = 30%.',
      },
      {
        name: 'DNA Turns Formula',
        formulaLatex: 'Turns = BasePairs / 10 = TotalNucleotides / 20',
        description: 'Complete turns in a DNA double helix.',
        variables: 'Turns',
        sampleCalculation: 'For 200 nucleotides: 200 / 20 = 10 turns.',
      },
      {
        name: 'Hydrogen Bonds',
        formulaLatex: 'H = 2(A) + 3(G)',
        description: 'Total hydrogen bonds between complementary strands.',
        variables: 'A, G count',
        sampleCalculation: 'For 40 A and 60 G: 2(40) + 3(60) = 260 bonds.',
      },
    ],
    comparisonTables: [
      {
        title: 'DNA Polymerase vs DNA Ligase',
        columnA: 'DNA Polymerase',
        columnB: 'DNA Ligase',
        rows: [
          {
            criteria: 'Catalytic Direction',
            valueA: "Strictly 5' to 3'",
            valueB: 'Seals nicks on lagging strand',
          },
          {
            criteria: 'Leading Strand Action',
            valueA: 'Continuous synthesis toward replication fork',
            valueB: 'Not required on leading strand',
          },
          {
            criteria: 'Lagging Strand Action',
            valueA: 'Synthesizes Okazaki fragments discontinuously',
            valueB: 'Joins Okazaki fragments into continuous strand',
          },
          {
            criteria: 'Role in DNA Repair',
            valueA: 'Adds correct complementary nucleotide into gap',
            valueB: 'Seals phosphodiester backbone',
          },
        ],
      },
    ],
    bloomProgression: [
      {
        levelName: 'Remembering',
        levelNumber: 1,
        objective: 'Recall the 3 enzymes involved in DNA replication.',
        masteryPrompt: 'Name the enzymes that unwind, polymerize, and seal DNA.',
      },
      {
        levelName: 'Understanding',
        levelNumber: 2,
        objective: 'Explain why replication is semi-conservative.',
        masteryPrompt: 'Why does each daughter molecule have one template and one new strand?',
      },
      {
        levelName: 'Applying',
        levelNumber: 3,
        objective: 'Calculate base pairs and turns for a 400-nucleotide gene with 30% C.',
        masteryPrompt: 'If %C = 30% in 400 nucleotides, find number of Adenine bases and turns.',
      },
      {
        levelName: 'Analyzing',
        levelNumber: 4,
        objective: 'Analyze why RNA viruses mutate faster than DNA organisms.',
        masteryPrompt: 'Why do single-stranded RNA viruses lack proofreading?',
      },
      {
        levelName: 'Evaluating',
        levelNumber: 5,
        objective: 'Evaluate Avery vs Griffith experiments.',
        masteryPrompt:
          'Why was Deoxyribonuclease essential to prove DNA is the transforming principle?',
      },
      {
        levelName: 'Creating',
        levelNumber: 6,
        objective: 'Design an experimental probe using radioactive labeling.',
        masteryPrompt: 'How could you prove whether mitochondria replicate their own DNA?',
      },
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
    schematicCaption:
      'DNA Replication Fork: Helicase separates strands, Leading strand synthesized continuously, Lagging strand discontinuously in Okazaki fragments.',
    flowcharts: [
      {
        stepNumber: 1,
        action: 'Helicase uncoils double helix',
        detail: 'Breaks hydrogen bonds at replication origin',
        triggerOrEnzyme: 'Helicase',
      },
      {
        stepNumber: 2,
        action: 'SSB Proteins stabilize single strands',
        detail: 'Prevents premature re-annealing',
        triggerOrEnzyme: 'SSB Proteins',
      },
      {
        stepNumber: 3,
        action: 'DNA Polymerase synthesizes Leading Strand',
        detail: "Continuous 5' to 3' synthesis toward fork",
        triggerOrEnzyme: 'DNA Polymerase',
      },
      {
        stepNumber: 4,
        action: 'DNA Polymerase synthesizes Lagging Strand',
        detail: 'Discontinuous synthesis forming Okazaki fragments',
        triggerOrEnzyme: 'DNA Polymerase',
      },
      {
        stepNumber: 5,
        action: 'DNA Ligase seals fragments',
        detail: 'Creates phosphodiester bonds to complete strand',
        triggerOrEnzyme: 'DNA Ligase',
      },
    ],
    visualInspectionMarkers: [
      "Check strand polarity: 5' end has free phosphate group; 3' end has free OH hydroxyl group.",
      'Verify base pairing: Purine (2 rings) pairs with Pyrimidine (1 ring) ensuring constant 2nm width.',
      'Inspect Okazaki arrows: Points away from replication fork for lagging strand.',
    ],
    youtube3DQueries: [
      {
        query: '3D Animation DNA Replication Fork Okazaki Fragments',
        topicDescription: 'Shows leading and lagging strand polymerase actions in high definition.',
        durationRecommendation: '3-5 mins',
      },
      {
        query: 'Hershey and Chase Experiment 3D Animation Bacteriophage',
        topicDescription: 'Visualizes viral injection of radioactive phosphorus vs sulfur.',
        durationRecommendation: '2-3 mins',
      },
    ],
    questions: [
      {
        id: 'q_dna_1',
        type: 'SCIENTIFIC_TERM',
        question:
          'Write the scientific term: The enzyme that specifically degrades DNA completely and was used to conclusively confirm DNA as the transforming substance.',
        officialAnswer: 'Deoxyribonuclease enzyme (إنزيم دي أوكسي ريبونيوكليز)',
        rationale:
          "Avery's experiment was finalized using deoxyribonuclease, which eliminated transformation.",
        ministryExamTip: "Do not write 'Ribonuclease' or just 'Nuclease'.",
      },
      {
        id: 'q_dna_2',
        type: 'GIVE_REASON',
        question:
          "Give reason: The two strands of the DNA double helix are antiparallel (one 5'->3' and the other 3'->5').",
        officialAnswer:
          'To ensure that the nitrogenous bases face inward correctly and form hydrogen bonds between complementary base pairs (A=T with 2 bonds and G≡C with 3 bonds).',
        rationale:
          'If parallel, the hydrogen bonding angles between purines and pyrimidines would not fit stably.',
        ministryExamTip:
          'Keywords: hydrogen bonds, bases face inwards, complementary base pairing.',
      },
      {
        id: 'q_dna_3',
        type: 'MCQ',
        question:
          'A DNA sample of 600 nucleotides contains 120 Adenine bases. What is the number of complete turns and total hydrogen bonds?',
        options: [
          '30 turns and 780 bonds',
          '60 turns and 600 bonds',
          '30 turns and 660 bonds',
          '20 turns and 800 bonds',
        ],
        correctOptionIndex: 0,
        officialAnswer: '30 turns and 780 bonds',
        rationale:
          'Turns = 600 / 20 = 30. A = 120, so T = 120. Total A+T = 240. Remaining G+C = 600 - 240 = 360, so G = 180, C = 180. H-bonds = 2(120) + 3(180) = 240 + 540 = 780 bonds.',
        ministryExamTip: 'Divide total nucleotides by 20 for turns in double helix!',
      },
    ],
    examTraps: [
      {
        misconception: 'Calculating complete turns by dividing nucleotides by 10 instead of 20.',
        scientificTruth:
          'Each turn consists of 10 BASE PAIRS, which equals 20 nucleotides in double-stranded DNA.',
        examTrapWarning:
          'If the question says "single strand", 10 nucleotides per turn. If "double-stranded DNA", divide total nucleotides by 20!',
        goldenRule: 'Turns = Base Pairs / 10 = Total Nucleotides / 20.',
      },
    ],
    flashcards: [
      {
        id: 'fc_dna_1',
        topicId: 'dna_structure_replication',
        frontPrompt: 'What enzyme proved conclusively that DNA is the transforming factor?',
        backAnswer: 'Deoxyribonuclease enzyme.',
        category: 'Experiments',
      },
      {
        id: 'fc_dna_2',
        topicId: 'dna_structure_replication',
        frontPrompt: 'How many hydrogen bonds connect Guanine and Cytosine?',
        backAnswer: '3 Hydrogen Bonds (A=T has 2).',
        category: 'Structure',
      },
      {
        id: 'fc_dna_3',
        topicId: 'dna_structure_replication',
        frontPrompt: 'In which direction does DNA Polymerase synthesize new strands?',
        backAnswer: "Strictly in the 5' to 3' direction.",
        category: 'Replication',
      },
    ],
    examSimulationItem: {
      id: 'sim_dna',
      questionType: 'GIVE_REASON',
      prompt:
        'Give Reason: The synthesis of the lagging strand during DNA replication occurs discontinuously.',
      maxScore: 2.0,
      requiredKeywords: ['dna polymerase', "5' to 3'", 'replication fork', 'okazaki', 'ligase'],
      partialCreditKeywords: ['antiparallel', 'unwinding'],
      modelAnswer:
        "Because DNA Polymerase can only add nucleotides in the 5' to 3' direction. Since the lagging template strand runs 5' to 3' toward the fork, the newly synthesized strand must be built away from the replication fork in short segments called Okazaki fragments, which are subsequently sealed by DNA ligase.",
      remediationNotes: "Emphasize that DNA Polymerase is strictly unidirectional (5' to 3').",
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
      {
        term: 'Perforin',
        definition:
          'Specialized protein secreted by TC cells that creates pores in target infected cell membrane.',
        arabicTerm: 'بروتين البيرفورين',
      },
      {
        term: 'Lymphokines',
        definition:
          'Inhibitory chemical messengers secreted by TS cells to deactivate and terminate the immune response.',
        arabicTerm: 'الليمفوكاينات',
      },
      {
        term: 'MHC',
        definition:
          'Major Histocompatibility Complex; protein that binds antigen fragments for presentation.',
        arabicTerm: 'بروتين التوافق النسيجي',
      },
    ],
    formulas: [],
    comparisonTables: [
      {
        title: 'Humoral Immunity vs Cell-Mediated Immunity',
        columnA: 'Humoral Immunity (خلطية)',
        columnB: 'Cell-Mediated Immunity (خلوية)',
        rows: [
          {
            criteria: 'Effector Cells',
            valueA: 'Plasma B-cells',
            valueB: 'Cytotoxic T-cells (TC) & NK cells',
          },
          {
            criteria: 'Weapons Used',
            valueA: 'Antibodies (Immunoglobulins)',
            valueB: 'Perforin & Lymphotoxins',
          },
          {
            criteria: 'Target Location',
            valueA: 'Extracellular pathogens in blood/lymph',
            valueB: 'Intracellular viral infections, cancer, transplanted tissues',
          },
          {
            criteria: 'Cell Entry',
            valueA: 'Antibodies CANNOT penetrate cell membranes (too large)',
            valueB: 'TC cells directly engage and destroy altered host cells',
          },
        ],
      },
    ],
    bloomProgression: [
      {
        levelName: 'Remembering',
        levelNumber: 1,
        objective: 'Recall CD markers for TH, TC, and TS.',
        masteryPrompt: 'Identify which T-cells bear CD4 vs CD8.',
      },
      {
        levelName: 'Understanding',
        levelNumber: 2,
        objective: 'Explain why antibodies cannot destroy intracellular viruses directly.',
        masteryPrompt:
          'Why is humoral immunity ineffective against viruses hidden inside host cells?',
      },
      {
        levelName: 'Applying',
        levelNumber: 3,
        objective: 'Predict immune response to organ transplantation rejection.',
        masteryPrompt: 'Which specific immune cells mediate kidney graft rejection?',
      },
      {
        levelName: 'Analyzing',
        levelNumber: 4,
        objective: 'Analyze the mechanism of Suppressor T-cells.',
        masteryPrompt: 'How do lymphokines prevent autoimmune overreaction?',
      },
      {
        levelName: 'Evaluating',
        levelNumber: 5,
        objective: 'Evaluate the efficacy of vaccines vs antivenom serums.',
        masteryPrompt:
          'Differentiate between active immunity (vaccine) and passive immunity (serum).',
      },
      {
        levelName: 'Creating',
        levelNumber: 6,
        objective: 'Formulate an immunotherapy targeting cancer markers.',
        masteryPrompt: 'How can monoclonal antibodies be conjugated to cytotoxic agents?',
      },
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
    schematicCaption:
      'Antibody Structure: 2 Heavy Chains + 2 Light Chains held by Disulfide bonds (S-S), with 2 identical Antigen-Binding sites.',
    flowcharts: [
      {
        stepNumber: 1,
        action: 'Macrophage engulfs pathogen',
        detail: 'Digests antigen and couples with MHC protein',
        triggerOrEnzyme: 'Phagocytosis',
      },
      {
        stepNumber: 2,
        action: 'TH Cell binds Antigen-MHC',
        detail: 'Receptor CD4 recognizes complex on macrophage',
        triggerOrEnzyme: 'CD4 Receptor',
      },
      {
        stepNumber: 3,
        action: 'TH secretes Interleukins',
        detail: 'Activates B-cells and triggers clonal expansion',
        triggerOrEnzyme: 'Interleukins',
      },
      {
        stepNumber: 4,
        action: 'B-cells differentiate',
        detail: 'Produce Plasma B-cells (Antibodies) and Memory cells',
        triggerOrEnzyme: 'Clonal Selection',
      },
      {
        stepNumber: 5,
        action: 'TC destroys infected cells',
        detail: 'Secretes Perforin to pierce membrane and Lymphotoxins',
        triggerOrEnzyme: 'Perforin',
      },
      {
        stepNumber: 6,
        action: 'TS stops immune response',
        detail: 'Secretes Lymphokines when pathogen is cleared',
        triggerOrEnzyme: 'Lymphokines',
      },
    ],
    visualInspectionMarkers: [
      'Verify 4 polypeptide chains: 2 identical heavy chains and 2 identical light chains.',
      'Check Disulfide bonds: 4 disulfide bonds hold the antibody structure.',
      'Identify Variable regions at tips forming 2 identical antigen-binding pockets.',
    ],
    youtube3DQueries: [
      {
        query: '3D Animation Cell Mediated Immunity Cytotoxic T cell Perforin',
        topicDescription: 'Shows TC engagement and perforin pore formation.',
        durationRecommendation: '3-4 mins',
      },
      {
        query: 'Humoral Immunity Antibody Production B Cell Plasma 3D',
        topicDescription: 'Visualizes macrophage antigen presentation and antibody secretion.',
        durationRecommendation: '3-5 mins',
      },
    ],
    questions: [
      {
        id: 'q_imm_1',
        type: 'SCIENTIFIC_TERM',
        question:
          'Write the scientific term: The chemical protein secreted by Cytotoxic T-cells that perforates the membrane of infected cells.',
        officialAnswer: 'Perforin protein (بروتين البيرفورين / البروتين صانع الثقوب)',
        rationale: 'Secreted by activated TC cells to punch holes in the target membrane.',
        ministryExamTip: 'Do not confuse with Lymphotoxins which digest nuclear DNA.',
      },
      {
        id: 'q_imm_2',
        type: 'GIVE_REASON',
        question:
          'Give reason: Antibodies are ineffective in eradicating intracellular viruses like COVID-19 or Influenza.',
        officialAnswer:
          'Because antibodies are relatively large molecular protein structures that cannot penetrate the cell membrane to reach viruses multiplying inside host cells; destroying intracellular viruses requires cell-mediated immunity via Cytotoxic T-cells (TC) and NK cells.',
        rationale: 'Antibody size prevents cytoplasmic entry.',
        ministryExamTip:
          'Keywords: large molecular size, cannot penetrate cell membrane, requires TC / NK cells.',
      },
    ],
    examTraps: [
      {
        misconception:
          'Believing antibodies enter host cells to kill viruses inside the cytoplasm.',
        scientificTruth:
          'Antibodies are too large to cross the plasma membrane! Intracellular pathogens can ONLY be eliminated by Cell-Mediated Immunity (TC and NK cells).',
        examTrapWarning:
          'Whenever an exam question mentions "virus inside liver cell" or "intracellular pathogen", the answer is Cytotoxic T-cells / Perforin, NEVER Antibodies alone!',
        goldenRule: 'Extracellular = Humoral Antibodies; Intracellular = TC Cells & Perforin.',
      },
    ],
    flashcards: [
      {
        id: 'fc_imm_1',
        topicId: 'immunity_humoral_cellular',
        frontPrompt: 'What surface receptor identifies T-Helper vs Cytotoxic T-cells?',
        backAnswer: 'TH has CD4; TC has CD8.',
        category: 'Cell Markers',
      },
      {
        id: 'fc_imm_2',
        topicId: 'immunity_humoral_cellular',
        frontPrompt: 'What substance terminates the immune response after pathogen eradication?',
        backAnswer: 'Lymphokines secreted by Suppressor T-cells (TS).',
        category: 'Homeostasis',
      },
    ],
    examSimulationItem: {
      id: 'sim_immunity',
      questionType: 'GIVE_REASON',
      prompt:
        'Give Reason: Suppressor T-cells (TS) play an indispensable role in maintaining immunological tolerance and body safety.',
      maxScore: 2.0,
      requiredKeywords: [
        'suppressor t-cells',
        'lymphokines',
        'inhibit',
        'plasma b-cells',
        'homeostasis',
      ],
      partialCreditKeywords: ['stop', 'apoptosis'],
      modelAnswer:
        'Because Suppressor T-cells (TS) secrete chemical substances called lymphokines upon eradication of the pathogen. Lymphokines inhibit plasma B-cells from producing more antibodies and cause apoptosis of excess helper and cytotoxic T-cells, thus preventing autoimmune damage and returning the immune system to resting homeostasis.',
      remediationNotes: 'Mention lymphokines and the inhibition of plasma B-cells.',
    },
  },

  genetic_engineering_recombinant: {
    topicId: 'genetic_engineering_recombinant',
    topicTitle: 'Genetic Engineering & Recombinant DNA',
    unit: 'UNIT_2',
    chapterTitle: 'Chapter 2: Nucleic Acids & Genetic Engineering',
    eli5Analogy:
      'Imagine DNA as a set of encyclopedia volumes. Restriction enzymes are microscopic molecular scissors that recognize exact symmetrical sentences (palindromes) and make clean angled cuts, leaving sticky tape (single-stranded sticky ends). A cloning vector (like a bacterial plasmid ring) is cut with the identical scissors so the human gene tape slots in seamlessly. DNA ligase acts as molecular glue. When placed inside multiplying bacteria, the cell turns into a biological factory printing millions of gene copies or producing pure human insulin!',
    arabicEli5:
      'تخيل أن الـ DNA كتاب مكتوب بحروف دقيقة. إنزيمات القصر البكتيرية هي مقصات جزيئية ذكية بتتعرف على جمل متناظرة تقرأ بنفس الطريقة من اليمين للشمال (مواقع التعرف)، وتقص بطريقة مائلة تسيب أطراف لاصقة مفردة. لما نقص البلازميد البكتيري بنفس المقص، جين الإنسولين البشري يركب مع البلازميد بتكامل تام، ويلحمهم إنزيم الربط زي الصمغ بالظبط. ولما البكتيريا تتكاثر، بتتحول لمصنع حيوي ينتج ملايين النسخ من الجين أو يصنع إنسولين بشري نقي!',
    textbookPageReference: 'Ministry Textbook Unit 2, Chapter 2, pp. 145–168',
    deepTechnicalBreakdown: `### 1. Bacterial Defense & Restriction Endonucleases
- **Natural Role**: Discovered in bacterial strains that resist bacteriophage viral infections. Over **250 distinct restriction endonucleases** have been isolated from various bacteria.
- **Modification Enzymes (إنزيمات المعدلة)**: The host bacterium protects its own genomic DNA from being cleaved by synthesizing modification enzymes that add **methyl groups ($-\\text{CH}_3$)** to specific nucleotides within the recognition sites on its own DNA.
- **Recognition Sites (مواقع التعرف)**:
  - Specific short sequences of **4 to 7 base pairs**.
  - **Palindromic Symmetry**: The sequence reads identically on both complementary strands when read in the $5' \\to 3'$ direction.
  - *Example*:
    \`\`\`
    5'- G | A A T T C -3'
    3'- C T T A A | G -5'
    \`\`\`
- **Staggered Cleavage & Sticky Ends**: Most restriction enzymes cut the sugar-phosphate backbones a few base pairs apart, producing single-stranded protruding overhangs called **Sticky Ends (أطراف لاصقة)** that can readily base-pair with any DNA fragment cleaved by the **same enzyme**.

### 2. Recombinant DNA Technology (DNA معاد الاتحاد)
- **Definition**: Joining DNA fragments from two different biological sources (e.g., human gene + bacterial plasmid vector) into a hybrid molecule.
- **Cloning Vectors (Plasmids & Phages)**:
  - **Plasmids**: Small, circular, double-stranded extra-chromosomal DNA molecules naturally found in bacteria and yeast.
  - The human gene of interest and the circular plasmid are digested with the **exact same restriction enzyme**.
  - The complementary sticky ends anneal by hydrogen bonding.
  - **DNA Ligase (إنزيم الربط)** covalently seals the phosphodiester bonds of the sugar-phosphate backbone.
- **Transformation & Bacterial Cloning**:
  - The recombinant plasmid is introduced into host *E. coli* cells treated to become permeable (calcium chloride/heat shock).
  - As the host bacterium replicates by binary fission every 20 minutes, the recombinant plasmid multiplies exponentially, producing millions of exact gene copies.

### 3. cDNA Synthesis via Reverse Transcriptase (إنزيم النسخ العكسي)
- **Biological Origin**: Retroviruses whose genetic material is **RNA** (e.g., HIV, avian myeloblastosis, influenza virus).
- **Why cDNA?**: Eukaryotic genomic DNA contains non-coding intervening sequences (**introns**). Bacteria lack RNA splicing machinery and cannot process pre-mRNA.
- **Process**:
  1. Mature spliced **mRNA** is isolated from specialized differentiated cells where the target gene is highly expressed (e.g., pancreatic beta-cells for **Insulin**, or reticulocytes for **Hemoglobin**).
  2. **Reverse Transcriptase** uses the single-stranded mRNA template (primed by an oligo-dT primer binding the $3'$ poly-A tail) to synthesize a complementary single-stranded DNA (**cDNA**).
  3. The mRNA template is degraded, and **DNA Polymerase** synthesizes the complementary second DNA strand, yielding a functional double-stranded gene consisting purely of expressed coding exons.

### 4. Polymerase Chain Reaction (PCR) & Automated Thermocycling
- **Purpose**: Rapid, automated in vitro amplification of specific DNA fragments without living bacterial hosts.
- **Thermal Cycler**: Instrument executing cyclical temperature changes:
  - **Denaturation ($94\\text{--}95^\\circ\\text{C}$)**: Breaks hydrogen bonds to separate DNA strands.
  - **Annealing ($50\\text{--}60^\\circ\\text{C}$)**: Specific synthetic oligonucleotide primers bind complementary target sequences.
  - **Extension ($72^\\circ\\text{C}$)**: Synthesis of new complementary strands.
- **Taq Polymerase Enzyme**: Isolated from the thermophilic bacterium *Thermus aquaticus* inhabiting hot springs; exceptionally thermostable and active at temperatures up to $95^\\circ\\text{C}$.
- **Yield**: Exponential replication according to $2^n$ (after 30 cycles, $>10^9$ copies are generated in a few hours).

### 5. DNA Hybridization (تهجين الحمض النووي)
- **Principle**: Heating DNA from two different species to $100^\\circ\\text{C}$ separates all double helices into single strands.
- **Renaturation**: When slowly cooled, complementary single strands from species A and species B collide and form **hybrid double-stranded DNA (DNA هجين)**.
- **Evolutionary Phylogenetic Index**:
  - The greater the number of shared homologous genes between two species, the more extensive the complementary hydrogen bonding in the hybrid helix.
  - The degree of evolutionary relatedness is quantitatively determined by measuring the **Melting Temperature ($T_m$)** required to re-separate the hybrid duplex. High $T_m$ = high evolutionary closeness.
- **Diagnostic Medical Application**: Detection of viral DNA/RNA (e.g. Hepatitis C, HIV) or latent disease alleles using radioactively or fluorescently labeled complementary DNA probes.

### 6. Applications & Human Genome Project
- **Medical Production**: Industrial synthesis of pure **Human Insulin** (commercialized in 1979, ending porcine/bovine allergic reactions) and **Human Interferon** (antiviral protein for viral infections and cancer therapy).
- **Agriculture**: Transfer of nitrogen-fixing bacterial genes into cereal crops; engineering pest-resistant crops (e.g. Bt endotoxin gene).
- **Human Genome Project (HGP)**: Mapping and sequencing all human genes (~20,000–25,000 genes) across the 23 chromosome pairs, mapping disease loci and enabling DNA fingerprinting in forensics.`,
    keyTerms: [
      {
        term: 'Restriction Endonuclease',
        definition:
          'Bacterial enzyme that recognizes specific palindromic DNA sequences and cleaves the sugar-phosphate backbone, producing sticky or blunt ends.',
        arabicTerm: 'إنزيم القصر / القطع',
      },
      {
        term: 'Recognition Site',
        definition:
          "A specific sequence of 4 to 7 base pairs on double-stranded DNA that reads identically in the 5' to 3' direction on both strands.",
        arabicTerm: 'موقع التعرف',
      },
      {
        term: 'Sticky Ends',
        definition:
          'Single-stranded protruding nucleotide overhangs generated by staggered cleavage of restriction enzymes that can pair with complementary fragments.',
        arabicTerm: 'الأطراف اللاصقة',
      },
      {
        term: 'Reverse Transcriptase',
        definition:
          'Viral enzyme isolated from retroviruses that synthesizes a single-stranded complementary DNA (cDNA) molecule from an mRNA template.',
        arabicTerm: 'إنزيم النسخ العكسي',
      },
      {
        term: 'Plasmid',
        definition:
          'Small, circular, double-stranded extrachromosomal DNA molecule found in bacteria and yeast, widely utilized as a gene cloning vector.',
        arabicTerm: 'البلازميد',
      },
      {
        term: 'Taq Polymerase',
        definition:
          'Heat-stable DNA polymerase enzyme extracted from thermophilic bacteria (Thermus aquaticus) that synthesizes DNA during PCR at high temperatures.',
        arabicTerm: 'إنزيم تاك بوليميريز',
      },
      {
        term: 'DNA Hybridization',
        definition:
          'The molecular process of heating and re-annealing single strands of DNA from two different origins to assess evolutionary relatedness or detect target genes.',
        arabicTerm: 'تهجين الحمض النووي',
      },
      {
        term: 'DNA Ligase',
        definition:
          'Enzyme that catalyzes the formation of covalent phosphodiester bonds between adjacent nucleotides to join DNA fragments permanently.',
        arabicTerm: 'إنزيم الربط',
      },
    ],
    formulas: [
      {
        name: 'Palindromic Recognition Symmetry Rule',
        formulaLatex: "5'-\\text{Sequence}-3' \\equiv 3'-\\text{Complementary Sequence}-5'",
        description:
          "Recognition sequences for restriction endonucleases must read identically from the 5' end to the 3' end on both complementary antiparallel strands.",
        variables: 'Length of recognition sequence = 4 to 7 base pairs',
        sampleCalculation:
          "EcoRI site: Top strand 5'-GAATTC-3' matches bottom strand 3'-CTTAAG-5' (which read 5'->3' is also GAATTC).",
      },
      {
        name: 'PCR Exponential Amplification Formula',
        formulaLatex: 'N = N_0 \\times 2^n',
        description:
          'Calculates the total number of synthesized DNA duplexes after n thermocycling rounds in a PCR thermal cycler.',
        variables:
          'N = Final DNA copy count; N0 = Initial target molecules; n = Number of PCR cycles',
        sampleCalculation:
          'Starting with N0 = 10 molecules after n = 20 cycles: N = 10 * 2^20 = 10 * 1,048,576 = 10,485,760 copies.',
      },
      {
        name: 'Hybrid Duplex Melting Temperature vs Homology',
        formulaLatex: 'T_m \\propto \\text{Degree of Complementary Base Pairing (Homology)}',
        description:
          'The thermal energy required to denature hybrid DNA duplexes is directly proportional to the evolutionary relatedness and base compatibility between two species.',
        variables: 'Tm = Melting temperature (°C); Higher Tm indicates closer evolutionary kinship',
        sampleCalculation:
          'Human-Chimpanzee hybrid DNA separates at a significantly higher Tm than Human-Frog hybrid DNA.',
      },
    ],
    comparisonTables: [
      {
        title: 'Bacterial Restriction Endonucleases vs Modification Enzymes',
        columnA: 'Restriction Endonucleases (إنزيمات القصر)',
        columnB: 'Modification Enzymes (الإنزيمات المعدلة)',
        rows: [
          {
            criteria: 'Biological Function',
            valueA: 'Cleaves and destroys invading viral (bacteriophage) foreign DNA',
            valueB: 'Protects the host bacterial genome from self-destruction',
          },
          {
            criteria: 'Biochemical Action',
            valueA: 'Hydrolyzes phosphodiester bonds at specific recognition sequences',
            valueB: 'Adds methyl groups (-CH3) to specific nucleotides within recognition sites',
          },
          {
            criteria: 'Substrate Targeted',
            valueA: 'Unmethylated foreign DNA lacking protective methyl tags',
            valueB: 'Host bacterial genomic DNA at identical recognition sequences',
          },
          {
            criteria: 'End Result',
            valueA: 'Viral DNA fragmented into harmless, non-functional pieces',
            valueB: 'Bacterial DNA becomes impervious to its own restriction enzymes',
          },
        ],
      },
      {
        title: 'In Vivo Cloning (Plasmids) vs In Vitro Amplification (PCR)',
        columnA: 'Biological Cloning via Plasmids (داخل الخلايا)',
        columnB: 'PCR Amplification in Thermal Cycler (في المختبر)',
        rows: [
          {
            criteria: 'Operating System',
            valueA: 'Living host cells (E. coli or yeast cultures)',
            valueB: 'Automated cell-free thermal cycler machine',
          },
          {
            criteria: 'Enzymes Required',
            valueA: 'Restriction endonucleases, DNA ligase, host DNA polymerase',
            valueB: 'Thermostable Taq Polymerase enzyme only',
          },
          {
            criteria: 'Time Requirement',
            valueA: 'Days to weeks (bacterial growth, plating, colony selection)',
            valueB: 'A few hours (automated 25-35 cycles)',
          },
          {
            criteria: 'Product Capacity',
            valueA: 'Can produce both cloned DNA and active translated proteins (e.g. Insulin)',
            valueB: 'Produces pure amplified DNA fragments only (no protein translation)',
          },
        ],
      },
      {
        title: 'Genomic DNA vs Complementary DNA (cDNA) for Human Genes',
        columnA: 'Genomic DNA (الحمض النووي الجينومي)',
        columnB: 'Complementary DNA / cDNA (الحمض النووي التكاملي)',
        rows: [
          {
            criteria: 'Source Material',
            valueA: 'Extracted directly from cell nucleus chromosomes',
            valueB: 'Synthesized from mature cytoplasmic mRNA via Reverse Transcriptase',
          },
          {
            criteria: 'Intron Content',
            valueA: 'Contains non-coding intervening sequences (introns) between exons',
            valueB: 'Contains continuous coding sequences (exons only, zero introns)',
          },
          {
            criteria: 'Bacterial Expression',
            valueA: 'Cannot be expressed in bacteria (bacteria lack splicing machinery)',
            valueB: 'Successfully transcribed and translated into human proteins in bacteria',
          },
          {
            criteria: 'Tissue Specificity',
            valueA: 'Identical in every nucleated cell of the body',
            valueB: 'Specific to the tissue where target gene is actively expressed',
          },
        ],
      },
    ],
    bloomProgression: [
      {
        levelName: 'Remembering',
        levelNumber: 1,
        objective:
          'Recall the definition, natural origin, and number of discovered bacterial restriction endonucleases.',
        masteryPrompt:
          'Where are restriction endonucleases naturally found, and what is their primary defensive function?',
      },
      {
        levelName: 'Understanding',
        levelNumber: 2,
        objective:
          'Explain why bacteria do not digest their own chromosomal DNA despite possessing restriction enzymes.',
        masteryPrompt:
          'How do modification enzymes protect bacterial host DNA from cleavage by endogenous restriction endonucleases?',
      },
      {
        levelName: 'Applying',
        levelNumber: 3,
        objective:
          'Determine whether a given 6-nucleotide sequence constitutes a valid palindromic recognition site.',
        masteryPrompt:
          "Evaluate whether the sequence 5'-GAATTC-3' is a palindromic restriction site by writing its complementary strand.",
      },
      {
        levelName: 'Analyzing',
        levelNumber: 4,
        objective:
          'Analyze why human insulin cannot be synthesized in bacteria using genomic DNA directly from the nucleus.',
        masteryPrompt:
          'Why is mature mRNA from pancreatic beta cells required instead of genomic nuclear DNA to clone the human insulin gene in E. coli?',
      },
      {
        levelName: 'Evaluating',
        levelNumber: 5,
        objective:
          'Evaluate the biochemical necessity of Taq polymerase in PCR over human DNA polymerase.',
        masteryPrompt:
          'What would occur in a PCR thermal cycler if human DNA polymerase was substituted for Taq polymerase at 94°C?',
      },
      {
        levelName: 'Creating',
        levelNumber: 6,
        objective:
          'Design a complete experimental protocol to clone and express a human clotting factor gene in bacterial cells.',
        masteryPrompt:
          'Formulate the step-by-step molecular pipeline to isolate, ligate, transform, and harvest a recombinant human therapeutic protein.',
      },
    ],
    asciiSchematic: `================= RECOMBINANT DNA CLONING PIPELINE =================
    Human Cell Nucleus                      Bacterial Cell
         |                                        |
  [mRNA with Exons]                        [Circular Plasmid]
         | (Reverse Transcriptase)                |
      [cDNA]                                      |
         |                                        |
         +-------------------+--------------------+
                             |
                   Same Restriction Enzyme
                             |
                             v
           Human Gene                     Opened Plasmid
       (with Sticky Ends)               (with Sticky Ends)
         \\-A-A-T-T-3'                     5'-A-A-T-T-\\
           \\________/                       \\________/
                \\                               /
                 \\========== DNA LIGASE =======/
                                 |
                                 v
                    [RECOMBINANT PLASMID VECTOR]
                                 |
                                 v
                  Transform into Host E. coli
                                 |
                      Bacterial Binary Fission
                                 |
                                 v
                 Millions of Cloned Gene Copies
                                 +
                     Pure Human Protein Output`,
    schematicCaption:
      'Recombinant DNA Construction: Digestion of target cDNA and plasmid vector with the identical restriction endonuclease yields complementary sticky ends annealed by DNA ligase.',
    flowcharts: [
      {
        stepNumber: 1,
        action: 'mRNA isolation from target tissue',
        detail: 'Isolate mature spliced mRNA from beta-cells of islets of Langerhans',
        triggerOrEnzyme: 'Cellular differentiation',
      },
      {
        stepNumber: 2,
        action: 'cDNA synthesis via Reverse Transcriptase',
        detail:
          'Viral reverse transcriptase synthesizes single-stranded cDNA; DNA polymerase forms double-stranded cDNA',
        triggerOrEnzyme: 'Reverse Transcriptase',
      },
      {
        stepNumber: 3,
        action: 'Vector & gene restriction cleavage',
        detail:
          'Cut both human cDNA and bacterial plasmid with the same restriction enzyme to generate compatible sticky ends',
        triggerOrEnzyme: 'Restriction Endonuclease',
      },
      {
        stepNumber: 4,
        action: 'Covalent ligation',
        detail:
          'DNA ligase forms covalent phosphodiester bonds between plasmid and human gene sticky ends',
        triggerOrEnzyme: 'DNA Ligase',
      },
      {
        stepNumber: 5,
        action: 'Bacterial transformation',
        detail: 'Introduce recombinant plasmid into permeable E. coli cells',
        triggerOrEnzyme: 'Heat shock / CaCl2',
      },
      {
        stepNumber: 6,
        action: 'Clonal expression & harvesting',
        detail:
          'Bacteria multiply by binary fission every 20 minutes, producing pure recombinant therapeutic protein',
        triggerOrEnzyme: 'Cellular division',
      },
    ],
    visualInspectionMarkers: [
      "Recognition site diagrams: Check symmetry: 5'->3' on strand 1 must match 5'->3' on strand 2.",
      'Sticky ends: Must have 4-5 single-stranded unbonded nucleotide overhangs complementary to each other.',
      'PCR cycle graphs: Three distinct temperature plateaus: Denaturation (94°C), Annealing (55°C), Extension (72°C).',
      'DNA Hybridization curves: Steeper melting curve and higher Tm indicates closer evolutionary kinship.',
    ],
    youtube3DQueries: [
      {
        query: 'Recombinant DNA technology 3D animation restriction enzymes plasmid',
        topicDescription:
          'Visualizes restriction enzyme cleavage, sticky ends annealing, and plasmid transformation into bacteria.',
        durationRecommendation: '4-6 mins',
      },
      {
        query: 'Polymerase Chain Reaction PCR 3D molecular animation Taq polymerase',
        topicDescription:
          'Demonstrates thermal cycler denaturation, primer annealing, and Taq polymerase extension cycles.',
        durationRecommendation: '3-5 mins',
      },
    ],
    questions: [
      {
        id: 'q_gene_1',
        type: 'SCIENTIFIC_TERM',
        question:
          'Write the scientific term: A viral enzyme isolated from retroviruses that uses a mature mRNA template to synthesize a complementary single strand of DNA.',
        officialAnswer: 'Reverse Transcriptase (إنزيم النسخ العكسي)',
        rationale:
          'Reverse transcriptase reverses the standard central dogma (RNA -> DNA), allowing intron-free cDNA synthesis from mature eukaryotic mRNA.',
        ministryExamTip: 'Must mention its viral origin (retroviruses) and mRNA substrate.',
      },
      {
        id: 'q_gene_2',
        type: 'GIVE_REASON',
        question:
          'Give reason: Bacterial restriction endonucleases do not digest the genomic DNA of the bacterium that produces them.',
        officialAnswer:
          'Because the bacterium produces modification enzymes that add protective methyl groups (-CH3) to specific nucleotides within the recognition sites on its own DNA, altering their structural profile and rendering them resistant to cleavage by its endogenous restriction endonucleases.',
        rationale:
          'Methylation at recognition sites masks the host DNA from restriction endonuclease catalytic cleavage.',
        ministryExamTip:
          'Keywords required: "Modification enzymes", "Methyl groups (-CH3)", "Recognition sites".',
      },
      {
        id: 'q_gene_3',
        type: 'WHAT_HAPPENS_IF',
        question:
          'What happens if: A mixture of DNA extracted from humans and chimpanzees is heated to 100°C and then allowed to cool slowly?',
        officialAnswer:
          'The hydrogen bonds break at 100°C separating the double helices into single strands. Upon slow cooling, complementary single strands from both species collide and re-anneal to form hybrid double-stranded DNA (DNA هجين). Because of high evolutionary relatedness and sequence homology, a large number of hybrid helices form with high melting temperature (Tm).',
        rationale:
          'DNA hybridization occurs between complementary sequences of different species; high homology forms stable hybrid duplexes.',
        ministryExamTip:
          'Mention both: 1) Strand separation at 100°C, and 2) Formation of hybrid DNA (DNA هجين) with high thermal stability.',
      },
      {
        id: 'q_gene_4',
        type: 'MCQ',
        question:
          'Which of the following enzymes is isolated from a thermophilic bacterium living in hot springs and is indispensable for automated Polymerase Chain Reaction (PCR)?',
        options: ['DNA Ligase', 'Taq Polymerase', 'Reverse Transcriptase', 'Deoxyribonuclease'],
        correctOptionIndex: 1,
        officialAnswer: 'Taq Polymerase',
        rationale:
          'Taq Polymerase is isolated from Thermus aquaticus and can withstand repeated denaturation cycles at temperatures exceeding 90°C without denaturation.',
        ministryExamTip:
          'Taq Polymerase operates in the thermal cycler; Reverse Transcriptase is for cDNA synthesis.',
      },
    ],
    examTraps: [
      {
        misconception:
          'Believing human genomic DNA from the nucleus can be directly inserted into bacteria to produce active insulin.',
        scientificTruth:
          'Human genomic DNA contains introns. Bacteria lack spliceosomes and cannot remove introns, resulting in non-functional junk protein. Therefore, cDNA synthesized from mature mRNA (exons only) must be used!',
        examTrapWarning:
          'If asked why mRNA from pancreatic cells is used rather than nuclear DNA: Bacteria cannot excise introns from pre-mRNA!',
        goldenRule:
          'Genomic DNA = Introns + Exons (fails in bacteria); cDNA = Exons only (succeeds in bacteria).',
      },
      {
        misconception:
          'Thinking restriction enzymes and modification enzymes cut at different sequences.',
        scientificTruth:
          'Both restriction endonucleases and modification enzymes recognize the EXACT same recognition sequence; modification enzymes methylate it, preventing restriction enzymes from cutting it.',
        examTrapWarning: 'Modification enzymes do NOT cut DNA; they only add methyl groups (-CH3)!',
        goldenRule: 'Restriction enzymes cut; Modification enzymes methylate.',
      },
      {
        misconception: 'Confusing the function of Reverse Transcriptase with RNA Polymerase.',
        scientificTruth:
          'RNA Polymerase transcribes DNA into RNA; Reverse Transcriptase synthesizes DNA from an RNA template.',
        examTrapWarning: 'Transcription = DNA -> RNA; Reverse Transcription = RNA -> cDNA!',
        goldenRule: 'Reverse Transcriptase = RNA to DNA.',
      },
    ],
    flashcards: [
      {
        id: 'fc_gene_1',
        topicId: 'genetic_engineering_recombinant',
        frontPrompt:
          'What is the natural biological function of restriction endonucleases in bacteria?',
        backAnswer:
          'To defend against bacteriophages by recognizing and cleaving invading foreign viral DNA.',
        category: 'Enzymology',
      },
      {
        id: 'fc_gene_2',
        topicId: 'genetic_engineering_recombinant',
        frontPrompt: 'What structural feature characterizes a restriction enzyme recognition site?',
        backAnswer:
          "A palindromic sequence of 4 to 7 base pairs that reads identically on both complementary strands in the 5' to 3' direction.",
        category: 'Molecular Biology',
      },
      {
        id: 'fc_gene_3',
        topicId: 'genetic_engineering_recombinant',
        frontPrompt:
          'Why must the target human gene and the plasmid vector be cut with the EXACT same restriction enzyme?',
        backAnswer:
          'To produce complementary sticky ends that can anneal together via complementary base pairing.',
        category: 'Genetic Engineering',
      },
      {
        id: 'fc_gene_4',
        topicId: 'genetic_engineering_recombinant',
        frontPrompt: 'Why is Taq polymerase used in PCR instead of normal human DNA polymerase?',
        backAnswer:
          'Because Taq polymerase is exceptionally heat-stable and does not denature at the 94°C-95°C denaturation temperatures.',
        category: 'Biotechnology',
      },
    ],
    examSimulationItem: {
      id: 'sim_recombinant_cloning',
      questionType: 'GIVE_REASON',
      prompt:
        'Give Reason: In genetic engineering, researchers synthesize the human insulin gene from mature mRNA rather than isolating it directly from the chromosomal DNA of the nucleus.',
      maxScore: 2.0,
      requiredKeywords: [
        'mrna',
        'introns',
        'exons',
        'bacteria',
        'splicing',
        'reverse transcriptase',
      ],
      partialCreditKeywords: ['mature', 'protein', 'non-coding'],
      modelAnswer:
        'Because chromosomal nuclear DNA contains non-coding intervening sequences (introns) interspersed between expressed coding sequences (exons). Since bacterial host cells lack RNA splicing machinery (spliceosomes) to excise introns, inserting genomic DNA directly would result in an abnormal, non-functional protein. Mature mRNA has already undergone post-transcriptional splicing, containing only exons; using Reverse Transcriptase allows researchers to synthesize a complementary DNA (cDNA) copy that bacteria can faithfully translate into active human insulin.',
      remediationNotes:
        'Full marks require mentioning: 1) Presence of introns in genomic DNA, 2) Inability of bacteria to splice introns, and 3) Role of Reverse Transcriptase in making intron-free cDNA.',
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
