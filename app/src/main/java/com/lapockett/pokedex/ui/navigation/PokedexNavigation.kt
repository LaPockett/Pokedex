package com.lapockett.pokedex.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lapockett.pokedex.ui.screens.FavoritesScreen
import com.lapockett.pokedex.ui.screens.ListPokemonScreen
import com.lapockett.pokedex.ui.screens.PokemonDetailsScreen
import com.lapockett.pokedex.ui.components.TopAppBarPokedex
import com.lapockett.pokedex.viewModel.FavoritePokemonViewModel
import kotlinx.coroutines.launch

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
            viewModelFav = viewModelFav,
            navController = navController,
            currentRoute = currentRoute
        ) },
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
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    navController = navController,
                    scrollState = scrollState,
                    viewModelFav = viewModelFav,
                    isShiny = isShiny
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