package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulasScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Sarcomere, 1: DNA Chargaff, 2: Protein & Codons

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Biological Formula Lab",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Calculations & Mathematical Shortcuts",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("formulas_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Sarcomere Math", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_calc_sarcomere")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("DNA & Chargaff", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Biotech, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_calc_dna")
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Codons & Proteins", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_calc_protein")
                )
            }

            when (selectedTab) {
                0 -> SarcomereCalculatorTab()
                1 -> DnaCalculatorTab()
                2 -> ProteinCalculatorTab()
            }
        }
    }
}

@Composable
fun SarcomereCalculatorTab() {
    var inputSarcomeres by remember { mutableStateOf("5") }
    val sCount = inputSarcomeres.toIntOrNull() ?: 5
    val result = remember(sCount) { SarcomereCalculator.calculateFromSarcomeres(sCount) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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
                    Text(
                        text = "🔬 Sarcomere Parameters Solver",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Enter number of sarcomeres or examine Z-lines, dark bands, light bands, and H-zone disappearance.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = inputSarcomeres,
                        onValueChange = { if (it.length <= 4) inputSarcomeres = it },
                        label = { Text("Number of Sarcomeres (n)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_sarcomere_count")
                    )
                }
            }
        }

        item {
            Text(
                text = "Calculation Output (Solved Results)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultMetricCard("Z-Lines", "${result.numberOfZLines}", "n + 1", BioTealPrimary, Modifier.weight(1f))
                ResultMetricCard("A-Bands", "${result.numberOfABands}", "Dark / Myosin", MolecularPurple, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultMetricCard("Complete I-Bands", "${result.numberOfCompleteIBands}", "n - 1", BioCyanAccent, Modifier.weight(1f))
                ResultMetricCard("Incomplete I-Bands", "${result.numberOfIncompleteIBands}", "Outer ends", Color(0xFF78909C), Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultMetricCard("H-Zone (Relaxed)", "${result.numberOfHZonesRelaxed}", "Semi-light", ExamGold, Modifier.weight(1f))
                ResultMetricCard("H-Zone (Contracted)", "${result.numberOfHZonesContracted}", "Full Overlap = 0!", AlertCoral, Modifier.weight(1f))
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📖 Step-by-Step Derivation:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    result.explanations.forEach { exp ->
                        Text(
                            text = "• $exp",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
fun DnaCalculatorTab() {
    var baseType by remember { mutableStateOf("A") }
    var basePercentInput by remember { mutableStateOf("20") }
    var totalNucleotidesInput by remember { mutableStateOf("1000") }

    val percent = basePercentInput.toDoubleOrNull() ?: 20.0
    val totalN = totalNucleotidesInput.toIntOrNull() ?: 1000

    val result = remember(baseType, percent, totalN) {
        DnaCalculator.calculateFromBasePercent(baseType, percent, totalN)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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
                    Text(
                        text = "🧬 Chargaff DNA Double Helix Solver",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "In double-stranded DNA: %A = %T and %G = %C. Turns = Base Pairs / 10.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = basePercentInput,
                            onValueChange = { basePercentInput = it },
                            label = { Text("Base % ($baseType)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("input_dna_percent")
                        )
                        OutlinedTextField(
                            value = totalNucleotidesInput,
                            onValueChange = { totalNucleotidesInput = it },
                            label = { Text("Total Nucleotides") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("input_dna_total")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("A", "T", "G", "C").forEach { base ->
                            FilterChip(
                                selected = baseType == base,
                                onClick = { baseType = base },
                                label = { Text("Base $base") }
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Equivalence & Helical Geometry",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultMetricCard("Adenine (%A)", "${String.format("%.1f", result.adeninePercent)}%", "= Thymine", BioTealPrimary, Modifier.weight(1f))
                ResultMetricCard("Thymine (%T)", "${String.format("%.1f", result.thyminePercent)}%", "= Adenine", BioTealPrimary, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultMetricCard("Guanine (%G)", "${String.format("%.1f", result.guaninePercent)}%", "= Cytosine", MolecularPurple, Modifier.weight(1f))
                ResultMetricCard("Cytosine (%C)", "${String.format("%.1f", result.cytosinePercent)}%", "= Guanine", MolecularPurple, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultMetricCard("Base Pairs", "${result.numberOfBasePairs}", "Total N / 2", BioCyanAccent, Modifier.weight(1f))
                ResultMetricCard("Complete Turns", "${String.format("%.1f", result.numberOfCompleteTurns)}", "BP / 10", ExamGold, Modifier.weight(1f))
            }
        }

        item {
            ResultMetricCard("Hydrogen Bonds", "${result.hydrogenBondsCount}", "2(A) + 3(G)", AlertCoral, Modifier.fillMaxWidth())
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📖 Step-by-Step Derivation:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    result.explanations.forEach { exp ->
                        Text(
                            text = "• $exp",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
fun ProteinCalculatorTab() {
    var inputAminoAcids by remember { mutableStateOf("50") }
    val aaCount = inputAminoAcids.toIntOrNull() ?: 50
    val result = remember(aaCount) { ProteinCalculator.calculateFromAminoAcids(aaCount) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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
                    Text(
                        text = "🧪 Protein Synthesis & Codon Counter",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Calculates mRNA codons, nucleotides, DNA rungs, peptide bonds, and tRNA requirements.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = inputAminoAcids,
                        onValueChange = { if (it.length <= 4) inputAminoAcids = it },
                        label = { Text("Number of Amino Acids in Polypeptide") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_amino_acid_count")
                    )
                }
            }
        }

        item {
            Text(
                text = "Molecular Synthesis Requirements",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultMetricCard("mRNA Codons", "${result.minimumCodonsOnMrna}", "Amino Acids + 1 (Stop)", BioTealPrimary, Modifier.weight(1f))
                ResultMetricCard("mRNA Nucleotides", "${result.minimumNucleotidesOnMrna}", "Codons × 3", MolecularPurple, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultMetricCard("DNA Nucleotides", "${result.minimumNucleotidesOnDnaMolecule}", "Double strand = mRNA × 2", BioCyanAccent, Modifier.weight(1f))
                ResultMetricCard("tRNA Required", "${result.numberOfTrnaRequired}", "Stop codon has no tRNA!", ExamGold, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultMetricCard("Peptide Bonds", "${result.numberOfPeptideBonds}", "Amino Acids - 1", SuccessGreen, Modifier.weight(1f))
                ResultMetricCard("H₂O Released", "${result.numberOfWaterMoleculesReleased}", "Condensation = Bonds", AlertCoral, Modifier.weight(1f))
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📖 Step-by-Step Derivation:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    result.explanations.forEach { exp ->
                        Text(
                            text = "• $exp",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
fun ResultMetricCard(
    label: String,
    value: String,
    detail: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = accentColor,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = detail,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
