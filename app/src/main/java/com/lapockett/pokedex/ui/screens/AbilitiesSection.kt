package com.lapockett.pokedex.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lapockett.pokedex.model.LocalPadding
import com.lapockett.pokedex.model.ui.AbilityUI

@Composable
fun AbilityChip(name: String, isHidden: Boolean) {
    AssistChip(
        onClick = {},
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = name.replaceFirstChar { it.uppercase() })
                if (isHidden) {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Hidden",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}

@Composable
fun PokemonAbilitiesSection(abilities: List<AbilityUI>) {
    val paddingValues = LocalPadding.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = paddingValues.normal)
    ) {
        Text(
            text = "Abilities",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(paddingValues.tiny))

        Row(
            horizontalArrangement = Arrangement.spacedBy(paddingValues.tiny)
        ) {
            abilities.forEach { ability ->
                AbilityChip(
                    name = ability.name,
                    isHidden = ability.isHidden
                )
            }
        }
    }
}
