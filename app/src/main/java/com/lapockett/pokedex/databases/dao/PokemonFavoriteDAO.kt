package com.lapockett.pokedex.databases.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lapockett.pokedex.entitie.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonFavoriteDao {
    @Query("SELECT * FROM favorite_pokemons")
    fun getAllFavoritePokemon(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM favorite_pokemons WHERE id = :pokemonId")
    suspend fun getFavoritePokemonById(pokemonId: Int): PokemonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Delete
    suspend fun deletePokemon(pokemon: PokemonEntity)

    @Query("DELETE FROM favorite_pokemons WHERE id = :pokemonId")
    suspend fun deletePokemonById(pokemonId: Int)
}