package com.lapockett.pokedex.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lapockett.pokedex.model.LocalPadding

import com.lapockett.pokedex.ui.theme.PokedexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarPokedex() {
    //stringResource(id = R.string.app_name)
    val paddingValues = LocalPadding.current
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
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Shiny"
                )
            }
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Shiny"
                )
            }
            Box( contentAlignment = Alignment.Center) {
                BadgedBox(badge = { Badge { Text("8") } }) {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Favorite"
                        )
                    }
                }
            }
        }
    )
}
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PokedexNavigation(){
    val navController = rememberNavController()

    PokedexTheme {
        Scaffold(
            topBar = { TopAppBarPokedex() },
            contentWindowInsets = WindowInsets.safeDrawing,
            floatingActionButton = {},
            floatingActionButtonPosition = FabPosition.End,
        ) {
        padding ->
            NavHost(
                navController = navController,
                startDestination = "ListPokemon",
                modifier = Modifier.fillMaxWidth().padding(padding)
            ){
                composable("ListPokemon"){
                    ListPokemonScreen()
                }
            }
        }
    }
}