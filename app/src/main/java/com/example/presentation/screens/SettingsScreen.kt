package com.example.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    themeController: ThemeController = LocalThemeController.current,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val currentThemeMode by themeController.themeMode.collectAsState()
    var notificationsEnabled by remember { mutableStateOf(true) }
    var resetDialogVisible by remember { mutableStateOf(false) }
    var progressResetDone by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("الإعدادات والمظهر (Settings)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Theme & Eye-Strain Reduction Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(BioTealPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Palette,
                                    contentDescription = "Theme",
                                    tint = BioTealPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("المظهر وحماية العين (Theme & Eye Comfort)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("تبديل النمط لتقليل الإجهاد البصري أثناء المذاكرة", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Material 3 SingleChoiceSegmentedButtonRow
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            AppThemeMode.entries.forEachIndexed { index, mode ->
                                SegmentedButton(
                                    selected = currentThemeMode == mode,
                                    onClick = { themeController.setThemeMode(mode) },
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = AppThemeMode.entries.size),
                                    icon = {
                                        Icon(
                                            imageVector = when (mode) {
                                                AppThemeMode.LIGHT -> Icons.Default.LightMode
                                                AppThemeMode.DARK -> Icons.Default.DarkMode
                                                AppThemeMode.SYSTEM -> Icons.Default.BrightnessAuto
                                            },
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    modifier = Modifier.testTag("theme_mode_${mode.name}")
                                ) {
                                    Text(
                                        text = when (mode) {
                                            AppThemeMode.LIGHT -> "نهاري"
                                            AppThemeMode.DARK -> "ليلي"
                                            AppThemeMode.SYSTEM -> "تلقائي"
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = if (currentThemeMode == mode) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Eye Comfort Explainer Note
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (currentThemeMode == AppThemeMode.DARK) {
                                Color(0xFF1E293B)
                            } else {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (currentThemeMode == AppThemeMode.DARK) Icons.Default.Bedtime else Icons.Default.WbSunny,
                                    contentDescription = null,
                                    tint = if (currentThemeMode == AppThemeMode.DARK) Color(0xFFFBBF24) else BioTealPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (currentThemeMode == AppThemeMode.DARK) {
                                            "🌙 الوضع الليلي مفعّل (Eye-Strain Reduction)"
                                        } else {
                                            "☀️ الوضع النهاري مفعّل (High Clarity)"
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (currentThemeMode == AppThemeMode.DARK) Color(0xFFFBBF24) else BioTealPrimary
                                    )
                                    Text(
                                        text = if (currentThemeMode == AppThemeMode.DARK) {
                                            "درجات Slate و Midnight العميقة تحد من انبعاث الضوء الأزرق وإجهاد عضلات العين في جلسات المذاكرة الطويلة."
                                        } else {
                                            "ألوان هادئة ومتناسقة تمنحك تبايناً واضحاً لقراءة الملاحظات ومخططات الأحياء نهاراً."
                                        },
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // System Specifications
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("مواصفات التطبيق والنظام:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("المنهج الدراسي", fontSize = 13.sp)
                            Text("الثانوية العامة (لغات) 2026", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BioTealPrimary)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("محرك التكرار المتباعد", fontSize = 13.sp)
                            Text("FSRS-Kotlin v5 (Open Source)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BioTealPrimary)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("منظومة الذكاء الاصطناعي", fontSize = 13.sp)
                            Text("5-Agent Synchronized Swarm", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SuccessGreen)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("التخزين المحلي والمظهر", fontSize = 13.sp)
                            Text("M3 Dynamic + Persistent Theme", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BioTealPrimary)
                        }
                    }
                }
            }

            // Notifications & Reminders
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("خيارات التنبيهات:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("تذكيرات المراجعة المتباعدة", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text("إشعار بالبطاقات المستحقة وفق FSRS", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            }
                            Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
                        }
                    }
                }
            }

            // Reset Progress
            item {
                Button(
                    onClick = { resetDialogVisible = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("إعادة ضبط تقدم الطالب والبطاقات", fontWeight = FontWeight.Bold)
                }

                if (progressResetDone) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("✅ تم تصفير سجل المراجعات بنجاح.", color = SuccessGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }

        if (resetDialogVisible) {
            AlertDialog(
                onDismissRequest = { resetDialogVisible = false },
                title = { Text("تأكيد إعادة الضبط") },
                text = { Text("هل أنت متأكد من رغبتك في تصفير سجل المراجعات ونقاط الخبرة؟ لا يمكن التراجع عن هذا الإجراء.") },
                confirmButton = {
                    TextButton(onClick = {
                        resetDialogVisible = false
                        progressResetDone = true
                    }) {
                        Text("نعم، إعادة الضبط", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { resetDialogVisible = false }) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }
}
