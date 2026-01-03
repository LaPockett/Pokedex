package com.lapockett.pokedex.models

data class Type(
    val slot: Int,
    val type: TypeX
)


data class PokemonDetailsUI(
    val id: Int,
    val name: String,
    val types: List<Type>,
    val imageUrl: String
)