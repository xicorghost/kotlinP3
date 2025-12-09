package com.example.levelupgamerx.data.repository

import com.example.levelupgamerx.data.local.dao.CarritoDao
import com.example.levelupgamerx.data.local.entity.CarritoEntity
import com.example.levelupgamerx.data.local.entity.toDomain
import com.example.levelupgamerx.domain.model.ItemCarrito
import com.example.levelupgamerx.domain.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio del carrito de compras - CORREGIDO
 */
class CarritoRepository(private val carritoDao: CarritoDao) {

    /** Obtiene todos los items del carrito en tiempo real */
    fun obtenerCarrito(): Flow<List<ItemCarrito>> {
        return carritoDao.obtenerTodo().map { entities ->
            entities.map { entity ->
                ItemCarrito(
                    producto = entity.toDomain(),
                    cantidad = entity.cantidad
                )
            }
        }
    }

    /**
     * Agrega un producto al carrito.
     * Si ya existe → incrementa la cantidad.
     */
    suspend fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        val existente = carritoDao.obtenerPorProductoId(producto.id)
        if (existente != null) {
            val nuevaCantidad = existente.cantidad + cantidad
            carritoDao.actualizarCantidad(producto.id, nuevaCantidad)
        } else {
            val entity = CarritoEntity(
                productoId = producto.id,
                codigo = producto.codigo,
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                precio = producto.precio,
                imagenUrl = producto.imagenUrl,
                categoria = producto.categoria.name, // ✅ Convertir enum a String
                stock = producto.stock,
                cantidad = cantidad,
                calificacionPromedio = producto.calificacionPromedio,
                numeroResenas = producto.numeroResenas
            )
            carritoDao.insertar(entity)
        }
    }

    /**
     * Modifica la cantidad de un producto existente.
     * Si la cantidad es 0 o negativa → lo elimina.
     */
    suspend fun modificarCantidad(productoId: Int, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarProducto(productoId)
        } else {
            carritoDao.actualizarCantidad(productoId, nuevaCantidad)
        }
    }

    /** Elimina un producto del carrito */
    suspend fun eliminarProducto(productoId: Int) {
        carritoDao.eliminarProducto(productoId)
    }

    /** Vacía todo el carrito */
    suspend fun vaciarCarrito() {
        carritoDao.vaciar()
    }

    /** Calcula el total del carrito */
    fun obtenerTotal(): Flow<Double> {
        return carritoDao.obtenerTotal().map { it ?: 0.0 }
    }
}