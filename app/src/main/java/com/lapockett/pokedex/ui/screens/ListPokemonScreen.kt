package com.lapockett.pokedex.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.lapockett.pokedex.databases.data.RetrofitServiceFactory
import com.lapockett.pokedex.entitie.PokemonEntity
import com.lapockett.pokedex.mappers.toEntity
import com.lapockett.pokedex.model.LocalColors
import com.lapockett.pokedex.model.LocalPadding
import com.lapockett.pokedex.model.PokemonListState
import com.lapockett.pokedex.model.ui.PokemonListItemUI
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

    val listState by viewModel.listState.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    // Forzar que el foco se quite de la barra de búsqueda cuando hago scroll
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling) {
                    focusManager.clearFocus()
                }
            }
    }
    LaunchedEffect(searchQuery) {
        viewModel.search(searchQuery)
    }
    LaunchedEffect(scrollState, searchQuery) {
        snapshotFlow { scrollState.layoutInfo }
            .collect { layoutInfo ->
                if (searchQuery.isBlank()) {
                    val totalItems = layoutInfo.totalItemsCount
                    val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    if (lastVisibleIndex >= totalItems - 14) {
                        viewModel.loadPokemon()
                    }
                }
            }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = paddingValues.tiny)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            } // Forzar que el foco se quite de la barra de búsqueda cuando hago click fuera de ella
    ) {
        // FIJO, SIEMPRE SE VE
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddingValues.extraTiny)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val effectiveState = if (searchQuery.isBlank()) listState else searchState
            when (effectiveState) {
                is PokemonListState.Success -> {
                    if (effectiveState.data.isEmpty()) {
                        Text(
                            text = "No se encontraron resultados",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            state = scrollState
                        ) {
                            items(items = effectiveState.data, key = { it.id }) { pokemon ->
                                PokemonItem(
                                    pokemon,
                                    isShiny = isShiny,
                                    onClick = { navController.navigate(Screen.Detail.createRoute(pokemon.id)) },
                                    viewModelFav = viewModelFav
                                )
                            }
                            if (isLoading && searchQuery.isBlank()) {
                                item(span = { GridItemSpan(2) }) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                    }
                }
                is PokemonListState.Error -> {
                    Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = effectiveState.message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.retryLoadPokemon() }) {
                        Text("Reintentar")
                    }
                } }
                is PokemonListState.Loading, is PokemonListState.Idle -> {
                    if (searchQuery.isBlank()) {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), state = scrollState) {
                            items(20) { PokemonSkeletonItem() }
                        }
                    } else {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), state = scrollState) {
                            items(20) { PokemonSkeletonItem() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonItem(
    pokemon: PokemonListItemUI,
    isShiny: Boolean,
    onClick: () -> Unit,
    viewModelFav: FavoritePokemonViewModel
) {
    val paddingValues = LocalPadding.current
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
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            if (isFavorite) {
                                viewModelFav.addFavoritePokemon(pokemon.toEntity())
                            } else {
                                viewModelFav.removeFavoritePokemon(pokemon.toEntity())
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
                        val backgroundColor = pokemonTypeToColor(type.name)
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = type.name.replaceFirstChar { it.uppercase() },
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

@Composable
fun PokemonSkeletonItem() {
    val paddingValues = LocalPadding.current
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues.extraTiny),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(paddingValues.tiny)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(14.dp)
                            .background(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                                RoundedCornerShape(4.dp)
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                                RoundedCornerShape(50.dp)
                            )
                    )
                }
                Spacer(Modifier.height(paddingValues.tiny))
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                            RoundedCornerShape(8.dp)
                        )
                )
                Spacer(Modifier.height(paddingValues.tiny))
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(16.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                            RoundedCornerShape(4.dp)
                        )
                )
                Spacer(Modifier.height(paddingValues.tiny))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(24.dp)
                                .background(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                                    RoundedCornerShape(50.dp)
                                )
                        )
                    }
                }
                Spacer(Modifier.height(paddingValues.extraTiny))
            }
        }
    }
}