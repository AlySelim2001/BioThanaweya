import React, { useState } from 'react';
import { ArrowLeft, Calculator, Activity, Dna, FlaskConical, AlertCircle } from 'lucide-react';
import {
  SarcomereCalculator,
  SarcomereResult,
  DnaCalculator,
  DnaCalculationResult,
  CodonCalculator,
  CodonCalculationResult,
} from '../services/calculators';

interface FormulaLabScreenProps {
  onBack: () => void;
}

export const FormulaLabScreen: React.FC<FormulaLabScreenProps> = ({ onBack }) => {
  const [selectedTab, setSelectedTab] = useState<'sarcomere' | 'chargaff' | 'codon'>('sarcomere');

  // Sarcomere State
  const [sarcomereInput, setSarcomereInput] = useState('5');
  const [sarcomereResult, setSarcomereResult] = useState<SarcomereResult | null>(() => {
    try {
      return SarcomereCalculator.calculateFromSarcomeres(5);
    } catch {
      return null;
    }
  });
  const [sarcomereError, setSarcomereError] = useState('');

  const handleSarcomereChange = (val: string) => {
    setSarcomereInput(val);
    const num = Number(val);
    if (!val || isNaN(num) || num <= 0) {
      setSarcomereError('Please enter a positive integer greater than zero.');
      setSarcomereResult(null);
      return;
    }
    try {
      setSarcomereError('');
      setSarcomereResult(SarcomereCalculator.calculateFromSarcomeres(num));
    } catch (err: any) {
      setSarcomereError(err.message);
      setSarcomereResult(null);
    }
  };

  // Chargaff State
  const [dnaBase, setDnaBase] = useState('A');
  const [dnaPercent, setDnaPercent] = useState('20');
  const [dnaTotal, setDnaTotal] = useState('1000');
  const [chargaffResult, setChargaffResult] = useState<DnaCalculationResult | null>(() => {
    try {
      return DnaCalculator.calculateFromBasePercent('A', 20, 1000);
    } catch {
      return null;
    }
  });
  const [chargaffError, setChargaffError] = useState('');

  const handleChargaffChange = (base: string, percentStr: string, totalStr: string) => {
    setDnaBase(base);
    setDnaPercent(percentStr);
    setDnaTotal(totalStr);

    const p = parseFloat(percentStr);
    const tot = parseInt(totalStr, 10);

    if (isNaN(p) || p <= 0 || p >= 50) {
      setChargaffError('Base percentage must be strictly between 0% and 50% (< 50%).');
      setChargaffResult(null);
      return;
    }
    if (isNaN(tot) || tot <= 0) {
      setChargaffError('Total nucleotides must be greater than zero.');
      setChargaffResult(null);
      return;
    }

    try {
      setChargaffError('');
      setChargaffResult(DnaCalculator.calculateFromBasePercent(base, p, tot));
    } catch (err: any) {
      setChargaffError(err.message);
      setChargaffResult(null);
    }
  };

  // Codon State
  const [mrnaInput, setMrnaInput] = useState('90');
  const [includeStart, setIncludeStart] = useState(true);
  const [codonResult, setCodonResult] = useState<CodonCalculationResult | null>(() => {
    try {
      return CodonCalculator.calculateFromNucleotides(90, true);
    } catch {
      return null;
    }
  });
  const [codonError, setCodonError] = useState('');

  const handleCodonChange = (val: string, incStart: boolean) => {
    setMrnaInput(val);
    setIncludeStart(incStart);

    const nt = parseInt(val, 10);
    if (isNaN(nt) || nt <= 0) {
      setCodonError('mRNA nucleotide count must be positive.');
      setCodonResult(null);
      return;
    }
    if (nt % 3 !== 0) {
      setCodonError('mRNA nucleotide count must be a multiple of 3.');
      setCodonResult(null);
      return;
    }

    try {
      setCodonError('');
      setCodonResult(CodonCalculator.calculateFromNucleotides(nt, incStart));
    } catch (err: any) {
      setCodonError(err.message);
      setCodonResult(null);
    }
  };

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6 animate-in fade-in duration-300">
      {/* Header */}
      <div className="flex items-center gap-4 pb-4 border-b border-slate-800">
        <button
          onClick={onBack}
          className="p-2 rounded-xl bg-slate-900 border border-slate-800 hover:bg-slate-800 text-slate-300 hover:text-white transition"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>
        <div>
          <h1 className="text-xl md:text-2xl font-black text-white flex items-center gap-2">
            <Calculator className="w-6 h-6 text-amber-400" />
            Formula & Bio-Law Lab
          </h1>
          <p className="text-xs text-slate-400 font-arabic">
            معمل القوانين والمسائل الحسابية المنهجية (القطع العضلية، شارجاف، وتخليق البروتين)
          </p>
        </div>
      </div>

      {/* Tabs */}
      <div className="flex items-center gap-2 p-1 rounded-xl bg-slate-900 border border-slate-800">
        <button
          onClick={() => setSelectedTab('sarcomere')}
          className={`flex items-center gap-2 flex-1 justify-center py-2.5 rounded-lg text-xs font-bold transition ${
            selectedTab === 'sarcomere'
              ? 'bg-teal-500 text-slate-950 shadow-sm'
              : 'text-slate-400 hover:text-white'
          }`}
        >
          <Activity className="w-4 h-4" />
          Sarcomere Landmarks
        </button>
        <button
          onClick={() => setSelectedTab('chargaff')}
          className={`flex items-center gap-2 flex-1 justify-center py-2.5 rounded-lg text-xs font-bold transition ${
            selectedTab === 'chargaff'
              ? 'bg-purple-500 text-white shadow-sm'
              : 'text-slate-400 hover:text-white'
          }`}
        >
          <Dna className="w-4 h-4" />
          Chargaff & DNA Helix
        </button>
        <button
          onClick={() => setSelectedTab('codon')}
          className={`flex items-center gap-2 flex-1 justify-center py-2.5 rounded-lg text-xs font-bold transition ${
            selectedTab === 'codon'
              ? 'bg-cyan-500 text-slate-950 shadow-sm'
              : 'text-slate-400 hover:text-white'
          }`}
        >
          <FlaskConical className="w-4 h-4" />
          Codons & Translation
        </button>
      </div>

      {/* TAB 1: Sarcomere */}
      {selectedTab === 'sarcomere' && (
        <div className="space-y-6">
          <div className="rounded-2xl border border-slate-800 bg-[#0E1A35]/80 p-6 space-y-4 shadow-lg">
            <div className="space-y-1">
              <h3 className="text-sm font-bold text-white flex items-center gap-2">
                <Activity className="w-4 h-4 text-teal-400" />
                🔬 Sarcomere Landmark Solver (حل مسائل القطع العضلية):
              </h3>
              <p className="text-xs text-slate-400">
                Validates positive integer inputs while enforcing constant A-band length and maximal
                contraction H-zone collapse.
              </p>
            </div>

            <div className="max-w-xs space-y-1">
              <label className="text-xs font-bold text-slate-300">Number of Sarcomeres (n):</label>
              <input
                type="number"
                min="1"
                value={sarcomereInput}
                onChange={(e) => handleSarcomereChange(e.target.value)}
                className="w-full px-4 py-2 rounded-xl bg-slate-900 border border-slate-700 text-white font-mono text-sm focus:outline-none focus:border-teal-400"
                placeholder="e.g. 5"
              />
            </div>

            {sarcomereError && (
              <div className="flex items-center gap-2 text-xs text-rose-400 bg-rose-500/10 p-3 rounded-xl border border-rose-500/20">
                <AlertCircle className="w-4 h-4 shrink-0" />
                <span>{sarcomereError}</span>
              </div>
            )}
          </div>

          {sarcomereResult && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 md:grid-cols-3 gap-3.5">
                <div className="p-4 rounded-xl bg-slate-900 border border-teal-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    خطوط Z (Z-lines)
                  </div>
                  <div className="text-2xl font-mono font-black text-teal-400">
                    {sarcomereResult.zLinesCount}
                  </div>
                  <div className="text-[10px] text-teal-300/70 font-mono">Formula: n + 1</div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-purple-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    المناطق الداكنة (A-bands)
                  </div>
                  <div className="text-2xl font-mono font-black text-purple-400">
                    {sarcomereResult.aBandsCount}
                  </div>
                  <div className="text-[10px] text-purple-300/70 font-mono">
                    ثابتة لا تقصر أبدًا (= n)
                  </div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-cyan-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    المناطق المضيئة الكاملة (I-bands)
                  </div>
                  <div className="text-2xl font-mono font-black text-cyan-400">
                    {sarcomereResult.completeIBandsCount}
                  </div>
                  <div className="text-[10px] text-cyan-300/70 font-mono">
                    Formula: n - 1 = Z - 2
                  </div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-slate-700 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    المضيئة غير الكاملة (طرفية)
                  </div>
                  <div className="text-2xl font-mono font-black text-slate-300">
                    {sarcomereResult.incompleteIBandsCount}
                  </div>
                  <div className="text-[10px] text-slate-500 font-mono">
                    دائمًا 2 على طرفي اللييفة
                  </div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-amber-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    شبه المضيئة H (في الانبساط)
                  </div>
                  <div className="text-2xl font-mono font-black text-amber-400">
                    {sarcomereResult.hZonesRelaxedCount}
                  </div>
                  <div className="text-[10px] text-amber-300/70 font-mono">تساوي n تمامًا</div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-rose-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    شبه المضيئة H (انقباض تام)
                  </div>
                  <div className="text-2xl font-mono font-black text-rose-400">
                    {sarcomereResult.hZonesContractedCount}
                  </div>
                  <div className="text-[10px] text-rose-300/70 font-mono">تختفي تمامًا = 0!</div>
                </div>
              </div>

              {/* Explanations */}
              <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800 space-y-2">
                <h4 className="text-xs font-bold text-slate-300 font-arabic">
                  خطوات الاستنتاج الرياضي:
                </h4>
                <div className="space-y-1.5 text-xs text-slate-300 font-arabic">
                  {sarcomereResult.explanations.map((exp, idx) => (
                    <div key={idx} className="flex items-start gap-2">
                      <span className="text-teal-400 font-bold">•</span>
                      <span>{exp}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}
        </div>
      )}

      {/* TAB 2: Chargaff */}
      {selectedTab === 'chargaff' && (
        <div className="space-y-6">
          <div className="rounded-2xl border border-slate-800 bg-[#0E1A35]/80 p-6 space-y-4 shadow-lg">
            <div className="space-y-1">
              <h3 className="text-sm font-bold text-white flex items-center gap-2">
                <Dna className="w-4 h-4 text-purple-400" />
                🧬 Chargaff's Law & DNA Double Helix Solver (قوانين شارجاف واللفات):
              </h3>
              <p className="text-xs text-slate-400">
                Enforces %A = %T and %G = %C, 50% purines/pyrimidines, and 10 base pairs per helical
                turn.
              </p>
            </div>

            <div className="space-y-3">
              <div className="flex items-center gap-2">
                <span className="text-xs font-bold text-slate-300">Target Nitrogenous Base:</span>
                {['A', 'T', 'G', 'C'].map((b) => (
                  <button
                    key={b}
                    onClick={() => handleChargaffChange(b, dnaPercent, dnaTotal)}
                    className={`w-8 h-8 rounded-lg font-mono font-bold text-xs transition ${
                      dnaBase === b
                        ? 'bg-purple-600 text-white shadow-md'
                        : 'bg-slate-800 text-slate-300 hover:text-white'
                    }`}
                  >
                    {b}
                  </button>
                ))}
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 max-w-md">
                <div className="space-y-1">
                  <label className="text-xs font-bold text-slate-300">Base Percentage (%):</label>
                  <input
                    type="number"
                    step="0.1"
                    min="0.1"
                    max="49.9"
                    value={dnaPercent}
                    onChange={(e) => handleChargaffChange(dnaBase, e.target.value, dnaTotal)}
                    className="w-full px-4 py-2 rounded-xl bg-slate-900 border border-slate-700 text-white font-mono text-sm focus:outline-none focus:border-purple-400"
                    placeholder="e.g. 20"
                  />
                </div>

                <div className="space-y-1">
                  <label className="text-xs font-bold text-slate-300">Total Nucleotides:</label>
                  <input
                    type="number"
                    min="20"
                    step="20"
                    value={dnaTotal}
                    onChange={(e) => handleChargaffChange(dnaBase, dnaPercent, e.target.value)}
                    className="w-full px-4 py-2 rounded-xl bg-slate-900 border border-slate-700 text-white font-mono text-sm focus:outline-none focus:border-purple-400"
                    placeholder="e.g. 1000"
                  />
                </div>
              </div>
            </div>

            {chargaffError && (
              <div className="flex items-center gap-2 text-xs text-rose-400 bg-rose-500/10 p-3 rounded-xl border border-rose-500/20">
                <AlertCircle className="w-4 h-4 shrink-0" />
                <span>{chargaffError}</span>
              </div>
            )}
          </div>

          {chargaffResult && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 md:grid-cols-4 gap-3.5">
                <div className="p-4 rounded-xl bg-slate-900 border border-teal-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400">Adenine (%A)</div>
                  <div className="text-2xl font-mono font-black text-teal-400">
                    {chargaffResult.adeninePercent.toFixed(1)}%
                  </div>
                  <div className="text-[10px] text-teal-300/70 font-mono">= % Thymine</div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-teal-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400">Thymine (%T)</div>
                  <div className="text-2xl font-mono font-black text-teal-400">
                    {chargaffResult.thyminePercent.toFixed(1)}%
                  </div>
                  <div className="text-[10px] text-teal-300/70 font-mono">= % Adenine</div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-purple-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400">Guanine (%G)</div>
                  <div className="text-2xl font-mono font-black text-purple-400">
                    {chargaffResult.guaninePercent.toFixed(1)}%
                  </div>
                  <div className="text-[10px] text-purple-300/70 font-mono">= % Cytosine</div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-purple-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400">Cytosine (%C)</div>
                  <div className="text-2xl font-mono font-black text-purple-400">
                    {chargaffResult.cytosinePercent.toFixed(1)}%
                  </div>
                  <div className="text-[10px] text-purple-300/70 font-mono">= % Guanine</div>
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-3 gap-3.5">
                <div className="p-4 rounded-xl bg-slate-900 border border-cyan-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400">
                    Base Pairs (أزواج القواعد)
                  </div>
                  <div className="text-2xl font-mono font-black text-cyan-400">
                    {chargaffResult.basePairsCount}
                  </div>
                  <div className="text-[10px] text-cyan-300/70 font-mono">Total / 2</div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-amber-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400">
                    Complete Turns (اللفات التامة)
                  </div>
                  <div className="text-2xl font-mono font-black text-amber-400">
                    {chargaffResult.completeTurnsCount.toFixed(1)}
                  </div>
                  <div className="text-[10px] text-amber-300/70 font-mono">Base Pairs / 10</div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-rose-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400">
                    Hydrogen Bonds (الروابط)
                  </div>
                  <div className="text-2xl font-mono font-black text-rose-400">
                    {chargaffResult.hydrogenBondsTotal}
                  </div>
                  <div className="text-[10px] text-rose-300/70 font-mono">2(A) + 3(G)</div>
                </div>
              </div>

              {/* Explanations */}
              <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800 space-y-2">
                <h4 className="text-xs font-bold text-slate-300 font-arabic">
                  خطوات التحقق الرياضي:
                </h4>
                <div className="space-y-1.5 text-xs text-slate-300 font-arabic">
                  {chargaffResult.explanations.map((exp, idx) => (
                    <div key={idx} className="flex items-start gap-2">
                      <span className="text-purple-400 font-bold">•</span>
                      <span>{exp}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}
        </div>
      )}

      {/* TAB 3: Codon */}
      {selectedTab === 'codon' && (
        <div className="space-y-6">
          <div className="rounded-2xl border border-slate-800 bg-[#0E1A35]/80 p-6 space-y-4 shadow-lg">
            <div className="space-y-1">
              <h3 className="text-sm font-bold text-white flex items-center gap-2">
                <FlaskConical className="w-4 h-4 text-cyan-400" />
                🧪 Codon & Protein Synthesis Calculator (حاسبة الشفرة الوراثية):
              </h3>
              <p className="text-xs text-slate-400">
                Verifies triplet codons, excludes stop codons from amino acid counts, and calculates
                peptide bonds and water elimination.
              </p>
            </div>

            <div className="space-y-3 max-w-md">
              <div className="space-y-1">
                <label className="text-xs font-bold text-slate-300">
                  mRNA Nucleotides (Must be multiple of 3):
                </label>
                <input
                  type="number"
                  step="3"
                  min="6"
                  value={mrnaInput}
                  onChange={(e) => handleCodonChange(e.target.value, includeStart)}
                  className="w-full px-4 py-2 rounded-xl bg-slate-900 border border-slate-700 text-white font-mono text-sm focus:outline-none focus:border-cyan-400"
                  placeholder="e.g. 90"
                />
              </div>

              <label className="flex items-center gap-2 text-xs text-slate-300 cursor-pointer">
                <input
                  type="checkbox"
                  checked={includeStart}
                  onChange={(e) => handleCodonChange(mrnaInput, e.target.checked)}
                  className="rounded bg-slate-800 border-slate-700 text-cyan-500 focus:ring-0"
                />
                <span>Include AUG start codon (Methionine) in final peptide chain</span>
              </label>
            </div>

            {codonError && (
              <div className="flex items-center gap-2 text-xs text-rose-400 bg-rose-500/10 p-3 rounded-xl border border-rose-500/20">
                <AlertCircle className="w-4 h-4 shrink-0" />
                <span>{codonError}</span>
              </div>
            )}
          </div>

          {codonResult && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 md:grid-cols-3 gap-3.5">
                <div className="p-4 rounded-xl bg-slate-900 border border-teal-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    كودونات mRNA
                  </div>
                  <div className="text-2xl font-mono font-black text-teal-400">
                    {codonResult.totalCodons}
                  </div>
                  <div className="text-[10px] text-teal-300/70 font-mono">Nucleotides / 3</div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-purple-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    الأحماض الأمينية
                  </div>
                  <div className="text-2xl font-mono font-black text-purple-400">
                    {codonResult.aminoAcidsCount}
                  </div>
                  <div className="text-[10px] text-purple-300/70 font-mono">Codons - 1 (Stop)</div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-cyan-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    الروابط الببتيدية
                  </div>
                  <div className="text-2xl font-mono font-black text-cyan-400">
                    {codonResult.peptideBondsCount}
                  </div>
                  <div className="text-[10px] text-cyan-300/70 font-mono">Amino Acids - 1</div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-rose-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    جزيئات الماء المنزوعة
                  </div>
                  <div className="text-2xl font-mono font-black text-rose-400">
                    {codonResult.waterMoleculesReleased}
                  </div>
                  <div className="text-[10px] text-rose-300/70 font-mono">
                    = عدد الروابط الببتيدية
                  </div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-amber-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    جزيئات tRNA المشاركة
                  </div>
                  <div className="text-2xl font-mono font-black text-amber-400">
                    {codonResult.trnaMoleculesRequired}
                  </div>
                  <div className="text-[10px] text-amber-300/70 font-mono">
                    كودون الوقف ليس له tRNA!
                  </div>
                </div>

                <div className="p-4 rounded-xl bg-slate-900 border border-emerald-500/30 space-y-1">
                  <div className="text-[11px] font-bold text-slate-400 font-arabic">
                    نيوكليوتيدات جين DNA
                  </div>
                  <div className="text-2xl font-mono font-black text-emerald-400">
                    {codonResult.dnaDoubleHelixNucleotides}
                  </div>
                  <div className="text-[10px] text-emerald-300/70 font-mono">
                    شريط مزدوج = mRNA × 2
                  </div>
                </div>
              </div>

              {/* Explanations */}
              <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800 space-y-2">
                <h4 className="text-xs font-bold text-slate-300 font-arabic">
                  خطوات التحقق الرياضي:
                </h4>
                <div className="space-y-1.5 text-xs text-slate-300 font-arabic">
                  {codonResult.explanations.map((exp, idx) => (
                    <div key={idx} className="flex items-start gap-2">
                      <span className="text-cyan-400 font-bold">•</span>
                      <span>{exp}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};
