import '../entities/dna_calculation_result.dart';

/// Pure client-side calculation engine for Egyptian Thanaweya Amma DNA & Molecular Biology.
/// Formulated strictly according to Chargaff's rules and Ministry of Education exam standards.
class DnaCalculatorEngine {
  /// Calculates DNA base percentages, helical turns, and total hydrogen bonds.
  /// [baseName] can be 'A', 'T', 'G', or 'C'.
  /// [percent] must be strictly between 0% and 50% (< 50%) for any single base in double-stranded DNA.
  /// [totalNucleotides] must be a positive integer representing the double-stranded segment.
  static DnaCalculationResult calculateFromBasePercent({
    required String baseName,
    required double percent,
    int totalNucleotides = 1000,
  }) {
    if (percent <= 0 || percent >= 50.0) {
      throw ArgumentError(
        'نسبة أي قاعدة منفردة في لولب DNA المزدوج يجب أن تكون أكبر من 0% وأقل تماماً من 50%.',
      );
    }
    if (totalNucleotides <= 0) {
      throw ArgumentError('إجمالي عدد النيوكليوتيدات يجب أن يكون رقماً موجباً أكبر من الصفر.');
    }

    final p = double.parse(percent.toStringAsFixed(2));
    double a = 0.0;
    double t = 0.0;
    double g = 0.0;
    double c = 0.0;

    final norm = baseName.toUpperCase().trim();
    if (norm == 'A' || norm == 'ADENINE' || norm == 'T' || norm == 'THYMINE') {
      a = p;
      t = p;
      final remaining = 100.0 - (a + t);
      g = double.parse((remaining / 2.0).toStringAsFixed(2));
      c = g;
    } else if (norm == 'G' || norm == 'GUANINE' || norm == 'C' || norm == 'CYTOSINE') {
      g = p;
      c = p;
      final remaining = 100.0 - (g + c);
      a = double.parse((remaining / 2.0).toStringAsFixed(2));
      t = a;
    } else {
      // Default balanced distribution if unknown input
      a = 25.0;
      t = 25.0;
      g = 25.0;
      c = 25.0;
    }

    final basePairs = totalNucleotides ~/ 2;
    // In DNA double helix: 10 base pairs (or 20 nucleotides) per complete helical turn
    final turns = double.parse((basePairs / 10.0).toStringAsFixed(1));

    final countA = (totalNucleotides * (a / 100.0)).round();
    final countT = countA;
    final countG = (totalNucleotides * (g / 100.0)).round();
    final countC = countG;

    // A=T pairs have 2 hydrogen bonds; G≡C pairs have 3 hydrogen bonds
    final totalHBonds = (countA * 2) + (countG * 3);

    final explanationsAr = <String>[
      'قاعدة شارجاف: في جزيء DNA مزدوج الشريط، تتساوى نسبة الأدينين مع الثايمين (%A = %T = ${a.toStringAsFixed(1)}%) ونسبة الجوانين مع السيتوزين (%G = %C = ${g.toStringAsFixed(1)}%).',
      'مجموع البيورينات (A + G) = 50.0% ومجموع البيريميدينات (T + C) = 50.0% دائمًا في اللولب المزدوج.',
      'أزواج القواعد النيتروجينية = إجمالي النيوكليوتيدات / 2 = $totalNucleotides / 2 = $basePairs زوج.',
      'عدد اللفات التامة = أزواج القواعد / 10 = $basePairs / 10 = ${turns.toStringAsFixed(1)} لفة كاملة (أو النيوكليوتيدات / 20).',
      'إجمالي الروابط الهيدروجينية = (2 × عدد A) + (3 × عدد G) = (2 × $countA) + (3 × $countG) = $totalHBonds رابطة هيدروجينية.',
    ];

    return DnaCalculationResult(
      baseName: norm,
      inputPercent: p,
      adeninePercent: a,
      thyminePercent: t,
      guaninePercent: g,
      cytosinePercent: c,
      purinesPercent: 50.0,
      pyrimidinesPercent: 50.0,
      totalNucleotides: totalNucleotides,
      basePairsCount: basePairs,
      completeTurnsCount: turns,
      adenineCount: countA,
      thymineCount: countT,
      guanineCount: countG,
      cytosineCount: countC,
      hydrogenBondsTotal: totalHBonds,
      explanationsAr: explanationsAr,
    );
  }
}
