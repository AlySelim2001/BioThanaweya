package com.example.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.example.presentation.components.AnatomicalDissectionCard
import com.example.presentation.components.AsciiSchematicBox
import com.example.presentation.components.EmpatheticCompanionCard
import com.example.presentation.components.SevenAgentBadge
import com.example.presentation.viewmodel.TopicUiState
import com.example.presentation.viewmodel.TopicViewModel
import com.example.ui.components.ComparisonTableCard
import com.example.ui.theme.*
import com.example.util.TextToSpeechHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(
    viewModel: TopicViewModel,
    onBack: () -> Unit,
    onNavigateToPractice: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Educator, 1: Visuals, 2: PBL

    // Built-in Text-To-Speech
    var isTtsSpeaking by remember { mutableStateOf(false) }
    val ttsHelper = remember {
        TextToSpeechHelper(
            context = context,
            onSpeechStatusChange = { speaking ->
                isTtsSpeaking = speaking
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            ttsHelper.shutdown()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    when (val s = state) {
                        is TopicUiState.Success -> {
                            Column {
                                Text(
                                    text = s.studyPack.topicTitle,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1
                                )
                                Text(
                                    text = s.studyPack.chapterTitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        else -> Text("Concept Breakdown")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("topic_detail_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state is TopicUiState.Success) {
                        val pack = (state as TopicUiState.Success).studyPack
                        IconButton(onClick = { onNavigateToPractice(pack.topicId) }) {
                            Icon(Icons.Default.FactCheck, contentDescription = "Go to Practice", tint = BioTealPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        when (val s = state) {
            is TopicUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BioTealPrimary)
                }
            }
            is TopicUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is TopicUiState.Success -> {
                val pack = s.studyPack
                val isArabic = s.activeExplanationMode == "eli5"
                val textToRead = if (isArabic) {
                    pack.arabicEli5.ifBlank { pack.eli5Analogy }
                } else {
                    pack.deepTechnicalBreakdown
                }

                Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    // English Tabs
                    PrimaryTabRow(selectedTabIndex = selectedTab) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Educator (Deep / ELI5)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Visuals (ASCII / 3D)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("Projects (PBL)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.Engineering, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        when (selectedTab) {
                            0 -> {
                                // Agent 5: Empathetic Student Companion & Formative Diagnostic
                                item {
                                    var companionSession by remember(pack.topicId) {
                                        mutableStateOf(
                                            com.example.domain.engine.EmpatheticCompanionEngine.createDiagnosticSession(pack.topicId)
                                        )
                                    }
                                    EmpatheticCompanionCard(
                                        session = companionSession,
                                        onSentimentChanged = { sentiment ->
                                            companionSession = com.example.domain.engine.EmpatheticCompanionEngine.createDiagnosticSession(
                                                topicId = pack.topicId,
                                                studentInput = sentiment.name,
                                                historicalAccuracy = if (companionSession.detectedComprehensionLevel == com.example.domain.model.StudentComprehensionLevel.MASTERED) 0.9 else 0.6
                                            ).copy(initialSentiment = sentiment)
                                        },
                                        onExportAnkiJson = {
                                            val json = com.example.domain.engine.AnkiExportHelper.exportToAnkiJson(pack.topicTitle, pack.flashcards)
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("Anki Deck", json)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "تم نسخ بطاقات Anki (JSON) للحافظة بنجاح!", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }

                                // Agent 1: Educator
                                item {
                                    SevenAgentBadge(1, "Master Biology Educator Agent", "Dual-Layer Scaffolding & Bloom's Progression")
                                }

                                // Textbook Page Citation Chip
                                item {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.AutoStories,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = pack.textbookPageReference.ifBlank { "Ministry Textbook Review" },
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                }

                                // Bilingual Explanation Mode Switcher & TTS Row
                                item {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Card(
                                            shape = RoundedCornerShape(14.dp),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(4.dp),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Button(
                                                    onClick = {
                                                        if (isTtsSpeaking) ttsHelper.stop()
                                                        viewModel.setExplanationMode("technical")
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (s.activeExplanationMode == "technical") MolecularPurple else Color.Transparent,
                                                        contentColor = if (s.activeExplanationMode == "technical") Color.White else MaterialTheme.colorScheme.onSurface
                                                    ),
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier.weight(1f).testTag("toggle_technical_btn")
                                                ) {
                                                    Icon(Icons.Default.Biotech, contentDescription = null, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("English Deep Dive", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                }

                                                Button(
                                                    onClick = {
                                                        if (isTtsSpeaking) ttsHelper.stop()
                                                        viewModel.setExplanationMode("eli5")
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (s.activeExplanationMode == "eli5") BioTealPrimary else Color.Transparent,
                                                        contentColor = if (s.activeExplanationMode == "eli5") Color.White else MaterialTheme.colorScheme.onSurface
                                                    ),
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier.weight(1f).testTag("toggle_eli5_btn")
                                                ) {
                                                    Text("🧸 Arabic ELI5", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }

                                        // Text-To-Speech Listen Button
                                        Button(
                                            onClick = {
                                                if (isTtsSpeaking) {
                                                    ttsHelper.stop()
                                                } else {
                                                    ttsHelper.speak(textToRead, isArabic)
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isTtsSpeaking) AlertCoral else BioTealPrimary.copy(alpha = 0.9f)
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth().testTag("tts_listen_btn")
                                        ) {
                                            Icon(
                                                imageVector = if (isTtsSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                                contentDescription = "Listen to explanation",
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (isTtsSpeaking) "Stop Audio Narration" else if (isArabic) "استمع إلى الشرح الصوتي (Arabic Audio)" else "Listen to Deep Dive (English Audio)",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                // Explanation Card with Automatic RTL / LTR
                                item {
                                    if (s.activeExplanationMode == "eli5") {
                                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                            Card(
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(containerColor = BioCyanLight.copy(alpha = 0.6f)),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, BioCyanAccent.copy(alpha = 0.4f))
                                            ) {
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = "🧸 شرح مبسط وتشبيه من واقع الحياة:",
                                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                            color = DeepNavy
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                    Text(
                                                        text = pack.arabicEli5.ifBlank { pack.eli5Analogy },
                                                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                                                        color = DeepNavy
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                            Card(
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                                            ) {
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.Biotech, contentDescription = null, tint = MolecularPurple, modifier = Modifier.size(20.dp))
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                            text = "Rigorous Scientific Mechanism:",
                                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                    Text(
                                                        text = pack.deepTechnicalBreakdown,
                                                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // Key Scientific Terms
                                if (pack.keyTerms.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "Key Scientific Terminology:",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    items(pack.keyTerms) { kt ->
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                    Text(kt.term, fontWeight = FontWeight.Bold, color = BioTealPrimary)
                                                    if (kt.arabicTerm.isNotBlank()) {
                                                        Text(kt.arabicTerm, fontWeight = FontWeight.Bold, color = MolecularPurple, fontSize = 12.sp)
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(kt.definition, style = MaterialTheme.typography.bodySmall)
                                            }
                                        }
                                    }
                                }

                                // Comparison Tables
                                items(pack.comparisonTables) { table ->
                                    ComparisonTableCard(table = table)
                                }
                            }
                            1 -> {
                                // Agent 2: Visual Architect
                                item {
                                    SevenAgentBadge(2, "Visual Learning Architect", "AI Diagram Generator + ASCII Schematics + 3D Markers")
                                }

                                // AI-Driven Image Generation Flow for Biological Schematics
                                item {
                                    val diagramVm = remember(pack.topicId) {
                                        com.example.presentation.viewmodel.BiologicalDiagramViewModel(
                                            topicId = pack.topicId,
                                            visualArchitectUseCase = viewModel.visualAgent
                                        )
                                    }
                                    com.example.presentation.components.BiologicalDiagramGeneratorCard(
                                        viewModel = diagramVm
                                    )
                                }

                                // Interactive Mind-Mapping & Mermaid Engine
                                item {
                                    val mindMap = remember(pack.topicId) {
                                        com.example.domain.engine.BiologyMindMapProvider.getMindMapForTopic(pack.topicId)
                                    }
                                    com.example.presentation.components.InteractiveMindMapCard(
                                        mindMap = mindMap
                                    )
                                }

                                // Anatomical Dissection Protocol (Agent 2)
                                item {
                                    val dissection = remember(pack.topicId) {
                                        com.example.domain.engine.AnatomicalDissectionProvider.getProtocolForTopic(pack.topicId)
                                    }
                                    AnatomicalDissectionCard(protocol = dissection)
                                }

                                // ASCII Schematic (Strictly LTR Monospace)
                                item {
                                    AsciiSchematicBox(
                                        title = "Structural Schematic (ASCII)",
                                        asciiContent = s.visualAssets.asciiSchematic,
                                        caption = s.visualAssets.schematicCaption
                                    )
                                }

                                // Visual Inspection Markers
                                item {
                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = ExamGoldLight.copy(alpha = 0.6f)),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, ExamGold.copy(alpha = 0.4f))
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text("👁️ Diagram Inspection Markers for Ministry Exam:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy))
                                            Spacer(modifier = Modifier.height(6.dp))
                                            s.visualAssets.inspectionMarkers.forEach { m ->
                                                Text("• $m", fontSize = 12.sp, color = DeepNavy, modifier = Modifier.padding(vertical = 2.dp))
                                            }
                                        }
                                    }
                                }

                                // 3D YouTube Videos Launcher
                                items(s.visualAssets.youtube3DQueries) { v ->
                                    Card(
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, AlertCoral.copy(alpha = 0.3f))
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(10.dp)).background(AlertCoral.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.PlayArrow, contentDescription = "Watch", tint = AlertCoral)
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(v.query, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                Text(v.topicDescription, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Button(
                                                onClick = {
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=" + Uri.encode(v.query)))
                                                    context.startActivity(intent)
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = AlertCoral),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                modifier = Modifier.height(32.dp)
                                            ) {
                                                Text("Watch 3D", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                            2 -> {
                                // Agent 6: PBL
                                item {
                                    SevenAgentBadge(6, "Problem-Based Learning Agent", "Real-World Clinical & Biotechnology Missions")
                                }

                                val pbl = s.pblProject
                                item {
                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF004D40).copy(alpha = 0.12f)),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00796B))
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(pbl.titleArabic, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Clinical Scenario: ${pbl.scenario}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text("Mission Target: ${pbl.mission}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                }

                                item {
                                    Text("Scientific Evaluation Rubrics:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                }

                                items(pbl.rubrics) { r ->
                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text(r.criterion, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                Text("${r.weightPoints}%", fontWeight = FontWeight.ExtraBold, color = BioTealPrimary, fontSize = 12.sp)
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("Exceeds Standards: ${r.excellentDescription}", fontSize = 11.sp, color = BioSuccessGreen)
                                            Text("Needs Revision: ${r.poorDescription}", fontSize = 11.sp, color = AlertCoral)
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
}
