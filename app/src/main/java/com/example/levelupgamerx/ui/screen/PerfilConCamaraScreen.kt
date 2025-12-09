package com.example.levelupgamerx.ui.screen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.repository.UsuarioRepository
import com.example.levelupgamerx.ui.component.CyberpunkButton
import com.example.levelupgamerx.ui.component.CyberpunkCard
import com.example.levelupgamerx.ui.theme.* // Asegúrate de que NeonGreen, CyberBlue, etc. estén definidos aquí
import com.example.levelupgamerx.domain.model.Usuario // Asegúrate de que esta sea la importación correcta
import com.example.levelupgamerx.util.rememberCameraLauncher // Asegúrate de que esta función exista
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilConCamaraScreen(
    usuarioRepository: UsuarioRepository,
    preferenciasManager: PreferenciasManager,
    onBackClick: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Usamos el modelo Usuario que incluye 'fotoPerfil' y 'nombre'
    var usuario by remember { mutableStateOf<Usuario?>(null) }
    var rutaFotoPerfil by remember { mutableStateOf<String?>(null) }
    var mostrarDialogoFoto by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    // Launcher de cámara
    val (lanzarCamara, _) = rememberCameraLauncher(
        onFotoTomada = { ruta ->
            rutaFotoPerfil = ruta

            // Guardar en preferencias
            preferenciasManager.guardarFotoPerfil(ruta)

            scope.launch {
                usuario?.let {
                    // Ahora 'fotoPerfil' debe existir en el modelo Usuario
                    usuarioRepository.actualizarUsuario(it.copy(fotoPerfil = ruta))
                }
            }

            mostrarDialogoFoto = false
        },
        onError = { error ->
            mensajeError = error
        }
    )

    // Cargar usuario
    LaunchedEffect(Unit) {
        val id = preferenciasManager.obtenerUsuarioId() // Corregido a obtenerUsuarioId()
        if (id != null) {
            // CORRECCIÓN: Se usa obtenerPorId(), tal como está en el UsuarioRepository
            usuario = usuarioRepository.obtenerPorId(id)
            rutaFotoPerfil = usuario?.fotoPerfil ?: preferenciasManager.obtenerFotoPerfil()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MI PERFIL") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCard,
                    titleContentColor = NeonGreen
                )
            )
        },
        containerColor = DarkBackground
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // FOTO DE PERFIL
            Box(contentAlignment = Alignment.BottomEnd) {

                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(DarkCard)
                        .border(3.dp, NeonGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val path = rutaFotoPerfil
                    if (path != null) {
                        // Usamos remember para evitar recalcular
                        val bitmap = remember(path) { BitmapFactory.decodeFile(path) }
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            IconoPerfilDefault()
                        }
                    } else {
                        IconoPerfilDefault()
                    }
                }

                FloatingActionButton(
                    onClick = { mostrarDialogoFoto = true },
                    modifier = Modifier.size(48.dp),
                    containerColor = CyberBlue
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cambiar foto", tint = DarkBackground)
                }
            }

            // INFORMACIÓN DEL USUARIO
            CyberpunkCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // CORRECCIÓN: Usamos 'nombre' en lugar de 'username'
                    InfoRow(Icons.Default.Person, "Usuario", usuario?.nombre ?: "---")
                    InfoRow(Icons.Default.Email, "Email", usuario?.email ?: "---")
                    InfoRow(Icons.Default.Star, "Puntos LevelUp", "${usuario?.puntos ?: 0} pts")

                    if (usuario?.esDuoc == true) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, null, tint = Success, modifier = Modifier.size(20.dp))
                            Text(
                                text = "Miembro DUOC UC - 20% descuento",
                                color = Success,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ERROR
            mensajeError?.let {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Error.copy(alpha = 0.2f))
                ) {
                    Text(text = it, color = Error, modifier = Modifier.padding(12.dp))
                }
            }

            // CERRAR SESIÓN
            CyberpunkButton(
                text = "CERRAR SESIÓN",
                onClick = {
                    preferenciasManager.cerrarSesionUsuario()
                    onCerrarSesion()
                },
                modifier = Modifier.fillMaxWidth(),
                // CORRECCIÓN: Asumimos que el parámetro es 'color'
                buttonColor = Error
            )
        }
    }

    // DIÁLOGO DE FOTO
    if (mostrarDialogoFoto) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoFoto = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CameraAlt, null, tint = CyberBlue)
                    Spacer(Modifier.width(8.dp))
                    Text("Cambiar foto de perfil", color = NeonGreen)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    Button(
                        onClick = { lanzarCamara() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberBlue)
                    ) {
                        Icon(Icons.Default.CameraAlt, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Tomar foto")
                    }

                    if (rutaFotoPerfil != null) {
                        OutlinedButton(
                            onClick = {
                                rutaFotoPerfil = null
                                preferenciasManager.eliminarFotoPerfil()

                                scope.launch {
                                    usuario?.let {
                                        // Ahora 'fotoPerfil' existe en el modelo Usuario
                                        usuarioRepository.actualizarUsuario(it.copy(fotoPerfil = null))
                                    }
                                }

                                mostrarDialogoFoto = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Delete, null, tint = Error)
                            Spacer(Modifier.width(8.dp))
                            Text("Eliminar foto", color = Error)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoFoto = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = DarkSurface
        )
    }
}

@Composable
private fun IconoPerfilDefault() {
    Icon(
        Icons.Default.Person,
        contentDescription = "Sin foto",
        tint = TextDisabled,
        modifier = Modifier.size(80.dp)
    )
}

@Composable
private fun InfoRow(
    icono: ImageVector,
    label: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, null, tint = CyberBlue, modifier = Modifier.size(24.dp))
        Column {
            Text(label, color = TextSecondary, fontSize = 12.sp)
            Text(valor, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}