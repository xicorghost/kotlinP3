package com.example.levelupgamerx.domain.repository

import com.example.levelupgamerx.domain.model.Producto
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de productos gamer.
 * Define las operaciones que la capa de datos debe implementar.
 */
interface RepositorioProductos {

    /** Obtiene todos los productos disponibles (Flow reactivo). */
    fun obtenerProductos(): Flow<List<Producto>>

    /** Busca un producto por su ID. */
    suspend fun obtenerProductoPorId(id: Int): Producto?

    /** Inserta varios productos iniciales o importados. */
    suspend fun insertarProductos(productos: List<Producto>)

    /** Inserta un solo producto y retorna su ID generado. */
    suspend fun insertarProducto(producto: Producto): Long

    /** Actualiza un producto existente. */
    suspend fun actualizarProducto(producto: Producto)

    /** Elimina un producto específico. */
    suspend fun eliminarProducto(producto: Producto)

    /** Elimina todos los productos de la base de datos. */
    suspend fun eliminarTodosLosProductos()

    suspend fun buscarPorNombreExacto(nombre: String): Producto?
}