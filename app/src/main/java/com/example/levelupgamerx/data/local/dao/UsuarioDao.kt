package com.example.levelupgamerx.data.local.dao

import androidx.room.*
import com.example.levelupgamerx.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para gestión de usuarios
 */
@Dao
interface UsuarioDao {

    /** Obtiene todos los usuarios */
    @Query("SELECT * FROM usuarios ORDER BY fechaRegistro DESC")
    fun obtenerTodos(): Flow<List<UsuarioEntity>>

    /** Busca un usuario por ID */
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtenerPorId(id: String): UsuarioEntity?

    /** Busca un usuario por email */
    @Query("SELECT * FROM usuarios WHERE email = :email")
    suspend fun obtenerPorEmail(email: String): UsuarioEntity?

    /** Busca un usuario por código de referido */
    @Query("SELECT * FROM usuarios WHERE codigoReferido = :codigo")
    suspend fun obtenerPorCodigoReferido(codigo: String): UsuarioEntity?

    /** Valida credenciales de login */
    @Query("SELECT * FROM usuarios WHERE email = :email AND contrasena = :contrasena")
    suspend fun validarLogin(email: String, contrasena: String): UsuarioEntity?

    /** Inserta un nuevo usuario */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity): Long

    /** Actualiza un usuario existente */
    @Update
    suspend fun actualizar(usuario: UsuarioEntity)

    /** Elimina un usuario */
    @Delete
    suspend fun eliminar(usuario: UsuarioEntity)

    /** Actualiza puntos y nivel de un usuario */
    @Query("UPDATE usuarios SET puntos = :puntos, nivel = :nivel WHERE id = :usuarioId")
    suspend fun actualizarPuntosYNivel(usuarioId: String, puntos: Int, nivel: Int)

    /** Agrega un producto comprado a la lista del usuario */
    @Query("UPDATE usuarios SET productosComprados = :productosJson WHERE id = :usuarioId")
    suspend fun actualizarProductosComprados(usuarioId: String, productosJson: String)

    /** Incrementa la cantidad de referidos */
    @Query("UPDATE usuarios SET cantidadReferidos = cantidadReferidos + 1 WHERE codigoReferido = :codigoReferido")
    suspend fun incrementarReferidos(codigoReferido: String)

    /** Cuenta total de usuarios */
    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contarUsuarios(): Int

    /** Usuarios con descuento DUOC */
    @Query("SELECT COUNT(*) FROM usuarios WHERE esDuoc = 1")
    suspend fun contarUsuariosDuoc(): Int
}