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
import com.example.levelupgamerx.domain.model.ItemCarrito
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Delete

@Composable
fun CarritoItemCard(
    item: ItemCarrito,
    onAumentarCantidad: () -> Unit,
    onDisminuirCantidad: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = item.producto.imagenUrl),
                contentDescription = item.producto.nombre,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.producto.nombre, fontWeight = FontWeight.Bold)
                Text("Cantidad: ${item.cantidad}")
                Text("Subtotal: $${item.subtotal.toInt()}")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onAumentarCantidad) {
                    Icon(Icons.Filled.Add, contentDescription = "Más")
                }
                IconButton(onClick = onDisminuirCantidad) {
                    Icon(Icons.Filled.RemoveCircle, contentDescription = "Menos")
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}