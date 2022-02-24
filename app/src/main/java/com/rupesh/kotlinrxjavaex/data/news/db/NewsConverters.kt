package com.rupesh.kotlinrxjavaex.data.news.db

import androidx.room.TypeConverter
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSource

class NewsConverters {

    @TypeConverter
    fun fromSource(source: NewsSource): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): NewsSource {
        return NewsSource(name, name)
    }
}