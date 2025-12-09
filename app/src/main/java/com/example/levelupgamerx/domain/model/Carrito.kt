package com.example.levelupgamerx.domain.model

/**
 * Carrito de compras completo del usuario
 * Maneja totales y cantidad global de productos
 */
data class Carrito(
    val items: List<ItemCarrito> = emptyList()
) {
    val cantidadTotal: Int
        get() = items.sumOf { it.cantidad }

    val precioTotal: Double
        get() = items.sumOf { it.subtotal }

    val estaVacio: Boolean
        get() = items.isEmpty()
}