package com.lapockett.pokedex.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lapockett.pokedex.databases.dao.PokemonFavoriteDao
import com.lapockett.pokedex.entitie.PokemonEntity

@Database(
    version = 1,
    entities = [PokemonEntity::class],
    exportSchema = false
)
abstract class PokemonFavDatabase : RoomDatabase() {
    abstract fun favoritePokemonDao(): PokemonFavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonFavDatabase? = null

        fun getDatabase(context: Context): PokemonFavDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonFavDatabase::class.java,
                    "pokemon_favorites.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}