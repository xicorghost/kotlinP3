package com.example.levelupgamerx.data.local.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit configurado para la API de LevelUp GamerX
 * API desplegada en Railway
 *
 * @author LevelUp GamerX Team
 */
object RetrofitClient {

    /**
     * URL base de la API en Railway
     * IMPORTANTE: Mantener la barra final /
     */
    private const val URL_BASE = "https://api-dfs2-dm-production.up.railway.app/api/gaming/"
    /**
     * Interceptor para logging de peticiones HTTP
     */
    private val interceptorLog = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Cliente HTTP con timeouts configurados
     */
    private val clienteHttp = OkHttpClient.Builder()
        .addInterceptor(interceptorLog)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Instancia de Retrofit inicializada de forma perezosa
     */
    val instancia: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Crea una instancia del servicio API
     */
    fun <T> crearServicio(servicioClase: Class<T>): T {
        return instancia.create(servicioClase)
    }
}