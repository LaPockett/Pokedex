package com.lapockett.pokedex.repository

import com.lapockett.pokedex.databases.data.PokeApiService
import com.lapockett.pokedex.mappers.toDetailsUI
import com.lapockett.pokedex.mappers.toListItemUI
import com.lapockett.pokedex.model.ui.PokemonDetailsUI
import com.lapockett.pokedex.model.ui.PokemonListItemUI

interface PokemonRepository {
    suspend fun getRawPokemonPage(offset: Int, limit: Int): List<String>
    suspend fun getPokemonByName(name: String): PokemonListItemUI
    suspend fun getPokemonById(id: Int): PokemonDetailsUI
}

class PokemonRepositoryImpl(
    private val apiService: PokeApiService
) : PokemonRepository {

    override suspend fun getRawPokemonPage(offset: Int, limit: Int): List<String> {
        return apiService.getPokemonList(offset, limit).results.map { it.name }
    }

    override suspend fun getPokemonByName(name: String): PokemonListItemUI {
        return apiService.getPokemonByName(name).toListItemUI()
    }

    override suspend fun getPokemonById(id: Int): PokemonDetailsUI {
        return apiService.getPokemonById(id).toDetailsUI()
    }
}