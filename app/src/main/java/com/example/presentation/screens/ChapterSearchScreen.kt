package com.example.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.repository.SearchResultItem
import com.example.presentation.viewmodel.ChapterSearchUiState
import com.example.presentation.viewmodel.ChapterSearchViewModel
import com.example.ui.theme.*
import com.example.util.ArabicSearchUtils

/**
 * ChapterSearchScreen:
 * Offline-first full-text search interface enabling instant keyword lookups
 * across all 11 Thanawya Amma biology curriculum chapters using Room database.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterSearchScreen(
    viewModel: ChapterSearchViewModel,
    onBack: () -> Unit,
    onNavigateToTopic: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag("chapter_search_scaffold"),
            topBar = {
                SearchTopAppBar(
                    query = state.query,
                    onQueryChanged = { viewModel.onQueryChanged(it) },
                    onClearQuery = { viewModel.clearQuery() },
                    onSearchSubmit = {
                        focusManager.clearFocus()
                        viewModel.commitSearch(it)
                    },
                    onBack = onBack
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Filter Chips (Units & Popular Keywords)
                    SearchFiltersSection(
                        selectedUnit = state.selectedUnit,
                        selectedChapterId = state.selectedChapterId,
                        popularKeywords = viewModel.popularKeywords,
                        recentSearches = state.recentSearches,
                        currentQuery = state.query,
                        onSelectUnit = { viewModel.setUnitFilter(it) },
                        onSelectChapter = { viewModel.setChapterFilter(it) },
                        onSelectKeyword = {
                            focusManager.clearFocus()
                            viewModel.selectKeyword(it)
                        },
                        onDeleteRecent = { viewModel.deleteRecentSearch(it) },
                        onClearRecent = { viewModel.clearRecentSearches() }
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 1.dp
                    )

                    // Results Status Bar
                    SearchResultsStatusBar(
                        resultCount = state.totalMatchesCount,
                        query = state.query,
                        isSearching = state.isSearching,
                        selectedUnit = state.selectedUnit,
                        selectedChapterId = state.selectedChapterId
                    )

                    // Results List or Empty State
                    if (state.results.isEmpty() && !state.isSearching) {
                        SearchEmptyState(
                            query = state.query,
                            onSuggestedKeyword = {
                                focusManager.clearFocus()
                                viewModel.selectKeyword(it)
                            }
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("search_results_list"),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(
                                items = state.results,
                                key = { it.content.id }
                            ) { resultItem ->
                                SearchResultCard(
                                    item = resultItem,
                                    searchQuery = state.query,
                                    onClick = {
                                        viewModel.selectDetailItem(resultItem)
                                    }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }
                }

                // Lesson Content Detail Modal Bottom Sheet
                state.selectedDetailItem?.let { detail ->
                    ChapterContentDetailBottomSheet(
                        item = detail,
                        onDismiss = { viewModel.selectDetailItem(null) },
                        onOpenTopic = {
                            viewModel.selectDetailItem(null)
                            onNavigateToTopic(detail.content.chapterKey)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Top App Bar with embedded instant search text field and back button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopAppBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSearchSubmit: (String) -> Unit,
    onBack: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("search_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "الرجوع"
                    )
                }

                TextField(
                    value = query,
                    onValueChange = onQueryChanged,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chapter_search_input_field"),
                    placeholder = {
                        Text(
                            text = "ابحث بالكلمات المفتاحية (مثال: أوكسينات، ساركومير...)",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "بحث",
                            tint = BioTealPrimary
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = onClearQuery,
                                modifier = Modifier.testTag("search_clear_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "مسح البحث"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearchSubmit(query) })
                )
            }
        }
    }
}

/**
 * Filter chips section: Units, Chapter Selector, and Popular Search Keywords.
 */
@Composable
private fun SearchFiltersSection(
    selectedUnit: Int?,
    selectedChapterId: Int?,
    popularKeywords: List<String>,
    recentSearches: List<String>,
    currentQuery: String,
    onSelectUnit: (Int?) -> Unit,
    onSelectChapter: (Int?) -> Unit,
    onSelectKeyword: (String) -> Unit,
    onDeleteRecent: (String) -> Unit,
    onClearRecent: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Unit Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = selectedUnit == null,
                onClick = { onSelectUnit(null) },
                label = { Text("المنهج كاملاً (11 فصلاً)", fontSize = 12.sp) },
                leadingIcon = {
                    if (selectedUnit == null) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                },
                modifier = Modifier.testTag("filter_unit_all")
            )

            FilterChip(
                selected = selectedUnit == 1,
                onClick = { onSelectUnit(if (selectedUnit == 1) null else 1) },
                label = { Text("الوحدة الأولى: التركيب والوظيفة", fontSize = 12.sp) },
                leadingIcon = {
                    if (selectedUnit == 1) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                },
                modifier = Modifier.testTag("filter_unit_1")
            )

            FilterChip(
                selected = selectedUnit == 2,
                onClick = { onSelectUnit(if (selectedUnit == 2) null else 2) },
                label = { Text("الوحدة 2: بيولوجيا جزيئية", fontSize = 12.sp) },
                leadingIcon = {
                    if (selectedUnit == 2) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                },
                modifier = Modifier.testTag("filter_unit_2")
            )
        }

        // Popular Biology Search Keywords Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "الأكثر بحثاً:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            popularKeywords.forEach { keyword ->
                val isSelected = currentQuery.trim() == keyword
                SuggestionChip(
                    onClick = { onSelectKeyword(keyword) },
                    label = {
                        Text(
                            text = keyword,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (isSelected) BioTealPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        labelColor = if (isSelected) BioTealPrimary else MaterialTheme.colorScheme.onSurface
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = if (isSelected) BioTealPrimary else Color.Transparent
                    )
                )
            }
        }

        // Recent Searches Row (if any exist)
        if (recentSearches.isNotEmpty() && currentQuery.isBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "سجل البحث:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                recentSearches.forEach { recent ->
                    InputChip(
                        selected = false,
                        onClick = { onSelectKeyword(recent) },
                        label = { Text(recent, fontSize = 11.sp) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "حذف من السجل",
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { onDeleteRecent(recent) }
                            )
                        }
                    )
                }

                TextButton(
                    onClick = onClearRecent,
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Text("مسح الكل", fontSize = 11.sp, color = AlertCoral)
                }
            }
        }
    }
}

/**
 * Status bar showing result count and active filters.
 */
@Composable
private fun SearchResultsStatusBar(
    resultCount: Int,
    query: String,
    isSearching: Boolean,
    selectedUnit: Int?,
    selectedChapterId: Int?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (resultCount > 0) SuccessGreen else AlertCoral)
            )

            Text(
                text = if (query.isBlank()) {
                    "جميع دروس ومفاهيم المنهج ($resultCount قسماً)"
                } else {
                    "نتائج البحث عن '$query': $resultCount نتيجة"
                },
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        if (isSearching) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                strokeWidth = 2.dp,
                color = BioTealPrimary
            )
        } else {
            Text(
                text = "بحث فوري بلا إنترنت ⚡",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = BioTealPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

/**
 * Individual Search Result Card with snippet highlighting and curriculum metadata.
 */
@Composable
private fun SearchResultCard(
    item: SearchResultItem,
    searchQuery: String,
    onClick: () -> Unit
) {
    val content = item.content

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("search_result_item_${content.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                colors = listOf(
                    BioTealPrimary.copy(alpha = 0.25f),
                    BioCyanAccent.copy(alpha = 0.15f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Chapter & Unit badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chapter Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BioTealPrimary.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = BioTealPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "فصل ${content.chapterId}: ${content.chapterTitleAr}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BioTealPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Unit Chip
                Text(
                    text = if (content.unitNumber == 1) "الوحدة 1" else "الوحدة 2",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            // Section Title
            Text(
                text = content.sectionTitleAr,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Contextual Matched Snippet with Highlight
            val highlightedSnippet = remember(item.matchedSnippet, searchQuery) {
                buildAnnotatedSnippet(item.matchedSnippet, searchQuery)
            }

            Text(
                text = highlightedSnippet,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Ministry Exam Trap / Secret Banner
            if (content.examNotesAr.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ExamGold.copy(alpha = 0.12f))
                        .border(1.dp, ExamGold.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = ExamGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = content.examNotesAr,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Footer: Keywords & Click to expand
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Keywords Preview
                val keywordsList = remember(content.keywordsAr) {
                    content.keywordsAr.split(" ").filter { it.isNotBlank() }.take(3)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    keywordsList.forEach { kw ->
                        Text(
                            text = "#$kw",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.5.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "التفاصيل",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BioTealPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = null,
                        tint = BioTealPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Builds an AnnotatedString highlighting any words from search query.
 */
private fun buildAnnotatedSnippet(text: String, query: String) = buildAnnotatedString {
    if (query.isBlank()) {
        append(text)
        return@buildAnnotatedString
    }

    val tokens = ArabicSearchUtils.normalize(query)
        .split(" ")
        .filter { it.isNotBlank() }

    if (tokens.isEmpty()) {
        append(text)
        return@buildAnnotatedString
    }

    // Split words and match normalized variants
    val words = text.split(Regex("(?<=\\s)|(?=\\s)"))
    for (w in words) {
        val normWord = ArabicSearchUtils.normalize(w)
        val isMatched = tokens.any { token -> normWord.contains(token) }

        if (isMatched && w.isNotBlank()) {
            withStyle(
                style = SpanStyle(
                    color = BioTealPrimary,
                    background = BioTealPrimary.copy(alpha = 0.15f),
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(w)
            }
        } else {
            append(w)
        }
    }
}

/**
 * Empty search results state with helpful guidance and suggestions.
 */
@Composable
private fun SearchEmptyState(
    query: String,
    onSuggestedKeyword: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(BioTealPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SearchOff,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = BioTealPrimary
                )
            }

            Text(
                text = if (query.isNotBlank()) {
                    "لم يتم العثور على نتائج مطابقة لـ '$query'"
                } else {
                    "ابحث في محتوى فصول الأحياء الـ 11"
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Text(
                text = if (query.isNotBlank()) {
                    "تأكد من كتابة الكلمة بشكل صحيح، أو جرّب أحد المصطلحات الوزارية الشائعة:"
                } else {
                    "يمكنك البحث عن أي عضو، إنزيم، هرمون، قانون، أو تجربة في المنهج:"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Suggestions
            val suggestions = listOf("أوكسينات", "ساركومير", "دورة كريبس", "DNA", "جسم مضاد", "نيفرون")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                suggestions.forEach { suggestion ->
                    FilledTonalButton(
                        onClick = { onSuggestedKeyword(suggestion) },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(suggestion, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

/**
 * Comprehensive Bottom Sheet for full syllabus section breakdown.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChapterContentDetailBottomSheet(
    item: SearchResultItem,
    onDismiss: () -> Unit,
    onOpenTopic: () -> Unit
) {
    val content = item.content

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Chapter and Section Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "الفصل ${content.chapterId}: ${content.chapterTitleAr}",
                        style = MaterialTheme.typography.labelMedium.copy(color = BioTealPrimary)
                    )
                    Text(
                        text = content.sectionTitleAr,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق")
                }
            }

            HorizontalDivider()

            // Scrollable Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Full Content Body
                item {
                    Text(
                        text = "الشرح التفصيلي للمفهوم:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = content.contentAr,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 24.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Ministry Exam Secrets & Traps
                if (content.examNotesAr.isNotBlank()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = ExamGold.copy(alpha = 0.12f)
                            ),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.linearGradient(listOf(ExamGold, ExamGold))
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = ExamGold)
                                    Text(
                                        text = "تنبيهات وأسرار امتحانات الثانوية العامة:",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = ExamGold
                                    )
                                }
                                Text(
                                    text = content.examNotesAr,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Formulas & Scientific Rules
                if (content.scientificFormulas.isNotBlank()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = BioCyanAccent.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Functions, contentDescription = null, tint = BioTealPrimary)
                                    Text(
                                        text = "القوانين والعلاقات الرياضية / البيولوجية:",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = BioTealPrimary
                                    )
                                }
                                Text(
                                    text = content.scientificFormulas,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Keywords Tags
                item {
                    Text(
                        text = "الكلمات المفتاحية:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val keywords = content.keywordsAr.split(" ").filter { it.isNotBlank() }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        keywords.forEach { kw ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(kw, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }

            // Bottom Action
            Button(
                onClick = onOpenTopic,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_detail_open_topic_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BioTealPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.MenuBook, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("فتح بطاقة الفصل ومتابعة الدراسة", fontWeight = FontWeight.Bold)
            }
        }
    }
}
