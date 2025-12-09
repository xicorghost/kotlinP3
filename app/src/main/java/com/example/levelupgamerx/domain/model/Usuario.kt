package com.example.levelupgamerx.domain.model

/**
 * Modelo de Usuario con sistema completo de Level-Up Gamer
 * Incluye: puntos, niveles, referidos, descuento DUOC
 */
data class Usuario(
    val id: String = "",
    val nombre: String,
    val email: String,
    val edad: Int,
    val contrasena: String = "", // En producción: nunca guardar en texto plano

    // Sistema de puntos y niveles
    val puntos: Int = 0,
    val nivel: Int = 1,

    // Sistema de referidos
    val codigoReferido: String = generarCodigoReferido(),
    val referidoPor: String? = null, // Código de quien lo refirió
    val cantidadReferidos: Int = 0,

    // Descuento DUOC
    val esDuoc: Boolean = false,

    // Productos comprados (para poder dejar reseñas)
    val productosComprados: List<String> = emptyList(),

    // Rol (usuario o admin)
    val rol: Rol = Rol.USUARIO,

    // Fecha de registro
    val fechaRegistro: Long = System.currentTimeMillis(),

    val fotoPerfil: String? = null // <-- ¡AGREGADO PARA SOLUCIONAR EL ERROR!
) {
    /**
     * Calcula el nivel basado en los puntos
     * Cada 500 puntos = 1 nivel
     */
    fun calcularNivel(): Int = (puntos / 500) + 1

    /**
     * Puntos necesarios para el siguiente nivel
     */
    fun puntosParaSiguienteNivel(): Int {
        val puntosDelNivelActual = (nivel - 1) * 500
        val puntosDelSiguienteNivel = nivel * 500
        return puntosDelSiguienteNivel - puntos
    }

    /**
     * Progreso hacia el siguiente nivel (0.0 a 1.0)
     */
    fun progresoNivel(): Float {
        val puntosDelNivelActual = (nivel - 1) * 500
        val puntosDelSiguienteNivel = nivel * 500
        val puntosEnEsteNivel = puntos - puntosDelNivelActual
        val puntosNecesarios = puntosDelSiguienteNivel - puntosDelNivelActual
        return (puntosEnEsteNivel.toFloat() / puntosNecesarios).coerceIn(0f, 1f)
    }

    /**
     * Descuento aplicable en compras
     * - DUOC: 20% (0.8)
     * - Normal: 0% (1.0)
     */
    fun multiplicadorDescuento(): Double = if (esDuoc) 0.8 else 1.0

    /**
     * Porcentaje de descuento en formato texto
     */
    fun textoDescuento(): String = if (esDuoc) "20% DESCUENTO DUOC" else "Sin descuento"

    /**
     * Verifica si puede dejar reseña en un producto
     */
    fun puedeResenar(codigoProducto: String): Boolean {
        return productosComprados.contains(codigoProducto)
    }

    companion object {
        /**
         * Genera un código de referido único
         * Formato: LUG + 6 caracteres alfanuméricos
         */
        fun generarCodigoReferido(): String {
            val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            val codigo = (1..6)
                .map { caracteres.random() }
                .joinToString("")
            return "LUG$codigo"
        }
    }
}

/**
 * Roles del sistema
 */
enum class Rol {
    USUARIO,
    ADMIN
}

/**
 * DTO para registro de usuario
 */
data class RegistroUsuario(
    val nombre: String,
    val email: String,
    val edad: Int,
    val contrasena: String,
    val codigoReferido: String? = null
)

/**
 * DTO para login
 */
data class LoginUsuario(
    val email: String,
    val contrasena: String
)

/**
 * Resultado de validación de registro
 */
sealed class ResultadoValidacion {
    object Exitoso : ResultadoValidacion()
    data class Error(val mensaje: String) : ResultadoValidacion()
}

/**
 * Valida los datos de registro
 */
fun RegistroUsuario.validar(): ResultadoValidacion {
    return when {
        nombre.isBlank() -> ResultadoValidacion.Error("El nombre es requerido")
        nombre.length < 3 -> ResultadoValidacion.Error("El nombre debe tener al menos 3 caracteres")
        email.isBlank() -> ResultadoValidacion.Error("El email es requerido")
        !email.contains("@") -> ResultadoValidacion.Error("Email inválido")
        edad < 18 -> ResultadoValidacion.Error("Debes ser mayor de 18 años")
        contrasena.length < 6 -> ResultadoValidacion.Error("La contraseña debe tener al menos 6 caracteres")
        else -> ResultadoValidacion.Exitoso
    }
}

/**
 * Extension: Convierte RegistroUsuario a Usuario
 */
fun RegistroUsuario.toUsuario(codigoReferidoValido: Boolean = false): Usuario {
    return Usuario(
        id = java.util.UUID.randomUUID().toString(),
        nombre = nombre,
        email = email,
        edad = edad,
        contrasena = contrasena, // En producción: hashear
        esDuoc = email.lowercase().contains("duoc"),
        puntos = if (codigoReferidoValido) 100 else 0, // Bonus por código de referido
        referidoPor = if (codigoReferidoValido) codigoReferido else null
    )
}