package com.example.levelupgamerx.domain.model

import com.example.levelupgamerx.data.local.entity.ProductoEntity

fun Producto.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = this.id,
        codigo = this.codigo,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagenUrl = this.imagenUrl,   // ← CAMBIADO
        categoria = this.categoria.name,
        stock = this.stock,
        calificacionPromedio = this.calificacionPromedio,
        numeroResenas = this.numeroResenas
    )
}

fun ProductoEntity.toDomain(): Producto {
    return Producto(
        id = this.id,
        codigo = this.codigo,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagenUrl = this.imagenUrl,   // ← CAMBIADO
        categoria = CategoriaProducto.fromString(this.categoria),
        stock = this.stock,
        calificacionPromedio = this.calificacionPromedio,
        numeroResenas = this.numeroResenas
    )
}
