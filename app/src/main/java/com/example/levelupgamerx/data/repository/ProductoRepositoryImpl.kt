package com.example.levelupgamerx.data.repository

import com.example.levelupgamerx.data.local.dao.ProductoDao
import com.example.levelupgamerx.data.local.entity.toEntity
import com.example.levelupgamerx.data.local.entity.toProducto
import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.domain.repository.RepositorioProductos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación del repositorio de productos gamer.
 * Traduce entre entidades de Room y modelos de dominio.
 */
class ProductoRepositoryImpl(
    private val productoDao: ProductoDao
) : RepositorioProductos {

    override fun obtenerProductos(): Flow<List<Producto>> {
        return productoDao.obtenerTodos().map { entities ->
            entities.map { it.toProducto() }
        }
    }

    override suspend fun obtenerProductoPorId(id: Int): Producto? {
        return productoDao.obtenerPorId(id)?.toProducto()
    }

    override suspend fun insertarProductos(productos: List<Producto>) {
        val entities = productos.map { it.toEntity() }
        productoDao.insertarLista(entities)
    }

    override suspend fun insertarProducto(producto: Producto): Long {
        return productoDao.insertar(producto.toEntity())
    }

    override suspend fun actualizarProducto(producto: Producto) {
        productoDao.actualizar(producto.toEntity())
    }

    override suspend fun eliminarProducto(producto: Producto) {
        productoDao.eliminar(producto.toEntity())
    }

    override suspend fun eliminarTodosLosProductos() {
        productoDao.eliminarTodos()
    }

    override suspend fun buscarPorNombreExacto(nombre: String): Producto? {
        return productoDao.buscarPorNombreExacto(nombre)?.toProducto()
    }

}