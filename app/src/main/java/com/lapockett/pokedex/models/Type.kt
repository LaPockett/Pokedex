package com.lapockett.pokedex.models

data class Type(
    val slot: Int,
    val type: TypeX
)


data class PokemonListDetailsUI(
    val id: Int,
    val name: String,
    val types: List<Type>,
    val imageUrl: String
)