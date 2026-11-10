package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechHelper(
    context: Context,
    private val onSpeakingChanged: (Boolean) -> Unit = {},
    private val onInitComplete: () -> Unit = {},
    private val onSpeechStatusChange: (Boolean) -> Unit = onSpeakingChanged
) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.language = Locale("ar")
                onInitComplete()
            }
        }
    }

    private fun notifyStatus(speaking: Boolean) {
        onSpeakingChanged(speaking)
        onSpeechStatusChange(speaking)
    }

    fun speak(text: String, isArabic: Boolean = true) {
        if (!isInitialized || tts == null) return
        val loc = if (isArabic) Locale("ar") else Locale.US
        tts?.language = loc
        notifyStatus(true)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TTS_${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
        notifyStatus(false)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        notifyStatus(false)
    }
}
