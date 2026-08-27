package com.lapockett.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.lapockett.pokedex.mappers.toListItemUI
import com.lapockett.pokedex.model.LocalPadding
import com.lapockett.pokedex.ui.navigation.Screen
import com.lapockett.pokedex.viewModel.FavoritePokemonViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    scrollState: LazyGridState,
    viewModelFav: FavoritePokemonViewModel,
    isShiny: Boolean
) {
    val paddingValues = LocalPadding.current
    val favorites by viewModelFav.favoritePokemon.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = paddingValues.tiny)
    ) {
        if (favorites.isEmpty()) {
            Text(
                text = "You don't have any favorite Pokémon yet",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = scrollState
            ) {
                items(items = favorites, key = { it.id }) { favoriteEntity ->
                    PokemonItem(
                        pokemon = favoriteEntity.toListItemUI(),
                        isShiny = isShiny,
                        onClick = { navController.navigate(Screen.Detail.createRoute(favoriteEntity.id)) },
                        viewModelFav = viewModelFav
                    )
                }
            }
        }
    }
}