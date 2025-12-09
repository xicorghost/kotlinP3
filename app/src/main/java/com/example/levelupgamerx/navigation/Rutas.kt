package com.example.levelupgamerx.navigation

/**
 * Rutas de navegación actualizadas
 */
object Rutas {
    // Rutas principales
    const val SPLASH = "splash"
    const val PORTADA = "portada"

    const val PRODUCTOS_API = "productos_api"
    const val HOME = "home"
    const val CARRITO = "carrito"

    // Usuario
    const val LOGIN_USUARIO = "login_usuario"
    const val REGISTRO = "registro"
    const val PERFIL = "perfil"

    // Admin
    const val LOGIN_ADMIN = "login_admin"
    const val PANEL_ADMIN = "panel_admin"

    // Rutas con argumentos
    const val DETALLE_BASE = "detalle/{productoId}"
    const val FORMULARIO_PRODUCTO_BASE = "formulario_producto?productoId={productoId}"

    // Funciones helper
    fun detalleConId(id: Int) = "detalle/$id"
    fun formularioEditar(id: Int) = "formulario_producto?productoId=$id"
    fun formularioNuevo() = "formulario_producto"
}