package com.lapockett.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.lapockett.pokedex.data.RetrofitServiceFactory
import com.lapockett.pokedex.ui.theme.ListPokemonScreen
import com.lapockett.pokedex.ui.theme.PokedexTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val service = RetrofitServiceFactory.makeRetrofitService()
        lifecycleScope.launch {
            val pokemon = service.getPokemon(0, 20)
            println(pokemon)
        }
        setContent {
            PokedexTheme {
                    ListPokemonScreen()
            }
        }
    }
}