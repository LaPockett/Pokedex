package com.lapockett.pokedex.model.ui

data class PokemonListItemUI(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<TypeUI>
)

data class PokemonDetailsUI(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val imageUrl: String,
    val types: List<TypeUI>,
    val stats: List<StatUI>,
    val abilities: List<AbilityUI>
)

data class TypeUI(
    val slot: Int,
    val name: String
)

data class StatUI(
    val name: String,
    val baseStat: Int,
    val effort: Int
)

data class AbilityUI(
    val name: String,
    val isHidden: Boolean,
    val slot: Int
)