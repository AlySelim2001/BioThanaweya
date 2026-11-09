package com.example.domain.engine

import com.example.data.model.Flashcard

object AnkiExportHelper {

    fun exportToAnkiJson(topicTitle: String, flashcards: List<Flashcard>): String {
        val notesJson = flashcards.joinToString(",\n    ") { card ->
            val frontEscaped = escapeJson(card.frontPrompt)
            val backEscaped = escapeJson(card.backAnswer)
            val categoryEscaped = escapeJson(card.category)
            """{
      "id": "${card.id}",
      "deckName": "BioThanaweya::${escapeJson(topicTitle)}",
      "modelName": "Basic (and reversed card)",
      "fields": {
        "Front": "$frontEscaped",
        "Back": "$backEscaped"
      },
      "tags": ["thanaweya_amma", "biology", "$categoryEscaped", "unit_${card.boxLevel}"]
    }"""
        }

        return """{
  "__version__": 2,
  "creator": "BioThanaweya Scholar Sidekick AI",
  "topic": "${escapeJson(topicTitle)}",
  "cardsCount": ${flashcards.size},
  "notes": [
    $notesJson
  ]
}"""
    }

    private fun escapeJson(text: String): String {
        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}
