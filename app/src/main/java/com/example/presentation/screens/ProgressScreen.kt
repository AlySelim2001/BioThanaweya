package com.example.presentation.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.MasteryLevel
import com.example.data.model.UnitCategory
import com.example.presentation.viewmodel.ChartType
import com.example.presentation.viewmodel.ProgressUiState
import com.example.presentation.viewmodel.ProgressViewModel
import com.example.presentation.viewmodel.TopicProgressItem
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

import com.example.presentation.components.WeeklyAdaptiveRemediationCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel,
    onBack: () -> Unit,
    onNavigateToTopic: (String) -> Unit = {},
    onNavigateToPractice: (String) -> Unit = {},
    onNavigateToFsrs: (String) -> Unit = {},
    onNavigateToGlossary: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("progress_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "تحليل الإتقان والتقدم الدراسي",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "11 موضوعاً في أحياء الثانوية العامة (Recharts & D3)",
                            style = MaterialTheme.typography.labelSmall,
                            color = BioTealPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("progress_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToGlossary,
                        modifier = Modifier.testTag("nav_glossary_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "المسرد المفهومي",
                            tint = MolecularPurple
                        )
                    }
                    IconButton(
                        onClick = {
                            val activeTopic = (state as? ProgressUiState.Success)?.selectedTopic
                            if (activeTopic != null) {
                                viewModel.simulateTopicReview(activeTopic.topicId, 0.95f)
                            }
                        },
                        modifier = Modifier.testTag("simulate_study_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Simulate Review",
                            tint = ExamGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        when (val s = state) {
            is ProgressUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = BioTealPrimary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "جاري تحميل بيانات الإتقان وحساب درجات المنهج...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            is ProgressUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "حدث خطأ: ${s.message}", color = MaterialTheme.colorScheme.error)
                }
            }
            is ProgressUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Executive Summary Cards (Score forecast, Mastery gauge, Breakdown)
                    item {
                        ProgressOverviewCard(successState = s)
                    }

                    // 1.5. Adaptive Diagnostic Weekly Remediation Plan
                    item {
                        WeeklyAdaptiveRemediationCard(
                            plan = s.weeklyPlan,
                            onToggleTask = { dayNumber -> viewModel.toggleRemedialTask(dayNumber) },
                            onNavigateToTopic = onNavigateToTopic,
                            onNavigateToPractice = onNavigateToPractice,
                            onNavigateToFsrs = onNavigateToFsrs
                        )
                    }

                    // 2. Unit Filter Chips & Chart Type Switcher
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Chart Type Selector
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                ChartType.values().forEach { chartType ->
                                    val isSelected = s.selectedChartType == chartType
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (isSelected) BioTealPrimary else Color.Transparent
                                            )
                                            .clickable { viewModel.setChartType(chartType) }
                                            .padding(vertical = 8.dp)
                                            .testTag("chart_tab_${chartType.name.lowercase()}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = chartType.labelAr,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            ),
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            // Unit Filter Chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = s.selectedUnitFilter == null,
                                    onClick = { viewModel.setUnitFilter(null) },
                                    label = { Text("كافة الفصول (11)") },
                                    modifier = Modifier.testTag("filter_all_units")
                                )
                                FilterChip(
                                    selected = s.selectedUnitFilter == UnitCategory.UNIT_1,
                                    onClick = { viewModel.setUnitFilter(UnitCategory.UNIT_1) },
                                    label = { Text("الوحدة 1 (التركيب والوظيفة)") },
                                    modifier = Modifier.testTag("filter_unit_1")
                                )
                                FilterChip(
                                    selected = s.selectedUnitFilter == UnitCategory.UNIT_2,
                                    onClick = { viewModel.setUnitFilter(UnitCategory.UNIT_2) },
                                    label = { Text("الوحدة 2 (البيولوجيا الجزيئية)") },
                                    modifier = Modifier.testTag("filter_unit_2")
                                )
                            }
                        }
                    }

                    // 3. Visualization Canvas based on Selected Chart Type
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(16.dp))
                                .testTag("main_chart_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = when (s.selectedChartType) {
                                                ChartType.BAR_RECHARTS -> "📊 أعمدة الإتقان الوزارية (Recharts Style)"
                                                ChartType.RADAR_D3 -> "🕸️ الرادار المعرفي للأحياء (D3 Spider Chart)"
                                                ChartType.WEB_D3 -> "🌐 مخطط D3.js التفاعلي (Interactive SVG)"
                                            },
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "انقر على أي عمود أو محور للتفاصيل وعرض بطاقة الموضوع",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                when (s.selectedChartType) {
                                    ChartType.BAR_RECHARTS -> {
                                        RechartsComposeBarChart(
                                            topics = s.filteredTopics,
                                            selectedTopicId = s.selectedTopic?.topicId,
                                            onSelectTopic = { viewModel.selectTopic(it) }
                                        )
                                    }
                                    ChartType.RADAR_D3 -> {
                                        D3RadarSpiderChart(
                                            competencies = s.radarCompetencies,
                                            topicTitle = s.selectedTopic?.titleAr ?: "متوسط المنهج بالكامل"
                                        )
                                    }
                                    ChartType.WEB_D3 -> {
                                        D3InteractiveWebChart(topics = s.filteredTopics)
                                    }
                                }
                            }
                        }
                    }

                    // 4. Interactive Recharts-Style Floating Tooltip / Inspection Card
                    if (s.selectedTopic != null) {
                        item {
                            RechartsInspectorCard(
                                topic = s.selectedTopic,
                                onNavigateToTopic = onNavigateToTopic,
                                onNavigateToPractice = onNavigateToPractice,
                                onNavigateToFsrs = onNavigateToFsrs,
                                onSimulateReview = { viewModel.simulateTopicReview(s.selectedTopic.topicId, 0.95f) }
                            )
                        }
                    }

                    // 5. Full 11 Topics Detailed Breakdown List
                    item {
                        Text(
                            text = "قائمة الفصول والموضوعات الـ 11 المفصلة",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    items(s.filteredTopics, key = { it.topicId }) { topic ->
                        TopicMasteryRowCard(
                            topic = topic,
                            isSelected = topic.topicId == s.selectedTopic?.topicId,
                            onClick = { viewModel.selectTopic(topic.topicId) },
                            onNavigateToTopic = { onNavigateToTopic(topic.topicId) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 1. Progress Overview Card: Displays predicted Ministry exam score and circular mastery gauge
 */
@Composable
private fun ProgressOverviewCard(successState: ProgressUiState.Success) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .testTag("progress_overview_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circular Mastery Gauge
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(90.dp)) {
                    CircularProgressIndicator(
                        progress = { successState.overallMasteryPercentage / 100f },
                        modifier = Modifier.size(80.dp),
                        color = BioTealPrimary,
                        strokeWidth = 8.dp,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${successState.overallMasteryPercentage}%",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = BioTealPrimary
                        )
                        Text(
                            text = "الإتقان العام",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp
                        )
                    }
                }

                // Ministry Exam Predicted Score
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = ExamGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "توقع امتحان الثانوية العامة",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = ExamGold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${successState.predictedMinistryScore} / 60",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = when {
                            successState.predictedMinistryScore >= 54 -> "مستوى امتياز متقدم (أوائل جمهورية) 🌟"
                            successState.predictedMinistryScore >= 45 -> "مستوى جيد جداً، بحاجة لتثبيت الفخاخ"
                            else -> "يحتاج مراجعة مكثفة للمفاهيم الأساسية"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))

            // Topic State Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatusPill(
                    count = successState.masteredTopicsCount,
                    label = "متقن (>=80%)",
                    color = SuccessGreen
                )
                StatusPill(
                    count = successState.inProgressTopicsCount,
                    label = "قيد التعلم (45-79%)",
                    color = ExamGold
                )
                StatusPill(
                    count = successState.needsReviewTopicsCount,
                    label = "بحاجة لمراجعة (<45%)",
                    color = AlertCoral
                )
            }
        }
    }
}

@Composable
private fun StatusPill(count: Int, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 2. Recharts-style Compose Bar Chart
 * Features:
 * - Animated vertical bars with rounded caps
 * - Horizontal dotted gridlines at 25%, 50%, 75%, 100%
 * - 80% Ministry Distinction benchmark reference line (Red/Gold line with label)
 * - Click to select bar with active glow effect
 */
@Composable
private fun RechartsComposeBarChart(
    topics: List<TopicProgressItem>,
    selectedTopicId: String?,
    onSelectTopic: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Benchmark Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp, 3.dp)
                        .background(Color(0xFFE53935))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "حد التفوق الوزاري (80%)",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFE53935)
                )
            }
            Text(
                text = "اسحب أفقياً لعرض كافة الفصول ⬅️",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontally Scrollable Bar Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .horizontalScroll(scrollState)
                .padding(vertical = 8.dp)
        ) {
            val barWidth = 44.dp
            val barSpacing = 20.dp
            val totalContentWidth = ((barWidth + barSpacing) * topics.size) + 40.dp

            // Background Grid and 80% Reference Line
            Canvas(modifier = Modifier.size(width = totalContentWidth, height = 200.dp)) {
                val height = size.height
                val width = size.width

                // Grid lines at 25%, 50%, 75%, 100%
                val gridColor = Color.Gray.copy(alpha = 0.2f)
                val stroke = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f))

                for (pct in listOf(0.25f, 0.5f, 0.75f, 1.0f)) {
                    val y = height * (1f - pct)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f,
                        pathEffect = stroke.pathEffect
                    )
                }

                // 80% Reference Line (Benchmark)
                val benchmarkY = height * (1f - 0.80f)
                drawLine(
                    color = Color(0xFFE53935).copy(alpha = 0.75f),
                    start = Offset(0f, benchmarkY),
                    end = Offset(width, benchmarkY),
                    strokeWidth = 2.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 6f), 0f)
                )
            }

            // Interactive Bars Row
            Row(
                modifier = Modifier
                    .width(totalContentWidth)
                    .height(240.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(barSpacing),
                verticalAlignment = Alignment.Bottom
            ) {
                topics.forEach { topic ->
                    val isSelected = topic.topicId == selectedTopicId
                    val animatedProgress by animateFloatAsState(
                        targetValue = topic.masteryPercentage / 100f,
                        animationSpec = tween(durationMillis = 650),
                        label = "bar_anim_${topic.topicId}"
                    )

                    Column(
                        modifier = Modifier
                            .width(barWidth)
                            .clickable { onSelectTopic(topic.topicId) }
                            .testTag("bar_topic_${topic.topicId}"),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        // Value Badge on top of bar
                        Text(
                            text = "${topic.masteryPercentage}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                fontSize = 10.sp
                            ),
                            color = if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Vertical Bar
                        Box(
                            modifier = Modifier
                                .width(if (isSelected) 34.dp else 28.dp)
                                .height((150.dp * animatedProgress).coerceAtLeast(6.dp))
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = when (topic.masteryLevel) {
                                            MasteryLevel.MASTERED -> listOf(BioTealPrimary, BioCyanAccent)
                                            MasteryLevel.GOOD -> listOf(Color(0xFF43A047), Color(0xFF81C784))
                                            MasteryLevel.LEARNING -> listOf(ExamGold, Color(0xFFFFD54F))
                                            MasteryLevel.NEEDS_REVIEW -> listOf(AlertCoral, Color(0xFFE57373))
                                        }
                                    )
                                )
                                .border(
                                    width = if (isSelected) 2.5.dp else 0.dp,
                                    color = if (isSelected) Color.White else Color.Transparent,
                                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Arabic Abbreviation Label
                        Text(
                            text = topic.shortNameAr,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            ),
                            color = if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Chapter number indicator
                        Text(
                            text = "ف${topic.chapterNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * 3. D3-Style Radar / Spider Chart
 * Draws concentric polygon webs and a polygon overlay mapping the 5 cognitive dimensions
 */
@Composable
private fun D3RadarSpiderChart(
    competencies: Map<String, Float>,
    topicTitle: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "المحاور المعرفية لـ: $topicTitle",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = BioTealPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))

        val entries = competencies.entries.toList()
        val numAxes = entries.size.coerceAtLeast(3)

        Box(
            modifier = Modifier
                .size(260.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = (size.minDimension / 2f) * 0.75f
                val angleStep = (2 * Math.PI / numAxes).toFloat()

                // Draw concentric pentagon web lines (20%, 40%, 60%, 80%, 100%)
                for (level in 1..5) {
                    val levelRadius = radius * (level / 5f)
                    val webPath = Path()
                    for (i in 0 until numAxes) {
                        val angle = (i * angleStep) - (Math.PI / 2).toFloat()
                        val x = center.x + levelRadius * cos(angle)
                        val y = center.y + levelRadius * sin(angle)
                        if (i == 0) webPath.moveTo(x, y) else webPath.lineTo(x, y)
                    }
                    webPath.close()
                    drawPath(
                        path = webPath,
                        color = Color.Gray.copy(alpha = 0.25f),
                        style = Stroke(width = 1.5f)
                    )
                }

                // Draw radial axis spokes
                for (i in 0 until numAxes) {
                    val angle = (i * angleStep) - (Math.PI / 2).toFloat()
                    val endX = center.x + radius * cos(angle)
                    val endY = center.y + radius * sin(angle)
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.35f),
                        start = center,
                        end = Offset(endX, endY),
                        strokeWidth = 1.5f
                    )
                }

                // Draw student mastery polygon
                val masteryPath = Path()
                for (i in 0 until numAxes) {
                    val value = entries[i].value.coerceIn(0f, 1f)
                    val valueRadius = radius * value
                    val angle = (i * angleStep) - (Math.PI / 2).toFloat()
                    val x = center.x + valueRadius * cos(angle)
                    val y = center.y + valueRadius * sin(angle)

                    if (i == 0) masteryPath.moveTo(x, y) else masteryPath.lineTo(x, y)

                    // Draw vertex dot
                    drawCircle(
                        color = BioTealPrimary,
                        radius = 4.5f,
                        center = Offset(x, y)
                    )
                }
                masteryPath.close()

                // Fill polygon with translucent glowing teal
                drawPath(
                    path = masteryPath,
                    brush = Brush.radialGradient(
                        colors = listOf(BioCyanAccent.copy(alpha = 0.45f), BioTealPrimary.copy(alpha = 0.20f)),
                        center = center,
                        radius = radius
                    )
                )

                // Outline polygon
                drawPath(
                    path = masteryPath,
                    color = BioTealPrimary,
                    style = Stroke(width = 3f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Competency List Legend
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            entries.forEach { (name, score) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = name, style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "${(score * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = if (score >= 0.8f) SuccessGreen else if (score >= 0.6f) ExamGold else AlertCoral
                    )
                }
            }
        }
    }
}

/**
 * 4. D3.js Interactive Web Chart (Embedded SVG / Web Component)
 * Provides high-fidelity SVG interactive chart for students requiring web-standard data visualization.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun D3InteractiveWebChart(topics: List<TopicProgressItem>) {
    val jsonData = remember(topics) {
        topics.joinToString(",") {
            """{"name":"${it.shortNameAr}","pct":${it.masteryPercentage},"title":"${it.titleAr}"}"""
        }
    }

    val htmlContent = remember(jsonData) {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
            <style>
                body { margin: 0; padding: 12px; background: #0D1B2A; color: #FFF; font-family: -apple-system, sans-serif; direction: rtl; }
                .chart-title { font-size: 13px; font-weight: bold; color: #00897B; margin-bottom: 8px; text-align: center; }
                svg { width: 100%; height: 210px; overflow: visible; }
                .bar { rx: 4px; transition: all 0.3s ease; cursor: pointer; }
                .bar:hover { opacity: 0.8; filter: drop-shadow(0 0 4px #00ACC1); }
                .benchmark-line { stroke: #E53935; stroke-dasharray: 4,4; stroke-width: 1.5; }
                .label { font-size: 9px; fill: #B0BEC5; text-anchor: middle; }
                .pct-label { font-size: 8px; fill: #FFF; font-weight: bold; text-anchor: middle; }
                .tooltip { font-size: 10px; fill: #FFB300; text-anchor: middle; }
            </style>
        </head>
        <body>
            <div class="chart-title">مخطط تفاعلي D3.js SVG - الثانوية العامة</div>
            <svg id="svgChart" viewBox="0 0 360 200">
                <!-- 80% Benchmark -->
                <line x1="10" y1="50" x2="350" y2="50" class="benchmark-line" />
                <text x="345" y="45" font-size="8" fill="#E53935" text-anchor="end">حد التفوق 80%</text>
                <g id="barsGroup"></g>
            </svg>
            <script>
                const data = [$jsonData];
                const g = document.getElementById("barsGroup");
                const totalBars = data.length;
                const startX = 20;
                const availWidth = 320;
                const slotWidth = availWidth / totalBars;
                const barWidth = slotWidth * 0.7;
                const baseHeight = 160;

                data.forEach((d, i) => {
                    const x = startX + (i * slotWidth);
                    const h = (d.pct / 100) * 120;
                    const y = baseHeight - h;

                    const color = d.pct >= 80 ? "#00897B" : (d.pct >= 65 ? "#43A047" : (d.pct >= 45 ? "#FB8C00" : "#E53935"));

                    // Bar
                    const rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
                    rect.setAttribute("x", x);
                    rect.setAttribute("y", y);
                    rect.setAttribute("width", barWidth);
                    rect.setAttribute("height", h);
                    rect.setAttribute("fill", color);
                    rect.setAttribute("class", "bar");
                    rect.onclick = () => alert(d.title + ": " + d.pct + "% إتقان");
                    g.appendChild(rect);

                    // Pct Label
                    const pctText = document.createElementNS("http://www.w3.org/2000/svg", "text");
                    pctText.setAttribute("x", x + barWidth/2);
                    pctText.setAttribute("y", y - 4);
                    pctText.setAttribute("class", "pct-label");
                    pctText.textContent = d.pct + "%";
                    g.appendChild(pctText);

                    // Name Label
                    const nameText = document.createElementNS("http://www.w3.org/2000/svg", "text");
                    nameText.setAttribute("x", x + barWidth/2);
                    nameText.setAttribute("y", baseHeight + 14);
                    nameText.setAttribute("class", "label");
                    nameText.textContent = d.name;
                    g.appendChild(nameText);
                });
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    setBackgroundColor(0xFF0D1B2A.toInt())
                    loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                }
            },
            update = { webView ->
                webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        )
    }
}

/**
 * 5. Recharts-style Inspector Tooltip Card
 */
@Composable
private fun RechartsInspectorCard(
    topic: TopicProgressItem,
    onNavigateToTopic: (String) -> Unit,
    onNavigateToPractice: (String) -> Unit,
    onNavigateToFsrs: (String) -> Unit,
    onSimulateReview: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .border(2.dp, BioTealPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .testTag("recharts_inspector_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(BioTealPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${topic.chapterNumber}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = BioTealPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = topic.titleAr,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = topic.titleEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Mastery Status Badge
                Badge(
                    containerColor = when (topic.masteryLevel) {
                        MasteryLevel.MASTERED -> SuccessGreen
                        MasteryLevel.GOOD -> Color(0xFF43A047)
                        MasteryLevel.LEARNING -> ExamGold
                        MasteryLevel.NEEDS_REVIEW -> AlertCoral
                    }
                ) {
                    Text(
                        text = "${topic.masteryPercentage}% • ${topic.masteryLevel.label}",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats row (Stability, Cards Reviewed, Unit)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "ثبات FSRS", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = "${"%.1f".format(topic.stabilityDays)} يوم",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = BioTealPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "البطاقات المراجعة", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = "${topic.cardsReviewed}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "الوحدة", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = topic.unit.code,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = ExamGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onNavigateToTopic(topic.topicId) },
                    modifier = Modifier.weight(1f).testTag("action_open_topic")
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("الشرح")
                }
                OutlinedButton(
                    onClick = { onNavigateToPractice(topic.topicId) },
                    modifier = Modifier.weight(1f).testTag("action_open_practice")
                ) {
                    Icon(Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تدريب")
                }
                Button(
                    onClick = onSimulateReview,
                    colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                    modifier = Modifier.weight(1.2f).testTag("action_boost_mastery")
                ) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("رفع الإتقان")
                }
            }
        }
    }
}

/**
 * 6. Topic Mastery Row Card
 */
@Composable
private fun TopicMasteryRowCard(
    topic: TopicProgressItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    onNavigateToTopic: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (isSelected) 1.5.dp else 0.dp,
                color = if (isSelected) BioTealPrimary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .testTag("topic_row_${topic.topicId}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (topic.masteryLevel) {
                            MasteryLevel.MASTERED -> SuccessGreen.copy(alpha = 0.15f)
                            MasteryLevel.GOOD -> Color(0xFF43A047).copy(alpha = 0.15f)
                            MasteryLevel.LEARNING -> ExamGold.copy(alpha = 0.15f)
                            MasteryLevel.NEEDS_REVIEW -> AlertCoral.copy(alpha = 0.15f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${topic.chapterNumber}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = when (topic.masteryLevel) {
                        MasteryLevel.MASTERED -> SuccessGreen
                        MasteryLevel.GOOD -> Color(0xFF43A047)
                        MasteryLevel.LEARNING -> ExamGold
                        MasteryLevel.NEEDS_REVIEW -> AlertCoral
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = topic.titleAr,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${topic.unit.code} • ${topic.titleEn}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { topic.masteryPercentage / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = when (topic.masteryLevel) {
                        MasteryLevel.MASTERED -> SuccessGreen
                        MasteryLevel.GOOD -> Color(0xFF43A047)
                        MasteryLevel.LEARNING -> ExamGold
                        MasteryLevel.NEEDS_REVIEW -> AlertCoral
                    },
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${topic.masteryPercentage}%",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = when (topic.masteryLevel) {
                        MasteryLevel.MASTERED -> SuccessGreen
                        MasteryLevel.GOOD -> Color(0xFF43A047)
                        MasteryLevel.LEARNING -> ExamGold
                        MasteryLevel.NEEDS_REVIEW -> AlertCoral
                    }
                )
                Text(
                    text = topic.masteryLevel.label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
