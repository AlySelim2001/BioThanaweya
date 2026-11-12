import '../entities/genetics_cross_result.dart';

/// Pure client-side genetics inheritance simulation engine for Egyptian Thanaweya Amma.
/// Supports Monohybrid, Dihybrid, Incomplete Dominance (انعدام السيادة), and Lethal Genes (الجينات المميتة).
class GeneticsInheritanceEngine {
  /// Solves a monohybrid cross between two single-gene parents (e.g., 'Aa' x 'Aa').
  static GeneticsCrossResult crossMonohybrid({
    required String parent1,
    required String parent2,
    bool isIncompleteDominance = false,
    bool isDominantLethal = false,
    bool isRecessiveLethal = false,
  }) {
    final p1 = parent1.trim();
    final p2 = parent2.trim();

    if (p1.length != 2 || p2.length != 2) {
      throw ArgumentError('التركيب الجيني للتهجين أحادي الهجين يجب أن يتكون من حرفين (مثل Aa).');
    }

    final gametes1 = [p1[0], p1[1]];
    final gametes2 = [p2[0], p2[1]];

    final matrix = <List<PunnettCell>>[];
    final genotypeFrequencies = <String, int>{};
    final phenotypeFrequencies = <String, int>{};

    for (final g2 in gametes2) {
      final row = <PunnettCell>[];
      for (final g1 in gametes1) {
        // Normalize genotype with uppercase first
        final sortedLetters = [g1, g2]..sort((a, b) {
            final isUpperA = a.toUpperCase() == a;
            final isUpperB = b.toUpperCase() == b;
            if (isUpperA != isUpperB) return isUpperA ? -1 : 1;
            return a.compareTo(b);
          });
        final genotype = sortedLetters.join();

        bool isLethal = false;
        String phenotype = '';

        if (isDominantLethal && genotype[0] == genotype[0].toUpperCase() && genotype[1] == genotype[0]) {
          isLethal = true;
          phenotype = 'مميت سائد (يموت في الرحم)';
        } else if (isRecessiveLethal && genotype[0] == genotype[0].toLowerCase() && genotype[1] == genotype[0]) {
          isLethal = true;
          phenotype = 'مميت متنحي (يموت بعد الولادة/الإنبات)';
        } else if (isIncompleteDominance) {
          if (genotype[0] == genotype[1]) {
            phenotype = genotype[0] == genotype[0].toUpperCase()
                ? 'صفة نقية أولى (أحمر)'
                : 'صفة نقية ثانية (أبيض)';
          } else {
            phenotype = 'صفة وسطية جديدة (قرنفلي)';
          }
        } else {
          // Standard Mendelian Complete Dominance
          final hasDominant = genotype.contains(RegExp(r'[A-Z]'));
          phenotype = hasDominant ? 'صفة سائدة' : 'صفة متنحية';
        }

        row.add(PunnettCell(
          gameteTop: g1,
          gameteSide: g2,
          genotype: genotype,
          phenotype: phenotype,
          isLethal: isLethal,
        ));

        genotypeFrequencies[genotype] = (genotypeFrequencies[genotype] ?? 0) + 1;
        if (!isLethal) {
          phenotypeFrequencies[phenotype] = (phenotypeFrequencies[phenotype] ?? 0) + 1;
        } else {
          phenotypeFrequencies['أفراد ميتة ($phenotype)'] =
              (phenotypeFrequencies['أفراد ميتة ($phenotype)'] ?? 0) + 1;
        }
      }
      matrix.add(row);
    }

    String ratioSummary = '';
    String examTrap = '';
    final explanations = <String>[];

    if (isDominantLethal || isRecessiveLethal) {
      ratioSummary = 'نسبة الأحياء 2 : 1 (يتحور النمط المندلي 3:1 إلى 2:1 بسبب موت ربع النسل 25%).';
      examTrap =
          'فخ وزاري: في الجينات المميتة، تبلغ نسبة الفاقد 25% من إجمالي النسل، وتكون نسبة الطرز المظهرية بين الأحياء 2 : 1 دائمًا عند تزاوج هجينين.';
      explanations.add('تزاوج هجينين ينتج 4 احتمالات: 1 سائد نقي (25%) + 2 هجين (50%) + 1 متنحي (25%).');
      explanations.add('موت الأفراد النقية المميتة يجعل نسبة الأحياء المتبقية 2 هجين : 1 نقي.');
    } else if (isIncompleteDominance) {
      ratioSummary = 'نسبة الطرز المظهرية 1 : 2 : 1 (تتطابق نسبة الطرز المظهرية مع الطرز الجينية تمامًا).';
      examTrap =
          'فخ وزاري: في حالة انعدام السيادة، كل طراز مظهري يدل مباشرة على طراز جيني واحد فقط، ولا توجد سيادة تامة.';
      explanations.add('لا يسود أي جين على الآخر بل يشتركان معًا في إظهار صفة وسطية جديدة.');
      explanations.add('النسبة الناتجة: 1 صفة أولى (25%) : 2 صفة وسطية (50%) : 1 صفة ثانية (25%).');
    } else {
      ratioSummary = 'نسبة مندلية كاملة: 3 سائد : 1 متنحي (طرز مظهرية)، 1 : 2 : 1 (طرز جينية).';
      examTrap =
          'فخ وزاري: الصفة السائدة قد تكون نقية (AA) بنسبة 25% أو هجينة (Aa) بنسبة 50%، بينما المتنحية دائمًا نقية (aa).';
      explanations.add('قانون مندل الأول (انعزال العوامل الوراثية): تنعزل الجينات أثناء تكوين الأمشاج.');
    }

    return GeneticsCrossResult(
      crossType: isDominantLethal
          ? 'lethal_genes_dominant'
          : isRecessiveLethal
              ? 'lethal_genes_recessive'
              : isIncompleteDominance
                  ? 'incomplete_dominance'
                  : 'monohybrid_mendelian',
      parent1Genotype: p1,
      parent2Genotype: p2,
      gametesParent1: gametes1,
      gametesParent2: gametes2,
      matrix: matrix,
      genotypeFrequencies: genotypeFrequencies,
      phenotypeFrequencies: phenotypeFrequencies,
      ratioSummary: ratioSummary,
      explanationsAr: explanations,
      ministryExamTrapNote: examTrap,
    );
  }

  /// Solves a dihybrid cross between two 2-gene parents (e.g., 'AaBb' x 'AaBb').
  static GeneticsCrossResult crossDihybrid({
    required String parent1,
    required String parent2,
  }) {
    final p1 = parent1.trim();
    final p2 = parent2.trim();

    if (p1.length != 4 || p2.length != 4) {
      throw ArgumentError('التركيب الجيني للتهجين ثنائي الهجين يجب أن يتكون من 4 أحرف (مثل AaBb).');
    }

    final gametes1 = _generateDihybridGametes(p1);
    final gametes2 = _generateDihybridGametes(p2);

    final matrix = <List<PunnettCell>>[];
    final genotypeFrequencies = <String, int>{};
    final phenotypeFrequencies = <String, int>{};

    for (final g2 in gametes2) {
      final row = <PunnettCell>[];
      for (final g1 in gametes1) {
        final pairA = [g1[0], g2[0]]..sort((a, b) {
            final isUpperA = a.toUpperCase() == a;
            final isUpperB = b.toUpperCase() == b;
            if (isUpperA != isUpperB) return isUpperA ? -1 : 1;
            return a.compareTo(b);
          });
        final pairB = [g1[1], g2[1]]..sort((a, b) {
            final isUpperA = a.toUpperCase() == a;
            final isUpperB = b.toUpperCase() == b;
            if (isUpperA != isUpperB) return isUpperA ? -1 : 1;
            return a.compareTo(b);
          });
        final genotype = '${pairA.join()}${pairB.join()}';

        final hasDomA = pairA.any((c) => c.toUpperCase() == c);
        final hasDomB = pairB.any((c) => c.toUpperCase() == c);

        String phenotype = '';
        if (hasDomA && hasDomB) {
          phenotype = 'سائد للصفتين معا (A_B_)';
        } else if (hasDomA && !hasDomB) {
          phenotype = 'سائد للأولى متنحي للثانية (A_bb)';
        } else if (!hasDomA && hasDomB) {
          phenotype = 'متنحي للأولى سائد للثانية (aaB_)';
        } else {
          phenotype = 'متنحي للصفتين معا (aabb)';
        }

        row.add(PunnettCell(
          gameteTop: g1,
          gameteSide: g2,
          genotype: genotype,
          phenotype: phenotype,
        ));

        genotypeFrequencies[genotype] = (genotypeFrequencies[genotype] ?? 0) + 1;
        phenotypeFrequencies[phenotype] = (phenotypeFrequencies[phenotype] ?? 0) + 1;
      }
      matrix.add(row);
    }

    final explanations = <String>[
      'قانون التوزيع الحر (قانون مندل الثاني): توزع جينات الصفتين على الأمشاج توزيعاً حراً ومستقلاً تماماً.',
      'عدد الأمشاج الناتجة من كل فرد هجين = 2^2 = 4 أنواع بنسب متساوية (25% لكل مشيج).',
      'إجمالي احتمالات النسل = 4 أمشاج مذكر × 4 أمشاج مؤنث = 16 فرداً في مربع بانيت.',
    ];

    const examTrap =
        'فخ وزاري: نسبة 9 : 3 : 3 : 1 تظهر فقط عند تزاوج فردين هجينين في كلا الصفتين (AaBb × AaBb). إذا كان أحد الأبوين نقياً لأي صفة تتغير النسبة تماماً.';

    return GeneticsCrossResult(
      crossType: 'dihybrid_mendelian',
      parent1Genotype: p1,
      parent2Genotype: p2,
      gametesParent1: gametes1,
      gametesParent2: gametes2,
      matrix: matrix,
      genotypeFrequencies: genotypeFrequencies,
      phenotypeFrequencies: phenotypeFrequencies,
      ratioSummary: 'نسبة التوزيع الحر لمندل: 9 : 3 : 3 : 1 (من أصل 16 احتمال).',
      explanationsAr: explanations,
      ministryExamTrapNote: examTrap,
    );
  }

  static List<String> _generateDihybridGametes(String genotype) {
    final a1 = genotype[0];
    final a2 = genotype[1];
    final b1 = genotype[2];
    final b2 = genotype[3];

    return [
      '$a1$b1',
      '$a1$b2',
      '$a2$b1',
      '$a2$b2',
    ];
  }
}
