package com.lapockett.pokedex.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lapockett.pokedex.R
import com.lapockett.pokedex.data.RetrofitServiceFactory
import com.lapockett.pokedex.model.LocalColors
import com.lapockett.pokedex.model.LocalFontSizes
import com.lapockett.pokedex.model.LocalPadding
import com.lapockett.pokedex.models.AbilityX
import com.lapockett.pokedex.models.StatX
import com.lapockett.pokedex.models.Type
import com.lapockett.pokedex.repository.PokemonRepositoryImpl
import com.lapockett.pokedex.utils.formatHeight
import com.lapockett.pokedex.utils.formatPokemonId
import com.lapockett.pokedex.utils.formatWeight
import com.lapockett.pokedex.utils.pokemonTypeToColor
import com.lapockett.pokedex.viewModel.PokemonVM

data class PokemonDetailsUI(
    val id: Int,
    val height: Int,
    val weight: Int,
    val base_experience: Int,
    val name: String,
    val imageUrl: String,
    val types: List<Type>,
    val stats: List<StatUI>,
    val abilities: List<AbilityUI>
)

data class StatUI(
    val base_stat: Int,
    val effort: Int,
    val stat: StatX
)

data class AbilityUI(
    val ability: AbilityX,
    val is_hidden: Boolean,
    val slot: Int
)

@Composable
fun PokemonDetailsScreen(pokemonId: Int, isShiny: Boolean){
    val api = remember { RetrofitServiceFactory.makeRetrofitService() }
    val repository = remember { PokemonRepositoryImpl(api) }
    val viewModel = remember { PokemonVM(repository) }
    val pokemonDetail by viewModel.pokemonDetails.collectAsState()
    val colorValues = LocalColors.current
    val paddingValues = LocalPadding.current
    val imageUrl = if (isShiny) {
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/${pokemonDetail.id}.png"
    } else {
        pokemonDetail.imageUrl
    }

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemonDetails(pokemonId)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column (
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
            AsyncImage(
                model = imageUrl,
                contentDescription = "Example",
                modifier = Modifier.size(200.dp),
                onError = {
                    println("Error loading image: ${it.result.throwable.message}")
                }
            )
            Text(
                text = pokemonDetail.name.replaceFirstChar { it.uppercase() },
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                pokemonDetail.types.take(2).forEach { type ->
                    val backgroundColor = pokemonTypeToColor(type.type.name)
                    AssistChip(
                        onClick = {},
                        label = { Text(
                            text = type.type.name.replaceFirstChar { it.uppercase() },
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
                        ) },
                        modifier = Modifier.wrapContentWidth(),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = backgroundColor,
                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
            ) {
                PokemonStatCard(
                    icon = painterResource(R.drawable.rule),
                    value = "${formatHeight(pokemonDetail.height)} m",
                    label = "Height"
                )
                Spacer(Modifier.width(paddingValues.big))

                PokemonStatCard(
                    icon = painterResource(R.drawable.weight),
                    value = "${formatWeight(pokemonDetail.weight)} kg",
                    label = "Weight"
                )
                Spacer(Modifier.width(paddingValues.big))

                PokemonStatCard(
                    icon = painterResource(R.drawable.ray),
                    value = pokemonDetail.base_experience.toString(),
                    label = "Base XP"
                )
            }
            val mainTypeColor = pokemonTypeToColor(
                pokemonDetail.types.firstOrNull()?.type?.name ?: "normal"
            )

            PokemonStatsSection(
                stats = pokemonDetail.stats,
                typeColor = mainTypeColor
            )
            PokemonAbilitiesSection(
                abilities = pokemonDetail.abilities
            )
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

