// Theme.kt
package com.lapockett.pokedex.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PokemonRedLight,
    onPrimary = Color.White,
    primaryContainer = PokemonRedDark,
    onPrimaryContainer = Color.White,

    secondary = PokemonBlueLight,
    onSecondary = Color.White,
    secondaryContainer = PokemonBlueDark,
    onSecondaryContainer = Color.White,

    tertiary = PokemonYellowLight,
    onTertiary = Color.Black,
    tertiaryContainer = PokemonGold,
    onTertiaryContainer = Color.Black,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,

    outline = DarkOutline,
    outlineVariant = DarkSurfaceVariant,

    error = DarkError,
    onError = Color.Black,
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = DarkError,

    inverseSurface = LightSurface,
    inverseOnSurface = LightOnSurface,
    inversePrimary = PokemonRed,

    scrim = Color.Black,
    surfaceTint = PokemonRedLight
)

private val LightColorScheme = lightColorScheme(
    primary = PokemonRed,
    onPrimary = Color.White,
    primaryContainer = PokemonRedLight,
    onPrimaryContainer = Color.White,

    secondary = PokemonBlue,
    onSecondary = Color.White,
    secondaryContainer = PokemonBlueLight,
    onSecondaryContainer = Color.White,

    tertiary = PokemonGold,
    onTertiary = Color.Black,
    tertiaryContainer = PokemonYellow,
    onTertiaryContainer = Color.Black,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,

    outline = LightOutline,
    outlineVariant = LightSurfaceVariant,

    error = LightError,
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),

    inverseSurface = DarkSurface,
    inverseOnSurface = DarkOnSurface,
    inversePrimary = PokemonRedLight,

    scrim = Color.Black,
    surfaceTint = PokemonRed
)

@Composable
fun PokedexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivado para usar nuestra paleta Pokémon
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
