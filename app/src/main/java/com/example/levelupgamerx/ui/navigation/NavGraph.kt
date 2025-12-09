package com.example.levelupgamerx.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.levelupgamerx.data.local.AppDatabase
import com.example.levelupgamerx.data.local.PreferenciasManager
import com.example.levelupgamerx.data.repository.*
import com.example.levelupgamerx.navigation.Rutas
import com.example.levelupgamerx.ui.screen.*
import com.example.levelupgamerx.ui.viewmodel.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
//import androidx.compose.runtime.collectAsState


/**
 * NavGraph principal actualizado con todas las funcionalidades
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    database: AppDatabase,
    preferenciasManager: PreferenciasManager,
    modifier: Modifier = Modifier
) {
    // Inicializar repositorios
    val productoRepository = ProductoRepositoryImpl(database.productoDao())
    val carritoRepository = CarritoRepository(database.carritoDao())
    val usuarioRepository = UsuarioRepository(database.usuarioDao())
    val resenaRepository = ResenaRepository(database.resenaDao(), database.productoDao())
    val productoRemotoRepository = ProductoRemotoRepository() // Repositorio API
    val productoLocalRepository = ProductoLocalRepository(database.productoDao())

    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH,
        modifier = modifier
    ) {
        // ==================== SPLASH SCREEN ====================
        composable(route = Rutas.SPLASH) {
            SplashScreen(navController = navController)
        }

        // ==================== PORTADA ====================
        composable(route = Rutas.PORTADA) {
            PortadaScreen(
                onEntrarClick = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.PORTADA) { inclusive = true }
                    }
                },
                onAdminClick = {
                    navController.navigate(Rutas.LOGIN_ADMIN)
                }
            )
        }

        // ==================== HOME ====================
        composable(route = Rutas.HOME) {
            HomeScreen(
                productoRepository = productoRepository,
                carritoRepository = carritoRepository,
                preferenciasManager = preferenciasManager,
                onProductoClick = { id ->
                    navController.navigate(Rutas.detalleConId(id))
                },
                onCarritoClick = {
                    navController.navigate(Rutas.CARRITO)
                },
                onPerfilClick = {
                    // Si está logueado va a perfil, sino a login
                    if (preferenciasManager.estaUsuarioLogueado()) {
                        navController.navigate(Rutas.PERFIL)
                    } else {
                        navController.navigate(Rutas.LOGIN_USUARIO)
                    }
                },
                onVolverPortada = {
                    navController.navigate(Rutas.PORTADA) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            )
        }

        // ==================== DETALLE PRODUCTO ====================
        composable(
            route = Rutas.DETALLE_BASE,
            arguments = listOf(
                navArgument("productoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0

            DetalleProductoScreen(
                productoId = productoId,
                productoRepository = productoRepository,
                carritoRepository = carritoRepository,
                resenaRepository = resenaRepository,
                usuarioRepository = usuarioRepository,
                preferenciasManager = preferenciasManager,
                onVolverClick = { navController.popBackStack() }
            )
        }

        // ==================== CARRITO ====================
        composable(route = Rutas.CARRITO) {
            CarritoScreen(
                carritoRepository = carritoRepository,
                usuarioRepository = usuarioRepository,
                preferenciasManager = preferenciasManager,
                onBackClick = { navController.popBackStack() },
                onCompraExitosa = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.CARRITO) { inclusive = true }
                    }
                }
            )
        }

        // ==================== LOGIN USUARIO ====================
        composable(route = Rutas.LOGIN_USUARIO) {
            LoginUsuarioScreen(
                usuarioRepository = usuarioRepository,
                preferenciasManager = preferenciasManager,
                onLoginExitoso = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.LOGIN_USUARIO) { inclusive = true }
                    }
                },
                onRegistrarseClick = {
                    navController.navigate(Rutas.REGISTRO)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // ==================== REGISTRO USUARIO ====================
        composable(route = Rutas.REGISTRO) {
            RegistroScreen(
                usuarioRepository = usuarioRepository,
                preferenciasManager = preferenciasManager,
                onRegistroExitoso = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.REGISTRO) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // ==================== PERFIL USUARIO ====================
        composable(route = Rutas.PERFIL) {
            PerfilUsuarioScreen(
                usuarioRepository = usuarioRepository,
                preferenciasManager = preferenciasManager,
                onBackClick = { navController.popBackStack() },
                onCerrarSesion = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.PERFIL) { inclusive = true }
                    }
                }
            )
        }

        // ... dentro de NavHost { ... }

// ==================== PERFIL USUARIO ====================
        composable(route = Rutas.PERFIL) {
            // 🛑 CAMBIO: Usar la pantalla PerfilConCamaraScreen con las dependencias
            PerfilConCamaraScreen(
                usuarioRepository = usuarioRepository,
                preferenciasManager = preferenciasManager,
                onBackClick = { navController.popBackStack() },
                onCerrarSesion = {
                    // Lógica para cerrar la sesión y navegar al inicio (Portada o Login)
                    navController.navigate(Rutas.PORTADA) { // O Rutas.LOGIN_USUARIO
                        popUpTo(Rutas.PERFIL) { inclusive = true }
                    }
                }
            )
        }

        // ==================== LOGIN ADMIN ====================
        composable(route = Rutas.LOGIN_ADMIN) {
            LoginAdminScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.PANEL_ADMIN) {
                        popUpTo(Rutas.LOGIN_ADMIN) { inclusive = true }
                    }
                },
                onVolverClick = { navController.popBackStack() },
                onValidarCredenciales = { user, pass ->
                    preferenciasManager.validarCredencialesAdmin(user, pass)
                },
                onGuardarSesion = { username ->
                    preferenciasManager.guardarSesionAdmin(username)
                }
            )
        }

        // ==================== PANEL ADMIN ====================
        composable(route = Rutas.PANEL_ADMIN) {
            val username = preferenciasManager.obtenerUsernameAdmin() ?: "Admin"

            val viewModel: ProductoViewModel = viewModel(
                factory = ProductoViewModelFactory(productoRepository)
            )

            //val uiState = androidx.compose.runtime.collectAsState(viewModel.uiState).value
            val uiState by viewModel.uiState.collectAsState()

            AdminPanelScreen(
                productos = uiState.productos,
                usernameAdmin = username,
                onAgregarProducto = {
                    navController.navigate(Rutas.formularioNuevo())
                },
                onEditarProducto = { producto ->
                    navController.navigate(Rutas.formularioEditar(producto.id))
                },
                onEliminarProducto = { producto ->
                    viewModel.eliminarProducto(producto)
                },
                onExplorarAPI = {        // 🔥 AGREGA ESTO
                    navController.navigate(Rutas.PRODUCTOS_API)
                },
                onCerrarSesion = {
                    preferenciasManager.cerrarSesionAdmin()
                    navController.navigate(Rutas.PORTADA) {
                        popUpTo(Rutas.PANEL_ADMIN) { inclusive = true }
                    }
                }
            )
        }

        // ==================== FORMULARIO PRODUCTO ====================
        composable(
            route = Rutas.FORMULARIO_PRODUCTO_BASE,
            arguments = listOf(
                navArgument("productoId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: -1

            FormularioProductoScreen(
                productoId = productoId,
                productoRepository = productoRepository,
                onBackClick = { navController.popBackStack() }
            )


        }
        // ==================== EXPLORAR PRODUCTOS API (CORREGIDO) ====================
        composable(route = Rutas.PRODUCTOS_API) {
            ProductosApiScreen(
                // 🛑 CORRECCIÓN: Usar el repositorio Remoto para la API
                productoRepository = productoRemotoRepository,
                // 🛑 CORRECCIÓN: Pasar el repositorio Local (lo requiere el ViewModelFactory)
                localRepository = productoLocalRepository,

                onBackClick = { navController.popBackStack() },
                onProductoClick = { producto ->
                    // Aquí podrías navegar al detalle de la API si tuvieras una ruta para ello
                    // navController.navigate(Rutas.detalleConId(producto.id))
                }
            )
        }
    }

}
