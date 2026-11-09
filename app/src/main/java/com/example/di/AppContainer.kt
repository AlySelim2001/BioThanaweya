package com.example.di

import android.content.Context
import com.example.data.local.BioDatabase
import com.example.data.repository.AiRepositoryImpl
import com.example.data.repository.BiologyRepositoryImpl
import com.example.domain.repository.AiRepository
import com.example.domain.repository.BiologyRepository
import com.example.domain.usecase.*

class AppContainer(context: Context) {

    val database: BioDatabase by lazy {
        BioDatabase.getInstance(context)
    }

    val aiRepository: AiRepository by lazy {
        AiRepositoryImpl()
    }

    val themeController: com.example.ui.theme.ThemeController by lazy {
        com.example.ui.theme.ThemeController.create(context)
    }

    val syncManager: com.example.data.sync.SyncManager by lazy {
        com.example.data.sync.SyncManager(context, database)
    }

    val biologyRepository: BiologyRepository by lazy {
        BiologyRepositoryImpl(
            flashcardDao = database.flashcardDao(),
            progressDao = database.progressDao(),
            examAttemptDao = database.examAttemptDao(),
            topicMasteryDao = database.topicMasteryDao(),
            savedDefinitionDao = database.savedDefinitionDao(),
            keyConceptDao = database.keyConceptDao(),
            examMistakeDao = database.examMistakeDao()
        )
    }

    val chapterProgressRepository: com.example.domain.repository.ChapterProgressRepository by lazy {
        com.example.data.repository.ChapterProgressRepositoryImpl(
            chapterProgressDao = database.chapterProgressDao()
        )
    }

    val chapterSearchRepository: com.example.domain.repository.ChapterSearchRepository by lazy {
        com.example.data.repository.ChapterSearchRepositoryImpl(
            chapterContentDao = database.chapterContentDao(),
            searchHistoryDao = database.searchHistoryDao()
        )
    }

    // 7 Specialized Collaborative Agents
    val educatorAgent: Agent1EducatorUseCase by lazy {
        Agent1EducatorUseCase(aiRepository)
    }

    val visualArchitectAgent: Agent2VisualArchitectUseCase by lazy {
        Agent2VisualArchitectUseCase(aiRepository)
    }

    val assessmentAgent: Agent3AssessmentUseCase by lazy {
        Agent3AssessmentUseCase()
    }

    val examinerAgent: Agent4ExaminerUseCase by lazy {
        Agent4ExaminerUseCase()
    }

    val metacognitiveAgent: Agent5MetacognitiveUseCase by lazy {
        Agent5MetacognitiveUseCase()
    }

    val pblAgent: Agent6PblUseCase by lazy {
        Agent6PblUseCase()
    }

    val wellbeingAgent: Agent7WellbeingUseCase by lazy {
        Agent7WellbeingUseCase()
    }

    // Central Orchestrator
    val orchestrator: OrchestratorUseCase by lazy {
        OrchestratorUseCase(
            educatorAgent = educatorAgent,
            visualAgent = visualArchitectAgent,
            assessmentAgent = assessmentAgent,
            examinerAgent = examinerAgent,
            metacognitiveAgent = metacognitiveAgent,
            pblAgent = pblAgent,
            wellbeingAgent = wellbeingAgent
        )
    }
}
