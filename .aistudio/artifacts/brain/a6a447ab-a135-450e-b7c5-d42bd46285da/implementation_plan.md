# Canonical Hormonal Coordination MasterStudyPack Verbatim Insertion

Integrate the user's exact, canonical `hormonal_coordination_endocrine` MasterStudyPack entry verbatim into `STUDY_PACKS` inside `src/services/curriculumRepository.ts`, preserving all counts, questions (`hce_q1` to `hce_q8`), and flashcards (`hce_fc1` to `hce_fc6`) without modification, followed by automated verification.

## User Review & Critical Decisions

> [!IMPORTANT]
> The following decisions were clarified and confirmed in Phase 1:
> - **Verbatim Insertion**: Replace the existing `hormonal_coordination_endocrine` pack in `src/services/curriculumRepository.ts:647-917` verbatim with the user's exact provided text without paraphrasing or changing any counts.
> - **Automated Validation**: Run `npm test` and platform compilation checks (`compile_applet`, `lint_applet`) following the replacement to ensure 100% integrity across all 5 test suites.

---

## 1. Overview & Content Structure

The provided pack conforms strictly to `MasterStudyPack` (`src/types/index.ts:166-194`) and official Egyptian Ministry curriculum standards:
- **Topic Identifiers**: `topicId: 'hormonal_coordination_endocrine'`, `topicTitle: 'Plant Auxins & the Human Endocrine System'`, `unit: 'UNIT_1'`, `chapterTitle: 'Chapter 2: Hormonal Coordination'`.
- **Textbook Page Reference**: `'Ministry Textbook Unit 1, Chapter 2 (page numbers to be filled from the printed book)'`.
- **Technical Breakdown**:
  1. *Auxins (Plant Hormones)*: IAA, apical meristems, phototropism, apical dominance, parthenocarpy.
  2. *Endocrine System*: Ductless glands, steroids vs amino-acid based hormones, negative feedback.
  3. *Pituitary Gland*: Anterior lobe (GH, TSH, ACTH, FSH, LH, prolactin) and disorders (dwarfism, gigantism, acromegaly); Posterior lobe (hypothalamic ADH and oxytocin storage/release).
  4. *Thyroid & Parathyroids*: Thyroxine, simple/exophthalmic goiter, cretinism, myxedema; Calcitonin vs Parathormone (PTH) calcium regulation and tetany.
  5. *Adrenal Glands*: Cortex (cortisol, aldosterone, sex hormones) vs Medulla (adrenaline, noradrenaline).
  6. *Pancreas*: Beta cells (insulin), alpha cells (glucagon), diabetes mellitus vs diabetes insipidus.
- **Formulas**: Blood glucose homeostasis & blood calcium homeostasis loops.
- **Comparison Tables**: Anterior vs Posterior Pituitary, Calcitonin vs Parathormone, Insulin vs Glucagon.
- **Bloom Taxonomy**: All 6 levels (Remembering through Creating).
- **Questions**: 8 questions (`hce_q1` to `hce_q8`) covering scientific term, give reason, what happens if, diagram analysis, and MCQ.
- **Flashcards**: 6 flashcards (`hce_fc1` to `hce_fc6`).
- **Exam Simulation**: Official 2-mark item on exophthalmic goiter.

---

## 2. Implementation Steps

1. **Replace `src/services/curriculumRepository.ts:647-917`**:
   - Use `replace_file_content` to insert the provided `hormonal_coordination_endocrine` block verbatim.
2. **Execute Full Test & Compile Verification**:
   - Run `npm test` (all 36 unit tests).
   - Co-execute `compile_applet` and `lint_applet`.
   - Confirm dev server health on port 3000.
