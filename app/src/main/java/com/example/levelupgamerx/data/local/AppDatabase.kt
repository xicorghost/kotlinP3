package com.example.levelupgamerx.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.levelupgamerx.data.local.dao.CarritoDao
import com.example.levelupgamerx.data.local.dao.ProductoDao
import com.example.levelupgamerx.data.local.dao.ResenaDao
import com.example.levelupgamerx.data.local.dao.UsuarioDao
import com.example.levelupgamerx.data.local.entity.*

/**
 * Base de datos principal de Level-Up Gamer
 * Versión actualizada con todas las tablas y funcionalidades
 */
@Database(
    entities = [
        ProductoEntity::class,
        CarritoEntity::class,
        UsuarioEntity::class,
        ResenaEntity::class
    ],
    version = 2, // Incrementamos la versión
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun resenaDao(): ResenaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "levelupgamer_db"
                )
                    .fallbackToDestructiveMigration() // En producción: usar migraciones apropiadas
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}