package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

class TextToSpeechHelper(
    private val context: Context,
    private val onInitComplete: (Boolean) -> Unit = {},
    private val onSpeechStatusChange: (Boolean) -> Unit = {}
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    var isInitialized: Boolean = false
        private set
    var isSpeaking: Boolean = false
        private set

    init {
        try {
            tts = TextToSpeech(context, this)
        } catch (e: Exception) {
            Log.e("TextToSpeechHelper", "Failed to construct TextToSpeech", e)
            onInitComplete(false)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    isSpeaking = true
                    onSpeechStatusChange(true)
                }

                override fun onDone(utteranceId: String?) {
                    isSpeaking = false
                    onSpeechStatusChange(false)
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    isSpeaking = false
                    onSpeechStatusChange(false)
                }
            })
            onInitComplete(true)
        } else {
            isInitialized = false
            Log.e("TextToSpeechHelper", "TTS Initialization failed with status: $status")
            onInitComplete(false)
        }
    }

    fun speak(text: String, isArabic: Boolean = false) {
        if (!isInitialized || tts == null) return

        val locale = if (isArabic) Locale("ar") else Locale.US
        val langResult = tts?.setLanguage(locale)
        if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.w("TextToSpeechHelper", "Requested locale $locale not fully supported, falling back to default.")
            tts?.setLanguage(Locale.US)
        }

        // Clean up markdown markers for natural speech
        val cleanedText = text
            .replace(Regex("[#*`_\\[\\]()]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        val utteranceId = "tts_utterance_${System.currentTimeMillis()}"
        tts?.speak(cleanedText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stop() {
        tts?.stop()
        isSpeaking = false
        onSpeechStatusChange(false)
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("TextToSpeechHelper", "Error shutting down TTS", e)
        }
        tts = null
        isInitialized = false
        isSpeaking = false
    }
}
