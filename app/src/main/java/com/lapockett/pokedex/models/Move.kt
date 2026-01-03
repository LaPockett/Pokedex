package com.lapockett.pokedex.models

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)