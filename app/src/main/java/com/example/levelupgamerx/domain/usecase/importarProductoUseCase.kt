package com.example.levelupgamerx.domain.usecase

//import com.example.levelupgamerx.data.local.entity.Producto
import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.domain.repository.RepositorioProductos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Caso de uso para importar un producto de la API a la base de datos local
 */
class ImportarProductoUseCase(
    //private val productoRepository: ProductoRepository
    private val repositorioProductos: RepositorioProductos
) {
    /**
     * Importa un producto desde la API a la base de datos local
     *
     * @param producto Producto a importar
     * @return true si se importó correctamente, false si ya existe
     */
    suspend fun ejecutar(producto: Producto): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            // Verificar si el producto ya existe por nombre
            val productoExistente = repositorioProductos.buscarPorNombreExacto(producto.nombre)

            if (productoExistente != null) {
                // El producto ya existe
                return@withContext Result.failure(
                    Exception("El producto '${producto.nombre}' ya existe en tu inventario")
                )
            }

            // Crear una copia del producto con ID 0 para que Room genere uno nuevo
            val productoNuevo = producto.copy(id = 0)

            // Insertar el producto
            repositorioProductos.insertarProducto(productoNuevo)

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Importa múltiples productos
     */
    suspend fun importarMultiples(productos: List<Producto>): Result<ImportarMultiplesResult> =
        withContext(Dispatchers.IO) {
            try {
                var importados = 0
                var duplicados = 0
                val errores = mutableListOf<String>()

                productos.forEach { producto ->
                    val resultado = ejecutar(producto)
                    if (resultado.isSuccess) {
                        importados++
                    } else {
                        duplicados++
                        errores.add(resultado.exceptionOrNull()?.message ?: "Error desconocido")
                    }
                }

                Result.success(
                    ImportarMultiplesResult(
                        total = productos.size,
                        importados = importados,
                        duplicados = duplicados,
                        errores = errores
                    )
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}

/**
 * Resultado de importar múltiples productos
 */
data class ImportarMultiplesResult(
    val total: Int,
    val importados: Int,
    val duplicados: Int,
    val errores: List<String>
)