/*package com.example.levelupgamerx.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)*/
package com.example.levelupgamerx.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.levelupgamerx.R

// =====================================================
// TIPOGRAFÍA LEVEL-UP GAMER
// Fuente retro/cyberpunk similar a VT323 de la web
// =====================================================

// Configuración de Google Fonts
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Fuente principal: VT323 (fuente monoespaciada retro)
val vt323FontName = GoogleFont("VT323")

val VT323FontFamily = FontFamily(
    Font(googleFont = vt323FontName, fontProvider = provider)
)

// Fuente alternativa: Orbitron (cyberpunk/tech)
val orbitronFontName = GoogleFont("Orbitron")

val OrbitronFontFamily = FontFamily(
    Font(googleFont = orbitronFontName, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = orbitronFontName, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = orbitronFontName, fontProvider = provider, weight = FontWeight.Black)
)

// Tipografía Material 3 personalizada
val LevelUpTypography = Typography(
    // Títulos grandes (Logo, pantallas principales)
    displayLarge = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 57.sp,
        letterSpacing = 3.sp
    ),
    displayMedium = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        letterSpacing = 2.sp
    ),
    displaySmall = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        letterSpacing = 1.sp
    ),

    // Encabezados (Títulos de sección)
    headlineLarge = TextStyle(
        fontFamily = VT323FontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        letterSpacing = 1.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = VT323FontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        letterSpacing = 1.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = VT323FontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),

    // Títulos (Cards, diálogos)
    titleLarge = TextStyle(
        fontFamily = VT323FontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = VT323FontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = VT323FontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),

    // Cuerpo (Texto general)
    bodyLarge = TextStyle(
        fontFamily = VT323FontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = VT323FontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodySmall = TextStyle(
        fontFamily = VT323FontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    // Etiquetas (Botones, chips)
    labelLarge = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)