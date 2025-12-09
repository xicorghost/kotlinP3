package com.example.levelupgamerx.ui.state


import com.example.levelupgamerx.domain.model.ErroresFormulario
import com.example.levelupgamerx.domain.model.FormularioRegistro

/**
 * Estado de la UI para el formulario de registro.
 */
data class RegistroUiState(
    val formulario: FormularioRegistro = FormularioRegistro(),
    val errores: ErroresFormulario = ErroresFormulario()
)