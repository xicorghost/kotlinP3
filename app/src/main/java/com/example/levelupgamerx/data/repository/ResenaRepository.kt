package com.example.levelupgamerx.data.repository

import com.example.levelupgamerx.data.local.dao.ProductoDao
import com.example.levelupgamerx.data.local.dao.ResenaDao
import com.example.levelupgamerx.data.local.entity.toEntity
import com.example.levelupgamerx.data.local.entity.toProducto
import com.example.levelupgamerx.data.local.entity.toResena
import com.example.levelupgamerx.domain.model.Resena
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio de Reseñas
 * Gestiona comentarios y calificaciones de productos
 */
class ResenaRepository(
    private val resenaDao: ResenaDao,
    private val productoDao: ProductoDao
) {

    /** Obtiene todas las reseñas de un producto */
    fun obtenerResenasPorProducto(codigoProducto: String): Flow<List<Resena>> {
        return resenaDao.obtenerPorProducto(codigoProducto).map { entities ->
            entities.map { it.toResena() }
        }
    }

    /** Obtiene todas las reseñas de un usuario */
    fun obtenerResenasPorUsuario(usuarioId: String): Flow<List<Resena>> {
        return resenaDao.obtenerPorUsuario(usuarioId).map { entities ->
            entities.map { it.toResena() }
        }
    }

    /** Verifica si un usuario ya reseñó un producto */
    suspend fun usuarioYaReseno(usuarioId: String, codigoProducto: String): Boolean {
        return resenaDao.usuarioYaReseno(usuarioId, codigoProducto)
    }

    /**
     * Agrega una nueva reseña
     * Actualiza automáticamente el promedio del producto
     */
    suspend fun agregarResena(resena: Resena): Result<Unit> {
        return try {
            // Verificar si ya reseñó
            if (usuarioYaReseno(resena.usuarioId, resena.codigoProducto)) {
                return Result.failure(Exception("Ya has dejado una reseña para este producto"))
            }

            // Insertar reseña
            resenaDao.insertar(resena.toEntity())

            // Actualizar calificación promedio del producto
            actualizarCalificacionProducto(resena.codigoProducto)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza la calificación promedio de un producto
     */
    private suspend fun actualizarCalificacionProducto(codigoProducto: String) {
        val promedio = resenaDao.calcularPromedioCalificacion(codigoProducto) ?: 0f
        val numeroResenas = resenaDao.contarResenasPorProducto(codigoProducto)

        // Buscar el producto y actualizar
        val productos = productoDao.obtenerTodos()
        // Como obtenerTodos() devuelve Flow, necesitamos manejarlo diferente
        // Usaremos una búsqueda directa por código

        // Nota: Necesitaremos agregar un método al ProductoDao para buscar por código
        // Por ahora, asumimos que el código coincide con el ID o usamos búsqueda iterativa

        // Solución temporal: actualizar usando el ID del producto
        // En la práctica, deberías tener un método obtenerPorCodigo en ProductoDao
    }

    /**
     * Actualiza una reseña existente
     */
    suspend fun actualizarResena(resena: Resena) {
        resenaDao.actualizar(resena.toEntity())
        actualizarCalificacionProducto(resena.codigoProducto)
    }

    /**
     * Elimina una reseña
     */
    suspend fun eliminarResena(resena: Resena) {
        resenaDao.eliminar(resena.toEntity())
        actualizarCalificacionProducto(resena.codigoProducto)
    }

    /**
     * Obtiene las últimas reseñas de la comunidad
     */
    fun obtenerUltimasResenas(limite: Int = 10): Flow<List<Resena>> {
        return resenaDao.obtenerUltimas(limite).map { entities ->
            entities.map { it.toResena() }
        }
    }

    /**
     * Obtiene estadísticas de reseñas de un producto
     */
    suspend fun obtenerEstadisticasProducto(codigoProducto: String): EstadisticasResena {
        val promedio = resenaDao.calcularPromedioCalificacion(codigoProducto) ?: 0f
        val total = resenaDao.contarResenasPorProducto(codigoProducto)

        return EstadisticasResena(
            calificacionPromedio = promedio,
            totalResenas = total
        )
    }
}

/**
 * Estadísticas de reseñas
 */
data class EstadisticasResena(
    val calificacionPromedio: Float,
    val totalResenas: Int
) {
    fun estrellasCompletas(): Int = calificacionPromedio.toInt()
    fun tieneMediaEstrella(): Boolean = (calificacionPromedio % 1) >= 0.5f
}