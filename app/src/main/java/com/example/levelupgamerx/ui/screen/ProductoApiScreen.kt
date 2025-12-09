package com.example.levelupgamerx.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.levelupgamerx.domain.model.Producto
// Importaciones de los repositorios concretos (capa data)
import com.example.levelupgamerx.data.repository.ProductoLocalRepository
import com.example.levelupgamerx.data.repository.ProductoRemotoRepository

import com.example.levelupgamerx.ui.component.*
import com.example.levelupgamerx.ui.theme.*
import com.example.levelupgamerx.ui.viewmodel.ProductosApiViewModel
import com.example.levelupgamerx.ui.viewmodel.ProductosApiViewModelFactory
import com.example.levelupgamerx.ui.viewmodel.ProductosApiUiState

/**
 * Pantalla para explorar productos desde la API de Railway
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosApiScreen(
    onBackClick: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    // 🛑 CORRECCIÓN: Usar el tipo de repositorio CONCRETO que el Factory espera
    productoRepository: ProductoRemotoRepository,
    localRepository: ProductoLocalRepository,

    viewModel: ProductosApiViewModel = viewModel(
        factory = ProductosApiViewModelFactory(
            productoRepository, // Pasa el ProductoRemotoRepository
            localRepository    // Pasa el ProductoLocalRepository
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var busqueda by remember { mutableStateOf("") }
    var mostrarFiltros by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar mensaje de importación
    LaunchedEffect(uiState.mensajeImportacion) {
        uiState.mensajeImportacion?.let { mensaje ->
            snackbarHostState.showSnackbar(
                message = mensaje,
                duration = SnackbarDuration.Short
            )
            viewModel.limpiarMensajeImportacion()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("EXPLORAR PRODUCTOS API")
                        Text(
                            text = "Powered by Railway",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = NeonGreen)
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarFiltros = !mostrarFiltros }) {
                        Icon(
                            Icons.Default.FilterList,
                            "Filtros",
                            tint = if (mostrarFiltros) CyberYellow else NeonGreen
                        )
                    }
                    IconButton(onClick = { viewModel.cargarProductos() }) {
                        Icon(Icons.Default.Refresh, "Recargar", tint = CyberBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCard,
                    titleContentColor = NeonGreen
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Barra de búsqueda
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = busqueda,
                        onValueChange = { busqueda = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Buscar productos...", color = TextDisabled) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, null, tint = CyberBlue)
                        },
                        trailingIcon = {
                            if (busqueda.isNotEmpty()) {
                                IconButton(onClick = { busqueda = "" }) {
                                    Icon(Icons.Default.Clear, "Limpiar", tint = TextSecondary)
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = TextDisabled,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    IconButton(
                        onClick = {
                            if (busqueda.isNotBlank()) {
                                viewModel.buscarProductos(busqueda)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Send, "Buscar", tint = NeonGreen)
                    }
                }
            }

            // Panel de filtros
            if (mostrarFiltros) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "FILTROS RÁPIDOS",
                            color = CyberYellow,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.cargarProductosGamer() },
                                label = { Text("Solo Gamer") }
                            )
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.filtrarPorCategoria("MOUSE") },
                                label = { Text("Mouse") }
                            )
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.filtrarPorCategoria("ACCESORIOS") },
                                label = { Text("Accesorios") }
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.filtrarPorCategoria("CONSOLAS") },
                                label = { Text("Consolas") }
                            )
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.cargarProductos() },
                                label = { Text("Todos") }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Contenido principal
            when {
                uiState.estaCargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(color = NeonGreen)
                            Text("Cargando productos...", color = TextSecondary)
                        }
                    }
                }

                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                Icons.Default.ErrorOutline,
                                null,
                                modifier = Modifier.size(64.dp),
                                tint = Error
                            )
                            Text(
                                text = uiState.error ?: "Error desconocido",
                                color = Error,
                                fontSize = 16.sp
                            )
                            CyberpunkButton(
                                text = "Reintentar",
                                onClick = { viewModel.cargarProductos() }
                            )
                        }
                    }
                }

                uiState.productos.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                Icons.Default.SearchOff,
                                null,
                                modifier = Modifier.size(64.dp),
                                tint = TextDisabled
                            )
                            Text("No se encontraron productos", color = TextSecondary)
                            CyberpunkButton(
                                text = "Cargar todos",
                                onClick = { viewModel.cargarProductos() }
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "${uiState.productos.size} productos encontrados",
                                color = TextSecondary,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        items(uiState.productos) { producto ->
                            ProductoApiCard(
                                producto = producto,
                                onClick = { onProductoClick(producto) },
                                onImportar = { viewModel.importarProducto(producto) },
                                importando = uiState.importando
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Card de producto de la API con diseño cyberpunk
 */
@Composable
fun ProductoApiCard(
    producto: Producto,
    onClick: () -> Unit,
    onImportar: () -> Unit,
    importando: Boolean = false
) {
    val context = LocalContext.current

    CyberpunkCard(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen
            val imageResId = context.resources.getIdentifier(
                producto.imagenUrl,
                "drawable",
                context.packageName
            )

            Image(
                painter = rememberAsyncImagePainter(
                    if (imageResId != 0) imageResId else producto.imagenUrl
                ),
                contentDescription = producto.nombre,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )

            // Información
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = producto.nombre,
                    color = NeonGreen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                Text(
                    // Acceder al nombre legible del enum
                    text = producto.categoria.displayName,
                    color = CyberBlue,
                    fontSize = 12.sp
                )

                Text(
                    text = producto.descripcion,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 2
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AttachMoney,
                            null,
                            tint = CyberYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = producto.precioFormateado(),
                            color = CyberYellow,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Stock: ${producto.stock}",
                        color = if (producto.stock > 0) Success else Error,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Botón importar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = onImportar,
                    enabled = !importando
                ) {
                    if (importando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Success,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.Download,
                            "Importar",
                            tint = Success
                        )
                    }
                }
                Text(
                    text = if (importando) "..." else "Importar",
                    color = Success,
                    fontSize = 10.sp
                )
            }
        }
    }
}