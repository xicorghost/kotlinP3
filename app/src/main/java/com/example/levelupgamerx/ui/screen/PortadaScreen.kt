package com.example.levelupgamerx.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelupgamerx.ui.component.CyberpunkButton
import com.example.levelupgamerx.ui.theme.*

/**
 * Pantalla de bienvenida cyberpunk
 */
@Composable
fun PortadaScreen(
    onEntrarClick: () -> Unit,
    onAdminClick: () -> Unit
) {
    // Animación de pulsación
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkCard, DarkSurface, DarkBackground)
                )
            )
            .drawBehind {
                // Cuadrícula cyberpunk
                val gridSize = 30f
                val strokeWidth = 1f

                var x = 0f
                while (x < size.width) {
                    drawLine(
                        color = NeonGreen.copy(alpha = 0.1f),
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = strokeWidth
                    )
                    x += gridSize
                }

                var y = 0f
                while (y < size.height) {
                    drawLine(
                        color = NeonGreen.copy(alpha = 0.1f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                    y += gridSize
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo principal
            Icon(
                imageVector = Icons.Default.SportsEsports,
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                tint = NeonGreen.copy(alpha = glowAlpha)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Título principal
            Text(
                text = "LEVEL-UP",
                fontSize = 56.sp,
                fontWeight = FontWeight.Black,
                color = NeonGreen,
                letterSpacing = 5.sp
            )

            Text(
                text = "GAMER",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = CyberBlue,
                letterSpacing = 8.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtítulo
            Text(
                text = "> Tu Tienda Gamer en Chile <",
                color = TextSecondary,
                fontSize = 16.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Botón principal
            CyberpunkButton(
                text = "Entrar a la Tienda",
                onClick = onEntrarClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón admin
            OutlinedButton(
                onClick = onAdminClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CyberPurple
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(CyberPurple, CyberBlue)
                    )
                )
            ) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ACCESO ADMINISTRADOR",
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Información adicional
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "✓ Sistema de Puntos LevelUp",
                    color = Success,
                    fontSize = 14.sp
                )
                Text(
                    text = "✓ 20% Descuento para Estudiantes DUOC",
                    color = Success,
                    fontSize = 14.sp
                )
                Text(
                    text = "✓ Programa de Referidos",
                    color = Success,
                    fontSize = 14.sp
                )
            }
        }
    }
}