package com.example.levelupgamerx.domain.model

/**
 * Modelo auxiliar para mostrar estadísticas en el panel admin
 */
data class Estadistica(
    val categoria: String,
    val totalProductos: Int,
    val totalStock: Int,
    val valorInventario: Double
)