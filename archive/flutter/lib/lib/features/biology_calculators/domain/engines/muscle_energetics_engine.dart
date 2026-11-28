import '../entities/muscle_energetics_result.dart';

/// Pure client-side calculations for muscle sarcomere zones and cellular respiration ATP energetics.
/// Formulated strictly according to Egyptian Ministry of Education curriculum (Unit 1 & Unit 2).
class MuscleEnergeticsEngine {
  /// Calculates sarcomere bands and zones from either sarcomere count or Z-line count.
  /// In muscle contraction:
  /// - A-bands remain constant (equal to number of sarcomeres).
  /// - Z-lines = Sarcomeres + 1.
  /// - Complete I-bands = Sarcomeres - 1 (in relaxed state between adjacent Z-lines).
  /// - Incomplete I-bands = 2 (at the two peripheral ends of the myofibril).
  /// - H-zones = Sarcomeres in relaxed state, but = 0 during full muscle contraction.
  static SarcomereCalculationResult calculateSarcomere({
    required int sarcomeres,
    bool isFullyContracted = false,
  }) {
    if (sarcomeres <= 0) {
      throw ArgumentError('عدد القطع العضلية (Sarcomeres) يجب أن يكون عدداً صحيحاً موجباً (≥ 1).');
    }

    final s = sarcomeres;
    final z = s + 1;
    final a = s;
    final completeI = s > 1 ? s - 1 : 0;
    const incompleteI = 2;
    final hRelaxed = s;
    final hContracted = 0;

    final explanations = <String>[
      'خطوط Z المنصفة للمناطق المضيئة = عدد القطع العضلية + 1 = $s + 1 = $z خط داكن.',
      'المناطق الداكنة A = عدد القطع العضلية = $s (ثابتة الطول دائماً في الانبساط والانقباض لأن خيوط الميوسين لا تقصر).',
      'المناطق المضيئة الكاملة I = عدد القطع العضلية - 1 = $completeI (الواقعة بين خطوط Z المتجاورة).',
      'المناطق المضيئة غير الكاملة = 2 (منطقتان على طرفي اللييفة العضلية دائماً).',
      isFullyContracted
          ? 'المناطق شبه المضيئة H أثناء الانقباض التام = 0 (تختفي تماماً نتيجة تداخل وتلامس خيوط الأكتين في المنتصف).'
          : 'المناطق شبه المضيئة H أثناء الانبساط = $hRelaxed (تساوي عدد القطع العضلية).',
    ];

    return SarcomereCalculationResult(
      numberOfSarcomeres: s,
      zLinesCount: z,
      aBandsCount: a,
      completeIBandsCount: completeI,
      incompleteIBandsCount: incompleteI,
      hZonesRelaxedCount: hRelaxed,
      hZonesContractedCount: hContracted,
      isFullyContracted: isFullyContracted,
      explanationsAr: explanations,
    );
  }

  /// Calculates net ATP yield from complete aerobic cellular respiration per glucose molecule.
  /// Standard Ministry model:
  /// - Glycolysis: 2 direct ATP + 2 NADH (yielding 6 ATP via ETC).
  /// - Link reaction: 2 NADH (yielding 6 ATP via ETC).
  /// - Krebs cycle (2 turns): 2 direct ATP + 6 NADH (18 ATP) + 2 FADH2 (4 ATP).
  /// - Total Net Aerobic Yield = 38 ATP per glucose molecule.
  /// - Anaerobic (Lactic fermentation) = 2 ATP only.
  static AtpEnergeticsResult calculateCellularAtp({int glucoseMolecules = 1}) {
    if (glucoseMolecules <= 0) {
      throw ArgumentError('عدد جزيئات الجلوكوز يجب أن يكون رقماً موجباً أكبر من الصفر.');
    }

    final g = glucoseMolecules;
    final directGlycolysis = 2 * g;
    final nadhGlycolysis = 2 * g;
    final nadhLink = 2 * g;
    final directKrebs = 2 * g;
    final nadhKrebs = 6 * g;
    final fadh2Krebs = 2 * g;

    final totalNadh = nadhGlycolysis + nadhLink + nadhKrebs; // 10 * g
    final totalFadh2 = fadh2Krebs; // 2 * g

    // In mitochondrial respiratory chain: 1 NADH -> 3 ATP; 1 FADH2 -> 2 ATP
    final atpFromNadh = totalNadh * 3;
    final atpFromFadh2 = totalFadh2 * 2;
    final oxidativeAtp = atpFromNadh + atpFromFadh2; // (30 + 4) * g = 34 * g
    final directTotal = directGlycolysis + directKrebs; // 4 * g
    final totalNet = directTotal + oxidativeAtp; // 38 * g

    final anaerobic = 2 * g;

    final explanations = <String>[
      'الانشطار الخلوي (في السيتوسول): ينتج 2 جزيء ATP مباشر + 2 جزيء NADH.',
      'أكسدة حمض البيروفيك (الخطوة الانتقالية): ينتج 2 جزيء NADH لكل جزيء جلوكوز.',
      'دورة كريبس (دورتان لكل جلوكوز): ينتج 2 جزيء ATP مباشر + 6 جزيئات NADH + 2 جزيء FADH₂.',
      'سلسلة نقل الإلكترون: كل جزيء NADH يمنح 3 ATP وكل FADH₂ يمنح 2 ATP.',
      'إجمالي صافي الطاقة الهوائية = $totalNet جزيء ATP (38 ATP لكل جلوكوز).',
      'في حالة التنفس اللاهوائي (التخمر الحمضي وإجهاد العضلات): ينتج 2 جزيء ATP فقط لكل جلوكوز.',
    ];

    return AtpEnergeticsResult(
      glucoseMolecules: g,
      glycolysisDirectAtp: directGlycolysis,
      glycolysisNadhCount: nadhGlycolysis,
      linkReactionNadhCount: nadhLink,
      krebsDirectAtp: directKrebs,
      krebsNadhCount: nadhKrebs,
      krebsFadh2Count: fadh2Krebs,
      oxidativePhosphorylationAtp: oxidativeAtp,
      totalNetAtp: totalNet,
      anaerobicLacticAtp: anaerobic,
      explanationsAr: explanations,
    );
  }
}
