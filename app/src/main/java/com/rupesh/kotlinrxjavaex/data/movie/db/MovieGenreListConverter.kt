package com.rupesh.kotlinrxjavaex.data.movie.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MovieGenreListConverter {

    @TypeConverter
    fun fromGenreList(genreIds: List<Int>): String? {
        val gSon = Gson()
        return gSon.toJson(genreIds)
    }

    @TypeConverter
    fun toGenreList(value: String): List<Int> {
        val listType = object: TypeToken<List<Int>>(){}.type
        return Gson().fromJson(value, listType)
    }
}