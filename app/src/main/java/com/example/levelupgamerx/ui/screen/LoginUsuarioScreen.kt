package com.example.levelupgamerx.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.repository.UsuarioRepository
import com.example.levelupgamerx.domain.model.LoginUsuario
import com.example.levelupgamerx.ui.component.*
import com.example.levelupgamerx.ui.theme.*
import com.example.levelupgamerx.ui.viewmodel.UsuarioViewModel
import com.example.levelupgamerx.ui.viewmodel.UsuarioViewModelFactory

/**
 * Pantalla de login para usuarios normales
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUsuarioScreen(
    usuarioRepository: UsuarioRepository,
    preferenciasManager: PreferenciasManager,
    onLoginExitoso: () -> Unit,
    onRegistrarseClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(usuarioRepository, preferenciasManager)
    )
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // Manejar login exitoso
    LaunchedEffect(uiState.loginExitoso) {
        if (uiState.loginExitoso) {
            onLoginExitoso()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("INICIAR SESIÓN") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono
            Icon(
                imageVector = Icons.Default.Login,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = NeonGreen
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "BIENVENIDO DE VUELTA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NeonGreen,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Formulario
            CyberpunkTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            CyberpunkTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = "Contraseña",
                modifier = Modifier.fillMaxWidth()
            )

            // Error
            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.error!!,
                    color = Error,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón login
            CyberpunkButton(
                text = "Iniciar Sesión",
                onClick = {
                    viewModel.loginUsuario(LoginUsuario(email, contrasena))
                },
                modifier = Modifier.fillMaxWidth(),
                loading = uiState.estaCargando
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón registrarse
            TextButton(onClick = onRegistrarseClick) {
                Text(
                    text = "¿No tienes cuenta? REGÍSTRATE",
                    color = CyberBlue
                )
            }
        }
    }
}