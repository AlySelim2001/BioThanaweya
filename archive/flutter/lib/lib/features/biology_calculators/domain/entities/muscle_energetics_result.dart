/// Represents calculations for muscle sarcomere zones and bands under relaxed vs. contracted states.
class SarcomereCalculationResult {
  final int numberOfSarcomeres;
  final int zLinesCount;
  final int aBandsCount;
  final int completeIBandsCount;
  final int incompleteIBandsCount;
  final int hZonesRelaxedCount;
  final int hZonesContractedCount;
  final bool isFullyContracted;
  final List<String> explanationsAr;

  const SarcomereCalculationResult({
    required this.numberOfSarcomeres,
    required this.zLinesCount,
    required this.aBandsCount,
    required this.completeIBandsCount,
    required this.incompleteIBandsCount,
    required this.hZonesRelaxedCount,
    required this.hZonesContractedCount,
    required this.isFullyContracted,
    required this.explanationsAr,
  });

  Map<String, dynamic> toMap() => {
        'numberOfSarcomeres': numberOfSarcomeres,
        'zLinesCount': zLinesCount,
        'aBandsCount': aBandsCount,
        'completeIBandsCount': completeIBandsCount,
        'incompleteIBandsCount': incompleteIBandsCount,
        'hZonesRelaxedCount': hZonesRelaxedCount,
        'hZonesContractedCount': hZonesContractedCount,
        'isFullyContracted': isFullyContracted,
        'explanationsAr': explanationsAr,
      };

  factory SarcomereCalculationResult.fromMap(Map<String, dynamic> map) =>
      SarcomereCalculationResult(
        numberOfSarcomeres: (map['numberOfSarcomeres'] as num).toInt(),
        zLinesCount: (map['zLinesCount'] as num).toInt(),
        aBandsCount: (map['aBandsCount'] as num).toInt(),
        completeIBandsCount: (map['completeIBandsCount'] as num).toInt(),
        incompleteIBandsCount: (map['incompleteIBandsCount'] as num).toInt(),
        hZonesRelaxedCount: (map['hZonesRelaxedCount'] as num).toInt(),
        hZonesContractedCount: (map['hZonesContractedCount'] as num).toInt(),
        isFullyContracted: map['isFullyContracted'] as bool? ?? false,
        explanationsAr: List<String>.from(map['explanationsAr'] as List),
      );
}

/// Represents cellular respiration energy output and ATP net yield breakdown per glucose molecule.
class AtpEnergeticsResult {
  final int glucoseMolecules;
  final int glycolysisDirectAtp;
  final int glycolysisNadhCount;
  final int linkReactionNadhCount;
  final int krebsDirectAtp;
  final int krebsNadhCount;
  final int krebsFadh2Count;
  final int oxidativePhosphorylationAtp;
  final int totalNetAtp;
  final int anaerobicLacticAtp;
  final List<String> explanationsAr;

  const AtpEnergeticsResult({
    required this.glucoseMolecules,
    required this.glycolysisDirectAtp,
    required this.glycolysisNadhCount,
    required this.linkReactionNadhCount,
    required this.krebsDirectAtp,
    required this.krebsNadhCount,
    required this.krebsFadh2Count,
    required this.oxidativePhosphorylationAtp,
    required this.totalNetAtp,
    required this.anaerobicLacticAtp,
    required this.explanationsAr,
  });

  Map<String, dynamic> toMap() => {
        'glucoseMolecules': glucoseMolecules,
        'glycolysisDirectAtp': glycolysisDirectAtp,
        'glycolysisNadhCount': glycolysisNadhCount,
        'linkReactionNadhCount': linkReactionNadhCount,
        'krebsDirectAtp': krebsDirectAtp,
        'krebsNadhCount': krebsNadhCount,
        'krebsFadh2Count': krebsFadh2Count,
        'oxidativePhosphorylationAtp': oxidativePhosphorylationAtp,
        'totalNetAtp': totalNetAtp,
        'anaerobicLacticAtp': anaerobicLacticAtp,
        'explanationsAr': explanationsAr,
      };

  factory AtpEnergeticsResult.fromMap(Map<String, dynamic> map) =>
      AtpEnergeticsResult(
        glucoseMolecules: (map['glucoseMolecules'] as num).toInt(),
        glycolysisDirectAtp: (map['glycolysisDirectAtp'] as num).toInt(),
        glycolysisNadhCount: (map['glycolysisNadhCount'] as num).toInt(),
        linkReactionNadhCount: (map['linkReactionNadhCount'] as num).toInt(),
        krebsDirectAtp: (map['krebsDirectAtp'] as num).toInt(),
        krebsNadhCount: (map['krebsNadhCount'] as num).toInt(),
        krebsFadh2Count: (map['krebsFadh2Count'] as num).toInt(),
        oxidativePhosphorylationAtp:
            (map['oxidativePhosphorylationAtp'] as num).toInt(),
        totalNetAtp: (map['totalNetAtp'] as num).toInt(),
        anaerobicLacticAtp: (map['anaerobicLacticAtp'] as num).toInt(),
        explanationsAr: List<String>.from(map['explanationsAr'] as List),
      );
}
