package com.example.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.viewmodel.FormulaLabViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulaLabScreen(
    viewModel: FormulaLabViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Calculate, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "مختبر القوانين والمسائل الحيوية",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("formula_lab_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab Row
            PrimaryTabRow(
                selectedTabIndex = uiState.selectedTab,
                modifier = Modifier.fillMaxWidth().testTag("formula_tab_row")
            ) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.setSelectedTab(0) },
                    text = { Text("القطع العضلية", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.setSelectedTab(1) },
                    text = { Text("قواعد تشارجاف", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = uiState.selectedTab == 2,
                    onClick = { viewModel.setSelectedTab(2) },
                    text = { Text("لفات DNA", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                when (uiState.selectedTab) {
                    0 -> SarcomereCalculatorView(uiState = uiState, onCalculate = viewModel::calculateSarcomere)
                    1 -> ChargaffCalculatorView(uiState = uiState, onCalculate = viewModel::calculateChargaff)
                    2 -> DnaTurnsCalculatorView(uiState = uiState, onCalculate = viewModel::calculateDnaTurns)
                }
            }
        }
    }
}

@Composable
private fun SarcomereCalculatorView(
    uiState: com.example.presentation.viewmodel.FormulaLabUiState,
    onCalculate: (String) -> Unit
) {
    var textInput by remember { mutableStateOf(uiState.sarcomereInput) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "حسابات القطعة العضلية (نظرية الخيوط المنزلقة)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "قانون الامتحان الوزاري: عدد خطوط Z = n + 1، وعدد المناطق الداكنة A = n، وعدد المناطق المضيئة الكاملة = n - 1.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textInput,
                onValueChange = {
                    textInput = it
                    onCalculate(it)
                },
                label = { Text("أدخل عدد القطع العضلية (n)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().testTag("sarcomere_input_field")
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            val res = uiState.sarcomereResult
            CalculationRow("عدد خطوط داكنة Z (Z-lines):", "${res.zLines}")
            CalculationRow("عدد المناطق الداكنة (A-bands):", "${res.darkBandsA}")
            CalculationRow("المناطق المضيئة الكاملة (Complete I):", "${res.completeLightBandsI}")
            CalculationRow("المناطق المضيئة غير الكاملة (طرفية):", "${res.incompleteLightBandsI}")
            CalculationRow("المناطق شبه المضيئة (H في حالة انبساط):", "${res.hBandsRelaxed}")
            CalculationRow("المناطق شبه المضيئة (H في انقباض تام):", "${res.hBandsFullyContracted} (تختفي تماماً)")
        }
    }
}

@Composable
private fun ChargaffCalculatorView(
    uiState: com.example.presentation.viewmodel.FormulaLabUiState,
    onCalculate: (String) -> Unit
) {
    var textInput by remember { mutableStateOf(uiState.adenineInput) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "قواعد تشارجاف للولب المزدوج (DNA)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "A = T و G = C، ومجموع البيورينات (A+G) = مجموع البيريميدينات (T+C) = 50% دائماً في أي لولب مزدوج.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textInput,
                onValueChange = {
                    textInput = it
                    onCalculate(it)
                },
                label = { Text("أدخل نسبة الأدينين (A%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth().testTag("chargaff_input_field")
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            val res = uiState.chargaffResult
            CalculationRow("نسبة الأدينين (A%):", "${res.adeninePercent}%")
            CalculationRow("نسبة الثايمين (T% = A%):", "${res.thyminePercent}%")
            CalculationRow("نسبة الجوانين (G%):", "${res.guaninePercent}%")
            CalculationRow("نسبة السيتوزين (C% = G%):", "${res.cytosinePercent}%")
            CalculationRow("إجمالي البيورينات (A + G):", "${res.purinesPercent}%")
            CalculationRow("إجمالي البيريميدينات (T + C):", "${res.pyrimidinesPercent}%")
        }
    }
}

@Composable
private fun DnaTurnsCalculatorView(
    uiState: com.example.presentation.viewmodel.FormulaLabUiState,
    onCalculate: (String) -> Unit
) {
    var textInput by remember { mutableStateOf(uiState.basePairsInput) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "حساب لفات اللولب المزدوج وعدد النيوكليوتيدات",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "اللفة الكاملة في اللولب المزدوج تحتوي على 10 أزواج من النيوكليوتيدات (20 نيوكليوتيدة مفردة).",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textInput,
                onValueChange = {
                    textInput = it
                    onCalculate(it)
                },
                label = { Text("أدخل عدد أزواج القواعد (Base Pairs)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().testTag("dna_turns_input_field")
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            val res = uiState.dnaTurnsResult
            CalculationRow("عدد أزواج القواعد النيتروجينية:", "${res.totalBasePairs} زوج")
            CalculationRow("إجمالي عدد النيوكليوتيدات المفردة:", "${res.totalNucleotides} نيوكليوتيدة")
            CalculationRow("عدد اللفات الكاملة (Turns):", "${res.completeTurns} لفة")
            CalculationRow("أقصى عدد كودونات محتمل:", "${res.codonsCount} كودون")
        }
    }
}

@Composable
private fun CalculationRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}
