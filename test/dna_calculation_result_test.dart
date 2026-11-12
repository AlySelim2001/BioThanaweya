import 'package:flutter_test/flutter_test.dart';
import 'package:smarts_bio/features/biology_calculators/domain/entities/dna_calculation_result.dart';

DnaCalculationResult _buildSampleResult({
  String baseName = 'A',
  double inputPercent = 28.0,
  double adeninePercent = 28.0,
  double thyminePercent = 28.0,
  double guaninePercent = 22.0,
  double cytosinePercent = 22.0,
  double purinesPercent = 50.0,
  double pyrimidinesPercent = 50.0,
  int totalNucleotides = 1000,
  int basePairsCount = 500,
  double completeTurnsCount = 50.0,
  int adenineCount = 280,
  int thymineCount = 280,
  int guanineCount = 220,
  int cytosineCount = 220,
  int hydrogenBondsTotal = 1220,
  List<String> explanationsAr = const [
    'نسبة الأدينين تساوي الثايمين في اللولب المزدوج.',
    'إجمالي البيورينات يساوي إجمالي البيريميدينات.',
  ],
}) {
  return DnaCalculationResult(
    baseName: baseName,
    inputPercent: inputPercent,
    adeninePercent: adeninePercent,
    thyminePercent: thyminePercent,
    guaninePercent: guaninePercent,
    cytosinePercent: cytosinePercent,
    purinesPercent: purinesPercent,
    pyrimidinesPercent: pyrimidinesPercent,
    totalNucleotides: totalNucleotides,
    basePairsCount: basePairsCount,
    completeTurnsCount: completeTurnsCount,
    adenineCount: adenineCount,
    thymineCount: thymineCount,
    guanineCount: guanineCount,
    cytosineCount: cytosineCount,
    hydrogenBondsTotal: hydrogenBondsTotal,
    explanationsAr: explanationsAr,
  );
}

Map<String, dynamic> _buildSampleMap() {
  return <String, dynamic>{
    'baseName': 'G',
    'inputPercent': 22.5,
    'adeninePercent': 27.5,
    'thyminePercent': 27.5,
    'guaninePercent': 22.5,
    'cytosinePercent': 22.5,
    'purinesPercent': 50.0,
    'pyrimidinesPercent': 50.0,
    'totalNucleotides': 250,
    'basePairsCount': 125,
    'completeTurnsCount': 12.5,
    'adenineCount': 69,
    'thymineCount': 69,
    'guanineCount': 56,
    'cytosineCount': 56,
    'hydrogenBondsTotal': 306,
    'explanationsAr': <String>[
      'شرح خطوة أولى بالعربية',
      'شرح خطوة ثانية بالعربية',
    ],
  };
}

void main() {
  group('DnaCalculationResult Entity', () {
    test('stores all constructor fields accurately including double turns and explanations', () {
      const result = DnaCalculationResult(
        baseName: 'A',
        inputPercent: 28.0,
        adeninePercent: 28.0,
        thyminePercent: 28.0,
        guaninePercent: 22.0,
        cytosinePercent: 22.0,
        purinesPercent: 50.0,
        pyrimidinesPercent: 50.0,
        totalNucleotides: 250,
        basePairsCount: 125,
        completeTurnsCount: 12.5,
        adenineCount: 70,
        thymineCount: 70,
        guanineCount: 55,
        cytosineCount: 55,
        hydrogenBondsTotal: 305,
        explanationsAr: ['خطوة 1', 'خطوة 2'],
      );

      expect(result.baseName, equals('A'));
      expect(result.inputPercent, equals(28.0));
      expect(result.adeninePercent, equals(28.0));
      expect(result.thyminePercent, equals(28.0));
      expect(result.guaninePercent, equals(22.0));
      expect(result.cytosinePercent, equals(22.0));
      expect(result.purinesPercent, equals(50.0));
      expect(result.pyrimidinesPercent, equals(50.0));
      expect(result.totalNucleotides, equals(250));
      expect(result.basePairsCount, equals(125));
      expect(result.completeTurnsCount, equals(12.5));
      expect(result.completeTurnsCount, isA<double>());
      expect(result.adenineCount, equals(70));
      expect(result.thymineCount, equals(70));
      expect(result.guanineCount, equals(55));
      expect(result.cytosineCount, equals(55));
      expect(result.hydrogenBondsTotal, equals(305));
      expect(result.explanationsAr, equals(const ['خطوة 1', 'خطوة 2']));
    });

    group('toMap', () {
      test('serializes all 17 fields with exact keys, types, and values', () {
        final result = _buildSampleResult();
        final map = result.toMap();

        expect(map.keys, hasLength(17));
        expect(map['baseName'], equals('A'));
        expect(map['baseName'], isA<String>());
        expect(map['inputPercent'], equals(28.0));
        expect(map['inputPercent'], isA<double>());
        expect(map['adeninePercent'], equals(28.0));
        expect(map['thyminePercent'], equals(28.0));
        expect(map['guaninePercent'], equals(22.0));
        expect(map['cytosinePercent'], equals(22.0));
        expect(map['purinesPercent'], equals(50.0));
        expect(map['pyrimidinesPercent'], equals(50.0));
        expect(map['totalNucleotides'], equals(1000));
        expect(map['totalNucleotides'], isA<int>());
        expect(map['basePairsCount'], equals(500));
        expect(map['basePairsCount'], isA<int>());
        expect(map['completeTurnsCount'], equals(50.0));
        expect(map['completeTurnsCount'], isA<double>());
        expect(map['adenineCount'], equals(280));
        expect(map['thymineCount'], equals(280));
        expect(map['guanineCount'], equals(220));
        expect(map['cytosineCount'], equals(220));
        expect(map['hydrogenBondsTotal'], equals(1220));
        expect(
          map['explanationsAr'],
          equals(const [
            'نسبة الأدينين تساوي الثايمين في اللولب المزدوج.',
            'إجمالي البيورينات يساوي إجمالي البيريميدينات.',
          ]),
        );
      });

      test('preserves empty explanationsAr list as provided without alteration', () {
        final result = _buildSampleResult(explanationsAr: const []);
        final map = result.toMap();

        expect(map['explanationsAr'], isA<List<String>>());
        expect(map['explanationsAr'], isEmpty);
      });
    });

    group('fromMap', () {
      test('reconstructs entity accurately from a valid Map', () {
        final sourceMap = _buildSampleMap();
        final result = DnaCalculationResult.fromMap(sourceMap);

        expect(result.baseName, equals('G'));
        expect(result.inputPercent, equals(22.5));
        expect(result.adeninePercent, equals(27.5));
        expect(result.thyminePercent, equals(27.5));
        expect(result.guaninePercent, equals(22.5));
        expect(result.cytosinePercent, equals(22.5));
        expect(result.purinesPercent, equals(50.0));
        expect(result.pyrimidinesPercent, equals(50.0));
        expect(result.totalNucleotides, equals(250));
        expect(result.basePairsCount, equals(125));
        expect(result.completeTurnsCount, equals(12.5));
        expect(result.adenineCount, equals(69));
        expect(result.thymineCount, equals(69));
        expect(result.guanineCount, equals(56));
        expect(result.cytosineCount, equals(56));
        expect(result.hydrogenBondsTotal, equals(306));
        expect(
          result.explanationsAr,
          equals(const ['شرح خطوة أولى بالعربية', 'شرح خطوة ثانية بالعربية']),
        );
      });

      test('coerces num values and dynamic lists consistently with JSON decoding', () {
        final jsonLikeMap = <String, dynamic>{
          'baseName': 'C',
          'inputPercent': 20, // int for double field
          'adeninePercent': 30,
          'thyminePercent': 30,
          'guaninePercent': 20,
          'cytosinePercent': 20,
          'purinesPercent': 50,
          'pyrimidinesPercent': 50,
          'totalNucleotides': 400,
          'basePairsCount': 200,
          'completeTurnsCount': 20, // int for double field
          'adenineCount': 120,
          'thymineCount': 120,
          'guanineCount': 80,
          'cytosineCount': 80,
          'hydrogenBondsTotal': 480,
          'explanationsAr': <dynamic>['خطوة مستعادة'],
        };

        final result = DnaCalculationResult.fromMap(jsonLikeMap);

        expect(result.inputPercent, equals(20.0));
        expect(result.inputPercent, isA<double>());
        expect(result.completeTurnsCount, equals(20.0));
        expect(result.completeTurnsCount, isA<double>());
        expect(result.totalNucleotides, equals(400));
        expect(result.totalNucleotides, isA<int>());
        expect(result.explanationsAr, isA<List<String>>());
        expect(result.explanationsAr, equals(const ['خطوة مستعادة']));
      });

      test('throws TypeError when required keys are missing', () {
        final incompleteMap = _buildSampleMap()..remove('baseName');

        expect(
          () => DnaCalculationResult.fromMap(incompleteMap),
          throwsA(isA<TypeError>()),
        );
      });

      test('throws TypeError when numeric or list fields have incompatible types', () {
        final invalidNumericMap = _buildSampleMap()..['inputPercent'] = '22.5';
        final invalidListMap = _buildSampleMap()..['explanationsAr'] = 'not a list';

        expect(
          () => DnaCalculationResult.fromMap(invalidNumericMap),
          throwsA(isA<TypeError>()),
        );
        expect(
          () => DnaCalculationResult.fromMap(invalidListMap),
          throwsA(isA<TypeError>()),
        );
      });
    });

    test('round-trip fromMap(result.toMap()) preserves all field values identically', () {
      final original = _buildSampleResult(
        baseName: 'T',
        inputPercent: 31.5,
        adeninePercent: 31.5,
        thyminePercent: 31.5,
        guaninePercent: 18.5,
        cytosinePercent: 18.5,
        totalNucleotides: 250,
        basePairsCount: 125,
        completeTurnsCount: 12.5,
        adenineCount: 79,
        thymineCount: 79,
        guanineCount: 46,
        cytosineCount: 46,
        hydrogenBondsTotal: 296,
        explanationsAr: const ['الخطوة الأولى', 'الخطوة الثانية'],
      );

      final restored = DnaCalculationResult.fromMap(original.toMap());

      expect(restored.baseName, equals(original.baseName));
      expect(restored.inputPercent, equals(original.inputPercent));
      expect(restored.adeninePercent, equals(original.adeninePercent));
      expect(restored.thyminePercent, equals(original.thyminePercent));
      expect(restored.guaninePercent, equals(original.guaninePercent));
      expect(restored.cytosinePercent, equals(original.cytosinePercent));
      expect(restored.purinesPercent, equals(original.purinesPercent));
      expect(restored.pyrimidinesPercent, equals(original.pyrimidinesPercent));
      expect(restored.totalNucleotides, equals(original.totalNucleotides));
      expect(restored.basePairsCount, equals(original.basePairsCount));
      expect(restored.completeTurnsCount, equals(original.completeTurnsCount));
      expect(restored.adenineCount, equals(original.adenineCount));
      expect(restored.thymineCount, equals(original.thymineCount));
      expect(restored.guanineCount, equals(original.guanineCount));
      expect(restored.cytosineCount, equals(original.cytosineCount));
      expect(restored.hydrogenBondsTotal, equals(original.hydrogenBondsTotal));
      expect(restored.explanationsAr, equals(original.explanationsAr));
      expect(restored.toMap(), equals(original.toMap()));
    });
  });
}
