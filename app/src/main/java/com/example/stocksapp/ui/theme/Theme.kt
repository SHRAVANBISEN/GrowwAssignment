package com.example.stocksapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkAccent,
    onPrimary = Color.Black,
    primaryContainer = DarkAccent.copy(alpha = 0.3f),
    onPrimaryContainer = DarkOnBackground,

    secondary = DarkAccent,
    onSecondary = Color.Black,
    secondaryContainer = DarkAccent.copy(alpha = 0.2f),
    onSecondaryContainer = DarkOnBackground,

    tertiary = DarkAccent,
    onTertiary = Color.Black,
    tertiaryContainer = DarkAccent.copy(alpha = 0.2f),
    onTertiaryContainer = DarkOnBackground,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkBackground,
    onSurface = DarkOnBackground,
    surfaceVariant = DarkBackground.copy(alpha = 0.8f),
    onSurfaceVariant = DarkOnBackground.copy(alpha = 0.8f),

    surfaceTint = DarkAccent,
    inverseSurface = DarkOnBackground,
    inverseOnSurface = DarkBackground,

    error = Color(0xFFFF6B6B),
    onError = Color.Black,
    errorContainer = Color(0xFFFF6B6B).copy(alpha = 0.2f),
    onErrorContainer = DarkOnBackground,

    outline = DarkOnBackground.copy(alpha = 0.5f),
    outlineVariant = DarkOnBackground.copy(alpha = 0.3f),

    scrim = Color.Black.copy(alpha = 0.5f)
)

private val LightColorScheme = lightColorScheme(
    primary = LightAccent,
    onPrimary = Color.Black,
    primaryContainer = LightAccent.copy(alpha = 0.3f),
    onPrimaryContainer = LightOnBackground,

    secondary = LightAccent,
    onSecondary = Color.Black,
    secondaryContainer = LightAccent.copy(alpha = 0.2f),
    onSecondaryContainer = LightOnBackground,

    tertiary = LightAccent,
    onTertiary = Color.Black,
    tertiaryContainer = LightAccent.copy(alpha = 0.2f),
    onTertiaryContainer = LightOnBackground,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightBackground,
    onSurface = LightOnBackground,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = LightOnBackground.copy(alpha = 0.8f),

    surfaceTint = LightAccent,
    inverseSurface = LightOnBackground,
    inverseOnSurface = LightBackground,

    error = Color(0xFFD32F2F),
    onError = Color.White,
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = Color(0xFFB71C1C),

    outline = LightOnBackground.copy(alpha = 0.5f),
    outlineVariant = LightOnBackground.copy(alpha = 0.3f),

    scrim = Color.Black.copy(alpha = 0.5f)
)

@Composable
fun StocksAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
