package com.lapockett.pokedex.repository

import com.lapockett.pokedex.databases.data.PokeApiService
import com.lapockett.pokedex.model.PokemonResponse
import com.lapockett.pokedex.models.PokemonListDetailsUI
import com.lapockett.pokedex.ui.screens.PokemonDetailsUI

interface PokemonRepository {
    suspend fun getPokemonList(offset: Int, limit: Int): PokemonResponse
    suspend fun getPokemonByName(name: String): PokemonListDetailsUI
    suspend fun getPokemonById(id: Int): PokemonDetailsUI
}

class PokemonRepositoryImpl(private val apiService: PokeApiService) : PokemonRepository{
    override suspend fun getPokemonList(offset: Int, limit: Int): PokemonResponse {
        return apiService.getPokemon(offset, limit)
    }
    override suspend fun getPokemonByName(name: String): PokemonListDetailsUI {
        return apiService.getPokemonByName(name)
    }
    override suspend fun getPokemonById(id: Int): PokemonDetailsUI {
        return apiService.getPokemonById(id)
    }
}