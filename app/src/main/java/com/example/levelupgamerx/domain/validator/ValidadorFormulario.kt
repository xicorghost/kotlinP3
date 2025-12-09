package com.example.levelupgamerx.domain.validator

import com.example.levelupgamerx.domain.model.FormularioRegistro
import com.example.levelupgamerx.domain.model.ErroresFormulario

/**
 * Validador de formularios del registro de usuario.
 * Revisa formato de campos y contraseñas.
 */
object ValidadorFormulario {

    fun validarNombreCompleto(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre no puede estar vacío"
            nombre.length < 3 -> "Debe tener al menos 3 caracteres"
            else -> null
        }
    }

    fun validarEmail(email: String): String? {
        return when {
            email.isBlank() -> "El correo es obligatorio"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Correo inválido"
            else -> null
        }
    }

    fun validarTelefono(telefono: String): String? {
        return when {
            telefono.isBlank() -> "El teléfono es obligatorio"
            !telefono.matches(Regex("^[0-9]{8,15}$")) ->
                "Teléfono inválido (solo números)"
            else -> null
        }
    }

    fun validarDireccion(direccion: String): String? {
        return if (direccion.isBlank()) "La dirección es obligatoria" else null
    }

    fun validarPassword(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es obligatoria"
            password.length < 6 -> "Debe tener al menos 6 caracteres"
            else -> null
        }
    }

    fun validarConfirmacion(password: String, confirmacion: String): String? {
        return when {
            confirmacion.isBlank() -> "Confirma tu contraseña"
            confirmacion != password -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    fun validarTerminos(acepta: Boolean): String? {
        return if (!acepta) "Debes aceptar los términos y condiciones" else null
    }

    /**
     * Valida todo el formulario completo y retorna errores en bloque.
     */
    fun validarFormulario(form: FormularioRegistro): ErroresFormulario {
        return ErroresFormulario(
            nombreCompletoError = validarNombreCompleto(form.nombreCompleto),
            emailError = validarEmail(form.email),
            telefonoError = validarTelefono(form.telefono),
            direccionError = validarDireccion(form.direccion),
            passwordError = validarPassword(form.password),
            confirmarPasswordError = validarConfirmacion(form.password, form.confirmarPassword),
            terminosError = validarTerminos(form.aceptaTerminos)
        )
    }
}