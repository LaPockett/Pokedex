package com.lapockett.pokedex.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lapockett.pokedex.Converters
import com.lapockett.pokedex.databases.dao.PokemonFavoriteDao
import com.lapockett.pokedex.entitie.PokemonEntity

@Database(
    version = 2,
    entities = [PokemonEntity::class],
    exportSchema = false
)
@TypeConverters(Converters::class)
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
                            ).fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}