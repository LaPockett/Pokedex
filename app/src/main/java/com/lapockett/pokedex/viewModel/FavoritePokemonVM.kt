package com.lapockett.pokedex.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lapockett.pokedex.databases.PokemonFavDatabase
import com.lapockett.pokedex.entitie.PokemonEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritePokemonViewModel(
    context: Context
) : ViewModel() {

    private val database = PokemonFavDatabase.getDatabase(context)
    private val dao = database.favoritePokemonDao()

    val favoritePokemon: StateFlow<List<PokemonEntity>> = dao
        .getAllFavoritePokemon()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    suspend fun isPokemonFavorite(pokemonId: Int): Boolean {
        return dao.getFavoritePokemonById(pokemonId) != null
    }

    fun addFavoritePokemon(pokemon: PokemonEntity) {
        viewModelScope.launch {
            dao.insertPokemon(pokemon)
        }
    }

    fun removeFavoritePokemon(pokemon: PokemonEntity) {
        viewModelScope.launch {
            dao.deletePokemon(pokemon)
        }
    }
}