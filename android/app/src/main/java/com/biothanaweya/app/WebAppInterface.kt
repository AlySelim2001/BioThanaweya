package com.biothanaweya.app

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.webkit.JavascriptInterface
import android.widget.Toast

/**
 * Native JavaScript Bridge for BioThanaweya.
 * Exposes device-level hardware capabilities (tactile haptics, native toasts, device telemetry)
 * to the WebView client application.
 */
class WebAppInterface(private val context: Context) {

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    /**
     * Triggers native tactile haptic feedback matching the requested intensity pattern.
     * @param intensity "light", "medium", "heavy", or "success"
     */
    @JavascriptInterface
    fun triggerHaptic(intensity: String) {
        val vib = vibrator ?: return
        if (!vib.hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            when (intensity.lowercase()) {
                "light" -> {
                    val effect = VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE)
                    vib.vibrate(effect)
                }
                "medium" -> {
                    val effect = VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE)
                    vib.vibrate(effect)
                }
                "heavy" -> {
                    val effect = VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE)
                    vib.vibrate(effect)
                }
                "success" -> {
                    val timings = longArrayOf(0, 30, 60, 45)
                    val amplitudes = intArrayOf(0, 150, 0, 200)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    vib.vibrate(effect)
                }
                "error" -> {
                    val timings = longArrayOf(0, 50, 40, 50, 40, 50)
                    val amplitudes = intArrayOf(0, 220, 0, 220, 0, 220)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    vib.vibrate(effect)
                }
                else -> {
                    val effect = VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE)
                    vib.vibrate(effect)
                }
            }
        } else {
            @Suppress("DEPRECATION")
            when (intensity.lowercase()) {
                "light" -> vib.vibrate(15)
                "medium" -> vib.vibrate(35)
                "heavy" -> vib.vibrate(60)
                "success" -> vib.vibrate(longArrayOf(0, 30, 60, 45), -1)
                "error" -> vib.vibrate(longArrayOf(0, 50, 40, 50, 40, 50), -1)
                else -> vib.vibrate(25)
            }
        }
    }

    /**
     * Displays a native Android Toast notification.
     */
    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Reports whether the web application is running inside the native Android container.
     */
    @JavascriptInterface
    fun isNativeAndroid(): Boolean {
        return true
    }

    /**
     * Returns the build version of the native container.
     */
    @JavascriptInterface
    fun getAppVersion(): String {
        return "1.0.0"
    }
}
