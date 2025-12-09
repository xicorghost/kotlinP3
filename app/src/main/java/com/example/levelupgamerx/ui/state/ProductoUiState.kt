package com.example.levelupgamerx.ui.state

import com.example.levelupgamerx.domain.model.Producto

/**
 * Estado de la UI para la pantalla de productos gamer.
 * Controla el estado de carga, datos y errores.
 */
data class ProductoUiState(
    val estaCargando: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val error: String? = null
) {
    val hayProductos: Boolean
        get() = productos.isNotEmpty()
}