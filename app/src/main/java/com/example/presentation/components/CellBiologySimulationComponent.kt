package com.example.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.example.data.repository.CellBiologyDataProvider
import com.example.domain.model.*
import com.example.ui.theme.*

@Composable
fun CellBiologySimulationComponent(
    modifier: Modifier = Modifier,
    initialOrganelle: OrganelleType? = OrganelleType.MITOCHONDRIA
) {
    var cellType by remember { mutableStateOf(CellType.ANIMAL_CELL) }
    var selectedOrganelle by remember { mutableStateOf<OrganelleType?>(initialOrganelle) }
    var simulationMode by remember { mutableStateOf(SimulationViewMode.EXPLORATION) }
    var isCutawayEnabled by remember { mutableStateOf(true) }
    var tourStepIndex by remember { mutableIntStateOf(0) }

    val availableOrganelles = remember(cellType) {
        CellBiologyDataProvider.getOrganellesForCell(cellType)
    }

    // Keep selected organelle valid when switching cell types
    LaunchedEffect(cellType) {
        if (selectedOrganelle != null) {
            val exists = availableOrganelles.any { it.type == selectedOrganelle }
            if (!exists) {
                selectedOrganelle = availableOrganelles.firstOrNull()?.type
            }
        }
    }

    // Handle guided tour stepping
    LaunchedEffect(tourStepIndex, simulationMode) {
        if (simulationMode == SimulationViewMode.GUIDED_TOUR && availableOrganelles.isNotEmpty()) {
            val validIndex = tourStepIndex.coerceIn(0, availableOrganelles.size - 1)
            selectedOrganelle = availableOrganelles[validIndex].type
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("cell_simulation_master_component"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Control Toolbar: Cell Type Switcher & Cutaway Toggle & Mode Selector
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cell Type Switcher (Animal vs Plant)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(2.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .testTag("btn_select_animal_cell"),
                            color = if (cellType == CellType.ANIMAL_CELL) BioTealPrimary else Color.Transparent,
                            onClick = { cellType = CellType.ANIMAL_CELL }
                        ) {
                            Text(
                                text = "خلية حيوانية",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (cellType == CellType.ANIMAL_CELL) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .testTag("btn_select_plant_cell"),
                            color = if (cellType == CellType.PLANT_CELL) SuccessGreen else Color.Transparent,
                            onClick = { cellType = CellType.PLANT_CELL }
                        ) {
                            Text(
                                text = "خلية نباتية",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (cellType == CellType.PLANT_CELL) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // Cutaway Toggle
                    FilterChip(
                        selected = isCutawayEnabled,
                        onClick = { isCutawayEnabled = !isCutawayEnabled },
                        label = {
                            Text(
                                text = if (isCutawayEnabled) "المقطع المفرغ 3D" else "الغلاف الكامل",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (isCutawayEnabled) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        modifier = Modifier.testTag("btn_toggle_cutaway")
                    )
                }

                // Simulation View Mode Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SimulationViewMode.values().forEach { mode ->
                        val isSelected = simulationMode == mode
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                simulationMode = mode
                                if (mode == SimulationViewMode.GUIDED_TOUR) {
                                    tourStepIndex = 0
                                    selectedOrganelle = availableOrganelles.firstOrNull()?.type
                                }
                            },
                            label = { Text(mode.titleArabic, fontSize = 11.sp) },
                            leadingIcon = {
                                val icon = when (mode) {
                                    SimulationViewMode.EXPLORATION -> Icons.Default.Explore
                                    SimulationViewMode.GUIDED_TOUR -> Icons.Default.Tour
                                    SimulationViewMode.SECRETORY_PATHWAY -> Icons.Default.AltRoute
                                    SimulationViewMode.ATP_PRODUCTION -> Icons.Default.Bolt
                                }
                                Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BioTealPrimary.copy(alpha = 0.2f),
                                selectedLabelColor = BioTealPrimary
                            ),
                            modifier = Modifier.testTag("sim_mode_${mode.name}")
                        )
                    }
                }
            }
        }

        // Guided Tour Step Bar (if active)
        if (simulationMode == SimulationViewMode.GUIDED_TOUR) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = ExamGold.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ExamGold.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { if (tourStepIndex > 0) tourStepIndex-- },
                        enabled = tourStepIndex > 0
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "الجولة الإرشادية للعضيات (${tourStepIndex + 1} من ${availableOrganelles.size})",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = ExamGold)
                        )
                        Text(
                            text = availableOrganelles.getOrNull(tourStepIndex)?.arabicName ?: "",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(
                        onClick = { if (tourStepIndex < availableOrganelles.size - 1) tourStepIndex++ },
                        enabled = tourStepIndex < availableOrganelles.size - 1
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                    }
                }
            }
        }

        // 3D-Like Viewport
        Cell3DViewport(
            cellType = cellType,
            selectedOrganelle = selectedOrganelle,
            simulationMode = simulationMode,
            isCutawayEnabled = isCutawayEnabled,
            onOrganelleSelected = { organelle ->
                selectedOrganelle = organelle
            }
        )

        // Organelles Fast-Select Chip Carousel
        Column {
            Text(
                text = "اختر عضي للتركيز وفحص الوظيفة والتركيب:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableOrganelles.forEach { organelle ->
                    val isSelected = selectedOrganelle == organelle.type
                    val color = Color(organelle.type.primaryColorHex)
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedOrganelle = organelle.type },
                        label = { Text(organelle.arabicName, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = color.copy(alpha = 0.2f),
                            selectedLabelColor = color
                        ),
                        modifier = Modifier.testTag("chip_organelle_${organelle.type.name}")
                    )
                }
            }
        }

        // Detailed Educational Information & Function Sheet for Selected Organelle
        AnimatedVisibility(
            visible = selectedOrganelle != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            val organelle = selectedOrganelle?.let { CellBiologyDataProvider.organelleDetails[it] }
            if (organelle != null) {
                OrganelleDetailCard(
                    detail = organelle,
                    onClose = { selectedOrganelle = null },
                    onLaunchSimulationMode = { modeName ->
                        when (modeName) {
                            "SECRETORY_PATHWAY" -> simulationMode = SimulationViewMode.SECRETORY_PATHWAY
                            "ATP_POWERHOUSE" -> simulationMode = SimulationViewMode.ATP_PRODUCTION
                        }
                    }
                )
            }
        }
    }
}
