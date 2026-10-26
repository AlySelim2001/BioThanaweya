package com.example.domain.repository

interface AiRepository {
    suspend fun generateExplanation(topic: String, style: String): Result<String>
    suspend fun evaluateAnswer(question: String, studentAnswer: String, modelAnswer: String): Result<String>
    suspend fun generatePblProject(topic: String): Result<String>
}
