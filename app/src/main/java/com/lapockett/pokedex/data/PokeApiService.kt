package com.lapockett.pokedex.data

import com.lapockett.pokedex.model.PokemonResponse
import com.lapockett.pokedex.models.PokemonListDetailsUI
import com.lapockett.pokedex.ui.screens.PokemonDetailsUI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): PokemonListDetailsUI

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): PokemonDetailsUI
}

object RetrofitServiceFactory {
    fun makeRetrofitService(): PokeApiService {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PokeApiService::class.java)
    }
}