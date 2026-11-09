import '../models/lab_diagram_dto.dart';
import '../models/hotspot_point_dto.dart';

/// قاعدة البيانات المحلية للرسومات والمسارات الحيوية المنهجية (Offline-First)
class CurriculumLabDatabase {
  static List<LabDiagramDto> getCoreCurriculumDiagrams() {
    return [
      // 1. دورة كريبس (Krebs Cycle)
      const LabDiagramDto(
        id: 'diag_krebs_cycle',
        title: 'دورة كريبس (Krebs Cycle)',
        chapter: 'التنفس الخلوي وإنتاج الطاقة',
        description: 'المسار الأيضي الحيوي داخل ماتركس الميتوكوندريا لتحويل أسيتيل مرافق الإنزيم أ إلى جزيئات حاملة للطاقة وإطلاق CO2.',
        assetImagePath: 'assets/images/lab/krebs_cycle.png',
        hotspots: [
          HotspotPointDto(
            id: 'hs_acetyl_co_a',
            title: 'أسيتيل مرافق الإنزيم أ (2C)',
            detailedExplanation: 'يدخل الدورة متفاعلاً مع حمض الأوكسالوستريك (4C) لتشكيل حمض الستريك سداسي الكربون.',
            ministryTrapNote: 'تنبيه وزاري: عدد ذرات الكربون هنا 2 ذرة، بينما الأوكسالوستريك 4 ذرات.',
            xPositionRatio: 0.15,
            yPositionRatio: 0.30,
          ),
          HotspotPointDto(
            id: 'hs_citric_acid',
            title: 'حمض الستريك (6C)',
            detailedExplanation: 'المركب الوسطي الأول الناتج عن اتحاد مجموعة الأسيتيل مع الأوكسالوستريك في بداية الدورة.',
            ministryTrapNote: 'أول مركب ثابت بـ 6 ذرات كربون في الدورة ويتحول تباعاً لحمض كيتوجلوتاريك (5C).',
            xPositionRatio: 0.40,
            yPositionRatio: 0.20,
          ),
          HotspotPointDto(
            id: 'hs_atp_nadh',
            title: 'إنتاج NADH و FADH2 و ATP',
            detailedExplanation: 'يتم إطلاق الإلكترونات والهيدروجين لاختزال النواقل وإنتاج جزيء طاقة مباشر ATP في كل دورة.',
            ministryTrapNote: 'لكل جزيء جلوكوز تحدث دورة كريبس مرتين (لكل جزيء أسيتيل قادم من انشطار الجلوكوز).',
            xPositionRatio: 0.75,
            yPositionRatio: 0.65,
          ),
        ],
      ),

      // 2. شوكة التضاعف الجيني (DNA Replication Fork)
      const LabDiagramDto(
        id: 'diag_dna_replication',
        title: 'شوكة التضاعف الجيني (DNA Replication Fork)',
        chapter: 'البيولوجيا الجزيئية (DNA)',
        description: 'آلية تفكك شريطي الـ DNA وبناء الأشرطة الجديدة بواسطة إنزيمات اللولب والبلمرة والربط.',
        assetImagePath: 'assets/images/lab/dna_replication.png',
        hotspots: [
          HotspotPointDto(
            id: 'hs_helicase',
            title: 'إنزيم اللولب (Helicase)',
            detailedExplanation: 'يقوم بكسر الروابط الهيدروجينية بين القواعد النيتروجينية المزدوجة لفتح الشريطين وتكوين الشوكة.',
            ministryTrapNote: 'يعمل في اتجاه واحد ويتطلب استهلاك طاقة ATP لفك اللولب.',
            xPositionRatio: 0.30,
            yPositionRatio: 0.50,
          ),
          HotspotPointDto(
            id: 'hs_dna_polymerase',
            title: 'إنزيم بلمرة DNA (DNA Polymerase)',
            detailedExplanation: 'يبني الشريط الجديد في الاتجاه من 5\' إلى 3\' حصراً بإضافة نيوكليوتيدات متوافقة مع القالب.',
            ministryTrapNote: 'لا يمكنه البدء من الصفر بل يتطلب قطعة بادئة (RNA Primer)، ويبني الشريط المتأخر كقطع أوكازاكي.',
            xPositionRatio: 0.65,
            yPositionRatio: 0.45,
          ),
          HotspotPointDto(
            id: 'hs_okazaki_fragments',
            title: 'قطع أوكازاكي وإنزيم الربط (Ligase)',
            detailedExplanation: 'قطع قصيرة تتكون على الشريط المتأخر (Lagging Strand) ويقوم إنزيم الربط بوصلها ببعضها.',
            ministryTrapNote: 'يعمل إنزيم الربط على ربط هيكل سكر-فوسفات بروابط تساهمية قوية.',
            xPositionRatio: 0.82,
            yPositionRatio: 0.70,
          ),
        ],
      ),

      // 3. التشريح العصبي والتشابك العصبي (Synapse Transmission)
      const LabDiagramDto(
        id: 'diag_synapse_transmission',
        title: 'التشابك العصبي وانتقال السيال (Chemical Synapse)',
        chapter: 'التنسيق العصبي والعضلي',
        description: 'آلية مرور السيال العصبي عبر الشق التشابكي بواسطة الناقل الكيميائي (الأسيتيل كولين) وتأثير أيونات الكالسيوم.',
        assetImagePath: 'assets/images/lab/synapse.png',
        hotspots: [
          HotspotPointDto(
            id: 'hs_calcium_channels',
            title: 'مضخات وقنوات الكالسيوم (Ca²⁺ Channels)',
            detailedExplanation: 'تنفتح عند وصول جهد الفعل مسببة تدفق أيونات الكالسيوم إلى داخل زر التشابك العصبي.',
            ministryTrapNote: 'بدون أيونات الكالسيوم لا يمكن تحرير الأسيتيل كولين حتى مع وصول السيال العصبي.',
            xPositionRatio: 0.25,
            yPositionRatio: 0.35,
          ),
          HotspotPointDto(
            id: 'hs_synaptic_vesicles',
            title: 'حويصلات التشابك (Synaptic Vesicles)',
            detailedExplanation: 'تحتوي على الأسيتيل كولين وتنفجر عند التحامها بغشاء الخلية قبل التشابكية تحت تأثير Ca²⁺.',
            ministryTrapNote: 'يفرز الناقل بالانفجار الخلوي إلى الشق التشابكي الذي يبلغ عرضه 20-30 نانومتر.',
            xPositionRatio: 0.50,
            yPositionRatio: 0.40,
          ),
          HotspotPointDto(
            id: 'hs_cholinesterase',
            title: 'إنزيم الكولين إستريز ومستقبلات ما بعد التشابك',
            detailedExplanation: 'يرتبط الأسيتيل كولين بمستقبلات الغشاء بعد التشابكي لفتح قنوات Na⁺، ثم يحطمه الكولين إستريز لإعادة الاستقطاب.',
            ministryTrapNote: 'غياب الكولين إستريز يسبب استمرار إثارة الغشاء وعدم العودة لحالة الراحة مما يسبب تشنجات.',
            xPositionRatio: 0.75,
            yPositionRatio: 0.60,
          ),
        ],
      ),

      // 4. المناعة والخلية البلعمية (Phagocytosis & Antigen Presentation)
      const LabDiagramDto(
        id: 'diag_macrophage_immunity',
        title: 'البلعمة وعرض المستضد (Macrophage & MHC-II)',
        chapter: 'المناعة في الكائنات الحية',
        description: 'ابتلاع الميكروب وتفكيكه بواسطة إنزيمات الليسوسوم وربطه مع بروتين التوافق النسيجي MHC لعرضه على السطح.',
        assetImagePath: 'assets/images/lab/macrophage.png',
        hotspots: [
          HotspotPointDto(
            id: 'hs_pseudopodia',
            title: 'الأقدام الكاذبة والابتلاع (Phagosome)',
            detailedExplanation: 'تحيط الخلية البلعمية الكبيرة بالميكروب الغريب بواسطة امتدادات سيتوبلازمية وتدخله في فجوة بلعمية.',
            ministryTrapNote: 'تعتبر البلعمية خط الدفاع الثاني والثالث وحلقة الوصل مع المناعة المكتسبة.',
            xPositionRatio: 0.20,
            yPositionRatio: 0.40,
          ),
          HotspotPointDto(
            id: 'hs_lysosome_fusion',
            title: 'اندماج الليسوسوم والإنزيمات الهاضمة',
            detailedExplanation: 'تفرز الليسوسومات إنزيمات محللة لتفتيت بروتينات الميكروب إلى ببتيدات صغيرة.',
            ministryTrapNote: 'التحليل هنا هضم إنزيمي للأنتيجينات دون إيذاء الخلية البلعمية نفسها.',
            xPositionRatio: 0.50,
            yPositionRatio: 0.50,
          ),
          HotspotPointDto(
            id: 'hs_mhc_antigen',
            title: 'معقد التوافق النسيجي والأنتيجين (MHC-Antigen Complex)',
            detailedExplanation: 'يرتبط الأنتيجين المفتت ببروتين MHC وينتقل للغشاء الخارجي لتتعرف عليه الخلايا التائية المساعدة (Th) عبر CD4.',
            ministryTrapNote: 'الخلايا Th لا تتعرف على الأنتيجين حراً بل مشروطاً بارتباطه ببروتين MHC.',
            xPositionRatio: 0.80,
            yPositionRatio: 0.35,
          ),
        ],
      ),
    ];
  }
}
