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
    data class Topic(val topicId: String) : NavDestination
    data class Practice(val topicId: String) : NavDestination
    data class Fsrs(val topicId: String) : NavDestination
    data object FormulaLab : NavDestination
    data object SimulatedExam : NavDestination
    data object Settings : NavDestination
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
            } catch (ignored: Exception) {}
        }

        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                var currentDest by remember { mutableStateOf<NavDestination>(NavDestination.Dashboard) }

                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp
                        ) {
                            NavigationBarItem(
                                selected = currentDest is NavDestination.Dashboard || currentDest is NavDestination.Topic,
                                onClick = { currentDest = NavDestination.Dashboard },
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                label = { Text("Dashboard") },
                                modifier = Modifier.testTag("nav_bottom_dashboard")
                            )
                            NavigationBarItem(
                                selected = currentDest is NavDestination.FormulaLab,
                                onClick = { currentDest = NavDestination.FormulaLab },
                                icon = { Icon(Icons.Default.Calculate, contentDescription = "Formulas") },
                                label = { Text("Formulas") },
                                modifier = Modifier.testTag("nav_bottom_formula_lab")
                            )
                            NavigationBarItem(
                                selected = currentDest is NavDestination.Practice,
                                onClick = { currentDest = NavDestination.Practice("sarcomere_contraction") },
                                icon = { Icon(Icons.Default.FactCheck, contentDescription = "Practice") },
                                label = { Text("Practice") },
                                modifier = Modifier.testTag("nav_bottom_practice")
                            )
                            NavigationBarItem(
                                selected = currentDest is NavDestination.SimulatedExam,
                                onClick = { currentDest = NavDestination.SimulatedExam },
                                icon = { Icon(Icons.Default.Timer, contentDescription = "Exam Sim") },
                                label = { Text("Exam Sim") },
                                modifier = Modifier.testTag("nav_bottom_exam_sim")
                            )
                            NavigationBarItem(
                                selected = currentDest is NavDestination.Fsrs,
                                onClick = { currentDest = NavDestination.Fsrs("sarcomere_contraction") },
                                icon = { Icon(Icons.Default.Style, contentDescription = "FSRS") },
                                label = { Text("FSRS") },
                                modifier = Modifier.testTag("nav_bottom_fsrs")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (val dest = currentDest) {
                            is NavDestination.Dashboard -> {
                                val dashboardVm = remember {
                                    DashboardViewModel(appContainer.orchestrator, appContainer.biologyRepository)
                                }
                                DashboardScreen(
                                    viewModel = dashboardVm,
                                    onNavigateToTopic = { topicId -> currentDest = NavDestination.Topic(topicId) },
                                    onNavigateToPractice = { topicId -> currentDest = NavDestination.Practice(topicId) },
                                    onNavigateToFsrs = { topicId -> currentDest = NavDestination.Fsrs(topicId) },
                                    onNavigateToFormulaLab = { currentDest = NavDestination.FormulaLab },
                                    onNavigateToSimulatedExam = { currentDest = NavDestination.SimulatedExam },
                                    onNavigateToSettings = { currentDest = NavDestination.Settings }
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
                                    onBack = { currentDest = NavDestination.Dashboard }
                                )
                            }
                            is NavDestination.Settings -> {
                                SettingsScreen(
                                    onBack = { currentDest = NavDestination.Dashboard }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

