package com.lapockett.pokedex.utils

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun formatPokemonId(
    pokemonId: Int
): String {
    //return String.format("#%03d", pokemonId)
    if (pokemonId < 10) {
        return "#00$pokemonId"
    } else if (pokemonId < 100) {
        return "#0$pokemonId"
    }
    return "#$pokemonId"
}
fun formatWeight(
    weight: Int
): Double {
    return weight / 10.0
}
fun formatHeight(
    height: Int
): Double {
    return height / 10.0
}

