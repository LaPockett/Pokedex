package com.lapockett.pokedex.model.network

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonNamedResult>
)

data class PokemonNamedResult(
    val name: String,
    val url: String
)

data class PokemonNetworkModel(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val base_experience: Int,
    val types: List<TypeSlotNetwork>,
    val stats: List<StatSlotNetwork>,
    val abilities: List<AbilitySlotNetwork>
)

data class TypeSlotNetwork(
    val slot: Int,
    val type: TypeInfoNetwork
)

data class TypeInfoNetwork(
    val name: String,
    val url: String
)

data class StatSlotNetwork(
    val base_stat: Int,
    val effort: Int,
    val stat: StatInfoNetwork
)

data class StatInfoNetwork(
    val name: String,
    val url: String
)

data class AbilitySlotNetwork(
    val ability: AbilityInfoNetwork,
    val is_hidden: Boolean,
    val slot: Int
)

data class AbilityInfoNetwork(
    val name: String,
    val url: String
)