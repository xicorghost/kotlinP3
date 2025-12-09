package com.example.levelupgamerx.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelupgamerx.ui.theme.*

/**
 * Componentes reutilizables con estilo cyberpunk
 */

/**
 * Card cyberpunk con efecto de brillo neón
 */
@Composable
fun CyberpunkCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        ),
        border = BorderStroke(2.dp, NeonGreen.copy(alpha = alpha)),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick ?: {}
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

/**
 * Botón cyberpunk con efecto neón
 */
@Composable
fun CyberpunkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    buttonColor: Color = DarkCard // Usamos DarkCard como valor por defecto
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp),
        enabled = enabled && !loading,
        colors = ButtonDefaults.buttonColors(
            containerColor = DarkCard,
            contentColor = NeonGreen,
            disabledContainerColor = DarkSurface,
            disabledContentColor = TextDisabled
        ),
        border = BorderStroke(2.dp, NeonGreen),
        shape = RoundedCornerShape(4.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = NeonGreen,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text.uppercase(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }
    }
}

/**
 * TextField cyberpunk
 */
@Composable
fun CyberpunkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            isError = isError,
            singleLine = singleLine,
            maxLines = maxLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextSecondary,
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = NeonGreenDark,
                focusedLabelColor = NeonGreen,
                unfocusedLabelColor = TextSecondary,
                cursorColor = NeonGreen,
                errorBorderColor = Error,
                errorLabelColor = Error,
                errorCursorColor = Error
            ),
            shape = RoundedCornerShape(4.dp)
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

/**
 * Sistema de estrellas para calificaciones
 */
@Composable
fun RatingStars(
    rating: Float,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = CyberYellow,
    onRatingChanged: ((Int) -> Unit)? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(5) { index ->
            val filled = index < rating.toInt()
            val halfFilled = !filled && index < rating && rating % 1 >= 0.5f

            IconButton(
                onClick = { onRatingChanged?.invoke(index + 1) },
                modifier = Modifier.size(size),
                enabled = onRatingChanged != null
            ) {
                Text(
                    text = when {
                        filled -> "★"
                        halfFilled -> "⯨"
                        else -> "☆"
                    },
                    fontSize = size.value.sp,
                    color = if (filled || halfFilled) color else TextDisabled
                )
            }
        }
    }
}

/**
 * Badge de descuento DUOC
 */
@Composable
fun DescuentoDuocBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(NeonGreen, NeonGreenLight)
                ),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "20% DESCUENTO DUOC",
            color = DarkBackground,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

/**
 * Indicador de puntos y nivel
 */
@Composable
fun PuntosNivelDisplay(
    puntos: Int,
    nivel: Int,
    progresoNivel: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "NIVEL $nivel",
                    color = NeonGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$puntos PUNTOS LEVELUP",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        // Barra de progreso
        LinearProgressIndicator(
            progress = { progresoNivel },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = NeonGreen,
            trackColor = DarkCard,
        )
    }
}

/**
 * Header cyberpunk con efecto de cuadrícula
 */
@Composable
fun CyberpunkHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkCard, DarkSurface)
                )
            )
            .drawBehind {
                // Dibujar cuadrícula
                val gridSize = 30f
                val strokeWidth = 1f

                // Líneas verticales
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

                // Líneas horizontales
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
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = title.uppercase(),
                color = NeonGreen,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp
            )

            subtitle?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = TextSecondary,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Divider cyberpunk con efecto neón
 */
@Composable
fun CyberpunkDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 2.dp,
        color = NeonGreen.copy(alpha = 0.3f)
    )
}

/**
 * Chip de categoría
 */
@Composable
fun CategoriaChip(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto.uppercase()) },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = DarkSurface,
            labelColor = TextSecondary,
            selectedContainerColor = NeonGreen,
            selectedLabelColor = DarkBackground
        ),
        border = BorderStroke(
            width = 2.dp,
            color = NeonGreen
        )
    )
}

/*@Composable
fun CategoriaChip(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto.uppercase()) },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = DarkSurface,
            labelColor = TextSecondary,
            selectedContainerColor = NeonGreen,
            selectedLabelColor = DarkBackground
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = NeonGreen,
            selectedBorderColor = NeonGreen,
            borderWidth = 2.dp
        )
    )
}*/