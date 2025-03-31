package com.challenges.dailychallenges.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8DEF8),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF0F1B2B),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFB00020)
)

private val NeonDarkColors = darkColorScheme(
    primary = NeonBlue,
    onPrimary = PureBlack,
    primaryContainer = DeepDarkBlue,
    onPrimaryContainer = NeonBlue,
    
    secondary = NeonPink,
    onSecondary = PureBlack,
    secondaryContainer = DarkBlue,
    onSecondaryContainer = NeonPink,
    
    tertiary = NeonGreen,
    onTertiary = PureBlack,
    tertiaryContainer = DeepDarkBlue,
    onTertiaryContainer = NeonGreen,
    
    background = BlackBlue,
    onBackground = Color.White,
    
    surface = DeepDarkBlue,
    onSurface = Color.White,
    surfaceVariant = DarkBlue,
    onSurfaceVariant = Color.LightGray,
    
    error = NeonYellow,
    onError = PureBlack,
    errorContainer = DeepDarkBlue,
    onErrorContainer = NeonYellow,
    
    outline = NeonBlueAlpha70
)

@Composable
fun ChallengesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> NeonDarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if (darkTheme) BlackBlue.toArgb() else colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}