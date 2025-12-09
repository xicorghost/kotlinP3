/*package com.example.levelupgamerx.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun LevelupgamerxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
}*/
package com.example.levelupgamerx.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Esquema de colores oscuro cyberpunk para Level-Up Gamer
 * Solo modo oscuro - temática retro/gaming
 */
private val DarkColorScheme = darkColorScheme(
    // Colores principales
    primary = NeonGreen,
    onPrimary = DarkBackground,
    primaryContainer = DarkCard,
    onPrimaryContainer = NeonGreenLight,

    // Colores secundarios
    secondary = CyberBlue,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = CyberBlue,

    // Colores terciarios
    tertiary = CyberPurple,
    onTertiary = DarkBackground,
    tertiaryContainer = DarkSurface,
    onTertiaryContainer = CyberPurple,

    // Fondos
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,

    // Bordes y contornos
    outline = NeonGreen,
    outlineVariant = NeonGreenDark,

    // Estados
    error = Error,
    onError = DarkBackground,
    errorContainer = DarkSurface,
    onErrorContainer = Error,

    // Scrim (overlay oscuro)
    scrim = BlackTransparent70,

    // Contenedores inversos
    inverseSurface = NeonGreen,
    inverseOnSurface = DarkBackground,
    inversePrimary = DarkCard,

    // Tono de superficie
    surfaceTint = NeonGreenTransparent30


)

/**
 * Tema principal de Level-Up Gamer
 * Siempre en modo oscuro con estética cyberpunk
 */
@Composable
fun LevelUpGamerTheme(
    darkTheme: Boolean = true, // Siempre oscuro
    dynamicColor: Boolean = false, // No usamos colores dinámicos del sistema
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LevelUpTypography,
        content = content
    )
}