package com.example.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
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
import com.example.presentation.viewmodel.FormulaLabViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulaLabScreen(
    viewModel: FormulaLabViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Formula & Bio-Law Lab", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("Sarcomere, Chargaff & Codon Solvers", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("formula_lab_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Sarcomere", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_sarcomere_calc")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Chargaff & DNA", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Biotech, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_chargaff_calc")
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Codons & Protein", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_codon_calc")
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (selectedTab) {
                    0 -> {
                        // Sarcomere Calculator
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("🔬 Sarcomere Landmark Solver:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Validates positive inputs (rejects negative/zero) while enforcing constant A-band length.", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = state.sarcomereInput,
                                        onValueChange = { viewModel.onSarcomereInputChanged(it) },
                                        label = { Text("Number of Sarcomeres (n)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth().testTag("sarcomere_input_field")
                                    )
                                }
                            }
                        }


                        val result = state.sarcomereResult
                        if (result != null && result.isSuccess) {
                            val r = result.getOrNull()!!
                            item {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    MetricPill("خطوط Z", "${r.zLinesCount}", "n + 1", BioTealPrimary, Modifier.weight(1f))
                                    MetricPill("المناطق الداكنة A", "${r.aBandsCount}", "ثابتة لا تقصر أبداً", MolecularPurple, Modifier.weight(1f))
                                }
                            }
                            item {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    MetricPill("المناطق المضيئة الكاملة I", "${r.completeIBandsCount}", "n - 1", BioCyanAccent, Modifier.weight(1f))
                                    MetricPill("المناطق المضيئة غير الكاملة", "${r.incompleteIBandsCount}", "دائمًا 2 على الأطراف", Color.Gray, Modifier.weight(1f))
                                }
                            }
                            item {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    MetricPill("شبه المضيئة H (انبساط)", "${r.hZonesRelaxedCount}", "تساوي n", ExamGold, Modifier.weight(1f))
                                    MetricPill("شبه المضيئة H (انقباض تام)", "${r.hZonesContractedCount}", "تختفي تمامًا = 0!", AlertCoral, Modifier.weight(1f))
                                }
                            }
                            item {
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("خطوات الاستنتاج الرياضي:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        r.explanations.forEach { exp ->
                                            Text("• $exp", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            }
                        } else if (result != null && result.isFailure) {
                            item {
                                Text(
                                    text = "❌ خطأ في الإدخال: ${result.exceptionOrNull()?.message}",
                                    color = AlertCoral,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    1 -> {
                        // Chargaff Calculator
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("🧬 حاسبة شارجاف وحلزون DNA المزدوج:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("تفرض تماثل A=T و G=C والمجموع 100% بدقة 1e-4 مع ثبات 10 أزواج قواعد لكل لفة تامة.", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        listOf("A", "T", "G", "C").forEach { base ->
                                            FilterChip(
                                                selected = state.dnaBaseInput == base,
                                                onClick = { viewModel.onDnaChanged(base, state.dnaPercentInput, state.dnaTotalInput) },
                                                label = { Text("قاعدة $base") }
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        OutlinedTextField(
                                            value = state.dnaPercentInput,
                                            onValueChange = { viewModel.onDnaChanged(state.dnaBaseInput, it, state.dnaTotalInput) },
                                            label = { Text("النسبة المئوية %") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.weight(1f).testTag("chargaff_percent_field")
                                        )
                                        OutlinedTextField(
                                            value = state.dnaTotalInput,
                                            onValueChange = { viewModel.onDnaChanged(state.dnaBaseInput, state.dnaPercentInput, it) },
                                            label = { Text("إجمالي النيوكليوتيدات") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.weight(1f).testTag("chargaff_total_field")
                                        )
                                    }
                                }
                            }
                        }

                        val result = state.chargaffResult
                        if (result != null && result.isSuccess) {
                            val r = result.getOrNull()!!
                            item {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    MetricPill("أدنين (%A)", "${r.adeninePercent}%", "= ثايمين", BioTealPrimary, Modifier.weight(1f))
                                    MetricPill("ثايمين (%T)", "${r.thyminePercent}%", "= أدنين", BioTealPrimary, Modifier.weight(1f))
                                }
                            }
                            item {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    MetricPill("جوانين (%G)", "${r.guaninePercent}%", "= سيتوزين", MolecularPurple, Modifier.weight(1f))
                                    MetricPill("سيتوزين (%C)", "${r.cytosinePercent}%", "= جوانين", MolecularPurple, Modifier.weight(1f))
                                }
                            }
                            item {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    MetricPill("أزواج القواعد", "${r.basePairsCount}", "الإجمالي / 2", BioCyanAccent, Modifier.weight(1f))
                                    MetricPill("اللفات التامة (10 bp)", "${String.format("%.1f", r.completeTurnsCount)}", "ثابت المنهج = 10", ExamGold, Modifier.weight(1f))
                                }
                            }
                            item {
                                MetricPill("الروابط الهيدروجينية", "${r.hydrogenBondsTotal}", "2(A) + 3(G)", AlertCoral, Modifier.fillMaxWidth())
                            }
                        } else if (result != null && result.isFailure) {
                            item {
                                Text(
                                    text = "❌ خطأ في الحساب: ${result.exceptionOrNull()?.message}",
                                    color = AlertCoral,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    2 -> {
                        // Codon Calculator
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("🧪 حاسبة الكودونات وسلسلة عديد الببتيد:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("تتحقق من أن الطول مضاعف للعدد 3، وتستبعد كودونات التوقف (UAA/UAG/UGA) من الأحماض الأمينية.", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = state.rnaNucleotidesInput,
                                        onValueChange = { viewModel.onCodonChanged(it, state.stopCodonCountInput, state.includeStartMethionine) },
                                        label = { Text("عدد نيوكليوتيدات mRNA (مضاعف لـ 3)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth().testTag("codon_nucleotides_field")
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = state.includeStartMethionine,
                                            onCheckedChange = { viewModel.onCodonChanged(state.rnaNucleotidesInput, state.stopCodonCountInput, it) }
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("احتساب كودون البدء (AUG / ميثيونين) ضمن السلسلة", fontSize = 12.sp)
                                    }
                                }
                            }
                        }

                        val result = state.codonResult
                        if (result != null && result.isSuccess) {
                            val r = result.getOrNull()!!
                            item {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    MetricPill("كودونات mRNA", "${r.totalCodons}", "النيوكليوتيدات / 3", BioTealPrimary, Modifier.weight(1f))
                                    MetricPill("الأحماض الأمينية", "${r.aminoAcidsCount}", "كودونات - التوقف", MolecularPurple, Modifier.weight(1f))
                                }
                            }
                            item {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    MetricPill("الروابط الببتيدية", "${r.peptideBondsCount}", "أحماض - 1", BioCyanAccent, Modifier.weight(1f))
                                    MetricPill("جزيئات الماء الناتجة", "${r.waterMoleculesReleased}", "= الروابط الببتيدية", AlertCoral, Modifier.weight(1f))
                                }
                            }
                            item {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    MetricPill("جزيئات tRNA المشاركة", "${r.trnaMoleculesRequired}", "كودون التوقف ليس له tRNA!", ExamGold, Modifier.weight(1f))
                                    MetricPill("نيوكليوتيدات جين DNA", "${r.dnaDoubleHelixNucleotides}", "شريط مزدوج = mRNA × 2", Color(0xFF00796B), Modifier.weight(1f))
                                }
                            }
                        } else if (result != null && result.isFailure) {
                            item {
                                Text(
                                    text = "❌ خطأ في الحساب: ${result.exceptionOrNull()?.message}",
                                    color = AlertCoral,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun MetricPill(
    label: String,
    value: String,
    subText: String,
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
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = accentColor, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subText, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
