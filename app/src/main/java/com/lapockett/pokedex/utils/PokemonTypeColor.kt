package com.lapockett.pokedex.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.lapockett.pokedex.model.LocalColors

@Composable
fun pokemonTypeToColor(type: String): Color {
    val colors = LocalColors.current

    return when (type.lowercase()) {
        "poison" -> colors.poisonColor
        "grass" -> colors.grassColor
        "fire" -> colors.fireColor
        "flying" -> colors.flyingColor
        "water" -> colors.waterColor
        "electric" -> colors.electricColor
        "ice" -> colors.iceColor
        "fighting" -> colors.fightingColor
        "ground" -> colors.groundColor
        "psychic" -> colors.psychicColor
        "bug" -> colors.bugColor
        "rock" -> colors.rockColor
        "ghost" -> colors.ghostColor
        "dragon" -> colors.dragonColor
        "dark" -> colors.darkColor
        "steel" -> colors.steelColor
        "normal" -> colors.normalColor
        "fairy" -> colors.fairyColor
        else -> Color(0xff818080)
    }
}