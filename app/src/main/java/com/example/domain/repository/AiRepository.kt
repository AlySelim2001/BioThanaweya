package com.example.domain.repository

import android.graphics.Bitmap

interface AiRepository {
    suspend fun generateExplanation(topic: String, style: String): Result<String>
    suspend fun evaluateAnswer(question: String, studentAnswer: String, modelAnswer: String): Result<String>
    suspend fun generatePblProject(topic: String): Result<String>
    suspend fun generateBiologicalDiagram(prompt: String): Result<Bitmap>
}
