package com.example.levelupgamerx.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * PreferenciasManager actualizado
 * Gestiona sesiones de usuarios y administradores
 */
class PreferenciasManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        NOMBRE_ARCHIVO,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val NOMBRE_ARCHIVO = "levelupgamer_prefs"

        // Claves para Admin
        private const val KEY_ADMIN_LOGUEADO = "admin_logueado"
        private const val KEY_USERNAME_ADMIN = "username_admin"
        const val ADMIN_USERNAME = "admin"
        const val ADMIN_PASSWORD = "admin123"

        // Claves para Usuario
        private const val KEY_USUARIO_LOGUEADO = "usuario_logueado"
        private const val KEY_USUARIO_ID = "usuario_id"
        private const val KEY_USUARIO_NOMBRE = "usuario_nombre"
        private const val KEY_USUARIO_EMAIL = "usuario_email"
        private const val KEY_USUARIO_PUNTOS = "usuario_puntos"
        private const val KEY_USUARIO_NIVEL = "usuario_nivel"
        private const val KEY_USUARIO_ES_DUOC = "usuario_es_duoc"
        private const val KEY_USUARIO_CODIGO_REFERIDO = "usuario_codigo_referido"

        // Clave para la Foto de Perfil
        private const val KEY_FOTO_PERFIL = "foto_perfil" // <-- ¡AGREGAR ESTO!
    }

    // ==================== FUNCIONES DE ADMIN ====================

    fun guardarSesionAdmin(username: String) {
        prefs.edit().apply {
            putBoolean(KEY_ADMIN_LOGUEADO, true)
            putString(KEY_USERNAME_ADMIN, username)
            apply()
        }
    }

    fun estaAdminLogueado(): Boolean {
        return prefs.getBoolean(KEY_ADMIN_LOGUEADO, false)
    }

    fun obtenerUsernameAdmin(): String? {
        return prefs.getString(KEY_USERNAME_ADMIN, null)
    }

    fun cerrarSesionAdmin() {
        prefs.edit().apply {
            remove(KEY_ADMIN_LOGUEADO)
            remove(KEY_USERNAME_ADMIN)
            apply()
        }
    }

    fun validarCredencialesAdmin(username: String, password: String): Boolean {
        return username == ADMIN_USERNAME && password == ADMIN_PASSWORD
    }

    // ==================== FUNCIONES DE USUARIO ====================

    /**
     * Guarda la sesión de un usuario normal
     */
    fun guardarSesionUsuario(
        id: String,
        nombre: String,
        email: String,
        puntos: Int,
        nivel: Int,
        esDuoc: Boolean,
        codigoReferido: String
    ) {
        prefs.edit().apply {
            putBoolean(KEY_USUARIO_LOGUEADO, true)
            putString(KEY_USUARIO_ID, id)
            putString(KEY_USUARIO_NOMBRE, nombre)
            putString(KEY_USUARIO_EMAIL, email)
            putInt(KEY_USUARIO_PUNTOS, puntos)
            putInt(KEY_USUARIO_NIVEL, nivel)
            putBoolean(KEY_USUARIO_ES_DUOC, esDuoc)
            putString(KEY_USUARIO_CODIGO_REFERIDO, codigoReferido)
            apply()
        }
    }

    /**
     * Verifica si hay un usuario logueado
     */
    fun estaUsuarioLogueado(): Boolean {
        return prefs.getBoolean(KEY_USUARIO_LOGUEADO, false)
    }

    /**
     * Obtiene el ID del usuario logueado
     */
    fun obtenerUsuarioId(): String? {
        return prefs.getString(KEY_USUARIO_ID, null)
    }

    /**
     * Obtiene el nombre del usuario logueado
     */
    fun obtenerUsuarioNombre(): String? {
        return prefs.getString(KEY_USUARIO_NOMBRE, null)
    }

    /**
     * Obtiene el email del usuario logueado
     */
    fun obtenerUsuarioEmail(): String? {
        return prefs.getString(KEY_USUARIO_EMAIL, null)
    }

    /**
     * Obtiene los puntos del usuario
     */
    fun obtenerUsuarioPuntos(): Int {
        return prefs.getInt(KEY_USUARIO_PUNTOS, 0)
    }

    /**
     * Actualiza los puntos del usuario en sesión
     */
    fun actualizarPuntos(puntos: Int, nivel: Int) {
        prefs.edit().apply {
            putInt(KEY_USUARIO_PUNTOS, puntos)
            putInt(KEY_USUARIO_NIVEL, nivel)
            apply()
        }
    }

    /**
     * Obtiene el nivel del usuario
     */
    fun obtenerUsuarioNivel(): Int {
        return prefs.getInt(KEY_USUARIO_NIVEL, 1)
    }

    /**
     * Verifica si el usuario tiene descuento DUOC
     */
    fun esUsuarioDuoc(): Boolean {
        return prefs.getBoolean(KEY_USUARIO_ES_DUOC, false)
    }

    /**
     * Obtiene el código de referido del usuario
     */
    fun obtenerCodigoReferido(): String? {
        return prefs.getString(KEY_USUARIO_CODIGO_REFERIDO, null)
    }

    /**
     * Cierra la sesión del usuario
     */
    fun cerrarSesionUsuario() {
        prefs.edit().apply {
            remove(KEY_USUARIO_LOGUEADO)
            remove(KEY_USUARIO_ID)
            remove(KEY_USUARIO_NOMBRE)
            remove(KEY_USUARIO_EMAIL)
            remove(KEY_USUARIO_PUNTOS)
            remove(KEY_USUARIO_NIVEL)
            remove(KEY_USUARIO_ES_DUOC)
            remove(KEY_USUARIO_CODIGO_REFERIDO)
            apply()
        }
    }

    /**
     * Cierra cualquier sesión activa (usuario o admin)
     */
    fun cerrarTodasLasSesiones() {
        cerrarSesionUsuario()
        cerrarSesionAdmin()
    }

    /**
     * Datos del usuario en sesión
     */
    data class DatosUsuarioSesion(
        val id: String,
        val nombre: String,
        val email: String,
        val puntos: Int,
        val nivel: Int,
        val esDuoc: Boolean,
        val codigoReferido: String
    )

    /**
     * Obtiene todos los datos del usuario en sesión
     */
    fun obtenerDatosUsuarioSesion(): DatosUsuarioSesion? {
        if (!estaUsuarioLogueado()) return null

        val id = obtenerUsuarioId() ?: return null
        val nombre = obtenerUsuarioNombre() ?: return null
        val email = obtenerUsuarioEmail() ?: return null
        val codigo = obtenerCodigoReferido() ?: return null

        return DatosUsuarioSesion(
            id = id,
            nombre = nombre,
            email = email,
            puntos = obtenerUsuarioPuntos(),
            nivel = obtenerUsuarioNivel(),
            esDuoc = esUsuarioDuoc(),
            codigoReferido = codigo
        )
    }

    // ==================== FUNCIONES DE FOTO DE PERFIL ====================

    /**
     * Guarda la ruta local de la foto de perfil
     */
    fun guardarFotoPerfil(ruta: String) {
        prefs.edit().apply {
            putString(KEY_FOTO_PERFIL, ruta)
            apply()
        }
    }

    /**
     * Obtiene la ruta local de la foto de perfil
     */
    fun obtenerFotoPerfil(): String? {
        return prefs.getString(KEY_FOTO_PERFIL, null)
    }

    /**
     * Elimina la ruta de la foto de perfil de las preferencias
     */
    fun eliminarFotoPerfil() {
        prefs.edit().apply {
            remove(KEY_FOTO_PERFIL)
            apply()
        }
    }
}