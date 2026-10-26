package com.example.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import com.example.ui.theme.BioTealPrimary
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var resetDialogVisible by remember { mutableStateOf(false) }
    var progressResetDone by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("الإعدادات والمعلومات", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
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
                            Text("7 Collaborative Agents", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SuccessGreen)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("التخزين المحلي", fontSize = 13.sp)
                            Text("Room Database (Offline-First)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BioTealPrimary)
                        }
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("خيارات العرض والتنبيهات:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("الوضع الليلي (Dark Mode)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text("تفعيل النمط المظلم المريح للعينين", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            }
                            Switch(checked = darkModeEnabled, onCheckedChange = { darkModeEnabled = it })
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

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
