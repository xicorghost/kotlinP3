package com.example.levelupgamerx.data.repository

import com.example.levelupgamerx.data.local.dao.UsuarioDao
import com.example.levelupgamerx.data.local.entity.toEntity
import com.example.levelupgamerx.data.local.entity.toUsuario
import com.example.levelupgamerx.domain.model.LoginUsuario
import com.example.levelupgamerx.domain.model.RegistroUsuario
import com.example.levelupgamerx.domain.model.Usuario
import com.example.levelupgamerx.domain.model.toUsuario
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio de Usuarios
 * Gestiona registro, login, puntos, referidos
 */
class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {
    private val gson = Gson()

    /** Obtiene todos los usuarios */
    fun obtenerUsuarios(): Flow<List<Usuario>> {
        return usuarioDao.obtenerTodos().map { entities ->
            entities.map { it.toUsuario() }
        }
    }

    /** Busca un usuario por ID */
    suspend fun obtenerPorId(id: String): Usuario? {
        return usuarioDao.obtenerPorId(id)?.toUsuario()
    }

    /** Busca un usuario por email */
    suspend fun obtenerPorEmail(email: String): Usuario? {
        return usuarioDao.obtenerPorEmail(email)?.toUsuario()
    }

    /** Verifica si un código de referido es válido */
    suspend fun validarCodigoReferido(codigo: String): Boolean {
        return usuarioDao.obtenerPorCodigoReferido(codigo) != null
    }

    /**
     * Registra un nuevo usuario
     * Valida código de referido y otorga puntos bonus
     */
    suspend fun registrarUsuario(registro: RegistroUsuario): Result<Usuario> {
        return try {
            // Verificar si el email ya existe
            val usuarioExistente = usuarioDao.obtenerPorEmail(registro.email)
            if (usuarioExistente != null) {
                return Result.failure(Exception("El email ya está registrado"))
            }

            // Validar código de referido si existe
            var codigoValido = false
            if (!registro.codigoReferido.isNullOrBlank()) {
                val referidor = usuarioDao.obtenerPorCodigoReferido(registro.codigoReferido)
                if (referidor != null) {
                    codigoValido = true
                    // Incrementar contador de referidos del referidor
                    usuarioDao.incrementarReferidos(registro.codigoReferido)
                    // Otorgar puntos bonus al referidor
                    val nuevoPuntos = referidor.puntos + 100
                    val nuevoNivel = (nuevoPuntos / 500) + 1
                    usuarioDao.actualizarPuntosYNivel(referidor.id, nuevoPuntos, nuevoNivel)
                }
            }

            // Crear usuario
            val nuevoUsuario = registro.toUsuario(codigoValido)
            usuarioDao.insertar(nuevoUsuario.toEntity())

            Result.success(nuevoUsuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Login de usuario
     * En producción: usar hash de contraseña
     */
    suspend fun loginUsuario(login: LoginUsuario): Usuario? {
        return usuarioDao.validarLogin(login.email, login.contrasena)?.toUsuario()
    }

    /**
     * Agrega puntos a un usuario por compra
     * 1000 pesos = 1 punto
     */
    suspend fun agregarPuntosPorCompra(usuarioId: String, totalCompra: Double) {
        val usuario = obtenerPorId(usuarioId) ?: return
        val puntosGanados = (totalCompra / 1000).toInt()
        val nuevosPuntos = usuario.puntos + puntosGanados
        val nuevoNivel = (nuevosPuntos / 500) + 1

        usuarioDao.actualizarPuntosYNivel(usuarioId, nuevosPuntos, nuevoNivel)
    }

    /**
     * Agrega puntos por dejar reseña
     * Bonus: 50 puntos
     */
    suspend fun agregarPuntosPorResena(usuarioId: String) {
        val usuario = obtenerPorId(usuarioId) ?: return
        val nuevosPuntos = usuario.puntos + 50
        val nuevoNivel = (nuevosPuntos / 500) + 1

        usuarioDao.actualizarPuntosYNivel(usuarioId, nuevosPuntos, nuevoNivel)
    }

    /**
     * Registra un producto como comprado por el usuario
     */
    suspend fun registrarCompra(usuarioId: String, codigoProducto: String) {
        val usuario = obtenerPorId(usuarioId) ?: return
        val productosActualizados = usuario.productosComprados.toMutableList().apply {
            if (!contains(codigoProducto)) {
                add(codigoProducto)
            }
        }
        val productosJson = gson.toJson(productosActualizados)
        usuarioDao.actualizarProductosComprados(usuarioId, productosJson)
    }

    /**
     * Actualiza los datos de un usuario
     */
    suspend fun actualizarUsuario(usuario: Usuario) {
        usuarioDao.actualizar(usuario.toEntity())
    }

    /**
     * Estadísticas de usuarios
     */
    suspend fun obtenerEstadisticas(): EstadisticasUsuarios {
        val total = usuarioDao.contarUsuarios()
        val duoc = usuarioDao.contarUsuariosDuoc()
        return EstadisticasUsuarios(
            totalUsuarios = total,
            usuariosDuoc = duoc,
            usuariosNormales = total - duoc
        )
    }
}

/**
 * Datos estadísticos de usuarios
 */
data class EstadisticasUsuarios(
    val totalUsuarios: Int,
    val usuariosDuoc: Int,
    val usuariosNormales: Int
)