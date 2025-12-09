package com.example.levelupgamerx.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.repository.CarritoRepository
import com.example.levelupgamerx.data.repository.UsuarioRepository
import com.example.levelupgamerx.domain.model.ItemCarrito
import com.example.levelupgamerx.domain.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestión del carrito de compras
 */
class CarritoViewModel(
    private val carritoRepository: CarritoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val preferencias: PreferenciasManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    init {
        cargarCarrito()
    }

    /**
     * Carga los items del carrito
     */
    private fun cargarCarrito() {
        viewModelScope.launch {
            carritoRepository.obtenerCarrito().collect { items ->
                val esDuoc = preferencias.esUsuarioDuoc()
                val total = calcularTotal(items, esDuoc)

                _uiState.value = _uiState.value.copy(
                    items = items,
                    total = total,
                    totalSinDescuento = calcularTotal(items, false),
                    descuentoAplicado = esDuoc
                )
            }
        }
    }

    /**
     * Calcula el total del carrito
     */
    private fun calcularTotal(items: List<ItemCarrito>, aplicarDescuento: Boolean): Double {
        val subtotal = items.sumOf { it.producto.precio * it.cantidad }
        return if (aplicarDescuento) subtotal * 0.8 else subtotal
    }

    /**
     * Agrega un producto al carrito
     */
    fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        viewModelScope.launch {
            try {
                carritoRepository.agregarProducto(producto, cantidad)
                _uiState.value = _uiState.value.copy(
                    mensaje = "Producto agregado al carrito"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al agregar producto: ${e.message}"
                )
            }
        }
    }

    /**
     * Incrementa la cantidad de un producto
     */
    fun incrementarCantidad(productoId: Int) {
        viewModelScope.launch {
            val item = _uiState.value.items.find { it.producto.id == productoId }
            if (item != null) {
                carritoRepository.modificarCantidad(productoId, item.cantidad + 1)
            }
        }
    }

    /**
     * Decrementa la cantidad de un producto
     */
    fun decrementarCantidad(productoId: Int) {
        viewModelScope.launch {
            val item = _uiState.value.items.find { it.producto.id == productoId }
            if (item != null) {
                carritoRepository.modificarCantidad(productoId, item.cantidad - 1)
            }
        }
    }

    /**
     * Elimina un producto del carrito
     */
    fun eliminarProducto(productoId: Int) {
        viewModelScope.launch {
            carritoRepository.eliminarProducto(productoId)
            _uiState.value = _uiState.value.copy(
                mensaje = "Producto eliminado del carrito"
            )
        }
    }

    /**
     * Procesa la compra
     */
    fun procesarCompra() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(estaProcesando = true)

            try {
                val usuarioId = preferencias.obtenerUsuarioId()
                if (usuarioId == null) {
                    _uiState.value = _uiState.value.copy(
                        estaProcesando = false,
                        error = "Debes iniciar sesión para comprar"
                    )
                    return@launch
                }

                // Registrar productos comprados
                _uiState.value.items.forEach { item ->
                    usuarioRepository.registrarCompra(usuarioId, item.producto.codigo)
                }

                // Agregar puntos por compra
                val totalCompra = _uiState.value.total
                usuarioRepository.agregarPuntosPorCompra(usuarioId, totalCompra)

                // Actualizar puntos en preferencias
                val usuario = usuarioRepository.obtenerPorId(usuarioId)
                usuario?.let {
                    preferencias.actualizarPuntos(it.puntos, it.nivel)
                }

                // Calcular puntos ganados
                val puntosGanados = (totalCompra / 1000).toInt()

                // Vaciar carrito
                carritoRepository.vaciarCarrito()

                _uiState.value = _uiState.value.copy(
                    estaProcesando = false,
                    compraExitosa = true,
                    puntosGanados = puntosGanados
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    estaProcesando = false,
                    error = "Error al procesar la compra: ${e.message}"
                )
            }
        }
    }

    /**
     * Limpia el carrito
     */
    fun vaciarCarrito() {
        viewModelScope.launch {
            carritoRepository.vaciarCarrito()
            _uiState.value = _uiState.value.copy(
                mensaje = "Carrito vaciado"
            )
        }
    }

    /**
     * Limpia mensajes de estado
     */
    fun limpiarEstado() {
        _uiState.value = _uiState.value.copy(
            error = null,
            mensaje = null,
            compraExitosa = false
        )
    }
}

/**
 * Estado UI del carrito
 */
data class CarritoUiState(
    val items: List<ItemCarrito> = emptyList(),
    val total: Double = 0.0,
    val totalSinDescuento: Double = 0.0,
    val descuentoAplicado: Boolean = false,
    val estaProcesando: Boolean = false,
    val compraExitosa: Boolean = false,
    val puntosGanados: Int = 0,
    val error: String? = null,
    val mensaje: String? = null
) {
    val estaVacio: Boolean get() = items.isEmpty()
    val cantidadItems: Int get() = items.sumOf { it.cantidad }
    val ahorro: Double get() = totalSinDescuento - total
}

/**
 * Factory para CarritoViewModel
 */
class CarritoViewModelFactory(
    private val carritoRepository: CarritoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val preferencias: PreferenciasManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarritoViewModel(carritoRepository, usuarioRepository, preferencias) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}