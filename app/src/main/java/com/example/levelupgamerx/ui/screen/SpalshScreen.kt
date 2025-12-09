package com.example.levelupgamerx.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.levelupgamerx.navigation.Rutas
import com.example.levelupgamerx.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Splash Screen con animación cyberpunk
 */
@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    // Animación de pulsación del logo
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(Unit) {
        // Animación de entrada
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(800)
        )

        delay(2000)

        // Navegar a portada
        navController.navigate(Rutas.PORTADA) {
            popUpTo(Rutas.SPLASH) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkCard, DarkBackground)
                )
            )
            .drawBehind {
                // Cuadrícula animada de fondo
                val gridSize = 30f
                val strokeWidth = 1f

                var x = 0f
                while (x < size.width) {
                    drawLine(
                        color = NeonGreen.copy(alpha = 0.05f),
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = strokeWidth
                    )
                    x += gridSize
                }

                var y = 0f
                while (y < size.height) {
                    drawLine(
                        color = NeonGreen.copy(alpha = 0.05f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                    y += gridSize
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Logo animado
            Icon(
                imageVector = Icons.Default.SportsEsports,
                contentDescription = "Level-Up Logo",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value),
                tint = NeonGreen.copy(alpha = pulseAlpha)
            )

            // Texto principal
            Text(
                text = "LEVEL-UP",
                color = NeonGreen.copy(alpha = alpha.value),
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 5.sp
            )

            Text(
                text = "GAMER",
                color = CyberBlue.copy(alpha = alpha.value),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 8.sp
            )

            // Subtítulo
            Text(
                text = "> DESAFÍA TUS LÍMITES <",
                color = TextSecondary.copy(alpha = alpha.value),
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )
        }
    }
}