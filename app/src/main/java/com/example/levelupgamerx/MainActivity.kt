package com.example.levelupgamerx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.levelupgamerx.ui.theme.LevelUpGamerTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.levelupgamerx.data.local.AppDatabase
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.local.ProductoInicializador
import com.example.levelupgamerx.ui.navigation.NavGraph
import com.example.levelupgamerx.ui.theme.LevelUpGamerTheme

/**
 * MainActivity principal de Level-Up Gamer
 * Punto de entrada de la aplicación
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar base de datos
        val database = AppDatabase.getDatabase(applicationContext)

        // Inicializar preferencias
        val preferenciasManager = PreferenciasManager(applicationContext)

        // Cargar productos y reseñas iniciales
        ProductoInicializador.inicializarProductos(applicationContext)

        setContent {
            LevelUpGamerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavGraph(
                        navController = navController,
                        database = database,
                        preferenciasManager = preferenciasManager
                    )
                }
            }
        }
    }
}