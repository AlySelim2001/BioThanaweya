/// Represents the molecular calculation results for a double-stranded DNA segment
/// according to Chargaff's base-pairing rules and B-DNA helical geometry.
class DnaCalculationResult {
  /// Normalized input nitrogenous base identifier (e.g., 'A', 'T', 'G', or 'C').
  final String baseName;

  /// Input percentage (0% < percent < 50%) provided for [baseName].
  final double inputPercent;

  /// Percentage of Adenine (A) nucleotides in the double-stranded segment (%).
  final double adeninePercent;

  /// Percentage of Thymine (T) nucleotides in the double-stranded segment (%).
  final double thyminePercent;

  /// Percentage of Guanine (G) nucleotides in the double-stranded segment (%).
  final double guaninePercent;

  /// Percentage of Cytosine (C) nucleotides in the double-stranded segment (%).
  final double cytosinePercent;

  /// Combined percentage of purine bases (A + G), always 50.0% in double-stranded DNA.
  final double purinesPercent;

  /// Combined percentage of pyrimidine bases (T + C), always 50.0% in double-stranded DNA.
  final double pyrimidinesPercent;

  /// Total number of nucleotides across both DNA strands.
  final int totalNucleotides;

  /// Number of nitrogenous base pairs (half of [totalNucleotides]).
  final int basePairsCount;

  /// Number of complete helical turns (10 base pairs / 20 nucleotides per turn).
  final double completeTurnsCount;

  /// Calculated count of Adenine (A) nucleotides in the segment.
  final int adenineCount;

  /// Calculated count of Thymine (T) nucleotides in the segment.
  final int thymineCount;

  /// Calculated count of Guanine (G) nucleotides in the segment.
  final int guanineCount;

  /// Calculated count of Cytosine (C) nucleotides in the segment.
  final int cytosineCount;

  /// Total number of hydrogen bonds: 2 per A=T pair plus 3 per G≡C pair.
  final int hydrogenBondsTotal;

  /// Step-by-step Arabic derivation explanations for the calculation results.
  final List<String> explanationsAr;

  const DnaCalculationResult({
    required this.baseName,
    required this.inputPercent,
    required this.adeninePercent,
    required this.thyminePercent,
    required this.guaninePercent,
    required this.cytosinePercent,
    required this.purinesPercent,
    required this.pyrimidinesPercent,
    required this.totalNucleotides,
    required this.basePairsCount,
    required this.completeTurnsCount,
    required this.adenineCount,
    required this.thymineCount,
    required this.guanineCount,
    required this.cytosineCount,
    required this.hydrogenBondsTotal,
    required this.explanationsAr,
  });

  Map<String, dynamic> toMap() => {
        'baseName': baseName,
        'inputPercent': inputPercent,
        'adeninePercent': adeninePercent,
        'thyminePercent': thyminePercent,
        'guaninePercent': guaninePercent,
        'cytosinePercent': cytosinePercent,
        'purinesPercent': purinesPercent,
        'pyrimidinesPercent': pyrimidinesPercent,
        'totalNucleotides': totalNucleotides,
        'basePairsCount': basePairsCount,
        'completeTurnsCount': completeTurnsCount,
        'adenineCount': adenineCount,
        'thymineCount': thymineCount,
        'guanineCount': guanineCount,
        'cytosineCount': cytosineCount,
        'hydrogenBondsTotal': hydrogenBondsTotal,
        'explanationsAr': explanationsAr,
      };

  factory DnaCalculationResult.fromMap(Map<String, dynamic> map) =>
      DnaCalculationResult(
        baseName: map['baseName'] as String,
        inputPercent: (map['inputPercent'] as num).toDouble(),
        adeninePercent: (map['adeninePercent'] as num).toDouble(),
        thyminePercent: (map['thyminePercent'] as num).toDouble(),
        guaninePercent: (map['guaninePercent'] as num).toDouble(),
        cytosinePercent: (map['cytosinePercent'] as num).toDouble(),
        purinesPercent: (map['purinesPercent'] as num).toDouble(),
        pyrimidinesPercent: (map['pyrimidinesPercent'] as num).toDouble(),
        totalNucleotides: (map['totalNucleotides'] as num).toInt(),
        basePairsCount: (map['basePairsCount'] as num).toInt(),
        completeTurnsCount: (map['completeTurnsCount'] as num).toDouble(),
        adenineCount: (map['adenineCount'] as num).toInt(),
        thymineCount: (map['thymineCount'] as num).toInt(),
        guanineCount: (map['guanineCount'] as num).toInt(),
        cytosineCount: (map['cytosineCount'] as num).toInt(),
        hydrogenBondsTotal: (map['hydrogenBondsTotal'] as num).toInt(),
        explanationsAr: List<String>.from(map['explanationsAr'] as List),
      );
}
