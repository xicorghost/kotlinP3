package com.example.levelupgamerx.domain.model

/**
 * Modelo de Producto actualizado con datos de la web
 * Incluye sistema de reseñas y calificaciones
 */
data class Producto(
    val id: Int = 0,
    val codigo: String, // Código único del producto (ej: LL001, JM002)
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String,
    val categoria: CategoriaProducto,
    val stock: Int,

    // Sistema de reseñas
    val calificacionPromedio: Float = 0f,
    val numeroResenas: Int = 0
) {
    /** Precio formateado con separadores de miles */
    fun precioFormateado(): String {
        val precioEntero = precio.toInt()
        return "$${precioEntero.toString().reversed().chunked(3).joinToString(".").reversed()}"
    }

    /** Calcula el precio con descuento DUOC */
    fun precioConDescuento(esDuoc: Boolean): Double {
        return if (esDuoc) precio * 0.8 else precio
    }

    /** Precio con descuento formateado */
    fun precioConDescuentoFormateado(esDuoc: Boolean): String {
        val precioFinal = precioConDescuento(esDuoc).toInt()
        return "$${precioFinal.toString().reversed().chunked(3).joinToString(".").reversed()}"
    }

    /** True si hay stock disponible */
    val hayStock: Boolean
        get() = stock > 0

    /** Estrellas de calificación en texto */
    fun estrellasTexto(): String {
        val estrellas = calificacionPromedio.toInt()
        return "★".repeat(estrellas) + "☆".repeat(5 - estrellas)
    }
}

/**
 * Categorías de productos (adaptadas de la web)
 */
enum class CategoriaProducto(val displayName: String) {
    JUEGOS_MESA("Juegos de Mesa"),
    CLAVES_STEAM("Claves Steam"),
    ACCESORIOS("Accesorios"),
    CONSOLAS("Consolas"),
    COMPUTADORES("Computadores Gamers"),
    SILLAS_GAMER("Sillas Gamers"),
    MOUSE("Mouse"),
    MOUSEPAD("Mousepad"),
    POLERAS("Poleras Personalizadas"),

    OTRO("Otro Producto");

    companion object {
        fun fromString(value: String): CategoriaProducto {
            //return values().find { it.name == value } ?: ACCESORIOS
            return values().find { it.name == value.uppercase() } ?: OTRO
        }
    }
}

/**
 * Productos iniciales de la web Level-Up Gamer
 */
object ProductosWeb {
    fun obtenerProductosIniciales(): List<Producto> = listOf(
        // Código Aleatorio Steam
        Producto(
            id = 1,
            codigo = "LL001",
            nombre = "Código Aleatorio De Steam",
            descripcion = "Un código único con una combinación de letras y números que sirve para activar y desbloquear un juego o software en tu cuenta de Steam.",
            precio = 29990.0,
            imagenUrl = "codigo_steam",
            categoria = CategoriaProducto.CLAVES_STEAM,
            stock = 50,
            calificacionPromedio = 4.5f,
            numeroResenas = 3
        ),

        // Monopoly
        Producto(
            id = 2,
            codigo = "JM002",
            nombre = "Monopoly",
            descripcion = "Un juego de mesa en el que los jugadores compran, venden e intercambian propiedades inmobiliarias en un tablero, con el objetivo de llevar a los oponentes a la bancarrota.",
            precio = 24990.0,
            imagenUrl = "monopoly",
            categoria = CategoriaProducto.JUEGOS_MESA,
            stock = 15,
            calificacionPromedio = 5.0f,
            numeroResenas = 2
        ),

        // Control Xbox
        Producto(
            id = 3,
            codigo = "AC001",
            nombre = "Controlador Inalámbrico Xbox Series X",
            descripcion = "Ofrece una experiencia de juego cómoda con botones mapeables y respuesta táctil mejorada.",
            precio = 59990.0,
            imagenUrl = "control_xbox",
            categoria = CategoriaProducto.ACCESORIOS,
            stock = 20,
            calificacionPromedio = 4.7f,
            numeroResenas = 3
        ),

        // Auriculares
        Producto(
            id = 4,
            codigo = "AC002",
            nombre = "Auriculares Gamer HyperX Cloud II",
            descripcion = "Sonido envolvente de calidad con micrófono desmontable y almohadillas de espuma viscoelástica.",
            precio = 79990.0,
            imagenUrl = "audifonos_hyperx",
            categoria = CategoriaProducto.ACCESORIOS,
            stock = 12,
            calificacionPromedio = 4.8f,
            numeroResenas = 3
        ),

        // PlayStation 5
        Producto(
            id = 5,
            codigo = "CO001",
            nombre = "PlayStation 5",
            descripcion = "Consola de última generación de Sony con gráficos impresionantes y tiempos de carga ultrarrápidos.",
            precio = 549990.0,
            imagenUrl = "playstation5",
            categoria = CategoriaProducto.CONSOLAS,
            stock = 5,
            calificacionPromedio = 5.0f,
            numeroResenas = 4
        ),

        // PC Gamer
        Producto(
            id = 6,
            codigo = "CG001",
            nombre = "PC Gamer ASUS ROG Strix",
            descripcion = "Potente equipo diseñado para gamers exigentes con los últimos componentes.",
            precio = 1299990.0,
            imagenUrl = "pc_gamer_asus",
            categoria = CategoriaProducto.COMPUTADORES,
            stock = 3,
            calificacionPromedio = 4.7f,
            numeroResenas = 3
        ),

        // Silla Gamer
        Producto(
            id = 7,
            codigo = "SG001",
            nombre = "Silla Gamer Secretlab Titan",
            descripcion = "Diseñada para el máximo confort con soporte ergonómico y personalización ajustable.",
            precio = 349990.0,
            imagenUrl = "silla_secretlab",
            categoria = CategoriaProducto.SILLAS_GAMER,
            stock = 8,
            calificacionPromedio = 4.5f,
            numeroResenas = 3
        ),

        // Mouse
        Producto(
            id = 8,
            codigo = "MS001",
            nombre = "Mouse Gamer Logitech G502 HERO",
            descripcion = "Sensor de alta precisión y botones personalizables para control preciso.",
            precio = 49990.0,
            imagenUrl = "mouse_logitech",
            categoria = CategoriaProducto.MOUSE,
            stock = 25,
            calificacionPromedio = 5.0f,
            numeroResenas = 3
        ),

        // Mousepad
        Producto(
            id = 9,
            codigo = "MP001",
            nombre = "Mousepad Razer Goliathus Extended Chroma",
            descripcion = "Área de juego amplia con iluminación RGB personalizable.",
            precio = 29990.0,
            imagenUrl = "mousepad_razer",
            categoria = CategoriaProducto.MOUSEPAD,
            stock = 30,
            calificacionPromedio = 4.5f,
            numeroResenas = 3
        ),

        // Polera
        Producto(
            id = 10,
            codigo = "PP001",
            nombre = "Polera Gamer Personalizada Level-Up",
            descripcion = "Camiseta cómoda con posibilidad de personalizarla con tu gamer tag o diseño favorito.",
            precio = 14990.0,
            imagenUrl = "polera_levelup",
            categoria = CategoriaProducto.POLERAS,
            stock = 40,
            calificacionPromedio = 4.0f,
            numeroResenas = 2
        )
    )
}