package com.example.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppThemeMode(val titleArabic: String, val titleEnglish: String, val iconName: String) {
    SYSTEM("تلقائي للنظام", "System Default", "settings_brightness"),
    LIGHT("الوضع النهاري", "Light Mode", "light_mode"),
    DARK("الوضع الليلي (حماية العين)", "Eye-Strain Dark Mode", "dark_mode");

    fun shouldApplyDark(systemInDark: Boolean): Boolean {
        return when (this) {
            SYSTEM -> systemInDark
            LIGHT -> false
            DARK -> true
        }
    }

    @Composable
    fun isDark(): Boolean {
        return shouldApplyDark(isSystemInDarkTheme())
    }
}

class ThemeController(
    private val prefs: SharedPreferences? = null
) {
    private val _themeMode = MutableStateFlow(
        prefs?.getString("pref_app_theme_mode", AppThemeMode.SYSTEM.name)?.let {
            runCatching { AppThemeMode.valueOf(it) }.getOrDefault(AppThemeMode.SYSTEM)
        } ?: AppThemeMode.SYSTEM
    )
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
        prefs?.edit()?.putString("pref_app_theme_mode", mode.name)?.apply()
    }

    companion object {
        fun create(context: Context): ThemeController {
            val prefs = context.getSharedPreferences("biothanaweya_theme_prefs", Context.MODE_PRIVATE)
            return ThemeController(prefs)
        }
    }
}

val LocalThemeController = staticCompositionLocalOf { ThemeController() }
