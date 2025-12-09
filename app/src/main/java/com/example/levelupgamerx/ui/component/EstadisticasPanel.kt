package com.example.levelupgamerx.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.levelupgamerx.domain.model.Producto

/**
 * Panel simple de estadísticas para el administrador.
 * Muestra totales por categoría.
 */
@Composable
fun EstadisticasPanel(productos: List<Producto>) {
    val categorias = productos.groupBy { it.categoria }

    Column(modifier = Modifier.padding(16.dp)) {
        categorias.forEach { (categoria, lista) ->
            val totalStock = lista.sumOf { it.stock }
            val valorInventario = lista.sumOf { it.precio * it.stock }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = categoria.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Productos: ${lista.size}")
                    Text("Stock total: $totalStock")
                    Text("Valor inventario: $${valorInventario.toInt()}")
                }
            }
        }
    }
}