import { ExamQuestion, OfficialExamPaper } from '../../types/exam';

export const INITIAL_EXAM_PAPERS: OfficialExamPaper[] = [
  {
    id: 'ministry_2024_session_1',
    titleArabic: 'امتحان الثانوية العامة الرسمي 2024 - الدور الأول',
    subtitleArabic: 'الورقة الامتحانية المعتمدة من وزارة التربية والتعليم المصرية (60 درجة)',
    year: 2024,
    session: 'first_session',
    sessionNameArabic: 'الدور الأول 2024',
    durationMinutes: 180,
    totalMarks: 60,
    questionCount: 46,
    difficultyTier: 'standard_ministry',
    descriptionArabic:
      'النموذج الوزاري الحقيقي المعتمد لامتحانات شهادة إتمام الدراسة الثانوية العامة، يحتوي على أسئلة الفهم والتحليل والتطبيقات الحسابية وفخاخ الربط بين الفصول.',
  },
  {
    id: 'ministry_2023_session_1',
    titleArabic: 'امتحان الثانوية العامة الرسمي 2023 - الدور الأول',
    subtitleArabic: 'النموذج القياسي المعتمد مع توزيع درجات 1 و 2 درجة',
    year: 2023,
    session: 'first_session',
    sessionNameArabic: 'الدور الأول 2023',
    durationMinutes: 180,
    totalMarks: 60,
    questionCount: 46,
    difficultyTier: 'standard_ministry',
    descriptionArabic:
      'امتحان رسمي يركز على تجارب البيولوجيا الجزيئية وميكانيكية الاستجابة المناعية وحسابات القطعة العضلية.',
  },
  {
    id: 'ministry_experimental_2024',
    titleArabic: 'النموذج الاسترشادي الوزاري المعتمد 2024',
    subtitleArabic: 'نموذج مركز تطوير المناهج والتقويم التربوي الفائق',
    year: 2024,
    session: 'experimental',
    sessionNameArabic: 'النموذج الاسترشادي',
    durationMinutes: 180,
    totalMarks: 60,
    questionCount: 46,
    difficultyTier: 'challenging',
    descriptionArabic:
      'أقوى نماذج الوزارة الاسترشادية المتضمنة لأدق المستويات المعرفية العليا، ورسوم بيانية لتركيز الهرمونات والأجسام المضادة.',
  },
];

export const OFFICIAL_EXAM_QUESTIONS: ExamQuestion[] = [
  // --- الدعامة والحركة ---
  {
    id: 'q_2024_01',
    paperId: 'ministry_2024_session_1',
    questionNumber: 1,
    chapterId: 'support_and_movement',
    chapterTitleArabic: 'الدعامة والحركة',
    topicTitleArabic: 'الانقباض العضلي وروابط الميوسين',
    marks: 1,
    textArabic:
      'أي مما يلي يلزم توافره بصورة مباشرة لفك الروابط المستعرضة الممتدة من خيوط الميوسين عن خيوط الأكتين أثناء انبساط العضلة الهيكلية؟',
    diagramSvgKey: 'sarcomere',
    diagramCaptionArabic: 'آلية الانزلاق وروابط الميوسين المستعرضة',
    options: [
      { id: 'A', label: 'أ', textArabic: 'جزيئات ATP فقط' },
      { id: 'B', label: 'ب', textArabic: 'أيونات الكالسيوم فقط' },
      { id: 'C', label: 'ج', textArabic: 'جزيئات ATP وأيونات الكالسيوم معاً' },
      { id: 'D', label: 'د', textArabic: 'إنزيم الكولين استريز فقط' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'فخ السؤال الشائع هو اختيار "ج" (ATP + كالسيوم) لأن تكوين الروابط واتصالها يحتاج كلاهما، لكن "انفصال وفك" الروابط المستعرضة عند الانبساط يتطلب طاقة ATP فقط دون الحاجة لأيونات الكالسيوم التي يتم ضخها بنشاط إلى الشبكة الساركوبلازمية.',
    scientificReferenceArabic:
      'كتاب الوزارة - الحركة في الإنسان: دور جزيئات ATP في انفصال الروابط المستعرضة عن خيوط الأكتين.',
  },
  {
    id: 'q_2024_02',
    paperId: 'ministry_2024_session_1',
    questionNumber: 2,
    chapterId: 'support_and_movement',
    chapterTitleArabic: 'الدعامة والحركة',
    topicTitleArabic: 'العمود الفقري والمفاصل',
    marks: 2,
    textArabic:
      'الفقرة العظمية المنصفة تماماً للعمود الفقري للإنسان تتمفصل مع الفقرة التي تليها بواسطة:',
    options: [
      { id: 'A', label: 'أ', textArabic: 'نتوءين مفصليين خلفيين وقرص غضروفي' },
      { id: 'B', label: 'ب', textArabic: 'نتوءين مفصليين أماميين فقط' },
      { id: 'C', label: 'ج', textArabic: 'قرص غضروفي بدون نتوءات' },
      { id: 'D', label: 'د', textArabic: 'نتوءين مستعرضين ونتوء شوكي' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'الفقرة المنصفة للعمود الفقري (الذي يضم 33 فقرة) هي الفقرة رقم 17 (فقرة ظهرية/صدرية). تتمفصل الفقرة مع التي تليها (رقم 18) من خلال زوج النتوءات المفصلية السفلية (الخلفية) لجسم الفقرة 17 مع النتوءات المفصلية العلوية (الأمامية) للفقرة 18، بالإضافة إلى القرص الغضروفي الفاصل بين جسمي الفقرتين.',
    scientificReferenceArabic:
      'كتاب الوزارة - الهيكل العظمي المحوري: تمفصل الفقرات العظمية في العمود الفقري.',
  },
  {
    id: 'q_2024_03',
    paperId: 'ministry_2024_session_1',
    questionNumber: 3,
    chapterId: 'support_and_movement',
    chapterTitleArabic: 'الدعامة والحركة',
    topicTitleArabic: 'الدعامة الفسيولوجية والتركيبية',
    marks: 1,
    textArabic:
      'عند غمر خلية نباتية ذات ضغط أسموزي 5% في محلول مائي مجهول التركيز، لوحظ انكماش الفجوة العصارية للخلية. ما التركيز المتوقع للمحلول؟',
    options: [
      { id: 'A', label: 'أ', textArabic: '8%' },
      { id: 'B', label: 'ب', textArabic: '5%' },
      { id: 'C', label: 'ج', textArabic: '2%' },
      { id: 'D', label: 'د', textArabic: 'ماء مقطر (0%)' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'انكماش الفجوة العصارية وفقدان الدعامة الفسيولوجية يعني خروج الماء من الخلية إلى الوسط الخارجي بالخاصية الأسموزية، وهذا لا يحدث إلا إذا كان تركيز المحلول الخارجي أعلى من تركيز الفجوة العصارية (أعلى من 5%، أي 8%).',
    scientificReferenceArabic: 'كتاب الوزارة - الدعامة الفسيولوجية والخاصية الأسموزية.',
  },

  // --- التنسيق الهرموني ---
  {
    id: 'q_2024_04',
    paperId: 'ministry_2024_session_1',
    questionNumber: 4,
    chapterId: 'hormonal_coordination',
    chapterTitleArabic: 'التنسيق الهرموني',
    topicTitleArabic: 'الغدة النخامية وهرمون ADH',
    marks: 1,
    textArabic:
      'شخص تناول كمية كبيرة من الوجبات المالحة دون شرب كميات كافية من الماء. أي التغيرات الهرمونية والفسيولوجية التالية تحدث فوراً في جسمه؟',
    diagramSvgKey: 'feedback_loop',
    diagramCaptionArabic: 'التنظيم الأسموزي والتغذية الراجعة لهرمون ADH',
    options: [
      { id: 'A', label: 'أ', textArabic: 'زيادة إفراز هرمون ADH وانخفاض أسموزية البول' },
      { id: 'B', label: 'ب', textArabic: 'زيادة إفراز هرمون ADH وارتفاع أسموزية البول' },
      { id: 'C', label: 'ج', textArabic: 'نقص إفراز هرمون الألدوستيرون ونقص حجم الدم' },
      { id: 'D', label: 'د', textArabic: 'نقص إفراز ADH وزيادة حجم البول' },
    ],
    correctOptionId: 'B',
    trapExplanationArabic:
      'زيادة الأملاح ترفع أسموزية الدم، فتحفز المستقبلات الأسموزية تحت المهاد لزيادة إفراز ADH من الفص الخلفي للنخامية. هذا يؤدي لإعادة امتصاص الماء من أنيببات الكلى، فيقل حجم البول ويزداد تركيز أملاحه (أي ترتفع أسموزية البول). الفخ هو الخلط بين أسموزية الدم وأسموزية البول.',
    scientificReferenceArabic:
      'كتاب الوزارة - الجزء العصبي للغدة النخامية والهرمون المضاد لإدرار البول.',
  },
  {
    id: 'q_2024_05',
    paperId: 'ministry_2024_session_1',
    questionNumber: 5,
    chapterId: 'hormonal_coordination',
    chapterTitleArabic: 'التنسيق الهرموني',
    topicTitleArabic: 'هرمونات تنظيم الكالسيوم',
    marks: 2,
    textArabic:
      'امرأة تعاني من سرعة الانفعال والغضب الشديد مع تشنجات عضلية مؤلمة دون وجود اضطراب في الغدة الدرقية. أي التحاليل الطبية التالية يتوقع أن يسجل انخفاضاً حاداً؟',
    options: [
      { id: 'A', label: 'أ', textArabic: 'هرمون الباراثورمون وكالسيوم الدم' },
      { id: 'B', label: 'ب', textArabic: 'هرمون الكالسيتونين وكالسيوم العظام' },
      { id: 'C', label: 'ج', textArabic: 'هرمون الثيروكسين وسكر الجلوكوز' },
      { id: 'D', label: 'د', textArabic: 'هرمون الأدرينالين وضغط الدم' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'التشنجات العضلية المؤلمة وسرعة الانفعال من الأعراض الكلاسيكية لنقص إفراز الغدد جارات الدرقية لهرمون الباراثورمون، مما يؤدي لهبوط نسبة الكالسيوم في الدم وزيادة استثارة الخلايا العصبية والعضلية.',
    scientificReferenceArabic: 'كتاب الوزارة - الغدد جارات الدرقية ووظيفة هرمون الباراثورمون.',
  },

  // --- التكاثر ---
  {
    id: 'q_2024_06',
    paperId: 'ministry_2024_session_1',
    questionNumber: 6,
    chapterId: 'reproduction',
    chapterTitleArabic: 'التكاثر في الكائنات الحية',
    topicTitleArabic: 'التوالد البكري ونحل العسل',
    marks: 1,
    textArabic:
      'أي الأفراد التالية تنتج دائماً من انقسام ميوزي يليه انقسام ميتوزي دون إخصاب في عالم الحشرات؟',
    options: [
      { id: 'A', label: 'أ', textArabic: 'ذكور نحل العسل' },
      { id: 'B', label: 'ب', textArabic: 'شغالات نحل العسل' },
      { id: 'C', label: 'ج', textArabic: 'إناث حشرة المن' },
      { id: 'D', label: 'د', textArabic: 'ملكات نحل العسل' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'ملكة نحل العسل (2n) تنتج بويضاتها (n) بانقسام ميوزي، وتنمو البويضة غير المخصبة بالتوالد البكري الطبيعي عبر انقسامات ميتوزية متتالية لتعطي ذكراً أحادي المجموعة الصبغية (n). أما الشغالات والملكات فتنتج من إخصاب، وإناث المن في التوالد البكري تنتج بويضاتها ميتوزياً (2n).',
    scientificReferenceArabic: 'كتاب الوزارة - صور التكاثر اللاجنسي: التوالد البكري في نحل العسل.',
  },
  {
    id: 'q_2024_07',
    paperId: 'ministry_2024_session_1',
    questionNumber: 7,
    chapterId: 'reproduction',
    chapterTitleArabic: 'التكاثر في الكائنات الحية',
    topicTitleArabic: 'التكاثر في النباتات الزهرية',
    marks: 2,
    textArabic:
      'ثمرة تفاح تحتوي على 5 بذور سليمة كاملة التكوين. ما الحد الأدنى لعدد الأنوية الزكرية التي شاركت في تكوين هذه البذور؟',
    diagramSvgKey: 'pollen_tube',
    diagramCaptionArabic: 'أنبوبة اللقاح والإخصاب المزدوج',
    options: [
      { id: 'A', label: 'أ', textArabic: '10 أنوية زكرية' },
      { id: 'B', label: 'ب', textArabic: '5 أنوية زكرية' },
      { id: 'C', label: 'ج', textArabic: '15 نواة' },
      { id: 'D', label: 'د', textArabic: '20 نواة' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'تكوين كل بذرة ناتجة عن إخصاب مزدوج يتطلب حبة لقاح تنبت وتحتوي على "نواتين زكريتين" (نواة تخصب البيضة لتكوين الزيجوت 2n، ونواة تندمج مع النواتين القطبيتين لتكوين الإندوسبيرم 3n). لذلك 5 بذور تتطلب 5 × 2 = 10 أنوية زكرية.',
    scientificReferenceArabic:
      'كتاب الوزارة - التكاثر في النباتات الزهرية: الإخصاب المزدوج وتكوين البذرة.',
  },
  {
    id: 'q_2024_08',
    paperId: 'ministry_2024_session_1',
    questionNumber: 8,
    chapterId: 'reproduction',
    chapterTitleArabic: 'التكاثر في الكائنات الحية',
    topicTitleArabic: 'الجهاز التناسلي الأنثوي ودورة الطمث',
    marks: 1,
    textArabic:
      'تصل بطانة الرحم إلى أقصى سمك وإمداد دموي لها تحت التأثير المباشر لهرمون البروجستيرون في اليوم:',
    options: [
      { id: 'A', label: 'أ', textArabic: 'الحادي والعشرين إلى الثالث والعشرين من بدء الطمث' },
      { id: 'B', label: 'ب', textArabic: 'الرابع عشر من بدء الطمث' },
      { id: 'C', label: 'ج', textArabic: 'الخامس من بدء الطمث' },
      { id: 'D', label: 'د', textArabic: 'الثامن والعشرين لحظة تفكك البطانة' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'التبويض يحدث في اليوم 14 ويتكون الجسم الأصفر، والذي يستمر في تصاعد إفراز البروجستيرون ليصل إلى قمته بعد التبويض بنحو أسبوع (الأيام 21-23)، حيث تكون بطانة الرحم في ذروة سمكها واستعدادها لغرس التوتية.',
    scientificReferenceArabic: 'كتاب الوزارة - دورة الطمث ومراحل نضج البويضة والتبويض والتبطن.',
  },

  // --- المناعة ---
  {
    id: 'q_2024_09',
    paperId: 'ministry_2024_session_1',
    questionNumber: 9,
    chapterId: 'immunity',
    chapterTitleArabic: 'المناعة في الكائنات الحية',
    topicTitleArabic: 'تركيب الجسم المضاد',
    marks: 2,
    textArabic:
      'جسم مضاد يحتوي على 4 سلاسل ببتيدية متصلة بروابط ثنائية الكبريتيد. كم عدد الروابط ثنائية الكبريتيد التي تربط السلسلتين الثقيلتين معاً، وبينهما وبين السلاسل الخفيفة إجمالاً؟',
    diagramSvgKey: 'antibody',
    diagramCaptionArabic: 'التركيب الكيميائي للجسم المضاد IgG',
    options: [
      {
        id: 'A',
        label: 'أ',
        textArabic: '4 روابط ثنائية الكبريتيد (2 بين الثقيلتين، و1 بين كل ثقيلة وخفيفة)',
      },
      { id: 'B', label: 'ب', textArabic: 'رابطتان فقط' },
      { id: 'C', label: 'ج', textArabic: '6 روابط ثنائية الكبريتيد' },
      { id: 'D', label: 'د', textArabic: '8 روابط' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'طبقاً لمنهج الثانوية العامة المصري: ترتبط السلسلتان الثقيلتان معاً برابطتين كبريتيديتين ثنائيتين، وترتبط كل سلسلة خفيفة بالسلسلة الثقيلة المجاورة برابطة كبريتيدية ثنائية واحدة. إجمالي الروابط = 2 + 1 + 1 = 4 روابط.',
    scientificReferenceArabic: 'كتاب الوزارة - المواد الكيميائية المساعدة والأجسام المضادة.',
  },
  {
    id: 'q_2024_10',
    paperId: 'ministry_2024_session_1',
    questionNumber: 10,
    chapterId: 'immunity',
    chapterTitleArabic: 'المناعة في الكائنات الحية',
    topicTitleArabic: 'المناعة الخلوية والخلايا الليمفاوية',
    marks: 1,
    textArabic:
      'المادة الكيميائية المناعية المسؤولة عن تثبيط وتوقف الاستجابة المناعية وتدمير بعض الخلايا البائية والتائية بعد القضاء على الميكروب هي:',
    options: [
      { id: 'A', label: 'أ', textArabic: 'الليمفوكينات المفرزة من الخلايا التائية المثبطة (Ts)' },
      { id: 'B', label: 'ب', textArabic: 'السيتوكينات المفرزة من التائية المساعدة (Th)' },
      { id: 'C', label: 'ج', textArabic: 'البيرفورين المفرز من الخلايا القاتلة (Tc)' },
      { id: 'D', label: 'د', textArabic: 'الكيموكينات المفرزة من البلعمية' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'الخلايا التائية الكابحة/المثبطة Ts تفرز بروتينات "الليمفوكينات" التي ترتبط بمستقبلات الخلايا البائية والتائية لتثبيط نشاطها وتحفيز موت الكثير منها لإنهاء المعركة المناعية.',
    scientificReferenceArabic:
      'كتاب الوزارة - مراحل الاستجابة المناعية: دور الخلايا Ts والليمفوكينات.',
  },

  // --- البيولوجيا الجزيئية DNA & RNA ---
  {
    id: 'q_2024_11',
    paperId: 'ministry_2024_session_1',
    questionNumber: 11,
    chapterId: 'molecular_biology_dna_rna',
    chapterTitleArabic: 'البيولوجيا الجزيئية (DNA & RNA)',
    topicTitleArabic: 'قواعد تشارجاف وروابط DNA',
    marks: 2,
    textArabic:
      'قطعة من جزيء DNA مزدوج اللولب تحتوي على 600 زوج من القواعد النيتروجينية، فإذا كانت نسبة الجوانين (G) تساوي 30%، فكم يكون عدد الروابط الهيدروجينية الكلية في هذه القطعة؟',
    diagramSvgKey: 'dna_replication',
    diagramCaptionArabic: 'الروابط الهيدروجينية المزدوجة والثلاثية في لولب DNA',
    options: [
      { id: 'A', label: 'أ', textArabic: '1560 رابطة هيدروجينية' },
      { id: 'B', label: 'ب', textArabic: '1200 رابطة' },
      { id: 'C', label: 'ج', textArabic: '1800 رابطة' },
      { id: 'D', label: 'د', textArabic: '1440 رابطة' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'الخطوات الرياضية بدقة:\n1. إجمالي القواعد = 600 زوج × 2 = 1200 نيوكليوتيدة.\n2. نسبة G = 30%، إذن عدد G = 1200 × 0.30 = 360 قاعدة، وبالتالي C = 360 قاعدة.\n3. نسبة A + T = 40%، إذن عدد A = 1200 × 0.20 = 240 قاعدة، و T = 240 قاعدة.\n4. الروابط الثلاثية بين C و G = 360 × 3 = 1080 رابطة.\n5. الروابط الثنائية بين A و T = 240 × 2 = 480 رابطة.\nالإجمالي = 1080 + 480 = 1560 رابطة هيدروجينية!',
    scientificReferenceArabic:
      'مسائل البيولوجيا الجزيئية المعتمدة - حساب الروابط الهيدروجينية وقواعد تشارجاف.',
  },
  {
    id: 'q_2024_12',
    paperId: 'ministry_2024_session_1',
    questionNumber: 12,
    chapterId: 'molecular_biology_dna_rna',
    chapterTitleArabic: 'البيولوجيا الجزيئية (DNA & RNA)',
    topicTitleArabic: 'تخليق البروتين وكودونات mRNA',
    marks: 1,
    textArabic:
      'إذا كان تتابع القواعد في مضاد الكودون لأحد جزيئات tRNA هو 5`-UAC-3`، فما تتابع النيوكليوتيدات المقابل على شريط DNA القالب غير الناسخ (الشريط غير المماثل)؟',
    options: [
      { id: 'A', label: 'أ', textArabic: '5`-TAC-3`' },
      { id: 'B', label: 'ب', textArabic: '3`-ATG-5`' },
      { id: 'C', label: 'ج', textArabic: '5`-ATG-3`' },
      { id: 'D', label: 'د', textArabic: '3`-AUG-5`' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'فخ الاتجاهات المشهور! مضاد الكودون هو 5`-UAC-3`، إذن كودون mRNA المقابل له (بالمعاكسة في الاتجاه والتكامل) هو 3`-AUG-5` أي يقرأ 5`-GUA-3`. ولكن mRNA تم نسخه من شريط DNA القالب 3`-TAC-5`. الشريط غير القالب (الشريط المقابل المتطابق مع mRNA فيما عدا U بدل T) يكون 5`-TAC-3`. الانتباه لاتجاه الأطراف 5` و 3` هو مفتاح المسألة.',
    scientificReferenceArabic: 'كتاب الوزارة - تخليق البروتين وتكامل الكودونات ومضادات الكودون.',
  },
  {
    id: 'q_2024_13',
    paperId: 'ministry_2024_session_1',
    questionNumber: 13,
    chapterId: 'support_and_movement',
    chapterTitleArabic: 'الدعامة والحركة',
    topicTitleArabic: 'الشد في المحاليق وحركة النبات',
    marks: 1,
    textArabic: 'إذا لم يجد محلاق نبات البازلاء دعامة صلبة يلتف حولها أثناء حركته الدورانية، فإنه:',
    options: [
      { id: 'A', label: 'أ', textArabic: 'يذبل ويموت ويسقط' },
      { id: 'B', label: 'ب', textArabic: 'يستقيم رأسياً لأعلى' },
      { id: 'C', label: 'ج', textArabic: 'تتغلظ أنسجته باللجنين ويزداد طوله' },
      { id: 'D', label: 'د', textArabic: 'يلتف حول ساقه الأصلية' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'المحلاق إذا لم يجد دعامة يلتف حولها فإنه يذبل ويموت ويسقط. التغلظ بالأنسجة الدعامية وتكوين اللجنين لا يحدث إلا بعد ملامسة الدعامة والالتفاف حولها وتكوين الحركة اللولبية.',
    scientificReferenceArabic: 'كتاب الوزارة - حركة الشد في محاليق النباتات المتسلقة.',
  },
  {
    id: 'q_2024_14',
    paperId: 'ministry_2024_session_1',
    questionNumber: 14,
    chapterId: 'immunity',
    chapterTitleArabic: 'المناعة في الكائنات الحية',
    topicTitleArabic: 'المناعة الفطرية في النبات',
    marks: 1,
    textArabic:
      'أي الوسائل المناعية التركيبية التالية في النبات توجد سلفاً وتتضاعف أو تتكون كاستجابة فورية للإصابة بميكروب؟',
    options: [
      { id: 'A', label: 'أ', textArabic: 'تغلظ الجدار الخلوي باللجنين وانتفاخ الجدر الخلوية' },
      { id: 'B', label: 'ب', textArabic: 'تكوين التيلوزات فقط' },
      { id: 'C', label: 'ج', textArabic: 'إفراز الصموغ فقط' },
      { id: 'D', label: 'د', textArabic: 'الحساسية المفرطة' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'الجدار الخلوي هو وسيلة مناعية تركيبية موجودة أصلاً (سلفاً)، وعند حدوث الإصابة تنتفخ جدر خلايا البشرة وتحت البشرة لتثبيط اختراق الميكروب، فتكون استجابة ناتجة عن الإصابة مكملة لما هو موجود سلفاً. بينما التيلوزات والصموغ تتكون فقط بعد الإصابة ولا توجد سلفاً.',
    scientificReferenceArabic:
      'كتاب الوزارة - المناعة التركيبية في النبات: الوسائل الموجودة سلفاً والتي تتكون كاستجابة للإصابة.',
  },
  {
    id: 'q_2024_15',
    paperId: 'ministry_2024_session_1',
    questionNumber: 15,
    chapterId: 'molecular_biology_dna_rna',
    chapterTitleArabic: 'البيولوجيا الجزيئية (DNA & RNA)',
    topicTitleArabic: 'إنزيمات تضاعف DNA',
    marks: 2,
    textArabic:
      'أي الإنزيمات التالية يعمل على كسر الروابط الهيدروجينية بين القواعد النيتروجينية لشريطي لولب DNA المزدوج أثناء بدء عملية التضاعف؟',
    options: [
      { id: 'A', label: 'أ', textArabic: 'إنزيم اللولب (Helicase)' },
      { id: 'B', label: 'ب', textArabic: 'إنزيم بلمرة DNA' },
      { id: 'C', label: 'ج', textArabic: 'إنزيم الربط (Ligase)' },
      { id: 'D', label: 'د', textArabic: 'إنزيم دي أوكسي ريبونيوكليز' },
    ],
    correctOptionId: 'A',
    trapExplanationArabic:
      'إنزيم اللولب يتحرك على امتداد جزيء DNA فاصلاً الشريطين عن بعضهما بكسر الروابط الهيدروجينية الضعيفة. الفخ هو اختيار إنزيم بلمرة DNA الذي يبني الأشرطة الجديدة، أو دي أوكسي ريبونيوكليز الذي يحلل الروابط التساهمية والهيدروجينية تحليلاً كاملاً وليس للتضاعف.',
    scientificReferenceArabic: 'كتاب الوزارة - إنزيمات تضاعف DNA: إنزيم اللولب والبلمرة والربط.',
  },
];
