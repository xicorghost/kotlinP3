package com.example.levelupgamerx.data.local.dao

import androidx.room.*
import com.example.levelupgamerx.data.local.entity.CarritoEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la tabla "carrito" - CORREGIDO
 */
@Dao
interface CarritoDao {

    /** Obtiene todos los items en el carrito */
    @Query("SELECT * FROM carrito")
    fun obtenerTodo(): Flow<List<CarritoEntity>>

    /** Inserta un producto al carrito */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: CarritoEntity)

    /** Borra todos los items del carrito */
    @Query("DELETE FROM carrito")
    suspend fun vaciar()

    /** Calcula el total del carrito (precio * cantidad) */
    @Query("SELECT SUM(precio * cantidad) FROM carrito")
    fun obtenerTotal(): Flow<Double?>

    /** Busca si un producto ya está en el carrito */
    @Query("SELECT * FROM carrito WHERE productoId = :productoId LIMIT 1")
    suspend fun obtenerPorProductoId(productoId: Int): CarritoEntity?

    /** Actualiza la cantidad de un producto */
    @Query("UPDATE carrito SET cantidad = :cantidad WHERE productoId = :productoId")
    suspend fun actualizarCantidad(productoId: Int, cantidad: Int)

    /** Elimina un producto del carrito */
    @Query("DELETE FROM carrito WHERE productoId = :productoId")
    suspend fun eliminarProducto(productoId: Int)
}