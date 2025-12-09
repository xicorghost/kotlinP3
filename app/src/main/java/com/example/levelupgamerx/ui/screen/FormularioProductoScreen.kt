/*package com.example.levelupgamerx.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelupgamerx.data.local.entity.ProductoEntity
import com.example.levelupgamerx.domain.repository.RepositorioProductos
import kotlinx.coroutines.launch
import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.domain.model.CategoriaProducto // <-- AÑADE ESTA LÍNEA
import java.util.UUID // <-- AÑADE ESTA LÍNEA para generar un código

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    productoId: Int,
    productoRepository: RepositorioProductos,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Estados del formulario
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    var cargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    val esEdicion = productoId != -1
    val titulo = if (esEdicion) "Editar Producto" else "Nuevo Producto"

    // Cargar producto si es edición
    LaunchedEffect(productoId) {
        if (esEdicion) {
            cargando = true
            val producto = productoRepository.obtenerProductoPorId(productoId)
            producto?.let {
                nombre = it.nombre
                descripcion = it.descripcion
                precio = it.precio.toString()
                stock = it.stock.toString()
                imagenUrl = it.imagenUrl
                categoria = it.categoria
            }
            cargando = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del producto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4
                )

                // Precio
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    prefix = { Text("$") }
                )

                // Stock
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it.filter { char -> char.isDigit() } },
                    label = { Text("Stock disponible") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Categoría
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ej: Acción, RPG, Deportes") }
                )

                // URL de imagen
                OutlinedTextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it },
                    label = { Text("URL de la imagen") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("https://...") }
                )

                // Mensaje de error
                mensajeError?.let { error ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // Botón guardar
                Button(
                    onClick = {
                        // Validar campos
                        when {
                            nombre.isBlank() -> mensajeError = "El nombre es obligatorio"
                            descripcion.isBlank() -> mensajeError = "La descripción es obligatoria"
                            precio.isBlank() -> mensajeError = "El precio es obligatorio"
                            stock.isBlank() -> mensajeError = "El stock es obligatorio"
                            categoria.isBlank() -> mensajeError = "La categoría es obligatoria"
                            imagenUrl.isBlank() -> mensajeError = "La URL de imagen es obligatoria"
                            else -> {
                                scope.launch {
                                    try {
                                        val producto = Producto(
                                            id = if (esEdicion) productoId else 0,
                                            nombre = nombre,
                                            descripcion = descripcion,
                                            precio = precio.toDoubleOrNull() ?: 0.0,
                                            stock = stock.toIntOrNull() ?: 0,
                                            imagenUrl = imagenUrl,
                                            categoria = categoria
                                        )

                                        if (esEdicion) {
                                            productoRepository.actualizarProducto(producto)
                                        } else {
                                            productoRepository.insertarProducto(producto)
                                        }

                                        onBackClick()
                                    } catch (e: Exception) {
                                        mensajeError = "Error al guardar: ${e.message}"
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !cargando
                ) {
                    Text(
                        text = if (esEdicion) "ACTUALIZAR PRODUCTO" else "CREAR PRODUCTO",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}*/
package com.example.levelupgamerx.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer // <-- IMPORTACIÓN CORREGIDA
import androidx.compose.ui.unit.dp
import com.example.levelupgamerx.data.local.entity.ProductoEntity
import com.example.levelupgamerx.domain.model.CategoriaProducto
import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.domain.repository.RepositorioProductos
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    productoId: Int,
    productoRepository: RepositorioProductos,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Estados del formulario
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }

    // Estados corregidos para Codigo y Categoría (Enum)
    var codigo by remember { mutableStateOf("") }
    var categoriaString by remember { mutableStateOf("") }
    var categoriaExpandida by remember { mutableStateOf(false) }

    var cargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    val esEdicion = productoId != -1
    val titulo = if (esEdicion) "Editar Producto" else "Nuevo Producto"

    // Cargar producto si es edición
    LaunchedEffect(productoId) {
        if (esEdicion) {
            cargando = true
            val producto = productoRepository.obtenerProductoPorId(productoId)
            producto?.let {
                nombre = it.nombre
                descripcion = it.descripcion
                precio = it.precio.toString()
                stock = it.stock.toString()
                imagenUrl = it.imagenUrl
                codigo = it.codigo
                // CORRECCIÓN: Convertir CategoriaProducto a String para la UI
                categoriaString = it.categoria.displayName
            }
            cargando = false
        } else {
            // Inicializar un código único para productos nuevos
            codigo = UUID.randomUUID().toString().substring(0, 8).uppercase()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Codigo del producto
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    label = { Text("Código de Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !esEdicion
                )

                // Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del producto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4
                )

                // Precio
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    prefix = { Text("$") }
                )

                // Stock
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it.filter { char -> char.isDigit() } },
                    label = { Text("Stock disponible") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // --- Dropdown para Categoría (Usa el Enum CategoriaProducto) ---
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = categoriaString,
                        onValueChange = { /* Solo se cambia por Dropdown */ },
                        label = { Text("Categoría") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Seleccionar Categoría",
                                // CORRECCIÓN DEL ERROR 'rotation': Usamos graphicsLayer
                                modifier = Modifier.graphicsLayer {
                                    rotationZ = if (categoriaExpandida) 90f else 270f
                                }
                            )
                        },
                        // Permite abrir el Dropdown al hacer click en el campo
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { categoriaExpandida = true }
                    )

                    DropdownMenu(
                        expanded = categoriaExpandida,
                        onDismissRequest = { categoriaExpandida = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        CategoriaProducto.values().forEach { categoriaEnum ->
                            DropdownMenuItem(
                                text = { Text(categoriaEnum.displayName) },
                                onClick = {
                                    categoriaString = categoriaEnum.displayName
                                    categoriaExpandida = false
                                }
                            )
                        }
                    }
                }
                // --- Fin Dropdown Categoría ---

                // URL de imagen
                OutlinedTextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it },
                    label = { Text("URL de la imagen") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("https://...") }
                )

                // Mensaje de error
                mensajeError?.let { error ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // Botón guardar
                Button(
                    onClick = {
                        // Validar campos
                        when {
                            nombre.isBlank() -> mensajeError = "El nombre es obligatorio"
                            descripcion.isBlank() -> mensajeError = "La descripción es obligatoria"
                            precio.isBlank() -> mensajeError = "El precio es obligatorio"
                            stock.isBlank() -> mensajeError = "El stock es obligatorio"
                            categoriaString.isBlank() -> mensajeError = "La categoría es obligatoria"
                            imagenUrl.isBlank() -> mensajeError = "La URL de imagen es obligatoria"
                            codigo.isBlank() -> mensajeError = "El código es obligatorio"
                            else -> {
                                scope.launch {
                                    try {
                                        // CORRECCIÓN: Convertir String de la UI al Enum para el modelo
                                        val categoriaEnum = CategoriaProducto.values().find {
                                            it.displayName == categoriaString
                                        } ?: CategoriaProducto.ACCESORIOS

                                        // CORRECCIÓN: Se utiliza el constructor completo de Producto (incluyendo código y categoría Enum)
                                        val productoAGuardar = Producto(
                                            id = if (esEdicion) productoId else 0,
                                            codigo = codigo,
                                            nombre = nombre,
                                            descripcion = descripcion,
                                            precio = precio.toDoubleOrNull() ?: 0.0,
                                            stock = stock.toIntOrNull() ?: 0,
                                            imagenUrl = imagenUrl,
                                            categoria = categoriaEnum
                                        )

                                        if (esEdicion) {
                                            productoRepository.actualizarProducto(productoAGuardar)
                                        } else {
                                            productoRepository.insertarProducto(productoAGuardar)
                                        }

                                        onBackClick()
                                    } catch (e: Exception) {
                                        mensajeError = "Error al guardar: ${e.message}"
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !cargando
                ) {
                    Text(
                        text = if (esEdicion) "ACTUALIZAR PRODUCTO" else "CREAR PRODUCTO",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}