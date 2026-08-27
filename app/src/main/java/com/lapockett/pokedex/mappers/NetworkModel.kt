package com.lapockett.pokedex.mappers

import com.lapockett.pokedex.entitie.PokemonEntity
import com.lapockett.pokedex.model.network.PokemonNetworkModel
import com.lapockett.pokedex.model.ui.AbilityUI
import com.lapockett.pokedex.model.ui.CriesUI
import com.lapockett.pokedex.model.ui.PokemonDetailsUI
import com.lapockett.pokedex.model.ui.PokemonListItemUI
import com.lapockett.pokedex.model.ui.StatUI
import com.lapockett.pokedex.model.ui.TypeUI

fun PokemonEntity.toListItemUI(): PokemonListItemUI {
    return PokemonListItemUI(
        id = id,
        name = name.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        types = types.mapIndexed { index, typeName ->
            TypeUI(slot = index + 1, name = typeName)
        }
    )
}

fun PokemonListItemUI.toEntity(): PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = types.map { it.name }
    )
}

fun PokemonNetworkModel.toListItemUI(): PokemonListItemUI {
    return PokemonListItemUI(
        id = id,
        name = name,
        imageUrl = buildOfficialArtworkUrl(id),
        types = types.map { TypeUI(slot = it.slot, name = it.type.name) }
    )
}

fun PokemonNetworkModel.toDetailsUI(): PokemonDetailsUI {
    return PokemonDetailsUI(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = base_experience,
        imageUrl = buildOfficialArtworkUrl(id),
        types = types.map { TypeUI(slot = it.slot, name = it.type.name) },
        stats = stats.map {
            StatUI(
                name = it.stat.name,
                baseStat = it.base_stat,
                effort = it.effort
            )
        },
        abilities = abilities.map {
            AbilityUI(
                name = it.ability.name,
                isHidden = it.is_hidden,
                slot = it.slot
            )
        },
        cries = CriesUI(
            latest = cries.latest,
            legacy = cries.legacy
        )
    )
}

private fun buildOfficialArtworkUrl(id: Int): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"