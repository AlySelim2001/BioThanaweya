package com.example.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.domain.repository.AiRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.ImagePart
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.generationConfig
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

    // Lazy image model initialization using gemini-nano-banana-2.1 per guidelines
    private val imageModel by lazy {
        try {
            val config = generationConfig {
                responseModalities = listOf(ResponseModality.IMAGE)
            }
            Firebase.ai.generativeModel(
                modelName = "gemini-nano-banana-2.1",
                generationConfig = config
            )
        } catch (e: Exception) {
            Log.w(TAG, "Firebase.ai image model initialization deferred: ${e.message}")
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

    override suspend fun generateBiologicalDiagram(prompt: String): Result<Bitmap> {
        return try {
            val genModel = imageModel ?: return Result.failure(
                IllegalStateException("AI Image Model unavailable. Please ensure Firebase App Check is initialized and device is connected.")
            )
            val response = withTimeout(60_000L) {
                genModel.generateContent(prompt)
            }
            val imagePart = response.candidates.firstOrNull()
                ?.content?.parts?.filterIsInstance<ImagePart>()
                ?.firstOrNull()

            val bitmap = imagePart?.image
            if (bitmap != null) {
                Result.success(bitmap)
            } else {
                Result.failure(IllegalStateException("No image data returned in AI candidate response."))
            }
        } catch (e: TimeoutCancellationException) {
            Result.failure(IllegalStateException("Diagram generation timed out after 60 seconds. Please try again."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Biological diagram generation failed: ${e.message}")
            Result.failure(e)
        }
    }
}
