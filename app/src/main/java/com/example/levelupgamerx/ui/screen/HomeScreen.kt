package com.example.levelupgamerx.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import coil.request.ImageRequest
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.repository.CarritoRepository
import com.example.levelupgamerx.data.repository.ProductoRepositoryImpl
import com.example.levelupgamerx.domain.model.CategoriaProducto
import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.ui.component.*
import com.example.levelupgamerx.ui.theme.*
import com.example.levelupgamerx.ui.viewmodel.ProductoViewModel
import com.example.levelupgamerx.ui.viewmodel.ProductoViewModelFactory
import androidx.compose.foundation.BorderStroke // <-- ¡Esta es la importación que falta!

/**
 * Home Screen actualizado con diseño cyberpunk completo
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productoRepository: ProductoRepositoryImpl,
    carritoRepository: CarritoRepository,
    preferenciasManager: PreferenciasManager,
    onProductoClick: (Int) -> Unit,
    onCarritoClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onVolverPortada: () -> Unit
) {
    val viewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(productoRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    var textoBusqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todas") }

    // Obtener datos del usuario si está logueado
    val usuarioLogueado = preferenciasManager.estaUsuarioLogueado()
    val nombreUsuario = preferenciasManager.obtenerUsuarioNombre()
    val puntos = preferenciasManager.obtenerUsuarioPuntos()
    val nivel = preferenciasManager.obtenerUsuarioNivel()
    val esDuoc = preferenciasManager.esUsuarioDuoc()

    // Filtrar productos
    val productosFiltrados = remember(uiState.productos, textoBusqueda, categoriaSeleccionada) {
        uiState.productos.filter { producto ->
            val coincideBusqueda = textoBusqueda.isBlank() ||
                    producto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                    producto.descripcion.contains(textoBusqueda, ignoreCase = true)

            val coincideCategoria = categoriaSeleccionada == "Todas" ||
                    producto.categoria.displayName == categoriaSeleccionada

            coincideBusqueda && coincideCategoria
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("LEVEL-UP GAMER")
                        if (usuarioLogueado && nombreUsuario != null) {
                            Text(
                                text = "Hola, $nombreUsuario",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolverPortada) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    if (usuarioLogueado) {
                        IconButton(onClick = onPerfilClick) {
                            Icon(Icons.Default.Person, "Perfil")
                        }
                    } else {
                        IconButton(onClick = onPerfilClick) {
                            Icon(Icons.Default.Login, "Login")
                        }
                    }
                    IconButton(onClick = onCarritoClick) {
                        Icon(Icons.Default.ShoppingCart, "Carrito")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCard,
                    titleContentColor = NeonGreen,
                    navigationIconContentColor = NeonGreen,
                    actionIconContentColor = NeonGreen
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        when {
            uiState.estaCargando -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonGreen)
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = Error
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Header con información del usuario
                    if (usuarioLogueado) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkCard),
                                border = BorderStroke(2.dp, NeonGreen)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (esDuoc) {
                                        DescuentoDuocBadge()
                                    }

                                    PuntosNivelDisplay(
                                        puntos = puntos,
                                        nivel = nivel,
                                        progresoNivel = 0.5f // Calcular progreso real
                                    )
                                }
                            }
                        }
                    }

                    // Barra de búsqueda
                    item {
                        CyberpunkTextField(
                            value = textoBusqueda,
                            onValueChange = { textoBusqueda = it },
                            label = "Buscar productos...",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    // Filtros de categoría
                    item {
                        LazyRow(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            item {
                                CategoriaChip(
                                    texto = "Todas",
                                    seleccionado = categoriaSeleccionada == "Todas",
                                    onClick = { categoriaSeleccionada = "Todas" }
                                )
                            }

                            items(CategoriaProducto.values()) { categoria ->
                                CategoriaChip(
                                    texto = categoria.displayName,
                                    seleccionado = categoriaSeleccionada == categoria.displayName,
                                    onClick = { categoriaSeleccionada = categoria.displayName }
                                )
                            }
                        }
                    }

                    // Lista de productos
                    items(productosFiltrados) { producto ->
                        ProductoCardCyberpunk(
                            producto = producto,
                            esDuoc = esDuoc,
                            onClick = { onProductoClick(producto.id) }
                        )
                    }

                    // Mensaje si no hay productos
                    if (productosFiltrados.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No se encontraron productos",
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Card de producto con estilo cyberpunk
 */
@Composable
fun ProductoCardCyberpunk(
    producto: Producto,
    esDuoc: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(
        producto.imagenUrl,
        "drawable",
        context.packageName
    )

    CyberpunkCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(if (imageResId != 0) imageResId else producto.imagenUrl)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(100.dp)
                    .background(DarkBackground),
                contentScale = ContentScale.Crop
            )

            // Información
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Categoría
                Text(
                    text = "[${producto.categoria.displayName}]",
                    color = CyberBlue,
                    fontSize = 12.sp
                )

                // Nombre
                Text(
                    text = producto.nombre,
                    color = NeonGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Calificación
                if (producto.numeroResenas > 0) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RatingStars(
                            rating = producto.calificacionPromedio,
                            size = 16.dp
                        )
                        Text(
                            text = "(${producto.numeroResenas})",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                // Precio
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (esDuoc) {
                        Text(
                            text = producto.precioFormateado(),
                            color = TextDisabled,
                            fontSize = 14.sp,
                            style = LocalTextStyle.current.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        )
                        Text(
                            text = producto.precioConDescuentoFormateado(true),
                            color = Success,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = producto.precioFormateado(),
                            color = NeonGreen,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Stock
                Text(
                    text = if (producto.hayStock) "Stock: ${producto.stock}" else "Sin stock",
                    color = if (producto.hayStock) Success else Error,
                    fontSize = 12.sp
                )
            }
        }
    }
}