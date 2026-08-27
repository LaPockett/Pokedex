package com.lapockett.pokedex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lapockett.pokedex.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lapockett.pokedex.model.LocalPadding
import com.lapockett.pokedex.ui.navigation.Screen
import com.lapockett.pokedex.viewModel.FavoritePokemonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarPokedex(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onToggleShiny: () -> Unit,
    isShiny: Boolean,
    viewModelFav: FavoritePokemonViewModel,
    navController: NavController,
    currentRoute: String?
) {
    val paddingValues = LocalPadding.current
    val favorites by viewModelFav.favoritePokemon.collectAsState()
    val badgeCount = when {
        favorites.size < 10 -> favorites.size.toString()
        else -> "10+"
    }
    val goToMain = {
        navController.navigate(Screen.Main.route) {
            popUpTo(Screen.Main.route) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
    TopAppBar(
        navigationIcon =  {
            IconButton(
                onClick = goToMain
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pokeball),
                    contentDescription = "Go to main",
                    modifier = Modifier.size(40.dp).padding(start = paddingValues.extraTiny)
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier.padding(start = paddingValues.extraTiny)
                    .clickable { goToMain() }
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
                        onClick = {
                            navController.navigate(Screen.Favorites.route) {
                                launchSingleTop = true
                            }
                        },
                        enabled = currentRoute != Screen.Favorites.route
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