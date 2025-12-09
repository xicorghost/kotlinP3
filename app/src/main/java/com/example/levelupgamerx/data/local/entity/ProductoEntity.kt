package com.example.levelupgamerx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.levelupgamerx.domain.model.CategoriaProducto
import com.example.levelupgamerx.domain.model.Producto

/**
 * Entidad Room actualizada para productos con sistema de reseñas
 */
@Entity(tableName = "productos_gamer")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String,
    val categoria: String, // Guardamos como String para Room
    val stock: Int,
    val calificacionPromedio: Float = 0f,
    val numeroResenas: Int = 0
)

/**
 * Convierte ProductoEntity a modelo de dominio Producto
 */
fun ProductoEntity.toProducto(): Producto = Producto(
    id = id,
    codigo = codigo,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    imagenUrl = imagenUrl,
    categoria = CategoriaProducto.fromString(categoria),
    stock = stock,
    calificacionPromedio = calificacionPromedio,
    numeroResenas = numeroResenas
)

/**
 * Convierte modelo de dominio Producto a ProductoEntity
 */
fun Producto.toEntity(): ProductoEntity = ProductoEntity(
    id = id,
    codigo = codigo,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    imagenUrl = imagenUrl,
    categoria = categoria.name,
    stock = stock,
    calificacionPromedio = calificacionPromedio,
    numeroResenas = numeroResenas
)