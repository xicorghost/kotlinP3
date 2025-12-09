package com.example.levelupgamerx.domain.model

import java.text.SimpleDateFormat
import java.util.*

/**
 * Modelo de Reseña de producto
 */
data class Resena(
    val id: String = UUID.randomUUID().toString(),
    val codigoProducto: String,
    val usuarioId: String,
    val nombreUsuario: String,
    val calificacion: Int, // 1-5 estrellas
    val comentario: String,
    val fecha: Long = System.currentTimeMillis()
) {
    /**
     * Fecha formateada: "15/11/2025"
     */
    fun fechaFormateada(): String {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formato.format(Date(fecha))
    }

    /**
     * Estrellas en formato visual: ★★★★☆
     */
    fun estrellasVisuales(): String {
        return "★".repeat(calificacion) + "☆".repeat(5 - calificacion)
    }

    /**
     * Tiempo transcurrido desde la reseña (ej: "hace 2 días")
     */
    fun tiempoTranscurrido(): String {
        val ahora = System.currentTimeMillis()
        val diferencia = ahora - fecha

        val segundos = diferencia / 1000
        val minutos = segundos / 60
        val horas = minutos / 60
        val dias = horas / 24

        return when {
            dias > 0 -> "hace $dias ${if (dias == 1L) "día" else "días"}"
            horas > 0 -> "hace $horas ${if (horas == 1L) "hora" else "horas"}"
            minutos > 0 -> "hace $minutos ${if (minutos == 1L) "minuto" else "minutos"}"
            else -> "hace unos momentos"
        }
    }
}

/**
 * Reseñas iniciales para los productos (datos de la web)
 */
object ResenasIniciales {
    fun obtenerResenasIniciales(): List<Resena> = listOf(
        // Reseñas para LL001 (Código Steam)
        Resena(
            codigoProducto = "LL001",
            usuarioId = "demo1",
            nombreUsuario = "GamerPro",
            calificacion = 5,
            comentario = "Excelente código, obtuve un juego increíble. Totalmente recomendado.",
            fecha = System.currentTimeMillis() - (8 * 24 * 60 * 60 * 1000L) // hace 8 días
        ),
        Resena(
            codigoProducto = "LL001",
            usuarioId = "demo2",
            nombreUsuario = "JugadorX",
            calificacion = 4,
            comentario = "Buen servicio, llegó rápido el código.",
            fecha = System.currentTimeMillis() - (9 * 24 * 60 * 60 * 1000L)
        ),
        Resena(
            codigoProducto = "LL001",
            usuarioId = "demo3",
            nombreUsuario = "SteamLover",
            calificacion = 5,
            comentario = "Me tocó un juego que tenía en mi wishlist, increíble!",
            fecha = System.currentTimeMillis() - (11 * 24 * 60 * 60 * 1000L)
        ),

        // Reseñas para JM002 (Monopoly)
        Resena(
            codigoProducto = "JM002",
            usuarioId = "demo4",
            nombreUsuario = "BoardGamer",
            calificacion = 5,
            comentario = "El clásico Monopoly, perfecto estado y llegó bien empaquetado.",
            fecha = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000L)
        ),
        Resena(
            codigoProducto = "JM002",
            usuarioId = "demo5",
            nombreUsuario = "FamilyGamer",
            calificacion = 5,
            comentario = "Perfecto para jugar en familia, edición completa y en excelente estado.",
            fecha = System.currentTimeMillis() - (13 * 24 * 60 * 60 * 1000L)
        ),

        // Reseñas para AC001 (Control Xbox)
        Resena(
            codigoProducto = "AC001",
            usuarioId = "demo6",
            nombreUsuario = "XboxFan",
            calificacion = 5,
            comentario = "Control original, funciona perfecto. La conectividad es excelente.",
            fecha = System.currentTimeMillis() - (9 * 24 * 60 * 60 * 1000L)
        ),
        Resena(
            codigoProducto = "AC001",
            usuarioId = "demo7",
            nombreUsuario = "ConsoleGamer",
            calificacion = 4,
            comentario = "Muy buen control, la batería dura bastante. Recomendado.",
            fecha = System.currentTimeMillis() - (12 * 24 * 60 * 60 * 1000L)
        ),
        Resena(
            codigoProducto = "AC001",
            usuarioId = "demo8",
            nombreUsuario = "ProPlayer",
            calificacion = 5,
            comentario = "Ideal para juegos competitivos, respuesta inmediata.",
            fecha = System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000L)
        ),

        // Reseñas para AC002 (Auriculares)
        Resena(
            codigoProducto = "AC002",
            usuarioId = "demo9",
            nombreUsuario = "AudioPhile",
            calificacion = 5,
            comentario = "Sonido espectacular, muy cómodos incluso en sesiones largas.",
            fecha = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000L)
        ),
        Resena(
            codigoProducto = "AC002",
            usuarioId = "demo10",
            nombreUsuario = "StreamerPro",
            calificacion = 5,
            comentario = "El micrófono tiene excelente calidad, perfectos para streaming.",
            fecha = System.currentTimeMillis() - (11 * 24 * 60 * 60 * 1000L)
        ),
        Resena(
            codigoProducto = "AC002",
            usuarioId = "demo11",
            nombreUsuario = "GamerChile",
            calificacion = 4,
            comentario = "Muy buenos auriculares, el único detalle es que son un poco pesados.",
            fecha = System.currentTimeMillis() - (13 * 24 * 60 * 60 * 1000L)
        ),

        // Más reseñas para otros productos...
        Resena(
            codigoProducto = "CO001",
            usuarioId = "demo12",
            nombreUsuario = "PSFanatic",
            calificacion = 5,
            comentario = "La mejor consola que he tenido, los gráficos son impresionantes!",
            fecha = System.currentTimeMillis() - (8 * 24 * 60 * 60 * 1000L)
        ),
        Resena(
            codigoProducto = "CO001",
            usuarioId = "demo13",
            nombreUsuario = "NextGen",
            calificacion = 5,
            comentario = "Carga super rápida, no más tiempos de espera. Vale cada peso.",
            fecha = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000L)
        ),
        Resena(
            codigoProducto = "CO001",
            usuarioId = "demo14",
            nombreUsuario = "Gamer2025",
            calificacion = 4,
            comentario = "Excelente consola, aunque difícil de conseguir. Llegó bien empaquetada.",
            fecha = System.currentTimeMillis() - (12 * 24 * 60 * 60 * 1000L)
        ),
        Resena(
            codigoProducto = "CO001",
            usuarioId = "demo15",
            nombreUsuario = "VideoJuegos",
            calificacion = 5,
            comentario = "El DualSense es una maravilla, experiencia de juego única.",
            fecha = System.currentTimeMillis() - (15 * 24 * 60 * 60 * 1000L)
        )
    )
}

/**
 * Validación de reseña
 */
fun Resena.validar(): ResultadoValidacion {
    return when {
        calificacion !in 1..5 -> ResultadoValidacion.Error("La calificación debe estar entre 1 y 5")
        comentario.isBlank() -> ResultadoValidacion.Error("El comentario no puede estar vacío")
        comentario.length < 10 -> ResultadoValidacion.Error("El comentario debe tener al menos 10 caracteres")
        comentario.length > 500 -> ResultadoValidacion.Error("El comentario no puede exceder 500 caracteres")
        else -> ResultadoValidacion.Exitoso
    }
}