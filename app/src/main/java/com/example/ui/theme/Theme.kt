package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// M3 Eye-Strain Reduction Dark Scheme: Deep soothing slate/navy with softened contrast
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2DD4BF),           // Soft luminous emerald teal
    onPrimary = Color(0xFF042F2E),
    primaryContainer = Color(0xFF115E59),  // Deep soothing teal container
    onPrimaryContainer = Color(0xFFCCFBF1),
    secondary = Color(0xFFA78BFA),         // Gentle lilac purple
    onSecondary = Color(0xFF2E1065),
    secondaryContainer = Color(0xFF5B21B6),
    onSecondaryContainer = Color(0xFFEDE9FE),
    tertiary = Color(0xFFFBBF24),          // Warm golden amber (low glare)
    onTertiary = Color(0xFF451A03),
    background = Color(0xFF0B0F19),        // Deep midnight slate background
    onBackground = Color(0xFFE2E8F0),      // Soft off-white to eliminate glare
    surface = Color(0xFF111827),           // Low-glare dark card surface
    onSurface = Color(0xFFF1F5F9),         // Readable, soft high-legibility text
    surfaceVariant = Color(0xFF1E293B),    // Subtle slate elevation
    onSurfaceVariant = Color(0xFF94A3B8),  // Muted secondary text
    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF334155),
    error = Color(0xFFF87171),
    onError = Color(0xFF450A0A)
)

// M3 Light Scheme: Soothing soft light palette without harsh white glaring
private val LightColorScheme = lightColorScheme(
    primary = BioTealPrimary,              // Calm emerald #00897B
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2F1),  // Soft mint container
    onPrimaryContainer = Color(0xFF004D40),
    secondary = MolecularPurple,           // #7E57C2
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE7F6),
    onSecondaryContainer = Color(0xFF311B92),
    tertiary = Color(0xFFD97706),
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),        // Soothing off-white (prevents harsh glare)
    onBackground = Color(0xFF0F172A),      // Deep slate-navy
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),    // Soft card variant
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    error = AlertCoral,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    darkTheme: Boolean = themeMode.isDark(),
    dynamicColor: Boolean = false,
    themeController: ThemeController? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val contentWithTheme = @Composable {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

    if (themeController != null) {
        CompositionLocalProvider(LocalThemeController provides themeController) {
            contentWithTheme()
        }
    } else {
        contentWithTheme()
    }
}
