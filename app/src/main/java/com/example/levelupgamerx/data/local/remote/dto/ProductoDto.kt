package com.example.levelupgamerx.data.local.remote.dto

import com.example.levelupgamerx.domain.model.CategoriaProducto
import com.example.levelupgamerx.domain.model.Producto
import com.google.gson.annotations.SerializedName

/**
 * DTO para la API de productos gamer en Railway
 *
 * Estructura JSON de ejemplo:
 * {
 *   "id": 1,
 *   "codigo": "TKL-001",
 *   "nombre": "Teclado Gamer HyperX",
 *   "descripcion": "RGB Mecánico",
 *   "precio": 49990,
 *   "imagenUrl": "url",
 *   "categoria": "ACCESORIOS",
 *   "stock": 15
 * }
 */
data class ProductoDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("codigo")
    val codigo: String? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("imagenUrl")
    val imagenUrl: String?,

    @SerializedName("categoria")
    val categoria: String?,

    @SerializedName("stock")
    val stock: Int
)

/**
 * Convierte ProductoDto de la API a Producto (modelo de dominio)
 */
fun ProductoDto.aProducto(): Producto {
    return Producto(
        id = 0, // Room generará el ID
        codigo = codigo ?: "API-${id}", // Si no hay código, generar uno
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        //imagenUrl = imagenUrl?,
        imagenUrl = imagenUrl ?: "url_imagen_por_defecto",
        //categoria = CategoriaProducto.fromString(categoria),
        categoria = CategoriaProducto.fromString(categoria ?: "OTRO"),
        stock = stock,
        calificacionPromedio = 0f,
        numeroResenas = 0
    )
}

/**
 * Convierte Producto (modelo de dominio) a ProductoDto para enviar a la API
 */
fun Producto.aDto(): ProductoDto {
    return ProductoDto(
        id = id,
        codigo = codigo,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        //imagenUrl = imagenUrl,
        imagenUrl = imagenUrl ?: "url_imagen_por_defecto",
        categoria = categoria.name, // Convertir enum a String
        stock = stock
    )
}

/**
 * Convierte lista de ProductoDto a lista de Producto
 */
fun List<ProductoDto>.aProductos(): List<Producto> {
    return this.map { it.aProducto() }
}

/**
 * Convierte lista de Producto a lista de ProductoDto
 */
fun List<Producto>.aDtos(): List<ProductoDto> {
    return this.map { it.aDto() }
}