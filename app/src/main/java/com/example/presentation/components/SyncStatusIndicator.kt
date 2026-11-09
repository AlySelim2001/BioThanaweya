package com.example.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.RoomSyncSummary
import com.example.domain.model.SyncStatus
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * SyncStatusChip:
 * A compact, visually refined Material 3 pill designed for TopAppBars or screen headers
 * that communicates real-time synchronization between the Room database and background sync services.
 */
@Composable
fun SyncStatusChip(
    syncStatus: SyncStatus,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val config = remember(syncStatus) { getSyncVisualConfig(syncStatus) }

    // Infinite rotation transition for the syncing icon
    val infiniteTransition = rememberInfiniteTransition(label = "SyncRotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotatingAngle"
    )

    // Pulse animation for the live status dot
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Surface(
        onClick = onClick,
        modifier = modifier
            .minimumInteractiveComponentSize()
            .testTag("sync_status_chip"),
        shape = RoundedCornerShape(20.dp),
        color = config.containerColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, config.borderColor),
        tonalElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            // Live Status Indicator Dot or Spinning Icon
            if (syncStatus is SyncStatus.Syncing) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = "Syncing",
                    tint = config.contentColor,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(rotationAngle)
                        .testTag("sync_spinner_icon")
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(config.contentColor)
                )
                Icon(
                    imageVector = config.icon,
                    contentDescription = config.accessibilityDescription,
                    tint = config.contentColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            AnimatedContent(
                targetState = config.badgeText,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith
                            fadeOut(animationSpec = tween(220))
                },
                label = "SyncLabelAnim"
            ) { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = config.contentColor,
                    modifier = Modifier.testTag("sync_status_label")
                )
            }

            // Pending badge if there are offline unsynced changes
            if (syncStatus is SyncStatus.Offline && syncStatus.pendingChangesCount > 0) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(config.contentColor)
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "+${syncStatus.pendingChangesCount}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

/**
 * SyncStatusBanner:
 * An informational banner card displaying detailed synchronization context,
 * offline data safety reassurance, or active sync progress.
 */
@Composable
fun SyncStatusBanner(
    syncStatus: SyncStatus,
    onTriggerSync: () -> Unit,
    modifier: Modifier = Modifier
) {
    val config = remember(syncStatus) { getSyncVisualConfig(syncStatus) }
    val isSyncing = syncStatus is SyncStatus.Syncing

    // Infinite subtle rotation animation for the banner icon during sync
    val infiniteTransition = rememberInfiniteTransition(label = "BannerSyncAnim")
    val bannerRotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "BannerRotatingAngle"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("sync_status_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = config.containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, config.borderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(config.contentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSyncing) Icons.Default.Sync else config.icon,
                            contentDescription = config.accessibilityDescription,
                            tint = config.contentColor,
                            modifier = Modifier
                                .size(18.dp)
                                .then(
                                    if (isSyncing) Modifier
                                        .rotate(bannerRotationAngle)
                                        .testTag("sync_banner_spinner_icon")
                                    else Modifier
                                )
                        )
                    }
                    Column {
                        Text(
                            text = config.badgeText,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = config.contentColor
                        )
                        Text(
                            text = config.subText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Action button (Retry or Force Sync)
                if (syncStatus !is SyncStatus.Syncing) {
                    FilledTonalButton(
                        onClick = onTriggerSync,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("sync_retry_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync Now",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (syncStatus is SyncStatus.Error) "إعادة المحاولة" else "مزامنة الآن",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Syncing Progress Indicator
            if (syncStatus is SyncStatus.Syncing) {
                if (syncStatus.progressFraction != null) {
                    LinearProgressIndicator(
                        progress = { syncStatus.progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = BioTealPrimary,
                        trackColor = BioTealPrimary.copy(alpha = 0.2f)
                    )
                } else {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = BioTealPrimary,
                        trackColor = BioTealPrimary.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}

/**
 * SyncDetailsBottomSheet:
 * Detailed inspection bottom sheet showing local Room database metrics,
 * offline synchronization guarantees, and manual refresh controls.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncDetailsBottomSheet(
    syncStatus: SyncStatus,
    roomSummary: RoomSyncSummary,
    onDismiss: () -> Unit,
    onTriggerSync: () -> Unit,
    onToggleSimulatedOffline: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isSyncing = syncStatus is SyncStatus.Syncing

    val sheetInfiniteTransition = rememberInfiniteTransition(label = "SheetSyncRotation")
    val sheetRotationAngle by sheetInfiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SheetRotatingAngle"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("sync_details_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BioTealPrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSyncing) Icons.Default.Sync else Icons.Default.CloudSync,
                            contentDescription = null,
                            tint = BioTealPrimary,
                            modifier = Modifier
                                .size(24.dp)
                                .then(
                                    if (isSyncing) Modifier.rotate(sheetRotationAngle) else Modifier
                                )
                        )
                    }
                    Column {
                        Text(
                            text = "حالة مزامنة قاعدة البيانات",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Room SQLite (bio_thanaweya.db • الإصدار ${roomSummary.databaseVersion})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق")
                }
            }

            HorizontalDivider()

            // Main Status Banner inside sheet
            SyncStatusBanner(
                syncStatus = syncStatus,
                onTriggerSync = onTriggerSync
            )

            // Room Database Stored Telemetry Grid
            Text(
                text = "السجلات المحفوظة محلياً في Room (Offline-First):",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RoomMetricCard(
                    title = "بطاقات التكرار (FSRS)",
                    count = "${roomSummary.totalFlashcards}",
                    icon = Icons.Default.Style,
                    tint = MolecularPurple,
                    modifier = Modifier.weight(1f)
                )
                RoomMetricCard(
                    title = "تعاريف الأحياء",
                    count = "${roomSummary.savedDefinitionsCount}",
                    icon = Icons.Default.Bookmark,
                    tint = BioTealPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RoomMetricCard(
                    title = "سجلات الامتحانات",
                    count = "${roomSummary.examAttemptsCount}",
                    icon = Icons.Default.AssignmentTurnedIn,
                    tint = ExamGold,
                    modifier = Modifier.weight(1f)
                )
                RoomMetricCard(
                    title = "مفاهيم المنهج",
                    count = "${roomSummary.keyConceptsCount}",
                    icon = Icons.Default.AutoStories,
                    tint = Agent1EducatorColor,
                    modifier = Modifier.weight(1f)
                )
            }

            // Sync Information Row
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "آخر مزامنة ناجحة:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatTimestamp(roomSummary.lastSyncTimestamp),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "حالة اتصال الشبكة:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (roomSummary.isOnline) "متصل بالإنترنت ✓" else "بدون اتصال (محلي) ⚠️",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (roomSummary.isOnline) BioSuccessGreen else AlertCoral
                            )
                        )
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onToggleSimulatedOffline,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("sync_toggle_offline_btn")
                ) {
                    Icon(
                        imageVector = if (roomSummary.isOnline) Icons.Default.WifiOff else Icons.Default.Wifi,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (roomSummary.isOnline) "محاكاة انقطاع الشبكة" else "استعادة الاتصال",
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = onTriggerSync,
                    enabled = syncStatus !is SyncStatus.Syncing,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("sync_manual_trigger_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = BioTealPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .then(
                                if (isSyncing) Modifier.rotate(sheetRotationAngle) else Modifier
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "مزامنة الآن", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RoomMetricCard(
    title: String,
    count: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = tint.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, tint.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = count,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = tint
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private data class SyncVisualConfig(
    val badgeText: String,
    val subText: String,
    val icon: ImageVector,
    val contentColor: Color,
    val containerColor: Color,
    val borderColor: Color,
    val accessibilityDescription: String
)

private fun getSyncVisualConfig(status: SyncStatus): SyncVisualConfig {
    return when (status) {
        is SyncStatus.Synced -> SyncVisualConfig(
            badgeText = "متزامن",
            subText = "جميع سجلات الأحياء محدثة مع السحابة",
            icon = Icons.Default.CloudDone,
            contentColor = BioSuccessGreen,
            containerColor = SuccessGreenLight,
            borderColor = BioSuccessGreen.copy(alpha = 0.35f),
            accessibilityDescription = "قاعدة البيانات متزامنة بالكامل"
        )
        is SyncStatus.Syncing -> SyncVisualConfig(
            badgeText = "جارٍ المزامنة...",
            subText = status.stageDescription,
            icon = Icons.Default.Sync,
            contentColor = BioTealPrimary,
            containerColor = BioCyanLight,
            borderColor = BioTealPrimary.copy(alpha = 0.4f),
            accessibilityDescription = "جارٍ مزامنة البيانات حالياً"
        )
        is SyncStatus.Offline -> SyncVisualConfig(
            badgeText = "بدون اتصال (محلي)",
            subText = status.reason,
            icon = Icons.Default.CloudOff,
            contentColor = Color(0xFFD97706), // Warm Amber
            containerColor = ExamGoldLight,
            borderColor = Color(0xFFD97706).copy(alpha = 0.4f),
            accessibilityDescription = "التطبيق يعمل دون اتصال؛ البيانات محفوظة في Room"
        )
        is SyncStatus.Error -> {
            val badge = if (status.isBackingOff && status.nextRetryDelaySeconds != null) {
                "إعادة محاولة (${status.retryAttempt})"
            } else {
                "خطأ بالمزامنة"
            }
            val sub = if (status.isBackingOff && status.nextRetryDelaySeconds != null) {
                "${status.errorMessage} • إعادة المحاولة بعد ${status.nextRetryDelaySeconds}ث"
            } else {
                status.errorMessage
            }
            SyncVisualConfig(
                badgeText = badge,
                subText = sub,
                icon = Icons.Default.SyncProblem,
                contentColor = AlertCoral,
                containerColor = AlertCoralLight,
                borderColor = AlertCoral.copy(alpha = 0.4f),
                accessibilityDescription = "حدث خطأ أثناء المزامنة: $sub"
            )
        }
    }
}

private fun formatTimestamp(epoch: Long): String {
    val diffMillis = System.currentTimeMillis() - epoch
    return when {
        diffMillis < 60_000 -> "منذ لحظات"
        diffMillis < 3600_000 -> "منذ ${diffMillis / 60_000} دقيقة"
        else -> SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(epoch))
    }
}
