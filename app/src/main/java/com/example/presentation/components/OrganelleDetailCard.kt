package com.example.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.OrganelleDetail
import com.example.domain.model.OrganelleType
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganelleDetailCard(
    detail: OrganelleDetail,
    onClose: () -> Unit,
    onLaunchSimulationMode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedQuizOption by remember(detail.type) { mutableStateOf<Int?>(null) }
    var isQuizSubmitted by remember(detail.type) { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("organelle_detail_card_${detail.type.name}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            Color(detail.type.primaryColorHex).copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Organelle Identification & Action Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        Color(detail.type.primaryColorHex),
                                        Color(detail.type.primaryColorHex).copy(alpha = 0.65f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = detail.type.shortBadge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = detail.arabicName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color(detail.type.primaryColorHex).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = detail.roleBadge,
                                    fontSize = 10.sp,
                                    color = Color(detail.type.primaryColorHex),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = detail.englishName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_organelle_card_btn")
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.outline)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Navigation
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("الوظيفة والتركيب", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Biotech, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_organelle_function")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("فخاخ الامتحان", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.WarningAmber, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_organelle_traps")
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("محاكاة حية", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_organelle_sim")
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("اختبر فهمك", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_organelle_quiz")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Content Panes
            when (selectedTab) {
                0 -> OrganelleFunctionContent(detail)
                1 -> OrganelleExamTrapsContent(detail)
                2 -> OrganelleMicroSimContent(detail, onLaunchSimulationMode)
                3 -> OrganelleQuizContent(
                    quiz = detail.microQuiz,
                    selectedOption = selectedQuizOption,
                    isSubmitted = isQuizSubmitted,
                    onSelectOption = { if (!isQuizSubmitted) selectedQuizOption = it },
                    onSubmit = { isQuizSubmitted = true },
                    onReset = {
                        selectedQuizOption = null
                        isQuizSubmitted = false
                    }
                )
            }
        }
    }
}

@Composable
private fun OrganelleFunctionContent(detail: OrganelleDetail) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Primary Function Card
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BioTealPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "الوظيفة الحيوية المباشرة",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BioTealPrimary)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = detail.primaryFunction,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Detailed Ultrastructure
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Layers, contentDescription = null, tint = MolecularPurple, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "التركيب المجهري الدقيق (Ultrastructure)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MolecularPurple)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = detail.detailedStructure,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ELI5 Everyday Analogy
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = ExamGold.copy(alpha = 0.1f),
            border = androidx.compose.foundation.BorderStroke(1.dp, ExamGold.copy(alpha = 0.35f))
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = ExamGold, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "التشبيه الواقعي (ELI5)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = ExamGold)
                    )
                    Text(
                        text = detail.eli5Analogy,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganelleExamTrapsContent(detail: OrganelleDetail) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // High Alert Ministry Trap Box
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = AlertCoral.copy(alpha = 0.08f),
            border = androidx.compose.foundation.BorderStroke(1.dp, AlertCoral.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = AlertCoral, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "فخ امتحانات الثانوية العامة (مهم جداً)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AlertCoral
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = detail.ministryExamTrap,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Cell Comparison Chip (Animal vs Plant presence)
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (detail.isPresentInAnimal) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (detail.isPresentInAnimal) SuccessGreen else AlertCoral,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (detail.isPresentInAnimal) "موجود في الخلية الحيوانية" else "يغيب عن الخلية الحيوانية",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (detail.isPresentInPlant) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (detail.isPresentInPlant) SuccessGreen else AlertCoral,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (detail.isPresentInPlant) "موجود في الخلية النباتية" else "يغيب عن الخلية النباتية",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Biochemical reactions list
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "التفاعلات والمعادلات المرتبطة بالمنهج:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                detail.keyReactions.forEach { reaction ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(BioTealPrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = reaction,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrganelleMicroSimContent(
    detail: OrganelleDetail,
    onLaunchSimulationMode: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        when (detail.type) {
            OrganelleType.MITOCHONDRIA -> {
                MitochondriaMicroSimulator()
            }
            OrganelleType.ROUGH_ER, OrganelleType.GOLGI_APPARATUS, OrganelleType.RIBOSOMES -> {
                SecretoryPathwaySimulator(onLaunchFlow = { onLaunchSimulationMode("SECRETORY_PATHWAY") })
            }
            OrganelleType.CENTRAL_VACUOLE, OrganelleType.CELL_MEMBRANE -> {
                OsmosisTurgorSimulator()
            }
            OrganelleType.LYSOSOMES -> {
                LysosomePhagocytosisSimulator()
            }
            else -> {
                GenericOrganelleSimulator(detail)
            }
        }
    }
}

@Composable
private fun MitochondriaMicroSimulator() {
    var glucoseUnits by remember { mutableFloatStateOf(1f) }
    val atpYield = (glucoseUnits * 38).toInt()

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFD84315).copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD84315).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFFFF5722))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "محاكي التنفس الخلوي وإنتاج جزيئات ATP",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "جزيئات الجلوكوز المؤكسدة (Glucose Input): ${glucoseUnits.toInt()}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Slider(
                value = glucoseUnits,
                onValueChange = { glucoseUnits = it },
                valueRange = 1f..10f,
                steps = 8,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFF5722),
                    activeTrackColor = Color(0xFFFF5722)
                ),
                modifier = Modifier.testTag("slider_glucose_input")
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF263238))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الحصيلة الكلية الناتجة:",
                    fontSize = 12.sp,
                    color = Color.White
                )
                Text(
                    text = "$atpYield جزيء ATP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFFD54F)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "C₆H₁₂O₆ + 6O₂ ➔ 6CO₂ + 6H₂O + 38 ATP (دورة كربس + سلسلة نقل الإلكترون)",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun SecretoryPathwaySimulator(onLaunchFlow: () -> Unit) {
    var stepIndex by remember { mutableIntStateOf(0) }
    val steps = listOf(
        "1. النواة: نسخ جين البروتين إلى شفرة الـ mRNA ونقلها عبر الثقوب النووية.",
        "2. الشبكة الخشنة (RER): ترجمة الشفرة بريبوسومات الشبكة وطي السلسلة الببتيدية.",
        "3. حويصلات ناقلة: برعمة الحويصلات ونقل البروتين إلى وجه الاستقبال (Cis) لجهاز جولجي.",
        "4. جهاز جولجي: فرز وتعديل وتغليف البروتين وإطلاق حويصلات إفرازية من وجه التصدير (Trans).",
        "5. الغشاء البلازمي: الإخراج الخلوي (Exocytosis) بإدماج غشاء الحويصلة مع غشاء الخلية."
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF1E88E5).copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E88E5).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Route, contentDescription = null, tint = Color(0xFF1E88E5))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "محاكاة مسار تصنيع وتصدير البروتين",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E88E5).copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = steps[stepIndex],
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { if (stepIndex > 0) stepIndex-- },
                    enabled = stepIndex > 0,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("السابق", fontSize = 11.sp)
                }

                Button(
                    onClick = {
                        if (stepIndex < steps.size - 1) stepIndex++
                        else onLaunchFlow()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (stepIndex < steps.size - 1) "الخطوة التالية" else "تشغيل التدفق 3D", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun OsmosisTurgorSimulator() {
    var mediumType by remember { mutableStateOf("HYPOTONIC") } // HYPOTONIC, ISOTONIC, HYPERTONIC

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF0288D1).copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0288D1).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFF0288D1))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "محاكي الإسموزية والدعامة الفسيولوجية بالفجوة",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = mediumType == "HYPOTONIC",
                    onClick = { mediumType = "HYPOTONIC" },
                    label = { Text("ماء مقطر / منخفض", fontSize = 10.sp) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = mediumType == "ISOTONIC",
                    onClick = { mediumType = "ISOTONIC" },
                    label = { Text("متساوي التركيز", fontSize = 10.sp) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = mediumType == "HYPERTONIC",
                    onClick = { mediumType = "HYPERTONIC" },
                    label = { Text("محلول مركز (سكري)", fontSize = 10.sp) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            val description = when (mediumType) {
                "HYPOTONIC" -> "يدخل الماء بالإسموزية إلى الفجوة العصارية فيزداد حجم العصير الخلوي ويضغط على البروتوبلازم ويدفعه نحو الجدار فتكتسب الخلية الدعامة الفسيولوجية وتتوتر."
                "ISOTONIC" -> "معدل دخول الماء يساوي معدل خروجه، وتظل الفجوة في حالة اتزان دون زيادة في ضغط الامتلاء."
                else -> "يخرج الماء من الفجوة العصارية إلى الوسط الخارجي الأكثر تركيزاً فيقل حجم الفجوة وينكمش البروتوبلازم مبتعداً عن الجدار (بلزمة) وتفقد الدعامة الفسيولوجية."
            }

            Text(
                text = description,
                fontSize = 11.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun LysosomePhagocytosisSimulator() {
    var isDigested by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFE91E63).copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE91E63).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFFE91E63))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "محاكاة هضم الميكروب بواسطة إنزيمات اللايسوسوم",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (!isDigested)
                    "الحالة: تم ابتلاع البكتيريا داخل فجوة بلعمية، ويقترب اللايسوسوم الأولي للاندماج وإفراز إنزيمات التحلل الحمضية (pH 5)."
                else
                    "الحالة: اندمج اللايسوسوم وتفتت البكتيريا إلى أجزاء غير ضارة، وتُعرض الأنتيجينات على سطح الخلية المناعية.",
                fontSize = 11.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { isDigested = !isDigested },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().testTag("btn_trigger_lysosome_digest")
            ) {
                Text(if (!isDigested) "دمج اللايسوسوم وهضم الميكروب" else "إعادة تعيين المحاكاة", fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun GenericOrganelleSimulator(detail: OrganelleDetail) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "نشاط التفاعل الديناميكي للعضي",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = detail.biochemicalRole,
                fontSize = 11.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun OrganelleQuizContent(
    quiz: com.example.domain.model.CellMicroQuiz,
    selectedOption: Int?,
    isSubmitted: Boolean,
    onSelectOption: (Int) -> Unit,
    onSubmit: () -> Unit,
    onReset: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = quiz.questionArabic,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, lineHeight = 20.sp)
        )
        Text(
            text = quiz.questionEnglish,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.outline
        )

        quiz.options.forEachIndexed { index, option ->
            val isSelected = selectedOption == index
            val isCorrect = quiz.correctIndex == index

            val borderColor = when {
                !isSubmitted && isSelected -> BioTealPrimary
                isSubmitted && isCorrect -> SuccessGreen
                isSubmitted && isSelected && !isCorrect -> AlertCoral
                else -> MaterialTheme.colorScheme.outlineVariant
            }

            val bgColor = when {
                !isSubmitted && isSelected -> BioTealPrimary.copy(alpha = 0.12f)
                isSubmitted && isCorrect -> SuccessGreen.copy(alpha = 0.15f)
                isSubmitted && isSelected && !isCorrect -> AlertCoral.copy(alpha = 0.15f)
                else -> MaterialTheme.colorScheme.surface
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(enabled = !isSubmitted) { onSelectOption(index) }
                    .testTag("quiz_option_$index"),
                color = bgColor,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (!isSubmitted) {
            Button(
                onClick = onSubmit,
                enabled = selectedOption != null,
                colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().testTag("btn_submit_cell_quiz")
            ) {
                Text("تأكيد الإجابة والتحقق", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        } else {
            val isUserCorrect = selectedOption == quiz.correctIndex
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isUserCorrect) SuccessGreen.copy(alpha = 0.12f) else AlertCoral.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isUserCorrect) SuccessGreen else AlertCoral
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isUserCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (isUserCorrect) SuccessGreen else AlertCoral
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUserCorrect) "إجابة ممتازة ودقيقة!" else "إجابة غير صحيحة - راجع التفسير:",
                            fontWeight = FontWeight.Bold,
                            color = if (isUserCorrect) SuccessGreen else AlertCoral,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = quiz.explanationArabic,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "نصيحة الامتحان: ${quiz.ministryTip}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            OutlinedButton(
                onClick = onReset,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().testTag("btn_retry_cell_quiz")
            ) {
                Text("إعادة المحاولة", fontSize = 12.sp)
            }
        }
    }
}
