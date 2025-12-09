package com.example.levelupgamerx.data.local

import android.content.Context
import com.example.levelupgamerx.data.local.entity.toEntity
import com.example.levelupgamerx.domain.model.ProductosWeb
import com.example.levelupgamerx.domain.model.ResenasIniciales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Inicializador de datos corregido
 */
object ProductoInicializador {

    fun inicializarProductos(context: Context) {
        val database = AppDatabase.getDatabase(context)
        val productoDao = database.productoDao()
        val resenaDao = database.resenaDao()

        CoroutineScope(Dispatchers.IO).launch {
            // Verificar si ya hay productos
            val productosExistentes = productoDao.obtenerPorId(1)

            if (productosExistentes == null) {
                // Insertar productos
                val productos = ProductosWeb.obtenerProductosIniciales()
                productoDao.insertarLista(productos.map { it.toEntity() })

                // Insertar reseñas
                val resenas = ResenasIniciales.obtenerResenasIniciales()
                resenaDao.insertarTodas(resenas.map { resena ->
                    com.example.levelupgamerx.data.local.entity.ResenaEntity(
                        id = resena.id,
                        codigoProducto = resena.codigoProducto,
                        usuarioId = resena.usuarioId,
                        nombreUsuario = resena.nombreUsuario,
                        calificacion = resena.calificacion,
                        comentario = resena.comentario,
                        fecha = resena.fecha
                    )
                })

                // Actualizar calificaciones de productos
                productos.forEach { producto ->
                    val promedio = resenaDao.calcularPromedioCalificacion(producto.codigo) ?: 0f
                    val numeroResenas = resenaDao.contarResenasPorProducto(producto.codigo)

                    productoDao.actualizarCalificacion(producto.codigo, promedio, numeroResenas)
                }
            }
        }
    }
}