package com.lapockett.pokedex.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lapockett.pokedex.model.PokemonDetailState
import com.lapockett.pokedex.model.PokemonListState
import com.lapockett.pokedex.model.ui.PokemonListItemUI
import com.lapockett.pokedex.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class PokemonVM(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _listState = MutableStateFlow<PokemonListState>(PokemonListState.Idle)
    val listState: StateFlow<PokemonListState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow<PokemonDetailState>(PokemonDetailState.Idle)
    val detailState: StateFlow<PokemonDetailState> = _detailState.asStateFlow()

    private val limit = 20
    private var offset = 0
    private var hasMore = true

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val loadedPokemon = mutableListOf<PokemonListItemUI>()

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        if (_isLoading.value || !hasMore) return

        viewModelScope.launch {
            _isLoading.value = true
            if (loadedPokemon.isEmpty()){
                _listState.value = PokemonListState.Loading
            }
            try {
                val listResponse = repository.getRawPokemonPage(offset, limit)

                if (listResponse.isEmpty()) {
                    hasMore = false
                    _listState.value = PokemonListState.Success(
                        data = loadedPokemon.toList(),
                        canLoadMore = false
                    )
                    return@launch
                }
                val detailedList = listResponse.map { name ->
                    async { repository.getPokemonByName(name) }
                }.awaitAll()

                loadedPokemon.addAll(detailedList)
                offset += limit
                hasMore = listResponse.size == limit

                _listState.value = PokemonListState.Success(
                    data = loadedPokemon.toList(),
                    canLoadMore = hasMore
                )

            } catch (e: Exception) {
                _listState.value = PokemonListState.Error(
                    e.message ?: "ERROR cargando la lista de pokémon"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadPokemonDetails(pokemonId: Int) {
        viewModelScope.launch {
            _detailState.value = PokemonDetailState.Loading

            try {
                val detail = repository.getPokemonById(pokemonId)
                _detailState.value = PokemonDetailState.Success(detail)
            } catch (e: Exception) {
                _detailState.value = PokemonDetailState.Error(
                    e.message ?: "ERROR cargando el detalle del pokémon"
                )
            }
        }
    }
    fun retryLoadPokemon() {
        if (_listState.value is PokemonListState.Error) {
            loadPokemon()
        }
    }

    fun retryLoadDetails(pokemonId: Int) {
        if (_detailState.value is PokemonDetailState.Error) {
            loadPokemonDetails(pokemonId)
        }
    }
}