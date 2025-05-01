package com.example.debt.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlin.math.abs


val lightBGColors = listOf(
    Color(0xFFF8BBD0), // light pink
    Color(0xFFD1C4E9), // light purple
    Color(0xFFBBDEFB), // light blue
    Color(0xFFB2EBF2), // light cyan
    Color(0xFFB2DFDB), // light teal
    Color(0xFFC8E6C9), // light green
    Color(0xFFF0F4C3), // light lime
    Color(0xFFFFF9C4), // light yellow
    Color(0xFFFFE0B2), // light orange
    Color(0xFFFFCCBC), // light deep orange
    Color(0xFFE1BEE7), // light light purple
    Color(0xFFCFD8DC)  // light blue grey
)

val darkBGColors = listOf(
    Color(0xFFD17F94), // dusty rose
    Color(0xFFA58CC9), // muted purple
    Color(0xFF7FA8D1), // denim blue
    Color(0xFF7DB8C0), // teal grey
    Color(0xFF7FAFA7), // slate teal
    Color(0xFF96BFA4), // sage green
    Color(0xFFC5C78D), // olive sand
    Color(0xFFD9C87B), // golden sand
    Color(0xFFD9B17D), // amber sand
    Color(0xFFD99E8A), // terracotta
    Color(0xFFB58CC2), // dusty lavender
    Color(0xFF9FAFBA)  // storm grey
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val themeMode = PreferenceCache.selectedTheme

    val colorScheme = when(themeMode) {
        PreferenceCache.ThemeMode.LIGHT -> LightColorScheme
        PreferenceCache.ThemeMode.SYSTEM -> if (isSystemInDarkTheme()) { DarkColorScheme } else { LightColorScheme }
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

object AppColors {

    val textPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.onBackground
    val textSecondary: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)


    val accentPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.primary
    val accentSecondary: Color
        @Composable get() = MaterialTheme.colorScheme.secondary


    val background: Color
        @Composable get() = MaterialTheme.colorScheme.background
    val surface: Color
        @Composable get() = MaterialTheme.colorScheme.surface


    val error: Color
        @Composable get() = MaterialTheme.colorScheme.error
    val success: Color
        @Composable get() = Color(0xFF4CAF50) // Зеленый для успеха


    val divider: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)

    val yellowLight: Color
        @Composable get() = if (isSystemInDarkTheme()) {
            Color(0xFFFFF176)
        } else {
            Color(0xFFFFEB3B)
        }
    val yellowDark: Color
        @Composable get() = if (isSystemInDarkTheme()) {
            Color(0xFFFFA000)
        } else {
            Color(0xFFFFC107)
        }
}

fun generateColorScheme(id: Long, isSystemInDarkTheme: Boolean): Color {
    val index = abs(id.hashCode()) % (lightBGColors.size + darkBGColors.size)
    return if (isSystemInDarkTheme) darkBGColors[index] else lightBGColors[index]
}

private val YellowPrimary = Color(0xFFFFC107)
private val YellowSecondary = Color(0xFFFFA000)
private val YellowTertiary = Color(0xFFFFD54F)

private val LightColorScheme = lightColorScheme(
    primary = YellowPrimary,
    primaryContainer = YellowTertiary,
    secondary = YellowSecondary,
    secondaryContainer = YellowTertiary,
    tertiary = Color(0xFF424242),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFE7E7E7),
    error = Color(0xFFB00020),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
    onError = Color(0xFFFFFFFF)
)

private val DarkColorScheme = darkColorScheme(
    primary = YellowPrimary,
    primaryContainer = YellowSecondary,
    secondary = YellowTertiary,
    secondaryContainer = YellowSecondary,
    tertiary = Color(0xFFBDBDBD),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFB00020),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onError = Color(0xFF000000)
)
