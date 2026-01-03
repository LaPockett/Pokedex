package com.lapockett.pokedex.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import coil3.compose.AsyncImage
import com.lapockett.pokedex.data.RetrofitServiceFactory
import com.lapockett.pokedex.models.PokemonDetailsUI
import com.lapockett.pokedex.repository.PokemonRepositoryImpl
import com.lapockett.pokedex.viewModel.PokemonVM

@Preview
@Composable
fun ListPokemonScreen(){
    val api = remember { RetrofitServiceFactory.makeRetrofitService() }
    val repository = remember { PokemonRepositoryImpl(api) }
    val viewModel = remember { PokemonVM(repository) }

    val pokemonList by viewModel.pokemonList.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(items = pokemonList) { index ->
            PokemonItem(index)
        }
    }
}

@Composable
fun PokemonItem(pokemon: PokemonDetailsUI){
    Card(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(16.dp),
        onClick = { }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
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
                    text = pokemon.id.toString(),
                    fontSize = 14.sp,
                )
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                )
            }
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(100.dp),
                onError = {
                    println("Error loading image: ${it.result.throwable.message}")
                }
            )

            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                fontSize = 22.sp,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                pokemon.types.take(2).forEach { type ->
                    AssistChip(
                        onClick = {},
                        label = { Text(text = type.type.name.replaceFirstChar { it.uppercase() }, fontSize = 13.sp) },
                        modifier = Modifier.wrapContentWidth()
                    )
                }
            }
        }
    }
}