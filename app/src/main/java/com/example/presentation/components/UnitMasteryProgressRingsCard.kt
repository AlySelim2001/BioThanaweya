package com.example.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.viewmodel.UnitMasteryData
import com.example.ui.theme.*

@Composable
fun UnitMasteryProgressRingsCard(
    unit1: UnitMasteryData,
    unit2: UnitMasteryData,
    overallPercentage: Int,
    totalCardsReviewed: Int,
    questionsGraded: Int,
    streakDays: Int,
    onUnitClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("unit_progress_rings_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header with Room telemetry indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(BioTealPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.DonutLarge,
                            contentDescription = "Mastery Progress Rings",
                            tint = BioTealPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "معدل إتقان الوحدات الدراسية",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Unit Mastery Progress Rings (Room Database)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Storage,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Room Local DB",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dual Progress Rings: Unit 1 & Unit 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Unit 1 Progress Ring Card
                UnitProgressRingItem(
                    unit = unit1,
                    primaryColor = BioTealPrimary,
                    accentColor = Color(0xFF00E5FF),
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    testTag = "unit_1_progress_ring",
                    onClick = { onUnitClick(1) },
                    modifier = Modifier.weight(1f)
                )

                // Unit 2 Progress Ring Card
                UnitProgressRingItem(
                    unit = unit2,
                    primaryColor = MolecularPurple,
                    accentColor = Color(0xFFC084FC),
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    testTag = "unit_2_progress_ring",
                    onClick = { onUnitClick(2) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Room Database Interaction Telemetry Chips
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TelemetryStatItem(
                        icon = Icons.Default.Style,
                        iconTint = BioTealPrimary,
                        label = "بطاقات مراجعة",
                        value = "$totalCardsReviewed"
                    )
                    VerticalDivider(modifier = Modifier.height(24.dp))
                    TelemetryStatItem(
                        icon = Icons.Default.FactCheck,
                        iconTint = MolecularPurple,
                        label = "أسئلة مصححة",
                        value = "$questionsGraded"
                    )
                    VerticalDivider(modifier = Modifier.height(24.dp))
                    TelemetryStatItem(
                        icon = Icons.Default.LocalFireDepartment,
                        iconTint = AlertCoral,
                        label = "أيام الاستمرار",
                        value = "$streakDays يوم"
                    )
                    VerticalDivider(modifier = Modifier.height(24.dp))
                    TelemetryStatItem(
                        icon = Icons.Default.Verified,
                        iconTint = ExamGold,
                        label = "الإتقان الكلي",
                        value = "$overallPercentage%"
                    )
                }
            }
        }
    }
}

@Composable
private fun UnitProgressRingItem(
    unit: UnitMasteryData,
    primaryColor: Color,
    accentColor: Color,
    backgroundColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = unit.progressFraction,
        animationSpec = tween(durationMillis = 1000),
        label = "unitProgressRing"
    )

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor.copy(alpha = 0.3f)),
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ring Canvas with Percentage in Center
            Box(
                modifier = Modifier.size(105.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 10.dp.toPx()
                    val diameter = size.minDimension - strokeWidth
                    val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
                    val arcSize = Size(diameter, diameter)

                    // Track Background Ring
                    drawArc(
                        color = primaryColor.copy(alpha = 0.15f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Active Progress Ring
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(primaryColor, accentColor, primaryColor),
                            center = Offset(size.width / 2f, size.height / 2f)
                        ),
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Center Content: Percentage & Icon
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${unit.percentage}%",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = primaryColor
                        )
                    )
                    Text(
                        text = "إتقان",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Unit Label
            Text(
                text = if (unit.unitNumber == 1) "الوحدة الأولى" else "الوحدة الثانية",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (unit.unitNumber == 1) "التركيب والوظيفة" else "البيولوجيا الجزيئية",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = primaryColor,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sub-metrics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${unit.masteredTopics}/${unit.totalTopics}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("موضوع", fontSize = 9.sp, color = MaterialTheme.colorScheme.outline)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${unit.cardsReviewed}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("بطاقة", fontSize = 9.sp, color = MaterialTheme.colorScheme.outline)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${String.format("%.1f", unit.averageStability)}d",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("ثبات FSRS", fontSize = 9.sp, color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
private fun TelemetryStatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = label,
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
