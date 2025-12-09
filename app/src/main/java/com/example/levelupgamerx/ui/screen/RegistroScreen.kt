package com.example.levelupgamerx.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.repository.UsuarioRepository
import com.example.levelupgamerx.domain.model.RegistroUsuario
import com.example.levelupgamerx.ui.component.*
import com.example.levelupgamerx.ui.theme.*
import com.example.levelupgamerx.ui.viewmodel.UsuarioViewModel
import com.example.levelupgamerx.ui.viewmodel.UsuarioViewModelFactory
import androidx.compose.foundation.BorderStroke // <-- ¡Esta es la importación que falta!
import androidx.compose.ui.unit.sp // <-- ¡Añade esta línea!
import androidx.compose.ui.text.font.FontWeight // <-- ¡Añade esta línea!

/**
 * Pantalla de registro de usuarios
 * Incluye: validación, código de referido, detección DUOC
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    usuarioRepository: UsuarioRepository,
    preferenciasManager: PreferenciasManager,
    onRegistroExitoso: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(usuarioRepository, preferenciasManager)
    )
    val uiState by viewModel.uiState.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var codigoReferido by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarConfirmar by remember { mutableStateOf(false) }

    // Detectar email DUOC
    val esDuoc = email.lowercase().contains("duoc")

    // Manejar registro exitoso
    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            onRegistroExitoso()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("REGISTRARSE") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            CyberpunkHeader(
                title = "Nueva Cuenta",
                subtitle = "Únete a la comunidad Level-Up Gamer"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Formulario
            CyberpunkTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = "Nombre Completo"
            )

            CyberpunkTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )

            // Mostrar badge DUOC si aplica
            if (esDuoc) {
                DescuentoDuocBadge(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            CyberpunkTextField(
                value = edad,
                onValueChange = { if (it.all { char -> char.isDigit() }) edad = it },
                label = "Edad"
            )

            // Contraseña
            CyberpunkTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = "Contraseña"
            )

            CyberpunkTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                label = "Confirmar Contraseña"
            )

            CyberpunkDivider()

            // Código de referido (opcional)
            CyberpunkTextField(
                value = codigoReferido,
                onValueChange = { codigoReferido = it.uppercase() },
                label = "Código de Referido (Opcional)"
            )

            Text(
                text = "Si tienes un código de referido, ganas 100 puntos bonus",
                color = TextSecondary,
                fontSize = 12.sp
            )

            // Mensaje de error
            if (uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Error.copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, Error)
                ) {
                    Text(
                        text = uiState.error!!,
                        color = Error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de registro
            CyberpunkButton(
                text = "Crear Cuenta",
                onClick = {
                    val registro = RegistroUsuario(
                        nombre = nombre,
                        email = email,
                        edad = edad.toIntOrNull() ?: 0,
                        contrasena = contrasena,
                        codigoReferido = codigoReferido.ifBlank { null }
                    )
                    viewModel.registrarUsuario(registro)
                },
                modifier = Modifier.fillMaxWidth(),
                loading = uiState.estaCargando
            )

            // Información
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Beneficios al registrarte:",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Text("• Sistema de puntos LevelUp", color = TextSecondary)
                    Text("• Descuento 20% con email @duoc.cl", color = TextSecondary)
                    Text("• Código de referido único", color = TextSecondary)
                    Text("• Dejar reseñas en productos", color = TextSecondary)
                }
            }
        }
    }
}