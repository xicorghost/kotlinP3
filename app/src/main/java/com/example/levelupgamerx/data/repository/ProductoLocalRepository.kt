package com.example.levelupgamerx.data.repository

import com.example.levelupgamerx.data.local.dao.ProductoDao
import com.example.levelupgamerx.data.local.entity.toProducto
import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductoLocalRepository(
    private val productoDao: ProductoDao
) {

    /** Guarda un producto */
    suspend fun insertar(producto: Producto) {
        productoDao.insertar(producto.toEntity())
    }

    /** Devuelve TODOS los productos como Flow<List<Producto>> */
    fun obtenerTodos(): Flow<List<Producto>> {
        return productoDao.obtenerTodos()
            .map { lista -> lista.map { it.toProducto() } }
    }

    /** Elimina un producto */
    suspend fun eliminar(producto: Producto) {
        productoDao.eliminar(producto.toEntity())
    }
}


