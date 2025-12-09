package com.example.levelupgamerx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.levelupgamerx.domain.model.Resena

/**
 * Entidad Room para Reseñas de productos
 */
@Entity(tableName = "resenas")
data class ResenaEntity(
    @PrimaryKey
    val id: String,
    val codigoProducto: String,
    val usuarioId: String,
    val nombreUsuario: String,
    val calificacion: Int,
    val comentario: String,
    val fecha: Long
)

/**
 * Convierte ResenaEntity a modelo de dominio Resena
 */
fun ResenaEntity.toResena(): Resena = Resena(
    id = id,
    codigoProducto = codigoProducto,
    usuarioId = usuarioId,
    nombreUsuario = nombreUsuario,
    calificacion = calificacion,
    comentario = comentario,
    fecha = fecha
)

/**
 * Convierte modelo de dominio Resena a ResenaEntity
 */
fun Resena.toEntity(): ResenaEntity = ResenaEntity(
    id = id,
    codigoProducto = codigoProducto,
    usuarioId = usuarioId,
    nombreUsuario = nombreUsuario,
    calificacion = calificacion,
    comentario = comentario,
    fecha = fecha
)