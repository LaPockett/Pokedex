package com.lapockett.pokedex.model

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Global padding
 */
data class Padding(
    val extraTiny : Dp = 3.dp,
    val tiny : Dp = 8.dp,
    val small: Dp = 12.dp,
    val normal: Dp = 16.dp,
    val big: Dp = 20.dp,
    val large: Dp = 24.dp,
    val extraBig : Dp = 32.dp,
    val extraLarge : Dp = 36.dp
)
val LocalPadding = compositionLocalOf { Padding() }

/**
 * Global colors
 */
data class Colors(
    val poisonColor : Color = Color(0xff9f3f9f),
    val grassColor : Color = Color(0xff77c64f),
    val fireColor: Color = Color(0xffee7f30),
    val flyingColor : Color = Color(0xffa78fee),
    val waterColor : Color = Color(0xff678fee),
    val electricColor : Color = Color(0xfff6ce30),
    val iceColor : Color = Color(0xff97d6d6),
    val fightingColor : Color = Color(0xffbe3028),
    val groundColor : Color = Color(0xffdebe67),
    val psychicColor : Color = Color(0xfff65787),
    val bugColor : Color = Color(0xffa7b720),
    val rockColor : Color = Color(0xffb79f38),
    val ghostColor : Color = Color(0xff6f5797),
    val dragonColor : Color = Color(0xff6f38f6),
    val darkColor : Color = Color(0xFF6F5747),
    val steelColor : Color = Color(0xffb7b7ce),
    val normalColor : Color = Color(0xffa7a777),
    val fairyColor : Color = Color(0xffec98ab),
    val pokemonIdColor : Color = Color(0xda5e5d5d)
)
val LocalColors = compositionLocalOf { Colors() }