package com.example.levelupgamerx.data.local.remote


/**
 * Sealed class para representar los diferentes estados de una operación de red
 *
 * @param T Tipo de dato esperado en caso de éxito
 * @author LevelUp GamerX Team
 */
sealed class ResultadoApi<out T> {

    /**
     * Estado de éxito con datos
     */
    data class Exito<T>(val datos: T) : ResultadoApi<T>()

    /**
     * Estado de error con información del fallo
     */
    data class Error(
        val mensajeError: String,
        val codigoHttp: Int? = null,
        val excepcion: Throwable? = null
    ) : ResultadoApi<Nothing>()

    /**
     * Estado de carga
     */
    object Cargando : ResultadoApi<Nothing>()
}

// Funciones de extensión útiles

fun <T> ResultadoApi<T>.obtenerDatosONull(): T? {
    return when (this) {
        is ResultadoApi.Exito -> this.datos
        else -> null
    }
}

fun <T> ResultadoApi<T>.estaEnError(): Boolean {
    return this is ResultadoApi.Error
}

fun <T> ResultadoApi<T>.estaCargando(): Boolean {
    return this is ResultadoApi.Cargando
}

fun <T> ResultadoApi<T>.fueExitoso(): Boolean {
    return this is ResultadoApi.Exito
}

inline fun <T> ResultadoApi<T>.alSerExitoso(bloque: (T) -> Unit) {
    if (this is ResultadoApi.Exito) {
        bloque(this.datos)
    }
}

inline fun <T> ResultadoApi<T>.alSerError(bloque: (String) -> Unit) {
    if (this is ResultadoApi.Error) {
        bloque(this.mensajeError)
    }
}

inline fun <T, R> ResultadoApi<T>.mapear(transformacion: (T) -> R): ResultadoApi<R> {
    return when (this) {
        is ResultadoApi.Exito -> ResultadoApi.Exito(transformacion(this.datos))
        is ResultadoApi.Error -> ResultadoApi.Error(this.mensajeError, this.codigoHttp, this.excepcion)
        is ResultadoApi.Cargando -> ResultadoApi.Cargando
    }
}

fun <T> ResultadoApi<T>.obtenerMensajeError(mensajePorDefecto: String = ""): String {
    return when (this) {
        is ResultadoApi.Error -> this.mensajeError
        else -> mensajePorDefecto
    }
}