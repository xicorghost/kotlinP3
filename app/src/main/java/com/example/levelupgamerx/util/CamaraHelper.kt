package com.example.levelupgamerx.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper para manejo de cámara y almacenamiento de fotos
 */
class CamaraHelper(private val context: Context) {

    /**
     * Crea un archivo temporal para guardar la foto
     */
    fun crearArchivoImagen(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    /**
     * Obtiene la URI del archivo para la cámara
     */
    fun obtenerUriParaFoto(archivo: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )
    }

    /**
     * Guarda la foto en almacenamiento interno
     *
     * @return Ruta del archivo guardado
     */
    fun guardarFotoEnStorage(archivo: File, carpeta: String = "fotos_perfil"): String {
        val directorioFotos = File(context.filesDir, carpeta)
        if (!directorioFotos.exists()) {
            directorioFotos.mkdirs()
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val archivoDestino = File(directorioFotos, "foto_$timeStamp.jpg")

        // Comprimir y guardar
        val bitmap = BitmapFactory.decodeFile(archivo.absolutePath)
        FileOutputStream(archivoDestino).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        }

        return archivoDestino.absolutePath
    }

    /**
     * Carga una foto desde almacenamiento
     */
    fun cargarFoto(rutaArchivo: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(rutaArchivo)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Elimina una foto
     */
    fun eliminarFoto(rutaArchivo: String): Boolean {
        return try {
            File(rutaArchivo).delete()
        } catch (e: Exception) {
            false
        }
    }
}

/**
 * Composable para lanzar la cámara
 */
@Composable
fun rememberCameraLauncher(
    onFotoTomada: (String) -> Unit,
    onError: (String) -> Unit = {}
): Pair<() -> Unit, Boolean> {
    val context = LocalContext.current
    val camaraHelper = remember { CamaraHelper(context) }
    var archivoFotoActual by remember { mutableStateOf<File?>(null) }
    var esperandoFoto by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        esperandoFoto = false
        if (exito && archivoFotoActual != null) {
            try {
                val rutaGuardada = camaraHelper.guardarFotoEnStorage(archivoFotoActual!!)
                onFotoTomada(rutaGuardada)
            } catch (e: Exception) {
                onError("Error al guardar foto: ${e.message}")
            }
        } else {
            onError("No se pudo tomar la foto")
        }
    }

    val lanzarCamara = {
        try {
            val archivo = camaraHelper.crearArchivoImagen()
            archivoFotoActual = archivo
            val uri = camaraHelper.obtenerUriParaFoto(archivo)
            esperandoFoto = true
            launcher.launch(uri)
        } catch (e: Exception) {
            esperandoFoto = false
            onError("Error al abrir cámara: ${e.message}")
        }
    }

    return Pair(lanzarCamara, esperandoFoto)
}

/**
 * Composable para seleccionar imagen de galería
 */
@Composable
fun rememberGalleryLauncher(
    onImagenSeleccionada: (Uri) -> Unit,
    onError: (String) -> Unit = {}
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            onImagenSeleccionada(uri)
        } else {
            onError("No se seleccionó ninguna imagen")
        }
    }

    return { launcher.launch("image/*") }
}