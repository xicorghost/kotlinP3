package com.example.levelupgamerx.data.local.dao

import androidx.room.*
import com.example.levelupgamerx.data.local.entity.ResenaEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para gestión de reseñas
 */
@Dao
interface ResenaDao {

    /** Obtiene todas las reseñas de un producto */
    @Query("SELECT * FROM resenas WHERE codigoProducto = :codigoProducto ORDER BY fecha DESC")
    fun obtenerPorProducto(codigoProducto: String): Flow<List<ResenaEntity>>

    /** Obtiene todas las reseñas de un usuario */
    @Query("SELECT * FROM resenas WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    fun obtenerPorUsuario(usuarioId: String): Flow<List<ResenaEntity>>

    /** Verifica si un usuario ya reseñó un producto */
    @Query("SELECT EXISTS(SELECT 1 FROM resenas WHERE usuarioId = :usuarioId AND codigoProducto = :codigoProducto)")
    suspend fun usuarioYaReseno(usuarioId: String, codigoProducto: String): Boolean

    /** Obtiene una reseña específica */
    @Query("SELECT * FROM resenas WHERE id = :id")
    suspend fun obtenerPorId(id: String): ResenaEntity?

    /** Inserta una nueva reseña */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(resena: ResenaEntity): Long

    /** Inserta múltiples reseñas */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(resenas: List<ResenaEntity>)

    /** Actualiza una reseña existente */
    @Update
    suspend fun actualizar(resena: ResenaEntity)

    /** Elimina una reseña */
    @Delete
    suspend fun eliminar(resena: ResenaEntity)

    /** Calcula el promedio de calificaciones de un producto */
    @Query("SELECT AVG(calificacion) FROM resenas WHERE codigoProducto = :codigoProducto")
    suspend fun calcularPromedioCalificacion(codigoProducto: String): Float?

    /** Cuenta el número de reseñas de un producto */
    @Query("SELECT COUNT(*) FROM resenas WHERE codigoProducto = :codigoProducto")
    suspend fun contarResenasPorProducto(codigoProducto: String): Int

    /** Elimina todas las reseñas de un producto */
    @Query("DELETE FROM resenas WHERE codigoProducto = :codigoProducto")
    suspend fun eliminarPorProducto(codigoProducto: String)

    /** Obtiene las últimas N reseñas */
    @Query("SELECT * FROM resenas ORDER BY fecha DESC LIMIT :limite")
    fun obtenerUltimas(limite: Int = 10): Flow<List<ResenaEntity>>
}