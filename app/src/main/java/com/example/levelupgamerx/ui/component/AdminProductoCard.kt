package com.example.levelupgamerx.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.levelupgamerx.domain.model.Producto
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete


/**
 * Tarjeta de producto para el panel del administrador.
 */
@Composable
fun AdminProductoCard(
    producto: Producto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(producto.imagenUrl),
                contentDescription = producto.nombre,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold)
                Text("Categoría: ${producto.categoria}")
                Text("Stock: ${producto.stock}")
                Text("Precio: ${producto.precioFormateado()}")
            }

            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, "Editar")
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, "Eliminar")
                }
            }
        }
    }
}