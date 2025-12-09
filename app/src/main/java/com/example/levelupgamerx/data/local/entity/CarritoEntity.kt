package com.example.levelupgamerx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.levelupgamerx.domain.model.CategoriaProducto
import com.example.levelupgamerx.domain.model.Producto

/**
 * Entidad Room para tabla "carrito".
 * Representa productos agregados por el usuario.
 */
@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productoId: Int,
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String,
    val categoria: String, // Guardamos como String para Room
    val stock: Int,
    val cantidad: Int = 1,
    val calificacionPromedio: Float = 0f,
    val numeroResenas: Int = 0
)

/** Convierte CarritoEntity a modelo Producto (para mostrar en la UI) */
fun CarritoEntity.toDomain() = Producto(
    id = productoId,
    codigo = codigo,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    imagenUrl = imagenUrl,
    categoria = CategoriaProducto.fromString(categoria), // ✅ Convertir String a Enum
    stock = stock,
    calificacionPromedio = calificacionPromedio,
    numeroResenas = numeroResenas
)