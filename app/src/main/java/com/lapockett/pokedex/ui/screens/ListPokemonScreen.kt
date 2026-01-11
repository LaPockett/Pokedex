package com.lapockett.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.lapockett.pokedex.databases.data.RetrofitServiceFactory
import com.lapockett.pokedex.entitie.PokemonEntity
import com.lapockett.pokedex.model.LocalColors
import com.lapockett.pokedex.model.LocalPadding
import com.lapockett.pokedex.models.PokemonListDetailsUI
import com.lapockett.pokedex.repository.PokemonRepositoryImpl
import com.lapockett.pokedex.utils.formatPokemonId
import com.lapockett.pokedex.utils.pokemonTypeToColor
import com.lapockett.pokedex.viewModel.FavoritePokemonViewModel
import com.lapockett.pokedex.viewModel.PokemonVM

@Composable
fun ListPokemonScreen(
    navController: NavController,
    isShiny: Boolean,
    scrollState: LazyGridState,
    viewModelFav : FavoritePokemonViewModel
)
{
    val paddingValues = LocalPadding.current
    val api = remember { RetrofitServiceFactory.makeRetrofitService() }
    val repository = remember { PokemonRepositoryImpl(api) }
    val viewModel = remember { PokemonVM(repository) }

    val pokemonList by viewModel.pokemonList.collectAsState()

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.layoutInfo }
            .collect { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                if (lastVisibleIndex >= totalItems - 15) {
                    viewModel.loadPokemon()
                }
            }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = paddingValues.tiny)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = scrollState
        ) {
            items(items = pokemonList) { pokemon ->
                PokemonItem(
                    pokemon,
                    isShiny = isShiny,
                    onClick = {
                        navController.navigate(Screen.Detail.createRoute(pokemon.id))
                    },
                    viewModelFav = viewModelFav
                )
            }
        }
    }

}

@Composable
fun PokemonItem(
    pokemon: PokemonListDetailsUI,
    isShiny: Boolean,
    onClick: () -> Unit,
    viewModelFav: FavoritePokemonViewModel
) {
    val paddingValues = LocalPadding.current
    val colorValues = LocalColors.current
    var isFavorite by remember { mutableStateOf(false) }
    val imageUrl = if (isShiny) {
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/${pokemon.id}.png"
    } else {
        pokemon.imageUrl
    }
    LaunchedEffect(pokemon.id, viewModelFav) {
        isFavorite = viewModelFav.isPokemonFavorite(pokemon.id)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues.extraTiny)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(paddingValues.tiny)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatPokemonId(pokemon.id),
                        fontSize = MaterialTheme.typography.labelLarge.fontSize,
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        color = colorValues.pokemonIdColor
                    )
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            val pokemonEntity = PokemonEntity(
                                id = pokemon.id,
                                name = pokemon.name,
                                imageUrl = pokemon.imageUrl
                            )
                            if (isFavorite) {
                                viewModelFav.addFavoritePokemon(pokemonEntity)
                            } else {
                                viewModelFav.removeFavoritePokemon(pokemonEntity)
                            }
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint =
                                if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                }
                AsyncImage(
                    model = imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(100.dp),
                    onError = {
                        println("Error loading image: ${it.result.throwable.message}")
                    }
                )

                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    pokemon.types.take(2).forEach { type ->
                        val backgroundColor = pokemonTypeToColor(type.type.name)
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = type.type.name.replaceFirstChar { it.uppercase() },
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
                                )
                            },
                            modifier = Modifier.wrapContentWidth(),
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = backgroundColor,
                                labelColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }

    }
}