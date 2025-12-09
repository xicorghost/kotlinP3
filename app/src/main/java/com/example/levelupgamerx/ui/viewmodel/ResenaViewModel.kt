package com.example.levelupgamerx.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.repository.ResenaRepository
import com.example.levelupgamerx.data.repository.UsuarioRepository
import com.example.levelupgamerx.domain.model.Resena
import com.example.levelupgamerx.domain.model.ResultadoValidacion
import com.example.levelupgamerx.domain.model.validar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestión de reseñas
 */
class ResenaViewModel(
    private val resenaRepository: ResenaRepository,
    private val usuarioRepository: UsuarioRepository,
    private val preferencias: PreferenciasManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResenaUiState())
    val uiState: StateFlow<ResenaUiState> = _uiState.asStateFlow()

    /**
     * Carga las reseñas de un producto
     */
    fun cargarResenas(codigoProducto: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                estaCargando = true,
                codigoProductoActual = codigoProducto
            )

            resenaRepository.obtenerResenasPorProducto(codigoProducto).collect { resenas ->
                val estadisticas = resenaRepository.obtenerEstadisticasProducto(codigoProducto)

                // Verificar si el usuario puede reseñar
                val usuarioId = preferencias.obtenerUsuarioId()
                var puedeResenar = false

                if (usuarioId != null) {
                    val usuario = usuarioRepository.obtenerPorId(usuarioId)
                    puedeResenar = usuario?.puedeResenar(codigoProducto) == true &&
                            !resenaRepository.usuarioYaReseno(usuarioId, codigoProducto)
                }

                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    resenas = resenas,
                    calificacionPromedio = estadisticas.calificacionPromedio,
                    totalResenas = estadisticas.totalResenas,
                    puedeResenar = puedeResenar
                )
            }
        }
    }

    /**
     * Actualiza la calificación seleccionada
     */
    fun actualizarCalificacion(calificacion: Int) {
        _uiState.value = _uiState.value.copy(calificacionSeleccionada = calificacion)
    }

    /**
     * Actualiza el comentario
     */
    fun actualizarComentario(comentario: String) {
        _uiState.value = _uiState.value.copy(comentario = comentario)
    }

    /**
     * Publica una nueva reseña
     */
    fun publicarResena() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(estaPublicando = true)

            try {
                val usuarioId = preferencias.obtenerUsuarioId()
                val nombreUsuario = preferencias.obtenerUsuarioNombre()

                if (usuarioId == null || nombreUsuario == null) {
                    _uiState.value = _uiState.value.copy(
                        estaPublicando = false,
                        error = "Debes iniciar sesión para dejar una reseña"
                    )
                    return@launch
                }

                val resena = Resena(
                    codigoProducto = _uiState.value.codigoProductoActual,
                    usuarioId = usuarioId,
                    nombreUsuario = nombreUsuario,
                    calificacion = _uiState.value.calificacionSeleccionada,
                    comentario = _uiState.value.comentario
                )

                // Validar reseña
                val validacion = resena.validar()
                if (validacion is ResultadoValidacion.Error) {
                    _uiState.value = _uiState.value.copy(
                        estaPublicando = false,
                        error = validacion.mensaje
                    )
                    return@launch
                }

                // Publicar reseña
                val resultado = resenaRepository.agregarResena(resena)
                resultado.fold(
                    onSuccess = {
                        // Agregar puntos por reseña
                        usuarioRepository.agregarPuntosPorResena(usuarioId)

                        // Actualizar puntos en preferencias
                        val usuario = usuarioRepository.obtenerPorId(usuarioId)
                        usuario?.let {
                            preferencias.actualizarPuntos(it.puntos, it.nivel)
                        }

                        _uiState.value = _uiState.value.copy(
                            estaPublicando = false,
                            resenaPublicada = true,
                            calificacionSeleccionada = 0,
                            comentario = "",
                            puntosGanados = 50
                        )

                        // Recargar reseñas
                        cargarResenas(_uiState.value.codigoProductoActual)
                    },
                    onFailure = { e ->
                        _uiState.value = _uiState.value.copy(
                            estaPublicando = false,
                            error = e.message ?: "Error al publicar reseña"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    estaPublicando = false,
                    error = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    /**
     * Limpia el estado
     */
    fun limpiarEstado() {
        _uiState.value = _uiState.value.copy(
            error = null,
            resenaPublicada = false,
            puntosGanados = 0
        )
    }

    /**
     * Resetea el formulario
     */
    fun resetearFormulario() {
        _uiState.value = _uiState.value.copy(
            calificacionSeleccionada = 0,
            comentario = ""
        )
    }
}

/**
 * Estado UI de reseñas
 */
data class ResenaUiState(
    val codigoProductoActual: String = "",
    val resenas: List<Resena> = emptyList(),
    val calificacionPromedio: Float = 0f,
    val totalResenas: Int = 0,
    val estaCargando: Boolean = false,
    val estaPublicando: Boolean = false,
    val puedeResenar: Boolean = false,
    val resenaPublicada: Boolean = false,
    val puntosGanados: Int = 0,
    val calificacionSeleccionada: Int = 0,
    val comentario: String = "",
    val error: String? = null
) {
    val hayResenas: Boolean get() = resenas.isNotEmpty()
    val estrellasCompletas: Int get() = calificacionPromedio.toInt()
    val tieneMediaEstrella: Boolean get() = (calificacionPromedio % 1) >= 0.5f
    val formularioValido: Boolean
        get() = calificacionSeleccionada > 0 && comentario.length >= 10
}

/**
 * Factory para ResenaViewModel
 */
class ResenaViewModelFactory(
    private val resenaRepository: ResenaRepository,
    private val usuarioRepository: UsuarioRepository,
    private val preferencias: PreferenciasManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResenaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResenaViewModel(resenaRepository, usuarioRepository, preferencias) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}