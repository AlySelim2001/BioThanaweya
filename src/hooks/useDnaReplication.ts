import React from 'react';
import { NitrogenousBase, BasePair, EnzymeState, DNAStrandDirection } from '../types/dna.types';

export interface UseDnaReplicationReturn {
  currentStep: number;
  setCurrentStep: (step: number) => void;
  currentBase: NitrogenousBase;
  setCurrentBase: (base: NitrogenousBase) => void;
  complementaryBase: NitrogenousBase;
  hydrogenBondCount: 2 | 3;
  enzymeState: EnzymeState;
  setEnzymeState: (state: EnzymeState) => void;
  strandDirection: DNAStrandDirection;
  setStrandDirection: (dir: DNAStrandDirection) => void;
  dnaSequence: NitrogenousBase[];
  setDnaSequence: (seq: NitrogenousBase[]) => void;
  rnaPrimer: NitrogenousBase[];
  setRnaPrimer: (primer: NitrogenousBase[]) => void;
  okazakiFragments: number[][];
  setOkazakiFragments: (fragments: number[][]) => void;
  simulateStep: (step: number) => void;
  resetSimulation: () => void;
  getBasePairInfo: (base1: NitrogenousBase, base2: NitrogenousBase) => 2 | 3;
  validateChargaffRule: (aPercent: number, tPercent: number, gPercent: number, cPercent: number) => boolean;
}

export const useDnaReplication = (): UseDnaReplicationReturn => {
  const [currentStep, setCurrentStep] = React.useState<number>(0);
  const [currentBase, setCurrentBase] = React.useState<NitrogenousBase>('A');
  const [enzymeState, setEnzymeState] = React.useState<EnzymeState>('RestingDuplex');
  const [strandDirection, setStrandDirection] = React.useState<DNAStrandDirection>('5to3');
  const [dnaSequence, setDnaSequence] = React.useState<NitrogenousBase[]>(['A', 'T', 'C', 'G']);
  const [rnaPrimer, setRnaPrimer] = React.useState<NitrogenousBase[]>([]);
  const [okazakiFragments, setOkazakiFragments] = React.useState<number[][]>([]);

  // Get complementary base
  const getComplementaryBase = (base: NitrogenousBase): NitrogenousBase => {
    const map: Record<NitrogenousBase, NitrogenousBase> = {
      A: 'T',
      T: 'A',
      C: 'G',
      G: 'C',
      U: 'A',
    };
    return map[base];
  };

  // Get hydrogen bond count for a base pair
  const getHydrogenBondCount = (base1: NitrogenousBase, base2: NitrogenousBase): 2 | 3 => {
    const pairs: Record<string, 2 | 3> = {
      'A-T': 2,
      'T-A': 2,
      'C-G': 3,
      'G-C': 3,
      'A-U': 2,
      'U-A': 2,
    };
    return pairs[`${base1}-${base2}`] || 2;
  };

  // Validate Chargaff's rules
  const validateChargaffRule = (aPercent: number, tPercent: number, gPercent: number, cPercent: number): boolean => {
    const total = aPercent + tPercent + gPercent + cPercent;
    if (Math.abs(total - 100) > 0.01) return false;
    return Math.abs(aPercent - tPercent) < 0.01 && Math.abs(gPercent - cPercent) < 0.01;
  };

  // Advance simulation step
  const simulateStep = (step: number) => {
    setCurrentStep(step);
    setEnzymeState(step === 0 ? 'RestingDuplex' : 
      step === 1 ? 'HelicaseUnwinding' :
      step === 2 ? 'RNAPrimasePrimer' :
      step === 3 ? 'DNAPolymeraseLeading' :
      step === 4 ? 'DNAPolymeraseOkazaki1' : 'DNALigaseSealing');
  };

  const resetSimulation = () => {
    setCurrentStep(0);
    setEnzymeState('RestingDuplex');
    setCurrentBase('A');
    setDnaSequence(['A', 'T', 'C', 'G']);
    setRnaPrimer([]);
    setOkazakiFragments([]);
  };

  return {
    currentStep,
    setCurrentStep,
    currentBase,
    setCurrentBase,
    complementaryBase: getComplementaryBase(currentBase),
    hydrogenBondCount: getHydrogenBondCount(currentBase, getComplementaryBase(currentBase)),
    enzymeState,
    setEnzymeState,
    strandDirection,
    setStrandDirection,
    dnaSequence,
    setDnaSequence,
    rnaPrimer,
    setRnaPrimer,
    okazakiFragments,
    setOkazakiFragments,
    simulateStep,
    resetSimulation,
    getBasePairInfo: getHydrogenBondCount,
    validateChargaffRule,
  };
};