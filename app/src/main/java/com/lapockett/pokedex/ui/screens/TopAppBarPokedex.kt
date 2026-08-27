package com.lapockett.pokedex.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lapockett.pokedex.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.lapockett.pokedex.model.LocalPadding
import com.lapockett.pokedex.viewModel.FavoritePokemonViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarPokedex(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onToggleShiny: () -> Unit,
    isShiny: Boolean,
    viewModelFav: FavoritePokemonViewModel
) {
    val paddingValues = LocalPadding.current
    val favorites by viewModelFav.favoritePokemon.collectAsState()
    val badgeCount = when {
        favorites.size < 10 -> favorites.size.toString()
        else -> "10+"
    }
    TopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.pokeball),
                contentDescription = "Pokeball",
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = paddingValues.tiny)
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier.padding(start = paddingValues.big)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        actions = {
            IconButton(onClick = onToggleShiny) {
                Icon(
                    painter = if (isShiny)
                        painterResource(R.drawable.sparkles_filled)
                    else
                        painterResource(R.drawable.sparkles),
                    contentDescription = "Shiny",
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = onToggleTheme) {
                Icon(
                    painter = if (isDarkTheme)
                        painterResource(R.drawable.sun)
                    else
                        painterResource(R.drawable.moon),
                    contentDescription = "Theme",
                    modifier = Modifier.size(24.dp)
                )
            }
            Box(contentAlignment = Alignment.Center) {
                BadgedBox(badge = {
                    if (badgeCount.isNotEmpty()){
                        Badge {
                            Text(badgeCount)
                        }
                    }
                }) {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                }
            }
        }
    )
}

@SuppressLint("FrequentlyChangingValue")
@Composable
fun PokedexNavigation(
    isDarkTheme: Boolean,
    isShiny: Boolean,
    onToggleShiny: () -> Unit,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val scrollState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val viewModelFav = remember { FavoritePokemonViewModel(context) }
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    Scaffold(
        topBar = { TopAppBarPokedex(
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
            onToggleShiny = onToggleShiny,
            isShiny = isShiny,
            viewModelFav = viewModelFav) },
        contentWindowInsets = WindowInsets.safeDrawing,
        floatingActionButton = {
            if (scrollState.firstVisibleItemIndex > 0 && currentRoute == Screen.Main.route) {
                FloatingActionButton(
                    onClick = {
                       coroutineScope.launch {
                           scrollState.animateScrollToItem(0)
                       }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Scroll to top"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            composable(Screen.Main.route) {
                ListPokemonScreen(
                    navController = navController,
                    isShiny = isShiny,
                    scrollState = scrollState,
                    viewModelFav = viewModelFav
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("pokemonId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val pokemonId =
                    backStackEntry.arguments?.getInt("pokemonId") ?: return@composable
                PokemonDetailsScreen(pokemonId = pokemonId, isShiny = isShiny)
            }
        }
    }
}