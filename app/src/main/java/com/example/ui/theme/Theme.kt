package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MonexaDarkColorScheme = darkColorScheme(
    primary = MonexaCyanPrimary,
    onPrimary = Color(0xFF001424),
    primaryContainer = MonexaDarkSurfaceVariant,
    onPrimaryContainer = MonexaCyanPrimary,
    secondary = MonexaElectricBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E3A8A),
    onSecondaryContainer = Color(0xFF93C5FD),
    tertiary = MonexaEmeraldSuccess,
    onTertiary = Color(0xFF00210A),
    background = MonexaDarkBackground,
    onBackground = MonexaTextPrimaryDark,
    surface = MonexaDarkSurface,
    onSurface = MonexaTextPrimaryDark,
    surfaceVariant = MonexaDarkSurfaceVariant,
    onSurfaceVariant = MonexaTextSecondaryDark,
    outline = MonexaCardBorder,
    error = MonexaErrorRed,
    onError = Color.White
)

private val MonexaLightColorScheme = lightColorScheme(
    primary = MonexaLightPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = MonexaElectricBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDBEAFE),
    onSecondaryContainer = Color(0xFF1D4ED8),
    tertiary = Color(0xFF059669),
    onTertiary = Color.White,
    background = MonexaLightBackground,
    onBackground = MonexaTextPrimaryLight,
    surface = MonexaLightSurface,
    onSurface = MonexaTextPrimaryLight,
    surfaceVariant = MonexaLightSurfaceVariant,
    onSurfaceVariant = MonexaTextSecondaryLight,
    outline = MonexaCardBorderLight,
    error = MonexaErrorRed,
    onError = Color.White
)

@Composable
fun MonexaMonitorTheme(
    darkTheme: Boolean = true, // Default to futuristic dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MonexaDarkColorScheme else MonexaLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backwards compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MonexaMonitorTheme(darkTheme = darkTheme, content = content)
}
