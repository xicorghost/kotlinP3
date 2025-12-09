package com.example.levelupgamerx.domain.model

/**
 * Representa un ítem individual dentro del carrito
 * Incluye lógica de subtotal calculado automáticamente
 */
data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int = 1
) {
    val subtotal: Double
        get() = producto.precio * cantidad
}