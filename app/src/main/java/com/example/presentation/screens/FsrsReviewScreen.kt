package com.example.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.fsrs.FSRSRating
import com.example.presentation.components.SevenAgentBadge
import com.example.presentation.viewmodel.FsrsReviewViewModel
import com.example.presentation.viewmodel.FsrsUiState
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FsrsReviewScreen(
    viewModel: FsrsReviewViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Smart Spaced Review (FSRS-Kotlin)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("Agent 4 • Free Spaced Repetition Scheduler v5", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("fsrs_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        when (val s = state) {
            is FsrsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BioTealPrimary)
                }
            }
            is FsrsUiState.AllDone -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BioSuccessGreen, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Well Done! All Cards Completed", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("The FSRS-Kotlin engine has scheduled your optimal future review dates based on memory stability and retrievability.", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = onBack, shape = RoundedCornerShape(12.dp)) {
                            Text("Back to Dashboard")
                        }
                    }
                }
            }
            is FsrsUiState.Reviewing -> {
                val currentItem = s.queue[s.currentIndex]
                val card = currentItem.card

                val rotation by animateFloatAsState(
                    targetValue = if (s.isFlipped) 180f else 0f,
                    animationSpec = tween(durationMillis = 350),
                    label = "cardFlip"
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        SevenAgentBadge(
                            agentNumber = 4,
                            agentTitleArabic = "FSRS-Kotlin Memory Engine",
                            agentRoleEnglish = "Stability, Difficulty & Retrievability Optimization"
                        )
                    }

                    // Progress Header
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Card ${s.currentIndex + 1} of ${s.queue.size}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = BioSuccessGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${s.cardsReviewedToday} reviewed today",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BioSuccessGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        LinearProgressIndicator(
                            progress = { (s.currentIndex + 1).toFloat() / s.queue.size.toFloat() },
                            modifier = Modifier.fillMaxWidth().padding(top = 6.dp).clip(CircleShape)
                        )
                    }

                    // FSRS 3D Flip Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clickable { viewModel.flipCard() }
                                .graphicsLayer {
                                    rotationY = rotation
                                    cameraDistance = 12f * density
                                }
                                .testTag("fsrs_flip_card"),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (rotation > 90f) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (rotation <= 90f) {
                                    // Front Prompt
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                            ) {
                                                Text(card.category.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                            }
                                            Text("Tap to Flip 🔄", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                        }

                                        Text(
                                            text = card.frontPrompt,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Text("Active Recall Question", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                    }
                                } else {
                                    // Back Answer
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxSize().graphicsLayer { rotationY = 180f }
                                    ) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = BioSuccessGreen.copy(alpha = 0.2f)
                                            ) {
                                                Text("Model Answer", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BioSuccessGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                            }
                                            Text("Tap to Flip 🔄", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                        }

                                        Text(
                                            text = card.backAnswer,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )

                                        Text("Rate recall difficulty below 👇", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                    }
                                }
                            }
                        }
                    }

                    // 4 FSRS Rating Buttons
                    item {
                        Text("Rate Recall for FSRS Scheduler:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.rateCard(FSRSRating.AGAIN) },
                                colors = ButtonDefaults.buttonColors(containerColor = AlertCoral),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(44.dp).testTag("fsrs_rate_again")
                            ) {
                                Text("Again\n(Forgot)", fontSize = 10.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { viewModel.rateCard(FSRSRating.HARD) },
                                colors = ButtonDefaults.buttonColors(containerColor = ExamGold),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(44.dp).testTag("fsrs_rate_hard")
                            ) {
                                Text("Hard\n(Struggled)", fontSize = 10.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = DeepNavy)
                            }

                            Button(
                                onClick = { viewModel.rateCard(FSRSRating.GOOD) },
                                colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(44.dp).testTag("fsrs_rate_good")
                            ) {
                                Text("Good\n(Recalled)", fontSize = 10.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { viewModel.rateCard(FSRSRating.EASY) },
                                colors = ButtonDefaults.buttonColors(containerColor = BioSuccessGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(44.dp).testTag("fsrs_rate_easy")
                            ) {
                                Text("Easy\n(Instant)", fontSize = 10.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(40.dp)) }
                }
            }
        }
    }
}
