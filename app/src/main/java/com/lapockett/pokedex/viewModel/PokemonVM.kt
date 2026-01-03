package com.lapockett.pokedex.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lapockett.pokedex.models.PokemonDetailsUI
import com.lapockett.pokedex.models.Type
import com.lapockett.pokedex.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonVM(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList =
        MutableStateFlow<List<PokemonDetailsUI>>(emptyList())
    val pokemonList: StateFlow<List<PokemonDetailsUI>> = _pokemonList

    private var offset = 0
    private val limit = 20
    private var isLoading = false
    private var hasMore = true

    init {
        loadPokemon()
    }
    fun loadPokemon() {
        if (isLoading || !hasMore) return
        isLoading = true

        viewModelScope.launch {
            val listResponse = repository.getPokemonList(offset, limit)
            if(listResponse.results.isEmpty()){
                hasMore = false
                isLoading = false
                return@launch
            }

            val detailedList = listResponse.results.map { result ->
                val detail = repository.getPokemonByName(result.name)

                PokemonDetailsUI(
                    id = detail.id,
                    name = detail.name,
                    imageUrl = getPokemonImage(detail.id),
                    types = getPokemonTypes(detail.types)
                )
            }
            _pokemonList.value += detailedList
            offset += limit
            isLoading = false
        }
    }
    private fun getPokemonTypes(types: List<Type>): List<Type>{
        return types.map {
            Type(
                slot = it.slot,
                type = it.type
            )
        }
    }

    private fun getPokemonImage(id: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    }
}