package com.lapockett.pokedex.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lapockett.pokedex.model.LocalPadding
import com.lapockett.pokedex.model.ui.StatUI

@Composable
fun StatBar(
    statName: String,
    statValue: Int,
    maxStat: Int = 150,
    barColor: Color
) {
    val paddingValues = LocalPadding.current

    val animatedProgress by animateFloatAsState(
        targetValue = statValue / maxStat.toFloat(),
        label = "stat_anim"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statName,
                modifier = Modifier.width(90.dp),
                fontSize = 13.sp
            )
            Text(
                text = statValue.toString(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp)
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .weight(1f)
                    .padding(start = paddingValues.tiny)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .background(barColor, RoundedCornerShape(4.dp))
                )
            }
        }
        Spacer(Modifier.height(paddingValues.tiny))
    }
}

@Composable
fun PokemonStatsSection(stats: List<StatUI>, typeColor: Color) {
    val paddingValues = LocalPadding.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues.normal)
    ) {
        Text(
            text = "Base Stats",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(paddingValues.small))

        stats.forEach { stat ->
            StatBar(
                statName = stat.name.replace("-", " ").uppercase(),
                statValue = stat.baseStat,
                barColor = typeColor
            )
        }
    }
}
