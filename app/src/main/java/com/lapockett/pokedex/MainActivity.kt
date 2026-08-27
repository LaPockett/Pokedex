package com.lapockett.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.lapockett.pokedex.ui.navigation.PokedexNavigation
import com.lapockett.pokedex.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            var isShiny by rememberSaveable { mutableStateOf(false) }
            PokedexTheme(
                darkTheme = isDarkTheme
            ) {
                PokedexNavigation(
                    isDarkTheme= isDarkTheme,
                    isShiny = isShiny,
                    onToggleTheme = {
                    isDarkTheme = !isDarkTheme
                },
                    onToggleShiny = { isShiny = !isShiny }
                )
            }
        }
    }
}