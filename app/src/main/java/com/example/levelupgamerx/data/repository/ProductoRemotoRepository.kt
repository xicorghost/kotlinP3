package com.example.levelupgamerx.data.repository

/*import com.example.levelupgamerx.data.local.entity.ProductoEntity
import com.example.levelupgamerx.data.local.remote.ResultadoApi
import com.example.levelupgamerx.data.local.remote.RetrofitClient
import com.example.levelupgamerx.data.local.remote.api.ProductoApiService
import com.example.levelupgamerx.data.local.remote.dto.aProductos
import com.example.levelupgamerx.data.local.remote.dto.aDto*/
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.example.levelupgamerx.domain.model.CategoriaProducto

import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.data.local.remote.ResultadoApi
import com.example.levelupgamerx.data.local.remote.RetrofitClient
import com.example.levelupgamerx.data.local.remote.api.ProductoApiService
import com.example.levelupgamerx.data.local.remote.dto.aProductos
import com.example.levelupgamerx.data.local.remote.dto.aProducto
import com.example.levelupgamerx.data.local.remote.dto.aDto

/**
 * Repositorio para consumir productos desde la API de Railway
 *
 * @author LevelUp GamerX Team
 */
class ProductoRemotoRepository {

    private val apiService: ProductoApiService =
        RetrofitClient.crearServicio(ProductoApiService::class.java)

    /**
     * Obtiene todos los productos desde la API
     *
     * @return Flow con ResultadoApi<List<Producto>>
     */
    fun obtenerProductos(): Flow<ResultadoApi<List<Producto>>> = flow {
        emit(ResultadoApi.Cargando)

        try {
            val respuesta = apiService.obtenerTodos()

            if (respuesta.isSuccessful) {
                val productos = respuesta.body()?.aProductos() ?: emptyList()
                emit(ResultadoApi.Exito(productos))
            } else {
                emit(ResultadoApi.Error(
                    mensajeError = "Error al obtener productos: ${respuesta.code()}",
                    codigoHttp = respuesta.code()
                ))
            }
        } catch (e: Exception) {
            emit(ResultadoApi.Error(
                mensajeError = "Error de conexión: ${e.message}",
                excepcion = e
            ))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Obtiene solo productos de categorías gamer
     */
    fun obtenerProductosGamer(): Flow<ResultadoApi<List<Producto>>> = flow {
        emit(ResultadoApi.Cargando)

        try {
            val respuesta = apiService.obtenerTodos()

            if (respuesta.isSuccessful) {
                val todosLosProductos = respuesta.body()?.aProductos() ?: emptyList()

                val categoriasGamer = listOf(
                    CategoriaProducto.ACCESORIOS,
                    CategoriaProducto.COMPUTADORES,
                    CategoriaProducto.CONSOLAS,
                    CategoriaProducto.SILLAS_GAMER,
                    CategoriaProducto.MOUSE,
                    CategoriaProducto.MOUSEPAD,
                    CategoriaProducto.CLAVES_STEAM,
                    CategoriaProducto.JUEGOS_MESA,
                    CategoriaProducto.POLERAS
                )

                val productosGamer = todosLosProductos.filter { producto ->
                    producto.categoria in categoriasGamer
                }

                // Filtrar solo categorías gamer
                /*val productosGamer = todosLosProductos.filter { producto ->
                    producto.categoria in listOf(
                        "ACCESORIOS",
                        "COMPUTADORES",
                        "CONSOLAS",
                        "SILLAS_GAMER",
                        "MOUSE",
                        "MOUSEPAD",
                        "CLAVES_STEAM",
                        "JUEGOS_MESA",
                        "POLERAS"
                    )
                }*/

                emit(ResultadoApi.Exito(productosGamer))
            } else {
                emit(ResultadoApi.Error(
                    mensajeError = "Error al obtener productos gamer: ${respuesta.code()}",
                    codigoHttp = respuesta.code()
                ))
            }
        } catch (e: Exception) {
            emit(ResultadoApi.Error(
                mensajeError = "Error de conexión: ${e.message}",
                excepcion = e
            ))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Obtiene un producto por ID
     */
    fun obtenerProductoPorId(id: Int): Flow<ResultadoApi<Producto>> = flow {
        emit(ResultadoApi.Cargando)

        try {
            val respuesta = apiService.obtenerPorId(id)

            if (respuesta.isSuccessful) {
                val producto = respuesta.body()?.aProducto()
                if (producto != null) {
                    emit(ResultadoApi.Exito(producto))
                } else {
                    emit(ResultadoApi.Error("Producto no encontrado"))
                }
            } else {
                emit(ResultadoApi.Error(
                    mensajeError = "Error al obtener producto: ${respuesta.code()}",
                    codigoHttp = respuesta.code()
                ))
            }
        } catch (e: Exception) {
            emit(ResultadoApi.Error(
                mensajeError = "Error de conexión: ${e.message}",
                excepcion = e
            ))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Busca productos por nombre
     */
    fun buscarProductos(query: String): Flow<ResultadoApi<List<Producto>>> = flow {
        emit(ResultadoApi.Cargando)

        try {
            // Si tu API soporta búsqueda, usa buscarProductos
            // Si no, obtén todos y filtra localmente
            val respuesta = apiService.obtenerTodos()

            if (respuesta.isSuccessful) {
                val todosLosProductos = respuesta.body()?.aProductos() ?: emptyList()

                // Filtrar por nombre localmente
                val productosFiltrados = todosLosProductos.filter { producto ->
                    producto.nombre.contains(query, ignoreCase = true) ||
                            producto.descripcion.contains(query, ignoreCase = true)
                }

                emit(ResultadoApi.Exito(productosFiltrados))
            } else {
                emit(ResultadoApi.Error(
                    mensajeError = "Error en búsqueda: ${respuesta.code()}",
                    codigoHttp = respuesta.code()
                ))
            }
        } catch (e: Exception) {
            emit(ResultadoApi.Error(
                mensajeError = "Error de conexión: ${e.message}",
                excepcion = e
            ))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Obtiene productos por categoría
     */
    fun obtenerPorCategoria(categoria: String): Flow<ResultadoApi<List<Producto>>> = flow {
        emit(ResultadoApi.Cargando)

        try {
            val respuesta = apiService.obtenerTodos()

            if (respuesta.isSuccessful) {
                val todosLosProductos = respuesta.body()?.aProductos() ?: emptyList()

                val productosFiltrados = todosLosProductos.filter {
                    it.categoria.name.equals(categoria, ignoreCase = true)
                    //it.categoria.equals(categoria, ignoreCase = true)
                }

                emit(ResultadoApi.Exito(productosFiltrados))
            } else {
                emit(ResultadoApi.Error(
                    mensajeError = "Error al filtrar por categoría: ${respuesta.code()}",
                    codigoHttp = respuesta.code()
                ))
            }
        } catch (e: Exception) {
            emit(ResultadoApi.Error(
                mensajeError = "Error de conexión: ${e.message}",
                excepcion = e
            ))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Crea un nuevo producto (solo para admin)
     */
    suspend fun crearProducto(producto: Producto): ResultadoApi<Producto> {
        return try {
            val respuesta = apiService.crearProducto(producto.aDto())

            if (respuesta.isSuccessful) {
                val productoCreado = respuesta.body()?.aProducto()
                if (productoCreado != null) {
                    ResultadoApi.Exito(productoCreado)
                } else {
                    ResultadoApi.Error("No se pudo crear el producto")
                }
            } else {
                ResultadoApi.Error(
                    mensajeError = "Error al crear producto: ${respuesta.code()}",
                    codigoHttp = respuesta.code()
                )
            }
        } catch (e: Exception) {
            ResultadoApi.Error(
                mensajeError = "Error de conexión: ${e.message}",
                excepcion = e
            )
        }
    }

    /**
     * Actualiza un producto existente (solo para admin)
     */
    suspend fun actualizarProducto(producto: Producto): ResultadoApi<Producto> {
        return try {
            val respuesta = apiService.actualizarProducto(producto.id, producto.aDto())

            if (respuesta.isSuccessful) {
                val productoActualizado = respuesta.body()?.aProducto()
                if (productoActualizado != null) {
                    ResultadoApi.Exito(productoActualizado)
                } else {
                    ResultadoApi.Error("No se pudo actualizar el producto")
                }
            } else {
                ResultadoApi.Error(
                    mensajeError = "Error al actualizar producto: ${respuesta.code()}",
                    codigoHttp = respuesta.code()
                )
            }
        } catch (e: Exception) {
            ResultadoApi.Error(
                mensajeError = "Error de conexión: ${e.message}",
                excepcion = e
            )
        }
    }

    /**
     * Elimina un producto (solo para admin)
     */
    suspend fun eliminarProducto(id: Int): ResultadoApi<Unit> {
        return try {
            val respuesta = apiService.eliminarProducto(id)

            if (respuesta.isSuccessful) {
                ResultadoApi.Exito(Unit)
            } else {
                ResultadoApi.Error(
                    mensajeError = "Error al eliminar producto: ${respuesta.code()}",
                    codigoHttp = respuesta.code()
                )
            }
        } catch (e: Exception) {
            ResultadoApi.Error(
                mensajeError = "Error de conexión: ${e.message}",
                excepcion = e
            )
        }
    }
}