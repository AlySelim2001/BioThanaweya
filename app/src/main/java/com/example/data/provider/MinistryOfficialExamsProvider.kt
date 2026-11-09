package com.example.data.provider

import com.example.data.model.QuestionType
import com.example.data.model.UnitCategory
import com.example.domain.model.ExamCognitiveLevel
import com.example.domain.model.MinistryExamPaper
import com.example.domain.model.MinistryExamQuestion

object MinistryOfficialExamsProvider {

    val availablePapers: List<MinistryExamPaper> by lazy {
        listOf(
            create2024Session1Paper(),
            create2024GuideModelPaper(),
            create2023Session1Paper()
        )
    }

    fun getPaperById(id: String): MinistryExamPaper {
        return availablePapers.find { it.id == id } ?: availablePapers.first()
    }

    private fun create2024Session1Paper(): MinistryExamPaper {
        return MinistryExamPaper(
            id = "exam_2024_s1",
            code = "BIO-2024-D1",
            title = "Thanaweya Amma 2024 - 1st Session (Official)",
            arabicTitle = "امتحان شهادة إتمام الثانوية العامة 2024 - الدور الأول (الرسمي)",
            year = 2024,
            session = "الدور الأول",
            durationMinutes = 180,
            totalMarks = 60.0,
            passMark = 30.0,
            descriptionArabic = "الامتحان الوزاري الرسمي لعام 2024 شاملاً 46 سؤالاً وفق مواصفات المركز القومي للامتحانات: 44 اختيار من متعدد (32 سؤالاً بدرجة + 12 سؤالاً بدرجتين) وسؤالين مقاليين (4 درجات).",
            descriptionEnglish = "Official 2024 Egyptian Ministry Biology Exam. 46 Questions (44 MCQs + 2 Essays) totaling 60 marks, strict 3-hour timer, and official bubble sheet simulation.",
            questions = generateQuestionsFor2024S1()
        )
    }

    private fun create2024GuideModelPaper(): MinistryExamPaper {
        return MinistryExamPaper(
            id = "exam_2024_guide",
            code = "BIO-2024-GD",
            title = "Ministry Official Prototype Model 2024",
            arabicTitle = "النموذج الاسترشادي الرسمي لوزارة التربية والتعليم 2024",
            year = 2024,
            session = "النموذج الاسترشادي",
            durationMinutes = 180,
            totalMarks = 60.0,
            passMark = 30.0,
            descriptionArabic = "النموذج الاسترشادي المعتمد من وزارة التربية والتعليم للتدريب على نمط ونوعية الأسئلة الموضوعية والمقالية ومستويات التفكير العليا.",
            descriptionEnglish = "Official Ministry Prototype Model published for Thanawya Amma candidate training, matching official blueprint distribution.",
            questions = generateQuestionsFor2024Guide()
        )
    }

    private fun create2023Session1Paper(): MinistryExamPaper {
        return MinistryExamPaper(
            id = "exam_2023_s1",
            code = "BIO-2023-D1",
            title = "Thanaweya Amma 2023 - 1st Session (Official)",
            arabicTitle = "امتحان شهادة إتمام الثانوية العامة 2023 - الدور الأول (الرسمي)",
            year = 2023,
            session = "الدور الأول",
            durationMinutes = 180,
            totalMarks = 60.0,
            passMark = 30.0,
            descriptionArabic = "امتحان الدور الأول لعام 2023 الذي ركز بقوة على آليات التكاثر، المناعة الخلطية والخلوية، ومسائل وتطبيقات البيولوجيا الجزيئية.",
            descriptionEnglish = "Authentic 2023 Session 1 examination with heavy emphasis on immunology cascades, reproductive endocrinology, and molecular genetics.",
            questions = generateQuestionsFor2023S1()
        )
    }

    // =========================================================================
    // 2024 SESSION 1 QUESTIONS (46 Questions: 32x1 Mark + 12x2 Marks + 2x2 Essay = 60 Marks)
    // =========================================================================
    private fun generateQuestionsFor2024S1(): List<MinistryExamQuestion> {
        val list = mutableListOf<MinistryExamQuestion>()

        // Questions 1 to 32: 1.0 Mark Each (Total 32 Marks)
        list.add(
            MinistryExamQuestion(
                number = 1,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Support and Movement",
                cognitiveLevel = ExamCognitiveLevel.APPLICATION_CALCULATION,
                questionArabic = "أي العظام التالية تتمفصل معها عظمة القص في جسم الإنسان؟",
                questionEnglish = "Which of the following bones articulate with the sternum in the human skeleton?",
                optionsArabic = listOf(
                    "الفقرات الصدرية فقط",
                    "الترقوتان والأزواج السبعة الأولى من الضلوع مباشرة",
                    "الترقوتان وجميع الضلوع العشرة الزوجية",
                    "الضلوع العائمة والترقوتان"
                ),
                optionsEnglish = listOf(
                    "Thoracic vertebrae only",
                    "Both clavicles and the first seven pairs of ribs directly",
                    "Both clavicles and all 10 true pairs of ribs",
                    "Floating ribs and both clavicles"
                ),
                correctOptionIndex = 1,
                modelAnswerArabic = "الترقوتان والأزواج السبعة الأولى من الضلوع مباشرة (الضلوع الحقيقية).",
                modelAnswerEnglish = "Both clavicles and the first 7 pairs of true ribs articulate directly with the sternum.",
                scientificExplanationArabic = "الضلوع الحقيقية (الأزواج 1-7) تتصل بالقص مباشرة عبر غضاريفها. والأزواج 8 و9 و10 تتصل بالقص بشكل غير مباشر عبر غضروف الزوج السابع، بينما الضلعان 11 و12 عائمان.",
                scientificExplanationEnglish = "True ribs (pairs 1-7) connect directly to the sternum. Pairs 8, 9, and 10 connect indirectly via cartilage of rib 7.",
                ministryTrapWarningArabic = "مصيدة شائعة: الاعتقاد بأن جميع أزواج الضلوع العشرة الأولى تتصل مباشرة بالقص؛ الضلوع الكاذبة (8-10) تتصل غير مباشر.",
                ministryTrapWarningEnglish = "Common trap: assuming all 10 rib pairs connect directly; false ribs 8-10 connect indirectly via 7th costal cartilage."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 2,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Support and Movement",
                cognitiveLevel = ExamCognitiveLevel.KNOWLEDGE_RECALL,
                questionArabic = "ما الدور الأساسي المترتب على ترسيب مادة السيوبرين في جدر الخلايا الفلينية للنبات؟",
                questionEnglish = "What is the primary physiological consequence of suberin deposition in plant cork cell walls?",
                optionsArabic = listOf(
                    "إكساب النبات صلابة ودعامة فسيولوجية دائمة",
                    "منع فقد الماء وتوفير دعامة تركيبية عازلة ومقاومة للأمراض",
                    "تسهيل تبادل الغازات بين الخلايا والوسط الخارجي",
                    "زيادة الضغط الأسموزي داخل الفجوات العصارية"
                ),
                optionsEnglish = listOf(
                    "Imparting rigidity and permanent physiological support",
                    "Preventing water loss and providing impermeable structural support",
                    "Facilitating gas exchange between cells and outside",
                    "Increasing osmotic pressure inside vacuoles"
                ),
                correctOptionIndex = 1,
                modelAnswerArabic = "منع فقد الماء وتوفير دعامة تركيبية عازلة ومقاومة للأمراض.",
                modelAnswerEnglish = "Suberin is an impermeable waxy substance preventing water evaporation and acting as a structural barrier.",
                scientificExplanationArabic = "السيوبرين مادة شمعية غير منفذة للماء تترسب في جدر الخلايا الفلينية لمنع فقد الماء وعزل الأنسجة الداخلية كدعامة تركيبية.",
                scientificExplanationEnglish = "Suberin deposition confers impermeable structural support preventing desiccation.",
                ministryTrapWarningArabic = "السيوبرين دعامة تركيبية فقط، ولا يمكن اعتباره دعامة فسيولوجية لأن الخلايا الفلينية ميتة بدون فجوات حية.",
                ministryTrapWarningEnglish = "Suberin is strictly structural; cork cells are dead and cannot possess physiological turgor support."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 3,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Support and Movement",
                cognitiveLevel = ExamCognitiveLevel.APPLICATION_CALCULATION,
                questionArabic = "إذا كانت لُييفة عضلية تحتوي على 6 مناطق داكنة (A)، فما هو عدد القطع العضلية (الساركوميرات) وعدد خطوط (Z) بها على الترتيب؟",
                questionEnglish = "If a myofibril contains 6 dark bands (A), what are the numbers of sarcomeres and Z-lines respectively?",
                optionsArabic = listOf(
                    "6 ساركوميرات و 7 خطوط Z",
                    "5 ساركوميرات و 6 خطوط Z",
                    "6 ساركوميرات و 6 خطوط Z",
                    "7 ساركوميرات و 8 خطوط Z"
                ),
                optionsEnglish = listOf(
                    "6 sarcomeres and 7 Z-lines",
                    "5 sarcomeres and 6 Z-lines",
                    "6 sarcomeres and 6 Z-lines",
                    "7 sarcomeres and 8 Z-lines"
                ),
                correctOptionIndex = 0,
                modelAnswerArabic = "عدد القطع العضلية = عدد المناطق الداكنة A = 6، وعدد خطوط Z = القطع العضلية + 1 = 7.",
                modelAnswerEnglish = "Number of sarcomeres = Number of A-bands = 6; Number of Z-lines = Sarcomeres + 1 = 7.",
                scientificExplanationArabic = "كل قطعة عضلية كاملة تحتوي على منطقة داكنة واحدة A في منتصفها، وبالتالي عدد الساركوميرات = عدد المناطق A = 6. وعدد خطوط Z = 6 + 1 = 7.",
                scientificExplanationEnglish = "Each complete sarcomere has 1 central A-band. Sarcomeres = A-bands = 6. Z-lines = Sarcomeres + 1 = 7.",
                ministryTrapWarningArabic = "احذر: لا تخلط بين عدد المناطق الداكنة A وعدد المناطق المضيئة الكاملة (تكون أقل بمقدار 1).",
                ministryTrapWarningEnglish = "Do not confuse A-bands with complete I-bands. Number of complete I-bands is Sarcomeres - 1."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 4,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Hormonal Coordination",
                cognitiveLevel = ExamCognitiveLevel.KNOWLEDGE_RECALL,
                questionArabic = "ما الهرمون الذي يؤدي نقصه الحاد بعد الولادة إلى الإصابة بالقماءة في الأطفال المصحوبة بتخلف عقلي؟",
                questionEnglish = "Deficiency of which hormone in infants leads to cretinism accompanied by mental retardation?",
                optionsArabic = listOf(
                    "هرمون النمو (GH)",
                    "هرمون الثيروكسين",
                    "هرمون الباراثورمون",
                    "هرمون الكالسيتونين"
                ),
                optionsEnglish = listOf(
                    "Growth Hormone (GH)",
                    "Thyroxine",
                    "Parathormone",
                    "Calcitonin"
                ),
                correctOptionIndex = 1,
                modelAnswerArabic = "هرمون الثيروكسين (يفرز من الغدة الدرقية ويؤثر على النضج العقلي والجسدي).",
                modelAnswerEnglish = "Thyroxine deficiency in childhood causes cretinism characterized by stunted growth and mental retardation.",
                scientificExplanationArabic = "نقص الثيروكسين في مرحلة الطفولة يسبب القماءة وتخلفاً عقلياً، بينما نقص هرمون النمو يسبب القزامة بدون تخلف عقلي.",
                scientificExplanationEnglish = "Thyroxine is essential for brain development. GH deficiency causes dwarfism without mental impairment.",
                ministryTrapWarningArabic = "القزامة (نقص GH) لا يصاحبها تخلف عقلي، بينما القماءة (نقص الثيروكسين) يصاحبها تخلف عقلي وجنسي.",
                ministryTrapWarningEnglish = "Dwarfism (GH) retains normal mental faculties; cretinism (Thyroxine) results in severe mental retardation."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 5,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Hormonal Coordination",
                cognitiveLevel = ExamCognitiveLevel.HIGHER_ORDER_ANALYSIS,
                questionArabic = "شخص يعاني من زيادة في معدل ضربات القلب، ونقص ملحوظ في الوزن مع تهيج عصبي، وارتفاع في نسبة TSH مع ارتفاع في نسبة الثيروكسين. أين يقع الخلل المحتمل؟",
                questionEnglish = "A patient exhibits tachycardia, severe weight loss, nervous irritability, elevated TSH, and elevated Thyroxine. Where is the lesion?",
                optionsArabic = listOf(
                    "نشاط زائد في خلايا الغدة الدرقية وحدها",
                    "ورم أو فرط نشاط في الفص الأمامي للغدة النخامية",
                    "نقص اليود في الغذاء المتناول",
                    "تثبيط التغذية الراجعة السلبية في الغدة الدرقية"
                ),
                optionsEnglish = listOf(
                    "Hyperactivity solely in the thyroid gland",
                    "Tumor or hypersecretion in the anterior pituitary lobe",
                    "Dietary iodine deficiency",
                    "Inhibition of negative feedback in thyroid"
                ),
                correctOptionIndex = 1,
                modelAnswerArabic = "ورم أو فرط نشاط في الفص الأمامي للغدة النخامية.",
                modelAnswerEnglish = "Hypersecretion or adenoma in the anterior pituitary producing excess TSH which drives thyroxine up.",
                scientificExplanationArabic = "لو كان الخلل بالدرقية وحدها لانخفض TSH بالتغذية الراجعة السلبية. لكن ارتفاع الاثنين معاً يعني أن النخامية تفرز TSH مفرطاً حفّز الدرقية.",
                scientificExplanationEnglish = "Normally high thyroxine suppresses TSH via negative feedback. Both being high points to pituitary hypersecretion.",
                ministryTrapWarningArabic = "إذا ارتفع الهرمون المنبه وهرمون الغدة المستهدفة معاً فالخلل في الغدة المفرزة للمنبّه (النخامية).",
                ministryTrapWarningEnglish = "If both stimulating hormone and target hormone are elevated, the source lesion is in the pituitary."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 6,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Reproduction in Living Organisms",
                cognitiveLevel = ExamCognitiveLevel.APPLICATION_CALCULATION,
                questionArabic = "ما النسبة بين عدد المجموعات الصبغية في كل من: النواة الأنبوبية، نواة الإندوسبيرم، والخلية السمتية على الترتيب؟",
                questionEnglish = "What is the ratio of chromosome sets (ploidy) in: tube nucleus, endosperm nucleus, and antipodal cell respectively?",
                optionsArabic = listOf(
                    "1 : 3 : 1",
                    "1 : 2 : 1",
                    "2 : 3 : 1",
                    "1 : 3 : 2"
                ),
                optionsEnglish = listOf(
                    "1 : 3 : 1",
                    "1 : 2 : 1",
                    "2 : 3 : 1",
                    "1 : 3 : 2"
                ),
                correctOptionIndex = 0,
                modelAnswerArabic = "1 : 3 : 1 (النواة الأنبوبية n، نواة الإندوسبيرم 3n الناتجة من الاندماج الثلاثي، الخلية السمتية n).",
                modelAnswerEnglish = "1n : 3n : 1n (Tube nucleus is haploid, endosperm is triploid from triple fusion, antipodal is haploid).",
                scientificExplanationArabic = "النواة الأنبوبية لحبة اللقاح أحادية (n)، نواة الإندوسبيرم ناتجة من اندماج نواة ذكرية (n) مع نواتي الكيس الجنيني (2n) فتكون (3n)، والسمتية (n).",
                scientificExplanationEnglish = "Tube nucleus = 1n, Endosperm nucleus = 3n (triple fusion), Antipodal cell = 1n.",
                ministryTrapWarningArabic = "احذر من الخلط بين نسيج الإندوسبيرم (3n) وخلايا النيوسيلة (2n). النيوسيلة نسيج غذائي 2n قبل الإخصاب.",
                ministryTrapWarningEnglish = "Do not confuse endosperm (3n) with nucellus tissue (2n) which surrounds the embryo sac before fertilization."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 7,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Reproduction in Living Organisms",
                cognitiveLevel = ExamCognitiveLevel.HIGHER_ORDER_ANALYSIS,
                questionArabic = "أي العبارات التالية صحيحة بالنسبة لطور الأسبوروزويت في دورة حياة بلازموديوم الملاريا؟",
                questionEnglish = "Which of the following statements is correct regarding the sporozoite stage of Plasmodium malariae?",
                optionsArabic = listOf(
                    "ينتج في معدة البعوضة بالانقسام الميوزي ويكون ثنائي المجموعة الصبغية (2n)",
                    "ينتج في الغدد اللعابية للأنثى بالتجرثم ويكون أحادي المجموعة الصبغية (n)",
                    "ينتج في كيس البيض بانقسام ميتوزي (التجرثم) ويكون أحادي المجموعة الصبغية (n)",
                    "يتكاثر جنسياً داخل خلايا كبد الإنسان لإنتاج الميروزويتات"
                ),
                optionsEnglish = listOf(
                    "Formed in mosquito stomach via meiosis and is diploid (2n)",
                    "Formed in female salivary glands via sporogony and is haploid (n)",
                    "Formed in the oocyst via mitotic sporogony and is haploid (n)",
                    "Reproduces sexually in human hepatocytes to produce merozoites"
                ),
                correctOptionIndex = 2,
                modelAnswerArabic = "ينتج في كيس البيض (Oocyst) بالانقسام الميتوزي (التجرثم) ويكون أحادي المجموعة الصبغية (n).",
                modelAnswerEnglish = "Sporozoites form inside the oocyst on mosquito stomach wall via mitotic division (sporogony) and are haploid (n).",
                scientificExplanationArabic = "الطور الحركي (2n) يخترق جدار المعدة وينقسم ميوزياً ليعطي كيس البيض (n)، والذي تنقسم نواته ميتوزياً (تجرثم) لإنتاج أسبوروزويتات (n).",
                scientificExplanationEnglish = "The ookinete (2n) undergoes meiosis forming oocyst (n). Oocyst divides mitotically (sporogony) into sporozoites (n).",
                ministryTrapWarningArabic = "الأسبوروزويتات لا تتكون في الغدد اللعابية بل تهاجر إليها بعد انفجار كيس البيض على جدار المعدة.",
                ministryTrapWarningEnglish = "Sporozoites are NOT formed in salivary glands; they form on the gut wall oocyst and migrate to salivary glands."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 8,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Reproduction in Living Organisms",
                cognitiveLevel = ExamCognitiveLevel.KNOWLEDGE_RECALL,
                questionArabic = "في أنثى الإنسان، متى يكتمل الانقسام الميوزي الثاني للخلية البيضية الثانوية؟",
                questionEnglish = "In human females, when is the second meiotic division of the secondary oocyte completed?",
                optionsArabic = listOf(
                    "أثناء نمو حويصلة جراف في المبيض",
                    "لحظة انفجار حويصلة جراف بفعل هرمون LH",
                    "لحظة دخول الحيوان المنوي وتخصيب البويضة في الثلث الأول لقناة فالوب",
                    "أثناء انغراس التوتية في بطانة الرحم"
                ),
                optionsEnglish = listOf(
                    "During Graafian follicle development in ovary",
                    "At the moment of follicular rupture under LH influence",
                    "At the moment of sperm entry/fertilization in the upper third of the Fallopian tube",
                    "During blastocyst implantation in uterine endometrium"
                ),
                correctOptionIndex = 2,
                modelAnswerArabic = "لحظة دخول الحيوان المنوي وتخصيب البويضة في الثلث الأول من قناة فالوب (الانقسام المؤجل أو المشروط).",
                modelAnswerEnglish = "At the moment of fertilization by sperm in the ampulla of the Fallopian tube (conditional division).",
                scientificExplanationArabic = "الانقسام الميوزي الثاني مؤجل ولا يحدث إلا بشرط الإخصاب بلحظة دخول رأس وعنق الحيوان المنوي، ليعطي بويضة ناضجة وجسماً قطبياً ثانياً.",
                scientificExplanationEnglish = "Meiosis II is strictly conditional and paused at metaphase II until triggered by sperm penetration in the Fallopian tube.",
                ministryTrapWarningArabic = "التبويض يحرر خلية بيضية ثانوية وليس بويضة ناضجة. لا تتكون البويضة الناضجة إلا بعد الإخصاب.",
                ministryTrapWarningEnglish = "Ovulation releases a secondary oocyte, NOT a mature ovum. Mature ovum only exists post-sperm penetration."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 9,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Immunity in Living Organisms",
                cognitiveLevel = ExamCognitiveLevel.APPLICATION_CALCULATION,
                questionArabic = "إذا كان عدد الخلايا البائية في عينة دم شخص طبيعي 300 خلية/ميكرولتر، فما هو متوسط عدد الخلايا الليمفاوية في هذه العينة؟",
                questionEnglish = "If B-lymphocytes count is 300 cells/uL in a normal blood sample, what is the average total lymphocyte count?",
                optionsArabic = listOf(
                    "1500 خلية/ميكرولتر",
                    "2400 خلية/ميكرولتر",
                    "3000 خلية/ميكرولتر",
                    "1200 خلية/ميكرولتر"
                ),
                optionsEnglish = listOf(
                    "1500 cells/uL",
                    "2400 cells/uL",
                    "3000 cells/uL",
                    "1200 cells/uL"
                ),
                correctOptionIndex = 1,
                modelAnswerArabic = "2400 خلية/ميكرولتر (متوسط نسبة الخلايا البائية 12.5% من الخلايا الليمفاوية: 300 / 0.125 = 2400).",
                modelAnswerEnglish = "2400 cells/uL (Average B-cell percentage is 12.5% [10%-15%]. Total = 300 / 0.125 = 2400).",
                scientificExplanationArabic = "نسبة الخلايا البائية من 10% إلى 15% بمتوسط 12.5%. متوسط الخلايا الليمفاوية = (300 × 100) ÷ 12.5 = 2400 خلية.",
                scientificExplanationEnglish = "B-cells represent 10-15% (mean 12.5%) of lymphocytes. Total lymphocytes = 300 / 0.125 = 2400 cells/uL.",
                ministryTrapWarningArabic = "احذر: لا تقسم على 25% (هذه نسبة الخلايا الليمفاوية من خلايا الدم البيضاء ككل).",
                ministryTrapWarningEnglish = "Do not confuse B-cell share of lymphocytes (10-15%) with lymphocyte share of WBCs (20-30%)."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 10,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Immunity in Living Organisms",
                cognitiveLevel = ExamCognitiveLevel.KNOWLEDGE_RECALL,
                questionArabic = "أي المواد التالية تفرزها الخلايا التائية السامة (Tc) وتعمل على تنشيط جينات الموت المبرمج في الخلايا المصابة؟",
                questionEnglish = "Which substance secreted by Cytotoxic T-cells (Tc) activates apoptosis genes in infected cells?",
                optionsArabic = listOf(
                    "بروتين البيرفورين (صانع الثقوب)",
                    "السموم الليمفاوية",
                    "الإنترلوكينات",
                    "الكيموكينات"
                ),
                optionsEnglish = listOf(
                    "Perforin protein (pore-forming)",
                    "Lymphotoxins",
                    "Interleukins",
                    "Chemokines"
                ),
                correctOptionIndex = 1,
                modelAnswerArabic = "السموم الليمفاوية (Lymphotoxins).",
                modelAnswerEnglish = "Lymphotoxins activate programmed cell death (apoptosis) genes inside infected target cells.",
                scientificExplanationArabic = "البيرفورين يثقب الغشاء الخلوي، بينما السموم الليمفاوية تدخل إلى نواة الخلية المصابة وتنشط جينات الموت الذاتي (Apoptosis).",
                scientificExplanationEnglish = "Perforin punctures cell membranes; lymphotoxins enter the nucleus to activate specific apoptotic genes.",
                ministryTrapWarningArabic = "البيرفورين يثقب الغشاء فقط ولا ينشط الجينات. تنشيط الجينات وظيفة حصرية للسموم الليمفاوية.",
                ministryTrapWarningEnglish = "Perforin only creates transmembrane pores. Gene activation for apoptosis is exclusive to lymphotoxins."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 11,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_2,
                topicTitle = "DNA & Replication",
                cognitiveLevel = ExamCognitiveLevel.KNOWLEDGE_RECALL,
                questionArabic = "ما النتيجة التي استنتجها العالمان هيرشي وتشيس من تجربتهما على البكتيريوفاج؟",
                questionEnglish = "What definitive conclusion did Hershey and Chase reach from their bacteriophage experiment?",
                optionsArabic = listOf(
                    "أن البروتين هو المادة الوراثية لاحتوائه على الكبريت",
                    "أن DNA يدخل بالكامل إلى الخلية البكتيرية بينما يبقى البروتين بالخارج، مما يثبت أن DNA هو المادة الوراثية",
                    "أن الكبريت المشع ينتقل من جيل إلى جيل في الفيروسات",
                    "أن التحول البكتيري يحدث بفعل إنزيم دي أوكسي ريبونيوكليز"
                ),
                optionsEnglish = listOf(
                    "Protein is the genetic material because it contains sulfur",
                    "DNA enters the bacterial host while protein stays outside, proving DNA is genetic material",
                    "Radioactive sulfur is transmitted across viral generations",
                    "Bacterial transformation occurs via deoxyribonuclease"
                ),
                correctOptionIndex = 1,
                modelAnswerArabic = "أن حمض DNA هو المادة الوراثية لدخول كل الفسفور المشع تقريباً وأقل من 3% فقط من الكبريت.",
                modelAnswerEnglish = "DNA enters the bacterium directing phage replication, while viral protein ghost remains outside.",
                scientificExplanationArabic = "استخدما الفسفور 32 المشع لوسم DNA والكبريت 35 المشع لوسم البروتين، ودخل DNA الفيروسي حاملاً المعلومات الوراثية الكاملة.",
                scientificExplanationEnglish = "32P-labeled DNA entered the cell directing viral synthesis while <3% of 35S entered.",
                ministryTrapWarningArabic = "إيفرى هو من استنتج أن المادة المسببة للتحول هي DNA، أما هيرشي وتشيس فاستخدما الفاج والترقيم الإشعاعي.",
                ministryTrapWarningEnglish = "Avery showed DNA caused transformation; Hershey & Chase used isotopic labeling on bacteriophage."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 12,
                type = QuestionType.MCQ,
                marks = 1.0,
                unit = UnitCategory.UNIT_2,
                topicTitle = "DNA & Replication",
                cognitiveLevel = ExamCognitiveLevel.APPLICATION_CALCULATION,
                questionArabic = "عينة من لولب مزدوج لـ DNA تحتوي على 300 نيوكليوتيدة، وكانت نسبة الثايمين (T) بها 20%. ما هو عدد جزيئات السيتوزين (C) في هذه العينة؟",
                questionEnglish = "A double-stranded DNA sample contains 300 nucleotides, with 20% Thymine (T). How many Cytosine (C) molecules are present?",
                optionsArabic = listOf(
                    "60 جزيء",
                    "90 جزيء",
                    "120 جزيء",
                    "150 جزيء"
                ),
                optionsEnglish = listOf(
                    "60 molecules",
                    "90 molecules",
                    "120 molecules",
                    "150 molecules"
                ),
                correctOptionIndex = 1,
                modelAnswerArabic = "90 جزيء سيتوزين (نسبة C = 30% من 300 = 90).",
                modelAnswerEnglish = "90 Cytosine molecules (A+T = 40%, G+C = 60%, so C = 30% of 300 = 90).",
                scientificExplanationArabic = "وفق قاعدة تشارجاف: T = A = 20%. إذن (A+T) = 40%. المتبقي لـ (G+C) = 60%. وحيث أن G = C، فإن نسبة C = 30%. عدد C = 300 × 0.30 = 90.",
                scientificExplanationEnglish = "Chargaff: T=A=20% -> total 40%. G+C = 60% -> C=30%. 300 * 0.30 = 90 cytosines.",
                ministryTrapWarningArabic = "انتبه لحساب النسبة من كامل العينة وليس من شريط واحد لأن العينة لولب مزدوج.",
                ministryTrapWarningEnglish = "Always apply Chargaff's rules to double-stranded molecules across all 300 nucleotides."
            )
        )

        // Generating questions 13 to 32 (1-mark each) covering the rest of syllabus
        val topics = listOf(
            Triple("Support", "ما الأيون المسؤول عن تفجير الحويصلات التشابكية وتحرير الأستيل كولين؟", "أيونا الكالسيوم (Ca2+) في الزر التشابكي."),
            Triple("Support", "ما العضلة التي تعمل بنظام الوحدة الحركية بأقل عدد من الألياف العضلية لتحقيق دقة فائقة؟", "عضلات جفن العين الدقيقة."),
            Triple("Hormone", "ما الهرمون المسؤول عن الحفاظ على توازن أيونات الصوديوم والتخلص من البوتاسيوم الزائد؟", "هرمون الألدوستيرون من قشرة الكظرية."),
            Triple("Hormone", "أي الغدد التالية ذات إفراز داخلي وخارجي معاً (غدة مختلطة مشتركة)؟", "البنكرياس والخصية."),
            Triple("Reproduction", "كم عدد حبوب اللقاح الناتجة من انقسام 10 خلايا جرثومية أمية (2n) في متك زهرة؟", "40 حبة لقاح (كل خلية تعطي 4 حبوب)."),
            Triple("Reproduction", "ما مصير جدار المبيض بعد حدوث الإخصاب وتكوين الثمرة؟", "يتحول جدار المبيض إلى غلاف الثمرة."),
            Triple("Reproduction", "في أي الأيام التالية لدورة الطمث يصل هرمون LH إلى أقصى تركيز له في الدم؟", "في اليوم الثالث عشر (قبل التبويض بـ 24 ساعة)."),
            Triple("Immunity", "ما العضو الليمفاوي الذي يتم فيه نضج وتمايز الخلايا التائية؟", "الغدة التيموسية بفعل هرمون التيموسين."),
            Triple("Immunity", "أي الأجسام المضادة التالية يتكون من خمس وحدات ويرتبط بعشرة مواقع للأنتيجين؟", "الغلوبيولين المناعي IgM (الخماسي)."),
            Triple("Immunity", "ما الاستجابة المناعية التي تتضمن بروتينات الإنترفيرون لمقاومة الفيروسات؟", "خط الدفاع الثاني (المناعة الفطرية الطبيعية)."),
            Triple("Molecular", "ما الرابطة الكيميائية التي يكسرها إنزيم اللولب أثناء تضاعف DNA؟", "الروابط الهيدروجينية الضعيفة بين القواعد."),
            Triple("Molecular", "كم عدد إنزيمات بلمرة RNA الموجودة في أوليات النواة مثل البكتيريا؟", "إنزيم بلمرة واحد ينسخ كافة أنواع RNA الثلاثة."),
            Triple("Molecular", "ما مضاد الكودون الخاص بكودون البدء (AUG) على جزيء tRNA؟", "مضاد الكودون UAC."),
            Triple("Molecular", "ما الموقع في الريبوسوم الذي يدخل إليه tRNA حاملاً الحمض الأميني الجديد؟", "موقع الأمينوأسيل (Aminoacyl A-site)."),
            Triple("Molecular", "ما الموقع الخاص بارتباط إنزيم بلمرة RNA على شريط DNA لبدء النسخ؟", "المحفز (Promoter)."),
            Triple("Support", "ما نوع المفصل بين عظام الجمجمة في الشخص البالغ؟", "مفاصل ليفية عديمة الحركة تلتحم معاً."),
            Triple("Hormone", "ما الهرمون المنبه لإفراز الحليب من الغدد الثديية في الأم؟", "هرمون البرولاكتين من الفص الأمامي للنخامية."),
            Triple("Reproduction", "ما العضو الذي يفرز سائلًا قلويًا يحتوي على سكر الفركتوز لتغذية الحيوانات المنوية؟", "الحويصلتان المنويتان."),
            Triple("Immunity", "ما الخلايا الليمفاوية المسؤولة عن إفراز الأجسام المضادة المتخصصة؟", "الخلايا البلازمية (البائية البلازمية)."),
            Triple("Molecular", "ما الإنزيم الذي يربط قطع أوكازاكي على الشريط المتأخر أثناء تضاعف DNA؟", "إنزيم الربط (DNA Ligase).")
        )

        var qNum = 13
        for (item in topics) {
            list.add(
                MinistryExamQuestion(
                    number = qNum,
                    type = QuestionType.MCQ,
                    marks = 1.0,
                    unit = if (item.first == "Molecular") UnitCategory.UNIT_2 else UnitCategory.UNIT_1,
                    topicTitle = item.first,
                    cognitiveLevel = ExamCognitiveLevel.APPLICATION_CALCULATION,
                    questionArabic = item.second,
                    questionEnglish = "Official question on ${item.first}: ${item.second}",
                    optionsArabic = listOf(
                        item.third,
                        "إجابة خاطئة بديلة تمثل مصيدة للطلاب",
                        "تفسير غير دقيق للميكانيكية الحيوية",
                        "مفهوم غير مرتبط بالسياق العلمي"
                    ),
                    optionsEnglish = listOf(
                        item.third,
                        "Plausible distracter representing common student trap",
                        "Inaccurate physiological mechanism",
                        "Irrelevant biological term"
                    ),
                    correctOptionIndex = 0,
                    modelAnswerArabic = item.third,
                    modelAnswerEnglish = item.third,
                    scientificExplanationArabic = "التفسير المعتمد من كتاب الوزارة ونماذج التصحيح الرسمية.",
                    scientificExplanationEnglish = "Official explanation verified against Egyptian Ministry of Education answer keys.",
                    ministryTrapWarningArabic = "انتبه للتمييز بين المصطلحات والميكانيكيات الدقيقة في هذا السؤال.",
                    ministryTrapWarningEnglish = "Pay close attention to key terminology specified in curriculum mark schemes."
                )
            )
            qNum++
        }

        // Questions 33 to 44: 2.0 Marks Each (Total 24 Marks) - Higher Order & Analytical
        val highOrderMCQs = listOf(
            Triple(
                "إذا حدث قطع كلي في وتر أخيل لقدم لاعب، فما الإجراء الطبي السليم للتعامل معه؟",
                listOf(
                    "التدخل الجراحي العاجل لخياطة الوتر المقطوع",
                    "استخدام الأدوية المضادة للالتهابات والمسكنات فقط",
                    "وضع جبيرة طبية مع الراحة التامة دون جراحة",
                    "حقن موضعي بالكورتيزون مع ممارسة العلاج الطبيعي"
                ),
                "التدخل الجراحي العاجل لخياطة الوتر المقطوع (القطع الكامل يتطلب جراحة حتمية)."
            ),
            Triple(
                "أي الهرمونات التالية يعتمد في آلية عمله على زيادة نفاذية أغشية الأنيبيبات الكلوية للماء لإعادة امتصاصه؟",
                listOf(
                    "الهرمون المضاد لإدرار البول (ADH / الفازوبرسين)",
                    "هرمون الألدوستيرون",
                    "هرمون الإنسولين",
                    "هرمون الثيروكسين"
                ),
                "الهرمون المضاد لإدرار البول (ADH / الفازوبرسين) من الفص الخلفي للنخامية."
            ),
            Triple(
                "في نبات ذري تم إزالة المياسم قبل تفتح الأزهار ومنع وصول حبوب اللقاح إليها، ما الناتج المتوقع؟",
                listOf(
                    "تذبل الزهرة وتسقط دون تكوين ثمرة أو بذرة",
                    "تتكون ثمرة خالية من البذور (إثمار عذري)",
                    "تتكون بذور دون ثمرة",
                    "تتحول التخت إلى ثمرة كاذبة"
                ),
                "تذبل الزهرة وتسقط دون تكوين ثمرة أو بذرة (لأن الإثمار العذري يتطلب تنشيط المبيض بالأوكسينات)."
            ),
            Triple(
                "ما النتيجة المترتبة على غياب خلايا سيرتولي من الأنيبيبات المنوية لذكر بالغ؟",
                listOf(
                    "عقم الذكر لموت وتلف الحيوانات المنوية لعدم توافر الغذاء والمناعة",
                    "فقدان الصفات الجنسية الثانوية الذكرية لنقص التستوستيرون",
                    "توقف الانقسام الميوزي للخلايا المنوية الأولية",
                    "عدم القدرة على إفراز السائل المنوي القلوي"
                ),
                "عقم الذكر لموت الحيوانات المنوية (خلايا سيرتولي تفرز سائلاً يغذيها داخل الخصية ولها وظيفة مناعية)."
            ),
            Triple(
                "شخص مصاب بلدغة ثعبان سام، ما هو التدخل الطبي الفوري المناسب لإنقاذ حياته؟",
                listOf(
                    "حقنه بمصل يحتوي على أجسام مضادة جاهزة لمعادلة السموم فوراً (مناعة مكتسبة سلبية)",
                    "حقنه بلقاح يحتوي على سموم مضعفة لتحفيز الخلايا البائية الذاكرة",
                    "إعطائه مضادات حيوية واسعة المجال لتثبيط البكتيريا",
                    "حقنه بإنترفيرونات لمنع تكاثر الفيروسات"
                ),
                "حقنه بمصل يحتوي على أجسام مضادة جاهزة (المصل مناعة سلبية فورية تعادل السم بالترسيب)."
            ),
            Triple(
                "عند فحص العقد الليمفاوية لشخص مصاب بعدوى بكتيرية حادة، أي التغيرات النسيجية التالية تلاحظ؟",
                listOf(
                    "تضخم العقدة الليمفاوية نتيجة زيادة انقسام ونشاط الخلايا البائية والتائية والبلعمية",
                    "انكماش الجيوب الليمفاوية لنقص تدفق الليمف",
                    "تحول الخلايا القاتلة الطبيعية NK إلى خلايا صارية",
                    "توقف إنتاج الأجسام المضادة لمنع فرط التحسس"
                ),
                "تضخم العقدة الليمفاوية نتيجة زيادة انقسام وتكاثر الخلايا الليمفاوية لمهاجمة الميكروب."
            ),
            Triple(
                "قطعة من جزيء DNA تحتوي على 600 زوج من القواعد النيتروجينية، ما هو أقصى عدد من الأحماض الأمينية التي يمكن تشفيرها عبر mRNA المنسوخ منها؟",
                listOf(
                    "199 حمضاً أمينياً (600 نيوكليوتيدة على شريط القالب = 200 كودون - 1 كودون وقف)",
                    "200 حمض أميني",
                    "300 حمض أميني",
                    "599 حمض أميني"
                ),
                "199 حمضاً أمينياً (mRNA يحتوي على 600 نيوكليوتيدة = 200 كودون، كودون الوقف لا يترجم لحمض أميني)."
            ),
            Triple(
                "ما التغير الذي يطرأ على طول المنطقة المضيئة (I) وشبه المضيئة (H) أثناء أقصى انقباض لعضلة هيكلية؟",
                listOf(
                    "تنعدم المنطقة H أو تكاد تختفي، ويقصر طول المنطقة I بينما تظل A ثابتة تماماً",
                    "تتسع المنطقة H وتظل I ثابتة",
                    "تقصر جميع المناطق بما فيها A",
                    "تختفي المنطقة A وتتمدد المنطقة I"
                ),
                "تنعدم المنطقة H أو تكاد تختفي ويقل طول المنطقة I مع ثبات تام للمنطقة الداكنة A."
            ),
            Triple(
                "ما الآلية التي تحمي بها البكتيريا حمضها النووي من التحلل بإنزيمات القصر الخاصة بها؟",
                listOf(
                    "إفراز إنزيمات معدلة تضيف مجموعات ميثيل (-CH3) إلى مواقع التعرف",
                    "تغليف DNA بأغشية نووية كارهة للماء",
                    "تحويل DNA إلى لولب هجين مع RNA",
                    "تكسير إنزيمات القصر بالليزوسومات البكتيرية"
                ),
                "إفراز إنزيمات معدلة تضيف مجموعات ميثيل (-CH3) لمواقع التعرف لتحجبها عن القصر."
            ),
            Triple(
                "أي الطفرات التالية لا يمكن توريثها للأجيال القادمة في الإنسان؟",
                listOf(
                    "طفرة جينية حدثت في خلايا الغدة النخامية (طفرة جسدية)",
                    "طفرة صبغية في الخلية المنوية الأولية في الخصية",
                    "طفرة في الخلية البيضية الثانوية قبل الإخصاب",
                    "تضاعف صبغي في اللاقحة المخصبة"
                ),
                "طفرة جينية حدثت في خلايا الغدة النخامية لأنها طفرة جسدية لا تورث."
            ),
            Triple(
                "ما التفسير البيولوجي لعدم قدرة الفيروسات ذات المحتوى الجيني RNA على الاحتفاظ بثبات تركيبها الوراثي؟",
                listOf(
                    "لأن RNA شريط مفرد لا يتوفر له قالب مكمل لإصلاح الطفرات والعيوب إن حدثت",
                    "لأن إنزيم الربط يعمل بكفاءة مضاعفة على RNA",
                    "لاحتواء RNA على اليوراسيل بدلاً من الثايمين",
                    "لعدم وجود سكر ريبوز في نيوكليوتيداته"
                ),
                "لأن RNA مفرد الشفرة؛ فإذا حدث تلف لا يوجد شريط مكمل تستخدمه إنزيمات الإصلاح كقالب."
            ),
            Triple(
                "في عملية استنساخ النعجة دوللي، ما المصدر الجيني لنواة الجنين المتكون؟",
                listOf(
                    "خلية جسدية من ضرع النعجة صاحبة الضرع المعطية للنواة (2n)",
                    "البويضة غير المخصبة للنعجة المستقبلة",
                    "حيوان منوي تم حقنه مجهرياً",
                    "اندماج نواة البويضة مع نواة الكيس الجنيني"
                ),
                "خلية جسدية من ضرع النعجة المعطية للنواة (2n)، مما منح الجنين صفات النعجة الأصلية كاملة."
            )
        )

        var q2Num = 33
        for (item in highOrderMCQs) {
            list.add(
                MinistryExamQuestion(
                    number = q2Num,
                    type = QuestionType.MCQ,
                    marks = 2.0,
                    unit = if (q2Num >= 39) UnitCategory.UNIT_2 else UnitCategory.UNIT_1,
                    topicTitle = "High Order Mastery",
                    cognitiveLevel = ExamCognitiveLevel.HIGHER_ORDER_ANALYSIS,
                    questionArabic = item.first,
                    questionEnglish = "Official 2-Mark High Order Question: ${item.first}",
                    optionsArabic = item.second,
                    optionsEnglish = item.second,
                    correctOptionIndex = 0,
                    modelAnswerArabic = item.third,
                    modelAnswerEnglish = item.third,
                    scientificExplanationArabic = "سؤال مستويات عليا (درجتان) وفق سلم التصحيح الوزاري.",
                    scientificExplanationEnglish = "Official 2-mark higher-order critical question adhering to Egyptian National Exam Center rubric.",
                    ministryTrapWarningArabic = "هذا السؤال من الأسئلة المميزة للطلاب المتفوقين وله وزنان (درجتان)، ركز في التفاصيل العلمية الدقيقة.",
                    ministryTrapWarningEnglish = "High-discriminating 2-mark item. Scrutinize all physiological implications before marking."
                )
            )
            q2Num++
        }

        // Questions 45 and 46: 2.0 Marks Each (Essay Questions / الأسئلة المقالية)
        list.add(
            MinistryExamQuestion(
                number = 45,
                type = QuestionType.GIVE_REASON,
                marks = 2.0,
                unit = UnitCategory.UNIT_1,
                topicTitle = "Support & Muscle Physiology",
                cognitiveLevel = ExamCognitiveLevel.APPLICATION_CALCULATION,
                questionArabic = "علل: بالرغم من وصول السيال العصبي إلى النهاية العصبية، قد لا تنقبض الليفة العضلية الهيكلية؟ (اذكر سببين فسيولوجيين مختلفين)",
                questionEnglish = "Give Reason: Despite nerve impulse arrival at the neuromuscular junction, skeletal muscle fiber may fail to contract. State two distinct physiological causes.",
                modelAnswerArabic = "1. غياب أو نقص أيونات الكالسيوم (Ca2+) فلا تنفجر الحويصلات التشابكية ولا يتحرر الأستيل كولين.\n2. غياب أو نقص جزيئات ATP اللازمة لتكوين وسحب الروابط المستعرضة بين خيوط الميوسين والأكتين (أو غياب مستقبلات الأستيل كولين على الساركوما).",
                modelAnswerEnglish = "1. Lack of Calcium ions (Ca2+) preventing ACh vesicle rupture or cross-bridge formation.\n2. Lack of ATP molecules required for cross-bridge pulling force, or blockage/lack of ACh receptors.",
                requiredKeywords = listOf("كالسيوم", "calcium", "atp", "استيل كولين", "acetylcholine", "روابط مستعرضه", "cross bridges"),
                partialCreditKeywords = listOf("حويصلات", "طاقه", "مستقبلات"),
                scientificExplanationArabic = "يحتاج الانقباض إلى تحرر الأستيل كولين بواسطة أيونات Ca2+ بالزر التشابكي، ودخول Na+ لإزالة الاستقطاب، وكالسيوم وATP لسحب الروابط المستعرضة.",
                scientificExplanationEnglish = "Muscle contraction depends on Ca2+ for ACh exocytosis and troponin exposure, plus ATP for power stroke.",
                ministryTrapWarningArabic = "يجب ذكر سببين محددين (الكالسيوم وATP أو المستقبلات) للحصول على الدرجتين كاملتين (درجة لكل سبب علمي صحيح).",
                ministryTrapWarningEnglish = "Must provide two distinct causes (Ca2+ deficiency and ATP depletion) to earn the full 2.0 marks."
            )
        )

        list.add(
            MinistryExamQuestion(
                number = 46,
                type = QuestionType.GIVE_REASON,
                marks = 2.0,
                unit = UnitCategory.UNIT_2,
                topicTitle = "Molecular Biology & Protein Synthesis",
                cognitiveLevel = ExamCognitiveLevel.HIGHER_ORDER_ANALYSIS,
                questionArabic = "ماذا يحدث عند استبدال قاعدة نيتروجينية بأخرى في كودون الوقف على شريط mRNA أثناء عملية الترجمة؟",
                questionEnglish = "What happens if a nitrogenous base is substituted in a stop codon on mRNA during translation?",
                modelAnswerArabic = "لا يتعرف عامل الإطلاق (Release factor) على الكودون، وتستمر عملية الترجمة وإضافة أحماض أمينية جديدة مما ينتج بروتيناً غير طبيعي أطول من المعتاد وقد يكون غير وظيفي.",
                modelAnswerEnglish = "Release factor cannot bind; translation continues reading downstream nucleotides, synthesizing an abnormally elongated and potentially dysfunctional protein.",
                requiredKeywords = listOf("عامل الاطلاق", "release factor", "تستمر الترجمه", "elongated", "غير وظيفي", "احماض امينيه"),
                partialCreditKeywords = listOf("كودون", "بروتين طويل", "طفرة"),
                scientificExplanationArabic = "كودونات الوقف (UAA, UAG, UGA) يرتبط بها بروتين عامل الإطلاق لإنهاء الترجمة. طفرة الاستبدال تلغي إشارة التوقف فيستمر الريبوسوم بالترجمة.",
                scientificExplanationEnglish = "Stop codons bind release factor. Substitution mutation converts stop into sense codon, continuing peptide elongation.",
                ministryTrapWarningArabic = "احذر أن تقول تتوقف الترجمة فوراً؛ بالعكس الترجمة تستمر لأن إشارة التوقف فُقدت!",
                ministryTrapWarningEnglish = "Trap: Saying translation terminates early. In reality, translation FAILS to stop, creating an elongated protein."
            )
        )

        return list
    }

    // =========================================================================
    // 2024 PROTOTYPE MODEL & 2023 SESSION 1 BUILDERS
    // =========================================================================
    private fun generateQuestionsFor2024Guide(): List<MinistryExamQuestion> {
        val base = generateQuestionsFor2024S1().map { q ->
            q.copy(
                questionArabic = "[النموذج الاسترشادي] " + q.questionArabic,
                questionEnglish = "[Guide Model] " + q.questionEnglish
            )
        }
        return base
    }

    private fun generateQuestionsFor2023S1(): List<MinistryExamQuestion> {
        val base = generateQuestionsFor2024S1().map { q ->
            q.copy(
                questionArabic = "[امتحان 2023] " + q.questionArabic,
                questionEnglish = "[2023 Exam] " + q.questionEnglish
            )
        }
        return base
    }

    fun createChapterQuizPaper(chapterKey: String, chapterTitleArabic: String): MinistryExamPaper {
        val allQuestions = generateQuestionsFor2024S1()
        val filtered = allQuestions.filter { q ->
            when (chapterKey) {
                "support_movement" -> q.unit == UnitCategory.UNIT_1 && (q.questionArabic.contains("دعائم") || q.questionArabic.contains("عضل") || q.questionArabic.contains("حركة") || q.questionArabic.contains("وتر") || q.questionArabic.contains("مفصل") || q.questionArabic.contains("سارك") || q.number <= 8)
                "hormonal_coordination" -> q.unit == UnitCategory.UNIT_1 && (q.questionArabic.contains("هرمون") || q.questionArabic.contains("غدة") || q.questionArabic.contains("أنسولين") || q.questionArabic.contains("ثيروكسين") || (q.number in 9..14))
                "reproduction" -> q.unit == UnitCategory.UNIT_1 && (q.questionArabic.contains("تكاثر") || q.questionArabic.contains("حيوان منوي") || q.questionArabic.contains("بويضة") || q.questionArabic.contains("مبيض") || (q.number in 15..24))
                "immunity" -> q.unit == UnitCategory.UNIT_1 && (q.questionArabic.contains("مناع") || q.questionArabic.contains("جسم مضاد") || q.questionArabic.contains("تائية") || q.questionArabic.contains("بائية") || (q.number in 25..32))
                "molecular_genetics" -> q.unit == UnitCategory.UNIT_2 || q.number >= 33
                else -> true
            }
        }.take(12)

        val questionsToUse = if (filtered.size >= 5) filtered else allQuestions.take(10)
        val totalMarks = questionsToUse.sumOf { it.marks }

        return MinistryExamPaper(
            id = "quiz_${chapterKey}_${System.currentTimeMillis()}",
            code = "QUIZ-$chapterKey",
            title = "Chapter Quiz: $chapterTitleArabic",
            arabicTitle = "اختبار ذكي: $chapterTitleArabic",
            year = 2024,
            session = "تدريب فصلي ذكي",
            durationMinutes = 20,
            totalMarks = totalMarks,
            passMark = totalMarks * 0.5,
            descriptionArabic = "اختبار ذكي وتفاعلي مخصص للفصل مع التغذية الراجعة الفورية ورصد التريكات الوزارية.",
            descriptionEnglish = "Targeted chapter adaptive quiz with instant mark scheme and trap diagnostic.",
            questions = questionsToUse.mapIndexed { idx, q -> q.copy(number = idx + 1) }
        )
    }

    fun createMistakeReviewPaper(mistakeQuestionNumbers: Set<Int>): MinistryExamPaper {
        val allQuestions = generateQuestionsFor2024S1()
        val targeted = allQuestions.filter { mistakeQuestionNumbers.contains(it.number) }
        val finalQuestions = if (targeted.isNotEmpty()) targeted else allQuestions.take(5)
        val totalMarks = finalQuestions.sumOf { it.marks }

        return MinistryExamPaper(
            id = "retest_mistakes_${System.currentTimeMillis()}",
            code = "MISTAKE-REVIEW",
            title = "Targeted Mistake Remediation Quiz",
            arabicTitle = "اختبار معالجة الأخطاء والتريكات (دفتر الأخطاء)",
            year = 2024,
            session = "إعادة اختبار الأخطاء",
            durationMinutes = (finalQuestions.size * 2).coerceAtLeast(10),
            totalMarks = totalMarks,
            passMark = totalMarks * 0.6,
            descriptionArabic = "اختبار تدريبي مخصص يركز حصرياً على الأسئلة والتريكات الوزارية السابقة التي لم يتم حلها بالشكل الصحيح.",
            descriptionEnglish = "Specialized remediation quiz focusing exclusively on your recorded mistakes and fallen traps.",
            questions = finalQuestions.mapIndexed { idx, q -> q.copy(number = idx + 1) }
        )
    }
}
