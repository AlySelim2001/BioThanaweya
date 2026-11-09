/**
 * @module src/services/calculators.test.ts
 * @description Vitest unit tests for Egyptian Thanaweya Amma biological formula solvers.
 */

import { describe, it, expect } from 'vitest';
import {
  SarcomereCalculator,
  DnaCalculator,
  CodonCalculator,
} from './calculators';

describe('SarcomereCalculator', () => {
  it('correctly calculates structural landmarks for 5 sarcomeres', () => {
    const result = SarcomereCalculator.calculateFromSarcomeres(5);

    expect(result.numberOfSarcomeres).toBe(5);
    expect(result.zLinesCount).toBe(6); // Z = n + 1
    expect(result.aBandsCount).toBe(5); // A = n (constant length)
    expect(result.completeIBandsCount).toBe(4); // I_complete = n - 1
    expect(result.incompleteIBandsCount).toBe(2); // Always 2 ends
    expect(result.hZonesRelaxedCount).toBe(5); // H_relaxed = n
    expect(result.hZonesContractedCount).toBe(0); // H disappears in maximal contraction
  });

  it('correctly calculates sarcomeres from Z-lines count', () => {
    const result = SarcomereCalculator.calculateFromZLines(6);
    expect(result.numberOfSarcomeres).toBe(5);
    expect(result.zLinesCount).toBe(6);
  });

  it('throws error for invalid non-positive sarcomere count', () => {
    expect(() => SarcomereCalculator.calculateFromSarcomeres(0)).toThrow();
    expect(() => SarcomereCalculator.calculateFromSarcomeres(-3)).toThrow();
  });

  it('throws error for fewer than 2 Z-lines', () => {
    expect(() => SarcomereCalculator.calculateFromZLines(1)).toThrow();
  });
});

describe('DnaCalculator (Chargaff & Double Helix Rules)', () => {
  it('correctly determines complementary bases from Adenine percent', () => {
    // If Adenine is 20%, Thymine must be 20%, Guanine must be 30%, Cytosine must be 30%
    const result = DnaCalculator.calculateFromBasePercent('A', 20, 1000);

    expect(result.adeninePercent).toBe(20);
    expect(result.thyminePercent).toBe(20);
    expect(result.guaninePercent).toBe(30);
    expect(result.cytosinePercent).toBe(30);
    expect(result.purinesPercent).toBe(50);
    expect(result.pyrimidinesPercent).toBe(50);
    expect(result.basePairsCount).toBe(500); // 1000 / 2
    expect(result.completeTurnsCount).toBe(50); // 500 / 10
    // Hydrogen bonds: (200 * 2) + (300 * 3) = 400 + 900 = 1300
    expect(result.hydrogenBondsTotal).toBe(1300);
  });

  it('correctly determines complementary bases from Guanine percent', () => {
    // If Guanine is 35%, Cytosine is 35%, Adenine is 15%, Thymine is 15%
    const result = DnaCalculator.calculateFromBasePercent('G', 35, 1000);

    expect(result.guaninePercent).toBe(35);
    expect(result.cytosinePercent).toBe(35);
    expect(result.adeninePercent).toBe(15);
    expect(result.thyminePercent).toBe(15);
  });

  it('throws error when percentage is not strictly between 0% and 50%', () => {
    expect(() => DnaCalculator.calculateFromBasePercent('A', 50)).toThrow();
    expect(() => DnaCalculator.calculateFromBasePercent('A', 55)).toThrow();
    expect(() => DnaCalculator.calculateFromBasePercent('A', 0)).toThrow();
  });
});

describe('CodonCalculator (Translation & Genetic Code)', () => {
  it('calculates codons, amino acids, and water molecules for 300 mRNA nucleotides', () => {
    // 300 nucleotides / 3 = 100 codons
    // Amino acids = 100 - 1 (stop codon excluded) = 99
    // Peptide bonds = 99 - 1 = 98
    // Water eliminated = 98
    // tRNA required = 99 (stop codon binds Release Factor, not tRNA)
    // DNA nucleotides = 300 * 2 = 600
    const result = CodonCalculator.calculateFromNucleotides(300);

    expect(result.rnaNucleotides).toBe(300);
    expect(result.totalCodons).toBe(100);
    expect(result.aminoAcidsCount).toBe(99);
    expect(result.peptideBondsCount).toBe(98);
    expect(result.waterMoleculesReleased).toBe(98);
    expect(result.trnaMoleculesRequired).toBe(99);
    expect(result.dnaDoubleHelixNucleotides).toBe(600);
  });

  it('calculates reverse values from target amino acids', () => {
    const result = CodonCalculator.calculateFromAminoAcids(99);

    expect(result.aminoAcidsCount).toBe(99);
    expect(result.totalCodons).toBe(100); // 99 + 1 stop codon
    expect(result.rnaNucleotides).toBe(300);
  });

  it('throws error when mRNA nucleotides is not a multiple of 3', () => {
    expect(() => CodonCalculator.calculateFromNucleotides(100)).toThrow();
  });

  it('throws error when codons count is less than 2', () => {
    expect(() => CodonCalculator.calculateFromNucleotides(3)).toThrow();
  });
});
