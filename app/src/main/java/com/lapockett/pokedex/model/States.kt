package com.lapockett.pokedex.model

import com.lapockett.pokedex.model.ui.PokemonDetailsUI
import com.lapockett.pokedex.model.ui.PokemonListItemUI

sealed class PokemonListState {
    object Idle : PokemonListState()
    object Loading : PokemonListState()
    data class Success(
        val data: List<PokemonListItemUI>,
        val canLoadMore: Boolean
    ) : PokemonListState()
    data class Error(val message: String) : PokemonListState()
}

sealed class PokemonDetailState {
    object Idle : PokemonDetailState()
    object Loading : PokemonDetailState()
    data class Success(val data: PokemonDetailsUI) : PokemonDetailState()
    data class Error(val message: String) : PokemonDetailState()
}
