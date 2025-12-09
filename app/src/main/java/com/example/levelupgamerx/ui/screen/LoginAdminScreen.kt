package com.example.levelupgamerx.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelupgamerx.ui.component.*
import com.example.levelupgamerx.ui.theme.*
import androidx.compose.foundation.BorderStroke // <-- ¡Esta es la importación que falta!

/**
 * Login Admin actualizado con diseño cyberpunk
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAdminScreen(
    onLoginExitoso: () -> Unit,
    onVolverClick: () -> Unit,
    onValidarCredenciales: (String, String) -> Boolean,
    onGuardarSesion: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mostrarPassword by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ACCESO ADMINISTRADOR") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCard,
                    titleContentColor = CyberPurple,
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono
            Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = CyberPurple
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "PANEL DE ADMINISTRACIÓN",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CyberPurple,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Usuario
            CyberpunkTextField(
                value = username,
                onValueChange = {
                    username = it
                    mensajeError = null
                },
                label = "Usuario",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    mensajeError = null
                },
                label = { Text("Contraseña") },
                visualTransformation = if (mostrarPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                        Icon(
                            if (mostrarPassword) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            "Toggle password",
                            tint = NeonGreen
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextSecondary,
                    focusedBorderColor = CyberPurple,
                    unfocusedBorderColor = NeonGreenDark,
                    focusedLabelColor = CyberPurple,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = CyberPurple
                )
            )

            // Error
            if (mensajeError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Error.copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, Error)
                ) {
                    Text(
                        text = mensajeError!!,
                        color = Error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón login
            Button(
                onClick = {
                    when {
                        username.isBlank() || password.isBlank() -> {
                            mensajeError = "Completa todos los campos"
                        }
                        onValidarCredenciales(username, password) -> {
                            onGuardarSesion(username)
                            onLoginExitoso()
                        }
                        else -> {
                            mensajeError = "Credenciales incorrectas"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyberPurple,
                    contentColor = DarkBackground
                )
            ) {
                Text(
                    text = "INICIAR SESIÓN",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Credenciales por defecto
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, CyberPurple.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Credenciales de prueba:",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Usuario: admin",
                        color = CyberPurple,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Contraseña: admin123",
                        color = CyberPurple,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}