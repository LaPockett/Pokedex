package com.lapockett.pokedex.model.network

data class Pokemon(
    val is_hidden: Boolean,
    val pokemon: PokemonX,
    val slot: Int
)