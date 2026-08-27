package com.lapockett.pokedex
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromTypesList(types: List<String>): String {
        return types.joinToString(separator = ",")
    }

    @TypeConverter
    fun toTypesList(data: String): List<String> {
        return if (data.isBlank()) emptyList() else data.split(",")
    }
}