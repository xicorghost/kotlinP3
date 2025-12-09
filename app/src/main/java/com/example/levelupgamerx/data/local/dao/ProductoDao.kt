package com.example.levelupgamerx.data.local.dao

import androidx.room.*
import com.example.levelupgamerx.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO actualizado de productos con búsqueda por código
 */
@Dao
interface ProductoDao {

    /** Lista todos los productos ordenados por nombre */
    @Query("SELECT * FROM productos_gamer ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<ProductoEntity>>

    /** Busca un producto por su ID */
    @Query("SELECT * FROM productos_gamer WHERE id = :id")
    suspend fun obtenerPorId(id: Int): ProductoEntity?

    /** Busca un producto por su código único */
    @Query("SELECT * FROM productos_gamer WHERE codigo = :codigo")
    suspend fun obtenerPorCodigo(codigo: String): ProductoEntity?

    /** Filtra productos por categoría */
    @Query("SELECT * FROM productos_gamer WHERE categoria = :categoria ORDER BY nombre ASC")
    fun obtenerPorCategoria(categoria: String): Flow<List<ProductoEntity>>

    /** Busca productos por nombre (búsqueda) */
    @Query("SELECT * FROM productos_gamer WHERE nombre LIKE '%' || :query || '%' ORDER BY nombre ASC")
    fun buscarPorNombre(query: String): Flow<List<ProductoEntity>>

    /** Productos con stock disponible */
    @Query("SELECT * FROM productos_gamer WHERE stock > 0 ORDER BY nombre ASC")
    fun obtenerConStock(): Flow<List<ProductoEntity>>

    /** Productos ordenados por calificación */
    @Query("SELECT * FROM productos_gamer ORDER BY calificacionPromedio DESC, numeroResenas DESC")
    fun obtenerMejorCalificados(): Flow<List<ProductoEntity>>

    /** Inserta una lista completa (reemplaza si ya existen) */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarLista(productos: List<ProductoEntity>)

    /** Inserta un solo producto y devuelve su ID */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: ProductoEntity): Long

    /** Actualiza datos de un producto existente */
    @Update
    suspend fun actualizar(producto: ProductoEntity)

    /** Actualiza stock de un producto */
    @Query("UPDATE productos_gamer SET stock = :nuevoStock WHERE id = :productoId")
    suspend fun actualizarStock(productoId: Int, nuevoStock: Int)

    /** Actualiza calificación de un producto */
    @Query("UPDATE productos_gamer SET calificacionPromedio = :promedio, numeroResenas = :numeroResenas WHERE codigo = :codigo")
    suspend fun actualizarCalificacion(codigo: String, promedio: Float, numeroResenas: Int)

    /** Elimina un producto específico */
    @Delete
    suspend fun eliminar(producto: ProductoEntity)

    /** Borra todos los productos */
    @Query("DELETE FROM productos_gamer")
    suspend fun eliminarTodos()

    /** Cuenta total de productos */
    @Query("SELECT COUNT(*) FROM productos_gamer")
    suspend fun contarProductos(): Int

    /** Valor total del inventario */
    @Query("SELECT SUM(precio * stock) FROM productos_gamer")
    suspend fun calcularValorInventario(): Double?

    /*nueva funcion dao*/
    @Query("SELECT * FROM productos_gamer WHERE LOWER(nombre) = LOWER(:nombre) LIMIT 1")
    suspend fun buscarPorNombreExacto(nombre: String): ProductoEntity?
}