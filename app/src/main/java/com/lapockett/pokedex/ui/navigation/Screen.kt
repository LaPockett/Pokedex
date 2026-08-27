package com.lapockett.pokedex.ui.navigation

sealed class Screen(val route: String) {
    object Main: Screen("list_pokemon_screen")
    object Detail: Screen("pokemon_detail_screen/{pokemonId}"){
        fun createRoute(pokemonId: Int): String = "pokemon_detail_screen/$pokemonId"
    }
    object Favorites: Screen("favorites_screen")
}