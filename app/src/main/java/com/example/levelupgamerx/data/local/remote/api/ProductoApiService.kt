package com.example.levelupgamerx.data.local.remote.api

import com.example.levelupgamerx.data.local.remote.dto.ProductoDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface del servicio API para productos gamer
 *
 * API Base: https://api-dfs2-dm-production.up.railway.app/
 *
 * @author LevelUp GamerX Team
 */
interface ProductoApiService {

    /**
     * Obtiene todos los productos
     *
     * Endpoint: GET /productos
     */
    @GET("productos")
    suspend fun obtenerTodos(): Response<List<ProductoDto>>

    /**
     * Obtiene un producto específico por ID
     *
     * Endpoint: GET /productos/{id}
     */
    @GET("productos/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Int
    ): Response<ProductoDto>

    /**
     * Crea un nuevo producto
     *
     * Endpoint: POST /productos
     */
    @POST("productos")
    suspend fun crearProducto(
        @Body producto: ProductoDto
    ): Response<ProductoDto>

    /**
     * Actualiza un producto existente
     *
     * Endpoint: PUT /productos/{id}
     */
    @PUT("productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Int,
        @Body producto: ProductoDto
    ): Response<ProductoDto>

    /**
     * Elimina un producto
     *
     * Endpoint: DELETE /productos/{id}
     */
    @DELETE("productos/{id}")
    suspend fun eliminarProducto(
        @Path("id") id: Int
    ): Response<Unit>

    /**
     * Busca productos por nombre
     *
     * Si tu API soporta búsqueda, ajusta el endpoint
     */
    @GET("productos")
    suspend fun buscarProductos(
        @Query("nombre") nombre: String
    ): Response<List<ProductoDto>>

    /**
     * Obtiene productos por categoría
     *
     * Si tu API soporta filtrado por categoría
     */
    @GET("productos")
    suspend fun obtenerPorCategoria(
        @Query("categoria") categoria: String
    ): Response<List<ProductoDto>>
}

/**
 * NOTA: Si tu API tiene rutas diferentes, ajusta los endpoints.
 * Por ejemplo, si usa /api/productos en lugar de /productos,
 * cambia las rutas en los @GET, @POST, etc.
 */