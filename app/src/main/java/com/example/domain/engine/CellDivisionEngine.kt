package com.example.domain.engine

import com.example.domain.model.DivisionProcess
import com.example.domain.model.DivisionStage
import com.example.domain.model.DivisionStageInfo

object CellDivisionEngine {

    fun getStageInfo(process: DivisionProcess, stage: DivisionStage): DivisionStageInfo {
        return when (process) {
            DivisionProcess.MITOSIS -> getMitosisStageInfo(stage)
            DivisionProcess.MEIOSIS_1 -> getMeiosis1StageInfo(stage)
            DivisionProcess.MEIOSIS_2 -> getMeiosis2StageInfo(stage)
        }
    }

    private fun getMitosisStageInfo(stage: DivisionStage): DivisionStageInfo {
        return when (stage) {
            DivisionStage.INTERPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MITOSIS,
                descriptionArabic = "تهيئة الخلية للانقسام ومضاعفة المادة الوراثية لتستقبل كل خلية جديدة نسخة طبق الأصل من المعلومات الوراثية.",
                descriptionEnglish = "Preparation for division with DNA synthesis (replication) ensuring exact genetic replication.",
                chromosomeCountFormula = "46 كروموسوم أحادي الكروماتيد (2n)",
                chromatidsCountFormula = "46 كروماتيد تتضاعف إلى 92",
                dnaContentFormula = "2C تتضاعف إلى 4C",
                ministryExamTrapWarning = "فخ وزاري: مضاعفة المادة الوراثية تعني مضاعفة جزيئات DNA (من 46 إلى 92 جزيء DNA) وليس مضاعفة عدد الكروموسومات الذي يظل 46 كروموسوماً!",
                keyMicroscopicEventArabic = "ظهور الشبكة الكروماتينية، مضاعفة الجسم المركزي (السنتروسوم)"
            )
            DivisionStage.PROPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MITOSIS,
                descriptionArabic = "تكثف الشبكة الكروماتينية لتظهر الكروموسومات واضحة، كل كروموسوم يتكون من كروماتيدين متصلين عند السنترومير.",
                descriptionEnglish = "Chromatin condenses into visible paired chromatids joined at the centromere; nucleolus and nuclear envelope disappear.",
                chromosomeCountFormula = "46 كروموسوم ثنائي الكروماتيد (2n)",
                chromatidsCountFormula = "92 كروماتيد",
                dnaContentFormula = "4C (92 جزيء DNA)",
                ministryExamTrapWarning = "فخ وزاري: في هذا الطور تتلاشى النوية والغشاء النووي وتبدأ خيوط المغزل في التكون من السنتروسوم في الخلية الحيوانية أو السيتوبلازم في النباتية.",
                keyMicroscopicEventArabic = "اختفاء الغشاء النووي والنوية، وامتداد خيوط المغزل"
            )
            DivisionStage.METAPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MITOSIS,
                descriptionArabic = "تصطف الكروموسومات في خط استواء الخلية فرادى، وتتصل بخيوط المغزل عند السنتروميرات.",
                descriptionEnglish = "Chromosomes line up individually along the metaphase equatorial plate attached to spindle fibers.",
                chromosomeCountFormula = "46 كروموسوم (2n)",
                chromatidsCountFormula = "92 كروماتيد",
                dnaContentFormula = "4C",
                ministryExamTrapWarning = "فخ وزاري: في الميتوزي تصطف الكروموسومات فرادى (Single file)، بينما في الطور الاستوائي الأول للميوزي تصطف في أزواج متماثلة (Tetrads).",
                keyMicroscopicEventArabic = "اصطفاف الكروموسومات فرادى على خط استواء الخلية"
            )
            DivisionStage.ANAPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MITOSIS,
                descriptionArabic = "ينقسم السنترومير طولياً إلى نصفين وتنفصل الكروماتيدات الشقيقة متجهة إلى قطبي الخلية بفعل تقلص خيوط المغزل.",
                descriptionEnglish = "Centromeres divide longitudinally; sister chromatids separate into individual daughter chromosomes.",
                chromosomeCountFormula = "92 كروموسوم بنوي أحادي (4n مؤقتاً)",
                chromatidsCountFormula = "92 كروماتيد (تعتبر كروموسومات)",
                dnaContentFormula = "4C تنقسم مناصفة (2C عند كل قطب)",
                ministryExamTrapWarning = "فخ وزاري قمة في الأهمية: انقسام السنترومير يحول الكروماتيدات إلى كروموسومات بنوية أحادية، فيتضاعف عدد الكروموسومات مؤقتاً إلى 92 كروموسوماً!",
                keyMicroscopicEventArabic = "انشطار السنترومير طولياً وتقلص خيوط المغزل"
            )
            DivisionStage.TELOPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MITOSIS,
                descriptionArabic = "تتكون نواتان جديدتان يعود الغشاء النووي والنوية للظهور، وينقسم السيتوبلازم لينتج خليتان متطابقتان تماماً للخلية الأم.",
                descriptionEnglish = "Nuclear membranes reform, chromosomes decondense back to chromatin, and cytokinesis yields two identical diploid cells.",
                chromosomeCountFormula = "46 كروموسوم في كل خلية (2n)",
                chromatidsCountFormula = "46 كروماتيد في كل خلية",
                dnaContentFormula = "2C في كل خلية",
                ministryExamTrapWarning = "النتيجة النهائية: خليتان تحتوي كل منهما على (2n) كروموسوم ومحتوى DNA مماثل تماماً للخلية الأصلية قبل التضاعف (2C).",
                keyMicroscopicEventArabic = "تكون الغشاء النووي وانقسام السيتوبلازم (Cytokinesis)"
            )
        }
    }

    private fun getMeiosis1StageInfo(stage: DivisionStage): DivisionStageInfo {
        return when (stage) {
            DivisionStage.INTERPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MEIOSIS_1,
                descriptionArabic = "طور تحضيري تتضاعف فيه جزيئات DNA استعداداً للانقسام الاختزالي لإنتاج الأمشاج.",
                descriptionEnglish = "DNA replication produces 4C DNA content prior to reductional meiosis.",
                chromosomeCountFormula = "46 كروموسوم (2n)",
                chromatidsCountFormula = "92 كروماتيد",
                dnaContentFormula = "2C تتضاعف إلى 4C",
                ministryExamTrapWarning = "فخ وزاري: يحدث الطور البيني وتضاعف المادة الوراثية مرة واحدة فقط قبل الميوزي الأول، ولا يتكرر قبل الميوزي الثاني!",
                keyMicroscopicEventArabic = "مضاعفة جزيئات DNA (تكوين 92 جزيء DNA)"
            )
            DivisionStage.PROPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MEIOSIS_1,
                descriptionArabic = "اقتران كل كروموسومين متماثلين لتكوين مجموعة رباعية (Tetrad)، وحدوث ظاهرة العبور الوراثي (Crossing Over) لتبادل الأجزاء الوراثية.",
                descriptionEnglish = "Synapsis of homologous pairs forming tetrads; Crossing Over exchanges genetic segments for variation.",
                chromosomeCountFormula = "46 كروموسوم (2n) في 23 مجموعة رباعية",
                chromatidsCountFormula = "92 كروماتيد",
                dnaContentFormula = "4C",
                ministryExamTrapWarning = "فخ وزاري: ظاهرة العبور الوراثي تحدث في الطور التمهيدي الأول فقط، وهي المسؤولة عن التنوع الوراثي في الأفراد الناتجة عن التكاثر الجنسي.",
                keyMicroscopicEventArabic = "تشكل المجموعات الرباعية وظاهرة العبور الوراثي"
            )
            DivisionStage.METAPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MEIOSIS_1,
                descriptionArabic = "تصطف أزواج الكروموسومات المتماثلة في خط استواء الخلية على هيئة مجموعات رباعية مزدوجة.",
                descriptionEnglish = "Homologous chromosome pairs align along the equatorial metaphase plate in double rows.",
                chromosomeCountFormula = "46 كروموسوم (23 زوجاً متماثلاً)",
                chromatidsCountFormula = "92 كروماتيد",
                dnaContentFormula = "4C",
                ministryExamTrapWarning = "فخ وزاري: هنا تصطف أزواج الكروموسومات في صفين متقابلين، وليس في صف واحد كالميتوزي.",
                keyMicroscopicEventArabic = "اصطفاف الأزواج المتماثلة على جانبي خط الاستواء"
            )
            DivisionStage.ANAPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MEIOSIS_1,
                descriptionArabic = "انفصال الكروموسومات المتماثلة عن بعضها دون انشطار السنترومير! يتجه كل كروموسوم كامل ثنائي الكروماتيد نحو أحد القطبين.",
                descriptionEnglish = "Homologous chromosomes separate toward opposite poles without centromere division.",
                chromosomeCountFormula = "23 كروموسوم ثنائي الكروماتيد عند كل قطب (n)",
                chromatidsCountFormula = "46 كروماتيد عند كل قطب",
                dnaContentFormula = "2C عند كل قطب",
                ministryExamTrapWarning = "أخطر فخ في الامتحانات: في الطور الانفصالي الأول لا ينقسم السنترومير إطلاقاً! الانفصال يحدث بين الكروموسومات المتماثلة وليس الكروماتيدات الشقيقة.",
                keyMicroscopicEventArabic = "انفصال الكروموسومات المتماثلة وبقاء السنتروميرات سليمة"
            )
            DivisionStage.TELOPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MEIOSIS_1,
                descriptionArabic = "انقسام الخلية إلى خليتين بنويتين تحتوي كل منهما على نصف العدد الأصلي للكروموسومات (n)، لكن كل كروموسوم لا يزال ثنائي الكروماتيد.",
                descriptionEnglish = "Two haploid daughter cells form, each containing n double-chromatid chromosomes.",
                chromosomeCountFormula = "23 كروموسوم في كل خلية (n)",
                chromatidsCountFormula = "46 كروماتيد في كل خلية",
                dnaContentFormula = "2C في كل خلية",
                ministryExamTrapWarning = "فخ وزاري: بالرغم من اختزال عدد الكروموسومات إلى (n = 23)، فإن كمية DNA لا تزال (2C) لأن كل كروموسوم مكون من كروماتيدين.",
                keyMicroscopicEventArabic = "تكون خليتين أحاديتي العدد الصبغي (n) بثنائي الكروماتيد"
            )
        }
    }

    private fun getMeiosis2StageInfo(stage: DivisionStage): DivisionStageInfo {
        return when (stage) {
            DivisionStage.INTERPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MEIOSIS_2,
                descriptionArabic = "فترة راحة قصيرة جداً لا يحدث فيها أي تضاعف للحمض النووي DNA.",
                descriptionEnglish = "Brief interkinesis phase without DNA replication.",
                chromosomeCountFormula = "23 كروموسوم (n)",
                chromatidsCountFormula = "46 كروماتيد",
                dnaContentFormula = "2C",
                ministryExamTrapWarning = "فخ وزاري: لا يحدث تضاعف للـ DNA بين الميوزي الأول والثاني إطلاقاً.",
                keyMicroscopicEventArabic = "غياب تضاعف المادة الوراثية"
            )
            DivisionStage.PROPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MEIOSIS_2,
                descriptionArabic = "تتكثف الكروموسومات في كل من الخليتين وتتكون خيوط المغزل مجدداً.",
                descriptionEnglish = "Chromosomes re-condense in both haploid cells; new spindle apparatus forms.",
                chromosomeCountFormula = "23 كروموسوم (n)",
                chromatidsCountFormula = "46 كروماتيد",
                dnaContentFormula = "2C",
                ministryExamTrapWarning = "الكروموسومات هنا أحادية المجموعة الصبغية (n = 23) ولكنها ثنائية الكروماتيد.",
                keyMicroscopicEventArabic = "ظهور خيوط المغزل في الخليتين"
            )
            DivisionStage.METAPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MEIOSIS_2,
                descriptionArabic = "تصطف الكروموسومات الـ 23 فرادى في خط استواء كل من الخليتين.",
                descriptionEnglish = "23 chromosomes line up individually along the equatorial plate in both cells.",
                chromosomeCountFormula = "23 كروموسوم (n)",
                chromatidsCountFormula = "46 كروماتيد",
                dnaContentFormula = "2C",
                ministryExamTrapWarning = "يشبه الميتوزي تماماً ولكن بنصف عدد الكروموسومات (23 بدلاً من 46).",
                keyMicroscopicEventArabic = "اصطفاف الكروموسومات فرادى على خط الاستواء"
            )
            DivisionStage.ANAPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MEIOSIS_2,
                descriptionArabic = "ينقسم السنترومير طولياً في كل كروموسوم وتنفصل الكروماتيدات الشقيقة متجهة إلى القطبين.",
                descriptionEnglish = "Centromeres divide longitudinally; sister chromatids separate toward opposite poles.",
                chromosomeCountFormula = "46 كروموسوم بنوي أحادي مؤقتاً في كل خلية",
                chromatidsCountFormula = "46 كروماتيد",
                dnaContentFormula = "2C تنقسم إلى 1C عند كل قطب",
                ministryExamTrapWarning = "فخ وزاري: ينقسم السنترومير هنا للمرة الأولى في الميوزي! فيتحول كل كروماتيد إلى كروموسوم بنوي مستقل.",
                keyMicroscopicEventArabic = "انشطار السنترومير وانفصال الكروماتيدات الشقيقة"
            )
            DivisionStage.TELOPHASE -> DivisionStageInfo(
                stage = stage,
                process = DivisionProcess.MEIOSIS_2,
                descriptionArabic = "تكون 4 خلايا أحادية العدد الصبغي (الأمشاج: حيوانات منوية أو بويضات)، تحتوي كل منها على (n) كروموسوم ومحتوى (1C) من DNA.",
                descriptionEnglish = "Cytokinesis completes, yielding 4 genetically unique haploid gametes (n, 1C DNA).",
                chromosomeCountFormula = "23 كروموسوم في كل مشيج (n)",
                chromatidsCountFormula = "23 كروماتيد أحادي (n)",
                dnaContentFormula = "1C (23 جزيء DNA)",
                ministryExamTrapWarning = "النتيجة النهائية للميوزي: 4 أمشاج كل مشيج به n=23 كروموسوم أحادي وكمية DNA=1C، مع تباين وراثي ناتج عن العبور.",
                keyMicroscopicEventArabic = "تكون 4 أمشاج فردية متباينة وراثياً"
            )
        }
    }
}
