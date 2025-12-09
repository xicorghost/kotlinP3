package com.example.levelupgamerx.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.data.local.remote.ResultadoApi
import com.example.levelupgamerx.data.repository.ProductoLocalRepository
import com.example.levelupgamerx.data.repository.ProductoRemotoRepository // Importación correcta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de productos de la API.
 * Recibe ambos repositorios por inyección de dependencias (a través del Factory).
 */
class ProductosApiViewModel(
    // ✅ 1. Recibir ambos repositorios
    private val productoRemotoRepository: ProductoRemotoRepository,
    private val productoLocalRepository: ProductoLocalRepository
) : ViewModel() {

    // Se eliminó la instanciación interna 'private val repository = ProductoRemotoRepository()'

    private val _uiState = MutableStateFlow(ProductosApiUiState())
    val uiState: StateFlow<ProductosApiUiState> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    /**
     * Carga todos los productos
     */
    fun cargarProductos() {
        viewModelScope.launch {
            // ✅ 2. Usar el repositorio inyectado
            productoRemotoRepository.obtenerProductos().collect { resultado ->
                actualizarEstado(resultado)
            }
        }
    }

    /**
     * Carga solo productos gamer
     */
    fun cargarProductosGamer() {
        viewModelScope.launch {
            // ✅ 2. Usar el repositorio inyectado
            productoRemotoRepository.obtenerProductosGamer().collect { resultado ->
                actualizarEstado(resultado)
            }
        }
    }

    /**
     * Busca productos por nombre
     */
    fun buscarProductos(query: String) {
        viewModelScope.launch {
            // ✅ 2. Usar el repositorio inyectado
            productoRemotoRepository.buscarProductos(query).collect { resultado ->
                actualizarEstado(resultado)
            }
        }
    }

    /**
     * Filtra productos por categoría
     */
    fun filtrarPorCategoria(categoria: String) {
        viewModelScope.launch {
            // ✅ 2. Usar el repositorio inyectado
            productoRemotoRepository.obtenerPorCategoria(categoria).collect { resultado ->
                actualizarEstado(resultado)
            }
        }
    }

    /**
     * Actualiza el estado según el resultado de la API
     */
    private fun actualizarEstado(resultado: ResultadoApi<List<Producto>>) {
        when (resultado) {
            is ResultadoApi.Cargando -> {
                _uiState.value = _uiState.value.copy(
                    estaCargando = true,
                    error = null
                )
            }
            is ResultadoApi.Exito -> {
                _uiState.value = _uiState.value.copy(
                    productos = resultado.datos,
                    estaCargando = false,
                    error = null
                )
            }
            is ResultadoApi.Error -> {
                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    error = resultado.mensajeError
                )
            }
        }
    }

    /**
     * Limpia el error
     */
    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Importa un producto a la base de datos local
     */
    fun importarProducto(producto: Producto) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(importando = true)

            try {
                // El repositorio local ya está inyectado
                productoLocalRepository.insertar(producto)

                _uiState.value = _uiState.value.copy(
                    importando = false,
                    mensajeImportacion = "Producto importado exitosamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    importando = false,
                    mensajeImportacion = "Error al importar: ${e.message}"
                )
            }
        }
    }

    fun limpiarMensajeImportacion() {
        _uiState.value = _uiState.value.copy(mensajeImportacion = null)
    }
}


/**
 * Estado de la UI
 */
data class ProductosApiUiState(
    val productos: List<Producto> = emptyList(),
    val estaCargando: Boolean = false,
    val error: String? = null,
    val importando: Boolean = false,
    val mensajeImportacion: String? = null
)