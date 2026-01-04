package com.lapockett.pokedex.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lapockett.pokedex.models.PokemonListDetailsUI
import com.lapockett.pokedex.models.Type
import com.lapockett.pokedex.repository.PokemonRepository
import com.lapockett.pokedex.ui.screens.AbilityUI
import com.lapockett.pokedex.ui.screens.PokemonDetailsUI
import com.lapockett.pokedex.ui.screens.StatUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonVM(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList =
        MutableStateFlow<List<PokemonListDetailsUI>>(emptyList())
    val pokemonList: StateFlow<List<PokemonListDetailsUI>> = _pokemonList

    private val _pokemonDetails =
        MutableStateFlow<PokemonDetailsUI>(
            PokemonDetailsUI(
                id = 0,
                height = 0,
                weight = 0,
                baseExperience = 0,
                name = "",
                imageUrl = "",
                types = emptyList(),
                stats = emptyList(),
                abilities = emptyList()
            )
        )
    val pokemonDetails: StateFlow<PokemonDetailsUI> = _pokemonDetails

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

                PokemonListDetailsUI(
                    id = detail.id,
                    name = detail.name,
                    imageUrl = getPokemonDefaultImage(detail.id),
                    types = getPokemonTypes(detail.types)
                )
            }
            _pokemonList.value += detailedList
            offset += limit
            isLoading = false
        }
    }
    fun loadPokemonDetails(pokemonId: Int) {
        viewModelScope.launch {
            val detail = repository.getPokemonById(pokemonId)
            _pokemonDetails.value = PokemonDetailsUI(
                id = detail.id,
                height = detail.height,
                weight = detail.weight,
                baseExperience = detail.baseExperience,
                name = detail.name,
                imageUrl = getPokemonDefaultImage(detail.id),
                types = getPokemonTypes(detail.types),
                stats = getPokemonStats(detail.stats),
                abilities = getPokemonAbilities(detail.abilities)
            )

        }
    }
    private fun getPokemonAbilities(abilities: List<AbilityUI>): List<AbilityUI>{
        return abilities.map {
            AbilityUI(
                ability = it.ability,
                is_hidden = it.is_hidden,
                slot = it.slot
            )
        }
    }
    private fun getPokemonStats(stats: List<StatUI>): List<StatUI>{
        return stats.map {
            StatUI(
                base_stat = it.base_stat,
                effort = it.effort,
                stat = it.stat
            )
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

    private fun getPokemonDefaultImage(id: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    }
}