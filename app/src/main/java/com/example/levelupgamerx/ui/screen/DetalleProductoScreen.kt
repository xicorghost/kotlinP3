package com.example.levelupgamerx.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.levelupgamerx.data.repository.ResenaRepository
import com.example.levelupgamerx.data.repository.UsuarioRepository
import com.example.levelupgamerx.domain.model.Producto
import com.example.levelupgamerx.ui.component.*
import com.example.levelupgamerx.ui.theme.*
import com.example.levelupgamerx.ui.viewmodel.*
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke

/**
 * Pantalla de detalle de producto con reseñas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    productoRepository: ProductoRepositoryImpl,
    carritoRepository: CarritoRepository,
    resenaRepository: ResenaRepository,
    usuarioRepository: UsuarioRepository,
    preferenciasManager: PreferenciasManager,
    onVolverClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var producto by remember { mutableStateOf<Producto?>(null) }
    var estaCargando by remember { mutableStateOf(true) }
    var mostrarMensaje by remember { mutableStateOf(false) }
    var mensajeTexto by remember { mutableStateOf("") }
    var mostrarReseñas by remember { mutableStateOf(false) }

    val esDuoc = preferenciasManager.esUsuarioDuoc()
    val usuarioLogueado = preferenciasManager.estaUsuarioLogueado()

    // Cargar producto
    LaunchedEffect(productoId) {
        estaCargando = true
        producto = productoRepository.obtenerProductoPorId(productoId)
        estaCargando = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DETALLE DEL PRODUCTO") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                estaCargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = NeonGreen
                    )
                }
                producto == null -> {
                    Text(
                        text = "Producto no encontrado",
                        modifier = Modifier.align(Alignment.Center),
                        color = Error
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Imagen del producto
                        val imageResId = context.resources.getIdentifier(
                            producto!!.imagenUrl,
                            "drawable",
                            context.packageName
                        )

                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context)
                                    .data(if (imageResId != 0) imageResId else producto!!.imagenUrl)
                                    .crossfade(true)
                                    .build()
                            ),
                            contentDescription = producto!!.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Crop
                        )

                        // Información del producto
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Categoría
                            Text(
                                text = "[${producto!!.categoria.displayName}]",
                                color = CyberBlue,
                                fontSize = 14.sp
                            )

                            // Nombre
                            Text(
                                text = producto!!.nombre,
                                color = NeonGreen,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Calificación y reseñas
                            if (producto!!.numeroResenas > 0) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RatingStars(
                                        rating = producto!!.calificacionPromedio,
                                        size = 24.dp
                                    )
                                    Text(
                                        text = "${producto!!.calificacionPromedio}/5.0",
                                        color = CyberYellow,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "(${producto!!.numeroResenas} reseñas)",
                                        color = TextSecondary,
                                        fontSize = 14.sp
                                    )
                                }

                                // Botón ver reseñas
                                OutlinedButton(
                                    onClick = { mostrarReseñas = true },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = NeonGreen
                                    ),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        width = 2.dp
                                    )
                                ) {
                                    Text("VER TODAS LAS RESEÑAS")
                                }
                            }

                            CyberpunkDivider()

                            // Descripción
                            Text(
                                text = "DESCRIPCIÓN",
                                color = NeonGreen,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = producto!!.descripcion,
                                color = TextSecondary,
                                fontSize = 16.sp
                            )

                            CyberpunkDivider()

                            // Precio
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "PRECIO:",
                                        color = TextSecondary,
                                        fontSize = 14.sp
                                    )
                                    if (esDuoc) {
                                        Text(
                                            text = producto!!.precioFormateado(),
                                            color = TextDisabled,
                                            fontSize = 18.sp,
                                            style = LocalTextStyle.current.copy(
                                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                            )
                                        )
                                        Text(
                                            text = producto!!.precioConDescuentoFormateado(true),
                                            color = Success,
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        DescuentoDuocBadge()
                                    } else {
                                        Text(
                                            text = producto!!.precioFormateado(),
                                            color = NeonGreen,
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            // Stock
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Stock disponible:",
                                    color = TextSecondary
                                )
                                Text(
                                    text = "${producto!!.stock} unidades",
                                    color = if (producto!!.hayStock) Success else Error,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Botón agregar al carrito
                            CyberpunkButton(
                                text = "Agregar al Carrito",
                                onClick = {
                                    if (!usuarioLogueado) {
                                        mensajeTexto = "Debes iniciar sesión para comprar"
                                        mostrarMensaje = true
                                    } else {
                                        scope.launch {
                                            producto?.let {
                                                carritoRepository.agregarProducto(it)
                                                mensajeTexto = "✓ Producto agregado al carrito"
                                                mostrarMensaje = true
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = producto!!.hayStock
                            )

                            // Mensaje de confirmación
                            if (mostrarMensaje) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (mensajeTexto.contains("✓"))
                                            Success.copy(alpha = 0.2f)
                                        else
                                            Error.copy(alpha = 0.2f)
                                    )
                                ) {
                                    Text(
                                        text = mensajeTexto,
                                        color = if (mensajeTexto.contains("✓")) Success else Error,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                                LaunchedEffect(Unit) {
                                    kotlinx.coroutines.delay(3000)
                                    mostrarMensaje = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal de reseñas
    if (mostrarReseñas && producto != null) {
        ResenasModal(
            codigoProducto = producto!!.codigo,
            nombreProducto = producto!!.nombre,
            resenaRepository = resenaRepository,
            usuarioRepository = usuarioRepository,
            preferenciasManager = preferenciasManager,
            onDismiss = { mostrarReseñas = false }
        )
    }
}

/**
 * Modal de reseñas completo
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResenasModal(
    codigoProducto: String,
    nombreProducto: String,
    resenaRepository: ResenaRepository,
    usuarioRepository: UsuarioRepository,
    preferenciasManager: PreferenciasManager,
    onDismiss: () -> Unit
) {
    val viewModel: ResenaViewModel = viewModel(
        factory = ResenaViewModelFactory(resenaRepository, usuarioRepository, preferenciasManager)
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(codigoProducto) {
        viewModel.cargarResenas(codigoProducto)
    }

    // Cerrar modal al publicar exitosamente
    LaunchedEffect(uiState.resenaPublicada) {
        if (uiState.resenaPublicada) {
            kotlinx.coroutines.delay(2000)
            viewModel.limpiarEstado()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("CERRAR", color = NeonGreen)
            }
        },
        title = {
            Text(
                text = "RESEÑAS: $nombreProducto",
                color = NeonGreen
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Calificación promedio
                if (uiState.totalResenas > 0) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCard)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${uiState.calificacionPromedio}/5.0",
                                color = CyberYellow,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                            RatingStars(
                                rating = uiState.calificacionPromedio,
                                size = 28.dp
                            )
                            Text(
                                text = "Basado en ${uiState.totalResenas} reseñas",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                CyberpunkDivider()

                // Formulario para dejar reseña
                if (uiState.puedeResenar) {
                    Text(
                        text = "DEJA TU RESEÑA",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )

                    Text("Calificación:", color = TextSecondary)
                    RatingStars(
                        rating = uiState.calificacionSeleccionada.toFloat(),
                        onRatingChanged = { viewModel.actualizarCalificacion(it) }
                    )

                    OutlinedTextField(
                        value = uiState.comentario,
                        onValueChange = { viewModel.actualizarComentario(it) },
                        label = { Text("Tu opinión") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = NeonGreenDark,
                            focusedLabelColor = NeonGreen,
                            cursorColor = NeonGreen
                        )
                    )

                    CyberpunkButton(
                        text = "Publicar Reseña",
                        onClick = { viewModel.publicarResena() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.formularioValido,
                        loading = uiState.estaPublicando
                    )

                    if (uiState.resenaPublicada) {
                        Text(
                            text = "✓ Reseña publicada! +${uiState.puntosGanados} puntos LevelUp",
                            color = Success
                        )
                    }
                } else if (!preferenciasManager.estaUsuarioLogueado()) {
                    Text(
                        text = "Debes iniciar sesión y comprar el producto para dejar una reseña",
                        color = TextSecondary
                    )
                } else {
                    Text(
                        text = "Debes comprar este producto para poder reseñarlo",
                        color = TextSecondary
                    )
                }

                if (uiState.error != null) {
                    Text(
                        text = uiState.error!!,
                        color = Error
                    )
                }

                CyberpunkDivider()

                // Lista de reseñas
                Text(
                    text = "OPINIONES DE LA COMUNIDAD",
                    color = NeonGreen,
                    fontWeight = FontWeight.Bold
                )

                if (uiState.hayResenas) {
                    uiState.resenas.forEach { resena ->
                        ResenaCard(resena = resena)
                    }
                } else {
                    Text(
                        text = "Aún no hay reseñas. ¡Sé el primero!",
                        color = TextSecondary
                    )
                }
            }
        },
        containerColor = DarkSurface
    )
}

/**
 * Card individual de reseña
 */
@Composable
fun ResenaCard(resena: com.example.levelupgamerx.domain.model.Resena) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = resena.nombreUsuario,
                    color = NeonGreen,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = resena.fechaFormateada(),
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            RatingStars(
                rating = resena.calificacion.toFloat(),
                size = 16.dp
            )

            Text(
                text = resena.comentario,
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}