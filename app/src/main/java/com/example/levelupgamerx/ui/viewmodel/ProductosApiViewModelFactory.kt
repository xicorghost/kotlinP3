package com.example.levelupgamerx.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelupgamerx.data.repository.ProductoLocalRepository
import com.example.levelupgamerx.data.repository.ProductoRemotoRepository // Usamos el repositorio remoto correcto

// Esta debe ser la ÚNICA definición de la clase en este archivo
class ProductosApiViewModelFactory(
    private val apiRepository: ProductoRemotoRepository, // Repositorio API
    private val localRepository: ProductoLocalRepository // Repositorio Local
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductosApiViewModel::class.java)) {
            return ProductosApiViewModel(apiRepository, localRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}