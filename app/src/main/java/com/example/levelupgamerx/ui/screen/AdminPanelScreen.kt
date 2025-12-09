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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.ui.component.*
import com.example.levelupgamerx.ui.theme.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

/**
 * Panel de administración actualizado con diseño cyberpunk
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    productos: List<Producto>,
    usernameAdmin: String,
    onAgregarProducto: () -> Unit,
    onEditarProducto: (Producto) -> Unit,
    onEliminarProducto: (Producto) -> Unit,
    // Y en la función AdminPanelScreen agregar el parámetro:
    onExplorarAPI: () -> Unit,
    onCerrarSesion: () -> Unit


) {
    var tabSeleccionada by remember { mutableStateOf(0) }
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                // En el Scaffold, actions de TopAppBar:

                title = {
                    Column {
                        Text("PANEL ADMIN")
                        Text(
                            text = usernameAdmin,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onExplorarAPI) {
                        Icon(Icons.Default.CloudDownload, "API", tint = CyberBlue)
                    }
                    IconButton(onClick = onCerrarSesion) {
                        Icon(
                            Icons.Default.Logout,
                            "Cerrar Sesión",
                            tint = Error
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCard,
                    titleContentColor = CyberPurple,
                    actionIconContentColor = Error
                )
            )
        },
        floatingActionButton = {
            if (tabSeleccionada == 0) {
                FloatingActionButton(
                    onClick = onAgregarProducto,
                    containerColor = NeonGreen,
                    contentColor = DarkBackground
                ) {
                    Icon(Icons.Default.Add, "Agregar")
                }
            }
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs
            /*TabRow(
                selectedTabIndex = tabSeleccionada,
                containerColor = DarkCard,
                contentColor = NeonGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[tabSeleccionada]),
                        color = NeonGreen
                    )
                }
            ) {
                Tab(
                    selected = tabSeleccionada == 0,
                    onClick = { tabSeleccionada = 0 },
                    text = { Text("PRODUCTOS") }
                )
                Tab(
                    selected = tabSeleccionada == 1,
                    onClick = { tabSeleccionada = 1 },
                    text = { Text("ESTADÍSTICAS") }
                )
            }*/
            // Tabs
            TabRow(
                selectedTabIndex = tabSeleccionada,
                containerColor = DarkCard,
                contentColor = NeonGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[tabSeleccionada]),
                        color = NeonGreen
                    )
                }
            ) {
                Tab(
                    selected = tabSeleccionada == 0,
                    onClick = { tabSeleccionada = 0 },
                    text = { Text("PRODUCTOS") }
                )
                Tab(
                    selected = tabSeleccionada == 1,
                    onClick = { tabSeleccionada = 1 },
                    text = { Text("ESTADÍSTICAS") }
                )
            }

            when (tabSeleccionada) {
                0 -> {
                    // Lista de productos
                    if (productos.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Inventory,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = TextDisabled
                                )
                                Text(
                                    text = "No hay productos",
                                    color = TextSecondary
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(productos) { producto ->
                                AdminProductoCardCyberpunk(
                                    producto = producto,
                                    onEditar = { onEditarProducto(producto) },
                                    onEliminar = {
                                        productoAEliminar = producto
                                        mostrarDialogoEliminar = true
                                    }
                                )
                            }
                        }
                    }
                }
                1 -> {
                    // Estadísticas
                    EstadisticasAdminPanel(productos = productos)
                }
            }
        }
    }

    // Diálogo de confirmación de eliminación
    if (mostrarDialogoEliminar && productoAEliminar != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoEliminar = false
                productoAEliminar = null
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEliminarProducto(productoAEliminar!!)
                        mostrarDialogoEliminar = false
                        productoAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Error)
                ) {
                    Text("ELIMINAR")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        productoAEliminar = null
                    }
                ) {
                    Text("CANCELAR", color = TextSecondary)
                }
            },
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = Warning
                    )
                    Text("CONFIRMAR ELIMINACIÓN", color = Warning)
                }
            },
            text = {
                Text(
                    text = "¿Estás seguro de eliminar \"${productoAEliminar?.nombre}\"?\nEsta acción no se puede deshacer.",
                    color = TextPrimary
                )
            },
            containerColor = DarkSurface
        )
    }
}


/**
 * Card de producto para admin
 */
@Composable
fun AdminProductoCardCyberpunk(
    producto: Producto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(
        producto.imagenUrl,
        "drawable",
        context.packageName
    )

    CyberpunkCard {
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
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            // Información
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "[${producto.codigo}]",
                    color = CyberBlue,
                    fontSize = 12.sp
                )
                Text(
                    text = producto.nombre,
                    color = NeonGreen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Categoría: ${producto.categoria.displayName}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = "Stock: ${producto.stock}",
                    color = if (producto.hayStock) Success else Error,
                    fontSize = 12.sp
                )
                Text(
                    text = producto.precioFormateado(),
                    color = CyberYellow,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Botones
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onEditar) {
                    Icon(
                        Icons.Default.Edit,
                        "Editar",
                        tint = CyberBlue
                    )
                }
                IconButton(onClick = onEliminar) {
                    Icon(
                        Icons.Default.Delete,
                        "Eliminar",
                        tint = Error
                    )
                }
            }
        }
    }
}

/**
 * Panel de estadísticas
 */
@Composable
fun EstadisticasAdminPanel(productos: List<Producto>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Resumen general
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total productos
                CyberpunkCard(modifier = Modifier.weight(1f)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Inventory,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${productos.size}",
                            color = NeonGreen,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Productos",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                // Stock total
                CyberpunkCard(modifier = Modifier.weight(1f)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Storage,
                            contentDescription = null,
                            tint = CyberBlue,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${productos.sumOf { it.stock }}",
                            color = CyberBlue,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Stock Total",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Valor inventario
        item {
            CyberpunkCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "VALOR INVENTARIO",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        val valorTotal = productos.sumOf { it.precio * it.stock }
                        Text(
                            text = "$${valorTotal.toInt().toString().reversed().chunked(3).joinToString(".").reversed()}",
                            color = CyberYellow,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Icon(
                        Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = CyberYellow,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        // Por categoría
        item {
            Text(
                text = "ESTADÍSTICAS POR CATEGORÍA",
                color = NeonGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        val categorias = productos.groupBy { it.categoria }
        items(categorias.entries.toList()) { (categoria, lista) ->
            CyberpunkCard {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = categoria.displayName,
                        color = NeonGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Productos: ${lista.size}",
                        color = TextSecondary
                    )
                    Text(
                        text = "Stock: ${lista.sumOf { it.stock }}",
                        color = TextSecondary
                    )
                    val valorCategoria = lista.sumOf { it.precio * it.stock }
                    Text(
                        text = "Valor: $${valorCategoria.toInt().toString().reversed().chunked(3).joinToString(".").reversed()}",
                        color = CyberYellow,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}