package com.lapockett.pokedex.entitie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pokemons")
data class PokemonEntity(
    @PrimaryKey
    val id: Int,
    val name: String? = null,
    val imageUrl: String? = null,
    val types: List<String> = emptyList()
)