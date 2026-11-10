package com.example.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.fsrs.FSRSRating
import com.example.presentation.viewmodel.FsrsReviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FsrsReviewScreen(
    viewModel: FsrsReviewViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "مراجعة الفلاش كاردز (FSRS)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("fsrs_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.resetSession() },
                        modifier = Modifier.testTag("fsrs_reset_btn")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "إعادة الجلسة")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.cards.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("لا توجد بطاقات متاحة حالياً للمراجعة.")
                }
                return@Scaffold
            }

            if (uiState.isSessionComplete) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                        .testTag("fsrs_session_complete_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "🎉 أحسنت صنعاً!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "تم إكمال مراجعة جميع بطاقات هذا الموضوع وحفظ فترات الاستقرار في خوارزمية FSRS.",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onBack,
                            modifier = Modifier.testTag("fsrs_complete_finish_btn")
                        ) {
                            Text("العودة إلى لوحة التحكم")
                        }
                    }
                }
                return@Scaffold
            }

            // Progress Bar
            val total = uiState.cards.size
            val current = uiState.currentIndex + 1
            val progress = (current.toFloat() / total.toFloat()).coerceIn(0f, 1f)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("البطاقة $current من $total", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("${(progress * 100).toInt()}%", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Flip Card View
            val cardItem = uiState.cards.getOrNull(uiState.currentIndex)
            if (cardItem != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable { viewModel.flipCurrentCard() }
                        .testTag("fsrs_active_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (cardItem.isFlipped)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (cardItem.isFlipped) "الإجابة النموذجية:" else "السؤال / المفهوم:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (cardItem.isFlipped)
                                    cardItem.flashcard.backAnswer
                                else
                                    cardItem.flashcard.frontPrompt,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                lineHeight = 24.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = if (cardItem.isFlipped) "اضغط لإخفاء الإجابة" else "اضغط لكشف الإجابة والتقييم",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // FSRS Rating Controls
                if (cardItem.isFlipped) {
                    Text(
                        "كيف كان مستوى تذكرك للإجابة؟",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.rateCurrentCard(FSRSRating.AGAIN) },
                            modifier = Modifier.weight(1f).testTag("fsrs_rating_again"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("نسيت (Again)", fontSize = 10.sp)
                        }
                        Button(
                            onClick = { viewModel.rateCurrentCard(FSRSRating.HARD) },
                            modifier = Modifier.weight(1f).testTag("fsrs_rating_hard"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                        ) {
                            Text("صعب (Hard)", fontSize = 10.sp)
                        }
                        Button(
                            onClick = { viewModel.rateCurrentCard(FSRSRating.GOOD) },
                            modifier = Modifier.weight(1f).testTag("fsrs_rating_good"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("جيد (Good)", fontSize = 10.sp)
                        }
                        Button(
                            onClick = { viewModel.rateCurrentCard(FSRSRating.EASY) },
                            modifier = Modifier.weight(1f).testTag("fsrs_rating_easy"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("سهل (Easy)", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}
