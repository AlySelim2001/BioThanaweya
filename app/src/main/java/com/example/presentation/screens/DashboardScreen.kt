package com.example.presentation.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Chapter
import com.example.data.model.MasteryLevel
import com.example.data.model.SubTopic
import com.example.data.model.UnitCategory
import com.example.presentation.components.GamificationBanner
import com.example.presentation.components.SevenAgentBadge
import com.example.presentation.viewmodel.DashboardUiState
import com.example.presentation.viewmodel.DashboardViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToTopic: (String) -> Unit,
    onNavigateToPractice: (String) -> Unit,
    onNavigateToFsrs: (String) -> Unit,
    onNavigateToFormulaLab: () -> Unit,
    onNavigateToSimulatedExam: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var selectedUnitFilter by remember { mutableStateOf<UnitCategory?>(null) }
    var showAgentsList by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BioTealPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Biotech, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "BioThanaweya",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                            )
                            Text(
                                text = "Thanaweya Amma (Languages) • 7 AI Agents",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showAgentsList = !showAgentsList }) {
                        Icon(
                            imageVector = if (showAgentsList) Icons.Default.ExpandLess else Icons.Default.Hub,
                            contentDescription = "7 Agents Ecosystem",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onNavigateToFormulaLab, modifier = Modifier.testTag("nav_formula_lab_btn")) {
                        Icon(Icons.Default.Calculate, contentDescription = "Formula Lab", tint = ExamGold)
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        when (val s = state) {
            is DashboardUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BioTealPrimary)
                }
            }
            is DashboardUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        // Hero Header
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    Brush.linearGradient(listOf(DeepNavy, Color(0xFF1E3A5F)))
                                )
                                .padding(18.dp)
                        ) {
                            Column {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = BioCyanAccent.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "7-AGENT AI LEARNING ECOSYSTEM",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BioCyanAccent,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Thanaweya Biology (Units 1 & 2)",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = "Grounded in the official Ministry textbook. Deep English breakdown, Arabic ELI5 analogies, Mark Scheme grading & FSRS retention.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFECEFF1),
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                Spacer(modifier = Modifier.height(14.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = { onNavigateToPractice(s.suggestedTopicId) },
                                        colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f).testTag("dashboard_practice_btn")
                                    ) {
                                        Icon(Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Practice", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }

                                    OutlinedButton(
                                        onClick = { onNavigateToFsrs(s.suggestedTopicId) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BioCyanAccent),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, BioCyanAccent),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f).testTag("dashboard_fsrs_btn")
                                    ) {
                                        Icon(Icons.Default.Style, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("FSRS Flashcards", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Simulated Ministry Exam Hero Card
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = AlertCoral.copy(alpha = 0.08f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AlertCoral.copy(alpha = 0.4f)),
                            modifier = Modifier.testTag("dashboard_simulated_exam_card")
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Timer, contentDescription = null, tint = AlertCoral, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Simulated Ministry Exam Mode",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = AlertCoral
                                        )
                                        Text(
                                            text = "45-Minute Timed • 15 Authentic Review Items • Voice Answering",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Experience the actual Egyptian Ministry examination format with timer countdown, Mark Scheme grading, and official PDF citations.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = onNavigateToSimulatedExam,
                                    colors = ButtonDefaults.buttonColors(containerColor = AlertCoral),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth().testTag("dashboard_launch_exam_btn")
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Launch Ministry Exam Simulation", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // FSRS Mastery Analytics Heatmap
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.testTag("mastery_heatmap_card")
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Analytics, contentDescription = null, tint = BioTealPrimary)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Curriculum Mastery Heatmap",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = BioSuccessGreen.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "${s.overallMasteryPercentage}% Retained",
                                            color = BioSuccessGreen,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "FSRS-Kotlin retention stability across all curriculum units:",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    s.topicMasteries.forEach { mastery ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                                .clickable { onNavigateToTopic(mastery.topicId) }
                                                .padding(horizontal = 12.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = mastery.title,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = "${mastery.unit.code} • ${mastery.stability.toInt()}d Stability",
                                                    fontSize = 10.sp,
                                                    color = MaterialTheme.colorScheme.outline
                                                )
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color(mastery.masteryLevel.colorHex).copy(alpha = 0.18f)
                                            ) {
                                                Text(
                                                    text = mastery.masteryLevel.label,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(mastery.masteryLevel.colorHex),
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Gamification & Streak Banner (Agent 7)
                    item {
                        GamificationBanner(profile = s.wellbeingProfile)
                    }

                    // Wellbeing Tip Card (Agent 7)
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = ExamGoldLight.copy(alpha = 0.6f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ExamGold.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Spa, contentDescription = null, tint = Color(0xFFF57F17), modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "💡 Agent 7 Wellbeing Tip: ${s.dailyTip}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = DeepNavy
                                )
                            }
                        }
                    }

                    // 7 Collaborative Agents Collapsible
                    item {
                        AnimatedVisibility(visible = showAgentsList) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Collaborative 7-Agent Architecture:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                SevenAgentBadge(1, "Master Biology Educator", "ELI5 + Deep Technical Breakdown + Bloom's Progression")
                                SevenAgentBadge(2, "Visual Learning Architect", "ASCII Schematics (LTR) + 3D YouTube + Exam Markers")
                                SevenAgentBadge(3, "Assessment Specialist", "High-Yield Items + Ministry Traps + Penalty Flags")
                                SevenAgentBadge(4, "Ministry Examiner", "Text Normalization + MarkScheme Engine + FSRS-Kotlin")
                                SevenAgentBadge(5, "Metacognitive Evaluator", "Confidence-Accuracy Gap Analysis + Calibration")
                                SevenAgentBadge(6, "Problem-Based Learning", "Lab Missions + Experimental Rubrics + Problem Solving")
                                SevenAgentBadge(7, "Student Wellbeing & Gamification", "Streaks + XP Rewards + Exam Anxiety Relief")
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }

                    // Unit Filter Chips
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedUnitFilter == null,
                                onClick = { selectedUnitFilter = null },
                                label = { Text("All Topics (${s.subTopics.size})") }
                            )
                            FilterChip(
                                selected = selectedUnitFilter == UnitCategory.UNIT_1,
                                onClick = { selectedUnitFilter = UnitCategory.UNIT_1 },
                                label = { Text("Unit 1 (Structure & Function)") }
                            )
                            FilterChip(
                                selected = selectedUnitFilter == UnitCategory.UNIT_2,
                                onClick = { selectedUnitFilter = UnitCategory.UNIT_2 },
                                label = { Text("Unit 2 (Molecular Bio)") }
                            )
                        }
                    }

                    // Chapters Section Header
                    item {
                        Text(
                            text = "Ministry Curriculum Topics:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    val filteredTopics = if (selectedUnitFilter == null) {
                        s.subTopics
                    } else {
                        s.subTopics.filter { it.unit == selectedUnitFilter }
                    }

                    items(filteredTopics) { topic ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToTopic(topic.id) }
                                .testTag("topic_tile_${topic.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (topic.unit == UnitCategory.UNIT_1) BioTealPrimary.copy(alpha = 0.15f) else MolecularPurple.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = topic.unit.code,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (topic.unit == UnitCategory.UNIT_1) BioTealPrimary else MolecularPurple,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Bolt, contentDescription = null, tint = ExamGold, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("High-Yield", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ExamGold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = topic.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = topic.arabicTitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = topic.summary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = { onNavigateToTopic(topic.id) }) {
                                        Text("Study Pack", fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(40.dp)) }
                }
            }
        }
    }
}
