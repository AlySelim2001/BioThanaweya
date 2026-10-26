package com.example.domain.engine

object ArabicTextNormalizer {

    // Regex matching Arabic Tashkeel / Harakat
    private val TASHKEEL_REGEX = Regex("[\u064B-\u0652\u0670\u0640]")

    fun normalize(text: String): String {
        if (text.isBlank()) return ""

        var normalized = text.lowercase().trim()

        // Remove Tashkeel & Tatweel
        normalized = TASHKEEL_REGEX.replace(normalized, "")

        // Normalize Alefs
        normalized = normalized
            .replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('آ', 'ا')
            .replace('ٱ', 'ا')

        // Normalize Taa Marbuta to Haa
        normalized = normalized.replace('ة', 'ه')

        // Normalize Alef Maksura to Yaa
        normalized = normalized.replace('ى', 'ي')

        // Remove punctuation and clean excess whitespace
        normalized = normalized.replace(Regex("[.,;:!؟?()\"'\\[\\]{}\\-_/]"), " ")
        normalized = normalized.replace(Regex("\\s+"), " ").trim()

        return normalized
    }
}
