package com.example.levelupgamerx.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.repository.UsuarioRepository
import com.example.levelupgamerx.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestión de usuarios
 * Login, registro, puntos, referidos
 */
class UsuarioViewModel(
    private val repository: UsuarioRepository,
    private val preferencias: PreferenciasManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    init {
        cargarUsuarioActual()
    }

    /**
     * Carga el usuario de la sesión actual
     */
    private fun cargarUsuarioActual() {
        viewModelScope.launch {
            val usuarioId = preferencias.obtenerUsuarioId()
            if (usuarioId != null) {
                val usuario = repository.obtenerPorId(usuarioId)
                _uiState.value = _uiState.value.copy(
                    usuarioActual = usuario,
                    estaLogueado = usuario != null
                )
            }
        }
    }

    /**
     * Registra un nuevo usuario
     */
    fun registrarUsuario(registro: RegistroUsuario) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(estaCargando = true)

            // Validar datos
            val validacion = registro.validar()
            if (validacion is ResultadoValidacion.Error) {
                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    error = validacion.mensaje
                )
                return@launch
            }

            // Intentar registro
            val resultado = repository.registrarUsuario(registro)
            resultado.fold(
                onSuccess = { usuario ->
                    // Guardar sesión
                    preferencias.guardarSesionUsuario(
                        id = usuario.id,
                        nombre = usuario.nombre,
                        email = usuario.email,
                        puntos = usuario.puntos,
                        nivel = usuario.nivel,
                        esDuoc = usuario.esDuoc,
                        codigoReferido = usuario.codigoReferido
                    )
                    _uiState.value = _uiState.value.copy(
                        estaCargando = false,
                        usuarioActual = usuario,
                        estaLogueado = true,
                        error = null,
                        registroExitoso = true
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        estaCargando = false,
                        error = e.message ?: "Error en el registro"
                    )
                }
            )
        }
    }

    /**
     * Login de usuario
     */
    fun loginUsuario(login: LoginUsuario) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(estaCargando = true)

            val usuario = repository.loginUsuario(login)
            if (usuario != null) {
                // Guardar sesión
                preferencias.guardarSesionUsuario(
                    id = usuario.id,
                    nombre = usuario.nombre,
                    email = usuario.email,
                    puntos = usuario.puntos,
                    nivel = usuario.nivel,
                    esDuoc = usuario.esDuoc,
                    codigoReferido = usuario.codigoReferido
                )
                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    usuarioActual = usuario,
                    estaLogueado = true,
                    error = null,
                    loginExitoso = true
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    error = "Credenciales incorrectas"
                )
            }
        }
    }

    /**
     * Cierra la sesión del usuario
     */
    fun cerrarSesion() {
        preferencias.cerrarSesionUsuario()
        _uiState.value = UsuarioUiState()
    }

    /**
     * Agrega puntos por compra
     */
    fun agregarPuntosPorCompra(totalCompra: Double) {
        viewModelScope.launch {
            val usuario = _uiState.value.usuarioActual ?: return@launch
            repository.agregarPuntosPorCompra(usuario.id, totalCompra)

            // Actualizar usuario
            val usuarioActualizado = repository.obtenerPorId(usuario.id)
            _uiState.value = _uiState.value.copy(usuarioActual = usuarioActualizado)

            // Actualizar preferencias
            usuarioActualizado?.let {
                preferencias.actualizarPuntos(it.puntos, it.nivel)
            }
        }
    }

    /**
     * Registra una compra de producto
     */
    fun registrarCompra(codigoProducto: String) {
        viewModelScope.launch {
            val usuario = _uiState.value.usuarioActual ?: return@launch
            repository.registrarCompra(usuario.id, codigoProducto)

            // Recargar usuario
            val usuarioActualizado = repository.obtenerPorId(usuario.id)
            _uiState.value = _uiState.value.copy(usuarioActual = usuarioActualizado)
        }
    }

    /**
     * Limpia mensajes de estado
     */
    fun limpiarEstado() {
        _uiState.value = _uiState.value.copy(
            error = null,
            registroExitoso = false,
            loginExitoso = false
        )
    }
}

/**
 * Estado UI del usuario
 */
data class UsuarioUiState(
    val usuarioActual: Usuario? = null,
    val estaLogueado: Boolean = false,
    val estaCargando: Boolean = false,
    val error: String? = null,
    val registroExitoso: Boolean = false,
    val loginExitoso: Boolean = false
)

/**
 * Factory para UsuarioViewModel
 */
class UsuarioViewModelFactory(
    private val repository: UsuarioRepository,
    private val preferencias: PreferenciasManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuarioViewModel(repository, preferencias) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}