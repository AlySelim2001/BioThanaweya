package com.example.domain.engine

import com.example.domain.model.CompanionDiagnosticSession
import com.example.domain.model.MicroIntervention
import com.example.domain.model.StudentComprehensionLevel
import com.example.domain.model.StudentSentiment

object EmpatheticCompanionEngine {

    fun detectSentiment(input: String): StudentSentiment {
        val lower = input.lowercase()
        return when {
            lower.contains("تعبت") || lower.contains("مرهق") || lower.contains("مش قادر") || lower.contains("نعسان") || lower.contains("طاقة") ->
                StudentSentiment.FATIGUED
            lower.contains("خايف") || lower.contains("قلقان") || lower.contains("متوتر") || lower.contains("مرعوب") || lower.contains("امتحان") || lower.contains("صعب") ->
                StudentSentiment.ANXIOUS
            lower.contains("مش فاهم") || lower.contains("لخبطة") || lower.contains("مشوش") || lower.contains("معقد") || lower.contains("ازاي") ->
                StudentSentiment.CONFUSED
            lower.contains("واثق") || lower.contains("سهلة") || lower.contains("تمام") || lower.contains("فاهم") || lower.contains("مستعد") || lower.contains("ممتاز") ->
                StudentSentiment.CONFIDENT
            else ->
                StudentSentiment.CURIOUS
        }
    }

    fun evaluateComprehension(
        quizAccuracy: Double, // 0.0 to 1.0
        reviewAttempts: Int,
        activeSentiment: StudentSentiment
    ): StudentComprehensionLevel {
        return when {
            quizAccuracy >= 0.85 && reviewAttempts >= 2 && activeSentiment != StudentSentiment.CONFUSED ->
                StudentComprehensionLevel.MASTERED
            quizAccuracy >= 0.50 ->
                StudentComprehensionLevel.PROFICIENT
            else ->
                StudentComprehensionLevel.NOVICE
        }
    }

    fun createDiagnosticSession(
        topicId: String,
        studentInput: String = "",
        historicalAccuracy: Double = 0.65
    ): CompanionDiagnosticSession {
        val sentiment = if (studentInput.isNotBlank()) detectSentiment(studentInput) else StudentSentiment.CURIOUS
        val comprehension = evaluateComprehension(historicalAccuracy, 1, sentiment)

        val greeting = when (sentiment) {
            StudentSentiment.FATIGUED ->
                "أهلاً بك يا بطل! أرى أنك تبذل جهداً كبيراً اليوم. لا تقلق، سنقسّم هذا الدرس إلى أجزاء صغيرة وممتعة حتى لا تشعر بأي إرهاق. خذ رشفة ماء ولنبدأ معاً بهدوء!"
            StudentSentiment.ANXIOUS ->
                "مرحباً بك! امتحان الثانوية العامة ليس شبحاً بل خطة محكمة. تذكر أن كل طالب متفوق شعر بالتوتر في البداية. أنا هنا معك كمعلمك الخاص لنفكك أي فخ وزاري خطوة بخطوة."
            StudentSentiment.CONFUSED ->
                "أهلاً بك! الارتباك هو أول مؤشرات التعلم الحقيقي؛ هذا يعني أن عقلك يبني روابط جديدة! سنستخدم تشبيهات من واقع حياتك اليومية لتتضح الصورة تماماً."
            StudentSentiment.CONFIDENT ->
                "يا سلام على هذه الروح القوية والمعنويات المرتفعة! هيا بنا نختبر دقتك ونستهدف الدرجة النهائية (60/60) باقتناص أدق تفاصيل المنهج!"
            StudentSentiment.CURIOUS ->
                "أهلاً بباحث المستقبل! مادة الأحياء مليئة بالإعجاز الدقيق والتفاصيل المبهرة. هيا نبحر في أسرار هذا الموضوع!"
        }

        val baselineTone = when (comprehension) {
            StudentComprehensionLevel.NOVICE ->
                "نبرة صبورة ومشجعة: البدء بالتشبيه الحياتي الملموس (ELI5) وتثبيت المصطلحات الرئيسية قبل التعمق."
            StudentComprehensionLevel.PROFICIENT ->
                "نبرة تدريبية رصينة: التركيز على العلاقات الرياضية وجداول المقارنة وخطوات الآليات الحيوية."
            StudentComprehensionLevel.MASTERED ->
                "نبرة تحفيزية متقدمة: تفكيك مصائد امتحانات الوزارة والأسئلة الاستنتاجية المعقدة لتأكيد العلامة الكاملة."
        }

        val microIntervention = getMicroInterventionForTopic(topicId, comprehension)
        val checkInQuestion = getCheckInQuestionForTopic(topicId, comprehension)

        return CompanionDiagnosticSession(
            topicId = topicId,
            initialSentiment = sentiment,
            detectedComprehensionLevel = comprehension,
            pedagogicalBaselineTone = baselineTone,
            empatheticGreeting = greeting,
            checkInQuestionArabic = checkInQuestion,
            targetedMicroIntervention = microIntervention
        )
    }

    private fun getMicroInterventionForTopic(
        topicId: String,
        level: StudentComprehensionLevel
    ): MicroIntervention {
        return when (topicId) {
            "sarcomere_contraction" -> MicroIntervention(
                id = "micro_sarcomere",
                titleArabic = "تدخل تدعيمي: لغز خيوط الميوسين وسحب الأكتين",
                auxiliaryAnalogy = "تخيل أن الميوسين هو فريق تجديف بقوارب يمسك بالمجاديف (الروابط المستعرضة) ليسحب حبال الأكتين نحو المنتصف، وكل ضربة مجداف تحتاج شحنة وقود (ATP) وتوجيه ضوئي (Ca²⁺)!",
                correctiveDrillPrompt = "إذا احتوت ليفة عضلية على 5 قطع عضلية (Sarcomeres)، كم عدد خطوط Z وكم عدد المناطق الداكنة A؟",
                correctiveModelAnswer = "عدد خطوط Z = عدد القطع + 1 = 6 خطوط Z. عدد المناطق الداكنة A = عدد القطع العضلية = 5 مناطق A.",
                scaffoldingAdjustment = "تثبيت قاعدة: طول المنطقة A لا يتغير أبداً أثناء الانقباض أو الانبساط لأن خيوط الميوسين نفسها لا تقصر.",
                empatheticEncouragement = "أنت رائع! حل هذا التدريب البسيط هو أول خطوة لضمان أسئلة المسائل العضلية في الامتحان."
            )
            "dna_structure_replication" -> MicroIntervention(
                id = "micro_dna",
                titleArabic = "تدخل تدعيمي: اتجاهات إنزيم بلمرة DNA",
                auxiliaryAnalogy = "إنزيم بلمرة DNA مثل قطار ذو اتجاه واحد إجباري؛ لا يسير إلا من محطة 5' إلى محطة 3'. لذا الشريط المقابل يضطر لبناء نفسه في صورة محطات متقطعة (قطع أوكازاكي)!",
                correctiveDrillPrompt = "إذا كانت نسبة الجوانين (G) في عينة DNA لولب مزدوج هي 30%، فما هي نسبة الثايمين (T)؟",
                correctiveModelAnswer = "بما أن G = 30%، إذن C = 30%. مجموع (G+C) = 60%. يتبقى لـ (A+T) نسبة 40%، إذن نسبة T = 20%.",
                scaffoldingAdjustment = "تطبيق فوري لقوانين شارجاف: البيورينات (A+G) = 50% والبيريميدينات (T+C) = 50% في اللولب المزدوج.",
                empatheticEncouragement = "خطوة ممتازة! انتبه دائماً إلى شرط 'اللولب المزدوج' قبل تطبيق نسب شارجاف."
            )
            else -> MicroIntervention(
                id = "micro_generic",
                titleArabic = "تدخل تدعيمي: استيعاب الآلية البيولوجية خطوة بخطوة",
                auxiliaryAnalogy = "العمليات الحيوية مثل الدوائر الكهربائية المغلقة؛ لكل سبب نتيجة ولكل محفز استجابة متزنة.",
                correctiveDrillPrompt = "اذكر مصطلحاً علمياً: هرمون أو إنزيم يلعب دور المحفز الأساسي في هذه الآلية؟",
                correctiveModelAnswer = "مراجعة الكلمات المفتاحية في بطاقة المفهوم والتأكد من تحديد موقع العضو والوظيفة بدقة.",
                scaffoldingAdjustment = "تبسيط السؤال إلى اختيار من متعدد ثم التدرج إلى التفسير المقالي.",
                empatheticEncouragement = "استمر بتركيزك؛ التعلم المتدرج يضمن ترسيخ المعلومة في الذاكرة طويلة المدى."
            )
        }
    }

    private fun getCheckInQuestionForTopic(
        topicId: String,
        level: StudentComprehensionLevel
    ): String {
        return when (topicId) {
            "sarcomere_contraction" ->
                "كيف تشعر الآن بعد مراجعة نظرية هكسلي؟ هل تستطيع أن تشرح لي بجملة واحدة: لماذا يؤدي غياب ATP إلى الشد العضلي المؤلم بدلاً من الشلل المرتخي؟"
            "dna_structure_replication" ->
                "ما هو انطباعك عن شوكة التضاعف؟ هل الفارق بين الشريط القائد والشريط المتخلف (أوكازاكي) أصبح واضحاً ومريحاً بالنسبة لك؟"
            "immunity_humoral_cellular" ->
                "هل تشعر بالثقة الآن في التمييز بين دور الخلية التائية المساعدة TH ودور الخلية التائية السامة TC عند دخول الفيروس إلى داخل الخلية؟"
            else ->
                "على مقياس من 1 إلى 5، ما مدى ارتياحك لاستيعاب هذا المفهوم العلمي، وما النقطة التي تود أن نتعمق فيها معاً؟"
        }
    }
}
