package com.lapockett.pokedex.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lapockett.pokedex.R
import com.lapockett.pokedex.databases.data.RetrofitServiceFactory
import com.lapockett.pokedex.model.LocalColors
import com.lapockett.pokedex.model.LocalPadding
import com.lapockett.pokedex.model.PokemonDetailState
import com.lapockett.pokedex.repository.PokemonRepositoryImpl
import com.lapockett.pokedex.utils.formatHeight
import com.lapockett.pokedex.utils.formatPokemonId
import com.lapockett.pokedex.utils.formatWeight
import com.lapockett.pokedex.utils.pokemonTypeToColor
import com.lapockett.pokedex.viewModel.PokemonVM

@Composable
fun PokemonDetailsScreen(pokemonId: Int, isShiny: Boolean) {
    val api = remember { RetrofitServiceFactory.makeRetrofitService() }
    val repository = remember { PokemonRepositoryImpl(api) }
    val viewModel = remember { PokemonVM(repository) }
    val detailState by viewModel.detailState.collectAsState()
    val colorValues = LocalColors.current
    val paddingValues = LocalPadding.current

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemonDetails(pokemonId)
    }

    when (val state = detailState) {
        is PokemonDetailState.Loading, is PokemonDetailState.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is PokemonDetailState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.retryLoadDetails(pokemonId) }) {
                        Text("Reintentar")
                    }
                }
            }
        }
        is PokemonDetailState.Success -> {
            val pokemon = state.data

            val imageUrl = if (isShiny) {
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/${pokemon.id}.png"
            } else {
                pokemon.imageUrl
            }
            val mainTypeColor = pokemonTypeToColor(
                pokemon.types.firstOrNull()?.name ?: "normal"
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(paddingValues.tiny))
                    Text(
                        text = formatPokemonId(pokemonId),
                        fontSize = MaterialTheme.typography.labelLarge.fontSize,
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        color = colorValues.pokemonIdColor
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(250.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        mainTypeColor.copy(alpha = 0.5f),
                                        mainTypeColor.copy(alpha = 0.4f),
                                        mainTypeColor.copy(alpha = 0.3f),
                                        mainTypeColor.copy(alpha = 0.2f),
                                        mainTypeColor.copy(alpha = 0.1f),
                                        Color.Transparent
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Pokémon ${pokemon.name}",
                                modifier = Modifier.size(216.dp),
                                onError = {
                                    println("Error loading image: ${it.result.throwable.message}")
                                }
                            )
                            Text(
                                text = pokemon.name.replaceFirstChar { it.uppercase() },
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = paddingValues.extraTiny),
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
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        PokemonStatCard(
                            icon = painterResource(R.drawable.rule),
                            value = "${formatHeight(pokemon.height)} m",
                            label = "Height"
                        )
                        Spacer(Modifier.width(paddingValues.big))
                        PokemonStatCard(
                            icon = painterResource(R.drawable.weight),
                            value = "${formatWeight(pokemon.weight)} kg",
                            label = "Weight"
                        )
                        Spacer(Modifier.width(paddingValues.big))
                        PokemonStatCard(
                            icon = painterResource(R.drawable.ray),
                            value = pokemon.baseExperience.toString(),  // antes base_experience
                            label = "Base XP"
                        )
                    }
                    PokemonStatsSection(
                        stats = pokemon.stats,
                        typeColor = mainTypeColor
                    )
                    PokemonAbilitiesSection(
                        abilities = pokemon.abilities
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonStatCard(
    icon: Painter,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val paddingValues = LocalPadding.current
    Card(
        modifier = modifier
            .height(100.dp)
            .width(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = value,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(paddingValues.tiny))
            Text(
                text = value,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

