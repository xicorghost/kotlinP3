package com.example.levelupgamerx.domain.model

/**
 * Datos ingresados por el usuario en el formulario de registro
 */
data class FormularioRegistro(
    val nombreCompleto: String = "",
    val email: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val aceptaTerminos: Boolean = false
)

/**
 * Errores de validación del formulario
 */
data class ErroresFormulario(
    val nombreCompletoError: String? = null,
    val emailError: String? = null,
    val telefonoError: String? = null,
    val direccionError: String? = null,
    val passwordError: String? = null,
    val confirmarPasswordError: String? = null,
    val terminosError: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(
            nombreCompletoError,
            emailError,
            telefonoError,
            direccionError,
            passwordError,
            confirmarPasswordError,
            terminosError
        ).any { it != null }
}