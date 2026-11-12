/// Represents a single cell entry in the Punnett Square matrix
class PunnettCell {
  final String gameteTop;
  final String gameteSide;
  final String genotype;
  final String phenotype;
  final bool isLethal;

  const PunnettCell({
    required this.gameteTop,
    required this.gameteSide,
    required this.genotype,
    required this.phenotype,
    this.isLethal = false,
  });

  Map<String, dynamic> toMap() => {
        'gameteTop': gameteTop,
        'gameteSide': gameteSide,
        'genotype': genotype,
        'phenotype': phenotype,
        'isLethal': isLethal,
      };

  factory PunnettCell.fromMap(Map<String, dynamic> map) => PunnettCell(
        gameteTop: map['gameteTop'] as String,
        gameteSide: map['gameteSide'] as String,
        genotype: map['genotype'] as String,
        phenotype: map['phenotype'] as String,
        isLethal: map['isLethal'] as bool? ?? false,
      );
}

/// Represents the comprehensive outcome of a genetic inheritance cross simulation.
class GeneticsCrossResult {
  final String crossType; // 'monohybrid', 'dihybrid', 'incomplete_dominance', 'lethal_genes'
  final String parent1Genotype;
  final String parent2Genotype;
  final List<String> gametesParent1;
  final List<String> gametesParent2;
  final List<List<PunnettCell>> matrix;
  final Map<String, int> genotypeFrequencies;
  final Map<String, int> phenotypeFrequencies;
  final String ratioSummary;
  final List<String> explanationsAr;
  final String ministryExamTrapNote;

  const GeneticsCrossResult({
    required this.crossType,
    required this.parent1Genotype,
    required this.parent2Genotype,
    required this.gametesParent1,
    required this.gametesParent2,
    required this.matrix,
    required this.genotypeFrequencies,
    required this.phenotypeFrequencies,
    required this.ratioSummary,
    required this.explanationsAr,
    required this.ministryExamTrapNote,
  });

  Map<String, dynamic> toMap() => {
        'crossType': crossType,
        'parent1Genotype': parent1Genotype,
        'parent2Genotype': parent2Genotype,
        'gametesParent1': gametesParent1,
        'gametesParent2': gametesParent2,
        'matrix': matrix.map((row) => row.map((c) => c.toMap()).toList()).toList(),
        'genotypeFrequencies': genotypeFrequencies,
        'phenotypeFrequencies': phenotypeFrequencies,
        'ratioSummary': ratioSummary,
        'explanationsAr': explanationsAr,
        'ministryExamTrapNote': ministryExamTrapNote,
      };

  factory GeneticsCrossResult.fromMap(Map<String, dynamic> map) =>
      GeneticsCrossResult(
        crossType: map['crossType'] as String,
        parent1Genotype: map['parent1Genotype'] as String,
        parent2Genotype: map['parent2Genotype'] as String,
        gametesParent1: List<String>.from(map['gametesParent1'] as List),
        gametesParent2: List<String>.from(map['gametesParent2'] as List),
        matrix: (map['matrix'] as List)
            .map((row) => (row as List)
                .map((c) => PunnettCell.fromMap(c as Map<String, dynamic>))
                .toList())
            .toList(),
        genotypeFrequencies: Map<String, int>.from(map['genotypeFrequencies'] as Map),
        phenotypeFrequencies: Map<String, int>.from(map['phenotypeFrequencies'] as Map),
        ratioSummary: map['ratioSummary'] as String,
        explanationsAr: List<String>.from(map['explanationsAr'] as List),
        ministryExamTrapNote: map['ministryExamTrapNote'] as String,
      );
}
