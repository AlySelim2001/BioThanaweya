package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BioCyanAccent,
    onPrimary = DeepNavy,
    primaryContainer = BioTealDark,
    onPrimaryContainer = BioCyanLight,
    secondary = MolecularPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A148C),
    onSecondaryContainer = MolecularPurpleLight,
    tertiary = ExamGold,
    onTertiary = DeepNavy,
    background = DeepNavy,
    onBackground = Color(0xFFECEFF1),
    surface = SurfaceDark,
    onSurface = Color(0xFFECEFF1),
    surfaceVariant = Color(0xFF1E334D),
    onSurfaceVariant = Color(0xFFCFD8DC),
    error = AlertCoral,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BioTealPrimary,
    onPrimary = Color.White,
    primaryContainer = BioCyanLight,
    onPrimaryContainer = BioTealDark,
    secondary = MolecularPurple,
    onSecondary = Color.White,
    secondaryContainer = MolecularPurpleLight,
    onSecondaryContainer = Color(0xFF311B92),
    tertiary = Color(0xFFF57F17),
    onTertiary = Color.White,
    background = SurfaceLight,
    onBackground = DeepNavy,
    surface = CardBackgroundLight,
    onSurface = DeepNavy,
    surfaceVariant = Color(0xFFEDF2F7),
    onSurfaceVariant = Color(0xFF455A64),
    error = AlertCoral,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep tailored biology theme consistent
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
