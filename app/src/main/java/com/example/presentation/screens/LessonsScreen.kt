package com.example.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Chapter
import com.example.data.model.SubTopic
import com.example.data.model.UnitCategory
import com.example.data.repository.BiologyCurriculumRepository
import com.example.ui.theme.*

/**
 * شاشة الدروس والمحتوى التعليمي (Lessons / Curriculum Prototype)
 * تصميم متكامل وفق معايير Material Design 3 مع دعم كامل للغة العربية (RTL)
 * يتيح للطالب استعراض وحدات وفصول منهج الأحياء للثانوية العامة المصرية والتنقل السلس.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsScreen(
    onNavigateToTopic: (String) -> Unit,
    onNavigateToPractice: (String) -> Unit,
    onNavigateToFsrs: (String) -> Unit,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedUnitFilter by remember { mutableStateOf<UnitCategory?>(null) }
    var expandedChapterId by remember { mutableStateOf<String?>("unit1_chap1") }

    val chapters = remember { BiologyCurriculumRepository.chapters }
    val subTopics = remember { BiologyCurriculumRepository.subTopics }

    val filteredChapters = remember(searchQuery, selectedUnitFilter) {
        chapters.filter { chap ->
            val matchesUnit = selectedUnitFilter == null || chap.unit == selectedUnitFilter
            val matchesQuery = searchQuery.isBlank() ||
                    chap.arabicTitle.contains(searchQuery, ignoreCase = true) ||
                    chap.title.contains(searchQuery, ignoreCase = true) ||
                    subTopics.any { sub ->
                        sub.chapterId == chap.id && (
                                sub.arabicTitle.contains(searchQuery, ignoreCase = true) ||
                                sub.title.contains(searchQuery, ignoreCase = true)
                        )
                    }
            matchesUnit && matchesQuery
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "الدروس والمحتوى التعليمي",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "منهج الأحياء للثانوية العامة • خريطة الوحدات والفصول",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("btn_lessons_back")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // شريط البحث الموجه
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_lessons_search"),
                        placeholder = { Text("ابحث عن درس، فصل، أو مفهوم علمي...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "بحث", tint = MaterialTheme.colorScheme.primary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "مسح")
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        )
                    )
                }

                // فلترة الوحدات الدراسية عبر Chips
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedUnitFilter == null,
                                onClick = { selectedUnitFilter = null },
                                label = { Text("جميع الوحدات (${chapters.size})") },
                                leadingIcon = if (selectedUnitFilter == null) {
                                    { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedUnitFilter == UnitCategory.UNIT_1,
                                onClick = { selectedUnitFilter = UnitCategory.UNIT_1 },
                                label = { Text("الوحدة الأولى: التركيب والوظيفة") },
                                leadingIcon = if (selectedUnitFilter == UnitCategory.UNIT_1) {
                                    { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedUnitFilter == UnitCategory.UNIT_2,
                                onClick = { selectedUnitFilter = UnitCategory.UNIT_2 },
                                label = { Text("الوحدة الثانية: البيولوجيا الجزيئية") },
                                leadingIcon = if (selectedUnitFilter == UnitCategory.UNIT_2) {
                                    { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                    }
                }

                // بطاقة الإحصائيات التعليمية السريعة
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "تغطية المنهج الوزاري المصري 2025",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "12 فصلاً معتمداً • شرح تفاعلي متعدد الوكلاء الذكائيين • بنك أسئلة وفخاخ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }

                // قائمة الفصول الدراسية والموضوعات الفرعية (Accordion Style)
                items(filteredChapters, key = { it.id }) { chapter ->
                    val isExpanded = expandedChapterId == chapter.id
                    val chapterSubTopics = subTopics.filter { it.chapterId == chapter.id }

                    ChapterAccordionCard(
                        chapter = chapter,
                        subTopics = chapterSubTopics,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            expandedChapterId = if (isExpanded) null else chapter.id
                        },
                        onNavigateToTopic = onNavigateToTopic,
                        onNavigateToPractice = onNavigateToPractice,
                        onNavigateToFsrs = onNavigateToFsrs
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun ChapterAccordionCard(
    chapter: Chapter,
    subTopics: List<SubTopic>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onNavigateToTopic: (String) -> Unit,
    onNavigateToPractice: (String) -> Unit,
    onNavigateToFsrs: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("chapter_card_${chapter.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 3.dp else 1.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(
                    if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )
            )
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // شريط رأس الفصل (Header)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // أيقونة ورقم الفصل
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${chapter.number}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isExpanded) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chapter.arabicTitle,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = chapter.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onToggleExpand) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "طي الفصل" else "توسيع الفصل",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // محتوى الفصل عند التوسيع
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // ملخص الفصل
                    Text(
                        text = chapter.summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // الدروس والموضوعات المندرجة تحت الفصل
                    if (subTopics.isEmpty()) {
                        Text(
                            text = "لا توجد موضوعات تفصيلية مسجلة لهذا الفصل حالياً.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        subTopics.forEach { subTopic ->
                            SubTopicItemCard(
                                subTopic = subTopic,
                                onNavigateToTopic = onNavigateToTopic,
                                onNavigateToPractice = onNavigateToPractice,
                                onNavigateToFsrs = onNavigateToFsrs
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun SubTopicItemCard(
    subTopic: SubTopic,
    onNavigateToTopic: (String) -> Unit,
    onNavigateToPractice: (String) -> Unit,
    onNavigateToFsrs: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = subTopic.arabicTitle,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (subTopic.highYieldFlag) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Badge(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            ) {
                                Text(text = "تركيز وزاري", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subTopic.summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // أزرار العمليات المباشرة للدرس
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onNavigateToTopic(subTopic.id) },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(38.dp)
                        .testTag("btn_start_lesson_${subTopic.id}"),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("شرح الدرس", style = MaterialTheme.typography.labelMedium)
                }

                OutlinedButton(
                    onClick = { onNavigateToPractice(subTopic.id) },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("btn_practice_topic_${subTopic.id}"),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("تدريب", style = MaterialTheme.typography.labelMedium)
                }

                OutlinedButton(
                    onClick = { onNavigateToFsrs(subTopic.id) },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("btn_fsrs_topic_${subTopic.id}"),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.Style, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("FSRS", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
