import { describe, it, expect, vi, beforeEach } from 'vitest';
import { MinistryExaminerEngine } from './examinerEngine';
import { MinistryExamItem } from '../types';

describe('MinistryExaminerEngine Local Heuristics & Scoring', () => {
  const sampleExamItem: MinistryExamItem = {
    id: 'test_sarcomere_sim',
    questionType: 'GIVE_REASON',
    prompt: 'Give Reason: The absence of ATP in a skeletal muscle fiber leads to sustained muscle spasm.',
    maxScore: 2.0,
    requiredKeywords: ['atp', 'cross bridges', 'detach', 'actin', 'spasm'],
    partialCreditKeywords: ['myosin', 'relaxation', 'tear'],
    modelAnswer:
      'Because ATP is necessary for the detachment of myosin cross-bridges from actin filaments. In the absence of ATP, cross-bridges remain locked to actin, preventing relaxation and causing sustained muscle spasm.',
    remediationNotes: 'You must explicitly explain that ATP is required to BREAK the cross-bridge bond, not just for contraction.',
  };

  it('rejects answers shorter than 5 characters with 0 score and NEEDS_REVISION status', () => {
    const emptyResult = MinistryExaminerEngine.gradeStudentAnswerLocally(sampleExamItem, '');
    expect(emptyResult.scoreAwarded).toBe(0.0);
    expect(emptyResult.gradeStatus).toBe('NEEDS_REVISION');
    expect(emptyResult.detectedKeywords).toEqual([]);
    expect(emptyResult.missingKeywords).toEqual(sampleExamItem.requiredKeywords);
    expect(emptyResult.examinerFeedback).toContain('too brief or empty');

    const shortResult = MinistryExaminerEngine.gradeStudentAnswerLocally(sampleExamItem, 'no');
    expect(shortResult.scoreAwarded).toBe(0.0);
    expect(shortResult.gradeStatus).toBe('NEEDS_REVISION');
  });

  it('awards FULL_MARK when at least 85% of required keywords are matched', () => {
    // 5 required keywords: 'atp', 'cross bridges', 'detach', 'actin', 'spasm'
    // Providing all 5 keywords:
    const completeAnswer =
      'Because ATP is required to detach the myosin cross-bridges from actin filaments, and without it the muscle suffers from painful spasm.';
    const result = MinistryExaminerEngine.gradeStudentAnswerLocally(sampleExamItem, completeAnswer);

    expect(result.scoreAwarded).toBe(sampleExamItem.maxScore);
    expect(result.gradeStatus).toBe('FULL_MARK');
    expect(result.detectedKeywords.length).toBeGreaterThanOrEqual(4);
    expect(result.examinerFeedback).toContain('Excellent!');
    expect(result.modelAnswer).toBe(sampleExamItem.modelAnswer);
  });

  it('awards PARTIAL_CREDIT and penalty warning when only 50% to 84% keywords match', () => {
    // Providing 3 out of 5 keywords (60%): 'atp', 'detach', 'spasm'
    const partialAnswer =
      'Because ATP is needed to detach the hooks, otherwise severe spasm occurs.';
    const result = MinistryExaminerEngine.gradeStudentAnswerLocally(sampleExamItem, partialAnswer);

    expect(result.scoreAwarded).toBe(1.5); // 0.75 * 2.0
    expect(result.gradeStatus).toBe('PARTIAL_CREDIT');
    expect(result.missingKeywords).toContain('actin');
    expect(result.examinerFeedback).toContain('Penalty Warning');
    expect(result.examinerFeedback).toContain('actin');
  });

  it('resolves biological synonyms accurately in isKeywordPresent', () => {
    // Calcium synonyms
    expect(MinistryExaminerEngine.isKeywordPresent('influx of Ca2+ ions into knob', 'calcium')).toBe(true);
    expect(MinistryExaminerEngine.isKeywordPresent('elevated Ca++ in blood', 'calcium')).toBe(true);
    expect(MinistryExaminerEngine.isKeywordPresent('presence of ca ions in sarcoplasm', 'calcium')).toBe(true);

    // Acetylcholine synonyms
    expect(MinistryExaminerEngine.isKeywordPresent('release of ACh neurotransmitter', 'acetylcholine')).toBe(true);
    expect(MinistryExaminerEngine.isKeywordPresent('diffusion of acetyl-choline', 'acetylcholine')).toBe(true);

    // Cholinesterase synonyms
    expect(MinistryExaminerEngine.isKeywordPresent('hydrolysis by AChE at the synapse', 'cholinesterase')).toBe(true);
    expect(MinistryExaminerEngine.isKeywordPresent('degradation by acetylcholinesterase', 'cholinesterase')).toBe(true);

    // ATP synonym
    expect(MinistryExaminerEngine.isKeywordPresent('hydrolysis of adenosine triphosphate', 'atp')).toBe(true);

    // Cross-bridge synonym (transverse hooks)
    expect(MinistryExaminerEngine.isKeywordPresent('formation of transverse hooks pulling actin', 'cross bridges')).toBe(true);

    // T-Helper & Cytotoxic markers
    expect(MinistryExaminerEngine.isKeywordPresent('activation of TH cells with CD4 receptors', 't helper')).toBe(true);
    expect(MinistryExaminerEngine.isKeywordPresent('lysis of cancer cells by CD8+ TC lymphocytes', 'cytotoxic')).toBe(true);

    // Repolarization / resting potential
    expect(MinistryExaminerEngine.isKeywordPresent('restoration of resting potential across sarcolemma', 'repolarization')).toBe(true);

    // DNA replication enzymes
    expect(MinistryExaminerEngine.isKeywordPresent('the enzyme that unzips the double helix', 'helicase')).toBe(true);
    expect(MinistryExaminerEngine.isKeywordPresent('synthesis of fragments on the lagging strand', 'okazaki')).toBe(true);
  });

  it('appends remediation guidance to feedback when remediationNotes are provided', () => {
    const studentAnswer = 'Because ATP is needed to relax the muscle.';
    const result = MinistryExaminerEngine.gradeStudentAnswerLocally(sampleExamItem, studentAnswer);

    expect(result.examinerFeedback).toContain('Remediation Focus:');
    expect(result.examinerFeedback).toContain(sampleExamItem.remediationNotes);
    expect(result.remediationAdvice).toBe(sampleExamItem.remediationNotes);
  });
});

describe('MinistryExaminerEngine Offline Resilience & Fallback', () => {
  const sampleExamItem: MinistryExamItem = {
    id: 'test_offline_item',
    questionType: 'GIVE_REASON',
    prompt: 'Give Reason: Cutin deposition on leaves preserves turgor pressure.',
    maxScore: 2.0,
    requiredKeywords: ['cutin', 'impermeable', 'transpiration', 'turgor'],
    partialCreditKeywords: ['water loss'],
    modelAnswer: 'Cutin is an impermeable substance preventing transpiration, indirectly sustaining turgor.',
    remediationNotes: 'Highlight both structural and physiological aspects.',
  };

  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it('falls back seamlessly to local grading when network fetch throws or server is offline', async () => {
    // Mock global fetch rejection (e.g. offline device)
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new Error('Network unreachable')));

    const studentAnswer = 'Cutin is impermeable to water, reducing transpiration and keeping turgor pressure.';
    const result = await MinistryExaminerEngine.gradeWithAIOrFallback(sampleExamItem, studentAnswer);

    expect(result).toBeDefined();
    expect(result.gradeStatus).toBe('FULL_MARK');
    expect(result.scoreAwarded).toBe(2.0);
    expect(result.detectedKeywords).toContain('cutin');
    expect(result.detectedKeywords).toContain('impermeable');
    expect(result.detectedKeywords).toContain('transpiration');
    expect(result.detectedKeywords).toContain('turgor');
  });

  it('uses remote AI response when server returns valid scoreAwarded payload', async () => {
    const mockAiResponse = {
      scoreAwarded: 1.8,
      detectedKeywords: ['cutin', 'transpiration', 'turgor'],
      missingKeywords: ['impermeable'],
      examinerFeedback: 'AI Ministry Examiner: Strong reasoning, slight penalty for omitting impermeability.',
      gradeStatus: 'PARTIAL_CREDIT',
      remediationAdvice: 'Mention impermeability.',
    };

    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue({
        ok: true,
        json: async () => mockAiResponse,
      })
    );

    const studentAnswer = 'Cutin stops transpiration and protects turgor.';
    const result = await MinistryExaminerEngine.gradeWithAIOrFallback(sampleExamItem, studentAnswer);

    expect(result.scoreAwarded).toBe(1.8);
    expect(result.gradeStatus).toBe('PARTIAL_CREDIT');
    expect(result.examinerFeedback).toContain('AI Ministry Examiner');
  });
});
