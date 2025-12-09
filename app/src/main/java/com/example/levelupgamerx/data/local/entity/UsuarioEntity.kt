package com.example.levelupgamerx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.levelupgamerx.domain.model.Rol
import com.example.levelupgamerx.domain.model.Usuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Entidad Room para Usuarios
 */
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val email: String,
    val edad: Int,
    val contrasena: String,
    val puntos: Int = 0,
    val nivel: Int = 1,
    val codigoReferido: String,
    val referidoPor: String? = null,
    val cantidadReferidos: Int = 0,
    val esDuoc: Boolean = false,
    val productosComprados: String = "[]", // JSON Array como String
    val rol: String = Rol.USUARIO.name,
    val fechaRegistro: Long = System.currentTimeMillis()
)

/**
 * Convierte UsuarioEntity a modelo de dominio Usuario
 */
fun UsuarioEntity.toUsuario(): Usuario {
    val gson = Gson()
    val type = object : TypeToken<List<String>>() {}.type
    val productos: List<String> = gson.fromJson(productosComprados, type) ?: emptyList()


    return Usuario(
        id = id,
        nombre = nombre,
        email = email,
        edad = edad,
        contrasena = contrasena,
        puntos = puntos,
        nivel = nivel,
        codigoReferido = codigoReferido,
        referidoPor = referidoPor,
        cantidadReferidos = cantidadReferidos,
        esDuoc = esDuoc,
        productosComprados = productos,
        rol = Rol.valueOf(rol),
        fechaRegistro = fechaRegistro
    )
}

/**
 * Convierte modelo de dominio Usuario a UsuarioEntity
 */
fun Usuario.toEntity(): UsuarioEntity {
    val gson = Gson()
    val productosJson = gson.toJson(productosComprados)

    return UsuarioEntity(
        id = id,
        nombre = nombre,
        email = email,
        edad = edad,
        contrasena = contrasena,
        puntos = puntos,
        nivel = nivel,
        codigoReferido = codigoReferido,
        referidoPor = referidoPor,
        cantidadReferidos = cantidadReferidos,
        esDuoc = esDuoc,
        productosComprados = productosJson,
        rol = rol.name,
        fechaRegistro = fechaRegistro
    )
}

/**
 * Type Converters para Room (conversión de tipos complejos)
 */
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return gson.toJson(list)
    }
}