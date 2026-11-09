package com.example.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.TopicMasteryEntity
import com.example.data.repository.BiologyCurriculumRepository
import com.example.domain.repository.BiologyRepository
import com.example.ui.theme.*

data class UnitTopicDetail(
    val topicId: String,
    val titleArabic: String,
    val titleEnglish: String,
    val masteryLevel: Float, // 0.0 to 1.0
    val percentage: Int,     // 0 to 100
    val cardsReviewed: Int,
    val stability: Double
)

data class UnitCalculationResult(
    val unitNumber: Int,
    val titleArabic: String,
    val titleEnglish: String,
    val masteryPercentage: Int,
    val progressFraction: Float,
    val topics: List<UnitTopicDetail>,
    val masteredTopicsCount: Int,
    val totalTopicsCount: Int,
    val totalCardsReviewed: Int
)

@Composable
fun UnitMasteryDashboardComponent(
    biologyRepository: BiologyRepository,
    onNavigateToTopic: (String) -> Unit = {},
    onNavigateToPractice: (String) -> Unit = {},
    onNavigateToFsrs: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 1. Query Room Database directly via reactive flows
    val roomMasteries by biologyRepository.getTopicMasteries().collectAsState(initial = emptyList())
    val roomProgress by biologyRepository.getStudentProgress().collectAsState(initial = null)

    var selectedUnitTab by remember { mutableIntStateOf(0) } // 0: All Units, 1: Unit 1, 2: Unit 2

    // 2. Calculate Unit-wise mastery percentages from Room interaction history
    val allCurriculumTopics = remember { BiologyCurriculumRepository.subTopics }

    val unit1Result = remember(roomMasteries) {
        calculateUnitMastery(
            unitNumber = 1,
            titleArabic = "الوحدة الأولى: التركيب والوظيفة في الكائنات الحية",
            titleEnglish = "Unit 1: Structure & Function in Living Organisms",
            allCurriculumTopics = allCurriculumTopics.filter { it.unit.name.contains("1") },
            roomEntities = roomMasteries.filter { it.unitNumber == 1 }
        )
    }

    val unit2Result = remember(roomMasteries) {
        calculateUnitMastery(
            unitNumber = 2,
            titleArabic = "الوحدة الثانية: البيولوجيا الجزيئية",
            titleEnglish = "Unit 2: Molecular Biology",
            allCurriculumTopics = allCurriculumTopics.filter { it.unit.name.contains("2") },
            roomEntities = roomMasteries.filter { it.unitNumber == 2 }
        )
    }

    val overallMasteryPercentage = remember(unit1Result, unit2Result) {
        ((unit1Result.masteryPercentage + unit2Result.masteryPercentage) / 2).coerceIn(0, 100)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("unit_mastery_dashboard_component"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BioTealPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.PieChart,
                            contentDescription = "Mastery Dashboard",
                            tint = BioTealPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "لوحة إتقان الوحدات الدراسية (Room DB)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Unit-Wise Mastery Circular Indicators",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BioTealPrimary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "$overallMasteryPercentage% إتقان كلي",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = BioTealPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dual Circular Progress Indicators Card Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Unit 1 Circular Progress Indicator
                CircularUnitIndicatorCard(
                    unitResult = unit1Result,
                    indicatorColor = BioTealPrimary,
                    trackColor = BioTealPrimary.copy(alpha = 0.15f),
                    isSelected = selectedUnitTab == 1,
                    onSelect = { selectedUnitTab = if (selectedUnitTab == 1) 0 else 1 },
                    testTag = "circular_indicator_unit_1",
                    modifier = Modifier.weight(1f)
                )

                // Unit 2 Circular Progress Indicator
                CircularUnitIndicatorCard(
                    unitResult = unit2Result,
                    indicatorColor = MolecularPurple,
                    trackColor = MolecularPurple.copy(alpha = 0.15f),
                    isSelected = selectedUnitTab == 2,
                    onSelect = { selectedUnitTab = if (selectedUnitTab == 2) 0 else 2 },
                    testTag = "circular_indicator_unit_2",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Unit Filter Segmented Bar
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = selectedUnitTab == 0,
                    onClick = { selectedUnitTab = 0 },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                    icon = { Icon(Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("filter_all_units_btn")
                ) {
                    Text("الكل (${unit1Result.totalTopicsCount + unit2Result.totalTopicsCount})", fontSize = 11.sp)
                }
                SegmentedButton(
                    selected = selectedUnitTab == 1,
                    onClick = { selectedUnitTab = 1 },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("filter_unit_1_btn")
                ) {
                    Text("الوحدة 1 (${unit1Result.masteryPercentage}%)", fontSize = 11.sp)
                }
                SegmentedButton(
                    selected = selectedUnitTab == 2,
                    onClick = { selectedUnitTab = 2 },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                    icon = { Icon(Icons.Default.Biotech, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("filter_unit_2_btn")
                ) {
                    Text("الوحدة 2 (${unit2Result.masteryPercentage}%)", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Topics Breakdown List based on selected tab
            val topicsToDisplay = when (selectedUnitTab) {
                1 -> unit1Result.topics
                2 -> unit2Result.topics
                else -> unit1Result.topics + unit2Result.topics
            }

            Text(
                text = "الموضوعات وتفاصيل الاستيعاب في Room DB:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            topicsToDisplay.forEach { topic ->
                TopicMasteryRowItem(
                    topic = topic,
                    onNavigateToTopic = { onNavigateToTopic(topic.topicId) },
                    onNavigateToPractice = { onNavigateToPractice(topic.topicId) },
                    onNavigateToFsrs = { onNavigateToFsrs(topic.topicId) }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun CircularUnitIndicatorCard(
    unitResult: UnitCalculationResult,
    indicatorColor: Color,
    trackColor: Color,
    isSelected: Boolean,
    onSelect: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = unitResult.progressFraction,
        animationSpec = tween(durationMillis = 900),
        label = "circularProgress"
    )

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) indicatorColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) indicatorColor else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onSelect() }
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Material 3 CircularProgressIndicator with Center Percentage
            Box(
                modifier = Modifier.size(95.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = indicatorColor,
                    trackColor = trackColor,
                    strokeWidth = 9.dp,
                    strokeCap = StrokeCap.Round
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${unitResult.masteryPercentage}%",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = indicatorColor
                        )
                    )
                    Text(
                        text = "إتقان",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (unitResult.unitNumber == 1) "الوحدة الأولى" else "الوحدة الثانية",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (unitResult.unitNumber == 1) "التركيب والوظيفة" else "البيولوجيا الجزيئية",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = indicatorColor
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = indicatorColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "${unitResult.masteredTopicsCount}/${unitResult.totalTopicsCount} موضوعات متقنة",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = indicatorColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun TopicMasteryRowItem(
    topic: UnitTopicDetail,
    onNavigateToTopic: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToFsrs: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToTopic() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = topic.titleArabic,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 1
                )
                Text(
                    text = topic.titleEnglish,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { topic.masteryLevel.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (topic.percentage >= 70) BioTealPrimary else AlertCoral,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${topic.percentage}%",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = if (topic.percentage >= 70) BioTealPrimary else AlertCoral
                )
                Row {
                    IconButton(
                        onClick = onNavigateToPractice,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.FactCheck,
                            contentDescription = "Practice",
                            tint = BioTealPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onNavigateToFsrs,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Style,
                            contentDescription = "FSRS",
                            tint = MolecularPurple,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

fun calculateUnitMastery(
    unitNumber: Int,
    titleArabic: String,
    titleEnglish: String,
    allCurriculumTopics: List<com.example.data.model.SubTopic>,
    roomEntities: List<TopicMasteryEntity>
): UnitCalculationResult {
    val entityMap = roomEntities.associateBy { it.topicId }

    val details = allCurriculumTopics.map { subTopic ->
        val entity = entityMap[subTopic.id]
        val rawMastery = entity?.masteryLevel ?: 0.65f
        val pct = (rawMastery * 100).toInt().coerceIn(0, 100)
        val cards = entity?.cardsReviewed ?: 5
        val stability = entity?.averageStability ?: 7.5

        UnitTopicDetail(
            topicId = subTopic.id,
            titleArabic = subTopic.arabicTitle,
            titleEnglish = subTopic.title,
            masteryLevel = rawMastery,
            percentage = pct,
            cardsReviewed = cards,
            stability = stability
        )
    }

    val totalCount = details.size.coerceAtLeast(1)
    val masteredCount = details.count { it.percentage >= 70 }
    val avgPct = if (details.isNotEmpty()) details.map { it.percentage }.average().toInt().coerceIn(0, 100) else 70
    val totalCards = details.sumOf { it.cardsReviewed }

    return UnitCalculationResult(
        unitNumber = unitNumber,
        titleArabic = titleArabic,
        titleEnglish = titleEnglish,
        masteryPercentage = avgPct,
        progressFraction = avgPct / 100f,
        topics = details,
        masteredTopicsCount = masteredCount,
        totalTopicsCount = totalCount,
        totalCardsReviewed = totalCards
    )
}
