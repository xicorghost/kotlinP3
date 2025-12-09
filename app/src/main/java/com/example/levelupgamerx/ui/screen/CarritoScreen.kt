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
import coil.request.ImageRequest
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.repository.CarritoRepository
import com.example.levelupgamerx.data.repository.UsuarioRepository
import com.example.levelupgamerx.ui.component.*
import com.example.levelupgamerx.ui.theme.*
import com.example.levelupgamerx.ui.viewmodel.CarritoViewModel
import com.example.levelupgamerx.ui.viewmodel.CarritoViewModelFactory
import androidx.compose.foundation.BorderStroke

/**
 * Pantalla de carrito con descuentos DUOC y puntos LevelUp
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    carritoRepository: CarritoRepository,
    usuarioRepository: UsuarioRepository,
    preferenciasManager: PreferenciasManager,
    onBackClick: () -> Unit,
    onCompraExitosa: () -> Unit
) {
    val viewModel: CarritoViewModel = viewModel(
        factory = CarritoViewModelFactory(carritoRepository, usuarioRepository, preferenciasManager)
    )
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Manejar compra exitosa
    LaunchedEffect(uiState.compraExitosa) {
        if (uiState.compraExitosa) {
            kotlinx.coroutines.delay(2000)
            onCompraExitosa()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("MI CARRITO")
                        if (uiState.cantidadItems > 0) {
                            Text(
                                text = "${uiState.cantidadItems} ${if (uiState.cantidadItems == 1) "producto" else "productos"}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    if (!uiState.estaVacio) {
                        IconButton(onClick = { viewModel.vaciarCarrito() }) {
                            Icon(Icons.Default.DeleteSweep, "Vaciar", tint = Error)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCard,
                    titleContentColor = NeonGreen,
                    navigationIconContentColor = NeonGreen
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        if (uiState.estaVacio) {
            // Carrito vacío
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = TextDisabled
                    )
                    Text(
                        text = "Tu carrito está vacío",
                        color = TextSecondary,
                        fontSize = 18.sp
                    )
                    CyberpunkButton(
                        text = "Ir a comprar",
                        onClick = onBackClick
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Lista de productos
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.items) { item ->
                        CarritoItemCardCyberpunk(
                            item = item,
                            onIncrementar = { viewModel.incrementarCantidad(item.producto.id) },
                            onDecrementar = { viewModel.decrementarCantidad(item.producto.id) },
                            onEliminar = { viewModel.eliminarProducto(item.producto.id) }
                        )
                    }
                }

                // Resumen de compra
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "RESUMEN DE COMPRA",
                            color = NeonGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        CyberpunkDivider()

                        // Subtotal
                        if (uiState.descuentoAplicado) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Subtotal:", color = TextSecondary)
                                Text(
                                    text = "$${uiState.totalSinDescuento.toInt().toString().reversed().chunked(3).joinToString(".").reversed()}",
                                    color = TextDisabled,
                                    style = LocalTextStyle.current.copy(
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                )
                            }

                            // Descuento
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Descuento DUOC 20%:", color = Success)
                                    Icon(
                                        imageVector = Icons.Default.Discount,
                                        contentDescription = null,
                                        tint = Success,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "-$${uiState.ahorro.toInt().toString().reversed().chunked(3).joinToString(".").reversed()}",
                                    color = Success,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL:",
                                color = NeonGreen,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$${uiState.total.toInt().toString().reversed().chunked(3).joinToString(".").reversed()}",
                                color = NeonGreen,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Puntos que ganará
                        val puntosGanados = (uiState.total / 1000).toInt()
                        if (puntosGanados > 0) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = CyberYellow.copy(alpha = 0.2f)
                                ),
                                border = BorderStroke(1.dp, CyberYellow)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Stars,
                                        contentDescription = null,
                                        tint = CyberYellow,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Ganarás $puntosGanados puntos LevelUp con esta compra",
                                        color = CyberYellow,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        CyberpunkDivider()

                        // Botón comprar
                        CyberpunkButton(
                            text = "Finalizar Compra",
                            onClick = { viewModel.procesarCompra() },
                            modifier = Modifier.fillMaxWidth(),
                            loading = uiState.estaProcesando
                        )

                        // Mensaje de error
                        if (uiState.error != null) {
                            Text(
                                text = uiState.error!!,
                                color = Error,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Diálogo de compra exitosa
        if (uiState.compraExitosa) {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    CyberpunkButton(
                        text = "Continuar",
                        onClick = {
                            viewModel.limpiarEstado()
                            onCompraExitosa()
                        }
                    )
                },
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Success,
                            modifier = Modifier.size(32.dp)
                        )
                        Text("¡COMPRA EXITOSA!", color = Success)
                    }
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Tu pedido ha sido procesado correctamente",
                            color = TextPrimary
                        )
                        if (uiState.puntosGanados > 0) {
                            Text(
                                text = "✓ Has ganado ${uiState.puntosGanados} puntos LevelUp",
                                color = CyberYellow,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Ahora puedes dejar reseñas de los productos comprados",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                },
                containerColor = DarkSurface
            )
        }
    }
}

/**
 * Card de item del carrito con diseño cyberpunk
 */
@Composable
fun CarritoItemCardCyberpunk(
    item: com.example.levelupgamerx.domain.model.ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onEliminar: () -> Unit
) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(
        item.producto.imagenUrl,
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
                        .data(if (imageResId != 0) imageResId else item.producto.imagenUrl)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = item.producto.nombre,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            // Información
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.producto.nombre,
                    color = NeonGreen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Precio unit.: ${item.producto.precioFormateado()}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Text(
                    text = "Subtotal: $${item.subtotal.toInt().toString().reversed().chunked(3).joinToString(".").reversed()}",
                    color = CyberYellow,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Controles
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onIncrementar,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Aumentar",
                        tint = Success
                    )
                }

                Text(
                    text = "${item.cantidad}",
                    color = NeonGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onDecrementar,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircle,
                        contentDescription = "Disminuir",
                        tint = Warning
                    )
                }

                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Error
                    )
                }
            }
        }
    }
}