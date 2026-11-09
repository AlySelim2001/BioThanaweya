package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.di.AppContainer
import com.example.presentation.screens.*
import com.example.presentation.viewmodel.*
import com.example.ui.theme.MyApplicationTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.activity.compose.BackHandler
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AppCheckInit"
private const val DEBUG_TOKEN_EXTRA = "FIREBASE_APPCHECK_DEBUG_TOKEN"
private const val DEBUG_STORE_PREFIX = "com.google.firebase.appcheck.debug.store."
private const val DEBUG_SECRET_KEY = "com.google.firebase.appcheck.debug.DEBUG_SECRET"

fun configureAppCheck(context: Context, intent: Intent?) {
    try {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }

        val apps = FirebaseApp.getApps(context)
        if (apps.isNotEmpty()) {
            val firebaseApp = FirebaseApp.getInstance()
            if (BuildConfig.DEBUG) {
                val debugToken = intent?.getStringExtra(DEBUG_TOKEN_EXTRA)
                if (!debugToken.isNullOrEmpty()) {
                    val persistenceKey = firebaseApp.persistenceKey
                    val prefs = context.getSharedPreferences(
                        "$DEBUG_STORE_PREFIX$persistenceKey",
                        Context.MODE_PRIVATE
                    )
                    prefs.edit().putString(DEBUG_SECRET_KEY, debugToken).commit()
                    Log.d(TAG, "App Check debug secret configured successfully.")
                }
                FirebaseAppCheck.getInstance(firebaseApp).installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance()
                )
                Log.d(TAG, "DebugAppCheckProviderFactory installed.")
            }
        } else {
            Log.d(TAG, "FirebaseApp is not configured in this build; skipping optional App Check.")
        }
    } catch (e: Exception) {
        Log.d(TAG, "Firebase App Check optional initialization skipped: ${e.message}")
    }
}

sealed interface NavDestination {
    data object Dashboard : NavDestination
    data object ChaptersDashboard : NavDestination
    data object ChapterSearch : NavDestination
    data object Lessons : NavDestination
    data object QuestionBank : NavDestination
    data class Topic(val topicId: String) : NavDestination
    data class Practice(val topicId: String) : NavDestination
    data class Fsrs(val topicId: String) : NavDestination
    data object FormulaLab : NavDestination
    data object SimulatedExam : NavDestination
    data object Settings : NavDestination
    data object Progress : NavDestination
    data object UnitMasteryDashboard : NavDestination
    data object Glossary : NavDestination
    data object CellSimulation : NavDestination
    data object MistakeAnalytics : NavDestination
}

@AndroidEntryPoint(ComponentActivity::class)
class MainActivity : Hilt_MainActivity() {

    lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 1: App Check Foundation
        configureAppCheck(this, intent)

        // Step 2: Initialize DI Container
        appContainer = AppContainer(this)

        // Step 3: Seed database records asynchronously
        lifecycleScope.launch {
            try {
                appContainer.biologyRepository.seedDefaultCardsIfEmpty()
                appContainer.biologyRepository.initializeTopicMasteriesIfEmpty()
                appContainer.biologyRepository.seedDefaultKeyConceptsIfEmpty()
                appContainer.chapterProgressRepository.seedDefault11ChaptersIfEmpty()
                appContainer.chapterSearchRepository.seedCurriculumContentIfEmpty()
            } catch (ignored: Exception) {}
        }

        enableEdgeToEdge()

        setContent {
            val themeMode by appContainer.themeController.themeMode.collectAsState()
            MyApplicationTheme(
                themeMode = themeMode,
                themeController = appContainer.themeController
            ) {
                var currentDest by remember { mutableStateOf<NavDestination>(NavDestination.Dashboard) }

                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp
                        ) {
                            NavigationBarItem(
                                selected = currentDest is NavDestination.Dashboard,
                                onClick = { currentDest = NavDestination.Dashboard },
                                icon = { Icon(Icons.Default.Home, contentDescription = "الرئيسية") },
                                label = { Text("الرئيسية") },
                                modifier = Modifier.testTag("nav_bottom_dashboard")
                            )
                            NavigationBarItem(
                                selected = currentDest is NavDestination.Lessons || currentDest is NavDestination.Topic,
                                onClick = { currentDest = NavDestination.Lessons },
                                icon = { Icon(Icons.Default.MenuBook, contentDescription = "الدروس") },
                                label = { Text("الدروس") },
                                modifier = Modifier.testTag("nav_bottom_lessons")
                            )
                            NavigationBarItem(
                                selected = currentDest is NavDestination.QuestionBank || currentDest is NavDestination.Practice,
                                onClick = { currentDest = NavDestination.QuestionBank },
                                icon = { Icon(Icons.Default.Quiz, contentDescription = "بنك الأسئلة") },
                                label = { Text("بنك الأسئلة") },
                                modifier = Modifier.testTag("nav_bottom_question_bank")
                            )
                            NavigationBarItem(
                                selected = currentDest is NavDestination.SimulatedExam,
                                onClick = { currentDest = NavDestination.SimulatedExam },
                                icon = { Icon(Icons.Default.Assignment, contentDescription = "الاختبارات") },
                                label = { Text("الاختبارات") },
                                modifier = Modifier.testTag("nav_bottom_exam_sim")
                            )
                            NavigationBarItem(
                                selected = currentDest is NavDestination.Progress || currentDest is NavDestination.UnitMasteryDashboard,
                                onClick = { currentDest = NavDestination.Progress },
                                icon = { Icon(Icons.Default.BarChart, contentDescription = "التقدم") },
                                label = { Text("التقدم") },
                                modifier = Modifier.testTag("nav_bottom_progress")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (val dest = currentDest) {
                            is NavDestination.Dashboard -> {
                                var selectedDashboardTab by remember { mutableIntStateOf(0) }
                                val chaptersVm = remember {
                                    ChaptersDashboardViewModel(appContainer.chapterProgressRepository)
                                }

                                Column(modifier = Modifier.fillMaxSize()) {
                                    TabRow(
                                        selectedTabIndex = selectedDashboardTab,
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        modifier = Modifier.testTag("main_dashboard_tabs")
                                    ) {
                                        Tab(
                                            selected = selectedDashboardTab == 0,
                                            onClick = { selectedDashboardTab = 0 },
                                            text = { Text("فصول المنهج (11)", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                                            icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                                            modifier = Modifier.testTag("tab_chapters_grid")
                                        )
                                        Tab(
                                            selected = selectedDashboardTab == 1,
                                            onClick = { selectedDashboardTab = 1 },
                                            text = { Text("المساعد والوكلاء", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                                            icon = { Icon(Icons.Default.Psychology, contentDescription = null) },
                                            modifier = Modifier.testTag("tab_agents_overview")
                                        )
                                    }

                                    if (selectedDashboardTab == 0) {
                                        ChaptersDashboardScreen(
                                            viewModel = chaptersVm,
                                            onNavigateToTopic = { topicId -> currentDest = NavDestination.Topic(topicId) },
                                            onNavigateToPractice = { topicId -> currentDest = NavDestination.Practice(topicId) },
                                            onNavigateToExam = { currentDest = NavDestination.SimulatedExam },
                                            onNavigateToFormulaLab = { currentDest = NavDestination.FormulaLab },
                                            onNavigateToSettings = { currentDest = NavDestination.Settings },
                                            onNavigateToSearch = { currentDest = NavDestination.ChapterSearch }
                                        )
                                    } else {
                                        val dashboardVm = remember {
                                            DashboardViewModel(appContainer.orchestrator, appContainer.biologyRepository)
                                        }
                                        DashboardScreen(
                                            viewModel = dashboardVm,
                                            syncManager = appContainer.syncManager,
                                            onNavigateToTopic = { topicId -> currentDest = NavDestination.Topic(topicId) },
                                            onNavigateToPractice = { topicId -> currentDest = NavDestination.Practice(topicId) },
                                            onNavigateToFsrs = { topicId -> currentDest = NavDestination.Fsrs(topicId) },
                                            onNavigateToFormulaLab = { currentDest = NavDestination.FormulaLab },
                                            onNavigateToSimulatedExam = { currentDest = NavDestination.SimulatedExam },
                                            onNavigateToSettings = { currentDest = NavDestination.Settings },
                                            onNavigateToProgress = { currentDest = NavDestination.Progress },
                                            onNavigateToUnitMastery = { currentDest = NavDestination.UnitMasteryDashboard },
                                            onNavigateToGlossary = { currentDest = NavDestination.Glossary },
                                            onNavigateToCellSimulation = { currentDest = NavDestination.CellSimulation }
                                        )
                                    }
                                }
                            }
                            is NavDestination.ChaptersDashboard -> {
                                val chaptersVm = remember {
                                    ChaptersDashboardViewModel(appContainer.chapterProgressRepository)
                                }
                                ChaptersDashboardScreen(
                                    viewModel = chaptersVm,
                                    onNavigateToTopic = { topicId -> currentDest = NavDestination.Topic(topicId) },
                                    onNavigateToPractice = { topicId -> currentDest = NavDestination.Practice(topicId) },
                                    onNavigateToExam = { currentDest = NavDestination.SimulatedExam },
                                    onNavigateToFormulaLab = { currentDest = NavDestination.FormulaLab },
                                    onNavigateToSettings = { currentDest = NavDestination.Settings },
                                    onNavigateToSearch = { currentDest = NavDestination.ChapterSearch }
                                )
                            }
                            is NavDestination.Lessons -> {
                                LessonsScreen(
                                    onNavigateToTopic = { topicId -> currentDest = NavDestination.Topic(topicId) },
                                    onNavigateToPractice = { topicId -> currentDest = NavDestination.Practice(topicId) },
                                    onNavigateToFsrs = { topicId -> currentDest = NavDestination.Fsrs(topicId) },
                                    onBack = { currentDest = NavDestination.Dashboard }
                                )
                            }
                            is NavDestination.QuestionBank -> {
                                QuestionBankScreen(
                                    onBack = { currentDest = NavDestination.Dashboard }
                                )
                            }
                            is NavDestination.Glossary -> {
                                BilingualGlossaryScreen(
                                    onBack = { currentDest = NavDestination.Dashboard },
                                    onNavigateToTopic = { topicId -> currentDest = NavDestination.Topic(topicId) },
                                    onNavigateToPractice = { topicId -> currentDest = NavDestination.Practice(topicId) }
                                )
                            }
                            is NavDestination.UnitMasteryDashboard -> {
                                UnitMasteryDashboardScreen(
                                    biologyRepository = appContainer.biologyRepository,
                                    onBack = { currentDest = NavDestination.Dashboard },
                                    onNavigateToTopic = { topicId -> currentDest = NavDestination.Topic(topicId) },
                                    onNavigateToPractice = { topicId -> currentDest = NavDestination.Practice(topicId) },
                                    onNavigateToFsrs = { topicId -> currentDest = NavDestination.Fsrs(topicId) }
                                )
                            }
                            is NavDestination.Progress -> {
                                val progressVm = remember {
                                    ProgressViewModel(appContainer.biologyRepository)
                                }
                                ProgressScreen(
                                    viewModel = progressVm,
                                    onBack = { currentDest = NavDestination.Dashboard },
                                    onNavigateToTopic = { topicId -> currentDest = NavDestination.Topic(topicId) },
                                    onNavigateToPractice = { topicId -> currentDest = NavDestination.Practice(topicId) },
                                    onNavigateToFsrs = { topicId -> currentDest = NavDestination.Fsrs(topicId) },
                                    onNavigateToGlossary = { currentDest = NavDestination.Glossary }
                                )
                            }
                            is NavDestination.Topic -> {
                                val topicVm = remember(dest.topicId) { TopicViewModel(appContainer.orchestrator, dest.topicId) }
                                TopicDetailScreen(
                                    viewModel = topicVm,
                                    onBack = { currentDest = NavDestination.Dashboard },
                                    onNavigateToPractice = { topicId -> currentDest = NavDestination.Practice(topicId) }
                                )
                            }
                            is NavDestination.Practice -> {
                                val practiceVm = remember(dest.topicId) { PracticeViewModel(appContainer.orchestrator, dest.topicId) }
                                PracticeScreen(
                                    viewModel = practiceVm,
                                    onBack = { currentDest = NavDestination.Dashboard }
                                )
                            }
                            is NavDestination.Fsrs -> {
                                val fsrsVm = remember(dest.topicId) { FsrsReviewViewModel(appContainer.orchestrator, dest.topicId) }
                                FsrsReviewScreen(
                                    viewModel = fsrsVm,
                                    onBack = { currentDest = NavDestination.Dashboard }
                                )
                            }
                            is NavDestination.FormulaLab -> {
                                val formulaVm = remember { FormulaLabViewModel() }
                                FormulaLabScreen(
                                    viewModel = formulaVm,
                                    onBack = { currentDest = NavDestination.Dashboard }
                                )
                            }
                            is NavDestination.SimulatedExam -> {
                                val examVm = remember {
                                    SimulatedExamViewModel(appContainer.biologyRepository)
                                }
                                SimulatedExamScreen(
                                    viewModel = examVm,
                                    onBack = { currentDest = NavDestination.Dashboard },
                                    onNavigateToFormulaLab = { currentDest = NavDestination.FormulaLab },
                                    onNavigateToFsrs = { currentDest = NavDestination.Fsrs("sarcomere_contraction") },
                                    onNavigateToGlossary = { currentDest = NavDestination.Glossary },
                                    onNavigateToMistakeAnalytics = { currentDest = NavDestination.MistakeAnalytics }
                                )
                            }
                            is NavDestination.MistakeAnalytics -> {
                                val mistakeVm = remember {
                                    MistakeAnalyticsViewModel(appContainer.biologyRepository)
                                }
                                MistakeAnalyticsDashboardScreen(
                                    viewModel = mistakeVm,
                                    onBack = { currentDest = NavDestination.SimulatedExam },
                                    onLaunchRemediationQuiz = { currentDest = NavDestination.SimulatedExam }
                                )
                            }
                            is NavDestination.Settings -> {
                                SettingsScreen(
                                    themeController = appContainer.themeController,
                                    onBack = { currentDest = NavDestination.Dashboard }
                                )
                            }
                            is NavDestination.CellSimulation -> {
                                CellBiologySimulationScreen(
                                    onBack = { currentDest = NavDestination.Dashboard }
                                )
                            }
                            is NavDestination.ChapterSearch -> {
                                val searchVm = remember {
                                    ChapterSearchViewModel(appContainer.chapterSearchRepository)
                                }
                                ChapterSearchScreen(
                                    viewModel = searchVm,
                                    onBack = { currentDest = NavDestination.Dashboard },
                                    onNavigateToTopic = { topicKey -> currentDest = NavDestination.Topic(topicKey) }
                                )
                            }
                        }

                        BackHandler(enabled = currentDest !is NavDestination.Dashboard) {
                            currentDest = NavDestination.Dashboard
                        }
                    }
                }
            }
        }
    }
}

