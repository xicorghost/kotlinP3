package com.example.levelupgamerx.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.levelupgamerx.data.repository.ProductoRepositoryImpl
import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.ui.state.ProductoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel para gestión de productos
 */
class ProductoViewModel(
    private val repository: ProductoRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    /**
     * Carga todos los productos desde el repositorio
     */
    private fun cargarProductos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(estaCargando = true)

            repository.obtenerProductos()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        estaCargando = false,
                        error = e.message ?: "Error desconocido"
                    )
                }
                .collect { productos ->
                    _uiState.value = _uiState.value.copy(
                        estaCargando = false,
                        productos = productos,
                        error = null
                    )
                }
        }
    }

    /**
     * Busca un producto por ID
     */
    suspend fun obtenerProductoPorId(id: Int): Producto? {
        return repository.obtenerProductoPorId(id)
    }

    /**
     * Filtra productos por categoría
     */
    fun filtrarPorCategoria(categoria: String) {
        viewModelScope.launch {
            if (categoria == "Todas") {
                cargarProductos()
            } else {
                // Implementar filtrado
                val productosFiltrados = _uiState.value.productos.filter {
                    it.categoria.displayName == categoria
                }
                _uiState.value = _uiState.value.copy(productos = productosFiltrados)
            }
        }
    }

    /**
     * Busca productos por nombre
     */
    fun buscarProductos(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                cargarProductos()
            } else {
                val todos = repository.obtenerProductos()// 🔥 Obtienes la lista real
                val productosEncontrados = _uiState.value.productos.filter {
                    it.nombre.contains(query, ignoreCase = true) ||
                            it.descripcion.contains(query, ignoreCase = true)
                }
                _uiState.value = _uiState.value.copy(productos = productosEncontrados)
            }
        }
    }

    /**
     * Agrega un nuevo producto (admin)
     */
    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.insertarProducto(producto)
                cargarProductos() // Recargar lista
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al agregar producto: ${e.message}"
                )
            }
        }
    }

    /**
     * Actualiza un producto existente (admin)
     */
    fun actualizarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.actualizarProducto(producto)
                cargarProductos()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al actualizar producto: ${e.message}"
                )
            }
        }
    }

    /**
     * Elimina un producto (admin)
     */
    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.eliminarProducto(producto)
                cargarProductos()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al eliminar producto: ${e.message}"
                )
            }
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * Factory para crear ProductoViewModel con dependencias
 */
class ProductoViewModelFactory(
    private val repository: ProductoRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}