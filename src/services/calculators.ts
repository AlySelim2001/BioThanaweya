export interface SarcomereResult {
  numberOfSarcomeres: number;
  zLinesCount: number;
  aBandsCount: number;
  completeIBandsCount: number;
  incompleteIBandsCount: number;
  hZonesRelaxedCount: number;
  hZonesContractedCount: number;
  explanations: string[];
}

export const SarcomereCalculator = {
  calculateFromSarcomeres(sarcomeres: number): SarcomereResult {
    if (sarcomeres <= 0) {
      throw new Error('Number of sarcomeres must be a positive integer (≥ 1).');
    }
    const s = Math.floor(sarcomeres);
    const z = s + 1;
    const aBands = s;
    const completeI = Math.max(0, s - 1);
    const incompleteI = 2;
    const hRelaxed = s;
    const hContracted = 0; // Complete contraction: actin slides and overlaps completely

    const explanations = [
      `Z-lines = Sarcomeres + 1 = ${s} + 1 = ${z} (خطوط Z المنصفة للمناطق المضيئة)`,
      `A-bands (Dark bands) = Sarcomeres = ${s} (المناطق الداكنة ثابتة الطول دائمًا ولا تقصر أبدًا لأن طول الميوسين ثابت)`,
      `Complete I-bands = Sarcomeres - 1 = ${completeI} (المناطق المضيئة الكاملة الواقعة بين خطين Z)`,
      `Incomplete I-bands = 2 (منطقتان مضيئتان غير كاملتين على طرفي اللييفة العضلية)`,
      `H-zones (Relaxed) = ${hRelaxed} (تساوي عدد القطع العضلية في حالة الانبساط)`,
      `H-zones (Full Contraction) = 0 (تختفي تمامًا في حالة الانقباض التام لتداخل خيوط الأكتين معًا)`,
    ];

    return {
      numberOfSarcomeres: s,
      zLinesCount: z,
      aBandsCount: aBands,
      completeIBandsCount: completeI,
      incompleteIBandsCount: incompleteI,
      hZonesRelaxedCount: hRelaxed,
      hZonesContractedCount: hContracted,
      explanations,
    };
  },

  calculateFromZLines(zLines: number): SarcomereResult {
    if (zLines < 2) {
      throw new Error('Number of Z-lines must be at least 2.');
    }
    return this.calculateFromSarcomeres(zLines - 1);
  },
};

export interface DnaCalculationResult {
  baseName: string;
  adeninePercent: number;
  thyminePercent: number;
  guaninePercent: number;
  cytosinePercent: number;
  purinesPercent: number;
  pyrimidinesPercent: number;
  sampleTotalNucleotides: number;
  basePairsCount: number;
  completeTurnsCount: number;
  hydrogenBondsTotal: number;
  explanations: string[];
}

export const DnaCalculator = {
  calculateFromBasePercent(
    baseName: string,
    percent: number,
    totalNucleotides = 1000
  ): DnaCalculationResult {
    if (percent <= 0 || percent >= 50) {
      throw new Error(
        'Percentage for any single base in double-stranded DNA must be strictly between 0% and 50% (< 50%).'
      );
    }
    if (totalNucleotides <= 0) {
      throw new Error('Total nucleotides must be greater than zero.');
    }

    const p = Math.round(percent * 100) / 100;
    let a = 0;
    let t = 0;
    let g = 0;
    let c = 0;

    const baseUpper = baseName.toUpperCase().trim();
    if (
      baseUpper === 'A' ||
      baseUpper === 'ADENINE' ||
      baseUpper === 'T' ||
      baseUpper === 'THYMINE'
    ) {
      a = p;
      t = p;
      const rem = 100 - (a + t);
      g = Math.round((rem / 2) * 100) / 100;
      c = g;
    } else if (
      baseUpper === 'G' ||
      baseUpper === 'GUANINE' ||
      baseUpper === 'C' ||
      baseUpper === 'CYTOSINE'
    ) {
      g = p;
      c = p;
      const rem = 100 - (g + c);
      a = Math.round((rem / 2) * 100) / 100;
      t = a;
    } else {
      a = 25;
      t = 25;
      g = 25;
      c = 25;
    }

    const basePairs = Math.floor(totalNucleotides / 2);
    // In DNA double helix, 10 base pairs (or 20 nucleotides) per complete helical turn
    const turns = Math.round((basePairs / 10) * 10) / 10;

    const countA = Math.round(totalNucleotides * (a / 100));
    const countG = Math.round(totalNucleotides * (g / 100));
    // A=T has 2 hydrogen bonds; G≡C has 3 hydrogen bonds
    const hBonds = countA * 2 + countG * 3;

    const explanations = [
      `قاعدة شارجاف: في جزيء DNA مزدوج الشريط، %A = %T (${a.toFixed(1)}%) و %G = %C (${g.toFixed(1)}%)`,
      `البيورينات (A + G) = 50.0% والبيريميدينات (T + C) = 50.0%`,
      `إجمالي النيوكليوتيدات = ${totalNucleotides} نيوكليوتيدة → أزواج القواعد = ${basePairs} زوج`,
      `اللفات التامة في لولب DNA المزدوج = أزواج القواعد / 10 = ${basePairs} / 10 = ${turns.toFixed(1)} لفة (أو النيوكليوتيدات / 20)`,
      `إجمالي الروابط الهيدروجينية = 2(A) + 3(G) = (2 × ${countA}) + (3 × ${countG}) = ${hBonds} رابطة`,
    ];

    return {
      baseName: baseUpper,
      adeninePercent: a,
      thyminePercent: t,
      guaninePercent: g,
      cytosinePercent: c,
      purinesPercent: 50.0,
      pyrimidinesPercent: 50.0,
      sampleTotalNucleotides: totalNucleotides,
      basePairsCount: basePairs,
      completeTurnsCount: turns,
      hydrogenBondsTotal: hBonds,
      explanations,
    };
  },
};

export interface CodonCalculationResult {
  rnaNucleotides: number;
  totalCodons: number;
  aminoAcidsCount: number;
  peptideBondsCount: number;
  waterMoleculesReleased: number;
  trnaMoleculesRequired: number;
  dnaDoubleHelixNucleotides: number;
  explanations: string[];
}

export const CodonCalculator = {
  calculateFromNucleotides(
    mrnaNucleotides: number,
    _includeStartMethionine = true
  ): CodonCalculationResult {
    if (mrnaNucleotides <= 0) {
      throw new Error('mRNA nucleotides count must be positive.');
    }
    if (mrnaNucleotides % 3 !== 0) {
      throw new Error('mRNA nucleotide count must be a multiple of 3 (each triplet is a codon).');
    }

    const codons = Math.floor(mrnaNucleotides / 3);
    if (codons < 2) {
      throw new Error('A functional mRNA strand requires at least 2 codons (Start + Stop).');
    }

    // Amino acids = Codons - 1 (stop codon does not code for any amino acid)
    const aa = codons - 1;
    const peptideBonds = Math.max(0, aa - 1);
    const waterMolecules = peptideBonds;
    // Stop codon binds Release Factor, not tRNA!
    const trnaCount = aa;
    // DNA is double-stranded, so DNA nucleotides = mRNA nucleotides * 2
    const dnaNucleotides = mrnaNucleotides * 2;

    const explanations = [
      `عدد كودونات mRNA = النيوكليوتيدات / 3 = ${mrnaNucleotides} / 3 = ${codons} كودون`,
      `عدد الأحماض الأمينية في السلسلة = كودونات - 1 (كودون الوقف UAA/UAG/UGA) = ${codons} - 1 = ${aa} حمض أميني`,
      `الروابط الببتيدية المتكونة = الأحماض الأمينية - 1 = ${aa} - 1 = ${peptideBonds} رابطة ببتيدية`,
      `جزيئات الماء المنزوعة بالتكثيف = الروابط الببتيدية = ${waterMolecules} جزيء H₂O`,
      `جزيئات tRNA المشاركة في النقل = ${trnaCount} جزيء (فخ وزاري: كودون الوقف ليس له tRNA بل يرتبط به عامل الإطلاق)`,
      `نيوكليوتيدات قطعة جين DNA المزدوج = نيوكليوتيدات mRNA × 2 = ${mrnaNucleotides} × 2 = ${dnaNucleotides} نيوكليوتيدة`,
    ];

    return {
      rnaNucleotides: mrnaNucleotides,
      totalCodons: codons,
      aminoAcidsCount: aa,
      peptideBondsCount: peptideBonds,
      waterMoleculesReleased: waterMolecules,
      trnaMoleculesRequired: trnaCount,
      dnaDoubleHelixNucleotides: dnaNucleotides,
      explanations,
    };
  },

  calculateFromAminoAcids(aminoAcids: number): CodonCalculationResult {
    if (aminoAcids <= 0) {
      throw new Error('Amino acid count must be at least 1.');
    }
    const aa = Math.floor(aminoAcids);
    const codons = aa + 1; // plus stop codon
    const mrnaNucleotides = codons * 3;
    return this.calculateFromNucleotides(mrnaNucleotides);
  },
};
