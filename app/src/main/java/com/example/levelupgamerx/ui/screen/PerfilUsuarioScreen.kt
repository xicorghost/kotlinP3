package com.example.levelupgamerx.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.repository.UsuarioRepository
import com.example.levelupgamerx.ui.component.*
import com.example.levelupgamerx.ui.theme.*
import com.example.levelupgamerx.ui.viewmodel.UsuarioViewModel
import com.example.levelupgamerx.ui.viewmodel.UsuarioViewModelFactory
import androidx.compose.foundation.BorderStroke // <-- ¡Esta es la importación que falta!

/**
 * Pantalla de perfil de usuario
 * Muestra: puntos, nivel, código de referido, estadísticas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioScreen(
    usuarioRepository: UsuarioRepository,
    preferenciasManager: PreferenciasManager,
    onBackClick: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val viewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(usuarioRepository, preferenciasManager)
    )
    val uiState by viewModel.uiState.collectAsState()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var mostrarMensaje by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MI PERFIL") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.cerrarSesion()
                        onCerrarSesion()
                    }) {
                        Icon(Icons.Default.Logout, "Cerrar Sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCard,
                    titleContentColor = NeonGreen,
                    navigationIconContentColor = NeonGreen,
                    actionIconContentColor = Error
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        if (uiState.usuarioActual == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay sesión activa", color = TextSecondary)
            }
        } else {
            val usuario = uiState.usuarioActual!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header con avatar y nombre
                CyberpunkCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = NeonGreen
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = usuario.nombre,
                                color = NeonGreen,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = usuario.email,
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                            if (usuario.esDuoc) {
                                DescuentoDuocBadge()
                            }
                        }
                    }
                }

                // Sistema de puntos y nivel
                CyberpunkCard {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "PROGRESO LEVELUP",
                            color = NeonGreen,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        PuntosNivelDisplay(
                            puntos = usuario.puntos,
                            nivel = usuario.nivel,
                            progresoNivel = usuario.progresoNivel()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Puntos para siguiente nivel:",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "${usuario.puntosParaSiguienteNivel()} puntos",
                                    color = CyberYellow,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Sistema de referidos
                CyberpunkCard {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = CyberPurple,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "PROGRAMA DE REFERIDOS",
                                color = NeonGreen,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Comparte tu código y gana 100 puntos por cada amigo que se registre",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )

                        // Código de referido
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = DarkBackground
                            ),
                            border = BorderStroke(2.dp, CyberPurple)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "TU CÓDIGO:",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = usuario.codigoReferido,
                                        color = CyberPurple,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 2.sp
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(usuario.codigoReferido))
                                        mostrarMensaje = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copiar",
                                        tint = CyberPurple
                                    )
                                }
                            }
                        }

                        // Estadísticas de referidos
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${usuario.cantidadReferidos}",
                                    color = Success,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Referidos",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${usuario.cantidadReferidos * 100}",
                                    color = CyberYellow,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Puntos ganados",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        if (mostrarMensaje) {
                            Text(
                                text = "✓ Código copiado al portapapeles",
                                color = Success
                            )
                            LaunchedEffect(Unit) {
                                kotlinx.coroutines.delay(2000)
                                mostrarMensaje = false
                            }
                        }
                    }
                }

                // Estadísticas generales
                CyberpunkCard {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "ESTADÍSTICAS",
                            color = NeonGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        StatItem(
                            icon = Icons.Default.ShoppingCart,
                            label = "Productos comprados",
                            value = "${usuario.productosComprados.size}"
                        )

                        StatItem(
                            icon = Icons.Default.Star,
                            label = "Reseñas dejadas",
                            value = "0" // Implementar contador real
                        )

                        StatItem(
                            icon = Icons.Default.CalendarMonth,
                            label = "Miembro desde",
                            value = java.text.SimpleDateFormat("dd/MM/yyyy")
                                .format(java.util.Date(usuario.fechaRegistro))
                        )
                    }
                }

                // Beneficios
                CyberpunkCard {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "TUS BENEFICIOS",
                            color = NeonGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        BenefitItem(
                            icon = "✓",
                            text = "Sistema de puntos LevelUp",
                            enabled = true
                        )

                        BenefitItem(
                            icon = "✓",
                            text = usuario.textoDescuento(),
                            enabled = usuario.esDuoc
                        )

                        BenefitItem(
                            icon = "✓",
                            text = "Código de referido único",
                            enabled = true
                        )

                        BenefitItem(
                            icon = "✓",
                            text = "Sistema de reseñas",
                            enabled = true
                        )
                    }
                }

                // Botón cerrar sesión
                CyberpunkButton(
                    text = "Cerrar Sesión",
                    onClick = {
                        viewModel.cerrarSesion()
                        onCerrarSesion()
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CyberBlue,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
        Text(
            text = value,
            color = NeonGreen,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BenefitItem(
    icon: String,
    text: String,
    enabled: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            color = if (enabled) Success else TextDisabled,
            fontSize = 16.sp
        )
        Text(
            text = text,
            color = if (enabled) TextPrimary else TextDisabled,
            fontSize = 14.sp
        )
    }
}