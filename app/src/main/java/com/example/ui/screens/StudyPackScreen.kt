package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MasterStudyPack
import com.example.data.model.QuestionType
import com.example.data.repository.BiologyCurriculumRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyPackScreen(
    topicId: String,
    onBack: () -> Unit,
    onNavigateToExaminer: (String) -> Unit,
    onNavigateToFormulas: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val pack = remember(topicId) { BiologyCurriculumRepository.getMasterStudyPack(topicId) }

    // 5-Step Pipeline Tabs
    // 0: Educator (ELI5 & Technical)
    // 1: Visual Architect (ASCII & 3D Media)
    // 2: Assessment Specialist (Traps & Items)
    // 3: Adaptive Recall (Flashcards)
    // 4: Synthesis & Simulation
    var currentStep by remember { mutableIntStateOf(0) }
    var explanationMode by remember { mutableStateOf("eli5") } // "eli5" or "technical"
    var selectedQuestionType by remember { mutableStateOf<QuestionType?>(null) }
    var showCopiedToast by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = pack.topicTitle,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1
                        )
                        Text(
                            text = pack.chapterTitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("study_pack_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val fullSummary = buildString {
                                append("=== THANAWEYA AMMA MASTER STUDY PACK ===\n")
                                append("Topic: ${pack.topicTitle}\n")
                                append("Unit: ${pack.unit.title}\n\n")
                                append("--- [ELI5 ANALOGY] ---\n${pack.eli5Analogy}\n\n")
                                append("--- [DEEP TECHNICAL BREAKDOWN] ---\n${pack.deepTechnicalBreakdown}\n\n")
                                append("--- [KEY FORMULAS] ---\n")
                                pack.formulas.forEach { f ->
                                    append("${f.name}: ${f.formulaLatex} (${f.description})\n")
                                }
                            }
                            clipboardManager.setText(AnnotatedString(fullSummary))
                            showCopiedToast = true
                        },
                        modifier = Modifier.testTag("copy_pack_button")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Copy Study Pack")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Pipeline Step Indicator Header
            ScrollableTabRow(
                selectedTabIndex = currentStep,
                edgePadding = 12.dp,
                divider = {},
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = currentStep == 0,
                    onClick = { currentStep = 0 },
                    text = { Text("1. Educator", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_educator")
                )
                Tab(
                    selected = currentStep == 1,
                    onClick = { currentStep = 1 },
                    text = { Text("2. Visuals", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_visuals")
                )
                Tab(
                    selected = currentStep == 2,
                    onClick = { currentStep = 2 },
                    text = { Text("3. Questions", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.AssignmentLate, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_questions")
                )
                Tab(
                    selected = currentStep == 3,
                    onClick = { currentStep = 3 },
                    text = { Text("4. Flashcards", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Style, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_flashcards")
                )
                Tab(
                    selected = currentStep == 4,
                    onClick = { currentStep = 4 },
                    text = { Text("5. Synthesis", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_synthesis")
                )
            }

            // Step Content
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentStep) {
                    0 -> EducatorStepContent(
                        pack = pack,
                        explanationMode = explanationMode,
                        onModeChange = { explanationMode = it },
                        onNavigateToFormulas = onNavigateToFormulas
                    )
                    1 -> VisualStepContent(pack = pack, context = context)
                    2 -> AssessmentStepContent(
                        pack = pack,
                        selectedType = selectedQuestionType,
                        onTypeSelected = { selectedQuestionType = it }
                    )
                    3 -> AdaptiveFlashcardsStepContent(pack = pack)
                    4 -> SynthesisStepContent(
                        pack = pack,
                        onNavigateToExaminer = { onNavigateToExaminer(pack.topicId) }
                    )
                }

                // Copy Notification Snackbar
                androidx.compose.animation.AnimatedVisibility(
                    visible = showCopiedToast,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SuccessGreen,
                        shadowElevation = 6.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Full Master Study Pack copied to clipboard!", color = Color.White, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DISMISS",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.clickable { showCopiedToast = false }
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 1: Master Biology Educator
// -------------------------------------------------------------
@Composable
fun EducatorStepContent(
    pack: MasterStudyPack,
    explanationMode: String,
    onModeChange: (String) -> Unit,
    onNavigateToFormulas: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AgentHeaderBadge(
                agentNumber = 1,
                agentTitle = "Master Biology Educator",
                agentRole = "Pedagogical Strategist • Scaffolding & Bloom's Progression"
            )
        }

        // Dual-Layer Architecture Switcher
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = { onModeChange("eli5") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (explanationMode == "eli5") BioTealPrimary else Color.Transparent,
                            contentColor = if (explanationMode == "eli5") Color.White else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_eli5_button")
                    ) {
                        Text("🧸 ELI5 Intuitive Analogy", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { onModeChange("technical") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (explanationMode == "technical") MolecularPurple else Color.Transparent,
                            contentColor = if (explanationMode == "technical") Color.White else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_technical_button")
                    ) {
                        Text("🔬 Deep Technical Breakdown", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Explanation Body
        item {
            if (explanationMode == "eli5") {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BioCyanLight.copy(alpha = 0.5f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BioCyanAccent.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🧸", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Explain Like I'm 5 (Intuitive Anchor)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = DeepNavy
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = pack.eli5Analogy,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = DeepNavy
                        )
                    }
                }
            } else {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🔬", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Exhaustive Scientific Mechanisms",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = pack.deepTechnicalBreakdown,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Bloom's Taxonomy Cognitive Progression
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎯 Bloom's Taxonomy Progression",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Systematic mastery from foundational recall to creative evaluation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    pack.bloomProgression.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (item.levelNumber) {
                                            1, 2 -> SuccessGreen
                                            3, 4 -> ExamGold
                                            else -> MolecularPurple
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${item.levelNumber}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = item.levelName.uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = item.objective,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Prompt: \"${item.masteryPrompt}\"",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Mathematical Formulas Section
        if (pack.formulas.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📐 Mathematical Formulas & Base Ratios",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = onNavigateToFormulas) {
                        Text("Open Lab ↗", fontSize = 12.sp)
                    }
                }
            }

            items(pack.formulas) { formula ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0F172A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = formula.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = ExamGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E293B),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = formula.formulaLatex,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = BioCyanAccent,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = formula.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💡 Example: ${formula.sampleCalculation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }
            }
        }

        // Comparison Tables
        if (pack.comparisonTables.isNotEmpty()) {
            items(pack.comparisonTables) { table ->
                ComparisonTableCard(table = table)
            }
        }

        // Key Terminology
        item {
            Text(
                text = "📖 Essential Ministry Terms",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                pack.keyTerms.forEach { term ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = term.term,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (term.arabicTerm.isNotBlank()) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "(${term.arabicTerm})",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = term.definition,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

// -------------------------------------------------------------
// STEP 2: Visual & Spatial Learning Architect
// -------------------------------------------------------------
@Composable
fun VisualStepContent(
    pack: MasterStudyPack,
    context: android.content.Context
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AgentHeaderBadge(
                agentNumber = 2,
                agentTitle = "Visual & Spatial Learning Architect",
                agentRole = "Multi-Modal Diagramming • Structural Mapping • 3D Media Curation"
            )
        }

        // ASCII Schematic Viewer
        item {
            AsciiSchematicViewer(
                title = "ASCII Structural Schematic",
                asciiContent = pack.asciiSchematic,
                caption = pack.schematicCaption
            )
        }

        // Visual Analysis Markers
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ExamGoldLight.copy(alpha = 0.6f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, ExamGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RemoveRedEye, contentDescription = null, tint = DeepNavy)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Exam Diagram Inspection Markers",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        )
                    }
                    Text(
                        text = "Exact features Thanaweya Amma examiners look for when scoring diagram questions:",
                        fontSize = 12.sp,
                        color = DeepNavy.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )

                    pack.visualInspectionMarkers.forEachIndexed { i, marker ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${i + 1}.",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = DeepNavy
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = marker,
                                fontSize = 12.sp,
                                color = DeepNavy
                            )
                        }
                    }
                }
            }
        }

        // Physiological Flowchart
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🔄 Sequential Physiological Pathway",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    pack.flowcharts.forEachIndexed { index, step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(BioTealPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${step.stepNumber}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = step.action,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = step.detail,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (step.triggerOrEnzyme.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "⚡ Key Actor: ${step.triggerOrEnzyme}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                        if (index < pack.flowcharts.size - 1) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 11.dp)
                                    .width(2.dp)
                                    .height(14.dp)
                                    .background(BioTealPrimary.copy(alpha = 0.4f))
                            )
                        }
                    }
                }
            }
        }

        // YouTube 3D Curated Animations
        item {
            Text(
                text = "🎬 Curated 3D Biological Animations",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap to search and watch high-yield 3D simulations directly:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        items(pack.youtube3DQueries) { video ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, AlertCoral.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AlertCoral.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Watch",
                            tint = AlertCoral,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.query,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = video.topicDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.outline)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Rec duration: ${video.durationRecommendation}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/results?search_query=" + Uri.encode(video.query))
                            )
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AlertCoral),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Search", fontSize = 11.sp)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

// -------------------------------------------------------------
// STEP 3: Assessment Specialist & Ministry Exam Simulator
// -------------------------------------------------------------
@Composable
fun AssessmentStepContent(
    pack: MasterStudyPack,
    selectedType: QuestionType?,
    onTypeSelected: (QuestionType?) -> Unit
) {
    val filteredQuestions = if (selectedType == null) {
        pack.questions
    } else {
        pack.questions.filter { it.type == selectedType }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AgentHeaderBadge(
                agentNumber = 3,
                agentTitle = "Assessment Specialist",
                agentRole = "Ministry Exam Simulator • Trap Deconstruction • High-Yield Items"
            )
        }

        // Common Exam Traps & Misconceptions Card
        if (pack.examTraps.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AlertCoralLight.copy(alpha = 0.7f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AlertCoral.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = AlertCoral)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Thanaweya Amma Exam Traps",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB71C1C)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        pack.examTraps.forEach { trap ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = "❌ Misconception: ${trap.misconception}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFFB71C1C)
                                )
                                Text(
                                    text = "✅ Scientific Truth: ${trap.scientificTruth}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DeepNavy
                                )
                                Text(
                                    text = "⚠️ Exam Trap: ${trap.examTrapWarning}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFC62828),
                                    fontSize = 11.sp
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFB71C1C).copy(alpha = 0.1f),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text(
                                        text = "⭐ Golden Rule: ${trap.goldenRule}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB71C1C),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            HorizontalDivider(
                                color = AlertCoral.copy(alpha = 0.2f),
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Question Type Filters
        item {
            Text(
                text = "Practice Items (${filteredQuestions.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = selectedType == null,
                    onClick = { onTypeSelected(null) },
                    label = { Text("All (${pack.questions.size})", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedType == QuestionType.GIVE_REASON,
                    onClick = { onTypeSelected(QuestionType.GIVE_REASON) },
                    label = { Text("Give Reason", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedType == QuestionType.MCQ,
                    onClick = { onTypeSelected(QuestionType.MCQ) },
                    label = { Text("MCQs", fontSize = 11.sp) }
                )
            }
        }

        // Practice Questions List
        items(filteredQuestions) { q ->
            QuestionCard(question = q)
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
fun QuestionCard(
    question: com.example.data.model.PracticeQuestion,
    modifier: Modifier = Modifier
) {
    var revealed by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableIntStateOf(-1) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = question.type.label.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.question,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )

            // If MCQ, show choices
            if (question.type == QuestionType.MCQ && question.options.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    question.options.forEachIndexed { index, option ->
                        val isSelected = selectedOption == index
                        val isCorrect = index == question.correctOptionIndex
                        val optionColor = when {
                            revealed && isCorrect -> SuccessGreenLight
                            revealed && isSelected && !isCorrect -> AlertCoralLight
                            isSelected -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedOption = index
                                    revealed = true
                                },
                            shape = RoundedCornerShape(10.dp),
                            color = optionColor,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${('A' + index)}. ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = option,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Reveal Answer Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { revealed = !revealed }) {
                    Icon(
                        imageVector = if (revealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (revealed) "Hide Answer" else "Reveal Answer & Rationale", fontSize = 12.sp)
                }
            }

            AnimatedVisibility(visible = revealed) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "✅ Official Model Answer:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = SuccessGreen
                    )
                    Text(
                        text = question.officialAnswer,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🧠 Scientific Rationale: ${question.rationale}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (question.ministryExamTip.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "📌 Ministry Exam Tip: ${question.ministryExamTip}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ExamGold
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 4: Adaptive Learning & Evaluator (Flashcards)
// -------------------------------------------------------------
@Composable
fun AdaptiveFlashcardsStepContent(
    pack: MasterStudyPack
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var masteryScore by remember { mutableIntStateOf(0) }
    val total = pack.flashcards.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AgentHeaderBadge(
                agentNumber = 4,
                agentTitle = "Adaptive Learning & Evaluator",
                agentRole = "Spaced Repetition Flashcards • Active Recall • Mastery Progression"
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Card ${currentIndex + 1} of $total",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Mastery Score: $masteryScore",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = SuccessGreen
                )
            }
            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / total.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .clip(CircleShape)
            )
        }

        if (pack.flashcards.isNotEmpty()) {
            val card = pack.flashcards[currentIndex]
            item {
                AnimatedFlashcardItem(
                    flashcard = card,
                    onDifficultySelected = { rating ->
                        masteryScore += rating * 10
                        if (currentIndex < total - 1) {
                            currentIndex++
                        } else {
                            currentIndex = 0 // loop or restart
                        }
                    }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentIndex > 0) currentIndex--
                    },
                    enabled = currentIndex > 0
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Previous")
                }

                Button(
                    onClick = {
                        if (currentIndex < total - 1) currentIndex++
                    },
                    enabled = currentIndex < total - 1
                ) {
                    Text("Next")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

// -------------------------------------------------------------
// STEP 5: Assembly & Synthesis
// -------------------------------------------------------------
@Composable
fun SynthesisStepContent(
    pack: MasterStudyPack,
    onNavigateToExaminer: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(Color(0xFF004D40), Color(0xFF00796B))
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "STEP 5: MASTER SYNTHESIS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Icon(Icons.Default.Verified, contentDescription = null, tint = ExamGold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Complete Study Pack Assembled",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "All 4 Agent layers have synthesized this comprehensive learning pack according to Bloom's taxonomy and Egyptian Ministry standards.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE0F2F1),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📋 Executive Syllabus Summary",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Core Concept: ${pack.topicTitle} (${pack.unit.code})\n" +
                                "• Key Mechanisms: ${pack.keyTerms.size} official definitions aligned with Ministry curriculum\n" +
                                "• Diagrams: Monospace ASCII Schematics with inspection markers\n" +
                                "• Assessment Items: ${pack.questions.size} official-type practice questions with trap warnings\n" +
                                "• Spaced Repetition: ${pack.flashcards.size} active recall flashcards ready for Anki review",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onNavigateToExaminer,
                            colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("synthesis_grade_now_button")
                        ) {
                            Icon(Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Launch Exam Grader", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                val fullText = buildString {
                                    append("=== BIO-THANAWEYA STUDY PACK ===\n")
                                    append("${pack.topicTitle}\n\n")
                                    append(pack.deepTechnicalBreakdown)
                                }
                                clipboardManager.setText(AnnotatedString(fullText))
                                copied = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(if (copied) Icons.Default.Check else Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (copied) "Copied!" else "Copy", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}
