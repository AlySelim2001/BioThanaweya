package com.example.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ChapterProgress
import com.example.presentation.viewmodel.ChapterFilterMode
import com.example.presentation.viewmodel.ChaptersDashboardUiState
import com.example.presentation.viewmodel.ChaptersDashboardViewModel
import com.example.ui.theme.*

/**
 * ChaptersDashboardScreen:
 * Main dashboard UI displaying all 11 Egyptian Thanawya Amma Biology curriculum chapters
 * in an adaptive 2-column Grid layout with prominent Circular Progress Indicators,
 * complete RTL directionality for Arabic text, and offline persistence backed by ChapterProgress.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChaptersDashboardScreen(
    viewModel: ChaptersDashboardViewModel,
    onNavigateToTopic: (String) -> Unit = {},
    onNavigateToPractice: (String) -> Unit = {},
    onNavigateToExam: () -> Unit = {},
    onNavigateToFormulaLab: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    // Enforce full RTL layout direction for Arabic-first experience
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag("chapters_dashboard_scaffold"),
            topBar = {
                ChaptersDashboardTopAppBar(
                    overallPercentage = state.overallCompletionPercentage,
                    onSearchClick = onNavigateToSearch,
                    onResetClick = { showResetDialog = true }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = BioTealPrimary,
                            modifier = Modifier.testTag("dashboard_loading_indicator")
                        )
                    }
                } else {
                    ChaptersGridContent(
                        state = state,
                        onFilterChanged = { viewModel.setFilterMode(it) },
                        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                        onChapterClicked = { viewModel.selectChapterForDetails(it) },
                        onToggleCompletion = { viewModel.toggleChapterCompletion(it) },
                        onNavigateToSearch = onNavigateToSearch
                    )
                }

                // Chapter Details & Study Actions Bottom Sheet
                state.selectedChapterForDetails?.let { chapter ->
                    ChapterDetailsBottomSheet(
                        chapter = chapter,
                        onDismiss = { viewModel.selectChapterForDetails(null) },
                        onToggleCompletion = {
                            viewModel.toggleChapterCompletion(chapter.chapterId)
                        },
                        onRecordQuestion = {
                            viewModel.recordQuestionSolved(chapter.chapterId)
                        },
                        onRecordFlashcard = {
                            viewModel.recordFlashcardMastered(chapter.chapterId)
                        },
                        onStartPractice = {
                            viewModel.selectChapterForDetails(null)
                            onNavigateToPractice(chapter.chapterKey)
                        }
                    )
                }

                // Reset Confirmation Dialog
                if (showResetDialog) {
                    AlertDialog(
                        onDismissRequest = { showResetDialog = false },
                        icon = { Icon(Icons.Default.Refresh, contentDescription = null, tint = AlertCoral) },
                        title = {
                            Text(
                                text = "إعادة ضبط تقدم الفصول",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.Right
                            )
                        },
                        text = {
                            Text(
                                text = "هل أنت متأكد من رغبتك في إعادة ضبط تقدم الـ 11 فصلاً دراسياً إلى 0%؟ لا يمكن التراجع عن هذه العملية.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Right
                            )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.resetAllCurriculumProgress()
                                    showResetDialog = false
                                },
                                modifier = Modifier.testTag("confirm_reset_button")
                            ) {
                                Text("إعادة الضبط", color = AlertCoral, fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showResetDialog = false },
                                modifier = Modifier.testTag("cancel_reset_button")
                            ) {
                                Text("إلغاء")
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * Top App Bar with Arabic Title, curriculum badge, and reset action.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChaptersDashboardTopAppBar(
    overallPercentage: Float,
    onSearchClick: () -> Unit,
    onResetClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(BioTealPrimary, BioCyanAccent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Biotech,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = "منهج الأحياء • الثانوية العامة",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = "متابعة الـ 11 فصلاً دراسياً (أوفلاين)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("dashboard_search_action_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "البحث الفوري في محتوى المنهج",
                    tint = BioTealPrimary
                )
            }
            IconButton(
                onClick = onResetClick,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("dashboard_reset_progress_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "إعادة ضبط التقدم",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

/**
 * Main Grid Content displaying Overall Progress Banner, Filters, Search, and 11 Chapter Cards.
 */
@Composable
private fun ChaptersGridContent(
    state: ChaptersDashboardUiState,
    onFilterChanged: (ChapterFilterMode) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onChapterClicked: (ChapterProgress) -> Unit,
    onToggleCompletion: (Int) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 156.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("chapters_vertical_grid")
    ) {
        // Overall Curriculum Summary Card (spans full width)
        item(span = { GridItemSpan(maxLineSpan) }) {
            OverallCurriculumProgressCard(
                percentage = state.overallCompletionPercentage,
                completedCount = state.completedChaptersCount,
                totalCount = state.totalChaptersCount
            )
        }

        // Search Bar (spans full width)
        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("chapters_search_input"),
                placeholder = {
                    Text(
                        text = "ابحث في الفصول (مثال: الدعامة، التكاثر، DNA)...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "بحث",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChanged("") },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "مسح البحث")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )
        }

        // Full-Text Search Quick Callout Card (spans full width)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Card(
                onClick = onNavigateToSearch,
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BioTealPrimary.copy(alpha = 0.08f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        listOf(
                            BioTealPrimary.copy(alpha = 0.35f),
                            BioCyanAccent.copy(alpha = 0.2f)
                        )
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_open_full_text_search")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BioTealPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SavedSearch,
                                contentDescription = null,
                                tint = BioTealPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "البحث الفوري في كامل نصوص المنهج (Room FTS)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "ابحث بالمصطلحات والتراكيب وقوانين الوزارة بلا إنترنت",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = null,
                        tint = BioTealPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Filter Chips Row (spans full width)
        item(span = { GridItemSpan(maxLineSpan) }) {
            FilterChipsRow(
                currentFilter = state.filterMode,
                onFilterChanged = onFilterChanged,
                completedCount = state.completedChaptersCount,
                totalCount = state.totalChaptersCount
            )
        }

        // Section Title: Chapters Grid
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "فصول المنهج الدراسي (${state.filteredChapters.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (state.filterMode == ChapterFilterMode.ALL) "11 فصلاً معتمداً" else "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Empty state when search/filter yield no results
        if (state.filteredChapters.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                EmptyChaptersState(searchQuery = state.searchQuery)
            }
        } else {
            // Render 11 Chapter Cards in 2-column Adaptive Grid
            items(
                items = state.filteredChapters,
                key = { it.chapterId }
            ) { chapter ->
                ChapterGridCard(
                    chapter = chapter,
                    onClick = { onChapterClicked(chapter) },
                    onToggleCompletion = { onToggleCompletion(chapter.chapterId) }
                )
            }
        }

        // Footer spacing
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Overall Curriculum Progress Header Card with Circular Progress Ring.
 */
@Composable
private fun OverallCurriculumProgressCard(
    percentage: Float,
    completedCount: Int,
    totalCount: Int
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (percentage / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "overall_progress_anim"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("overall_progress_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = DeepNavy
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(DeepNavy, SurfaceDark)
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BioTealPrimary.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "معدل الإنجاز العام",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = BioCyanAccent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Text(
                        text = "إجمالي تقدم منهج الأحياء",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )

                    Text(
                        text = "$completedCount من $totalCount فصول مكتملة بالكامل",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "• الوحدة 1: 9 فصول",
                            style = MaterialTheme.typography.labelSmall,
                            color = BioCyanLight
                        )
                        Text(
                            text = "• الوحدة 2: فصلان",
                            style = MaterialTheme.typography.labelSmall,
                            color = MolecularPurpleLight
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Prominent Circular Progress Ring for Overall Progress
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .testTag("overall_circular_progress"),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1.0f },
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White.copy(alpha = 0.12f),
                        strokeWidth = 7.dp,
                        strokeCap = StrokeCap.Round
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = BioCyanAccent,
                        strokeWidth = 7.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${percentage.toInt()}%",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = "إنجاز",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Filter Chips Row for switching between All 11 Chapters, Unit 1, Unit 2, and Completed.
 */
@Composable
private fun FilterChipsRow(
    currentFilter: ChapterFilterMode,
    onFilterChanged: (ChapterFilterMode) -> Unit,
    completedCount: Int,
    totalCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("filter_chips_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = currentFilter == ChapterFilterMode.ALL,
            onClick = { onFilterChanged(ChapterFilterMode.ALL) },
            label = { Text("الكل ($totalCount)") },
            modifier = Modifier.testTag("filter_chip_all")
        )
        FilterChip(
            selected = currentFilter == ChapterFilterMode.UNIT_1,
            onClick = { onFilterChanged(ChapterFilterMode.UNIT_1) },
            label = { Text("الوحدة ١ (9)") },
            modifier = Modifier.testTag("filter_chip_unit1")
        )
        FilterChip(
            selected = currentFilter == ChapterFilterMode.UNIT_2,
            onClick = { onFilterChanged(ChapterFilterMode.UNIT_2) },
            label = { Text("الوحدة ٢ (2)") },
            modifier = Modifier.testTag("filter_chip_unit2")
        )
        FilterChip(
            selected = currentFilter == ChapterFilterMode.COMPLETED_ONLY,
            onClick = { onFilterChanged(ChapterFilterMode.COMPLETED_ONLY) },
            label = { Text("المكتملة ($completedCount)") },
            modifier = Modifier.testTag("filter_chip_completed")
        )
    }
}

/**
 * ChapterGridCard:
 * Individual Chapter Card in the 2-column Grid displaying:
 * - Chapter title (Arabic & English)
 * - Circular Progress Indicator with completion percentage
 * - Questions and Flashcards solved count
 * - Unit number & Chapter badge
 * - Completion status
 */
@Composable
fun ChapterGridCard(
    chapter: ChapterProgress,
    onClick: () -> Unit,
    onToggleCompletion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = chapter.progressFraction,
        animationSpec = tween(durationMillis = 600),
        label = "chapter_progress_anim_${chapter.chapterId}"
    )

    val progressColor = when {
        chapter.isCompleted || chapter.completionPercentage >= 100f -> BioSuccessGreen
        chapter.completionPercentage > 50f -> BioTealPrimary
        chapter.completionPercentage > 0f -> BioCyanAccent
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("chapter_card_${chapter.chapterId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Row: Chapter Badge + Unit Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (chapter.unitNumber == 1) BioTealPrimary.copy(alpha = 0.12f) else MolecularPurple.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "الفصل ${chapter.chapterId}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (chapter.unitNumber == 1) BioTealPrimary else MolecularPurple,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Completion status icon
                if (chapter.isCompleted || chapter.completionPercentage >= 100f) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "فصل مكتمل",
                        tint = BioSuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "الوحدة ${chapter.unitNumber}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Chapter Title in Arabic (Prominent)
            Text(
                text = chapter.titleAr,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("chapter_title_${chapter.chapterId}")
            )

            // English Subtitle
            Text(
                text = chapter.titleEn,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Center Circular Progress Indicator Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circular Progress Indicator with centered percentage text
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .testTag("circular_progress_${chapter.chapterId}"),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1.0f },
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 5.dp,
                        strokeCap = StrokeCap.Round
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = progressColor,
                        strokeWidth = 5.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${chapter.completionPercentage.toInt()}%",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Questions & Flashcards Micro-Metrics
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${chapter.questionsSolved}/${chapter.totalQuestions}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = "أسئلة",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${chapter.flashcardsMastered}/${chapter.totalFlashcards}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "بطاقات",
                            tint = MolecularPurple,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            // Quick completion toggle / indicator
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onToggleCompletion() },
                color = if (chapter.isCompleted) BioSuccessGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (chapter.isCompleted) Icons.Default.Check else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = if (chapter.isCompleted) BioSuccessGreen else MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (chapter.isCompleted) "مكتمل ✓" else "متابعة المذاكرة",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (chapter.isCompleted) BioSuccessGreen else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * Bottom Sheet for detailed chapter inspection and interactive learning actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChapterDetailsBottomSheet(
    chapter: ChapterProgress,
    onDismiss: () -> Unit,
    onToggleCompletion: () -> Unit,
    onRecordQuestion: () -> Unit,
    onRecordFlashcard: () -> Unit,
    onStartPractice: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .testTag("chapter_details_bottom_sheet"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "الفصل ${chapter.chapterId} • الوحدة ${chapter.unitNumber}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = chapter.titleAr,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = chapter.titleEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Large Circular Progress
                Box(
                    modifier = Modifier.size(68.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 6.dp
                    )
                    CircularProgressIndicator(
                        progress = { chapter.progressFraction },
                        modifier = Modifier.fillMaxSize(),
                        color = if (chapter.isCompleted) BioSuccessGreen else BioTealPrimary,
                        strokeWidth = 6.dp
                    )
                    Text(
                        text = "${chapter.completionPercentage.toInt()}%",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Summary Text
            if (chapter.summaryAr.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = chapter.summaryAr,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "أسئلة محلولة",
                    value = "${chapter.questionsSolved} / ${chapter.totalQuestions}",
                    icon = Icons.Default.Quiz
                )
                StatItem(
                    label = "بطاقات متقنة",
                    value = "${chapter.flashcardsMastered} / ${chapter.totalFlashcards}",
                    icon = Icons.Default.Layers
                )
                StatItem(
                    label = "وقت المذاكرة",
                    value = "${chapter.studyTimeMinutes} دقيقة",
                    icon = Icons.Default.Timer
                )
            }

            // Interactive Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRecordQuestion,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("record_question_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("حل سؤال (+1)")
                }

                OutlinedButton(
                    onClick = onRecordFlashcard,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("record_flashcard_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("إتقان بطاقة (+1)")
                }
            }

            Button(
                onClick = onToggleCompletion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("toggle_completion_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (chapter.isCompleted) MaterialTheme.colorScheme.errorContainer else BioTealPrimary
                )
            ) {
                Icon(
                    imageVector = if (chapter.isCompleted) Icons.Default.Clear else Icons.Default.Check,
                    contentDescription = null,
                    tint = if (chapter.isCompleted) MaterialTheme.colorScheme.onErrorContainer else Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (chapter.isCompleted) "إلغاء وضع الاكتمال" else "تعليم الفصل كمكتمل بنسبة 100%",
                    color = if (chapter.isCompleted) MaterialTheme.colorScheme.onErrorContainer else Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = BioTealPrimary, modifier = Modifier.size(20.dp))
        Text(value, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * Empty state for search/filter.
 */
@Composable
private fun EmptyChaptersState(searchQuery: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = if (searchQuery.isNotEmpty()) "لا توجد فصول مطابقة للبحث \"$searchQuery\"" else "لا توجد فصول متاحة في هذا التصنيف",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
