package com.lapockett.pokedex.repository

import com.lapockett.pokedex.data.PokeApiService
import com.lapockett.pokedex.model.PokemonResponse
import com.lapockett.pokedex.models.PokemonDetailsUI

interface PokemonRepository {
    suspend fun getPokemonList(offset: Int, limit: Int): PokemonResponse
    suspend fun getPokemonByName(name: String): PokemonDetailsUI
}

class PokemonRepositoryImpl(private val apiService: PokeApiService) : PokemonRepository{
    override suspend fun getPokemonList(offset: Int, limit: Int): PokemonResponse {
        return apiService.getPokemon(offset, limit)
    }
    override suspend fun getPokemonByName(name: String): PokemonDetailsUI {
        return apiService.getPokemonByName(name)
    }
}