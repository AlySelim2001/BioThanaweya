package com.example.util

/**
 * ArabicSearchUtils:
 * High-performance offline text normalizer and search helpers for Arabic biology curriculum.
 * Handles diacritics (tashkeel), hamza variations, yeh/alef maksura, ta marbuta,
 * kashida, and tokenized snippet extraction with search term highlighting.
 */
object ArabicSearchUtils {

    // Regex to match Arabic diacritics (fatha, damma, kasra, sukun, shadda, tanween)
    private val TASHKEEL_REGEX = Regex("[\\u064B-\\u065F\\u0670]")

    // Regex to match punctuation and non-alphanumeric separators
    private val PUNCTUATION_REGEX = Regex("[\\p{Punct}&&[^%_]]")

    /**
     * Normalizes an Arabic string for resilient, accent-insensitive search lookups:
     * - Strips all harakat / tashkeel
     * - Converts all alef variations (أ, إ, آ, ء) to bare alef (ا)
     * - Converts alef maksura (ى) to yeh (ي)
     * - Converts ta marbuta (ة) to heh (ه)
     * - Removes kashida / tatweel (ـ)
     * - Collapses multiple spaces
     */
    fun normalize(text: String?): String {
        if (text.isNullOrBlank()) return ""
        var s = text.lowercase()
        s = s.replace(TASHKEEL_REGEX, "")
        s = s.replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('آ', 'ا')
            .replace('ٱ', 'ا')
            .replace('ى', 'ي')
            .replace('ة', 'ه')
            .replace("ـ", "")
        s = s.replace(PUNCTUATION_REGEX, " ")
        return s.trim().replace(Regex("\\s+"), " ")
    }

    /**
     * Checks if target text contains the query using Arabic normalization and resilient token matching.
     */
    fun matches(target: String, query: String): Boolean {
        if (query.isBlank()) return true
        val normTarget = normalize(target)
        val queryTokens = normalize(query).split(" ").filter { it.isNotBlank() }
        if (queryTokens.isEmpty()) return true
        val targetWords = normTarget.split(" ").filter { it.isNotBlank() }
        return queryTokens.all { token ->
            normTarget.contains(token) || targetWords.any { word -> matchesWordFuzzy(word, token) }
        }
    }

    private fun matchesWordFuzzy(targetWord: String, token: String): Boolean {
        if (targetWord.contains(token) || token.contains(targetWord)) return true
        // Resilient handling of Arabic transliteration variants (e.g. كريبس vs كربس, جليكوجين vs جلايكوجين)
        val cleanTarget = targetWord.replace("ي", "").replace("و", "")
        val cleanToken = token.replace("ي", "").replace("و", "")
        if (cleanTarget.length >= 3 && cleanToken.length >= 3 && (cleanTarget.contains(cleanToken) || cleanToken.contains(cleanTarget))) {
            return true
        }
        return false
    }

    /**
     * Extracts a contextual snippet around the matched query from long body text.
     * Returns a truncated window around the first match, with ellipses.
     */
    fun extractSnippet(fullText: String, query: String, maxLength: Int = 140): String {
        if (fullText.isBlank()) return ""
        if (query.isBlank()) {
            return if (fullText.length <= maxLength) fullText else fullText.take(maxLength) + "..."
        }

        val normText = normalize(fullText)
        val firstToken = normalize(query).split(" ").firstOrNull { it.isNotBlank() } ?: ""

        val matchIndex = if (firstToken.isNotEmpty()) {
            normText.indexOf(firstToken)
        } else -1

        if (matchIndex < 0) {
            return if (fullText.length <= maxLength) fullText else fullText.take(maxLength) + "..."
        }

        val start = (matchIndex - 40).coerceAtLeast(0)
        val end = (start + maxLength).coerceAtMost(fullText.length)

        var snippet = fullText.substring(start, end).trim()
        if (start > 0) {
            snippet = "... $snippet"
        }
        if (end < fullText.length) {
            snippet = "$snippet ..."
        }
        return snippet
    }

    /**
     * Calculates relevance score: higher score means closer match to titles and keywords.
     */
    fun calculateScore(
        query: String,
        sectionTitle: String,
        keywords: String,
        chapterTitle: String,
        content: String,
        baseImportance: Int = 1
    ): Int {
        val normQ = normalize(query)
        if (normQ.isBlank()) return baseImportance

        var score = baseImportance
        val normSection = normalize(sectionTitle)
        val normKeywords = normalize(keywords)
        val normChapter = normalize(chapterTitle)
        val normContent = normalize(content)

        if (normSection.contains(normQ)) score += 50
        if (normSection == normQ) score += 40
        if (normKeywords.contains(normQ)) score += 30
        if (normChapter.contains(normQ)) score += 20
        if (normContent.contains(normQ)) score += 10

        // Bonus for token matches
        val tokens = normQ.split(" ").filter { it.isNotBlank() }
        for (token in tokens) {
            if (normSection.contains(token)) score += 15
            if (normKeywords.contains(token)) score += 10
            if (normContent.contains(token)) score += 5
        }

        return score
    }
}
