export type NitrogenousBase = 'A' | 'T' | 'C' | 'G' | 'U';
export type BasePair = 'A-T' | 'T-A' | 'C-G' | 'G-C';
export type EnzymeState = 
  | 'RestingDuplex'
  | 'HelicaseUnwinding'
  | 'RNAPrimasePrimer'
  | 'DNAPolymeraseLeading'
  | 'DNAPolymeraseOkazaki1'
  | 'DNAPolymeraseOkazaki2'
  | 'DNALigaseSealing';
export type DNAStrandDirection = '5to3' | '3to5';
export type Codon = `${NitrogenousBase}${NitrogenousBase}${NitrogenousBase}`;
export enum ComplementaryBase {
  A = 'T',
  T = 'A',
  C = 'G',
  G = 'C',
  U = 'A',
}