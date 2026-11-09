package com.example.presentation.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UnitCategory
import com.example.domain.engine.BilingualBiologyGlossaryProvider
import com.example.domain.engine.GlossaryEntry
import com.example.ui.theme.*
import com.example.util.TextToSpeechHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BilingualGlossaryScreen(
    onBack: () -> Unit,
    onNavigateToTopic: (String) -> Unit,
    onNavigateToPractice: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedUnitFilter by remember { mutableStateOf<UnitCategory?>(null) }
    var expandedEntryId by remember { mutableStateOf<String?>("sarcomere") }

    // TTS
    var isTtsSpeaking by remember { mutableStateOf(false) }
    val ttsHelper = remember {
        TextToSpeechHelper(context) { speaking -> isTtsSpeaking = speaking }
    }
    DisposableEffect(Unit) {
        onDispose { ttsHelper.shutdown() }
    }

    val entries = remember(searchQuery, selectedUnitFilter) {
        BilingualBiologyGlossaryProvider.search(searchQuery, selectedUnitFilter)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("bilingual_glossary_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "المسرد البيولوجي المفهومي الذكي",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Bilingual Conceptual Lexicon & Ministry Traps",
                            style = MaterialTheme.typography.labelSmall,
                            color = BioTealPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("glossary_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search OutlinedTextField
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("glossary_search_input"),
                placeholder = { Text("ابحث بالمصطلح (عربي أو English أو فخاخ الوزارة)...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = BioTealPrimary)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BioTealPrimary,
                    cursorColor = BioTealPrimary
                )
            )

            // Unit Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedUnitFilter == null,
                    onClick = { selectedUnitFilter = null },
                    label = { Text("الكل (${BilingualBiologyGlossaryProvider.entries.size})") },
                    modifier = Modifier.testTag("glossary_filter_all")
                )
                FilterChip(
                    selected = selectedUnitFilter == UnitCategory.UNIT_1,
                    onClick = { selectedUnitFilter = UnitCategory.UNIT_1 },
                    label = { Text("الوحدة الأولى: التركيب والوظيفة") },
                    modifier = Modifier.testTag("glossary_filter_unit1")
                )
                FilterChip(
                    selected = selectedUnitFilter == UnitCategory.UNIT_2,
                    onClick = { selectedUnitFilter = UnitCategory.UNIT_2 },
                    label = { Text("الوحدة الثانية: البيولوجيا الجزيئية") },
                    modifier = Modifier.testTag("glossary_filter_unit2")
                )
            }

            // Results count badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "عدد المصطلحات المطابقة: ${entries.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "منهج الثانوية العامة ومدارس اللغات",
                    style = MaterialTheme.typography.labelSmall,
                    color = MolecularPurple
                )
            }

            // Entries List
            if (entries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "لم يتم العثور على مصطلح يطابق بحثك.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("glossary_list"),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(entries, key = { it.id }) { item ->
                        val isExpanded = expandedEntryId == item.id

                        GlossaryItemCard(
                            entry = item,
                            isExpanded = isExpanded,
                            onToggleExpand = {
                                expandedEntryId = if (isExpanded) null else item.id
                            },
                            onSpeakText = { text ->
                                ttsHelper.speak(text)
                            },
                            onNavigateToTopic = onNavigateToTopic,
                            onNavigateToPractice = onNavigateToPractice
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GlossaryItemCard(
    entry: GlossaryEntry,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onSpeakText: (String) -> Unit,
    onNavigateToTopic: (String) -> Unit,
    onNavigateToPractice: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isExpanded) 1.5.dp else 0.5.dp,
                color = if (isExpanded) BioTealPrimary else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onToggleExpand() }
            .testTag("glossary_item_${entry.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Arabic & English Term
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.termAr,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = entry.termEn,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = BioTealPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onSpeakText("${entry.termAr}. ${entry.definitionAr}") },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = "Pronounce",
                            tint = MolecularPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onToggleExpand,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse" else "Expand"
                        )
                    }
                }
            }

            // Chapter Tag Chip
            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "📖 ${entry.chapterTitleAr}",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = entry.unit.title,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Concise Arabic Definition (always visible)
            Text(
                text = entry.definitionAr,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp
            )

            // Expanded Section
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // English Definition
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "English Academic Definition:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = entry.definitionEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Ministry Exam Trap Callout Box
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ExamGold.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ExamGold.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Default.WarningAmber,
                                contentDescription = "Exam Trap",
                                tint = ExamGold,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "فخ امتحان الثانوية العامة (Ministry Trap):",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = entry.ministryTrap,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Formula or Key Rule if available
                    if (entry.formulaOrKeyRule != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MolecularPurple.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Calculate,
                                    contentDescription = null,
                                    tint = MolecularPurple,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "القاعدة الرياضية: ${entry.formulaOrKeyRule}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MolecularPurple
                                )
                            }
                        }
                    }

                    // Navigation Action Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onNavigateToTopic(entry.relatedTopicId) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("شرح الفصل", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onNavigateToPractice(entry.relatedTopicId) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary)
                        ) {
                            Icon(Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تدريب الوزارة", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
