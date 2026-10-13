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
    primary = LavenderPrimaryLight,
    onPrimary = Color(0xFF1E1038),
    primaryContainer = Color(0xFF4A2A82),
    onPrimaryContainer = Color(0xFFEDE7F6),
    secondary = LavenderSecondary,
    onSecondary = Color(0xFF1E1038),
    secondaryContainer = Color(0xFF3B2068),
    onSecondaryContainer = Color(0xFFF3EEFA),
    tertiary = LavenderTertiary,
    onTertiary = Color(0xFF1E1038),
    background = LavenderDarkBackground,
    onBackground = LavenderDarkTextPrimary,
    surface = LavenderDarkSurface,
    onSurface = LavenderDarkTextPrimary,
    surfaceVariant = LavenderDarkSurfaceVariant,
    onSurfaceVariant = LavenderDarkTextSecondary,
    outline = LavenderOutline,
    outlineVariant = LavenderDarkSurfaceVariant,
    error = RoseWarning,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = LavenderPrimary,
    onPrimary = Color.White,
    primaryContainer = LavenderContainer,
    onPrimaryContainer = OnLavenderContainer,
    secondary = LavenderSecondary,
    onSecondary = Color.White,
    secondaryContainer = LavenderSecondaryContainer,
    onSecondaryContainer = OnLavenderSecondaryContainer,
    tertiary = LavenderTertiary,
    onTertiary = Color.White,
    background = LightLavenderBackground,
    onBackground = LavenderTextPrimaryLight,
    surface = PureWhiteSurface,
    onSurface = LavenderTextPrimaryLight,
    surfaceVariant = LightLavenderSurfaceVariant,
    onSurfaceVariant = LavenderTextSecondaryLight,
    outline = LavenderOutline,
    outlineVariant = LavenderOutlineVariant,
    error = RoseWarning,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to ensure our custom Light Lavender + White theme is strictly preserved
    content: @Composable () -> Unit,
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
