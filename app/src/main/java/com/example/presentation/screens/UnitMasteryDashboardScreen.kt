package com.example.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.repository.BiologyRepository
import com.example.presentation.components.UnitMasteryDashboardComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitMasteryDashboardScreen(
    biologyRepository: BiologyRepository,
    onBack: () -> Unit,
    onNavigateToTopic: (String) -> Unit = {},
    onNavigateToPractice: (String) -> Unit = {},
    onNavigateToFsrs: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("unit_mastery_dashboard_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "لوحة إتقان الوحدات الدراسية",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Unit Mastery Dashboard (Room DB Live Telemetry)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("unit_dashboard_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
                UnitMasteryDashboardComponent(
                    biologyRepository = biologyRepository,
                    onNavigateToTopic = onNavigateToTopic,
                    onNavigateToPractice = onNavigateToPractice,
                    onNavigateToFsrs = onNavigateToFsrs
                )
            }
        }
    }
}
