import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smarts_bio/features/biology_calculators/domain/engines/dna_calculator_engine.dart';
import 'package:smarts_bio/features/biology_calculators/domain/engines/genetics_inheritance_engine.dart';
import 'package:smarts_bio/features/biology_calculators/domain/engines/muscle_energetics_engine.dart';
import 'package:smarts_bio/features/biology_calculators/domain/entities/calculator_fsrs_item.dart';
import 'package:smarts_bio/features/biology_calculators/data/datasources/local_calculator_storage_source.dart';
import 'package:smarts_bio/features/biology_calculators/data/repositories/biology_calculator_repository_impl.dart';
import 'package:smarts_bio/features/biology_calculators/presentation/controllers/biology_calculator_controller.dart';
import 'package:smarts_bio/features/biology_calculators/presentation/controllers/calculator_session_state.dart';
import 'package:smarts_bio/features/biology_calculators/presentation/widgets/punnett_matrix_table.dart';
import 'package:smarts_bio/features/biology_calculators/presentation/widgets/sarcomere_diagram_view.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('DNA & Chargaff Molecular Engine Tests', () {
    test('DnaCalculatorEngine correctly computes base percentages, turns, and hydrogen bonds', () {
      // Test A = 28% in 1000 nucleotides
      final result = DnaCalculatorEngine.calculateFromBasePercent(
        baseName: 'A',
        percent: 28.0,
        totalNucleotides: 1000,
      );

      expect(result.adeninePercent, equals(28.0));
      expect(result.thyminePercent, equals(28.0));
      expect(result.guaninePercent, equals(22.0));
      expect(result.cytosinePercent, equals(22.0));
      expect(result.purinesPercent, equals(50.0));
      expect(result.pyrimidinesPercent, equals(50.0));
      expect(result.basePairsCount, equals(500));
      expect(result.completeTurnsCount, equals(50.0));
      expect(result.adenineCount, equals(280));
      expect(result.guanineCount, equals(220));
      // Hydrogen bonds: 2(280) + 3(220) = 560 + 660 = 1220
      expect(result.hydrogenBondsTotal, equals(1220));
      expect(result.explanationsAr.length, greaterThanOrEqualTo(5));
    });

    test('DnaCalculatorEngine validates boundary percentages strictly', () {
      expect(
        () => DnaCalculatorEngine.calculateFromBasePercent(baseName: 'A', percent: 0.0),
        throwsArgumentError,
      );
      expect(
        () => DnaCalculatorEngine.calculateFromBasePercent(baseName: 'A', percent: 50.0),
        throwsArgumentError,
      );
      expect(
        () => DnaCalculatorEngine.calculateFromBasePercent(baseName: 'A', percent: 60.0),
        throwsArgumentError,
      );
      expect(
        () => DnaCalculatorEngine.calculateFromBasePercent(baseName: 'A', percent: 25.0, totalNucleotides: 0),
        throwsArgumentError,
      );
    });
  });

  group('Genetics & Punnett Square Engine Tests', () {
    test('crossMonohybrid calculates standard Mendelian 3:1 ratio for Aa x Aa', () {
      final cross = GeneticsInheritanceEngine.crossMonohybrid(parent1: 'Aa', parent2: 'Aa');
      expect(cross.crossType, equals('monohybrid_mendelian'));
      expect(cross.matrix.length, equals(2));
      expect(cross.matrix[0].length, equals(2));

      // Genotypic counts: 1 AA, 2 Aa, 1 aa
      expect(cross.genotypeFrequencies['AA'], equals(1));
      expect(cross.genotypeFrequencies['Aa'], equals(2));
      expect(cross.genotypeFrequencies['aa'], equals(1));

      // Phenotypic counts: 3 dominant, 1 recessive
      expect(cross.phenotypeFrequencies['صفة سائدة'], equals(3));
      expect(cross.phenotypeFrequencies['صفة متنحية'], equals(1));
      expect(cross.ratioSummary, contains('3 سائد : 1 متنحي'));
    });

    test('crossMonohybrid handles Incomplete Dominance 1:2:1', () {
      final cross = GeneticsInheritanceEngine.crossMonohybrid(
        parent1: 'Aa',
        parent2: 'Aa',
        isIncompleteDominance: true,
      );
      expect(cross.crossType, equals('incomplete_dominance'));
      expect(cross.phenotypeFrequencies['صفة وسطية جديدة (قرنفلي)'], equals(2));
      expect(cross.phenotypeFrequencies['صفة نقية أولى (أحمر)'], equals(1));
      expect(cross.phenotypeFrequencies['صفة نقية ثانية (أبيض)'], equals(1));
    });

    test('crossMonohybrid handles Lethal Genes (2:1 survivor ratio)', () {
      final cross = GeneticsInheritanceEngine.crossMonohybrid(
        parent1: 'Aa',
        parent2: 'Aa',
        isDominantLethal: true,
      );
      expect(cross.crossType, equals('lethal_genes_dominant'));
      expect(cross.ratioSummary, contains('2 : 1'));
      expect(cross.ministryExamTrapNote, contains('25%'));
    });

    test('crossDihybrid calculates Mendelian 9:3:3:1 ratio for AaBb x AaBb', () {
      final cross = GeneticsInheritanceEngine.crossDihybrid(parent1: 'AaBb', parent2: 'AaBb');
      expect(cross.crossType, equals('dihybrid_mendelian'));
      expect(cross.matrix.length, equals(4));
      expect(cross.matrix[0].length, equals(4));

      int totalCells = 0;
      for (final row in cross.matrix) {
        totalCells += row.length;
      }
      expect(totalCells, equals(16));

      expect(cross.phenotypeFrequencies['سائد للصفتين معا (A_B_)'], equals(9));
      expect(cross.phenotypeFrequencies['سائد للأولى متنحي للثانية (A_bb)'], equals(3));
      expect(cross.phenotypeFrequencies['متنحي للأولى سائد للثانية (aaB_)'], equals(3));
      expect(cross.phenotypeFrequencies['متنحي للصفتين معا (aabb)'], equals(1));
    });
  });

  group('Muscle Physiology & Cellular ATP Energetics Tests', () {
    test('calculateSarcomere calculates Z-lines, A-bands, and H-zones in relaxed and contracted states', () {
      final relaxed = MuscleEnergeticsEngine.calculateSarcomere(sarcomeres: 4, isFullyContracted: false);
      expect(relaxed.numberOfSarcomeres, equals(4));
      expect(relaxed.zLinesCount, equals(5));
      expect(relaxed.aBandsCount, equals(4));
      expect(relaxed.completeIBandsCount, equals(3));
      expect(relaxed.incompleteIBandsCount, equals(2));
      expect(relaxed.hZonesRelaxedCount, equals(4));
      expect(relaxed.hZonesContractedCount, equals(0));

      final contracted = MuscleEnergeticsEngine.calculateSarcomere(sarcomeres: 4, isFullyContracted: true);
      expect(contracted.isFullyContracted, isTrue);
      expect(contracted.explanationsAr.any((e) => e.contains('تختفي')), isTrue);
    });

    test('calculateCellularAtp yields 38 ATP per glucose molecule in aerobic respiration', () {
      final atp1 = MuscleEnergeticsEngine.calculateCellularAtp(glucoseMolecules: 1);
      expect(atp1.totalNetAtp, equals(38));
      expect(atp1.glycolysisDirectAtp, equals(2));
      expect(atp1.krebsDirectAtp, equals(2));
      expect(atp1.oxidativePhosphorylationAtp, equals(34));
      expect(atp1.anaerobicLacticAtp, equals(2));

      final atp2 = MuscleEnergeticsEngine.calculateCellularAtp(glucoseMolecules: 3);
      expect(atp2.totalNetAtp, equals(114)); // 38 * 3
      expect(atp2.anaerobicLacticAtp, equals(6));
    });
  });

  group('FSRS Spaced Repetition Item Adaptation Tests', () {
    test('CalculatorFsrsItem increases intervals upon correct responses and resets on failure', () {
      final initial = CalculatorFsrsItem(
        formulaId: 'dna_turns',
        category: 'dna',
        titleAr: 'قانون اللفات',
        formulaPromptAr: 'ما عدد اللفات؟',
        nextReviewDate: DateTime.now().subtract(const Duration(minutes: 5)),
      );

      expect(initial.isDueForReview, isTrue);

      // Attempt 1: Correct
      final rep1 = initial.evaluateAttempt(wasCorrect: true);
      expect(rep1.repetition, equals(1));
      expect(rep1.intervalDays, equals(1.0));

      // Attempt 2: Correct
      final rep2 = rep1.evaluateAttempt(wasCorrect: true);
      expect(rep2.repetition, equals(2));
      expect(rep2.intervalDays, equals(6.0));

      // Attempt 3: Failed -> resets repetition and interval
      final failed = rep2.evaluateAttempt(wasCorrect: false);
      expect(failed.repetition, equals(0));
      expect(failed.intervalDays, equals(1.0));
      expect(failed.totalMistakes, equals(1));
    });
  });

  group('Biology Calculator Controller & Presentation Tests', () {
    setUp(() {
      SharedPreferences.setMockInitialValues({});
    });

    test('BiologyCalculatorController updates reactive state across tabs and records history', () async {
      final controller = BiologyCalculatorController();
      await controller.initialize();

      expect(controller.state.dnaResult, isNotNull);
      expect(controller.state.geneticsResult, isNotNull);
      expect(controller.state.sarcomereResult, isNotNull);
      expect(controller.state.atpResult, isNotNull);
      expect(controller.state.allFsrsItems.isNotEmpty, isTrue);

      // Switch active tab
      controller.setActiveTab(CalculatorTab.sarcomere);
      expect(controller.state.activeTab, equals(CalculatorTab.sarcomere));

      // Perform custom calculation
      controller.calculateSarcomere(sarcomeres: 8, isFullyContracted: true);
      expect(controller.state.sarcomereResult?.numberOfSarcomeres, equals(8));
      expect(controller.state.sarcomereResult?.isFullyContracted, isTrue);

      // Verify history accumulation
      await controller.loadHistory();
      expect(controller.state.calculationHistory.isNotEmpty, isTrue);
    });

    testWidgets('PunnettMatrixTable and SarcomereDiagramView render cleanly without layout overflow', (WidgetTester tester) async {
      final crossResult = GeneticsInheritanceEngine.crossMonohybrid(parent1: 'Aa', parent2: 'Aa');
      final sarcomereResult = MuscleEnergeticsEngine.calculateSarcomere(sarcomeres: 3, isFullyContracted: false);

      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(
            body: SingleChildScrollView(
              child: Column(
                children: [
                  PunnettMatrixTable(result: crossResult),
                  const SizedBox(height: 16),
                  SarcomereDiagramView(result: sarcomereResult),
                ],
              ),
            ),
          ),
        ),
      );

      await tester.pumpAndSettle();

      expect(find.byType(PunnettMatrixTable), findsOneWidget);
      expect(find.byType(SarcomereDiagramView), findsOneWidget);
      expect(find.textContaining('مربع بانيت'), findsOneWidget);
      expect(find.textContaining('المخطط التوضيحي للقطعة العضلية'), findsOneWidget);
    });
  });
}
