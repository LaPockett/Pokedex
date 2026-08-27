package com.lapockett.pokedex.model.network

data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language,
    val version_group: VersionGroup
)