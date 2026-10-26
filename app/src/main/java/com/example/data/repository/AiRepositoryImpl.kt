package com.example.data.repository

import android.util.Log
import com.example.domain.repository.AiRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AiRepositoryImpl"

@Singleton
class AiRepositoryImpl @Inject constructor() : AiRepository {

    // Lazy generative model initialization per firebase-ai-logic-android guidelines
    private val model by lazy {
        try {
            Firebase.ai.generativeModel("gemini-3.5-flash-lite")
        } catch (e: Exception) {
            Log.w(TAG, "Firebase.ai initialization deferred: ${e.message}")
            null
        }
    }

    override suspend fun generateExplanation(topic: String, style: String): Result<String> {
        return try {
            val genModel = model ?: return Result.failure(IllegalStateException("AI model unavailable offline"))
            val response = withTimeout(45_000L) {
                genModel.generateContent(
                    "You are an Elite Thanaweya Amma Biology Educator. Provide a $style explanation for: $topic"
                )
            }
            val text = response.text
            if (!text.isNullOrBlank()) {
                Result.success(text)
            } else {
                Result.failure(IllegalStateException("Empty AI response"))
            }
        } catch (e: TimeoutCancellationException) {
            Result.failure(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "AI generation failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun evaluateAnswer(
        question: String,
        studentAnswer: String,
        modelAnswer: String
    ): Result<String> {
        return try {
            val genModel = model ?: return Result.failure(IllegalStateException("AI model unavailable offline"))
            val prompt = """
                You are an official Egyptian Ministry of Education Biology examiner.
                Question: $question
                Model Answer: $modelAnswer
                Student Answer: $studentAnswer
                Evaluate this answer strictly according to Thanaweya Amma standards and provide feedback.
            """.trimIndent()
            val response = withTimeout(45_000L) {
                genModel.generateContent(prompt)
            }
            val text = response.text
            if (!text.isNullOrBlank()) {
                Result.success(text)
            } else {
                Result.failure(IllegalStateException("Empty AI response"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generatePblProject(topic: String): Result<String> {
        return try {
            val genModel = model ?: return Result.failure(IllegalStateException("AI model unavailable offline"))
            val response = withTimeout(45_000L) {
                genModel.generateContent("Create a practical biology project task for Thanaweya Amma on: $topic")
            }
            Result.success(response.text ?: "")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
